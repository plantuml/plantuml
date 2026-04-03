
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.jar.JarFile
import java.util.Base64
import java.util.Properties

println("Running build.gradle.kts")
println(project.version)

val javacRelease = (project.findProperty("javacRelease") ?: "11") as String

plugins {
	java
	jacoco
	alias(libs.plugins.gorylenko.gradle.git.properties)
//	alias(libs.plugins.adarshr.test.logger)
	alias(libs.plugins.sonarqube)
}

group = "net.sourceforge.plantuml"
description = "PlantUML"

java {
    withSourcesJar()
    withJavadocJar()
}

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
}

repositories {
	mavenLocal()
	mavenCentral()
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

		// 5) Transform non-ASCII chars in string literals so TeaVM emits correct JS.
		// TeaVM misreads CONSTANT_Utf8 class file entries for non-ASCII chars, treating
		// each UTF-8 byte as a separate Latin-1 char. Replacing "〶" with
		// "" + (char)0x3016 + "" avoids CONSTANT_Utf8 entirely -- the value is computed
		// at runtime via char arithmetic. Char literals (e.g. '〶') use integer constants
		// in bytecode and are NOT affected, so they are left as-is.
		fun transformNonAsciiInStringLiterals(source: String): String {
			val sb = StringBuilder(source.length)
			var i = 0
			var inString = false
			var inChar = false
			var inLineComment = false
			var inBlockComment = false
			while (i < source.length) {
				val c = source[i]
				when {
					inLineComment -> {
						sb.append(c)
						if (c == '\n') inLineComment = false
					}
					inBlockComment -> {
						sb.append(c)
						if (c == '*' && i + 1 < source.length && source[i + 1] == '/') {
							sb.append('/')
							i++
							inBlockComment = false
						}
					}
					inChar -> {
						sb.append(c)
						if (c == '\\' && i + 1 < source.length) {
							i++
							sb.append(source[i])
						} else if (c == '\'') {
							inChar = false
						}
					}
					inString -> when {
						c == '\\' && i + 1 < source.length -> {
							sb.append(c); i++; sb.append(source[i])
						}
						c == '"' -> { sb.append(c); inString = false }
						c.code > 127 -> sb.append("\" + String.valueOf((char)0x${c.code.toString(16).uppercase()}) + \"")
						else -> sb.append(c)
					}
					else -> when {
						c == '"' -> { sb.append(c); inString = true }
						c == '\'' -> { sb.append(c); inChar = true }
						c == '/' && i + 1 < source.length && source[i + 1] == '/' -> {
							sb.append(c); inLineComment = true
						}
						c == '/' && i + 1 < source.length && source[i + 1] == '*' -> {
							sb.append(c); inBlockComment = true
						}
						else -> sb.append(c)
					}
				}
				i++
			}
			return sb.toString()
		}

		var transformedCount = 0
		outDir.walkTopDown().filter { it.isFile && it.name.endsWith(".java") }.forEach { file ->
			val original = file.readText(Charsets.UTF_8)
			val transformed = transformNonAsciiInStringLiterals(original)
			if (transformed != original) {
				file.writeText(transformed, Charsets.UTF_8)
				transformedCount++
			}
		}
		println("Transformed non-ASCII string literals in $transformedCount file(s)")
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

sonar {
  properties {
    property("sonar.projectKey", "plantuml_plantuml")
    property("sonar.organization", "plantuml")
  }
 }
 