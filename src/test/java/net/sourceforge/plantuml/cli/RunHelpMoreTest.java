package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunHelpMoreTest extends AbstractCliTest {

	@StdIo
	@Test
	void testHelpMore(StdOut out) throws Exception {
		Run.main(new String[] { "--help-more" });

		final String output = out.capturedString();

		// Check header (same as -help)
		assertTrue(output.contains("plantuml - generate diagrams from plain text"));
		assertTrue(output.contains("Usage:"));

		// Check that --help-more shows additional flags not in -help
		assertTrue(output.contains("-pipe"));
		assertTrue(output.contains("-svg"));

		// Check footer
		assertTrue(output.contains("Examples:"));
		assertTrue(output.contains("https://plantuml.com"));
	}

}
