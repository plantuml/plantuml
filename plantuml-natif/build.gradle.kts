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
// Produces one native binary via the GraalVM buildtools plugin:
//   - plantuml (built with -Djava.awt.headless=true, for server / CLI use)
//
// Build with:
//   gradle :plantuml-natif:nativeCompile -x test
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
	binaries.named("main") {
		imageName.set("plantuml")
		mainClass.set(application.mainClass)
		runtimeArgs(listOf("-Djava.awt.headless=true"))
		// The brotli package builds its static dictionary into a direct (off-heap)
		// ByteBuffer in a class static initializer. GraalVM initializes classes at
		// build time by default, but direct ByteBuffers allocated at build time do
		// not survive into the image -- the dictionary ends up null at run time
		// ("brotli dictionary is not set"). Forcing run-time initialization makes
		// the static initializer run at startup, allocating the buffer for real.
		//
		// By default native-image targets the build machine's own CPU features,
		// so the binary fails on older CPUs with an error like:
		//   "The current machine does not support all of the following CPU
		//    features that are required by the image: [..., AVX, AVX2, BMI1,
		//    BMI2, FMA]. Please rebuild the executable with an appropriate
		//    setting of the -march option."
		// We use '-march=compatibility' to target a conservative baseline
		// (x86-64-v1 / equivalent) so the binary runs on older hardware, at a
		// small performance cost.
		buildArgs(listOf(
			"-Djava.awt.headless=true",
			"-march=compatibility",
			"--enable-url-protocols=https",
			"--initialize-at-run-time=net.sourceforge.plantuml.brotli.Dictionary,net.sourceforge.plantuml.brotli.Dictionary\$DataLoader,net.sourceforge.plantuml.brotli.DictionaryData"
		))
	}
	toolchainDetection = false
}
