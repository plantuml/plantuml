package net.sourceforge.plantuml;

import static net.sourceforge.plantuml.StringUtils.substringAfterLast;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StringUtilsTest {

	@ParameterizedTest
	@CsvSource({
			" ''      , ''    ",
			" a       , 'a'   ",
			" Xa      , 'a'   ",
			" aX      , ''    ",
			" XaX     , ''    ",
			" XaXb    , 'b'   ",
			" foo     , 'foo' ",
			" fooXbar , 'bar' ",
	})
	void test_substringAfterLast(String input, String expectedOutput) {
		assertThat(substringAfterLast(input, 'X'))
				.isEqualTo(expectedOutput);
	}
}
