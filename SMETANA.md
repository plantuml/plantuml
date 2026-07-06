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

> **Correction (July 2026, later session):** the above paragraph is wrong and is kept
> only so the mistake isn't repeated. Having actually read `lib/dotgen/fastgr.c`,
> `virtual_node()` in the **real C** does *not* call `agnode()` either — it builds the
> node by hand (`NEW(node_t)`, i.e. zeroed memory) and only ever sets `AGTYPE(n)`,
> never `tag.id`. So `id == 0` for virtual/aux nodes (`ln`, `rn`, cluster skeleton
> rank-leaders, `sn` nodes, etc.) is the **correct, faithful, intentional** behavior in
> both C and this Java port — not a translation gap. It's harmless in C because all the
> internal cluster/rank/position code identifies these nodes by pointer, never by
> `tag.id`; the id-based `agnameof()` crash is a real (and correctly diagnosed) issue,
> but "fixing" `virtual_node()` to assign proper ids would be a *deviation* from
> upstream Graphviz, not a fix. `SmetanaDebug.safeName()` remains the right way to
> reference such nodes in debug traces; there is nothing further to "properly fix" here.

---

## Case study: `zdev.Test_1` crash — `shortest__c.triangulate()` (July 2026)

### Symptom

`Test_1` (a class diagram, `!pragma layout smetana`) crashed with:

```
java.lang.IllegalStateException: libpath/%s:%d: %sgraphviz-2.38.0\lib\pathplan\shortest.c26triangulation failed
	at gen.lib.pathplan.shortest__c.triangulate(shortest__c.java:311)
	at gen.lib.pathplan.shortest__c.triangulate(shortest__c.java:307)
	at gen.lib.pathplan.shortest__c.Pshortestpath(shortest__c.java:150)
	at gen.lib.common.routespl__c._routesplines(routespl__c.java:387)
	...
```

### Root cause: a real `longjmp` treated as an ordinary crash

In the original C (`lib/pathplan/shortest.c`), `triangulate()` ends its "no diagonal
found" branch with:

```c
fprintf(stderr, "libpath/%s:%d: %s\n", __FILE__, __LINE__, "triangulation failed");
longjmp(jbuf, 1);
```

Unlike the `init_rank` case above, this **is** a genuine abort: `jbuf` is set up via
`setjmp(jbuf)` at the very top of `Pshortestpath()`, and `longjmp` unwinds straight back
there, making `Pshortestpath` return `-2` (its documented "memory/allocation problem"
failure code — reused here for "triangulation gave up"). Callers of `Pshortestpath`
(routespline generation) are written to tolerate a `-2` return.

In the Java port, this had been translated as a direct `throw new
IllegalStateException(...)`, which is **not** caught anywhere — it propagates all the
way up and crashes the whole render, instead of being caught at the `Pshortestpath`
level the way `longjmp` would catch it in C.

**Why this differs from the `init_rank` fix:** that case was a non-fatal C diagnostic
wrongly translated as fatal. This case is a genuinely fatal-to-the-computation C path
(real `longjmp`) that was translated as fatal-to-the-JVM (uncaught exception) instead of
fatal-to-the-*method-call* the way `setjmp`/`longjmp` scopes it in C.

### Fix

`gen/lib/pathplan/shortest__c.java`:
- Added a private `PathplanAbort extends RuntimeException` nested class right next to
  the existing `jbuf` field, documented as the Java stand-in for "a `longjmp(jbuf, 1)`
  back to this file's `setjmp` point".
- `triangulate()`'s "triangulation failed" branch now logs to `System.err` (matching the
  style of the neighboring `System.err.println` diagnostics already in this file) and
  throws `PathplanAbort` instead of `IllegalStateException`.
- `Pshortestpath()`'s dead `if (setjmp(jbuf)!=0) return -2;` (dead because this port's
  `setjmp()` always returns `0` — there is no real stack-jump mechanism) was replaced by
  a `try { ... } catch (PathplanAbort abort) { return -2; }` wrapped around the method
  body, which is the faithful equivalent: whatever would have `longjmp`'d back to
  `setjmp` in C now gets caught here and produces the same `-2` return.

**Note:** the other `longjmp(jbuf,1)` call sites in this same file (inside `growpnls`,
`growtris`, `growdq`, `growops`, guarding malloc/realloc failures) were deliberately
*left* as `UNSUPPORTED(...)`. They're unreachable in practice — this Java port's
malloc/realloc equivalents never return `null` — so there was no observed bug there to
fix, consistent with lesson #1 below (don't touch what isn't actually broken).

### General lesson refinement

This case sharpens lesson #1 below: an `UNSUPPORTED`/uncaught-exception translation of an
`agerr(...)` block can be wrong in *either direction* — either because the C original
wasn't actually fatal (the `init_rank` case), or because it *was* fatal but scoped to a
`setjmp`/`longjmp` pair that the Java translation didn't reconstruct (this case). Before
"fixing" such a block, find both the `agerr`/`fprintf` call **and** whatever `longjmp`
follows it, and check whether the enclosing function actually has a matching `setjmp` —
if so, the fix needs a real (exception-based) jump target in Java, not just removing the
throw.

---

## Case study: `zdev.Test_1` crash (2) — `SmetanaEdge.getDotPathInternal()` NPE (July 2026)

### Symptom

After fixing the `triangulate()`/`longjmp` issue above, `Test_1` progressed further but
then crashed with:

```
java.lang.NullPointerException: Cannot read field "list" because "splines" is null
	at net.sourceforge.plantuml.sdot.SmetanaEdge.getDotPathInternal(SmetanaEdge.java:356)
	at net.sourceforge.plantuml.sdot.SmetanaEdge.drawU(SmetanaEdge.java:124)
	...
```

### Root cause: a downstream consumer assumed routing always succeeds

This is not a new bug in Smetana itself — it's the *expected* consequence of the
previous fix working correctly. When `Pshortestpath`/`Proutespline` bail out on a
genuinely degenerate case (same family as the `triangulate()` abort above), the C code
in `dotsplines.c` (`make_regular_edge`, `_routesplines`, etc.) does exactly what its Java
port does: `if (pn == 0) return;`, silently leaving that one edge's `ED_spl` unset
(`null`) and moving on to the next edge, rather than crashing the whole layout.

The PlantUML-side renderer, `net.sourceforge.plantuml.sdot.SmetanaEdge`, wasn't written
with this possibility in mind: `getDotPathInternal()` unconditionally dereferenced
`data.spl.list`, assuming every edge always gets a fully-routed spline. Once the
`triangulate()` fix above started letting `Pshortestpath` return `-2` gracefully instead
of crashing the JVM, this previously-unreachable-in-practice null-spline case became
reachable, and promptly NPE'd one layer up.

### Fix

`net/sourceforge/plantuml/sdot/SmetanaEdge.java`, `getDotPathInternal()`: if
`data.spl` is `null`, don't dereference it — fall back to a straight-line `DotPath`
between the tail and head node centers (`AGTAIL`/`AGHEAD` + `ND_coord`), built the same
way a degenerate 2-point spline would render. New helper: `getFallbackStraightDotPath()`.
This mirrors the "a straight line is better than failing" fallback already used inside
`Pshortestpath` itself, just one layer further out, at the point where PlantUML actually
consumes the (possibly-missing) routing result.

### General lesson

Fixing a crash that turns a hard JVM abort into a graceful "give up on this one edge"
outcome (matching upstream Graphviz's own error handling) can surface previously-dead
code paths further downstream that were never written to expect a partial/degenerate
result. After any such fix, re-run the test: a *different*, more localized crash further
down the pipeline is a good sign, not a regression — it means the fix is doing its job
and exposing the next place that needs the same "degrade gracefully" treatment.

---

## Debugging session in progress: `zdev.Test_1` cluster layout overlaps (July 2026)

With the two crash fixes above in place, `Test_1` (`!pragma layout smetana`, nested
packages `entities` / `entities.mindmap`) now renders an image, but nodes and package
boxes heavily overlap and edge labels collide — compare against `zdev.Test_2`, the same
diagram without the pragma (native dot layout), which renders correctly. So this is a
layout-quality bug specific to Smetana's cluster/rank/position handling, not a crash.

To investigate, `dumpLayoutState`/`dumpClusters` were added to `gen/lib/dotgen/dotinit__c.java`
and wired into `dotLayout()`'s main loop, right after each of `dot_rank`, `dot_mincross`,
and `dot_position`. They write to `smetana.txt` (via `SmetanaDebug.TRACE`) a dump of every
node's rank/order/coord and enclosing cluster, plus every cluster's bounding box
(recursively). This is meant to make it possible to compare, phase by phase, where the
cluster nesting/positioning starts to diverge from a sane layout — likely somewhere in
`cluster__c.java` (`merge_ranks`, `expand_cluster`, `mark_clusters`) or `position__c.java`'s
cluster-containment code (`pos_clusters`, `contain_subclust`, `separate_subclust`).

Not yet root-caused as of this note. Left in place for the next session to pick up;
harmless to leave permanently (TRACE/safeName never throw).

**Update:** the trace (see `smetana.txt`) shows cluster14 (`entities.mindmap`) member
nodes with x-coordinates (e.g. -503, -353, -205) far outside both cluster14's own
computed bbox (`[21,99]`) *and* the parent cluster6's bbox (`[-307,107]`) — direct,
numeric evidence that the cluster-containment aux edges (`contain_clustnodes` /
`contain_subclust` / `separate_subclust` in `position__c.java`) are not actually
constraining these nodes during the X network-simplex pass. The initial hypothesis —
that this was caused by `fastgr__c.virtual_node()`'s `id == 0` bug (id-based lookup
collisions between different clusters' `ln`/`rn`/aux nodes) — was checked against the
real C source (`lib/dotgen/fastgr.c`) and is **wrong**: `virtual_node()` in C never sets
`tag.id` either (it bypasses `agnode()` entirely), so `id == 0` for virtual/aux nodes is
faithful, intentional upstream behavior, not a porting gap. See the correction note in
the `virtual_node()` id=0 discussion above. The real cause of the containment failure is
still open; next step is to trace through `contain_clustnodes`/`pos_clusters` directly
(e.g. confirm the aux edges they create via `make_aux_edge` actually reach `rank()`'s
network simplex with correct endpoints/minlen) rather than assuming an id-identity issue.

**Second update, after adding `dumpAuxEdges`/rank-array traces:** a real anomaly was
found and chased for a while — `GD_minrank(root)` becomes `-1` partway through
`dot_position`, and `GD_rank(root)[-1]` unexpectedly holds 7 virtual nodes. This looked
like memory corruption in `CArray`'s offset handling. **It is not a bug.** Bisecting with
more traces localized it to `flat_edges()` (`flat__c.java`) calling `abomination(g)`,
which does `GD_minrank(g, GD_minrank(g) - 1)` — this is a real, faithfully-translated
upstream mechanism (the function is even called `abomination` in the original C!): when
a labeled flat/same-rank edge exists on rank 0, dot inserts an extra rank `-1` purely to
hold that edge's label virtual node. `CArray.REALLOC__`/`.plus_()` correctly support the
resulting negative-offset indexing. This diagram has several labeled same-rank edges
(`card`, `owner`, `category`, `leftNode`, `rightNode`, etc.), hence the 7 nodes at rank
`-1`. **Lesson:** don't assume a surprising-looking value is corruption before checking
whether the C original has a deliberate mechanism that produces it — grep the C source
for the field name (here, `GD_minrank`) before hypothesizing a memory/aliasing bug.

With that false lead cleared, `contain_nodes`/`contain_subclust`/`keepout_othernodes`
were re-examined correctly: `GD_rank(cluster6)[0].n == 8` (cluster6's own 4 nodes *plus*
nested cluster14's 4 nodes) is correct, not a bug — a cluster's local rank-0 slice
legitimately spans its nested sub-cluster's nodes too. Under that reading, every
containment edge produced for this diagram (`ln(cluster6)→sh0013`, `sh0018→rn(cluster6)`,
`ln(cluster14)→sh0021`, `sh0018→rn(cluster14)`, `ln(cluster6)→ln(cluster14)`,
`rn(cluster14)→rn(cluster6)`) is consistent and correctly nests cluster14 inside
cluster6. So `position__c.java`'s constraint construction is exonerated.

**Current best hypothesis, not yet confirmed:** since the constraint graph fed to the
X-position solver is well-formed, but the *solved* x-coordinates violate it (cluster14's
members end up outside cluster6's own bbox), the bug most likely lives in the network
simplex solver itself, `gen/lib/common/ns__c.java` (the `rank(zz, g, 2, nsiter2(...))`
call) — the same file already flagged as fragile in the `Test_0`/`init_rank` case study
above. Not yet investigated in this session; a good starting point next time would be to
dump the network simplex's internal tree/cut-value state, or to compare `ns__c.java`
against `lib/common/ns.c` function by function the way `position__c.java` was just
compared against `position.c`.

### Root cause CONFIRMED: a genuine cycle in the X-position auxiliary graph, not a Smetana bug

`ns__c.java` was compared function-by-function against `lib/common/ns.c`
(`feasible_tree`, `tight_tree`, `treesearch`, `add_tree_edge`, `incident`, `leave_edge`,
`enter_edge`, `dfs_enter_outedge/inedge`, `update`, `treeupdate`, `rerank`, `LR_balance`,
`x_val`, `x_cutval`, `dfs_range`, `dfs_cutval`, `scan_and_normalize`) and found to be a
faithful, correct translation — no bug there.

Instrumentation added directly in `rank2()`/`init_rank()` (see "Debugging tools" below)
proved conclusively that:

1. `rank(zz, g, 2, nsiter2(zz,g))` for the X-position pass on this diagram has
   `N_nodes=35`, and `init_graph()` finds the graph **not already feasible**, so
   `init_rank()` (Kahn's-algorithm topological sort) runs to seed initial ranks.
2. `init_rank()` only manages to rank **18 of the 35 nodes** (`ctr=18`) — the classic
   signature of a **genuine cycle** in the graph (Kahn's algorithm always stalls exactly
   on nodes reachable only through a cycle). The 17 stuck nodes keep whatever stale
   `ND_rank` they had from earlier (unrelated) computations, e.g. real class node
   `sh0018` is left at `1260` — nonsense as an X-coordinate seed.
3. `feasible_tree()` then builds a tree on top of this already-inconsistent baseline and
   reports success (`return 0`), but the result provably violates the graph's own
   `ED_minlen` constraints — e.g. 9 of 66 edges have negative slack immediately after
   `feasible_tree()` returns, including violating the *most basic* left-right ordering
   constraint (`sh0010 -> sh0021 minlen=253` ends up with `slack=-606`).
4. Reconstructing the actual `ND_in` edge lists (dumped via `dumpInTails`) and manually
   re-running Kahn's algorithm by hand reproduced `ctr=18` exactly, and exposed the
   literal cycle:

   ```
   <unnamed sn-node A> --(minlen=62)--> <unnamed sn-node B>
   <unnamed sn-node B> --(minlen=89)--> CString:sh0011
   CString:sh0011      --(minlen=243)-> CString:sh0010
   CString:sh0010      --(minlen=253)-> CString:sh0021
   CString:sh0021      --(minlen=102)-> <unnamed sn-node A>   (closes the cycle)
   ```

   All five `minlen`s are positive, so this cycle is **mathematically infeasible** — no
   ranking algorithm, however correct, could satisfy it. This is not a Smetana port bug;
   it's an actual malformed (cyclic) input graph.

**Where the cycle comes from:** nodes A and B (and the parallel chain
`1752535057→2116299597→50720817→1496220730→488600086` visible in the trace) each have
exactly two outgoing aux edges — the signature of an `sn` node created by
`make_edge_pairs()` in `position__c.java` to encode a same-rank **port-ordering**
constraint (`ED_head_port`/`ED_tail_port`) for one specific original edge. The cycle means
that the port-based ordering constraints for whichever edges these `sn` nodes represent
(most likely the diagram's `MindMapLink --> MindMapNode : leftNode`/`rightNode` edges, or
the `card`/`owner`/`category` association labels — all edges with explicit port-like
label placement between nodes that are far apart in the row) are **inconsistent with the
left-to-right node order that `mincross` already committed to**. In other words: mincross
picked an order, then `make_edge_pairs`'s per-edge port constraints implicitly demand a
different, conflicting order for the same nodes — and nothing catches or prevents that
conflict before it's handed to network simplex as a cycle.

**Not yet fixed.** This is a deeper design-level issue than the earlier crash fixes: it's
not obviously a one-line translation bug, since `make_edge_pairs`/`position__c.java` was
already verified faithful to `position.c`. Two directions worth trying next:
- Confirm whether **native Graphviz `dot`** (no `!pragma layout smetana`) hits the same
  kind of conflict internally for this exact diagram and just happens to break the cycle
  or avoid it some other way (e.g. maybe real dot's `mincross`/`make_edge_pairs` never
  produces port constraints this contradictory in practice, and something upstream of
  `create_aux_edges` — flat-edge handling, port validation, adjacency checks in
  `checkFlatAdjacent`/`flat_edges` — is where the real difference lies).
- Identify exactly which *original* PlantUML-level edges correspond to sn-nodes A, B, and
  the parallel chain (their `ED_head_port`/`ED_tail_port` `.p.x` values, traceable via
  `ND_save_out`/`ED_to_orig` back to the real edge), to see concretely which two
  relationships in the `.puml` source are making contradictory port demands.

---

## Debugging tools added to `ns__c.java` / `position__c.java` / `dotinit__c.java` (July 2026)

During the `Test_1` cluster-layout investigation, several ad-hoc `TRACE(...)` calls (all
using `SmetanaDebug.TRACE`, writing to `smetana.txt`) were added and left in place. They
are silent by default (only visible if `smetana.txt` is inspected) and safe to keep
permanently; a future session can freely add more in the same style, or remove these once
the cluster/cycle investigation concludes.

- **`dotinit__c.dumpLayoutState`/`dumpClusters`** (called from `dotLayout()` after each of
  `dot_rank`, `dot_mincross`, `dot_position`): dumps every node's rank/order/coord and
  cluster, plus every cluster's bbox and local rank-array contents (guarded against
  `GD_rank(clust) == null`, since a cluster's rank array isn't allocated until
  `dot_mincross`).
- **`position__c.dumpAuxEdges`** (called in `dot_position` right before the X-position
  `rank()` call): dumps the raw `GD_rank(g)[r].v[]` array contents per rank, and every
  edge of the X-position auxiliary graph, with cluster boundary nodes (`GD_ln`/`GD_rn`,
  recursively over all clusters) resolved to readable labels (`ln(cluster6)`, etc.) via
  `collectClusterBoundaryLabels`, since these are virtual nodes with `tag.id==0` and
  `safeName()` alone can't tell them apart.
- Several one-line `TRACE("... minrank=" + GD_minrank(g) + ...)` checkpoints through
  `dot_position()` and `create_aux_edges()`, and a `TRACE("... rank(g,2,...) returned "
  + rankResult ...)` after the X-position `rank()` call.
- **`ns__c.dumpNegativeSlackEdges(zz, phase)`**: walks every edge in the current ns graph
  (`zz.G_ns`) and logs any with `SLACK(e) < 0`, plus a summary count. Called right after
  `feasible_tree()` returns in `rank2()`. This is the key diagnostic that proved the
  "feasible" tree wasn't actually feasible.
- **`ns__c.dumpInTails(zz, v)`**: returns a string listing a node's real `ND_in` edges
  (tail name + minlen), used inside `init_rank()` to dump every node's actual in-edges
  and priority at entry, and every still-unranked node's leftover priority/stale rank at
  exit. This is what let the exact cycle be reconstructed by hand.
- Full per-iteration tracing of `rank2()`'s main `leave_edge`/`enter_edge`/`update` loop
  (tail/head/rank/cutvalue/slack of both the leaving and entering edge, before and after
  `update()`).

**If continuing this investigation:** all of the above is already in place and working;
run `zdev.Test_1`, read `smetana.txt` from the top for `ns.init_rank:` lines to see the
cycle-defining `ND_in` lists directly, rather than re-instrumenting from scratch.

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

## Debugging tools added to trace original PlantUML `Link` identity (July 2026)

To push the `Test_1` cluster-layout investigation further (identifying exactly
which `.puml`-level edges the two colliding flat-edge labels A/B correspond to
- see "Root cause CONFIRMED" above), two more `TRACE(...)` calls were added.
Unlike the earlier ones, these live outside `gen.lib.*` entirely, on the
PlantUML side, because that's the only place the real entity names/labels are
still available (by the time an edge reaches `flat_node`/`position__c`, all
that's left is an opaque `ST_Agedge_s`).

- **`CucaDiagramFileMakerSmetana.createEdge()`**: right before returning the
  newly-created `ST_Agedge_s e`, logs `link.getEntity1()/getEntity2().getName()`,
  `link.getLabel()`, `getRole1()/getRole2()`, `getQuantifier1()/getQuantifier2()`,
  and `System.identityHashCode(e)`.
- **`flat__c.flat_node(ST_Agedge_s e)`**: right after `ND_alg(vn, e)`, logs
  `System.identityHashCode(vn)` (the label virtual node - this is the same
  identity hash `SmetanaDebug.safeName()`'s `<unnamed:NNN>` fallback prints,
  i.e. what shows up for A/B in `dumpAuxEdges`/`ns__c` traces) alongside
  `System.identityHashCode(e)`.

**How to use these together:** find the `flat_node: labelNodeIdentityHash=<A's
number>` line to get `origEdgeIdentityHash=NNN`, then find the `createEdge:
... edgeIdentityHash=NNN` line (same NNN) to read off the real entity names,
label, roles and quantifiers of the original `.puml` edge that A represents.
Repeat for B. This should tell us concretely which two relationships in
`entities`/`entities.mindmap` are the ones whose independent label ordering
(`flat_limits`) conflicts with the real nodes' left-right order.

Note: `flat_node(ST_Agedge_s e)`'s signature deliberately matches
`lib/dotgen/flat.c`'s `flat_node(edge_t *e)` exactly (no `Globals zz`
parameter), so unlike the `gen.lib` traces this one can't call
`agnameof()`/`safeName()` to print tail/head node names directly - identity-hash
cross-referencing against the `createEdge` trace is the way around that.

**Not yet run.** Next step: run `zdev.Test_1` again, and grep the new
`smetana.txt` for `flat_node:` and `createEdge:` lines, cross-referencing by
`identityHash` as described above.

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

6. **A "successful" return code doesn't mean the result is correct — check invariants
   directly.** `ns__c.rank2()`/`feasible_tree()` can return `0` (success) while silently
   producing a ranking that violates its own `ED_minlen` constraints, if the *input* to
   the algorithm was already broken (e.g. a cyclic constraint graph feeding `init_rank`).
   When a downstream result looks wrong but every function along the way matches its C
   original line-for-line, stop trusting return codes and directly verify the
   mathematical invariant the algorithm is supposed to maintain (here: recompute
   `SLACK(e)` for every edge and look for negative values) rather than continuing to
   read code.

7. **`ctr != N_nodes` in `init_rank` (Kahn's algorithm) always means a genuine cycle** in
   whatever graph was passed to `rank2()` — not a translation bug in `init_rank` itself
   (that specific `UNSUPPORTED`→non-fatal-warning translation bug was already fixed in
   the `Test_0` case study above; don't re-diagnose it). When this fires, the next step
   is reconstructing the actual cycle from the stuck nodes' real `ND_in` edges (dumped
   via a helper like `dumpInTails`), not assuming the ranking algorithm itself is at
   fault.

8. **Multi-turn debugging sessions accumulate stale hypotheses — read them critically.**
   Several leads in the `Test_1` case study above were investigated, written down, and
   later proven wrong by the *next* piece of evidence (the `virtual_node` id=0 "bug",
   the `GD_minrank=-1` "corruption", the network-simplex-itself-is-buggy hypothesis).
   Each wrong turn was still useful — it ruled something out — but a future session
   should treat every hypothesis in this file as provisional until its "confirmed" or
   "wrong" follow-up note, and should keep applying the same discipline (verify against
   the real C source and against directly-dumped runtime state, not against what "should"
   be true) rather than trusting an earlier session's conclusion at face value.
