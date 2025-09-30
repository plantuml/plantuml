package net.sourceforge.plantuml.cli;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagSyntaxTest extends AbstractCliTest {

	@StdIo({ "Hello", "World" })
	@Test
	void test1(StdOut out) throws Exception {
		assertExit(200, () -> {
			Run.main(new String[] { "-syntax" });
		});

		assertLineSplitEquals(out.capturedString(), "ERROR", "1", "Syntax Error? (Assumed diagram type: sequence)");
	}

	@StdIo({ "Alice->Bob: hello" })
	@Test
	void test2(StdOut out) throws Exception {
		Run.main(new String[] { "-syntax" });

		assertLineSplitEquals(out.capturedString(), "SEQUENCE", "(2 participants)");
	}

	@StdIo({ "class Dummy" })
	@Test
	void test3(StdOut out) throws Exception {

		Run.main(new String[] { "-syntax" });

		assertLineSplitEquals(out.capturedString(), "CLASS", "(1 entities)");
	}

}
