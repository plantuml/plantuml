package net.sourceforge.plantuml.project.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.UnicodeBracketedExpression;

import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexResult;

class TimeResolutionTest {

	// ========== Format A: DD MONTH YYYY ==========

	@ParameterizedTest
	@CsvSource({
		"15 January 2024,   15, January,  2024",
		"1 Feb 2025,        1,  Feb,      2025",
		"25 december 2000,  25, december, 2000",
		"3 MAR 99,          3,  MAR,      99",
	})
	void testRegexA(String input, String expectedDay, String expectedMonth, String expectedYear) {
		final IRegex regex = TimeResolution.toRegexA_DD_MONTH_YYYY("YEAR", "MONTH", "DAY");
		final RegexResult result = regex.matcher(input);
		assertNotNull(result);
		assertEquals(expectedDay.trim(), result.get("DAY", 0));
		assertEquals(expectedMonth.trim(), result.get("MONTH", 0));
		assertEquals(expectedYear.trim(), result.get("YEAR", 0));
	}

	@ParameterizedTest
	@CsvSource({
		"15 January 2024,   15, January,  2024",
		"1 Feb 2025,        1,  Feb,      2025",
		"25 december 2000,  25, december, 2000",
		"3 MAR 99,          3,  MAR,      99",
	})
	void testUbrexA(String input, String expectedDay, String expectedMonth, String expectedYear) {
		final UnicodeBracketedExpression ubrex = TimeResolution.toUbrexA_DD_MONTH_YYYY("YEAR", "MONTH", "DAY");
		final UMatcher matcher = ubrex.match(input, 0);
		assertTrue(matcher.startMatch(), "Should match: " + input);
		assertEquals(expectedDay.trim(), matcher.getCapture("DAY").get(0));
		assertEquals(expectedMonth.trim(), matcher.getCapture("MONTH").get(0));
		assertEquals(expectedYear.trim(), matcher.getCapture("YEAR").get(0));
	}

	// ========== Format B: YYYY-MM-DD ==========

	@ParameterizedTest
	@CsvSource({
		"2024-01-15, 2024, 01, 15",
		"2024/12/25, 2024, 12, 25",
		"99.3.7,     99,   3,  7",
	})
	void testRegexB(String input, String expectedYear, String expectedMonth, String expectedDay) {
		final IRegex regex = TimeResolution.toRegexB_YYYY_MM_DD("YEAR", "MONTH", "DAY");
		final RegexResult result = regex.matcher(input);
		assertNotNull(result);
		assertEquals(expectedYear.trim(), result.get("YEAR", 0));
		assertEquals(expectedMonth.trim(), result.get("MONTH", 0));
		assertEquals(expectedDay.trim(), result.get("DAY", 0));
	}

	@ParameterizedTest
	@CsvSource({
		"2024-01-15, 2024, 01, 15",
		"2024/12/25, 2024, 12, 25",
		"99.3.7,     99,   3,  7",
	})
	void testUbrexB(String input, String expectedYear, String expectedMonth, String expectedDay) {
		final UnicodeBracketedExpression ubrex = TimeResolution.toUbrexB_YYYY_MM_DD("YEAR", "MONTH", "DAY");
		final UMatcher matcher = ubrex.match(input, 0);
		assertTrue(matcher.startMatch(), "Should match: " + input);
		assertEquals(expectedYear.trim(), matcher.getCapture("YEAR").get(0));
		assertEquals(expectedMonth.trim(), matcher.getCapture("MONTH").get(0));
		assertEquals(expectedDay.trim(), matcher.getCapture("DAY").get(0));
	}

	// ========== Format C: MONTH DD, YYYY ==========

	@ParameterizedTest
	@CsvSource({
		"January 15 2024,     January,  15, 2024",
		"Feb 1 2025,          Feb,      1,  2025",
		"december 25 2000,    december, 25, 2000",
		"MAR 3 99,            MAR,      3,  99",
	})
	void testRegexC(String input, String expectedMonth, String expectedDay, String expectedYear) {
		final IRegex regex = TimeResolution.toRegexC_MONTH_DD_YYYY("YEAR", "MONTH", "DAY");
		final RegexResult result = regex.matcher(input);
		assertNotNull(result);
		assertEquals(expectedMonth.trim(), result.get("MONTH", 0));
		assertEquals(expectedDay.trim(), result.get("DAY", 0));
		assertEquals(expectedYear.trim(), result.get("YEAR", 0));
	}

	@ParameterizedTest
	@CsvSource({
		"January 15 2024,     January,  15, 2024",
		"Feb 1 2025,          Feb,      1,  2025",
		"december 25 2000,    december, 25, 2000",
		"MAR 3 99,            MAR,      3,  99",
	})
	void testUbrexC(String input, String expectedMonth, String expectedDay, String expectedYear) {
		final UnicodeBracketedExpression ubrex = TimeResolution.toUbrexC_MONTH_DD_YYYY("YEAR", "MONTH", "DAY");
		final UMatcher matcher = ubrex.match(input, 0);
		assertTrue(matcher.startMatch(), "Should match: " + input);
		assertEquals(expectedMonth.trim(), matcher.getCapture("MONTH").get(0));
		assertEquals(expectedDay.trim(), matcher.getCapture("DAY").get(0));
		assertEquals(expectedYear.trim(), matcher.getCapture("YEAR").get(0));
	}

	// ========== Regex vs Ubrex consistency ==========

	@Test
	void testRegexAndUbrexAgreeOnFormatA() {
		final String input = "15 January 2024";
		final IRegex regex = TimeResolution.toRegexA_DD_MONTH_YYYY("YEAR", "MONTH", "DAY");
		final RegexResult regexResult = regex.matcher(input);

		final UnicodeBracketedExpression ubrex = TimeResolution.toUbrexA_DD_MONTH_YYYY("YEAR", "MONTH", "DAY");
		final UMatcher ubrexMatcher = ubrex.match(input, 0);

		assertTrue(ubrexMatcher.startMatch());
		assertEquals(regexResult.get("YEAR", 0), ubrexMatcher.getCapture("YEAR").get(0));
		assertEquals(regexResult.get("MONTH", 0), ubrexMatcher.getCapture("MONTH").get(0));
		assertEquals(regexResult.get("DAY", 0), ubrexMatcher.getCapture("DAY").get(0));
	}

	@Test
	void testRegexAndUbrexAgreeOnFormatB() {
		final String input = "2024-06-15";
		final IRegex regex = TimeResolution.toRegexB_YYYY_MM_DD("YEAR", "MONTH", "DAY");
		final RegexResult regexResult = regex.matcher(input);

		final UnicodeBracketedExpression ubrex = TimeResolution.toUbrexB_YYYY_MM_DD("YEAR", "MONTH", "DAY");
		final UMatcher ubrexMatcher = ubrex.match(input, 0);

		assertTrue(ubrexMatcher.startMatch());
		assertEquals(regexResult.get("YEAR", 0), ubrexMatcher.getCapture("YEAR").get(0));
		assertEquals(regexResult.get("MONTH", 0), ubrexMatcher.getCapture("MONTH").get(0));
		assertEquals(regexResult.get("DAY", 0), ubrexMatcher.getCapture("DAY").get(0));
	}

	@Test
	void testRegexAndUbrexAgreeOnFormatC() {
		final String input = "January 15 2024";
		final IRegex regex = TimeResolution.toRegexC_MONTH_DD_YYYY("YEAR", "MONTH", "DAY");
		final RegexResult regexResult = regex.matcher(input);

		final UnicodeBracketedExpression ubrex = TimeResolution.toUbrexC_MONTH_DD_YYYY("YEAR", "MONTH", "DAY");
		final UMatcher ubrexMatcher = ubrex.match(input, 0);

		assertTrue(ubrexMatcher.startMatch());
		assertEquals(regexResult.get("YEAR", 0), ubrexMatcher.getCapture("YEAR").get(0));
		assertEquals(regexResult.get("MONTH", 0), ubrexMatcher.getCapture("MONTH").get(0));
		assertEquals(regexResult.get("DAY", 0), ubrexMatcher.getCapture("DAY").get(0));
	}

	@Test
	void testRegexAndUbrexAgreeOnFormatA1() {
		final String input = "5 zzdecember 2018";
		final IRegex regex = TimeResolution.toRegexA_DD_MONTH_YYYY("YEAR", "MONTH", "DAY");
		final RegexResult regexResult = regex.matcher(input);
		System.err.println(regexResult);

		final UnicodeBracketedExpression ubrex = TimeResolution.toUbrexA_DD_MONTH_YYYY("YEAR", "MONTH", "DAY");
		final UMatcher ubrexMatcher = ubrex.match(input, 0);
		System.err.println(ubrexMatcher);

		assertTrue(ubrexMatcher.startMatch());
		assertEquals(regexResult.get("YEAR", 0), ubrexMatcher.getCapture("YEAR").get(0));
		assertEquals(regexResult.get("MONTH", 0), ubrexMatcher.getCapture("MONTH").get(0));
		assertEquals(regexResult.get("DAY", 0), ubrexMatcher.getCapture("DAY").get(0));
	}

}
