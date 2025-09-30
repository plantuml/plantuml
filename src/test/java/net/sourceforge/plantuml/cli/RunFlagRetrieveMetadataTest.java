package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagRetrieveMetadataTest extends AbstractCliTest {

	@Test
	@StdIo
	void testSvg(StdOut out) throws IOException, InterruptedException {
		Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-svg", file.toAbsolutePath().toString() });

		assertLs("[test.svg, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		assertFalse(out.capturedString().contains("test.svg"));
		assertLineSplitNotContains(out.capturedString(), "@startuml", "alice->bob : hello", "@enduml");

		Run.main(new String[] { "-metadata", svgFile.toAbsolutePath().toString() });

		assertTrue(out.capturedString().contains("test.svg"));
		assertLineSplitContains(out.capturedString(), "@startuml", "alice->bob : hello", "@enduml");
	}

	@Test
	@StdIo
	void testPng(StdOut out) throws IOException, InterruptedException {
		Path file = aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-png", file.toAbsolutePath().toString() });

		assertLs("[test.png, test.txt]", tempDir);

		final Path svgFile = tempDir.resolve("test.png");
		assertTrue(Files.exists(svgFile));

		assertFalse(out.capturedString().contains("test.png"));
		assertLineSplitNotContains(out.capturedString(), "@startuml", "alice->bob : hello", "@enduml");

		Run.main(new String[] { "-metadata", svgFile.toAbsolutePath().toString() });

		assertTrue(out.capturedString().contains("test.png"));
		assertLineSplitContains(out.capturedString(), "@startuml", "alice->bob : hello", "@enduml");
	}

}
