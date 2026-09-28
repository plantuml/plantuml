// PlantUML under the BSD license.
// Everything shared by the license variants lives in the convention plugin:
// license-variants/build-logic/src/main/kotlin/plantuml.license-variant.gradle.kts

plugins {
	id("plantuml.license-variant")
}

licenseVariant {
	id = "bsd"
	pomLicenseName = "BSD License"
	pomLicenseUrl = "https://opensource.org/license/bsd-2-clause/"
}
