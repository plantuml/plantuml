# EBNF — Vega test plan

## Objective

Provide requirement-based non-regression tests for [EBNF diagrams](../../../../main/java/net/sourceforge/plantuml/ebnf/) using `@startebnf` / `@endebnf`.

> [!TIP]
> Each test is a `.puml` file rendered to SVG by the Vega harness (`test.vega.VegaTest`) and compared with its committed reference `.svg`.

## Requirement groups

| Group | Topic |
|-------|-------|
| E01 | Minimal EBNF diagram |
| E02 | Elements, operators, and grouping |
| E03 | Special sequences |
| E04 | Full repetition |
| E05 | Drawing modes |
| E06 | Comments and notes |
| E07 | Global style |
| E08 | Rerouted arrow heads |
| E09 | Concatenation order |
| E10 | Unicode identifiers |
| E11 | Except/restriction operator |
| E12 | Empty rules and empty special sequences |

File names follow `Enn.xx-<slug>.puml`; all files and their SVG references are flat in this directory.

## Test matrix

| Requirement | Test ID | File | Expected Result | Request or Issue |
|-------------|---------|------|-----------------|:----------------:|
| E01 — Minimal diagram | E01.01 | `E01.01-minimal-binary.puml` | Renders the `binaryDigit` rule with two alternatives | [QA-16529](https://forum.plantuml.net/16529/could-we-add-syntax-diagrams) |
| E02 — EBNF elements | E02.01 | `E02.01-all-elements.puml` | Literals, special tokens, rules, required/optional items, repetitions, separators, terminators, alternatives, and grouping | [QA-16529](https://forum.plantuml.net/16529/could-we-add-syntax-diagrams) |
| E03 — Special sequences | E03.01 | `E03.01-special-sequences.puml` | ISO-style and Unicode special sequences | [QA-16781](https://forum.plantuml.net/16781/allow-special-sequence-management-special-sequence-symbol) |
| E04 — Full repetition | E04.01 | `E04.01-full-repetition.puml` | Repetition counts on terminals, rules, and optional/repeated expressions | [QA-16750](https://forum.plantuml.net/16750/ebnf-allow-full-repetition-management-with-repetition-symbol) |
| E05 — Drawing modes | E05.01 | `E05.01-expanded-mode.puml` | Expanded repetition rendering (default) | [QA-16529](https://forum.plantuml.net/16529/could-we-add-syntax-diagrams) |
|  | E05.02 | `E05.02-compact-mode.puml` | Compact rendering selected with `!pragma compact` | [QA-16692](https://forum.plantuml.net/16692/ebnf-theo-as-default), [GH-1585](https://github.com/plantuml/plantuml/issues/1585) |
| E06 — Comments and notes | E06.01 | `E06.01-comments.puml` | Rule notes and inline notes | [QA-16694](https://forum.plantuml.net/16694/ebnf-add-notes) |
|  | E06.02 | `E06.02-comment-positions.puml` | Notes above and below rules, alternatives, and terms | — |
| E07 — Global style | E07.01 | `E07.01-style.puml` | EBNF elements and notes use the configured colors | [QA-16529](https://forum.plantuml.net/16529/could-we-add-syntax-diagrams) |
| E08 — Rerouted arrows | E08.01 | `E08.01-rerouted-arrows.puml` | Rerouted lines retain arrow heads | [EBNF more arrow head](https://github.com/plantuml/plantuml/commit/5bbb5a8d27e56db31aec97617edfbda6e3082703) |
| E09 — Concatenation order | E09.01 | `E09.01-concatenation-order.puml` | Grouped elements and their notes preserve source order | [QA-17090](https://forum.plantuml.net/17090/ebnf-perserve-the-order-of-element), [EBNF concatenation order](https://github.com/plantuml/plantuml/commit/9521f6c06f6fa3af96c3d12ceb897c54fb97541a) |
| E10 — Unicode identifiers | E10.01 | `E10.01-unicode-identifiers.puml` | Accented identifiers are accepted and rendered | [QA-17145](https://forum.plantuml.net/17145/ebnf-allow-accentuated-unicode-char-ebnf-meta-identifier-rule), [EBNF better unicode support](https://github.com/plantuml/plantuml/commit/99ea667a0c3b38fea848a208730d6776d668b5a6) |
| E11 — Except/restriction | E11.01 | `E11.01-except-symbol.puml` | A character range and a rule exclusion render correctly | [QA-16735](https://forum.plantuml.net/16735/ebnf-allow-full-restriction-management-with-except-symbol) |
| E12 — Empty rules | E12.01 | `E12.01-empty-rule.puml` | An empty rule as in §8.2 of ISO-EBNF | [QA-16779](https://forum.plantuml.net/16779/ebnf-allow-empty-definition) |
|  | E12.02 | `E12.02-empty-special-sequence.puml` | Empty special sequence `??` | — |

## Execution notes

- Run `test.vega.VegaTest` from the repository root.
- Generate or refresh references with `VEGA_FORCE_WRITE=true`, review the generated files, then re-run without the flag.
- `vega.json`, `vega-summary.txt`, and `vega-summary.md` are rewritten on every run.
