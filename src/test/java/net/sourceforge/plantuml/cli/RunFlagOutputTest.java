package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.sourceforge.plantuml.Run;

class RunFlagOutputTest extends AbstractCliTest {

	@TempDir
	Path outputDir;

	@Test
	void testSvgForceName() throws IOException, InterruptedException {
		Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-output", outputDir.toAbsolutePath().toString(), "-svg",
				file.toAbsolutePath().toString() });

		assertLs("[test.txt]", tempDir);
		assertLs("[test.svg]", outputDir);

		Path ofileSvg = outputDir.resolve("test.svg");

		assertTrue(Files.exists(ofileSvg));

		final String svgContent = new String(Files.readAllBytes(ofileSvg), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(svgContent.contains("<svg"));
		assertTrue(svgContent.contains("alice"));
		assertTrue(svgContent.contains("bob"));
		assertTrue(svgContent.contains("hello"));

		// System.out.println(svgContent.split("\n")[0]);

	}

}
