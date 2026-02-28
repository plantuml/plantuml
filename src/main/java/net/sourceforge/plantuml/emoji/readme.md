# Directory Documentation for `emoji`

## Description
This package provides classes used to manage [PlantUML Emoji](https://plantuml.com/en/creole#68305e25f5788db0) icon set.

## Constraint
The SAX-based SVG parser supports only inline attributes and does not parse CSS
`<style>` blocks or class selectors.
The SAX parser honors `text-anchor` for `<text>` elements; positioning can
differ from the Nano parser when anchors are used.
Embedded raster images (PNG/JPEG data URIs in `<image>` elements) are
supported; external URLs and embedded SVG images are not.
Default parser on this branch is SAX (see SvgSpriteParserFactory).

## Link
- [PlantUML Emoji](https://plantuml.com/en/creole#68305e25f5788db0) icon set

## Credit
- :octocat: [EmojiTwo/emojitwo](https://github.com/EmojiTwo/emojitwo)
