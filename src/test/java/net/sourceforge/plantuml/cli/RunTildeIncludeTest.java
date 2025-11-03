package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

/**
 * This test checks that the CLI correctly resolves includes written with the
 * tilde syntax (~/...) so that files located under the user home can be
 * included from a diagram stored elsewhere.
 */
class RunTildeIncludeTest extends AbstractCliTest {

	@Test
	void tilde_includes_should_resolve_from_user_home() throws IOException, InterruptedException {
		// 1) prepare a folder in the user's home that will be referenced with ~/
		final Path userHome = Path.of(System.getProperty("user.home"));
		final Path homeIncludeDir = userHome.resolve("plantuml-tilde-test");
		final Path homeLogoDir = homeIncludeDir.resolve("logo");

		Files.createDirectories(homeLogoDir);

		// deepest file first: ~/plantuml-tilde-test/logo/logo.puml
		final Path homeLogoPuml = homeLogoDir.resolve("logo.puml");
		Files.writeString(
				homeLogoPuml,
				String.join(System.lineSeparator(),
						"title Included from user home"),
				StandardCharsets.UTF_8);

		// header in the home dir that includes the logo with a path relative to itself
		// (this part is just to show the included file is really reached)
		final Path homeHeaderPuml = homeIncludeDir.resolve("header.puml");
		Files.writeString(
				homeHeaderPuml,
				String.join(System.lineSeparator(),
						"!include ./logo/logo.puml",
						"actor Alice",
						"actor Bob",
						"Alice -> Bob : from home header"),
				StandardCharsets.UTF_8);

		// 2) now create the main diagram in the temp directory used by AbstractCliTest
		// and make it include the file under ~/...
		final Path main = tempDir.resolve("main.puml");
		Files.writeString(
				main,
				String.join(System.lineSeparator(),
						"@startuml",
						"!include ~/plantuml-tilde-test/header.puml",
						"Bob -> Alice : back from temp",
						"@enduml"),
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
		// comes from ~/plantuml-tilde-test/header.puml
		assertTrue(content.contains("from home header"));
		// comes from main.puml
		assertTrue(content.contains("back from temp"));
		// comes from ~/plantuml-tilde-test/logo/logo.puml
		assertTrue(content.contains("Included from user home"));
	}
}
