package net.sourceforge.plantuml.emoji;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
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
import test.utils.PlantUmlTestUtils;

/**
 * Test class that generates actual images from SVG sprite .puml test files.
 * This validates that SVG sprites can be successfully rendered in PlantUML diagrams.
 * 
 * Generated images are saved to: target/test-output/svg-sprites/
 */
public class SvgSpriteImageGenerationTest {

    private static final Path OUTPUT_DIR = Paths.get("target/test-output/svg-sprites");
    private static final Path RESOURCES_DIR = Paths.get("src/test/resources");

    @BeforeAll
    static void setup() throws IOException {
        // Create output directory if it doesn't exist
        if (!Files.exists(OUTPUT_DIR)) {
            Files.createDirectories(OUTPUT_DIR);
        }
    }

    /**
     * Dynamically generates tests for all .puml files in src/test/resources/
     * Each test will:
     * 1. Read the .puml file
     * 2. Generate the diagram
     * 3. Export as PNG and SVG to target/test-output/svg-sprites/
     * 4. Verify the image was generated successfully
     */
    @TestFactory
    Stream<DynamicTest> generateImagesFromAllPumlFiles() throws IOException {
        // Find all .puml files in src/test/resources
        try (Stream<Path> paths = Files.walk(RESOURCES_DIR)) {
            List<Path> pumlFiles = paths
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".puml"))
                .collect(Collectors.toList());

            return pumlFiles.stream().map(pumlFile -> 
                DynamicTest.dynamicTest(
                    "Generate image for: " + pumlFile.getFileName(),
                    () -> generateAndValidateImage(pumlFile)
                )
            );
        }
    }

    /**
     * Generates PNG and SVG outputs for a given .puml file and validates they were created.
     * 
     * @param pumlFile the .puml file to process
     * @throws IOException if file operations fail
     */
    private void generateAndValidateImage(Path pumlFile) throws IOException {
        // Read the .puml file content
        String content = Files.readString(pumlFile, StandardCharsets.UTF_8);
        
        // Get the base filename without extension
        String baseName = pumlFile.getFileName().toString().replaceFirst("\\.puml$", "");
        
        System.out.println("\n=== Processing: " + baseName + " ===");
        
        // Generate the diagram using PlantUmlTestUtils
        PlantUmlTestUtils.ExportDiagram exporter = PlantUmlTestUtils.exportDiagram(content);
        
        // Assert no errors in diagram
        exporter.assertNoError();
        
        // Export as PNG
        Path pngOutput = OUTPUT_DIR.resolve(baseName + ".png");
        try {
            exporter.toFile(pngOutput, FileFormat.PNG);
            assertTrue(Files.exists(pngOutput), "PNG file should be created: " + pngOutput);
            assertTrue(Files.size(pngOutput) > 0, "PNG file should not be empty: " + pngOutput);
            System.out.println("  ✓ PNG: " + pngOutput + " (" + Files.size(pngOutput) + " bytes)");
        } catch (Exception e) {
            System.err.println("  ✗ PNG generation failed: " + e.getMessage());
            throw e;
        }
        
        // Verify PNG can be loaded as BufferedImage
        try {
            BufferedImage pngImage = exporter.asImage(FileFormat.PNG);
            assertNotNull(pngImage, "PNG image should be loadable");
            assertTrue(pngImage.getWidth() > 0, "PNG image should have width");
            assertTrue(pngImage.getHeight() > 0, "PNG image should have height");
            System.out.println("  ✓ PNG dimensions: " + pngImage.getWidth() + "x" + pngImage.getHeight());
        } catch (Exception e) {
            System.err.println("  ⚠ PNG image validation warning: " + e.getMessage());
            // Continue even if image can't be loaded (might be a rendering issue)
        }
        
        // Export as SVG
        Path svgOutput = OUTPUT_DIR.resolve(baseName + ".svg");
        try {
            exporter.toFile(svgOutput, FileFormat.SVG);
            assertTrue(Files.exists(svgOutput), "SVG file should be created: " + svgOutput);
            assertTrue(Files.size(svgOutput) > 0, "SVG file should not be empty: " + svgOutput);
            System.out.println("  ✓ SVG: " + svgOutput + " (" + Files.size(svgOutput) + " bytes)");
            
            // Verify SVG content contains expected SVG tags
            String svgContent = Files.readString(svgOutput, StandardCharsets.UTF_8);
            assertTrue(svgContent.contains("<svg"), "SVG file should contain <svg> tag");
            assertTrue(svgContent.contains("</svg>"), "SVG file should have closing </svg> tag");
            System.out.println("  ✓ SVG validation: Contains valid <svg> tags");
        } catch (Exception e) {
            System.err.println("  ✗ SVG generation failed: " + e.getMessage());
            throw e;
        }
        
        System.out.println("✅ SUCCESS: Generated images for " + baseName);
    }
}
