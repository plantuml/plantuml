# Packet diagram — Vega test plan

## Objective

Provide requirement-based non-regression tests for the [packet diagram](../../../../main/java/net/sourceforge/plantuml/packetdiag/) syntax (`@startpacketdiag` / `@endpacketdiag`).

> [!TIP]
> Each test is a `.puml` file rendered by the Vega harness (`test.vega.VegaTest`) and compared with its committed reference output. Some tests also check debug output or an expected error.

## Requirement groups

| Group | Topic |
|-------|-------|
| R01 | Bit-grid layout |
| R02 | `colwidth` |
| R03 | `node_height` |
| R04 | `scale_direction` |
| R05 | `scale_interval` |
| R06 | Range |
| R07 | Range errors |
| R08 | Automatic wrapping |
| R09 | Wrapping at a range boundary |
| R10 | Sparse packets |
| R11 | `len` attribute |
| R12 | `rotate` attribute |
| R13 | `height` and `colheight` attributes |
| R14 | `description` attribute |
| R15 | `label` attribute |
| R16 | `numbered` attribute |
| R17 | Per-field styling |
| R18 | Global style |
| R19 | Variable-length payload |
| R20 | Nested/composite fields |
| R21 | Length validation |
| R22 | `same_height` |
| R23 | Syntax errors |

## Test matrix

| Requirement | Test ID | File | Expected Result | Request or Issue |
|-------------|---------|------|-----------------|:----------------:|
| R01 — Bit-grid | R01.01 | `R01.01-simple.puml` | Basic packet fields aligned to a bit grid | [#2530](https://github.com/plantuml/plantuml/issues/2530) |
| R02 — `colwidth` | R02.01 | `R02.01-colwidth_0.puml` | Zero bits per column |
|  | R02.02 | `R02.02-colwidth_1.puml` | One bit per column |
|  | R02.03 | `R02.03-colwidth_10.puml` | Ten bits per column |
|  | R02.04 | `R02.04-colwidth_16.puml` | Sixteen bits per column |
|  | R02.05 | `R02.05-colwidth_32.puml` | Thirty-two bits per column |
|  | R02.06 | `R02.06-colwidth_33.puml` | More than thirty-two bits per column |
| R03 — `node_height` | R03.01 | `R03.01-node_height_0.puml` | Zero node height |
|  | R03.02 | `R03.02-node_height_80.puml` | Node height of 80 |
|  | R03.03 | `R03.03-node_height_0_with_range.puml` | Zero node height with an explicit range |
|  | R03.04 | `R03.04-node_height_80_with_range.puml` | Node height of 80 with an explicit range |
|  | R03.05 | `R03.05-node_height_0_at_end_with_range.puml` | Range declared after zero node height |
|  | R03.06 | `R03.06-node_height_80_at_end_with_range.puml` | Range declared after node height of 80 |
| R04 — `scale_direction` | R04.01 | `R04.01-scale_direction_ltr.puml` | Scale runs left to right |
|  | R04.02 | `R04.02-scale_direction_rtl.puml` | Scale runs right to left |
| R05 — `scale_interval` | R05.01 | `R05.01-scale_interval_0.puml` | Scale interval of 0 |
|  | R05.02 | `R05.02-scale_interval_1.puml` | Scale interval of 1 |
|  | R05.03 | `R05.03-scale_interval_2.puml` | Scale interval of 2 |
|  | R05.04 | `R05.04-scale_interval_4.puml` | Scale interval of 4 |
|  | R05.05 | `R05.05-scale_interval_8.puml` | Scale interval of 8 |
| R06 — Range | R06.01 | `R06.01-Range_2.puml` | Range of 2 |
|  | R06.02 | `R06.02-Range_8.puml` | Range of 8 |
|  | R06.03 | `R06.03-Range_16.puml` | Range of 16 |
| R07 — Range errors | R07.01 | `R07.01-Range_Error1.puml` | Invalid range (`4-2`) raises `IllegalArgumentException` |
|  | R07.02 | `R07.02-Range_Error2.puml` | Invalid range (`4-3`) raises `ArithmeticException` |
| R08 — Automatic wrapping | R08.01 | `R08.01-Automatic_wrapping_0.puml` | Automatic wrapping with `colwidth = 0` |
|  | R08.02 | `R08.02-Automatic_wrapping_1.puml` | Automatic wrapping with `colwidth = 1` |
|  | R08.03 | `R08.03-Automatic_wrapping_2.puml` | Automatic wrapping with `colwidth = 2` |
|  | R08.04 | `R08.04-Automatic_wrapping_4.puml` | Automatic wrapping with `colwidth = 4` |
|  | R08.05 | `R08.05-Automatic_wrapping_8.puml` | Automatic wrapping with `colwidth = 8` |
| R09 — Range and wrapping | R09.01 | `R09.01-Range_17_automatic_wrapping.puml` | 17-bit field with automatic wrapping |
| R10 — Sparse packets | R10.01 | `R10.01-Sparse_packet.puml` | Sparse packet fields |
|  | R10.02 | `R10.02-Sparse_packet_with_range.puml` | Sparse packet fields with an explicit range |
| R11 — `len` | R11.01 | `R11.01-len_0.puml` | Field with `len = 0` |
|  | R11.02 | `R11.02-len_1.puml` | Field with `len = 1` |
|  | R11.03 | `R11.03-len_2.puml` | Field with `len = 2` |
|  | R11.04 | `R11.04-len_16.puml` | Field with `len = 16` |
|  | R11.05 | `R11.05-len_17.puml` | Field with `len = 17` |
|  | R11.06 | `R11.06-len_empty_8.puml` | Empty `len` followed by a field with `len = 8` |
|  | R11.07 | `R11.07-len_0_8.puml` | Fields with `len = 0` and `len = 8` |
|  | R11.08 | `R11.08-len_1_8.puml` | Fields with `len = 1` and `len = 8` |
|  | R11.09 | `R11.09-len_8_8.puml` | Two fields with `len = 8` |
|  | R11.10 | `R11.10-len_9_8.puml` | Fields with `len = 9` and `len = 8` |
| R12 — `rotate` | R12.01 | `R12.01-rotate_270.puml` | Field rotated by 270 degrees |
|  | R12.02 | `R12.02-rotate_0_360.puml` | Rotation values from 0 to 360 degrees |
| R13 — Field height | R13.01 | `R13.01-height.puml` | Field-specific `height` |
|  | R13.02 | `R13.02-colheight.puml` | Field-specific `colheight` |
| R14 — `description` | R14.01 | `R14.01-description.puml` | Unquoted description value |
|  | R14.02 | `R14.02-description_quoted.puml` | Quoted description value |
| R15 — `label` | R15.01 | `R15.01-label.puml` | Custom labels for fields |
| R16 — `numbered` | R16.01 | `R16.01-numbered.puml` | Custom field numbering |
| R17 — Per-field style | R17.01 | `R17.01-color.puml` | Field fill, text, and line colors |
| R18 — Global style | R18.01 | `R18.01-skin.puml` | Apply the `rose` skin inside a `packetdiag` block |
|  | R18.02 | `R18.02-skin.puml` | Apply the `rose` skin without a `packetdiag` block |
| R22 — `same_height` | R22.01 | `R22.01-same_height_default.puml` | Default behavior |
|  | R22.02 | `R22.02-same_height_true.puml` | `same_height = true` |
|  | R22.03 | `R22.03-same_height_false.puml` | `same_height = false` |
|  | R22.04 | `R22.04-same_height_default_colheight.puml` | Default behavior with `colheight` |
|  | R22.05 | `R22.05-same_height_true_colheight.puml` | `same_height = true` with `colheight` |
|  | R22.06 | `R22.06-same_height_false_colheight.puml` | `same_height = false` with `colheight` |
| R23 — Syntax error | R23.01 | `R23.01-error.puml` | Status 400 for invalid packet syntax |

> [!NOTE]
> - `R01.01-simple.puml` also has a committed `.txt` debug-output reference.
> - `error-with-exception.puml` is an additional fixture expecting status 503 and an `IllegalArgumentException`.
> - R19 to R21 (variable-length payloads, nested/composite fields, and length validation) are listed as unsupported in the [issue inventory](https://github.com/plantuml/plantuml/issues/2530#issuecomment-3951079667) and have no Vega cases here.

> [!WARNING]
> The issue inventory is a snapshot from its comment; the Vega matrix above records the tests currently present in this directory.

## Execution notes

- Run `test.vega.VegaTest` from the repository root (see `CLAUDE.md`).
- Generate or refresh references with `VEGA_FORCE_WRITE=true`, review the generated files, then re-run without the flag.
- `vega.json`, `vega-summary.txt` and `vega-summary.md` are rewritten on every run and must be reverted.
