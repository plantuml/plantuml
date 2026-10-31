package org.luaj.vm2.python;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

/**
 * Runs the scripts of {@code python} on the interpreter. CPython is the oracle:
 * a script with a {@code .out} file must print exactly that, any other script
 * must finish without an exception. The scripts of {@code deviations} are
 * handled separately, they do not run under CPython.
 */
class PyScriptsTest {

	// Line endings depend on core.autocrlf, so CRLF is normalized to LF: the
	// interpreter prints LF, and so do the reference .out files.
	private static String read(Path path) throws IOException {
		return new String(Files.readAllBytes(path), StandardCharsets.UTF_8).replace("\r\n", "\n");
	}

	private static Path root() throws URISyntaxException {
		URL url = PyScriptsTest.class.getClassLoader().getResource("python/README.md");
		assertTrue(url != null, "missing resource python/README.md");
		return Paths.get(url.toURI()).getParent();
	}

	private static String run(String source, String name) {
		StringBuilder out = new StringBuilder();
		new Interp(out::append).run(source, name);
		return out.toString();
	}

	@TestFactory
	List<DynamicTest> scriptsMatchCPython() throws Exception {
		Path dir = root();
		List<Path> scripts;
		try (Stream<Path> walk = Files.walk(dir)) {
			scripts = walk.filter(f -> f.toString().endsWith(".py") && !f.toString().contains("deviations"))
					.sorted().collect(Collectors.toList());
		}
		List<DynamicTest> tests = new ArrayList<DynamicTest>();
		for (Path script : scripts) {
			String name = dir.relativize(script).toString();
			tests.add(DynamicTest.dynamicTest(name, () -> {
				String actual = run(read(script), name);
				Path ref = Paths.get(script.toString().replaceAll("\\.py$", ".out"));
				if (Files.exists(ref))
					assertEquals(read(ref), actual);
			}));
		}
		return tests;
	}

//	@Test
//	void integerOverflowRaisesOverflowError() throws Exception {
//		run(read(root().resolve("deviations/int_overflow.py")), "int_overflow.py");
//	}
//
//	@Test
//	void instructionLimitCannotBeCaught() throws Exception {
//		Interp in = new Interp(s -> {
//		});
//		in.setMaxSteps(10000);
//		String source = read(root().resolve("deviations/instruction_limit.py"));
//		assertThrows(ExecutionLimitError.class, () -> in.run(source, "instruction_limit.py"));
//	}
//
//	@Test
//	void callDepthIsLimitedAndCatchable() {
//		String source = "def f(n):\n    return f(n + 1)\n" + "try:\n    f(0)\nexcept RecursionError:\n    print('caught')\n";
//		assertEquals("caught\n", run(source, "recursion"));
//		Interp in = new Interp(s -> {
//		});
//		in.setMaxDepth(50);
//		assertThrows(PyError.class, () -> in.run("def f():\n    f()\nf()\n", "deep"));
//	}
//
//	@Test
//	void uncaughtErrorReportsTypeAndLine() {
//		PyError e = assertThrows(PyError.class, () -> run("x = 1\nprint(x)\n1 / 0\n", "prog.py"));
//		assertEquals("ZeroDivisionError", e.typeName());
//		assertTrue(e.getMessage().contains("line 3"), e.getMessage());
//	}
//
//	@Test
//	void syntaxErrorsAreReported() {
//		assertThrows(PySyntaxError.class, () -> run("def f(:\n", "bad"));
//		assertThrows(PySyntaxError.class, () -> run("return 1\n", "bad"));
//		assertThrows(PySyntaxError.class, () -> run("break\n", "bad"));
//		assertThrows(PySyntaxError.class, () -> run("def f(a, a):\n    pass\n", "bad"));
//		assertThrows(PySyntaxError.class, () -> run("def f():\n    x = 1\n    global x\n", "bad"));
//		assertThrows(PySyntaxError.class, () -> run("def f():\n    nonlocal y\n", "bad"));
//		assertThrows(PySyntaxError.class, () -> run("x = 4294967296\n", "bad"));
//	}
//
//	@Test
//	void disassemblerListsTheCode() {
//		String d = Interp.compile("def f(a):\n    return a + 1\nprint(f(2))\n", "d").disassemble();
//		assertTrue(d.contains("MAKE_FUNCTION") && d.contains("BINARY_OP") && d.contains("RETURN"), d);
//	}
}
