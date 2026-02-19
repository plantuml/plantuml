package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunHelpTest extends AbstractCliTest {

	@StdIo
	@Test
	void testHelp(StdOut out) throws Exception {
		Run.main(new String[] { "-help" });

		final String output = out.capturedString();

		// Check header
		assertTrue(output.contains("plantuml - generate diagrams from plain text"));
		assertTrue(output.contains("Usage:"));
		assertTrue(output.contains("java -jar plantuml.jar"));

		// Check some well-known flags are documented
		assertTrue(output.contains("-pipe"));
		assertTrue(output.contains("-svg"));
		assertTrue(output.contains("-verbose"));
		assertTrue(output.contains("-help"));

		// Check footer
		assertTrue(output.contains("Examples:"));
		assertTrue(output.contains("Exit codes:"));
		assertTrue(output.contains("See also:"));
		assertTrue(output.contains("https://plantuml.com"));
	}

}
