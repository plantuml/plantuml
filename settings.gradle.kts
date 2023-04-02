rootProject.name = "plantuml"

val isCiBuild = System.getenv("CI") != null

include("plantuml-mit")
