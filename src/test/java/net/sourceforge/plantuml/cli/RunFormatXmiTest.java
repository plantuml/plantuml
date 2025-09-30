package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFormatXmiTest extends AbstractCliTest {

	@Test
	void testXmi() throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-txmi", file.toAbsolutePath().toString() });

		assertLs("[test.txt, test.xmi]", tempDir);

		final Path xmiFile = tempDir.resolve("test.xmi");
		assertTrue(Files.exists(xmiFile));

		final String content = new String(Files.readAllBytes(xmiFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		assertTrue(content.contains("<uml:Model"));
		assertTrue(content.contains("</uml:Model>"));
		assertTrue(content.contains("<lifeline name=\"bob\""));
		assertTrue(content.contains("<lifeline name=\"alice\""));
		assertTrue(content.contains("<message name=\"hello\""));

	}

}
