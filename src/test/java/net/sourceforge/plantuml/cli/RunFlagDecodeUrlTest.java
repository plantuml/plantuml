package net.sourceforge.plantuml.cli;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagDecodeUrlTest extends AbstractCliTest {

	@Test
	@StdIo
	void testSvg(StdOut out) throws IOException, InterruptedException {

		Run.main(new String[] { "-decodeurl", "SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKaX9JSvFILKeBSfHA4ajBk5oICrB0Ie30000" });
		assertLineSplitContains(out.capturedString(), "@startuml", "Bob -> Alice : decode url test", "@enduml");

	}
}
