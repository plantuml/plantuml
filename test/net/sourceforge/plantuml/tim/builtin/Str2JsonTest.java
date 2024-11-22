package net.sourceforge.plantuml.tim.builtin;

import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutput;
import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutputFromInput;

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

class Str2JsonTest {
	TFunction cut = new Str2Json();
	final String cutName = "str2json";
	final String paramTestName = "[{index}] " + cutName + "({0}) = {1}";

	@Test
	void Test_without_Param() throws EaterException {
		assertTimExpectedOutput(cut, "");
	}

	@ParameterizedTest(name = paramTestName)
	@CsvSource(nullValues = "null", value = {
			" 0,   0",
			" -1, -1",
			" 12, 12",
	})
	void Test_with_Integer(Integer input, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}

	@ParameterizedTest(name = paramTestName)
	@CsvSource(value = {
		    " 0, 0 ",
		    " 1, 1 ",
		    " 12, 12 ",
		    " -1, -1 ",
			" true, true ",
		    " [true], [true] ",
			" null, null ",
			" '', '' ",
			" \"\", '' ",
			" a, '' ",
		    " \"a\", a ",
		    " '\"a\"', a ",
		    " \"a b\", a b ",
		    " 'a b', '' ",
			" [], [] ",
			" '[1, 2]', '[1,2]' ",
			" '[\"a\"]', '[\"a\"]' ",
			" '{\"a\":[1, 2]}', '{\"a\":[1,2]}' ",
			" '[{\"a\":[1, 2]}]' , '[{\"a\":[1,2]}]' ",
			" '{\"a\":\"abc\"}', {\"a\":\"abc\"} ",
			" '[{\"a\":[1, 2]}, {\"b\":[3, 4]}]', '[{\"a\":[1,2]},{\"b\":[3,4]}]'",
			" '{\"a\":[1, 2], \"b\":\"abc\", \"b\":true}', '{\"a\":[1,2],\"b\":\"abc\",\"b\":true}'",
			" '[3, \"different\", { \"types\" : \"of values\" }]', '[3,\"different\",{\"types\":\"of values\"}]'",
			" '{\"a\": 1, \"b\": \"two\"}', '{\"a\":1,\"b\":\"two\"}' ",
	})
	void Test_with_String(String input, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}

	@ParameterizedTest(name = paramTestName)
	@CsvSource(value = {
		    " 0, 0 ",
		    " 1, 1 ",
		    " 12, 12 ",
		    " -1, -1 ",
			" true, true ",
		    " [true], [true] ",
			" null, null ",
			//" '', '' ",
			//" \"\", '' ",
			//" a, '' ",
		    //" \"a\", a ",
		    //" '\"a\"', a ",
		    //" \"a b\", a b ",
			//" 'a b', a b ",
			" [], [] ",
			" '[1, 2]', '[1,2]' ",
			" '[\"a\"]', '[\"a\"]' ",
			" '{\"a\":[1, 2]}', '{\"a\":[1,2]}' ",
			" '[{\"a\":[1, 2]}]' , '[{\"a\":[1,2]}]' ",
			" '{\"a\":\"abc\"}', {\"a\":\"abc\"} ",
			" '[{\"a\":[1, 2]}, {\"b\":[3, 4]}]', '[{\"a\":[1,2]},{\"b\":[3,4]}]'",
			" '{\"a\":[1, 2], \"b\":\"abc\", \"b\":true}', '{\"a\":[1,2],\"b\":\"abc\",\"b\":true}'",
			" '[3, \"different\", { \"types\" : \"of values\" }]', '[3,\"different\",{\"types\":\"of values\"}]'",
			" '{\"a\": 1, \"b\": \"two\"}', '{\"a\":1,\"b\":\"two\"}' ",
	})
	void Test_with_Json(@ConvertWith(StringJsonConverter.class) JsonValue input, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}
}
