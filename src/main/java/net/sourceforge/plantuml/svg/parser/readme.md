# SVG Parser Package

This package provides SVG sprite parsing support, including a zero-dependency SAX-based parser and helper classes for sprite rendering.

## Parser selection
Nano is the default parser. Use a pragma to explicitly select SAX parsing:

```
!pragma svgparser sax
```

Or to reassert Nano:

```
!pragma svgparser nano
```

## Nano parser constraints
- Legacy, regex-based parsing with a limited SVG subset.
- No CSS `<style>` blocks or class selectors.
- Definitions and references (such as `<defs>` and `<use>`) are not parsed.
- SVG gradients are not parsed.

## SAX parser constraints
- Inline attributes only; CSS style blocks and class selectors are ignored.
- `text-anchor` is honored for `<text>` elements; positioning can differ from the Nano parser.
- Embedded raster images via data URIs (PNG/JPEG) are supported.
- External image URLs and embedded SVG images are not supported.

## SAX parser additional features
- Broader SVG element coverage: rect, circle, ellipse, line, polyline, polygon, path, text, image, group, defs, symbol, use.
- Transforms are parsed (translate, rotate, scale, matrix).
- Inline style attributes are applied consistently across element types.
- Gradient fills are supported (linear gradients, with simplified stops).

## SAX gradient constraints
- Linear gradients support multiple stops and `stop-opacity`.
- Gradient direction follows the SVG `x1/y1/x2/y2` vector.
- Radial gradients are not supported.

## Scope: sprites vs. standard diagrams
- Multi-stop and spreadMethod handling applies only to SVG sprites parsed by the SAX sprite parser.
- Standard PlantUML diagram gradients (e.g., `#ffd200|8cfcff`) remain two-stop and use the existing `HColorGradient` flow.

## Test coverage note
- No dedicated unit tests for non-sprite gradient rendering were found as part of this change set; the focused test run was the SVG sprite image generation suite.
