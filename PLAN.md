# PLAN: JLaTeXMath inside PlantUML (branch `master-jlatexmath`)

Status: **draft, nothing implemented yet.** This file is the hand-over for the
next working sessions. Update it as decisions are made (move items from
"Open questions" to "Decisions").

## 1. Goal

Make math and LaTeX work in the TeaVM builds (C first, then JavaScript / Wasm),
which today cannot render them:

| Feature                                   | JVM | TeaVM C build today                                     |
|-------------------------------------------|-----|---------------------------------------------------------|
| `@startmath` (AsciiMath)                  | ok  | "Diagram not supported by this release of PlantUML"    |
| `@startlatex`                             | ok  | same                                                    |
| `<math>...</math>`, `<latex>...</latex>` in labels (Creole) | ok | shown as plain text                     |

Out of scope for now: `\includegraphics`, GeoGebra/Fcscore atoms, anything
that needs a real window system.

Success criteria:

1. The same formulas render in the JVM jar and in the C executable, with the
   same layout, in SVG, PNG and PDF (and the TXT/TIKZ paths that exist today).
2. No reflection and no dependency on `java.awt`, `javax.swing`, `javax.imageio`,
   `javax.xml` or Batik in the code used by the TeaVM builds.
3. Output is deterministic across platforms (needed by the Vega tests), as
   already done for PDF.
4. The license variants (MIT, EPL, BSD, ASL, LGPL) keep building, see 7.

## 2. How PlantUML uses JLaTeXMath today

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

### 5.2 Glyph outlines

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

- Vega tests from 5.1 on the JVM, and on the C build through
  `tools/vega-c/run_vega_c.py` (once the Vega headers are there).
- Update `net/sourceforge/plantuml/math/readme.md`, `CHANGES.md`, and the
  README of the C build.

## 6. Suggested phases

| Phase | Content | Done when |
|-------|---------|-----------|
| 0 | 5.1 corpus + references; answer the cheap open questions (XML in TeaVM, pinned upstream commit) | corpus committed, references reviewed |
| 1 | Vendor the sources as they are, call them directly (no reflection), JVM only, **no behaviour change** | corpus unchanged on the JVM |
| 2 | Canvas interface + `UGraphic` implementation, glyph outlines (5.2) | corpus renders without AWT fonts, differences understood |
| 3 | Remove `java.awt` / `javax.*` leftovers, XML and resources (5.3, 5.4) | the code compiles with TeaVM (JS and C) |
| 4 | Wire-up (5.5), first formulas in `plantuml.exe` | `@startmath`, `@startlatex`, `<math>` work in the C build |
| 5 | Pruning (5.6), size and start-up measurements, Vega on C (5.7) | size delta acceptable, CI green |
| 6 | Same for JavaScript / Wasm if wanted | |

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

### Open questions

1. **Package name**: keep `org.scilab.forge.jlatexmath` (easier to diff against
   upstream, like `org.openpdf`) or move under `net.sourceforge.plantuml`?
2. **One code path or two**: new renderer everywhere (deterministic, no Batik,
   but SVG output changes from text or Batik shapes to our paths on the JVM too)
   or keep the original library on the JVM and the new one only in TeaVM?
3. **Fonts in SVG**: glyphs as paths (self-contained, larger SVG) or `<text>`
   with an embedded font (smaller, needs the font on the viewer side)?
4. **Alphabets**: ship the Cyrillic and Greek font modules (size) or only the
   base fonts?
5. (answered, see Decisions: vendor tag `1.0.7`)
6. (answered, see Decisions: no `javax.xml` in TeaVM)
7. `AtomMath`'s TIKZ / LaTeX output path (`matchesProperty("TIKZ")`) must keep
   working: it only needs the formula source, no rendering.
8. Static caches and global state in `TeXFormula` / `DefaultTeXFont` versus the
   C build (single-threaded, no monitors): confirm there is no `synchronized`
   left that matters.

## 9. First things to do in the next session

1. ~~Answer questions 5 and 6~~ (done, see section 8).
2. Write the corpus and the Vega references (phase 0).
3. Vendor the upstream sources at the chosen commit and wire direct calls
   (phase 1), as one reviewable patch.

Working method, as usual: small patches (`git format-patch`), each with an
English commit message starting with the emojis (here 🍵 for the C build plus
the usual type emoji).
