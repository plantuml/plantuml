package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagComputeUrlTest extends AbstractCliTest {

	@Test
	@StdIo
	void testSvg(StdOut out) throws IOException, InterruptedException {
		Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-computeurl", file.toAbsolutePath().toString() });

		assertLs("[test.txt]", tempDir);

		assertTrue(out.capturedString().contains("Iyp9J4xLjKlAJrAmKiX8pSd91m00"));

	}

}
