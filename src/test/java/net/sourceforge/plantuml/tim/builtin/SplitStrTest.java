package net.sourceforge.plantuml.tim.builtin;

import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutputFromInput;

import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TFunction;

/**
 * Tests the builtin function.
 */
@IndicativeSentencesGeneration(separator = ": ", generator = ReplaceUnderscores.class)

class SplitStrTest {
	TFunction cut = new SplitStr();
	final String cutName = "SplitStr";

	@ParameterizedTest(name = "[{index}] " + cutName + "(''{0}'', ''{1}'') = {2}")
	@CsvSource(nullValues = "null", value = {
		" abc~def~ghi,     ~,         '[\"abc\",\"def\",\"ghi\"]' ",
		" foozbar,         z,         '[\"foo\",\"bar\"]' ",
		" FooBar,          (?=[A-Z]), '[\"FooBar\"]' ",
		" SomeDumbExample, (?=[A-Z]), '[\"SomeDumbExample\"]' ",
	})
	void Test_with_String(String input, String regex, String expected) throws EaterException {
		assertTimExpectedOutputFromInput(cut, input, regex, expected);
	}
}
