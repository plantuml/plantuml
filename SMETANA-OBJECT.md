# SMETANA-OBJECT: target object-oriented design for Smetana

This document describes a **coherent target**: what Smetana would look like as a real
object-oriented Java library. It is *not* a refactoring plan — `REFACTOR-SMETANA.md`
describes *how to get there* incrementally; this document describes *where we are
going*, so that every refactoring step can be checked against a stable destination.

The design is driven by one concrete goal: **improving the PlantUML ↔ Smetana
integration**. Today Smetana is used as a black box (see §2); the target exposes the
graph, the layout passes and their intermediate results as first-class objects with
methods and capabilities.

For each class: name, main methods, and **where the code comes from** in today's
sources (`gen.lib.*`, `h.*`, `smetana.core`, `net.sourceforge.plantuml.sdot`).

---

## 1. Relationship with REFACTOR-SMETANA.md

The refactoring tracks converge onto this target:

| Track (REFACTOR-SMETANA.md) | Produces here |
|---|---|
| C1/C2/C3 (Agrec collapse, Macro accessors, typing) | the `smetana.model` classes (§5) |
| B1/B2/B3 (elist, arrays, cdt → java.util) | the internals of `Graph`/`Node`/`Edge` |
| D1/D2 (CFunction → interfaces, shapes vtable) | `Shape` and the pass callbacks |
| E1/E2/E3 (algorithm classes, Globals dismantling) | the `smetana.layout` pipeline (§6) |
| A4 (setjmp → exceptions) | `LayoutException` |
| S2/S3 (layout fingerprints) | become trivial: they are just `LayoutResult`/stage snapshots printed |

Nothing in this document requires a big-bang: every class below is the *endgame* of an
already-planned track.

---

## 2. Why: the black-box problem, as seen from `CucaDiagramFileMakerSmetana`

Today's integration (in `net.sourceforge.plantuml.sdot.CucaDiagramFileMakerSmetana`):

1. **Stringly-typed construction**: `agsafeset(zz, agnode, new CString("width"),
   new CString("0.1"), ...)` — every property is a `CString` attribute, parsed back by
   Smetana internally.
2. **The `_dim_W_H_` label hack**: label sizes are smuggled through the `label`
   attribute as a magic string (`Macro.createHackInitDimensionFromLabel`), parsed by
   `Macro.hackInitDimensionFromLabel` inside `shapes__c`. Node sizes travel as inches
   (`node.getWidth() / 72`) while results come back in points.
3. **Global state + global lock**: `Globals.open()` / `Globals.close()` wrapped in a
   `static ReentrantLock` — one layout at a time per JVM, painful for plantuml-server
   and the MCP server.
4. **Cast-based read-back**: results are extracted by casting `gr.data` to
   `ST_Agraphinfo_t`, wrapping in `BoxInfo`, reading `ED_spl(e)` inside `SmetanaEdge` —
   with `System.err` fallbacks when the casts fail.
5. **Y-axis flip done by hand** (`YMirror`), margins and paddings re-derived on the
   PlantUML side.
6. **No intermediate access**: ranks, orders, virtual chains, the reason why a layout
   looks the way it does — all invisible. Debugging means `SmetanaDebug.TRACE()`
   archaeology (see `SMETANA.md`).

The target API replaces each of these with typed objects.

---

## 3. Design principles

- **No global state.** One engine instance per layout; concurrent layouts are safe by
  construction. What is `Globals` today becomes constructor parameters, per-pass
  instance fields, or `LayoutResult` content.
- **One unit everywhere: points** (1/72 inch, `double`). The inch never crosses the
  API. Y grows downward by default (screen convention), with the flip handled inside
  `LayoutResult` — `YMirror` disappears from PlantUML.
- **Inputs are built, outputs are immutable.** `Graph` is mutable until `layout()` is
  called; `LayoutResult` and all stage snapshots are read-only.
- **Everything the C stored in `void*`/attributes becomes a typed method.** The string
  attribute map survives only as an escape hatch (`attr(String)`), not as the main API.
- **Intermediate results are first-class** (`Ranking`, `Ordering`, `Coordinates`,
  `Routing`) and observable via a listener, because that is precisely what the
  black-box mode lacks.
- **Errors are exceptions** (`LayoutException` with graph context), never
  `UnsupportedOperationException` landmines.
- **TeaVM-compatible**: plain `java.util`, no reflection, no AWT (same constraints as
  today, see REFACTOR-SMETANA.md §2).

---

## 4. Package map

```
smetana.geom      Point, Size, Box, Bezier, Spline
smetana.model     Graph, Node, Edge, Cluster, Label, Shape, Attributes
smetana.layout    DotLayoutEngine, LayoutOptions, LayoutResult, LayoutException,
                  pipeline passes (CycleBreaker, Ranker, NetworkSimplex, MinCross,
                  Positioner, SplineRouter, PathPlanner),
                  stage snapshots (Ranking, Ordering, Coordinates, Routing),
                  LayoutListener
smetana.debug     SmetanaDebug (kept), fingerprint dumpers
```

`gen.lib.gvc.*` (plugin machinery), `emit__c`, `agerror__c`, the cdt package and the
whole `smetana.core` emulation layer have **no equivalent** in the target: they are
infrastructure for problems Java does not have (see §9).

---

## 5. `smetana.geom` — geometry values

Small immutable value classes. Sources are the geometry structs of `h.*` plus the
geometry macros of `Macro`.

### `Point`
| Method | Notes | Source |
|---|---|---|
| `x()`, `y()` | doubles, points | `ST_pointf` / `ST_point` |
| `plus(Point)`, `minus(Point)`, `distance(Point)` | | `Macro.DIST2/LEN/hypot`, `geom__c` |

### `Size`
| Method | Notes | Source |
|---|---|---|
| `width()`, `height()` | | today: the `_dim_` hack + `width`/`height` attrs |

### `Box`
| Method | Notes | Source |
|---|---|---|
| `lowerLeft()`, `upperRight()` | | `ST_boxf` (`LL`/`UR`) |
| `width()`, `height()`, `center()` | | derived |
| `union(Box)`, `contains(Point)`, `expand(double)` | | `Macro.INSIDE`, `geom__c`, PlantUML's `getSmetanaMinMax()` |

### `Bezier`
| Method | Notes | Source |
|---|---|---|
| `points()` | control points, `List<Point>` | `ST_bezier.list` |
| `start()`, `end()` | actual endpoints when clipped to shapes | `ST_bezier.sp/ep` + `sflag/eflag` |

### `Spline`
| Method | Notes | Source |
|---|---|---|
| `beziers()` | `List<Bezier>` | `ST_splines.list` |
| `boundingBox()` | | `ST_splines.bb` |
| `pointAt(double t)`, `asPath()` | convenience for rendering | today re-implemented in `SmetanaEdge.getDotPath()` |

---

## 6. `smetana.model` — the graph

The cgraph layer, typed. The `Agrec`/`AGDATA` machinery, the cdt dictionaries and the
id/refstr registry all disappear *inside* these classes (java.util maps).

### `Graph`
The main-graph and subgraph API merged (`ST_Agraph_s` + `ST_Agraphinfo_t`).

| Method | Notes | Source |
|---|---|---|
| `Graph()` / `newSubgraph(String name)` | subgraph names starting with `cluster` → see `newCluster` | `graph__c.agopen`, `subg__c.agsubg` |
| `newCluster(String name)` | returns `Cluster` | `subg__c` + `dotgen/cluster__c` conventions |
| `newNode(String name)` / `newNode()` (anonymous) | | `node__c.agnode`, `id__c.idmap` |
| `newEdge(Node tail, Node head)` | multi-edges allowed | `edge__c.agedge` |
| `nodes()`, `edges()`, `clusters()` | insertion-ordered iteration (determinism) | today: cdt seq dictionaries (`Ag_subnode_seq_disc`, …) |
| `findNode(String)`, `findEdge(Node, Node)` | | `agfindnode`/`agfindedge` (`Macro`, `edge__c`) |
| `parent()`, `root()`, `isRoot()` | | `obj__c.agroot`, `GD_parent` |
| `nodeCount()`, `edgeCount()` | | `graph__c.agnnodes/agnedges` |
| `attr(String)`, `setAttr(String, String)` | escape hatch, kept | `attr__c.agget/agsafeset` |

### `Node`
`ST_Agnode_s` with `ST_Agnodeinfo_t` merged in (REFACTOR-SMETANA C1). Input methods
first, layout-result accessors move to `LayoutResult` (§7) — the node itself stays
input-only so that one graph can be laid out twice without state bleeding.

| Method | Notes | Source |
|---|---|---|
| `name()` | | `id__c.agnameof` (post B4: plain `String`) |
| `setSize(Size)` / `size()` | **replaces the `width`/`height` inch attributes** | `ND_width/ND_height`, `shapes__c.poly_init` |
| `setShape(Shape)` / `shape()` | default `Shape.BOX` for PlantUML | `ND_shape`, `shapes__c` |
| `setLabel(Label)` / `label()` | **replaces the `_dim_` hack** | `ND_label`, `labels__c.make_label` |
| `graph()` | owning graph | `agraphof` |
| `outEdges()`, `inEdges()`, `edges()` | | `ND_out`/`ND_in` elists, `edge__c.agfstout/agfstin` |
| `degree()` | | `edge__c.agdegree` |

### `Edge`
`ST_Agedge_s` + `ST_Agedgeinfo_t` merged. The in/out edge-pair trick
(`AGIN2OUT`/`AGOUT2IN`, `PREV`/`NEXT`) becomes an internal `opposite` reference,
invisible to the API.

| Method | Notes | Source |
|---|---|---|
| `tail()`, `head()` | | `agtail`/`aghead` (`edge__c`) |
| `setMinLength(int)` | rank distance | `ED_minlen` (today: `minlen` string attr) |
| `setWeight(int)` | | `ED_weight` |
| `setLabel(Label)`, `setHeadLabel(Label)`, `setTailLabel(Label)` | | `ED_label/ED_head_label/ED_tail_label` (today: `_dim_` hacks on `label`/`headlabel`/`taillabel`) |
| `setConstraint(boolean)` | rank-constraining or not | `rank__c` (edge `constraint` attr) |
| `isSelfLoop()` | | `agtail == aghead` checks scattered in `dotsplines__c` |

### `Cluster`
Subgraph with layout semantics (a box drawn around its content).

| Method | Notes | Source |
|---|---|---|
| `setLabel(Label)` / `label()` | | `GD_label`, PlantUML's cluster `_dim_` hack |
| `nodes()`, `subClusters()` | | `GD_clust`, `cluster__c` |
| `setMargin(double)` | | `CL_OFFSET` constant in `Macro` |

### `Label`
Today a label is either real text (measured by the stub `gvtextlayout__c`) or a
PlantUML-provided box smuggled as `_dim_W_H_`. The target makes the second case
first-class and the normal one.

| Method | Notes | Source |
|---|---|---|
| `Label.ofSize(Size)` | **the** PlantUML constructor: a reserved box, no text | replaces `Macro.createHackInitDimensionFromLabel` + `hackInitDimensionFromLabel` |
| `Label.ofText(String, TextMeasurer)` | for standalone/MCP use | `labels__c.make_label`, `textspan__c` |
| `size()` | | `ST_textlabel_t.dimen` |

(`TextMeasurer` is a one-method interface `Size measure(String text, double fontsize)`;
PlantUML would back it with its `StringBounder`, the headless build with
`StringBounderFromWidthTable`. Source: `gvtextlayout__c`, `textspan__c`.)

### `Shape`
The `ST_shape_functions` vtable as an interface (REFACTOR-SMETANA D2).

| Method | Notes | Source |
|---|---|---|
| `initialize(Node)` | size adjustment | `shapes__c.poly_init` / `record_init` |
| `boundary(Node)` | polygon for clipping | `ST_polygon_t`, `poly_path` |
| `inside(Node, Point)` | spline clipping predicate | `shapes__c.poly_inside` / `record_inside` |
| `port(Node, String)` | named ports | `shapes__c.poly_port`, `ST_port` |

Implementations: `BoxShape`, `EllipseShape` (from `p_box`/`p_ellipse` in `Globals`),
`RecordShape` (from `record_*` in `shapes__c`). PlantUML only ever uses `BoxShape`.

---

## 7. `smetana.layout` — the engine, the pipeline, the results

### `DotLayoutEngine` (the facade)
Replaces `gvContext(zz)` + `gvLayoutJobs(zz, gvc, g)` + the global lock. An instance
holds no static state → **concurrent layouts become legal**.

| Method | Notes | Source |
|---|---|---|
| `DotLayoutEngine(LayoutOptions)` | | `gvc__c.gvContext`, `gvcontext__c` (mostly deleted, see §9) |
| `LayoutResult layout(Graph)` | the one-shot call, current behavior | `gvlayout__c.gvLayoutJobs` → `dotinit__c.dotLayout` |
| `LayoutResult layout(Graph, LayoutListener)` | same, with stage observation | new (wraps the pipeline of `dotinit__c.dotLayout`: `dot1_rank`; `dot_mincross`; `dot_position`; `dot_sameports`; `dot_splines`; `dot_compoundEdges`) |

### `LayoutOptions`
The configuration half of `Globals` plus the graph-level attributes parsed today by
`input__c`/`dotinit__c` from strings.

| Method | Notes | Source |
|---|---|---|
| `rankdir(Rankdir)` | TB/LR/BT/RL enum | `GD_rankdir`, `Macro.RANKDIR_*` (today: `rankdir` string attr set by PlantUML) |
| `nodeSeparation(double)`, `rankSeparation(double)` | points | `GD_nodesep/GD_ranksep`, `Macro.DEFAULT_NODESEP/RANKSEP` |
| `concentrate(boolean)` | | `Globals.Concentrate`, `conc__c` |
| `maxMincrossIterations(int)` | | `Globals.MaxIter`, `mincross__c` |
| `edgeType(EdgeType)` | SPLINE/LINE/ORTHO… | `Macro.ET_*`, `dotsplines__c` |

### `LayoutResult`
**The class that kills the black box.** Immutable, all coordinates in points, Y-down
already applied. Everything `BoxInfo`, `SmetanaEdge`, `getSmetanaMinMax()` and
`YMirror` reverse-engineer today becomes a method here.

| Method | Notes | Source |
|---|---|---|
| `position(Node)` → `Point` | node center | `ND_coord` (read via `BoxInfo.fromNode` today) |
| `box(Node)` → `Box` | | `ND_coord` ± `ND_lw/ND_rw/ND_ht` |
| `box(Cluster)` → `Box` | | `GD_bb` (the `ST_Agraphinfo_t` cast in `drawGroup` today) |
| `spline(Edge)` → `Spline` | | `ED_spl` (read inside `SmetanaEdge` today) |
| `labelPosition(Edge)` / `headLabelPosition(Edge)` / `tailLabelPosition(Edge)` → `Point` | | `ST_textlabel_t.pos` on `ED_label/ED_head_label/ED_tail_label` |
| `labelPosition(Cluster)` → `Point` | | `GD_label(g).pos`, `cluster__c` |
| `boundingBox()` → `Box` | whole drawing | `GD_bb(root)` (replaces `getSmetanaMinMax()`) |
| `ranking()`, `ordering()` | the intermediate snapshots, retained | see below |

### Stage snapshots (the intermediate objects)

These are what make white-box integration and debugging possible — and they make the
S2/S3 fingerprints of REFACTOR-SMETANA.md trivial (`toString()` them).

**`Ranking`** — result of the rank pass.
| Method | Source |
|---|---|
| `rank(Node)` → `int` | `ND_rank` |
| `minRank()`, `maxRank()` | `GD_minrank/GD_maxrank` |
| `nodesAtRank(int)` | `GD_rank(g)[r].v` (`ST_rank_t`) |

**`Ordering`** — result of mincross: left-to-right order per rank, including virtual
nodes.
| Method | Source |
|---|---|
| `order(Node)` → `int` | `ND_order` |
| `rankSequence(int)` → `List<NodeOrVirtual>` | `GD_rank(g)[r].v` |
| `crossings()` → `int` | `mincross__c.ncross` |

**`Coordinates`** — after position: `x(Node)`, `y(Node)` (`ND_coord`,
`position__c.set_ypos`/x from the aux-graph network simplex).

**`Routing`** — after splines: `spline(Edge)`, `boxesUsed(Edge)` (`ED_spl`, the
`boxes`/`ST_path` machinery of `dotsplines__c`/`routespl__c`).

**`VirtualStructure`** — optional, for deep debugging: the chains of virtual nodes
created for long edges (`class2__c.mkvirtualnode`/`fastgr__c.virtual_node`,
`ED_to_virt`/`ED_to_orig`): `chain(Edge)` → `List<VirtualNode>`. This is exactly what
was reverse-engineered by hand in the `init_rank` debugging session (`SMETANA.md`).

### `LayoutListener`
| Method | Source |
|---|---|
| `afterRanking(Ranking)` | hook after `rank__c.dot1_rank` |
| `afterOrdering(Ordering)` | hook after `mincross__c.dot_mincross` |
| `afterPositioning(Coordinates)` | hook after `position__c.dot_position` |
| `afterRouting(Routing)` | hook after `dotsplines__c.dot_splines` |

Default no-op implementation; `SmetanaDebug`/fingerprint dumpers become listeners.

### The pass classes (internal, but public enough to test in isolation)

Direct product of REFACTOR-SMETANA Track E — each is today a `xxx__c` static bag plus a
cluster of `Globals` fields:

| Class | Responsibility | Source (`gen.lib.*`) | Ex-`Globals` state absorbed |
|---|---|---|---|
| `ComponentDecomposer` | split into connected components, pack back | `dotgen/decomp__c`, `pack/pack__c` | `Last_node_decomp`, `Cmark`, `G_decomp` |
| `CycleBreaker` | DFS back-edge reversal | `dotgen/acyclic__c` | (stateless) |
| `EdgeClassifier` | real/virtual/flat/cluster edge classification | `dotgen/class1__c`, `class2__c` | — |
| `Ranker` | Y-levels, cluster ranking | `dotgen/rank__c`, `dotgen/cluster__c` | `Minrank/Maxrank` |
| `NetworkSimplex` | the solver, used twice (rank + X-position; see `SMETANA.md`) | `common/ns__c` | `Tree_node`, `Tree_edge`, `Low`, `Lim`, `Slack`, `S_i`, `Search_size`, `Enter`, `N_nodes`, `G_ns` |
| `MinCross` | crossing reduction, flat edges | `dotgen/mincross__c`, `flat__c`, `conc__c` | `MinQuit`, `Convergence`, `ReMincross`, `TE_list`, `TI_list`, `GlobalMin/MaxRank`, `Root` |
| `Positioner` | X coords via auxiliary balance graph | `dotgen/position__c` | (aux graph is local) |
| `SplineRouter` | edge routing, self-loops, ports | `dotgen/dotsplines__c`, `common/splines__c`, `routespl__c`, `sameport__c` | `ps`, `maxpn`, `nedges`, `nboxes`, `routeinit`, `edges`, `boxes`, `Offset` |
| `PathPlanner` | shortest path in polygon corridors | `pathplan/route__c`, `shortest__c`, `solvers__c`, `util__c` | `tris`, `pnls`, `pnlps`, `dq`, `ops_*`, `opn_*` |

### `LayoutException`
Unchecked, replaces both the `longjmp` paths (REFACTOR-SMETANA A4) and the
`UNSUPPORTED` throws; carries the pass name and, when available, the offending
node/edge names — i.e. what `SmetanaDebug.safeName()` + stack reading provide manually
today.

---

## 8. The PlantUML integration, before / after

Before (today, `CucaDiagramFileMakerSmetana`):

```java
lock.lock();                                        // global JVM lock
final Globals zz = Globals.open();                  // global state
final ST_Agraph_s g = agopen(zz, new CString("g"), zz.Agdirected, null);
final ST_Agnode_s n = agnode(zz, g, new CString(uid), true);
agsafeset(zz, n, new CString("width"), new CString("" + (node.getWidth() / 72)), ...);
agsafeset(zz, e, new CString("label"), createHackInitDimensionFromLabel(w, h), ...);
gvLayoutJobs(zz, gvContext(zz), g);
// read-back: casts, BoxInfo, ED_spl via SmetanaEdge, YMirror, getSmetanaMinMax()
```

After:

```java
final Graph g = new Graph();
final Node n = g.newNode(uid).setSize(new Size(node.getWidth(), node.getHeight()));
final Edge e = g.newEdge(n1, n2).setMinLength(len - 1)
                .setLabel(Label.ofSize(labelDim));
final Cluster c = g.newCluster(clusterId).setLabel(Label.ofSize(titleDim));

final LayoutResult r = new DotLayoutEngine(
        new LayoutOptions().rankdir(rankdir)).layout(g);

r.box(n);          // node placement — replaces BoxInfo.fromNode + YMirror
r.spline(e);       // replaces SmetanaEdge's ED_spl read
r.labelPosition(e);
r.box(c);          // replaces the ST_Agraphinfo_t cast in drawGroup
r.boundingBox();   // replaces getSmetanaMinMax()
```

Concrete wins for PlantUML:
- the `ReentrantLock` and `Globals.open/close` disappear (plantuml-server, MCP server,
  worker threads);
- the `_dim_` hack, the `/72`, `YMirror` and `BoxInfo` disappear;
- `r.ranking()` opens features that are impossible today (e.g. aligning PlantUML
  swimlane-like constructs on ranks, or explaining a layout in `diagram_explain`);
- nested layouts (composite states, `GroupMakerStateSmetana`) become two independent
  engine instances instead of sequential locked passes;
- S2 layout fingerprints (REFACTOR-SMETANA phase 0) are `LayoutResult` +
  snapshot `toString()`s — the test oracle and the public API are the same thing.

---

## 9. What has no equivalent in the target (deleted, not ported)

| Today | Why it disappears |
|---|---|
| `gen.lib.gvc.*` (`gvContext`, `gvplugin`, `gvtextlayout` plugin lookup) | Graphviz's dynamic plugin machinery; in Java the "plugins" are just the classes of §7 |
| `gen.lib.cdt.*`, `FieldOffset`, `ST_dt*` | replaced by `java.util` maps inside `Graph` (REFACTOR-SMETANA B3) |
| `smetana.core` emulation (`CString`, `CArray*`, `__ptr__`, `__struct__`, `CFunction`, `Memory`, `jmp_buf`, `ZType`, `size_t`) | replaced by `String`, collections, interfaces, exceptions |
| `Macro` | accessors → model methods; geometry → `smetana.geom`; constants → enums; dt* → gone |
| `agerror__c`, `emit__c` (mostly stubbed today) | `LayoutException` + the caller's own rendering |
| `Globals` | split across `LayoutOptions`, pass instances, `Graph` internals |
| The `h.*` `ST_*` naming | `smetana.model` names; keep `@Original` on methods still mirroring C functions for upstream traceability |

---

## 10. Open questions

1. **How much of `Ordering`/`VirtualStructure` to expose publicly** vs keep
   package-private with a debug accessor — exposing virtual nodes leaks pipeline
   internals into the API; a read-only snapshot decouples it, but freezes vocabulary.
2. **`Node` immutability after layout**: current design keeps layout results *out* of
   the model (all in `LayoutResult`), which allows re-layout of the same `Graph` with
   different options. Verify no pass genuinely needs to persist state on the model
   between passes (`ND_rank` etc. become pass-internal maps or a `LayoutState` scratch
   object keyed by node).
3. **Ports**: PlantUML does not use dot ports today (`tailport`/`headport` attrs are
   set to `none`); decide whether `Shape.port()` is in the v1 API or deferred.
4. **`pack__c`** (multi-component packing): expose `ComponentDecomposer` options
   (packing mode/margin) or keep hardwired defaults as today.
5. **Compatibility bridge**: during the transition, `CucaDiagramFileMakerSmetana` could
   target the new API while a thin adapter maps it onto the old `agopen`/`gvLayoutJobs`
   world — worth it only if the migration spans many releases; otherwise switch the
   integration once `LayoutResult` exists.
