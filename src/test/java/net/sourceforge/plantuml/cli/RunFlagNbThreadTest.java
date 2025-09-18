package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.Run;

class RunFlagNbThreadTest extends AbstractCliTest {

	@StdIo
	@Test
	void testSvg(StdErr err) throws IOException, InterruptedException {
		Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-v", "-nbthread", "42", "-svg", file.toAbsolutePath().toString() });
		assertTrue(err.capturedString().contains("Using several threads: 42"));

		assertLs("[test.svg, test.txt]", tempDir);

	}

}
