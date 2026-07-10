# ASCIIVERSE.md — Moving ASCII sequence diagram rendering onto Teoz

This is a living engineering log, in the same spirit as `SMETANA.md`. It records
the design decisions, the reasoning behind them, and the open questions for the
project of retiring the ASCII rendering dependency on the old Puma engine
(`SequenceDiagramFileMakerPuma2`, `DrawableSet`, `DrawableSetInitializer`, ...)
in favor of Teoz (`SequenceDiagramFileMakerTeoz`, `Tile`, `PlayingSpace`, ...).

## 1. The problem

We want to delete the old Puma rendering engine for sequence diagrams and rely
solely on Teoz. However `SequenceDiagramTxtMaker` (the ASCII/UTXT renderer)
still depends on Puma-only classes, most notably `DrawableSet` and
`DrawableSetInitializer`. These classes cannot simply be deleted without
breaking ASCII output.

The naive fix — "just make `SequenceDiagramTxtMaker` call into Teoz like the
graphical renderers do" — does not work directly, because Puma and Teoz live
in two fundamentally different coordinate worlds.

## 2. The two coordinate worlds

**Puma / ASCII world (today):**
- Entirely grid-first. `TextStringBounder` returns dimensions in *character
  cells* (a string's height is `1`, not a pixel height).
- `DrawableSet` / `DrawableSetInitializer` compute the whole layout themselves,
  directly in integer character coordinates.
- `UGraphicTxt.draw()` divides incoming pixel translations by `10` to fall
  back onto a character grid — a leftover of the fact that some of the
  drawing primitives are shared with the pixel pipeline, but the actual
  `ComponentText*` classes (`ComponentTextArrow`, `ComponentTextParticipant`,
  etc., in package `asciiart`) already return `getPreferredHeight()` /
  `getPreferredWidth()` **directly in character cells** when fed a
  `TextStringBounder`. There is no pixel measurement involved once that
  bounder is in place.

**Teoz world (today):**
- Entirely pixel-first for the X axis. Each `LivingSpace` position is a `Real`,
  a variable in a real constraint solver (`addConstraints()` declares
  relations like `point2.ensureBiggerThan(point1.addFixed(width))`, and
  `xorigin.compileNow()` resolves the whole system to concrete pixel values
  such as `137.4`).
- The X placement of a living line is therefore the *output* of a global
  pixel-based constraint solve, using real font metrics.

Quantizing the compiled `Real` pixel values onto a character grid after the
fact (`column = round(pixelX / 10)`) is a trap: it produces overlaps and
misaligned columns, because two participants close in pixels can still need
to be at least one full cell apart, and because character widths (CJK,
emoji, `Wcwidth`) don't scale linearly with pixel widths. This is exactly
what made a naive "reuse Teoz's pixel output for ASCII" approach fail.

## 3. Key finding: the Y axis is not actually a `Real` problem

Unlike X, Teoz's vertical placement is **not** driven by the `Real` solver.
`YGauge.USE_ME` is `false`: the `YGauge`/`Real`-based vertical machinery is
present in the code but dead. In practice:

- `AbstractTile.getYGauge()` throws `UnsupportedOperationException` and is
  never called on the live path.
- The real vertical position is a plain `double` carried by `TimeHook`
  (`CommonTile.y`), and it is produced by a simple sequential accumulation:
  `PlayingSpace.drawUInternal()` calls
  `GroupingTile.fillPositionelTiles(stringBounder, new TimeHook(startingY), tiles, ...)`,
  which walks the tile list once, in order, handing each tile a running
  `TimeHook` and advancing it by that tile's `getPreferredHeight()`.

So Y is already "additive stacking", not a global constraint solve. There is
no analogous quantization trap: if the *heights themselves* are already
expressed in character-cell units, the same stacking loop needs no
conversion, no rounding, and no possibility of overlap.

This means the height values matter more than the stacking mechanism: as long
as `getPreferredHeight()` (or its ASCII equivalent) returns whole line counts,
the existing `fillPositionelTiles`-style loop can be reused almost verbatim
for the ASCII path — just fed with a `TextStringBounder` instead of a real
font metrics bounder.

## 4. The "10.0 = 1 character" convention

`UGraphicTxt.draw()` divides pixel translation by `10` to land on the
character grid. This convention already exists in PlantUML and is not an
invention for this project. However, it should **not** be used to convert
Teoz's pixel-measured Y values into ASCII line counts. Two regimes are
possible:

- **Regime A (rejected):** let Teoz measure heights with the real graphical
  font metrics, then divide the result by 10 and round. This reintroduces
  quantization error, because a graphically-measured arrow height is not
  guaranteed to be a clean multiple of 10.
- **Regime B (chosen):** inject `TextStringBounder` into the Teoz pipeline so
  that `getPreferredHeight()` / `getPreferredWidth()` come out already
  expressed in character cells — no pixels involved, hence nothing to
  divide or round. This is consistent with how the existing `asciiart`
  `ComponentText*` classes already behave.

The `10 px = 1 cell` constant remains useful only for Teoz's **hardcoded
pixel constants** that have no `StringBounder`-derived equivalent (e.g.
`CommunicationTile.LIVE_DELTA_SIZE`, `PlayingSpace.startingY`, the `(5,5)`
offsets in `getTextBlock()`). These should eventually get dedicated ASCII
constants rather than being derived from the pixel ones by division.

## 5. The plan: `Tile extends AsciiBlock`, powered by `Real` where it matters (X) and by simple stacking where it doesn't (Y)

The `asciiverse` package (`AsciiBlock`, `InfinitePlan`, `InfiniteString`,
`ATable`, `ATranslate`, `AsciiSnake`) is an already-existing, self-contained,
grid-first ASCII drawing model, entirely decoupled from `UGraphic` / pixels /
`Real`. `ATable` is a complete, working implementation of `AsciiBlock`.
`AsciiSnake` (meant for arrows/lifelines) is currently an empty skeleton.

The plan is:

1. **`Tile extends AsciiBlock`.** Add the `AsciiBlock` contract
   (`asciiDimension()`, `asciiDraw(InfinitePlan)`) to the `Tile` interface.
   `AsciiBlock` provides `default` methods that throw
   `UnsupportedOperationException`, so this compiles immediately without
   touching any of the ~30 existing `Tile` implementations. Each concrete
   tile (`CommunicationTile`, `TileParticipant`-equivalent, `NoteTile`,
   `GroupingTile`, ...) can then be migrated incrementally, exactly the way
   `AsciiSnake` is already a stub waiting to be filled in.

2. **Reuse the Teoz tile tree as topology, not as geometry.** The tile tree
   built by `TileBuilder` already encodes the correct structure of the
   diagram: which living spaces exist, in what order events occur, which
   messages/notes/groupings nest inside which, anchors, parallel messages,
   etc. This structure is exactly what the ASCII renderer needs and does not
   need to be rebuilt. What must **not** be reused is the tile tree's pixel
   geometry (`Real` X positions, graphically-measured heights).

3. **X axis: a native ASCII column solver.**
   Teoz's `Real` constraint solver is pixel-only and must not be quantized
   after the fact (see §2). Instead, X placement for ASCII needs its own
   solver operating directly in integer columns: each tile declares a minimal
   ASCII width via `asciiDimension()` (e.g. `CommunicationTile`'s ASCII width
   = label length in cells + arrowhead), and a column-assignment pass (the
   ASCII analogue of `addConstraints()` + `compileNow()`, akin to what
   `DrawableSetInitializer` used to do, and structurally similar to how
   `ATable` derives column widths from `columnMinWidths`) assigns each living
   space an integer column. Tiles then draw relative to resolved columns,
   never by reading `getMinX()`/`getMaxX()` (`Real`) directly.

4. **Y axis: reuse the existing sequential stacking, fed with cell-based
   heights.**
   Because `YGauge.USE_ME == false` already made Y a simple running
   `TimeHook` accumulation (§3), the same `fillPositionelTiles`-style loop can
   drive the ASCII layout, provided the heights it accumulates are already in
   character-cell units (Regime B, §4) — i.e. the `StringBounder` used during
   the ASCII pass must be `TextStringBounder`, not a graphical font-metrics
   bounder. No new vertical solver is needed. Two details to watch:
   - `getContactPointRelative()` / `getZZZ()`: the exact row where an arrow
     "touches" must land on a whole line; for parallel/grouping tiles with
     an offset contact point, the rounding rule needs to be decided
     explicitly.
   - Parallel tiles (`TileParallel`, `message.isParallel()`) share the same
     vertical band in pixels; in ASCII, "same row" is literal — need to make
     sure they don't silently overwrite each other in `InfinitePlan`
     (`drawChar` overwrites without warning).

5. **Output path.** `SequenceDiagramFileMakerTeoz.getTextBlock()` currently
   returns a pixel-world `TextBlock`. The ASCII path needs a distinct exit
   point that walks the (ASCII-augmented) tile tree and calls `asciiDraw()`
   on an `InfinitePlan`, then exports it as text — rather than going through
   `getTextBlock()` at all.

## 6. Summary of responsibilities

| Axis | Source of truth today | What ASCII reuses | What ASCII must rebuild |
|------|------------------------|--------------------|--------------------------|
| Topology (who/what/order) | Teoz tile tree (`TileBuilder`) | Reused as-is | — |
| X (columns) | `Real` constraint solver, pixel-based | Nothing (must not quantize `Real` output) | A native integer column solver, fed by `asciiDimension()` |
| Y (rows) | Plain sequential `TimeHook` stacking (`YGauge.USE_ME == false`) | The stacking loop itself | Only the height *values* — must come from `TextStringBounder` (cell units), not graphical metrics |

## 7. Open questions

- **`TimeHook` reuse vs. a separate ASCII channel.** Should `TimeHook`/
  `callbackY` be reused directly with its unit reinterpreted as "lines"
  instead of "pixels" (simpler, but mixes two semantics in one class), or
  should there be a parallel `asciiCallbackY(int line)` channel to keep the
  two worlds strictly separate? **Still open** — not yet needed, because the
  current implementation (see §9) drives Y with a plain sequential counter
  in `PlayingSpaceWithParticipants.asciiDraw()`, not with `TimeHook` at all.
- ~~Where exactly is the `StringBounder`/`skin` injected today...~~
  **Resolved, see §9.1 and §9.2** — the `StringBounder` question turned out
  to be a non-issue (already correct by construction); the `skin` question
  turned out to be more radical than expected (not substitutable at all).
- **Order of migration for concrete `Tile` implementations** — confirmed in
  practice: participant heads → message (`CommunicationTile`) done first.
  Still to come: self-message → note → grouping → reference/delay/divider.
- **`AsciiSnake` implementation** — still an unimplemented skeleton, and
  still not used. `CommunicationTile.asciiDraw()` ended up **not** needing
  it: the arrow/lifeline drawing logic was written directly on the tile
  (see §9.3) rather than factored out first. Whether to extract a shared
  `AsciiSnake` implementation once more tile types need arrows/lines is
  still open.
- **Rounding rule for contact points** on tiles whose contact point is not at
  a natural line boundary (parallel groupings, notes with offset anchors).
  Still open — not yet encountered, since only the simple non-parallel
  `CommunicationTile` case has been implemented so far.
- **New: how does the arrowhead interact with the target lifeline?**
  Resolved for the simple case, see §9.3 — but the rule ("arrowhead sits
  one column before the target's lifeline column, the lifeline character
  itself is left for the lifeline-drawing pass to fill in") was arrived at
  empirically by comparing rendered screenshots, not derived up front. It
  will need re-checking once self-messages, multicast, or `create` messages
  (which draw a participant head instead of touching a lifeline) are
  migrated.
- **New: real column solver — now built (X only).** The ad hoc linear X
  layout described below is **gone**, replaced by a real `Real`-based ASCII
  column solver (see §14). The Y axis is still the flat `y += 3` counter;
  the `fillPositionelTiles`-style Y recursion is the remaining piece.

## 8. Rejected idea: `XDimensionReal2D`

While discussing the column solver (§5 point 3, §7), the idea came up of
giving `AsciiBlock.asciiDimension()` a new return type, `XDimensionReal2D`
— a `Real`-based counterpart to `XDimension2D` (`Real width`, `Real height`
instead of `double width`, `double height`), so that ASCII dimensions could
participate in a `Real` constraint graph the same way pixel positions do
today.

**Rejected**, after rereading `RealUtils`/`RealLine`/`AbstractReal`/`RealMax`:
a `Real` is not a generic lazy numeric value — it is specifically a *position
on a shared axis* (`AbstractReal` registers itself on a `RealLine` at
construction; `RealLine.compile()` computes a global `min`/`max` across every
`Real` registered on it; `RealUtils.max(...)` requires all its arguments to
share the same `RealLine`). `PositiveForce` — the only constraint `Real`
knows how to express — is a minimum-distance relation *between two points*,
not a property of a size. A width or a height is a quantity, not a point on
an axis, and forcing it into `Real` would mean registering it on some
`RealLine` for no reason, polluting that line's global `min`/`max` with
values that have no business being compared against positions.

The correct symmetry (confirmed by rereading how pixel Teoz already works):
sizes are, and remain, plain numbers (`double` in the pixel world,
presumably `int` in the ASCII world) — they are *inputs* to constraints
(`point2.ensureBiggerThan(point1.addFixed(width))`, where `width` is a
`double`). Only *positions* (`LivingSpace.getPosB/C/D`) are `Real`. So:
`asciiDimension()` keeps returning a plain integer-based dimension type
(`XDimension2D` continues to be reused, or a future `int`-based equivalent —
not decided yet, see §7), and if/when the ASCII column solver from §5 point
3 is built, it is the **columns** (positions) that should be expressed as
`Real` on a dedicated ASCII `RealLine`/`RealOrigin` — separate from the pixel
one, with the convention "1.0 = 1 character" — never the dimensions
themselves.

**Update:** the "not decided yet" part above is now decided. Arnaud added
`asciiverse.ADimension2D` — a plain `int width`/`int height` value type,
API-compatible in spirit with `XDimension2D` (`delta`, `withWidth`,
`mergeTB`, `mergeLR`, `atLeast`, `max`), but never reusing `XDimension2D`
itself (which is `double`-based and lives in the pixel `klimt.geom`
package). `AsciiBlock.asciiDimension()` now returns `ADimension2D`, and
`Participant.asciiDimension()` / `SequenceDiagramFileMakerTeoz.getAsciiBlock()`
were updated accordingly. This closes the loop begun in this section: sizes
in the ASCII world are plain integers, never `Real`, and now they have their
own proper type instead of borrowing the pixel one.

## 9. Status: first working slice — `alice -> bob: this is a test`

Unlike the "design discussion only" status this doc previously recorded,
there is now a first working, compiling, rendering slice. It intentionally
cuts corners flagged as still-to-do in §7 (originally no real column
solver — since built, see §14; still no `AsciiSnake`, Y is a flat counter,
only the simplest non-parallel, non-`create`, non-multicast
`CommunicationTile` case). It is meant as a
walking skeleton to validate the overall shape of the plan (§5) against a
real test (`zdev/Test_0.java`), not as the final architecture for any of the
individual pieces.

### 9.1 What actually got built, file by file

- **`AsciiBlock`** (`asciiverse`): both `asciiDimension()` and `asciiDraw()`
  now have `default` implementations that throw `UnsupportedOperationException`
  (originally only `asciiDimension()` did — `asciiDraw()` was abstract, which
  would have broken the "compiles without touching existing implementations"
  promise of §5 point 1).
- **`Tile extends AsciiBlock`** — done, exactly as planned in §5 point 1.
  Compiles with zero changes to any of the ~30 existing `Tile`
  implementations.
- **`Participant implements AsciiBlock`** — the participant now draws its
  own ASCII head (`asciiDraw()`: the box + its code) and reports its own
  ASCII footprint (`asciiDimension()`: `code.length() + 2` wide, 3 tall),
  the same way `CommunicationTile` draws its own message. This is the same
  object-oriented principle applied consistently, one object at a time:
  **each object is responsible for drawing itself**, rather than some
  central function (`PlayingSpaceWithParticipants.asciiDraw()`) knowing the
  internal drawing details of every other type. `PlayingSpaceWithParticipants`
  no longer computes box width from `label.length() + 2` inline nor calls
  `plan.drawBox()`/`plan.drawString()` for heads directly — it just asks
  each `Participant` for its `asciiDimension()` (to lay out columns) and
  then calls `p.asciiDraw(plan.move(boxLeft.get(p), 0))` (to draw it),
  exactly mirroring how it already treats `CommunicationTile`. This is the
  same pattern as `drawU()` in the pixel world, where `LivingSpace.drawHead()`
  and `CommunicationTile.drawU()` each draw themselves and the orchestrating
  code (`PlayingSpaceWithParticipants.drawU()`) never reaches into another
  class's drawing internals.
- **`Component extends AsciiBlock`** — discussed as a possible mirror move
  (so that `Rose` arrow/participant components could eventually get their
  own `asciiDraw()`, keeping `CommunicationTile.drawU()`'s pattern of
  delegating to `getComponent()` symmetric between pixel and ASCII) but
  **not implemented**. In practice `CommunicationTile.asciiDraw()` (§9.3)
  computes its ASCII geometry directly, without going through
  `getComponent()`/`Rose` at all. Whether to revisit the `Component extends
  AsciiBlock` idea once more component types need ASCII rendering is open.
- **`StringBounder` injection (§7 resolved)** — turned out to already be
  correct with zero code changes. `SequenceDiagram.exportTxt()` builds
  `new FileFormatOption(FileFormat.ATXT)`, which flows into
  `SequenceDiagramFileMakerTeoz`'s constructor
  (`fileFormatOption.getDefaultStringBounder(...)`), which delegates to
  `FileFormat.getDefaultStringBounder()` — already returning
  `new TextStringBounder(this)` for `ATXT`/`UTXT`. So every `stringBounder`
  used inside `SequenceDiagramFileMakerTeoz.createMainTile()` (`LivingSpace`
  positions, `TileArguments`, `Dolls`, `PlayingSpace`, `addConstraints()`...)
  is already a `TextStringBounder` when exporting ASCII. Confirmed:
  `TextStringBounder.calculateDimension()` returns `(text.length(), 1)` —
  pure character count, no font involved.
- **`skin` (`Rose`) injection (§7 resolved, negatively)** — unlike the
  `StringBounder`, this is **not** just a matter of injecting a different
  bounder. `AbstractComponentRoseArrow` (used by `CommunicationTile`'s
  arrow) hardcodes pixel constants independent of any `StringBounder`
  (`arrowDeltaX = 10`, `arrowDeltaY = 4`, `getPaddingY() = 4`, a
  `ClockwiseTopRightBottomLeft.topRightBottomLeft(1, 7, 1, 7)` padding).
  Feeding `Rose` components a `TextStringBounder` would only fix the *text*
  measurement inside `getPreferredDimension()`; the surrounding
  padding/arrow-head geometry would still come out in pixels, mixed in the
  same result. Conclusion: ASCII tiles must **not** call
  `getComponent(stringBounder).getPreferredDimension(...)` for layout —
  they compute their own cell-based geometry from scratch. This is the
  reason `Component extends AsciiBlock` was shelved (above) rather than
  pursued as the vehicle for ASCII arrow drawing.
- **`LivingSpace` ASCII positions** — the pixel `posB/C/D/A/E` (all `Real`)
  now have exact ASCII counterparts `getAsciiPosB/C/D/A/E()` (also `Real`,
  but on the dedicated ASCII `RealLine`, convention 1.0 = 1 char). The
  earlier throwaway `int asciiLifeColumn` field is gone; `getAsciiLifeColumn()`
  is now `(int) getAsciiPosC().getCurrentValue()` and a new
  `getAsciiLeftColumn()` returns `(int) getAsciiPosB().getCurrentValue()`.
  See §14 — this is what replaced the flat linear column pass.
- **`PlayingSpace.getTiles()`** — new accessor exposing the `List<Tile>`
  built by `TileBuilder`. This is what let `PlayingSpaceWithParticipants`
  stop walking `diagram.events()` directly and instead iterate the real
  Teoz tile tree (§5 point 2), even though only `CommunicationTile` is
  currently handled (`instanceof` filter; everything else is silently
  skipped for now).
- **`InfinitePlan`** — added `drawString(String, x, y)` and
  `drawBox(left, top, width, height)` (frame only, no label — label drawing
  was deliberately kept as a separate call so `drawBox` stays reusable for
  notes/groupings later).

### 9.2 `PlayingSpaceWithParticipants.asciiDraw()` — current shape

Builds and compiles the ASCII column graph (§14), then delegates all
drawing to the objects themselves: each `Participant` draws its own head at
its resolved left column (`p.asciiDraw(plan.move(livingSpaces.get(p).getAsciiLeftColumn(), 0))`
— see §9.1), each `CommunicationTile` draws its own message via
`tile.asciiDraw(plan.move(0, y))`, advancing `y` by a fixed 3 rows per
message (label row, arrow row, blank row). Lifelines (`|`) are filled in
**after** all messages are drawn, at each participant's resolved lifeline
column (`getAsciiLifeColumn()`), only into cells still empty — so a tile's
own drawing (arrowhead, dashes) is never overwritten by the lifeline pass,
only complemented. The method no longer computes any geometry itself: the
old `boxLeft` map, the `gap`/`maxMessageLabelWidth()` heuristic, and the
`setAsciiLifeColumn` calls are all gone.

This Y handling (flat `y += 3` per message) is a placeholder for the
`fillPositionelTiles`-style stacking described in §5 point 4 — it works
for a single message but does not yet size itself from each tile's real
ASCII height, and does not yet handle notes/groupings/newpages at all.

### 9.3 `CommunicationTile.asciiDraw()` — current shape

Draws relative to the plan (the caller translates the plan's row origin to
the tile's row before calling): row 0 is the label (centered between the
two lifeline columns), row 1 is the arrow. Columns come from
`livingSpace1/2.getAsciiLifeColumn()` — never from `getPosC()`/`Real`.

The arrow/lifeline junction was tuned empirically against rendered
screenshots (three iterations) to reach the following rule, which differs
slightly between the two ends of the arrow:

- Dashes fill only the **open interval** between the two columns
  (`from + 1` to `to - 1`), never the columns themselves — so the tile
  never overwrites either lifeline's `|` directly.
- The arrowhead (`>` or `<`) is drawn **one column before** the target
  lifeline column (`xb - 1` for a left-to-right arrow, `xb + 1` for a
  right-to-left one) — not on the target column itself. The target's own
  lifeline cell is then left empty by the tile and is filled in afterwards
  by `PlayingSpaceWithParticipants`'s lifeline-drawing pass (§9.2), so the
  final row reads e.g. `|--------------->|` with the target's `|` supplied
  by the lifeline pass, not by the arrow.
- The source lifeline column is left untouched by the same mechanism
  (dashes start one column after it), so the source's `|` is likewise
  supplied by the lifeline pass rather than by the tile.

This convention (arrowhead stops short of the target column, lifeline pass
fills both ends) has only been validated for the simple, non-`create`,
non-multicast, non-parallel case. It will need re-examination for:
- `create` messages, where the target has no prior lifeline to fill in and
  a participant head must be drawn instead (mirroring `drawU`'s
  `livingSpace2.drawHead(...)` branch);
- multicast messages (`drawMulticast` in the pixel path);
- self-messages (`CommunicationTileSelf`), not yet migrated.

### 9.4 Expected output after the latest edit (not yet re-confirmed)

For:
```
@startuml
alice->bob: this is a test
@enduml
```
rendered to `FileFormat.ATXT` via `zdev/Test_0.java`, the output validated a few
iterations ago (arrowhead drawn directly on the target column, i.e. `xb`) was:
```
,-----.                ,---.
|alice|                |bob|
`-----'                `---'
   |   this is a test    |
   |--------------------->
   |                     |
```
Arnaud then changed the arrowhead position by hand to `xb - 1` / `xb + 1`
(one column before the target's lifeline, rather than on it). Recomputing
with `xa = 3`, `xb = 25`: dashes now fill columns `4..23` (20, one fewer
than before, since the old dash at column 24 is what the arrowhead now
occupies), the arrowhead sits at column 24, and column 25 — left untouched
by the tile — is filled in afterwards by the lifeline pass. This changes
the rendered row: the target's `|` now reappears immediately after the
arrowhead, which it previously did not:
```
,-----.                ,---.
|alice|                |bob|
`-----'                `---'
   |   this is a test    |
   |-------------------->|
   |                     |
```
This has been derived from reading the code, not from re-running the test.
Worth confirming against a fresh `outputdev/Test_0.atxt` before trusting it
as the new baseline.

### 9.5 Next steps

In likely order: self-message (`CommunicationTileSelf.asciiDraw()`) →
replace the ad hoc column layout in §9.2 with the real `Real`-based ASCII
column solver from §5 point 3 (needed as soon as more than one message with
different label widths must share the gap between two participants) →
note (`NoteTile`) → grouping (`GroupingTile`) → reference/delay/divider →
revisit `TimeHook`/`AsciiSnake` factoring once enough tile types are
migrated to see what they actually have in common.

## 10. End-to-end test harness: `VegaTest` / `hello1.puml`

Up to now the only test exercising the ASCII path was the manual
`zdev/Test_0.java` scratch test. The proper, checked-in test harness for the
whole project is `test.vega.VegaTest` (data-driven: every `.puml` file under
`src/test/resources/vega/**` is a test case, with an optional YAML header
controlling the `FileFormat` and expected assertions, and an
`allow-failure: true`/no-header fallback for exploratory files).

`src/test/resources/vega/asciiverse/hello1.puml` already existed (`output:
txt`, `alice->bob: this is a test` / `return ok`) as the intended model for
an ASCII test, but nothing wired it up:

- **`VegaInputFile.getFileFormats()`** parses the YAML `output:` value and
  maps short aliases to `FileFormat` (`"SVG"` → `SVG_DETERMINISTIC`,
  `"LATEX"` → `LATEX_DETERMINISTIC`, else `FileFormat.valueOf(trimmed)`).
  `"txt"` doesn't match any real enum constant (`ATXT`/`UTXT`, not `TXT`),
  so `hello1.puml` would have thrown `IllegalArgumentException` on
  `FileFormat.valueOf("TXT")`. Added a `"TXT"` → `FileFormat.ATXT` alias,
  same pattern as the two existing ones. `output: utxt` works with no alias
  needed, since `UTXT` already matches the enum constant name directly.
- **`VegaInputFile.CHECKERS`** had no entry for `ATXT`/`UTXT` — added
  `VegaCheckerAtxt` (new, extension `.atxt`) and `VegaCheckerUtxt` (new,
  extension `.utxt`), both trivial one-liners delegating to
  `VegaChecker.checkTextOutput(...)`, exactly like `VegaCheckerLatex` /
  `VegaCheckerScxml`. Deliberately not reusing `.txt` as the extension,
  since `VegaCheckerDebug` already hardcodes `.txt` for its own
  expected-output files — giving ASCII output its own extensions avoids any
  collision for a `.puml` file that might request both `debug` and `txt`
  output.

This exercises the real production entry point (`SourceStringReader` →
`SequenceDiagram.exportTxt()`), not the manual `zdev` harness — first real
confirmation that the slice built in §9 works end-to-end outside of the
scratch test. Like every other text-format checker, the first run
auto-creates the missing expected file (`hello1.atxt`, `hello1.utxt`); later
runs diff against it. First run of `hello1.atxt` came back matching a hand-
drawn expectation (participant heads, one arrow with a label, `return`
rendered as a second, reverse arrow) — the two-message case works, not just
the single-message one from §9.4.

## 11. Footer boxes

The pixel `drawU()` draws participant heads twice when
`playingSpace.isShowFootbox()` is true: once at the top
(`livingSpaces.drawHeads(ug, ..., VerticalAlignment.BOTTOM)`) and once at the
bottom (`livingSpaces.drawHeads(ug.apply(UTranslate.dy(pageHeight +
headHeight)), ..., VerticalAlignment.TOP)`). The ASCII path needed the same
thing: after the lifelines are filled in (§9.2), if `playingSpace.isShowFootbox()`,
each `Participant` draws itself a second time via
`p.asciiDraw(plan.move(boxLeft.get(p), bottomY))` — `bottomY` already points
exactly one row past the last lifeline row, so no extra offset arithmetic
was needed. Reused `PlayingSpace.isShowFootbox()` as-is; no new accessor
required. This is the same "ask the object to draw itself again at a
different position" pattern as everywhere else in §9 — no special-casing of
footboxes as a distinct concept.

## 12. ASCII vs. Unicode character set: `InfinitePlan` becomes `FileFormat`-aware

`ATXT` and `UTXT` are two different `FileFormat`s (see `FileFormat.java`),
and the legacy Puma-era ASCII renderer (`asciiart.UmlCharAreaImpl`) draws
boxes with plain ASCII (`,`/`.`/`` ` ``/`'`/`-`/`|`) for `ATXT` but with
Unicode box-drawing characters (`┌`/`┐`/`└`/`┘`/`─`/`│`) for `UTXT`
(`drawBoxSimple` vs. `drawBoxSimpleUnicode`). Until now, `InfinitePlan`
ignored this distinction entirely: its constructor hardcoded
`new TextStringBounder(FileFormat.ATXT)`, so `UTXT` output came out
identical to `ATXT` (plain ASCII), which is what Arnaud caught by testing
`hello1.utxt`.

**Fix, following the same "each object owns its own rendering knowledge"
principle as §9 and §11:** `InfinitePlan` now takes the real `FileFormat` in
its constructor (propagated through `move()` via `stringBounder.getFileFormat()`,
so a translated plan stays consistent), and centralizes the ASCII/Unicode
character choice itself, rather than leaving every caller to hardcode a
character and guess the format:

- `InfinitePlan.isUnicode()` — `true` iff `FileFormat.UTXT`.
- `getHLineChar()` / `getVLineChar()` — public, used by `CommunicationTile`
  (arrow dashes) and `PlayingSpaceWithParticipants` (lifeline fill pass).
- `getTopLeftChar()` / `getTopRightChar()` / `getBottomLeftChar()` /
  `getBottomRightChar()` — private, used internally by `drawBox()` only.

Callers (`CommunicationTile.asciiDraw()`, the lifeline-fill loop in
`PlayingSpaceWithParticipants.asciiDraw()`) no longer hardcode `'-'`/`'|'`;
they ask `plan.getHLineChar()`/`plan.getVLineChar()`. `Participant.asciiDraw()`
needed no change at all: it already only calls `plan.drawBox(...)` /
`plan.drawString(...)`, and `drawBox()` picks the right corner/line
characters internally — the box-drawing knowledge was already fully
contained in `InfinitePlan`, one object's format-awareness benefiting every
caller for free.

**Arrowheads stay plain ASCII (`>`/`<`) even in `UTXT` mode.** This mirrors
the legacy `asciiart.ComponentTextArrow.drawU()`, which only swaps the
*line* character (`'\u2500'` instead of `'-'`) for `UTXT` and leaves the
arrowhead characters themselves untouched (`charArea.drawChar('>', ...)` /
`drawChar('<', ...)`, unconditionally). Consistent with the standing rule
from §1/§9.1: copy the *idea* from the doomed `asciiart` package, never the
code, since that package is going away (see §13).

## 13. `Wcwidth`: the one class from the doomed `asciiart` package worth keeping

While reading `asciiart.*` for the Unicode character set (§12), one class
stood out as genuinely reusable rather than Puma-specific plumbing:
`asciiart.Wcwidth`. It implements the classic `wcwidth()`/`wcswidth()`
algorithm (attributed to Yasuhiro Matsumoto, based on Markus Kuhn's
reference implementation) for computing how many terminal *columns* a
Unicode code point occupies — `0` for combining/format characters, `1` for
ordinary characters, `2` for East-Asian wide/fullwidth characters (CJK,
fullwidth forms, etc.). This has nothing to do with Puma, `DrawableSet`, or
any of the classes actually being retired — it's a pure, self-contained
Unicode-width utility.

**Why it matters for this project:** `TextStringBounder.calculateDimension()`
currently returns `new XDimension2D(text.length(), 1)` — plain UTF-16 code
unit count, not display-column count. For plain ASCII/Latin text this is
correct (each character is exactly one column), but it silently
under-counts the width of any CJK participant name or message label (each
such character should count as `2` columns, not `1`), which would corrupt
column alignment in the ASCII column solver still to be built (§5 point 3,
§9.1). When that solver (or any future width-sensitive ASCII code) needs
correct column counts for non-Latin text, `Wcwidth.length(CharSequence)`
is the tool for it — to be copy-pasted (or, since it has zero dependency on
anything Puma-related, possibly *moved* rather than copy-pasted, unlike
everything else in `asciiart`) into wherever it ends up living once
`asciiart` itself is deleted. Not yet wired in anywhere — `TextStringBounder`
still uses plain `.length()` today; this is a known gap, not a regression.

## 14. The ASCII column solver: `Real` for X, on its own line, 1.0 = 1 char

This is the piece §5 point 3 promised and §9 deferred: X placement is now a
real constraint solve, not the flat linear scan it replaced. The design is a
near-exact transposition of how the pixel `createMainTile()` lays out the X
axis, with three deliberate differences (separate `RealLine`, integer deltas,
tiny gap constant).

**Two separate `RealLine`s, never mixed.** The pixel world builds its X graph
on a `RealOrigin` created in `SequenceDiagramFileMakerTeoz.createMainTile()`
(`xorigin`); every pixel `posB/C/D` chains off it, one `xorigin.compileNow()`
resolves it. The ASCII world now has an entirely separate graph, built in the
`getAsciiBlock()` path only (inside `PlayingSpaceWithParticipants.asciiDraw()`,
which is only ever reached from `getAsciiBlock()` — so PNG/SVG exports pay
nothing for it). Mixing the two is not just undesirable but impossible:
`Real.ensureBiggerThan()` / `RealUtils.max()` require both operands to live on
the same `RealLine`, so an accidental pixel↔ASCII constraint would throw at
runtime. Keeping the ASCII origin local to `asciiDraw()` guarantees the
separation structurally.

**`LivingSpace` ASCII positions mirror the pixel ones exactly.** For each
participant:

| pixel | ASCII | formula |
|-------|-------|---------|
| `posB` (left edge) | `getAsciiPosB()` | the chained origin position |
| `posC` (centre / lifeline) | `getAsciiPosC()` | `asciiPosB.addFixed(width / 2)` |
| `posD` (right edge, exclusive) | `getAsciiPosD()` | `asciiPosB.addFixed(width)` |
| `posA` (left margin) | `getAsciiPosA()` | `asciiPosB.addFixed(-marginBefore)` |
| `posE` (right margin) | `getAsciiPosE()` | `asciiPosD.addFixed(marginAfter)` |

where `width = p.asciiDimension().getWidth()` (an `int`: `code.length() + 2`).
Same lazy-creation pattern as the pixel getters, same "exclusive right edge"
convention for `posD`. `getAsciiLifeColumn()` = `(int) getAsciiPosC().getCurrentValue()`
and `getAsciiLeftColumn()` = `(int) getAsciiPosB().getCurrentValue()`, both
valid only after compile.

**Why the resolved values are exact integers.** A `Real` stores a `double`
internally, so in principle a resolved column could be `18.0000001` and the
`(int)` cast would truncate wrongly. It cannot happen here: every delta fed
into the ASCII graph is an integer (`width`, `width / 2` — integer division,
the `ASCII_GAP` constant, the per-message `label.length() + 2`), and the
origin starts at 0. A sum of integers stays exact in `double` far beyond any
realistic diagram width, so `getCurrentValue()` lands on a whole number and
the cast is lossless. This is precisely the property the §2 "quantization
trap" lacked: there, we would have divided a *pixel-measured* value (already
fractional) by 10; here we never measure in pixels at all — we solve directly
in cells.

**The constraints, two kinds:**

- *Neighbour spacing* — `LivingSpaces.asciiAddConstraints()`, the exact
  pendant of `addConstraints()`: for consecutive participants,
  `current.getAsciiPosA().ensureBiggerThan(previous.getAsciiPosE().addFixed(ASCII_GAP))`.
  The gap is a dedicated `ASCII_GAP = 1` cell (not the pixel `10`, not `10 / 10`
  — a genuine ASCII constant, per §4).
- *Message width* — `Tile.asciiAddConstraints()` (new interface method,
  `default` no-op so the ~30 implementations compile unchanged, like
  `asciiDraw()`), overridden by `CommunicationTile`: the two lifeline centres
  must be at least `label.length() + 2` cells apart (mirroring the legacy
  `asciiart.ComponentTextArrow.getPreferredWidth()` = label width + 2). It
  pushes whichever centre is on the right, decided by comparing
  `getCurrentValue()` *before* compile — which works because the chained
  origin gives every participant a monotonic initial value in declaration
  order, exactly as the pixel `isReverse()` relies on. Livebox/level
  adjustments from the pixel version are dropped for now (liveboxes aren't
  migrated to ASCII yet).

The solver then takes the `max` of all forces (that is what `PositiveForce`
does): a long message label pushes its two participants apart to fit;
short/absent labels fall back to the 1-cell neighbour gap. Both constraint
families coexist on the one shared ASCII line.

**Wiring, in `PlayingSpaceWithParticipants.asciiDraw()`:**
```
asciiXOrigin = RealUtils.createOrigin()
asciiCurrent = asciiXOrigin.addAtLeast(0)
for each participant p:            // chain posB, exactly like createMainTile()
    ls = livingSpaces.get(p)
    ls.setAsciiPosB(asciiCurrent)
    asciiCurrent = ls.getAsciiPosD().addAtLeast(0)
livingSpaces.asciiAddConstraints()  // neighbour gaps
for each tile: tile.asciiAddConstraints()   // message widths (top-level only)
asciiXOrigin.compileNow()
```
After compile, the draw phase reads resolved columns via `getAsciiLeftColumn()`
(heads, footboxes) and `getAsciiLifeColumn()` (arrows, lifelines). No geometry
is computed in `asciiDraw()` anymore — the `boxLeft` map, the `gap` heuristic
and `maxMessageLabelWidth()` are all gone.

**Still X-only.** Y remains the flat `y += 3` counter, and the per-tile
`asciiAddConstraints()` loop only visits **top-level** tiles
(`playingSpace.getTiles()`); nested tiles inside a `GroupingTile` are not
reached yet. Both are the same remaining piece: when the `fillPositionelTiles`-
style Y recursion is built, `GroupingTile` will get an `asciiAddConstraints()`
override that recurses into its own `tiles` (just as its pixel
`addConstraints()` already does `for (Tile t : tiles) t.addConstraints()`), and
the X and Y recursions will share the same tree walk. For the current
non-nested test cases (`hello1`), top-level iteration covers everything, and
the rendered output is unchanged from §9.4 — the point of this step was correct
foundations, not a visible difference.

## 15. Fixing `hello2.puml`: note-decorator tiles silently swallowed message and note

`hello2.puml` (`alice->bob: this is a test` followed by `note right: Some right
note`) rendered as two empty participant boxes with nothing in between — the
message itself had vanished, not just the note.

**Root cause.** `TileBuilder.buildOne()` does not add a message's
`CommunicationTile` to the top-level tile list directly when it has an
attached note: it wraps it in a decorator (`CommunicationTileNoteRight`,
`CommunicationTileNoteLeft`, `..NoteTop`, `..NoteBottom`, or the `Self*`
equivalents), the same pattern `GroupingTile`/`ElseTile` etc. use throughout
Teoz. These decorators `extends AbstractTile` (not `CommunicationTile`) and
delegate to the wrapped tile in every pixel method
(`addConstraints() { tile.addConstraints(); }`, `drawU()` calls
`((UDrawable) tile).drawU(ug)` before drawing its own note). The orchestrator's
filter, `if (tile instanceof CommunicationTile == false) continue;`, does not
recognize the decorator as "a message" and skips the whole tile — which is
why both the arrow and the note disappeared, not just the note.

**Fix, two parts:**

1. **`PlayingSpaceWithParticipants.asciiDraw()`'s top-level loop** no longer
   filters by `instanceof CommunicationTile`. Instead it calls
   `tile.asciiDraw(...)` inside a `try`/`catch (UnsupportedOperationException)`
   and skips the tile (without advancing `y`) on catch. This works because
   `AsciiBlock`'s default `asciiDraw()` throwing `UnsupportedOperationException`
   was always meant as the "not migrated yet" sentinel (§5 point 1, §9.1) —
   catching it here is reading that sentinel where it was always meant to be
   read, rather than duplicating the same information as a growing
   `instanceof` chain that the orchestrator would need updating for every
   future migrated type. A tile becomes part of the ASCII render the moment
   it gets a real `asciiDraw()` override, full stop — no orchestrator change
   needed, which is exactly what let the fix below "just work" once written.
2. **`CommunicationTileNoteRight` and `CommunicationTileNoteLeft`** each got
   a real `asciiAddConstraints()` (delegates to `tile.asciiAddConstraints()`,
   mirroring `addConstraints()`) and `asciiDraw()` (delegates
   `tile.asciiDraw(plan)` first — mirroring `drawU()`'s
   `((UDrawable) tile).drawU(ug)` — then draws the note itself as a flattened
   single-line `": <text>"` (right) or `"<text> :"` (left) immediately outside
   the target/source lifeline column, on the arrow row).

**Deliberately left alone (still silently skipped, unchanged behavior):**
`CommunicationTileNoteTop`/`CommunicationTileNoteBottom` (the note occupies
an *extra* row above/below the message in pixel — doing this properly in
ASCII needs the still-missing per-tile ASCII height, i.e. the Y-axis
recursion, not just a delegate call) and `CommunicationTileSelfNoteLeft`/
`CommunicationTileSelfNoteRight` (they wrap `CommunicationTileSelf`, which
has no `asciiDraw()` at all yet — self-messages are still on the §9.5 to-do
list). Both continue to throw `UnsupportedOperationException` by inheriting
the `AsciiBlock` default, so they are silently skipped by the new catch,
exactly as every currently-unmigrated tile type already was.

**Known gaps in the new note rendering** (acceptable for a first slice, worth
fixing before trusting arbitrary diagrams):

- The note's own width is **not** reserved on the ASCII column solver (§14):
  `asciiAddConstraints()` only forwards the message's own width constraint,
  nothing accounts for the note text possibly needing room past the target
  participant. For `hello2`, `bob` is the rightmost participant so there is
  nothing to collide with; a `note right` on a non-rightmost participant
  could overlap whatever comes next.
- `CommunicationTileNoteLeft`'s note can land at a negative column (e.g. the
  leftmost participant, little room to its left), which `InfiniteString`
  cannot handle (`StringBuilder.setCharAt` on a negative index throws) —
  `InfinitePlan` has no negative-x guard anywhere today, this isn't new, but
  the note-left case is the first place likely to actually hit it.
- Multi-line note `Display`s are flattened to one line (all `CharSequence`
  parts concatenated with no separator) — same simplification
  `CommunicationTile.asciiLabel()` already makes for message labels.

## 16. A real note box: `InfinitePlan.drawNoteBox()`

The single-line `": <text>"`/`"<text> :"` bracket from §15 was a placeholder;
notes now get a proper folded-corner ("dog-ear") box, the ASCII/Unicode
equivalent of the classic UML note shape, matching what PNG/SVG already draw.

**Idea copied, not code, per the standing rule (§1/§9.1/§12).** The legacy
`asciiart.UmlCharAreaImpl.drawNoteSimple()`/`drawNoteSimpleUnicode()` were the
reference for *what the box should look like*: ASCII cuts a `"!."`/`"|_\\"`
dog-ear into the top-right corner instead of a plain corner character; Unicode
draws a **double-line** box (`═║╔╗╚╝`, deliberately different from the
participant boxes' single-line `─│┌┐└┘`, exactly like the legacy code keeps
them visually distinct) plus a `░` shaded block simulating the fold. None of
that code was reused — `InfinitePlan.drawNoteBoxAscii()`/`drawNoteBoxUnicode()`
are new methods, written from scratch against `InfinitePlan`'s own primitives.

**New on `InfinitePlan`:**
- `drawNoteBox(left, top, width, height)` — public entry point, dispatches on
  `isUnicode()`.
- `drawNoteBoxAscii`/`drawNoteBoxUnicode` — private, mirror `drawBox()`'s
  shape but with note-specific corners/fold.
- `fillHLine(char, xFrom, xTo, y)` / `fillVLine(char, yFrom, yTo, x)` —
  private, character-parameterized versions of the existing public
  `drawHLine`/`drawVLine` (which now just call these with
  `getHLineChar()`/`getVLineChar()`). Needed because the note box's Unicode
  borders use `═`/`║` (double line), not the box's `─`/`│` (single line) —
  two different line characters need to coexist, so the line-drawing loop
  itself had to be decoupled from the fixed box character choice.

**Wiring, in `CommunicationTileNoteRight`/`CommunicationTileNoteLeft`:** the
box is 3 rows tall (top border, text, bottom border) — which fits exactly
inside the 3 rows already allocated per message (§9.2: label row, arrow row,
blank row), so **no Y-axis change was needed**. Width is
`max(5, noteText.length() + 2)` (the `5` floor keeps the ASCII dog-ear from
degenerating — it needs at least 4 columns to have room for both corners and
the fold). The box sits immediately after the target's lifeline column
(`NoteRight`) or immediately before the source's (`NoteLeft`); the text itself
is drawn on the middle row, one column padded from the left border.

**Same known gaps as §15, now on the box instead of the bracket:** the box's
width still isn't reserved on the ASCII column solver (§14) — it can overlap
a participant further right; `NoteLeft`'s box can still land at a negative
column on a narrow diagram (`InfinitePlan` has no negative-x guard); and
multi-line `Display`s are still flattened to one line.

## 17. Dashed arrows: `return` messages weren't dotted in ASCII/Unicode

Pixel output correctly draws `return` messages (and any other message whose
`ArrowConfiguration.isDotted()` is true) as a dashed line; the ASCII/Unicode
output drew a solid line regardless — `CommunicationTile.asciiDraw()` never
checked `isDotted()` at all, unlike its pixel counterpart
(`ComponentTextArrow.drawU()`'s `if (config.isDotted()) { ... blank every
other cell ... }`).

**Fix, same "idea copied, not code" rule as §12/§16.** The legacy method draws
the full line first, then blanks every other cell (`for (i=1;i<width;i+=2)
charArea.drawChar(' ', i, yarrow);`). Reimplemented as a new overload
`InfinitePlan.drawHLine(xFrom, xTo, y, boolean dotted)`: when `dotted` is
false it's exactly the existing `drawHLine(xFrom, xTo, y)`; when true, it
draws the line character only on every other cell in the first place (same
visual result, no intermediate full line to blank out afterwards).
`CommunicationTile.asciiDraw()` now calls
`plan.drawHLine(from + 1, to - 1, 1, message.getArrowConfiguration().isDotted())`
instead of the plain 3-argument version — the one-line change that was
missing. Centralizing the alternating-cell logic in `InfinitePlan` (rather
than in `CommunicationTile`) keeps the same division of responsibility as
§12/§16: `InfinitePlan` owns every format/style-dependent character choice,
callers just say *what* they want (dotted or not), never *how* to draw it.

## 18. `AsciiBlock`: objects that draw themselves, and a real multi-line note

§16's note box (`InfinitePlan.drawNoteBox()`) drew eagerly on `this` and took
raw `width`/`height` — the caller had to know the box's shape rules (the `!.`
/`|_\` dog-ear floor, the double-line Unicode variant) well enough to pick
numbers, and separately draw the note *text* itself right after, by hand,
already flattened to one line (§15/§16 "known gaps"). This section replaces
that with the pattern the rest of ASCIIVERSE is moving to: **objects that
know how to draw themselves**, composed rather than hand-wired by the
caller. This is expected to be used intensively going forward — every new
ASCII-drawable shape (boxes, notes, eventually whole tiles) should be an
`AsciiBlock`, not a `void drawX(width, height)` method plus caller-side
bookkeeping.

**The interface** (already existed, unused before this): `AsciiBlock` has two
`default` methods, both throwing `UnsupportedOperationException` out of the
box — the same "not migrated yet" sentinel role `asciiDraw()` already plays
for `Tile`/`AbstractTile` (§15 point 1):
- `asciiDimension()` → an `ADimension2D` (immutable width/height, already
  used elsewhere for pixel-side layout math, e.g. `mergeLR`/`mergeTB`) telling
  a caller how much room the block needs *before* it's drawn.
- `asciiDraw(InfinitePlan plan)` → draws the block, relative to wherever the
  `plan` argument is currently positioned (its `dx`/`dy`, set by chained
  `move()` calls). This is the contract every `AsciiBlock` follows: the block
  itself never knows its absolute column/row, only its own top-left origin.

**`ANote`**: the note box's shape (dog-ear / double-line-with-shade, §16)
moved here verbatim from `InfinitePlan.drawNoteBoxAscii()`/`drawNoteBoxUnicode()`
— same characters, same conditions, only every implicit `this.` became an
explicit `plan.` parameter, because the block is created once (at
`createNoteBox()` time) but drawn later, against a `plan` the caller has since
repositioned. That split — draw logic reading `plan`, never `this` — is what
lets `ANote` reach `InfinitePlan`'s private-turned-package-private
`fillHLine`/`fillVLine` on an *arbitrary* `InfinitePlan` instance: Java
privacy is per top-level class, not per instance, so any class in package
`asciiverse` can call them on any `InfinitePlan`, which is exactly why
`fillHLine`/`fillVLine` dropped their `private` modifier (kept `private`
would have forced `ANote`'s logic to stay as anonymous classes *inside*
`InfinitePlan`, the awkward shape this section moves away from).

**`ANote` also owns its content now.** Its constructor is
`ANote(boolean isUnicode, int width, int height, AsciiBlock text)`: `text` is
itself an `AsciiBlock` (typically a `Display`, see below), and
`ANote.asciiDraw(plan)` draws the border first, then
`text.asciiDraw(plan.move(1, 1))` — one column/row padding inside the border
on every side, the same offset the old caller-side
`plan.move(left + 1, 1).drawString(noteText)` used, just delegated instead of
hand-placed.

**`InfinitePlan.createNoteBox()` changed shape to match**: it used to take
`(int width, int height)`; it now takes `(AsciiBlock text)` and derives the
box's size from `text.asciiDimension()` itself —
`width = max(5, textWidth + 2)`, `height = max(3, textHeight + 2)` (the `+2`
on each axis is the border; the floors are the same ones the old
caller-computed `Math.max(5, noteText.length() + 2)` used, just no longer
duplicated at every call site). This is the detail that bit us once already:
an early caller-side draft recomputed `width` itself
(`Math.max(5, noteText.asciiDimension().getWidth())`, missing the `+2`) to
position the box before drawing it, which agreed with the real box width only
by coincidence while the text stayed under the `5`-column floor, and would
have silently overlapped the lifeline by two columns for any longer note. The
fix: never recompute a size `createNoteBox()` already knows — call it first,
read the *box's own* `asciiDimension()` back, and position from that:
```
final AsciiBlock noteBox = plan.createNoteBox(noteText);
final int width = noteBox.asciiDimension().getWidth();
final int left = sourceColumn - width - 1;
noteBox.asciiDraw(plan.move(left, 0));
```
The general lesson, worth repeating for every future `AsciiBlock`: a shape's
size rule should live in exactly one place (the factory/constructor that
builds the shape), and every caller that needs the size for positioning
should read it back off the built block's `asciiDimension()`, never
reimplement the formula.

**`Display implements AsciiBlock`** — the actual fix for the "multi-line
notes are flattened to one line" gap called out in §15/§16. `Display` is
already, structurally, a `List<CharSequence>` of lines
(`displayData`/`get(i)`/`size()`); it needed no new state, only two methods:
- `asciiDimension()` → `new ADimension2D(contentWidth(), size())`, reusing
  the pre-existing `contentWidth()` (max line length) and `size()` (line
  count) — both already used by pixel-side code, now doing double duty.
- `asciiDraw(plan)` → `for i in 0..size(): plan.move(0, i).drawString(get(i))`,
  one `CharSequence` per row.
A `Note`'s `Display` (`noteOnMessage.getDisplay()`) can now be passed straight
as the `text` argument to `createNoteBox()` — no more `asciiNoteText()`
flattening every line into one `StringBuilder`; real multi-line notes render
correctly, and `CommunicationTileNoteLeft`/`Right`'s `asciiNoteText()` shrank
to a one-line `return noteOnMessage.getDisplay();`.

**Still open**, inherited unchanged from §15/§16: the note box's width still
isn't reserved on the ASCII column solver (§14) — a note on a non-rightmost
(`NoteRight`) or non-leftmost (`NoteLeft`) participant can still overlap
whatever comes next; and `NoteLeft`'s box can still land at a negative column
on a narrow diagram (`InfinitePlan` has no negative-x guard anywhere). Both
are layout-solver problems, orthogonal to the `AsciiBlock` plumbing this
section adds — the multi-line gap is the one this section actually closes.

## 19. `CommunicationTile`'s own label can be multi-line too

§18 wired `message.getLabel()` into `CommunicationTile` as an `AsciiBlock`,
but left the arrow pinned to a hardcoded row 1 — a multi-line label would
have drawn past row 0 and collided with the arrow. Unlike the note box's
same gap (still open, §16/§18: the box's width isn't reserved on the column
solver, and it isn't sized around a growable Y span), this one was cheap to
close outright, because the message tile's *own* Y footprint is exactly what
the orchestrator loop already asks every tile for via `asciiDimension()` —
no column-solver changes needed, only a Y one.

**`CommunicationTile` now:**
- `asciiLabelRows()` — `Math.max(1, message.getLabel().asciiDimension().getHeight())`.
  The floor of `1` preserves the historical spacing: a message with no label
  still gets one blank row above the arrow, exactly as before.
- `asciiDraw(plan)` — draws the label starting at row 0 (one row per line,
  via `label.asciiDraw()`, unchanged from §18), then draws the arrow at row
  `asciiLabelRows()` instead of the constant `1`. A one-line label reproduces
  the old row-0/row-1 layout exactly; a longer label just pushes the arrow
  further down.
- `asciiDimension()` (new override; previously inherited the `AsciiBlock`
  default, i.e. "not migrated") — `new ADimension2D(asciiMessageWidth(),
  asciiLabelRows() + 2)`: label rows, plus the arrow row itself, plus one
  blank trailing row (the historical fixed "3" from §9.2/§15, now
  `asciiLabelRows() + 2` so it grows with the label instead of staying `3`).

**`PlayingSpaceWithParticipants.asciiDraw()`'s top-level loop** changed its Y
advance from a flat `y += 3` to `y += tile.asciiDimension().getHeight()`,
read back *after* a successful `tile.asciiDraw()` (same reasoning as
`InfinitePlan.createNoteBox()` in §18: don't hardcode at the call site a
size the object itself can report). Tiles that draw successfully but don't
yet override `asciiDimension()` — currently `CommunicationTileNoteRight`/
`Left`, still inheriting the `AsciiBlock` default — fall back to the old
fixed `3` via a second `try`/`catch (UnsupportedOperationException)`, so
behavior for anything not yet touched is unchanged. As those decorators (and
future tile types) grow their own `asciiDimension()`, this fallback simply
stops being exercised for them, with no further orchestrator change needed —
the same "a tile becomes part of the render the moment it gets a real
override" principle §15 already established for `asciiDraw()` itself, now
extended to `asciiDimension()`.

**Still open:** `CommunicationTileNoteRight`/`Left` still don't report their
own `asciiDimension()` (they fall back to the fixed `3`), so a multi-line
*note* still doesn't grow the Y allocation the way a multi-line *label* now
does — only the note's own box (§18) draws correctly top-to-bottom; the
surrounding message row doesn't yet make room below it. Fixing that is the
same recipe as this section: give those decorators a real `asciiDimension()`
that forwards to their wrapped tile's height plus the note box's height.

## 20. `CommunicationTileNoteRight`/`Left` now report their own height too

The closing paragraph of §19 turned out to be exactly the fix: both note
decorators now override `asciiDimension()`, so a multi-line note grows the Y
allocation the same way a multi-line label already does.

**One wrinkle first, and a correction to how it was first solved.** The note
box's size formula (`width = max(5, textWidth + 2)`, `height = max(3,
textHeight + 2)`) lived only inside `InfinitePlan.createNoteBox()` (§18),
which needs a `plan` instance to call (`isUnicode()` reads the plan's
`FileFormat`) — but a decorator's `asciiDimension()` has no `plan` argument
at all. The first fix pulled the formula out into a static
`ANote.dimensionFor(text)`. On reflection that was one layer too many:
`ANote`'s width/height don't need to be *given* to it at all — they're a
pure function of `text.asciiDimension()`, so `ANote` computes them itself,
once, inside its own constructor, which now takes only `AsciiBlock text`.
`isUnicode` dropped out of the constructor too, for the same kind of reason:
it's not needed to know the *size*, only to pick border *characters* at draw
time, so `ANote.asciiDraw(plan)` now reads `plan.isUnicode()` directly off
whatever plan it's asked to draw against — the same pattern every other
`AsciiBlock` in this file already follows (§18: a block never knows anything
about where or how it'll be drawn until `asciiDraw(plan)` hands it a plan).
The practical effect: `InfinitePlan.createNoteBox(text)` is now just
`return new ANote(text);`, and any caller that only needs the *size* —
CommunicationTileNoteRight/Left's `asciiDimension()`, below — builds a
throwaway `new ANote(text)` and reads `.asciiDimension()` off it, never
touching a plan. No separate static method needed: the object *is* the
single source of truth for its own size, reachable from anywhere, which is a
cleaner resolution of §18's "one place owns the size rule" than adding a
second static entry point every time a new caller can't reach the first one.

**`CommunicationTileNoteRight`/`Left`.`asciiDimension()`** (new): mirrors the
pixel-side `getPreferredHeight()` right above it, which already does
`Math.max(tile.getPreferredHeight(), dim.getHeight())` — same idea, ASCII
side: `Math.max(tile's asciiDimension height, new
ANote(noteText).asciiDimension() height)`. The inner `tile.asciiDimension()`
call is wrapped in `try`/`catch (UnsupportedOperationException)` falling
back to the historical fixed `3`, exactly like the orchestrator's own
fallback added in §19 for the same reason (an inner tile that draws but
hasn't grown an `asciiDimension()` override yet). Width is passed through
from the inner tile unchanged — the note box's *width* still isn't reserved
on the ASCII column solver (§14), so reporting a bigger one here wouldn't be
acted on by anything yet; only height actually feeds the Y-axis today.

**Still open, narrowed:** the column-solver width gap from §14/§15/§16 is now
the only thing left in this family — a note box can still overlap a
participant further right (`NoteRight`) or land at a negative column
(`NoteLeft`, no negative-x guard in `InfinitePlan`). Both the Y-axis gaps
this section and §19 opened with are closed.

*(The `try`/`catch` around `tile.asciiDimension()` described above was
removed shortly after this section was written — see §22, part of the same
crash-instead-of-mask policy §21 introduced.)*

## 21. Policy change: unmigrated top-level tiles crash instead of being skipped

Since §15, `PlayingSpaceWithParticipants.asciiDraw()`'s top-level loop wrapped
`tile.asciiDraw(...)` in `try`/`catch (UnsupportedOperationException)`,
treating the `AsciiBlock` default's exception as a deliberate "not migrated
yet" sentinel and silently skipping the tile. That `try`/`catch` (both the
one around `asciiDraw()` and the one added in §19 around `asciiDimension()`)
has been removed: a top-level tile with no real ASCII support now crashes
with `UnsupportedOperationException` instead of vanishing from the output.

**Why the reversal.** Silently skipping made every not-yet-migrated tile
invisible rather than loud — fine for incremental development (each new
`asciiDraw()` override "just worked" the moment it was added, no orchestrator
change needed, as §15 stressed), but it also meant a genuinely missing
feature renders as *nothing*, not an error: a diagram silently missing a tile
is a worse failure mode to debug than a stack trace pointing at exactly which
tile type still needs work.

**Immediate fallout: `EmptyTile`.** `EmptyTile` is a pure spacer (`drawU()` is
already a no-op; it exists purely to occupy Y-space via its `height` field)
— it had never had an `asciiDraw()`/`asciiDimension()` override, so it always
fell through to the `AsciiBlock` default, and the old `catch` made that
invisible. With the `catch` gone, any diagram that produces one crashes at
`AsciiBlock.asciiDraw()`. Fix: give it real overrides, both trivial
(`asciiDraw()` does nothing, `asciiDimension()` returns `new ADimension2D(0,
0)`) — not a workaround, the actual correct behavior for a tile that draws
nothing and needs no room, same as `drawU()`'s own no-op.

**Going forward:** every other `Tile` implementation that still inherits the
`AsciiBlock` default (there are roughly 30, per §15) will now surface the same
way — a crash naming the exact class, at the exact call site, the first time
a diagram exercises it — rather than staying silently invisible until
someone happens to compare rendered output against expectations. That is the
intended trade: slower to notice per-tile gaps exist, but each one is now
unmissable and self-diagnosing the moment a diagram hits it, rather than a
latent gap in visual coverage nobody is tracking.

## 22. Extending the crash policy to `CommunicationTileNoteRight`/`Left`'s own `asciiDimension()`

§21 changed the *orchestrator's* top-level loop from skip-on-
`UnsupportedOperationException` to crash-on-it. One `try`/`catch` of the same
shape survived that pass, one level down: `CommunicationTileNoteRight`/
`Left`.`asciiDimension()` (§19–§20) wrapped its own `tile.asciiDimension()`
call in `try`/`catch (UnsupportedOperationException)`, falling back to the
historical fixed `3` if the wrapped tile didn't report a real height. Removed
for the same reason §21 gave: a wrapped tile with no ASCII support should
crash loudly, naming itself, rather than have this decorator quietly
substitute a made-up `3` and let a wrong-looking diagram render without any
error. The two decorators' `asciiDimension()` now simply call
`tile.asciiDimension()` unguarded — same code shape as every other place in
`asciiverse`/`teoz` that reads a size back off an `AsciiBlock` (§18's
"read the size back, don't reimplement/mask it" rule extends naturally to
"and don't catch it away either").

## 23. `InfiniteString`: columns left of 0, and `InfinitePlan.exportTxt()` alignment

§15/§16/§18 all flagged the same known gap in passing: `CommunicationTileNoteLeft`'s
note box can land at a negative column on a narrow diagram (not enough room
to its left), and `InfinitePlan` had "no negative-x guard anywhere". The
reason was one level below `InfinitePlan` itself: `InfiniteString` — the
backing store for a single row — only ever grew rightward. It already
declared a `negativeChars` field, unused, evidently a started-but-abandoned
attempt; `getString()` threw `UnsupportedOperationException("Work in
progress")` outright for any negative `startingPosition`, and `setCharAt()`/
`getCharAt()` would have thrown `StringIndexOutOfBoundsException` from the
underlying `StringBuilder` the moment a negative position was actually used
(nothing before this section ever exercised that path, since nothing called
`InfinitePlan.move()` with an eventual negative net offset until the note-left
case in §15).

**The fix, finishing what `negativeChars` started.** `InfiniteString` now
genuinely extends in both directions from position 0: `positiveChars`
unchanged (position `n` at index `n`), `negativeChars` new (position `-1` at
index `0`, `-2` at index `1`, ... — index `= -position - 1`, so appending to
it extends further left exactly the way appending to `positiveChars` extends
further right). `setCharAt`/`getCharAt` dispatch on the sign of `position`;
`setStringAt` delegates to `setCharAt` one character at a time (rather than
pre-sizing both halves up front), so a single string can start negative and
cross into positive territory, or vice versa, in one call without the caller
needing to split it. `getString(startingPosition)` no longer throws for a
negative argument: it renders the requested negative span (left to right,
padding any never-written position with a space, same convention
`getCharAt()` already used) followed by all of `positiveChars`.

**`getLeftmostPosition()`** — new, `-negativeChars.length()` (so `0` if a line
never went negative). A single `InfiniteString` only knows its own extent;
rendering many lines together (`InfinitePlan.exportTxt()`) needs one common
left margin for every line, or lines that never drew left of 0 would start
one column further right than neighboring lines that did, breaking
alignment. `exportTxt()` now computes `startingPosition` as the minimum of
every line's `getLeftmostPosition()` before its (unchanged) second pass that
calls `getString(startingPosition)` on each line — if no line ever went
negative, this is `0`, exactly the historical behavior, so ordinary diagrams
are unaffected.

**Tests**, `InfiniteStringTest.java` (new, JUnit 5 `assertEquals` —
deliberately not AssertJ's `assertThat`, per house preference): positive and
negative positions read/written independently, the position-0 boundary,
overwriting a negative char, `setStringAt` purely positive / purely negative /
crossing zero, and `getString()`'s four shapes (from `0` as before, from a
negative start with and without positive content, and padding with spaces
when asked further left than anything written) — plus `getLeftmostPosition()`
itself (zero by default, reflects the furthest-left char written, doesn't
shrink back if a later write is less far left, and works through a
`setStringAt` that crosses zero).

**Still open:** this fixes the storage layer a negative column relies on, and
wires the one caller (`exportTxt()`) that needs to know about it globally.
It does not by itself reserve room on the ASCII column solver (§14) for a
note box that extends left of a participant's own column — that gap (a note
can still visually overlap something to its left in the *column-assignment*
sense, only the *storage* no longer crashes) is unchanged from §15/§16/§18.

## 24. Groups draw themselves: `GroupingTile`/`PartitionTile` frames (`hello3`)

`hello3.puml` (two `partition` blocks, each containing messages and a note)
crashed with `UnsupportedOperationException: ...PartitionTile` at
`AsciiBlock.asciiDraw()`. `PartitionTile extends GroupingTile`, and
`GroupingTile` — the tile behind every `alt`/`opt`/`loop`/`group`/`partition`
— had no ASCII overrides at all, so the orchestrator's top-level loop hit the
`AsciiBlock` "not migrated" default and crashed (the intended §21 policy:
unmigrated top-level tiles crash loudly rather than vanish). This section
migrates `GroupingTile`, so `PartitionTile` inherits ASCII support unchanged.

**Same "each object draws itself" pattern as everywhere else (§9/§11).** A
group renders as a frame box enclosing its stacked children, title on the top
border row — the structural ASCII counterpart of the pixel `drawU()` (frame
component + children translated below the header). The three ASCII methods
mirror their pixel siblings one-for-one:
- `asciiAddConstraints()` recurses into `tiles`, exactly like
  `addConstraints()` does.
- `asciiDimension()` = frame width (from the children's column span) ×
  (1 header row + Σ children heights + 1 footer row).
- `asciiDraw(plan)` draws the box via `InfinitePlan.drawBox()` (reusing its
  ASCII/Unicode corner logic — no new characters), stamps the title into the
  top border, then draws each child at `plan.move(0, y)`, advancing `y` by
  each child's own `asciiDimension().getHeight()` (the §19 rule again).

**New `Tile.getAsciiMinX()`/`getAsciiMaxX()` — the ASCII counterpart of
`getMinX()`/`getMaxX()`, and `Real`, not resolved columns.** A frame must be
wide enough to enclose its children, but the pixel `getMinX()`/`getMaxX()`
return `Real` positions on the *pixel* line — useless for ASCII. The first
cut of this used a post-compile `int[]`, but that broke the architecture's own
rule (§8): positions stay `Real`, composable *before* compile, exactly like
the pixel `min`/`max` fields are built from `RealUtils.min(min2)`/
`RealUtils.max(max2)` in the constructor above. So `Tile` instead gained two
`default Real` methods, both returning `null` by default:
- `CommunicationTile` overrides them as `RealUtils.min/max(livingSpace1.getAsciiLifeColumn(),
  livingSpace2.getAsciiLifeColumn())` — composed lazily, no resolved value
  needed yet.
- The two note decorators delegate to the wrapped message, like their other
  ASCII delegations.
- `GroupingTile` composes the union of its children's `Real` (skipping nulls,
  e.g. `EmptyTile`) via `RealUtils.min`/`max`, then applies the frame margin
  with `addFixed()` — still lazy, still uncompiled. This can't happen in the
  constructor the way the pixel min/max do, because the ASCII `Real` graph
  (`asciiPosB`...) doesn't exist until `PlayingSpaceWithParticipants.asciiDraw()`
  wires it up; it's computed on demand instead.

Only `GroupingTile`'s private `asciiFrameColumns()` — called from
`asciiDimension()`/`asciiDraw()`, after the ASCII `RealLine` has compiled —
finally casts these `Real` down to `int` columns for `InfinitePlan`.
`LivingSpace.getAsciiLifeColumn()`/`getAsciiLeftColumn()` were changed the
same way (now return `Real`, i.e. `getAsciiPosC()`/`getAsciiPosB()` directly);
every caller that needs an actual column for drawing now reads
`.getCurrentValue()` and casts at the point of use
(`CommunicationTile.asciiDraw()`, the note decorators' `asciiDraw()`, and the
three column-based lookups in `PlayingSpaceWithParticipants.asciiDraw()` —
heads, lifeline fill, footboxes), instead of holding a resolved `int` any
earlier than strictly necessary.

**Children draw at `dx = 0`, the frame at absolute columns.** The one subtlety
worth recording: `CommunicationTile.asciiDraw()` computes its arrow columns
from `getAsciiLifeColumn()` (absolute), ignoring the plan's horizontal
translation — it only uses the plan's *row*. So `GroupingTile.asciiDraw()`
draws children with `plan.move(0, y)` (row only), while the frame border
itself is drawn at the absolute columns `asciiColumnRange()` resolved to
(child min/max ± a 2-cell `ASCII_FRAME_MARGIN`, the ASCII analogue of the
pixel `MARGINX`). The orchestrator's later lifeline-fill pass writes `|` only
into *empty* cells, so it runs the lifelines through the group body but leaves
the frame's horizontal borders intact where they cross a lifeline column —
the correct "border crosses lifeline" look, for free.

**Known gaps (consistent with the rest of this log):**
- The frame's own *width* (title length, and the group margin) is not
  reserved on the ASCII column solver (§14) — a very long group title could
  overrun the frame past its participants, the same class of gap as the note
  box width.
- Only the frame's *height* feeds the Y axis (via `asciiDimension()`); that
  part is correct and recursive.
- Group-level end-notes (`note left`/`note right` after `end`, drawn by the
  pixel `drawNotes()`) are not yet rendered in ASCII — `hello3`'s notes are
  message-attached (`CommunicationTileNoteRight`/`Left`, already handled),
  not group-attached, so this isn't exercised yet.
- Title is flattened to one line (§18 simplification), fine for the
  single-word partition/group titles in the current tests.

**Reference files.** `hello3.atxt`/`hello3.utxt` on disk predate this fix
(they were auto-written by an earlier partial/among-crash run and show only
participant heads — no frames, messages, or notes). They must be deleted (or
regenerated with `VEGA_FORCE_WRITE`) so Vega rewrites them from the now-
correct output, then eyeballed, before they can be trusted as a baseline.

## 25. A generic margin decorator: `AsciiBlockMarginLR`

Message labels used to be drawn flush against the arrow they annotate
(`|this is a test |` — one accidental space from centering rounding, not an
actual margin). Rather than hand-adjusting the centering math in
`CommunicationTile`, this adds a small, reusable `AsciiBlock` decorator:
`AsciiBlockMarginLR(AsciiBlock inner, int marginLeft, int marginRight)`, in
the `asciiverse` package alongside `ANote`/`Display`. It wraps any
`AsciiBlock` and pads it with blank columns on each side:
- `asciiDimension()` grows the inner block's width by `marginLeft +
  marginRight` (height untouched) — *unless* the inner block is empty
  (`width == 0`), in which case it's returned unchanged: there's no text to
  pad, so an unlabeled message still reserves no extra space.
- `asciiDraw(plan)` just delegates to `inner.asciiDraw(plan.move(marginLeft,
  0))` — the inner block never knows it's been padded.

Same composition principle as every other `AsciiBlock` in this file (§18):
the wrapper owns the padding rule once, callers read the size back rather
than recomputing it.

**Wired into `CommunicationTile`** via a new private `asciiLabel()` returning
`new AsciiBlockMarginLR(message.getLabel(), 1, 1)`, used everywhere the raw
`message.getLabel()` used to be read: `asciiDraw()` (centering + drawing),
`asciiLabelRows()` (height), and `asciiMessageWidth()` (width, for the column
solver's constraint in `asciiAddConstraints()`).

**First attempt got this wrong: dropping the historical `+2` broke the
margin's visual effect entirely.** The initial cut of this change assumed the
existing `+2` in `asciiMessageWidth()` ("mirroring the legacy
`asciiart.ComponentTextArrow.getPreferredWidth()`") *was* the margin, just
implicit, and replaced it outright with `asciiLabel().asciiDimension().getWidth()`
(which, for a 1+1 margin, is numerically identical to the old `label+2` — so
the reserved gap between the two lifelines didn't change at all, and the
rendered output was pixel-for-pixel identical to before the whole change).

The `+2` and the margin are two *different* things and both are needed:
- The margin (inside `asciiLabel()`) reserves blank columns between the text
  and the edges of its own block.
- The `+2` reserves one additional "runway" column immediately next to each
  lifeline that the label block must never touch at all.

Without that separate `+2`, the label block — sized to exactly the reserved
width — gets centered flush against the lifeline columns themselves (its left
edge lands exactly on `xa`, its right edge exactly on `xb`). Its own margin
cell then coincides with the lifeline column, and the lifeline-fill pass
(`PlayingSpaceWithParticipants.asciiDraw()`) draws the `|` right over it,
silently erasing the margin. The fix: keep the `+2` *in addition to* the
margin —
```java
private int asciiMessageWidth() {
    return asciiLabel().asciiDimension().getWidth() + 2;
}
```
— so the block is centered with genuine breathing room on both sides before
the lifeline columns start. (The two sides end up off by one column from each
other in some cases — e.g. 1 blank column on the left, 2 on the right — a
normal integer-rounding artifact of centering an even-width block in a
lifeline gap that isn't perfectly aligned to it; not worth correcting further
for a first cut.)

**Not yet wired into notes** (`ANote`/the note decorators still draw their
text flush against the border, one column of padding from the border itself
from §16/§18, which is a different margin than this one). `AsciiBlockMarginLR`
is generic enough to reuse there too if/when that's wanted.

**`AsciiBlock.marginLR()` — a fluent default method.** Arnaud added a default
method directly on the `AsciiBlock` interface:
```java
public default AsciiBlock marginLR(int marginLeft, int marginRight) {
    return new AsciiBlockMarginLR(this, marginLeft, marginRight);
}
```
so any `AsciiBlock` can wrap itself in a margin inline —
`message.getLabel().marginLR(1, 1)` — instead of the caller having to name
and import `AsciiBlockMarginLR` directly. Same pattern as `Real.addFixed()`/
`ensureBiggerThan()` being methods on `Real` itself rather than static
utilities: the decorator is still a plain standalone class (usable on its
own, and this is how `CommunicationTile.asciiLabel()` currently constructs
it), but every `AsciiBlock` now also gets it for free as a chainable method.

## 26. The frame should be an `AsciiBlock`, not inlined: `AGroupFrame`

§24 migrated `GroupingTile` by drawing the frame **inline** in `asciiDraw()`:
`plan.move(left, 0).drawBox(width, height)` plus a `plan.drawString()` for the
title, with the geometry computed by the tile itself (`asciiFrameColumns()`,
`asciiBodyHeight()`, `asciiTitle()`). That works, but it breaks the symmetry
this whole log has been converging on — and the break is worth naming
precisely, because the pixel side already shows the shape the ASCII side
should have.

**What the pixel `drawU()` actually does.** `GroupingTile.drawU()` is not a
drawer, it is an *orchestrator*. It never draws the frame itself: it obtains a
`Component` from the skin (`getComponent()` →
`skin.createComponent(..., GROUPING_HEADER_TEOZ, ...)`) and delegates
(`comp.drawU(ug.apply(translate), area, context)`). All the frame geometry —
border, the title tab, corners, colours — lives *in the Component*, drawn into
an `Area` the tile hands it. The tile's own job is only to compute min/max,
stack the children below the header, and delegate the frame, the else
separators, the notes, and the background each to their own object.

**The abstraction that matches `Component` already exists: `AsciiBlock`.**
The correspondence is exact, and §18 already established it for notes:

| pixel | ASCII |
|-------|-------|
| `Component.getPreferredDimension(sb)` | `AsciiBlock.asciiDimension()` |
| `Component.drawU(ug, area, context)` | `AsciiBlock.asciiDraw(plan)` |
| `Area` (size handed in) | `ADimension2D` |
| note `Component` (`createComponentNote`) | `ANote` |
| **frame `Component` (`GROUPING_HEADER_TEOZ`)** | **— missing —** |

`ANote` is the proof the pattern is right: the note-box shape moved *out* of
the caller and *into* an `AsciiBlock` (§16→§18), so the note draws itself, the
same way the pixel note `Component` does. The group frame is the one shape §24
left behind on the wrong side of that line — drawn by the tile, not by an
object of its own.

**The one real difference from `ANote`: self-sized vs. size-handed-in.**
`ANote` derives its own dimension from its content (`max(5, textWidth+2)` etc.)
— it is *self-sized*, like a label. A group frame is the opposite: its size is
dictated from outside, by the span of its children and the stacked body height
— it is a *container*. That is exactly the pixel distinction between a content
`Component` and the `Area` a container hands one: the pixel frame `Component`
is told its size (the `Area` argument), it does not compute it. So the ASCII
frame block, unlike `ANote`, takes an explicit `ADimension2D` in its
constructor rather than computing one — the faithful ASCII transposition of
"Component drawn into an Area handed to it".

**`AGroupFrame implements AsciiBlock`** (new, `asciiverse` package, alongside
`ANote`/`Display`/`AsciiBlockMarginLR`): constructed with the frame's
`ADimension2D` and its (flattened, §18) title; `asciiDimension()` returns that
dimension unchanged; `asciiDraw(plan)` draws a plain box via
`InfinitePlan.drawBox()` (the frame is an ordinary rectangle — no dog-ear — so
it reuses the plan's own ASCII/Unicode corner logic from §12, exactly as
`Participant.asciiDraw()` does for the participant head, rather than
reimplementing borders the way `ANote` must for its special shape) and stamps
the title onto the top border row. Like every `AsciiBlock` (§18), it draws
relative to the plan's origin and never knows its absolute column — the caller
positions the plan.

**`GroupingTile.asciiDraw()` becomes the orchestrator its pixel sibling
already is:** it computes the frame's absolute left column
(`asciiFrameColumns()`), asks an `AGroupFrame` to draw itself there
(`frame.asciiDraw(plan.move(left, 0))`), then stacks the children below the
header exactly as before (`plan.move(0, y)`, advancing `y` by each child's
`asciiDimension().getHeight()`, §19). The child-stacking stays in the tile —
correctly: the pixel `drawU()` doesn't put the children inside the header
`Component` either, it stacks them itself below it. `asciiFrameColumns()`,
`asciiBodyHeight()`, `asciiTitle()` are unchanged; only the four lines that
used to draw the box and title inline now build and delegate to an
`AGroupFrame`. The rendered output is identical — this is a structural
refactor, symmetry not pixels.

**The two symmetry gaps `AGroupFrame` does *not* close (recorded here so they
aren't lost).** The pixel `drawU()` delegates to *three* kinds of frame object,
not one; only the header is addressed here:
- **Else separators.** Pixel: `drawAllElses()` draws a full-width
  `GROUPING_ELSE_TEOZ` `Component` per `else`, spanning `min..max` — drawn by
  the *parent* `GroupingTile`, because only the parent knows the full frame
  width (`ElseTile.drawU()` itself is nearly a no-op). ASCII: **nothing**.
  Worse, `ElseTile` has no `asciiDimension()`/`asciiDraw()` at all, so an
  `alt`/`else` diagram currently crashes at the `AsciiBlock` default (the §21
  crash policy) the moment its `tiles` list is walked. Closing this needs the
  same split the pixel side has — an `ElseTile.asciiDimension()` for its row in
  the stack, plus a parent-drawn full-width separator (an `AsciiBlock` of its
  own, the counterpart of the else `Component`).
- **Group-level end-notes.** Pixel: `drawNotes()` draws `note left`/`note
  right`-after-`end` `Component`s at the frame corners. ASCII: not rendered
  (§24 already flagged this; `hello3`'s notes are message-attached, so it isn't
  exercised yet). The `ANote` block already exists to draw them — what's
  missing is `GroupingTile.asciiDraw()` calling it, the counterpart of
  `drawNotes()`.

**Legitimately absent, not a gap.** The pixel `drawBackground()` pass
(`Context2D.isBackground()`, the `Blotter`, per-`else` background colours) has
no ASCII counterpart and needs none — ASCII has no fill and no colour. This
asymmetry is correct and intentional, unlike the two above.

**Immediate step (this section):** `AGroupFrame` + the
`GroupingTile.asciiDraw()` delegation. The two gaps above are the natural next
sections once `alt`/`else` (the else separator) and group end-notes are
exercised by a test.

## 27. `alt`/`else` dividers: `ElseTile` + `AElseSeparator`

This closes the first of the two gaps §26 flagged. Before it, any `alt`/`else`
crashed with `UnsupportedOperationException: ...ElseTile` at
`AsciiBlock.asciiDraw()` (the §21 crash policy): `ElseTile` had no ASCII
support at all, so the moment `GroupingTile`'s stacking loop walked an else
child it hit the `AsciiBlock` "not migrated" default.

**The fix is the exact two-way split the pixel side already has.** In pixel, an
else boundary is drawn by *two* collaborators, not one: `ElseTile.drawU()` is a
no-op on the live path, and the full-width divider is drawn by the *parent*
`GroupingTile.drawAllElses()`, which alone knows the frame's `min..max` span.
The ASCII side mirrors that split one-for-one:

- **`ElseTile` owns only its row height and its label.**
  `asciiDimension()` returns a single divider row (`new ADimension2D(labelWidth,
  1)` — only the height is consumed, by `asciiBodyHeight()` and the stacking
  loop; the width is nominal). `asciiDraw()` is a deliberate no-op, mirroring
  the pixel `drawU()`'s live-path no-op rather than inheriting the `AsciiBlock`
  throw (it is in fact never reached — the parent special-cases else tiles —
  but an explicit no-op documents the intent and won't crash if that ever
  changes). `asciiLabel()` exposes the guard/comment as a flat string, built
  from `getComment()` exactly as the pixel `getComponent()` wraps
  `Display.create(anElse.getComment())`.

- **`AElseSeparator implements AsciiBlock`** (new, `asciiverse`) is the
  counterpart of the pixel `GROUPING_ELSE_TEOZ` `Component`. Like `AGroupFrame`
  (§26) it is a *container* block whose width is imposed from the outside, so
  it takes an explicit `(width, label)` rather than self-sizing.
  `asciiDraw(plan)` draws a **dotted** horizontal line across the frame
  *interior* only (columns `1..width-2`), leaving the frame's own side borders
  (drawn by `AGroupFrame` at columns `0` and `width-1`) intact, then stamps the
  label near the left corner with the same ` " " + label + " " ` tab treatment
  as the frame title. Dotted (not solid) so it reads as a divider rather than a
  second border; the dash character is `plan`-chosen (§12), correct for ATXT
  and UTXT with no branch.

- **`GroupingTile.asciiDraw()` draws the separator itself**, special-casing
  `ElseTile` in its stacking loop (`if (tile instanceof ElseTile) new
  AElseSeparator(width, ...).asciiDraw(plan.move(left, y))`, else the normal
  `tile.asciiDraw(plan.move(0, y))`). The `instanceof` is not a smell here: it
  is the same shape the pixel side already has (`drawAllElses()` is a separate
  pass, and `drawCompBackground()` likewise tests `tile instanceof ElseTile`),
  for the same structural reason — the divider's horizontal span is the
  parent's knowledge, not the child's.

**The "divider crosses the lifelines" look, for free.** The separator's dashes
are drawn into the interior cells before the orchestrator's lifeline-fill pass
(§9.2), which only writes `|` into *empty* cells — so the dashes win at the
crossings and the divider reads as an unbroken dashed line across the whole
frame, over the lifelines, exactly as the pixel divider does. The frame's own
side borders (`|` at both ends of the row) are untouched because the separator
never draws columns `0`/`width-1`.

**Known gaps (consistent with §24/§26):**
- The else label's width is not reserved on the ASCII column solver (§14) — a
  long guard can overflow the frame past its participants, the same class of
  gap as the frame title and the note box. The label is simply skipped (dashes
  still drawn) when it wouldn't fit, via the same width guard `AGroupFrame`
  uses for its title.
- The divider is a single row: the pixel `MARGINY`-style vertical breathing
  room around the else component has no ASCII counterpart yet. Fine for a first
  slice.
- Multi-line else guards are flattened to one line (§18 simplification).

**Still open from §26:** the *second* gap — group-level end-notes (`note
left`/`note right` after `end`, pixel `drawNotes()`) — is untouched; `ANote`
exists to draw them, what's missing is `GroupingTile.asciiDraw()` calling it.
That is the next natural section. And the legitimately-absent pixel
`drawBackground()`/`Blotter` (per-`else` fill colours) stays absent — ASCII
has no fill.

**Reference files.** No checked-in Vega case exercises `alt`/`else` yet; add
one (e.g. an `alt`/`else`/`end` with a message in each branch) and generate its
`.atxt`/`.utxt` with `VEGA_FORCE_WRITE`, then eyeball the divider before
trusting it as a baseline.

## 28. `AGroupFrame`, second cut: distinct header/side/footer characters, and a real title tab

Arnaud provided a hand-drawn ATXT mockup for `hello4.puml` (nested `group`s with
messages and both a right- and a left-attached note) showing a frame rendered
more closely after the actual pixel shape than §26's first cut: distinct
characters for each of the frame's three edges, and a small pentagon-style tab
for the title instead of the title simply stamped on the top border. Three
points were confirmed with Arnaud before implementing:

- The title stays comment-only ("p1", not "GROUP / p1") for the `group`
  keyword, unchanged from today's pixel-mirroring rule (§9.1 `display` —
  `start.getTitle().equals("group")` suppresses the keyword). The mockup's
  "PARTITION / p1" text was a hand-drawn stand-in, not a literal spec.
- The bottom border in tildes (`~~~~`) is an intentional, confirmed stylistic
  choice — not a slip — distinct from the top border's dashes.
- The participant head box style (`._.`/`|a|`/`'+'` in the mockup, vs. today's
  `,-.`/`|a|`/`` `-' ``) got no preference either way; left untouched for this
  pass (see "Not touched" below).

**Three edges, three different characters, deliberately.** The first cut
(§26) reused `InfinitePlan.drawBox()` wholesale — a uniform rectangle, all four
sides the same `|`/`-` as everything else on the plan, including participant
lifelines running straight through it. That was the exact ambiguity visible in
the pre-§28 `hello4.atxt` reference: `,-|p1 -----.` — the frame's own top-left
corner and participant `b`'s lifeline character are both `|`/`-`, indistinguish­
able at the point they cross. The redesigned `AGroupFrame` gives each edge its
own character, so a frame reads as a frame at a glance, never confusable with
a lifeline crossing through it:

| edge | character | rationale |
|------|-----------|-----------|
| top | `,`/`-`/`.` (Unicode-aware) | an ordinary box edge — unchanged from §26, still ASCII/Unicode-correct |
| sides | `!` | deliberately not `|` — the fix for the lifeline-collision ambiguity above |
| bottom | `~` (full width, no corners) | confirmed distinct style (this section), not a plain box edge with a swapped character |

The top border is drawn with `plan.drawHLine()` (already Unicode-aware, §12)
and two corner calls; `InfinitePlan.getTopLeftChar()`/`getTopRightChar()`
dropped their `private` modifier so `AGroupFrame` (same package) can reach them
directly — the same relaxation already made for `fillHLine`/`fillVLine` when
`ANote` needed them (§18). The sides and bottom are new, ATXT-only characters
with no Unicode-aware equivalent yet (see "Still ATXT-only" below) — `drawBox()`
itself is no longer called at all by this class, since a uniform box is no
longer what it draws.

**A real title tab, not a stamp on the border.** §26's title was `" " + title +
" "` written directly onto the top border row, overwriting whatever dashes were
underneath. The pixel `GROUPING_HEADER_TEOZ` component actually draws a small
cut-corner pentagon tab in the frame's top-left, separate from the frame's own
top border — visible in the original pixel screenshot Arnaud shared. The new
`AGroupFrame` reproduces that structurally, the ASCII transposition of the same
idea `ANote`'s dog-ear already uses for its own corner cut (§18):

- Row 1 (one row below the frame's own top border, one column in from the left
  `!` — the same padding convention `ANote`'s text uses): the title text,
  `" " + title + " "`.
- Row 2: a diagonal-fold line under the title — underscores (`_`) the width of
  the title text, then a trailing `/` — the ASCII stand-in for the pentagon's
  cut corner, the same idea as `ANote`'s `!.`/`|_\` dog-ear, simplified (no
  second character, no counterpart on the left).

Skipped entirely (falls back to the top border alone, no tab rows reserved)
when there's no title or it wouldn't fit — same policy, same numeric threshold
(`width > title.length() + 4`), as §26's title stamp.

**The header is no longer a fixed row count — `AGroupFrame` decides,
`GroupingTile` asks.** Because the tab may or may not be drawn (depending on
whether the title fits), the number of rows before the body starts is no
longer the constant `ASCII_HEADER_ROWS = 1` §26 had; it's `1` (top border only)
or `3` (top border + the tab's two rows), and only `AGroupFrame` — which alone
decides `hasTab()` — should own that decision. `AGroupFrame.getBodyRowOffset()`
is the new accessor; `GroupingTile` never re-derives the "does the tab fit"
guard itself. Two call sites need it:
- `asciiDimension()` needs the header row count *before* the frame is ever
  drawn (to size the tile), so it builds a throwaway `AGroupFrame` purely to
  read `getBodyRowOffset()` back — the dimension it's built with only needs a
  correct *width* for this query (height is irrelevant, passed as `0`), the
  same "read the size back, don't reimplement the rule" discipline `ANote`
  established (§18/§20).
- `asciiDraw()` builds the real `AGroupFrame` (correct width *and* height) to
  actually draw the frame, then reuses that *same instance*'s
  `getBodyRowOffset()` to start its child-stacking loop — no second throwaway
  needed here, since the real instance already exists.

**Still ATXT-only.** Unlike every other shape added to this package so far
(`ANote`, the box, `AElseSeparator`'s dotted line), the new sides/bottom/fold
characters (`!`, `~`, `_`, `/`) are hardcoded, not chosen through an
`isUnicode()`-aware getter on `InfinitePlan`. This mirrors Arnaud's explicit
sequencing for this session ("on fera le UTXT après") rather than an
oversight — the natural next step, once this ATXT shape is confirmed against
real rendered output, is picking Unicode equivalents for these four characters
(most likely double-line or dashed-line box-drawing glyphs, by analogy with how
`ANote` already distinguishes its Unicode double-line box from the participant
box's single-line one, §16) and threading them through the same
`isUnicode()`-dispatch pattern every other shape uses.

**Not touched: the participant head box.** The mockup's alternate
`._.`/`|a|`/`'+'` style (a `+` junction where the lifeline descends, instead of
today's `,-.`/`|a|`/`` `-' ``) got no preference from Arnaud either way, and
`Participant.asciiDraw()` is shared by every diagram (not just groups), so it
was left exactly as-is for this pass — changing it would ripple into every
existing reference file (`hello1`–`hello3`), not just `hello4`'s. Worth
revisiting as its own, separately-scoped section if/when there's a concrete
reason to.

**Known gaps, carried over from §26/§27 unchanged:** the frame's own
width (title tab, else labels) is still not reserved on the ASCII column
solver (§14); group-level end-notes (`drawNotes()`'s pixel counterpart) are
still not rendered in ASCII — both remain exactly as §26 left them, this
section only redraws the frame's own shape.

**Reference files.** `hello3.atxt`/`hello4.atxt` on disk predate this change
and must be regenerated (`VEGA_FORCE_WRITE`) and eyeballed against the new
shape before they can be trusted as a baseline — same caveat §24 already
recorded for §24's own change, now doubly true.

## 29. Closing the tab's right edge

Arnaud renamed the test to `group.puml` (checked in at
`src/test/resources/vega/asciiverse/group.puml`) and, comparing the generated
`group.atxt` against the mockup, spotted the one piece §28 left incomplete: the
title tab's text row (`! p1` ) had no right edge of its own — the text simply
trailed off into a padding space, with nothing marking where the tab actually
ends. The mockup shows a character there, closing it off.

**Fix:** the text row's trailing padding space becomes `" /"` instead
(`" " + title + " /"`, was `" " + title + " "`) — a first attempt used a bare
`|` there, but Arnaud settled on `" /"` instead. That trailing `/` does **not**
land in the same column as the fold row's own `/` beneath it: the text row is
now one column wider than `tabWidth()` (the width the fold row and the
`hasTab()` fit guard still use), so the text row's `/` sits one column to the
right of, and one row above, the fold row's `/`. The two slashes form a
genuine 45-degree diagonal running across both rows — the ordinary UML frame
cut-corner pentagon look, just traced over two character rows instead of one
continuous line — rather than the straight-edge-then-cut shape a same-column
`|` would have given.

**Why this was easy to miss by eyeballing the pre-fix `group.atxt` alone:**
participant `c`'s lifeline happens to run through the frame at a column close
to the tab (the frame spans `b..c`, and `c`'s lifeline sits near the frame's
right side), so the orchestrator's lifeline-fill pass (§9.2) was already
filling a `|` into the empty cell it found at the tab's text row — at *its own*
column (`c`'s lifeline column), not the tab's. That incidental `|`, sitting a
few columns further right than the tab, could look at a glance like the tab was
already closed; only comparing against the mockup made clear the two `|`
characters are unrelated (one was meant to be the tab's own edge; the other is
participant `c`'s lifeline crossing the header, unaffected either way) and that
the tab's own edge was genuinely missing.

**Reference files.** `group.atxt`/`group.utxt` (the renamed former
`hello4.atxt`/`.utxt`) still need regenerating (`VEGA_FORCE_WRITE`) and
eyeballing against this fix, same outstanding step §28 already flagged.

## 30. `AGroupFrame` in UTXT: a genuinely different shape, not a character swap

§28 explicitly deferred Unicode support for the group frame ("Still
ATXT-only"). Arnaud provided a screenshot of the target UTXT rendering (a
`partition` diagram, generated by the legacy engine) and pointed at
`asciiart.ComponentTextGroupingHeader`/`ComponentTextGroupingBody`/
`ComponentTextGroupingElse`/`ComponentTextGroupingTail` — the old,
soon-to-be-deleted Puma-era components that already draw this exact shape —
as a reference to copy the *idea* from, per the standing rule (§1/§9.1/§12/
§16): read for the characters and the junction logic, never reused as code.

**Confirmed with Arnaud before implementing:** the tab shows the group's own
title (`p1`), not a keyword badge like the screenshot's `PARTITION` —
consistent with §9.1/§28's existing rule that `group` suppresses the keyword.
And the tab's right edge, unlike the legacy rendering (see below), continues
as a divider for the frame's *entire* height, confirmed explicitly.

**Why this isn't just “pick Unicode characters for the existing ATXT
layout,” unlike every other shape in this package.** `ANote`'s two variants
(§16) share one geometry (a box with a corner cut) and differ only in which
characters draw it. The UTXT group frame is a *different layout*, not a
recolored ATXT one: ATXT's tab is a self-contained pentagon with its own top,
left, and diagonal-cut edges, floating with a small margin inside the frame's
own independent border (§28/§29). The UTXT tab instead *shares* the frame's own
top border and left side outright — it draws no top or left edge of its own at
all — and owns only its right edge and its bottom edge, exactly matching how
`ComponentTextGroupingHeader`'s UTXT branch is built (a `╤`/`│`/`┘` vertical
run at the tab's right column, a `╟` junction at its bottom-left where it
meets the frame's own left side, plain `─` for its own bottom). Because the
two layouts don't actually share geometry, `AGroupFrame.asciiDraw()` is now a
thin dispatcher to `asciiDrawAscii()`/`asciiDrawUnicode()` — the same split
`ANote` already uses (§18), chosen for the same reason: past a certain point,
threading `isUnicode()` branches through one method is worse than two methods
that each read cleanly top to bottom.

**The frame itself is double-line** (`╔═╗`/`║`/`╚═╝`), not a
Unicode-ified version of the ATXT `!`/`~` characters. This reuses the exact
same double-line convention `ANote` already established for UTXT note boxes
(§16) — so frames and notes now visually match each other in UTXT, and both
stay visually distinct from participants' single-line boxes (`┌─┐`/`│`) and
lifelines/arrows (also single-line). The ATXT `!` sides and tilde bottom (§28)
were solving the same “stay distinct from a lifeline” problem with a
different tool (dedicated ASCII characters, since ASCII has no double-line
glyphs to borrow) — UTXT gets to reuse an existing, established distinction
instead of inventing one.

**The tab, precisely:** no border of its own on top or left (shares the
frame's `╔`/`═` top and `║` left, drawn as part of the frame itself, with one
substitution — the top border swaps in a `╤` junction, DOWN SINGLE AND
HORIZONTAL DOUBLE, at exactly the divider's column, instead of a plain `═`
there). Text row: `" " + title + " "` (no trailing decoration the way ATXT's
`" /"` needed one, §29 — the box's own `│` supplies the closing edge instead of
a diagonal), followed by a single-line `│` at the divider column. Closing
row: a `╟` junction at column 0 (double-line side meeting a single-line run
heading right — copied straight from the legacy header), a `─` run, and a
`┘` corner at the divider column, closing the tab's own bottom-right.

**The full-height divider, the one deliberate departure from the legacy
rendering.** Re-reading `ComponentTextGroupingHeader`/`Body`/`Tail` closely: the
legacy `│` at the tab's right column only exists for the header's own three
rows; nothing in `Body` or `Tail` repaints it for the rows below, so the
original rendering's divider stops at the tab. Arnaud's confirmed answer
(“toute la hauteur”) is a deliberate improvement over that: `asciiDrawUnicode()`
continues the `│` for every row between the tab's closing row and the bottom
border, which gets its own new junction — `╧`, UP SINGLE AND HORIZONTAL
DOUBLE, the mirror image of the top's `╤` — closing the divider off cleanly
rather than letting it run into (or stop short of) the bottom double line.

**Known gaps, unchanged from §28/§29:** the frame's own width (tab, else
labels) is still not reserved on the ASCII column solver (§14); group-level
end-notes are still not rendered in either format. `AElseSeparator`'s own
Unicode handling is untouched by this section (already Unicode-aware via
`plan.drawHLine(..., true)`, §27) — only the frame itself changed here.

**Reference files.** `group.utxt` needs regenerating (`VEGA_FORCE_WRITE`) and
eyeballing against this shape — it has never had a correct reference to begin
with, unlike `group.atxt` which at least matched the pre-§28 uniform-box
rendering at some point.

## 31. `PartitionTile`: full-width frame and comment-only title (`partition.puml`)

`partition.puml` (two `partition` blocks — `p1` around a `b -> c`/`c --> b`
exchange with a right-attached note, `p2` around an `a -> b` with a
left-attached note) rendered with two visible defects the pixel output does
not have: **no title** on either frame, and a **frame too small** — it wrapped
only the columns the inner messages touched (`b..c` for `p1`, `a..b` for `p2`)
instead of spanning the whole diagram the way a partition does in pixel.

**Root cause — a partition is not a plain group, and `GroupingTile`'s ASCII
path treated it as one.** In the pixel world `PartitionTile` already overrides
three `GroupingTile` methods precisely because a partition lays out
differently: `getComponent()` draws only the comment (`"p1"`, never the
`"partition"` keyword), and `getArea()`/`getWidth()` make the frame span
`getBorder2() - getBorder1()` (the whole diagram, drawn at `getBorder1()`),
not the children's span. The ASCII path (§24/§26/§28) had no such overrides,
so `PartitionTile` inherited `GroupingTile`'s two ASCII layout decisions
wholesale, both wrong for a partition:

- **Title.** `GroupingTile.asciiTitle()` flattens the `display` field, which
  for a non-`group` keyword is `Display.create(start.getTitle(),
  start.getComment())` — i.e. `"partition" + "p1"` → `"partition p1"` (12
  chars). That is both the wrong text (pixel shows only `"p1"`) *and* long
  enough that `AGroupFrame.hasTab()` (`width > title.length() + 2`, §28)
  refused to draw the tab at all for the narrow children-based frame — so the
  title vanished entirely rather than merely reading wrong.
- **Width.** `GroupingTile.asciiFrameColumns()` derives the frame span from
  the children's `getAsciiMinX()`/`getAsciiMaxX()` (± `ASCII_FRAME_MARGIN`),
  which for `p1` is just `b..c`.

**Fix — the two ASCII overrides are the exact counterparts of the pixel ones
above**, the same object-oriented principle as everywhere else in this log
(each tile owns the way it lays itself out; a partition overrides only what
differs from a plain group, in ASCII exactly as in pixel):

- `PartitionTile.asciiTitle()` → `getGroupingStart().getComment()` only,
  mirroring the pixel `getComponent()`'s `Display.create(getGroupingStart()
  .getComment())`. With the frame now full-width (below), `"p1"` easily
  clears the `hasTab()` threshold, so the title tab draws.
- `PartitionTile.asciiFrameColumns()` → the min `getAsciiPosB()` and max
  `getAsciiPosD() - 1` across *every* living space (± `ASCII_FRAME_MARGIN`),
  the ASCII analogue of the pixel `getBorder1()`/`getBorder2()` full-diagram
  span. Computed by iterating all living spaces (like `GroupingTile`'s own
  fallback branch) rather than assuming `getFirstLivingSpace()`/
  `getLastLivingSpace()` are the geometric extremes.

To let `PartitionTile` override them, `GroupingTile.asciiTitle()`,
`asciiFrameColumns()` and the `ASCII_FRAME_MARGIN` constant dropped their
`private`/`private static` modifiers to `protected`. Nothing else in
`GroupingTile` changed, and no pixel-path method was touched — the SVG
output (and its reference) is unaffected; only the two ASCII methods a
partition overrides are new.

**Notes fall just outside the widened frame, by construction — not by luck.**
The right note is drawn at `targetColumn + 2` past the last participant's
lifeline (`CommunicationTileNoteRight.asciiDraw()`); the left note ends just
before the first participant's lifeline (`CommunicationTileNoteLeft`). The
full-width frame's borders land at `firstPosB - 2` / `lastPosD - 1 + 2`, i.e.
inside the participant boxes' own margin — so both notes sit immediately
outside the frame, exactly as they stick out past the partition frame in the
pixel rendering (both images Arnaud provided). The note decorators already
delegate `getAsciiMinX()`/`getAsciiMaxX()` to their wrapped message and do
**not** reserve the note's own width (the standing §14/§15 column-solver gap),
so the notes never pushed the frame wider to begin with — consistent here
rather than a new problem.

**Known gaps, unchanged from §24/§26/§28:** the frame's own width (now the
full-diagram span plus the title tab) is still not reserved on the ASCII
column solver (§14) — moot for a partition, which spans participants that
already exist rather than needing extra room; and group-level end-notes
(`drawNotes()`'s pixel counterpart) are still not rendered — `partition.puml`'s
notes are message-attached, so this isn't exercised.

**Reference files.** `partition.atxt`/`partition.utxt` on disk predate this fix
(they carry the missing-title, too-narrow output) and must be regenerated
(`VEGA_FORCE_WRITE`) and eyeballed against the pixel image before they can be
trusted as a baseline.


