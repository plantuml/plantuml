# PR: SVG sprite parser selection via pragma

## Title
Enable SVG sprite parser selection via pragma (Nano default)

## Description
This PR implements pragma-based selection for SVG sprite parsing while keeping the Nano parser as the default to preserve backward compatibility. The SAX parser can be enabled explicitly with a pragma and remains opt-in. TeaVM builds always force Nano to avoid missing `javax.xml.parsers` during JavaScript generation.

## Motivation / Context
Maintainers requested that Nano remain the default parser and SAX be explicitly enabled. This change provides a simple, diagram-local switch that avoids breaking existing users while allowing testing and adoption of the SAX parser.

Related PR: https://github.com/plantuml/plantuml/pull/2548 (closed, #2548)

## Current behavior
- SVG sprite parsing uses Nano by default.
- There was no pragma-based selector for SAX vs Nano.

## New behavior
- Nano remains the default.
- The SAX parser is enabled per diagram using pragmas:

```
!pragma svgparser sax
```

or

```
!pragma svgparser nano
```

## TeaVM behavior
TeaVM does not include `javax.xml.parsers`. The parser factory forces Nano when `TeaVM.isTeaVM()` is true. TeaVM resolves this branch at compile time, so the JVM-only SAX path is removed from generated JavaScript output and class stubs.

## Tests
- Updated SVG sprite tests to use the SAX pragma where appropriate.
- Parser selection tests now validate pragma behavior and Nano default.

## Documentation
- Documented Nano vs SAX constraints in the svg parser package Javadoc.
- Added a package README that highlights Nano default behavior and SAX capabilities/constraints.

## Notes / Limitations
- SAX parser supports inline attributes only (no CSS `<style>` blocks or class selectors).
- Gradient rendering is simplified (first/last stops only; direction approximated).
- Embedded raster images are supported only via data URIs (PNG/JPEG).

## SAX Advantages
- Broader SVG element coverage: rect, circle, ellipse, line, polyline, polygon, path, text, image, group, defs, symbol, use.
- Transform parsing (translate, rotate, scale, matrix).
- Consistent inline styling across supported elements.
- Linear gradient fills (with simplified stop handling).

## Files changed (high level)
- Pragma key and selection logic for SVG parser choice.
- TeaVM fallback to Nano.
- Tests and SVG sprite fixtures updated to use `!pragma svgparser sax`.
- SVG parser documentation updates.
