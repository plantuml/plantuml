package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFlagFilenameTest extends AbstractCliTest {


	@Test
	void testSvg() throws IOException, InterruptedException {
		Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-svg", "-filename", "foo", file.toAbsolutePath().toString() });

		assertLs("[test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String svgContent = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(svgContent.contains("<svg"));
		assertTrue(svgContent.contains("alice"));
		assertTrue(svgContent.contains("bob"));
		assertTrue(svgContent.contains("hello"));

		// System.out.println(svgContent.split("\n")[0]);

	}


}
