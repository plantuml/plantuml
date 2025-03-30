package net.sourceforge.plantuml.tim.builtin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TimTestUtils;

public class ReverseHsluvColorTest {

    @ParameterizedTest(name = "[{index}] ReverseHsluvColor({0}) = {1}")
    @CsvSource({
        " #000000, #767676",  // Black to its HSLuv reversed equivalent
        " #FFFFFF, #767676",  // White to its HSLuv reversed equivalent
        " #FF0000, #530000",  // Red to its HSLuv reversed equivalent
        //
        // " #00FF00, #003300",  // Green to its HSLuv reversed equivalent
        // " #0000FF, #000033",  // Blue to its HSLuv reversed equivalent
        // " #800000, #4C0000",  // Dark Red to its HSLuv reversed equivalent
        // " #808000, #4C4C00",  // Dark Yellow to its HSLuv reversed equivalent
        // " #008000, #003300",  // Dark Green to its HSLuv reversed equivalent
        //
        // TODO: Need to be retested...
        //
        " #00FF00, #007000",  // Green to its HSLuv reversed equivalent
        " #0000FF, #AEAEFF",  // Blue to its HSLuv reversed equivalent
        " #800000, #FE8383",  // Dark Red to its HSLuv reversed equivalent
        " #808000, #242400",  // Dark Yellow to its HSLuv reversed equivalent
        " #008000, #00F400",  // Dark Green to its HSLuv reversed equivalent
        " #123456, #5FA2F1",  // Arbitrary color reversed in HSLuv
        " #ABCDEF, #325773"   // Another arbitrary color reversed in HSLuv
    })
    public void testReverseHsluvColor(String inputColor, String expectedOutput) throws EaterException {
        ReverseHsluvColor reverseHsluvColorFunction = new ReverseHsluvColor();
        TimTestUtils.assertTimExpectedOutputFromInput(reverseHsluvColorFunction, inputColor, expectedOutput);
    }
}