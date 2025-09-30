package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagFiledirTest extends AbstractCliTest {

	@StdIo({ "@startuml", "Alice->Bob: hello %dirpath()", "@enduml" })
	@Test
	void testDirpath(StdOut out) throws Exception {
		Run.main(new String[] { "-pipe", "-svg", "-filedir", "foo" });

		assertTrue(out.capturedString().contains("<svg"));
		assertTrue(out.capturedString().contains("Alice"));
		assertTrue(out.capturedString().contains("Bob"));
		assertTrue(out.capturedString().contains("hello foo"));
		assertTrue(out.capturedString().contains("</svg>"));
	}

}
