package net.sourceforge.plantuml.preproc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EvalBooleanTest {
    
    private static final Truth TEST_TRUTH = new Truth() {
        @Override
        public boolean isTrue(String func) {
            switch (func) {
                case "A":
                case "C":
                    return true;
                case "B":
                case "D":
                    return false;
                default:
                    return false;
            }
        }
    };

    @ParameterizedTest
    @CsvSource({
        // Basic variables
        "A, true",
        "B, false",
        "C, true",
        "D, false",
        
        // NOT operator
        "!A, false",
        "!B, true",
        "!C, false",
        "!D, true",
        
        // AND operator
        "A && A, true", // true && true = true
        "A && B, false", // true && false = false
        "B && A, false", // false && true = false
        "B && B, false", // false && false = false
        "C && C, true", // true && true = true
        "C && D, false", // true && false = false
        
        // OR operator
        "A || A, true", // true || true = true
        "A || B, true", // true || false = true
        "B || A, true", // false || true = true
        "B || B, false", // false || false = false
        "C || D, true", // true || false = true
        "D || D, false", // false || false = false
        
        // Combined operators
        "!A && B, false", // (false && false) = false
        "!A || B, false", // (false || false) = false
        "A && !B, true", // (true && true) = true
        "A || !B, true", // (true || true) = true
        "A && B || C, true",   // (true && false) || true = true
        "A || B && C, true",   // true || (false && true) = true
        "A && (B || C), true",  // true && (false || true) = true
        "(A || B) && C, true",  // (true || false) && true = true
        "!(A && C), false",     // !(true && true) = false
        "!A || !B, true",       // false || true = true
        "!(A || B) && C, false",// !(true || false) && true = false
        
        // Operator precedence
        "A || B && C, true",    // true || (false && true) = true
        "A && B || C, true",    // (true && false) || true = true
        "A && (B || C), true",  // true && (false || true) = true
        
        // Formatting and spaces
        "  A  , true", // spaces around variable
        "  !  A  , false", // spaces around operator
        "A  &&  B  , false", // spaces around operator
        "(  A  )  , true", // spaces around parentheses
        "  (  A  ||  B  )  &&  C  , true" // spaces around operator and parentheses
    })
    void testBooleanExpressions(String expression, boolean expected) {
        assertEquals(expected, new EvalBoolean(expression, TEST_TRUTH).eval());
    }

    @Test
    void testIdentifierWithSpecialCharacters() {
        Truth customTruth = func -> func.equals("var_1") || func.equals("$var");
        assertTrue(new EvalBoolean("var_1", customTruth).eval());
        assertTrue(new EvalBoolean("$var", customTruth).eval());
        assertFalse(new EvalBoolean("!var_1", customTruth).eval());
    }

    @Test
    void testUnknownIdentifier() {
        assertFalse(new EvalBoolean("UNKNOWN", TEST_TRUTH).eval());
    }

    @Test
    void testEmptyExpression() {
        assertThrows(IllegalArgumentException.class, () -> 
            new EvalBoolean("", TEST_TRUTH).eval()
        );
    }

    @Test
    void testUnopenedParenthesis() {
        assertThrows(IllegalArgumentException.class, () -> 
            new EvalBoolean("A && B)", TEST_TRUTH).eval()
        );
    }

    @Test
    void testInvalidCharacter() {
        assertThrows(IllegalArgumentException.class, () -> 
            new EvalBoolean("A &@ B", TEST_TRUTH).eval()
        );
    }

    @Test
    void testComplexNestedExpression() {
        assertFalse(new EvalBoolean("!(!(A && B) && (C || D))", TEST_TRUTH).eval());
        assertTrue(new EvalBoolean("!((A || C) && (B || D))", TEST_TRUTH).eval());
    }
}