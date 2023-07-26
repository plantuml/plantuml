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
 * Tests the builtin function %ord.
 */
class OrdTest {

	/**
	 * Tests ord according to a list of input / expected output 
	 *
	 * @throws EaterException should not
	 * @throws EaterExceptionLocated should not
	 */
	@ParameterizedTest
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
	void executeReturnFunctionOrdTest(String input, String expected) throws EaterException, EaterExceptionLocated {
		Ord cut = new Ord();

		List<TValue> values = Collections.singletonList(TValue.fromString(input));
		TValue tValue = cut.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}
}