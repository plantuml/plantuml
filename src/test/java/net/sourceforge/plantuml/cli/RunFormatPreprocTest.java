package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFormatPreprocTest extends AbstractCliTest {

	// wip @Test
	void testSvg() throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-f", "preproc", file.toAbsolutePath().toString() });

		assertLs("[test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("<svg "));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("hello"));
		assertTrue(content.contains("data-diagram-type=\"SEQUENCE\""));
	}

}
