# SVG Parser Package

This package provides SVG sprite parsing support, including a zero-dependency SAX-based parser and helper classes for sprite rendering.

## SAX parser constraints
- Inline attributes only; CSS style blocks and class selectors are ignored.
- `text-anchor` is honored for `<text>` elements; positioning can differ from the Nano parser.
- Embedded raster images via data URIs (PNG/JPEG) are supported.
- External image URLs and embedded SVG images are not supported.

## Gradient constraints
- Linear gradients are supported, but only the first and last stops are used.
- Intermediate stops, offsets, and `stop-opacity` are ignored.
- Gradient direction is approximated to horizontal, vertical, or diagonal.
- Radial gradients are not supported.
