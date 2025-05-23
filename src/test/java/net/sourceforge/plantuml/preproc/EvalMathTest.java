package net.sourceforge.plantuml.preproc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link EvalMath} class.
 * This class verifies the correctness of mathematical expression evaluation,
 * including basic operations, operator precedence, unary operators, and error handling.
 */
public class EvalMathTest {

	private static final double DELTA = 0.0001;

	@ParameterizedTest
	@CsvSource({"'2+3', 5.0", "'2-3', -1.0", "'2*3', 6.0", "'6/3', 2.0"})
	void testBasicOperations(String expression, double expected) {
		assertEquals(expected, new EvalMath(expression).eval(), DELTA);
	}

	@ParameterizedTest
	@CsvSource({"'33+2*(4+1)', 43.0", "'3+4*2', 11.0", "'(3+4)*2', 14.0", "'3+8/ 2', 7.0"})
	//Parentheses, Exponents, Multiplication and Division and Addition and Subtraction. In that order.
	void testPEMDAS(String expression, double expected) {
		assertEquals(expected, new EvalMath(expression).eval(), DELTA);
	}

	@ParameterizedTest
	@CsvSource({"'-5', -5.0", "'+5', 5.0", "'-(2-10)', 8.0"})
	void testUnaryOperators(String expression, double expected) {
		assertEquals(expected, new EvalMath(expression).eval(), DELTA);
	}

	@ParameterizedTest
	@CsvSource({"'3.14', 3.14", "'2.12+2.12', 4.24"})
	void testDecimalNumbers(String expression, double expected) {
		assertEquals(expected, new EvalMath(expression).eval(), DELTA);
	}

	@Test
	public void testInvalidCharacters() {
		assertThrows(RuntimeException.class, () -> new EvalMath("2+@3").eval());
	}

	@Test
	public void testMainExpression() {
		//Keeping the legacy of the original testing example
		assertEquals(43.0, new EvalMath("33+2*(4+1)").eval(), DELTA);
	}
}
