// PlantUML under the MIT License.
// Everything shared by the license variants lives in the convention plugin:
// license-variants/build-logic/src/main/kotlin/plantuml.license-variant.gradle.kts

plugins {
	id("plantuml.license-variant")
	alias(libs.plugins.teavm)
}

licenseVariant {
	id = "mit"
	pomLicenseName = "MIT License"
	pomLicenseUrl = "https://opensource.org/license/mit/"
}

// ============================================
// TeaVM - JavaScript engine (MIT flavor)
// ============================================

dependencies {
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

// ============================================
// npm package - assemble a publishable npm package for the MIT JS engine
// ============================================
//
// Produces license-variants/plantuml-mit/build/npm-plantuml/, a self-contained npm package
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
//     unchanged -- they are copied verbatim from src/main/teavm;
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
//   cd license-variants/plantuml-mit/build/npm-plantuml
//   npm publish --access public                # done manually by the maintainer
//
val teavmJsOutputDir = layout.buildDirectory.dir("generated/teavm/js")

tasks.register("npmPackage") {
	description = "Assembles a publishable npm package for the MIT TeaVM JS engine (issue #2715)."
	group = "teavm"

	// generateJavaScript produces plantuml-mit.js in build/generated/teavm/js.
	// processResources is not needed: the bundle's companion files are copied
	// directly from the root src/main/teavm tree below.
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
		// shared teavm sources. Everything the demo pages reference, minus the
		// heavy stdlib bundles (and minus plantuml.js, supplied above from the
		// MIT build). main.js imports plantuml-codec.js and zoom.js, and
		// index.html loads vendor/fflate-*.min.js (copied below): without them
		// the playground cannot start when opened from the package.
		val include = listOf(
			"viz-global.js",
			"emoji.js",
			"openiconic.js",
			"themes.js",
			"main.js",
			"plantuml-codec.js",
			"zoom.js",
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

		// The companion files are split by origin under the root
		// src/main/teavm tree (issue #2870); the package itself stays flat.
		val teavmSrcDir = rootProject.layout.projectDirectory.dir("src/main/teavm")
		copy {
			for (sub in listOf("web", "generated", "vendor")) {
				from(teavmSrcDir.dir(sub)) {
					include(*include.toTypedArray())
				}
			}
			// Same rule as the root `teavm` task: apart from viz-global.js
			// (loaded from the root), third-party code keeps its vendor/ folder,
			// license included.
			from(teavmSrcDir.dir("vendor")) {
				exclude("viz-global.js")
				into("vendor")
			}
			into(pkgDir)
		}

		// LICENSE file. npm auto-includes a file named LICENSE if present in the
		// package dir; since the whole point of this release is the MIT license,
		// ship the full text (copied from mit-license.txt, renamed to LICENSE).
		copy {
			from(layout.projectDirectory) {
				include("mit-license.txt")
				rename { "LICENSE" }
			}
			into(pkgDir)
		}

		// package.json and README.md (shown on npmjs.com) come from templates in
		// npm/; @npmName@, @npmVersion@ and @cdnInstall@ are substituted below.
		val cdnInstall = if (isPrerelease) "npm install $npmName@beta" else "npm install $npmName"
		fun render(template: String): String =
			layout.projectDirectory.file("npm/$template").asFile.readText(Charsets.UTF_8)
				.replace("@npmName@", npmName)
				.replace("@npmVersion@", npmVersion)
				.replace("@cdnInstall@", cdnInstall)
		file("$pkgDir/package.json").writeText(render("package.json.tmpl"))
		file("$pkgDir/README.md").writeText(render("README.md.tmpl"))

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
