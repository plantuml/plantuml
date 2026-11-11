package org.luaj.vm2.python;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * The same program run twice: with its str values as bare Strings, then as
 * PyStr. The builtins, the formats, f-strings, json and dicts must not see the
 * difference.
 */
class PyStrScriptTest {

	private static final String[][] GLOBALS = { { "n", "42" }, { "f", "1.5" }, { "e", "😀b" }, { "sep", "-" },
			{ "end", "!\n" }, { "spec", ">4" }, { "j", "{\"a\": [1, \"x\"]}" }, { "k", "key" }, { "b2", "ff" } };

	private static String run(String source, boolean objects) {
		final StringBuilder out = new StringBuilder();
		final Interp in = new Interp(out::append);
		for (String[] g : GLOBALS)
			in.setGlobal(g[0], objects ? PyStr.of(g[1]) : g[1]);
		in.run(source, "pystr.py");
		return out.toString();
	}

	private static void same(String source, String expected) {
		assertEquals(expected, run(source, false), "with Strings");
		assertEquals(expected, run(source, true), "with PyStr");
	}

	@Test
	void conversions() {
		same("print(int(n) + 1, float(f) * 2, int(b2, 16), ord(e[0]), ord('a'))\n", "43 3.0 255 128512 97\n");
	}

	@Test
	void sequenceOperations() {
		same("print(len(e), e[0], e[1], e[::-1], list(reversed(e)))\n"
				+ "print(e + k, e * 2, k == 'key', 'key' == k, sorted([k, 'abc']), 'ke' in k)\n",
				"2 😀 b b😀 ['b', '😀']\n😀bkey 😀b😀b True True ['abc', 'key'] True\n");
	}

	@Test
	void printWithSeparatorAndEnd() {
		same("for c in e:\n    print(c, end=sep)\nprint('x', 'y', sep=sep, end=end)\n", "😀-b-x-y!\n");
	}

	@Test
	void formats() {
		same("print(format(5, spec) + '|', f'{e}:{k!r}', '%s/%s' % (e, k), '{}'.format(e), '{:>4}'.format(k))\n"
				+ "print(format(k, '.2'), '%c' % e[0], '%5s|' % k)\n",
				"   5| 😀b:'key' 😀b/key 😀b  key\nke 😀   key|\n");
	}

	@Test
	void dictsAndKeywords() {
		same("d = {}\nd[k] = 1\nprint(d['key'], k in d, d)\n" + "def g(**kw):\n    return kw\nprint(g(**d))\n"
				+ "d2 = {'key': 5}\nprint(d2[k], d2.get(k))\n", "1 True {'key': 1}\n{'key': 1}\n5 5\n");
	}

	@Test
	void classesAndStrMethods() {
		same("class C:\n    pass\nprint(C.__module__, k.upper(), ','.join([k, e]), e.split('b'))\n",
				"__main__ KEY key,😀b ['😀', '']\n");
	}

	@Test
	void json() {
		same("import json\nprint(json.loads(j), json.dumps(k), json.dumps({k: [e]}))\n",
				"{'a': [1, 'x']} \"key\" {\"key\": [\"\\ud83d\\ude00b\"]}\n");
	}

	@Test
	void unpacking() {
		same("print(*e)\nprint(list(e), tuple(k))\nx, y = e\nprint(x, y)\n",
				"😀 b\n['😀', 'b'] ('k', 'e', 'y')\n😀 b\n");
	}

	@Test
	void sumOfStrsIsRefused() {
		for (boolean objects : new boolean[] { false, true }) {
			final String out = run("try:\n    sum(['a'], k)\nexcept TypeError as x:\n    print('refused')\n", objects);
			assertTrue(out.startsWith("refused"), out);
		}
	}
}
