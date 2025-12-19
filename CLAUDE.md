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

## Code Style Guidelines

- **Java Version**: Java 8 compatibility is required. Do not use `var`, lambda features beyond Java 8, or other post-Java 8 syntax.
- **Indentation**: Use tabs for indentation, not spaces.
- **Braces**: Opening braces on the same line as the statement.
- **Imports**: Explicit imports, no wildcard imports.
- **Final variables**: Prefer `final` for local variables when possible.
- **Boolean negation**: Prefer positive conditions (`foo == false`) over negation operators (`!foo`).

## Architecture Notes

- Main source code is in `src/main/java/net/sourceforge/plantuml/`
- The project uses a custom preprocessor and parser for PlantUML syntax

