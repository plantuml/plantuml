package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.Isolated;
import org.junitpioneer.jupiter.StdErr;
import org.junitpioneer.jupiter.StdIo;

@Isolated
@Execution(ExecutionMode.SAME_THREAD)
public class ProgressBarTest {

	private static final String CLEAR = "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b";

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
	void test_progressbar01(StdErr err) throws Exception {
		ProgressBar.setEnable(true);
		ProgressBar.incTotal(2);

		final StringBuilder expected = new StringBuilder();
		expected.append("[                              ] 0/2");

		assertEquals(expected.toString(), err.capturedString());

	}

	@Test
	@StdIo
	void test_progressbar02(StdErr err) throws Exception {
		ProgressBar.setEnable(true);
		ProgressBar.incTotal(2);
		ProgressBar.incTotal(2);

		final String expected = progress( //
				"[                              ] 0/2", //
				"[                              ] 0/4");

		assertEquals(expected.toString(), err.capturedString());

	}

	@Test
	@StdIo
	void test_progressbar03(StdErr err) throws Exception {
		ProgressBar.setEnable(true);
		ProgressBar.incTotal(2);
		ProgressBar.incTotal(2);
		ProgressBar.incDone(false);

		final String expected = progress( //
				"[                              ] 0/2", //
				"[                              ] 0/4", //
				"[#######                       ] 1/4");

		assertEquals(expected.toString(), err.capturedString());

	}

	@Test
	@StdIo
	void test_progressbar05(StdErr err) throws Exception {
		ProgressBar.setEnable(true);
		ProgressBar.incTotal(2);
		ProgressBar.incTotal(2);
		ProgressBar.incDone(false);
		ProgressBar.incDone(false);

		final String expected = progress( //
				"[                              ] 0/2", //
				"[                              ] 0/4", //
				"[#######                       ] 1/4", //
				"[###############               ] 2/4");

		assertEquals(expected, err.capturedString());

	}

	@Test
	@StdIo
	void test_progressbar04(StdErr err) throws Exception {
		ProgressBar.setEnable(true);
		ProgressBar.incTotal(2);
		ProgressBar.incTotal(2);
		ProgressBar.incDone(false);
		ProgressBar.incDone(false);
		ProgressBar.incDone(false);
		ProgressBar.incDone(false);

		final String expected = progress( //
				"[                              ] 0/2", //
				"[                              ] 0/4", //
				"[#######                       ] 1/4", //
				"[###############               ] 2/4", //
				"[######################        ] 3/4", //
				"[##############################] 4/4");

		assertEquals(expected.toString(), err.capturedString());

	}

	public static String progress(String... lines) {
		final StringBuilder result = new StringBuilder();
		for (String s : lines) {
			if (result.length() > 0) {
				result.append(CLEAR);
				result.append("                                    ");
				result.append(CLEAR);
			}
			result.append(s);

		}
		return result.toString();
	}

}
