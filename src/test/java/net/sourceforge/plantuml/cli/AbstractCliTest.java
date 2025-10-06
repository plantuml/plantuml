package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

public class AbstractCliTest {

	@TempDir
	Path tempDir;

	@FunctionalInterface
	public interface ThrowingRunnable {
		void run() throws Exception;
	}

	@BeforeEach
	void setUp() {
		System.setProperty(Exit.DISABLE_PROPERTY, "true");
		GlobalConfig.getInstance().put(GlobalConfigKey.VERBOSE, false);
		GlobalConfig.getInstance().put(GlobalConfigKey.FILE_SEPARATOR, null);
	}

	@AfterEach
	void tearDown() {
		GlobalConfig.getInstance().put(GlobalConfigKey.VERBOSE, false);
		GlobalConfig.getInstance().put(GlobalConfigKey.FILE_SEPARATOR, null);
	}

	public int assertExitCode(ThrowingRunnable action) throws Exception {
		try {
			action.run();
			fail("Expected System.exit(...) to be called, but it was not.");
			return -1; // unreachable
		} catch (Exit.ExitRequest ex) {
			return ex.status;
		}
	}

	public void assertExit(int expectedCode, ThrowingRunnable action) throws Exception {
		final int code = assertExitCode(action);
		assertEquals(expectedCode, code, "Unexpected System.exit code");
	}

	public Path aliceBob_hello(Path dir, String fileName) throws IOException {
		final Path file = tempDir.resolve(fileName);
		Files.writeString(file, String.join(System.lineSeparator(), "@startuml", "alice->bob : hello", "@enduml"));
		return file;
	}

	public Path syntax_error(Path dir, String fileName) throws IOException {
		final Path file = tempDir.resolve(fileName);
		Files.writeString(file, String.join(System.lineSeparator(), "@startuml", "error", "@enduml"));
		return file;
	}

	public void assertLs(String list, Path dir) throws IOException {
		final Set<String> files = Files.list(dir).map(p -> p.getFileName().toString())
				.collect(Collectors.toCollection(TreeSet::new));
		assertEquals(list, files.toString());
	}

	public void assertLineSplitEquals(String actual, String... expectedLines) {
		final List<String> lines = Arrays.asList(actual.split("\\R"));

		if (lines.size() != expectedLines.length)
			throw new AssertionError(lines.toString());

		for (int i = 0; i < expectedLines.length; i++)
			assertEquals(expectedLines[i], lines.get(i), "Line " + (i + 1) + " does not match.");
	}

	public void assertLineSplitContains(String actual, String... expectedLines) {
		final List<String> lines = Arrays.asList(actual.split("\\R"));
		final List<String> expected = Arrays.asList(expectedLines);

		for (int i = 0; i + expected.size() <= lines.size(); i++)
			if (lines.subList(i, i + expected.size()).equals(expected))
				return;
		throw new AssertionError(lines);
	}

	public void assertLineSplitNotContains(String actual, String... expectedLines) {
		final List<String> lines = Arrays.asList(actual.split("\\R"));
		final List<String> expected = Arrays.asList(expectedLines);

		for (int i = 0; i + expected.size() <= lines.size(); i++)
			if (lines.subList(i, i + expected.size()).equals(expected))
				throw new AssertionError(lines);
	}

	public String cleanControlChars(String s) {
		return s.replaceAll("\\p{Cntrl}", "");
	}

	public void assertEqualsButControlChars(String expected, String actual) {
		assertEquals(cleanControlChars(expected), cleanControlChars(actual));

	}

}
