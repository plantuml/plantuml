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

class JsonRemoveTest {
	TFunction cut = new JsonRemove();
	final String cutName = "json_remove";
	final String paramTestName = "[{index}] " + cutName + "({0}, {1}) = {2}";

	@ParameterizedTest(name = paramTestName)
	@CsvSource(value = {
			" [], -1,  []",
			" [], 1,  []",
			" [0], 0, [] ",
			" [0], 1, [0] ",
			" '[1,2,3]', -1,  '[1,2,3]' ", // To debate...
			" '[1,2,3]', 0,  '[2,3]' ",
			" '[1,2,3]', 1,  '[1,3]' ",
			" '[1,2,3]', 2,  '[1,2]' ",
			" '[1,2,3]', 3,  '[1,2,3]' ",
			" '[{\"a\":[1, 2]}, 1, \"a\"]', 2, '[{\"a\":[1,2]},1]' ",
	})
	void Test_with_Array_Json(@ConvertWith(StringJsonConverter.class) JsonValue input1, Integer input2, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input1, input2, expected);
	}
	
	@ParameterizedTest(name = paramTestName)
	@CsvSource(value = {
			" '{\"z\":0,\"a\":1}',                                       a, '{\"z\":0}'",
			" '{\"z\":0,\"a\":1}',                                       b, '{\"z\":0,\"a\":1}'",
			" '{\"a\": 1, \"b\": \"two\",\"c\":3}',                      c, '{\"a\":1,\"b\":\"two\"}'",
			" '{\"a\":1,\"b\":\"two\",\"d\":{\"da\":1,\"db\":\"two\"}}', d, '{\"a\":1,\"b\":\"two\"}'",

	})
	void Test_with_Object_Json(@ConvertWith(StringJsonConverter.class) JsonValue input1, String input2, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input1, input2, expected);
	}

	@Nested
	class Not_Nominal_Test {
		@ParameterizedTest(name = paramTestName)
		@CsvSource(value = {
				" [],        a,   []",
				" '[1,2,3]', abc, '[1,2,3]' ",
				" '[1,2,3]', 1,   '[1,2,3]' ",
				" 123, 1, 123",
				" true,  a, true",
				" true,  1, true",
				" \"abc\", a, abc",
				" \"a b c\", a, a b c", 
				" \"a b c\", 1, a b c",
		})
		void Test_with_not_good_type(@ConvertWith(StringJsonConverter.class) JsonValue input1, String input2, String expected) throws EaterException {
			assertTimExpectedOutputFromInput(cut, input1, input2, expected);
		}

		@ParameterizedTest(name = paramTestName)
		@CsvSource(value = {
				" '{\"z\":0,\"a\":1}',                   5, '{\"z\":0,\"a\":1}'",
				" '{\"z\":0,\"5\":1}',                   5, '{\"z\":0}'", // Allow cast int -> string
				" '{\"a\": 1, \"b\": \"two\",\"c\":3}', 10, '{\"a\":1,\"b\":\"two\",\"c\":3}'",
		})
		void Test_with_not_good_type(@ConvertWith(StringJsonConverter.class) JsonValue input1, Integer input2, String expected) throws EaterException {
			assertTimExpectedOutputFromInput(cut, input1, input2, expected);
		}
	}
}
