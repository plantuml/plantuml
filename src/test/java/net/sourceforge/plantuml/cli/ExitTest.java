package net.sourceforge.plantuml.cli;

import org.junit.jupiter.api.Test;

public class ExitTest extends AbstractCliTest {

	@Test
	void exitsWith42() throws Exception {
		assertExit(42, () -> {
			Exit.exit(42);
		});
	}
}
