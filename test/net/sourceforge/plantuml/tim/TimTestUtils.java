package net.sourceforge.plantuml.tim;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.tim.expression.TValue;
import net.sourceforge.plantuml.json.JsonValue;

/**
 * Class to help test of `tim/stdlib`.
 */
public class TimTestUtils {

	// Tfunc: () -> (String)
	public static void assertTimExpectedOutput(TFunction func, String expected) throws EaterException {
		TValue tValue = func.executeReturnFunction(null, null, null, null, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (Integer) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, Integer input, String expected) throws EaterException {
		List<TValue> values = Collections.singletonList(TValue.fromInt(input));
		TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (String) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, String input, String expected) throws EaterException {
		List<TValue> values = Collections.singletonList(TValue.fromString(input));
		TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (String, String) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, String input1, String input2, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromString(input1), TValue.fromString(input2));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (JsonValue) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, JsonValue input, String expected) throws EaterException {
		List<TValue> values = Collections.singletonList(TValue.fromJson(input));
		TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

}