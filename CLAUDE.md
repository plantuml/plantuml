# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PlantUML is a tool that allows users to create UML diagrams from plain text descriptions. It supports various diagram types including sequence, class, activity, component, state diagrams, and more.

## Build and Test Commands

```bash
# Build the project
gradle build

# Run tests
gradle test
```

If Gradle cannot download its distribution (offline or restricted sandbox), Ant
is a fully working fallback and needs no network access:

```bash
# Requires a full JDK (not just a JRE) and ant
ant dist          # compiles src/main/java and produces ./plantuml.jar
```

`.clide/` holds the committed compile-only stubs (TeaVM, Ant, opentest4j; OpenPDF is part of the sources, in `src/main/java/org/openpdf`).
The test jars themselves (JUnit 5, JUnit Pioneer, XMLUnit) are **not** committed:
clide unpacks them into the gitignored `.clide/tmp/jar-junit/` the first time it
opens this project, and they can also be copied from a clide checkout (`lib/`).
Once they are there, tests compile and run without Gradle:

```bash
javac -nowarn -d /tmp/testclasses \
      -cp "plantuml.jar:.clide/*:.clide/tmp/jar-junit/*" \
      -sourcepath src/test/java src/test/java/test/vega/VegaTest.java

java -jar .clide/tmp/jar-junit/junit-platform-console-standalone-*.jar execute \
     -cp "plantuml.jar:/tmp/testclasses:src/test/resources" \
     --select-class test.vega.VegaTest --details=summary
```

`src/test/resources` must be on the runtime classpath, and the working directory
must be the repository root (Vega resolves `src/test/resources/vega` relatively).

## Reproducing a diagram from the command line

```bash
java -jar plantuml.jar -tsvg  -pipe < test.puml   # SVG on stdout
java -jar plantuml.jar -tutxt -pipe < test.puml   # quick ASCII check
```

A syntax error usually shows up as `Syntax Error? (Assumed diagram type: sequence)`:
when a command regex stops matching, the parser falls back to a sequence diagram,
so the reported line is rarely the real culprit.

## Vega: the main regression harness

`test.vega.VegaTest` walks `src/test/resources/vega/**` and turns every `.puml`
file into a dynamic test. Adding a non-regression test means adding one file:

```
---
output: svg              # also: utxt, atxt, debug, latex, scxml, graphml, xmi, preproc
dark: true               # optional: render as --dark-mode does
expected-description: (1 entities)
---
@startuml
...
@enduml
```

Then generate the reference file(s) once, review them, and re-run without the flag:

```bash
VEGA_FORCE_WRITE=true <run VegaTest>   # writes the .svg/.txt/... next to the .puml
```

Notes:
- Always check that the new test **fails** before the fix and passes after it.
- A file with no YAML header is always regenerated and never asserted.
- `vega.json`, `vega-summary.txt` and `vega-summary.md` are rewritten on every
  run; revert them unless the change is meaningful.
- The `.puml` file name has no meaning for the framework: the directory groups
  the tests, so put a class-diagram non-regression in `vega/nonreg/simple/`.

## Digging into history

The recommended shallow clone has no history to bisect. To find when a line
changed, deepen first:

```bash
git fetch --deepen 500
git log -L <start>,<end>:<file>
```

## Cloning this repository

Do a shallow clone - do not fetch the full history:

```bash
git clone --depth 1 https://github.com/plantuml/plantuml
```

## Code Style Guidelines

- **Java Version**: Java 8 compatibility is required. Do not use `var`, lambda features beyond Java 8, or other post-Java 8 syntax.
- **Indentation**: Use tabs for indentation, not spaces.
- **Braces**:
  - Opening braces on the same line as the statement.
  - For `if`/`for`/`while` with a single statement: no braces, statement on the next line with indentation.
  - For blocks with multiple statements: braces required, opening brace on the same line.
- **Imports**: Explicit imports, no wildcard imports.
- **Final variables**: Prefer `final` for local variables when possible.
- **Boolean negation**: Prefer positive conditions (`foo == false`) over negation operators (`!foo`).

## Architecture Notes

- Main source code is in `src/main/java/net/sourceforge/plantuml/`
- The project uses a custom preprocessor and parser for PlantUML syntax

## Packages to avoid modifying

**Generated code**:
- `gen` - Java port of Graphviz (Smetana). It may now be modified: when a
  piece of the original C code is still `UNSUPPORTED(...)`, port it faithfully
  from Graphviz 2.38 and keep the original C line as a trailing comment.
  Keep such changes minimal and cover them with a test.
- `h` - generated code (do not modify)

**External/third-party packages** (do not modify):
- `jcckit` - external charting library
- `zext` - external library
- `org.stathissideris` - external library (ditaa)
- `org.scilab.forge.jlatexmath` - external library (JLaTeXMath 1.0.7, vendored; the drawing is being replaced by a canvas in phase 2 of `PLAN.md`, see its `readme.md` for what is modified)



## clide (semantic navigator and test-runner bootstrap)

**clide** is a semantic code navigator. It is two Python entry points around one
Java **daemon** that does the actual work: `start_clide.py` starts it, `clide.py`
talks to it, and neither of them is `java` itself. Read `clide/CLAUDE.md` once;
it is kept current and what follows can drift.

clide builds itself: `start_clide.py` runs `build_clide.py` (`javac` plus
Python's `zipfile`, a few seconds, only when `clide.jar` is missing or stale), so
no `ant` or Gradle is needed for clide. It needs a **JDK 21** and `python3` on
`PATH`. This is a requirement of clide only: PlantUML itself must still stay
Java 8 compatible (see Code Style Guidelines). If `java`/`javac` is missing,
`start_clide.py` says so plainly.

**Why you need it even for a five-line fix.** Beyond navigation, clide provides
the JUnit console runner used in "Vega: the main regression harness": it unpacks
the test jars (JUnit 5, JUnit Pioneer, XMLUnit) into the gitignored
`.clide/tmp/jar-junit/` the first time it runs against the project. Those jars
are not committed. Skip clide and you cannot run the Vega tests, which means no
reference files and no proof that the fix does not move anything else. Grep is
enough to *find* a small bug; it is not enough to *finish* the job.

Clone clide next to the PlantUML checkout (shallow, like this repository), so
that it sits in `../clide`:

```bash
git clone --depth 1 https://github.com/plantuml/clide ../clide
```

**Step 1 - start the daemon, once.** From the repository root:

```bash
python3 ../clide/start_clide.py .
# prints the boot trace, then a line ending "... is ready to use." - proceed once you see it
ls .clide/tmp/jar-junit/   # the JUnit jars should now be there
```

It launches the daemon detached, prints its boot trace live and waits for it to
be ready. One daemon per project, reused for the whole session. Running the same
command again is safe: it recognizes the running daemon and starts nothing
further. A client that finds no daemon fails with a message naming this exact
command - that is the fix, not a retry of the client.

**Step 2 - connect a client and send commands.** `clide.py` is the *only*
client. It is line-oriented, **one token per line**: keyword first, then one
line per parameter, `exit` last. This wrapper pays for itself immediately (run it
from the repository root; the paths are resolved when the script is written):

```bash
cat > /tmp/c.sh <<EOF
#!/bin/bash
{ cat; echo exit; } | python3 "$(cd ../clide && pwd)/clide.py" "$(pwd)" 2>/dev/null
EOF
chmod +x /tmp/c.sh
printf 'help\nexit\n' | /tmp/c.sh      # once: every command with its exact arity
printf 'find_symbol\nSomeClassName\n' | /tmp/c.sh
```

The project path may be left out of a bare `clide.py` call (it defaults to the
last one `start_clide.py` was pointed at); the wrapper keeps it explicit because
that is safer in a script reused across a whole session. `man <keyword>` details
one command.

Two things to get right the first time:

- `find_symbol SomeClassName` on one line fails with `UNKNOWN_KEYWORD`: the whole
  line was looked up as a keyword. One token per line.
- Never open a second client before the first one finished with its own
  command. Two short-lived invocations back to back are fine - each connects,
  finishes, and disconnects with `exit` - and no daemon-side state is lost
  between them (transactions and open files live in the daemon).

Each invocation pays a fixed ~40 ms (Python starting and connecting to the warm
daemon), once per invocation and not per command, so pipe several commands
before `exit` when all their parameters are known up front:

```bash
printf 'rebuild\nerrors\nfind_symbol\nSomeUnrelatedClassName\n' | /tmp/c.sh
```

A command that needs a position printed by the previous one (`find_symbol`
feeding `list_members`) still has to be its own invocation.

**If clide cannot be fetched or started** in your environment, carry on with grep
and `ant dist`, but say so in your reply, together with what this prevented:
without the JUnit jars, the Vega tests cannot be run, so there are no reference
files and no regression check.