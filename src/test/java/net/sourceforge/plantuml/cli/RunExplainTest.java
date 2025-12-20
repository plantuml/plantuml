package net.sourceforge.plantuml.cli;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunExplainTest extends AbstractCliTest {

	@Test
	void testExplain() throws IOException, InterruptedException {
		final Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "--explain", file.toAbsolutePath().toString() });
	}

}
