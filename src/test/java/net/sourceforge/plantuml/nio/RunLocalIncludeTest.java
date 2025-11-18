package net.sourceforge.plantuml.nio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;
import net.sourceforge.plantuml.cli.AbstractCliTest;

/**
 * This test ensures that the standard include syntax
 * (!include foo.puml) resolves the file relative to
 * the directory of the file being processed,
 * not from the process working directory.
 */
class RunLocalIncludeTest extends AbstractCliTest {

	@Test
	void standard_includes_should_resolve_from_file_directory_not_process_dir() throws IOException, InterruptedException {
		// 1) Prepare a file "foo.puml" in the same directory as the main diagram (tempDir)
		final Path localInclude = tempDir.resolve("foo.puml");
		Files.writeString(
				localInclude,
				String.join(System.lineSeparator(),
						"title Included from local directory"),
				StandardCharsets.UTF_8);

		// 2) Prepare a second "foo.puml" in the process directory that should NOT be used
		final Path processDir = Path.of("").toAbsolutePath();
		final Path wrongInclude = processDir.resolve("foo.puml");
		Files.writeString(
				wrongInclude,
				String.join(System.lineSeparator(),
						"title WRONG include from process directory"),
				StandardCharsets.UTF_8);

		try {
			// 3) Create the main file in tempDir, which includes "foo.puml" (no path prefix)
			final Path main = tempDir.resolve("main.puml");
			Files.writeString(
					main,
					String.join(System.lineSeparator(),
							"@startuml",
							"!include foo.puml",
							"actor Alice",
							"actor Bob",
							"Alice -> Bob : from main",
							"@enduml"),
					StandardCharsets.UTF_8);

			// 4) Run the CLI
			Run.main(new String[] { "-svg", main.toAbsolutePath().toString() });

			// 5) Check outputs
			assertLs("[foo.puml, main.puml, main.svg]", tempDir);

			final Path svgFile = tempDir.resolve("main.svg");
			assertTrue(Files.exists(svgFile));

			final String content = Files.readString(svgFile, StandardCharsets.UTF_8);

			// The generated SVG should contain text from the local foo.puml
			assertTrue(content.contains("Included from local directory"),
					"Should include file from same directory as main.puml");

			// It must NOT contain the wrong one from process working directory
			assertFalse(content.contains("WRONG include from process directory"),
					"Should not include file from process working directory");

		} finally {
			// 6) Clean up the accidental foo.puml created in processDir
			Files.deleteIfExists(processDir.resolve("foo.puml"));
		}
	}
}
