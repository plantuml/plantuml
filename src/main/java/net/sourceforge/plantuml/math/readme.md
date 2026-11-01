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
- [`UGraphicTeXGraphics.java`](./UGraphicTeXGraphics.java) is the canvas of JLaTeXMath drawn on a `UGraphic` (glyphs and lines as paths, no AWT font, no Batik). Not used by `AtomMath` yet (`PLAN.md`, step 2c).
- `org.apache.batik` (optional, SVG output only, until `PLAN.md` phase 2 replaces it)
