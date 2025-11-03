package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunRelativeIncludeTest extends AbstractCliTest {

	/**
	 * Directory tree created in the temporary folder:
	 *
	 * tempDir/
	 *  ├─ main.puml
	 *  └─ templates/
	 *      ├─ header.puml   (does !include ./logo/logo.puml)
	 *      └─ logo/
	 *          └─ logo.puml
	 *
	 * main.puml does: !include ./templates/header.puml
	 */
	@Test
	void relative_includes_should_resolve_from_each_file() throws IOException, InterruptedException {
		// 1) create the directory structure
		final Path main = tempDir.resolve("main.puml");
		final Path templatesDir = tempDir.resolve("templates");
		final Path logoDir = templatesDir.resolve("logo");

		Files.createDirectories(logoDir);

		// deepest file first
		final Path logoPuml = logoDir.resolve("logo.puml");
		Files.writeString(logoPuml,
				String.join(System.lineSeparator(), "title PlantUML logo included"),
				StandardCharsets.UTF_8);

		// header.puml includes the logo using a path relative to itself
		final Path headerPuml = templatesDir.resolve("header.puml");
		Files.writeString(headerPuml, String.join(System.lineSeparator(), "!include ./logo/logo.puml", "actor Alice",
				"actor Bob", "Alice -> Bob : from header"), StandardCharsets.UTF_8);

		// main.puml includes the header
		Files.writeString(main, String.join(System.lineSeparator(), "@startuml", "!include ./templates/header.puml",
				"Bob -> Alice : back", "@enduml"), StandardCharsets.UTF_8);

		// 2) invoke the CLI
		Run.main(new String[] { "-svg", main.toAbsolutePath().toString() });

		// 3) basic checks on the output folder
		// we should at least have main.puml and main.svg
		assertLs("[main.puml, main.svg, templates]", tempDir);

		// 4) verify the generated SVG
		final Path svgFile = tempDir.resolve("main.svg");
		assertTrue(Files.exists(svgFile));

		final String content = Files.readString(svgFile, StandardCharsets.UTF_8);

		// present because it’s an SVG
		assertTrue(content.contains("<svg"));
		// comes from header.puml
		assertTrue(content.contains("from header"));
		// comes from main.puml
		assertTrue(content.contains("back"));
		// trace from logo.puml: we check the title defined inside it
		assertTrue(content.contains("PlantUML logo included"));

	}
}
