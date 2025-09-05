//    permits to start the build setting the javac release parameter, no parameter means build for java8:
// gradle clean build -x javaDoc -PjavacRelease=8
// gradle clean build -x javaDoc -PjavacRelease=17
//    also supported is to build first, with java17, then switch the java version, and run the test with java8:
// gradle clean build -x javaDoc -x test
// gradle test
val javacRelease = (project.findProperty("javacRelease") ?: "8") as String

plugins {
	java
	`maven-publish`
	signing
}

group = "net.sourceforge.plantuml"
description = "PlantUML"

java {
	withSourcesJar()
	withJavadocJar()
}

dependencies {
	compileOnly(libs.ant)
	testImplementation(libs.assertj.core)
	testImplementation(libs.junit.jupiter)
	testImplementation(libs.jlatexmath)
	testImplementation(libs.xmlunit.core)
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
	if (JavaVersion.current().isJava8) {
		java.targetCompatibility = JavaVersion.VERSION_1_8
	} else {
		options.release.set(Integer.parseInt(javacRelease))
	}
}

tasks.withType<Jar>().configureEach {
	manifest {
		attributes["Main-Class"] = "net.sourceforge.plantuml.Run"
		attributes["Implementation-Version"] = archiveVersion
		attributes["Build-Jdk-Spec"] = System.getProperty("java.specification.version")
		from(rootProject.layout.projectDirectory.file("manifest.txt"))
	}

	// source sets for java and resources are on "src", only put once into the jar
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

val syncSources by tasks.registering(Sync::class) {
	from(rootProject.layout.projectDirectory.dir("src/main/java"))
	into(project.layout.buildDirectory.dir("sources/sjpp/java"))
}

val preprocessLicenceAntTask by tasks.registering() {
	dependsOn(syncSources)
	inputs.dir(project.layout.buildDirectory.dir("sources/sjpp/java"))
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
				"define" to "__MIT__",
				"header" to project.layout.buildDirectory.file("../mit-license.txt").get().asFile.absolutePath
			)
		}
	}
}

tasks.processResources{
	dependsOn(preprocessLicenceAntTask)
}

tasks.compileJava{
	dependsOn(preprocessLicenceAntTask)
}

tasks.named("sourcesJar"){
	dependsOn(preprocessLicenceAntTask)
}


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
					name.set("MIT License")
					url.set("https://opensource.org/license/mit/")
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
			name = "OSSRH"
			val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
			val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
			url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
			credentials {
				username = System.getenv("OSSRH_USERNAME")
				password = System.getenv("OSSRH_PASSWORD")
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
