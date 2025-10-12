package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFormatPreprocTest extends AbstractCliTest {

	@Test
	void testPreproc() throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-f", "preproc", "--define", "hello=byebye", file.toAbsolutePath().toString() });

		assertLs("[test.preproc, test.txt]", tempDir);

		final Path resultFile = tempDir.resolve("test.preproc");
		assertTrue(Files.exists(resultFile));

		final String content = new String(Files.readAllBytes(resultFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("alice->bob : byebye"));
	}

	@Test
	void testPreproc2() throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "--preproc", "--define", "hello=byebye", file.toAbsolutePath().toString() });

		assertLs("[test.preproc, test.txt]", tempDir);

		final Path resultFile = tempDir.resolve("test.preproc");
		assertTrue(Files.exists(resultFile));

		final String content = new String(Files.readAllBytes(resultFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("alice->bob : byebye"));
	}

}
