package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.Run;

class RunSyntaxErrorTest extends AbstractCliTest {

	@Test
	@StdIo
	void testCaptureStderr(StdErr err) {
		System.err.println("This is an error!");
		assertTrue(err.capturedString().contains("This is an error!"));
	}

	@Test
	void testWriteFile() throws IOException {
		Path file = tempDir.resolve("test.txt");

		Files.writeString(file, String.join(System.lineSeparator(), //
				"Hello World", "Second line", "Third line"));

		String content = Files.readString(file);
		assertTrue(content.contains("Hello"));
		assertTrue(content.contains("Second line"));
	}

	@StdIo
	@Test
	void testSvg(StdErr err) throws Exception {
		Path file = syntax_error(tempDir, "test.txt");

		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-svg", file.toAbsolutePath().toString() });
		});
		assertTrue(err.capturedString().contains("Error line 2 in file"));
		assertTrue(err.capturedString().contains("Some diagram description contains errors"));

		final Set<String> files = Files.list(tempDir).map(p -> p.getFileName().toString())
				.collect(Collectors.toCollection(TreeSet::new));
		assertEquals("[test.svg, test.txt]", files.toString());

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("<svg"));
		assertTrue(content.contains("[From test.txt (line 2) ]"));
		assertTrue(content.contains("Syntax Error?"));

		// System.out.println(content.split("\n")[0]);

	}

}
