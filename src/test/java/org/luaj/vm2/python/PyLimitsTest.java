package org.luaj.vm2.python;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * The interpreter does not rely on the JVM to stop a runaway recursion or to
 * report a misuse: deep programs end with a Python error, a dict changed during
 * an iteration is detected by the dict, and sorting never fails because of the
 * order it is given.
 */
class PyLimitsTest {

	private static String run(String source) {
		StringBuilder out = new StringBuilder();
		new Interp(out::append).run(source, "limits.py");
		return out.toString();
	}

	private static String repeat(String s, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++)
			sb.append(s);
		return sb.toString();
	}

	// ------------------------------------------------------------ deep trees

	@Test
	void longChainsAreAcceptedUpToTheLimit() {
		assertEquals("900\n", run("x = 1" + repeat(" + 1", 899) + "\nprint(x)\n"));
	}

	@Test
	void longChainsAreRejectedNotOverflowed() {
		String[] programs = { "x = 1" + repeat(" + 1", 5000) + "\n", "x = a" + repeat(".b", 5000) + "\n",
				"x = f" + repeat("()", 5000) + "\n", "x = f" + repeat("[0]", 5000) + "\n",
				"x = [1 " + repeat("for a in [1] ", 5000) + "]\n" };
		for (String source : programs) {
			PySyntaxError e = assertThrows(PySyntaxError.class, () -> Interp.compile(source, "limits.py"));
			assertEquals("too many levels of nesting", e.detail);
		}
	}

	@Test
	void longElifChainDoesNotRecurseInTheParser() {
		StringBuilder source = new StringBuilder("x = 400\nif x == 0:\n    print('zero')\n");
		for (int i = 1; i < 500; i++)
			source.append("elif x == ").append(i).append(":\n    print(").append(i).append(")\n");
		source.append("else:\n    print('other')\n");
		assertEquals("400\n", run(source.toString()));
		// and a longer one, which a recursive parser could not read, is refused by the limit
		StringBuilder longer = new StringBuilder("x = 1\nif x == 0:\n    pass\n");
		for (int i = 1; i < 20000; i++)
			longer.append("elif x == ").append(i).append(":\n    pass\n");
		assertThrows(PySyntaxError.class, () -> Interp.compile(longer.toString(), "limits.py"));
	}

	@Test
	void deepTupleKeysAreRefused() {
		String source = "t = ()\nfor i in range(150):\n    t = (t,)\nprint({t: 1}[t])\nfor i in range(5000):\n    t = (t,)\n"
				+ "try:\n    {t: 1}\nexcept RecursionError as e:\n    print('RecursionError')\n";
		assertEquals("1\nRecursionError\n", run(source));
	}

	// ------------------------------------------------------------ dicts changed during an iteration

	@Test
	void dictChangedDuringIteration() {
		String source = "def grow():\n    d = {1: 1, 2: 2}\n    for k in d:\n        d[k + 10] = 1\n"
				+ "def swap():\n    d = {1: 1, 2: 2, 3: 3}\n    for k in d:\n        if k == 1:\n            del d[2]\n            d[7] = 7\n"
				+ "def clear():\n    d = {1: 1, 2: 2}\n    for k, v in d.items():\n        d.clear()\n"
				+ "def pop():\n    d = {1: 1, 2: 2, 3: 3}\n    for v in d.values():\n        d.pop(1)\n"
				+ "for f in [grow, swap, clear, pop]:\n    try:\n        f()\n    except RuntimeError as e:\n        print(e)\n"
				+ "d = {1: 1, 2: 2, 3: 3}\nfor k in d:\n    d[k] = 0\n    d.setdefault(k, 5)\nprint(d)\n";
		String message = "dictionary changed size during iteration\n";
		assertEquals(message + message + message + message + "{1: 0, 2: 0, 3: 0}\n", run(source));
	}

	// ------------------------------------------------------------ sorting

	@Test
	void sortIsStable() {
		String source = "class P:\n    def __init__(self, k, tag):\n        self.k = k\n        self.tag = tag\n"
				+ "    def __lt__(self, other):\n        return self.k < other.k\n"
				+ "ps = [P(i % 3, i) for i in range(40)]\n"
				+ "print([p.tag for p in sorted(ps)][:14])\nprint([p.tag for p in sorted(ps, reverse=True)][:14])\n"
				+ "print(sorted([(i % 2, i) for i in range(40)], key=lambda t: t[0])[:4])\n";
		assertEquals("[0, 3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39]\n"
				+ "[2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35, 38, 1]\n[(0, 0), (0, 2), (0, 4), (0, 6)]\n",
				run(source));
	}

	@Test
	void sortWithNaNsEnds() {
		// the comparison is not a total order: the Java library may refuse it, CPython does not
		String source = "inf = 1e308 * 10\nnan = inf - inf\nxs = []\nfor i in range(100):\n    xs.append(float(i % 7))\n    xs.append(nan)\n"
				+ "print(len(sorted(xs)))\nxs.sort(reverse=True)\nprint(len(xs))\n";
		assertEquals("200\n200\n", run(source));
	}

	@Test
	void sortOfIncomparableValuesNamesTheTypesAsCPython() {
		String source = "for xs in [[1, 'a'], [2, 1, 'a', 3]]:\n    try:\n        sorted(xs)\n    except TypeError as e:\n        print(e)\n";
		String message = "'<' not supported between instances of 'str' and 'int'\n";
		assertEquals(message + message, run(source));
	}

	// ------------------------------------------------------------ messages that used to come from a catch

	@Test
	void starAndDictUpdateErrors() {
		String source = "def f(*a):\n    return a\n"
				+ "for g in [lambda: f(*5), lambda: f(*None), lambda: dict([1, 2]), lambda: dict([(1, 2, 3)])]:\n"
				+ "    try:\n        g()\n    except Exception as e:\n        print(type(e).__name__, e)\n"
				+ "print(f(*[1], *(2,), *'ab', *range(2), *{3: 4}))\nprint(dict([(1, 2), [3, 4], 'ab']))\n";
		assertEquals("TypeError argument after * must be an iterable, not int\n"
				+ "TypeError argument after * must be an iterable, not NoneType\n"
				+ "TypeError cannot convert dictionary update sequence element #0 to a sequence\n"
				+ "ValueError dictionary update sequence element #0 has length 3; 2 is required\n"
				+ "(1, 2, 'a', 'b', 0, 1, 3)\n{1: 2, 3: 4, 'a': 'b'}\n", run(source));
		assertTrue(PyOps.isIterable(new PyList()) && PyOps.isIterable(PyTuple.EMPTY) && PyOps.isIterable("")
				&& PyOps.isIterable(Integer.valueOf(1)) == false);
	}
}
