# Experimental: PlantUML compiled to C with TeaVM

TeaVM can compile Java bytecode to C (in addition to JavaScript). The C
backend ships with its own runtime and garbage collector, so the result is a
native executable with no JVM.

Current step: a plain `HelloWorldC` (no PlantUML code involved), to validate
the tool chain. Each line it prints exercises one part of the runtime
(strings, collections, lambdas, exceptions, GC, double formatting).

## 1. Generate the C sources

Requires Gradle running on JDK 17+ (like `generateJavaScript`):

```
gradlew generateC
```

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

Then open **"ARM64 Native Tools Command Prompt for VS 2022"** (or run
`vcvarsall.bat arm64` in a regular prompt) and, from the repository root:

```
cmake -S build\generated\teavm\c -B build\c-native -G Ninja
cmake --build build\c-native
build\generated\teavm\c\bin\plantuml-c.exe Arnaud
```

### Linux / macOS

```
cmake -S build/generated/teavm/c -B build/c-native
cmake --build build/c-native
build/generated/teavm/c/bin/plantuml-c Arnaud
```

## Expected output

Same as on the JVM (`java ... net.sourceforge.plantuml.teavm.c.HelloWorldC Arnaud`):

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

## Known issues before going further than HelloWorld

- `TeaVM.isTeaVM()` is a `@PlatformMarker` without value: it is `true` for
  every TeaVM target, C included. The JavaScript-only branches it guards
  (`@JSBody`, DOM, viz.js) cannot be compiled to C. It will have to be split
  into "any TeaVM target" and "TeaVM JavaScript" (`Platforms.JAVASCRIPT`).
- Text measurement: the JVM uses AWT and the browser uses a canvas; neither
  exists in C. `StringBounderFromWidthTable` is the candidate.
