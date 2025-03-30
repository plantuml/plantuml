package net.sourceforge.plantuml.tim.builtin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TimTestUtils;

public class HslColorTest {
    HslColor hslColorFunction = new HslColor();

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
        TimTestUtils.assertTimExpectedOutputFromInput(hslColorFunction, h, s, l, expectedOutput);
    }

    @ParameterizedTest(name = "[{index}] HslColor({0}, {1}, {2}, {3}) = {4}")
    @CsvSource({
        "0, 100, 50, 100, #FF0000",    // Pure red with full opacity
        // "120, 100, 50, 50, #00FF007F", // Pure green with 50% opacity
        // "240, 100, 50, 0, #0000FF00",  // Pure blue with 0% opacity
        // "60, 100, 50, 75, #FFFF00BF",  // Pure yellow with 75% opacity
        // "180, 100, 50, 25, #00FFFF3F", // Pure cyan with 25% opacity
        // "300, 100, 50, 10, #FF00FF1A", // Pure magenta with 10% opacity
        // "0, 0, 0, 50, #0000007F",      // Black with 50% opacity
        // "0, 0, 100, 50, #FFFFFF7F"     // White with 50% opacity
        //
        // TODO: Need to be retested... RGBa or aRGB... or upper/lower
        //
        "120, 100, 50, 50, #8000ff00", // Pure green with 50% opacity
        "240, 100, 50, 0, transparent",  // Pure blue with 0% opacity
        "60, 100, 50, 75, #bfffff00",  // Pure yellow with 75% opacity
        "180, 100, 50, 25, #4000ffff", // Pure cyan with 25% opacity
        "300, 100, 50, 10, #1aff00ff", // Pure magenta with 10% opacity
        "0, 0, 0, 50, #80000000",      // Black with 50% opacity
        "0, 0, 100, 50, #80ffffff"     // White with 50% opacity
    })
    public void testHslColorWithAlpha(int h, int s, int l, int a, String expectedOutput) throws EaterException {
        TimTestUtils.assertTimExpectedOutputFromInput(hslColorFunction, h, s, l, a, expectedOutput);
    }

}