package net.sourceforge.plantuml.gantt.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.UnicodeBracketedExpression;

class ComplementDurationTest {

	private static UnicodeBracketedExpression ubrex() {
		return new ComplementDuration().toUnicodeBracketedExpressionComplement();
	}

	// ========== Single element ==========

	@ParameterizedTest
	@CsvSource({ //
			"2 days,     2, day", //
			"1 day,      1, day", //
			"3 weeks,    3, week", //
			"5 hours,    5, hour", //
			"30 minutes, 30, minute", //
			"15 seconds, 15, second", //
			"6 months,   6, month", //
	})
	void testSingleElement(String input, String expectedNum, String expectedUnit) {
		final UMatcher matcher = ubrex().match(input, 0);
		assertTrue(matcher.exactMatch(), "Should fully match: " + input);
		assertEquals(expectedNum.trim(), matcher.findValuesByKey("CNUM0").get(0));
		assertEquals(expectedUnit.trim(), matcher.findValuesByKey("CUNIT0").get(0));
	}

	// The trailing "s" is optional and is NOT part of the captured unit.

	@ParameterizedTest
	@CsvSource({ //
			"1 day,    day", //
			"2 days,   day", //
			"1 week,   week", //
			"1 hour,   hour", //
			"1 minute, minute", //
			"1 second, second", //
			"1 month,  month", //
	})
	void testOptionalTrailingS(String input, String expectedUnit) {
		final UMatcher matcher = ubrex().match(input, 0);
		assertTrue(matcher.exactMatch(), "Should fully match: " + input);
		assertEquals(expectedUnit.trim(), matcher.findValuesByKey("CUNIT0").get(0));
	}

	// ========== Two elements joined by "and" ==========

	@Test
	void testTwoDaysAndThreeWeeks() {
		final UMatcher matcher = ubrex().match("2 days and 3 weeks", 0);

		assertTrue(matcher.exactMatch(), "Should fully match");
		assertEquals("2 days and 3 weeks", matcher.getAcceptedMatch());

		// First element -> CNUM0 / CUNIT0
		assertEquals("2", matcher.findValuesByKey("CNUM0").get(0));
		assertEquals("day", matcher.findValuesByKey("CUNIT0").get(0));

		// Repeated element -> CNUM1 / CUNIT1
		assertEquals("3", matcher.findValuesByKey("CNUM1").get(0));
		assertEquals("week", matcher.findValuesByKey("CUNIT1").get(0));
	}

	@Test
	void testHoursAndMinutes() {
		final UMatcher matcher = ubrex().match("2 hours and 30 minutes", 0);

		assertTrue(matcher.exactMatch(), "Should fully match");
		assertEquals("2", matcher.findValuesByKey("CNUM0").get(0));
		assertEquals("hour", matcher.findValuesByKey("CUNIT0").get(0));
		assertEquals("30", matcher.findValuesByKey("CNUM1").get(0));
		assertEquals("minute", matcher.findValuesByKey("CUNIT1").get(0));
	}

	// ========== Elements separated by a space only (no "and", no comma) ==========

	@Test
	void testSpaceOnlySeparator() {
		final UMatcher matcher = ubrex().match("1 hour 2 hours", 0);

		assertTrue(matcher.exactMatch(), "Should fully match");
		assertEquals("1 hour 2 hours", matcher.getAcceptedMatch());
		assertEquals("1", matcher.findValuesByKey("CNUM0").get(0));
		assertEquals("hour", matcher.findValuesByKey("CUNIT0").get(0));
		assertEquals("2", matcher.findValuesByKey("CNUM1").get(0));
		assertEquals("hour", matcher.findValuesByKey("CUNIT1").get(0));
	}

	// ========== Three elements: CNUM1/CUNIT1 must accumulate two occurrences ==========

	@Test
	void testThreeElements() {
		final UMatcher matcher = ubrex().match("1 hour and 30 minutes and 15 seconds", 0);

		assertTrue(matcher.exactMatch(), "Should fully match");

		assertEquals("1", matcher.findValuesByKey("CNUM0").get(0));
		assertEquals("hour", matcher.findValuesByKey("CUNIT0").get(0));

		// The two repeated elements land under the same names, at indices 0 and 1.
		assertEquals(2, matcher.findValuesByKey("CNUM1").size());
		assertEquals("30", matcher.findValuesByKey("CNUM1").get(0));
		assertEquals("minute", matcher.findValuesByKey("CUNIT1").get(0));
		assertEquals("15", matcher.findValuesByKey("CNUM1").get(1));
		assertEquals("second", matcher.findValuesByKey("CUNIT1").get(1));
	}

	// ========== "m" disambiguation: month vs minute ==========

	@Test
	void testMonthVsMinuteAreDistinctUnits() {
		final UMatcher matcher = ubrex().match("3 months and 4 minutes", 0);

		assertTrue(matcher.exactMatch(), "Should fully match");
		assertEquals("month", matcher.findValuesByKey("CUNIT0").get(0));
		assertEquals("minute", matcher.findValuesByKey("CUNIT1").get(0));
	}

	// ========== Non-matching inputs ==========

	@ParameterizedTest
	@ValueSource(strings = { //
			"hello", // not a duration at all
			"2 days and", // trailing connector, no second element
			"2 fortnights", // unknown unit
	})
	void testDoesNotFullyMatch(String input) {
		final UMatcher matcher = ubrex().match(input, 0);
		assertFalse(matcher.exactMatch(), "Should not fully match: " + input);
	}
}
