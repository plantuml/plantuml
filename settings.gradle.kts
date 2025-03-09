// https://docs.gradle.org/current/javadoc/org/gradle/api/initialization/Settings.html

rootProject.name = "plantuml"

// Skip full CI for now
val isCiBuild = System.getenv("HACK_TO_SKIP_FULL_CI") != null
val version: String by settings

println("Running settings.gradle.kts")
println("Version is " + version)

// Check Java version
val javaVersion = JavaVersion.current()
println("Current Java version is " + javaVersion)

if (isCiBuild) {
    include("plantuml-asl")
    include("plantuml-bsd")
    include("plantuml-epl")
    include("plantuml-lgpl")
    include("plantuml-mit")
    
    // Only include plantuml-gplv2 if Java version is 11 or higher
    if (javaVersion.isCompatibleWith(JavaVersion.VERSION_11)) {
        include("plantuml-gplv2")
    } else {
        println("Skipping plantuml-gplv2 as it requires Java 11 or higher")
    }
} else {
    println("Not a CI build: only GPL will be generated")
}
