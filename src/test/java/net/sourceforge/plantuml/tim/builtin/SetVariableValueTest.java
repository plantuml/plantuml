package net.sourceforge.plantuml.tim.builtin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TMemoryGlobal;
import net.sourceforge.plantuml.tim.TMemoryLocal;
import net.sourceforge.plantuml.tim.expression.TValue;

public class SetVariableValueTest {

    @ParameterizedTest(name = "[{index}] SetVariableValue({0}, {1})")
    @CsvSource({
        "xxx, aaa",
        "0  , 111",
        "'' , '' ",
    })
    public void testSetVariableValue(String inputKey, String inputValue) throws EaterException {
        SetVariableValue setVariableValueFunction = new SetVariableValue();
        List<TValue> args = Arrays.asList(TValue.fromString(inputKey), TValue.fromString(inputValue));

        // Global memory
        TMemoryGlobal memoryGlobal = new TMemoryGlobal();
        TValue resultGlobal = setVariableValueFunction.executeReturnFunction(null, memoryGlobal, null, args, null);
        assertEquals("", resultGlobal.toString());
        assertEquals(inputValue, memoryGlobal.getVariable(inputKey).toString());

        // Local memory
        TMemoryLocal memoryLocal = new TMemoryLocal(new TMemoryGlobal(), new HashMap<>());
        TValue resultLocal = setVariableValueFunction.executeReturnFunction(null, memoryLocal, null, args, null);
        assertEquals("", resultLocal.toString());
        assertEquals(inputValue, memoryLocal.getVariable(inputKey).toString());
    }

}
