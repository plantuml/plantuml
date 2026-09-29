// Convention plugin: how PlantUML artifacts are published to Maven Central.
//
// Shared by the license variants (through plantuml.license-variant) and by
// plantuml-mit-light. The consumer creates the Maven publication named "maven"
// (what goes into it differs from one project to another); this plugin adds
// everything else: POM data, the CentralPortal repository and signing.
//
// Usage:
//
//   plugins {
//       id("plantuml.publishing")
//   }
//
//   plantumlPublishing {
//       pomLicenseName = "MIT License"
//       pomLicenseUrl = "https://opensource.org/license/mit/"
//       // optional: pomName, pomDescription, developer(id, name, email)
//   }
//
//   publishing {
//       publications.create<MavenPublication>("maven") { ... }
//   }

plugins {
	`maven-publish`
	signing
}

val plantumlPublishing = extensions.create<PlantumlPublishingExtension>("plantumlPublishing")

plantumlPublishing.pomName.convention("PlantUML")
plantumlPublishing.pomDescription.convention("PlantUML is a component that allows to quickly write diagrams from text.")

// The consumer's `plantumlPublishing { ... }` and publication are configured
// after this plugin is applied, hence afterEvaluate.
afterEvaluate {
	publishing {
		publications.withType<MavenPublication>().configureEach {
			pom {
				name.set(plantumlPublishing.pomName)
				description.set(plantumlPublishing.pomDescription)
				url.set("https://plantuml.com/")
				licenses {
					license {
						name.set(plantumlPublishing.pomLicenseName)
						url.set(plantumlPublishing.pomLicenseUrl)
					}
				}
				developers {
					developer {
						id.set("arnaud.roques")
						name.set("Arnaud Roques")
						email.set("plantuml@gmail.com")
					}
					plantumlPublishing.extraDevelopers.forEach { (devId, devName, devEmail) ->
						developer {
							id.set(devId)
							name.set(devName)
							email.set(devEmail)
						}
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
}
