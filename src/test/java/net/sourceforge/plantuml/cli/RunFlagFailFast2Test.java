package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.Run;

class RunFlagFailFast2Test extends AbstractCliTest {

	@StdIo
	@Test
	void test1(StdErr err) throws Exception {
		syntax_error(tempDir, "test.txt");

		assertExit(200, () -> {
			Run.main(new String[] { "-failfast2", "-svg", tempDir.toAbsolutePath().toString() });
		});
		assertTrue(err.capturedString().contains("Some diagram description contains errors"));

		assertLs("[test.txt]", tempDir);

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
			Run.main(new String[] { "-failfast2", "-svg", tempDir.toAbsolutePath().toString() });
		});
		assertTrue(err.capturedString().contains("Some diagram description contains errors"));

		final Set<String> files = Files.list(tempDir).map(p -> p.getFileName().toString())
				.collect(Collectors.toCollection(TreeSet::new));

		assertTrue(files.contains("test551.txt"));
		assertTrue(files.size() == 101);

	}

}
