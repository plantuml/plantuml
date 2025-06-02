package net.sourceforge.plantuml.preproc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VariablesTest {

    @Test
    void testCountDefaultValue() {
        Variables variables = new Variables("func", "funcDefinition");
				DefineVariable var1 =new DefineVariable("");
				DefineVariable var2 =new DefineVariable("MY_VAR=\"defaultValue\"");

        variables.add(var1);
        variables.add(var2);

        assertEquals(1, variables.countDefaultValue());
    }

    @Test
    void testRemoveSomeDefaultValuesNoRemoval() {
        Variables variables = new Variables("func", "funcDefinition");
        DefineVariable var =new DefineVariable("MY_VAR=\"defaultValue\"");

        variables.add(var);
        Variables result = variables.removeSomeDefaultValues(0);

        assertEquals(1, result.countDefaultValue());
    }

    @Test
    void testRemoveSomeDefaultValuesRemoval() {
        Variables variables = new Variables("func", "funcDefinition");
        DefineVariable var1 = new DefineVariable("MY_VAR=\"defaultValueA\"");
        DefineVariable var2 = new DefineVariable("MY_VAR=\"defaultValueB\"");

        variables.add(var1);
        variables.add(var2);
        Variables result = variables.removeSomeDefaultValues(1);

        assertEquals(1, result.countDefaultValue());
    }

    @Test
    void testRemoveSomeDefaultValuesException() {
        Variables variables = new Variables("func", "funcDefinition");
				DefineVariable var =new DefineVariable("MY_VAR=\"defaultValue\"");

        variables.add(var);

        assertThrows(IllegalArgumentException.class, () -> variables.removeSomeDefaultValues(2));
    }

    @Test
    void testApplyOnWithDefaultValues() {
        Variables variables = new Variables("func", "funcDefinition ##MY_VAR##");
				DefineVariable var =new DefineVariable("MY_VAR=\"defaultValue\"");
        variables.add(var);

        String result = variables.applyOn("func()");
        assertEquals("funcDefinition defaultValue", result);
    }

    @Test
    void testApplyOnWithoutDefaultValues() {
        Variables variables = new Variables("func", "definition ##MY_VAR##");
				DefineVariable var =new DefineVariable("MY_VAR=\"defaultValue\"");
        variables.add(var);

        String result = variables.applyOn("func(\"defaultValue\")");
        assertTrue(result.contains("defaultValue"));
    }
}
