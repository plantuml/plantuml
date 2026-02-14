//    permits to start the build setting the javac release parameter, no parameter means build for java8:
// gradle clean build -x javaDoc -PjavacRelease=8
// gradle clean build -x javaDoc -PjavacRelease=17
//    also supported is to build first, with java17, then switch the java version, and run the test with java8:
// gradle clean build -x javaDoc -x test
// gradle test

import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.jar.JarFile
import java.util.Base64
import java.util.Properties
import org.gradle.api.tasks.Sync

println("Running build.gradle.kts")
println(project.version)

val javacRelease = (project.findProperty("javacRelease") ?: "8") as String

plugins {
	java
	`maven-publish`
	signing
    eclipse
	jacoco
	id("com.gorylenko.gradle-git-properties") version "2.5.7"
//	alias(libs.plugins.adarshr.test.logger)
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

sourceSets {
	create("teavm") {
		java.srcDir(layout.buildDirectory.dir("generated/teavm-sjpp"))
		// If resources are needed at TeaVM runtime, you can also add them:
		resources.srcDir("src/main/resources")
		// Note: compileClasspath will be configured after dependencies are declared
	}
}

val jdependConfig by configurations.creating
val teavmConfig by configurations.creating

// Separate configuration for TeaVM compile dependencies (requires Java 11+)
val teavmCompileConfig by configurations.creating {
	attributes {
		// Force Java 11 compatibility for TeaVM dependencies
		attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 11)
	}
}


dependencies {
	compileOnly(libs.ant)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

	testImplementation(libs.glytching.junit.extensions)
	testImplementation(libs.assertj.core)
	testImplementation(libs.xmlunit.core)
	if (JavaVersion.current().isJava8) {
		testImplementation(libs.mockito.core.j8)
		testImplementation(libs.mockito.junit.jupiter.j8)
	} else {
		testImplementation(libs.mockito.core)
		testImplementation(libs.mockito.junit.jupiter)
		testImplementation(libs.junit.pioneer)
	}
	implementation(libs.jlatexmath)
    implementation(libs.elk.core)
    implementation(libs.elk.alg.layered)
    implementation(libs.elk.alg.mrtree)

	// JDepend for package metrics
	jdependConfig(libs.jdepend)

	// TeaVM CLI for compilation (contains the main class)
    teavmConfig(libs.teavm.cli)
	
	// TeaVM dependencies for Java to JavaScript compilation (Java 11+ only)
	teavmCompileConfig(libs.teavm.jso.apis)
	teavmCompileConfig(libs.teavm.jso)

    // Custom configuration for pdfJar task
    configurations.create("pdfJarDeps")
    "pdfJarDeps"(libs.fop)
    "pdfJarDeps"(libs.batik.all)

}

repositories {
	mavenLocal()
	mavenCentral()
}

// Configure teavm sourceSet classpath after dependencies are declared
// Note: teavm sourceSet compiles preprocessed sources from generated/teavm-sjpp
// It does NOT need the main sourceSet output (which would trigger unnecessary compilation)
sourceSets["teavm"].apply {
	compileClasspath = teavmCompileConfig
	runtimeClasspath = teavmCompileConfig
}

tasks.compileJava {
	if (JavaVersion.current().isJava8) {
		java.targetCompatibility = JavaVersion.VERSION_1_8
	} else {
		options.release.set(Integer.parseInt(javacRelease))
	}
}

tasks.named<JavaCompile>("compileTeavmJava") {
	// TeaVM requires Java 11 minimum, regardless of the main javacRelease setting
	options.release.set(11)
	dependsOn("preprocessForTeaVM")
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes["Main-Class"] = "net.sourceforge.plantuml.Run"
        attributes["Implementation-Version"] = archiveVersion
        attributes["Build-Jdk-Spec"] = System.getProperty("java.specification.version")
        from("manifest.txt")
    }

    // Add dependencies to the JAR
    val runtimeClasspath = configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    from(runtimeClasspath) {
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA") // Avoid conflict on signature
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}



val checkJarEntries by tasks.registering {
    dependsOn(tasks.named("jar"))
    doLast {
        val jarFile = tasks.named<Jar>("jar").get().archiveFile.get().asFile
        JarFile(jarFile).use { jar ->
            val required = listOf(
                "net/sourceforge/plantuml/Run.class",
                "sprites/archimate/access.png",
                "skin/plantuml.skin"
            )
            val missing = required.filter { jar.getEntry(it) == null }
            if (missing.isNotEmpty()) {
                throw GradleException("Missing entries in JAR: $missing")
            }
        }
        println("All required entries found in ${jarFile.name}")
    }
}

tasks.named<Jar>("jar") {
    finalizedBy(checkJarEntries)
}

tasks.named("check") {
    dependsOn(checkJarEntries)
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
					name.set("The GNU General Public License")
					url.set("http://www.gnu.org/licenses/gpl.txt")
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

tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"
}

tasks.withType<Javadoc>().configureEach {
	options {
		this as StandardJavadocDocletOptions
		addBooleanOption("Xdoclint:none", true)
		addStringOption("Xmaxwarns", "50")
		encoding = "UTF-8"
		isUse = true
	}
}

tasks.test {
	doFirst {
		println("Java Home:" + System.getProperty("java.home"));
		println("Java Version: " + System.getProperty("java.version"));
	}
	useJUnitPlatform()
	testLogging.showStandardStreams = true
}

tasks.register<Test>("runIntermediateTest") {
    description = "Runs the 'IntermediateTest'"
    group = "dev"
    useJUnitPlatform()
    filter {
        includeTestsMatching("IntermediateTest*")
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    val mainClasses = sourceSets.main.get().output.classesDirs

    classDirectories.setFrom(
        files(mainClasses).asFileTree.matching {
            include("**/*.class")
        }
    )

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }
}

tasks.register<JavaExec>("jdepend") {
    description = "Generates JDepend package dependency reports (text and HTML)"
    group = "documentation"
    
    dependsOn(tasks.classes)
    
    mainClass.set("jdepend.textui.JDepend")
    classpath = jdependConfig
    
    val outputDir = layout.buildDirectory.dir("reports/jdepend").get().asFile
    val textReport = file("$outputDir/jdepend-report.txt")
    
    doFirst {
        outputDir.mkdirs()
    }
    
    args = listOf(
        "-file", textReport.absolutePath,
        sourceSets.main.get().output.classesDirs.asPath
    )
    
    // Filter noisy stderr from old JDepend ("Unknown constant: 18" warnings)
    val rawErr = ByteArrayOutputStream()
    errorOutput = rawErr
    
    doLast {
        val filtered = rawErr.toString(Charsets.UTF_8).lineSequence()
            .filterNot { it.trim().startsWith("Unknown constant:") }
            .joinToString("\n")
            .trim()
        if (filtered.isNotEmpty()) {
            logger.warn(filtered)
        }
        println("JDepend reports generated:")
        println("  Text: ${textReport.absolutePath}")
    }
}

abstract class JdependHtmlTask @Inject constructor(
    private val execOperations: ExecOperations
) : DefaultTask() {
    
    @get:InputFiles
    abstract val classesDir: ConfigurableFileCollection
    
    @get:InputFiles
    abstract val jdependClasspath: ConfigurableFileCollection
    
    @get:InputFile
    abstract val xsltFile: RegularFileProperty
    
    @get:OutputFile
    abstract val htmlReport: RegularFileProperty
    
    @get:OutputFile
    abstract val xmlReport: RegularFileProperty
    
    @TaskAction
    fun generate() {
        htmlReport.get().asFile.parentFile.mkdirs()
        
        // Generate XML report (with stderr filtering for "Unknown constant:" warnings)
        val rawErr = ByteArrayOutputStream()
        execOperations.javaexec {
            mainClass.set("jdepend.xmlui.JDepend")
            classpath = jdependClasspath
            args("-file", xmlReport.get().asFile.absolutePath, classesDir.asPath)
            errorOutput = rawErr
        }
        val filtered = rawErr.toString(Charsets.UTF_8).lineSequence()
            .filterNot { it.trim().startsWith("Unknown constant:") }
            .joinToString("\n")
            .trim()
        if (filtered.isNotEmpty()) {
            logger.warn(filtered)
        }
        
        // Convert XML to HTML using XSLT
        if (xmlReport.get().asFile.exists()) {
            val factory = javax.xml.transform.TransformerFactory.newInstance()
            val transformer = factory.newTransformer(
                javax.xml.transform.stream.StreamSource(xsltFile.get().asFile)
            )
            transformer.transform(
                javax.xml.transform.stream.StreamSource(xmlReport.get().asFile),
                javax.xml.transform.stream.StreamResult(htmlReport.get().asFile)
            )
            
            println("JDepend reports generated:")
            println("  HTML: ${htmlReport.get().asFile.absolutePath}")
            println("  XML:  ${xmlReport.get().asFile.absolutePath}")
        }
    }
}

tasks.register<JdependHtmlTask>("jdependHtml") {
    description = "Generates JDepend HTML report"
    group = "documentation"
    
    dependsOn(tasks.classes)
    
    val outputDir = layout.buildDirectory.dir("reports/jdepend").get().asFile
    classesDir.from(sourceSets.main.get().output.classesDirs)
    jdependClasspath.from(jdependConfig)
    xsltFile.set(file("tools/jdepend-report.xsl"))
    htmlReport.set(file("$outputDir/jdepend-report.html"))
    xmlReport.set(file("$outputDir/jdepend-report.xml"))
}


val pdfJar by tasks.registering(Jar::class) {
	group = "build" // OR for example, "build"
	description = "Assembles a jar containing dependencies to create PDFs."
	manifest.attributes["Main-Class"] = "net.sourceforge.plantuml.Run"
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  val dependencies = configurations["pdfJarDeps"].map(::zipTree) + configurations.runtimeClasspath.get().map(::zipTree)
	from(dependencies) {
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA") // Avoid conflict on signature
    }
	with(tasks.jar.get())
	archiveAppendix.set("pdf")
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
		sign(closureOf<SignOperation> { sign(pdfJar.get()) })
	}
}

// Site generation task - creates a comprehensive project site
tasks.register("site") {
	description = "Generates project site with documentation, reports, test results and demo."
	group = "documentation"
	
	val siteDir = layout.buildDirectory.dir("site").get().asFile
	
	dependsOn(
		// Doc.
		tasks.javadoc,
		tasks.test,
		tasks.jacocoTestReport,
		"jdepend",
		"jdependHtml",
		// Demo.
		"teavm"
	)
	
	doLast {
		println("[SITE] Starting site generation...")
		println("[SITE] siteDir = ${siteDir.absolutePath}")
		siteDir.mkdirs()

		// Check TeaVM output
		val teavmDir = layout.buildDirectory.dir("teavm/js").get().asFile
		println("[SITE] teavmDir = ${teavmDir.absolutePath}")
		println("[SITE] teavmDir.exists() = ${teavmDir.exists()}")
		if (teavmDir.exists()) {
			println("[SITE] teavmDir contents:")
			teavmDir.listFiles()?.forEach { println("[SITE]   - ${it.name} (${it.length()} bytes)") }
		} else {
			println("[SITE] WARNING: teavmDir does not exist!")
		}

		// Generate timestamp
		val timestamp = LocalDateTime.now().toString().replace('T', ' ').substring(0, 19)

		// Read template and perform substitutions
		val templateFile = file("site/index.template.html")
		var htmlContent = templateFile.readText()
		
		htmlContent = htmlContent
			.replace("{{VERSION}}", project.version.toString())
			.replace("{{TIMESTAMP}}", timestamp)
			.replace("{{JAVA_RELEASE}}", javacRelease)
			.replace("{{GROUP}}", project.group.toString())
			.replace("{{DESCRIPTION}}", project.description ?: "")
			.replace("{{GRADLE_VERSION}}", gradle.gradleVersion)

		// Write the index.html file
		val indexHtml = file("$siteDir/index.html")
		indexHtml.writeText(htmlContent)
		
		// Copy CSS file to site directory
		copy {
			from("site/css/main-site.css")
			into("$siteDir/css")
		}

		// Copy all reports to site directory
		copy {
			from(layout.buildDirectory.dir("docs/javadoc"))
			into("$siteDir/javadoc")
		}
		
		copy {
			from(layout.buildDirectory.dir("reports/tests"))
			into("$siteDir/tests")
		}
		
		copy {
			from(layout.buildDirectory.dir("reports/jacoco"))
			into("$siteDir/jacoco")
		}
		
		copy {
			from(layout.buildDirectory.dir("reports/jdepend"))
			into("$siteDir/jdepend")
		}

		println("[SITE] Copying teavm/js to js-plantuml...")
		copy {
			from(layout.buildDirectory.dir("teavm/js"))
			into("$siteDir/js-plantuml")
		}
		
		// Verify copy result
		val jsPlantUmlDir = file("$siteDir/js-plantuml")
		println("[SITE] js-plantuml dir exists: ${jsPlantUmlDir.exists()}")
		if (jsPlantUmlDir.exists()) {
			println("[SITE] js-plantuml contents:")
			jsPlantUmlDir.listFiles()?.forEach { println("[SITE]   - ${it.name} (${it.length()} bytes)") }
		}
		
		// Check for classes.js specifically
		val classesJs = file("$siteDir/js-plantuml/classes.js")
		println("[SITE] classes.js exists: ${classesJs.exists()}")
		if (classesJs.exists()) {
			println("[SITE] classes.js size: ${classesJs.length()} bytes")
		}
	
		println("========================================")
		println("Project site generated successfully!")
		println("========================================")
		println("Location: ${siteDir.absolutePath}")
		println("Open: ${siteDir.absolutePath}/index.html")
		println("========================================")
		println("Contents:")
		println("  - API Documentation (Javadoc)")
		println("  - Test Results Report")
		println("  - Code Coverage Report (JaCoCo)")
		println("  - Package Dependencies (JDepend)")
		println("  - JavaScript Demo (js-plantuml)")
		println("========================================")
	}
}

// ============================================
// CompilationInfo - Git commit & timestamp injection
// ============================================

gitProperties {
	dateFormat = "yyyy-MM-dd'T'HH:mm:ssX"
}

tasks.named("generateGitProperties") {
	doLast {
		val propsFile = layout.buildDirectory.file("resources/main/git.properties").get().asFile
		if (propsFile.exists()) {
			println("----- git.properties -----")
			propsFile.readLines()
				.sorted()
				.forEach { println(it) }
			println("--------------------------")
		} else {
			println("git.properties not found")
		}
	}
}

val filteredSrcDir = layout.buildDirectory.dir("generated/sources/git-filtered")

val filterSourcesWithBuildInfo by tasks.registering {
	dependsOn("generateGitProperties")
	mustRunAfter("processResources")

	inputs.dir("src/main/java")
	outputs.dir(filteredSrcDir)

	doLast {
		// 1) Read git.properties
		val propsFile = layout.buildDirectory.file("resources/main/git.properties").get().asFile
		val props = Properties().apply { propsFile.inputStream().use { load(it) } }
		val commitId = props.getProperty("git.commit.id.abbrev")
			?: error("git.commit.id.abbrev not found in ${propsFile.absolutePath}")

		// 2) Compute compile timestamp (epoch millis)
		val compileTs = System.currentTimeMillis().toString()

		// 3) Copy sources
		val outDir = filteredSrcDir.get().asFile
		outDir.deleteRecursively()
		project.copy {
			from("src/main/java")
			into(outDir)
		}

		// 4) Ant replace in the copy
		val targetFile = outDir.resolve("net/sourceforge/plantuml/version/CompilationInfo.java")
		if (!targetFile.exists()) {
			error("Target file not found: ${targetFile.absolutePath}")
		}

		// 4) Get project version
		val projectVersion = project.version.toString()

		ant.withGroovyBuilder {
			// version replacement
			"replace"(
				"file" to targetFile.absolutePath,
				"token" to "\$version\$",
				"value" to projectVersion
			)

			// commit token replacement
			"replace"(
				"file" to targetFile.absolutePath,
				"token" to "\$git.commit.id\$",
				"value" to commitId
			)

			// timestamp replacement
			"replace"(
				"file" to targetFile.absolutePath,
				"token" to "COMPILE_TIMESTAMP = 000L",
				"value" to "COMPILE_TIMESTAMP = ${compileTs}L"
			)
		}

		println("Injected version into ${targetFile.name}: $projectVersion")
		println("Injected git.commit.id into ${targetFile.name}: $commitId")
		println("Injected compile timestamp into ${targetFile.name}: $compileTs")
	}
}

sourceSets.named("main") {
	java.setSrcDirs(listOf(filteredSrcDir))
}

tasks.compileJava {
	dependsOn(filterSourcesWithBuildInfo)
}

tasks.named("sourcesJar") {
	dependsOn(filterSourcesWithBuildInfo)
}

// ============================================
// TeaVM Configuration - Java to JavaScript
// ============================================

// Sync sources for TeaVM preprocessing (use filtered sources with CompilationInfo injected)
val syncSourcesForTeaVM by tasks.registering(Sync::class) {
	dependsOn(filterSourcesWithBuildInfo)
	from(filteredSrcDir)
	into(layout.buildDirectory.dir("sources/teavm-sjpp/java"))
}

// Preprocess sources with SJPP for TeaVM
val preprocessForTeaVM by tasks.registering {
	dependsOn(syncSourcesForTeaVM)

	inputs.dir(layout.buildDirectory.dir("sources/teavm-sjpp/java"))
	outputs.dir(layout.buildDirectory.dir("generated/teavm-sjpp"))

	doLast {
		ant.withGroovyBuilder {
			"taskdef"(
				"name" to "sjpp",
				"classname" to "sjpp.SjppAntTask",
				"classpath" to rootProject.layout.projectDirectory.files("sjpp.jar").asPath
			)
			"sjpp"(
				"src" to layout.buildDirectory.dir("sources/teavm-sjpp/java").get().asFile.absolutePath,
				"dest" to layout.buildDirectory.dir("generated/teavm-sjpp").get().asFile.absolutePath,
				"define" to "__TEAVM__"
			)
		}
	}
}

// Generate embedded resources for TeaVM (Base64 encoded)
val generateTeavmEmbeddedResources by tasks.registering {
	dependsOn(preprocessForTeaVM)

	val outDir = layout.buildDirectory.dir("generated/teavm-sjpp/net/sourceforge/plantuml/teavm")
	inputs.file("src/main/resources/skin/plantuml.skin")
	outputs.dir(outDir)

	doLast {
		val skinFile = file("src/main/resources/skin/plantuml.skin")
		val bytes = skinFile.readBytes()
		val b64 = Base64.getEncoder().encodeToString(bytes)

		// Split into lines to avoid a giant single line
		val chunks = b64.chunked(120).joinToString(separator = "\" +\n            \"", prefix = "\"", postfix = "\"")

		val target = outDir.get().file("EmbeddedResources.java").asFile
		target.parentFile.mkdirs()
		target.writeText(
			"""
			package net.sourceforge.plantuml.teavm;

			import java.io.ByteArrayInputStream;
			import java.io.InputStream;
			import java.util.Base64;

			public final class EmbeddedResources {
				private EmbeddedResources() {
				}

				private static final String PLANTUML_SKIN_B64 =
						$chunks;

				public static InputStream openPlantumlSkin() {
					byte[] data = Base64.getDecoder().decode(PLANTUML_SKIN_B64);
					return new ByteArrayInputStream(data);
				}
			}
			""".trimIndent(),
			Charsets.UTF_8
		)
	}
}

tasks.named<JavaCompile>("compileTeavmJava") {
	dependsOn(generateTeavmEmbeddedResources)
}

// Task to compile Java to JavaScript using TeaVM
tasks.register<JavaExec>("generateJavaScript") {
	description = "Compiles Java to JavaScript using TeaVM"
	group = "teavm"
	
	// 1) preprocess -> 2) compile the preprocessed sources -> 3) TeaVM
	dependsOn(preprocessForTeaVM)
	dependsOn(tasks.named("compileTeavmJava"))
	
	mainClass.set("org.teavm.cli.TeaVMRunner")
	
	// TeaVMRunner + compiled classes from the teavm sourceSet
	classpath = teavmConfig + sourceSets["teavm"].output
	
	val outputDir = layout.buildDirectory.dir("teavm/js").get().asFile
	
	args(
		"-d", outputDir.absolutePath,
		"-t", "javascript",
//		"-G",  // Generate source maps
//		"-g",  // Generate debug information
		"net.sourceforge.plantuml.teavm.browser.PlantUMLBrowser"  // Main class as positional argument
	)
	
	doFirst {
		outputDir.mkdirs()
		println("Compiling Java to JavaScript with TeaVM (preprocessed __TEAVM__) ...")
		println("Output directory: ${outputDir.absolutePath}")
	}
	
	doLast {
		println("[TEAVM] JavaScript generation complete!")
		println("[TEAVM] Checking output directory: ${outputDir.absolutePath}")
		println("[TEAVM] outputDir.exists() = ${outputDir.exists()}")
		if (outputDir.exists()) {
			println("[TEAVM] outputDir contents:")
			outputDir.listFiles()?.forEach { println("[TEAVM]   - ${it.name} (${it.length()} bytes)") }
		}
		val classesJs = file("${outputDir.absolutePath}/classes.js")
		println("[TEAVM] classes.js exists: ${classesJs.exists()}")
		if (!classesJs.exists()) {
			println("[TEAVM] WARNING: classes.js was NOT generated!")
		}
	}
}

// Custom task to build TeaVM JS version with HTML file
tasks.register("teavm") {
	description = "Prepares TeaVM JS version with HTML file"
	group = "teavm"
	
	dependsOn("generateJavaScript")
	
	val outputDir = layout.buildDirectory.dir("teavm/js").get().asFile
	
	doLast {
		// Copy the HTML template and Viz.js library (without erasing existing files)
		copy {
			from("src/main/resources/teavm/index.html")
			from("src/main/resources/teavm/viz-global.js")
			into(outputDir)
		}
		
		println("")
		println("======================")
		println("TeaVM Ready!  --> ${outputDir.absolutePath}/index.html")
		println("======================")
		println("")
	}
}

// Task to create a ZIP archive of the TeaVM JS version
tasks.register<Zip>("teavmZip") {
	description = "Creates a ZIP archive of the TeaVM JS version"
	group = "teavm"
	
	dependsOn("teavm")
	
	// Use lazy evaluation to ensure files are read after teavm task completes
	from(layout.buildDirectory.dir("teavm/js")) {
		include("classes.js", "index.html", "viz-global.js")
	}
	
	destinationDirectory.set(layout.buildDirectory.dir("libs"))
	archiveBaseName.set("js-plantuml")
	archiveVersion.set(project.version.toString())
	
	doLast {
		println("")
		println("======================")
		println("TeaVM ZIP created: ${archiveFile.get().asFile.absolutePath}")
		println("======================")
		println("")
	}
}
