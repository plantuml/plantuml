// PlantUML under the GNU Lesser General Public License v3.0 or later.
// Everything shared by the license variants lives in the convention plugin:
// license-variants/build-logic/src/main/kotlin/plantuml.license-variant.gradle.kts

plugins {
	id("plantuml.license-variant")
}

licenseVariant {
	id = "lgpl"
	pomLicenseName = "GNU Lesser General Public License v3.0 or later"
	pomLicenseUrl = "https://www.gnu.org/licenses/lgpl-3.0.html"
}
