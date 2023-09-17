package net.sourceforge.plantuml.tim.stdlib;

import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutput;
import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutputFromInput;

import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.TFunction;

/**
 * Tests the builtin function.
 */
@IndicativeSentencesGeneration(separator = ": ", generator = ReplaceUnderscores.class)

class AlwaysTrueTest {
	TFunction cut = new AlwaysTrue();
	final String cutName = "AlwaysTrue";

	@Test
	void Test_without_Param() throws EaterException, EaterExceptionLocated {
		assertTimExpectedOutput(cut, "1");
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "(''{0}'') = {1}")
	@CsvSource(nullValues = "null", value = {
			" 0     , 1 ",
			" 1     , 1 ",
			" 'a'   , 1 ",
	})
	void Test_with_String(String input, String expected) throws EaterException, EaterExceptionLocated {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}

	@ParameterizedTest(name = "[{index}] " + cutName + "({0}) = {1}")
	@CsvSource(nullValues = "null", value = {
			" 0     , 1 ",
			" 1     , 1 ",
			" 123   , 1 ",
	})
	void Test_with_Integer(Integer input, String expected) throws EaterException, EaterExceptionLocated {
		assertTimExpectedOutputFromInput(cut, input, expected);
	}
}
