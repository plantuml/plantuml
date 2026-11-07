package org.luaj.vm2.python;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * The parts of the class support that CPython cannot check: what is rejected
 * at compile time, and the documented deviations. The behaviour shared with
 * CPython is tested by the scripts language/classes.py,
 * language/inheritance.py and errors/classes.py.
 */
class PyClassTest {

	private static String run(String source) {
		StringBuilder out = new StringBuilder();
		new Interp(out::append).run(source, "classes.py");
		return out.toString();
	}

	private static void assertRejected(String source) {
		assertThrows(PySyntaxError.class, () -> Interp.compile(source, "classes.py"), source);
	}

	@Test
	void unsupportedClassBodiesAreRejected() {
		String[] sources = { "class A:\n    if x:\n        y = 1\n", "class A:\n    for i in y:\n        pass\n",
				"class A:\n    while x:\n        pass\n", "class A:\n    try:\n        pass\n    except E:\n        pass\n",
				"class A:\n    global g\n", "class A:\n    import math\n", "class A:\n    del x\n",
				"class A:\n    return 1\n", "class A:\n    assert x\n", "class A:\n    raise E\n" };
		for (String s : sources)
			assertRejected(s);
	}

	@Test
	void specialMethodsOtherThanInitAreRejected() {
		String[] sources = { "class A:\n    def __str__(self):\n        return ''\n",
				"class A:\n    def __eq__(self, o):\n        return True\n", "class A:\n    __slots__ = ()\n",
				"class A:\n    __repr__ = None\n", "class A:\n    class __Meta__:\n        pass\n" };
		for (String s : sources)
			assertRejected(s);
		// __init__ and private names are accepted
		Interp.compile("class A:\n    __n = 1\n    def __init__(self):\n        self.__x = 1\n    def __f(self):\n"
				+ "        pass\n", "classes.py");
	}

	@Test
	void otherUnsupportedFormsAreRejected() {
		assertRejected("class A(B, C):\n    pass\n");
		assertRejected("class A(B):\n    def f(self):\n        return super().f()\n");
		assertRejected("class A(metaclass=M):\n    pass\n");
		assertRejected("@dec\nclass A:\n    pass\n");
		// a class attribute that hides, for the methods, a variable of the enclosing function
		assertRejected("def f():\n    x = 1\n    class A:\n        x = 2\n        def g(self):\n            return x\n"
				+ "    return A\n");
	}

	@Test
	void builtinTypesOtherThanObjectAndExceptionsCannotBeSubclassed() {
		PyError e = assertThrows(PyError.class, () -> run("class L(list):\n    pass\n"));
		assertEquals("TypeError", e.typeName());
		assertEquals("ok\n", run("class O(object):\n    pass\nclass E(KeyError):\n    pass\nprint('ok')\n"));
	}

	@Test
	void reprOfAnInstanceHasNoAddress() {
		String source = "class A:\n    def f(self):\n        pass\na = A()\nprint(repr(a))\nprint(a.f)\nprint(A)\n"
				+ "def g():\n    class B:\n        pass\n    return B()\nprint(g())\n";
		assertEquals("<__main__.A object>\n<bound method A.f of <__main__.A object>>\n<class '__main__.A'>\n"
				+ "<__main__.g.<locals>.B object>\n", run(source));
	}

	@Test
	void classesKnowTheModuleTheyAreDefinedIn() {
		// a module has its own globals, whose __name__ is the name of the module
		StringBuilder out = new StringBuilder();
		Interp in = new Interp(out::append);
		in.globals.put("__name__", "palette");
		in.run("class Box:\n    class Inner:\n        pass\nprint(Box, Box.__module__, Box().__module__)\n"
				+ "print(Box(), Box.Inner)\n", "palette");
		assertEquals("<class 'palette.Box'> palette palette\n<palette.Box object> <class 'palette.Box.Inner'>\n",
				out.toString());
	}

	@Test
	void errorReportsQualifyTheClassOfAModuleOnly() {
		// as CPython's traceback: "palette.Err: m", but "Err: m" in the main script
		Interp in = new Interp(s -> {
		});
		in.globals.put("__name__", "palette");
		PyError e = assertThrows(PyError.class,
				() -> in.run("class Err(ValueError):\n    pass\nraise Err('m')\n", "palette"));
		assertEquals("palette.Err", e.typeName());
		assertTrue(e.getMessage().endsWith("\npalette.Err: m"), e.getMessage());
		e = assertThrows(PyError.class,
				() -> run("class A:\n    class Err(Exception):\n        pass\nraise A.Err('m')\n"));
		assertEquals("A.Err", e.typeName());
		e = assertThrows(PyError.class, () -> run("raise KeyError('k')\n"));
		assertEquals("KeyError", e.typeName());
	}

	@Test
	void errorInAMethodReportsItsLine() {
		PyError e = assertThrows(PyError.class,
				() -> run("class A:\n    def f(self):\n        return 1 / 0\nA().f()\n"));
		assertEquals("ZeroDivisionError", e.typeName());
		assertTrue(e.getMessage().contains("line 3, in f"), e.getMessage());
	}

	@Test
	void methodCallsAreCountedAndBounded() {
		Interp in = new Interp(s -> {
		});
		in.setMaxDepth(50);
		PyError e = assertThrows(PyError.class,
				() -> in.run("class A:\n    def f(self):\n        return self.f()\nA().f()\n", "deep"));
		assertEquals("RecursionError", e.typeName());
	}

	@Test
	void disassemblerShowsTheClassMachinery() {
		String d = Interp.compile("class A:\n    def __init__(self):\n        self.x = 1\n", "d").disassemble();
		assertTrue(d.contains("__build_class__") && d.contains("STORE_ATTR") && d.contains("BUILD_DICT"), d);
	}
}
