# OpenPDF compile-only stub

Minimal stand-ins for the `org.openpdf.*` types PlantUML's PDF backend
(`src/main/java/net/sourceforge/plantuml/openpdf/**`) references. The
**only** purpose of this jar is to let `clide`/jdtls compile those files
without a real OpenPDF dependency, which is unreachable from Claude's sandbox
(no network access to Maven Central).

**This jar must never be used to actually generate a PDF.** It has no real
PDF-generation logic - it exists purely to satisfy the Java compiler.

## How it stays inert

Unlike the TeaVM stub (where the real API types are genuinely interfaces),
OpenPDF's types are real concrete classes, so every method that would need
real PDF-generation logic simply
`throw new UnsupportedOperationException(...)`.

Two exceptions to that rule, both deliberate:

- `Rectangle` and `PdfAction` are left as harmless data holders (they just
  store the numbers/URL they're constructed with). PlantUML's PDF backend
  never calls a method on an instance of either - it only constructs them
  and passes them along - so there is no "real work" to guard against here.
- `PdfWriter.getInstance(...)` and `BaseFont.createFont(...)` are each the
  single factory their class is obtained through in PlantUML's code, so
  making *those* throw already guarantees no real instance of
  `PdfWriter`/`BaseFont` (and everything downstream: `PdfContentByte`,
  `PdfTemplate`, ...) can ever exist. Every other method throws anyway, for
  consistency and defense in depth.

## Building

```
ant
```

Produces `openpdf-stub.jar`. Copy it into a project's `.clide/` directory
(see `clide`'s `JDTLS.md`) so `open_project` picks it up automatically.

## Types covered

| Type | Kind |
|---|---|
| `org.openpdf.text.DocumentException` | checked exception |
| `org.openpdf.text.BadElementException` | checked exception (extends the above) |
| `org.openpdf.text.Rectangle` | class - harmless data holder |
| `org.openpdf.text.Document` | class - all methods throw |
| `org.openpdf.text.Image` | class - all methods throw |
| `org.openpdf.text.pdf.BaseFont` | class - all methods throw |
| `org.openpdf.text.pdf.PdfWriter` | class - all methods throw |
| `org.openpdf.text.pdf.PdfContentByte` | class - all methods throw |
| `org.openpdf.text.pdf.PdfTemplate` | class (extends PdfContentByte) - all methods throw |
| `org.openpdf.text.pdf.PdfAction` | class - harmless data holder |
| `org.openpdf.text.pdf.PdfAnnotation` | class - all methods throw |

Validated against PlantUML: with this jar (alongside the TeaVM stub, and the
real Ant/JUnit5/JUnit-Pioneer/Mockito/XMLUnit/Glytching jars) in `.clide/`,
the whole project - `src/main` and `src/test` - compiles with
**zero errors** (confirmed via `clide`'s `print_diagnostics errors`).
