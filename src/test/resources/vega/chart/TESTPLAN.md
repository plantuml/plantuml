# Chart diagram - Vega test plan

## Objective

Provide requirement-based non-regression tests for the chart diagram syntax (`@startchart` / `@endchart`).
Each test is a single `.puml` file, rendered to SVG by the Vega harness (`test.vega.VegaTest`), and compared with the committed reference `.svg`.

## Requirement groups

| Group | Topic |
|-------|-------|
| R01 | Series (bar, line, area, scatter, labels) |
| R02 | Axes (h-axis, v-axis, v2-axis) |
| R03 | Layout (legend, grid) |
| R04 | Style |
| R05 | Annotation |
| R06 | Preprocessor |
| R07 | Errors |
| R08 | Missing values (`interpolate`, `connect`) |
| R09 | Default vertical range |
| R10 | Drawing order of series |
| R11 | Scatter label colour |

File names follow `Rnn.xx-<group>-<slug>.puml`; all files are flat in this directory.

## Execution notes

- Run `test.vega.VegaTest` from the repository root (see `CLAUDE.md`).
- Generate or refresh references with `VEGA_FORCE_WRITE=true`, review the `.svg` files, then re-run without the flag.
- `vega.json`, `vega-summary.txt` and `vega-summary.md` are rewritten on every run and must be reverted.
- R07.01 to R07.04 declare `expected-status: 400`: the parser rejects the chart or the renderer reports an error, and the error image is the reference.
  Only R07.02 reports a chart-specific message. For R07.01, R07.03 and R07.04 the chart command regex does not match,
  so the parser falls back to the default welcome image instead of a dedicated error message.
- R07.05 intentionally has no `@endchart`: no diagram block is found, which the harness cannot render,
  so the file uses `allow-failure: true` (reported as skipped) and has no reference `.svg`.

## Test matrix

| Requirement | Test ID | File | Expected Result |
|-------------|---------|------|-----------------|
| R01 Series | R01.01 | `R01.01-series-bar-basic.puml` | Renders a single bar series |
|  | R01.02 | `R01.02-series-bar-grouped.puml` | Two bar series side by side |
|  | R01.03 | `R01.03-series-bar-stacked.puml` | Two bar series stacked |
|  | R01.04 | `R01.04-series-bar-horizontal.puml` | Horizontal bars |
|  | R01.05 | `R01.05-series-line-basic.puml` | Renders a line series |
|  | R01.06 | `R01.06-series-line-coordinate-pair.puml` | Line from (x,y) pairs on numeric h-axis |
|  | R01.07 | `R01.07-series-area-basic.puml` | Renders an area series |
|  | R01.08 | `R01.08-series-scatter-style-with-stereotypes.puml` | Circle, square and triangle markers |
|  | R01.09 | `R01.09-series-mixed-types.puml` | Bar, line and area in one chart |
|  | R01.10 | `R01.10-series-data-labels.puml` | Values shown on bar and line |
| R02 Axes | R02.01 | `R02.01-axes-h-axis-spacing.puml` | Titled categorical axis with spacing |
|  | R02.02 | `R02.02-axes-v-axis-ticks.puml` | Custom ticks with labels |
|  | R02.03 | `R02.03-axes-v-axis-negative.puml` | Negative range and values |
|  | R02.04 | `R02.04-axes-v2-axis.puml` | Secondary axis used by a line |
|  | R02.05 | `R02.05-axes-label-position.puml` | label-right and label-top |
| R03 Layout | R03.01 | `R03.01-layout-legend-bottom.puml` | Legend placed at the bottom |
|  | R03.02 | `R03.02-layout-grid-both.puml` | Grid on both axes |
|  | R03.03 | `R03.03-layout-grid-v-scatter.puml` | Grid on v-axis of a scatter chart |
| R04 Style | R04.01 | `R04.01-style-block.puml` | chartDiagram style applied |
|  | R04.02 | `R04.02-style-stereotypes.puml` | Stereotype-based style on a bar |
| R05 Annotation | R05.01 | `R05.01-annotation-arrow.puml` | Annotation with arrow |
| R06 Preprocessor | R06.01 | `R06.01-preproc-procedure.puml` | Procedure expands to a bar |
|  | R06.02 | `R06.02-preproc-function.puml` | Function result used as data |
| R07 Error | R07.01 | `R07.01-error-coordinate-pair-with-categorical-axis.puml` | Status 400 (see notes) |
|  | R07.02 | `R07.02-error-coordinate-out-of-range.puml` | Status 400, message `X-coordinate 50.0 is outside h-axis range [0.0, 10.0]` |
|  | R07.03 | `R07.03-error-mixed-formats.puml` | Status 400 (see notes) |
|  | R07.04 | `R07.04-error-unknown-keyword.puml` | Status 400 (see notes) |
|  | R07.05 | `R07.05-error-missing-endchart.puml` | No diagram block (see notes) |
| R08 Missing values | R08.01 | `R08.01-missing-minimal-line-interpolate.puml` | Minimal line with `interpolate` |
|  | R08.02 | `R08.02-missing-value-interpolate.puml` | Missing value interpolated |
|  | R08.03 | `R08.03-missing-value-interpolate-labels.puml` | Interpolate with inline colour and labels |
|  | R08.04 | `R08.04-missing-values-interpolate-connect.puml` | Interpolate and connect over two missing values |
| R09 Default range | R09.01 | `R09.01-range-line-default.puml` | Line, default v range (issue #2956) |
|  | R09.02 | `R09.02-range-line-explicit.puml` | Line, explicit `v-axis 5 -> 40` |
|  | R09.03 | `R09.03-range-line-above-100.puml` | Line, values above 100 |
|  | R09.04 | `R09.04-range-bar-default.puml` | Bar, default v range |
|  | R09.05 | `R09.05-range-bar-above-100.puml` | Bar, values above 100 |
|  | R09.06 | `R09.06-range-line-missing-value.puml` | Line with missing value, default range |
| R10 Drawing order | R10.01 | `R10.01-order-bar-then-area.puml` | Bar defined before area (issue #2973) |
|  | R10.02 | `R10.02-order-area-then-bar.puml` | Area defined before bar |
| R11 Scatter labels | R11.01 | `R11.01-scatter-labels-inline-color.puml` | Labels with inline colour (PR #2974) |
|  | R11.02 | `R11.02-scatter-labels-theme.puml` | Labels with `!theme amiga` (PR #2974) |

Note: R09, R10 and R11 record current rendering for open enhancement requests; their references will change when those issues are fixed. R11 references still show black labels until PR #2974 is merged.
