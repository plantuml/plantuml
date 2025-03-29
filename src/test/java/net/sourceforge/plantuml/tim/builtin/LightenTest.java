package net.sourceforge.plantuml.tim.builtin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TimTestUtils;

public class LightenTest {

    @ParameterizedTest(name = "[{index}] Lighten({0}, {1}) = {2}")
    @CsvSource({
        " #000000, 10, #1A1A1A",  // Black color, lightened by 10%
        " #FFFFFF, 50, #FFFFFF",  // White color, no change
        " #123456, 20, #4D6A82",  // Arbitrary dark color, lightened by 20%
        " #ABCDEF, 30, #DDEFFF"   // Arbitrary light color, lightened by 30%
    })
    public void testLighten(String inputColor, int percentage, String expectedOutput) throws EaterException {
        Lighten lightenFunction = new Lighten();
        TimTestUtils.assertTimExpectedOutputFromInput(lightenFunction, inputColor, percentage, expectedOutput);
    }
}