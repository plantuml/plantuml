package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFormatScxmlTest extends AbstractCliTest {

	@Test
	void testScxml() throws IOException, InterruptedException {
		final Path file = tempDir.resolve("test.txt");
		Files.writeString(file, String.join(System.lineSeparator(), "@startuml", "state foo", "@enduml"));

		Run.main(new String[] { "-tscxml", file.toAbsolutePath().toString() });

		assertLs("[test.scxml, test.txt]", tempDir);

		final Path xmlFile = tempDir.resolve("test.scxml");
		assertTrue(Files.exists(xmlFile));

		final String content = new String(Files.readAllBytes(xmlFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		assertTrue(content.contains("<scxml xmlns=\"http://www.w3.org/2005/07/scxml\" version=\"1.0\">"));
		assertTrue(content.contains("<state id=\"foo\"/>"));
		assertTrue(content.contains("</scxml>"));

	}

}
