package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.Run;

class RunFlagCheckMetadataTest extends AbstractCliTest {

	@Test
	@StdIo
	void testSvg(StdErr err) throws IOException, InterruptedException {
		aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-v", "-svg", "-checkmetadata", tempDir.toAbsolutePath().toString() });
		assertFalse(err.capturedString().contains("Skipping  because metadata has not changed"));

		assertLs("[test.svg, test.txt]", tempDir);

		aliceBob_hello(tempDir, "test.txt");
		Run.main(new String[] { "-v", "-svg", "-checkmetadata", tempDir.toAbsolutePath().toString() });
		assertTrue(err.capturedString().contains("Skipping  because metadata has not changed"));

		assertLs("[test.svg, test.txt]", tempDir);

	}

	@Test
	@StdIo
	void testSvgChange(StdErr err) throws IOException, InterruptedException {
		aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-v", "-svg", "-checkmetadata", tempDir.toAbsolutePath().toString() });
		assertFalse(err.capturedString().contains("Skipping  because metadata has not changed"));

		assertLs("[test.svg, test.txt]", tempDir);

		final Path svg = tempDir.resolve("test.svg");
		assertTrue(new String(Files.readAllBytes(svg), java.nio.charset.StandardCharsets.UTF_8).contains("hello"));

		Files.writeString(tempDir.resolve("test.txt"),
				String.join(System.lineSeparator(), "@startuml", "alice->bob : bye changed", "@enduml"));

		Run.main(new String[] { "-v", "-svg", "-checkmetadata", tempDir.toAbsolutePath().toString() });

		assertFalse(new String(Files.readAllBytes(svg), java.nio.charset.StandardCharsets.UTF_8).contains("hello"));
		assertTrue(
				new String(Files.readAllBytes(svg), java.nio.charset.StandardCharsets.UTF_8).contains("bye changed"));

		assertFalse(err.capturedString().contains("Skipping  because metadata has not changed"));

		assertLs("[test.svg, test.txt]", tempDir);

	}

	@Test
	@StdIo
	void testPng(StdErr err) throws IOException, InterruptedException {
		aliceBob_hello(tempDir, "test.txt");
		Run.main(new String[] { "-v", "-png", "-checkmetadata", tempDir.toAbsolutePath().toString() });
		assertFalse(err.capturedString().contains("Skipping  because metadata has not changed"));

		assertLs("[test.png, test.txt]", tempDir);

		aliceBob_hello(tempDir, "test.txt");
		Run.main(new String[] { "-v", "-png", "-checkmetadata", tempDir.toAbsolutePath().toString() });
		assertTrue(err.capturedString().contains("Skipping  because metadata has not changed"));

		assertLs("[test.png, test.txt]", tempDir);

	}

	@Test
	@StdIo
	void testPngChange(StdErr err) throws IOException, InterruptedException {
		aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-v", "-png", "-checkmetadata", tempDir.toAbsolutePath().toString() });
		assertFalse(err.capturedString().contains("Skipping  because metadata has not changed"));

		assertLs("[test.png, test.txt]", tempDir);

		final Path file = tempDir.resolve("test.txt");
		Files.writeString(file,
				String.join(System.lineSeparator(), "@startuml", "alice->bob : hello changed", "@enduml"));

		Run.main(new String[] { "-v", "-png", "-checkmetadata", tempDir.toAbsolutePath().toString() });
		assertFalse(err.capturedString().contains("Skipping  because metadata has not changed"));

		assertLs("[test.png, test.txt]", tempDir);

	}

}
