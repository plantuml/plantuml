# Experimental: PlantUML compiled to C with TeaVM

TeaVM can compile Java bytecode to C (in addition to JavaScript). The C
backend ships with its own runtime and garbage collector, so the result is a
native executable with no JVM.

Current step: `MainC` renders **mindmap diagrams only, SVG only**. It reads
the PlantUML source from a file (or the standard input) and writes the SVG in a
file (or the standard output): `plantuml-c [input.puml [output.svg]]`. Prefer the
files on Windows, whose console encodes the standard streams. On a diagram error:
a message on stderr and nothing on stdout. Other diagram
types are commented out in `PSystemBuilderC` and will be added one by one.

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
build\generated\teavm\c\bin\plantuml-c.exe mindmap.puml mindmap.svg
```

If `Launch-VsDevShell.ps1` or `vcvarsall.bat` is missing, the Build Tools
installation is incomplete (for instance after an interrupted download): use
"More > Repair" in the Visual Studio Installer.

### Linux / macOS

```
cmake -S build/generated/teavm/c -B build/c-native
cmake --build build/c-native
build/generated/teavm/c/bin/plantuml-c mindmap.puml mindmap.svg
```

## Continuous integration

`.github/workflows/teavm-c.yml` runs on every push to `master-c`: it generates
the C sources once on Linux, then builds and smoke-tests (small mindmap to SVG) `plantuml-c.exe` on
Windows x64 and Windows ARM64. The executables are available as workflow
artifacts (`plantuml-c-windows-x64`, `plantuml-c-windows-arm64`).

## Expected output

Same as on the JVM (`java ... net.sourceforge.plantuml.teavm.c.HelloWorldC Arnaud`),
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

## What differs from the JavaScript build

- `TeaVM.isTeaVM()` is true for every TeaVM target, C included. Code that only
  exists in a browser (`@JSBody`, DOM, viz.js, script and file loaders) must be
  guarded by `TeaVM.isJavaScript()`; `TeaVM.isC()` selects the C behavior. Emoji,
  OpenIconic, `!theme`, `!include` of a library and Graphviz are not available
  in C (Smetana is always used for the layout).
- Text measurement: no AWT and no canvas, so `StringBounderFromWidthTable`.
- Async code: TeaVM turns a method into "async" code (fibers) when it can reach
  an `@Async` method of its classlib, and its coroutine transformation then fails
  with an `ArrayIndexOutOfBoundsException` on some methods. Plain locks are not
  a problem (without `Thread.start`, TeaVM ignores monitors). The culprit was
  `java.net.URL.openStream()` (XHR connection, JavaScript only): `SURL.openStream`
  returns `null` in C. Any new use of `java.net.URL`, `Thread.sleep`,
  `Object.wait`... on the path from `MainC` will bring the problem back.
