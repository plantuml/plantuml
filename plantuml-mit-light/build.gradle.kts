import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named

//
// Standard java setup
//
plugins {
    java
    `maven-publish`
    signing
}

group = "net.sourceforge.plantuml"
description = "PlantUML MIT Light"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenLocal()
    mavenCentral()
}

//
// We reference the original MIT project.
// We do not modify that project in any way.
//
val mitProject = project(":plantuml-mit")
val mitSourceSets = mitProject.extensions.getByType<SourceSetContainer>()
val mitMain = mitSourceSets.getByName("main")

val mitClassesOutput = mitMain.output
val mitAllSources = mitMain.allSource

// We reuse the completed MIT javadoc jar file.
val mitJavadocJarFile = mitProject.layout.buildDirectory.file(
    "libs/${mitProject.name}-${mitProject.version}-javadoc.jar"
)

//
// MAIN LIGHT JAR
//
tasks.named<Jar>("jar") {
    archiveBaseName.set("plantuml-mit-light")

    dependsOn(mitProject.tasks.named("classes"))

    from(mitClassesOutput) {
        exclude("**/*.spm")
        exclude("net/sourceforge/plantuml/emoji/data/**")
    }

    manifest {
        attributes["Main-Class"] = "net.sourceforge.plantuml.Run"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

//
// SOURCES JAR
//
tasks.named<Jar>("sourcesJar") {
    archiveBaseName.set("plantuml-mit-light")

    dependsOn(mitProject.tasks.named("sourcesJar"))
    dependsOn(mitProject.tasks.named("classes"))

    from(mitAllSources) {
        include("**/*.java")
        exclude("**/*.spm")
        exclude("net/sourceforge/plantuml/emoji/data/**")
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

//
// JAVADOC JAR
//
tasks.named<Jar>("javadocJar") {
    archiveBaseName.set("plantuml-mit-light")

    dependsOn(mitProject.tasks.named("javadocJar"))

    from(mitJavadocJarFile) {
        eachFile { relativePath = relativePath }
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

//
// PUBLISHING (identical behavior to all other subprojects)
//
publishing {
    publications.create<MavenPublication>("maven") {
        artifact(tasks.named("jar"))
        artifact(tasks.named("sourcesJar"))
        artifact(tasks.named("javadocJar"))

        groupId = project.group as String
        artifactId = "plantuml-mit-light"
        version = project.version as String

        pom {
            name.set("PlantUML MIT Light")
            description.set("Filtered MIT distribution of PlantUML.")
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
                developer {
                    id.set("nicolas.baumann")
                    name.set("Nicolas Baumann")
                    email.set("nicolas.baumann1@gmail.com")
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
            val releasesRepoUrl =
                "https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl =
                "https://central.sonatype.com/repository/maven-snapshots/"

            url = uri(
                if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl
                else releasesRepoUrl
            )

            credentials {
                username = System.getenv("CENTRAL_USERNAME")
                password = System.getenv("CENTRAL_PASSWORD")
            }
        }
    }
}

//
// SIGNING (identical behavior to all other subprojects)
//
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
