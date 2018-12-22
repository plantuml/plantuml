/*
 * Copyright 2018 Udo Klimaschewski
 * 
 * http://UdoJava.com/
 * http://about.me/udo.klimaschewski
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package net.sourceforge.plantuml.evalex;

import net.sourceforge.plantuml.evalex.Expression.ExpressionException;
import net.sourceforge.plantuml.evalex.Expression.LazyNumber;

/**
 * Abstract implementation of an unary operator.<br>
 * <br>
 * This abstract implementation implements eval so that it forwards its first
 * parameter to evalUnary.
 */
public abstract class AbstractUnaryOperator extends AbstractOperator {

	/**
	 * Creates a new operator.
	 * 
	 * @param oper
	 *            The operator name (pattern).
	 * @param precedence
	 *            The operators precedence.
	 * @param leftAssoc
	 *            <code>true</code> if the operator is left associative,
	 *            else <code>false</code>.
	 */
	protected AbstractUnaryOperator(String oper, int precedence, boolean leftAssoc) {
		super(oper, precedence, leftAssoc);
	}

	public LazyNumber eval(final LazyNumber v1, final LazyNumber v2) {
		if (v2 != null) {
			throw new ExpressionException("Did not expect a second parameter for unary operator");
		}
		return new LazyNumber() {
			public String getString() {
				return String.valueOf(AbstractUnaryOperator.this.evalUnary(v1.eval()));
			}
			
			public Number eval() {
				return AbstractUnaryOperator.this.evalUnary(v1.eval());
			}
		};
	}

	public Number eval(Number v1, Number v2) {
		if (v2 != null) {
			throw new ExpressionException("Did not expect a second parameter for unary operator");
		}
		return evalUnary(v1);
	}

	/**
	 * Implementation of this unary operator.
	 * 
	 * @param v1
	 *            The parameter.
	 * @return The result of the operation.
	 */
	public abstract Number evalUnary(Number v1);
}
