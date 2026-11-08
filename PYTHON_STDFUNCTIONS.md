# Standard functions in Python: from `%function()` to modules

Working document. Only the module `color` (section 3.3) is **[implemented]**; the rest is not. It prepares the decision
"which Python modules (or which additions to `puml`) give a Python diagram what
`StandardFunctions` gives a legacy diagram". It builds on
`PYTHON_PREPROCESSOR.md`: section 13.1 (the `puml` API), section 15 (shipped
modules), section 11.5 (macros), section 12 (order with the legacy
preprocessor) and the last open point of section 16 ("the fate of each `%`
builtin function").

Tags are those of `PYTHON_PREPROCESSOR.md`: **[decided]**, **[open]**,
**[implemented]**. Everything below is **[open]** unless stated.

## 1. Where we start

`net.sourceforge.plantuml.tim.StandardFunctions` registers **75 `%` functions**
(`tim/builtin/*.java`, a shared, stateless registry). They are called from the
legacy preprocessor (`%upper("x")`, in expressions and in text).

On the Python side, today:

- builtin modules `math`, `json`, `puml` (`emit`, `error`, `macro`, `version`,
  `edition`, `args`);
- Python's own builtins and methods (sections 4.2 and 4.3), which already
  replace many `%` functions;
- shipped modules (section 15) are specified, not implemented.

The question is therefore twofold: **what is already covered for free**, and
**what still needs a home**, and where that home is (`puml`, a builtin module
written in Java, or a shipped module written in Python).

## 2. Three places a function can live

| Place | Written in | Good for | Constraint |
|---|---|---|---|
| `puml` (builtin module, section 13.1) | Java (host) | what needs the host: environment, file name, security profile, legacy variables, special markers | grows the one module every diagram has without `import`; keep it small |
| a **builtin module** (like `math`, `json`) | Java | pure computation that needs a Java class of PlantUML (the colour table `HColorSet`, `HSLColor`, hsluv) | needs the same code on the JAR, JS and C editions (TeaVM) |
| a **shipped module** (section 15) | Python | pure computation that fits the subset; testable with CPython as oracle (section 15.11) | 32-bit integers, no clock, no `random`, no I/O |

Rule of thumb: **Python if it can be, Java if it must be**. A shipped module in
Python is preferred because CPython is then the test oracle and the behaviour
is identical on every edition by construction.

## 3. Classification of the functions

### 3.1 Covered by Python itself: nothing to add

The result is the Python expression; the only deliverable is a line in the
user documentation ("legacy → Python" table).

| Legacy | Python | Remarks |
|---|---|---|
| `%upper(s)`, `%lower(s)` | `s.upper()`, `s.lower()` | same |
| `%strlen(s)` | `len(s)` | legacy counts Java `char`s; Python counts code points. Differs only outside the BMP. |
| `%substr(s, pos[, len])` | `s[pos:pos+len]` | legacy: `pos >= len(s)` gives `""`, `len` larger than the rest is clipped: same as slicing. Negative `pos` is **not** equivalent (Java `substring` throws). |
| `%strpos(s, sub)` | `s.find(sub)` | both give `-1` when absent |
| `%size(x)` | `len(x)` for str/list/dict | legacy gives `0` for a number: `0 if isinstance(x, int) else len(x)`. |
| `%string(x)` | `str(x)` | |
| `%intval(s)` | `int(s)` | legacy error "Cannot convert ..." becomes `ValueError`. Same 32-bit limit. |
| `%boolval(s)` | none | accepts `true/false/1/0`, case-insensitive, else error. One-liner, see 3.3. |
| `%chr(n)`, `%ord(c)` | `chr(n)`, `ord(c)` | legacy returns `"\0"` / `0` on error; Python raises. Deliberate difference. |
| `%dec2hex(n)`, `%hex2dec(s)` | `format(n, "x")`, `int(s, 16)` | legacy `dec2hex` of a negative number gives two's complement (`ffffffff`), Python gives `-1`. Legacy swallows errors, Python raises. |
| `%mod(a, b)` | `a % b` | **not identical**: Java `%` truncates (`-7 % 3 == -1`), Python floors (`2`). Legacy "Divide by zero" becomes `ZeroDivisionError`. Document it. |
| `%true()`, `%false()`, `%not`, `%and`, `%or`, `%xor`, `%nand`, `%nor`, `%nxor` | `True`, `False`, `not`, `and`, `or`, `!=` on bools ... | `xor` is `a != b`, `nxor` is `a == b`. No module needed. Note that legacy `%true()` is `1` and not a Python `bool` in some contexts: only matters when printing (`True` vs `true`): see 4.3. |
| `%splitstr(s, seps)` | `s.split(sep)` | **not identical**: legacy uses `StringTokenizer`: every character of `seps` is a separator and empty tokens are dropped. Needs a helper (3.3). |
| `%dollar()`, `%percent()` | `"$"`, `"%"` | exist only to dodge the legacy preprocessor. Dropped. |
| `%eval(expr)` | the expression itself | `eval` does not exist (section 4.2) and is not needed: `!{1+2}` or `1+2`. Dropped. |
| `%random(...)` | see 4.1 | |
| `%str2json(s)`, `%load_json` (from text) | `json.loads(s)` | `loads` raises `ValueError`; legacy returns `""` on error. |
| `%get_json_type(x)` | `type(x)` / `isinstance` | legacy names (`string`, `number`, `array`, `object`, `boolean`) can be mapped by a helper if wanted; probably not worth it. |
| `%get_json_keys(x)` | `list(x.keys())` | for a list of objects, legacy concatenates the keys of every element. |
| `%json_key_exists(x, k)` | `k in x` | |

### 3.2 Needs the host: additions to `puml`

These read something Python cannot see. They are plain values, so they fit the
rule "plain values only at the boundaries" (section 13.1).

| Legacy | Proposal | Notes |
|---|---|---|
| `%version()` | `puml.version` | **[implemented]** |
| `%feature(name)` | `puml.feature(name)` | today only `"style"` and `"theme"`, both `1`. Cheap. Or drop it: `%feature` exists for libraries that must run on old versions, and a Python module is versioned with PlantUML (section 15.8). Leaning: drop. |
| `%getenv(name)` | `puml.getenv(name)` | must go through the security profile (`canWeReadThisEnvironmentVariable`), and reads system properties first. Returns `""` when absent or forbidden (keep, `None` would be more Pythonic: **[open]**). Not available at all on JS/C editions? To check. |
| `%file_exists(path)` | `puml.file_exists(path)` | goes through the security profile; contradicts "no direct disk access from text Python" (section 13.1) unless it uses the same finder as `puml.read`. Leaning: through the finder only. |
| `%filename()`, `%dirpath()`, `%filedate()` | `puml.filename`, `puml.dirpath`, `puml.filedate` | values of the environment (`context.getEnvironmentValue`), constant for a diagram: attributes, like `puml.version`, not functions. |
| `%get_current_theme()` | `puml.theme` | a dict (the theme metadata), `None`/empty without theme. |
| `%get_all_theme()`, `%get_all_stdlib([detailed])`, `%get_stdlib(name)` | `puml.themes()`, `puml.stdlibs(detailed=False)`, ... | tools and documentation more than diagrams. Low priority; same family as `puml.modules()` (section 15.8). |
| `%load_json(path[, charset])`, also URL | `json.loads(puml.read(path))` | `puml.read` is already planned (section 13.1) with the `!include` rules. No separate function. The legacy default value argument becomes a Python `try/except` or `puml.read(path, default=...)` **[open]**. |
| `%xargs()` | `puml.xargs` | only for the `-x` command line option; probably dropped. |
| `%newline()` / `%n()`, `%breakline()`, `%tab()`, `%left_align()`, `%right_align()` | see 4.2 | return **private marker characters** (`Jaws.BLOCK_E1_*`), not `"\n"`. They cannot be written by the user as plain Python strings. |

### 3.3 Pure computation, shipped modules in Python

Candidates for a first module, because they do not exist in the subset and are
easy to get exactly right with CPython as oracle. Names are provisional.

**Module `strutil`** (or fold into one module `std`, see section 5):

- `splitstr(s, seps)`: `StringTokenizer` semantics (any character of `seps`,
  no empty token). Otherwise people write `s.split()` and get a different
  result.
- `boolval(x)`.
- `hex`/`dec` helpers are not needed (3.1).

**Module `jsonx`** (the `%json_*` functions). The legacy ones are *pure*: they
return a modified **copy** and never touch their argument, which is not what a
Python `list.append` does. Worth providing so that translated code keeps its
meaning:

- `add(data, value)` for a list, `add(data, name, value)` for a dict;
- `set(data, index_or_name, value)`, and `set(dict, dict)` which **deep merges**;
- `remove(data, index_or_name)`;
- `merge(a, b)`: lists concatenate; dicts merge (shallow `merge`, not deep: the
  legacy `JsonObject.merge` behaviour must be re-read before specifying);
- all return a new value, leave their argument unchanged, and return the
  argument as is when it is not a list or a dict (legacy: `return data`).

The subset has no `copy` module and no `deepcopy`, so `jsonx` needs its own
recursive copy. A plain `dict(d)` / `list(l)` is enough for most diagrams and
should be shown first in the documentation; `jsonx` is for fidelity.

**Colours: the real question.** `%darken`, `%lighten`, `%is_dark`,
`%is_light`, `%reverse_color`, `%reverse_hsluv_color`, `%hsl_color` are the
functions libraries and themes use most (the C4 library, themes). They depend on:

- the **colour table** `HColorSet` (named colours: `"red"`, `"LightBlue"`,
  `"#RRGGBB"`, `"#RRGGBBAA"`, transparent, gradients?), about 150 names, loaded
  from a resource;
- the exact formulas of `HColor.darken/lighten/reverse/isDark/reverseHsluv`
  (inspired from the JavaScript package `color`) and `HSLColor`.

Two options:

1. **Builtin Java module `color`** reusing `HColorSet` and `HColor`:
   guaranteed identical to the legacy output (byte for byte, which matters for
   Vega non-regression), at the price of a Java API surface and TeaVM checks.
   **Decided and [implemented]: this one** (`PythonColorModule`, tests in `vega/python/color` comparing with the `%` functions). The Python side is
   `color.darken("red", 20)` returning the same string as `%darken`.
2. **Python module** with its own table and formulas: portable, but a second
   implementation of the colour rules to keep in sync, and rounding behaviour
   (`double` in Java, `float` in Python subset? the subset has `float` only as a
   Java `Double`) has to be proven identical. Not worth it now.

Errors: legacy gives "No such color" at the line; a `ValueError` with the same
text, reported on the line (section 9), is the natural equivalent.

### 3.4 Legacy-preprocessor functions: no Python equivalent needed (or a bridge)

These manipulate the *legacy* preprocessor's variables, functions and
procedures. In a Python diagram the variables are Python variables and the
functions are Python functions.

| Legacy | Python | Remarks |
|---|---|---|
| `%call_user_func("name", args)` | a direct call, or a dict of functions | The subset has no `getattr`/`globals()`. A Python author keeps a dict (`{"bold": bold}[name](x)`). Not provided. |
| `%invoke_procedure`, `%retrieve_procedure` | same | idem. |
| `%function_exists("$f")` | none | the name of a Python function is known statically. A `try/except NameError` covers the rest. |
| `%variable_exists("$v")` | `try/except NameError` | |
| `%get_variable_value("$v")`, `%set_variable_value("$v", x)` | none by default | **[open]** the bridge between the two worlds: "a way to read a legacy variable from Python" is already an open point of section 15.9. If a bridge is built, it is `puml.get(name)` / `puml.set(name, value)` and nothing more, and it is read-only for the first release. |

Whether to provide the bridge is a **migration** question (section 6), not an
API one: library code translated from legacy to Python never needs it, since
the whole library moves at once. It is only useful for a *diagram* that mixes
`$variable` set by the legacy part and Python code.

## 4. Points that need a decision

### 4.1 Time and randomness: `%now`, `%date`, `%random`

Section 13.1 and 15.7 **decided** "no clock and no `random` module, so a diagram
renders the same on every edition". `%now()`, `%date()` and `%random()` break
this, and they exist and are used in real diagrams (footers with the date,
"generated on").

Options:

1. **Keep the ban.** `puml.args` carries a date when the user wants one
   (`-Dnow=...`), which is already how a reproducible build would do it.
   Simple, consistent, and the migration path says "pass the date".
2. **Allow them in `puml` only**, explicitly marked non-deterministic
   (`puml.now()`, `puml.date(fmt, t=None, tz=None)`, `puml.random(...)`),
   not in shipped modules (15.7 stays true for them).
3. **Seed**: `puml.random` deterministic, from a seed taken from the diagram
   (a hash of the source, or `-Dseed`). Reproducible across runs and editions.

Leaning: **2 for `now` and `date`**, **3 for `random`**. If chosen:

- `%date` uses `java.text.SimpleDateFormat` patterns (`yyyy.MM.dd' at 'HH:mm`),
  which are **not** `strftime`. Offering `strftime` is the Pythonic choice but
  breaks translation of existing diagrams: **[open]**, probably
  `puml.date(fmt)` keeps the Java pattern, documented as such.
- time zones: `%date(fmt, epoch, "Europe/Paris")` validates the id. The JS and C
  editions may not have a zone database: check before promising it.
- `%now()` is a 32-bit epoch (it overflows in 2038 and `int(x)` in the subset is
  32-bit too, section 6). Fine for now, worth a remark.

### 4.2 Newline, tab and alignment markers

`%newline()`, `%n()`, `%breakline()`, `%tab()`, `%left_align()` and
`%right_align()` return private characters (`Jaws.BLOCK_E1_*`) that the later
stages understand (a newline *in the rendered text*, as opposed to a newline
in the PlantUML source). A Python string cannot contain them by accident, and
`"\n"` in a string given to `emit` is a **source** newline (`emit` splits
lines), which is `%breakline()` rather than `%newline()`.

Proposal: constants of `puml`, because they are values and not computations:

```python
puml.NEWLINE      # %newline(), %n()
puml.BREAKLINE    # %breakline()
puml.TAB          # %tab()
puml.LEFT_ALIGN   # %left_align()
puml.RIGHT_ALIGN  # %right_align()
```

or, closer to the legacy, `puml.newline()`. To settle with the
`JawsFlags.USE_BLOCK_E1_IN_NEWLINE_FUNCTION` flag in mind: the value depends on
a flag, so a constant fixed at the creation of the interpreter is fine (the
flag is global), but this must be verified. `%backslash()` is the same
family (`Jaws.BLOCK_E1_REAL_BACKSLASH`) and gets `puml.BACKSLASH`.

### 4.3 Booleans and integers printed in text

In the legacy preprocessor `%true()` prints `1` and a comparison prints
`true`/`false`, depending on context (`TValue`). In Python `str(True)` is
`True`. `!{x}` of a bool in a text line gives `True`, which is rarely what a
PlantUML line expects (`skinparam shadowing true`). This is not a function
issue, it is a **formatting** one, and it deserves its own paragraph in
section 11 or 13: an `!{...}` of a `bool` should print `true`/`false` (lower
case)? Raise it separately **[open]**.

### 4.4 Errors

Legacy functions fail with `EaterException` located on the line; several
**swallow** errors and return a neutral value (`%chr`, `%ord`, `%hex2dec`,
`%dec2hex`, `%str2json`). The Python translation raises. The legacy-to-Python
table must say it for each function, and a module shipped to replace a legacy
function must say whether it keeps the lenient behaviour. Leaning: **raise**
everywhere in new modules (the Python way), and keep the lenient behaviour
only in a function whose purpose is leniency.

## 5. How many modules?

Proposals, from fewest to most:

| Layout | Content | Comment |
|---|---|---|
| **A. Everything in `puml`** | `puml.upper`... | No: pollutes the module that every diagram has without `import`, and duplicates Python builtins. |
| **B. One shipped module `std`** | `splitstr`, `boolval`, json helpers, plus colours re-exported from `color` | One import, easy to document as "the `%` functions". Mixed concerns. |
| **C. Small modules by theme** (leaning) | `color` (Java builtin, 3.3), `jsonx` (Python), `strutil` (Python, tiny); the rest in `puml` (3.2) | Matches section 15.2 (flat names, one concern each), loads only what is used. |

Leaning: **C**, with `strutil` possibly folded into `jsonx` under a neutral name
if it stays at two functions (`splitstr`, `boolval`).

A flat list of names is imposed by section 15.3 (no dotted names), so there is
no `plantuml.color`; `color` is the module name, and must not clash with
user-facing identifiers (modules are not visible in the diagram globals unless
imported, section 15.4).

## 6. Migration story: "legacy → Python" table

Whatever is decided, the deliverable is a table in the user documentation, one
row per `%` function, with four columns: *legacy*, *Python*, *differences*,
*status* (covered, `puml`, module, dropped). Section 3 is its draft. A
function with a non-trivial difference (`%mod`, `%splitstr`, `%dec2hex`,
`%size`, `%strlen` outside the BMP, `%date` patterns) deserves a test that
shows both results side by side.

## 7. Tests

- Pure functions of a **shipped Python module**: scripts under
  `src/test/resources/python/` with CPython as oracle (section 15.11).
- Functions that must give **the same result as the legacy function**
  (`darken`, `lighten`, `hsl_color`, `reverse_*`, `json*`): a **Vega** diagram
  using both side by side, e.g. `!{color.darken("red", 20)}` and
  `%darken("red", 20)`, in a note or a label, with the same expected SVG. This
  is the cheapest way to guarantee non-regression for the colour functions.
- `puml` additions: a Vega diagram each, and a case in the security profile
  tests where relevant (`getenv`, `file_exists`, `read`).
- A test that lists the registered `%` functions (from
  `StandardFunctions`) and checks that each has a row in the legacy → Python
  table: the table cannot silently fall behind when a function is added.

## 8. Order of work (proposal)

1. Settle the open points of section 4 (time and random, markers, bool
   formatting) and the module layout of section 5.
2. The legacy → Python table (documentation only; no code), including the
   differences of section 3.1.
3. `puml` additions that are plain values: `filename`, `dirpath`, `filedate`,
   `theme`, the marker constants.
4. `color` (the most used by libraries), with the side-by-side Vega tests.
5. `jsonx` and `strutil` as the first **shipped Python modules**, which also
   exercises the loading mechanism of section 15 on something small.
6. `puml.getenv`, `puml.file_exists`, `puml.read` behind the security profile.
7. The date/random decision implemented.

## 9. Questions for the owner

1. Time and randomness (4.1): keep the ban, or `puml.now/date/random` (with a
   seeded `random`)?
2. Colours (3.3): a Java builtin module `color` that reuses `HColorSet`, or a
   port to Python?
3. Layout (section 5): one module `std`, or small modules by theme?
4. Do we want the bridge to legacy variables (3.4), or is "the whole library
   moves at once" enough?
5. `bool` printed in text (4.3): lower case in `!{...}`?
6. Errors (4.4): always raise, including where the legacy function was lenient?
