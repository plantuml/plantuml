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

import java.util.List;

import net.sourceforge.plantuml.evalex.Expression.LazyNumber;

/**
 * Base interface which is required for lazily evaluated functions. A function
 * is defined by a name, a number of parameters it accepts and of course
 * the logic for evaluating the result.
 */
public interface LazyFunction {

	/**
	 * Gets the name of this function.<br>
	 * <br>
	 * The name is use to invoke this function in the expression.
	 * 
	 * @return The name of this function.
	 */
	public abstract String getName();

	/**
	 * Gets the number of parameters this function accepts.<br>
	 * <br>
	 * A value of <code>-1</code> denotes that this function accepts a variable
	 * number of parameters.
	 * 
	 * @return The number of parameters this function accepts.
	 */
	public abstract int getNumParams();

	/**
	 * Gets whether the number of accepted parameters varies.<br>
	 * <br>
	 * That means that the function does accept an undefined amount of
	 * parameters.
	 * 
	 * @return <code>true</code> if the number of accepted parameters varies.
	 */
	public abstract boolean numParamsVaries();

	/**
	 * Gets whether this function evaluates to a boolean expression.
	 * 
	 * @return <code>true</code> if this function evaluates to a boolean
	 *         expression.
	 */
	public abstract boolean isBooleanFunction();

	/**
	 * Lazily evaluate this function.
	 * 
	 * @param lazyParams
	 *            The accepted parameters.
	 * @return The lazy result of this function.
	 */
	public abstract LazyNumber lazyEval(List<LazyNumber> lazyParams);
}
