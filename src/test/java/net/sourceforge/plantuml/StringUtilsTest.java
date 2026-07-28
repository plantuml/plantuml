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
}
