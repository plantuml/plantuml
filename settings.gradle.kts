// https://docs.gradle.org/current/javadoc/org/gradle/api/initialization/Settings.html

rootProject.name = "plantuml"

val isCiBuild = System.getenv("CI") != null
val version: String by settings

println("Running settings.gradle.kts")
// println(rootProject.projectDir)
println("Version is " + version)

include("plantuml-asl")
include("plantuml-bsd")
include("plantuml-epl")
include("plantuml-lgpl")
include("plantuml-mit")
include("plantuml-gplv2")


