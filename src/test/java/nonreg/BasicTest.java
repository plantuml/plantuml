package nonreg;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

/*
 * All non-regression tests must extends BasicTest class.
 *
 * The tests must have a single test method that call the 'checkImage()' method.
 * Diagram to be tested must be stored in the test itself, separated by triple-quoted strings
 * The expected result (build using UGraphicDebug, e.g. <code>String actualResult = runPlantUML("(9 entities)");</code>)
 * is stored in a class xxxxResult.java, also separated by triple-quoted strings.
 *
 */
public class BasicTest {

	private static final String TRIPLE_QUOTE = "\"\"\"";

	private static final boolean FORCE_RESULT_GENERATION = false;
	private static final boolean ENABLE_RESULT_GENERATION_IF_NONE_PRESENT = false;

	public BasicTest() {
		// We want a fully portable way of non regression test, so we force the usage of
		// Smetana. It probably means that non regression tests on
		// class/component/usecase are not complete.
		TitledDiagram.FORCE_SMETANA = true;
	}

	protected void checkImage(final String expectedDescription) throws IOException, UnsupportedEncodingException {
		final String actualResult = runPlantUML(expectedDescription);
		if (FORCE_RESULT_GENERATION
				|| (ENABLE_RESULT_GENERATION_IF_NONE_PRESENT && Files.exists(getResultFile()) == false)) {
			generatedResultJavaFile(actualResult, actualResult.getBytes(UTF_8));
		}
		final String imageExpectedResult = readTripleQuotedString(getResultFile());
		assertThat(actualResult).isEqualTo(imageExpectedResult);
	}

	private void generatedResultJavaFile(String actualResult, byte[] bytes) throws IOException {

		try (BufferedWriter writer = Files.newBufferedWriter(getResultFile(), UTF_8)) {
			writer.write("package " + getPackageName() + ";\n");
			writer.write("\n");
			writer.write("public class " + getClass().getSimpleName() + "Result {\n");
			writer.write("}\n");
			writer.write("/*\n");
			writer.write(TRIPLE_QUOTE + "\n");
			writer.write(actualResult);
			writer.write(TRIPLE_QUOTE + "\n");
			writer.write("*/");
		}

	}

	protected String getLocalFolder() {
		return "src/test/java/" + getPackageName().replace(".", "/");
	}

	private String getPackageName() {
		return getClass().getPackage().getName();
	}

	protected Path getResultFile() {
		return Paths.get(getLocalFolder(), getClass().getSimpleName() + "Result.java");
	}

	protected Path getDiagramFile() {
		return Paths.get(getLocalFolder(), getClass().getSimpleName() + ".java");
	}

	protected String runPlantUML(String expectedDescription) throws IOException, UnsupportedEncodingException {
		final String diagramText = readTripleQuotedString(getDiagramFile());
		final SourceStringReader ssr = new SourceStringReader(diagramText, UTF_8);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DiagramDescription diagramDescription = ssr.outputImage(baos, 0, new FileFormatOption(FileFormat.DEBUG));
		assertThat(diagramDescription.getDescription()).as("Bad description").isEqualTo(expectedDescription);

		return new String(baos.toByteArray(), UTF_8);
	}

	protected String readTripleQuotedString(Path path) throws IOException {
		assertThat(Files.exists(path)).as("Cannot find " + path).isTrue();
		assertThat(Files.isReadable(path)).as("Cannot read " + path).isTrue();
		final List<String> allLines = Files.readAllLines(path, UTF_8);
		final int first = allLines.indexOf(TRIPLE_QUOTE);
		final int last = allLines.lastIndexOf(TRIPLE_QUOTE);
		assertThat(first != -1).isTrue();
		assertThat(last != -1).isTrue();
		assertThat(last > first).isTrue();
		return packString(allLines.subList(first + 1, last));
	}

	protected String packString(Collection<String> list) {
		final StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
	}

}