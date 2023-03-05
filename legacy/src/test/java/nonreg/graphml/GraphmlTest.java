package nonreg.graphml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.plantuml.FileFormat;
import nonreg.AbstractNonRegTest;

public class GraphmlTest extends AbstractNonRegTest {

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

	private String runPlantUML(String expectedDescription) throws IOException, UnsupportedEncodingException {
		return runPlantUML(expectedDescription,FileFormat.GRAPHML);
	}

}
