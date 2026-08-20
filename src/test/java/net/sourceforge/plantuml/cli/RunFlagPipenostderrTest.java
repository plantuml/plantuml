package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

@Execution(ExecutionMode.SAME_THREAD)
@Isolated
class RunFlagPipenostderrTest extends AbstractCliTest {

	@StdIo({ "foo" })
	@Test
	void test1(StdOut out, StdErr err) throws Exception {
		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-pipe", "-svg" });
		});
		assertLineSplitContains(err.capturedString(), "ERROR", "1", "Syntax Error? (Assumed diagram type: sequence)");
		assertTrue(out.capturedString().contains("<svg"));
		assertTrue(out.capturedString().contains("foo"));
		assertTrue(out.capturedString().contains("Syntax Error?"));
		assertTrue(out.capturedString().contains("</svg>"));

	}

	@StdIo({ "foo" })
	@Test
	void test2(StdOut out, StdErr err) throws Exception {
		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-pipe", "-svg", "-pipenostderr" });
		});
		assertFalse(err.capturedString().contains("Syntax Error?"));

		assertLineSplitContains(out.capturedString(), "ERROR", "1", "Syntax Error? (Assumed diagram type: sequence)");

	}

	// https://github.com/plantuml/plantuml/issues/2820
	// The documented/historical spelling is "-pipeNoStderr" (camelCase). CLI flag
	// matching must stay case-insensitive like every other flag, or the option is
	// silently ignored and the full error image leaks onto stdout instead of being
	// replaced by the plain-text description.
	@StdIo({ "foo" })
	@Test
	void test3_mixedCaseFlag(StdOut out, StdErr err) throws Exception {
		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-pipe", "-svg", "-pipeNoStderr" });
		});
		assertFalse(err.capturedString().contains("Syntax Error?"));
		assertFalse(out.capturedString().contains("<svg"), "no error image should be written to stdout");

		assertLineSplitContains(out.capturedString(), "ERROR", "1", "Syntax Error? (Assumed diagram type: sequence)");

	}

}
