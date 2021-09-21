package net.sourceforge.plantuml;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.FileFormat.PNG;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.preproc.Defines;

class PSystemUtilsTest {

	//
	// Test Cases
	//

	@Test
	void test_splitPng(@TempDir Path tempDir) throws Exception {
		final String source = "" +
				"@startuml\n" +
				"page 3x2\n" +
				"class foo\n" +
				"@enduml\n";

		final List<FileImageData> fileImageData = render(source, tempDir);

		assertThat(tempDir.resolve("output.png"))
				.isNotEmptyFile();
		assertThat(tempDir.resolve("output_001.png"))
				.isNotEmptyFile();
		assertThat(tempDir.resolve("output_002.png"))
				.isNotEmptyFile();
		assertThat(tempDir.resolve("output_003.png"))
				.isNotEmptyFile();
		assertThat(tempDir.resolve("output_004.png"))
				.isNotEmptyFile();
		assertThat(tempDir.resolve("output_005.png"))
				.isNotEmptyFile();

		assertThat(fileImageData)
				.hasSize(6);
		assertThat(tempDir.toFile().listFiles())
				.hasSize(6);
	}

	@Test
	void test_splitPng_no_split(@TempDir Path tempDir) throws Exception {
		final String source = "" +
				"@startuml\n" +
				"class foo\n" +
				"@enduml\n";

		final List<FileImageData> fileImageData = render(source, tempDir);

		assertThat(tempDir.resolve("output.png"))
				.isNotEmptyFile();
		assertThat(fileImageData)
				.hasSize(1);
		assertThat(tempDir.toFile().listFiles())
				.hasSize(1);
	}

	//
	// Test DSL
	//

	private static List<FileImageData> render(String source, Path tempDir) throws IOException {
		final SourceStringReader reader = new SourceStringReader(Defines.createEmpty(), source, UTF_8.name(), Collections.<String>emptyList());

		final Diagram diagram = reader.getBlocks().get(0).getDiagram();

		final SuggestedFile suggestedFile = SuggestedFile.fromOutputFile(tempDir.resolve("output").toFile(), PNG, 0);

		return PSystemUtils.exportDiagrams(diagram, suggestedFile, new FileFormatOption(PNG), false);
	}
}
