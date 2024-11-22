package net.sourceforge.plantuml.tim.builtin;

import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutputFromInput;

import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.api.Nested;
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

class JsonMergeTest {
	TFunction cut = new JsonMerge();
	final String cutName = "json_merge";
	final String paramTestName = "[{index}] " + cutName + "({0}, {1}) = {2}";

	@ParameterizedTest(name = paramTestName)
	@CsvSource(value = {
			"  [],                [1],             [1]",
			"  [0],               [\"a\"],        '[0,\"a\"]' ",
			"  [0],               [{\"a\": 123}], '[0,{\"a\":123}]' ",
			"  [0],               [1],            '[0,1]' ",
			" '[{\"a\":[1, 2]}]', [1],            '[{\"a\":[1,2]},1]' ",

	})
	void Test_with_Array_Json(@ConvertWith(StringJsonConverter.class) JsonValue input1, @ConvertWith(StringJsonConverter.class) JsonValue input2, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input1, input2, expected);
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "({0}, {1}, {2}) = {3}")
	@CsvSource(value = {
			" {},         {\"a\":1},                    {\"a\":1}",
			" {},        '{\"a\":[1,2,3]}',            '{\"a\":[1,2,3]}'",
			" {},        '{\"a\":{\"b\":123}}',        '{\"a\":{\"b\":123}}'",
			" {},        '{\"a\":{\"b\":\"abc\"}}',    '{\"a\":{\"b\":\"abc\"}}'",
			" {\"z\":0}, '{\"a\":1}',                  '{\"z\":0,\"a\":1}'",
			" {\"z\":0}, '{\"a\":[1,2,3]}',            '{\"z\":0,\"a\":[1,2,3]}'",
			" {\"z\":0}, '{\"a\":{\"b\":123}}',        '{\"z\":0,\"a\":{\"b\":123}}'",
			" {\"z\":0}, '{\"a\":{\"b\":\"abc\"}}',    '{\"z\":0,\"a\":{\"b\":\"abc\"}}'",
			" '{\"a\": 1, \"b\": \"two\"}', {\"c\":3}, '{\"a\":1,\"b\":\"two\",\"c\":3}'",
			" '{\"a\": 1, \"b\": \"two\"}', '{\"d\":{\"da\": 1, \"db\": \"two\"}}', '{\"a\":1,\"b\":\"two\",\"d\":{\"da\":1,\"db\":\"two\"}}'",
			" {\"z\":0}, '{\"z\":1}',                  '{\"z\":1}'",
			" {\"z\":0}, '{\"z\":[1,2,3]}',            '{\"z\":[1,2,3]}'",
			" {\"z\":0}, '{\"z\":{\"b\":123}}',        '{\"z\":{\"b\":123}}'",
			" {\"z\":0}, '{\"z\":{\"b\":\"abc\"}}',    '{\"z\":{\"b\":\"abc\"}}'",
			" '{\"a\": 1, \"b\": \"two\"}', {\"b\":3}, '{\"a\":1,\"b\":3}'",

	})
	void Test_with_Object_Json(@ConvertWith(StringJsonConverter.class) JsonValue input1, @ConvertWith(StringJsonConverter.class) JsonValue input2, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input1, input2, expected);
	}

	@Nested
	class Not_Nominal_Test {
		@ParameterizedTest(name = paramTestName)
		@CsvSource(value = {
				"  [],                {\"a\":1},  []",
				"  [0],               {\"a\":1}, '[0]' ",
				"  [0],               {\"a\":1}, '[0]' ",
				"  [0],               {\"a\":1}, '[0]' ",
				" '[{\"a\":[1, 2]}]', {\"a\":1}, '[{\"a\":[1,2]}]' ",

		})
		void Test_with_Array_Object_Json(@ConvertWith(StringJsonConverter.class) JsonValue input1, @ConvertWith(StringJsonConverter.class) JsonValue input2, String expected) throws EaterException {
			assertTimExpectedOutputFromInput(cut, input1, input2, expected);
		}

		@ParameterizedTest(name = paramTestName)
		@CsvSource(value = {
				" {},                  [],                 {} ",
				" {\"a\":1},           [],                 {\"a\":1} ",
				" '{\"a\":[1,2,3]}',   [0],                '{\"a\":[1,2,3]}' ",
				" '{\"z\":0,\"a\":1}', [0],                '{\"z\":0,\"a\":1}' ",
				" '{\"z\":0,\"a\":1}', '[{\"a\":[1, 2]}]', '{\"z\":0,\"a\":1}' ",

		})
		void Test_with_Object_Array_Json(@ConvertWith(StringJsonConverter.class) JsonValue input1, @ConvertWith(StringJsonConverter.class) JsonValue input2, String expected) throws EaterException {
			assertTimExpectedOutputFromInput(cut, input1, input2, expected);
		}
	}
}
