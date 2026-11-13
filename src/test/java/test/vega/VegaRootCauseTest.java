package test.vega;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.TestAbortedException;

/**
 * The root cause of a test error must reach the JUnit report and stderr, not
 * only the intermediate exception (issue #2916).
 */
class VegaRootCauseTest {

	private static final String PATH = "src/test/resources/vega/foo/bar.puml";

	@Test
	void knownFailureHasTheRootCauseAsCause() {
		final Throwable intermediate = new AssertionFailedError("Exception mismatch");
		final Throwable root = new IllegalStateException("the real error");

		final TestAbortedException aborted = VegaInputFile.newKnownFailure(PATH, intermediate, root);

		assertSame(root, aborted.getCause());
		assertTrue(aborted.getMessage().contains("Known failure (allow-failure: true): " + PATH));
		assertTrue(aborted.getMessage().contains("Exception mismatch"));
	}

	@Test
	void knownFailureWithoutRootCauseHasTheTestExceptionAsCause() {
		final Throwable intermediate = new IndexOutOfBoundsException("Index 0 out of bounds for length 0");

		final TestAbortedException aborted = VegaInputFile.newKnownFailure(PATH, intermediate, null);

		assertSame(intermediate, aborted.getCause());
	}

	@Test
	void failureHasTheRootCauseAsSuppressed() {
		final Throwable failure = new AssertionFailedError("Exception mismatch");
		final Throwable root = new IllegalStateException("the real error");

		VegaInputFile.addRootCauseAsSuppressed(failure, root);

		assertEquals(1, failure.getSuppressed().length);
		assertSame(root, failure.getSuppressed()[0]);
	}

	@Test
	void failureWithoutRootCauseIsLeftAsIs() {
		final Throwable failure = new AssertionFailedError("Exception mismatch");

		VegaInputFile.addRootCauseAsSuppressed(failure, null);
		VegaInputFile.addRootCauseAsSuppressed(failure, failure);

		assertEquals(0, failure.getSuppressed().length);
	}

	@Test
	void stderrGetsTheRootCause() throws UnsupportedEncodingException {
		final String out = print(new AssertionFailedError("Exception mismatch"),
				new IllegalStateException("the real error"));

		assertTrue(out.contains("[Vega] Root cause for " + PATH + ":"));
		assertTrue(out.contains("IllegalStateException: the real error"));
		assertTrue(out.contains("VegaRootCauseTest"), "the stack trace is printed");
		assertTrue(out.contains("Exception mismatch") == false, "the intermediate exception is not the one shown");
	}

	@Test
	void stderrGetsTheTestExceptionWithoutRootCause() throws UnsupportedEncodingException {
		final String out = print(new AssertionFailedError("Exception mismatch"), null);

		assertTrue(out.contains("[Vega] Root cause for " + PATH + ":"));
		assertTrue(out.contains("AssertionFailedError: Exception mismatch"));
	}

	private static String print(Throwable e, Throwable root) throws UnsupportedEncodingException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(baos, true, "UTF-8");
		VegaInputFile.printRootCause(ps, PATH, e, root);
		ps.flush();
		return new String(baos.toByteArray(), "UTF-8");
	}

}
