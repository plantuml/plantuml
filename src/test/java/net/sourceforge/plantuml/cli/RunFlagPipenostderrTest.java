package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagPipenostderrTest extends AbstractCliTest {

	@StdIo({ "foo" })
	@Test
	void test1(StdOut out, StdErr err) throws Exception {
		assertExit(200, () -> {
			Run.main(new String[] { "-pipe", "-svg" });
		});
		assertLineSplitContains(err.capturedString(), "ERROR", "1", "Syntax Error?");
		assertTrue(out.capturedString().contains("<svg"));
		assertTrue(out.capturedString().contains("foo"));
		assertTrue(out.capturedString().contains("Syntax Error?"));
		assertTrue(out.capturedString().contains("</svg>"));

	}

	@StdIo({ "foo" })
	@Test
	void test2(StdOut out, StdErr err) throws Exception {
		assertExit(200, () -> {
			Run.main(new String[] { "-pipe", "-svg", "-pipenostderr" });
		});
		assertFalse(err.capturedString().contains("Syntax Error?"));

		assertLineSplitContains(out.capturedString(), "ERROR", "1", "Syntax Error?");

	}

}
