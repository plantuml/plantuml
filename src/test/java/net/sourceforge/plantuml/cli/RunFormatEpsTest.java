package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFormatEpsTest extends AbstractCliTest {

	@Test
	void testEps() throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-teps", file.toAbsolutePath().toString() });

		assertLs("[test.eps, test.txt]", tempDir);

		final Path epsFile = tempDir.resolve("test.eps");
		assertTrue(Files.exists(epsFile));

		final String content = new String(Files.readAllBytes(epsFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("%!PS-Adobe-3.0 EPSF-3.0"));
		assertTrue(content.contains("%%Title: noTitle"));
		assertTrue(content.contains("%plantuml done"));
		assertFalse(content.contains("bob"));
		assertFalse(content.contains("alice"));
		assertFalse(content.contains("hello"));

	}

	@Test
	void testEpsText() throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-teps:text", file.toAbsolutePath().toString() });

		assertLs("[test.eps, test.txt]", tempDir);

		final Path epsFile = tempDir.resolve("test.eps");
		assertTrue(Files.exists(epsFile));

		final String content = new String(Files.readAllBytes(epsFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("%!PS-Adobe-3.0 EPSF-3.0"));
		assertTrue(content.contains("%%Title: noTitle"));
		assertTrue(content.contains("%plantuml done"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("hello"));

	}

}
