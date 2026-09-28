// PlantUML under the GPL v2 license.
// Everything shared by the license variants lives in the convention plugin:
// license-variants/build-logic/src/main/kotlin/plantuml.license-variant.gradle.kts

plugins {
	id("plantuml.license-variant")
	alias(libs.plugins.graalvm.native)
	application
}

licenseVariant {
	id = "gplv2"
	pomLicenseName = "GPLv2 License"
	pomLicenseUrl = "https://www.gnu.org/licenses/old-licenses/gpl-2.0.html"
}

application {
	mainClass = "net.sourceforge.plantuml.Run"
}

graalvmNative {
	binaries.all { resources.autodetect() }
	binaries.create("full") {
		buildArgs(listOf("-Djava.awt.headless=false", "--enable-url-protocols=https"))
		runtimeArgs(listOf("-Djava.awt.headless=false"))
		imageName.set("plantuml-full")
		mainClass.set(application.mainClass)
		classpath(binaries.named("main").get().classpath)
	}
	binaries.create("headless") {
		imageName.set("plantuml-headless")
		mainClass.set(application.mainClass)
		classpath(binaries.named("main").get().classpath)
		runtimeArgs(listOf("-Djava.awt.headless=true"))
		buildArgs(listOf("-Djava.awt.headless=true", "--enable-url-protocols=https"))
	}
	toolchainDetection = false
}
