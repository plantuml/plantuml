package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.Run;

class RunFlagDebugSvekTest extends AbstractCliTest {

	@Test
	@StdIo
	void testSvg(StdErr err) throws IOException, InterruptedException {
		Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-v", "-debug_svek", "-svg", file.toAbsolutePath().toString() });

		assertLs("[test.svg, test.txt]", tempDir);
		
		assertTrue(err.capturedString().contains("setDebugSvek: true"));


	}

}
