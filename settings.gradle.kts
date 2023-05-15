rootProject.name = "plantuml"

val isCiBuild = System.getenv("CI") != null

include("plantuml-mit")
include("plantuml-epl")
include("plantuml-bsd")
include("plantuml-asl")
include("plantuml-lgpl")