# luaj (PlantUML edition): a Python subset interpreter

This repository started as a fork of [LuaJ](https://github.com/luaj/luaj) and
still carries its name and its history. The Lua interpreter has been dropped:
the code is now an interpreter for a **subset of Python 3**, written in pure
Java and meant to be embedded in [PlantUML](https://plantuml.com). The license
is unchanged (see `LICENSE`).

## 1. Goal and constraints

PlantUML ships in several editions that must behave identically: the regular
JAR, a JavaScript build and a native C build, the last two produced with
[TeaVM](https://teavm.org). The interpreter is therefore written **in Java**
and compiled to JVM bytecode, JavaScript and C. It is meant to:

- drive PlantUML from a script (build a model, call layout/render functions,
  query the result);
- generate diagrams from code embedded in the diagram source, partly
  replacing the text preprocessor (loops, functions, data structures).

Constraints that drive every decision:

- no reflection, no threads, no file system, no `Class.forName`;
- nothing that TeaVM cannot compile;
- no uncatchable failures (a `StackOverflowError` crashes a TeaVM/C binary):
  call depth and instruction count are bounded;
- the smallest possible surface: what is not needed is removed.

## 2. What it is

- Every accepted program is valid Python 3 and gives the same result in
  CPython, except for the documented deviations (32-bit integers, limits).
- Pipeline: lexer, parser (an AST identical to CPython's `ast`), scope
  resolver, code generator, stack machine.
- Design, supported subset, instruction set and deviations: see `PYTHON.md`.
- Tests: `src/test/resources/python` holds Python scripts whose reference
  output comes from CPython (`tools/cpython_check.py`); see `PYTHON.md`
  section 7.

## 3. Using it

```java
Interp in = new Interp(text -> System.out.print(text));
in.setMaxSteps(10_000_000);   // instruction counter, not catchable by the script
in.setMaxDepth(200);          // call depth, RecursionError
in.run("print(sum([i * i for i in range(10)]))", "demo.py");
```

Errors reach the host as `PySyntaxError` (compile time), `PyError` (an
uncaught Python exception, with a traceback in `getMessage()`) or
`ExecutionLimitError`.

## 4. Editions

`poc/` is a separate Gradle build that compiles the sources with TeaVM to
JavaScript and C and runs one test program on the three editions; see
`poc/README.md`.

## 5. What remains to do

- Check the TeaVM editions (the old findings are kept in `poc/README.md`: the
  interpreter loop must stay out of `try` blocks on TeaVM C; `Interp.run` has
  none, `Interp.execute` has one per call).
- The API towards PlantUML (functions exposed to the script, `$` lines in
  diagrams).
- More builtins and methods as the needs appear, speed.
