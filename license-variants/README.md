# License variants

PlantUML is primarily distributed under the GPL (root project). The same source
tree is also published under other licenses, each one being a Gradle subproject
of this folder:

| Directory          | Gradle project        | License header        | Specific build logic            |
|--------------------|-----------------------|-----------------------|---------------------------------|
| `plantuml-asl`     | `:plantuml-asl`       | `asl-license.txt`     | JAR content check               |
| `plantuml-bsd`     | `:plantuml-bsd`       | `bsd-license.txt`     |                                 |
| `plantuml-epl`     | `:plantuml-epl`       | `epl-license.txt`     | fat JAR (embeds dependencies)   |
| `plantuml-gplv2`   | `:plantuml-gplv2`     | `gplv2-license.txt`   | GraalVM native image            |
| `plantuml-lgpl`    | `:plantuml-lgpl`      | `lgpl-license.txt`    |                                 |
| `plantuml-mit`     | `:plantuml-mit`       | `mit-license.txt`     | TeaVM JS engine + npm package   |
| `plantuml-mit-light` | `:plantuml-mit-light` | (reuses `plantuml-mit`) | MIT JAR without heavy resources |

These subprojects are only part of the build in CI mode (`-Pci` or the `CI`
environment variable), see `settings.gradle.kts`. The Gradle project paths are
still `:plantuml-<id>`, so task names and published Maven artifactIds do not
depend on this folder.

## How a variant is built

Everything common lives in the convention plugin
`build-logic/src/main/kotlin/plantuml.license-variant.gradle.kts`:

1. `syncSources` copies the root `src/main/java`;
2. `preprocessLicenceAntTask` runs SJPP (`sjpp.jar`) with the define
   `__<ID>__` and the header `<id>-license.txt`;
3. the result is compiled, packaged (jar, sources, javadoc) and published
   as `net.sourceforge.plantuml:plantuml-<id>`.

A variant build script only declares its license and its specific logic:

```kotlin
plugins {
	id("plantuml.license-variant")
}

licenseVariant {
	id = "bsd"
	pomLicenseName = "BSD License"
	pomLicenseUrl = "https://opensource.org/license/bsd-2-clause/"
}
```
