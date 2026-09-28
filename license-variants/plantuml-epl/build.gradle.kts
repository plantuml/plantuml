// PlantUML under the Eclipse Public License v1.0.
// Everything shared by the license variants lives in the convention plugin:
// license-variants/build-logic/src/main/kotlin/plantuml.license-variant.gradle.kts

plugins {
	id("plantuml.license-variant")
}

licenseVariant {
	id = "epl"
	pomLicenseName = "Eclipse Public License v1.0"
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

// Only the main jar: sources and javadoc jars must not contain the dependencies
tasks.named<Jar>("jar") {
	// Add dependencies to the JAR (resolved lazily, at execution time)
	from(configurations.runtimeClasspath.map { cp -> cp.map { if (it.isDirectory) it else zipTree(it) } }) {
		exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA") // Avoid conflict on signature
	}
}
