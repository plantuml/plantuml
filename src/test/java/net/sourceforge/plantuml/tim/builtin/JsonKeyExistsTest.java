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

class JsonKeyExistsTest {
	TFunction cut = new JsonKeyExists();
	final String cutName = "JsonKeyExists";
	final String paramTestName = "[{index}] " + cutName + "({0}, {1}) = {2}";

	@ParameterizedTest(name = paramTestName)
	@CsvSource({
		" '{\"a\":[1, 2]}'  ,   a, 1 ",
		" '{\"abc\":[1, 2]}', abc, 1 ",
		" '{\"123\":[1, 2]}', 123, 1 ",
		" '{\"001\":[1, 2]}', 001, 1 ",
		" '{\"050\":[1, 2]}', 050, 1 ",
		" '{\"50\":[1, 2]}' ,  50, 1 ",
		" '{\"001\":[1, 2]}',   1, 0 ",
		" '{\"050\":[1, 2]}',  50, 0 ",
		" '{\"name\": \"Mark McGwire\", \"hr\": 65, \"avg\": 0.278}', hr, 1",
		" '{\"name\": \"Mark McGwire\", \"hr\": 65, \"avg\": 0.278}', foo, 0",
	})
	void Test_with_String(@ConvertWith(StringJsonConverter.class) JsonValue input, String key, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, key, expected);
	}

	@ParameterizedTest(name = paramTestName)
	@CsvSource({
		" '{\"123\":[1, 2]}', 123, 0 ",
		" '{\"001\":[1, 2]}', 001, 0 ",
		" '{\"001\":[1, 2]}',   1, 0 ",
		" '{\"050\":[1, 2]}', 050, 0 ",
		" '{\"050\":[1, 2]}',  50, 0 ",
		" '{\"50\":[1, 2]}' ,  50, 0 ",
	})
	void Test_with_Integer(@ConvertWith(StringJsonConverter.class) JsonValue input, Integer key, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, key, expected);
	}

	@ParameterizedTest(name = paramTestName)
	@CsvSource({
		" '{\"a\":[1, 2]}'  ,   \"a\", 1 ",
		" '{\"abc\":[1, 2]}', \"abc\", 1 ",
		" '{\"123\":[1, 2]}', \"123\", 1 ",
		" '{\"001\":[1, 2]}', \"001\", 1 ",
		" '{\"050\":[1, 2]}', \"050\", 1 ",
		" '{\"123\":[1, 2]}',     123, 0 ",
		" '{\"001\":[1, 2]}',       1, 0 ",
		" '{\"050\":[1, 2]}',      50, 0 ",
		" '{\"50\":[1, 2]}' ,      50, 0 ",
		" '{\"name\": \"Mark McGwire\", \"hr\": 65, \"avg\": 0.278}', \"hr\", 1",
		" '{\"name\": \"Mark McGwire\", \"hr\": 65, \"avg\": 0.278}', \"foo\", 0",
		" '{\"name\": \"Mark McGwire\", \"hr\": 65, \"avg\": 0.278}',   null, 0",
		" '{\"name\": \"Mark McGwire\", \"hr\": 65, \"avg\": 0.278}',   true, 0",
		" '{\"name\": \"Mark McGwire\", \"hr\": 65, \"avg\": 0.278}',  false, 0",
	})
	void Test_with_Json(@ConvertWith(StringJsonConverter.class) JsonValue input, @ConvertWith(StringJsonConverter.class) JsonValue key, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, key, expected);
	}
}
