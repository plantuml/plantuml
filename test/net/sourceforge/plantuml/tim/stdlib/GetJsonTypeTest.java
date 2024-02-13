package net.sourceforge.plantuml.tim.stdlib;

import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutput;
import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutputFromInput;
import static test.utils.JunitUtils.StringJsonConverter;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.TFunction;

/**
 * Tests the builtin function.
 */
@IndicativeSentencesGeneration(separator = ": ", generator = ReplaceUnderscores.class)

class GetJsonTypeTest {
	TFunction cut = new GetJsonType();
	final String cutName = "GetJsonType";

	@Disabled
	@Test
	void Test_without_Param() throws EaterException, EaterExceptionLocated {
		assertTimExpectedOutput(cut, "0");
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "(''{0}'') = {1}")
	@CsvSource(nullValues = "null", value = {
			" 0, string",
			" a, string",
			" -1, string",
	})
	void Test_with_String(String input, String expected) throws EaterException, EaterExceptionLocated {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "({0}) = {1}")
	@CsvSource(nullValues = "null", value = {
			" 0, number",
			" -1, number",
	})
	void Test_with_Integer(Integer input, String expected) throws EaterException, EaterExceptionLocated {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "({0}) = {1}")
	@CsvSource(value = {
			" 0                 , number",
			" 123               , number",
			" \"abc\"           , string",
			" \"string\"        , string",
			" '[1, 2]'          , array",
			" '[\"a\", \"b\"]'  , array",
			" '{\"a\":[1, 2]}'  , object",
			" '{\"a\":\"abc\"}' , object",
			" true              , boolean ",
			" false             , boolean ",
			" 1                 , number ",
			" null              , json ",
			" '{\"a\":[1, 2], \"b\":\"abc\", \"b\":true}' , object",
	})
	void Test_with_Json(@ConvertWith(StringJsonConverter.class) JsonValue input, String expected) throws EaterException, EaterExceptionLocated {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}
}
