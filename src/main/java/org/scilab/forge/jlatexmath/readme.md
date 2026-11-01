# org.scilab.forge.jlatexmath

Vendored copy of **JLaTeXMath**, a Java library that renders LaTeX formulas.

- Upstream: https://github.com/opencollab/jlatexmath
- Version: tag `1.0.7`, commit `cb86da9c5747ba1788fcb913988b98d82cb0bc0d` (2018-04-18)
- Content: the `jlatexmath` core module (156 Java files), including the
  `cyrillic` and `greek` registration classes of the font modules. The
  resources (fonts and XML descriptions) are in
  `src/main/resources/org/scilab/forge/jlatexmath`.
- License: GNU GPL version 2 **or later**, with the GNU linking exception, see
  the header of every file and the `COPYING` file of this directory.
  The headers are kept as they are.

## State

Phase 1 vendored the upstream files unmodified. **Phase 2 of `PLAN.md` has
started to modify them**, in small steps that each keep the rendering identical
(checked byte for byte on the corpus, then in `src/test/resources/vega/math` since
step 2c; the PNG baselines of the original renderer are in
`src/test/resources/jlatexmath-corpus`).

Step 2a, drawing through a canvas instead of `java.awt.Graphics2D`:

- added: `TeXGraphics` (the subset of Graphics2D that the boxes use) and
  `Graphics2DTeXGraphics` (the same calls on a real Graphics2D);
- modified: `Box.draw` and its subclasses (`CharBox`, `FramedBox`, `OvalBox`,
  `ShadowBox`, `HorizontalRule`, `OverUnderBox`, `GlueBox`, `StrutBox`,
  `HorizontalBox`, `VerticalBox`, `ReflectBox`, `RotateBox`, `ScaleBox`) take a
  `TeXGraphics`; `TeXIcon.paintIcon` wraps its Graphics2D in one;
- `GraphicsBox`, `GeoGebraLogoBox`, `FcscoreBox` and `JavaFontRenderingBox`
  need AWT (images, Java fonts): they use `TeXGraphics.getGraphics2D()` and draw
  nothing on a canvas that is not AWT. Their removal is planned (phase 3).

Step 2b-2, the glyphs as paths (no AWT font any more for the new canvas):

- added: `GlyphOutlines`, the reader of the `glyphs.bin` files made by
  `tools/jlatexmath-glyphs/GenerateGlyphOutlines.java` (no AWT, no `javax.*`,
  no reflection: it gives the outline of a glyph to a `Sink`, in font units);
- modified: `FontInfo` has `getGlyphs()` (the outlines of its font, read on the
  first use) and `getFontInfo(int)`. `getFont()` stays for the AWT canvas.
- checked by `tools/jlatexmath-glyphs/CheckGlyphOutlines.java`: the 51 fonts
  (base, Cyrillic, Greek) and 5229 glyphs read through `GlyphOutlines` differ
  from the AWT outlines by 0.84% of the area at most.

Step 2b-2, the canvas on `UGraphic`:

- added: `UGraphicTeXGraphics` (in `net.sourceforge.plantuml.math`, so that this
  package does not depend on PlantUML): a `TeXGraphics` that keeps its own
  affine transformation and gives paths to a `UGraphic`;
- modified: `TeXIcon.paint(TeXGraphics, Color, x, y)` draws the formula on any
  canvas (`paintIcon` is unchanged); if no color is given, the canvas keeps its own.
- checked on the 11 `latex-*` formulas of the corpus, PNG and SVG: same size as
  the baselines, same layout and shapes. The pixels are not the same, as
  expected: AWT hints the thin horizontal strokes (the shaft of an extensible
  arrow is one crisp pixel row), the path is not hinted (two grey rows), and
  the pieces of an extensible arrow, drawn as separate paths, show their seams.
  Step 2d of `PLAN.md` decides what to do about it, if anything.

Everything else is as upstream. The package is listed in `CLAUDE.md` among the
external packages, so that edits stay deliberate.

## License variants

This code is GPL, so it is removed from the MIT, EPL, BSD, ASL and LGPL
variants:

- the Java sources, by the `// ::remove folder` directive of
  [`package-info.java`](./package-info.java), the only file of this directory
  that is not from upstream (apart from this readme and `COPYING`);
- the resources, by the `processResources` exclusion in
  `license-variants/build-logic/.../plantuml.license-variant.gradle.kts`;
- the callers in `net.sourceforge.plantuml.math` (`TeXIconBuilder`,
  `ConverterSvg`) are guarded with `// ::comment when ...` and, for
  `TeXIconBuilder`, replaced by a `ClassNotFoundException`: the formula is then
  shown as text, exactly as when the jar was missing.
