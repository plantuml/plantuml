package nonreg.eps;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;

public class EpsTest {

	private static final String TRIPLE_QUOTE = "\"\"\"";

	public EpsTest() {
		TitledDiagram.FORCE_SMETANA = true;
	}

	protected void checkEpsAndDescription(final String expectedDescription) throws IOException {
		final String actual = runPlantUML(expectedDescription);
		assertTrue(actual.contains("%!PS-Adobe"), "Output should be valid EPS");
		assertTrue(actual.contains("%%EOF"), "EPS should end with %%EOF");
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

	private String runPlantUML(String expectedDescription) throws IOException {
		final String diagramText = readStringFromSourceFile(getDiagramFile(), TRIPLE_QUOTE, TRIPLE_QUOTE);
		final SourceStringReader ssr = new SourceStringReader(diagramText, UTF_8);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DiagramDescription diagramDescription = ssr.outputImage(baos, 0, new FileFormatOption(FileFormat.EPS, false));
		assertEquals(expectedDescription, diagramDescription.getDescription(), "Bad description");
		return new String(baos.toByteArray(), UTF_8);
	}

	private String readStringFromSourceFile(Path path, String startMarker, String endMarker) throws IOException {
		assertTrue(Files.exists(path), "Cannot find " + path);
		assertTrue(Files.isReadable(path), "Cannot read " + path);
		final List<String> allLines = Files.readAllLines(path, UTF_8);
		final int first = allLines.indexOf(startMarker);
		final int last = allLines.lastIndexOf(endMarker);
		assertTrue(first != -1, "Cannot find start marker " + startMarker);
		assertTrue(last != -1, "Cannot find end marker " + endMarker);
		assertTrue(last > first, "End marker must come after start marker");
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
