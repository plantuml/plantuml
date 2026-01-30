package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagPipeTest extends AbstractCliTest {

	@StdIo({ "@startuml", "Alice->Bob: hello", "@enduml" })
	@Test
	void test1(StdOut out) throws Exception {
		Run.main(new String[] { "-pipe", "-svg" });

		assertTrue(out.capturedString().contains("<svg"));
		assertTrue(out.capturedString().contains("Alice"));
		assertTrue(out.capturedString().contains("Bob"));
		assertTrue(out.capturedString().contains("hello"));
		assertTrue(out.capturedString().contains("</svg>"));
	}

	@StdIo({ "@startuml", "Alice->Bob: hello1", "@enduml", "@startuml", "Alice->Bob: hello2", "@enduml" })
	@Test
	void test2(StdOut out) throws Exception {
		Run.main(new String[] { "-pipe", "-svg" });

		assertTrue(out.capturedString().contains("<svg"));
		assertTrue(out.capturedString().contains("Alice"));
		assertTrue(out.capturedString().contains("Bob"));
		assertTrue(out.capturedString().contains("hello1"));
		assertTrue(out.capturedString().contains("hello2"));
		assertTrue(out.capturedString().contains("</svg>"));
	}

	@StdIo({ "foo" })
	@Test
	void test3(StdOut out, StdErr err) throws Exception {
		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-pipe", "-svg" });
		});
		assertLineSplitContains(err.capturedString(), "ERROR", "1", "Syntax Error?");
		assertTrue(out.capturedString().contains("<svg"));
		assertTrue(out.capturedString().contains("foo"));
		assertTrue(out.capturedString().contains("Syntax Error?"));
		assertTrue(out.capturedString().contains("</svg>"));

	}

}
