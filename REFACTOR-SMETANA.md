# Refactoring Smetana: from transliterated C to idiomatic Java

This document is a plan and a catalog of refactoring tracks to progressively transform
Smetana (the C→Java port of Graphviz/dot, see `SMETANA.md` and
`src/main/java/smetana/core/readme.md`) from a mechanical transliteration into idiomatic,
object-oriented Java using standard library types.

**Golden rule: no big-bang.** Every step is a behavior-preserving refactoring, validated
against golden outputs, committed independently. The code must compile and all tests must
pass after *every* step. Many tracks below are deliberately designed as
"encapsulate first, swap internals later" (strangler-fig pattern) so that the risky part
of each change is as small and as mechanical as possible.

---

## 1. Current state: inventory of C-emulation constructs

Three layers today:

| Layer | Location | Contents |
|---|---|---|
| Runtime / C emulation | `smetana.core` (~20 classes) | `CString`, `CArray`, `CArrayOfStar`, `CStarStar`, `__ptr__`, `__struct__`, `CFunction`, `FieldOffset`, `Globals`, `Macro`, `Memory`, `JUtils`, `jmp_buf`, `ZType`, `size_t` |
| Translated C structs | `h.*` (~100 `ST_*` classes) | One Java class per C struct (`ST_Agnode_s`, `ST_elist`, …), plus `EN_*` enums |
| Translated C code | `gen.lib.*` (~70 classes) | One Java class per `.c` file (`ns__c`, `mincross__c`, …), all-static methods, `@Original` traceability annotations |

The C idioms currently emulated, and where:

1. **C strings with pointer arithmetic** — `CString`: `StringBuilder` + `currentStart`
   offset, `\0` terminator, `strcmp`/`strchr`/`strdup`, in-place `setCharAt`.
2. **`malloc`/`realloc` arrays with pointer arithmetic** — `CArray<O>` (arrays of
   structs, pre-instantiated via `ZType.create()`) and `CArrayOfStar<O>` (arrays of
   pointers), both `Object[]` + `offset`, with `plus_()`, `minus_()`, `REALLOC` growth.
3. **Null-terminated edge/node lists** — `ST_elist` / `ST_nlist_t`: `{list, size}` pairs,
   grown by `Macro.elist_append` (`REALLOC(size+2)`, trailing `null` sentinel), iterated
   with `for (i = 0; (e = L.list.get_(i)) != null; i++)`.
4. **C macros as static methods** — `Macro`: ~200 `GD_*`/`ND_*`/`ED_*` accessors that all
   do `((ST_Agnodeinfo_t) n.data).field`, plus geometry helpers, plus ~150 int constants
   that should be enums or typed flags.
5. **The Graphviz `Agrec` mechanism** — `obj.data` is an untyped `ST_Agrec_s` cast at
   every access. In the port there is exactly *one* record type per object kind
   (`ST_Agnodeinfo_t`, `ST_Agedgeinfo_t`, `ST_Agraphinfo_t`), so the whole dynamic-record
   machinery is dead weight.
6. **Function pointers** — `CFunction { Object exe(Globals zz, Object... args); }` used
   for dictionary disciplines (`comparf`, `freef`, `memoryf`), shape vtables
   (`ST_shape_functions`), arrow generators, spline callbacks. Zero type safety.
7. **The cdt container library** — `gen.lib.cdt.*` (splay trees, `dtdisc` disciplines,
   `FieldOffset`-based intrusive links, `dtinsert`/`dtsearch`/`dtfirst`/`dtnext` in
   `Macro`). This is a *generic container library* re-implemented on top of the
   emulation layer — the single largest candidate for replacement by `java.util`.
8. **C globals** — `Globals` ("`zz`"): a god object mixing attribute symbol tables
   (`N_label`, `E_weight`, …), per-pass mutable state (`Tree_node`, `Low/Lim/Slack`,
   `Last_node_decomp`, shape-rendering caches `lastn/poly/vertex`…), and the
   `Memory.identityHashCode` id registry (`all`).
9. **`setjmp`/`longjmp`** — `jmp_buf` + `JUtils.setjmp`; the `longjmp` sites are
   currently `UNSUPPORTED(...)` stubs (e.g. in `ns__c.add_tree_edge`).
10. **`UNSUPPORTED(...)` landmines** — unported constructs that throw
    `UnsupportedOperationException` at runtime. As the `init_rank` case showed
    (see `SMETANA.md`), some of them are *mistranslated non-fatal diagnostics*.
11. **Edge-pair pointer arithmetic** — `AGIN2OUT(e) = e-1` / `AGOUT2IN(e) = e+1`
    emulated by `ST_Agedge_s.PREV/NEXT` and `plus_(±1)`.
12. **Poor-man's sort** — `JUtils.qsort` is a bubble sort (O(n²)) with a
    verify-sorted pass in a `finally` block.

---

## 2. Constraints and non-goals

- **Behavior must stay bit-identical.** Layout heuristics (mincross, acyclic DFS,
  network simplex) are order-sensitive; any change in iteration order or sort stability
  legitimately changes the output. So the target is *exact* output equality, checked by
  golden files, not "looks the same".
- **TeaVM compatibility** (Smetana is the layout engine of the JS/npm build): stick to
  plain `java.util` collections, `String`, arrays. No reflection, no AWT, no
  `String.format` in hot paths. Avoid `java.util.stream` in layout hot paths (perf on
  TeaVM). Preserve the SJPP markers (`// ::remove folder when __HAXE__` etc.).
- **License headers**: `gen.lib.*` and `h.*` files are derived from AT&T/EPL Graphviz
  sources — keep the license blocks when transforming derived code, even after renames.
- **Traceability**: keep the `@Original(path=..., name=...)` annotations as long as a
  method still corresponds to a C function. When a method diverges structurally, keep the
  annotation but note the divergence (this is what makes debugging-against-upstream-C
  possible, as in the `init_rank` session).
- **Non-goal**: re-implementing dot's algorithms from scratch, or "improving" layout
  quality. Any visible output change is a bug of the refactoring by definition
  (compare with native `dot` only to arbitrate genuine upstream quirks).

---

## 3. Phase 0 — Safety net first (prerequisite for everything)

Before touching anything, build cheap, exact, diff-friendly oracles:

- **S1. Smetana golden corpus in Vega.** A dedicated set of `.puml` files with
  `!pragma layout smetana` covering: class/state/activity/JSON/git diagrams, clusters,
  concurrent states, flat edges, self-loops, multi-edges, ports, rankdir LR/TB,
  disconnected components, large-ish graphs (>100 nodes). Golden SVGs compared via the
  existing Vega/pdiff machinery.
- **S2. Layout fingerprints (the real workhorse).** SVG diffs are noisy and indirect.
  Add a debug output that dumps, per diagram, a plain-text "fingerprint":
  `node name/id → rank, order, coord.x, coord.y` and `edge → spline control points`,
  emitted from `CucaDiagramFileMakerSmetana` (or a hook in `dotLayout`). Golden `.txt`
  files, exact string comparison. This catches divergences at the layout level with a
  much better signal than pixels, and pinpoints *which pass* diverged.
- **S3. Intermediate-pass fingerprints (optional but powerful).** Same idea after each
  pipeline stage: after `rank` (ranks only), after `mincross` (orders only), after
  `position` (coords). When a refactor of `ns__c` breaks something, S3 says so directly.
- **S4. A grep-based metrics script** (progress dashboard, see §8): counts of
  `UNSUPPORTED(`, `Macro.` static imports, `__ptr__` references, `CFunction` uses,
  `Globals` public fields, `dtinsert|dtsearch|dtfirst|dtnext` call sites, `CString` uses.

Only S1+S2 are blocking; S3/S4 can come during phase 1.

---

## 4. Refactoring tracks

Tracks are mostly independent; each is a sequence of small commits. Suggested global
ordering in §7.

### Track A — `smetana.core` quick wins (low risk, high confidence-building)

- **A1. `JUtils.qsort` → `Arrays.sort`.** The current bubble sort is *stable*;
  `Arrays.sort(Object[])` (TimSort) is also stable, so replacing it preserves current
  Java behavior even where C `qsort` (unstable) might have differed — we match *today's
  Java behavior*, which is what the goldens encode. Wrap the `CFunction` comparator in a
  `Comparator` adapter first; later (Track D) make comparators typed. Drop the
  verify-in-`finally` pass (or keep it under an `assert`). This is also a perf win
  (mincross sorts a lot).
- **A2. `Memory` cleanup.** `Memory.free()` is a no-op → delete all call sites
  (mechanical, zero risk). Move the `identityHashCode`/`fromIdentityHashCode` id
  registry out of the static class into a small `CStringRegistry` owned by `Globals`
  (it already lives in `zz.all`) with a proper API and error message.
- **A3. `UNSUPPORTED(...)` audit.** Systematic pass over all `UNSUPPORTED` calls,
  classifying each against the upstream C (the `@Original` annotation gives the file):
  **Prerequisite: A6 (or at least its id fix) must land first or together with A3.**
  Many of the C diagnostic paths that A3 re-enables (`agerr` → `LOG`) call
  `agnameof()` — e.g. the `init_rank` loop printing nodes with non-zero priority.
  Without the `virtual_node()` id fix, every `UNSUPPORTED` converted into a log line
  becomes a potential crash on virtual nodes: A3 done alone *arms* the mine instead of
  defusing it.
  - (a) *non-fatal C diagnostics* (`agerr` **not** followed by `longjmp`) → replace by
    `SmetanaDebug.LOG(...)`, like the `init_rank` fix;
  - (b) *genuine `longjmp` abort paths* → convert to a dedicated unchecked exception
    (see A4);
  - (c) *genuinely unported functionality* → keep the throw, but as a dedicated
    `SmetanaUnsupportedException` with the C context in the message, so crashes are
    self-explaining.
  Deliverable: a table (in this file or a companion `UNSUPPORTED-AUDIT.md`) listing every
  site and its classification. This directly de-mines the codebase.
- **A4. `setjmp`/`longjmp` → exceptions.** The only real pattern is
  `if (setjmp(jbuf)) { cleanup; return err; }` + `longjmp(jbuf, 1)` (e.g. `ns.c` error
  paths). Replace with `try { … } catch (NsAbortException e) { cleanup; return err; }`
  and `throw new NsAbortException()` at the (currently `UNSUPPORTED`) longjmp sites.
  Delete `jmp_buf`/`setjmp` afterwards. Small, contained, and it *fixes* today's
  behavior (those error paths currently crash instead of recovering like C does).
- **A5. `nodequeue` → `ArrayDeque<ST_Agnode_s>`.** `new_queue`/`enqueue`/`dequeue`/
  `free_queue` in `utils__c` map 1:1; encapsulate then swap.
- **A6. `virtual_node()` id fix — DECIDED: phase 1, coupled with A3.**
  The `fastgr__c.virtual_node()` id bug from `SMETANA.md`: virtual nodes are built with
  `new ST_Agnode_s()` directly, bypassing `agnode()`/`idmap()`, so `tag.id` stays `0`
  (even → `agnameof()` takes the "named object" branch → `Memory.fromIdentityHashCode`
  throws).
  - **Fix = the minimal one**: assign an odd anonymous id inside `virtual_node()`,
    i.e. exactly the `str == null` branch of `id__c.idmap()`:
    `n.tag.id = zz.ctr; zz.ctr += 2;`. **Scope warning: do NOT route through the real
    `agnode(g, NULL, 1)`** — `agnode` also does dictionary insertion, callbacks and
    `mainsub` setup, whereas today's (golden-encoded) behavior keeps virtual nodes out
    of the cgraph dictionaries (they only live in `GD_nlist`/`GD_rank` via
    `fast_node`). Full alignment with C would first require checking what upstream
    `fastgr.c` 2.38.0 actually does.
  - **Signature consequence**: `virtual_node(ST_Agraph_s g)` does not receive `zz`
    today; `Globals` must be threaded in from the callers (class2, mincross, cluster,
    flat… — inventory them; most already have `zz` in scope).
  - **Plan = two isolated commits**: (1) thread `zz` into `virtual_node` and callers,
    behavior strictly unchanged; (2) assign the odd id, with S2 fingerprints required
    identical before/after. Risk is very low but non-zero (`tag.id` of virtual nodes is
    consumed by no production path: not in cgraph dictionaries, and mincross ordering
    uses `tag.seq`/`ND_order`, not `id`) — which is precisely the kind of claim S2
    exists to verify. Therefore: after phase 0, never before.
  - Immediate payoff: `agnameof()` works on virtual nodes → debug traces usable without
    `safeName()` everywhere, and A3 becomes safe (see A3 prerequisite note).

### Track B — Containers: replace C emulation with `java.util`

The pattern for every item: **(1) encapsulate** the raw idiom behind methods on the
existing class, migrating call sites mechanically; **(2) swap** the internal
representation; **(3) simplify** call sites to idiomatic Java. Steps 1 and 3 are large
but trivially safe; step 2 is tiny and the only risky commit.

- **B1. `ST_elist` / `ST_nlist_t` → `ArrayList`.**
  - Encapsulate: add `append(e)`, `get(i)`, `size()`, `clear()`, `iterator()` /
    `Iterable` to `ST_elist`; replace the three raw idioms everywhere:
    `Macro.elist_append` / `alloc_elist`, direct `L.list.set_(L.size++, e)` writes,
    and null-sentinel loops `for (i=0; (e=L.list.get_(i))!=null; i++)` → for-each.
  - Careful with the **copy vs alias semantics** of `___()`/`copy()`: today
    `ND_save_in(n, v)` copies `{size, list}` field-by-field (shallow: the backing array
    is *shared*). The encapsulated API must reproduce exactly that (`saveFrom(other)`
    keeping shared backing vs `copyOf` — audit each of the few `___` call sites:
    `save_in`/`save_out` swaps in `rank__c`/`mincross__c` are the sensitive ones).
  - Swap: internal `CArrayOfStar` → `ArrayList<ST_Agedge_s>`; `size` field disappears.
- **B2. `CArrayOfStar` / `CArray` general retirement.** Outside elist/nlist, usages fall
  into three families:
  - plain growable arrays (e.g. `GD_rank`, `Tree_edge.list`, boxes, points) →
    `ArrayList` or plain Java arrays with an explicit `grow()`;
  - **pointer-arithmetic slices** (`plus_(delta)` passed around, `minus_` for index
    recovery — typical in `mincross__c`, `routespl__c`, pathplan) → replace by passing
    `(array, offset)` or a tiny `ListSlice` view first, then by explicit indices;
  - struct arrays pre-filled by `ZType.create()` (`CArray.ALLOC__`) → arrays of the
    struct type with an explicit fill loop; `ZType` dies with them.
  Do this file by file, not globally; pathplan (`route__c`, `shortest__c`) is
  self-contained and a good pilot.
- **B3. cdt → `java.util` maps (the big one).**
  **DECIDED ordering (see §7 and the decision record in §7bis): the `TreeMap` swap
  happens in phase 4, *after* C2 has been done on `dotgen`/`common`, and is preceded by
  a mandatory audit** (see "B3.0" below). Rationale: C2 and B3 are almost orthogonal
  file-wise (cdt call sites are concentrated in `gen.lib.cgraph`; C2 targets
  `dotgen`/`common`), C2's huge mechanical diff is better landed *before* opening a
  long-lived risky branch, and C2 pays off immediately on the code that day-to-day
  debugging actually reads. The only hard dependency is the reverse one: C2's *endgame*
  (private, typed struct fields) is blocked until B3 removes the
  `FieldOffset`/`getTheField` pokes into `id_link`/`seq_link`.

  **B3.0 — Mandatory audit before any cdt code is written.** Three traps must be
  inventoried first (deliverable: a short section in this file or a companion
  `CDT-AUDIT.md`, listing every usage site and its resolution):
  - **`dtview__c` / `Ag_dictop_G`**: in cgraph, subgraph dictionaries can be *stacked
    views* onto the parent's dictionary. `TreeMap` has no view mechanism — check
    whether the port actually exercises `dtview`, and if so model the parent-lookup
    explicitly.
  - **`DT_OBAG` (`Dtobag`)**: an ordered *multiset*; `TreeMap` rejects duplicate keys →
    `TreeMap<K, List<V>>` or a sorted list. Inventory who really uses it.
  - **`dtflatten` / `dtrestore` / `UNFLATTEN`**: the temporary "flattened" splay-tree
    mode used during iteration. With `TreeMap` iteration is already safe, so these are
    probably removable — but each call site must be audited, plus the exact
    equal-key insertion semantics (`DT_INSERT` returns the existing element in a
    `DT_OSET`).
  Note the failure mode if B3 goes wrong: a changed iteration order in the cgraph
  dictionaries alters node/edge order at graph *construction* time, so the whole layout
  diverges downstream — S2 will catch it, but the diagnosis points far from the cause.
  Hence the isolated, unit-testable swap.

  Graphviz's cdt library (`gen.lib.cdt.*`, `dtdisc` disciplines, `FieldOffset` intrusive
  links) implements ordered sets/multisets over splay trees. Every use maps cleanly:
  - `DT_OSET` ordered by `comparf` → `TreeMap<K, V>` / `TreeSet` with the same
    `Comparator` — **iteration order is by definition identical**, which is what makes
    this replacement safe for layout determinism;
  - name→symbol dictionaries (`AgDataDictDisc`, `Refstrdisc` keyed by `CString`) →
    `TreeMap<String, ST_Agsym_s>` (keep comparator = strcmp order, i.e. natural `String`
    order for the byte-compatible subset actually used);
  - node/edge id and seq dictionaries (`Ag_subnode_id_disc`,
    `Ag_mainedge_seq_disc`, …) → `TreeMap` keyed on `(id)` / `(seq)` with the
    translated `cmpf` logic as `Comparator`.
  Strategy: define a minimal `Dict<E>` interface (`insert`, `search`, `delete`, `first`,
  `next`/iterator, `size`) in `smetana.core`; first implementation *delegates to the
  existing dt code* (adapter), migrate all `Macro.dtinsert/dtsearch/dtfirst/dtnext/`
  `dtdelete` call sites plus direct `dt*__c` calls to it; then provide the
  `TreeMap`-backed implementation and flip a factory. Once green, delete
  `gen.lib.cdt.*`, `FieldOffset`, the intrusive `ST_dtlink_s` fields in `ST_Agedge_s`/
  `ST_Agsubnode_s`/`ST_Agsym_s`, and the `dtdisc` plumbing in `Globals`.
  This single track removes: 9 cdt files, `FieldOffset`, `ST_dt_s`/`ST_dtdisc_s`/
  `ST_dtlink_s`/`ST_dtdata_s`/`ST_dthold_s`/`ST_dtmethod_s`, most `CFunction`
  disciplines, and `getTheField` on `__ptr__`.
- **B4. `CString` → `String`.**
  Audit first: most `CString`s are immutable attribute names/values; mutation
  (`setCharAt`, `copyFrom`, `gmalloc` buffers) and pointer arithmetic (`plus_`,
  `strchr`-returning-pointer, parsing loops in `arrows__c`/`shapes__c`/`labels__c`) are
  concentrated in a few parsing routines.
  - Step 1: give `CString` proper `equals`/`hashCode` on content; migrate
    `streq`/`strcmp(a,b)==0`/`isSame` call sites to `.equals`.
  - Step 2: change *signatures* from `CString` to `String` on the pure-read boundary
    (attribute lookup: `agget`, `agattr`, `late_string` etc. can expose `String`
    overloads first), pushing `CString` inward.
  - Step 3: rewrite the few genuinely pointer-y parsers (arrowhead names, style parsing,
    record labels) as index-based `String` scanning, one function at a time, each behind
    a fingerprint check.
  - The `Memory.identityHashCode` registry and `ST_refstr_t` refcounting exist only to
    emulate `char*` identity for the id discipline — they die when B3+B4 land
    (`agnameof`/`idprint` become trivial `String` lookups; this also permanently kills
    the `virtual_node` crash family).

### Track C — Object orientation of the graph model (`h.*`)

- **C1. Collapse the `Agrec`/`AGDATA` mechanism.** Since exactly one info record exists
  per object kind, replace the untyped `ST_Agrec_s data` + cast with typed final fields:
  `ST_Agnode_s.info : ST_Agnodeinfo_t`, `ST_Agedge_s.info : ST_Agedgeinfo_t`,
  `ST_Agraph_s.info : ST_Agraphinfo_t` (created in `agbindrec`-equivalent spots).
  Transitional: keep `data` delegating to `info` until all accessors moved.
- **C2. Move the `Macro` accessors onto the structs.** `ND_rank(n)` → `n.rank()`,
  `ND_rank(n, v)` → `n.setRank(v)`, same for the ~200 `GD_/ND_/ED_` pairs. Fully
  mechanical and IDE-scriptable: first *move* each static method into the struct class
  (keeping a one-line delegating static in `Macro` so nothing else changes), then
  inline the delegates package by package (`dotgen`, then `common`, …). Do it by
  accessor family, dozens per commit.
  **DECIDED scope for phase 3: `dotgen`/`common` only — do not touch `gen.lib.cgraph`
  until B3 is done.** cgraph is where the cdt call sites live; leaving it alone keeps
  the two workstreams file-disjoint and rebases painless. Endgame: `Macro` shrinks to
  geometry helpers and constants; struct fields can then become private and properly
  typed — but note this endgame is *blocked* until B3 removes the
  `FieldOffset`/`getTheField` intrusive-link access (`id_link`/`seq_link`).
- **C3. Type the untyped.** Replace `__ptr__`-typed fields with real types:
  `ND_alg` (per-pass scratch `void*`) → dedicated typed fields per consumer or a small
  typed holder; `GD_minset/maxset`, `ND_inleaf/outleaf` likewise. Each removal shrinks
  the `__ptr__` interface until it can be deleted along with
  `UnsupportedC`/`UnsupportedStarStruct`.
- **C4. Edge pairs.** Replace `PREV`/`NEXT` + `plus_(±1)` (`AGIN2OUT`/`AGOUT2IN`) with
  an explicit `ST_Agedge_s.opposite` reference set at creation (`ST_Agedgepair_s`
  already materializes the pair) and `e.opposite()` / `e.asOut()` / `e.asIn()` methods.
  Removes the last "pointer arithmetic on objects" idiom.
- **C5. Constants → enums / typed flags.** `Macro`'s int families
  (NORMAL/VIRTUAL/SLACKNODE… node types, RANKDIR_*, ranktype, edge types, style bits)
  → Java enums or at least named `int` wrappers, once C2 gives them a natural home
  (e.g. `NodeType` enum on the node accessor). Do late: it touches many comparisons.
- **C6. Naming pass (last).** `ST_Agnode_s` → `GNode` / `ST_Agraph_s` → `GGraph` etc.,
  package `h` → `smetana.model`. Pure IDE rename, but do it *only at the end*: keeping C
  names as long as the code still mirrors C preserves grep-ability against upstream
  Graphviz, which SMETANA.md shows is the main debugging technique.

### Track D — Function pointers → interfaces and lambdas

- **D1. Typed functional interfaces** replacing `CFunction` per use family:
  `Comparator<T>` for `comparf` (feeds A1/B3), `ArrowGenerator` for `Arrowtypes[].gen`,
  `PenaltyFn`/callbacks in `dotsplines`/`routespl`, `splineInfo` swap/merge predicates.
- **D2. `ST_shape_functions` → a real `Shape` interface** (`init`, `free`, `port`,
  `inside`, `path`, `gencode`) with `PolygonShape` and `RecordShape` implementations;
  `Globals.Shapes[]` → a `Map<String, Shape>`. This is textbook vtable-to-interface.
- **D3. Discipline structs** (`ST_Agiddisc_s`, `ST_Agmemdisc_s`, `AgIdDisc`, `AgMemDisc`)
  → interfaces with one real implementation each; the mem discipline is a no-op in Java
  and can be deleted outright.

### Track E — Algorithm classes and the death of `Globals`

`Globals` fields cluster naturally by pass; each cluster becomes instance state of a
proper class, extracted one pass at a time:

- **E1. `NetworkSimplex`** (from `ns__c`): fields `Tree_node`, `Tree_edge`, `Low`,
  `Lim`, `Slack`, `S_i`, `Search_size`, `Enter`, `N_nodes`, `G_ns`, `Minrank/Maxrank` →
  instance fields; entry point `int rank(graph, balance, maxiter)`. `ns__c` keeps
  static wrappers delegating to `new NetworkSimplex(zz).rank(...)` until callers are
  migrated. This also structurally documents the two-invocations subtlety (rank vs
  position auxiliary graph) from SMETANA.md — two *instances*, no shared state.
- **E2. Same pattern for**: `Mincross` (`MinQuit`, `Convergence`, `ReMincross`,
  `TE_list`, `TI_list`, `GlobalMin/MaxRank`, `Root`), `Decompose` (`Last_node_decomp`,
  `Cmark`, `G_decomp`), `Position`/aux-graph builder, `DotSplines`/`RouteSpl`
  (`ps`, `maxpn`, `nedges`, `nboxes`, `routeinit`, `edges`, `boxes`…), `PathPlan`
  (`tris`, `pnls`, `dq`…), shape rendering caches (`lastn`, `poly`, `vertex`,
  `xsize/ysize`…) into the `Shape` objects of D2.
- **E3. What remains of `Globals`** splits into: `AttributeTables` (the `G_*`,`N_*`,
  `E_*` symbol handles), the graph universe/id registry, and a small `LayoutOptions`
  (Rankdir, Flip, Concentrate, MaxIter, State). At that point `zz` parameter threading
  can be reduced pass by pass (many functions only need it transitively for state that
  just moved into their own class).
- **E4. Package restructuring (last):** `gen.lib.dotgen` → `smetana.layout.dot`, etc.,
  and the `xxx__c` class names → algorithm names (`ns__c` → `NetworkSimplex` happens
  naturally in E1). Keep `@Original` on methods that still mirror C functions.

---

## 5. Traps checklist (read before each session)

- **Struct copy vs reference.** Every `___()`/`copy()` call site is a potential shallow/
  deep-copy subtlety (`ST_elist.___` shares the backing array; `MAKEFWDEDGE` clones an
  edge *and* its info in place). Audit each one when its type is refactored.
- **Sort stability & comparator consistency.** Goldens encode the *current Java*
  behavior (stable bubble sort). Keep stability. If a comparator is inconsistent
  (returns contradictory results), TimSort may throw where bubble sort silently
  proceeded — treat any such throw as a discovered latent bug, not as a reason to keep
  bubble sort.
- **Iteration order = layout output.** Only replace ordered structures
  (splay-by-comparator → `TreeMap` same comparator; insertion-ordered lists →
  `ArrayList`). Never introduce `HashMap`/`HashSet` iteration into a layout path.
- **`UNSUPPORTED` may hide non-fatal paths** (the `init_rank` lesson): always check the
  original C before assuming a branch is dead or must abort.
- **Debug code must be crash-proof**: use `SmetanaDebug.safeName()` / `TRACE()`
  conventions from `SMETANA.md` in any instrumentation added while refactoring.
- **Don't chase upstream Graphviz quirks**: if native `dot` produces the same odd
  output, it's not a regression.
- **TeaVM/SJPP**: after each phase, run the TeaVM build; don't touch `::` SJPP marker
  comments; no new dependencies (standard library only).

---

## 6. What "done" looks like (target architecture)

- `smetana.model` (ex-`h`): typed graph model — `GGraph`, `GNode`, `GEdge`, real fields,
  real methods, `java.util` collections, no casts, no `__ptr__`.
- `smetana.layout.*` (ex-`gen.lib`): one class per algorithm pass with instance state
  (`NetworkSimplex`, `Mincross`, `Ranker`, `Position`, `SplineRouter`, `PathPlan`,
  shapes), pipeline orchestrated by `DotLayout`.
- `smetana.core`: reduced to `SmetanaDebug`, geometry helpers, maybe `LayoutOptions`.
- Deleted entirely: `CString`, `CArray*`, `CFunction`, `FieldOffset`, `__ptr__`,
  `__struct__`, `ZType`, `size_t`, `jmp_buf`, `Memory`, `UnsupportedC*`,
  `gen.lib.cdt.*`, all `ST_dt*` structs, the `Agrec` machinery.
- `Globals`/`zz` gone or reduced to an explicit, documented context object.

---

## 7. Suggested roadmap

| Phase | Content | Tracks |
|---|---|---|
| 0 | Safety net: smetana Vega corpus + layout fingerprints + metrics script | S1–S4 |
| 1 | Core quick wins: qsort, Memory, nodequeue, setjmp→exceptions; **virtual_node id fix (A6) coupled with the UNSUPPORTED audit (A3) — A6 first or together, never A3 alone** | A1–A6 |
| 2 | Encapsulate containers (elist API, slice audit); pilot on pathplan; optionally start the `Dict` adapter (B3 encapsulation step, zero-risk) | B1, B2 (start), B3 (adapter) |
| 3 | Collapse Agrec + move Macro accessors onto structs — **scope: `dotgen`/`common` only, `cgraph` untouched until B3** | C1, C2 |
| 4 | **cdt audit first (B3.0: dtview, DT_OBAG, flatten/restore)**, then cdt → TreeMap; CFunction → typed interfaces; shapes vtable → interface | B3, D1–D3 |
| 5 | Algorithm classes, Globals dismantling | E1–E3 |
| 6 | CString → String (parsers last) | B4 |
| 7 | Typed constants/enums, edge-pair cleanup, C2 endgame on cgraph + private fields, renames, package moves | C3–C6, E4 |

### §7bis — Decision record

- **2026-07: ordering C2 vs B3 (cdt).** Decided: hybrid sequencing. (1) C2 mechanical
  pass on `dotgen`/`common` in phase 3 (immediate readability payoff on the code
  actually debugged daily, near-zero risk, huge diff landed before any long-lived
  branch); (2) the B3 *encapsulation* step (`Dict` adapter over existing dt code) may
  start as early as phase 2 since it is risk-free; (3) the `TreeMap` swap stays in
  phase 4, gated by the B3.0 audit (dtview / DT_OBAG / dtflatten-dtrestore /
  equal-key insertion semantics). Hard dependency to remember: C2 endgame (private
  typed fields) needs B3 done first.
- **2026-07: `virtual_node()` id fix (A6) moved to phase 1**, coupled with A3, because
  A3's `agerr`→`LOG` conversions re-enable diagnostic paths that call `agnameof()` on
  virtual nodes — A3 without A6 arms the mine. Fix is the *minimal* one (odd anonymous
  id from `zz.ctr`, NOT a full `agnode()` call), in two commits (thread `zz` into
  `virtual_node()` + callers, then assign the id), validated by identical S2
  fingerprints. See A6 for full details.

Phases 1–3 are safe, mostly mechanical, and already remove most day-to-day friction
(readable accessors, no landmines, debuggable errors). Phases 4–5 are where the code
becomes genuinely object-oriented. Each phase is shippable; there is no point of no
return anywhere.

---

## 8. Progress metrics (grep dashboard)

Track these counts over time (script candidate: `smetana-metrics` in CI or a local
gradle task):

```
UNSUPPORTED(            # landmines               (A3)
import static smetana.core.Macro.   # accessor coupling  (C2)
__ptr__                 # untyped pointers          (C3)
CFunction               # untyped function ptrs     (D)
CString                 # C strings                 (B4)
CArrayOfStar|CArray<    # C arrays                  (B2)
dtinsert|dtsearch|dtfirst|dtnext|dtdelete  # cdt    (B3)
public .* zz\.|Globals  # god-object fields         (E)
```

All trending to zero, except `Globals` which trends to a small documented context.
