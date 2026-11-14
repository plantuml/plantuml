# Chart diagram — Vega test plan

## Objective

Provide requirement-based non-regression tests for the [chart diagram](../../../../main/java/net/sourceforge/plantuml/chart/) syntax (`@startchart` / `@endchart`).

> [!TIP]
>  Each test is a single `.puml` file, rendered to SVG by the Vega harness (`test.vega.VegaTest`), and compared with the committed reference `.svg`.
>  The R18 tests are rendered as text instead, and compared with the committed `.utxt` and `.atxt`.

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
| R11 | Horizontal orientation |
| R12 | Short series title without quotes |
| R13 | Zero values |
| R15 | Pie (`pie`, `a-axis`, `labels`, `percent`, `donut`) |
| R16 | Radar (`radar`, `a-axis`, `r-axis`) |
| R17 | Errors of pie and radar |
| R18 | Text output (`utxt`, `atxt`) |

File names follow `Rnn.xx-<group>-<slug>.puml`; all files are flat in this directory.

## Test matrix

| Requirement | Test ID | File | Expected Result | Request or Issue |
|-------------|---------|------|-----------------|:----------------:|
| R01 — Series | R01.01 | `R01.01-series-bar-basic.puml` | Renders a single bar series |
|  | R01.02 | `R01.02-series-bar-grouped.puml` | Two bar series side by side |
|  | R01.03 | `R01.03-series-bar-stacked.puml` | Two bar series stacked |
|  | R01.04 | `R01.04-series-bar-horizontal.puml` | Horizontal bars |
|  | R01.05 | `R01.05-series-line-basic.puml` | Renders a line series |
|  | R01.06 | `R01.06-series-line-coordinate-pair.puml` | Line from (x,y) pairs on numeric h-axis |
|  | R01.07 | `R01.07-series-area-basic.puml` | Renders an area series |
|  | R01.08 | `R01.08-series-scatter-style-with-stereotypes.puml` | Circle, square and triangle markers |
|  | R01.09 | `R01.09-series-mixed-types.puml` | Bar, line and area in one chart |
|  | R01.10 | `R01.10-series-data-labels.puml` | Values shown on bar and line |
| R02 — Axes | R02.01 | `R02.01-axes-h-axis-spacing.puml` | Titled categorical axis with spacing |
|  | R02.02 | `R02.02-axes-v-axis-ticks.puml` | Custom ticks with labels |
|  | R02.03 | `R02.03-axes-v-axis-negative.puml` | Negative range and values |
|  | R02.04 | `R02.04-axes-v2-axis.puml` | Secondary axis used by a line |
|  | R02.05 | `R02.05-axes-label-position.puml` | label-right and label-top |
|  | R02.06 | `R02.06-axes-v-axis-title-wide-labels.puml` | v-axis title clear of wide tick labels (70000) | [#2618](https://github.com/plantuml/plantuml/issues/2618)
|  | R02.07 | `R02.07-axes-v2-axis-title-wide-labels.puml` | Same on the v2-axis, with the legend on the right |
|  | R02.08 | `R02.08-axes-v-axis-title-custom-ticks.puml` | Same with wide custom tick labels |
| R03 — Layout | R03.01 | `R03.01-layout-legend-bottom.puml` | Legend placed at the bottom |
|  | R03.02 | `R03.02-layout-grid-both.puml` | Grid on both axes |
|  | R03.03 | `R03.03-layout-grid-v-scatter.puml` | Grid on v-axis of a scatter chart |
|  | R03.04 | `R03.04-layout-grid-h-categories.puml` | h-axis grid: one line per category, at its tick | [#2636](https://github.com/plantuml/plantuml/issues/2636)
|  | R03.05 | `R03.05-layout-grid-v-ticks.puml` | v-axis grid: one line per v-axis tick (5 intervals) |
|  | R03.06 | `R03.06-layout-grid-spacing-custom-ticks.puml` | Grid follows h-axis `spacing` and v-axis custom ticks |
|  | R03.07 | `R03.07-layout-grid-horizontal-categories.puml` | Horizontal chart, `v-axis [A, B, C] grid` |
|  | R03.08 | `R03.08-layout-grid-horizontal-values.puml` | Horizontal chart, `h-axis 0 --> 100 grid` |
| R04 — Style | R04.01 | `R04.01-style-block.puml` | chartDiagram style applied |
|  | R04.02 | `R04.02-style-stereotypes.puml` | Stereotype-based style on a bar |
| R05 — Annotation | R05.01 | `R05.01-annotation-arrow.puml` | Annotation with arrow |
| R06 — Preprocessor | R06.01 | `R06.01-preproc-procedure.puml` | Procedure expands to a bar |
|  | R06.02 | `R06.02-preproc-function.puml` | Function result used as data |
| R07 — Error | R07.01 | `R07.01-error-coordinate-pair-with-categorical-axis.puml` | Status 400 (see notes) |
|  | R07.02 | `R07.02-error-coordinate-out-of-range.puml` | Status 400, message `X-coordinate 50.0 is outside h-axis range [0.0, 10.0]` |
|  | R07.03 | `R07.03-error-mixed-formats.puml` | Status 400 (see notes) |
|  | R07.04 | `R07.04-error-unknown-keyword.puml` | Status 400 (see notes) |
|  | R07.05 | `R07.05-error-missing-endchart.puml` | No diagram block (see notes) |
| R08 — Missing values | R08.01 | `R08.01-missing-minimal-line-interpolate.puml` | Minimal line with `interpolate` |
|  | R08.02 | `R08.02-missing-value-interpolate.puml` | Missing value interpolated |
|  | R08.03 | `R08.03-missing-value-interpolate-labels.puml` | Interpolate with inline colour and labels |
|  | R08.04 | `R08.04-missing-values-interpolate-connect.puml` | Interpolate and connect over two missing values |
|  | R08.05 | `R08.05-missing-value-scatter.puml` | Scatter with a missing value: no marker at index 2 | [#3008](https://github.com/plantuml/plantuml/issues/3008)
|  | R08.06 | `R08.06-missing-value-scatter-h-axis-range.puml` | Same, values spread over `h-axis 0 -> 10`, with labels |
| R09 — Default range | R09.01 | `R09.01-range-line-default.puml` | Line, default v range  | [#2956](https://github.com/plantuml/plantuml/issues/2956)
|  | R09.02 | `R09.02-range-line-explicit.puml` | Line, explicit `v-axis 5 -> 40` |
|  | R09.03 | `R09.03-range-line-above-100.puml` | Line, values above 100 |
|  | R09.04 | `R09.04-range-bar-default.puml` | Bar, default v range |
|  | R09.05 | `R09.05-range-bar-above-100.puml` | Bar, values above 100 |
|  | R09.06 | `R09.06-range-line-missing-value.puml` | Line with missing value, default range |
|  | R09.07 | `R09.07-range-bar-stacked.puml` | Stacked bars: the range holds the sums (0 -> 150) |
|  | R09.08 | `R09.08-range-horizontal-above-100.puml` | Horizontal bar above 100 stays inside the plot |
|  | R09.09 | `R09.09-range-bar-negative.puml` | Negative value: -20 -> 80, 0 on a tick |
|  | R09.10 | `R09.10-range-v2-axis-after-series.puml` | `v2-axis` written after the series that uses it |
|  | R09.11 | `R09.11-range-bar-decimal.puml` | Values below 1: 0 -> 1, ticks 0.2 apart |
| R10 — Drawing order | R10.01 | `R10.01-order-bar-then-area.puml` | Bar defined before area issue | [#2973](https://github.com/plantuml/plantuml/issues/2973) 
|  | R10.02 | `R10.02-order-area-then-bar.puml` | Area defined before bar: bars on top |
|  | R10.03 | `R10.03-order-area-then-line.puml` | Area defined before line: line on top |
|  | R10.04 | `R10.04-order-mixed-groups.puml` | Areas drawn together at the first area, bars at the first bar |
| R11 — Horizontal | R11.01 | `R11.01-horizontal-grouped.puml` | Horizontal grouped bars |
|  | R11.02 | `R11.02-horizontal-stacked.puml` | Horizontal stacked bars |
|  | R11.03 | `R11.03-horizontal-negative-value.puml` | Horizontal bar with a negative value |
|  | R11.04 | `R11.04-horizontal-area-legend-right.puml` | Horizontal areas with spacing and legend right |
| R12 — Short title | R12.01 | `R12.01-short-title-without-quote.puml` | `bar bar_title [...]` names the series `bar_title` | Part 3. of [#2632](https://github.com/plantuml/plantuml/issues/2632)
|  | R12.02 | `R12.02-short-title-all-series.puml` | Unquoted names on bar (with stereotype), line, area and scatter |
|  | R12.03 | `R12.03-short-title-needs-blank.puml` | Status 400: `barfoo [1, 2]` is not a bar named `foo` |
| R13 — Zero value | R13.01 | `R13.01-zero-value-vertical.puml` | Grouped bars with a zero value, vertical |
|  | R13.02 | `R13.02-zero-value-horizontal.puml` | Same chart, horizontal |
|  | R13.03 | `R13.03-zero-value-vertical-stacked.puml` | Stacked bars with a zero value, vertical |
|  | R13.04 | `R13.04-zero-value-horizontal-stacked.puml` | Stacked bars with a zero value, horizontal |
| R15 — Pie | R15.01 | `R15.01-pie-keyed.puml` | `pie [Dogs: 386, ...]`, names around the pie | [#3011](https://github.com/plantuml/plantuml/issues/3011)
|  | R15.02 | `R15.02-pie-a-axis-percent-legend.puml` | Names from `a-axis`, `percent`, legend at the bottom |
|  | R15.03 | `R15.03-pie-donut-labels-colors.puml` | `labels percent donut 40%`, a color per slice, legend right |
|  | R15.04 | `R15.04-pie-single-value-donut.puml` | A single value: a whole ring, `donut` alone is 50% |
|  | R15.05 | `R15.05-pie-quoted-keys-missing-zero.puml` | Quoted keys with `,` and `:`; zero and missing values give no slice |
|  | R15.06 | `R15.06-pie-style-stereotype.puml` | Stereotype style on the pie, legend at the top |
| R16 — Radar | R16.01 | `R16.01-radar-basic.puml` | Two series, by position and by key, `r-axis 0 --> 5 spacing 1` |
|  | R16.02 | `R16.02-radar-keys-auto-scale-circle.puml` | Categories from the keys, automatic scale, `grid circle` |
|  | R16.03 | `R16.03-radar-missing-value-custom-ticks.puml` | Missing value at the center, custom ticks, legend right |
|  | R16.04 | `R16.04-radar-style.puml` | `radar` style: LineColor and transparent BackGroundColor |
| R17 — Pie and radar errors | R17.01 | `R17.01-polar-error-pie-with-h-axis.puml` | Status 400: `"pie" cannot be used with "h-axis"` |
|  | R17.02 | `R17.02-polar-error-two-pies.puml` | Status 400: only one pie in a chart |
|  | R17.03 | `R17.03-polar-error-pie-negative.puml` | Status 400: negative value in a pie |
|  | R17.04 | `R17.04-polar-error-radar-with-bar.puml` | Status 400: `"bar" cannot be used with "radar"` |
|  | R17.05 | `R17.05-polar-error-unknown-key.puml` | Status 400: key not in `a-axis` |
|  | R17.06 | `R17.06-polar-error-mixed-keys.puml` | Status 400: keyed and positional values mixed |
|  | R17.07 | `R17.07-polar-error-r-axis-with-pie.puml` | Status 400: `"pie" cannot be used with "r-axis"` |
| R18 — Text output | R18.01 | `R18.01-txt-bar-vertical.puml` | Vertical bars, half blocks at the top, a long category label wrapped |
|  | R18.02 | `R18.02-txt-bar-horizontal.puml` | Horizontal bars, a band of rows per category, value ticks along the bottom |
|  | R18.03 | `R18.03-txt-line.puml` | A line in steps turning halfway between categories |
|  | R18.04 | `R18.04-txt-bar-line-axis-title.puml` | Bars and a line on a `v-axis` range with a title, legend under the title |
|  | R18.05 | `R18.05-txt-bar-grouped-line-legend.puml` | Three bar series side by side, each with its own fill, and a line |
|  | R18.06 | `R18.06-txt-bar-stacked-negative.puml` | Stacked bars, negative values growing downwards from 0 |
|  | R18.07 | `R18.07-txt-horizontal-grouped-titles.puml` | Horizontal grouped bars, title of the value axis under its ticks |
|  | R18.08 | `R18.08-txt-pie-table.puml` | A pie written as a table of shares |
|  | R18.09 | `R18.09-txt-table-area-line.puml` | An area cannot be drawn as text: the whole chart is a table of values |
|  | R18.10 | `R18.10-txt-table-radar.puml` | A radar written as a table, one per series |
|  | R18.11 | `R18.11-txt-missing-value.puml` | A missing value breaks the line, the points alone are short dashes |
|  | R18.12 | `R18.12-txt-no-series.puml` | No series: only the title |
|  | R18.13 | `R18.13-txt-table-coordinate-pairs.puml` | Coordinate pairs written as a table, named by their x |

> [!NOTE]
> - R07.01 to R07.04 declare `expected-status: 400`: the parser rejects the chart or the renderer reports an error, and the error image is the reference.
>  Only R07.02 reports a chart-specific message. For R07.01, R07.03 and R07.04 the chart command regex does not match,
>  so the parser falls back to the default welcome image instead of a dedicated error message.
> - R07.05 intentionally has no `@endchart`: no diagram block is found, which the harness cannot render,
>  so the file uses `allow-failure: true` (reported as skipped) and has no reference `.svg`.

> [!WARNING]
> - R09: with no range, a value axis goes from 0 (or below, for negative values) to a round maximum,
>  split in five intervals of 1, 2, 3, 4, 5, 6 or 8 times a power of ten. Stacked bars and areas count by their sums,
>  interpolated lines by their curve, arrow annotations get room above their point.
> - R10: series are drawn in the order they are written. Bars (grouped or stacked) and areas (stacked) are each drawn
>  as one group, at the place of the first series of the group: in R10.04 the area `A2`, written after the bar, is still under it.
> - R03: a grid line is drawn at each tick of the axis it belongs to, and only there:
>  `h-axis ... grid` gives lines across the h-axis ticks, `v-axis ... grid` across the v-axis ticks, in both orientations.
> - R15/R16: a pie or a radar takes its data as values (`[78, 21, 1]`) or keyed values (`[Nitrogen: 78, ...]`),
>  each optionally followed by a color. The categories around the circle come from `a-axis`, else from the keys.
>  Slices and spokes go clockwise from 12 o'clock. A pie and a radar cannot be mixed with each other, nor with the
>  cartesian keywords (bar, line, area, scatter, h-axis, v-axis, orientation, stackMode, annotation): see R17.
> - R18: as text, bars and lines are drawn on axes with the ticks of the graphic rendering; every value axis tick
>  gets a dotted line. A pie, and a chart holding an area, a scatter, coordinate pairs or a `v2-axis` (or a line in a
>  horizontal chart), are written as tables of values. Annotations, value labels and colors are not shown.
> - R16: the rings of a radar are always drawn, at the ticks of `r-axis`; `grid polygon` (default) or `grid circle`
>  gives their shape. Without a style color, each series takes a default color, its inside translucent.
> - R12: a series name is quoted (and may hold spaces) or a single word of letters, digits, `_`, `.` and `-`,
>  separated from the keyword by a blank.

## Execution notes

- Run `test.vega.VegaTest` from the repository root (see `CLAUDE.md`).
- Generate or refresh references with `VEGA_FORCE_WRITE=true`, review the `.svg` files, then re-run without the flag.
- `vega.json`, `vega-summary.txt` and `vega-summary.md` are rewritten on every run and must be reverted.

