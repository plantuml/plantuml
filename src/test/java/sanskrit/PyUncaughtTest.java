package sanskrit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

/**
 * Runs the scripts of {@code python/uncaught}: each {@code NAME.py} has a
 * {@code NAME.err} that says how the run ends (an uncaught Python error, the
 * end of the instruction budget, or no error) and, optionally, a
 * {@code NAME.out} with what the script printed before. This is data, so that
 * the Go port runs the same cases. The format is in {@code python/uncaught/README.md}.
 */
class PyUncaughtTest {

	private static final Pattern FRAME = Pattern.compile("line (\\d+), in (.*)");

	/** The content of a {@code .err} file. */
	static final class Expect {
		long maxSteps = -1;
		int maxDepth = -1;
		String error;
		Integer line;
		String text;
		final List<String> trace = new ArrayList<String>();

		static Expect parse(String content) {
			Expect e = new Expect();
			for (String raw : content.replace("\r\n", "\n").split("\n")) {
				String l = raw.trim();
				if (l.isEmpty() || l.startsWith("#"))
					continue;
				int colon = l.indexOf(':');
				String key = l.substring(0, colon);
				String value = raw.substring(raw.indexOf(':') + 1).trim();
				if (key.equals("max-steps"))
					e.maxSteps = Long.parseLong(value);
				else if (key.equals("max-depth"))
					e.maxDepth = Integer.parseInt(value);
				else if (key.equals("error"))
					e.error = value;
				else if (key.equals("line"))
					e.line = Integer.valueOf(value);
				else if (key.equals("text"))
					e.text = raw.substring(raw.indexOf(':') + 2); // keeps the spaces of the message
				else if (key.equals("trace"))
					e.trace.add(value);
				else
					throw new IllegalArgumentException("unknown key '" + key + "' in " + content);
			}
			if (e.error == null)
				throw new IllegalArgumentException("no 'error:' in " + content);
			return e;
		}
	}

	private static String read(Path path) throws IOException {
		return new String(Files.readAllBytes(path), StandardCharsets.UTF_8).replace("\r\n", "\n");
	}

	private static Path root() throws URISyntaxException {
		URL url = PyUncaughtTest.class.getClassLoader().getResource("python/uncaught");
		assertTrue(url != null, "missing resource python/uncaught");
		return Paths.get(url.toURI());
	}

	@TestFactory
	List<DynamicTest> scriptsEndAsExpected() throws Exception {
		Path dir = root();
		List<Path> scripts;
		try (Stream<Path> walk = Files.walk(dir)) {
			scripts = walk.filter(f -> f.toString().endsWith(".py")).sorted().collect(Collectors.toList());
		}
		assertTrue(scripts.isEmpty() == false, "no script in " + dir);
		List<DynamicTest> tests = new ArrayList<DynamicTest>();
		for (Path script : scripts) {
			String name = dir.relativize(script).toString();
			tests.add(DynamicTest.dynamicTest(name, () -> check(script)));
		}
		return tests;
	}

	private static void check(Path script) throws IOException {
		String base = script.toString().replaceAll("\\.py$", "");
		Expect expect = Expect.parse(read(Paths.get(base + ".err")));
		StringBuilder out = new StringBuilder();
		Interp in = new Interp(out::append);
		if (expect.maxSteps >= 0)
			in.setMaxSteps(expect.maxSteps);
		if (expect.maxDepth >= 0)
			in.setMaxDepth(expect.maxDepth);
		String actual = "none";
		try {
			in.run(read(script), script.getFileName().toString());
		} catch (PyError e) {
			actual = e.typeName();
			if (expect.error.equals(actual)) {
				if (expect.line != null)
					assertEquals(expect.line.intValue(), e.getLine(), "line");
				if (expect.text != null)
					assertEquals(expect.text, e.text(), "text");
				if (expect.trace.isEmpty() == false)
					assertEquals(expect.trace, trace(e), "trace");
			}
		} catch (ExecutionLimitError e) {
			actual = "ExecutionLimitError";
			if (expect.error.equals(actual) && expect.text != null)
				assertEquals(expect.text, e.getMessage(), "text");
		}
		assertEquals(expect.error, actual, "how the script ends");
		Path ref = Paths.get(base + ".out");
		if (Files.exists(ref))
			assertEquals(read(ref), out.toString(), "output before the end");
		else if (out.length() != 0)
			fail("the script prints something, and there is no .out: " + out);
	}

	/** The frames of the traceback as "line function", the outermost first, as the message of the error lists them. */
	private static List<String> trace(PyError e) {
		List<String> frames = new ArrayList<String>();
		Matcher m = FRAME.matcher(e.getMessage());
		while (m.find())
			frames.add(m.group(1) + " " + m.group(2));
		return frames;
	}
}
