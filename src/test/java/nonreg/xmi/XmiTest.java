package nonreg.xmi;

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

import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

public class XmiTest {

	private static final String TRIPLE_QUOTE = "\"\"\"";

	protected void checkXmlAndDescription(final String expectedDescription)
			throws IOException, UnsupportedEncodingException {
		final String star = removeVersion(runPlantUML(expectedDescription, FileFormat.XMI_STAR));
		final String starExpected = readStringFromSourceFile(getDiagramFile(), "{{{star", "}}}star");

		assertXMIEqual(star, starExpected);

		final String argo = removeVersion(runPlantUML(expectedDescription, FileFormat.XMI_ARGO));
		final String argoExpected = readStringFromSourceFile(getDiagramFile(), "{{{argo", "}}}argo");

		assertXMIEqual(argo, argoExpected);
		
		final String script = removeVersion(runPlantUML(expectedDescription, FileFormat.XMI_SCRIPT));
		final String scriptExpected = readStringFromSourceFile(getDiagramFile(), "{{{script", "}}}script");

		assertXMIEqual(script, scriptExpected);
	}

	private void assertXMIEqual(final String actual, final String expected) {
		// XMI is XML, so we can just use the xmlunit diffbuilder
		// Compare elements with the same xmi ID
		// checkForSimilar required to ignore order
		Diff diff = DiffBuilder.compare(Input.fromString(expected)).withTest(Input.fromString(actual))
				.ignoreWhitespace().ignoreComments().checkForSimilar()
				.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAttributes("xmi.id"))).build();
		
		if (diff.hasDifferences()) {
			System.out.println("Generated XMI: ");
			System.out.println(actual);
			assertTrue(false, diff.fullDescription());
		}
	}

	private String removeVersion(String xmi) {
		return xmi.replaceFirst("\\<XMI.exporterVersion\\>.*\\</XMI.exporterVersion\\>", "");
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

	private String runPlantUML(String expectedDescription, FileFormat format)
			throws IOException, UnsupportedEncodingException {
		final String diagramText = readStringFromSourceFile(getDiagramFile(), TRIPLE_QUOTE, TRIPLE_QUOTE);
		final SourceStringReader ssr = new SourceStringReader(diagramText, UTF_8);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DiagramDescription diagramDescription = ssr.outputImage(baos, 0, new FileFormatOption(format));
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
