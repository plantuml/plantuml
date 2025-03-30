package net.sourceforge.plantuml.tim;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.tim.expression.TValue;
import net.sourceforge.plantuml.json.JsonValue;

/**
 * Class to help test of `tim/builtin`.
 */
public class TimTestUtils {

	// Tfunc: () -> (String)
	public static void assertTimExpectedOutput(TFunction func, String expected) throws EaterException {
		final TValue tValue = func.executeReturnFunction(null, null, null, Collections.emptyList(), null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (Integer) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, Integer input, String expected) throws EaterException {
		final List<TValue> values = Collections.singletonList(TValue.fromInt(input));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (Integer, Integer) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, Integer input1, Integer input2, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromInt(input1), TValue.fromInt(input2));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (Integer, Integer, Integer) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, Integer input1, Integer input2, Integer input3, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromInt(input1), TValue.fromInt(input2), TValue.fromInt(input3));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (Integer, Integer, Integer, Integer) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, Integer input1, Integer input2, Integer input3, Integer input4, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromInt(input1), TValue.fromInt(input2), TValue.fromInt(input3), TValue.fromInt(input4));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (String) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, String input, String expected) throws EaterException {
		final List<TValue> values = Collections.singletonList(TValue.fromString(input));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (String, String) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, String input1, String input2, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromString(input1), TValue.fromString(input2));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (String, Int) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, String input1, Integer input2, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromString(input1), TValue.fromInt(input2));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (String, Int, Int) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, String input1, int input2, int input3, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromString(input1), TValue.fromInt(input2), TValue.fromInt(input3));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (JsonValue) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, JsonValue input, String expected) throws EaterException {
		final List<TValue> values = Collections.singletonList(TValue.fromJson(input));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}
	
	// Tfunc: (JsonValue, JsonValue) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, JsonValue input1,  JsonValue input2, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromJson(input1), TValue.fromJson(input2));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (JsonValue, Int) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, JsonValue input1,  Integer input2, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromJson(input1), TValue.fromInt(input2));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (JsonValue, Int, Int) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, JsonValue input1,  Integer input2, Integer input3, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromJson(input1), TValue.fromInt(input2), TValue.fromInt(input3));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (JsonValue, Int, String) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, JsonValue input1,  Integer input2, String input3, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromJson(input1), TValue.fromInt(input2), TValue.fromString(input3));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (JsonValue, Int, JsonValue) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, JsonValue input1,  Integer input2, JsonValue input3, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromJson(input1), TValue.fromInt(input2), TValue.fromJson(input3));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (JsonValue, String) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, JsonValue input1,  String input2, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromJson(input1), TValue.fromString(input2));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (JsonValue, String, JsonValue) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, JsonValue input1,  String input2, JsonValue input3, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromJson(input1), TValue.fromString(input2), TValue.fromJson(input3));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (JsonValue, String, String) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, JsonValue input1,  String input2, String input3, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromJson(input1), TValue.fromString(input2), TValue.fromString(input3));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

	// Tfunc: (JsonValue, String, Int) -> (String)
	public static void assertTimExpectedOutputFromInput(TFunction func, JsonValue input1,  String input2, Integer input3, String expected) throws EaterException {
		final List<TValue> values = Arrays.asList(TValue.fromJson(input1), TValue.fromString(input2), TValue.fromInt(input3));
		final TValue tValue = func.executeReturnFunction(null, null, null, values, null);
		assertEquals(expected, tValue.toString());
	}

}