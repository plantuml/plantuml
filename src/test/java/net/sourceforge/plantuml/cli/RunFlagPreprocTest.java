package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;

import net.sourceforge.plantuml.Run;


@Execution(ExecutionMode.SAME_THREAD)
@Isolated
class RunFlagPreprocTest extends AbstractCliTest {

	@Test
	void test1() throws IOException, InterruptedException {
		aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-preproc", tempDir.toAbsolutePath().toString() });

		assertLs("[test.preproc, test.txt]", tempDir);

		final Path preprocFile = tempDir.resolve("test.preproc");
		assertTrue(Files.exists(preprocFile));

		final String preproc = new String(Files.readAllBytes(preprocFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(preproc.contains("alice->bob : hello"), preproc);

	}

	@Test
	void test2() throws IOException, InterruptedException {
		aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-Dhello=bye", "-preproc", tempDir.toAbsolutePath().toString() });

		assertLs("[test.preproc, test.txt]", tempDir);

		final Path preprocFile = tempDir.resolve("test.preproc");
		assertTrue(Files.exists(preprocFile));

		final String preproc = new String(Files.readAllBytes(preprocFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(preproc.contains("alice->bob : bye"), preproc);

	}

}
