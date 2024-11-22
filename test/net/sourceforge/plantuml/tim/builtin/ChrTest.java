package net.sourceforge.plantuml.tim.builtin;

import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutputFromInput;

import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TFunction;

/**
 * Tests the builtin function %chr.
 */
@IndicativeSentencesGeneration(separator = ": ", generator = ReplaceUnderscores.class)

class ChrTest {
	TFunction cut = new Chr();
	final String cutName = "Chr";
	/**
	 * Tests chr according to a list of input / expected output 
	 *
	 * @throws EaterException should not
	 */
	@ParameterizedTest(name = "[{index}] " + cutName + "({0}) = ''{1}''")
	@CsvSource(nullValues = "null", value = {
			" 65   , A ",
			" 9    , '\t' ",
			" 32   , ' ' ",
			" 33   , '!' ",
			" 34   , '\"' ",
			" 224  , à ",
			" 233  , é ",
// DONE: fix `%chr` to allow Unicode chars, the corresponding tests are here:
			" 128512 , 😀 ",
			" 128512 , \uD83D\uDE00 ",
	})
	void Test_with_Integer(Integer input, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}
}