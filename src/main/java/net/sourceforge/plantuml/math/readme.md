# Directory Documentation for `math`

## Description
This package provides classes used to manage [PlantUML ASCII Math](https://plantuml.com/ascii-math) format.

## Link
- [PlantUML ASCII Math Format](https://plantuml.com/ascii-math)

## Reference
- [Ascii Math](http://asciimath.org)
- [JLaTeXMath](https://scilab.gitlab.io/legacy_wiki/JLaTeXMath)

## Credit
- :octocat: [Asciimath/Asciimathml](https://github.com/asciimath/asciimathml/tree/master/asciimath-based)
- [`ASCIIMathTeXImg.java`](./ASCIIMathTeXImg.java) is an internal port of [`ASCIIMathTeXImg.js`](https://github.com/asciimath/asciimathml/blob/master/asciimath-based/ASCIIMathTeXImg.js)
- :octocat: [Opencollab/Jlatexmath](https://github.com/opencollab/jlatexmath)

## Dependency _(if available)_
- `org.scilab.forge.jlatexmath` is part of PlantUML (vendored, see [its readme](../../../../org/scilab/forge/jlatexmath/readme.md)), except in the MIT, EPL, BSD, ASL and LGPL variants
- [`UGraphicTeXGraphics.java`](./UGraphicTeXGraphics.java) is the canvas of JLaTeXMath drawn on a `UGraphic`: glyphs and lines are paths, there is no AWT font and no Batik. A formula is a [`MathDrawing`](./MathDrawing.java) (built by `TeXIconBuilder`), drawn by `AtomMath` or exported (PNG, SVG, EPS, PDF) by `ScientificEquationSafe` through `TextBlockExporter`, so every output gets the same shapes.
- [`StringBounderTextMeasurer.java`](./StringBounderTextMeasurer.java) measures, with the `StringBounder` of the diagram, the text that no TeX font can draw (CJK...); `UGraphicTeXGraphics.drawText` draws it with the same font.
- [`MathResources.java`](./MathResources.java) tells JLaTeXMath where to read its resources: the files embedded in the executable in the C build ([`JlatexmathResources`](../teavm/c/JlatexmathResources.java)), the class loaders on the JVM. `TeXIconBuilder` calls it before the first formula.
- No `org.apache.batik` any more (`PLAN.md`, step 2c). When JLaTeXMath is not in the variant (MIT, EPL, BSD, ASL, LGPL) or fails, the source of the formula is shown as text.
