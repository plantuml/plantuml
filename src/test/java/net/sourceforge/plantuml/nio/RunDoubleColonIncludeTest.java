package net.sourceforge.plantuml.nio;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;
import net.sourceforge.plantuml.cli.AbstractCliTest;

/**
 * This test checks that the CLI correctly resolves includes written with the
 * non-standard syntax ::./foo.puml or ::/bar.puml, which must be resolved from
 * the process launch directory (the current working directory), rather than
 * from the folder containing the current file.
 */
class RunDoubleColonIncludeTest extends AbstractCliTest {

	@Test
	void double_colon_includes_should_resolve_from_process_directory() throws IOException, InterruptedException {
		// 1) prepare a folder in the current working directory that will be referenced
		// with ::./
		final Path processDir = Path.of("").toAbsolutePath();
		final Path includeDir = processDir.resolve("plantuml-doublecolon-test");
		final Path logoDir = includeDir.resolve("logo");

		try {
			// 1) create working directory structure under process dir

			Files.createDirectories(logoDir);

			// deepest file first: ./plantuml-doublecolon-test/logo/logo.puml
			final Path logoPuml = logoDir.resolve("logo.puml");
			Files.writeString(logoPuml, String.join(System.lineSeparator(), "title Included from process directory"),
					StandardCharsets.UTF_8);

			// header in the process directory that includes the logo using a path relative
			// to itself
			final Path headerPuml = includeDir.resolve("header.puml");
			Files.writeString(headerPuml, String.join(System.lineSeparator(), "!include ./logo/logo.puml",
					"actor Alice", "actor Bob", "Alice -> Bob : from process header"), StandardCharsets.UTF_8);

			// 2) now create the main diagram in the temporary directory used by
			// AbstractCliTest
			// and make it include the file under ::./...
			final Path main = tempDir.resolve("main.puml");
			Files.writeString(main, String.join(System.lineSeparator(), "@startuml",
					"!include ::./plantuml-doublecolon-test/header.puml", "Bob -> Alice : back from temp", "@enduml"),
					StandardCharsets.UTF_8);

			// 3) invoke the CLI
			Run.main(new String[] { "-svg", main.toAbsolutePath().toString() });

			// 4) basic checks on the output folder
			assertLs("[main.puml, main.svg]", tempDir);

			final Path svgFile = tempDir.resolve("main.svg");
			assertTrue(Files.exists(svgFile));

			final String content = Files.readString(svgFile, StandardCharsets.UTF_8);

			// SVG produced
			assertTrue(content.contains("<svg"));
			// comes from ./plantuml-doublecolon-test/header.puml (resolved from process
			// dir)
			assertTrue(content.contains("from process header"));
			// comes from main.puml
			assertTrue(content.contains("back from temp"));
			// comes from ./plantuml-doublecolon-test/logo/logo.puml
			assertTrue(content.contains("Included from process directory"));
		} finally {
			// 5) cleanup processDir test folder
			if (Files.exists(includeDir)) {
				try (var paths = Files.walk(includeDir)) {
					paths.sorted(Comparator.reverseOrder()).forEach(p -> {
						try {
							Files.deleteIfExists(p);
						} catch (IOException e) {
							System.err.println("Warning: could not delete " + p + ": " + e.getMessage());
						}
					});
				}
			}
		}
	}
}
