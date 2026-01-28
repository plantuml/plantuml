package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.Run;

class RunNoFileFound extends AbstractCliTest {

	@Test
	@StdIo
	void test1(StdErr err) throws Exception {
		final Path file = tempDir.resolve("i_do_not_exist.txt");

		assertExit(50, () -> {
			Run.main(new String[] { file.toAbsolutePath().toString() });
		});

		assertLs("[]", tempDir);
		assertTrue(err.capturedString().contains("No file found"), err.capturedString());

	}

	@Test
	@StdIo
	void test2(StdErr err) throws Exception {
		final Path file = tempDir.resolve("empty.txt");
		Files.writeString(file, "");

		assertExit(100, () -> {
			Run.main(new String[] { file.toAbsolutePath().toString() });
		});

		assertLs("[empty.txt]", tempDir);
		assertTrue(err.capturedString().contains("No diagram found"), err.capturedString());

	}

}
