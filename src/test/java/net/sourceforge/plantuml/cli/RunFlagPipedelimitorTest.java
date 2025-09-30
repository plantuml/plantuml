package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagPipedelimitorTest extends AbstractCliTest {


	@StdIo({ "@startuml", "Alice->Bob: hello1", "@enduml", "@startuml", "Alice->Bob: hello2", "@enduml" })
	@Test
	void test3(StdOut out) throws Exception {
		Run.main(new String[] { "-pipe", "-svg", "-pipedelimitor", "$$$" });

		assertTrue(out.capturedString().contains("<svg"));
		assertTrue(out.capturedString().contains("Alice"));
		assertTrue(out.capturedString().contains("Bob"));
		assertTrue(out.capturedString().contains("hello1"));
		assertTrue(out.capturedString().contains("hello2"));
		assertTrue(out.capturedString().contains("</svg>$$$"));
		int idx1 = out.capturedString().indexOf("</svg>$$$");
		int idx2 = out.capturedString().lastIndexOf("</svg>$$$");
		// Two times
		assertNotEquals(idx1, idx2);
	}

}
