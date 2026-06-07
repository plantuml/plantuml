//    permits to start the build setting the javac release parameter, no parameter means build for java8:
// gradle clean build -x javaDoc -PjavacRelease=8
// gradle clean build -x javaDoc -PjavacRelease=17
//    also supported is to build first, with java17, then switch the java version, and run the test with java8:
// gradle clean build -x javaDoc -x test
// gradle test
val javacRelease = (project.findProperty("javacRelease") ?: "11") as String

plugins {
	java
	`maven-publish`
	signing
	alias(libs.plugins.teavm)
}

group = "net.sourceforge.plantuml"
description = "PlantUML"

java {
	withSourcesJar()
	withJavadocJar()
}

dependencies {
	compileOnly(libs.ant)
	compileOnly(libs.teavm.jso.apis)
	compileOnly(libs.teavm.classlib)
	compileOnly(libs.openpdf)
	testImplementation(libs.assertj.core)
	testImplementation(libs.junit.jupiter)
	testImplementation(libs.jlatexmath)
	testImplementation(libs.xmlunit.core)
	teavm(teavm.libs.jsoApis)
}

teavm {
	js {
		mainClass.set("net.sourceforge.plantuml.teavm.browser.PlantUMLBrowser")
		moduleType.set(org.teavm.gradle.api.JSModuleType.ES2015)
		obfuscated.set(true)
		optimization.set(org.teavm.gradle.api.OptimizationLevel.BALANCED)
	}
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
      srcDir(rootProject.layout.projectDirectory.dir("src/main/resources"))
    }
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
		from(rootProject.layout.projectDirectory.file("manifest.txt"))
	}

	// source sets for java and resources are on "src", only put once into the jar
	exclude("teavm/**")
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
				"define" to "__MIT__",
				"header" to project.layout.buildDirectory.file("../mit-license.txt").get().asFile.absolutePath
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

// ============================================
// npm package - assemble a publishable npm package for the MIT JS engine
// ============================================
//
// Produces plantuml-mit/build/npm-plantuml/, a self-contained npm package
// exposing the MIT-licensed TeaVM-compiled PlantUML engine (issue #2715).
//
// This mirrors the root project's `npmPackage` task (which produces the GPL
// flavor for the project site/demo) but builds from the MIT flavor instead.
// The two tasks are independent: the root task is left untouched.
//
// The TeaVM plugin names its output after the subproject artifact, so the
// engine file produced here is `plantuml-mit.js`. It is copied into the
// package renamed to `plantuml.js` so that:
//   - the demo pages and main.js (which `import` "./plantuml.js") keep working
//     unchanged -- they are copied verbatim from src/main/resources/teavm;
//   - existing CDN URLs (unpkg/jsdelivr .../plantuml.js) keep resolving.
// All references to the engine in the bundle are relative imports from the
// HTML/JS files, never a self-reference baked into the .js, so renaming at
// copy time is sufficient.
//
// Like the root task, the heavy optional stdlib sprite bundles
// (ibm/tupadr3/material*/awslib*...) are deliberately excluded to keep the
// tarball small.
//
// Usage:
//   gradlew :plantuml-mit:npmPackage           # assemble build/npm-plantuml
//   cd plantuml-mit/build/npm-plantuml
//   npm publish --access public                # done manually by the maintainer
//
val teavmJsOutputDir = layout.buildDirectory.dir("generated/teavm/js")

tasks.register("npmPackage") {
	description = "Assembles a publishable npm package for the MIT TeaVM JS engine (issue #2715)."
	group = "teavm"

	// generateJavaScript produces plantuml-mit.js in build/generated/teavm/js.
	// processResources is not needed: the bundle's companion files are copied
	// directly from the root src/main/resources/teavm tree below.
	dependsOn("generateJavaScript")

	val outputDir = layout.buildDirectory.dir("npm-plantuml")

	doLast {
		val pkgDir = outputDir.get().asFile
		pkgDir.deleteRecursively()
		pkgDir.mkdirs()

		// Determine the npm version (must be valid semver).
		//
		// By default it is derived from the Gradle project version
		// (e.g. "1.2026.5beta1" -> "1.2026.5-beta.1"). Override with
		// -PnpmVersion=... to release a stable npm version while the project
		// itself is still a beta:
		//
		//   gradlew :plantuml-mit:npmPackage -PnpmVersion=1.2026.5
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

		// The engine file produced by TeaVM for this subproject, renamed to the
		// canonical "plantuml.js" expected by the bundle and by CDN consumers.
		copy {
			from(teavmJsOutputDir) {
				include("plantuml-mit.js")
				rename { "plantuml.js" }
			}
			into(pkgDir)
		}

		// Companion files that make up the package, copied verbatim from the
		// shared teavm resources. Everything the demo pages reference, minus the
		// heavy stdlib bundles (and minus plantuml.js, supplied above from the
		// MIT build).
		val include = listOf(
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
			from(rootProject.layout.projectDirectory.dir("src/main/resources/teavm")) {
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
			  "license": "MIT",
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

			[MIT](https://opensource.org/license/mit/).
		""".trimIndent()
		file("$pkgDir/README.md").writeText(readme + "\n")

		println("")
		println("======================")
		println("npm package assembled (MIT flavor):")
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
