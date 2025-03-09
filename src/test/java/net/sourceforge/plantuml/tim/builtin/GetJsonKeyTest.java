package net.sourceforge.plantuml.tim.builtin;

import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutput;
import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutputFromInput;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TFunction;
import test.utils.JunitUtils.StringJsonConverter;

/**
 * Tests the builtin function.
 */
@IndicativeSentencesGeneration(separator = ": ", generator = ReplaceUnderscores.class)

class GetJsonKeyTest {
	TFunction cut = new GetJsonKey();
	final String cutName = "GetJsonKey";

	@Disabled
	@Test
	void Test_without_Param() throws EaterException {
		assertTimExpectedOutput(cut, "0");
	}

	@Disabled // EaterException: Not JSON data
	@ParameterizedTest(name = "[{index}] " + cutName + "(''{0}'') = {1}")
	@CsvSource(nullValues = "null", value = {
			" 0, Not JSON data",
			" a, Not JSON data",
			" -1, Not JSON data",
	})
	void Test_with_String(String input, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}

	@Disabled // EaterException: Not JSON data
	@ParameterizedTest(name = "[{index}] " + cutName + "({0}) = {1}")
	@CsvSource(nullValues = "null", value = {
			" 0,  Not JSON data",
			" -1, Not JSON data",
	})
	void Test_with_Integer(Integer input, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "({0}) = {1}")
	@CsvSource(value = {
			"  []  , []",
			" '{\"a\":[1, 2]}'  , [\"a\"]",
			" '[{\"a\":[1, 2]}]'  , [\"a\"]",
			" '{\"a\":\"abc\"}' , [\"a\"]",
			" '[{\"a\":[1, 2]}, {\"b\":[3, 4]}]'  , '[\"a\",\"b\"]'",
			" '{\"a\":[1, 2], \"b\":\"abc\", \"b\":true}' , '[\"a\",\"b\",\"b\"]'",
			// DONE: Manage Array with different type inside
			// Ref.:
			// - https://datatracker.ietf.org/doc/html/rfc8259#section-5
			// - https://json-schema.org/understanding-json-schema/reference/array.html
			" '[3, \"different\", { \"types\" : \"of values\" }]', [\"types\"]",
	})
	void Test_with_Json(@ConvertWith(StringJsonConverter.class) JsonValue input, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}
}
