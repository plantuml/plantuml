package net.sourceforge.plantuml.tim.builtin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TimTestUtils;

public class SubstrTest {
    Substr substrFunction = new Substr();

    @ParameterizedTest(name = "[{index}] Substr({0}, {1}, {2}) = {3}")
    @CsvSource({
        "'hello world', 0, 5, 'hello'",      // Substring from start
        "'hello world', 6, 5, 'world'",      // Substring from middle
        "'hello world', 11, 5, ''",          // Substring from end (out of bounds)
        "'hello world', 0, 11, 'hello world'", // Full string
        "'hello world', 0, 20, 'hello world'", // Full string with large length
        "'hello world', 6, 20, 'world'",     // Substring with large length
        "'hello world', 6, 0, ''",           // Substring with zero length
        "'hello world', 6, 1, 'w'",          // Single character substring
        "'hello world', 4, 3, 'o w'",        // Substring in between
        "'', 0, 5, ''"                       // Empty string
    })
    public void testSubstr(String full, int pos, int len, String expectedOutput) throws EaterException {
        TimTestUtils.assertTimExpectedOutputFromInput(substrFunction, full, pos, len, expectedOutput);
    }

    @ParameterizedTest(name = "[{index}] Substr({0}, {1}) = {2}")
    @CsvSource({
        "'hello world', 0, 'hello world'",   // Full string
        "'hello world', 6, 'world'",         // Substring from middle
        "'hello world', 11, ''",             // Substring from end (out of bounds)
        "'hello world', 20, ''",             // Substring from out of bounds
        "'', 0, ''"                          // Empty string
    })
    public void testSubstrWithoutLength(String full, int pos, String expectedOutput) throws EaterException {
        TimTestUtils.assertTimExpectedOutputFromInput(substrFunction, full, pos, expectedOutput);
    }
}