// Runs Super-Linter locally, exactly like .github/workflows/super-linter.yml does in CI.
//
// Usage (Docker must be running):
//   gradlew superLinter                 # lint the files changed since origin/master (what CI does on push/PR)
//   gradlew superLinter -PlintAll       # lint the whole code base
//   gradlew superLinter -PlintBase=origin/foo   # compare against another ref
//
// What it does:
//   1. `git clone` of the current repository (committed state, LF line endings, whatever
//      core.autocrlf says on this machine) into build/superlinter/repo,
//   2. creates a `base` branch in that clone pointing to origin/master (or -PlintBase),
//   3. runs the same super-linter image/version and the same VALIDATE_xxx variables as the workflow.
//
// IMPORTANT: only COMMITTED changes are linted (like in CI). Commit first, then run it.
//
// Keep SUPER_LINTER_IMAGE and the env below in sync with .github/workflows/super-linter.yml.

val superLinterImage = "ghcr.io/super-linter/super-linter:slim-v8.7.0"

// ---------------------------------------------------------------------------------------
// Lightweight alternative, NO Docker: only Checkstyle (unused imports, star imports...),
// with the same tools/linters/checkstyle.xml as the CI.
//
//   gradlew checkstyleLint            # checks ALL .java files under src/
// ---------------------------------------------------------------------------------------
val checkstyleLintCp = configurations.create("checkstyleLint")
dependencies {
	add("checkstyleLint", "com.puppycrawl.tools:checkstyle:10.21.4")
}

tasks.register<JavaExec>("checkstyleLint") {
	group = "verification"
	description = "Runs Checkstyle (tools/linters/checkstyle.xml) on all Java files of src/, no Docker."
	classpath = checkstyleLintCp
	mainClass.set("com.puppycrawl.tools.checkstyle.Main")

	// Always checks every .java file under src/ (main, test...)
	args("-c", "tools/linters/checkstyle.xml", "src")
	workingDir = projectDir
}

tasks.register("superLinter") {
	group = "verification"
	description = "Runs Super-Linter (Docker) locally on the committed changes, like the CI workflow."

	// Never up-to-date: always re-run
	outputs.upToDateWhen { false }

	doLast {
		val lintAll = project.hasProperty("lintAll")
		val lintBase = (project.findProperty("lintBase") as String?) ?: "origin/master"
		val srcDir = projectDir
		val workDir = layout.buildDirectory.dir("superlinter").get().asFile
		val repoDir = File(workDir, "repo")

		fun run(dir: File, vararg cmd: String) {
			println("> " + cmd.joinToString(" "))
			val p = ProcessBuilder(*cmd).directory(dir).inheritIO().start()
			val code = p.waitFor()
			if (code != 0) throw GradleException("Command failed (exit $code): ${cmd.joinToString(" ")}")
		}

		// 1) Fresh clone of the committed state (LF, full history)
		workDir.deleteRecursively()
		workDir.mkdirs()
		run(workDir, "git", "-c", "core.autocrlf=false", "-c", "core.eol=lf",
			"clone", "--no-hardlinks", srcDir.absolutePath, repoDir.absolutePath)

		// 2) `base` = the ref to compare with (taken from the ORIGINAL repo, not from the clone)
		run(repoDir, "git", "fetch", srcDir.absolutePath, "+refs/remotes/$lintBase:refs/heads/base")

		// 3) Super-Linter, same configuration as the workflow
		val cmd = mutableListOf(
			"docker", "run", "--rm",
			"-e", "RUN_LOCAL=true",
			"-e", "DEFAULT_BRANCH=base",
			"-e", "VALIDATE_ALL_CODEBASE=$lintAll",
			"-e", "LINTER_RULES_PATH=tools/linters",
			"-e", "ENABLE_GITHUB_PULL_REQUEST_SUMMARY_COMMENT=false",
			"-e", "VALIDATE_EDITORCONFIG=true",
			"-e", "VALIDATE_GITHUB_ACTIONS=true",
			"-e", "VALIDATE_GITLEAKS=true",
			"-e", "VALIDATE_JAVA=true",
			"-e", "JAVA_FILE_NAME=checkstyle.xml",
			"-e", "VALIDATE_YAML=true",
			"-v", repoDir.absolutePath + ":/tmp/lint",
			superLinterImage
		)
		run(workDir, *cmd.toTypedArray())
		println("Super-Linter: OK")
	}
}
