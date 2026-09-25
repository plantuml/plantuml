package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


import java.util.Locale;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StringUtilsTest {

	@ParameterizedTest
	@CsvSource(nullValues = "null", value = {
			" null   , true  ",
			" ''     , true  ",
			" ' '    , true  ",
			" '\0'   , true  ",
			" '\n'   , true  ",
			" '\r'   , true  ",
			" '\t'   , true  ",
			" 'x'    , false ",
			" ' x '  , false ",
	})
	void test_isEmpty_isNotEmpty(String s, boolean empty) {
		assertEquals(empty, StringUtils.isEmpty(s));

		assertNotEquals(empty, StringUtils.isNotEmpty(s));
	}

	@ParameterizedTest
	@CsvSource(value = {
			" 'abc', 'abc' ",
			" '', '' ",
			" ' ', ' ' ",
			" '0', '\uE100' ",
			" 'a1b2', 'a\uE101b\uE102' ",
			" '\uE1000', '\uE100\uE100' ",
			" '1234567890', '\uE101\uE102\uE103\uE104\uE105\uE106\uE107\uE108\uE109\uE100' ",
			" 'e\uE1023', 'e\uE102\uE103' "
	})
	void test_toInternalBoldNumber(String s, String result) {
		assertEquals(result, StringUtils.toInternalBoldNumber(s));
	}

	@ParameterizedTest
	@CsvSource(value = {
			" 0.0      , 2  , '0'      ",
			" -0.0     , 2  , '0'      ",
			" 1.0      , 2  , '1'      ",
			" 1.5      , 1  , '1.5'    ",
			" 1.5      , 2  , '1.5'    ",
			" 2.25     , 2  , '2.25'   ",
			" 2.20     , 2  , '2.2'    ",
			" -2.5     , 1  , '-2.5'   ",
			" 100.0    , 3  , '100'    ",
			" 0.001    , 2  , '0'      ",
			" 3.14159  , 0  , '3'      ",
			" 3.5      , 0  , '4'      ",
			" -3.5     , 0  , '-4'     ",
			" 1.005    , 2  , '1.01'   ",
			" 123.456  , 2  , '123.46' ",
			" 0.1      , 5  , '0.1'    ",
			" -100.0   , 0  , '-100'   ",
			" 9.9999   , 2  , '10'     ",
			" 0.5      , 0  , '1'      ",
			" -0.5     , 0  , '-1'     ",
			" 10.0     , 5  , '10'     ",
			" 2.0      , 20 , '2'      ",
			" -0.001   , 2  , '0'      ",
			" -0.0001  , 2  , '0'      ",
			" 0.000123 , 6  , '0.000123'       ",
			" -0.000123, 6  , '-0.000123'      ",
	})
	void test_formatDecimal(double x, int decimal, String expected) {
		assertEquals(expected, StringUtils.formatDecimal(x, decimal));
	}

	// formatDecimal's fast path scales x to an int and rounds it, instead of going
	// through String.format/BigDecimal; these values sit (or were built to sit) right
	// on a rounding boundary, or just outside/at the edge of the fast path's safe
	// range, so they exercise the exact-but-slower String.format fallback.
	@ParameterizedTest
	@CsvSource(value = {
			" 4447.53134275    , 7  , '4447.5313428'    ",
			" -4447.53134275   , 7  , '-4447.5313428'   ",
			" 2500000.123456   , 5  , '2500000.12346'   ",
			" -2500000.123456  , 5  , '-2500000.12346'  ",
			" 123.456789012345 , 15 , '123.456789012345'",
	})
	void test_formatDecimal_fallbackPath(double x, int decimal, String expected) {
		assertEquals(expected, StringUtils.formatDecimal(x, decimal));
	}

	// formatDecimal writes its digits right to left into a single char[]; these cases
	// pin every shape of that layout: no fractional part, fractional zeros dropped,
	// leading zeros in the fractional part, sign, and the widest possible outputs.
	@ParameterizedTest
	@CsvSource(value = {
			" 7        , 0  , '7'                  ",
			" 7        , 3  , '7'                  ",
			" 70       , 0  , '70'                 ",
			" 1200     , 2  , '1200'               ",
			" 0.05     , 2  , '0.05'               ",
			" -0.05    , 2  , '-0.05'              ",
			" 0.5      , 2  , '0.5'                ",
			" 0.005    , 3  , '0.005'              ",
			" 0.0000001, 7  , '0.0000001'          ",
			" 0.000001 , 7  , '0.000001'           ",
			" 10.05    , 2  , '10.05'              ",
			" 10.5     , 2  , '10.5'               ",
			" 10.0     , 2  , '10'                 ",
			" 100.01   , 2  , '100.01'             ",
			" 100.10   , 2  , '100.1'              ",
			" 1234.5678, 4  , '1234.5678'          ",
			" -1234.5678, 4 , '-1234.5678'         ",
			" 1234.5670, 4  , '1234.567'           ",
			" 0.999999999999999, 15, '0.999999999999999' ",
			" 0.000000000000001, 15, '0.000000000000001' ",
			" -0.000000000000001, 15, '-0.000000000000001' ",
			" 19999999.99 , 2 , '19999999.99'      ",
			" 1999999999 , 0  , '1999999999'       ",
			" -1999999999 , 0 , '-1999999999'      ",
			" 19999999.9 , 2  , '19999999.9'       ",
			" 2000000000 , 0  , '2000000000'       ",
			" 12345678901 , 0 , '12345678901'      ",
			" 12345678.5 , 1  , '12345678.5'       ",
	})
	void test_formatDecimal_layout(double x, int decimal, String expected) {
		assertEquals(expected, StringUtils.formatDecimal(x, decimal));
	}

	// A value that rounds to zero must give "0", never "-0" nor "0.00".
	@ParameterizedTest
	@CsvSource(value = { " 0.004 , 2 ", " -0.004 , 2 ", " 0.0000004 , 6 ", " -0.4 , 0 ", " 1e-12 , 4 " })
	void test_formatDecimal_roundsToZero(double x, int decimal) {
		assertEquals("0", StringUtils.formatDecimal(x, decimal));
	}

	// Every digit count of the integer part, for every number of decimals.
	@Test
	void test_formatDecimal_allDigitCounts() {
		for (int decimal = 0; decimal <= 6; decimal++) {
			long p = 1;
			for (int digits = 1; digits <= 9; digits++) {
				final double x = p; // 1, 10, 100, ...
				assertEquals(reference(x, decimal), StringUtils.formatDecimal(x, decimal));
				final double y = p * 9 + 0.123456;
				assertEquals(reference(y, decimal), StringUtils.formatDecimal(y, decimal));
				assertEquals(reference(-y, decimal), StringUtils.formatDecimal(-y, decimal));
				p *= 10;
			}
		}
	}

	// Compares against the slow, exact String.format-based reference over many values
	// (fixed seed, so a failure is reproducible), covering the fast path, the near-tie
	// fallback and the out-of-range fallback.
	@Test
	void test_formatDecimal_matchesReference() {
		final Random rnd = new Random(20260925L);
		for (int i = 0; i < 200_000; i++) {
			final double x = (rnd.nextDouble() - 0.5) * Math.pow(10, rnd.nextInt(12) - 4);
			final int decimal = rnd.nextInt(16);
			assertEquals(reference(x, decimal), StringUtils.formatDecimal(x, decimal),
					"x=" + x + " decimal=" + decimal);
		}
	}

	// Values with few significant digits are the typical SVG coordinates, and sit
	// exactly on rounding boundaries in binary floating point (1.005, 2.675, ...).
	@Test
	void test_formatDecimal_matchesReferenceOnShortDecimals() {
		for (int decimal = 0; decimal <= 8; decimal++)
			for (int n = -3000; n <= 3000; n++) {
				final double x = n / 1000.0;
				assertEquals(reference(x, decimal), StringUtils.formatDecimal(x, decimal),
						"x=" + x + " decimal=" + decimal);
			}
	}

	@Test
	void test_formatDecimal_specialValues() {
		assertEquals("NaN", StringUtils.formatDecimal(Double.NaN, 2));
		assertEquals(reference(Double.POSITIVE_INFINITY, 2), StringUtils.formatDecimal(Double.POSITIVE_INFINITY, 2));
		assertEquals(reference(Double.NEGATIVE_INFINITY, 2), StringUtils.formatDecimal(Double.NEGATIVE_INFINITY, 2));
		assertEquals(reference(1e15, 2), StringUtils.formatDecimal(1e15, 2));
		assertEquals(reference(1.5, 20), StringUtils.formatDecimal(1.5, 20));
	}

	private static String reference(double x, int decimal) {
		if (x == 0.0)
			return "0";
		return StringUtils.trimZeros(String.format(Locale.US, "%." + decimal + "f", x));
	}

	@ParameterizedTest
	@CsvSource(value = {
			" '1'        , '1'       ",
			" '1.50'     , '1.5'     ",
			" '1.00'     , '1'       ",
			" '-2.500'   , '-2.5'    ",
			" '0.00'     , '0'       ",
			" '100'      , '100'     ",
			" '-0'       , '0'       ",
			" '-0.00'    , '0'       ",
	})
	void test_trimZeros(String s, String expected) {
		assertEquals(expected, StringUtils.trimZeros(s));
	}

	@Test
	void trinRemovesTheNonBreakingSpace() {
		assertEquals("foo", StringUtils.trin("\u00A0 foo\u00A0"));
		assertEquals("foo\u00A0bar", StringUtils.trin("\u00A0foo\u00A0bar\t"));
		assertEquals("", StringUtils.trin("\u00A0\u00A0"));
		assertEquals("foo", StringUtils.trim2("\u00A0foo \u00A0").toString());
	}

}
