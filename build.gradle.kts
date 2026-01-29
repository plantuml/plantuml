//    permits to start the build setting the javac release parameter, no parameter means build for java8:
// gradle clean build -x javaDoc -PjavacRelease=8
// gradle clean build -x javaDoc -PjavacRelease=17
//    also supported is to build first, with java17, then switch the java version, and run the test with java8:
// gradle clean build -x javaDoc -x test
// gradle test

import java.time.LocalDateTime
import java.util.jar.JarFile

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
    
    val outputDir = file("$buildDir/reports/jdepend")
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

tasks.register("jdependHtml") {
    description = "Generates JDepend HTML report"
    group = "documentation"
    
    dependsOn(tasks.classes)
    
    val outputDir = file("$buildDir/reports/jdepend")
    val htmlReport = file("$outputDir/jdepend-report.html")
    
    doFirst {
        outputDir.mkdirs()
    }
    
    doLast {
        val xmlReport = file("$outputDir/jdepend-report.xml")
        
        // Generate XML report using xmlui with -file argument (no GUI when file specified)
        javaexec {
            mainClass.set("jdepend.xmlui.JDepend")
            classpath = jdependConfig
            args = listOf(
                "-file", xmlReport.absolutePath,
                sourceSets.main.get().output.classesDirs.asPath
            )
        }
        
        // Convert XML to HTML using XSLT
        if (xmlReport.exists()) {
            val xsltFile = file("tools/jdepend-report.xsl")
            
            // Create XSLT 1.0 transformation stylesheet
            xsltFile.writeText("""<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" indent="yes" encoding="UTF-8"/>
  
  <xsl:template match="/JDepend">
    <html>
      <head>
        <meta charset="UTF-8"/>
        <title>JDepend Analysis Report</title>
        <style>
          body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
          h1 { color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px; }
          h2 { color: #666; margin-top: 30px; }
          .summary { background: white; padding: 20px; border-radius: 5px; margin: 20px 0; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
          table { width: 100%; border-collapse: collapse; background: white; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin: 20px 0; }
          th { background: #4CAF50; color: white; padding: 12px; text-align: left; font-weight: bold; }
          td { padding: 10px; border-bottom: 1px solid #ddd; }
          tr:hover { background: #f5f5f5; }
          .numeric { text-align: center; font-family: 'Courier New', monospace; }
          .package-name { font-weight: bold; color: #333; }
          .good { color: #4CAF50; font-weight: bold; }
          .warning { color: #FF9800; font-weight: bold; }
          .bad { color: #F44336; font-weight: bold; }
          .info { background: #E3F2FD; padding: 15px; border-left: 4px solid #2196F3; margin: 20px 0; }
          .metric-desc { font-size: 0.9em; color: #666; margin: 5px 0; }
          .cycles { background: #FFEBEE; padding: 15px; border-left: 4px solid #F44336; margin: 20px 0; }
        </style>
      </head>
      <body>
        <h1>JDepend Analysis Report</h1>
        
        <div class="summary">
          <p><strong>Total Packages Analyzed: </strong><xsl:value-of select="count(Packages/Package)"/></p>
        </div>
        
        <div class="info">
          <h3>Metric Definitions:</h3>
          <div class="metric-desc"><strong>TC:</strong> Total Classes - Number of classes and interfaces in the package</div>
          <div class="metric-desc"><strong>CC:</strong> Concrete Classes - Number of concrete classes</div>
          <div class="metric-desc"><strong>AC:</strong> Abstract Classes - Number of abstract classes and interfaces</div>
          <div class="metric-desc"><strong>Ca:</strong> Afferent Couplings - Number of packages that depend on this package</div>
          <div class="metric-desc"><strong>Ce:</strong> Efferent Couplings - Number of packages this package depends on</div>
          <div class="metric-desc"><strong>A:</strong> Abstractness (0-1) - Ratio of abstract classes to total classes</div>
          <div class="metric-desc"><strong>I:</strong> Instability (0-1) - Ratio Ce/(Ca+Ce), 0=stable, 1=unstable</div>
          <div class="metric-desc"><strong>D:</strong> Distance from Main Sequence (0-1) - |A+I-1|, ideal=0, problematic&gt;0.5</div>
        </div>
        
        <xsl:if test="Cycles/Package">
          <div class="cycles">
            <h3>⚠️ Dependency Cycles Detected</h3>
            <xsl:for-each select="Cycles/Package">
              <p><strong><xsl:value-of select="@name"/></strong></p>
            </xsl:for-each>
          </div>
        </xsl:if>
        
        <h2>Package Metrics</h2>
        <table>
          <thead>
            <tr>
              <th>Package</th>
              <th class="numeric">TC</th>
              <th class="numeric">CC</th>
              <th class="numeric">AC</th>
              <th class="numeric">Ca</th>
              <th class="numeric">Ce</th>
              <th class="numeric">A</th>
              <th class="numeric">I</th>
              <th class="numeric">D</th>
            </tr>
          </thead>
          <tbody>
            <xsl:apply-templates select="Packages/Package"/>
          </tbody>
        </table>
        
        <div class="info" style="margin-top: 30px;">
          <p><strong>Color Coding for Distance (D):</strong></p>
          <p><span class="good">■</span> Green (0-0.2): Good - Package is well-balanced</p>
          <p><span class="warning">■</span> Orange (0.2-0.5): Warning - Consider reviewing package design</p>
          <p><span class="bad">■</span> Red (&gt;0.5): Bad - Package is in Zone of Pain or Zone of Uselessness</p>
        </div>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="Package">
    <tr>
      <td class="package-name"><xsl:value-of select="@name"/></td>
      <td class="numeric"><xsl:value-of select="Stats/TotalClasses"/></td>
      <td class="numeric"><xsl:value-of select="Stats/ConcreteClasses"/></td>
      <td class="numeric"><xsl:value-of select="Stats/AbstractClasses"/></td>
      <td class="numeric"><xsl:value-of select="Stats/Ca"/></td>
      <td class="numeric"><xsl:value-of select="Stats/Ce"/></td>
      <td class="numeric"><xsl:value-of select="format-number(Stats/A, '0.00')"/></td>
      <td class="numeric"><xsl:value-of select="format-number(Stats/I, '0.00')"/></td>
      <td class="numeric">
        <xsl:attribute name="class">
          <xsl:text>numeric </xsl:text>
          <xsl:choose>
            <xsl:when test="Stats/D &lt;= 0.2">good</xsl:when>
            <xsl:when test="Stats/D &lt;= 0.5">warning</xsl:when>
            <xsl:otherwise>bad</xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        <xsl:value-of select="format-number(Stats/D, '0.00')"/>
      </td>
    </tr>
  </xsl:template>
  
</xsl:stylesheet>""")
            
            // Apply XSLT transformation
            ant.withGroovyBuilder {
                "xslt"(
                    "in" to xmlReport,
                    "out" to htmlReport,
                    "style" to xsltFile
                )
            }
            
            println("JDepend reports generated:")
            println("  HTML: ${htmlReport.absolutePath}")
            println("  XML:  ${xmlReport.absolutePath}")
        }
    }
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
	
	val siteDir = file("$buildDir/site")
	
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
			from("$buildDir/docs/javadoc")
			into("$siteDir/javadoc")
		}
		
		copy {
			from("$buildDir/reports/tests")
			into("$siteDir/tests")
		}
		
		copy {
			from("$buildDir/reports/jacoco")
			into("$siteDir/jacoco")
		}
		
		copy {
			from("$buildDir/reports/jdepend")
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
