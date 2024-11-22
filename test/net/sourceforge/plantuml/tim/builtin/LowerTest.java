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

class LowerTest {
	TFunction cut = new Lower();
	final String cutName = "Lower";

	// TODO: Manage Lower function without param. (today: we observe `Function not found %lower`)
	@Disabled
	@Test
	void Test_without_Param() throws EaterException {
		assertTimExpectedOutput(cut, "");
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "(''{0}'') = {1}")
	@CsvSource(nullValues = "null", value = {
			" 0   , 0 ",
			" 1   , 1 ",
			" A   , a ",
			" a   , a ",
			" F   , f ",
			" G   , g ",
			" É   , é ",
			" 😀 , 😀 ",
	})
	void Test_with_String(String input, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "({0}) = {1}")
	@CsvSource(nullValues = "null", value = {
			" 0    , 0 ",
			" 1    , 1 ",
			" 10   , 10 ",
			" -1    , -1 ",
	})
	void Test_with_Integer(Integer input, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "({0}) = {1}")
	@CsvSource(value = {
			" '{\"a\":[1, 2]}' , '{\"a\":[1,2]}'",
			" '{\"A\":[1, 2]}' , '{\"a\":[1,2]}'",
			" '[1, 2]'         , '[1,2]'",
			" '{\"a\":[1, 2], \"b\":\"abc\", \"b\":true}' , '{\"a\":[1,2],\"b\":\"abc\",\"b\":true}'",
			" '{\"A\":[1, 2], \"B\":\"ABC\", \"B\":true}' , '{\"a\":[1,2],\"b\":\"abc\",\"b\":true}' ",
			" true, true"
			// TODO: See JSON management of TRUE/FALSE
			//" TRUE             , true ",
	})
	void Test_with_Json(@ConvertWith(StringJsonConverter.class) JsonValue input, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}
}
