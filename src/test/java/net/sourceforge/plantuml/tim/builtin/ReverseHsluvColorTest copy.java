package net.sourceforge.plantuml.tim.builtin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TimTestUtils;

public class HslColorTest {

    @ParameterizedTest(name = "[{index}] HslColor({0}, {1}, {2}) = {3}")
    @CsvSource({
        "0, 100, 50, #FF0000",    // Pure red
        "120, 100, 50, #00FF00",  // Pure green
        "240, 100, 50, #0000FF",  // Pure blue
        "60, 100, 50, #FFFF00",   // Pure yellow
        "180, 100, 50, #00FFFF",  // Pure cyan
        "300, 100, 50, #FF00FF",  // Pure magenta
        "0, 0, 0, #000000",       // Black
        "0, 0, 100, #FFFFFF",     // White
        "0, 0, 50, #808080",      // Gray
        "0, 0, 75, #BFBFBF"       // Light gray
    })
    public void testHslColor(int h, int s, int l, String expectedOutput) throws EaterException {
        HslColor hslColorFunction = new HslColor();
        TimTestUtils.assertTimExpectedOutputFromInput(hslColorFunction, h, s, l, expectedOutput);
    }
}