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
	`maven-publish`
	signing
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
// Publishing
// ============================================

publishing {
	publications.create<MavenPublication>("maven") {
		from(components["java"])
		pom {
			name.set("PlantUML")
			description.set("PlantUML is a component that allows to quickly write diagrams from text.")
			groupId = project.group as String
			artifactId = project.name
			version = project.version as String
			url.set("https://plantuml.com/")
			licenses {
				license {
					name.set(licenseVariant.pomLicenseName)
					url.set(licenseVariant.pomLicenseUrl)
				}
			}
			developers {
				developer {
					id.set("arnaud.roques")
					name.set("Arnaud Roques")
					email.set("plantuml@gmail.com")
				}
			}
			scm {
				connection.set("scm:git:git://github.com:plantuml/plantuml.git")
				developerConnection.set("scm:git:ssh://git@github.com:plantuml/plantuml.git")
				url.set("https://github.com/plantuml/plantuml")
			}
		}
	}
	repositories {
		maven {
			name = "CentralPortal"
			val releasesRepoUrl = "https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/"
			val snapshotsRepoUrl = "https://central.sonatype.com/repository/maven-snapshots/"
			url = uri(
				if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
			)
			credentials {
				username = System.getenv("CENTRAL_USERNAME")
				password = System.getenv("CENTRAL_PASSWORD")
			}
		}
	}
}

signing {
	if (hasProperty("signing.gnupg.keyName") && hasProperty("signing.gnupg.passphrase")) {
		useGpgCmd()
	} else if (hasProperty("signingKey") && hasProperty("signingPassword")) {
		val signingKey: String? by project
		val signingPassword: String? by project
		useInMemoryPgpKeys(signingKey, signingPassword)
	}
	if (hasProperty("signing.gnupg.passphrase") || hasProperty("signingPassword")) {
		sign(publishing.publications["maven"])
	}
}
