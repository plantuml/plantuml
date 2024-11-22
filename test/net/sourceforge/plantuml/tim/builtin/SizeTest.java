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

class SizeTest {
	TFunction cut = new Size();
	final String cutName = "Size";

	// TODO: Manage `Size` function without param. (today: we observe `Function not found`)
	@Disabled
	@Test
	void Test_without_Param() throws EaterException {
		assertTimExpectedOutput(cut, "0");
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "(''{0}'') = {1}")
	@CsvSource(nullValues = "null", value = {
			" 0          , 1 ",
			" 1          , 1 ",
			" 10         , 2 ",
			" a          , 1 ",
			" A          , 1 ",
			" ABC        , 3 ",
			" '[1, 2]'   , 6 ",
			" '{[1, 2]}' , 8 ",
			" à          , 1 ",
// TODO: fix `Size` to allow Unicode chars, the corresponding tests are here:
//			" 😀         , 1 ",
//			" \uD83D\uDE00 , 1",
	})
	void Test_with_String(String input, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "({0}) = {1}")
	@CsvSource(nullValues = "null", value = {
			" 0    , 0 ",
			" 1    , 0 ",
			" 10   , 0 ",
	})
	void Test_with_Integer(Integer input, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "({0}) = {1}")
	@CsvSource(value = {
			" 0, 0",
			" \"abc\", 0",
			" '\"abc\"', 0",
			" '{\"a\":[1, 2]}' , 1",
			" '[1, 2]' , 2",
			" '[\"a\", \"b\"]' , 2",
			" '{\"a\":[1, 2], \"b\":\"abc\", \"b\":true}' , 3",
			" true, 0 ",
			" 1, 0 ",
			" null, 0 ",
	})
	void Test_with_Json(@ConvertWith(StringJsonConverter.class) JsonValue input, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}
}
