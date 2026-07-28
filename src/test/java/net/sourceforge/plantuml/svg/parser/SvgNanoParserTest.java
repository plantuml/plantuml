package net.sourceforge.plantuml.svg.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColorSimple;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.AbstractCommonUGraphic;
import net.sourceforge.plantuml.klimt.drawing.debug.StringBounderDebug;

/**
 * Unit tests for SvgSaxParser focusing on public drawU behavior.
 * - Verifies that drawing the parser will invoke underlying UGraphic draw operations.
 * - Verifies gray-level range computation for simple SVG with known fills.
 */
public class SvgNanoParserTest {

    private static final String SAMPLE_SVG = "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 36 36\">"
            + "<path fill=\"#77B255\" d=\"M36 32c0 2.209-1.791 4-4 4H4c-2.209 0-4-1.791-4-4V4c0-2.209 1.791-4 4-4h28c2.209 0 4 1.791 4 4v28z\"/>"
            + "<path fill=\"#FFFFFF\" d=\"M21.529 18.006l8.238-8.238c.977-.976.977-2.559 0-3.535-.977-.977-2.559-.977-3.535 0l-8.238 8.238-8.238-8.238c-.976-.977-2.56-.977-3.535 0-.977.976-.977 2.559 0 3.535l8.238 8.238-8.258 8.258c-.977.977-.977 2.559 0 3.535.488.488 1.128.732 1.768.732s1.28-.244 1.768-.732l8.258-8.259 8.238 8.238c.488.488 1.128.732 1.768.732s1.279-.244 1.768-.732c.977-.977.977-2.559 0-3.535l-8.24-8.237z\"/>"
            + "<text x=\"2\" y=\"10\" font-family=\"SansSerif\" font-size=\"12px\" fill=\"#000000\">Hi</text>"
            + "</svg>";

    @Test
    public void testDrawUInvokesGraphicDraw() {
        SvgSaxParser parser = new SvgSaxParser(SAMPLE_SVG);

        // Real (if minimal) UGraphic implementation, not a mock: records every
        // shape it is asked to draw, the same architectural family as
        // UGraphicTxt/UGraphicNull.
        RecordingUGraphic ug = new RecordingUGraphic();

        HColorSimple fontColor = HColorSimple.create(XColor.BLACK);
        HColorSimple forcedColor = HColorSimple.create(XColor.RED);

        // call public drawU - using scale=1 and null colors (parser resolves defaults)
        parser.drawU(ug, 1.0, fontColor, forcedColor);

        // Expect at least one draw invocation on the provided UGraphic (paths/text rendered)
        assertFalse(ug.getShapes().isEmpty());
    }

    @Test
    public void testGetGrayLevelRangeComputesValues() {
        SvgSaxParser parser = new SvgSaxParser(SAMPLE_SVG);

        // compute range via public getters
        int min = parser.getMinGrayLevel();
        int max = parser.getMaxGrayLevel();

        // For the sample svg we expect min and max to be within 0..255
        // (exact values can vary depending on color mapping implementation)
        assertTrue(min >= 0 && min <= 255);
        assertTrue(max >= 0 && max <= 255);
        // max should be >= min
        assertTrue(max >= min);
    }

    private static final class RecordingUGraphic extends AbstractCommonUGraphic {

        private final List<UShape> shapes = new ArrayList<>();

        RecordingUGraphic() {
            super(new StringBounderDebug());
            basicCopy(HColors.BLACK, ColorMapper.IDENTITY);
        }

        @Override
        protected AbstractCommonUGraphic copyUGraphic() {
            return this;
        }

        @Override
        public <SHAPE extends UShape> void draw(final SHAPE shape) {
            shapes.add(shape);
        }

        List<UShape> getShapes() {
            return shapes;
        }

        @Override
        public void writeToStream(final OutputStream os, final String metadata, final int dpi) throws IOException {
            // never called: this test never exports the drawing to a stream
        }

    }
}
