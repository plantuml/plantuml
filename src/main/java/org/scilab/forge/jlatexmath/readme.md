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
(checked byte for byte on the corpus in `src/test/resources/jlatexmath-corpus`).

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
