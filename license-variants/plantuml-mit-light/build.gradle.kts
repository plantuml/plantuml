import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named

//
// Standard java setup
//
plugins {
    java
    id("plantuml.publishing")
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

// What "light" removes from the MIT jars (compiled classes and sources alike)
val lightExcludes = listOf("**/*.spm", "net/sourceforge/plantuml/emoji/data/**")

// We reuse the completed MIT javadoc jar.
val mitJavadocJar = mitProject.tasks.named<Jar>("javadocJar")

// Settings shared by the jar, sourcesJar and javadocJar tasks
tasks.withType<Jar>().configureEach {
    archiveBaseName.set("plantuml-mit-light")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

//
// MAIN LIGHT JAR
//
tasks.named<Jar>("jar") {
    dependsOn(mitProject.tasks.named("classes"))

    from(mitMain.output) {
        exclude(lightExcludes)
        exclude("teavm/**")
    }

    manifest {
        attributes["Main-Class"] = "net.sourceforge.plantuml.Run"
    }
}

//
// SOURCES JAR
//
tasks.named<Jar>("sourcesJar") {
    dependsOn(mitProject.tasks.named("sourcesJar"))
    dependsOn(mitProject.tasks.named("classes"))

    from(mitMain.allSource) {
        include("**/*.java")
        exclude(lightExcludes)
    }
}

//
// JAVADOC JAR
//
tasks.named<Jar>("javadocJar") {
    dependsOn(mitJavadocJar)

    // Unpack the MIT javadoc jar (copying the file itself would nest a jar in the jar)
    from({ zipTree(mitJavadocJar.get().archiveFile) }) {
        exclude("META-INF/MANIFEST.MF")
    }
}

//
// PUBLISHING
// POM, CentralPortal repository and signing come from the plantuml.publishing
// convention plugin (license-variants/build-logic); only the content of the
// publication is specific here.
//
plantumlPublishing {
    pomName.set("PlantUML MIT Light")
    pomDescription.set("Filtered MIT distribution of PlantUML.")
    pomLicenseName.set("MIT License")
    pomLicenseUrl.set("https://opensource.org/license/mit/")
    developer("nicolas.baumann", "Nicolas Baumann", "nicolas.baumann1@gmail.com")
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifact(tasks.named("jar"))
        artifact(tasks.named("sourcesJar"))
        artifact(tasks.named("javadocJar"))
    }
}
