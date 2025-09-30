package net.sourceforge.plantuml.cli;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.Run;

class RunHelpMoreTest extends AbstractCliTest {

	@Test
	void testHelp() throws IOException, InterruptedException {

		Run.main(new String[] { "-help:more" });

	}

}
