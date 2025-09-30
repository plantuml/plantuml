package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.Run;

class RunFlagTimeoutTest extends AbstractCliTest {

	@StdIo
	@Test
	void testSvg(StdErr err) throws IOException, InterruptedException {

		assertEquals(30_000L, GlobalConfigKey.TIMEOUT_MS.getDefaultValue());
		assertEquals(30_000L, GlobalConfig.getInstance().value(GlobalConfigKey.TIMEOUT_MS));

		Path file = aliceBob_hello(tempDir, "test.txt");
		Run.main(new String[] { "-svg", "-timeout", "31", file.toAbsolutePath().toString() });

		assertEquals(30_000L, GlobalConfigKey.TIMEOUT_MS.getDefaultValue());
		assertEquals(31_000L, GlobalConfig.getInstance().value(GlobalConfigKey.TIMEOUT_MS));

	}

}
