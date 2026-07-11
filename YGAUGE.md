# YGAUGE — Migration of Teoz Y positioning to the Real constraint solver

Living engineering log. Branch dedicated to the `YGauge` experiment.

## Goal

Make Y symmetric with X in the Teoz sequence diagram engine: replace the
imperative, two-pass `TimeHook`/`callbackY()` vertical positioning with
per-tile `YGauge` intervals (`[min, max]` as `Real`s), solved by the same
constraint system as X. Each tile then knows its own absolute Y and draws
itself — same philosophy as the ASCIIVERSE initiative ("each object draws
itself").

The switch is `YGauge.USE_ME` (currently `false`), guarding ~29 references
across the `teoz` package.

## The problem being solved (why USE_ME exists)

### Current architecture (USE_ME = false, the active path)

- X coordinates are solved lazily by the `Real` constraint system
  (`addConstraints()`, `ensureBiggerThan`, `compileNow`).
- Y coordinates do NOT participate in the solver. They are computed in a
  second pass at drawing time:
  - `PlayingSpace.drawUInternal` → `GroupingTile.fillPositionelTiles`
    walks tiles sequentially, accumulating `y += tile.getPreferredHeight()`
    in a `TimeHook`, pushed into each tile via `callbackY(y)`.
  - Tiles are drawn from outside with `ug.apply(UTranslate.dy(posy))`.
- A tile does not know its own Y until the callback pass. Consequences:
  - Ghost `EmptyTile(4, ...)` spacers injected before/after each
    `GroupingTile` in `TileBuilder` to fake margins (then re-removed near
    parallel tiles by `removeEmptyCloseToParallel`).
  - The whole `mergeParallel` / `TileParallel` machinery plus
    `getContactPointRelative()` adjustments to align `&` parallel messages.
  - `PlayingSpace.getPreferredHeight()` must run a full simulated draw
    into a `LimitFinder` just to know the total height.
  - `LinkAnchor`, `yNewPages()`, livebox steps all depend on the mutable
    `TimeHook` state.

### Target architecture (USE_ME = true)

- Every tile constructor receives `currentY` (the gauge of the previous
  tile) and creates its own gauge, chaining Y at build time:

  ```java
  // CommunicationTile constructor
  if (message.isParallel())
      this.yGauge = YGauge.create(currentY.getMin(), dim.getHeight());
  else
      this.yGauge = YGauge.create(currentY.getMax(), dim.getHeight());
  ```

  Parallelism becomes a property of the chaining (start at previous `min`
  instead of previous `max`) — `TileParallel`/`mergeParallel` become
  unnecessary (`mergeParallel` short-circuits with `return tiles`).
- `drawU` starts with
  `ug = ug.apply(UTranslate.dy(getYGauge().getMin().getCurrentValue()))`:
  tiles draw themselves in absolute coordinates.
- `callbackY` becomes a no-op; `getTimeHook()` throws
  `IllegalStateException` (guard to detect forgotten call sites).
- `LinkAnchor.drawAnchor` computes anchors from gauges.
- Ghost `EmptyTile`s are no longer created; `EmptyTile` constructor throws
  `UnsupportedOperationException` under USE_ME (another guard).
- The Y `Real` line already exists in production:
  `SequenceDiagramFileMakerTeoz.createMainTile` creates a `yorigin`
  `RealOrigin`, passes it to `TileArguments`, and calls
  `yorigin.compileNow()` unconditionally. Gauges are built even when
  USE_ME is off — they are just never read.

## Inventory of USE_ME references (as of 2026-07-11)

| File | Role of the guard |
|---|---|
| `YGauge.java` | The flag itself |
| `CommonTile.callbackY` | No-op under USE_ME (kills the second pass) |
| `CommonTile.callbackY_internal` | Debug trace under USE_ME |
| `CommonTile.getTimeHook` | Throws under USE_ME (guard) |
| `AbstractTile` ctor | Debug trace `CREATING <class>` |
| `AbstractTile.getYGauge` | Default throws `UnsupportedOperationException` — tiles that never implemented their gauge are caught here |
| `EmptyTile` ctor | Throws under USE_ME (spacers must disappear) |
| `TileBuilder.buildSeveral` | `currentY = tile.getYGauge()` — Y chaining |
| `TileBuilder.buildOne` | Skips the two ghost `EmptyTile(4, ...)` around groups |
| `GroupingTile` ctor | Y chaining inside group body |
| `GroupingTile.drawU` | Absolute drawing of header + children (no dy(h) accumulation); background/else/notes path differs |
| `GroupingTile.drawBackground` | Same accumulation vs absolute split |
| `GroupingTile.drawAllElses` | ypos from gauges instead of TimeHooks |
| `GroupingTile.mergeParallel` | Returns tiles unchanged under USE_ME |
| `PlayingSpace.drawUInternal` | Draws tiles without external dy translate |
| `SequenceDiagramFileMakerTeoz.createMainTile` | Debug trace `COMPILING Y` before `yorigin.compileNow()` |
| `LinkAnchor.drawAnchor` | y1/y2 from gauge midpoints instead of TimeHooks |
| `CommunicationTile.drawU` and all other tile `drawU`s (Exo, Self, Note*, Delay, Divider, LifeEvent, Notes, Note, Reference, HSpace…) | Prologue `ug.apply(UTranslate.dy(gauge.min))` |
| `ElseTile.drawU` | Currently draws nothing (parent `GroupingTile.drawAllElses` does); under USE_ME the ElseTile draws itself |

## What already works under the flag

- Gauge construction and chaining through `TileBuilder` and `GroupingTile`.
- Elegant parallel-message handling via `currentY.getMin()`.
- Absolute self-drawing prologue in most `drawU` implementations.
- `LinkAnchor` gauge-based path.
- Guards (`getTimeHook` throw, `EmptyTile` throw, `getYGauge` default
  throw) to fail fast on unported code paths.

## Known gaps / hard points (why USE_ME is still false)

1. **Liveboxes — DONE (Phase 2, 2026-07-11).** `LivingSpace.addStepForLivebox`
   and `goCreate(y)` / `goDestroy(y)` were fed exclusively from
   `callbackY_internal`, which never fired under USE_ME. Solved by keeping
   the legacy traversal and substituting the gauge min inside
   `CommonTile.callbackY` (see session log).

2. **Group margins.** The two ghost `EmptyTile(4, ...)` provided 8px of
   breathing room around each group frame. Under USE_ME nothing replaces
   them yet: the `GroupingTile` gauge is
   `YGauge.create(firstY, getPreferredHeight())` and `getPreferredHeight`
   includes `MARGINY_MAGIC`, but the equivalence with the legacy layout
   (4 above + 4 below + MARGINY_MAGIC split) must be verified pixel-wise.

3. **ElseTile geometry.** Under USE_ME, `ElseTile.drawU`:
   - does NOT apply the `UTranslate.dy(gauge.min)` prologue (probable
     unfinished bug — it would draw at y=0);
   - draws only its own label height, whereas the legacy
     `GroupingTile.drawAllElses` stretches each else section down to the
     next else / the group bottom. The stretching logic (see the `ys` list
     in `drawAllElses` and the `Blotter` in `drawCompBackground`) must be
     re-expressed with gauges: `height = nextElse.gauge.min - this.gauge.min`.

4. **Group background (`Blotter`).** `drawCompBackground` computes else
   color-change offsets from `getTimeHook()` — will throw under USE_ME.
   Needs the gauge-based ypos (same formula as `drawAllElses`).

5. **Pagination.** `PlayingSpace.yNewPages()` reads
   `newpageTile.getTimeHook().getValue()` — will throw. Replace with
   `getYGauge().getMin().getCurrentValue()`.

6. **Total height.** `PlayingSpace.getPreferredHeight` uses the
   LimitFinder simulated draw + the returned `TimeHook`. With gauges the
   height is simply `lastGauge.getMax().getCurrentValue() + margin` — but
   only after `yorigin.compileNow()`. Check call order in
   `SequenceDiagramFileMakerTeoz` (getPreferredHeight may currently be
   called via `calculateDimension` after compile — OK — but verify no
   caller needs it before).

7. **Group notes** (`GroupingTile.drawNotes`) and **TileParallel removal**:
   `PlayingSpace.drawUInternal` still calls `fillPositionelTiles` for the
   `full` list used by `LinkAnchor` lookups; under USE_ME that pass should
   be replaced by a simple recursive collection of tiles (no Y math).

8. **`message.isParallel()` semantics — DONE (pdiff report #2, 2026-07-11).**
   Contact-line sharing implemented in the gauge itself (see session log):
   `YGauge` carries `contact`/`origin` Reals, message tiles use
   `createWithContact`/`createParallel`, life events propagate.

9. **Newpage / PlayingSpaceWithParticipants.** Page splitting translates
   pages by `-yNewPage`; with absolute-Y tiles the translate composition
   must be re-verified.

## Migration plan (proposed)

- **Phase 0 — Safety net.** Vega test corpus: one `.puml` per feature
  (plain messages, self, exo, create/destroy, activate levels, notes in
  all positions, groups, nested groups, alt/else with colors, par & merge,
  delay, divider, hspace, ref, newpage, anchors/links). Generate SVG
  references with USE_ME=false. Goal: flipping the flag must produce
  pixel-identical (or explainably different) output.
- **Phase 1 — Complete the gauges.** Make every tile implement
  `getYGauge()` (remove reliance on the `AbstractTile` default throw).
  Fix `ElseTile.drawU` prologue.
- **Phase 2 — Feed liveboxes from gauges.** New pass after
  `yorigin.compileNow()`: walk tiles, call the `addStepForLivebox` /
  `goCreate` / `goDestroy` equivalents with gauge values. Delete the
  `callbackY_internal` overrides.
- **Phase 3 — Groups.** Blotter, drawAllElses, notes, margins, from gauges.
- **Phase 4 — Pagination & height.** yNewPages, getPreferredHeight without
  LimitFinder, PlayingSpaceWithParticipants translation.
- **Phase 5 — Flip & clean.** USE_ME=true, run corpus, fix diffs. Then
  delete: TimeHook plumbing in CommonTile, TileParallel + mergeParallel,
  EmptyTile spacers, fillPositionelTiles Y math, the flag itself.

## Rules of engagement

- Same discipline as SMETANA.md: no downstream workaround without
  understanding the root cause of a diff.
- One phase per commit series; the build must stay green with USE_ME=false
  at every step (the flag protects master-bound merges).
- Claude edits sources via Filesystem at `C:\github\plantuml`; Arnaud
  builds with `gradle build` and runs the visual comparisons.

## Session log

### 2026-07-11 — Fix: parallel (&) contact-line alignment (pdiff report #2)

**Symptom:** `A->B: <2-line label>` / `& B->C: <1-line label>` — under
USE_ME the two arrows were not on the same line. Known gap #8: chaining
parallel tiles on `currentY.getMin()` aligns tile TOPS, while the legacy
`TileParallel` aligns CONTACT POINTS (the arrow line,
`getContactPointRelative()`), shifting each member down by
`maxContact - ownContact`.

**Fix (solver-based, reproduces legacy semantics exactly):** the YGauge now
optionally carries two more Reals:
- `contact`: the absolute Y of the arrow line, shared by all members of a
  parallel group;
- `origin`: the chaining origin of the group (max of the gauge preceding
  the group head) — the group top, above which no member may start.

Three new factories in `YGauge`:
- `createWithContact(currentY, contactRelative, height)` — used by every
  message tile (Communication, Self, Exo). The min FLOATS:
  `contact = origin.addAtLeast(contactRelative)`, `min = contact - c`.
  Without any & follower, solver minimization gives `min = origin`:
  strictly equivalent to the old sequential `create(currentY.getMax(), h)`.
  With a & follower having a taller label, the shared contact line is
  pushed down and this tile follows — exactly the legacy behavior where
  the group head itself gets shifted down.
- `createParallel(currentY, contactRelative, height)` — used by & tiles:
  `contact.ensureBiggerThan(origin + c)` then `min = contact - c`. The max
  accumulates (`RealUtils.max(currentY.getMax(), min + h)`) so the next
  sequential tile chains below the WHOLE group, like
  `TileParallel.getPreferredHeight()`. Falls back to top alignment when
  the previous gauge has no contact line.
- `createPropagating(currentY, height)` — used by `LifeEventTile`:
  forwards contact/origin so an activate/deactivate between two & messages
  does not break the sharing (legacy `moveRecentParallelTilesToPending`
  skips LifeEventTiles the same way).

The solver minimization plays the role of the legacy
`yPointAll = max(c_i)` computation — constraints are DAG-forward, no
cycle, so `RealLine.compile()` terminates.

**Touched:** `YGauge`, `CommunicationTile`, `CommunicationTileSelf`,
`CommunicationExoTile`, `LifeEventTile`.

**Legacy (USE_ME=false) impact to re-verify with pdiff:** the Y RealLine
now contains a few more Reals and `ensureBiggerThan` constraints even in
legacy mode (values still unread there). Must stay pixel-identical; only
compile cost changes marginally.

**Remaining parallel gaps (logged, not fixed):**
- note wrappers around a & message still chain on `currentY.getMax()`
  without contact sharing (note on parallel message → misalignment);
- `GroupingTile.bodyHeight` and `PlayingSpace.drawUInternal` finalY sum
  `getPreferredHeight()` over ALL tiles, so parallel members inside a
  group are double-counted (frame too tall / extra bottom space). To be
  reworked in Phase 3/4 with gauge-based heights.

### 2026-07-11 — Fix: missing self-translate prologue (pdiff report #1)

**Symptom (reported by pdiff, `...delay...` test case):** with USE_ME=true,
delay texts were all drawn at the top of the diagram, overlapping the first
message, while the vertical space was correctly reserved (arrows at the
right Y).

**Root cause:** under USE_ME each tile must translate itself to its
absolute gauge position (`ug.apply(UTranslate.dy(gauge.min))`), since
`PlayingSpace`/`GroupingTile` no longer apply the external `dy(posy)`.
`DelayTile.drawU` computed `ypos` from the gauge for `delayOn(...)` but
never applied it to the `ug` used to draw the component — the tile had
never been ported to absolute self-drawing. Systematic review found the
same missing prologue in six more `drawU` implementations.

**Fixed (prologue added):** `DelayTile`, `DividerTile`, `ReferenceTile`,
`NewpageTile`, `CommunicationTileNoteLeft`, `CommunicationTileSelfNoteLeft`
(for the two wrappers: prologue applied after the inner tile draws itself,
only for the note — same pattern as `CommunicationTileNoteRight`), and
`ElseTile` (label strip now at the right Y; height stretching still
Phase 3).

**Verified as already correct:** `CommunicationTile`, `Self`, `Exo`,
`NoteTile`, `NotesTile`, `LifeEventTile`, `NoteRight`, `SelfNoteRight`,
`NoteTop`, `NoteBottom` — note that for `NoteTop` the composition of the
wrapper's extra `dy(noteHeight + spacey)` with the inner tile's absolute
self-translate is correct only because inner gauge min == wrapper gauge min
(both created from the same `currentY.getMax()`); this invariant matters.

**Lesson:** "reserves space correctly but drawn at the top" is the
signature of a missing self-translate prologue in that tile's `drawU`.

### 2026-07-11 — Phase 2: liveboxes fed from gauges

**Design decision.** Instead of a new dedicated pass duplicating the livebox
math, the existing traversal is reused: `fillPositionelTiles` already runs
under USE_ME (it builds the `local`/`full` lists in
`PlayingSpace.drawUInternal`). `CommonTile.callbackY` now, under USE_ME,
**ignores the accumulated TimeHook and substitutes the min of the tile's own
YGauge** before running the normal path. Benefits:
- all `callbackY_internal` overrides (CommunicationTile, Self, Exo,
  LifeEventTile) run unchanged — arrowY offsets, activate/deactivate
  points, `goCreate`/`goDestroy`, and the `y += 5.0` collision hack in
  `LiveBoxes.addStep` are preserved exactly;
- the note wrappers work for free: their `callbackY_internal` forwards
  `tile.callbackY(y)` and the inner tile substitutes its own gauge;
- call ordering (document order, group recursion) is identical to legacy,
  and repeated layout passes stay idempotent (TreeMap/HashMap semantics,
  same as legacy);
- timing is safe: `fillPositionelTiles` only runs after
  `yorigin.compileNow()`, so `getCurrentValue()` is valid.

**Changes:**
- `CommonTile.callbackY`: gauge substitution under USE_ME (see comment in
  code). `callbackY_internal` debug trace removed.
- `CommonTile.getTimeHook`: the `IllegalStateException` guard is REMOVED.
  Rationale: `this.y` is now gauge-consistent by construction, so legacy
  consumers not yet migrated (Blotter offsets in `drawCompBackground`,
  `yNewPages`, `DelayTile` legacy branch) keep producing correct values —
  TimeHook becomes a temporary bridge carrying gauge values. This unblocks
  pdiff runs on the full corpus (with the guard, any `alt/else` diagram
  crashed before rendering). TimeHook is deleted entirely in Phase 5.
- `AbstractTile`: `CREATING` debug trace removed (stderr spam in batch
  runs; re-add locally if needed).
- `NewpageTile`: missing `yGauge` added (Phase 1 leftover; mandatory now
  that `callbackY` reads every tile's gauge). `TileMarged` still has no
  gauge and no `currentY` — it does not seem to be built by `TileBuilder`;
  if it is ever instantiated under USE_ME it will throw via the
  `AbstractTile.getYGauge` default. To investigate if it fires.
- `PlayingSpace`: the gauge chain now starts at `startingY` (8) instead of
  0, so gauge space == legacy TimeHook drawing space (otherwise the whole
  USE_ME rendering would be shifted 8px up vs the pdiff references).

**Expected pdiff status when flipping USE_ME=true after this phase:**
- plain messages, self, exo, activation bars, create/destroy: should be
  close to reference (liveboxes now positioned);
- groups: known diffs remain — missing 4px EmptyTile margins, no
  background Blotter (`GroupingTile.drawU` USE_ME branch skips the
  `isBackground()` path entirely), else sections not stretched
  (`ElseTile.drawU` also misses its `dy(gauge.min)` prologue). All Phase 3.
- parallel (`&`) messages: tops aligned instead of contact points —
  arrow lines may shift when labels differ in height. Phase 3 decision.

### 2026-07-11 — Archaeology session

- Full inventory of the 29 `USE_ME` references (table above).
- Identified the intent: single-pass, solver-based Y, tiles drawing
  themselves in absolute coordinates; parallel messages via
  `currentY.getMin()` chaining.
- Identified the blocking gaps: liveboxes fed only by `callbackY_internal`,
  ElseTile stretching/Blotter, pagination, group margins.
- Noted that `yorigin` is created and compiled unconditionally in
  production — the Y Real line is live but unread when USE_ME=false.
