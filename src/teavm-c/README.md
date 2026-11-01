# Experimental: PlantUML compiled to C with TeaVM

TeaVM can compile Java bytecode to C (in addition to JavaScript). The C
backend ships with its own runtime and garbage collector, so the result is a
native executable with no JVM.

Current step: the executable has the entry point of the JVM
(`net.sourceforge.plantuml.Run`), so it has the same command line and the same
help (`plantuml --help`). It renders **mindmap diagrams only, in SVG or PNG**
(PNG is the default format, as on the JVM): `plantuml [options] file.puml`
writes `file.png` (or `file.svg` with `-tsvg`) next to the source, or in the
`-o` directory, and `plantuml -tsvg -pipe < file.puml > file.svg` reads the
standard input. Prefer the files on Windows, whose console encodes the standard
streams. Other diagram types are commented out in `PSystemBuilderC` and will be
added one by one.

What the C build cannot do is guarded by `TeaVM.isC()`, so that TeaVM does not
compile (and links nothing of) it. The flags exist and are parsed as on the JVM,
but they only print "... is not available in the C build":

- the GUI (it is also what `plantuml` without arguments would start on the
  JVM: the C build prints the help instead), `--ftp-server`, `--http-server`,
  `--clipboard`, `-printfonts`, `-license`, the statistics, `--encode-sprite`,
  `--check-graphviz` (there is no Graphviz: Smetana computes the layout);
- the output formats other than SVG, PNG, text (`-ttxt`, `-tutxt`) and `--preproc`
  (PDF, EPS, LaTeX, HTML5, VDX, XMI... need AWT or other libraries);
- the extraction of the source of a PNG file (`--extract-source`; it works for
  an SVG file) and the check of the metadata of a PNG file (`--skip-fresh`: the
  PNG is always generated again).

The splash screen, the progress bar and `--threads` are ignored (the files are
processed one after the other), and the PNG is not split (`split` pages).
The exit codes of the help are those of the JVM (`exit` of the C library, as
`System.exit` does not exist in the classlib of the TeaVM C backend).

The security profile works as on the JVM: `-DPLANTUML_SECURITY_PROFILE=SANDBOX`
(or the environment variable) decides which local files a diagram may read
(`!include`, `!include_once`, `!include_many`, `!includesub`, `!import`,
`!theme ... from`, `%file_exists`), and `-Dplantuml.include.path=DIR` and
`-Dplantuml.allowlist.path=DIR` list the folders that stay readable in the
modes that restrict files. The profile of the browser build is unchanged (no
file system).

`!include`, `!include_once`, `!include_many` and `!includesub` read an
`http(s)` URL, with the same rules as the JVM: refused in SANDBOX, filtered by
`-Dplantuml.allowlist.url=URL1;URL2` (a URL starting with one of them), ports 80
and 443 only in INTERNET, no redirection except in INSECURE, and no host that
resolves to a loopback, link-local or private address (except in INSECURE or
for an allowlisted URL). The decision is taken in Java (`UrlPolicy`); the
transfer is a blocking native call (`plantuml_http.c`: WinHTTP on Windows,
libcurl on Linux and macOS), so there is no coroutine. A build made without
the libcurl development files (`libcurl4-openssl-dev`) still works, but refuses
every URL. The themes, `%load_json` and the images of a URL are not covered.

The help is the one of the JVM: it is built from the annotations of `CliFlag`,
which TeaVM cannot read, so `CliFlagDocTable` holds their values as plain code
(`CliFlagDocTableTest` fails when they differ).

The SVG should be byte for byte the one `java -jar plantuml.jar -tsvg` writes on
Windows (same version of PlantUML): same export code, and text measured as Java
measures it on Windows. The PNG has the size and the layout of the one `-tpng`
writes on Windows, and the same metadata (source of the diagram); only the
antialiasing differs (see "PNG output" below).

`HelloWorldC` (no PlantUML code involved) is kept to validate the tool chain
alone: set it as `mainClass` of the `c` block in `build.gradle.kts`.

## 1. Generate the C sources

Requires Gradle running on JDK 17+ (like `generateJavaScript`):

```
gradlew :generateC
```

The `:` prefix matters: the `plantuml-mcp-js` subproject also applies the TeaVM
plugin, so a plain `generateC` would also run there and fail (no main class).

Output: `build/generated/teavm/c/` (`all.txt` lists the `.c` files; Gradle
also copies `CMakeLists.txt` from this folder there).

## 2. Build the native executable

TeaVM's C runtime detects Windows through `_MSC_VER`, so on Windows the
compiler must be **MSVC** (or **clang-cl**). A GNU-style clang or gcc
(MinGW, llvm-mingw, MSYS2) defines `__GNUC__` and selects the POSIX code
paths, which will not compile.

### Windows on ARM64

Install Visual Studio 2022 Build Tools with the workload "Desktop development
with C++", and make sure these components are selected:

- MSVC ARM64/ARM64EC build tools
- C++ CMake tools for Windows (provides `cmake` and `ninja`)

Then load the MSVC environment in PowerShell (from the repository root).
`-HostArch` only accepts `x86` or `amd64`: the x64 compiler runs under
emulation, but it does generate native **ARM64** code (`-Arch arm64`):

```
$bt = "${env:ProgramFiles(x86)}\Microsoft Visual Studio\2022\BuildTools"
& "$bt\Common7\Tools\Launch-VsDevShell.ps1" -Arch arm64 -HostArch amd64 -SkipAutomaticLocation
cl    # the banner must end with "for ARM64"
```

This has to be done again in every new PowerShell window. Then:

```
cmake -S build\generated\teavm\c -B build\c-native -G Ninja
cmake --build build\c-native
build\generated\teavm\c\bin\plantuml.exe -tsvg mindmap.puml
```

If `Launch-VsDevShell.ps1` or `vcvarsall.bat` is missing, the Build Tools
installation is incomplete (for instance after an interrupted download): use
"More > Repair" in the Visual Studio Installer.

### Linux / macOS

```
cmake -S build/generated/teavm/c -B build/c-native
cmake --build build/c-native
build/generated/teavm/c/bin/plantuml -tsvg mindmap.puml
```

## Continuous integration

`.github/workflows/teavm-c.yml` is called by `ci.yml` on every push to `master`: it generates
the C sources once on Linux, then builds and smoke-tests (small mindmap to SVG and PNG, help, exit code) the
executable on Windows (MSVC), Linux and macOS, for x64 and arm64 each. The executables are available as
workflow artifacts (`plantuml-windows-x64`, `plantuml-windows-arm64`, `plantuml-linux-x64`,
`plantuml-linux-arm64`, `plantuml-macos-x64`, `plantuml-macos-arm64`), and they are all attached to the
snapshot release (`plantuml-{windows,linux,macos}-{amd64,arm64}-SNAPSHOT.zip`).

## Expected output

Same as on the JVM (`java ... net.sourceforge.plantuml.teavm.c.demo.HelloWorldC Arnaud`),
also stored in `expected-hello.txt` (used by the CI smoke test):

```
1. Hello from PlantUML compiled to C
2. Hello Arnaud (1 args)
3. Collections: [item0, item1, item2, item3, item4] 5
4. Lambda: 144
5. Exceptions: OK
6. GC survived, total=2088890
7. Double: 141.4213562373095
8. Done
```

## PNG output

`UgDiagram.exportPng` runs the same code as `-tpng` (`TextBlockExporter.exportPngTo`:
same image size and background), with `UGraphicRaster` (package
`klimt.drawing.raster`) instead of `UGraphicG2d`: the same drivers, line for
line, drawing on a `RasterCanvas` instead of a `Graphics2D`. In the C build the
canvas is `PngCanvas`: Java calls a small C facade over
[plutovg](https://github.com/sammycage/plutovg) through TeaVM's `@Import`,
which draws with the embedded fonts and encodes the PNG in memory; Java adds
PlantUML's chunks (`PngChunks`: copyleft, compressed source of the diagram).

The PNG is encoded in C (`native/plantuml_png.c`) with the choices of PngIO
on the JVM: an opaque image that fits in 256 colors gets a palette (a port of
`Quantify555`), otherwise each channel loses its least significant bit (a port
of `QuantifyPacked28`); the pixels are the ones PngIO writes for the same image
(only the deflate implementation differs: miniz instead of zlib). Encoding works
on the pixels of the surface, row by row: no copy of the image, and nothing in
the Java heap (64 MB at most in the C build). Keep the C port in step with
`net.sourceforge.plantuml.png.quant`.

`UGraphicRasterTest` checks the drivers on the JVM, with a canvas drawing with
Java2D: the PNG must be the one of `-tpng`, pixel for pixel.

Not supported yet: shadows, patterns (stripes...), images and sprites, circled
characters, the image map of links, the watermark; text is always drawn with
Liberation Sans, and the fonts of the machine for the characters it lacks (see
"Fonts of the machine"; no serif nor monospaced font embedded yet). TeaVM's
`System.getenv` always returns null, so `PLANTUML_LIMIT_SIZE` is not read: the
size of the image is limited to 4096 pixels.

`PngDemoC` (package `net.sourceforge.plantuml.teavm.c.demo`) validates the
native side alone, without PlantUML:

```
gradlew :generateC -PteavmCMainClass=PngDemoC
cmake -S build/generated/teavm/c -B build/c-native
cmake --build build/c-native
build/generated/teavm/c/bin/plantuml demo.png
```

It draws a small mind map whose boxes are sized from the measured text, and a
text sample on its baseline (red) between its ascent and descent lines (blue).
It prints the font metrics and a few text widths: at 14 px, `Hello world` must
be 69.248046875 wide, and ascent/descent 12.673828/2.9667969. These are exactly
what Java2D gives for the same font with fractional metrics (and for Arial,
whose metrics are the same).

Native sources (`native/`, copied by Gradle next to the generated C):

- `plantuml_canvas.h` / `.c`: the facade. Only `int`, `long`, `double` and
  primitive arrays cross the boundary: a canvas is a `long` handle, text is a
  UTF-16 `char[]` (TeaVM's String to C conversion depends on the C locale).
  The Java side is `NativeCanvas` (raw bindings) and `PngCanvas` (the
  `RasterCanvas`), in package `teavm.c`.
- `plantuml_png.h` / `.c`: the PNG encoder (see above).
- `miniz/`: miniz 3.1.2 (MIT), the amalgamated release (`miniz.c`, `miniz.h`),
  unmodified: deflate and CRC of the PNG encoder only.
- `plutovg/`: plutovg 1.3.3 (MIT; its rasterizer and stroker come
  from FreeType, see `source/FTL.TXT`), with one function added,
  `plutovg_font_face_glyph_outline_size` (marked "PlantUML addition" in
  `plutovg.h` and `source/plutovg-font.c`). It brings `stb_truetype` for fonts
  (its `stb_image_write` is not used). Built as a static library, without its
  system font loading: the output must not depend on the machine (see "Fonts of
  the machine" for the only exception).
- `plantuml_fonts.h` / `.c`: the fallback to the fonts of the machine.
- `fonts/`: Liberation Sans 2.1.5 (SIL Open Font License 1.1), metric
  compatible with Arial, i.e. with what Java's "SansSerif" is on Windows.
  `embed_font.cmake` turns the 4 TTF files (regular, bold, italic, bold italic)
  into C arrays when CMake configures.

## Text output (`-ttxt`, `-tutxt`)

No native code: the sequence diagrams use their own text renderer (`asciiverse`, called
by `SequenceDiagram.exportTxt`), the other diagrams are drawn by `UGraphicTxt` (only their
texts, as on the JVM). Text is measured in character cells (`TextStringBounder`), not with
the embedded fonts: `FileFormat.getDefaultStringBounder` picks it for these two formats.

## Text measurement with the embedded fonts

`StringBounderEmbeddedFont` (package `klimt.drawing.font`) measures text
without AWT, with the advance widths and vertical metrics of the embedded
Liberation Sans, so that the layout matches the fonts the C build draws with.
It reproduces `StringBounderAwt` on Windows: advances with fractional metrics
(no kerning, no ligatures), and for the logical font "SansSerif" the line height
of Java's composite font. "SansSerif" does not take every character from Arial:
those Arial lacks (CJK...) and those of its exclusion ranges in Java's
`fontconfig.properties` (dashes, typographic quotes, ellipsis...) come from
fallback fonts. Their widths are measured once with Java on Windows and kept in
`tools/font-metrics/sansserif-windows.txt`.
Text with characters of complex scripts (combining marks, Hebrew...) may differ
slightly: Java2D shapes it with HarfBuzz.

The metrics come from `LiberationSansMetrics`, generated from the TTF files and
the measured widths:

```
java tools/font-metrics/GenerateFontMetrics.java
```

To measure the widths again (on Windows only, with a Western locale):

```
gradlew compileJava
java -cp build/classes/java/main tools/font-metrics/MeasureLogicalFonts.java
java tools/font-metrics/GenerateFontMetrics.java
```

`StringBounderEmbeddedFontTest` checks the tables against Java2D on the same
TTF files and the measured widths (any OS), and the whole bounder against
`StringBounderAwt` on Windows.

## Fonts of the machine

The embedded Liberation Sans has no Chinese, Japanese, Korean or Arabic, no
symbols (✓, ☃...) and no emoji: they were drawn as the empty box of the missing
glyph. `plantuml_fonts.c` now looks for them in the fonts of the machine, glyph by
glyph, only for the code points Liberation Sans has no outline for:

- Text that Liberation Sans covers is measured and drawn by plutovg as a whole,
  exactly as before: its PNG does not depend on the machine (the Vega tests
  stay the same).
- The fonts are looked for in `/usr/share/fonts`, `/usr/local/share/fonts`,
  `~/.fonts` and `~/.local/share/fonts` (Linux), `/System/Library/Fonts`,
  `/Library/Fonts` and `~/Library/Fonts` (macOS), `%WINDIR%\Fonts` and
  `%LOCALAPPDATA%\Microsoft\Windows\Fonts` (Windows), after the folders of the
  `PLANTUML_FONT_DIRS` environment variable (separated by `:`, or `;` on
  Windows), which win. In a Docker image, install a font package such as
  `fonts-noto-cjk`.
- The fonts with the widest coverage (Noto Sans CJK, Source Han Sans, Noto Sans,
  DejaVu Sans, Arial Unicode, the Windows and macOS CJK fonts...) are tried
  first, regular before bold or light; the others follow, `unifont` last. A file
  is read at most once, and the answer for a code point is kept. Up to 192 MB of
  fonts are read; with no suitable font, the box of the missing glyph is drawn as
  before.
- Regular weight only: bold and italic text uses the regular fallback glyphs.
  Color emoji fonts (bitmaps) are skipped, as plutovg only draws outlines: emoji
  stay boxes unless a font with outlines has them (DejaVu Sans has a few
  symbols).
- No shaping and no right-to-left layout (plutovg has none): Arabic letters are
  drawn isolated and left to right.
- The layout is not changed: it is measured in Java (see above), with widths that
  do not depend on the machine. A fallback glyph keeps the advance of the font it
  comes from, which can differ slightly from the width reserved for it (full width
  CJK characters match).

## What differs from the JavaScript build

- `TeaVM.isTeaVM()` is true for every TeaVM target, C included. Code that only
  exists in a browser (`@JSBody`, DOM, viz.js, script and file loaders) must be
  guarded by `TeaVM.isJavaScript()`; `TeaVM.isC()` selects the C behavior. Emoji,
  OpenIconic, the sprites and svg of the standard library and Graphviz are not available in C (Smetana is always used for the layout).
- Text measurement: no AWT and no canvas, so `StringBounderEmbeddedFont` (see
  above), which measures as Java does on Windows.
- Export: `TextBlockExporter.exportTo` (and so `UgDiagram.exportDiagram`) only
  writes SVG and PNG in C, without referencing the drivers of the other
  formats, which need AWT (TeaVM compiles every method a reachable method
  calls).
- Input: the command line reads the file with `ReadLineReader`, as on the JVM
  (byte order mark removed, U+2013 replaced by `-`).
- Standard input (`-pipe`, `-pipemap`, `--syntax`): the `System.in` of the TeaVM C
  classlib (`ConsoleInputStream`) throws on every `read`, which aborted the process
  (exit code 134) before the first byte. `Run` uses `StdinStream` instead, which
  calls `read(0, ...)` of the C library through `native/plantuml_stdin.c`
  (binding: `NativeStdin`). The CI checks it on Linux and macOS (pipe, and input
  redirected from a file).
- Async code: TeaVM turns a method into "async" code (fibers) when it can reach
  an `@Async` method of its classlib, and its coroutine transformation then fails
  with an `ArrayIndexOutOfBoundsException` on some methods. Plain locks are not
  a problem (without `Thread.start`, TeaVM ignores monitors). The culprit was
  `java.net.URL.openStream()` (XHR connection, JavaScript only): `SURL.openStream`
  returns `null` in C. Any new use of `java.net.URL`, `Thread.sleep`,
  `Object.wait`... on the path from `Run` will bring the problem back.
  The JavaScript build uses no coroutine either (no worker thread, no `@Async`:
  see `PendingResources`), and `tools/teavm-coroutines/check_no_coroutines.py`
  fails the CI when either output contains one again.

## Embedded files (themes, standard library)

Files that Java reads at run time (the bundled `!theme` files and the standard
library) are embedded in the executable, since the C build has no classpath:

- Gradle (`copyNativeSources`) copies them from `src/main/resources` into
  `native/resources`, next to the generated C: `themes/*.puml`, and in
  `stdlib/` the list (`home.spm`) and, for each library, `info.spm`, `puml.spm`,
  `json.spm`, `sprite.spm`, `svg.spm` and `image.spm` (about 7 MB,
  Brotli-compressed). A raster image is written to the SVG as a PNG encoded in
  C (`PngImageC`).
- CMake (`native/embed_resources.cmake`) lists them, and `native/embed_tool.c`,
  a small program built and run by CMake, turns them into a C table. (A CMake
  script would take minutes for the megabytes of the standard library.)
- `native/plantuml_resources.c` looks a file up by its path under
  `src/main/resources` (for instance `themes/puml-theme-amiga.puml`), and Java
  reads it with `EmbeddedFiles.read(name)` (binding: `NativeResources`).
- The `.spm` files are compressed with Brotli. `native/brotli/` is the C decoder
  of Google (MIT, unmodified, see its README), and
  `EmbeddedFiles.readBrotli(name)` decompresses a file straight from the
  embedded bytes. `SpmChannel` uses it in C, so `!include <lib/file>`,
  `!theme x from <lib>`, `%` JSON of a library, the sprites (`<tupadr3/...>`,
  `<aws/...>`) and the list of libraries work as
  on the JVM. The Java Brotli decoder (and its dictionary) is not in the C build.

To embed more files, add `name=path` pairs to `RESOURCE_PAIRS` in
`CMakeLists.txt` and have Gradle copy them.

## Formulas (`@startmath`, `@startlatex`, `<math>`, `<latex>`)

JLaTeXMath (`org.scilab.forge.jlatexmath`, vendored) draws a formula as paths on
a `UGraphic`, so the SVG, PNG and PDF of the C build get the same shapes as the
JVM (see `PLAN.md`). The library has no `java.awt`, no `javax.*`, no reflection and
no `Character.UnicodeBlock`, none of which the C class library has. The text that
no TeX font can draw (CJK...) is measured with the `StringBounder` of the diagram and
drawn as a `UText`.

It reads 71 files (68 XML files and 3 `glyphs.bin`), through `TeXResources`: there is no
class loader in the C build, and TeaVM's resource support crashes its coroutine
transformation. `copyNativeSources` copies them from
`src/main/resources/org/scilab/forge/jlatexmath` into `native/resources/jlatexmath`,
CMake embeds them, and `MathResources` gives the library a `JlatexmathResources`,
which reads the embedded files (`jlatexmath/...`); on the JVM, the class loaders are
used. About 2 MB are added to the executable. The XML files are read by `XmlReader`
(no `javax.xml` either).

`PSystemBuilderC` has `PSystemMathFactory` and `PSystemLatexFactory`, and the
`TeaVM.isTeaVM() == false || TeaVM.isC()` tests of the Creole parser let `<math>` and
`<latex>` through (the browser build still shows them as text). Not in the C build:
the TikZ output of a formula, and EPS.

Test it with `python tools/vega-c/run_vega_c.py math`: 216 of the 221 tests of
`vega/math` are SVG or PDF ones that the script checks (the others are TikZ and
text outputs); `decimal:` is given to `plantuml.exe` as `--decimal`.

## PDF

PDF output goes through the OpenPDF sources, pruned and kept in
`src/main/java/org/openpdf` (no AWT, no `Font`, `TextLayout`, `Bidi`, image
decoders, ICC profiles, ...): there are no stand-in classes to maintain. Keep it
that way when touching OpenPDF: a new JDK dependency in `org/openpdf` shows up as
a `generateC` error.

OpenPDF has no class loader to read its resources (AFM font metrics, glyph list,
messages) in the C build, and TeaVM's resource support crashes its coroutine
transformation. `BaseFont.setResourceProvider` is given `OpenPdfResources`, which
reads the embedded files (`openpdf/...`, copied from `src/main/resources/org/openpdf`
by `copyNativeSources`); on the JVM, `ClassLoaderResources` is used. `OpenPdfSetup`
chooses one, once, before the first use of a font.
