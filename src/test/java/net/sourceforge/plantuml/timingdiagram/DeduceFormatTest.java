package net.sourceforge.plantuml.timingdiagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.DecimalFormat;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(Lifecycle.PER_CLASS)
class DeduceFormatTest {

	@ParameterizedTest(name = "{index} ⇒ \"{0}\"")
	@MethodSource("provideCases")
	void testDeduceFormat(String input, String expected, boolean expectNull) {
		DeduceFormat df = DeduceFormat.from(input);
		if (expectNull) {
			assertNull(df);
		} else {
			assertNotNull(df);
			assertEquals(expected, df.toString());
		}
	}

	private Stream<Arguments> provideCases() {
		return Stream.of(
				Arguments.of("123456",
						"DeduceFormat{forceSign=false, groupingSep=none, decimalSep=none, fractionDigits=0}", false),
				Arguments.of("1234,50",
						"DeduceFormat{forceSign=false, groupingSep=none, decimalSep=',', fractionDigits=2}", false),
				Arguments.of("+1234.50",
						"DeduceFormat{forceSign=true, groupingSep=none, decimalSep='.', fractionDigits=2}", false),
				Arguments.of("1,234.50",
						"DeduceFormat{forceSign=false, groupingSep=',', decimalSep='.', fractionDigits=2}", false),
				Arguments.of("1.234,50",
						"DeduceFormat{forceSign=false, groupingSep='.', decimalSep=',', fractionDigits=2}", false),
				Arguments.of("1 234,560",
						"DeduceFormat{forceSign=false, groupingSep=' ', decimalSep=',', fractionDigits=3}", false),
				Arguments.of("-2'000.00",
						"DeduceFormat{forceSign=false, groupingSep=''', decimalSep='.', fractionDigits=2}", false),
				Arguments.of("+42,00",
						"DeduceFormat{forceSign=true, groupingSep=none, decimalSep=',', fractionDigits=2}", false),
				Arguments.of("12\u00A0345,67",
						"DeduceFormat{forceSign=false, groupingSep='\u00A0', decimalSep=',', fractionDigits=2}", false),
				Arguments.of("1_234'567,89", null, true),
				Arguments.of("+42,000",
						"DeduceFormat{forceSign=true, groupingSep=none, decimalSep=',', fractionDigits=3}", false),
				Arguments.of("-2'000.1234",
						"DeduceFormat{forceSign=false, groupingSep=''', decimalSep='.', fractionDigits=4}", false),
				Arguments.of("12\u00A0345,6",
						"DeduceFormat{forceSign=false, groupingSep='\u00A0', decimalSep=',', fractionDigits=1}", false),
				Arguments.of("1234,",
						"DeduceFormat{forceSign=false, groupingSep=none, decimalSep=',', fractionDigits=0}", false));
	}

	@ParameterizedTest(name = "{index} ⇒ from(\"{0}\").format({1}) = \"{2}\"")
	@MethodSource("cases")
	void testFormatting(String sample, double value, String expected) {
		final DecimalFormat df = DeduceFormat.from(sample).getDecimalFormat();
		assertEquals(expected, df.format(value));
	}

	private static Stream<Arguments> cases() {
		return Stream.of(Arguments.of("1234", 5678, "5678"), //
				Arguments.of("+123 456.91", 5678, "+5 678.00"), //
				Arguments.of("-123456,91", 5678, "5678,00"));
	}

	@Test
	public void testMerge() {
		final DeduceFormat format1 = DeduceFormat.from("123.45");
		final DeduceFormat format2 = DeduceFormat.from("123.4500");

		final DeduceFormat merged = format1.mergeWith(format2);
		assertEquals(
				"DeduceFormat{forceSign=false, groupingSep=none, decimalSep='.', minFractionDigits=2, maxFractionDigits=4}",
				merged.toString());

		final DecimalFormat df = merged.getDecimalFormat();
		assertEquals("1.00", df.format(1));
		assertEquals("1.20", df.format(1.2));
		assertEquals("1.23", df.format(1.23));
		assertEquals("1.2346", df.format(1.23456));
	}
}
