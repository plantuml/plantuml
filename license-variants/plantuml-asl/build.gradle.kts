import java.util.jar.JarFile

// PlantUML under the Apache license.
// Everything shared by the license variants lives in the convention plugin:
// license-variants/build-logic/src/main/kotlin/plantuml.license-variant.gradle.kts

plugins {
	id("plantuml.license-variant")
}

licenseVariant {
	id = "asl"
	pomLicenseName = "ASL License"
	pomLicenseUrl = "https://opensource.org/license/apache-2-0/"
}

val checkJarEntries by tasks.registering {
	dependsOn(tasks.named("jar"))
	doLast {
		val jarFile = tasks.named<Jar>("jar").get().archiveFile.get().asFile
		JarFile(jarFile).use { jar ->
			val required = listOf(
				"net/sourceforge/plantuml/Run.class",
				"sprites/archimate/access.png",
				"skin/plantuml.skin"
			)
			val missing = required.filter { jar.getEntry(it) == null }
			if (missing.isNotEmpty()) {
				throw GradleException("Missing entries in JAR: $missing")
			}
		}
		println("All required entries found in ${jarFile.name}")
	}
}

tasks.named<Jar>("jar") {
	finalizedBy(checkJarEntries)
}

tasks.named("check") {
	dependsOn(checkJarEntries)
}
