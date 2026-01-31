//    permits to start the build setting the javac release parameter, no parameter means build for java8:
// gradle clean build -x javaDoc -PjavacRelease=8
// gradle clean build -x javaDoc -PjavacRelease=17
//    also supported is to build first, with java17, then switch the java version, and run the test with java8:
// gradle clean build -x javaDoc -x test
// gradle test

import java.time.LocalDateTime
import java.util.jar.JarFile
import org.gradle.process.ExecOperations
import javax.inject.Inject

println("Running build.gradle.kts")
println(project.version)

val javacRelease = (project.findProperty("javacRelease") ?: "8") as String

plugins {
	java
	`maven-publish`
	signing
    eclipse
	jacoco
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

val jdependConfig by configurations.creating

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
	implementation(libs.batik.all)

	// JDepend for package metrics
	jdependConfig("jdepend:jdepend:2.9.1")

    // Custom configuration for pdfJar task
    configurations.create("pdfJarDeps")
    "pdfJarDeps"(libs.fop)
    "pdfJarDeps"(libs.batik.all)

}

repositories {
	mavenLocal()
	mavenCentral()
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
		addStringOption("Xmaxwarns", "1")
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
	
	// Configure Java Util Logging to use our logging.properties file
	systemProperty("java.util.logging.config.file", 
		"${projectDir}/src/test/resources/logging.properties")
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
    
    doLast {
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
        
        // Generate XML report
        execOperations.javaexec {
            mainClass.set("jdepend.xmlui.JDepend")
            classpath = jdependClasspath
            args("-file", xmlReport.get().asFile.absolutePath, classesDir.asPath)
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
	description = "Generates project site with documentation, reports, and test results"
	group = "documentation"
	
	val siteDir = layout.buildDirectory.dir("site").get().asFile
	
	dependsOn(
		tasks.javadoc,
		tasks.test,
		tasks.jacocoTestReport,
		"jdepend",
		"jdependHtml"
	)
	
	doFirst {
		siteDir.mkdirs()
		
		// Generate timestamp
		val timestamp = LocalDateTime.now().toString().replace('T', ' ').substring(0, 19)
		
		// Create index.html for site navigation
		val indexHtml = file("$siteDir/index.html")
		indexHtml.writeText("""<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PlantUML Project Site - ${project.version}</title>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            background: #f5f5f5;
        }
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 40px;
            border-radius: 10px;
            margin-bottom: 30px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .header h1 {
            margin: 0 0 10px 0;
            font-size: 2.5em;
        }
        .header p {
            margin: 0;
            opacity: 0.9;
            font-size: 1.1em;
        }
        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            transition: transform 0.2s, box-shadow 0.2s;
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        }
        .card h2 {
            margin-top: 0;
            color: #333;
            font-size: 1.5em;
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
        }
        .card p {
            color: #666;
            line-height: 1.6;
        }
        .card a {
            display: inline-block;
            margin-top: 15px;
            padding: 10px 20px;
            background: #667eea;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background 0.2s;
        }
        .card a:hover {
            background: #5568d3;
        }
        .footer {
            text-align: center;
            color: #666;
            padding: 20px;
            margin-top: 30px;
        }
        .badge {
            display: inline-block;
            padding: 5px 10px;
            background: #e0e0e0;
            border-radius: 3px;
            font-size: 0.9em;
            margin: 5px 5px 0 0;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>PlantUML Project Site</h1>
        <p>Version ${project.version} | Generated $timestamp</p>
        <div style="margin-top: 15px;">
            <span class="badge">Java ${javacRelease}</span>
            <span class="badge">${group}</span>
        </div>
    </div>

    <div class="grid">
        <div class="card">
            <h2>📚 API Documentation</h2>
            <p>Complete JavaDoc API documentation for all classes and packages.</p>
            <a href="javadoc/index.html">View Javadoc</a>
        </div>

        <div class="card">
            <h2>🧪 Test Results</h2>
            <p>Detailed test execution reports with pass/fail status and execution times.</p>
            <a href="tests/test/index.html">View Test Report</a>
        </div>

        <div class="card">
            <h2>📊 Code Coverage</h2>
            <p>JaCoCo code coverage analysis showing tested vs untested code paths.</p>
            <a href="jacoco/test/html/index.html">View Coverage Report</a>
        </div>

        <div class="card">
            <h2>🔍 JDepend Analysis</h2>
            <p>Package dependency metrics including abstractness, instability, and cycles.</p>
            <a href="jdepend/jdepend-report.html">View HTML Report</a>
            <a href="jdepend/jdepend-report.txt" style="margin-left: 10px; background: #999;">Text Report</a>
        </div>
    </div>

    <div class="footer">
        <p><strong>${project.description}</strong></p>
        <p>Generated by Gradle ${gradle.gradleVersion}</p>
    </div>
</body>
</html>""")
	
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
	}
	
	doLast {
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
		println("========================================")
	}
}
