package net.sourceforge.plantuml.tim.builtin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TimTestUtils;

public class DarkenTest {

    @ParameterizedTest(name = "[{index}] Darken({0}, {1}) = {2}")
    @CsvSource({
        " #FF0000,  10, #E60000", // Red color, darkened by 10%
        " #00FF00,  50, #008000", // Green color, darkened by 50%
        " #0000FF,   0, #0000FF", // Blue color, no change
        " #123456, 100, #000000"  // Arbitrary color, darkened by 100%
    })
    public void testDarken(String inputColor, int percentage, String expectedOutput) throws EaterException {
        Darken darkenFunction = new Darken();
        TimTestUtils.assertTimExpectedOutputFromInput(darkenFunction, inputColor, percentage, expectedOutput);
    }
}
