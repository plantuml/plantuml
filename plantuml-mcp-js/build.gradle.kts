// plantuml-mcp-js
//
// TeaVM headless JS build powering the Node-based PlantUML MCP server.
//
// Compiles a minimal, DOM-free entry point
// (net.sourceforge.plantuml.teavm.headless.PlantUMLHeadless) to JavaScript, so
// the MCP server can run on a plain Node process with no Java runtime and no
// browser. This is the lower-barrier-to-entry counterpart of the Java
// Spring-AI based plantuml-mcp server (which requires a JRE).
//
// Unlike the root project's TeaVM 'browser' build (PlantUMLBrowser), this entry
// point pulls in none of the DOM / Viz.js / worker-thread machinery: the
// exported functions are synchronous and return plain strings.
//
// MVP: exposes a single 'version' function, to validate the full chain
// (TeaVM build -> JS artifact -> Node wrapper -> MCP stdio transport -> tool
// discovery by the client) before richer tools are added.
//
// Build the JS engine with:
//   gradle :plantuml-mcp-js:generateJavaScript
//
// The output lands in build/generated/teavm/js/ (the default TeaVM output dir).
//
// Requires Java 11+ (enforced in settings.gradle.kts), like the other
// TeaVM-related tooling.

plugins {
	java
	alias(libs.plugins.teavm)
}

group = "net.sourceforge.plantuml"
description = "PlantUML MCP server (TeaVM headless JS engine)"

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {
	// The headless entry point and Version live in the root project; the JS is
	// compiled from the exact same code as the main jar.
	implementation(project(":"))

	// TeaVM JSO APIs (provides @JSExport etc.) for the teavm source set.
	teavm(teavm.libs.jsoApis)
}

tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"
}

// ============================================
// TeaVM Configuration - Java to JavaScript (headless)
// ============================================

teavm {
	js {
		mainClass.set("net.sourceforge.plantuml.teavm.headless.PlantUMLHeadless")
		moduleType.set(org.teavm.gradle.api.JSModuleType.ES2015)
		// Keep the output readable for the MVP; tighten (obfuscate) later once
		// the chain is validated.
		obfuscated.set(false)
		optimization.set(org.teavm.gradle.api.OptimizationLevel.BALANCED)
		// outputDir defaults to build/generated/teavm/js
	}
}

// ============================================
// npm package - assemble a publishable npm package for the MCP server
// ============================================
//
// Produces build/npm-mcp-js/, a self-contained npm package ready for
// `npm publish`. It mirrors the root project's `npmPackage` task, but for the
// headless MCP server instead of the browser engine.
//
// Unlike the root task, this one does NOT generate package.json / README.md:
// those are versioned source files in this subproject. It only gathers them,
// together with the compiled engine, into a clean publish directory:
//
//   build/npm-mcp-js/
//     +- server.js       (copied as-is)
//     +- engine.js       (the TeaVM output, flattened next to server.js)
//     +- package.json    (copied as-is)
//     +- README.md       (copied as-is)
//
// server.js prefers ./engine.js and falls back to the raw build output, so the
// assembled package is self-contained while `node server.js` still works from
// the source checkout.
//
// Usage:
//   gradlew :plantuml-mcp-js:npmPackage      # assemble build/npm-mcp-js
//   cd build/npm-mcp-js
//   npm install                              # pull @modelcontextprotocol/sdk
//   npm publish --access public              # done manually by the maintainer
//
// The npm version is read from the source package.json. It can be overridden
// with -PnpmVersion=... (e.g. to cut a release without editing the file):
//   gradlew :plantuml-mcp-js:npmPackage -PnpmVersion=0.2.0
//
tasks.register("npmPackage") {
	description = "Assembles a publishable npm package for the MCP server (@plantuml/mcp-js)."
	group = "teavm"

	dependsOn("generateJavaScript")

	val teavmJsOutputDir = layout.buildDirectory.dir("generated/teavm/js")
	val outputDir = layout.buildDirectory.dir("npm-mcp-js")
	val srcDir = projectDir

	doLast {
		val pkgDir = outputDir.get().asFile
		pkgDir.deleteRecursively()
		pkgDir.mkdirs()

		// 1) Copy the compiled engine, flattened to engine.js next to server.js.
		val engineSrc = teavmJsOutputDir.get().file("plantuml-mcp-js.js").asFile
		if (!engineSrc.exists()) {
			error("Compiled engine not found: ${engineSrc.absolutePath}. Run generateJavaScript first.")
		}
		engineSrc.copyTo(file("$pkgDir/engine.js"), overwrite = true)

		// 2) Copy the versioned source files as-is.
		listOf("server.js", "package.json", "README.md", "LICENSE").forEach { name ->
			val f = File(srcDir, name)
			if (!f.exists()) {
				error("Expected source file not found: ${f.absolutePath}")
			}
			f.copyTo(file("$pkgDir/$name"), overwrite = true)
		}

		// 3) Optionally override the version in the copied package.json.
		val npmVersionOverride = (project.findProperty("npmVersion") as String?)
			?.trim()?.takeIf { it.isNotEmpty() }
		if (npmVersionOverride != null) {
			val pj = file("$pkgDir/package.json")
			val patched = pj.readText().replaceFirst(
				Regex("\"version\"\\s*:\\s*\"[^\"]*\""),
				"\"version\": \"$npmVersionOverride\""
			)
			pj.writeText(patched)
		}

		// Report the version actually present in the assembled package.json.
		val finalVersion = Regex("\"version\"\\s*:\\s*\"([^\"]*)\"")
			.find(file("$pkgDir/package.json").readText())
			?.groupValues?.get(1) ?: "unknown"
		val isPrerelease = finalVersion.contains("-")

		println("")
		println("======================")
		println("npm package assembled:")
		println("  dir     : ${pkgDir.absolutePath}")
		println("  name    : @plantuml/mcp-js")
		println("  version : $finalVersion")
		println("  files   : server.js, engine.js, package.json, README.md, LICENSE")
		println("  next    : cd ${pkgDir.absolutePath} && npm install")
		if (isPrerelease) {
			println("  publish : npm publish --tag beta --access public")
		} else {
			println("  publish : npm publish --access public")
		}
		println("======================")
		println("")
	}
}
