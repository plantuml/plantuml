package nonreg.scxml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.plantuml.FileFormat;
import nonreg.AbstractNonRegTest;

public class ScXmlTest extends AbstractNonRegTest {

	protected void checkXmlAndDescription(final String expectedDescription)
			throws IOException, UnsupportedEncodingException {
		final String actual = runPlantUML(expectedDescription, FileFormat.SCXML);
		final String expected = readStringFromSourceFile(getDiagramFile(), "{{{", "}}}");

		if (sortString(actual).equals(sortString(expected)) == false) {
			assertEquals(expected, actual, "Generated ScXml is not ok");
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

}
