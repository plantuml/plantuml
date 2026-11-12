package org.luaj.vm2.python;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

/**
 * Programs found by the review of the interpreter at commit 148134d that get
 * past its limits: they end with a java.lang.OutOfMemoryError or a
 * java.lang.StackOverflowError, build a value larger than the limits allow, or
 * do an unbounded amount of work for one step of the instruction counter. A
 * port to a language that cannot recover from these (Go) would crash.
 * <p>
 * Each test states what is expected: a Python error ({@link PyError}), a
 * syntax error or the instruction limit, never an error of the JVM. They fail
 * at 148134d. Until they are fixed, several of them fill the heap of the JVM
 * that runs them before they fail (see {@link #expect}).
 * <p>
 * The other findings of the review are scripts, compared with the output of
 * CPython: {@code src/test/resources/python/review}.
 */
class PyReviewLimitsTest {

	private static String run(String source) {
		StringBuilder out = new StringBuilder();
		new Interp(out::append).run(source, "review.py");
		return out.toString();
	}

	private static String repeat(String s, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++)
			sb.append(s);
		return sb.toString();
	}

	/**
	 * The code must throw an exception of the expected class. An OutOfMemoryError is turned into a failed
	 * assertion here: JUnit does not catch it, and it would end the whole run of the tests instead of
	 * failing one of them.
	 */
	private static <T extends Throwable> T expect(Class<T> expected, Runnable code) {
		try {
			return assertThrows(expected, code::run);
		} catch (OutOfMemoryError e) {
			throw new AssertionError("java.lang.OutOfMemoryError instead of " + expected.getSimpleName());
		}
	}

	/** The program must end with the Python exception of this name. */
	private static void assertPyError(String type, String source) {
		assertEquals(type, expect(PyError.class, () -> run(source)).typeName());
	}

	/** The program must end with a Python exception, whatever its type: the value it asks for is too large. */
	private static void assertRefused(String source) {
		expect(PyError.class, () -> run(source));
	}

	// ------------------------------------------------------------ 32-bit integers

	@Test
	void lengthOfRangeDoesNotWrapAround() throws Exception {
		URL url = PyReviewLimitsTest.class.getClassLoader().getResource("python/deviations/range_len_overflow.py");
		assertTrue(url != null, "missing resource python/deviations/range_len_overflow.py");
		String source = new String(Files.readAllBytes(Paths.get(url.toURI())), StandardCharsets.UTF_8).replace("\r\n", "\n");
		run(source);
	}

	// ------------------------------------------------------------ lists: MAX_LIST when a list grows in place

	private static final String DOUBLE_40_TIMES = "l = [0]\nfor i in range(40):\n    ";

	@Test
	void growthInPlaceByAugmentedAdd() {
		// VM at 148134d: java.lang.OutOfMemoryError (l + l and l *= 2 are checked, l += l is not)
		assertPyError("MemoryError", DOUBLE_40_TIMES + "l += l\n");
	}

	@Test
	void growthInPlaceByExtend() {
		// VM at 148134d: java.lang.OutOfMemoryError
		assertPyError("MemoryError", DOUBLE_40_TIMES + "l.extend(l)\n");
	}

	@Test
	void growthInPlaceBySliceAssignment() {
		// VM at 148134d: java.lang.OutOfMemoryError
		assertPyError("MemoryError", DOUBLE_40_TIMES + "l[len(l):] = l\n");
	}

	// ------------------------------------------------------------ strings: MAX_STRING on every way to build one

	@Test
	void stringsAboveTheLimitByReplace() {
		// 2**40 characters. VM at 148134d: java.lang.OutOfMemoryError
		assertPyError("MemoryError", "s = 'a' * (1 << 20)\nx = s.replace('a', s)\n");
	}

	@Test
	void stringsAboveTheLimitByReplaceOfTheEmptyString() {
		// VM at 148134d: java.lang.OutOfMemoryError
		assertPyError("MemoryError", "s = 'a' * (1 << 25)\nx = s.replace('', s)\n");
	}

	@Test
	void stringsAboveTheLimitByStarWidth() {
		// '%2000000000d' % 1 is refused ("width too big"), the same width given by * is not.
		// VM at 148134d: java.lang.OutOfMemoryError
		assertRefused("x = '%*d' % (2000000000, 1)\n");
	}

	@Test
	void stringsAboveTheLimitByPrecision() {
		// a precision of 10 digits is refused, one of 9 digits is not: a str of 100 million characters,
		// above PyOps.MAX_STRING (1 << 26). VM at 148134d: no error
		assertRefused("x = '{:.100000000e}'.format(1.5)\n");
	}

	@Test
	void stringsAboveTheLimitByStrftime() {
		// a str of 120 million characters. VM at 148134d: no error
		assertRefused("import time\nx = time.strftime('%c' * 5000000, time.gmtime(0))\n");
	}

	@Test
	void manyLargeStringsAreBounded() {
		// 200 strings of 32 MB, each one below MAX_STRING: nothing bounds their sum.
		// VM at 148134d: java.lang.OutOfMemoryError (with a heap large enough: no error, which is the defect)
		assertPyError("MemoryError",
				"l = []\ns = 'a' * (1 << 25)\nfor i in range(200):\n    l.append(s + str(i))\n");
	}

	// ------------------------------------------------------------ nesting of the source

	@Test
	void longChainOfPowersIsRejectedNotOverflowed() throws InterruptedException {
		// 2000 levels give "too many levels of nesting"; a longer chain overflows the stack first: the right
		// operand of ** is parsed before the depth is counted. Compiled in a thread with a stack of 512 KB, so
		// that the result does not depend on the stack of the thread that runs the tests.
		// VM at 148134d: java.lang.StackOverflowError
		final String source = "x = " + repeat("1 ** ", 100000) + "1\n";
		final Throwable[] thrown = new Throwable[1];
		Thread t = new Thread(null, () -> {
			try {
				Interp.compile(source, "review.py");
			} catch (Throwable e) {
				thrown[0] = e;
			}
		}, "review-small-stack", 512 * 1024);
		t.start();
		t.join();
		assertTrue(thrown[0] instanceof PySyntaxError, "expected a PySyntaxError but was: " + thrown[0]);
	}

	@Test
	void deeplyNestedFormatSpecOfAnFStringIsRejected() {
		// f'{1:{1:{1: ... }}}', 100000 levels: every level copies the rest of the text.
		// VM at 148134d: java.lang.OutOfMemoryError
		String source = "x = f'" + repeat("{1:", 100000) + repeat("}", 100000) + "'\n";
		expect(PySyntaxError.class, () -> Interp.compile(source, "review.py"));
	}

	// ------------------------------------------------------------ the instruction counter bounds the work

	/**
	 * Runs the program with a limit of {@code maxSteps} instructions: it must reach the limit. The programs
	 * below do a few dozen iterations, each one on a value of several million items: counted as one step
	 * per iteration, they stay under a limit that the work they do is far above.
	 */
	private static void assertReachesTheLimit(long maxSteps, String source) {
		Interp in = new Interp(s -> {
		});
		in.setMaxSteps(maxSteps);
		expect(ExecutionLimitError.class, () -> in.run(source, "review.py"));
	}

	@Test
	void workOnLargeListsIsCounted() {
		// 40 concatenations of two lists of 4 million items: 320 million items copied, 40 steps counted.
		// With the default limit of 10 million steps the same loop runs for hours.
		// VM at 148134d: no error
		assertReachesTheLimit(100000, "a = [0] * (1 << 22)\nfor i in range(40):\n    x = a + a\n");
	}

	@Test
	void workOnLargeStringsIsCounted() {
		// 60 searches in a str of 64 million characters. VM at 148134d: no error
		assertReachesTheLimit(100000, "s = 'a' * (1 << 26)\nfor i in range(60):\n    x = 'b' in s\n");
	}

	@Test
	void workOfFormattingIsCounted() {
		// one conversion with a precision of 300000 digits takes about as long as a million simple steps,
		// and '{:.100000000%}'.format(1.5) runs for more than a minute without a step being counted.
		// VM at 148134d: no error
		assertReachesTheLimit(100000, "for i in range(40):\n    x = '{:.300000%}'.format(1.5)\n");
	}
}
