package net.sourceforge.plantuml.tim.builtin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TimTestUtils;

public class ReverseColorTest {

    @ParameterizedTest(name = "[{index}] ReverseColor({0}) = {1}")
    @CsvSource({
        " #000000, #FFFFFF",  // Black to White
        " #FFFFFF, #000000",  // White to Black
        " #123456, #EDCBA9",  // Arbitrary color reversed
        " #ABCDEF, #543210",  // Another arbitrary color reversed
        " #FF0000, #00FFFF",  // Red to Cyan
        " #00FF00, #FF00FF",  // Green to Magenta
        " #0000FF, #FFFF00",  // Blue to Yellow
        " #800000, #7FFFFF",  // Dark Red to Light Cyan
        " #808000, #7F7FFF",  // Dark Yellow to Light Blue
        " #008000, #FF7FFF"   // Dark Green to Light Magenta
    })
    public void testReverseColor(String inputColor, String expectedOutput) throws EaterException {
        ReverseColor reverseColorFunction = new ReverseColor();
        TimTestUtils.assertTimExpectedOutputFromInput(reverseColorFunction, inputColor, expectedOutput);
    }
}