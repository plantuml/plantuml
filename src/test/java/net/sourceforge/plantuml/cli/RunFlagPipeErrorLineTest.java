package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.Run;

/**
 * -pipe reports "Error line N in standard input", N being counted from the start
 * of the input as in file mode (https://github.com/plantuml/plantuml/issues/2997).
 */
@Execution(ExecutionMode.SAME_THREAD)
@Isolated
class RunFlagPipeErrorLineTest extends AbstractCliTest {

	@StdIo({ "@startuml", "error", "@enduml" })
	@Test
	void singleDiagram(StdErr err) throws Exception {
		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-pipe", "-tutxt" });
		});

		assertTrue(err.capturedString().contains("Error line 2 in standard input"));
	}

	@StdIo({ "' a comment", "", "@startuml", "Alice->Bob: hello", "@enduml", "@startuml", "Alice->Bob: hello",
			"error", "@enduml" })
	@Test
	void lineIsCountedFromTheStartOfTheInput(StdErr err) throws Exception {
		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-pipe", "-tutxt" });
		});

		assertTrue(err.capturedString().contains("Error line 8 in standard input"));
	}

	@StdIo({ "@startmindmap", "<style>", "root {", "  LineColor red", "", "  // a comment", "  LineThickness abc", "}",
			"</style>", "* root", "@endmindmap" })
	@Test
	void errorInStyleBlockPointsAtTheFaultyLine(StdErr err) throws Exception {
		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-pipe", "-tutxt" });
		});

		// Not line 2 (<style>) nor line 9 (</style>)
		assertTrue(err.capturedString().contains("Error line 7 in standard input"));
		assertTrue(err.capturedString().contains("Invalid value 'abc' for LineThickness"));
	}

	@StdIo({ "@startuml", "error", "@enduml" })
	@Test
	void nothingOnStderrWithPipeNoStderr(StdErr err) throws Exception {
		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-pipe", "-pipeNoStderr", "-tutxt" });
		});

		assertFalse(err.capturedString().contains("Error line"));
	}

	@StdIo({ "@startuml", "Alice->Bob: hello", "@enduml" })
	@Test
	void noErrorNoLine(StdErr err) throws Exception {
		Run.main(new String[] { "-pipe", "-tutxt" });

		assertFalse(err.capturedString().contains("Error line"));
	}

}
