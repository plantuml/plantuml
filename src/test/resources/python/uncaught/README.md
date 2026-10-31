# Scripts that end with an error

Each `NAME.py` is run, and `NAME.err` says how the run must end. They are data,
so that the Go port runs the same scripts. Run by `PyUncaughtTest`. These
scripts are not run by `PyScriptsTest`, which wants a script to finish.

## `NAME.err`

Lines `key: value`. Blank lines and lines starting with `#` are ignored.

| Key | Meaning |
|---|---|
| `error` | **Required.** `none` (the script ends), the name of a Python exception class that nothing catches (`ZeroDivisionError`, `AppError`...), or `ExecutionLimitError` (the instruction budget is used up; `except` cannot catch it) |
| `line` | line of the innermost frame, where the error was raised |
| `text` | `str(e)` of the exception, or the message of the limit error |
| `trace` | one per frame as `<line> <function>`, the outermost first (`<module>` for the main code); several keys. The traceback keeps at most 50 frames |
| `max-steps` | budget of steps (backward jumps and calls) for this run |
| `max-depth` | maximum depth of the calls for this run |

A key that is absent is not checked. `NAME.out` is what the script printed before
it ended; without it the script must print nothing.

## What is and is not CPython

The expectations of most scripts were produced by CPython 3.13 and compared with the
interpreter. The others say why in a `# DEVIATION` or `# NOT CPython` comment of their
`.err`: the interpreter has 32-bit integers, an instruction limit, a depth limit,
and a few messages and tracebacks that differ (each says which in its `.err`).

## Limits that are specified here

* `step_boundary_ok.py` takes exactly 27 steps: it ends with `max-steps: 27` and
  does not with 26 (`step_boundary_fail`). A step is a backward jump or a call.
* With `max-depth: 50` the deepest recursion that works is `f(48)` called from the
  module, 50 frames in all (`depth_ok`); `f(49)` raises `RecursionError`
  (`depth_exceeded`).
