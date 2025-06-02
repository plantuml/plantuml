package net.sourceforge.plantuml.preproc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefineVariableTest {

    @Test
    public void testNameWithoutDefaultValue() {
        DefineVariable variable = new DefineVariable("MY_VAR");
        assertEquals("MY_VAR", variable.getName());
        assertNull(variable.getDefaultValue());
    }

    @Test
		public void testNameWithDefaultValue() {
        DefineVariable variable = new DefineVariable("MY_VAR=\"default\"");
        assertEquals("MY_VAR", variable.getName());
        assertEquals("default", variable.getDefaultValue());
    }

    @Test
		public void testNameWithDefaultValueContainingSpaces() {
        DefineVariable variable = new DefineVariable("MY_VAR=\"default value\"");
        assertEquals("MY_VAR", variable.getName());
        assertEquals("default value", variable.getDefaultValue());
    }

    @Test
		public void testNameWithDefaultValueContainingSpecialCharacters() {
        DefineVariable variable = new DefineVariable("MY_VAR=\"value_with_@#$%^&*\"");
        assertEquals("MY_VAR", variable.getName());
        assertEquals("value_with_@#$%^&*", variable.getDefaultValue());
    }

    @Test
		public void testRemoveDefaultValue() {
        DefineVariable variable = new DefineVariable("MY_VAR=\"default\"");
			  assertEquals("default", variable.getDefaultValue());
        DefineVariable withoutDefault = variable.removeDefault();
        assertEquals("MY_VAR", withoutDefault.getName());
        assertNull(withoutDefault.getDefaultValue());
    }

    @Test
		public void testRemoveDefaultValueThrowsExceptionWhenNoDefault() {
        DefineVariable variable = new DefineVariable("MY_VAR");
        assertThrows(IllegalStateException.class, variable::removeDefault);
    }

    @Test
		public void testNameWithExtraSpaces() {
        DefineVariable variable = new DefineVariable("  MY_VAR  =  \"default\"  ");
        assertEquals("MY_VAR", variable.getName());
        assertEquals("default", variable.getDefaultValue());
    }

    @Test
		public void testNameWithOnlySpaces() {
        assertThrows(IllegalStateException.class, () -> new DefineVariable("   ").removeDefault());
    }

    @Test
		public void testEmptyName() {
        assertThrows(IllegalStateException.class, () -> new DefineVariable("").removeDefault());
    }
}