package net.sourceforge.plantuml.preproc;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DefinesTest {

    @Test
    void testStringComparison() {
        Defines defines = new Defines();
        assertTrue(defines.isDefine("\"foo\" == \"foo\""));
        assertFalse(defines.isDefine("\"foo\" == \"bar\""));
        assertTrue(defines.isDefine("\"foo\" != \"bar\""));
        assertFalse(defines.isDefine("\"foo\" != \"foo\""));
    }

    @Test
    void testJsonValueComparison() {
        Defines defines = new Defines();
        defines.define("data", Collections.singletonList("{ \"a\": \"foo\", \"b\": \"bar\" }"), false);
        assertTrue(defines.isDefine("$data[a] == \"foo\""));
        assertFalse(defines.isDefine("$data[a] == \"bar\""));
        assertTrue(defines.isDefine("$data[b] != \"foo\""));
        assertFalse(defines.isDefine("$data[b] != \"bar\""));
    }

    @Test
    void testJsonValueWithVariableKey() {
        Defines defines = new Defines();
        defines.define("data", Collections.singletonList("{ \"a\": \"foo\", \"b\": \"bar\" }"), false);
        defines.define("name", Collections.singletonList("a"), false);
        assertTrue(defines.isDefine("$data[$name] == \"foo\""));
        assertFalse(defines.isDefine("$data[$name] == \"bar\""));
    }

    @Test
    void testJsonFailureCases() {
        Defines defines = new Defines();
        defines.define("data", Collections.singletonList("{ \"a\": \"foo\", \"b\": \"bar\" }"), false);
        assertFalse(defines.isDefine("$data[c] == \"foo\"")); // Key does not exist
        assertFalse(defines.isDefine("$nonexistent[a] == \"foo\"")); // Variable does not exist
    }

    @Test
    void testNoRegression() {
        Defines defines = new Defines();
        defines.define("MY_VAR", Collections.emptyList(), false);
        assertTrue(defines.isDefine("MY_VAR"));
        assertFalse(defines.isDefine("!MY_VAR"));
        assertFalse(defines.isDefine("ANOTHER_VAR"));
    }
}
