# `openpdf` — PDF backend for PlantUML

A native, fully integrated PDF backend built on [OpenPDF](https://github.com/LibrePDF/OpenPDF). Plugs into PlantUML's `UGraphic` rendering pipeline alongside `UGraphicSvg` / `UGraphicG2d` / `UGraphicTikz`, and produces vector PDF — text stays selectable, geometry stays scalable, file size stays small.

This backend is reached via the standard PlantUML mechanism: `FileFormat.PDF.getDefaultStringBounder()` returns a `StringBounderOpenPdf` and the rendering pipeline instantiates a `UGraphicPdf` — no caller-visible API change.

## Description

The backend is split between a low-level PDF emitter (`PdfGraphics`) and a `UGraphic` adapter (`UGraphicPdf` plus a driver per shape type). The split mirrors what the SVG backend does in `klimt.drawing.svg`:

```
PdfGraphics              <-- low-level emitter (`pdfRectangle`, `pdfLine`, `text`, `pdfPath`, ...)
UGraphicPdf              <-- UGraphic adapter, registers the drivers below
  DriverRectanglePdf     <-- URectangle  -> PdfGraphics.pdfRectangle
  DriverLinePdf          <-- ULine       -> PdfGraphics.pdfLine
  DriverEllipsePdf       <-- UEllipse    -> PdfGraphics.pdfEllipse
  DriverPolygonPdf       <-- UPolygon    -> PdfGraphics.pdfPolygon
  DriverPathPdf          <-- UPath       -> PdfGraphics.pdfPath
  DriverDotPathPdf       <-- DotPath
  DriverTextPdf          <-- UText       -> PdfGraphics.text
  DriverImagePdf         <-- UImage      -> PdfGraphics.pdfImage
  DriverCenteredCharacterPdf <-- UCenteredCharacter
StringBounderOpenPdf     <-- font metrics (returned by FileFormat.PDF)
PdfOption                <-- scale, title/author/subject, background
```

`UImageSvg` and `UImageTikz` are explicitly `ignoreShape`d in v1 — see the omissions section below.

## Files

| File                          | Role                                                                                |
|-------------------------------|-------------------------------------------------------------------------------------|
| `PdfGraphics.java`            | Low-level PDF emitter; mirrors `SvgGraphics` method-by-method                       |
| `PdfOption.java`              | Configuration (scale, title, author, subject, background, color mapper, min dims)   |
| `UGraphicPdf.java`            | `UGraphic` implementation, registers the per-shape drivers                          |
| `Driver*Pdf.java`             | One driver per `UShape` subclass; translates PlantUML shapes into `PdfGraphics` calls |
| `StringBounderOpenPdf.java`   | Font metrics via OpenPDF's `BaseFont`; returned by `FileFormat.PDF`                 |
| `package-info.java`           | Package declaration                                                                 |

## Coordinate system

PlantUML and SVG use Y-down (origin top-left). PDF natively uses Y-up (origin bottom-left). Rather than fighting PDF with a CTM flip and then re-flipping text and images locally, we keep PDF's native Y-up and simply **negate Y on input**.

Every caller-supplied SVG coord `(x, y)` is drawn at PDF coord `(x, -y)`. For shapes that take a top-left corner plus a size (rectangles, raster images), the PDF lower-left corner is `(x, -(y + height))`. For point-like primitives (line endpoints, polygon vertices, ellipse center, path segments, text baseline) it's just `(x, -y)`.

### From negative-Y back to a normal page

The trick is what we do with that negative-Y content. We can't tell OpenPDF "please use a page with a negative-Y MediaBox" and have every viewer render it nicely; in practice that route gives a blank page. So we buffer the drawing into a **`PdfTemplate`** (a PDF form XObject) which can hold negative-Y content as long as its bounding box covers it.

Then at `createPdf()` time we finally know the extent `(maxX, maxY)`:

```java
template.setBoundingBox(new Rectangle(0f, -maxY, maxX, 0f));
document.setPageSize(new Rectangle(maxX, maxY));
document.newPage();
writer.getDirectContent().addTemplate(template, 0, maxY);  // translate up
```

The template is placed translated by `(0, maxY)`, so its negative-Y content shifts into `[0, 0, maxX, maxY]` — a perfectly standard page that every viewer renders the right way up.

A gotcha worth knowing: the template has a *bounding box* and content outside it is clipped by the viewer. We initialize it generously (`[0, 10000] x [-10000, 0]`) at open time, then tighten it to the actual content bounds at `createPdf`.

### Why this is nicer than the CTM-flip approach

The more common tutorial recipe is to apply a single `concatCTM(1, 0, 0, -1, 0, H)` so SVG coords flow through unchanged. The problem: it inverts the Y axis for *everything*, including text. Glyphs then render upside-down, and every `showText` call has to undo the flip with `setTextMatrix(1, 0, 0, -1, x, y)`. Same trap for images, which need a per-call `saveState / concatCTM / restoreState` cell flip.

With Y negation, the default text matrix and the default image placement just work. Text and images stay upright with the simplest possible API call — `cb.setTextMatrix(x, -y)` and `img.setAbsolutePosition(x, -(y+h))`. Less ceremony, fewer places for a future contributor to introduce an upside-down glyph.

### The one-line helper

All Y conversions in `PdfGraphics` go through a single private helper:

```java
private static float ny(double svgY) {
    return (float) -svgY;
}
```

Read as "negate y". If anything ever renders upside-down or misplaced in a generated PDF, the *one* place to look is whether a coordinate took the `ny()` route or leaked through as a positive PDF coord.

## Path rendering

`UPath` segments map directly to PDF path operators except in two cases:

- **Quadratic Bezier (`SEG_QUADTO`)**: PDF has no native quadratic operator. We convert on the fly to a cubic using the standard formula `C1 = P0 + 2/3*(P1-P0)`, `C2 = P2 + 2/3*(P1-P2)`. This requires tracking the current point, which `PdfContentByte` does not do for us — hence the `curX / curY` bookkeeping in `pdfPath`.

- **Elliptical arc (`SEG_ARCTO`)**: PDF has no arc operator. We implement the SVG 1.1 Appendix F.6 endpoint-to-center parametrization in `arcToCubics`, splitting the sweep into &le; 90 degree slices and approximating each slice with one cubic Bezier (control distance `(4/3) * tan(slice/4)`). The conversion handles non-zero `xAxisRotation`, the `largeArc` / `sweep` flags, and the degenerate cases (coincident endpoints, zero radius) per the SVG spec.

## Fonts and string metrics

In v1 we use the 14 standard PDF fonts (Helvetica, Times-Roman, Courier, plus bold/italic variants). They are part of the PDF spec — the reader is required to have them, so we do not embed anything and the file stays small.

Family resolution (`PdfGraphics.resolveFont`, also called by `StringBounderOpenPdf`) is fuzzy: any string containing `courier` or `mono` maps to Courier, anything with `times` or `serif` maps to Times, and the rest defaults to Helvetica. Bold/italic combinations pick the right variant. The cache is static and concurrent so `StringBounder` and `PdfGraphics` share the same `BaseFont` instances.

### A note on `StringBounderOpenPdf` height calculation

For the width, `BaseFont.getWidthPoint(text, fontSize)` gives the exact per-glyph width — that's what we want.

For the height, do **not** use `getAscentPoint(text, ...)` / `getDescentPoint(text, ...)`: those return the bounding box of the glyphs actually present in `text`. For a string like "Alice" (no descenders, no tall accents) the result is the height of just those glyphs, which is much smaller than what AWT's `FontMetrics.getStringBounds()` reports for the same text. PlantUML's layout assumes the AWT-style height (ascent + descent + leading of the whole font, constant for a given font/size).

The correct PDF equivalent is the font descriptor:

```java
final float fontAscent  = baseFont.getFontDescriptor(BaseFont.ASCENT, fontSize);
final float fontDescent = baseFont.getFontDescriptor(BaseFont.DESCENT, fontSize);  // negative
final float leading     = 0.15f * fontSize;  // typographic default
final float height      = fontAscent - fontDescent + leading;
```

This matches AWT to within a few hundredths of a point for Helvetica/Times/Courier.

To embed a custom TrueType font, use the escape hatch:

```java
BaseFont roboto = BaseFont.createFont(
    "/path/to/Roboto-Regular.ttf",
    BaseFont.IDENTITY_H,
    BaseFont.EMBEDDED);
// Then drive PdfGraphics.getRawContent() directly for the text run.
```

A proper font registry API will land if/when we need it for `UFontFace` system-resolved fonts.

## Links

PDF link annotations are flat rectangles attached to the page — they cannot be nested. `openLink/closeLink` track the bounding box of everything drawn between the two calls (callers must call `ensureVisible`, which the `UGraphic` drivers do automatically) and emit one `PdfAnnotation` covering that union. Nested calls produce sibling rectangles rather than truly nested clickable regions.

Note: `UGraphicPdf.startUrl / closeUrl` are currently no-ops — wiring them through to `PdfGraphics.openLink / closeLink` is a small but separate piece of work.

## What it does NOT do (yet)

- **Drop shadows.** Out of scope for v1 by design.
- **SVG-in-PDF.** Stdlib icons (`UImageSvg`) are ignored. PDF does not consume SVG; would require either a Batik dependency or a custom replay path.
- **Gradients.** Solid fill only; `HColorLinearGradient` / `HColorGradient` branches in the drivers are commented out, falling back to solid color.
- **Text background filters.** SVG's `filter` attribute for text background is not reproduced.
- **Underline / strike / wave text decorations.** The corresponding code is commented in `DriverTextPdf` and not yet ported.
- **Rotated text.** Easy to add via `setTextMatrix` with rotation; not wired into the public API yet.
- **Hyperlinks via `UGraphicPdf`.** The plumbing is in place in `PdfGraphics` but `startUrl / closeUrl` are stubs.
- **Groups, comments, metadata.** `startGroup / closeGroup / drawComment` are stubs; PDF marked-content tags are the right primitive but unused so far.
- **CSS classes, interactive scripts, dark mode.** Out of scope.
- **Not compatible with TeaVM.** OpenPDF pulls in `java.awt`, `java.util.zip`, etc. The PDF backend is server-side only — hence the `// ::comment when JAVA8` / `// ::done` and `// ::remove folder when JAVA8` markers around the integration points.


## Link

- OpenPDF on GitHub: <https://github.com/LibrePDF/OpenPDF>
- OpenPDF Maven Central: <https://central.sonatype.com/artifact/com.github.librepdf/openpdf>
- Reference SVG backend (we mirror its `SvgGraphics`): `src/main/java/net/sourceforge/plantuml/klimt/drawing/svg/SvgGraphics.java`
- Reference `UGraphic` implementation (we mirror its driver layout): `src/main/java/net/sourceforge/plantuml/klimt/drawing/svg/UGraphicSvg.java`

## Reference

- PDF 1.7 specification (ISO 32000-1). All operators we emit (`m`, `l`, `c`, `re`, `S`, `f`, `B`, `Tj`, ...) are standard.
- SVG 1.1 Specification, Appendix F.6 — elliptical arc implementation notes (used in `arcToCubics`).

## Credit

- [LibrePDF/OpenPDF](https://github.com/LibrePDF/OpenPDF) — the underlying PDF library (LGPL 2.1 / MPL 2.0, both compatible with PlantUML's GPL).

## Misc.

The `// ::comment when JAVA8` / `// ::done` markers in `FileFormat.java` exclude this backend from the Java 8 / TeaVM build paths, since OpenPDF requires Java 11+ and `java.awt`. The `// ::remove folder when JAVA8` marker at the top of `PdfGraphics` removes the whole `openpdf` package from TeaVM-targeted builds via the SJPP preprocessor.
