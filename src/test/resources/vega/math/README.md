# Vega tests of the formulas (`@startmath`, `@startlatex`, `<math>`, `<latex>`)

Each `.puml` is one test of `test.vega.VegaTest`, with its reference next to it
(`.svg`, and `.tex` for TikZ, `.pdf`, `.utxt`, `.atxt` where the header asks for
them). Everything is drawn by the renderer of this repository
(JLaTeXMath as paths on a `UGraphic`), so these references need no external
jar. The SVG references have `decimal: 1`.

| Directory | What it checks |
|-----------|----------------|
| (here) | the first corpus of phase 0: one formula per file, the PNG baselines of the original renderer are in `../../jlatexmath-corpus` |
| `asciimath/` | the AsciiMath syntax (`@startmath`): fractions, roots, sums, integrals, limits, matrices, cases, Greek, relations, sets, logic, arrows, accents, fonts, text, functions, brackets, symbols, colors |
| `latex/` | the LaTeX of JLaTeXMath, by theme: one file has several constructs on a line, so that a regression shows on a family of symbols |
| `inline/` | `<math>` and `<latex>` in Creole, in the diagrams: where a label can be, with colors, styles, skinparams (`monochrome`, `handwritten`, `dpi`), `scale`, several pages |
| `options/` | `color`, `backgroundcolor`, `scale`, `dpi` of `@startmath` and `@startlatex`, also wrong values |
| `errors/` | a formula that cannot be made: its source is shown as it is |
| `cjk/` | text that no TeX font can draw (Chinese, Japanese, Korean, Arabic, emoji): drawn with the font of the diagram |
| `formats/` | PDF, TikZ (`output: latex`) and text outputs of a formula |

To make or change a test, write the `.puml` with its header and run Vega with
`VEGA_FORCE_WRITE=true` (see `VegaTest`), then look at the result: a reference
that was never looked at only freezes what the code does. A quick way to look is
to render the same files to PNG (`java -jar plantuml.jar -tpng *.puml`).

Known behaviours of the library, kept in the references: in `cases`, the text
after `&` is not separated from the preceding formula; `\cancel`, `\color`
(as a switch), `\xleftrightarrow`, `\framebox`, `\hrule`, `\hskip`, `\textsl`,
`\emph`, `\dotsm` and a few others do not exist in JLaTeXMath 1.0.7; `%` and
`&` in a quoted AsciiMath text are TeX characters.
