// Remove-Item -Recurse -Force */build

import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.jar.JarFile
import java.util.Base64
import java.util.Properties

println("Running build.gradle.kts")
println(project.version)

val javacRelease = (project.findProperty("javacRelease") ?: "11") as String

// When -Pfast is set, skip expensive steps (tests, javadoc, etc.)
val fastBuild = project.hasProperty("fast")

plugins {
	java
	`maven-publish`
	signing
	eclipse
	jacoco
	alias(libs.plugins.gorylenko.gradle.git.properties)
//	alias(libs.plugins.adarshr.test.logger)
	alias(libs.plugins.teavm)
}

group = "net.sourceforge.plantuml"
description = "PlantUML"

java {
    if (!fastBuild) {
        withSourcesJar()
        withJavadocJar()
    }
}

val jdependConfig by configurations.creating

dependencies {
	compileOnly(libs.ant)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

	testImplementation(libs.glytching.junit.extensions)
	testImplementation(libs.assertj.core)
	testImplementation(libs.xmlunit.core)
	testImplementation(libs.mockito.core)
	testImplementation(libs.mockito.junit.jupiter)
	testImplementation(libs.junit.pioneer)
	implementation(libs.jlatexmath)
    implementation(libs.elk.core)
    implementation(libs.elk.alg.layered)
    implementation(libs.elk.alg.mrtree)
	implementation(libs.openpdf)

	// JDepend for package metrics
	jdependConfig(libs.jdepend)

	// TeaVM JSO APIs for browser interop (provided to the teavm source set by the plugin)
	teavm(teavm.libs.jsoApis)

}

repositories {
	mavenLocal()
	mavenCentral()
}

// ============================================
// TeaVM Configuration - Java to JavaScript
// ============================================

teavm {
	js {
		mainClass.set("net.sourceforge.plantuml.teavm.browser.PlantUMLBrowser")
		moduleType.set(org.teavm.gradle.api.JSModuleType.ES2015)
		obfuscated.set(true)
		optimization.set(org.teavm.gradle.api.OptimizationLevel.BALANCED)
		// obfuscated.set(false)
		// optimization.set(org.teavm.gradle.api.OptimizationLevel.NONE)
		// outputDir defaults to build/generated/teavm/js
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
        from("manifest.txt")
    }

    // Add dependencies to the JAR
    val runtimeClasspath = configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    from(runtimeClasspath) {
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA") // Avoid conflict on signature
    }

    // Exclude TeaVM resources (JS stdlib, HTML demo pages) from the Java JAR
    exclude("teavm/**")

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
	jvmArgs("-ea")
	maxHeapSize = "1g"

	doLast {
		val vegaSummary = file("src/test/resources/vega/vega-summary.txt")
		if (vegaSummary.exists()) {
			println("")
			println(vegaSummary.readText().trim())
		}
	}

	val failedTests = mutableListOf<String>()
	val skippedTests = mutableListOf<String>()

	addTestListener(object : TestListener {
		override fun beforeSuite(suite: TestDescriptor) {}
		override fun afterSuite(suite: TestDescriptor, result: TestResult) {
			if (suite.parent == null) {
				val passed = result.successfulTestCount
				val failed = result.failedTestCount
				val skipped = result.skippedTestCount
				val total = result.testCount
				println("\n===========================================")
				println(" TEST SUMMARY")
				println("===========================================")
				println(" Total:    $total")
				println(" Passed:   $passed")
				println(" Failed:   $failed")
				println(" Skipped:  $skipped")
				println(" Result:   ${result.resultType}")
				println("===========================================")
				if (failedTests.isNotEmpty()) {
					println(" Failed tests:")
					for (name in failedTests)
						println("   - $name")
					println("===========================================")
				}
				if (skippedTests.isNotEmpty()) {
					println(" Skipped tests:")
					for (name in skippedTests)
						println("   - $name")
					println("===========================================")
				}
			}
		}
		override fun beforeTest(testDescriptor: TestDescriptor) {}
		override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
			val label = if (testDescriptor.displayName != testDescriptor.name)
				"${testDescriptor.className}.${testDescriptor.name} [${testDescriptor.displayName}]"
			else
				"${testDescriptor.className}.${testDescriptor.name}"
			if (result.resultType == TestResult.ResultType.FAILURE)
				failedTests.add(label)
			if (result.resultType == TestResult.ResultType.SKIPPED)
				skippedTests.add(label)
		}
	})
}

tasks.register<Test>("runIntermediateTest") {
    description = "Runs the 'IntermediateTest'"
    group = "dev"
    useJUnitPlatform()
    jvmArgs("-ea")
	testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    filter {
        includeTestsMatching("IntermediateTest*")
    }
}

tasks.register<Test>("runVegaTest") {
    description = "Runs the 'VegaTest'"
    group = "dev"
    useJUnitPlatform()
    jvmArgs("-ea")
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    filter {
        includeTestsMatching("VegaTest")
    }
	doLast {
		val vegaSummary = file("src/test/resources/vega/vega-summary.txt")
		if (vegaSummary.exists()) {
			println("")
			println(vegaSummary.readText().trim())
		}
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

    @get:OutputFile
    abstract val xmlReport: RegularFileProperty

    // XSLT -> HTML
	@get:InputFile
    abstract val xsltFile: RegularFileProperty

	// output.html
    @get:OutputFile
    abstract val htmlReport: RegularFileProperty

    // XSLT -> PUML
    @get:InputFile
    abstract val pumlXsltFile: RegularFileProperty

    // output.puml
    @get:OutputFile
    abstract val pumlReport: RegularFileProperty
    
    @TaskAction
    fun generate() {
        htmlReport.get().asFile.parentFile.mkdirs()
        pumlReport.get().asFile.parentFile.mkdirs()
        
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
        
        // Convert XML to HTML + PUML using XSLT
        if (xmlReport.get().asFile.exists()) {
            val factory = javax.xml.transform.TransformerFactory.newInstance()

            // XML -> HTML
            run {
                val transformer = factory.newTransformer(
                    javax.xml.transform.stream.StreamSource(xsltFile.get().asFile)
                )
                transformer.transform(
                    javax.xml.transform.stream.StreamSource(xmlReport.get().asFile),
                    javax.xml.transform.stream.StreamResult(htmlReport.get().asFile)
                )
            }

            // XML -> PUML
            run {
                val transformer = factory.newTransformer(
                    javax.xml.transform.stream.StreamSource(pumlXsltFile.get().asFile)
                )
                transformer.transform(
                    javax.xml.transform.stream.StreamSource(xmlReport.get().asFile),
                    javax.xml.transform.stream.StreamResult(pumlReport.get().asFile)
                )
            }
            
            println("JDepend reports generated:")
            println("  XML : ${xmlReport.get().asFile.absolutePath}")
            println("  HTML: ${htmlReport.get().asFile.absolutePath}")
            println("  PUML: ${pumlReport.get().asFile.absolutePath}")
        }
    }
}

tasks.register<JdependHtmlTask>("jdependHtml") {
    description = "Generates JDepend HTML/PUML report"
    group = "documentation"
    
    dependsOn(tasks.classes)
    
    val outputDir = layout.buildDirectory.dir("reports/jdepend").get().asFile
    classesDir.from(sourceSets.main.get().output.classesDirs)
    jdependClasspath.from(jdependConfig)
	xmlReport.set(file("$outputDir/jdepend-report.xml"))

    xsltFile.set(file("tools/jdepend-report.xsl"))
    htmlReport.set(file("$outputDir/jdepend-report.html"))
    
    pumlXsltFile.set(file("tools/jdepend2puml.xsl"))
    pumlReport.set(file("$outputDir/jdepend-report.puml"))
}

tasks.register<JavaExec>("renderJdependPuml") {
    description = "Renders jdepend-report.puml with PlantUML"
    group = "documentation"

    dependsOn("jdependHtml", tasks.classes)

    val pumlInput = layout.buildDirectory.file("reports/jdepend/jdepend-report.puml")
    inputs.file(pumlInput)

    val outDir = layout.buildDirectory.dir("reports/jdepend")
    outputs.dir(outDir)

    mainClass.set("net.sourceforge.plantuml.Run")
    classpath = sourceSets.main.get().runtimeClasspath

    args(
        "-tsvg",
        pumlInput.get().asFile.absolutePath
    )
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

// TeaVM output directory (set by the official plugin, defaults to build/generated/teavm)
val teavmJsOutputDir = layout.buildDirectory.dir("generated/teavm/js")

// Site assembly task - creates the site from pre-existing build outputs
// Use 'site' for a full build (including TeaVM), or 'siteAssemble' if
// TeaVM JS files are already present (e.g. from CI artifact)
tasks.register("siteAssemble") {
	description = "Assembles project site from pre-existing build outputs (no TeaVM build)."
	group = "documentation"
	
	val siteDir = layout.buildDirectory.dir("site").get().asFile
	
	dependsOn(
		// Doc.
		tasks.javadoc,
		tasks.test,
		tasks.jacocoTestReport,
		"jdepend",
		"jdependHtml"
		// "renderJdependPuml"
	)
	
	doLast {
		println("::group::[SITE]")
		println("[SITE] Starting site generation...")
		println("[SITE] siteDir = ${siteDir.absolutePath}")
		siteDir.mkdirs()

		// Check TeaVM output
		val teavmDir = teavmJsOutputDir.get().asFile
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
			from(layout.buildDirectory.dir("reports/problems"))
			into("$siteDir/problems")
		}
		
		copy {
			from(layout.buildDirectory.dir("reports/jacoco"))
			into("$siteDir/jacoco")
		}
		
		copy {
			from(layout.buildDirectory.dir("reports/jdepend"))
			into("$siteDir/jdepend")
		}

		println("[SITE] Copying TeaVM JS output to js-plantuml...")
		copy {
			from(teavmJsOutputDir)
			into("$siteDir/js-plantuml")
		}
		
		// Verify copy result
		val jsPlantUmlDir = file("$siteDir/js-plantuml")
		println("[SITE] js-plantuml dir exists: ${jsPlantUmlDir.exists()}")
		if (jsPlantUmlDir.exists()) {
			println("[SITE] js-plantuml contents:")
			jsPlantUmlDir.listFiles()?.forEach { println("[SITE]   - ${it.name} (${it.length()} bytes)") }
		}
		
		// Check for the generated JS file
		val plantumlJs = file("$siteDir/js-plantuml/plantuml.js")
		println("[SITE] plantuml.js exists: ${plantumlJs.exists()}")
		if (plantumlJs.exists()) {
			println("[SITE] plantuml.js size: ${plantumlJs.length()} bytes")
		}
		println("::endgroup::")

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

// Full site generation task (builds TeaVM + assembles site)
tasks.register("site") {
	description = "Generates complete project site including TeaVM JS build."
	group = "documentation"
	
	dependsOn("teavm", "siteAssemble")
}

// Ensure siteAssemble runs AFTER teavm so JS files are available for copying
tasks.named("siteAssemble") {
	mustRunAfter("teavm")
}

// ============================================
// CompilationInfo - Git commit & timestamp injection
// ============================================

// Resolve the git.properties produced by gradle-git-properties from the
// generateGitProperties task's own output, so downstream tasks read it from
// wherever the plugin writes it (no hard-coded path).
val gitPropertiesFile = tasks.named<com.gorylenko.GenerateGitPropertiesTask>("generateGitProperties")
	.flatMap { it.output }

gitProperties {
	dateFormat = "yyyy-MM-dd'T'HH:mm:ssX"
}

tasks.named("generateGitProperties") {
	doLast {
		val propsFile = gitPropertiesFile.get().asFile
		if (propsFile.exists()) {
      println("::group::[Git Properties]")
			println("----- git.properties -----")
			propsFile.readLines()
				.sorted()
				.forEach { println(it) }
			println("--------------------------")
      println("::endgroup::")
		} else {
			println("git.properties not found")
      println("::warning file=git.properties,title=File not found::")
		}
	}
}

// `patchCompilationInfo` injects build metadata (version, git commit id, compile timestamp)
// directly into src/main/java/.../CompilationInfo.java.
//
// IMPORTANT: this task is intentionally NOT wired as a dependency of `compileJava`,
// `jar` or any other build task. It must be invoked EXPLICITLY (typically from CI,
// before `gradle build` / `gradle teavm`).
//
// Rationale: keeping the main source set pointing to plain `src/main/java` (no
// generated/filtered copy) avoids a lot of IDE-side problems for contributors.
// As a side effect, local builds produce a jar in which `CompilationInfo` keeps
// its placeholder values ("$version$", "$git.commit.id$", 0L). This is fine for
// dev builds; official artifacts are built by GitHub Actions, which calls
// `patchCompilationInfo` first.
//
// The task modifies a tracked source file in place. After running it, the working
// tree contains uncommitted changes -- this is expected in CI (ephemeral runner),
// but if you ever need to run it locally, remember to `git restore` the file
// afterwards, or use `-PpatchCompilationInfo` workflows carefully.
val patchCompilationInfo by tasks.registering {
	group = "build"
	description = "Patches src/main/java/.../CompilationInfo.java in place with version, git commit id and compile timestamp. NOT wired automatically -- call explicitly (typically from CI)."

	dependsOn("generateGitProperties")

	doLast {
		// 1) Read git.properties
		val propsFile = gitPropertiesFile.get().asFile
		val props = Properties().apply { propsFile.inputStream().use { load(it) } }
		val commitId = props.getProperty("git.commit.id.abbrev")
			?: error("git.commit.id.abbrev not found in ${propsFile.absolutePath}")

		// 2) Compute compile timestamp (epoch millis)
		val compileTs = System.currentTimeMillis().toString()

		// 3) Locate the target file directly in src/main/java
		val targetFile = file("src/main/java/net/sourceforge/plantuml/version/CompilationInfo.java")
		if (!targetFile.exists()) {
			error("Target file not found: ${targetFile.absolutePath}")
		}

		// 4) Get project version
		val projectVersion = project.version.toString()

		// 5) Ant replace in place
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

		println("Patched version into ${targetFile.name}: $projectVersion")
		println("Patched git.commit.id into ${targetFile.name}: $commitId")
		println("Patched compile timestamp into ${targetFile.name}: $compileTs")
	}
}

// ============================================
// TeaVM tasks - build JS version with HTML file
// ============================================

// Custom task to prepare TeaVM JS version with HTML file
tasks.register("teavm") {
	description = "Prepares TeaVM JS version with HTML file"
	group = "teavm"
	
	// The official plugin provides the 'generateJavaScript' task
	dependsOn("generateJavaScript")
	
	val outputDir = teavmJsOutputDir.get().asFile
	
	doLast {
		// Copy the HTML template and other resources (without erasing existing files)
		copy {
			from("src/main/resources/teavm")
			into(outputDir)
		}

		// List all produced .js files with human-readable sizes
		fun sizeInMB(bytes: Long): String {
			return "%.2f MB".format(bytes.toDouble() / (1024.0 * 1024.0))
		}

		val jsFiles = outputDir.listFiles { f -> f.isFile && f.name.endsWith(".js") }
			?.sortedBy { it.name.lowercase() }
		if (jsFiles != null && jsFiles.isNotEmpty()) {
			val nameWidth = maxOf(jsFiles.maxOf { it.name.length }, "TOTAL".length)
			val totalSize = jsFiles.sumOf { it.length() }
			val sizeWidth = sizeInMB(totalSize).length

			println("")
			println("TeaVM JS files:")
			for (f in jsFiles) {
				println("  %-${nameWidth}s  %${sizeWidth}s".format(f.name, sizeInMB(f.length())))
			}
			println("  %-${nameWidth}s  %${sizeWidth}s".format("TOTAL", sizeInMB(totalSize)))
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
	from(teavmJsOutputDir) {
		include("*.js", "*.html", "*.css", "*.svg", "*.ico")
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

// ============================================
// npm package - assemble a publishable npm package for the JS engine
// ============================================
//
// Produces build/npm-plantuml/, a self-contained npm package exposing the
// TeaVM-compiled PlantUML engine (issue #2715: publish js-plantuml on npm so
// it can be imported from a CDN such as unpkg / jsdelivr).
//
// The package ships the engine (plantuml.js), the Graphviz layout engine
// (viz-global.js) and the demo/playground pages, but NOT the heavy optional
// stdlib sprite bundles (ibm/tupadr3/material*/awslib*...), which would push
// the tarball above 100 MB. Those remain available from the project site.
//
// Usage:
//   gradlew npmPackage           # assemble build/npm-plantuml
//   cd build/npm-plantuml
//   npm publish --access public  # done manually by the maintainer
//
tasks.register("npmPackage") {
	description = "Assembles a publishable npm package for the TeaVM JS engine (issue #2715)."
	group = "teavm"

	dependsOn("teavm")

	val outputDir = layout.buildDirectory.dir("npm-plantuml")

	doLast {
		val pkgDir = outputDir.get().asFile
		pkgDir.deleteRecursively()
		pkgDir.mkdirs()

		// Determine the npm version (must be valid semver).
		//
		// By default it is derived from the Gradle project version
		// (e.g. "1.2026.5beta1" -> "1.2026.5-beta.1"). This can be overridden
		// with -PnpmVersion=... so the npm package can be released as stable
		// (e.g. "1.2026.5") even while the project itself is still a beta:
		//
		//   gradlew npmPackage -PnpmVersion=1.2026.5
		//
		val gradleVersion = project.version.toString()
		val npmVersion = (project.findProperty("npmVersion") as String?)?.trim()?.takeIf { it.isNotEmpty() }
			?: Regex("^(\\d+\\.\\d+\\.\\d+)([A-Za-z]+)(\\d+)$")
				.matchEntire(gradleVersion)
				?.let { m -> "${m.groupValues[1]}-${m.groupValues[2].lowercase()}.${m.groupValues[3]}" }
			?: gradleVersion
		val isPrerelease = npmVersion.contains("-")

		// npm package name. Defaults to the scoped name on the 'plantuml' org
		// (the unscoped "plantuml" name is owned by a third party). Override with
		// -PnpmName=... if needed.
		val npmName = (project.findProperty("npmName") as String?)?.trim()?.takeIf { it.isNotEmpty() }
			?: "@plantuml/core"

		// Files that make up the package. Everything the demo pages reference,
		// minus the heavy stdlib bundles.
		val include = listOf(
			"plantuml.js",
			"viz-global.js",
			"emoji.js",
			"openiconic.js",
			"main.js",
			"main.css",
			"favicon.svg",
			"favicon.ico",
			"index.html",
			"index-basic.html",
			"index-basic-dark.html",
			"index-collection.html",
			"github-integration-poc.html",
			"github-integration-web-worker-poc.html",
			"GITHUB_INTEGRATION.md"
		)

		copy {
			from(teavmJsOutputDir) {
				include(*include.toTypedArray())
			}
			into(pkgDir)
		}

		// package.json
		val packageJson = """
			{
			  "name": "$npmName",
			  "version": "$npmVersion",
			  "description": "PlantUML diagram rendering engine compiled to JavaScript with TeaVM. Renders UML and many other diagrams entirely in the browser, with no server and no Java.",
			  "type": "module",
			  "main": "plantuml.js",
			  "module": "plantuml.js",
			  "exports": {
			    ".": "./plantuml.js",
			    "./plantuml.js": "./plantuml.js",
			    "./viz-global.js": "./viz-global.js"
			  },
			  "files": [
			    "plantuml.js",
			    "viz-global.js",
			    "emoji.js",
			    "openiconic.js",
			    "main.js",
			    "main.css",
			    "favicon.svg",
			    "favicon.ico",
			    "index.html",
			    "index-basic.html",
			    "index-basic-dark.html",
			    "index-collection.html",
			    "github-integration-poc.html",
			    "github-integration-web-worker-poc.html",
			    "GITHUB_INTEGRATION.md",
			    "README.md"
			  ],
			  "keywords": [
			    "plantuml",
			    "uml",
			    "diagram",
			    "teavm",
			    "svg",
			    "graphviz",
			    "sequence-diagram",
			    "browser"
			  ],
			  "homepage": "https://plantuml.com/",
			  "repository": {
			    "type": "git",
			    "url": "git+https://github.com/plantuml/plantuml.git"
			  },
			  "bugs": {
			    "url": "https://github.com/plantuml/plantuml/issues"
			  },
			  "author": "Arnaud Roques",
			  "license": "GPL-3.0-or-later",
			  "sideEffects": false
			}
		""".trimIndent()
		file("$pkgDir/package.json").writeText(packageJson + "\n")

		// README.md (shown on npmjs.com)
		val cdnInstall = if (isPrerelease) "npm install $npmName@beta" else "npm install $npmName"
		val readme = """
			# $npmName

			The [PlantUML](https://plantuml.com/) diagram engine, compiled to JavaScript
			with [TeaVM](https://github.com/konsoletyper/teavm). It renders UML and many
			other diagrams **entirely in the browser** -- no server, no Java, no Graphviz
			binary required.

			This package is generated from the official PlantUML build (issue
			[#2715](https://github.com/plantuml/plantuml/issues/2715)).

			## Install

			```sh
			$cdnInstall
			```

			Or import straight from a CDN:

			```html
			<script src="https://unpkg.com/$npmName@$npmVersion/viz-global.js"></script>
			<script type="module">
			  import { render } from "https://unpkg.com/$npmName@$npmVersion/plantuml.js";
			  render("@startuml\nAlice -> Bob : Hello\n@enduml".split("\n"), "out");
			</script>
			<div id="out"></div>
			```

			## API

			The public surface is two functions exported from the `plantuml.js`
			ES2015 module. `viz-global.js` (the Graphviz layout engine) must be loaded
			as a classic script beforehand.

			- `render(lines, targetId)` -- render into the DOM element with that `id`.
			  `lines` is an `Array<string>`.
			- `render(lines, targetId, { dark: true })` -- same, in dark mode.
			- `renderToString(lines, onSuccess, onError)` -- deliver the SVG as a string
			  to `onSuccess(svg)`; errors go to `onError(message)`.

			Rendering is asynchronous: `render()` returns immediately and writes the SVG
			into the target element later. See
			[GITHUB_INTEGRATION.md](./GITHUB_INTEGRATION.md) for details and a full
			GitHub-style integration example.

			## What's included

			- `plantuml.js` -- the engine
			- `viz-global.js` -- Graphviz / Viz.js layout engine (required)
			- demo pages: `index.html` (playground), `index-basic.html`,
			  `index-basic-dark.html`, `index-collection.html`, and two GitHub
			  integration proofs of concept

			Heavy optional sprite libraries (IBM, tupadr3, material, AWS...) are **not**
			bundled here to keep the package small; load them from the project site if
			you need them.

			## License

			[GPL-3.0-or-later](https://www.gnu.org/licenses/gpl-3.0.txt). Images you
			generate with PlantUML are **not** covered by the GPL -- see the project
			[license](https://github.com/plantuml/plantuml/blob/master/license.txt).
		""".trimIndent()
		file("$pkgDir/README.md").writeText(readme + "\n")

		println("")
		println("======================")
		println("npm package assembled:")
		println("  dir     : ${pkgDir.absolutePath}")
		println("  name    : $npmName")
		println("  version : $npmVersion  (from gradle '$gradleVersion')")
		if (isPrerelease) {
			println("  NOTE    : prerelease -> publish with:  npm publish --tag beta --access public")
		} else {
			println("  publish : npm publish --access public")
		}
		println("======================")
		println("")
	}
}



if (fastBuild) {
    // -Pfast: build only the GPL jar, skip everything else.
    // Tests are NOT disabled here so that `./gradlew test -Pfast` still works
    // (useful in CI to compile fast then run tests).

    // 1) Skip javadoc (both generation and jar)
    tasks.withType<Javadoc>().configureEach {
        enabled = false
    }
    tasks.matching { it.name == "javadocJar" }.configureEach {
        enabled = false
    }
    tasks.matching { it.name == "sourcesJar" }.configureEach {
        enabled = false
    }

    // 2) Skip jacoco
    tasks.matching { it.name.startsWith("jacoco") }.configureEach {
        enabled = false
    }

    // 4) Key point: build only produces the GPL jar (no check)
    tasks.named("build") {
        setDependsOn(listOf(tasks.named("jar")))
    }

    // 5) Skip check when invoked via build (tests can still be run explicitly)
    tasks.named("check") {
        enabled = false
    }
}
