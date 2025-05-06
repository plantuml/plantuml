package net.sourceforge.plantuml.tim.builtin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TMemoryGlobal;
import net.sourceforge.plantuml.tim.TMemoryLocal;
import net.sourceforge.plantuml.tim.TVariableScope;
import net.sourceforge.plantuml.tim.expression.TValue;

public class GetVariableValueTest {

    @ParameterizedTest(name = "[{index}] GetVariableValue({1}) = {2} : Memory = {0}")
    @MethodSource("provideTestCasesForGetVariableValue")
    public void testGetVariableValue(Map<String, TValue> memoryData, String input, String expected) throws EaterException {
        GetVariableValue getVariableValueFunction = new GetVariableValue();
        
        // Global memory
        TMemoryGlobal memoryGlobal = new TMemoryGlobal();
        for (Map.Entry<String, TValue> entry : memoryData.entrySet()) {
            memoryGlobal.putVariable(entry.getKey(), entry.getValue(), TVariableScope.GLOBAL, null);
        }
        String resultGlobal = getVariableValueFunction.executeReturnFunction(null, memoryGlobal, null, Arrays.asList(TValue.fromString(input)), null).toString();
        assertEquals(expected, resultGlobal);

        // Local memory
        TMemoryLocal memoryLocal = new TMemoryLocal(new TMemoryGlobal(), memoryData);
        String resultLocal = getVariableValueFunction.executeReturnFunction(null, memoryLocal, null, Arrays.asList(TValue.fromString(input)), null).toString();
        assertEquals(expected, resultLocal);
    }

    public static Stream<Arguments> provideTestCasesForGetVariableValue() {
        return Stream.of(
            Arguments.of(
                new HashMap<>(),
                "xxx",
                ""
            ),
            Arguments.of(
                new HashMap<String, TValue>() {{
                    put("xxx", TValue.fromString("aaa"));
                }},
                "xxx",
                "aaa"
            ),
            Arguments.of(
                new HashMap<String, TValue>() {{
                    put("xxx", TValue.fromString("aaa"));
                }},
                "yyy",
                ""
            ),
            Arguments.of(
                new HashMap<String, TValue>() {{
                    put("xxx", TValue.fromString("aaa"));
                    put("yyy", TValue.fromString("bbb"));
                }},
                "xxx",
                "aaa"
            ),
            Arguments.of(
                new HashMap<String, TValue>() {{
                    put("xxx", TValue.fromString("aaa"));
                    put("yyy", TValue.fromString("bbb"));
                }},
                "zzz",
                ""
            ),
            Arguments.of(
                new HashMap<String, TValue>() {{
                    put("xxx", TValue.fromString("aaa"));
                }},
                "",
                ""
            ),
            Arguments.of(
                new HashMap<String, TValue>() {{
                    put("", TValue.fromString("ddd"));
                }},
                "",
                "ddd"
            )
        );
    }

}
