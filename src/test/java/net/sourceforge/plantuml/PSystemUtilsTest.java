package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.FileFormat.PNG;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.Isolated;

import net.sourceforge.plantuml.cli.AbstractCliTest;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.file.SuggestedFile;
import net.sourceforge.plantuml.preproc.Defines;

@Isolated
class PSystemUtilsTest extends AbstractCliTest {

	//
	// Test Cases
	//

	@Test
	void test_splitPng(@TempDir Path tempDir) throws Exception {
		final String source = "" + "@startuml\n" + "page 3x2\n" + "class foo\n" + "@enduml\n";

		final List<FileImageData> fileImageData = render(source, tempDir);

		assertLs("[output.png, output_001.png, output_002.png, output_003.png, output_004.png, output_005.png]",
				tempDir);

		assertTrue(Files.exists(tempDir.resolve("output.png")) && tempDir.resolve("output.png").toFile().length() > 0);
		assertTrue(Files.exists(tempDir.resolve("output_001.png")) && tempDir.resolve("output_001.png").toFile().length() > 0);
		assertTrue(Files.exists(tempDir.resolve("output_002.png")) && tempDir.resolve("output_002.png").toFile().length() > 0);
		assertTrue(Files.exists(tempDir.resolve("output_003.png")) && tempDir.resolve("output_003.png").toFile().length() > 0);
		assertTrue(Files.exists(tempDir.resolve("output_004.png")) && tempDir.resolve("output_004.png").toFile().length() > 0);
		assertTrue(Files.exists(tempDir.resolve("output_005.png")) && tempDir.resolve("output_005.png").toFile().length() > 0);

		assertEquals(6, fileImageData.size());
		assertEquals(6, tempDir.toFile().listFiles().length);
	}

	@Test
	void test_splitPng_no_split(@TempDir Path tempDir) throws Exception {
		final String source = "" + "@startuml\n" + "class foo\n" + "@enduml\n";

		final List<FileImageData> fileImageData = render(source, tempDir);

		assertTrue(Files.exists(tempDir.resolve("output.png")) && tempDir.resolve("output.png").toFile().length() > 0);
		assertEquals(1, fileImageData.size());
		assertEquals(1, tempDir.toFile().listFiles().length);
	}

	//
	// Test DSL
	//

	private static List<FileImageData> render(String source, Path tempDir) throws IOException {
		final SourceStringReader reader = new SourceStringReader(Defines.createEmpty(), source, UTF_8.name(),
				Collections.<String>emptyList());

		final Diagram diagram = reader.getBlocks().get(0).getDiagram();

		final SuggestedFile suggestedFile = SuggestedFile.fromOutputFile(tempDir.resolve("output").toFile(), PNG, 0);

		return PSystemUtils.exportDiagrams(diagram, suggestedFile, new FileFormatOption(PNG), false);
	}
}
