// plantuml-natif
//
// Dedicated subproject for building PlantUML as a GraalVM native image.
//
// Unlike the licence subprojects (asl, bsd, epl, gplv2, lgpl, mit, ...), this
// project does NOT run the SJPP licence preprocessing. It builds the native
// image directly from the root project artifact, so the binary contains exactly
// the same code as the main plantuml.jar (version/commit injected by the root
// 'filterSourcesWithBuildInfo' task).
//
// Produces two native binaries via the GraalVM buildtools plugin:
//   - plantuml-full     (AWT/Swing available, -Djava.awt.headless=false)
//   - plantuml-headless (-Djava.awt.headless=true, for server use)
//
// Build with:
//   gradle :plantuml-natif:nativeFullCompile :plantuml-natif:nativeHeadlessCompile -x test
//
// Requires Java 11+ (enforced in settings.gradle.kts).

plugins {
	java
	application
	alias(libs.plugins.graalvm.native)
}

group = "net.sourceforge.plantuml"
description = "PlantUML (native image)"

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {
	// The native image is built from the exact same code as the main jar.
	implementation(project(":"))
}

tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"
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
