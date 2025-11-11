package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.sourceforge.plantuml.Run;

class RunFlagOutputDirTest extends AbstractCliTest {

	@TempDir
	Path outputDir;

	@Test
	void shouldGenerateSvgInAbsoluteOutputDirectory() throws IOException, InterruptedException {
		// Create a simple PlantUML source file in the temporary folder
		Path sourceFile = aliceBob_hello(tempDir, "test.txt");

		// Run PlantUML with an absolute output directory
		Run.main(new String[] { 
				"-output", outputDir.toAbsolutePath().toString(), 
				"-svg", 
				sourceFile.toAbsolutePath().toString() 
		});

		// Verify expected files
		assertLs("[test.txt]", tempDir);
		assertLs("[test.svg]", outputDir);

		Path svgFile = outputDir.resolve("test.svg");
		assertTrue(Files.exists(svgFile));

		// Check SVG content
		final String content = Files.readString(svgFile);
		assertTrue(content.contains("<svg"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("hello"));
	}

	@Test
	void shouldGenerateSvgInRelativeOutputSubdirectory() throws IOException, InterruptedException {
		// Create a source file in the temporary directory
		Path sourceFile = aliceBob_hello(tempDir, "test2.txt");

		// Define a relative output path (e.g., "foo/bar")
		Path relativeOutput = Path.of("foo", "bar");
		Path expectedOutputDir = tempDir.resolve(relativeOutput);
		Files.createDirectories(expectedOutputDir.getParent());

		// Run PlantUML with a relative -output path
		Run.main(new String[] {
				"-output", relativeOutput.toString(),
				"-svg",
				sourceFile.toAbsolutePath().toString()
		});

		// Verify that the SVG file has been created under tempDir/foo/bar/test2.svg
		Path expectedSvg = expectedOutputDir.resolve("test2.svg");
		assertTrue(Files.exists(expectedSvg), 
				"Expected SVG not found in relative output directory: " + expectedSvg);

		// Verify expected files
		assertLs("[foo, test2.txt]", tempDir);
		assertLs("[test2.svg]", expectedOutputDir);


		// Check SVG content
		final String content = Files.readString(expectedSvg);
		assertTrue(content.contains("<svg"));
		assertTrue(content.contains("alice"));
		assertTrue(content.contains("bob"));
		assertTrue(content.contains("hello"));
	}
}
