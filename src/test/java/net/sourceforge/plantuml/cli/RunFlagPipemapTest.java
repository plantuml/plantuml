package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagPipemapTest extends AbstractCliTest {

	@StdIo({ "@startuml", "participant Alice [[http://plantuml.com/sequence]]", "Alice->Bob: hello", "@enduml" })
	@Test
	void test1(StdOut out) throws Exception {
		Run.main(new String[] { "-pipemap" });
		assertTrue(out.capturedString().contains("<map id=\"plantuml_map\" name=\"plantuml_map\">"));
		assertTrue(out.capturedString().contains("href=\"http://plantuml.com/sequence\""));
		assertTrue(out.capturedString().contains("title=\"http://plantuml.com/sequence\""));

	}

	@StdIo({ "foo" })
	@Test
	void test2(StdOut out) throws Exception {
		assertExit(ExitStatus.ERROR_200_SOME_DIAGRAMS_HAVE_ERROR, () -> {
			Run.main(new String[] { "-pipemap" });
		});

		assertFalse(out.capturedString().contains("<map id=\"plantuml_map\" name=\"plantuml_map\">"));

	}

}
