# Errors as text (ATXT, UTXT)

An error is written as plain text in ATXT and UTXT (see `PSystemError.exportTxt`): where it was
found, the source up to the faulty line, that line underlined with `^`, and the message.

Before, the graphical error image was drawn on a grid of characters: lines several hundred
characters wide, and the message at the very end, under the welcome text added to a source of
less than 5 lines.

| Test | Expected Result |
|------|-----------------|
| `E01-sequence-syntax-error.puml` | A syntax error in a sequence diagram |
| `E02-indented-faulty-line.puml` | The carets keep the indentation of the faulty line |
| `E03-class-diagram-error.puml` | A syntax error in a class diagram, a longer source |
| `E04-chart-mismatched-brackets.puml` | A chart error: `Mismatched '{' and ']' around data` |
| `E05-chart-execution-error.puml` | A chart error found while executing a command: `Invalid number "oops"` |
| `E06-preprocessor-unknown-function.puml` | A preprocessor error: `Unknown built-in function` |
| `E07-error-in-procedure.puml` | An error inside a procedure, reported at the faulty line of its body |
| `E08-long-source-skipped-lines.puml` | More than 40 lines: the middle of the source is skipped |
| `E09-unicode-faulty-line.puml` | Non-ASCII text in the faulty line, kept as it is |
