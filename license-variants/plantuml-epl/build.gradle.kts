// PlantUML under the EPL license.
// Everything shared by the license variants lives in the convention plugin:
// license-variants/build-logic/src/main/kotlin/plantuml.license-variant.gradle.kts

plugins {
	id("plantuml.license-variant")
}

licenseVariant {
	id = "epl"
	pomLicenseName = "EPL License"
	pomLicenseUrl = "https://opensource.org/license/epl-1-0/"
}

// The EPL flavor embeds its runtime dependencies (fat jar)
dependencies {
	implementation(libs.jlatexmath)

	implementation(libs.elk.core)
	implementation(libs.elk.alg.layered)
	implementation(libs.elk.alg.mrtree)

	implementation(libs.openpdf)
}

tasks.withType<Jar>().configureEach {
	// Add dependencies to the JAR
	val runtimeClasspath = configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
	from(runtimeClasspath) {
		exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA") // Avoid conflict on signature
	}
}
