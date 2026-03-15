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
	registerFeature("pdf") {
		usingSourceSet(sourceSets["main"])
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

	// JDepend for package metrics
	jdependConfig(libs.jdepend)

	// TeaVM JSO APIs for browser interop (provided to the teavm source set by the plugin)
	teavm(teavm.libs.jsoApis)

    // Custom configuration for pdfJar task
    configurations.create("pdfJarDeps")
    "pdfJarDeps"(libs.fop)
    "pdfJarDeps"(libs.batik.all)

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
}

tasks.register<Test>("runIntermediateTest") {
    description = "Runs the 'IntermediateTest'"
    group = "dev"
    useJUnitPlatform()
    jvmArgs("-ea")
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

gitProperties {
	dateFormat = "yyyy-MM-dd'T'HH:mm:ssX"
}

tasks.named("generateGitProperties") {
	doLast {
		val propsFile = layout.buildDirectory.file("resources/main/git.properties").get().asFile
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

val filteredSrcDir = layout.buildDirectory.dir("generated/sources/git-filtered")

// Cleanup of generated filtered sources (avoid leaving copied sources on disk)
val cleanupGitFilteredSources by tasks.registering(Delete::class) {
    group = "build"
    description = "Deletes build/generated/sources/git-filtered after the build."
    delete(filteredSrcDir)
}

val keepFilteredSrc = project.hasProperty("keepFilteredSrc")

tasks.named("build") {
    if (!keepFilteredSrc) finalizedBy(cleanupGitFilteredSources)
}
tasks.named("jar") {
    if (!keepFilteredSrc) finalizedBy(cleanupGitFilteredSources)
}

tasks.named("site") {
    if (!keepFilteredSrc) finalizedBy(cleanupGitFilteredSources)
}

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

tasks.configureEach {
    if (name == "sourcesJar") {
        dependsOn(filterSourcesWithBuildInfo)
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
		// Copy the HTML template and all js files (without erasing existing files)
		copy {
			from("src/main/resources/teavm") {
				include("index.html", "*.js")
			}
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
		include("*.js", "*.html")
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



if (fastBuild) {
    // 1) Skip tests
    tasks.withType<Test>().configureEach {
        enabled = false
    }

    // 2) Skip javadoc (both generation and jar)
    tasks.withType<Javadoc>().configureEach {
        enabled = false
    }
    tasks.matching { it.name == "javadocJar" }.configureEach {
        enabled = false
    }
    tasks.matching { it.name == "sourcesJar" }.configureEach {
        enabled = false
    }

    // 3) Optional but consistent: skip jacoco in fast mode
    tasks.matching { it.name.startsWith("jacoco") }.configureEach {
        enabled = false
    }

    // 4) Key point: build only produces the jar
    tasks.named("build") {
        setDependsOn(listOf(tasks.named("jar")))
    }

    // Optional: if someone runs "check" explicitly, skip it in fast mode
    tasks.named("check") {
        enabled = false
    }
}
