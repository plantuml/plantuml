package net.sourceforge.plantuml.svg.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.klimt.awt.PortableImage;
import test.utils.PlantUmlTestUtils;

/**
 * Generates images using SvgNanoParser for comparison with SAX output.
 * Does not fail on SVG error text since this is only for visual comparison.
 */
public class SvgSpriteImageGenerationNanoTest {

	private static final Path OUTPUT_DIR = Paths.get("target/test-output/svg-sprites");
	private static final Path RESOURCES_DIR = Paths.get("src/test/resources/svg-sprites");
	// NOTE: SAX parser does not support <style> blocks; keep this file excluded for now.
	private static final String[] SKIPPED_PUML_FILES = { "svg2GroupsWithStyle.puml" };

	@BeforeAll
	static void setup() throws IOException {
		if (!Files.exists(OUTPUT_DIR))
			Files.createDirectories(OUTPUT_DIR);
	}

	@TestFactory
	Stream<DynamicTest> generateImagesFromAllPumlFiles() throws IOException {
		if (SKIPPED_PUML_FILES.length > 0) {
			System.out.println("\n=== Skipping SVG sprite files ===");
			for (String skip : SKIPPED_PUML_FILES)
				System.out.println("  - " + skip);
		}
		try (Stream<Path> paths = Files.walk(RESOURCES_DIR)) {
			List<Path> pumlFiles = paths
					.filter(Files::isRegularFile)
					.filter(p -> p.toString().endsWith(".puml"))
					.filter(p -> shouldInclude(p.getFileName().toString()))
					.collect(Collectors.toList());

			return pumlFiles.stream().map(pumlFile ->
					DynamicTest.dynamicTest(
							"Generate nano image for: " + pumlFile.getFileName(),
							() -> generateAndValidateImage(pumlFile)
						)
				);
		}
	}

	private void generateAndValidateImage(Path pumlFile) throws IOException {
		String content = Files.readString(pumlFile, StandardCharsets.UTF_8);
		content = forceNanoPragma(content);
		String baseName = pumlFile.getFileName().toString().replaceFirst("\\.puml$", "");
		String nanoBaseName = baseName + "_nano";

		System.out.println("\n=== Processing (nano): " + baseName + " ===");

		PlantUmlTestUtils.ExportDiagram exporter = PlantUmlTestUtils.exportDiagram(content);

		Path pngOutput = OUTPUT_DIR.resolve(nanoBaseName + ".png");
		try {
			exporter.toFile(pngOutput, FileFormat.PNG);
			assertTrue(Files.exists(pngOutput), "PNG file should be created: " + pngOutput);
			assertTrue(Files.size(pngOutput) > 0, "PNG file should not be empty: " + pngOutput);
			System.out.println("  ✓ PNG: " + pngOutput + " (" + Files.size(pngOutput) + " bytes)");
		} catch (Exception e) {
			System.err.println("  ✗ PNG generation failed: " + e.getMessage());
			throw e;
		}

		try {
			PortableImage pngImage = exporter.asImage(FileFormat.PNG);
			assertNotNull(pngImage, "PNG image should be loadable");
			assertTrue(pngImage.getWidth() > 0, "PNG image should have width");
			assertTrue(pngImage.getHeight() > 0, "PNG image should have height");
			System.out.println("  ✓ PNG dimensions: " + pngImage.getWidth() + "x" + pngImage.getHeight());
		} catch (Exception e) {
			System.err.println("  ⚠ PNG image validation warning: " + e.getMessage());
		}

		Path svgOutput = OUTPUT_DIR.resolve(nanoBaseName + ".svg");
		try {
			exporter.toFile(svgOutput, FileFormat.SVG);
			assertTrue(Files.exists(svgOutput), "SVG file should be created: " + svgOutput);
			assertTrue(Files.size(svgOutput) > 0, "SVG file should not be empty: " + svgOutput);
			System.out.println("  ✓ SVG: " + svgOutput + " (" + Files.size(svgOutput) + " bytes)");

			String svgContent = Files.readString(svgOutput, StandardCharsets.UTF_8);
			assertTrue(svgContent.contains("<svg"), "SVG file should contain <svg> tag");
			assertTrue(svgContent.contains("</svg>"), "SVG file should have closing </svg> tag");
			System.out.println("  ✓ SVG validation: Contains valid <svg> tags");
		} catch (Exception e) {
			System.err.println("  ✗ SVG generation failed: " + e.getMessage());
			throw e;
		}

		System.out.println("✅ SUCCESS: Generated nano images for " + baseName);
	}

	private boolean shouldInclude(String fileName) {
		for (String skip : SKIPPED_PUML_FILES) {
			if (skip.equals(fileName))
				return false;
		}
		return true;
	}

	private String forceNanoPragma(String content) {
		String updated = content.replaceFirst("(?m)^!pragma\\s+svgparser\\s+\\S+\\s*$", "!pragma svgparser nano");
		if (updated.equals(content))
			updated = content.replaceFirst("(?m)^@startuml\\s*$", "@startuml\n!pragma svgparser nano");

		return updated;
	}
}
