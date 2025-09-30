package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.Run;

class RunFlagCharsetTest extends AbstractCliTest {

	@StdIo
	@Test
	void testSvg(StdErr err) throws IOException, InterruptedException {
		Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-v", "-svg", file.toAbsolutePath().toString() });
		assertTrue(err.capturedString().contains("Using charset UTF-8"));

		assertLs("[test.svg, test.txt]", tempDir);

	}

	@StdIo
	@Test
	void testSvgUtf8(StdErr err) throws IOException, InterruptedException {
		Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-v", "-svg", "-charset", "UTF-8", file.toAbsolutePath().toString() });
		assertTrue(err.capturedString().contains("Using charset UTF-8"));

		assertLs("[test.svg, test.txt]", tempDir);

	}

	@StdIo
	@Test
	void testSvgUsAscii(StdErr err) throws IOException, InterruptedException {
		Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-v", "-svg", "-charset", "US-ASCII", file.toAbsolutePath().toString() });
		assertTrue(err.capturedString().contains("Using charset US-ASCII"));

		assertLs("[test.svg, test.txt]", tempDir);

	}

}
