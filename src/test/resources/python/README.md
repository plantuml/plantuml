# Tests rewritten with a Python-like syntax

This directory holds the Lua test scripts of `src/test/resources`, rewritten by
hand in a **Python-like syntax**. It is an experiment: nothing here is parsed
yet. The goal is to see what the syntax looks like on real code before writing
the compiler.

The idea is a second front-end that compiles directly to the same bytecode as
the Lua front-end. So the **semantics stay those of the Lua VM**; only the
surface changes. A script written in either syntax can call the other.

The layout mirrors `src/test/resources`. Files are named `.py` for editor
highlighting, but they are not Python.

## Rules used in these files

Blocks and statements

| Lua                              | Python-like                               |
| -------------------------------- | ----------------------------------------- |
| `if a then ... elseif b ... end` | `if a:` ... `elif b:` ... `else:`         |
| `while c do ... end`             | `while c:`                                |
| `repeat ... until c`             | `while True:` ... `if c: break`           |
| `for i = a, b do`                | `for i in range(a, b):` (**b excluded**)  |
| `for i = a, b, s do`             | `for i in range(a, b, s):`                |
| `for k, v in pairs(t) do`        | `for k, v in pairs(t):`                   |
| `function f(a) ... end`          | `def f(a):`                               |
| `function t.f(a)`                | `def t.f(a):`                             |
| `function t:m(a)`                | `def t:m(a):` (implicit `self`)           |
| `function(a) return a end`       | `lambda a: a`                             |
| `local x = 1`                    | `x = 1` (first assignment in a function)  |
| `-- comment`                     | `# comment`                               |
| `[[long string]]`                | `"""long string"""`                       |

Expressions

| Lua            | Python-like                                      |
| -------------- | ------------------------------------------------ |
| `nil/true/false` | `None/True/False`                              |
| `~=`           | `!=`                                             |
| `x == nil`     | `x is None` (`x is not None`)                    |
| `#t`           | `len(t)`                                         |
| `a ^ b`        | `a ** b`                                         |
| `a .. b`       | `a .. b` (unchanged, see below)                  |
| `{1, 2, 3}`    | `[1, 2, 3]` (a table; first index is 1)          |
| `{x = 1}`      | `{"x": 1}`                                       |
| `{[k] = v}`    | `{k: v}`                                         |
| `...`          | `*args` (any name; stands for the Lua `...`)     |
| `s:upper()`    | `s:upper()` (unchanged)                          |
| `a = a + 1`    | `a += 1` (also `-=`, `*=`, `/=`, `..=`)          |

## Scoping

- At **module level**, assignments create globals (as in Python).
- Inside a function, assigning to a name creates a **local** if no enclosing
  function has a local of that name; otherwise it assigns to that upvalue. There
  is no `nonlocal`: this is the Lua behavior.
- `global x` makes `x` a global inside a function.
- Reading a name that is neither local nor an upvalue reads a global.
- Test scripts that rely on chunk-level locals (upvalues, performance) wrap
  their body in `def main():` and call `main()` at the end.

## What does NOT change (the VM is Lua's)

- Only `None` and `False` are false: `0`, `""` and `[]` are **true**.
- Tables are 1-based; `[a, b]` is `{a, b}`. There is no separate list/dict type.
- `/` is float division. Numbers are Lua numbers.
- Calls and returns have multiple values; `a, b = f()` and `return a, b` work.
- `..` stays the concatenation operator (a `+` on strings would be an
  arithmetic coercion in the VM). `//`, `%`, `&`, `|`, `^` (xor), `~`, `<<`,
  `>>` follow Lua 5.3 once the integer work is done.
- No `try/except`: `pcall(f, ...)` and `error(...)` as in Lua. A function passed
  to `pcall` is a `def` or a `lambda`.
- String escapes: `\n`, `\t`, `\\`, `\"`, `\xHH`. Decimal escapes (`\065`) are
  written `\x41`.

## More conventions found while rewriting

- The module-level chunk varargs are `*argv` (e.g. `select(1, *argv)`).
- Mixed table constructors: `[...]` for pure lists, `{"k": v, positional, key: v}`
  when keys and positional items are mixed.
- `assert` and `print` are plain functions: `print("x")`, `assert(c)`.
- `*args` in a non-final position truncates to one value, as `...` does in Lua;
  `(*args)` makes it explicit.
- Comparisons never chain: `a < b < c` is not Python's.
- `;` may separate several short statements on one line.
- Strings given to `load()` are written in the Python-like syntax.
- Module-level names are globals, so scripts that `require` helpers wrap their
  framework in `setup()` and declare public names `global`.
- Lambdas hold one expression only: statements or several return values need a `def`.
- `def name` is not `local function name`: it can shadow a global (see the
  `pc = pcall` workaround in `baselibargs.py` and `vm.py`).

## Limits observed

- No block scopes: a Lua `do ... end` becomes `if True:` (names then leak into the
  function) or a nested `def`. Loop variables and shadowing behave differently.
- `range()` has an exclusive limit, so float steps and inclusive float limits
  of numeric `for` loops do not map directly (see `nextvar.py`).
- Error messages of `range()` versus numeric `for` differ.

## Not rewritten

Left out because they test features removed from this fork or depend on Lua
internals: `luaj3.0-tests/metatags.lua` (coroutine, debug),
`lua5.2.1-tests/locals.lua` (block scopes, `_ENV`, debug), `checktable.lua` (C test
API `T`), `coroutine`, `debug`, `io`/`os`, `collectgarbage`, `goto`, `string.dump`,
`api.lua`, `gc.lua`, `db.lua`, `files.lua`, `main.lua`, `all.lua`, `closure.lua`,
`code.lua`, `events.lua`, `attrib.lua`, `errors.lua`, `big.lua`, `verybig.lua`,
`bitwise.lua`, `literals.lua`, `pm.lua`, `math.lua`, `strings.lua`, and the very
large data tables (`format/numbers.lua`). `calls.py`, `constructs.py` and
`nextvar.py` are partial.
