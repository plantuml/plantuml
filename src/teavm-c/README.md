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
- the output formats other than SVG, PNG and `--preproc` (they need AWT or
  other libraries);
- the extraction of the source of a PNG file (`--extract-source`; it works for
  an SVG file) and the check of the metadata of a PNG file (`--skip-fresh`: the
  PNG is always generated again).

The splash screen, the progress bar and `--threads` are ignored (the files are
processed one after the other), and the PNG is not split (`split` pages).
The exit codes of the help are those of the JVM (`exit` of the C library, as
`System.exit` does not exist in the classlib of the TeaVM C backend).

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

`.github/workflows/teavm-c.yml` runs on every push to `master-c`: it generates
the C sources once on Linux, then builds and smoke-tests (small mindmap to SVG and PNG, help, exit code) `plantuml.exe` on
Windows x64 and Windows ARM64. The executables are available as workflow
artifacts (`plantuml-c-windows-x64`, `plantuml-c-windows-arm64`).

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
Liberation Sans (no serif nor monospaced font embedded yet). TeaVM's
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
- `plutovg/`: plutovg 1.3.3, unmodified (MIT; its rasterizer and stroker come
  from FreeType, see `source/FTL.TXT`). It brings `stb_truetype` for fonts (its
  `stb_image_write` is not used). Built as a static library, without its
  system font loading: the output must not depend on the machine.
- `fonts/`: Liberation Sans 2.1.5 (SIL Open Font License 1.1), metric
  compatible with Arial, i.e. with what Java's "SansSerif" is on Windows.
  `embed_font.cmake` turns the 4 TTF files (regular, bold, italic, bold italic)
  into C arrays when CMake configures.

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
  `json.spm`, `sprite.spm` and `svg.spm` (about 4.3 MB, Brotli-compressed). The
  raster images (`image.spm`, 2.7 MB) are not embedded: `readDataImagePng` fails
  in the C build.
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
