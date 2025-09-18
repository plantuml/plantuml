package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFlagThemeTest extends AbstractCliTest {

	@Test
	void testThemeFoo() throws Exception {
		aliceBob_hello(tempDir, "test.txt");

		assertExit(200, () -> {
			Run.main(new String[] { "-svg", "-theme", "foo", tempDir.toAbsolutePath().toString() });
		});

		assertLs("[test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String svgContent = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(svgContent.contains("<svg"));
		assertTrue(svgContent.contains("Cannot load foo"));
		assertTrue(svgContent.contains("!theme foo"));
	}

	@Test
	void testThemeAmiga() throws Exception {
		aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-svg", "-theme", "amiga", tempDir.toAbsolutePath().toString() });

		assertLs("[test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String svgContent = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(svgContent.contains("<svg"));
		assertTrue(svgContent.contains("alice"));
		assertTrue(svgContent.contains("bob"));
		assertTrue(svgContent.contains("hello"));
	}

}
