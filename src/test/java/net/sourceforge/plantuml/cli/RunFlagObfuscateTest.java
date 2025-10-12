package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
class RunFlagObfuscateTest extends AbstractCliTest {

	@Test
	void test1() throws IOException, InterruptedException {
		aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "--obfuscate", tempDir.toAbsolutePath().toString() });

		assertLs("[test.obfuscate, test.txt]", tempDir);

		final Path preprocFile = tempDir.resolve("test.obfuscate");
		assertTrue(Files.exists(preprocFile));

		final String preproc = new String(Files.readAllBytes(preprocFile), java.nio.charset.StandardCharsets.UTF_8);

		// Should be obfuscated
		assertFalse(preproc.contains("alice->bob : hello"), preproc);

	}

	@Test
	void test2() throws IOException, InterruptedException {
		final Path file = tempDir.resolve("test.txt");
		Files.writeString(file, String.join(System.lineSeparator(), "@startuml", "italic->fafafa : hello", "@enduml"));

		Run.main(new String[] { "--obfuscate", tempDir.toAbsolutePath().toString() });

		assertLs("[test.obfuscate, test.txt]", tempDir);

		final Path preprocFile = tempDir.resolve("test.obfuscate");
		assertTrue(Files.exists(preprocFile));

		final String preproc = new String(Files.readAllBytes(preprocFile), java.nio.charset.StandardCharsets.UTF_8);

		// italic is not obfuscated (it's a keyword)
		// fafafa is not obfuscated (it's a potential color)
		assertTrue(preproc.contains("italic->fafafa"), preproc);

		// Should be obfuscated
		assertFalse(preproc.contains("hello"), preproc);

	}

}
