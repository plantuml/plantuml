# YGAUGE — Migration of Teoz Y positioning to the Real constraint solver

Living engineering log. Branch dedicated to the `YGauge` experiment.

## Goal

Make Y symmetric with X in the Teoz sequence diagram engine: replace the
imperative, two-pass `TimeHook`/`callbackY()` vertical positioning with
per-tile `YGauge` intervals (`[min, max]` as `Real`s), solved by the same
constraint system as X. Each tile then knows its own absolute Y and draws
itself — same philosophy as the ASCIIVERSE initiative ("each object draws
itself").

The switch is `YGauge.USE_ME` (currently `true` — flipped in the Phase 5
session below; several mentions further down in this doc still say
"currently false" and are stale, kept for historical trace of when each
section was written), guarding ~29 references across the `teoz` package.

## The problem being solved (why USE_ME exists)

### Legacy architecture (USE_ME = false, INACTIVE since Phase 5)

This is what the engine did before the flip. The code paths described here
(`TimeHook`, `TileParallel`/`mergeParallel`, ghost `EmptyTile`s) are still
PRESENT in the tree — Phase 5's cleanup deletion step has not run yet —
but they no longer execute by default; they're reachable only by manually
setting `USE_ME = false` again.

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

### Current architecture (USE_ME = true, the active path since Phase 5)

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

## Known gaps / hard points (open issues now that USE_ME = true)

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
- **Phase 5 — Flip & clean.** `USE_ME=true` — DONE, the flag is flipped and
  is the active default (see session log below). The "clean" half of this
  phase — deleting TimeHook plumbing in `CommonTile`, `TileParallel` +
  `mergeParallel`, `EmptyTile` spacers, `fillPositionelTiles` Y math, and
  the flag itself — has NOT run yet: these all still exist, now as dead
  legacy branches kept only for a manual `USE_ME=false` fallback and for
  computing `bodyHeight`/height clustering via `mergeParallelCore` (still
  called unconditionally from a few call sites, see `GroupingTile`). Do not
  delete until every "Known gaps" item above is closed and a full pdiff
  pass confirms no remaining call site depends on the legacy path.

## Rules of engagement

- Same discipline as SMETANA.md: no downstream workaround without
  understanding the root cause of a diff.
- One phase per commit series; since the Phase 5 flip, the build must stay
  green with USE_ME=true (now the shipped default) at every step. The old
  rule ("stay green with USE_ME=false") applied while the flag was still
  off during Phases 0–4 and is kept here only for historical context —
  don't follow it literally anymore.
- Claude edits sources via Filesystem at `C:\github\plantuml`; Arnaud
  builds with `gradle build` and runs the visual comparisons.

## Session log

### 2026-07-11 — Fix: standalone `& note ...` ignored parallel chaining (same bug class as GroupingTile)

**Report (with screenshot):** `{start} Alice -> Bob: start` / `& note right
of Bob: starting` / `{end} Bob -> Alice: finish` / `{start} <-> {end}: some
time` — with the note attached via a separate `& note ...` line (as opposed
to the `note right:` suffix form fixed in the previous session), the
`finish` message was pushed well below `start` — not just the `some time`
anchor, the whole diagram's vertical layout was stretched, with the note
sitting between the two arrows instead of beside the first one.

**Root cause: exactly the same bug class as the `GroupingTile` fix, above,
just in a different class.** A standalone note (`Note` event, built into a
plain `NoteTile` — as opposed to a `note right:`/`note left:` suffix on a
message, which builds a `CommunicationTileNoteRight/Left` WRAPPING the
message tile) can also carry `&` (`note.goParallel()`, parsed correctly in
`FactorySequenceNoteCommand`, confirmed). But `NoteTile`'s constructor
never checked `note.isParallel()`: it unconditionally built its gauge as
`YGauge.create(currentY.getMax(), getPreferredHeight())` — sequential
chaining only, same oversight as `GroupingTile` had. So `& note right of
Bob: ...` chained BELOW the `start` message instead of beside it, pushing
every subsequent tile (`finish`, and the `{start}<->{end}` anchor which
reads both tiles' now-inflated gauges) down by the note's height.

**Fix (`NoteTile` constructor):** mirrors `CommunicationTile` exactly —
`getContactPointRelative()` (already defined: the note's own vertical
center) is used as the contact offset, and the gauge is built with
`YGauge.createParallel(currentY, contactRelative, height)` when
`note.isParallel()`, `YGauge.createWithContact(currentY, contactRelative,
height)` otherwise (numerically identical to the old `YGauge.create(...)`
in the non-parallel case — verified `min` lands on the same value via
solver minimization, so plain untouched notes are unaffected).

**Not yet touched:** `NotesTile` (the OVER_SEVERAL / multi-line `Notes`
container, a distinct class from `NoteTile`) was not audited — no
`& notes ...` report so far, but worth checking given the pattern is now
three-for-three (`CommunicationTile*` was ported, `GroupingTile` and
`NoteTile` were not). A quick grep of every `TileBuilder.buildOne` branch
for `currentY.getMax()` used unconditionally (ignoring the event's own
`isParallel()`) would probably find any remaining instances at once,
rather than waiting for each one to be reported individually.

**To verify (Arnaud, after `gradle build`):** the reported diagram should
now render with `start`/`finish` at their original spacing and the note
beside the first arrow, regardless of whether the note is written as a
`note right:` suffix or a separate `& note right of ...` line.

### 2026-07-11 — Fix: LinkAnchor ({start}/{end} <->) misaligned when either end has a note

**Report (with screenshot):** `{start} Alice -> Bob: start` / `note right:
starting` / `{end} Bob -> Alice: finish` / `{start} <-> {end}: some time` —
the `some time` double-arrow (and the note itself) sat lower than expected
as soon as a note was attached to one of the anchored messages.

**Root cause:** `LinkAnchor.drawAnchor`'s USE_ME branch computed each
anchor's Y as the MIDPOINT of the tile's `[min, max]` gauge interval, plus
`getContactPointRelative()`:

```java
y1 = (tile1.getYGauge().getMin().getCurrentValue()
        + tile1.getYGauge().getMax().getCurrentValue()) / 2
        + tile1.getContactPointRelative();
```

But the legacy formula this was meant to reproduce is
`getTimeHook().getValue() + getContactPointRelative()`, and
`getTimeHook()` under USE_ME returns the gauge's `min` (substituted in
`CommonTile.callbackY`, see the Phase 2 session) — not the midpoint. For a
plain, note-less message the midpoint happens to sit close to the real
arrow line purely by coincidence (`contactPointRelative` is roughly half
the tile's own preferred height for an unadorned message), which is why
this went unnoticed until a `note right:`/`note left:` on an anchored
message grows the tile's `max` (note height) without moving the arrow
itself — exposing the gap between the midpoint and the true contact line.
Same class of bug as the earlier note-wrapper contact-propagation fix
(pdiff-adjacent session, above): note wrappers keep `min` identical to the
wrapped message's own `min` specifically so formulas like this stay valid,
but this call site was reading `max` too and averaging, which defeats that
invariant.

**Fix:** `y1`/`y2` are now `tile.getYGauge().getMin().getCurrentValue() +
tile.getContactPointRelative()` — the direct, non-averaged translation of
the legacy formula. Verified the delegation chain for a noted message:
`CommunicationTileNoteRight.getContactPointRelative()` delegates to the
wrapped tile, and its `yGauge.getMin()` is IDENTICAL to the wrapped tile's
own `min` (set in that class's constructor), so the formula lands on the
same Y whether or not a note is attached.

**To verify (Arnaud, after `gradle build`):** the reported diagram should
now render the `some time` anchor arrow at the same Y as before adding the
note (i.e. right at the `start`/`finish` arrows' own height), regardless of
the `note right:` on the first message. Also worth a quick check with a
note on the SECOND ({end}) message instead, and with both ends noted, to
confirm the fix isn't one-sided.

### 2026-07-11 — Fix: whole groups (opt/alt/...) ignored `&` parallel chaining

**Correction to this doc:** `YGauge.USE_ME` is actually `true` in the current
tree (the "currently `false`" line above is stale — left as-is elsewhere in
this doc since it predates several sessions, but flagging it here since it's
directly relevant to this bug).

**Report (with screenshot):** `opt ... end & opt ... end` (whole group
parallel, not a single message) rendered the two `opt` frames stacked
vertically instead of side by side; same for `alt ... end & alt ... end`.
A third case, two sequential `alt/else` blocks with no `&` between them,
rendered correctly (stacked, as expected) in both the reported (buggy) and
reference screenshots — isolating the bug to group-level `&`, not to
`alt/else` rendering in general.

**Root cause:** every message-level tile (`CommunicationTile`,
`CommunicationTileSelf`, `CommunicationExoTile`, via
`YGauge.create`/`createWithContact`/`createParallel`) was ported to chain
from the PREVIOUS gauge's `min` when `isParallel()` is true (top alignment)
and from its `max` otherwise (sequential stacking) — this is the whole
point of the gauge system (see "Goal" and "Target architecture" above).
`GroupingTile` — the tile for a whole `opt`/`alt`/`loop`/... block — was
never ported to make this same distinction: its constructor unconditionally
used `currentY.getMax()` for its own top (`firstY`), regardless of
`start.isParallel()`. So a group following `& opt`/`& alt` always chained
sequentially below the previous group, exactly like a non-parallel one —
indistinguishable in the gauge chain from a plain stacked group. Since
`mergeParallel()` is a no-op under `USE_ME` (Y positioning goes through the
gauge chain instead, not through `TileParallel` clustering — see the
Phase-2 session), nothing else in the pipeline compensates for this.

**Fix (`GroupingTile` constructor):** `firstY` (the group's own top) is now
`currentY.getMin()` when `start.isParallel()`, `currentY.getMax()`
otherwise — mirroring the message-tile tiles exactly, but WITHOUT the
contact-line machinery (`createWithContact`/`createParallel`): a
`GroupingTile`'s `getContactPointRelative()` is hardcoded to `0` (no arrow
line to share), so plain top alignment is the correct and sufficient
semantics here, unlike messages. The group's own `max` still needs to
dominate the PREVIOUS sibling's `max` so a later sequential tile chains
below the WHOLE pair, not just below whichever group happened to be built
second: built as `firstY.addAtLeast(getPreferredHeight())` with an extra
`ensureBiggerThan(previousMax)`, the same two-constraint pattern used by
`YGauge.createParallel` for the same reason (see pdiff report #3 session,
above — `RealMax` must never enter the chain, so this is NOT expressed via
`RealUtils.max`).

**Not yet touched:** `PartitionTile` (the `partition` block, handled by a
separate class from `GroupingTile`) was not audited for the same gap — no
`partition ... & partition ...` case was reported, and I did not want to
change untested code paths in the same pass. Worth checking if a similar
report comes in for partitions.

**To verify (Arnaud, after `gradle build`):** the reported diagram
(`opt & opt`, `alt & alt`, then two independent sequential `alt/else`
blocks) should now render the two `&`-joined pairs side by side, with the
two trailing independent `alt/else` blocks unaffected (they never went
through the parallel branch). Also worth a quick pdiff pass on the existing
parallel-message regression cases (d2, d4) to confirm this change — additive,
only touches `GroupingTile`, gated on `start.isParallel()` — didn't disturb
them.

### 2026-07-11 — Fix: extra space at the bottom of flat (groupless) diagrams with &

**Report:** after confirming the previous alignment fix worked (rebuild
confirmed the earlier misalignment report was indeed a stale build, as
suspected), a new symptom on the SAME diagram: extra blank space between
the last message and the footer participant boxes.

**Root cause: the exact same bug class as the group-height fix (two
sessions ago), but at the level ABOVE `GroupingTile` — in
`fillPositionelTiles` itself.** This method is used by BOTH
`PlayingSpace.drawUInternal` (top-level, returns the value used for the
diagram's total declared height) and, recursively, by every
`GroupingTile`'s own body. Its loop does
`y = new TimeHook(y.getValue() + tile.getPreferredHeight())` over
`mergeParallel(stringBounder, tiles)` — a no-op under USE_ME, so every `&`
member's height is summed separately instead of once per cluster,
inflating the returned `finalY` (hence `PlayingSpace.getPreferredHeight()`
= `max(inkExtent, finalY) + 10`, taller than the actual ink whenever `&` is
used anywhere in the diagram, groups or not).

The loop's OTHER side effects (`callbackY`, `local`/`full` population,
sub-group/sub-parallel recursion) still need to run per INDIVIDUAL flat
tile — and `callbackY` itself, under USE_ME, ignores the accumulated `y`
value entirely already (substitutes the tile's own gauge min — see the
Phase 2 session), so the flat loop's height bookkeeping serves NO purpose
other than producing this one return value.

**Fix:** left the existing loop untouched; added a USE_ME branch that
recomputes the return value from the SAME `mergeParallelCore` clustering
used for `bodyHeight`, applied to the same `tiles` list, added to the
starting Y. No change to per-tile drawing/positioning, no change to
recursive calls (their return value was already discarded by callers).

**Verified:** the reported diagram (5 flat messages, two `&` pairs, no
group) shrank from 739×258 to 739×199 (59px — consistent with two
clusters no longer double-counted). Full regression corpus re-run clean;
all previously-verified arrow alignments unchanged (unaffected by this
fix, which only touches the returned total-height value); the group-height
fix from two sessions ago (d7, groups with `&`) still measures exactly
191×291 — this session's fix is complementary to it (different call site:
`PlayingSpace`'s own top-level call vs `GroupingTile.bodyHeight`), not a
duplicate.

### 2026-07-11 — Investigation: reported misalignment not reproduced; fixed a real (adjacent) gap anyway

**Report:** `&`-parallel pair with `deactivate`/`activate` between the
members and a `note right:` on the second member; screenshot showed
Message1/Message2 and Message3/Message4 not on the same row.

**Could not reproduce.** Cross-checked participant X positions against
arrow Y coordinates in the harness's SVG output for the exact reported
diagram: both pairs land on the exact same Y (99.56 and — after the fix
below — 134.69, previously 163.83). This diagram has no `group`/`alt`, so
the two most recent fixes (group height, group notes) are irrelevant to
it; the relevant fixes are the earlier contact-sharing and OOM-anchoring
ones, both already in place in the harness. Flagged the possibility of a
stale build on Arnaud's side (same class of issue as pdiff report #5) and
asked for confirmation after a clean rebuild.

**Found and fixed a real, related gap anyway:** all note wrapper tiles
(`CommunicationTileNoteRight/Left`, `CommunicationTileSelfNoteRight/Left`,
`CommunicationTileNoteBottomTopAbstract`) built their own `yGauge` via
plain `YGauge.create(currentY.getMax(), getPreferredHeight())` — discarding
contact/origin entirely. Harmless when nothing chains off the wrapper via
`&`, but if a `&` message follows a NOTE-WRAPPED member (note on the FIRST
of a pair, not the second as in the reported diagram), the second member's
`createParallel` finds `contact == null` and falls back to top-alignment,
silently losing contact-line sharing. A same-height coincidence can mask
it entirely (my first attempt at a regression test had this coincidence
and wrongly "passed" even on the unfixed code — worth remembering: a
parallel-alignment test needs DIFFERING label heights between members to
be discriminating at all).

**Fix:** all five wrappers now build their gauge as
`new YGauge(innerGauge.getMin(), innerGauge.getMin().addAtLeast(getPreferredHeight()), innerGauge.getContact(), innerGauge.getOrigin())`
instead of `YGauge.create(...)`: `min` is IDENTICAL to the wrapped tile's
own min (the note never moves the arrow), `max` is anchored via
`addAtLeast` per the CHAIN ANCHORING invariant, and contact/origin pass
through unchanged so a later `&` member still finds and can push the
shared contact line. For `NoteTop`/`NoteBottom`, `getPreferredHeight()`
ADDS the note height rather than taking a max (note stacks above/below
rather than beside) — the min-identity invariant still holds and is what
makes their unusual draw-order composition (documented in an earlier
session) correct.

**Verified with a genuinely discriminating case** (`msg1` + note, `&
msg2` with a 3x taller multi-line label): WITHOUT the fix, arrows land at
y=70.43 and y=115.83 (misaligned, msg1 stuck at its own height, unaware
msg2 needs more room); WITH the fix, both at y=115.83 (msg1 correctly
pushed down to match). Confirmed by literally reverting the fix in the
harness and re-rendering side by side — do this kind of A/B check with an
env-flag or a clean revert-then-rebuild-then-restore sequence, not by
eyeballing; a botched manual revert/restore (a stray failed `cp`) produced
a false result earlier in this same investigation.

Full regression corpus re-run clean; all previously-verified alignments
unchanged, notably d8 (the reported diagram) improved slightly (its second
pair's Y moved from 163.83 to 134.69 — the previously-missing contact
propagation was adding unnecessary padding before the second pair even
though the pair itself was already internally aligned).

### 2026-07-11 — Fix: groups with & messages were too tall (known gap #7, closed)

**Symptom:** a `group`/`alt`/etc. containing a `&` message rendered with
extra vertical whitespace inside the frame, pushing everything after the
group further down than necessary.

**Root cause:** `GroupingTile`'s constructor sums
`bodyHeight += tile.getPreferredHeight()` over `tiles` to size both the
frame's own drawn rectangle (`getTotalHeight`, used by
`ComponentRoseGroupingHeader.drawInternalU`, which draws the border
rectangle at exactly that height) and the group's contribution to the
outer Y chain (`getPreferredHeight()` -> `this.yGauge`). Legacy relied on
`tiles = mergeParallel(...)` actually merging each `&` run into a single
`TileParallel` (whose `getPreferredHeight() = getContactPointRelative() +
getZZZ()`, i.e. tallest-before-contact + tallest-after-contact — counting
the cluster ONCE). Under USE_ME, `mergeParallel` is a no-op (Y positioning
now goes through the gauge chain instead), so `tiles` stays flat and every
`&` member's height is summed SEPARATELY — double-counting overlapping
rows.

**Fix:** extracted the unconditional clustering logic into
`mergeParallelCore` (same body `mergeParallel` used to have, minus the
USE_ME early-return guard). `mergeParallel` itself is unchanged (still a
no-op under USE_ME, still used for Y positioning-adjacent legacy code
paths). `bodyHeight` is now computed from a LOCAL, height-only clustered
view (`mergeParallelCore` applied unconditionally when USE_ME), while the
`tiles` field used for drawing stays flat — no interference with the
correctly-working gauge-based absolute self-positioning.

**Verified (instrumented A/B in the same build, env-flag toggle, not a
file swap — a first attempt via swapping files gave a false negative,
likely a stale compiled directory; worth remembering when comparing
builds by hand):**

| | bodyHeight (per group) | full diagram (2 groups) |
|---|---|---|
| before (flat sum) | 116.53 | 191×407 |
| after (clustered) | 58.27 | 191×291 |

116px saved across the two groups, matching (116.53-58.27)×2 exactly.
Reported diagram now renders tight (harness PNG, visual check). Full
regression corpus re-run clean; parallel contact-line Y values unchanged
(d2: 85.56; d4: 115.83/144.96).

**Remaining Phase 3 gaps (unchanged):** group background (Blotter) still
never drawn under USE_ME; group margins (legacy 4px ghost EmptyTiles)
still unverified pixel-wise.

### 2026-07-11 — Phase 3 start: group-end notes + else-line width overflow

**Report:** nested `alt`/`else` with `note right:` attached right after each
`end` — the notes vanish, and the else divider lines overflow past the
visible group frame (to the right, where the note would have been).

**Root cause 1 (notes lost):** `GroupingTile.drawU`'s USE_ME branch drew
the header and children but never called `drawNotes(ug)` — unlike the
legacy branch. Trivial: `drawNotes` needs no TimeHook/callback state (uses
the frame's own `min`/`max` fields, absolute X), so it only needed to be
called with `ug` translated to the frame's Y origin.

**Root cause 2 (else line overflow) — more interesting:**
`ComponentRoseGroupingElse.drawInternalU` draws the else separator as
`ULine.hline(area.width)` — the line IS exactly the Area's width, so any
width error shows up directly as a visible overflow. `ElseTile.drawU`
(written in an earlier YGauge session) sized that Area from
`parent.getMinX()/getMaxX()`. But `GroupingTile.getMinX()/getMaxX()`
deliberately reserve extra room for notes attached to the group's `end`
(`+ getNotesWidth(..., RIGHT/LEFT)`) so that the PARENT lays out enough
space around the frame — they are the group's OUTER footprint, not its
visible border. The legacy `drawAllElses` never used them; it drew
strictly from the group's raw `min`/`max` fields (the actual frame
border). `ElseTile.drawU` used `getMinX()/getMaxX()` instead (offset by
±`EXTERNAL_MARGINX1/2`, which cancels the margin term but not the notes-
width term), so whenever a note was attached to `end`, the else line
extended past the frame by exactly that note's width — matching the
reported overflow being on the side with the note.

**Fix:**
- `GroupingTile`: added package-private `getFrameMinX()`/`getFrameMaxX()`
  returning the raw `min`/`max` fields (the actual border), distinct from
  the public `getMinX()/getMaxX()` (outer footprint incl. notes reservation
  — used by the PARENT's own layout, unchanged).
  `drawU`'s USE_ME branch now also calls `drawNotes(...)`.
- `ElseTile.drawU`: sizes its Area from `((GroupingTile) parent)
  .getFrameMinX()/getFrameMaxX()` directly (no more ±EXTERNAL_MARGINX
  arithmetic on the wrong base).

**Verified:** the reported diagram (nested alt/else with end-notes) now
shows both notes and correctly bounded else separators (harness PNG
render, visual check). Full regression corpus re-run clean; parallel
contact-line Y values unchanged (d2: 85.56; d4: 115.83/144.96).

**Remaining Phase 3 gaps (still open, not touched this session):**
- Group background (`Blotter` / `drawCompBackground`) is still never
  called under USE_ME — colored `alt`/`else` sections show no background
  fill yet. Needs the same `getFrameMinX/MaxX` treatment plus a
  gauge-based per-else ypos (mirrors `drawAllElses`'s ys-list logic,
  already gauge-based since the earlier prologue fix).
- Else height stretching down to the next else / group bottom: the
  `ComponentRoseGroupingElse` only draws a thin separator strip
  (`getPreferredHeight` = text height + 16), so this turned out to be
  LESS urgent than first assumed in the Phase 0 write-up — there is no
  visible "section body" to stretch, just the dividers, which are now
  correctly positioned and bounded. Revisit only if a colored else
  background is added (Blotter needs the full section height to fill).
- Group margins (the legacy 4px ghost EmptyTiles) still unverified
  pixel-wise against this fix.

### 2026-07-11 — OOM / O(n^2): chain links must be anchored moveables (pdiff report #6)

**Symptom:** `OutOfMemoryError: Java heap space` on a large diagram (45k
lines), with a stack trace made almost entirely of repeated
`RealDelta.addAtLeast(RealDelta.java:55)` frames.

**First hypothesis (WRONG, but partially useful):** debug-name
concatenation. `RealDelta`'s ctor built its name as
`"[Delegated {" + delegated.getName() + "} ...]"` and
`RealImpl.addAtLeast` as `getName() + ".addAtLeast" + delta`, so each name
embedded its whole ancestry: O(n^2) retained characters along a long
chain. Fixed (names are now O(1); identity for debugging still comes from
`RealMoveable`'s atomic counter `#123_...`). This alone did NOT fix the
OOM — good hygiene, wrong root cause.

**Second hypothesis (WRONG, worth recording):** too many forces / a
degenerate `RealLine.compile()`, hence the idea of deduplicating forces or
replacing `List<PositiveForce>`. MEASURED AND DISPROVED: instrumenting
`compile()` gives, for n=400 messages, `forces=404 passes=2 ms=2`. The
force list is linear in n and the relaxation converges in two passes. The
solver is NOT the bottleneck; no dedup / no new data structure is needed.
(Keeping this negative result so the idea is not re-attempted.)

**Actual root cause (found by stack sampling):** the hot spot is
`RealDelta.addAtLeast`, called from `YGauge.createWithContact`. Look at
the two implementations:

```java
// RealImpl: O(1), creates one anchored moveable
public Real addAtLeast(double delta) {
    final RealImpl result = new RealImpl(..., this.currentValue + delta);
    getLine().addForce(new PositiveForce(this, result, delta));
    return result;
}

// RealDelta: delegates DOWNWARD, rebuilding one RealDelta per layer
public Real addAtLeast(double delta) {
    return new RealDelta(delegated.addAtLeast(delta), diff);
}
```

The gauges chained tile n onto tile n-1 through a `max` built with
`min.addFixed(height)` — i.e. a `RealDelta`. So every tile added one more
`RealDelta` layer, and the `origin.addAtLeast(...)` of tile n recursed
through all n layers below, allocating n new RealDeltas on the way:
**O(n^2) objects and O(n) recursion depth**, which is exactly the observed
OOM and the repeated-frame stack.

**Fix — CHAIN ANCHORING invariant (documented in YGauge):** anything
carried from one gauge to the next (the `max`, which becomes the next
tile's `origin`) MUST be an anchored moveable (`RealImpl`), never a
`RealDelta`. Concretely, in `create`, `createWithContact`,
`createParallel` and `createPropagating`, the `max` is now built with
`addAtLeast` (on the contact, itself a `RealImpl`) instead of `addFixed`.
`addFixed` remains fine for LEAF values nothing chains onto (the `min`,
read only for drawing).

Note `addAtLeast(height - contactRelative)` from the contact is
semantically the same lower bound as `addFixed(height)` from the min
(min = contact - contactRelative), and solver minimization makes it an
equality in the absence of other constraints.

**Measured (harness, -Xmx512m):**

| n messages | before | after |
|---|---|---|
| 100 | 1666 ms | 1484 ms |
| 400 | 9725 ms | 2031 ms |
| 1600 | OOM / timeout | 3821 ms |
| 6000 | OOM | 10.7 s (2.3 MB SVG) |
| 20000 | OOM | 50.8 s (7.6 MB SVG) |

Quadratic → near-linear. Full regression corpus re-run: all pass, and the
parallel arrow-line Y values are bit-identical to before the fix (d2:
85.56; d4: 115.83 / 144.96), so contact-alignment semantics are preserved.

**Lesson:** in the `real` package, `addFixed` produces a `RealDelta`
(a lazy view, cheap to make but expensive to build ON), while `addAtLeast`
produces a `RealImpl` (an anchored moveable, O(1) to build on). Never grow
a long chain through `RealDelta`.

### 2026-07-11 — Hardening: ULP-scaled tolerance + stale-build suspicion (pdiff report #5)

**Report:** same `Infinite Loop?` signature on a sprite-heavy diagram
(`<$circle,scale=2>`, `<:rocket:scale=2>`, `<&box,scale=2>`), with Arnaud
reporting the previous epsilon fix "changes nothing".

**Local repro:** rebuilt the harness (clone + branch teoz files + fix),
this exact diagram renders cleanly with the 1e-6 fix already in place —
no second bug found. The magnitudes in the reported force dump (66.0,
48.25, 29.83…) have an absorption threshold many orders of magnitude
below 1e-6, so the fix should categorically prevent this trace.

**Suspicion raised:** the crash report header reads literally
`PlantUML ($version$) has crashed` (unsubstituted placeholder) and the
stack traces through `zdev.Test_0.testExecute` via a raw JUnit
launcher — consistent with a test run against stale/uncompiled classes
rather than a full Gradle build+jar. Asked Arnaud to confirm a clean
rebuild picked up the `PositiveForce.java` change before investigating
further as a distinct bug.

**Hardening applied regardless:** the fixed 1e-6 tolerance is fine for
ordinary diagrams but has no formal guarantee of dominating the
accumulated absorption error on very large ones (many chained forces
compounding rounding). Replaced with a magnitude-scaled tolerance:

```java
final double epsilon = Math.max(0.000001,
        1000 * Math.ulp(Math.max(Math.abs(movingPointValue), Math.abs(fixedPointValue))));
if (diff >= -epsilon) { ... }
```

1000 ULPs of the larger operand is far above any single-addition rounding
error yet still many orders of magnitude below one pixel at any diagram
size. Falls back to the flat 1e-6 for values near zero (where ULP is
tiny) so tiny diagrams keep the same guarantee as before.

**Re-validated full regression corpus** (diag/cog+wifi+newpage, delay,
parallel contact, group+parallel, inverse-parallel+activate, this sprite
case): all six render cleanly with the hardened epsilon.

### 2026-07-11 — Crash fix: floating-point absorption in PositiveForce (pdiff report #4)

**Symptom:** `IllegalStateException: Infinite Loop?` in `RealLine.compile`
(yorigin) on a plain sequential diagram (`<&cog>` labels + `newpage`, no
parallel at all) — theoretically impossible, the Y chain is strictly
forward (`addAtLeast` forces only, no cycle).

**Investigation:** reproduced locally (harness: GitHub master clone + the
branch's modified teoz files + snapshot jar on the classpath, compiled
with javac; the crashing force dump from `RealLine.printCounter` +
instrumented `PositiveForce.apply`). The smoking gun:

```
moving val=78.06510416666666  fixed fval=56.932291666666664  min=21.1328125
-> move 7.105427357601002E-15    (forever)
```

**Root cause: floating-point absorption in the fixed-point iteration.**
Floating addition is not associative: a chained value initialized as
`((x+a)+b)` can differ by a few ULPs from the later constraint check
`(x') + b`. Here the violation was 7.1e-15, but the ULP of 78.06 is
~1.4e-14: `currentValue += 7.1e-15` does not change a single bit. The
force detects `diff < 0`, moves, nothing changes, fires forever. This is
a LATENT ENGINE BUG (strict `diff >= 0` comparison) that the X engine
never happened to trigger in years; the Y contact chains (long addAtLeast
sequences whose initial values are computed before upstream moves) expose
it deterministically.

**Fix:** tolerance in `PositiveForce.apply`: `if (diff >= -0.000001)`.
One microdot of slack, invisible at any zoom, guarantees convergence.
NOTE: this touches the SHARED real engine (X constraints too) — pdiff
must re-verify legacy USE_ME=false full-corpus iso (expected strictly
identical: a force satisfied within 1e-6 was producing sub-ULP moves
anyway).

**Validated in the local harness:** the crashing diagram renders; delay
case (report #1), parallel contact case (report #2, single shared arrow
line y=85.56), group+parallel case (report #3), and the inverse-parallel
case (& message with the TALLER 4-line label + activate + following
message: both arrows share y=115.83, next message at y=144.96 below the
whole group) all pass.

**Tooling note:** a local repro rig now exists (clone master, drop in the
branch's teoz files, compile teoz+real with javac against
plantuml-SNAPSHOT.jar, shadow via classpath order) — rebuildable in
minutes, very effective for force-level debugging with
`RealLine.printCounter`.

### 2026-07-11 — Crash fix: RealMax must never enter the gauge chain (pdiff report #3)

**Symptom:** `UnsupportedOperationException` at `RealMax.addAtLeast` ←
`YGauge.createWithContact` ← `CommunicationTile.<init>`, on a group
containing `&` messages: the tile FOLLOWING a parallel member chains from
`currentY.getMax()`, which was a `RealMax` (built by `createParallel` via
`RealUtils.max`).

**Root cause (two-fold, the second one is the nasty one):**
1. `RealMax` implements neither `addAtLeast` nor `ensureBiggerThan` —
   immediate crash when chained.
2. Even if it did, `RealMax.getCurrentValueInternal` CACHES its value on
   first read. `PositiveForce.apply()` re-reads its fixed point at every
   iteration of `RealLine.compile()`: a `RealMax` used as a force source
   would freeze a stale value mid-solve → silently wrong layout. `RealMax`
   / `RealMin` are post-compile, read-only combinators by design (the X
   engine only reads them after `compileNow`).

**Fix:** in `createParallel`, the group max is now a proper moveable Real:
`max = min.addAtLeast(height)` + `max.ensureBiggerThan(currentY.getMax())`.
Solver minimization computes the same maximum as `RealUtils.max`, while
staying chainable and cache-free. Verified through the `RealDelta`
delegation chain: `addAtLeast` preserves the offset and creates an
independent moveable (pushing the group max does not move the contact
line), `ensureBiggerThan`/`move` delegate down to the underlying
`RealImpl`. All forces point forward: no cycle, `compile()` terminates.

**Lesson (real package):** in a chained/constrained context, only use
`addFixed` / `addAtLeast` / `ensureBiggerThan` on moveables.
`RealUtils.max()/min()` are for post-compile reads only. A `RealMax`
reaching a force is a silent-corruption hazard, not just a crash hazard.

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
