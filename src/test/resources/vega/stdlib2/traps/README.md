# Preprocessor traps (legacy diagrams)

Like the rest of `vega/stdlib2`, these tests are run on two branches: the one
with the Python preprocessor and `main`, which only has the legacy
preprocessor. They must give the same SVG on both. Each file is a **legacy**
diagram (no Python) with something that the Python layer could misread:

- text that looks like an interpolation or a shell variable (`!{toto}`, `!{}`,
  `${HOME}`, `${{ ... }}`), in notes and in JSON / YAML diagrams;
- a `!` inside a line, and in Creole;
- a legacy procedure called as `!name()`, as `adaml` does;
- `!pragma teoz true`;
- Python keywords used as legacy names (`$def`, `$class`, `$print`) and as
  plain text;
- legacy directives indented with spaces and with tabs, nested loops,
  functions and `!return`;
- `!unquoted`, `!final`, `!local`, `!global`, builtin `%` functions;
- `!include`, `!startsub` / `!includesub`;
- comments, simple and in blocks, that hold Python-looking lines;
- legacy conditions with `!=`, `%not`, `!ifdef` / `!ifndef` / `!undef`.

Do not add here what differs **by design** between the two branches (section
8 of `PYTHON_PREPROCESSOR.md`): a text line that starts with `!` and a word
(`!important`), a line that starts with `!!`, a valid `!{expr}`, a `!python` or
`!endpy` line, a block without its end (`!if` without `!endif`, ...), and
`%version()`.

Some outputs show legacy behaviours that these tests freeze: `$for(def)` gives
`participant d` (the variable `$def` is substituted), and
`include_and_subparts` declares `Extra` twice (the `!include` of the file also
includes its `!startsub` part).
