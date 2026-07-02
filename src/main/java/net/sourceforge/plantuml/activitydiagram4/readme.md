# Directory Documentation for `activitydiagram4`

## Description

Next-generation layout engine for activity diagrams, replacing the
`activitydiagram3.ftile` rendering (the instruction/command layer of
`activitydiagram3` is reused as-is).

Goals:

- **native swimlanes**, horizontal or vertical, instead of the diagram3
  post-processing (per-lane `LimitFinder` passes, `setTranslate`, N+1 drawing
  passes through interceptors, `ConnectionCross`);
- **orientation as a parameter**: top-to-bottom (lanes as columns) or
  left-to-right (lanes as rows), see `FlowDirection`.

## Architecture

Three layers:

1. **`net.sourceforge.plantuml.real`** (existing): the monotone 1D constraint
   solver already used by teoz for sequence diagrams. Variables (`Real`),
   monotone forces (`addAtLeast`, `ensureBiggerThan`), fixpoint by relaxation
   (`compile`).
2. **The plan library** (this package): a 2D generalization — two independent
   constraint lines, one per logical axis (`PlanAxis.FLOW` /
   `PlanAxis.CROSS`). Key types:
   - `RealPlan`: the shared plan (variables factory, registries, `compile()`,
     flat drawing);
   - `RealPoint`, `RealRectangle`: positions are variables, sizes are known
     upfront (text is measured before layout);
   - `RealBand`: an interval on one axis — the native representation of a
     swimlane;
   - `RealScope`: the replacement of `FtileGeometry`, with the computation
     direction reversed: bounds are `RealMax`/`RealMin` variables *pushed* by
     the content instead of dimensions computed bottom-up;
   - `RealCorridor`: a routing channel that reserves its space (arrow + label)
     *during* the solve — fixing by construction the diagram3 bug where
     cross-lane arrow corridors are invisible to lane width computation;
   - `PlanPort`, `PlanConnection`, `ConnectionRouter`, `Route`: first-class
     connections, routed once, post-solve, with absolute coordinates.
3. **`planner`** (sub-package): mirrors `FtileFactory` so the instruction tree
   is reused. A `PlannedTile` is deliberately almost nothing (ports + scope):
   planners *declare* constraints into the shared plan, they never compute
   geometry.

## Design principles

- **Declare, don't compute.** No `calculateDimension`, no `getTranslateFor`,
  no translation caches. The plan holds the only truth.
- **Alignment by identity.** Two figures are aligned because they share the
  same `Real` (the *spine* of a `PlanContext`), never because a force pulls
  them together. Cheaper, cannot diverge, and compatible with the monotone
  solver (a bidirectional equality would create a cycle).
- **Monotone constraints only** ("X >= Y + c" plus min/max aggregations) over
  an acyclic dependency graph (content -> scope -> corridor -> band -> next
  band): convergence guaranteed. The fixpoint must not depend on the emission
  order (only the iteration count does) — explicit, tested invariant.
- **Conservative routing.** Corridors clear whole scopes rather than finding
  shortest paths; fine collision avoidance stays a post-solve concern.
- **Flat drawing.** After `compile()`, figures and routed connections are
  drawn in a single pass, in one absolute coordinate system. No recursion, no
  interceptors, no drawU/drawTranslate duality.
- **Debuggability first.** Every variable and force is named after its
  construction path; the solved plan can be dumped.

## Known open points

- The monotone solver packs everything toward the minimum: a post-solve
  cosmetic pass (`RealPlan.beautify()`) recenters elements that have slack.
- "SwimlaneWidth same" needs two solving phases (solve, measure, add minimum
  sizes, re-solve) — safe since monotone.
- Intrinsically oriented skins (hexagon branch sides, yes/no label placement)
  still need `FlowDirection` awareness; the library hides 100% of the
  positioning, not 100% of the orientation.

## History

- `activitydiagram3.ftile` (vcompact): rigid tree layout + compression +
  swimlanes as drawing post-processing.
- `activitydiagram3.gtile`: introduced named connection points (`GPoint`
  hooks — kept here as `PlanPort`/`PortDirection`) but kept the rigid
  translate-tree layout; never enabled (`USE_GTILE = false`).
- `sequencediagram.teoz`: the 1D precedent for constraint-based layout on
  `real`; also the precedent for shipping a new engine behind a pragma while
  the old one stays the default.

## Link

- [Activity Diagram (New Syntax)](https://plantuml.com/activity-diagram-beta)
