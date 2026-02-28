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
- Linear gradients are supported, but only the first and last stops are used.
- Intermediate stops, offsets, and `stop-opacity` are ignored.
- Gradient direction is approximated to horizontal, vertical, or diagonal.
- Radial gradients are not supported.
