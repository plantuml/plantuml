package nonreg;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.TitledDiagram;

/*
 * All non-regression tests must extends BasicTest class.
 *
 * The tests must have a single test method that call the 'checkImage()' method.
 * Diagram to be tested must be stored in the test itself, separated by triple-quoted strings
 * The expected result (build using UGraphicDebug) is stored in a class xxxxResult.java, also separated by triple-quoted strings.
 *
 */
public class BasicTest extends AbstractNonRegTest {

	private static final boolean FORCE_RESULT_GENERATION = false;
	private static final boolean ENABLE_RESULT_GENERATION_IF_NONE_PRESENT = false;

	public BasicTest() {
		// We want a fully portable way of non regression test, so we force the usage of
		// Smetana. It probably means that non regression tests on
		// class/component/usecase are not complete.
		TitledDiagram.FORCE_SMETANA = true;
	}

	protected void checkImage(final String expectedDescription) throws IOException {
		final String actualResult = runPlantUML(expectedDescription);
		if (FORCE_RESULT_GENERATION
				|| (ENABLE_RESULT_GENERATION_IF_NONE_PRESENT && Files.exists(getResultFile()) == false)) {
			generatedResultJavaFile(actualResult, actualResult.getBytes(UTF_8));
		}
		final String imageExpectedResult = readTripleQuotedString(getResultFile());
		assertEquals(imageExpectedResult, actualResult);
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

	protected String runPlantUML(String expectedDescription) throws IOException {
			return runPlantUML(expectedDescription,FileFormat.DEBUG);
	}

}
