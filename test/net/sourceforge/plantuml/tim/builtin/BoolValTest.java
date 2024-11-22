package net.sourceforge.plantuml.tim.builtin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.expression.TValue;

class BoolValTest {
	/**
	 * Tests boolval according to a list of input / expected output 
	 *
	 * @throws EaterException should not
	 */
	@ParameterizedTest
	@CsvSource(nullValues = "null", value = {
			"True, true",
			"true, true",
			"1, true",
			"False, false",
			"false, false",
			"0, false",
	})
	void executeReturnFunctionWithValidBooleanValueStringTest(String input, Boolean expected) throws EaterException {
		BoolVal cut = new BoolVal();
		List<TValue> values = Arrays.asList(TValue.fromString(input));

		TValue tValue = cut.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toBoolean());
	}
	
	@ParameterizedTest
	@CsvSource(nullValues = "null", value = {
		"2",
		"hello",
	})
	void executeReturnFunctionWithInvalidBooleanValueStringTest(String input) throws EaterException {
		BoolVal cut = new BoolVal();
        List<TValue> values = Arrays.asList(TValue.fromString(input));

		StringLocated stringLocated = new StringLocated("test location", null);
		EaterException exception = assertThrows(EaterException.class, () -> {
            cut.executeReturnFunction(null, null, stringLocated, values, null);
        });

		assertEquals("Cannot convert " + input + " to boolean.", exception.getMessage());
	}
}
