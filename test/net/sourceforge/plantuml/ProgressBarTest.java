package net.sourceforge.plantuml;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class ProgressBarTest {

	private static final PrintStream standardErr = System.err;
	private static final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	@BeforeAll
	public static void beforeAll() {
		System.setErr(new PrintStream(outputStreamCaptor));
	}

	@AfterAll
	public static void afterAll() {
		System.setErr(standardErr); // restore default stderr
	}

	@Test
	void test_progressbar() throws Exception {
		ProgressBar.setEnable(true);
		ProgressBar.incTotal(2);
		ProgressBar.incTotal(2);
		PeriodicIncrementDone t2 = new PeriodicIncrementDone(4);
		t2.start();
		t2.join();
		StringBuilder expected = new StringBuilder();
		expected.append("[                              ] 0/2");
		appendClear(expected);
		// increment total from 2 to 4
		expected.append("[                              ] 0/4");
		appendClear(expected);
		expected.append("[#######                       ] 1/4");
		appendClear(expected);
		expected.append("[###############               ] 2/4");
		appendClear(expected);
		expected.append("[######################        ] 3/4");
		appendClear(expected);
		expected.append("[##############################] 4/4");
		assertThat(outputStreamCaptor.toString().trim()).isEqualTo(expected.toString());
	}

	private void appendClear(StringBuilder sb) {
		sb.append("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
		sb.append("                                    ");
		sb.append("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
	}

	private static class PeriodicIncrementDone extends Thread {

		private int count;

		public PeriodicIncrementDone(int count) {
			this.count = count;
		}

		@Override
		public void run() {
			while (count-- > 0) {
				ProgressBar.incDone(false);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
