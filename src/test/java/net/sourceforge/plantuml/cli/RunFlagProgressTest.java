package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

import net.sourceforge.plantuml.ProgressBar;
import net.sourceforge.plantuml.ProgressBarTest;
import net.sourceforge.plantuml.Run;

@Isolated
class RunFlagProgressTest extends AbstractCliTest {

	@BeforeEach
	void setUp() {
		ProgressBar.reset();
	}

	@AfterEach
	void tearDown() {
		ProgressBar.reset();
	}

	@Test
	@StdIo
	void test1(StdErr err) throws Exception {
		aliceBob_hello(tempDir, "test.txt");

		Run.main(new String[] { "-progress", "-svg", tempDir.toAbsolutePath().toString() });

		assertLs("[test.svg, test.txt]", tempDir);

		final String expected = ProgressBarTest.progress( //
				"[                              ] 0/1", //
				"[##############################] 1/1");

		assertEquals(expected.toString(), err.capturedString());

	}

	@Test
	@StdIo
	void test3(StdErr err) throws Exception {
		for (int i = 0; i < 3; i++)
			aliceBob_hello(tempDir, "test" + i + ".txt");

		Run.main(new String[] { "-progress", "-svg", "-nbthread", "1", tempDir.toAbsolutePath().toString() });

		assertLs("[test0.svg, test0.txt, test1.svg, test1.txt, test2.svg, test2.txt]", tempDir);

		final String expected = ProgressBarTest.progress( //
				"[                              ] 0/3", //
				"[##########                    ] 1/3", //
				"[####################          ] 2/3", //
				"[##############################] 3/3");

		assertEquals(expected.toString(), err.capturedString());

	}

}
