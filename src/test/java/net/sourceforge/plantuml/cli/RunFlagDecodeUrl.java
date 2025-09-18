package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagDecodeUrl extends AbstractCliTest {

	@Test
	@StdIo
	void testSvg(StdOut out) throws IOException, InterruptedException {

		Run.main(new String[] { "-decodeurl", "SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKaX9JSvFILKeBSfHA4ajBk5oICrB0Ie30000" });
		assertTrue(out.capturedString().contains("@startuml"));
		assertTrue(out.capturedString().contains("Bob -> Alice : decode url test"));
		assertTrue(out.capturedString().contains("@enduml"));
	}

}
