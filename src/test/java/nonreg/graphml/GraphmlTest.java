package nonreg.graphml;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

public class GraphmlTest {

	private static final String TRIPLE_QUOTE = "\"\"\"";

	protected void checkXmlAndDescription(final String expectedDescription)
			throws IOException, UnsupportedEncodingException {
		final String actualResult = runPlantUML(expectedDescription);
		final String xmlExpected = readStringFromSourceFile(getDiagramFile(), "{{{", "}}}");

		// This is really a hack. Since XML generation does not guarantee the order of
		// attributes, we make an easy to do check by sorting characters.
		// Of course, this is really incomplete: a faulty String may match the expected
		// result if, for example, an attribute is moved from a node to another.
		// However, we consider that it is a good start.
		if (sortString(actualResult).equals(sortString(xmlExpected)) == false) {
			assertEquals(xmlExpected, actualResult, "Generated GraphML is not ok");
		}
	}

	private String sortString(String s) {
		final Map<Character, AtomicInteger> map = new TreeMap<>();
		for (int i = 0; i < s.length(); i++) {
			final char ch = s.charAt(i);
			// We ignore non writable characters
			if (ch <= ' ')
				continue;

			AtomicInteger count = map.get(ch);
			if (count == null)
				map.put(ch, new AtomicInteger(1));
			else
				count.addAndGet(1);
		}
		return map.toString();
	}

	private String getLocalFolder() {
		return "src/test/java/" + getPackageName().replace(".", "/");
	}

	private String getPackageName() {
		return getClass().getPackage().getName();
	}

	private Path getDiagramFile() {
		return Paths.get(getLocalFolder(), getClass().getSimpleName() + ".java");
	}

	private String runPlantUML(String expectedDescription) throws IOException, UnsupportedEncodingException {
		final String diagramText = readStringFromSourceFile(getDiagramFile(), TRIPLE_QUOTE, TRIPLE_QUOTE);
		final SourceStringReader ssr = new SourceStringReader(diagramText, UTF_8);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DiagramDescription diagramDescription = ssr.outputImage(baos, 0,
				new FileFormatOption(FileFormat.GRAPHML));
		assertEquals(expectedDescription, diagramDescription.getDescription(), "Bad description");

		return new String(baos.toByteArray(), UTF_8);
	}

	private String readStringFromSourceFile(Path path, String startMarker, String endMarker) throws IOException {
		assertTrue(Files.exists(path), "Cannot find " + path);
		assertTrue(Files.isReadable(path), "Cannot read " + path);
		final List<String> allLines = Files.readAllLines(path, UTF_8);
		final int first = allLines.indexOf(startMarker);
		final int last = allLines.lastIndexOf(endMarker);
		assertTrue(first != -1);
		assertTrue(last != -1);
		assertTrue(last > first);
		return packString(allLines.subList(first + 1, last));
	}

	private String packString(Collection<String> list) {
		final StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
	}

}
