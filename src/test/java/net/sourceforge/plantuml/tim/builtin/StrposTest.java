package net.sourceforge.plantuml.tim.builtin;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TimTestUtils;

public class StrposTest {

    @ParameterizedTest(name = "[{index}] Strpos({0}, {1}) = {2}")
    @CsvSource({
        "'hello world', 'hello', '0'",      // Substring at the beginning
        "'hello world', 'world', '6'",      // Substring in the middle
        "'hello world', '!', '-1'",         // Substring not found
        "'', 'world', '-1'",                // Empty string, substring not found
        "'hello world', '', '0'",           // Empty substring found at index 0
        "'aaa', 'a', '0'",                  // Repeated characters, first occurrence
        "'aaa', 'aa', '0'",                 // Repeated characters, first occurrence of the substring
        "'abcabcabc', 'abc', '0'",          // Repeated pattern, first occurrence
        "'abcabcabc', 'bca', '1'",          // Repeated pattern, first occurrence of the substring
        "'abcabcabc', 'cab', '2'"           // Repeated pattern, first occurrence of the substring
    })
    public void testStrpos(String full, String searched, String expectedOutput) throws EaterException {
        Strpos strposFunction = new Strpos();
        TimTestUtils.assertTimExpectedOutputFromInput(strposFunction, full, searched, expectedOutput);
    }
}