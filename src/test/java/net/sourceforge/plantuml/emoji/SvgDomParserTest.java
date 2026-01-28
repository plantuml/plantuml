package net.sourceforge.plantuml.emoji;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSimple;

/**
 * Unit tests for SvgDomParser using .puml sprite resource files.
 * 
 * <p>Each test:
 * <ul>
 *   <li>Extracts SVG content from a .puml resource file using @PumlFile annotation</li>
 *   <li>Creates a SvgDomParser instance with the extracted SVG</li>
 *   <li>Verifies that drawU() correctly delegates to UGraphic mock</li>
 * </ul>
 * 
 * <p>Test coverage includes:
 * <ul>
 *   <li>Basic shapes (rect, circle, ellipse, polyline)</li>
 *   <li>Path elements with stroke styling</li>
 *   <li>Text elements with color</li>
 *   <li>Linear gradients (on rectangles and circles)</li>
 *   <li>Groups (g elements) with and without style attributes</li>
 * </ul>
 */
public class SvgDomParserTest {

    /**
     * Custom annotation to specify the .puml resource file for a test.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PumlFile {
        String value();
    }

    /**
     * Utility method to extract SVG content from a .puml file.
     * Finds the first "sprite" token and then the <svg>...</svg> that follows it.
     * 
     * @param filename the name of the .puml file in src/test/resources/
     * @return the extracted SVG string
     * @throws Exception if file cannot be read or SVG not found
     */
    private String extractSvgFromPumlFile(String filename) throws Exception {
        final Path p = Path.of("src/test/resources/" + filename);
        final String content = Files.readString(p, StandardCharsets.UTF_8);

        // Find the first "sprite" token and then the <svg>...</svg> that follows it.
        final int spriteIdx = content.indexOf("sprite ");
        final int searchFrom = spriteIdx >= 0 ? spriteIdx : 0;
        final int svgStart = content.indexOf("<svg", searchFrom);
        final int svgEnd = svgStart >= 0 ? content.indexOf("</svg>", svgStart) : -1;

        assertTrue(svgStart >= 0 && svgEnd > svgStart, 
            "Could not find <svg>...</svg> in " + p);

        return content.substring(svgStart, svgEnd + "</svg>".length());
    }

    /**
     * Helper method to extract SVG from the @PumlFile annotation on the calling test method.
     * 
     * @param testInfo JUnit test information
     * @return the extracted SVG string
     * @throws Exception if annotation missing or file cannot be read
     */
    private String extractSvgFromAnnotation(TestInfo testInfo) throws Exception {
        final PumlFile annotation = testInfo.getTestMethod()
            .orElseThrow(() -> new IllegalStateException("Test method not found"))
            .getAnnotation(PumlFile.class);
        
        if (annotation == null) {
            throw new IllegalStateException("@PumlFile annotation is required");
        }
        
        return extractSvgFromPumlFile(annotation.value());
    }

    // ========== Individual test methods with @PumlFile annotation ==========

    @Test
    @PumlFile("svgRedStroke.puml")
    public void testRedStrokeSVG(TestInfo testInfo) throws Exception {
        final String svg = extractSvgFromAnnotation(testInfo);
        final SvgDomParser parser = new SvgDomParser(svg);
        final UGraphic ug = mock(UGraphic.class, org.mockito.Mockito.RETURNS_SELF);

        HColorSimple fontColor = HColorSimple.create(java.awt.Color.BLACK);
        HColorSimple forcedColor = HColorSimple.create(java.awt.Color.RED);

        parser.drawU(ug, 1.0, fontColor, forcedColor);

        // Custom assertions for this specific test
        verify(ug, atLeastOnce()).draw(any());
    }

    @Test
    @PumlFile("svgRedCircle.puml")
    public void testRedCircleSVG(TestInfo testInfo) throws Exception {
        final String svg = extractSvgFromAnnotation(testInfo);
        final SvgDomParser parser = new SvgDomParser(svg);
        final UGraphic ug = mock(UGraphic.class, org.mockito.Mockito.RETURNS_SELF);

        HColorSimple fontColor = HColorSimple.create(java.awt.Color.BLACK);
        HColorSimple forcedColor = HColorSimple.create(java.awt.Color.RED);

        parser.drawU(ug, 1.0, fontColor, forcedColor);

        verify(ug, atLeastOnce()).draw(any());
    }

    @Test
    @PumlFile("svg2Groups.puml")
    public void testSvg2Groups(TestInfo testInfo) throws Exception {
        final String svg = extractSvgFromAnnotation(testInfo);
        final SvgDomParser parser = new SvgDomParser(svg);
        final UGraphic ug = mock(UGraphic.class, org.mockito.Mockito.RETURNS_SELF);

        HColorSimple fontColor = HColorSimple.create(java.awt.Color.BLACK);
        HColorSimple forcedColor = HColorSimple.create(java.awt.Color.RED);

        parser.drawU(ug, 1.0, fontColor, forcedColor);

        // Custom verification for multiple groups
        verify(ug, atLeastOnce()).draw(any());
        // Could add: verify(ug, times(2)).apply(any(UTranslate.class));
    }

    @Test
    @PumlFile("svg2GroupsWithStyle.puml")
    public void testSvg2GroupsWithStyle(TestInfo testInfo) throws Exception {
        final String svg = extractSvgFromAnnotation(testInfo);
        final SvgDomParser parser = new SvgDomParser(svg);
        final UGraphic ug = mock(UGraphic.class, org.mockito.Mockito.RETURNS_SELF);

        HColorSimple fontColor = HColorSimple.create(java.awt.Color.BLACK);
        HColorSimple forcedColor = HColorSimple.create(java.awt.Color.RED);

        parser.drawU(ug, 1.0, fontColor, forcedColor);

        // Custom verification for styled groups
        verify(ug, atLeastOnce()).draw(any());
        // Could add: verify(ug, atLeastOnce()).apply(any(HColor.class));
    }

    @Test
    @PumlFile("svgRedText.puml")
    public void testSvgWithRedText(TestInfo testInfo) throws Exception {
        final String svg = extractSvgFromAnnotation(testInfo);
        final SvgDomParser parser = new SvgDomParser(svg);
        final UGraphic ug = mock(UGraphic.class, org.mockito.Mockito.RETURNS_SELF);

        HColorSimple fontColor = HColorSimple.create(java.awt.Color.BLACK);
        HColorSimple forcedColor = HColorSimple.create(java.awt.Color.RED);

        parser.drawU(ug, 1.0, fontColor, forcedColor);

        // Custom verification for text rendering
        verify(ug, atLeastOnce()).draw(any());
        // Could add: verify(ug, atLeastOnce()).apply(any(HColor.class));
    }

    @Test
    @PumlFile("svgRedRectLinearGradient.puml")
    public void testSvgWithLinearGradient(TestInfo testInfo) throws Exception {
        final String svg = extractSvgFromAnnotation(testInfo);
        final SvgDomParser parser = new SvgDomParser(svg);
        final UGraphic ug = mock(UGraphic.class, org.mockito.Mockito.RETURNS_SELF);

        HColorSimple fontColor = HColorSimple.create(java.awt.Color.BLACK);
        HColorSimple forcedColor = HColorSimple.create(java.awt.Color.RED);

        parser.drawU(ug, 1.0, fontColor, forcedColor);

        // Verify that gradient was applied - the rectangle should be drawn with gradient fill
        verify(ug, atLeastOnce()).draw(any());
        // The gradient will be applied through the UGraphic state
        verify(ug, atLeastOnce()).apply(any(HColor.class));
    }

    @Test
    @PumlFile("svgRedPolyLine.puml")
    public void testSvgWithPolyLine(TestInfo testInfo) throws Exception {
        final String svg = extractSvgFromAnnotation(testInfo);
        final SvgDomParser parser = new SvgDomParser(svg);
        final UGraphic ug = mock(UGraphic.class, org.mockito.Mockito.RETURNS_SELF);

        HColorSimple fontColor = HColorSimple.create(java.awt.Color.BLACK);
        HColorSimple forcedColor = HColorSimple.create(java.awt.Color.RED);

        parser.drawU(ug, 1.0, fontColor, forcedColor);

        // Verify that polyline was drawn
        verify(ug, atLeastOnce()).draw(any());
    }

    @Test
    @PumlFile("svgLinearGradientCircle.puml")
    public void testSvgWithLinearGradientCircle(TestInfo testInfo) throws Exception {
        final String svg = extractSvgFromAnnotation(testInfo);
        final SvgDomParser parser = new SvgDomParser(svg);
        final UGraphic ug = mock(UGraphic.class, org.mockito.Mockito.RETURNS_SELF);

        HColorSimple fontColor = HColorSimple.create(java.awt.Color.BLACK);
        HColorSimple forcedColor = HColorSimple.create(java.awt.Color.RED);

        parser.drawU(ug, 1.0, fontColor, forcedColor);

        // Verify that circle with gradient was drawn
        // Linear gradient should work on circles just like rectangles
        verify(ug, atLeastOnce()).draw(any());
        verify(ug, atLeastOnce()).apply(any(HColor.class));
    }

    /*
    public void extractFirstSpriteSvg_and_invoke_drawU() throws Exception {
        // Original test kept for backwards compatibility
        final String svg = extractSvgFromPumlFile("svgRedStroke.puml");
        final SvgDomParser parser = new SvgDomParser(svg);

        final UGraphic ug = mock(UGraphic.class, org.mockito.Mockito.RETURNS_SELF);

        HColorSimple fontColor = HColorSimple.create(java.awt.Color.BLACK);
        HColorSimple forcedColor = HColorSimple.create(java.awt.Color.RED);

        parser.drawU(ug, 1.0, fontColor, forcedColor);

        verify(ug, atLeastOnce()).draw(any());
    }
    */
    
}
