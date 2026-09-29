// Convention plugin shared by all license variants (license-variants/plantuml-<id>).
//
// A variant is the regular PlantUML source tree (root src/main/java), rewritten
// by SJPP with a different license header and __<ID>__ define, then compiled,
// packaged and published as net.sourceforge.plantuml:plantuml-<id>.
//
// Usage in a variant build script:
//
//   plugins {
//       id("plantuml.license-variant")
//   }
//
//   licenseVariant {
//       id = "mit"
//       pomLicenseName = "MIT License"
//       pomLicenseUrl = "https://opensource.org/license/mit/"
//   }
//
// Only what is specific to one variant (TeaVM/npm for mit, fat jar for epl,
// GraalVM for gplv2...) stays in the variant's own build script.
//
//    permits to start the build setting the javac release parameter, no parameter means build for java8:
// gradle clean build -x javaDoc -PjavacRelease=8
// gradle clean build -x javaDoc -PjavacRelease=17
//    also supported is to build first, with java17, then switch the java version, and run the test with java8:
// gradle clean build -x javaDoc -x test
// gradle test

plugins {
	java
	id("plantuml.publishing")
}

val licenseVariant = extensions.create<LicenseVariantExtension>("licenseVariant")

val javacRelease = (project.findProperty("javacRelease") ?: "11") as String

// Version catalog accessors (libs.xxx) are not generated for precompiled script plugins
val libs = the<VersionCatalogsExtension>().named("libs")
fun lib(alias: String) = libs.findLibrary(alias).get()

group = "net.sourceforge.plantuml"
description = "PlantUML"

java {
	withSourcesJar()
	withJavadocJar()
}

dependencies {
	compileOnly(lib("ant"))
	compileOnly(lib("teavm-jso-apis"))
	compileOnly(lib("openpdf"))
	testImplementation(lib("junit-jupiter"))
	testImplementation(lib("jlatexmath"))
	testImplementation(lib("xmlunit-core"))
}

// teavm-classlib 0.15+ is published for JVM 17+ only, while we compile with
// --release 11. The sources only need the JSO/interop APIs (Java 11 compatible),
// never the classlib, so keep it off compileClasspath (the TeaVM plugin adds it
// in plantuml-mit). Same rule as in the root build.gradle.kts.
configurations.compileClasspath {
	exclude(group = "org.teavm", module = "teavm-classlib")
}

repositories {
	mavenLocal()
	mavenCentral()
}

sourceSets {
	main {
		java {
			srcDirs("build/generated/sjpp")
		}
		resources {
			srcDir(rootProject.layout.projectDirectory.dir("src/main/resources"))
		}
	}
}

tasks.compileJava {
	options.release.set(Integer.parseInt(javacRelease))
}

tasks.withType<Jar>().configureEach {
	manifest {
		attributes["Main-Class"] = "net.sourceforge.plantuml.Run"
		attributes["Implementation-Version"] = archiveVersion
		attributes["Build-Jdk-Spec"] = System.getProperty("java.specification.version")
		from(rootProject.layout.projectDirectory.file("manifest.txt"))
	}

	// source sets for java and resources are on "src", only put once into the jar
	exclude("teavm/**")
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"
}

tasks.withType<Javadoc>().configureEach {
	options {
		this as StandardJavadocDocletOptions
		addBooleanOption("Xdoclint:none", true)
		addStringOption("Xmaxwarns", "1")
		encoding = "UTF-8"
		isUse = true
	}
}

// ============================================
// SJPP: rewrite the sources with the variant's license header
// ============================================

val syncSources by tasks.registering(Sync::class) {
	from(rootProject.layout.projectDirectory.dir("src/main/java"))
	into(project.layout.buildDirectory.dir("sources/sjpp/java"))
}

val licenseHeader = layout.projectDirectory.file(licenseVariant.id.map { "$it-license.txt" })

val preprocessLicenceAntTask by tasks.registering {
	dependsOn(syncSources)
	inputs.dir(project.layout.buildDirectory.dir("sources/sjpp/java"))
	inputs.property("licenseId", licenseVariant.id)
	inputs.file(licenseHeader)
	outputs.dir(project.layout.buildDirectory.dir("generated/sjpp"))
	doLast {
		ant.withGroovyBuilder {
			"taskdef"(
				"name" to "sjpp",
				"classname" to "sjpp.SjppAntTask",
				"classpath" to rootProject.layout.projectDirectory.files("sjpp.jar").asPath
			)
			"sjpp"(
				"src" to project.layout.buildDirectory.dir("sources/sjpp/java").get().asFile.absolutePath,
				"dest" to project.layout.buildDirectory.dir("generated/sjpp").get().asFile.absolutePath,
				"define" to "__${licenseVariant.id.get().uppercase()}__",
				"header" to licenseHeader.get().asFile.absolutePath
			)
		}
	}
}

tasks.processResources {
	dependsOn(preprocessLicenceAntTask)
}

tasks.compileJava {
	dependsOn(preprocessLicenceAntTask)
}

tasks.named("sourcesJar") {
	dependsOn(preprocessLicenceAntTask)
}

// ============================================
// Publishing (POM, repository and signing: see plantuml.publishing)
// ============================================

// (no type-safe accessor here: the extension is registered by a plugin of this same build)
extensions.configure<PlantumlPublishingExtension>("plantumlPublishing") {
	pomLicenseName.set(licenseVariant.pomLicenseName)
	pomLicenseUrl.set(licenseVariant.pomLicenseUrl)
}

publishing {
	publications.create<MavenPublication>("maven") {
		from(components["java"])
	}
}
