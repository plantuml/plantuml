package net.sourceforge.plantuml.tim.builtin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class DarkenFunctionTest {

    @ParameterizedTest
    @CsvSource({
        "#FF0000, 10, #E60000", // Red color, darkened by 10%
        "#00FF00, 50, #008000", // Green color, darkened by 50%
        "#0000FF, 0,  #0000FF", // Blue color, no change
        "#123456, 100, #000000" // Arbitrary color, darkened by 100%
    })
    public void testDarken(String inputColor, int percentage, String expectedOutput) {
        DarkenFunction darkenFunction = new DarkenFunction();
        String actualOutput = darkenFunction.darken(inputColor, percentage);
        assertEquals(expectedOutput, actualOutput);
    }
}
