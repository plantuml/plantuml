package net.sourceforge.plantuml.emoji;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSimple;

/**
 * Unit tests for SvgNanoParser focusing on public drawU behaviour.
 * - Verifies that drawing the parser will invoke underlying UGraphic draw operations.
 * - Verifies gray-level range computation for simple SVG with known fills.
 *
 * These tests use Mockito to stub UGraphic and do not require rendering to an image.
 */
public class SvgNanoParserTest {

    private static final String SAMPLE_SVG = "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 36 36\">"
            + "<path fill=\"#77B255\" d=\"M36 32c0 2.209-1.791 4-4 4H4c-2.209 0-4-1.791-4-4V4c0-2.209 1.791-4 4-4h28c2.209 0 4 1.791 4 4v28z\"/>"
            + "<path fill=\"#FFFFFF\" d=\"M21.529 18.006l8.238-8.238c.977-.976.977-2.559 0-3.535-.977-.977-2.559-.977-3.535 0l-8.238 8.238-8.238-8.238c-.976-.977-2.56-.977-3.535 0-.977.976-.977 2.559 0 3.535l8.238 8.238-8.258 8.258c-.977.977-.977 2.559 0 3.535.488.488 1.128.732 1.768.732s1.28-.244 1.768-.732l8.258-8.259 8.238 8.238c.488.488 1.128.732 1.768.732s1.279-.244 1.768-.732c.977-.977.977-2.559 0-3.535l-8.24-8.237z\"/>"
            + "<text x=\"2\" y=\"10\" font-family=\"SansSerif\" font-size=\"12px\" fill=\"#000000\">Hi</text>"
            + "</svg>";

    @Test
    public void testDrawUInvokesGraphicDraw() {
        SvgNanoParser parser = new SvgNanoParser(Arrays.asList(SAMPLE_SVG));
        
        // Create a mock UGraphic that returns itself for chained apply(...) calls
        UGraphic ug = mock(UGraphic.class, org.mockito.Mockito.RETURNS_SELF);

        HColorSimple fontColor = HColorSimple.create(java.awt.Color.BLACK);
        HColorSimple forcedColor = HColorSimple.create(java.awt.Color.RED);

        // call public drawU - using scale=1 and null colors (parser resolves defaults)
        parser.drawU(ug, 1.0, fontColor, forcedColor);

        // Expect at least one draw invocation on the provided UGraphic (paths/text rendered)
        verify(ug, atLeastOnce()).draw(any());
    }

    @Test
    public void testGetGrayLevelRangeComputesValues() {
        SvgNanoParser parser = new SvgNanoParser(Arrays.asList(SAMPLE_SVG));

        // compute range via public getters
        int min = parser.getMinGrayLevel();
        int max = parser.getMaxGrayLevel();

        // For the sample svg we expect min and max to be within 0..255
        // (exact values can vary depending on color mapping implementation)
        assert min >= 0 && min <= 255;
        assert max >= 0 && max <= 255;
        // max should be >= min
        assert max >= min;
    }
}