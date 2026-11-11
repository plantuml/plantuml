package org.luaj.vm2.python;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * A str is a PyStr or a bare String, and the operations of the interpreter
 * take both: the answer is a PyStr when the operand is one, and a String when
 * all the operands are Strings, as before.
 */
class PyStrOpsTest {

	private static PyStr p(String s) {
		return PyStr.of(s);
	}

	private static Object[] args(Object... a) {
		return a;
	}

	private static Object call(Object receiver, String method, Object... a) {
		final Object[] all = new Object[a.length + 1];
		all[0] = receiver;
		System.arraycopy(a, 0, all, 1, a.length);
		return Methods.lookup(receiver, method).call(null, all);
	}

	@Test
	void typeTruthAndLength() {
		assertSame(Types.STR, PyOps.typeOf(p("a")));
		assertSame(Types.STR, PyOps.typeOf("a"));
		assertFalse(PyOps.isTrue(PyStr.EMPTY));
		assertTrue(PyOps.isTrue(p("a")));
		assertEquals(3, PyOps.len(p("a😀b")));
		assertEquals(3, PyOps.len("a😀b"));
	}

	@Test
	void concatenationAndRepetition() {
		assertEquals(p("ab"), PyOps.binary(Op.BIN_ADD, p("a"), p("b")));
		assertEquals(p("ab"), PyOps.binary(Op.BIN_ADD, "a", p("b")));
		assertEquals(p("ab"), PyOps.binary(Op.BIN_ADD, p("a"), "b"));
		assertEquals("ab", PyOps.binary(Op.BIN_ADD, "a", "b"));
		assertEquals(p("😀😀"), PyOps.binary(Op.BIN_MUL, p("😀"), Integer.valueOf(2)));
		assertEquals(p("xx"), PyOps.binary(Op.BIN_MUL, Integer.valueOf(2), p("x")));
		assertEquals("xx", PyOps.binary(Op.BIN_MUL, "x", Integer.valueOf(2)));
		assertEquals(p("<5>"), PyOps.binary(Op.BIN_MOD, p("<%d>"), Integer.valueOf(5)));
	}

	@Test
	void equalityAndOrder() {
		assertTrue(PyOps.eq(p("a"), "a"));
		assertTrue(PyOps.eq("a", p("a")));
		assertTrue(PyOps.eq(p("a"), p("a")));
		assertFalse(PyOps.eq(p("a"), p("b")));
		assertFalse(PyOps.eq(p("1"), Integer.valueOf(1)));
		assertEquals(Boolean.TRUE, PyOps.compare(Op.CMP_LT, p("a"), "b"));
		assertEquals(Boolean.FALSE, PyOps.compare(Op.CMP_LT, "b", p("a")));
		assertTrue(PyOps.order(p("a"), "b") < 0);
		assertTrue(PyOps.order("b", p("a")) > 0);
		assertEquals(0, PyOps.order(p("a"), p("a")));
	}

	@Test
	void containment() {
		assertTrue(PyOps.contains(p("a😀b"), p("😀")));
		assertTrue(PyOps.contains("abc", p("b")));
		assertTrue(PyOps.contains(p("abc"), "c"));
		assertFalse(PyOps.contains(p("abc"), p("d")));
		assertThrows(PyError.class, () -> PyOps.contains(p("abc"), Integer.valueOf(1)));
	}

	@Test
	void subscriptsGiveStrs() {
		final Object a = PyOps.getitem(p("a😀b"), Integer.valueOf(1));
		assertTrue(a instanceof PyStr);
		assertEquals("😀", a.toString());
		// a bare String is read through a PyStr, so it gives one too
		final Object b = PyOps.getitem("a😀b", Integer.valueOf(1));
		assertTrue(b instanceof PyStr);
		assertEquals("😀", b.toString());
	}

	@Test
	void iterationGivesTheKindOfTheReceiver() {
		final PyIter it = PyOps.getIter(p("a😀"));
		final Object first = it.next();
		final Object second = it.next();
		assertTrue(first instanceof PyStr);
		assertEquals(p("😀"), second);
		assertNull(it.next());
		assertTrue(PyOps.getIter("a").next() instanceof String);
	}

	@Test
	void reprAndStr() {
		assertEquals(PyOps.repr("it's"), PyOps.repr(p("it's")));
		assertEquals("abc", PyOps.str(p("abc")));
	}

	@Test
	void methodsOfAPyStrGiveStrs() {
		assertEquals(p("ABC"), call(p("abc"), "upper"));
		// what a function gives goes to the program as a PyStr, whatever the receiver was
		assertEquals(p("ABC"), call("abc", "upper"));
		final PyList parts = (PyList) call(p("a,b"), "split", p(","));
		assertEquals(2, parts.items.size());
		assertEquals(p("a"), parts.items.get(0));
		assertEquals(p("b"), parts.items.get(1));
		assertEquals(Boolean.TRUE, call(p("abc"), "startswith", new PyTuple(args(p("x"), p("ab")))));
		assertEquals(Boolean.TRUE, call("abc", "startswith", p("ab")));
		final ArrayList<Object> items = new ArrayList<Object>();
		items.add(p("x"));
		items.add("y");
		assertEquals(p("x-y"), call(p("-"), "join", new PyList(items)));
		assertEquals(p("x-y"), call("-", "join", new PyList(items)));
	}

	@Test
	void aPyStrArgumentOfAStringMethodIsAccepted() {
		assertEquals(p("axc"), call("abc", "replace", p("b"), p("x")));
		assertEquals(Integer.valueOf(1), call("abc", "find", p("b")));
	}

	@Test
	void aKeywordArgumentThatIsAPyStr() {
		final Map<String, Object> kw = new LinkedHashMap<String, Object>();
		kw.put("sep", p(","));
		final Object r = Methods.lookup(p("a,b"), "split").callKw(null, args(p("a,b")), kw);
		assertEquals(p("b"), ((PyList) r).items.get(1));
	}

	@Test
	void methodsCountInCodePoints() {
		for (Object recv : new Object[] { p("a😀b😀"), "a😀b😀" }) {
			assertEquals(Integer.valueOf(2), call(recv, "find", "b"));
			assertEquals(Integer.valueOf(3), call(recv, "rfind", "😀"));
			assertEquals(Integer.valueOf(2), call(recv, "count", "😀"));
			assertEquals(Integer.valueOf(1), call(recv, "index", "😀"));
			assertEquals(Boolean.TRUE, call(recv, "startswith", "😀", Integer.valueOf(1)));
			assertEquals(Boolean.TRUE, call(recv, "endswith", "b😀"));
		}
		assertEquals(p("😀**"), call(p("😀"), "ljust", Integer.valueOf(3), "*"));
		assertEquals(p("00😀b"), call(p("😀b"), "zfill", Integer.valueOf(4)));
		assertEquals(p("*😀*"), call(p("😀"), "center", Integer.valueOf(3), "*"));
	}
}
