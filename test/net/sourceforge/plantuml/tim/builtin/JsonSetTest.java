package net.sourceforge.plantuml.tim.builtin;

import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutputFromInput;

import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
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

class JsonSetTest {
	TFunction cut = new JsonSet();
	final String cutName = "json_set";
	final String paramTestName = "[{index}] " + cutName + "({0}, {1}, {2}) = {3}";

	@ParameterizedTest(name = paramTestName)
	@CsvSource(value = {
			"  [0],               0, 1,            [1]",
			"  [0],               0, \"a\",        '[\"a\"]' ",
			"  [0],               0, {\"a\": 123}, '[{\"a\":123}]' ",
			"  [0],               0, [1],          '[[1]]' ",
			" '[{\"a\":[1, 2]}]', 0, 1,            '[1]' ",

	})
	void Test_with_Array_Json(@ConvertWith(StringJsonConverter.class) JsonValue input1, Integer input2, @ConvertWith(StringJsonConverter.class) JsonValue input3, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input1, input2, input3, expected);
	}

	@ParameterizedTest(name = paramTestName)
	@CsvSource(value = {
			"  [0],               0, -1,        '[\"-1\"]' ",
			"  [0],               0, 1,         '[\"1\"]' ",
			"  [0],               0, 123,       '[\"123\"]' ",
			"  [0],               0, a,         '[\"a\"]' ",
			"  [0],               0, \"a\",     '[\"\\\"a\\\"\"]' ",
			"  [0],               0, a b c,     '[\"a b c\"]' ",
			"  [0],               0, \"a b c\", '[\"\\\"a b c\\\"\"]' ",
			" '[0,1]',            1, -1,        '[0,\"-1\"]' ",
			" '[0,1]',            1, 1,         '[0,\"1\"]' ",
			" '[0,1]',            1, 123,       '[0,\"123\"]' ",
			" '[0,1]',            1, a,         '[0,\"a\"]' ",
			" '[0,1]',            1, \"a\",     '[0,\"\\\"a\\\"\"]' ",
			" '[0,1]',            1, a b c,     '[0,\"a b c\"]' ",
			" '[0,1]',            1, \"a b c\", '[0,\"\\\"a b c\\\"\"]' ",
			" '[{\"a\":[1, 2]}]', 0, 1,         '[\"1\"]' ",
			" '[{\"a\":[1, 2]}]', 0, a,         '[\"a\"]' ",
			" '[{\"a\":[1, 2]}, 1]', 1, 1,      '[{\"a\":[1,2]},\"1\"]' ",
			" '[{\"a\":[1, 2]}, 1]', 1, a,      '[{\"a\":[1,2]},\"a\"]' ",
	})
	void Test_with_Array_Json_add_Str(@ConvertWith(StringJsonConverter.class) JsonValue input1, Integer input2, String input3, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input1, input2, input3, expected);
	}

	@ParameterizedTest(name = paramTestName)
	@CsvSource(value = {
			"  [0],               0, -1,   [-1]",
			"  [0],               0, 1,    [1]",
			"  [0],               0, 123, '[123]' ",
			" '[{\"a\":[1, 2]}]', 0, 1,   '[1]' ",
			" '[{\"a\":[1, 2]}]', 0, 123, '[123]' ",
	})
	void Test_with_Array_Json_add_Int(@ConvertWith(StringJsonConverter.class) JsonValue input1, Integer input2, Integer input3, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input1, input2, input3, expected);
	}

	@ParameterizedTest(name = paramTestName)
	@CsvSource(value = {
			" {}, a, 1,  {\"a\":1}",
			" {}, a, '[1,2,3]',  '{\"a\":[1,2,3]}'",
			" {}, a, '{\"b\":123}',  '{\"a\":{\"b\":123}}'",
			" {}, a, '{\"b\":\"abc\"}',  '{\"a\":{\"b\":\"abc\"}}'",
			" {\"z\":0}, a, 1,  '{\"z\":0,\"a\":1}'",
			" {\"z\":0}, a, '[1,2,3]',  '{\"z\":0,\"a\":[1,2,3]}'",
			" {\"z\":0}, a, '{\"b\":123}',  '{\"z\":0,\"a\":{\"b\":123}}'",
			" {\"z\":0}, a, '{\"b\":\"abc\"}',  '{\"z\":0,\"a\":{\"b\":\"abc\"}}'",
			" '{\"a\": 1, \"b\": \"two\"}', c, 3, '{\"a\":1,\"b\":\"two\",\"c\":3}'",
			" '{\"a\": 1, \"b\": \"two\"}', d, '{\"da\": 1, \"db\": \"two\"}', '{\"a\":1,\"b\":\"two\",\"d\":{\"da\":1,\"db\":\"two\"}}'",
			" {\"a\":0}, a, 1,  '{\"a\":1}'",
			" {\"a\":0}, a, '[1,2,3]',  '{\"a\":[1,2,3]}'",
			" {\"a\":0}, a, '{\"b\":123}',  '{\"a\":{\"b\":123}}'",
			" {\"a\":0}, a, '{\"b\":\"abc\"}',  '{\"a\":{\"b\":\"abc\"}}'",
			" '{\"a\": 1, \"b\": \"two\"}', b, 3, '{\"a\":1,\"b\":3}'",
			" '{\"a\": 1, \"b\": \"two\"}', b, '{\"da\": 1, \"db\": \"two\"}', '{\"a\":1,\"b\":{\"da\":1,\"db\":\"two\"}}'",
			" '{\"a\":0, \"a\":5}', a, 1,  '{\"a\":0,\"a\":1}'",
			" '{\"a\":0, \"a\":5}', a, '[1,2,3]',  '{\"a\":0,\"a\":[1,2,3]}'",
			" '{\"a\":0, \"a\":5}', a, '{\"b\":123}',  '{\"a\":0,\"a\":{\"b\":123}}'",
			" '{\"a\":0, \"a\":5}', a, '{\"b\":\"abc\"}',  '{\"a\":0,\"a\":{\"b\":\"abc\"}}'",
			" '{\"a\": 1, \"b\": 5, \"b\": \"two\"}', b, 3, '{\"a\":1,\"b\":5,\"b\":3}'",
			" '{\"a\": 1, \"b\": 5, \"b\": \"two\"}', b, '{\"da\": 1, \"db\": \"two\"}', '{\"a\":1,\"b\":5,\"b\":{\"da\":1,\"db\":\"two\"}}'",

	})
	void Test_with_Object_Json(@ConvertWith(StringJsonConverter.class) JsonValue input1, String input2, @ConvertWith(StringJsonConverter.class) JsonValue input3, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input1, input2, input3, expected);
	}

	@ParameterizedTest(name = paramTestName)
	@CsvSource(value = {
			" {}, a, 1,         {\"a\":\"1\"}",
			" {}, a, 'abc',    '{\"a\":\"abc\"}'",
			" {}, a, 'a b c',  '{\"a\":\"a b c\"}'",
			" {\"age\" : 30}, name, Sally, '{\"age\":30,\"name\":\"Sally\"}'",
			" '{\"age\" : 30, \"name\":\"Bob\"}', name, Sally, '{\"age\":30,\"name\":\"Sally\"}'",
	})
	void Test_with_Object_Json_add_Str(@ConvertWith(StringJsonConverter.class) JsonValue input1, String input2, String input3, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input1, input2, input3, expected);
	}

	@ParameterizedTest(name = paramTestName)
	@CsvSource(value = {
			" {}, a, 1,    {\"a\":1}",
			" {}, a, 123, '{\"a\":123}'",
			" {\"age\" : 30}, name, 123,  '{\"age\":30,\"name\":123}'",
			" '{\"age\" : 30, \"name\":\"Bob\"}', name, 123,  '{\"age\":30,\"name\":123}'",
	})
	void Test_with_Object_Json_add_Int(@ConvertWith(StringJsonConverter.class) JsonValue input1, String input2, Integer input3, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input1, input2, input3, expected);
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "({0}, {1}) = {2}")
	@CsvSource(value = {
			" {},                                 {\"a\":1},        {\"a\":1}",
			" {\"age\" : 30},                     {\"name\":123},  '{\"age\":30,\"name\":123}'",
			" '{\"age\" : 30, \"name\":\"Bob\"}', {\"name\":123},  '{\"age\":30,\"name\":123}'",
			" '{\"partlen\": \"2\", \"color\": {\"A\":\"red\", \"B\":\"blue\"}}', '{\"color\":{\"A\":\"black\"}}', '{\"partlen\":\"2\",\"color\":{\"A\":\"black\",\"B\":\"blue\"}}'",
			" {}, \"a\", {}",
			" {}, \"a b c\", {}",
			" {}, 123, {}",
			" {}, '[1,2]', {}",
			" {}, true, {}",
			" {}, false, {}",
			" {}, null, {}",
	})
	void Test_with_Object_Json_add_Object(@ConvertWith(StringJsonConverter.class) JsonValue input1, @ConvertWith(StringJsonConverter.class) JsonValue input2, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input1, input2, expected);
	}
}
