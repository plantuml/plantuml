# TeaVM Integration for PlantUML

This directory contains the TeaVM integration to compile PlantUML Java code to JavaScript.

## What is TeaVM?

TeaVM is an ahead-of-time (AOT) compiler that translates Java bytecode to JavaScript, enabling Java applications to run in web browsers without requiring a JVM.

## Quick Start

### Build the Hello World Demo

Run the Gradle task to compile and prepare the demo:

```bash
./gradlew prepareTeaVMDemo
```

This will:
1. Compile the Java source code
2. Use TeaVM to convert the bytecode to JavaScript
3. Copy the HTML file to the output directory
4. Generate source maps for debugging

### View the Demo

After building, you can view the demo in two ways:

**Option 1: Direct file opening**
```bash
# Open the generated HTML file directly in your browser
# Location: build/teavm/js/index.html
```

**Option 2: Local web server (recommended)**
```bash
cd build/teavm/js
python -m http.server 8000
# Then open http://localhost:8000 in your browser
```

## Project Structure

```
src/main/
├── java/net/sourceforge/plantuml/teavm/
│   └── HelloWorldTeaVM.java          # Entry point with main() method
└── teavm/
    └── index.html                     # HTML template for the demo
```

## How It Works

1. **Java Source**: `HelloWorldTeaVM.java` contains a simple example using TeaVM's JSO (JavaScript Objects) API to manipulate the DOM
2. **Compilation**: TeaVM compiles the Java bytecode to optimized JavaScript
3. **Output**: Generated files in `build/teavm/js/`:
   - `classes.js` - The compiled JavaScript code
   - `classes.js.map` - Source map for debugging
   - `index.html` - HTML page that loads the JavaScript

## TeaVM Configuration

The configuration in `build.gradle.kts` includes:

```kotlin
teavm {
    js {
        mainClass.set("net.sourceforge.plantuml.teavm.HelloWorldTeaVM")
        outputDir.set(layout.buildDirectory.dir("teavm/js").get().asFile)
        sourceMap.set(true)
        optimizationLevel.set(org.teavm.tooling.TeaVMOptimizationLevel.SIMPLE)
        minifying.set(false)
    }
}
```

### Optimization Levels

- **SIMPLE**: Fast compilation, readable output, good for development
- **ADVANCED**: Balanced optimization
- **FULL**: Maximum optimization, smallest output, slower compilation (for production)

## Next Steps

This Hello World demo demonstrates the basic TeaVM integration. Future enhancements could include:

1. **PlantUML Core Integration**: Compile core PlantUML rendering logic to JavaScript
2. **Browser-based Diagram Editor**: Create a fully client-side diagram editor
3. **Offline Capabilities**: Enable diagram generation without server dependency
4. **PWA Support**: Progressive Web App for installable PlantUML

## Gradle Tasks

- `./gradlew generateJavaScript` - Only compile Java to JavaScript
- `./gradlew prepareTeaVMDemo` - Compile and prepare complete demo
- `./gradlew clean` - Clean generated files

## Documentation

- [TeaVM Official Site](https://teavm.org/)
- [TeaVM Documentation](https://teavm.org/docs/)
- [TeaVM GitHub](https://github.com/konsoletyper/teavm)

## Notes

- TeaVM requires Java 8+ compatibility
- The JSO API provides JavaScript interop capabilities
- Source maps enable debugging Java code in browser DevTools
