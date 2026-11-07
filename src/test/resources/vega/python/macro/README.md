# Vega tests of `@macro`

Specification tests of the macros of text Python, section 11.5 of
`PYTHON_PREPROCESSOR.md`: a function declared with `@macro` (or `@puml.macro`)
is called by name from the text lines of a diagram, like the legacy `!define`
and `!unquoted procedure`.

`@macro` is **not implemented yet**. These files have no YAML header, so Vega
tags them `specification` and allows them to fail; the expected output is in
the `' EXPECT (preproc):` comment of each file. When `@macro` is implemented,
give each file a YAML header (`output: preproc, svg`), generate its references
(`VEGA_FORCE_WRITE=true`), check them against the `EXPECT` comment, and the
test becomes a regular one. The error cases need `expected-status: 400` and
`expected-error-message` instead.

Groups: declaration (`inline_value`, `puml_macro_alias`, `explicit_*`,
`function_still_callable`, `declared_*`), arguments (`arguments_are_strings`,
`quoted_and_bare_arguments`, `parentheses_inside_arguments`,
`keyword_arguments_and_defaults`, `interpolation_first`, `no_arguments`),
result (`procedure_*`, `emits_then_returns`, `returns_none_and_emits_nothing`,
`several_calls_per_line`, `result_not_rescanned`,
`macro_calls_macro_as_python`), matching (`name_followed_directly_by_parenthesis`,
`no_macro_no_change`, `called_inside_a_python_block`) and errors
(`duplicate_declaration`, `conflict_with_legacy_define`,
`invalid_explicit_name`, `too_many_arguments`, `error_inside_a_macro`).
