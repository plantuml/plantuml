package net.sourceforge.plantuml.cucadiagram;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BodierAbstractTest {

    /**
     * Factorized test for BodierAbstract.matchScore().
     * Each test case provides: expected result, input string, candidate string.
     */
    @ParameterizedTest(name = "[{index}] matchScore(\"{1}\", \"{2}\") => {0}")
    @MethodSource("matchScoreCases")
    @DisplayName("matchScore – factorized cases")
    void testMatchScore(long expected, String full, String candidate) {
        assertEquals(expected, BodierAbstract.matchScore(full, candidate));
    }

    static Stream<Arguments> matchScoreCases() {
        return Stream.of(

            Arguments.of(0L, "reported", "reported"),                  // Exact match
            Arguments.of(1L, ".reported", "reported"),                 // Leading punctuation
            Arguments.of(2L, "1.reported", "reported"),                // Leading digit
            Arguments.of(1_000_000_000L, "Xreported", "reported"),     // Leading letter → large penalty
            Arguments.of(1_000L, "reported.", "reported"),             // Trailing punctuation
            Arguments.of(3_002L, "..reported$$$", "reported"),         // Multiple leading punctuation + trailing punctuation
            Arguments.of(1_002_004L, "....reportedX$$", "reported"),   // Leading punctuation + trailing letter + punctuation
            Arguments.of(1_003_004L, "....reportedX$ab", "reported"),  // Trailing letters after punctuation
            Arguments.of(9_000_000_004L, "{static} int counter", "counter"), // Keyword before candidate

            Arguments.of(4_000_001_005L, "attr1 = \"attr2\"", "attr2"), // Candidate inside quotes
            Arguments.of(11_000L, "attr2 = \"value2\"", "attr2")        // Candidate followed by assignment
        );
    }

    // ---------------------------------------------------------------------
    // Tests for startsWith with start index
    // ---------------------------------------------------------------------

    /**
     * Factorized test for BodierAbstract.startsWith().
     * Each test case provides: input string, start index, candidate string, expected result.
     */
    @ParameterizedTest(name = "[{index}] \"{0}\" startsWith@{1} \"{2}\" => {3}")
    @MethodSource("startsWithCases")
    @DisplayName("startsWith – factorized cases")
    void testStartsWith_Generic(String full, int startIdx, String candidate, boolean expected) {
        assertEquals(expected, BodierAbstract.startsWith(full, startIdx, candidate));
    }

    /**
     * Provides all test cases for startsWith with different start indexes.
     */
    static Stream<Arguments> startsWithCases() {
        return Stream.of(
            // Simple matches at index 0
            Arguments.of("hello", 0, "he", true),
            Arguments.of("hello", 0, "hello", true),
            Arguments.of("", 0, "h", false),
            Arguments.of("hello", 0, "HEL", false),
            Arguments.of("hello", 0, "he", true),
            Arguments.of("hello", 0, "hx", false),
            Arguments.of("h", 0, "hello", false),

            // Matches starting later in the string
            Arguments.of("hello", 1, "el", true),
            Arguments.of("hello", 1, "ello", true),
            Arguments.of("hello", 2, "l", true),
            Arguments.of("hello", 2, "ll", true),
            Arguments.of("hello", 2, "llo", true),

            // Negative matches
            Arguments.of("hello", 1, "he", false),
            Arguments.of("hello", 2, "lx", false),
            Arguments.of("hello", 3, "loX", false),
            Arguments.of("hello", 5, "x", false),

            // Matches near the end of the string
            Arguments.of("hello", 4, "o", true),
            Arguments.of("hello", 4, "oo", false),

            // Out-of-range or invalid indexes
            Arguments.of("hello", -1, "h", false),
            Arguments.of("a", 1, "a", false),

            // Mixed case with offset
            Arguments.of("Xsmile", 1, "sm", true)
        );
    }
}
