// PlantUML under the LGPL license.
// Everything shared by the license variants lives in the convention plugin:
// license-variants/build-logic/src/main/kotlin/plantuml.license-variant.gradle.kts

plugins {
	id("plantuml.license-variant")
}

licenseVariant {
	id = "lgpl"
	pomLicenseName = "LGPL License"
	pomLicenseUrl = "https://opensource.org/license/lgpl-2-1/"
}
