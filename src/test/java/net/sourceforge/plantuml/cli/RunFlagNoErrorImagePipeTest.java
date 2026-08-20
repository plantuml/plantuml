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

// https://github.com/plantuml/plantuml/issues/2820
// "--no-error-image" (alias "-noerror") was only ever wired to the file-based
// rendering path (Run.java -> SourceFileReaderAbstract#setNoErrorImage): Pipe.java
// never looked at CliFlag.NO_ERROR_IMAGE, so the flag was silently a no-op under
// "-pipe" regardless of how it was spelled. These tests pin the fix: unlike
// "-pipeNoStderr", "--no-error-image" only suppresses the error image on stdout and
// makes no claim about stderr, so the per-diagram info line must stay on stderr.
@Execution(ExecutionMode.SAME_THREAD)
@Isolated
class RunFlagNoErrorImagePipeTest extends AbstractCliTest {

	@StdIo({ "foo" })
	@Test
	void test1_longForm(StdOut out, StdErr err) throws Exception {
		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-pipe", "-svg", "--no-error-image" });
		});

		assertFalse(out.capturedString().contains("<svg"), "no error image should be written to stdout");
		assertTrue(err.capturedString().contains("Syntax Error?"), "unlike -pipeNoStderr, stderr is untouched");
	}

	@StdIo({ "foo" })
	@Test
	void test2_deprecatedAlias(StdOut out, StdErr err) throws Exception {
		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-pipe", "-svg", "-noerror" });
		});

		assertFalse(out.capturedString().contains("<svg"), "no error image should be written to stdout");
		assertTrue(err.capturedString().contains("Syntax Error?"), "unlike -pipeNoStderr, stderr is untouched");
	}

	@StdIo({ "@startuml", "Alice->Bob: hello", "@enduml" })
	@Test
	void test3_validDiagramIsUnaffected(StdOut out) throws Exception {
		Run.main(new String[] { "-pipe", "-svg", "--no-error-image" });

		assertTrue(out.capturedString().contains("<svg"));
		assertTrue(out.capturedString().contains("Alice"));
		assertTrue(out.capturedString().contains("Bob"));
		assertTrue(out.capturedString().contains("hello"));
		assertTrue(out.capturedString().contains("</svg>"));
	}

	@StdIo({ "foo" })
	@Test
	void test4_combinedWithPipeNoStderr(StdOut out, StdErr err) throws Exception {
		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-pipe", "-svg", "--no-error-image", "-pipeNoStderr" });
		});

		assertFalse(out.capturedString().contains("<svg"), "no error image should be written to stdout");
		assertFalse(err.capturedString().contains("Syntax Error?"), "-pipeNoStderr still reroutes info to stdout");
		assertLineSplitContains(out.capturedString(), "ERROR", "1", "Syntax Error? (Assumed diagram type: sequence)");
	}

}
