# PLAN: JLaTeXMath inside PlantUML (branch `master-jlatexmath`)

Status: **phases 0 and 1 are done, phase 2 is in progress** (see the table of
section 6 and the steps below it). This file is the hand-over for the next
working sessions. Update it as decisions are made (move items from
"Open questions" to "Decisions").

## 1. Goal

Make math and LaTeX work in the TeaVM builds (C first, then JavaScript / Wasm)
**and in regular Java without the need of an external .jar file** (no
JLaTeXMath jar, no Batik jar: the code is part of PlantUML).

Today the TeaVM builds cannot render them, and the JVM needs the optional
JLaTeXMath jar (and Batik for the SVG output):

| Feature                                   | JVM (with the jars) | TeaVM C build today                                     |
|-------------------------------------------|-----|---------------------------------------------------------|
| `@startmath` (AsciiMath)                  | ok  | "Diagram not supported by this release of PlantUML"    |
| `@startlatex`                             | ok  | same                                                    |
| `<math>...</math>`, `<latex>...</latex>` in labels (Creole) | ok | shown as plain text                     |

Out of scope for now: `\includegraphics`, GeoGebra/Fcscore atoms, anything
that needs a real window system.

Success criteria:

1. The same formulas render in the JVM jar and in the C executable, with the
   same layout, in SVG, PNG and PDF (and the TXT/TIKZ paths that exist today).
2. The regular jar renders math and LaTeX on its own: the `jlatexmath`
   dependency disappears from `build.gradle.kts`, `gradle/libs.versions.toml`
   and the license variants, and Batik is no longer needed for the SVG.
3. No reflection and no dependency on `java.awt`, `javax.swing`, `javax.imageio`,
   `javax.xml` or Batik in the code used by the TeaVM builds.
4. Output is deterministic across platforms (needed by the Vega tests), as
   already done for PDF.
5. The license variants (MIT, EPL, BSD, ASL, LGPL) keep building, see 7.

## 2. How PlantUML used JLaTeXMath (before phases 1 and 2)

Historical: this is the starting point. Phase 1 removed the reflection, phase 2c
removed `ConverterSvg`, `LatexBuilder.LatexImage` and the `getSvg` / `getImage`
of `ScientificEquation`: a formula is now a `MathDrawing` drawn on a `UGraphic`.

Everything goes through **reflection**, so that the jar works without the
library (`implementation(libs.jlatexmath)`, version `1.0.7`; Batik is
optional too).

- `net.sourceforge.plantuml.math`
  - `TeXIconBuilder`, `LatexBuilder`: build a `TeXIcon` by reflection
    (`TeXFormula`, `TeXIconBuilder`, style 0, size 20, insets, foreground).
  - `ConverterSvg`: SVG output through Batik (`SVGGraphics2D`, font as shapes),
    and registration of the Cyrillic and Greek alphabets.
  - `ScientificEquation`, `ScientificEquationSafe`: facade used by the rest.
  - `AsciiMath`, `ASCIIMathTeXImg` (1,000 lines): AsciiMath to TeX, pure Java
    (imports only `java.util`), an internal port of `ASCIIMathTeXImg.js`.
  - `PSystemMath`, `PSystemLatex` and their factories.
- `klimt/creole/atom/AtomMath` (drawing, `matchesProperty("TIKZ")`, SVG vs image)
  and `klimt/creole/legacy/StripeLatex`.
- Gates that keep math out of the TeaVM builds:
  `CreoleParser` (`TeaVM.isTeaVM() == false && Parser.isLatexStart(line)`),
  `CommandCreoleBuilder`, `StripeSimple`, `AtomMath.drawU` (`!TeaVM.isTeaVM()`),
  and the missing `PSystemMathFactory` / `PSystemLatexFactory` in
  `teavm/c/PSystemBuilderC` (and in the JS builder).
- No Vega test exists for math or latex today (nothing under
  `src/test/resources/vega` uses `@startmath`, `@startlatex`, `<math>`).

## 3. What JLaTeXMath is (measured on https://github.com/opencollab/jlatexmath)

- Core module: **156 Java files, about 24,000 lines**.
- Resources: **1.1 MB** = 30 TTF fonts, 44 XML files (font metrics, symbols,
  macros, glue settings), 3 text files. Extra modules: Cyrillic and Greek fonts.
- License: GPL v2 **or later**, with the GNU linking exception (see the header
  of each file). Compatible with PlantUML's GPL v3+, but it matters for the
  non-GPL variants (see 7).
- Clean split between **layout** (parser, atoms, boxes, font metrics: pure
  Java) and **drawing** (`Box.draw(Graphics2D g2, float x, float y)`).
  - Glyphs are drawn with `g2.drawChars(...)` using an AWT `Font` taken from
    `FontInfo`; **metrics do not come from AWT**, they come from the XML
    descriptions. So the glyph shapes are the only thing we need from the TTF.
  - `Graphics2D` appears in about 20 box classes; `java.awt` or `javax.swing`
    is imported in 34 files (`Graphics2D`, `Color`, `Font`, `AffineTransform`,
    `BufferedImage`, `Stroke`/`BasicStroke`, `Rectangle2D`, `Insets`, `Icon`).

Platform dependencies to remove or replace:

| Area | Where | Plan |
|------|-------|------|
| Drawing | `Box.draw(Graphics2D...)` in ~20 box classes, `TeXIcon` | new tiny canvas interface, see 4 |
| XML | `javax.xml.parsers.DocumentBuilderFactory` in `DefaultTeXFontParser`, `GlueSettingsParser`, `PredefinedTeXFormulaParser` (and the `TeXFormulaSettingsParser`, `TeXSymbolParser` readers) | check what TeaVM supports; else pre-convert or use a mini XML reader (5.3) |
| Resources | about 15 `getResourceAsStream` calls | resource abstraction, `EmbeddedFiles` in the C build (5.4) |
| Fonts | `Font.createFont`, `GraphicsEnvironment`, `FontInfo.getFont` | replaced by pre-extracted glyph outlines (5.2) |
| Images | `GraphicsAtom` (`Toolkit`, `BufferedImage`), `TeXFormula` (`ImageIO`, `BufferedImage`), `cache/JLaTeXMathCache` | drop (not needed) |
| Java font rendering | `JavaFontRenderingAtom/Box` (`TextLayout`) | drop |
| Reflection | `Class.forName` for the alphabet registrations in `TeXFormula`; `WebStartAlphabetRegistration`, `URLAlphabetRegistration` | direct calls, drop the others |
| Toolkit | `Toolkit.getDefaultToolkit().getScreenResolution()` in `TeXFormula` | constant |
| Misc atoms | `GeoGebraLogo*`, `Fcscore*`, `Longdiv`, ... | drop what PlantUML does not need (pruning pass, 5.6) |

## 4. Design

Keep the parser and the layout, replace the drawing.

1. Vendor the needed sources (pruned in the end, like OpenPDF) in this
   repository, in the Java 8 compatible style of the project (see `CLAUDE.md`:
   tabs, `== false`, explicit imports, no `var`).
2. Introduce a minimal canvas that `Box.draw` talks to instead of
   `Graphics2D`: fill rectangle, draw line (with thickness), draw glyph
   `(fontId, char, size)`, translate / scale / rotate (needed by `ScaleBox`,
   `RotateBox`, `ReflectBox`), set color. Nothing else.
3. Implement that canvas on top of PlantUML's own `UGraphic` (glyph = `UPath`),
   so SVG, PNG, PDF, EMF... all come for free, in every build, and Batik is no
   longer needed.
4. Replace the reflection layer in `net.sourceforge.plantuml.math` by direct
   calls (`ScientificEquation` keeps its API, so `AtomMath`, `PSystemMath` and
   `PSystemLatex` change little).

## 5. Work items

### 5.1 Baseline and corpus (do this first)

- Build a corpus of formulas (fractions, roots, sums and integrals with limits,
  matrices, `cases`, accents, `\text`, colors, Greek and Cyrillic, nested
  delimiters, `<math>` in a class label, in a note, in a sequence message) as
  Vega tests (`src/test/resources/vega/...`).
- Generate the reference outputs **with the current implementation** on the
  JVM (JLaTeXMath 1.0.7 + Batik). They are the target to compare with, not
  necessarily byte for byte (see open questions about text vs shapes).
- Status: done. The PNG baselines of the original renderer are in
  `src/test/resources/jlatexmath-corpus/baseline-jvm-png/` (see its
  `README.md`). The SVG references could not be made with the original
  renderer (Batik is not a dependency of the build): since step 2c they are the
  output of the new renderer, in the Vega tests `src/test/resources/vega/math/`
  (19 files).

### 5.2 Glyph outlines

Status: done. The tool and the three `glyphs.bin` files (step 2b-1), the reader
`GlyphOutlines` and the canvas `UGraphicTeXGraphics` (step 2b-2).

- Dev-time tool: read the 30 TTF files and extract, for each character that the
  XML metrics declare, its outline (move/line/quadratic/cubic) into a compact
  resource (binary or generated Java tables). Commit the generated file and the
  tool, like `OpenIconic.main()` does for `openiconic.js`.
- Measure the size of the result (the target is well under the 1.1 MB of today;
  to be measured, the subset of declared glyphs is small).
- At run time a glyph is a `UPath` scaled by `size / FONT_SCALE_FACTOR`:
  identical on JVM, JS, Wasm and C, hence deterministic.

### 5.3 XML

- Check whether the TeaVM C and JS class libraries support
  `javax.xml.parsers` (a 20-line repro is enough). Look also at how
  `SvgSpriteParserFactory` and the other existing readers of the repository
  cope with XML.
- If not supported: either convert the 44 XML files at dev time to a compact
  format that is trivial to read, or write a minimal reader that handles only
  what these files use (elements, attributes, no entities, no namespaces).

### 5.4 Resources

- One small abstraction instead of `getResourceAsStream`:
  JVM = class path, C = `EmbeddedFiles.read(...)` (add the files to
  `copyNativeSources` in `build.gradle.kts` and to `src/teavm-c/CMakeLists.txt`,
  as was done for `openiconic/all.txt`), JS = lazy-loaded bundle like
  `themes.js`, `emoji.js`, `openiconic.js`.
- Initialization must be lazy: nothing is parsed unless a diagram uses math
  (start-up time of the executable and of the browser build).

### 5.5 Wire-up

- Register `PSystemMathFactory` and `PSystemLatexFactory` in `PSystemBuilderC`
  (and `PSystemBuilderJs`).
- Remove the `TeaVM.isTeaVM()` gates listed in section 2.
- Make `AtomMath` draw through the new canvas instead of `UImage` / `UImageSvg`.

### 5.6 Pruning pass

Same method as for OpenPDF: call graph from the entry points actually used by
PlantUML, remove unreachable classes and methods (clide `list_could_be_private`
helps), remove the unused macros and symbols once the corpus passes. Keep a
list of what was removed so that it can be restored.

### 5.7 Tests, docs, CI

Status: `src/test/resources/vega/math` has about 220 Vega tests: AsciiMath
(`asciimath/`), LaTeX by theme (`latex/`: fractions, roots, scripts, big
operators, delimiters, matrices, accents, fonts, spacing, colors, boxes,
transforms, relations, arrows, symbols, functions, text, unicode, macros,
formulas of physics...), formulas in the diagrams (`inline/`: class, note,
sequence, usecase, activity, state, component, mindmap, WBS, Gantt, salt, JSON,
timing, ER, tables, legend, colors, styles, skinparams `monochrome`,
`handwritten`, `dpi`, `scale`), the options of `@startmath` / `@startlatex`
(`options/`), the formulas that cannot be made (`errors/`), CJK and other
scripts (`cjk/`), and the other outputs (`formats/`: PDF, TikZ, text). The unit
tests are in `src/test/java/org/scilab/forge/jlatexmath` (`GlyphOutlinesTest`,
`ForeignTextTest`). The SVG references are made with `decimal: 1` (0.1 pixel):
a third smaller than with 3 decimals, and enough to see a change of a shape or
of the layout. Vega on the C build is still to do.

- Vega tests from 5.1 on the JVM, and on the C build through
  `tools/vega-c/run_vega_c.py` (once the Vega headers are there).
- Update `net/sourceforge/plantuml/math/readme.md`, `CHANGES.md`, and the
  README of the C build.

## 6. Suggested phases

| Phase | Content | Done when |
|-------|---------|-----------|
| 0 | 5.1 corpus + references; answer the cheap open questions (XML in TeaVM, pinned upstream commit) | corpus committed, references reviewed (**done**) |
| 1 | Vendor the sources as they are, call them directly (no reflection), JVM only, **no behaviour change** | corpus unchanged on the JVM (**done**) |
| 2 | Canvas interface + `UGraphic` implementation, glyph outlines (5.2) | corpus renders without AWT fonts, differences understood (**in progress**, steps below) |
| 3 | Remove `java.awt` / `javax.*` leftovers, XML and resources (5.3, 5.4) | the code compiles with TeaVM (JS and C) |
| 4 | Wire-up (5.5), first formulas in `plantuml.exe` | `@startmath`, `@startlatex`, `<math>` work in the C build |
| 5 | Pruning (5.6), size and start-up measurements, Vega on C (5.7) | size delta acceptable, CI green |
| 6 | Same for JavaScript / Wasm if wanted | |

Steps of phase 2:

| Step | Content | State |
|------|---------|-------|
| 2a | `Box.draw` talks to `TeXGraphics` instead of `Graphics2D` (`Graphics2DTeXGraphics` keeps the AWT rendering) | done, corpus identical byte for byte |
| 2b-1 | `tools/jlatexmath-glyphs/GenerateGlyphOutlines.java` writes `glyphs.bin` (46 fonts, 4710 glyphs) | done, at most 0.8% area difference with AWT |
| 2b-2 | `GlyphOutlines` (reader of `glyphs.bin`, no AWT) and `UGraphicTeXGraphics` (`TeXGraphics` on `UGraphic`, glyph = path) | done, not used yet by `AtomMath` |
| 2c | `AtomMath` / `ScientificEquation*` draw through the canvas (PNG, SVG, PDF, EPS...), Batik and `ConverterSvg` go away, the corpus moves to Vega | done |
| 2d | Review of the corpus rendering (hinting, seams of extensible arrows), decision on the open questions 4, 7 and 8 | done: seams fixed, no hinting, see Decisions |

Steps of phase 3:

| Step | Content | State |
|------|---------|-------|
| 3a | Remove what PlantUML does not use (images, Java fonts, GeoGebra, Fcscore, URL alphabets, user XML formulas), the AWT canvas, `Icon`, `java.awt.Font`; the TTF files leave the jar | done |
| 3a+ | Text without a TeX glyph (CJK...) drawn by PlantUML; the fallback shows the source as it is; about 200 Vega tests (`vega/math`) and the unit tests of the package | done |
| 3b | `Color`, `Insets` and `Point2D` replaced by small classes of the package (`TeXGraphics` and `TeXIcon` without `java.awt`) | done: `TeXColor`, `TeXInsets` |
| 3c | No reflection (`MacroInfo` / `\newcommand`, the alphabets of the static block of `TeXFormula`), no `Character.UnicodeBlock` (to check with TeaVM) | |
| 3d | XML: a mini reader (5 parsers) or binary files made at dev time | |
| 3e | Resources: one abstraction (class path, `EmbeddedFiles`, bundle), also for `glyphs.bin` | |

Notes on phase 1: the vendored classes live in the jar, so the external
jar is no longer read by reflection and its Gradle dependency is removed in
the same phase. The SVG output went through Batik (reflection, optional)
until step 2c of phase 2 replaced it: that was the only external jar left
after phase 1.

Each phase should end with a patch that builds on the JVM (Java 8 target via
`sjpp` with `JAVA8`, Java 11 for the jar) and with the C build.

## 7. Licenses and variants

- The upstream header is GPL v2 or later plus the linking exception. Keep every
  header and add the attribution (upstream URL and commit) in this directory.
- The MIT, EPL, BSD, ASL and LGPL variants must not contain this code: guard it
  with the same `// ::comment when __MIT__ __EPL__ ...` / `// ::done` markers as
  ditaa and jcckit, and check `LICENSES.md` and the variant `build.gradle.kts`
  files (the EPL one currently has `implementation(libs.jlatexmath)`).
- Add the new package to the "external code, do not modify" list of `CLAUDE.md`
  if we want to keep it close to upstream, or to the normal list if we prune it
  freely (decision below).

## 8. Open questions / decisions

### Decisions

- **Upstream version (was question 5): vendor tag `1.0.7`**, commit
  `cb86da9c5747ba1788fcb913988b98d82cb0bc0d` of
  https://github.com/opencollab/jlatexmath (2018-04-18). It is the version the
  jar uses today, so phase 1 is "no behaviour change". The 9 upstream commits
  after the tag only touch the build (Maven, Travis, javadoc), the changelog
  and the LICENSE files of the font modules: nothing to take for the code.
- **XML in TeaVM (was question 6): not available.** `javax.xml.parsers` is not
  provided by TeaVM; the repository already works around it in
  `SvgSpriteParserFactory` (compile-time `TeaVM.isTeaVM()` forces
  `SvgNanoParser`). Consequence for 5.3: no point in a repro, the 44 XML
  files of JLaTeXMath must be read either by a minimal reader of our own (the
  files use only elements and attributes) or converted at dev time. To be
  decided in phase 3, after looking at what `SvgNanoParser` can be reused.

- **Package name (was question 1): keep `org.scilab.forge.jlatexmath`**, for
  now. It stays easy to diff against upstream.
- **One code path (was question 2): the new renderer everywhere**, on the JVM
  too: deterministic, no Batik. The SVG of a formula changes from the source
  text (without Batik) or Batik shapes to our own paths. The original library
  is not kept next to it.
- **Glyphs in SVG (was question 3): follows from the previous one.** The
  renderer draws through `UGraphic`, so a glyph is a path (self-contained SVG,
  larger than with `<text>`). Revisit only if the size of the SVG hurts.

- **Hinting (step 2d): none.** AWT snaps the thin strokes on the pixel grid
  (one crisp row for the shaft of an arrow), the paths give two grey rows in
  PNG. The output is vector and deterministic, so there is no grid to hint for;
  the PNG is a little softer. Revisit only if someone complains.
- **Seams of the extensible arrows and braces (step 2d): fixed** by drawing the
  glyphs that follow each other with the same color as one path (the
  rasterizer fills the union), see the readme of the package. A side effect:
  the SVG of a formula is one `<path>` per color, 3% smaller.
- **Alphabets (was question 4): keep both for now.** Same behaviour as the jar
  of today, and they are read only when a formula uses one of their characters
  (lazy). Measured (`glyphs.bin`, raw / gzip): base 419 KB / 252 KB (2348
  glyphs), Cyrillic 264 KB / 151 KB (760), Greek 427 KB / 107 KB (1602, the
  polytonic Greek of the 8 fonts); together 1.1 MB / 0.5 MB. The real decision
  is for phase 5, with the size of the C executable: the lever is then to keep
  the two files out of the embedded resources (they are optional files, like
  the other lazy bundles), not to cut glyphs. The TTF files (46 files, 1.2 MB)
  left the jar at step 3a: they are in `tools/jlatexmath-glyphs/ttf`.
- **TikZ / LaTeX output (was question 7): kept, and tested.** `AtomMath` gives
  the source of the formula, as before (same `.tex` before and after step 2c);
  `vega/tikz/tikz-math-inline` asserts it.
- **Static state and `synchronized` (was question 8): nothing in the way.** The
  only `synchronized` added is `GlyphOutlines.forBase` (the cache of the files);
  the repository has 114 occurrences already (`JsonObject`, `Emoji`, ...), but
  whether the C build accepts this one is to be confirmed when it compiles the
  code (phase 4: if not, drop the keyword, the C build is single threaded). The
  static maps of `FontInfo`, `DefaultTeXFont` and
  `TeXFormula` are upstream's and are not thread safe, as with the jar before:
  not a C build matter (single thread), and left as it was on the JVM.

- **Text without a TeX glyph (CJK...): drawn by PlantUML (after step 3a).** The
  parser drew any character that has no TeX symbol (a Chinese character in
  `\text{}`, for instance) with a Java font (`JavaFontRenderingAtom`). Since
  step 2c that box drew nothing, a blank gap, silently; step 3a made it a parse
  error; it is now drawn with the text engine of PlantUML (`UText`, which works
  in every build, C included). The layout needs the size of the text before
  anything is drawn, so the formula is made with a `TeXTextMeasurer` (a field of
  the `TeXEnvironment`, not a static one) that PlantUML implements with the
  `StringBounder` of the diagram. A run of such characters is one atom. Limits:
  the text is sans serif, whatever the TeX font style (`\textbf`, `\mathit`
  do not apply), it cannot be rotated or flipped, and its size is a whole number
  of pixels.
- **A formula must not change the next ones (found by a Vega run on another
  machine).** `SymbolAtom.get` gave the shared symbol of the table, and the
  macros `\limits`, `\nolimits`, `\normal` and `setUnicode` changed it: after
  one `\sum\nolimits`, every later `\sum` of the JVM had its limits as scripts
  (a server, a test suite in another order). `get` returns a copy now. The
  Vega references had been made after such a formula, so 14 of them were wrong,
  8 of which failed on the other machine. Still global, as upstream: the
  definitions of `\newcommand` and `\definecolor` stay for the next formulas;
  to change only if it matters on a server.
- **The fallback shows the source as it is.** When a formula cannot be made,
  the source is shown as text. It went through Creole, so `\text` was shown as
  a tab and "ext", `\n` as a newline, `**x**` as bold. It is now raw text.

### Open questions

1. (answered, see Decisions: keep `org.scilab.forge.jlatexmath`)
2. (answered, see Decisions: one code path, new renderer everywhere)
3. (answered, see Decisions: glyphs as paths)
4. (answered, see Decisions: keep both, decide with the size in phase 5)
5. (answered, see Decisions: vendor tag `1.0.7`)
6. (answered, see Decisions: no `javax.xml` in TeaVM)
7. (answered, see Decisions: TikZ output kept and tested)
8. (answered, see Decisions: nothing in the way)

## 9. First things to do in the next session

1. ~~Answer questions 5 and 6~~ (done, see section 8).
2. ~~Write the corpus and the baselines (phase 0)~~ (done, PNG only, see 5.1).
3. ~~Vendor the upstream sources at the chosen commit and wire direct calls
   (phase 1)~~ (done).
4. ~~Phase 2, step 2c: draw `AtomMath` through `UGraphicTeXGraphics`, move the
   corpus to Vega~~ (done).
5. ~~Phase 2, step 2d: review of the rendering~~ (done, see Decisions).
6. Phase 3 (no `java.awt` / `javax.*` left, XML, resources): steps 3a and 3b are
   done (see the table of steps in section 6); 3c is next (no reflection, no
   `Character.UnicodeBlock`), then 3d (XML) and 3e (resources). What is left of
   the platform in the package: `javax.xml` (5 parsers), the reflection of
   `MacroInfo` (`\newcommand`) and of the alphabets in `TeXFormula`,
   `Character.UnicodeBlock`, and `getResourceAsStream`.

Working method, as usual: small patches (`git format-patch`), each with an
English commit message starting with the emojis (here 🍵 for the C build plus
the usual type emoji).
