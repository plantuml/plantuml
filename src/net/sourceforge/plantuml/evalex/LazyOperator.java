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

import net.sourceforge.plantuml.evalex.Expression.LazyNumber;

/**
 * Base interface which is required for all operators.
 */
public interface LazyOperator {

	/**
	 * Gets the String that is used to denote the operator in the expression.
	 * 
	 * @return The String that is used to denote the operator in the expression.
	 */
	public abstract String getOper();

	/**
	 * Gets the precedence value of this operator.
	 * 
	 * @return the precedence value of this operator.
	 */
	public abstract int getPrecedence();

	/**
	 * Gets whether this operator is left associative (<code>true</code>) or if
	 * this operator is right associative (<code>false</code>).
	 * 
	 * @return <code>true</code> if this operator is left associative.
	 */
	public abstract boolean isLeftAssoc();
	
	/**
	 * Gets whether this operator evaluates to a boolean expression.
	 * @return <code>true</code> if this operator evaluates to a boolean
	 *         expression.
	 */
	public abstract boolean isBooleanOperator();

	/**
	 * Implementation for this operator.
	 * 
	 * @param v1
	 *            Operand 1.
	 * @param v2
	 *            Operand 2.
	 * @return The result of the operation.
	 */
	public abstract LazyNumber eval(LazyNumber v1, LazyNumber v2);
}
