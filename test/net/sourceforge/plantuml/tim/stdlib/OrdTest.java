package net.sourceforge.plantuml.tim.stdlib;

import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutputFromInput;

import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.TFunction;

/**
 * Tests the builtin function %ord.
 */
@IndicativeSentencesGeneration(separator = ": ", generator = ReplaceUnderscores.class)

class OrdTest {
	TFunction cut = new Ord();
	final String cutName = "Ord";
	/**
	 * Tests ord according to a list of input / expected output 
	 *
	 * @throws EaterException should not
	 * @throws EaterExceptionLocated should not
	 */
	@ParameterizedTest(name = "[{index}] " + cutName + "(''{0}'') = {1}")
	@CsvSource(nullValues = "null", value = {
			" A     , 65 ",
			" ABC   , 65 ",
			" 'A'   , 65 ",
			" '\t'  , 9 ",
			" ' '   , 32 ",
			" '!'   , 33 ",
			" '\"'  , 34 ",
			" à     , 224 ",			
			" 'à'   , 224 ",
			" é     , 233 ",
			" 'é'   , 233 ",
			" 😀   , 128512 ",
			" \uD83D\uDE00 , 128512 ",
	})
	void Test_with_String(String input, String expected) throws EaterException, EaterExceptionLocated {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}
}