# TeaVM compile-only stub

Minimal stand-ins for the `org.teavm.*` types PlantUML's TeaVM-specific
sources reference. The **only** purpose of this jar is to let `clide`/jdtls
compile those files (`src/main/java/net/sourceforge/plantuml/teavm/**`,
`PortableImageTeaVM.java`, `Emoji.java`, `OpenIconic.java`,
`SignatureUtils.java`, `PathSystem.java`, ...) without a real TeaVM
dependency, which is unreachable from Claude's sandbox (no network access to
Maven Central).

**This jar must never be used to actually run anything.** It gives no real
JavaScript interop - it exists purely to satisfy the Java compiler.

## How it stays inert

- Most `org.teavm.*` types PlantUML uses are genuinely interfaces in the real
  TeaVM API (JS-backed objects with no real Java implementation), so they're
  declared here the same way - with abstract methods, no bodies.
- The one method that needs a real body is `HTMLDocument.current()` (a static
  interface method, allowed since Java 8): it's the single factory PlantUML's
  code uses to obtain a document in the first place, so making it
  `throw new UnsupportedOperationException(...)` guarantees nothing
  downstream can ever obtain a real instance of any of these types either -
  every other method stays unreachable in practice.
- Annotations (`@JSBody`, `@JSExport`, `@JSFunctor`, `@Async`,
  `@PlatformMarker`) are pure markers with no logic - PlantUML's own methods
  they annotate stay `native` (or otherwise unchanged), so no stub behavior
  is needed there at all.

## Building

```
ant
```

Produces `teavm-stub.jar`. Copy it into a project's `.clide/` directory (see
`clide`'s `JDTLS.md`) so `open_project` picks it up automatically.

## Types covered

| Type | Kind |
|---|---|
| `org.teavm.jso.JSObject` | marker interface |
| `org.teavm.jso.JSBody` | annotation |
| `org.teavm.jso.JSExport` | annotation |
| `org.teavm.jso.JSFunctor` | annotation |
| `org.teavm.interop.Async` | annotation |
| `org.teavm.interop.AsyncCallback<T>` | interface |
| `org.teavm.interop.PlatformMarker` | annotation |
| `org.teavm.jso.dom.xml.Element` | interface |
| `org.teavm.jso.dom.xml.Document` | interface |
| `org.teavm.jso.dom.html.HTMLElement` | interface |
| `org.teavm.jso.dom.html.HTMLDocument` | interface (one throwing static method) |
| `org.teavm.jso.dom.html.HTMLCanvasElement` | interface |
| `org.teavm.jso.canvas.CanvasRenderingContext2D` | interface |
| `org.teavm.jso.canvas.ImageData` | interface |
| `org.teavm.jso.typedarrays.Uint8ClampedArray` | interface |

Validated against PlantUML: with this jar in `.clide/`, every
`org.teavm`-related compile error disappears (confirmed via `clide`'s
`print_diagnostics errors`); the only errors left concern other, unrelated
dependencies (OpenPDF, XMLUnit, Mockito, ...).
