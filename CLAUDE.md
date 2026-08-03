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

`.clide/` holds the committed compile-only stubs (TeaVM, OpenPDF, Ant, opentest4j).
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

**Generated code** (do not modify):
- `gen` - generated code
- `h` - generated code

**External/third-party packages** (do not modify):
- `jcckit` - external charting library
- `zext` - external library
- `org.stathissideris` - external library (ditaa)

