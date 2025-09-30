package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.Run;

class RunFlagFailFastTest extends AbstractCliTest {

	@StdIo
	@Test
	void test1(StdErr err) throws Exception {
		syntax_error(tempDir, "test.txt");

		assertExit(200, () -> {
			Run.main(new String[] { "-failfast", "-svg", tempDir.toAbsolutePath().toString() });
		});
		assertTrue(err.capturedString().contains("Error line 2 in file"));
		assertTrue(err.capturedString().contains("Some diagram description contains errors"));

		assertLs("[test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		final String content = new String(Files.readAllBytes(svgFile), java.nio.charset.StandardCharsets.UTF_8);

		assertTrue(content.contains("<svg"));
		assertTrue(content.contains("[From test.txt (line 2) ]"));
		assertTrue(content.contains("Syntax Error?"));

	}

	@StdIo
	@Test
	void testOneErrorOn100files(StdErr err) throws Exception {
		for (int i = 0; i < 100; i++)
			aliceBob_hello(tempDir, "test" + i + "0.txt");
		syntax_error(tempDir, "test551.txt");

		final Set<String> files0 = Files.list(tempDir).map(p -> p.getFileName().toString())
				.collect(Collectors.toCollection(TreeSet::new));
		assertEquals(101, files0.size());

		assertExit(200, () -> {
			Run.main(new String[] { "-failfast", "-svg", tempDir.toAbsolutePath().toString() });
		});
		assertTrue(err.capturedString().contains("Error line 2 in file"));
		assertTrue(err.capturedString().contains("Some diagram description contains errors"));

		final Set<String> files = Files.list(tempDir).map(p -> p.getFileName().toString())
				.collect(Collectors.toCollection(TreeSet::new));

		assertTrue(files.contains("test551.txt"));
		assertTrue(files.contains("test551.svg"));
		assertTrue(files.size() > 101);
		assertTrue(files.size() < 200);

	}

}
