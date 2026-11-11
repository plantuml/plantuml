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

	private static final String[] SYNTAX_FILES = { "statements", "expressions", "strings", "numbers", "layout",
			"classes", "decorators" };

	// Line endings depend on core.autocrlf, so CRLF is normalized to LF: AstDump
	// and the reference dumps always use LF.
	private static String read(Path path) throws IOException {
		return new String(Files.readAllBytes(path), StandardCharsets.UTF_8).replace("\r\n", "\n");
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
		Path dir = resource("python");
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
		e = assertThrows(PySyntaxError.class, () -> PyParser.parse("def f():\n    x = 1\n    with a: pass\n"));
		assertEquals(3, e.line);
	}

	@Test
	void crlfLineEndingsAreAccepted() {
		// the other sources accepted with or without a final newline are in cases/accepted.cases
		assertEquals(2, PyParser.parse("x = 1\r\ny = 2\r\n").body.size());
	}

	@Test
	void treeCanBeNavigated() {
		Ast.Module module = PyParser.parse("def f(a, b=2):\n    return a + b\n");
		Ast.FunctionDef def = (Ast.FunctionDef) module.body.get(0);
		assertEquals("f", def.name);
		Ast.Arguments args = def.args;
		assertEquals(2, args.args.size());
		assertEquals(1, args.defaults.size());
		Ast.Return ret = (Ast.Return) def.body.get(0);
		assertEquals(Ast.Operator.Add, ((Ast.BinOp) ret.value).op);
		assertEquals("FunctionDef", def.type());
		assertEquals(1, def.line);
		assertEquals(2, ret.line);
	}

	@Test
	void stringValuesAreDecoded() {
		Ast.Module module = PyParser.parse("x = 'a\\n\\x41\\u00e9\\U0001F600\\101'\ny = r'\\n'\n");
		assertEquals("a\nA\u00e9\uD83D\uDE00A", value(module.body.get(0)));
		assertEquals("\\n", value(module.body.get(1)));
		Ast.Module big = PyParser.parse("x = 1_000 + 0xff\n");
		Ast.BinOp sum = (Ast.BinOp) ((Ast.Assign) big.body.get(0)).value;
		assertEquals(Long.valueOf(1000), ((Ast.Constant) sum.left).value);
		assertEquals(Long.valueOf(255), ((Ast.Constant) sum.right).value);
	}

	/** The value of the constant assigned by an assignment statement. */
	private static Object value(Ast.Stmt assign) {
		return ((Ast.Constant) ((Ast.Assign) assign).value).value;
	}
}
