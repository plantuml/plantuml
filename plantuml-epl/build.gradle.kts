import org.apache.tools.ant.taskdefs.condition.Os

//    permits to start the build setting the javac release parameter, no parameter means build for java8:
// gradle clean build -x javaDoc -PjavacRelease=8
// gradle clean build -x javaDoc -PjavacRelease=17
//    also supported is to build first, with java17, then switch the java version, and run the test with java8:
// gradle clean build -x javaDoc -x test
// gradle test
val javacRelease = (project.findProperty("javacRelease") ?: "8") as String

val versionRegex = "\\d+\\.\\d+\\.\\d+".toRegex()
val simpleVersion = if (versionRegex.containsMatchIn(project.version.toString()))
		versionRegex.find(project.version.toString())?.value
	else
		project.version

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
	registerFeature("pdf") {
		usingSourceSet(sourceSets["main"])
	}
}

dependencies {
	compileOnly(libs.ant)
	testImplementation(libs.assertj.core)
	testImplementation(libs.junit.jupiter)
	testImplementation(libs.jlatexmath)
	testImplementation(libs.xmlunit.core)

	implementation(libs.jlatexmath)
	
    implementation(libs.elk.core)
    implementation(libs.elk.alg.layered)
    implementation(libs.elk.alg.mrtree)

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
			srcDirs("../src/main/resources")
        }
	}
}

tasks.processResources {
    from("../src/main/java") {
        include("**/graphviz.dat", "**/*.svg", "**/*.png", "**/*.txt")
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
		from("../manifest.txt")
	}

	// Add dependencies to the JAR
    val runtimeClasspath = configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    from(runtimeClasspath) {
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA") // Avoid conflict on signature
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
				"define" to "__EPL__",
				"header" to project.layout.buildDirectory.file("../epl-license.txt").get().asFile.absolutePath
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
					name.set("EPL License")
					url.set("https://opensource.org/license/epl-1-0/")
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

val deleteObsoleteLibsTask = tasks.register<Delete>("clear-eclipse-plugin-lib-dir") {
	delete(fileTree("../plantuml-eclipse/plugin/lib").include("*.jar"))
}

val copyLibsTask = tasks.register<Copy>("copy-libs-to-eclipse-plugin") {
	dependsOn("jar")
	dependsOn("sourcesJar")
	dependsOn(deleteObsoleteLibsTask)

	from("build/libs")
	into("../plantuml-eclipse/plugin/lib")
	include("*.jar")
	exclude("*-javadoc.jar")
}

val updateVersionsInManifestTask = tasks.register<Copy>("update-versions-in-manifest") {
	from("../plantuml-eclipse/plugin/META-INF") {
		include("MANIFEST.MF")
		filter { line: String ->
			if (line.startsWith("Bundle-Version:")) {
				"Bundle-Version: $simpleVersion.qualifier"
			}
			else if (line.startsWith("Bundle-ClassPath: lib/plantuml-epl-")) {
				"Bundle-ClassPath: lib/plantuml-epl-${project.version}.jar"
			}
			else line
		}
	}
	into("build/eclipse-files/plugin/META-INF")
	filteringCharset = "UTF-8"
}

val updateVersionsInClasspathTask = tasks.register<Copy>("update-versions-in-classpath") {
	val linePrefix = "<classpathentry exported=\"true\" kind=\"lib\" path=\"lib/plantuml-epl-"

	from("../plantuml-eclipse/plugin") {
		include(".classpath")
		filter { line: String ->
			if (line.contains(linePrefix, false)) {
				val start = line.indexOf(linePrefix) + linePrefix.length
				val end = line.indexOf(".jar\"")
				line.substring(0, start) + project.version + line.substring(end)
			}
			else line
		}
	}

	from("../plantuml-eclipse/plugin") {
		include("build.properties")
		filter { line: String ->
			if (line.startsWith("bin.includes = lib/plantuml-epl-")) {
				"bin.includes = lib/plantuml-epl-${project.version}.jar,\\"
			}
			else if (line.startsWith("src.includes = lib/plantuml-epl-")) {
				"src.includes = lib/plantuml-epl-${project.version}-sources.jar,\\"
			}
			else line
		}
	}
	into("build/eclipse-files/plugin")
	filteringCharset = "UTF-8"
}

val updateVersionInFeatureTask = tasks.register<Copy>("update-version-in-feature") {
	from("../plantuml-eclipse/feature") {
		include("feature.xml")
		filter { line: String ->
			if (line.trim().startsWith("version=\"") && line.endsWith(".qualifier\"")) {
				line.substring(0, line.indexOf("\"") + 1) + simpleVersion + line.substring(line.indexOf(".qualifier"))
			}
			else line
		}
	}
	into("build/eclipse-files/feature")
	filteringCharset = "UTF-8"
}

val updateVersionInParentPomTask = tasks.register<Copy>("update-version-in-pom") {
	val startTag = "<plantuml-lib-version>"
	val endTag = "</plantuml-lib-version>"

	from("../plantuml-eclipse") {
		include("pom.xml")
		filter { line: String ->
			if (line.trim().startsWith(startTag) && line.endsWith(endTag)) {
				line.substring(0, line.indexOf(startTag) + startTag.length) + simpleVersion + line.substring(line.indexOf(endTag))
			}
			else line
		}
	}
	into("build/eclipse-files")
	filteringCharset = "UTF-8"
}

val updateVersionsInEclipseProjectsTask = tasks.register<Copy>("update-versions-in-eclipse-projects") {
	dependsOn(updateVersionsInManifestTask)
	dependsOn(updateVersionsInClasspathTask)
	dependsOn(updateVersionInFeatureTask)
	dependsOn(updateVersionInParentPomTask)

	from("build/eclipse-files")
	into("../plantuml-eclipse")
	filteringCharset = "UTF-8"
}

val mvnCmd = if (Os.isFamily(Os.FAMILY_WINDOWS)) { "mvn.cmd" } else { "mvn"}

val checkMavenTask = tasks.register<Exec>("check-maven-version") {
	commandLine = listOf(mvnCmd, "-version")
}

val buildEclipseUpdateSiteTask = tasks.register<Exec>("build-Eclipse-update-site") {
	dependsOn(checkMavenTask)
	dependsOn(copyLibsTask)
	dependsOn(updateVersionsInEclipseProjectsTask)

	workingDir = file("../plantuml-eclipse").absoluteFile
	commandLine = listOf(mvnCmd, "clean", "package")
}

tasks.named("build"){
	finalizedBy(buildEclipseUpdateSiteTask)
}