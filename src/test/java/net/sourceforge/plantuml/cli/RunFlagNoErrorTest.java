package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.Run;

class RunFlagNoErrorTest extends AbstractCliTest {

	@StdIo
	@Test
	void test1(StdErr err) throws Exception {
		aliceBob_hello(tempDir, "ok.txt");
		syntax_error(tempDir, "error.txt");

		assertLs("[error.txt, ok.txt]", tempDir);

		assertExit(200, () -> {
			Run.main(new String[] { "-v", "-noerror", "-svg", tempDir.toAbsolutePath().toString() });
		});
		assertTrue(err.capturedString().contains("Error line 2 in file"));
		assertTrue(err.capturedString().contains("Some diagram description contains errors"));


		assertLs("[error.txt, ok.svg, ok.txt]", tempDir);


	}

}
