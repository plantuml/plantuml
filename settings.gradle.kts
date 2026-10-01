// https://docs.gradle.org/current/javadoc/org/gradle/api/initialization/Settings.html

pluginManagement {
    // TeaVM preview builds (0.16.0-dev-N) are not on Maven Central / Plugin Portal.
    // Remove this repository once 0.16.0 is released on Maven Central.
    repositories {
        maven { url = uri("https://teavm.org/maven/repository") }
        gradlePluginPortal()
        mavenCentral()
    }
    // Convention plugin "plantuml.license-variant" shared by license-variants/plantuml-*.
    // Only built when a license variant is part of the build (-Pci).
    includeBuild("license-variants/build-logic")
}

rootProject.name = "plantuml"

val isCiBuild = System.getenv("CI") != null || settings.providers.gradleProperty("ci").isPresent
val isGPLOnly = System.getenv("GPL_ONLY") != null
val version: String by settings

println("Running settings.gradle.kts")
println("Version is " + version)

// Check Java version
val javaVersion = JavaVersion.current()
println("Current Java version is " + javaVersion)

// License variants (same sources, re-licensed via SJPP) live under license-variants/.
// Gradle project paths stay ":plantuml-xxx" so task names and published
// artifactIds are unchanged; only the physical directory moves.
fun includeLicenseVariant(name: String) {
    include(name)
    project(":$name").projectDir = file("license-variants/$name")
}

val fastBuild = settings.providers.gradleProperty("fast").isPresent

if (fastBuild) {
    println("-Pfast: only GPL will be built (skipping licence subprojects)")
} else if (isCiBuild && !isGPLOnly) {
    includeLicenseVariant("plantuml-asl")
    includeLicenseVariant("plantuml-bsd")
    includeLicenseVariant("plantuml-epl")
    includeLicenseVariant("plantuml-lgpl")
    includeLicenseVariant("plantuml-mit")
    includeLicenseVariant("plantuml-mit-light")

    // Only include plantuml-gplv2 if Java version is 11 or higher
    if (javaVersion.isCompatibleWith(JavaVersion.VERSION_11)) {
        includeLicenseVariant("plantuml-gplv2")
    } else {
        println("Skipping plantuml-gplv2 as it requires Java 11 or higher")
    }
} else {
    println("Not a CI [without DevTest] build: only GPL will be generated")
}

// Native image subproject (GraalVM). Independent of the CI/licence logic above:
// the native binary is a deliverable that we also want to build locally.
// Requires Java 11 or higher (GraalVM native-image / buildtools plugin).
if (javaVersion.isCompatibleWith(JavaVersion.VERSION_11) && !isGPLOnly) {
    include("plantuml-natif")
} else {
    println("Skipping plantuml-natif (as it requires Java 11 or higher)")
}

// TeaVM headless JS build powering the Node-based PlantUML MCP server
// (plantuml-mcp-js). Like the other TeaVM-related tooling, it requires Java 11+.
if (javaVersion.isCompatibleWith(JavaVersion.VERSION_11) && !isGPLOnly) {
    include("plantuml-mcp-js")
} else {
    println("Skipping plantuml-mcp-js (as it requires Java 11 or higher)")
}
