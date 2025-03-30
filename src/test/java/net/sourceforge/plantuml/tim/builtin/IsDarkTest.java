package net.sourceforge.plantuml.tim.builtin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TimTestUtils;

public class IsDarkTest {

    @ParameterizedTest(name = "[{index}] IsDark({0}) = {1}")
    @CsvSource({
        " #000000, true",  // Black color, is dark
        " #FFFFFF, false", // White color, is not dark
        " #123456, true",  // Arbitrary dark color
        " #ABCDEF, false"  // Arbitrary light color
    })
    public void testIsDark(String inputColor, boolean expectedOutput) throws EaterException {
        IsDark isDarkFunction = new IsDark();
        TimTestUtils.assertTimExpectedOutputFromInput(isDarkFunction, inputColor, expectedOutput ? "1" : "0");
    }
}