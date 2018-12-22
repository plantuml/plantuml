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

/**
 * Abstract implementation of an operator.
 */
public abstract class AbstractLazyOperator implements LazyOperator {
	/**
	 * This operators name (pattern).
	 */
	protected String oper;
	/**
	 * Operators precedence.
	 */
	protected int precedence;
	/**
	 * Operator is left associative.
	 */
	protected boolean leftAssoc;
	/**
	 * Whether this operator is boolean or not.
	 */
	protected boolean booleanOperator = false;

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
	 * @param booleanOperator
	 *            Whether this operator is boolean.
	 */
	protected AbstractLazyOperator(String oper, int precedence, boolean leftAssoc, boolean booleanOperator) {
		this.oper = oper;
		this.precedence = precedence;
		this.leftAssoc = leftAssoc;
		this.booleanOperator = booleanOperator;
	}

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
	protected AbstractLazyOperator(String oper, int precedence, boolean leftAssoc) {
		this.oper = oper;
		this.precedence = precedence;
		this.leftAssoc = leftAssoc;
	}

	public String getOper() {
		return oper;
	}

	public int getPrecedence() {
		return precedence;
	}

	public boolean isLeftAssoc() {
		return leftAssoc;
	}

	public boolean isBooleanOperator() {
		return booleanOperator;
	}
}
