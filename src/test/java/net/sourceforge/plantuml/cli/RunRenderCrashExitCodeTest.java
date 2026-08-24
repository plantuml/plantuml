package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

// https://github.com/plantuml/plantuml/issues/258
//
// A diagram can parse without error and still fail to *render*: GraphViz/dot can crash,
// time out, or return something PlantUML cannot use. That failure has always been caught
// and turned into a "crash report" image (see UgDiagram.exportDiagram), and tagged with a
// non-OK status - but until now nothing looked at that status when computing the process
// exit code, so PlantUML exited 0 while quietly writing a stack trace image instead of the
// requested diagram. That made it impossible for another process driving PlantUML (the
// exact scenario from the original report) to detect that rendering had actually failed.
//
// Reproducing a *real* GraphViz crash needs a broken dot binary, which is awkward and
// platform-dependent to set up in a unit test, and isn't guaranteed to even be exercised
// since PlantUML silently falls back to its bundled Smetana/VizJs engines whenever a real
// "dot" is unavailable or unhealthy. GlobalConfigKey.SIMULATE_RENDER_CRASH_FOR_TESTS makes
// UgDiagram.exportDiagram() throw exactly where a real rendering failure would be caught,
// regardless of which layout engine would have been used, so these tests exercise the real
// crash-handling and exit-code path deterministically, without needing GraphViz at all -
// the fixture diagrams here are plain sequence diagrams.
class RunRenderCrashExitCodeTest extends AbstractCliTest {

	@AfterEach
	void resetSimulatedCrash() {
		// GlobalConfig is a process-wide singleton: never leave this flag set for other
		// tests, whether this test passed or failed.
		GlobalConfig.getInstance().put(GlobalConfigKey.SIMULATE_RENDER_CRASH_FOR_TESTS, false);
	}

	@Test
	void fileMode_renderCrash_exitsWithError() throws Exception {
		Path file = aliceBob_hello(tempDir, "test.txt");
		GlobalConfig.getInstance().put(GlobalConfigKey.SIMULATE_RENDER_CRASH_FOR_TESTS, true);

		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-svg", file.toAbsolutePath().toString() });
		});

		// The crash is still reported as an image: a file is written, it just contains a
		// crash report instead of the requested diagram - exactly what a real broken
		// GraphViz install produces today, and exactly why the exit code must not be 0.
		assertLs("[test.svg, test.txt]", tempDir);
		final Path svgFile = tempDir.resolve("test.svg");
		final String content = Files.readString(svgFile);
		assertTrue(content.contains("has crashed"));
		assertTrue(content.contains("Simulated rendering crash"));
	}

	@Test
	void fileMode_normalDiagram_stillExitsCleanly() throws Exception {
		Path file = aliceBob_hello(tempDir, "test.txt");
		// GlobalConfigKey.SIMULATE_RENDER_CRASH_FOR_TESTS left at its default (false):
		// rendering succeeds, so Run.main must return normally - nothing should call
		// Exit.exit(...) at all, i.e. the fix must not make healthy diagrams fail.
		Run.main(new String[] { "-svg", file.toAbsolutePath().toString() });

		assertLs("[test.svg, test.txt]", tempDir);
		final Path svgFile = tempDir.resolve("test.svg");
		final String content = Files.readString(svgFile);
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("hello"));
	}

	@StdIo({ "@startuml", "alice->bob: hello", "@enduml" })
	@Test
	void pipeMode_renderCrash_exitsWithError(StdOut out) throws Exception {
		GlobalConfig.getInstance().put(GlobalConfigKey.SIMULATE_RENDER_CRASH_FOR_TESTS, true);

		// -pipe is the exact use case from the original bug report: PlantUML driven from
		// another process, which needs a reliable exit code to know the output is bad.
		//
		// Note: we don't assert on captured stderr here. The crash is logged through
		// java.util.logging's ConsoleHandler (see Logme), which binds to System.err only
		// once, the first time the JVM touches that class - if some other test in the
		// suite triggers that before this one, the handler keeps writing to the real,
		// un-redirected stderr for the rest of the run, making captured-stderr assertions
		// order-dependent. The crash is still fully observable, and unambiguous, through
		// the exit code and through the crash report image written to stdout below.
		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-pipe", "-svg" });
		});

		assertTrue(out.capturedString().contains("has crashed"));
		assertTrue(out.capturedString().contains("Simulated rendering crash"));
	}

	@StdIo({ "@startuml", "alice->bob: hello", "@enduml" })
	@Test
	void pipeMode_normalDiagram_stillExitsCleanly(StdOut out) throws Exception {
		Run.main(new String[] { "-pipe", "-svg" });

		assertTrue(out.capturedString().contains("<svg"));
		assertTrue(out.capturedString().contains("alice"));
		assertTrue(out.capturedString().contains("bob"));
		assertTrue(out.capturedString().contains("hello"));
	}

}
