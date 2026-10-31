package sanskrit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * The str of a program are PyStr: the literals, what the operations and the
 * builtins make of them, and what a function of the host gives. The functions
 * of the host are given Strings.
 */
class PyStrProducersTest {

	private static Object global(String source, String name) {
		final Interp in = new Interp(s -> {
		});
		in.run(source, "producers.py");
		return in.globals.get(name);
	}

	private static void assertStr(String source, String name, String expected) {
		final Object v = global(source, name);
		assertTrue(v instanceof PyStr, name + " is " + (v == null ? "null" : v.getClass().getName()));
		assertEquals(expected, v.toString());
	}

	@Test
	void literalsAndOperations() {
		assertStr("x = 'abc'\n", "x", "abc");
		assertStr("x = 'a' + 'b'\n", "x", "ab");
		assertStr("x = 'ab' * 2\n", "x", "abab");
		assertStr("x = 'a😀b'[1]\n", "x", "😀");
		assertStr("x = 'abc'.upper()\n", "x", "ABC");
		assertStr("x = f'{1}-{2!r}'\n", "x", "1-2");
		assertStr("x = '%d' % 5\n", "x", "5");
	}

	@Test
	void builtinsAndNames() {
		assertStr("x = str(12)\n", "x", "12");
		assertStr("x = repr('a')\n", "x", "'a'");
		assertStr("x = chr(65)\n", "x", "A");
		assertStr("x = hex(255)\n", "x", "0xff");
		assertStr("def f():\n    pass\nx = f.__name__\n", "x", "f");
		assertStr("class C:\n    pass\nx = C.__name__\n", "x", "C");
		assertStr("x = __name__\n", "x", "__main__");
	}

	@Test
	void elementsOfAList() {
		final Object v = global("x = 'a,b'.split(',')\n", "x");
		assertTrue(((PyList) v).items.get(0) instanceof PyStr);
		final Object w = global("x = list('ab')\n", "x");
		assertTrue(((PyList) w).items.get(1) instanceof PyStr);
	}

	@Test
	void theFunctionsOfTheHostAreGivenStrings() {
		final StringBuilder out = new StringBuilder();
		final Interp in = new Interp(out::append);
		final PyModule m = new PyModule("host");
		m.put("kind", (inter, a) -> a[0] instanceof String ? "String" : "other");
		m.putString("name", "value");
		in.setGlobal("host", m);
		in.run("print(host.kind('x'), host.kind('x' + 'y'), host.name, host.name == 'value')\n", "host.py");
		assertEquals("String String value True\n", out.toString());
		assertTrue(in.globals.get("__name__") instanceof PyStr);
	}
}
