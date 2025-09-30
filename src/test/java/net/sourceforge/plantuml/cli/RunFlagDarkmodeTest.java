package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFlagDarkmodeTest extends AbstractCliTest {

	@Test
	void test1() throws IOException, InterruptedException {
		aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-svg", tempDir.toAbsolutePath().toString() });
		assertLs("[test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");

		final String normal = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);
		assertTrue(normal.contains("#E2E2F0"));
		assertFalse(normal.contains("#222222"));

		Run.main(new String[] { "-svg", "-darkmode", tempDir.toAbsolutePath().toString() });
		assertLs("[test.svg, test.txt]", tempDir);

		final String darkmode = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);
		assertFalse(darkmode.contains("#E2E2F0"));
		assertTrue(darkmode.contains("#222222"));

	}
}
