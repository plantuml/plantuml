package nonreg;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

public abstract class AbstractNonRegTest {
	private static final String SOURCES_DIR_KEY = "nonreg.graphml.sources.directory";
	protected static final String TRIPLE_QUOTE = "\"\"\"";

	protected final Path getResultFile() {
		return getLocalFolder().resolve(getClass().getSimpleName() + "Result.java");
	}

	protected final Path getDiagramFile() {
		return getLocalFolder().resolve(getClass().getSimpleName() + ".java");
	}

	protected final Path getLocalFolder() {
		return getSourcesDirectory().resolve(getPackageName().replace(".", "/"));
	}

	protected final Path getSourcesDirectory() {
		String propertyValue = System.getProperty(SOURCES_DIR_KEY);
		requireNonNull(propertyValue, "required system property %s not found".formatted(SOURCES_DIR_KEY));
		return Path.of(propertyValue);
	}

	protected final String getPackageName() {
		return getClass().getPackage().getName();
	}

	protected final String runPlantUML(String expectedDescription,FileFormat fileFormat) throws IOException {
		final String diagramText = readTripleQuotedString(getDiagramFile());
		final SourceStringReader ssr = new SourceStringReader(diagramText, UTF_8);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DiagramDescription diagramDescription = ssr.outputImage(baos, 0, new FileFormatOption(fileFormat));
		assertEquals(expectedDescription, diagramDescription.getDescription(), "Bad description");
		return new String(baos.toByteArray(), UTF_8);
	}

	protected String readStringFromSourceFile(Path path, String startMarker, String endMarker) throws IOException {
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

	protected final String readTripleQuotedString(Path path) throws IOException {
		return  readStringFromSourceFile(path,TRIPLE_QUOTE,TRIPLE_QUOTE);
	}

	protected final String packString(Collection<String> list) {
		final StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
	}
}
