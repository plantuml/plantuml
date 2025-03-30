package net.sourceforge.plantuml.tim.builtin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TimTestUtils;

public class IsLightTest {

    @ParameterizedTest(name = "[{index}] IsLight({0}) = {1}")
    @CsvSource({
        " #000000, false",  // Black color, is not light
        " #FFFFFF, true",   // White color, is light
        " #123456, false",  // Arbitrary dark color
        " #ABCDEF, true"    // Arbitrary light color
    })
    public void testIsLight(String inputColor, boolean expectedOutput) throws EaterException {
        IsLight isLightFunction = new IsLight();
        TimTestUtils.assertTimExpectedOutputFromInput(isLightFunction, inputColor, expectedOutput ? "1" : "0");
    }
}