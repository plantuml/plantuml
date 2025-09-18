package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagPipeImageIndexTest extends AbstractCliTest {

	@StdIo({ "@startuml", "Alice->Bob: hello1", "newpage", "Alice->Bob: hello2", "@enduml" })
	@Test
	void test1(StdOut out) throws Exception {
		Run.main(new String[] { "-svg", "-pipe", "-pipeimageindex", "0" });

		assertTrue(out.capturedString().contains("<svg"));
		assertTrue(out.capturedString().contains("Alice"));
		assertTrue(out.capturedString().contains("Bob"));
		assertTrue(out.capturedString().contains("hello1"));
		assertFalse(out.capturedString().contains("hello2"));
		assertTrue(out.capturedString().contains("</svg>"));

	}

	@StdIo({ "@startuml", "Alice->Bob: hello1", "newpage", "Alice->Bob: hello2", "@enduml" })
	@Test
	void test2(StdOut out) throws Exception {
		Run.main(new String[] { "-svg", "-pipe", "-pipeimageindex", "1" });

		assertTrue(out.capturedString().contains("<svg"));
		assertTrue(out.capturedString().contains("Alice"));
		assertTrue(out.capturedString().contains("Bob"));
		assertFalse(out.capturedString().contains("hello1"));
		assertTrue(out.capturedString().contains("hello2"));
		assertTrue(out.capturedString().contains("</svg>"));

	}

}
