package net.sourceforge.plantuml.tim.builtin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.expression.TValue;

public class ModuloTest {

    /**
     * Tests mod according to a list of input / expected output
     *
     * @throws EaterExceptionLocated should not
     */

    @ParameterizedTest
    @CsvSource(nullValues = "null", value = {
        " 3, 2, 1",
        " 2, 3, 2",
        " -5, -4, -1",
        " 7, -5, 2",
        " -8, -6, -2",})
    void executeReturnFunctionModuloTest(Integer dividend, Integer divisor, Integer expected) throws EaterException {
        Modulo cut = new Modulo();

        List<TValue> values = Arrays.asList(TValue.fromInt(dividend), TValue.fromInt(divisor));
        TValue tValue = cut.executeReturnFunction(null, null, null, values, null);
        assertEquals(expected, tValue.toInt());
    }

    @Test
    void executeReturnFunctionModuloWithZeroDivisorTest() throws EaterException {
        Modulo cut = new Modulo();

        StringLocated stringLocated = new StringLocated("test location", null);
        List<TValue> values = Arrays.asList(TValue.fromInt(42), TValue.fromInt(0));
        EaterException exception = assertThrows(EaterException.class, () -> {
            cut.executeReturnFunction(null, null, stringLocated, values, null);
        });

        assertEquals("Divide by zero", exception.getMessage());
    }
}
