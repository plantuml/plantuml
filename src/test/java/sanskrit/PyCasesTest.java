package sanskrit;

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
import org.junit.jupiter.api.TestFactory;

/**
 * Runs the {@code *.cases} files of {@code python/cases}: many short sources
 * in one file, each with the expected outcome of compiling it. This is data,
 * not Java, so that another implementation (the Go port) runs the very same
 * cases. The format is described in {@code python/cases/README.md}.
 */
class PyCasesTest {

	/** One case of a {@code .cases} file. */
	static final class Case {
		/** Line of the header in the file, 1-based. */
		final int headerLine;
		/** The header text after "### ", for instance "line 3" or "ok noeol". */
		final String header;
		final String source;

		Case(int headerLine, String header, String source) {
			this.headerLine = headerLine;
			this.header = header;
			this.source = source;
		}

		boolean isOk() {
			return header.startsWith("ok");
		}

		/** The line of the expected syntax error, or -1 when the source must compile. */
		int expectedLine() {
			return isOk() ? -1 : Integer.parseInt(header.substring("line ".length()).trim());
		}
	}

	private static final String MARK = "### ";

	/**
	 * Splits the text of a {@code .cases} file. The source of a case is its lines
	 * joined by LF and followed by a LF, unless the header ends with "noeol".
	 */
	static List<Case> parse(String text) {
		List<Case> cases = new ArrayList<Case>();
		String[] lines = text.replace("\r\n", "\n").split("\n", -1);
		int n = lines.length;
		if (n > 0 && lines[n - 1].isEmpty())
			n--; // the final LF of the file ends the last line, it does not start another one
		int i = 0;
		while (i < n) {
			if (lines[i].startsWith(MARK) == false)
				throw new IllegalArgumentException("line " + (i + 1) + ": a case must start with '" + MARK + "'");
			String header = lines[i].substring(MARK.length()).trim();
			int start = i + 1;
			int end = start;
			while (end < n && lines[end].startsWith(MARK) == false)
				end++;
			StringBuilder sb = new StringBuilder();
			for (int k = start; k < end; k++) {
				if (k > start)
					sb.append('\n');
				sb.append(lines[k]);
			}
			if (header.endsWith("noeol") == false && end > start)
				sb.append('\n');
			cases.add(new Case(i + 1, header, sb.toString()));
			i = end;
		}
		return cases;
	}

	private static Path root() throws URISyntaxException {
		URL url = PyCasesTest.class.getClassLoader().getResource("python/cases");
		assertTrue(url != null, "missing resource python/cases");
		return Paths.get(url.toURI());
	}

	@TestFactory
	List<DynamicTest> casesGiveTheExpectedOutcome() throws Exception {
		Path dir = root();
		List<Path> files;
		try (Stream<Path> walk = Files.walk(dir)) {
			files = walk.filter(f -> f.toString().endsWith(".cases")).sorted().collect(Collectors.toList());
		}
		assertTrue(files.isEmpty() == false, "no .cases file in " + dir);
		List<DynamicTest> tests = new ArrayList<DynamicTest>();
		for (Path file : files) {
			String text = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
			for (Case c : parse(text)) {
				String name = dir.relativize(file) + ":" + c.headerLine;
				tests.add(DynamicTest.dynamicTest(name, () -> check(c)));
			}
		}
		return tests;
	}

	private static void check(Case c) {
		if (c.isOk()) {
			Interp.compile(c.source, "case.py");
			return;
		}
		PySyntaxError e = assertThrows(PySyntaxError.class, () -> Interp.compile(c.source, "case.py"), c.source);
		assertEquals(c.expectedLine(), e.line, e.getMessage() + "\n" + c.source);
	}

	static void write(Path file, String text) throws IOException {
		Files.write(file, text.getBytes(StandardCharsets.UTF_8));
	}
}
