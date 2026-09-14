package net.sourceforge.plantuml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


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
}
