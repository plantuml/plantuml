package org.luaj.vm2.python;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

/**
 * The tree built by {@link PyParser} must be the one CPython builds. Two
 * checks: the small syntax files of {@code python-ast} against the reference
 * dumps stored next to them, and every test script of {@code python} against
 * a dump made on the fly by {@code tools/dump_ast.py} (skipped when python3 is
 * not available).
 */
class PyParserTest {

	private static final String[] SYNTAX_FILES = { "statements", "expressions", "strings", "numbers", "layout" };

	private static String read(Path path) throws IOException {
		return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
	}

	private static Path resource(String name) throws URISyntaxException {
		URL url = PyParserTest.class.getClassLoader().getResource(name);
		assertTrue(url != null, "missing resource " + name);
		return Paths.get(url.toURI());
	}

	@TestFactory
	List<DynamicTest> syntaxFilesMatchTheReferenceDumps() {
		List<DynamicTest> tests = new ArrayList<DynamicTest>();
		for (String name : SYNTAX_FILES) {
			tests.add(DynamicTest.dynamicTest(name, () -> {
				String source = read(resource("python-ast/" + name + ".py"));
				String expected = read(resource("python-ast/" + name + ".ast"));
				assertEquals(expected, AstDump.dump(PyParser.parse(source)));
			}));
		}
		return tests;
	}

	private static Path findProjectRoot(Path from) {
		for (Path dir = from; dir != null; dir = dir.getParent())
			if (Files.exists(dir.resolve("tools").resolve("dump_ast.py")))
				return dir;
		return null;
	}

	private static String cpythonDump(Path root, Path script) throws Exception {
		ProcessBuilder pb = new ProcessBuilder("python3", "-I", root.resolve("tools").resolve("dump_ast.py").toString(),
				script.toString());
		pb.directory(root.toFile());
		pb.redirectError(ProcessBuilder.Redirect.DISCARD);
		Process process = pb.start();
		byte[] out = process.getInputStream().readAllBytes();
		assertTrue(process.waitFor(60, TimeUnit.SECONDS), "python3 timed out");
		assertEquals(0, process.exitValue(), "python3 failed on " + script);
		return new String(out, StandardCharsets.UTF_8);
	}

	@TestFactory
	List<DynamicTest> testScriptsMatchCPython() throws Exception {
		Path dir = resource("python/README.md").getParent();
		Path root = findProjectRoot(dir);
		assumeTrue(root != null, "tools/dump_ast.py not found");
		try {
			Process p = new ProcessBuilder("python3", "--version").start();
			p.getInputStream().readAllBytes();
			assumeTrue(p.waitFor() == 0, "python3 not available");
		} catch (IOException e) {
			assumeTrue(false, "python3 not available");
		}
		List<Path> scripts;
		try (Stream<Path> walk = Files.walk(dir)) {
			scripts = walk.filter(f -> f.toString().endsWith(".py")).sorted().collect(Collectors.toList());
		}
		List<DynamicTest> tests = new ArrayList<DynamicTest>();
		for (Path script : scripts) {
			String name = dir.relativize(script).toString();
			tests.add(DynamicTest.dynamicTest(name, () -> {
				String expected = cpythonDump(root, script);
				assertEquals(expected, AstDump.dump(PyParser.parse(read(script))));
			}));
		}
		return tests;
	}

	private static void assertSyntaxError(String source) {
		assertThrows(PySyntaxError.class, () -> PyParser.parse(source), source);
	}

	@Test
	void unsupportedConstructsAreRejected() {
		String[] sources = { "class A: pass", "with a: pass", "try:\n pass\nfinally:\n pass",
				"try:\n pass\nexcept E:\n pass\nfinally:\n pass", "def f(): yield 1", "x = (i for i in y)",
				"f(i for i in y)", "{1, 2}", "{x for x in y}", "(x := 1)", "async def f(): pass", "@dec\ndef f(): pass",
				"from a import b", "a @ b", "x: int = 1", "def f(a: int): pass", "def f() -> int: pass", "a, *b = c",
				"[*a]", "(*a, b)", "b'bytes'", "1j", "...", "x = {**a}", "def f(a, /): pass", "x = yield",
				"await x", "'\\N{DIGIT ONE}'", "f'{x=}'", "try:\n pass\nexcept* E:\n pass" };
		for (String s : sources)
			assertSyntaxError(s);
	}

	@Test
	void invalidProgramsAreRejected() {
		String[] sources = { "del 1", "1 = x", "f() = 1", "x + 1 = 2", "(a, 1) = x", "if a:\n\tb", "if a:\npass",
				"  x = 1", "if a:\n    b\n  c", "x = 'abc", "x = '''abc", "x = (1", "x = 1)", "def f(a=1, b): pass",
				"f(a=1, 2)", "x = 08", "x = 1__0", "x = 1_", "f'{}'", "f'{x'", "f'}'", "f'{x!z}'", "0x", "0b12",
				"if a b", "else:\n pass", "def f(*): pass", "for x in: pass", "x = ", "1 +", "[1, 2", "{1: }",
				"lambda x: ", "try:\n pass", "print(*)", "a = b = ", "$", "x = 1 ?", "def (a): pass", "for x in y:",
				"if x:\n", "while", "a.", "a[", "f(", "'\\x4'", "'\\u12'", "f'{a:{'", "1 if 2", "lambda *: 1",
				"def f(**k, a): pass", "del f()", "x = 99999999999999999999", "global", "import", "import a.",
				"not", "a not b", "a is", "x = [i for i in]", "x = [for i in y]", "f(a b)", "(", ")", "}",
				"if a:\n  b\n c" };
		for (String s : sources)
			assertSyntaxError(s);
	}

	@Test
	void deepNestingIsAnErrorNotAStackOverflow() {
		StringBuilder sb = new StringBuilder("x = ");
		for (int i = 0; i < 100000; i++)
			sb.append('(');
		assertSyntaxError(sb.toString());
		sb = new StringBuilder("x = ");
		for (int i = 0; i < 100000; i++)
			sb.append("- ");
		assertSyntaxError(sb.append('1').toString());
		sb = new StringBuilder();
		for (int i = 0; i < 1000; i++) {
			for (int k = 0; k < i; k++)
				sb.append(' ');
			sb.append("if a:\n");
		}
		assertSyntaxError(sb.toString());
		sb = new StringBuilder("x = ");
		for (int i = 0; i < 100000; i++)
			sb.append("not ");
		assertSyntaxError(sb.append('1').toString());
		sb = new StringBuilder("x = ");
		for (int i = 0; i < 100000; i++)
			sb.append('[');
		assertSyntaxError(sb.toString());
	}

	@Test
	void truncatedAndDamagedSourceOnlyGivesSyntaxErrors() throws Exception {
		for (String name : SYNTAX_FILES) {
			String source = read(resource("python-ast/" + name + ".py"));
			for (int cut = 0; cut < source.length(); cut += 37) {
				String truncated = source.substring(0, cut);
				try {
					PyParser.parse(truncated);
				} catch (PySyntaxError expected) {
					// fine
				}
				String damaged = source.substring(0, cut) + source.substring(Math.min(source.length(), cut + 1));
				try {
					PyParser.parse(damaged);
				} catch (PySyntaxError expected) {
					// fine
				}
			}
		}
	}

	@Test
	void errorsReportTheirLine() {
		PySyntaxError e = assertThrows(PySyntaxError.class, () -> PyParser.parse("x = 1\ny = 2\nz = (\n"));
		assertTrue(e.getMessage().contains("line"), e.getMessage());
		e = assertThrows(PySyntaxError.class, () -> PyParser.parse("x = 1\ny = 2\nz = 3 ?\n"));
		assertEquals(3, e.line);
		e = assertThrows(PySyntaxError.class, () -> PyParser.parse("def f():\n    x = 1\n    class A: pass\n"));
		assertEquals(3, e.line);
	}

	@Test
	void sourceWithoutTrailingNewlineIsAccepted() {
		String[] sources = { "x = 1", "if a:\n    b", "def f():\n    return 1\n", "x = 1\n\n\n", "# only a comment",
				"", "\n\n", "if a:\n    b\n# trailing\n", "x = 1\r\ny = 2\r\n", "f(a)\n\n  # c\n" };
		for (String s : sources)
			PyParser.parse(s);
		assertEquals(2, PyParser.parse("x = 1\r\ny = 2\r\n").nodes("body").size());
	}

	@Test
	void treeCanBeNavigated() {
		Node module = PyParser.parse("def f(a, b=2):\n    return a + b\n");
		Node def = module.nodes("body").get(0);
		assertTrue(def.is("FunctionDef"));
		assertEquals("f", def.str("name"));
		Node args = def.node("args");
		assertEquals(2, args.nodes("args").size());
		assertEquals(1, args.nodes("defaults").size());
		Node ret = def.nodes("body").get(0);
		assertTrue(ret.is("Return"));
		assertEquals("Add", ret.node("value").node("op").type);
		assertEquals(1, def.line);
		assertEquals(2, ret.line);
	}

	@Test
	void stringValuesAreDecoded() {
		Node module = PyParser.parse("x = 'a\\n\\x41\\u00e9\\U0001F600\\101'\ny = r'\\n'\n");
		assertEquals("a\nA\u00e9\uD83D\uDE00A", module.nodes("body").get(0).node("value").get("value"));
		assertEquals("\\n", module.nodes("body").get(1).node("value").get("value"));
		Node big = PyParser.parse("x = 1_000 + 0xff\n");
		assertEquals(Long.valueOf(1000), big.nodes("body").get(0).node("value").node("left").get("value"));
		assertEquals(Long.valueOf(255), big.nodes("body").get(0).node("value").node("right").get("value"));
	}
}
