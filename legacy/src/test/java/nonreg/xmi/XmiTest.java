package nonreg.xmi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.plantuml.FileFormat;
import nonreg.AbstractNonRegTest;

public class XmiTest extends AbstractNonRegTest {

	protected void checkXmlAndDescription(final String expectedDescription)
			throws IOException, UnsupportedEncodingException {
		final String star = removeVersion(runPlantUML(expectedDescription, FileFormat.XMI_STAR));
		final String starExpected = readStringFromSourceFile(getDiagramFile(), "{{{star", "}}}star");

		// This is really a hack. Since XML generation does not guarantee the order of
		// attributes, we make an easy to do check by sorting characters.
		// Of course, this is really incomplete: a faulty String may match the expected
		// result if, for example, an attribute is moved from a node to another.
		// However, we consider that it is a good start.
		if (sortString(star).equals(sortString(starExpected)) == false) {
			assertEquals(starExpected, star, "XmiStar: Generated GraphML is not ok");
		}

		final String argo = removeVersion(runPlantUML(expectedDescription, FileFormat.XMI_ARGO));
		final String argoExpected = readStringFromSourceFile(getDiagramFile(), "{{{argo", "}}}argo");

		if (sortString(argo).equals(sortString(argoExpected)) == false) {
			assertEquals(argoExpected, argo, "XmiArgo: Generated GraphML is not ok");
		}

	}

	private String removeVersion(String xmi) {
		return xmi.replaceFirst("\\<XMI.exporterVersion\\>.*\\</XMI.exporterVersion\\>", "");
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
