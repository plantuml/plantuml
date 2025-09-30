package net.sourceforge.plantuml.cli;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import net.sourceforge.plantuml.Run;

class RunFlagPipePreprocTest extends AbstractCliTest {

	@StdIo({ "@startuml", "Alice->Bob: hello", "@enduml" })
	@Test
	void test1(StdOut out) throws Exception {
		Run.main(new String[] { "-pipe", "-preproc" });

		assertLineSplitEquals(out.capturedString(), "@startuml", "Alice->Bob: hello", "@enduml");
	}

	@StdIo({ "@startuml", "Alice->Bob: hello", "@enduml" })
	@Test
	void test2(StdOut out) throws IOException, InterruptedException {
		Run.main(new String[] { "-Dhello=bye", "-pipe", "-preproc" });
		assertLineSplitEquals(out.capturedString(), "@startuml", "Alice->Bob: bye", "@enduml");

	}

}
