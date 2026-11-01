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
  arrow is one crisp pixel row), the path is not hinted (two grey rows). This
  is kept (step 2d of `PLAN.md`): a vector output has no pixel grid to hint for.

Step 2d, the glyphs that follow each other with the same color are one path:

- `UGraphicTeXGraphics` gathers them, and the rasterizer fills their union.
  Drawn one by one, the pieces of an extensible arrow or brace overlap and their
  shared edge pixels are painted twice (dark dots on the shaft), as the
  strokes of `\not` and of the crossing symbols. Nothing is drawn out of order:
  the glyphs are flushed before any other shape, at a color change and at the
  end of the formula (`flush()`). A glyph that covers the hole of another one
  would fill it (non-zero rule): checked on the corpus and on a formula of about 60 symbols
  that overlap (`\not\in`, `\oint`, `\bigoplus`, accents on letters, ...), no
  difference but the cleaner crossings. The SVG of a formula is one path.
- the points that a font repeats are not drawn twice.

Phase 3, step 3a, what PlantUML does not use is removed (about 2,000 lines, all
of it tied to AWT, to the class loader or to files):

- images, Java fonts, GeoGebra logo, Fcscore: `GraphicsAtom`, `GraphicsBox`,
  `JavaFontRenderingAtom`, `JavaFontRenderingBox`, `GeoGebraLogoAtom`,
  `GeoGebraLogoBox`, `FcscoreAtom`, `FcscoreBox` and their macros
  (`\includegraphics`, `\GeoGebra`, `\jlmText`, `\jlmTextit`, `\jlmTextbf`,
  `\jlmTextitbf`, `\jlmExternalFont`, `\fcscore`);
- the image cache (`cache/JLaTeXMathCache`, `internal/util/Images`);
- the alphabets loaded from a URL or Web Start (`URLAlphabetRegistration`,
  `WebStartAlphabetRegistration`);
- the predefined formulas and commands read from a user XML file
  (`TeXFormulaParser`, `PredefinedTeXFormulaParser`, `TeXFormula.addPredefined*`),
  which worked by reflection;
- the external fonts of `TeXFormula` (`externalFontMap`, `FontInfos`,
  `registerExternalFont`).

Phase 3, step 3a, no more AWT canvas, AWT fonts or Swing:

- removed: `Graphics2DTeXGraphics`, `TeXGraphics.getGraphics2D()`,
  `TeXIcon.paintIcon` (and `implements Icon`: `getIconWidth` and `getIconHeight`
  stay), the image API of `TeXFormula` (`createImage`, `createPNG`,
  `createBufferedImage`, `setDefaultDPI` with `Toolkit`), `registerFonts`,
  `DefaultTeXFontParser.createFont` and every `java.awt.Font`
  (`Char.getFont`, `FontInfo.getFont`);
- the 46 TTF files are not in the jar any more: they moved to
  `tools/jlatexmath-glyphs/ttf`, with the generator and the checker of
  `glyphs.bin` (the generator makes the same three files again).

A character that has no TeX glyph (CJK, for instance) was drawn with a Java font
by `JavaFontRenderingAtom`; since step 2c that box drew nothing (a blank gap).
That is fixed by the next step.

Text without a TeX glyph (Chinese, Japanese, Korean, Arabic, emoji... any
character that has no symbol) is drawn by the application that hosts the
library, with its own font:

- added: `TeXTextMeasurer` (the size of a text, for a font size of 1: width,
  height above the baseline, depth), `ForeignTextAtom` and `ForeignTextBox`, and
  `TeXGraphics.drawText(String, double)`;
- modified: `TeXParser.convertCharacter` makes one `ForeignTextAtom` for a run
  of such characters (it stops at a character that has a symbol, at ASCII and at
  an alphabet that has fonts, such as Greek or Cyrillic; a control character is
  still a `ParseException`); `TeXEnvironment` carries the measurer, given by
  `TeXFormula.TeXIconBuilder.setTextMeasurer`. It is a field of the environment,
  not a static one, so that formulas made at the same time can have different
  measurers. Without a measurer a wide character is one em and the others half
  an em.
- in PlantUML, `StringBounderTextMeasurer` measures with the `StringBounder` of
  the diagram and `UGraphicTeXGraphics.drawText` draws a `UText` with the same
  font (sans serif), so the layout is the one of the drawing in every output
  format. A text can be neither rotated nor flipped (UGraphic cannot), and its
  size is a whole number of pixels.

A formula no longer changes the formulas made after it (a bug of upstream, found
by a Vega run in another order): `SymbolAtom.get` returns a copy of the symbol
of the table. `\limits`, `\nolimits` and `\normal` change the atom that they are
given, and `setUnicode` too; it was the shared one, so after a single
`\sum\nolimits` every `\sum` of the JVM had its limits as scripts. What is still
global, as upstream, on purpose: the commands of `\newcommand` and the colors of
`\definecolor` stay defined for the next formulas (a formula that uses `\R`
works after another one has defined it).

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
