package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunFormatObfuscateTest extends AbstractCliTest {

	@Test
	void testObfuscate() throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-f", "obfuscate", "--define", "hello=byebye class object", file.toAbsolutePath().toString() });

		assertLs("[test.obfuscate, test.txt]", tempDir);

		final Path resultFile = tempDir.resolve("test.obfuscate");
		assertTrue(Files.exists(resultFile));

		final String content = new String(Files.readAllBytes(resultFile), java.nio.charset.StandardCharsets.UTF_8);

		assertFalse(content.contains("alice->bob"));
		assertFalse(content.contains("byebye"));
		assertTrue(content.contains("->"));
		assertTrue(content.contains("class object"));
	}


	@Test
	void testObfuscate2() throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "--obfuscate", "--define", "hello=byebye class object", file.toAbsolutePath().toString() });

		assertLs("[test.obfuscate, test.txt]", tempDir);

		final Path resultFile = tempDir.resolve("test.obfuscate");
		assertTrue(Files.exists(resultFile));

		final String content = new String(Files.readAllBytes(resultFile), java.nio.charset.StandardCharsets.UTF_8);

		assertFalse(content.contains("alice->bob"));
		assertFalse(content.contains("byebye"));
		assertTrue(content.contains("->"));
		assertTrue(content.contains("class object"));
	}


}
