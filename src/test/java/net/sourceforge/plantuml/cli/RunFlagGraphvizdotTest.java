package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import net.sourceforge.plantuml.Run;

@Isolated
class RunFlagGraphvizdotTest extends AbstractCliTest {

	@Test
	void testSvg() throws IOException, InterruptedException {
		Path file = aliceBob_hello(tempDir, "test.txt");

		// hack to ensure that setDotExecutable is actually called, but we don't want to
		// modify the internal configuration

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> Run.main(new String[] { "-graphviz_dot", "x", "-svg", file.toAbsolutePath().toString() }));

		assertTrue(ex.getMessage().contains("setDotExecutable failure"));
	}

}
