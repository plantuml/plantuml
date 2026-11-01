# Math / LaTeX corpus: PNG baselines of the original renderer

The 19 formulas (`@startmath`, `@startlatex`, `<math>` and `<latex>` in a class
label, a note and a sequence message) are Vega tests since step 2c of
`PLAN.md`: see `src/test/resources/vega/math/`, with their SVG references made
by the new renderer (JLaTeXMath drawn as paths on a `UGraphic`).

This directory only keeps `baseline-jvm-png/`: the PNG of every formula as the
**original** renderer made it (JLaTeXMath 1.0.7 built from the upstream tag,
headless AWT, `-tpng`), before phase 2 of `PLAN.md`. They are not asserted,
they are the reference to look at when reviewing the rendering of the new
renderer (same layout and sizes; the pixels differ because AWT hints the thin
strokes and the paths are not hinted, a choice of step 2d: see `PLAN.md`).

Known upstream behaviour kept in the baseline: in `latex-cases`, the text after
`&` is not separated from the preceding `-x` (`-xotherwise`).

Two of the three `inline-*` baselines (`inline-class-label`: 145x114,
`inline-note`: 297x86) have another size than what the current code gives
(148x122 and 280x93), because of the text around the formula. The cause was not
looked for (another machine and other fonts is the likely one); it is not step
2c: the code just before it gives 148x122 and 280x93 too.
