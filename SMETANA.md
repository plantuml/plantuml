# Smetana Debugging Notes

This file collects findings from debugging sessions on Smetana (the C→Java port of
Graphviz/dot used by PlantUML, see `src/main/java/smetana/core/readme.md`). It is meant
to be read before starting a new debugging session, and updated after each one.

Related packages:
- `smetana.core` — runtime support (macros, memory, debug helpers)
- `gen.lib.*` — the actual translated Graphviz sources (one Java class per `.c` file)
- `h.*` — translated C structs

---

## Case study: `zdev.Test_0` crash (July 2026)

### Symptom

`Test_0` (a small state diagram with `!pragma layout smetana`) crashed with:

```
java.lang.UnsupportedOperationException: <random-key>
	at smetana.core.Macro.UNSUPPORTED(Macro.java)
	at gen.lib.common.ns__c.init_rank(ns__c.java)
	at gen.lib.common.ns__c.rank2(ns__c.java)
	at gen.lib.common.ns__c.rank(ns__c.java)
	at gen.lib.dotgen.position__c.dot_position(position__c.java:649)
	at gen.lib.dotgen.dotinit__c.dotLayout(dotinit__c.java)
	...
```

Reordering unrelated lines in the diagram source changed whether the crash happened at
all — a strong hint that something order-sensitive (cycle-breaking, tree search, hashing)
was involved, which initially pointed suspicion at the DFS-based cycle-breaking algorithm
in `acyclic__c.java`.

### Root cause: a translation bug, not an algorithmic one

`ns__c.init_rank()` is a straightforward Kahn's-algorithm topological sort (BFS with a
priority queue keyed by in-degree), used to seed initial ranks when the graph isn't
already "feasible". In the **original C** (`lib/common/ns.c`):

```c
if (ctr != N_nodes) {
    agerr(AGERR, "trouble in init_rank\n");
    for (v = GD_nlist(G); v; v = ND_next(v))
        if (ND_priority(v))
            agerr(AGPREV, "\t%s %d\n", agnameof(v), ND_priority(v));
}
```

This is **purely a diagnostic warning printed to stderr**. Execution continues normally
afterward — `init_rank()` just returns. Note the contrast with other `agerr(...)` calls
in the *same file* (e.g. in `add_tree_edge`, `update`), which are followed by
`longjmp(jbuf, 1)` and really do abort.

In the Java port, all four lines of this block had been translated uniformly as
`Macro.UNSUPPORTED(...)`, which **throws `UnsupportedOperationException`**. Because this
block doesn't call `longjmp`, it should never have been marked `UNSUPPORTED` — it's a
correctly-portable no-op-ish diagnostic, not an unported C construct.

**Fix** (`gen/lib/common/ns__c.java`, `init_rank`): replaced the `UNSUPPORTED(...)` calls
with a single `LOG("trouble in init_rank")` (a no-op by default, same convention as
`Macro.MAKEFWDEDGE`'s `SmetanaDebug.LOG(...)` call), and let the method return normally.

### Why the condition triggers at all: two separate network-simplex passes

`ctr != N_nodes` means the graph being ranked has a genuine cycle in it (some nodes never
reach priority 0). Investigating *why* led to an important architectural point:

**Graphviz's `dot` algorithm invokes network simplex (`ns.c` / `ns__c.java`) twice, on two
different graphs:**

1. **Rank assignment** (`rank.c` / `rank__c.java`, via `dot1_rank` → `class1_` →
   `decompose` → `acyclic_` → `rank1` → `rank`/`rank2`). This ranks the *real* diagram
   nodes into Y-levels. For `Test_0` this graph has 11 real nodes across 2 weakly
   connected components, and turned out to be already acyclic (zero back-edges found by
   `acyclic_`'s DFS).
2. **Position assignment** (`position.c` / `position__c.java`, via `dot_position`). This
   builds a **synthetic auxiliary "balance" graph** (using `make_aux_edge` and friends) to
   compute X-coordinates, encoding the left-to-right order decided by `mincross` plus
   "straightening" edges (one auxiliary node per real edge) so that network simplex
   prefers straight edges. This auxiliary graph is structurally unrelated to the rank.c
   graph and can be much larger (23 nodes for `Test_0`'s 11 real nodes) — its `minlen`
   values are physical point distances (tens to low hundreds), not small integer ranks.

The crash's stack trace (`position__c.dot_position` → `ns__c.rank` → `rank2` →
`init_rank`) confirmed the second call was the one hitting `ctr != N_nodes`, **not** the
first. So the DFS/cycle-breaking code in `acyclic__c.java` — the initial suspect — was
entirely innocent.

### Confirming it wasn't a bug at all

After fixing the `UNSUPPORTED` → `LOG` translation bug, `Test_0` rendered an image instead
of crashing — but a visually broken one (overlapping nodes/labels). Generating the same
diagram with **real, native Graphviz `dot`** produced **the exact same broken layout**.

Conclusion: the residual cycle in the position-assignment auxiliary graph is a genuine,
pre-existing edge case of Graphviz's own layout heuristics for this particular diagram —
not a Smetana porting bug. The *only* real bug was the non-fatal-C-warning-turned-fatal-
Java-exception in `init_rank`. Once fixed, Smetana's behavior matches native Graphviz
exactly (same non-fatal warning path, same degraded-but-non-crashing layout).

**Practical takeaway:** this crash was unrelated to issue #2735 (concurrent/orthogonal
states with `--` not rendering under `!pragma layout smetana`), despite both touching
Smetana's layout internals.

### A second, separate latent bug found (not yet fixed)

While instrumenting `init_rank` to print node names via `agnameof()`, a **different**
pre-existing bug surfaced:

```
java.lang.UnsupportedOperationException
	at smetana.core.Memory.fromIdentityHashCode(Memory.java:58)
	at gen.lib.cgraph.id__c.idprint(id__c.java:195)
	at gen.lib.cgraph.id__c.agnameof(id__c.java:306)
```

`fastgr__c.virtual_node(ST_Agraph_s g)` creates virtual nodes directly:

```java
n = new ST_Agnode_s();
AGTYPE(n, AGNODE);
n.base.data = new ST_Agnodeinfo_t();
...
fast_node(g, n);
```

This bypasses `agnode()`/`idmap()` entirely, so `n.tag.id` is never set and defaults to
Java's `0`. In the real C, virtual nodes go through `agnode(g, NULL, 1)`, which assigns a
proper **odd** anonymous id via a counter (`Globals.ctr`, correctly initialized to `1` in
the Java port). Because `0` is even, `agnameof()` → `idprint()` wrongly takes the "named
object" branch and calls `Memory.fromIdentityHashCode(zz, 0)`, which finds nothing in
`zz.all` and throws.

**Why this stayed hidden:** `agnameof()` is normally only called from diagnostic/error
paths (exactly the kind of code that tends to be `UNSUPPORTED`-stubbed elsewhere), so
production code paths essentially never call it on a virtual node. It only surfaced
because debug instrumentation called it directly.

**Not fixed yet** — flagged here for a future session. A proper fix would make
`virtual_node()` go through the normal id-assignment path (or otherwise assign a
consistent odd anonymous id), matching upstream `agnode(g, NULL, 1)` semantics.

---

## Debugging tools added to `SmetanaDebug.java`

Two small helpers were added for future debugging sessions and are meant to stay:

### `TRACE(String)`

```java
SmetanaDebug.TRACE("some message");
```

Writes a line to `smetana.txt` (created fresh — truncated — on the first call per JVM
run, then appended to, with a `flush()` after every line).

**Why a file instead of `System.out`/`System.err`:** in this debugging session, neither
`System.out.println` nor `System.err.println` reliably showed up in the console when
running `Test_0` via the Eclipse JUnit 6 runner (`org.eclipse.jdt.internal.junit6.runner`)
— even though this same runner captures and displays stdout/stderr in other contexts. The
theory floated (embedded NUL characters in some traced string breaking the console
sink) was plausible but not confirmed. Writing straight to disk sidesteps the issue
entirely and also survives crashes (flush-per-line), which is convenient. **If a future
session still can't see `System.err` output, try `TRACE()` to a file first before
spending time on the console issue itself.**

### `safeName(Globals zz, Object obj)`

```java
SmetanaDebug.safeName(zz, someNodeOrEdge)
```

A safe wrapper around `agnameof()` for use in trace/debug strings. Catches any exception
(in particular the `virtual_node()` id-0 bug described above) and falls back to
`<unnamed:IDENTITY_HASH (ExceptionClassName)>` instead of crashing the whole test. Always
prefer this over calling `agnameof()` directly in ad-hoc debug code, since debug
instrumentation should never be able to introduce a *new* crash on top of the one being
investigated.

---

## General lessons for future Smetana debugging

1. **Check whether an `UNSUPPORTED(...)` call is really unported, or just a translation
   artifact.** Search the corresponding line in the real Graphviz C source (paths and
   line references are usually in the `@Original(...)` annotation right above the
   method). If the `agerr(...)` in C isn't immediately followed by `longjmp(jbuf, 1)` (or
   similar abort), it's a non-fatal diagnostic and should not throw in Java.

2. **`dot`'s network simplex (`ns.c`) runs at least twice on different graphs** — once
   for rank/Y assignment (`rank.c`), once for position/X assignment (`position.c`, via an
   auxiliary "balance" graph built with `make_aux_edge`). When debugging anything inside
   `ns__c.java`, check the call stack to know which of the two graphs (and thus which part
   of the pipeline) is actually involved — `N_nodes` and the `minlen` magnitude are good
   tells: rank.c graphs tend to have small node counts and `minlen` of 1–2; the position.c
   auxiliary graph tends to be bigger and has `minlen` values in the tens/hundreds
   (physical point distances).

3. **Before assuming a Smetana-specific bug, compare against native Graphviz `dot`** on
   the same `.dot`/generated source. If native Graphviz produces the identical (even if
   ugly) output, the issue is an upstream Graphviz behavior/heuristic edge case, not a
   porting bug — don't spend time "fixing" it.

4. **Virtual/synthetic nodes and edges created via `fastgr__c.virtual_node()` /
   `new_virtual_edge()` do not have real `tag.id` values set the way real graph nodes do.**
   Avoid calling `agnameof()` (or anything else that depends on the id-lookup machinery)
   on them without going through `SmetanaDebug.safeName()`.

5. **Order-dependent bugs (diagram line order changes behavior) are not automatically
   evidence of a *porting* bug.** Cycle-breaking (`acyclic_`) and heuristics like
   `mincross` are inherently order-sensitive in upstream Graphviz too. Confirm against
   native `dot` before concluding it's a translation issue.
