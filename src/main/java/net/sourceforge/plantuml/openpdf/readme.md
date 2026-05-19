# `openpdf` — PDF backend for PlantUML

A vector PDF backend built on top of [OpenPDF](https://github.com/LibrePDF/OpenPDF), mirroring the public surface of `net.sourceforge.plantuml.klimt.drawing.svg.SvgGraphics` so it can later be wrapped by a `UGraphicPdf` driver.

## Description

This package is an experimental, self-contained PDF renderer for PlantUML. It does not depend on Batik or FOP. It produces native vector PDF — text stays selectable, geometry stays scalable, file size stays small.

The entry point is `PdfGraphics`, which exposes drawing primitives (rectangles, ellipses, polygons, lines, paths, text, raster images, hyperlinks) and accumulates a bounding box as you draw. When you call `createPdf(OutputStream)`, the page is cropped to the actual extent of what was drawn, exactly as `SvgGraphics` does for the SVG viewBox.

Three things deliberately left out in this first iteration:

- **drop shadows** — would require either soft masks or a darker offset duplicate; not core to the diagram's information content,
- **inlined SVG images** (stdlib icons) — PDF does not consume SVG; would require either a Batik dependency or a custom replay path,
- **gradients** — `PdfShading` supports them but the API is verbose; a flat fill is used instead in v1.

These can be added incrementally without changing the existing API.

## Quick start

```java
import java.awt.Color;
import java.io.FileOutputStream;
import net.sourceforge.plantuml.openpdf.*;

PdfGraphics g = new PdfGraphics(new PdfOption()
        .withTitle("My diagram")
        .withAuthor("PlantUML"));

g.setFillColor(new Color(220, 230, 250));
g.setStrokeColor(Color.BLACK);
g.setStrokeWidth(1, null);
g.pdfRectangle(20, 20, 200, 100, 8, 8);

g.setFillColor(Color.BLACK);
g.text("Hello, PDF!", 40, 70, "Helvetica", 18, true, false, 0);

try (FileOutputStream out = new FileOutputStream("hello.pdf")) {
    g.createPdf(out);
}
```

## API surface

| Concept              | Method                                                                       |
|----------------------|------------------------------------------------------------------------------|
| State — fill         | `setFillColor(Color)`                                                        |
| State — stroke       | `setStrokeColor(Color)` / `setStrokeWidth(double, double[] dash)`            |
| State — visibility   | `setHidden(boolean)`                                                         |
| Rectangle            | `pdfRectangle(x, y, w, h, rx, ry)`                                           |
| Line                 | `pdfLine(x1, y1, x2, y2)`                                                    |
| Ellipse              | `pdfEllipse(cx, cy, rx, ry)`                                                 |
| Polygon              | `pdfPolygon(double... points)`                                               |
| Path — open          | `newpath()`                                                                  |
| Path — segments      | `moveto / lineto / curveto / quadto / closepath`                             |
| Path — close & paint | `fill(windingRule)`                                                          |
| Path — replay AWT    | `drawPathIterator(x, y, PathIterator)`                                       |
| Text                 | `text(text, x, y, family, size, bold, italic, textLength)`                   |
| Raster image         | `pdfImage(bytes, x, y, w, h)` (PNG, JPEG)                                    |
| Hyperlink            | `openLink(url, title)` / `closeLink()`                                       |
| Output               | `createPdf(OutputStream)`                                                    |
| Escape hatch         | `getRawContent()` / `getRawWriter()`                                         |

## Mapping with `SvgGraphics`

The class is intentionally close to `SvgGraphics` so a future `UGraphicPdf` can drive both backends through the same `UGraphic` driver classes.

| `SvgGraphics`                       | `PdfGraphics`                       |
|-------------------------------------|-------------------------------------|
| `svgRectangle(x, y, w, h, rx, ry, deltaShadow)` | `pdfRectangle(x, y, w, h, rx, ry)`    |
| `svgLine(x1, y1, x2, y2, deltaShadow)`          | `pdfLine(x1, y1, x2, y2)`             |
| `svgEllipse(x, y, rx, ry, deltaShadow)`         | `pdfEllipse(cx, cy, rx, ry)`          |
| `svgPolygon(deltaShadow, points...)`            | `pdfPolygon(points...)`               |
| `svgPath(x, y, UPath, deltaShadow)`             | translate UPath → `newpath`/segments/`fill` |
| `text(..., textLength, attributes, ...)`        | `text(..., textLength)`               |
| `openLink / closeLink`                          | same names                            |
| `createXml(OutputStream)`                       | `createPdf(OutputStream)`             |

The shadow parameter is silently dropped (per v1 scope).

## Coordinate system

PlantUML and SVG use Y-down (origin top-left). PDF natively uses Y-up (origin bottom-left). To keep callers writing SVG-style coordinates, `PdfGraphics` applies a single CTM flip at startup:

```
1   0   0
0  -1   H
0   0   1
```

This works transparently for all shape primitives. The one wrinkle is text — glyphs drawn with the default text matrix would render upside-down. We compensate locally inside `text(...)`:

```java
cb.setTextMatrix(1, 0, 0, -1, x, y);
```

which flips the text frame back. Net effect: every caller-visible coordinate is in PlantUML/SVG conventions.

## Fonts

In v1 we use the 14 standard PDF fonts (Helvetica, Times-Roman, Courier, plus bold/italic variants). They are part of the PDF spec — the reader is required to have them, so we do not embed anything and the file stays small.

Family resolution is fuzzy: any string containing `courier` or `mono` maps to Courier, anything with `times` or `serif` maps to Times, and the rest defaults to Helvetica. This matches what real-world PlantUML diagrams ask for in 95% of cases.

To embed a custom TrueType font, use the escape hatch:

```java
BaseFont roboto = BaseFont.createFont(
    "/path/to/Roboto-Regular.ttf",
    BaseFont.IDENTITY_H,
    BaseFont.EMBEDDED);
// Then drive g.getRawContent() directly for the text run.
```

A proper font registry API will land if/when we need it for `UFontFace`.

## Links

PDF link annotations are flat rectangles attached to the page — they cannot be nested. `openLink/closeLink` track the bounding box of everything drawn between the two calls and emit one `PdfAnnotation` covering that union. Multiple successive links work fine; truly nested links produce sibling rectangles rather than nested clickable regions.

## What it does NOT do (yet)

- No drop shadows.
- No SVG-in-PDF (stdlib icons appear missing).
- No gradients (solid fill only).
- No text background filters (the SVG `filter` attribute for text background is silently ignored).
- No rotated text — easy to add via `setTextMatrix` with rotation, but not wired into the public API yet.
- No CSS classes / interactive scripts.
- Not compatible with TeaVM (OpenPDF pulls in `java.awt`, `java.util.zip`, etc.). The PDF backend is server-side only.

## Dependencies

Add to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.github.librepdf:openpdf:3.0.4'
}
```

Or to a `pom.xml`:

```xml
<dependency>
    <groupId>com.github.librepdf</groupId>
    <artifactId>openpdf</artifactId>
    <version>3.0.4</version>
</dependency>
```

The `openpdf` jar is about 2 MB. No transitive runtime dependencies are required for the features used here (ICU4J, Bouncy Castle, FOP are all marked `optional` in OpenPDF's POM).

## Examples

The `examples` subpackage contains five runnable mains. Each produces a `.pdf` file in the current directory.

| File                       | Output                | What it shows                                       |
|----------------------------|-----------------------|-----------------------------------------------------|
| `Example1Basic`            | `example1-basic.pdf`  | Rectangles, lines (solid & dashed), ellipse, polygon |
| `Example2Text`             | `example2-text.pdf`   | The 3 font families, bold/italic, length adjustment |
| `Example3Path`             | `example3-path.pdf`   | Direct path API + Java2D `Path2D` replay (star)     |
| `Example4Links`            | `example4-links.pdf`  | Three clickable boxes pointing to plantuml.com      |
| `Example5MiniDiagram`      | `example5-diagram.pdf`| A 3-box "class diagram" with inheritance arrows     |

Compile and run any of them with PowerShell on Windows (assuming OpenPDF 3.0.4 sits in `lib/`):

```powershell
javac -cp "lib\openpdf-3.0.4.jar;build\classes\java\main" `
      -d build\classes\java\main `
      src\main\java\net\sourceforge\plantuml\openpdf\PdfOption.java `
      src\main\java\net\sourceforge\plantuml\openpdf\PdfGraphics.java `
      src\main\java\net\sourceforge\plantuml\openpdf\examples\Example1Basic.java

java -cp "build\classes\java\main;lib\openpdf-3.0.4.jar" `
     net.sourceforge.plantuml.openpdf.examples.Example1Basic
```

Or, more realistically, integrate it into the Gradle build and run via:

```powershell
.\gradlew compileJava
java -cp "build\classes\java\main;<resolved openpdf jar>" `
     net.sourceforge.plantuml.openpdf.examples.Example1Basic
```

## Link

- OpenPDF on GitHub: <https://github.com/LibrePDF/OpenPDF>
- OpenPDF Maven Central: <https://central.sonatype.com/artifact/com.github.librepdf/openpdf>
- `SvgGraphics` (reference implementation we mirror): `src/main/java/net/sourceforge/plantuml/klimt/drawing/svg/SvgGraphics.java`

## Reference

- PDF 1.7 specification (ISO 32000-1) — the operators we emit (`m`, `l`, `c`, `re`, `S`, `f`, `B`, `Tj`, ...) are all standard.
- `PdfContentByte` Javadoc: <https://librepdf.github.io/OpenPDF/docs-1-2-7/com/lowagie/text/pdf/PdfContentByte.html>

## Credit

- [LibrePDF/OpenPDF](https://github.com/LibrePDF/OpenPDF) — the underlying PDF library (LGPL 2.1 / MPL 2.0).

## Misc.

OpenPDF is licensed under LGPL 2.1 / MPL 2.0, both compatible with PlantUML's GPL.

### Package namespace

We target **OpenPDF 3.0+** and import from `org.openpdf.*`. The previous `com.lowagie.*` namespace was deprecated in 2.4 and **removed entirely in 3.0** (August 2025). If you pin OpenPDF to an older version (2.3 or earlier), the imports will not resolve — bump to 3.0.4 or later.

| OpenPDF version | Maven artifact                          | Java packages                                  |
|-----------------|-----------------------------------------|------------------------------------------------|
| ≤ 2.3.x         | `com.github.librepdf:openpdf`           | `com.lowagie.*` only                           |
| 2.4.x           | `openpdf-core-modern` (or legacy artifact) | both, with `com.lowagie.*` deprecated         |
| **3.0.0+**      | `com.github.librepdf:openpdf`           | **`org.openpdf.*` only — `com.lowagie` removed** |

Most example code on the web still imports `com.lowagie.*` because the migration is recent and large dependent projects (e.g. Flying Saucer) have not caught up yet. Do not be misled — for any new code written in 2026 you want `org.openpdf.*`.
