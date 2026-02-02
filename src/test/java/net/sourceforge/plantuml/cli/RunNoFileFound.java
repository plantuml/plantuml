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

		assertExit(ExitStatus.ERROR_50_NO_FILE_FOUND, () -> {
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

		assertExit(ExitStatus.ERROR_100_NO_DIAGRAM_FOUND, () -> {
			Run.main(new String[] { file.toAbsolutePath().toString() });
		});

		assertLs("[empty.txt]", tempDir);
		assertTrue(err.capturedString().contains("No diagram found"), err.capturedString());
	}

	@Test
	@StdIo
	void test_subdirectory_does_not_exist(StdErr err) throws Exception {
		final Path subdir = tempDir.resolve("subdir_not_existing");

		assertExit(ExitStatus.ERROR_50_NO_FILE_FOUND, () -> {
			Run.main(new String[] { subdir.toAbsolutePath().toString() });
		});

		assertLs("[]", tempDir);
		assertTrue(err.capturedString().contains("No file found"), err.capturedString());
	}

	@Test
	@StdIo
	void test_subdirectory_empty(StdErr err) throws Exception {
		final Path subdir = tempDir.resolve("subdir_empty");
		Files.createDirectory(subdir);

		assertExit(ExitStatus.ERROR_50_NO_FILE_FOUND, () -> {
			Run.main(new String[] { subdir.toAbsolutePath().toString() });
		});

		assertLs("[subdir_empty]", tempDir);
		assertTrue(err.capturedString().contains("No file found"), err.capturedString());
	}

	@Test
	@StdIo
	void test_subdirectory_with_empty_file(StdErr err) throws Exception {
		final Path subdir = tempDir.resolve("subdir_with_empty_file");
		Files.createDirectory(subdir);
		final Path emptyFile = subdir.resolve("empty.txt");
		Files.writeString(emptyFile, "");

		assertExit(ExitStatus.ERROR_100_NO_DIAGRAM_FOUND, () -> {
			Run.main(new String[] { subdir.toAbsolutePath().toString() });
		});

		assertLs("[subdir_with_empty_file]", tempDir);
		assertLs("[empty.txt]", subdir);
		assertTrue(err.capturedString().contains("No diagram found"), err.capturedString());
	}

	@Test
	@StdIo
	void test_subdirectory_with_multiple_empty_files(StdErr err) throws Exception {
		final Path subdir = tempDir.resolve("subdir_with_multiple_empty_files");
		Files.createDirectory(subdir);
		Files.writeString(subdir.resolve("empty1.txt"), "");
		Files.writeString(subdir.resolve("empty2.txt"), "");

		assertExit(ExitStatus.ERROR_100_NO_DIAGRAM_FOUND, () -> {
			Run.main(new String[] { subdir.toAbsolutePath().toString() });
		});

		assertLs("[subdir_with_multiple_empty_files]", tempDir);
		assertLs("[empty1.txt, empty2.txt]", subdir);
		assertTrue(err.capturedString().contains("No diagram found"), err.capturedString());
	}

}
