# Compile-time cases

Short Python sources and the outcome expected when they are compiled
(`Interp.compile`: lexer, parser, resolver and code generator). They are data,
not Java, so that the Go port runs the same cases. Run by `PyCasesTest`.

## Format

A `.cases` file is a list of cases. Every case starts with a header line
`### <outcome>`; the lines up to the next header (or the end of the file) are
the source.

| Header | Meaning |
|---|---|
| `### line N` | compiling must fail with a syntax error reported on line `N` (1-based) |
| `### ok` | compiling must succeed |
| `### ok noeol` | same, and the source has no final newline |

The source is its lines joined by LF, followed by one LF. With `noeol` there
is no final LF. Files use LF only. A source line never starts with `### `.

## Files

| File | Content |
|---|---|
| `parser_unsupported.cases` | valid Python that this subset does not support (`with`, `yield`, `try/finally`, ...) |
| `parser_invalid.cases` | invalid programs |
| `class_bodies.cases` | statements not allowed in a class body |
| `class_special_methods.cases` | special methods other than the supported ones |
| `class_decorators.cases` | decorators of builtin descriptors and forms not allowed |
| `class_other.cases` | multiple inheritance, `super()`, metaclass, shadowed enclosing variable |
| `accepted.cases` | sources that must compile, with and without a final newline |

## Notes

* Only the **line** of the error is part of the expectation, not its message.
* The line is what the Java implementation reports, reviewed by hand. For
  a source that stops inside an open bracket (`x = (1`) it is the line *after*
  the last one, where the end of file is found; CPython reports the line of the
  bracket. A port must reproduce this, or the case must be changed on both sides.
* Deep nesting (100000 brackets, `not`, unary minus, 1000 nested blocks) is
  generated, not stored: see `PyParserTest.deepNestingIsAnErrorNotAStackOverflow`.
