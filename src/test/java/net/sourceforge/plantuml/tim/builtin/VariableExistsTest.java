package net.sourceforge.plantuml.tim.builtin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.TMemoryGlobal;
import net.sourceforge.plantuml.tim.TMemoryLocal;
import net.sourceforge.plantuml.tim.TVariableScope;
import net.sourceforge.plantuml.tim.expression.TValue;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class VariableExistsTest {

    @ParameterizedTest(name = "[{index}] VariableExists({1}) = {2} : Memory = {0}")
    @MethodSource("provideTestCasesForVariableExists")
    public void testVariableExists(Map<String, TValue> memoryData, String input, String expected) throws EaterException {
        VariableExists variableExistsFunction = new VariableExists();

        // Global memory
        TMemoryGlobal memoryGlobal = new TMemoryGlobal();
        for (Map.Entry<String, TValue> entry : memoryData.entrySet()) {
            memoryGlobal.putVariable(entry.getKey(), entry.getValue(), TVariableScope.GLOBAL, null);
        }
        String resultGlobal = variableExistsFunction.executeReturnFunction(null, memoryGlobal, null, Arrays.asList(TValue.fromString(input)), null).toString();
        assertEquals(expected, resultGlobal);

        // Local memory
        TMemoryLocal memoryLocal = new TMemoryLocal(new TMemoryGlobal(), memoryData);
        String resultLocal = variableExistsFunction.executeReturnFunction(null, memoryLocal, null, Arrays.asList(TValue.fromString(input)), null).toString();
        assertEquals(expected, resultLocal);
    }

    private static Stream<Arguments> provideTestCasesForVariableExists() {
        return Stream.of(
            Arguments.of(
                new HashMap<>(),
                "xxx",
                "0"
            ),
            Arguments.of(
                new HashMap<String, TValue>() {{
                    put("xxx", TValue.fromString("aaa"));
                }},
                "xxx",
                "1"
            ),
            Arguments.of(
                new HashMap<String, TValue>() {{
                    put("xxx", TValue.fromString("aaa"));
                }},
                "yyy",
                "0"
            ),
            Arguments.of(
                new HashMap<String, TValue>() {{
                    put("xxx", TValue.fromString("aaa"));
                    put("yyy", TValue.fromString("bbb"));
                }},
                "xxx",
                "1"
            ),
            Arguments.of(
                new HashMap<String, TValue>() {{
                    put("xxx", TValue.fromString("aaa"));
                    put("yyy", TValue.fromString("bbb"));
                }},
                "zzz",
                "0"
            ),
            Arguments.of(
                new HashMap<String, TValue>() {{
                    put("xxx", TValue.fromString("aaa"));
                }},
                "",
                "0"
            ),
            Arguments.of(
                new HashMap<String, TValue>() {{
                    put("", TValue.fromString("ddd"));
                }},
                "",
                "1"
            )
        );
    }
    
}
