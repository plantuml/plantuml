# Chart diagram — Vega test plan

## Objective

Provide requirement-based non-regression tests for the [chart diagram](../../../../main/java/net/sourceforge/plantuml/chart/) syntax (`@startchart` / `@endchart`).

> [!TIP]
>  Each test is a single `.puml` file, rendered to SVG by the Vega harness (`test.vega.VegaTest`), and compared with the committed reference `.svg`.
>  The R18 tests are rendered as text instead, and compared with the committed `.utxt` and `.atxt`.
>  The R19 to R23 tests are rendered three times, as SVG, `.utxt` and `.atxt` (the error tests as SVG only).

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
| R19 | Lenient JSON-like data for pie and radar (`{}`, `()`, single and double quotes) |
| R20 | Lenient JSON-like data for bar, line, area and scatter (`{}`, `()`, quotes, `[x, y]` pairs) |
| R21 | Keyed values for bar, line, area and scatter (`{Jan: 10, Feb: 20}`), matched with the h-axis categories |
| R22 | Pie and radar data written as a list, one value per line (`* key : value`, `- key : value`, `"key": value`) |
| R23 | Bar, line, area and scatter data written as a list (`bar "Sales"` then `* Jan : 10`) |

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
| R19 — Lenient data | R19.01 | `R19.01-lenient-pie-json-braces.puml` | Braces, quoted values: `{"Dogs": 386, "Cats": "85"}` |
|  | R19.02 | `R19.02-lenient-pie-parentheses.puml` | Data between parentheses |
|  | R19.03 | `R19.03-lenient-pie-single-quotes.puml` | Keys and values between single quotes |
|  | R19.04 | `R19.04-lenient-pie-quoted-values-spaces.puml` | Spaces around `:`, `,` and inside quoted values |
|  | R19.05 | `R19.05-lenient-pie-json-colors-donut.puml` | Colors after quoted values, donut, legend right |
|  | R19.06 | `R19.06-lenient-pie-apostrophe-and-quoted-comma.puml` | Unquoted apostrophe (`Driver's`), comma in single quotes, apostrophe in double quotes |
|  | R19.07 | `R19.07-lenient-pie-preprocessor-json.puml` | The data of a preprocessor JSON variable: `pie $data.pets`, `title $data.title` |
|  | R19.08 | `R19.08-lenient-pie-json-missing-value.puml` | `""` and an empty value are missing values |
|  | R19.09 | `R19.09-lenient-pie-name-stereotype-braces.puml` | Stereotype and name before braces |
|  | R19.10 | `R19.10-lenient-pie-json-decimals.puml` | Decimals and exponent, quoted or not |
|  | R19.11 | `R19.11-lenient-pie-json-a-axis-order.puml` | `a-axis` with JSON-like keyed data |
|  | R19.12 | `R19.12-lenient-pie-quoted-hash-and-colon.puml` | `#` and `:` inside quoted keys |
|  | R19.13 | `R19.13-lenient-pie-positional-braces.puml` | Positional values in braces, quoted or not |
|  | R19.14 | `R19.14-lenient-radar-braces-two-series.puml` | A series between brackets and a keyed series between braces |
|  | R19.15 | `R19.15-lenient-radar-parentheses-single-quotes.puml` | Parentheses, single quotes, unquoted apostrophe |
|  | R19.16 | `R19.16-lenient-radar-preprocessor-json.puml` | Two series from the members of a preprocessor JSON variable |
|  | R19.17 | `R19.17-lenient-radar-positional-braces-color.puml` | Positional values in braces with a color |
|  | R19.18 | `R19.18-lenient-radar-json-missing-value.puml` | `""` is a missing value |
|  | R19.19 | `R19.19-lenient-radar-mixed-brackets-series.puml` | Three series using brackets, parentheses and braces |
|  | R19.20 | `R19.20-lenient-error-mismatched-brackets.puml` | Status 400: `Mismatched '{' and ']' around data` |
|  | R19.21 | `R19.21-lenient-error-unclosed-single-quote.puml` | Status 400: unbalanced quotes |
|  | R19.22 | `R19.22-lenient-error-quoted-non-number.puml` | Status 400: `Invalid number "many"` |
|  | R19.23 | `R19.23-lenient-error-braces-mixed-keys.puml` | Status 400: keyed and positional values mixed |
|  | R19.24 | `R19.24-lenient-error-radar-mismatched-parentheses.puml` | Status 400: `Mismatched '(' and '}' around data` |
| R20 — Lenient cartesian data | R20.01 | `R20.01-lenient-xy-line-braces-quoted.puml` | Values between braces, double and single quotes, labels |
|  | R20.02 | `R20.02-lenient-xy-line-parentheses.puml` | Values between parentheses, with a color |
|  | R20.03 | `R20.03-lenient-xy-line-json-pairs-brackets.puml` | JSON array of pairs: `[[1, 2], [3, 5]]` |
|  | R20.04 | `R20.04-lenient-xy-line-pairs-braces-quoted.puml` | Pairs between braces, quoted coordinates |
|  | R20.05 | `R20.05-lenient-xy-line-pairs-mixed-delimiters.puml` | Pairs written with `()`, `[]` and `{}` in one series |
|  | R20.06 | `R20.06-lenient-xy-line-missing-values-quoted.puml` | `""` and an empty value are missing values, with and without `connect` |
|  | R20.07 | `R20.07-lenient-xy-line-preprocessor-json.puml` | `h-axis $d.months` and `line $d.sales` from a JSON variable, quoted numbers |
|  | R20.08 | `R20.08-lenient-xy-line-preprocessor-json-pairs.puml` | Pairs from a JSON array variable, `interpolate` |
|  | R20.09 | `R20.09-lenient-xy-line-v2-interpolate-braces.puml` | Braces with `v2` and `interpolate`, next to a bar |
|  | R20.10 | `R20.10-lenient-xy-line-decimals-negative.puml` | Negative, decimal and exponent values, quoted or not |
|  | R20.11 | `R20.11-lenient-xy-bar-braces-quoted.puml` | Bar values between braces, quoted, color and labels |
|  | R20.12 | `R20.12-lenient-xy-bar-parentheses-stacked.puml` | Stacked bars between parentheses and braces |
|  | R20.13 | `R20.13-lenient-xy-bar-horizontal-single-quotes.puml` | Horizontal bar, values between single quotes |
|  | R20.14 | `R20.14-lenient-xy-area-braces-quoted.puml` | Area values between braces, quoted |
|  | R20.15 | `R20.15-lenient-xy-area-parentheses-v2.puml` | Area between parentheses on the `v2-axis` |
|  | R20.16 | `R20.16-lenient-xy-scatter-json-pairs.puml` | Scatter pairs as JSON arrays and between braces |
|  | R20.17 | `R20.17-lenient-xy-scatter-parentheses-values.puml` | Scatter values between parentheses, quoted |
|  | R20.18 | `R20.18-lenient-xy-mixed-series-all-delimiters.puml` | Bar `[]`, line `()` and area `{}` in one chart |
|  | R20.19 | `R20.19-lenient-xy-error-mismatched-brackets.puml` | Status 400: `Mismatched '{' and ']' around data` |
|  | R20.20 | `R20.20-lenient-xy-error-pairs-in-bar.puml` | Status 400: coordinate pairs are not allowed in a bar |
|  | R20.21 | `R20.21-lenient-xy-error-pair-three-coordinates.puml` | Status 400: `Invalid coordinate pair "[1, 2, 3]"` |
|  | R20.22 | `R20.22-lenient-xy-error-mismatched-pair.puml` | Status 400: `Mismatched '(' and ']' around a coordinate pair` |
|  | R20.23 | `R20.23-lenient-xy-error-unclosed-quote.puml` | Status 400: unbalanced quotes |
|  | R20.24 | `R20.24-lenient-xy-error-quoted-non-number.puml` | Status 400: `Invalid number "ten"` |
| R21 — Keyed cartesian data | R21.01 | `R21.01-keyed-xy-bar-h-axis-order-missing.puml` | Keys in any order placed on the h-axis categories, a missing one leaves a gap |
|  | R21.02 | `R21.02-keyed-xy-line-categories-from-keys.puml` | No h-axis: the keys are the categories |
|  | R21.03 | `R21.03-keyed-xy-line-union-of-keys.puml` | No h-axis: the keys of the first series, then the new keys of the next ones |
|  | R21.04 | `R21.04-keyed-xy-line-preprocessor-json-object.puml` | A preprocessor JSON object: `line $data.adopted`, `title $data.title` |
|  | R21.05 | `R21.05-keyed-xy-bar-stacked-different-orders.puml` | Stacked bars whose keys are written in different orders |
|  | R21.06 | `R21.06-keyed-xy-area-missing-category.puml` | A category missing from an area breaks it, stacked on the other area |
|  | R21.07 | `R21.07-keyed-xy-scatter-keys.puml` | Scatter points placed by key |
|  | R21.08 | `R21.08-keyed-xy-bar-horizontal-v-axis.puml` | Horizontal chart: the keys are categories of the v-axis |
|  | R21.09 | `R21.09-keyed-xy-mixed-keyed-and-positional.puml` | A positional bar and a keyed line on the same h-axis |
|  | R21.10 | `R21.10-keyed-xy-quoted-special-keys.puml` | Keys with `,`, `:`, `#` and an unquoted apostrophe |
|  | R21.11 | `R21.11-keyed-xy-line-connect-interpolate.puml` | Missing keys with and without `interpolate connect` |
|  | R21.12 | `R21.12-keyed-xy-line-v2.puml` | Keyed line on the `v2-axis` |
|  | R21.13 | `R21.13-keyed-xy-brackets-and-parentheses.puml` | Unquoted keys between brackets and parentheses |
|  | R21.14 | `R21.14-keyed-xy-h-axis-after-series.puml` | `h-axis` written after the keyed series, with a category without value |
|  | R21.15 | `R21.15-keyed-xy-preprocessor-json-series.puml` | Two bars from the members of a preprocessor JSON object |
|  | R21.16 | `R21.16-keyed-xy-error-key-not-in-h-axis.puml` | Status 400: `"Mar" is not in h-axis` |
|  | R21.17 | `R21.17-keyed-xy-error-key-not-in-h-axis-after.puml` | Status 400: same, the h-axis being written after the series |
|  | R21.18 | `R21.18-keyed-xy-error-keys-with-h-axis-range.puml` | Status 400: keys need h-axis categories, not a range |
|  | R21.19 | `R21.19-keyed-xy-error-h-axis-range-after-keys.puml` | Status 400: same, the range being written after the series |
|  | R21.20 | `R21.20-keyed-xy-error-mixed-keyed-values.puml` | Status 400: keyed and positional values mixed in a series |
|  | R21.21 | `R21.21-keyed-xy-error-keyed-pair.puml` | Status 400: a coordinate pair cannot have a key |
|  | R21.22 | `R21.22-keyed-xy-error-key-not-in-v-axis.puml` | Status 400: `"C" is not in v-axis` (horizontal chart) |
|  | R21.23 | `R21.23-keyed-xy-error-empty-key.puml` | Status 400: `Empty key before the value 1` |
|  | R21.24 | `R21.24-keyed-xy-area-positional-missing-value.puml` | An empty value in an area (`[2, , 3, 4, ]`) breaks it |
| R22 — Data as a list | R22.01 | `R22.01-list-pie-star-items.puml` | `pie` alone, then `* key : value` items |
|  | R22.02 | `R22.02-list-pie-dash-items.puml` | `- key : value` items |
|  | R22.03 | `R22.03-list-pie-mermaid-quoted-keys.puml` | Mermaid items: `"Nitrogen": 78` |
|  | R22.04 | `R22.04-list-pie-name-options.puml` | Name and options on the `pie` line: `labels percent donut 40%` |
|  | R22.05 | `R22.05-list-pie-item-colors.puml` | A color after each value |
|  | R22.06 | `R22.06-list-pie-special-keys.puml` | Unquoted comma and apostrophe in keys, quoted `#` and `:` |
|  | R22.07 | `R22.07-list-pie-values-only.puml` | Values without keys: `* 50` |
|  | R22.08 | `R22.08-list-pie-missing-and-zero.puml` | An empty and a zero value give no slice, a quoted value |
|  | R22.09 | `R22.09-list-pie-title-colon-legend.puml` | `title Air: composition` stays a title; `legend` after the items |
|  | R22.10 | `R22.10-list-pie-stereotype-style.puml` | Stereotype style on a pie written as a list |
|  | R22.11 | `R22.11-list-pie-mixed-bullets.puml` | `*`, `-` and bare items mixed, any indentation, a tab |
|  | R22.12 | `R22.12-list-pie-preprocessor-foreach-keys.puml` | Items generated by `!foreach` over the keys of a JSON object |
|  | R22.13 | `R22.13-list-pie-preprocessor-foreach-array.puml` | Items generated by `!foreach` over a JSON array of objects |
|  | R22.14 | `R22.14-list-radar-a-axis.puml` | Two radars written as lists against the `a-axis`, color and labels |
|  | R22.15 | `R22.15-list-radar-categories-from-keys.puml` | No a-axis: the categories are the keys of the items |
|  | R22.16 | `R22.16-list-radar-values-only.puml` | Values without keys, matched with the a-axis by position |
|  | R22.17 | `R22.17-list-radar-inline-then-items.puml` | A radar with inline data, then a radar written as a list |
|  | R22.18 | `R22.18-list-radar-a-axis-after-items.puml` | `a-axis` written after the items |
|  | R22.19 | `R22.19-list-error-item-without-series.puml` | Status 400: an item needs a series written without data before it |
|  | R22.20 | `R22.20-list-error-bare-key-without-series.puml` | Status 400: same for a bare `Nitrogen : 78` line |
|  | R22.21 | `R22.21-list-error-mixed-keyed-items.puml` | Status 400: keyed and positional items mixed |
|  | R22.22 | `R22.22-list-error-pie-negative-item.puml` | Status 400: negative value in a pie |
|  | R22.23 | `R22.23-list-error-pie-all-zero.puml` | Status 400: a pie needs a value greater than 0 (checked once every line is read) |
|  | R22.24 | `R22.24-list-error-empty-pie.puml` | Status 400: same, for a pie without items |
|  | R22.25 | `R22.25-list-error-radar-key-not-in-a-axis.puml` | Status 400: `"QA" is not in a-axis` |
|  | R22.26 | `R22.26-list-error-a-axis-after-unknown-key.puml` | Status 400: same, the a-axis being written after the items |
|  | R22.27 | `R22.27-list-error-item-after-inline-pie.puml` | Status 400: a pie with inline data takes no items |
|  | R22.28 | `R22.28-list-error-item-after-inline-bar.puml` | Status 400: a bar with inline data takes no items |
|  | R22.29 | `R22.29-list-error-unclosed-quote-item.puml` | Status 400: unbalanced quotes in an item |
| R23 — Cartesian data as a list | R23.01 | `R23.01-xy-list-bar-keys-h-axis.puml` | Keyed items in any order placed on the h-axis, a missing one leaves a gap |
|  | R23.02 | `R23.02-xy-list-bar-categories-from-keys.puml` | No h-axis: the keys of the items are the categories |
|  | R23.03 | `R23.03-xy-list-line-mermaid-keys.puml` | Mermaid items: `"Jan": 38` |
|  | R23.04 | `R23.04-xy-list-line-values-only.puml` | Values without keys, `interpolate` |
|  | R23.05 | `R23.05-xy-list-line-missing-connect.puml` | Missing keys with and without `interpolate connect` |
|  | R23.06 | `R23.06-xy-list-area-stacked-lists.puml` | Two areas written as lists, a missing key breaks the second one |
|  | R23.07 | `R23.07-xy-list-bar-stacked-lists.puml` | Stacked bars written as lists, keys in different orders |
|  | R23.08 | `R23.08-xy-list-bar-horizontal.puml` | Horizontal chart: the keys are categories of the v-axis |
|  | R23.09 | `R23.09-xy-list-scatter-keys.puml` | Scatter points written as keyed items |
|  | R23.10 | `R23.10-xy-list-line-v2-with-inline-bar.puml` | A list on the `v2-axis` next to a bar with inline data |
|  | R23.11 | `R23.11-xy-list-mixed-series-lists.puml` | Bar, line and area lists: the categories are the union of their keys |
|  | R23.12 | `R23.12-xy-list-special-keys.puml` | Unquoted comma and apostrophe, quoted `#` and `:` |
|  | R23.13 | `R23.13-xy-list-h-axis-after-items.puml` | `h-axis` written after the items, with a category without value |
|  | R23.14 | `R23.14-xy-list-preprocessor-foreach.puml` | Items generated by `!foreach` over the keys of a JSON object |
|  | R23.15 | `R23.15-xy-list-title-legend-around-items.puml` | `title Sales: 2024` stays a title; an item after `legend` still goes to the bar |
|  | R23.16 | `R23.16-xy-list-error-item-color.puml` | Status 400: `A value of a bar cannot have a color of its own` |
|  | R23.17 | `R23.17-xy-list-error-key-not-in-h-axis.puml` | Status 400: `"Mar" is not in h-axis` |
|  | R23.18 | `R23.18-xy-list-error-key-not-in-h-axis-after.puml` | Status 400: same, the h-axis being written after the items |
|  | R23.19 | `R23.19-xy-list-error-keys-with-h-axis-range.puml` | Status 400: keys need h-axis categories, not a range |
|  | R23.20 | `R23.20-xy-list-error-coordinate-pair-item.puml` | Status 400: a coordinate pair cannot be written as a list item |
|  | R23.21 | `R23.21-xy-list-error-list-after-pairs.puml` | Status 400: a list cannot follow coordinate pairs |
|  | R23.22 | `R23.22-xy-list-error-mixed-keyed-items.puml` | Status 400: keyed and positional items mixed |
|  | R23.23 | `R23.23-xy-list-error-key-not-in-v-axis.puml` | Status 400: `"C" is not in v-axis` (horizontal chart) |

> [!NOTE]
> - R07.01 to R07.04 declare `expected-status: 400`: the parser rejects the chart or the renderer reports an error, and the error image is the reference.
>  R07.02 and R07.03 report a chart-specific message (R07.03: `Either every item is a coordinate pair (x, y), or none is`).
>  For R07.01 and R07.04 the chart command regex does not match,
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
> - R23: a bar, a line, an area or a scatter written without data takes its items in the same way. Their keys are
>  categories of the h-axis, as in R21; their values take no color, and coordinate pairs cannot be written as items.
> - R22: a pie or a radar written without data takes its values from the lines that follow it, one per line, as a
>  Markdown list item (`* key : value`, `- key : value`, `* value`) or as in Mermaid (`"key": value`). An item is read as an
>  item between brackets, except that a key may hold a comma without quotes. The items go to the last pie or radar written
>  without data, until another series is written. A bare line starting with `'` is a PlantUML comment: quote such a key
>  after a `*` or `-`. A pie written as a list is checked once every line is read.
> - R21: the values of a bar, a line, an area or a scatter may have keys, as for a radar: they are categories of the
>  h-axis (the v-axis of a horizontal chart), whatever their order. Without axis labels, the categories are the keys of the
>  first series, then the new keys of the next ones. A category without value in a series is a missing value, which also
>  breaks an area. Keys need categories: they cannot be used with an h-axis range.
> - R20: the data of a bar, a line, an area or a scatter may be JSON-like in the same way, and a coordinate pair may be
>  written `(x, y)`, `[x, y]` or `{x, y}`, so that a JSON array of arrays can be used directly (`line $points`).
>  Every item is a pair, or none is; a bar and an area take no pairs.
> - R19: the data of a pie or a radar may be JSON-like: enclosed in `[]`, `{}` or `()`, with keys and values quoted with
>  `"` or `'`, so that a preprocessor JSON object can be used directly (`pie $data.pets`). A single quote only opens a
>  quoted text at the start of an item, so an apostrophe (`Driver's: 5`) needs no quoting.
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

