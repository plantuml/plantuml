# Ant compile-only stub

Minimal stand-ins for the `org.apache.tools.ant.*` types PlantUML's two Ant
tasks (`src/main/java/net/sourceforge/plantuml/ant/CheckZipTask.java`,
`PlantUmlTask.java`) reference. The **only** purpose of this jar is to let
`clide`/jdtls compile those two files without pulling in the full real
`ant.jar` (~2.3 MB) - this stub is a few KB.

**This jar must never be used to actually run an Ant build.** It has no real
task-execution or file-scanning logic - it exists purely to satisfy the Java
compiler.

## A note on the self-reference risk

Building *this* stub jar with the real `ant` command, while the stub itself
defines classes named `org.apache.tools.ant.Task` etc. (the very same
classes the `ant` tool needs on its own classpath to run at all), looks
circular at first glance. It isn't, in practice: `build.xml`'s `<javac>`
task has `includeantruntime="false"`, which means the compile classpath is
just the JDK plus `classes.dir` - Ant's own runtime jars (the real
`org.apache.tools.ant.*` classes the outer `ant` process is using) are never
added to it. Verified by actually building this jar with `ant` and checking
the compiled `.class` files hold our throwing stub bodies, not the real
implementation.

## How it stays inert

Every method that would need a real running Ant project (file scanning,
logging, task execution) throws `UnsupportedOperationException`. Two
exceptions, both deliberate:

- `Project` is left completely empty: PlantUML's tasks only ever pass it
  around as an opaque handle (`getProject()` straight into
  `fileSet.getDir(project)`, etc.) - they never call a method on it, so
  there is no "real work" to guard against here.
- Constructors are harmless (no-op): the "real work" lives in the methods
  that would need actual Ant machinery, not in merely creating the object.

## Building

```
ant
```

Produces `ant-stub.jar`. Copy it into a project's `.clide/` directory (see
`clide`'s `JDTLS.md`) so `open_project` picks it up automatically.

## Types covered

| Type | Kind |
|---|---|
| `org.apache.tools.ant.Project` | class - harmless empty handle |
| `org.apache.tools.ant.BuildException` | unchecked exception (extends RuntimeException, matching the real API) |
| `org.apache.tools.ant.Task` | class - all methods throw |
| `org.apache.tools.ant.DirectoryScanner` | class - all methods throw |
| `org.apache.tools.ant.types.FileSet` | class - all methods throw |
| `org.apache.tools.ant.types.FileList` | class - all methods throw |

Validated against PlantUML: with this jar (alongside the TeaVM and OpenPDF
stubs, and the real JUnit5/JUnit-Pioneer/Mockito/XMLUnit/Glytching jars, but
*without* the real `ant.jar`) in `.clide/`, the whole project - `src/main`
and `src/test` - still compiles with **zero errors** (confirmed via
`clide`'s `print_diagnostics`), for a fraction of the classpath size.
