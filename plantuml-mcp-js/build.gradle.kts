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
//     +- README.md       (copied from README-NPM.md: shorter, user-facing;
//     |                   the longer README.md is for GitHub and is not shipped)
//     +- LICENSE         (copied as-is; MIT)
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
// Patch build metadata (version, git commit id, timestamp) into
// CompilationInfo.java BEFORE the engine is compiled, so the published engine
// reports a real version instead of the "$version$" placeholders left by local
// builds. The root `patchCompilationInfo` task edits the tracked source file in
// place; we depend on it here and restore the file afterwards (see doLast), so
// the working tree stays clean.
//
// CRITICAL ordering: patchCompilationInfo must run BEFORE the Java compilation
// that generateJavaScript consumes. dependsOn alone does not guarantee order in
// Gradle, so we also force the root :compileJava to run after the patch, and we
// invalidate any previously-compiled (placeholder) classes so the patched
// source is actually recompiled rather than served from an up-to-date cache.
val rootCompileJava = rootProject.tasks.named("compileJava")
rootCompileJava.configure {
	mustRunAfter(":patchCompilationInfo")
	// Force recompilation on the patched source: without this, a cached
	// up-to-date compileJava would keep the placeholder bytecode and the patch
	// would have no visible effect on the engine.
	outputs.upToDateWhen { false }
}

tasks.register("npmPackage") {
	description = "Assembles a publishable npm package for the MCP server (@plantuml/mcp-js)."
	group = "teavm"

	dependsOn(":patchCompilationInfo")
	dependsOn("generateJavaScript")

	val teavmJsOutputDir = layout.buildDirectory.dir("generated/teavm/js")
	val outputDir = layout.buildDirectory.dir("npm-mcp-js")
	val srcDir = projectDir
	val rootDir = rootProject.projectDir

	doLast {
		// Everything runs inside try/finally so that CompilationInfo.java is
		// restored to its pristine (placeholder) state no matter what -- including
		// when the placeholder guard below aborts the build. patchCompilationInfo
		// edits a tracked source file in place; leaving it modified would dirty the
		// working tree and risk committing build metadata into source.
		try {
			val pkgDir = outputDir.get().asFile
			pkgDir.deleteRecursively()
			pkgDir.mkdirs()

		// 1) Copy the compiled engine, flattened to engine.js next to server.js.
		val engineSrc = teavmJsOutputDir.get().file("plantuml-mcp-js.js").asFile
		if (!engineSrc.exists()) {
			error("Compiled engine not found: ${engineSrc.absolutePath}. Run generateJavaScript first.")
		}

		// Guard against publishing an engine still carrying the build-metadata
		// placeholders. patchCompilationInfo should have replaced them before the
		// engine was compiled; if a placeholder survives here, the ordering or the
		// compile cache misbehaved and we must NOT publish (npm versions are
		// immutable). Fail loudly instead.
		val engineText = engineSrc.readText()
		val leftoverPlaceholders = listOf("\$version\$", "\$git.commit.id\$")
			.filter { engineText.contains(it) }
		if (leftoverPlaceholders.isNotEmpty()) {
			error(
				"Unresolved build-metadata placeholder(s) ${leftoverPlaceholders} found in " +
				"${engineSrc.name}. patchCompilationInfo did not take effect before compilation. " +
				"Refusing to assemble the npm package (npm versions are immutable). " +
				"Try a clean build: gradlew :plantuml-mcp-js:npmPackage after deleting build/."
			)
		}
		engineSrc.copyTo(file("$pkgDir/engine.js"), overwrite = true)

		// 2) Copy the versioned source files as-is.
		listOf("server.js", "package.json", "LICENSE").forEach { name ->
			val f = File(srcDir, name)
			if (!f.exists()) {
				error("Expected source file not found: ${f.absolutePath}")
			}
			f.copyTo(file("$pkgDir/$name"), overwrite = true)
		}

		// 2b) The npm page uses a shorter, user-facing README. npm always renders
		// the file literally named README.md in the tarball, so we copy
		// README-NPM.md under that name. The longer README.md stays in the source
		// tree for GitHub (build/maintainer notes) and is NOT shipped. Fall back to
		// README.md if the npm-specific file is absent.
		val npmReadme = File(srcDir, "README-NPM.md")
		val readmeSrc = if (npmReadme.exists()) npmReadme else File(srcDir, "README.md")
		if (!readmeSrc.exists()) {
			error("No README found: expected README-NPM.md or README.md in ${srcDir.absolutePath}")
		}
		readmeSrc.copyTo(file("$pkgDir/README.md"), overwrite = true)

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
		} finally {
			// Restore CompilationInfo.java to its committed (placeholder) state.
			// Uses `git restore` so a clean checkout is left behind whether the
			// build succeeded or aborted. If git is unavailable or the restore
			// fails, warn loudly rather than silently leaving a dirty tree.
			try {
				val restore = ProcessBuilder(
					"git", "restore",
					"src/main/java/net/sourceforge/plantuml/version/CompilationInfo.java"
				)
					.directory(rootDir)
					.redirectErrorStream(true)
					.start()
				val out = restore.inputStream.bufferedReader().readText().trim()
				val code = restore.waitFor()
				if (code == 0) {
					println("Restored CompilationInfo.java (git restore).")
				} else {
					logger.warn(
						"WARNING: could not restore CompilationInfo.java (git exit $code). " +
						"Run: git restore src/main/java/net/sourceforge/plantuml/version/CompilationInfo.java" +
						(if (out.isNotEmpty()) "\n$out" else "")
					)
				}
			} catch (e: Exception) {
				logger.warn(
					"WARNING: failed to run git restore on CompilationInfo.java (${e.message}). " +
					"Restore it manually: git restore src/main/java/net/sourceforge/plantuml/version/CompilationInfo.java"
				)
			}
		}
	}
}
