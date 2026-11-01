# Math / LaTeX corpus (phase 0 of PLAN.md)

This directory is deliberately **outside** `src/test/resources/vega`: Vega runs
every `.puml` it finds there, and for a file without YAML header it writes an
`.svg` next to it at each run (not asserted). Until the new renderer exists,
that would only freeze the Batik-less fallback described below.

Formulas used to compare the current rendering (JLaTeXMath 1.0.7 through
reflection) with the future vendored and canvas-based renderer.

- `math-*.puml`: `@startmath` (AsciiMath)
- `latex-*.puml`: `@startlatex`
- `inline-*.puml`: `<math>` and `<latex>` in a class label, a note and a
  sequence message

## Moving it to Vega (phase 2)

Move the files to `src/test/resources/vega/math/`, add the YAML header
(`output: svg`), generate the references with `VEGA_FORCE_WRITE=true`, review
them, and run again without the flag.

## Why no SVG reference yet

On the JVM the SVG of a formula is produced through Batik, which is **not** a
dependency of the Gradle build. Without Batik, `ScientificEquationSafe` falls
back to the formula source as monospace text (for example `x^2`), so an SVG
reference generated without Batik would freeze the fallback, not the formula.

## Baseline

`baseline-jvm-png/` contains the PNG of every file, generated with the current
implementation (JLaTeXMath 1.0.7 built from the upstream tag, headless AWT,
`-tpng`). They are for visual review only, they are not asserted.
They are also the reference to look at when checking the layout of the new
renderer (same positions, same sizes, glyph shapes may differ slightly).

Known upstream behaviour kept in the baseline: in `latex-cases`, the text after
`&` is not separated from the preceding `-x` (`-xotherwise`).
