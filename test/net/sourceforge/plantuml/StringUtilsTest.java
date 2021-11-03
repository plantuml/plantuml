package net.sourceforge.plantuml;

import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat(StringUtils.isEmpty(s))
				.isEqualTo(empty);

		assertThat(StringUtils.isNotEmpty(s))
				.isNotEqualTo(empty);
	}
}
