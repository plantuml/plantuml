package net.sourceforge.plantuml.tim.stdlib;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.expression.TValue;

/**
 * Tests the builtin function %chr.
 */
class ChrTest {

	/**
	 * Tests chr according to a list of input / expected output 
	 *
	 * @throws EaterException should not
	 * @throws EaterExceptionLocated should not
	 */
	@ParameterizedTest
	@CsvSource(nullValues = "null", value = {
			" 65   , A ",
			" 9    , '\t' ",
			" 32   , ' ' ",
			" 33   , '!' ",
			" 34   , '\"' ",
			" 224  , à ",
			" 233  , é ",
// TODO: fix `%chr` to allow Unicode chars, the corresponding tests are here:
//			" 128512 , 😀 ",
//			" 128512 , \uD83D\uDE00 ",
	})
	void executeReturnFunctionChrTest(Integer input, String expected) throws EaterException, EaterExceptionLocated {
		Chr cut = new Chr();

		List<TValue> values = Collections.singletonList(TValue.fromInt(input));
		TValue tValue = cut.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}
}