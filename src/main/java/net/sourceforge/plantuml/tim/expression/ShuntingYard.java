/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 */
package net.sourceforge.plantuml.tim.expression;

import java.util.ArrayDeque;
import java.util.Deque;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;

// https://en.wikipedia.org/wiki/Shunting-yard_algorithm
// https://en.cppreference.com/w/c/language/operator_precedence
public class ShuntingYard {

	final private TokenStack ouputQueue = new TokenStack();
	final private Deque<Token> operatorStack = new ArrayDeque<>();

	private static final boolean TRACE = false;

	private void traceMe() {
		if (TRACE == false)
			return;
		System.err.println("-------------------");
		System.err.println("operatorStack=" + operatorStack);
		System.err.println("ouputQueue=" + ouputQueue);
		System.err.println("");
	}

	public ShuntingYard(TokenIterator it, Knowledge knowledge, StringLocated location) throws EaterException {

		while (it.hasMoreTokens()) {
			final Token token = it.nextToken();
			traceMe();
			if (TRACE)
				System.err.println("token=" + token);
			if (token.getTokenType() == TokenType.NUMBER || token.getTokenType() == TokenType.QUOTED_STRING) {
				ouputQueue.add(token);
			} else if (token.getTokenType() == TokenType.FUNCTION_NAME) {
				operatorStack.addFirst(token);
			} else if (token.getTokenType() == TokenType.PLAIN_TEXT) {
				final String name = token.getSurface();
				final TValue variable = knowledge.getVariable(name);
				if (variable == null) {
					if (isVariableName(name) == false)
						throw new EaterException("Parsing syntax error about " + name, location);

					ouputQueue.add(new Token(name, TokenType.QUOTED_STRING, null));
				} else {
					ouputQueue.add(variable.toToken());
				}
			} else if (isOperatorOrAffectation(token)) {
				while ((thereIsAFunctionAtTheTopOfTheOperatorStack() //
						|| thereIsAnOperatorAtTheTopOfTheOperatorStackWithGreaterPrecedence(token) //
						|| theOperatorAtTheTopOfTheOperatorStackHasEqualPrecedenceAndIsLeftAssociative(token)) //
						&& theOperatorAtTheTopOfTheOperatorStackIsNotALeftParenthesis(token))
					ouputQueue.add(operatorStack.removeFirst());

				// push it onto the operator stack.
				operatorStack.addFirst(token);
			} else if (token.getTokenType() == TokenType.OPEN_PAREN_FUNC) {
				operatorStack.addFirst(token);
			} else if (token.getTokenType() == TokenType.OPEN_PAREN_MATH) {
				operatorStack.addFirst(token);
			} else if (token.getTokenType() == TokenType.CLOSE_PAREN_FUNC) {
				while (operatorStack.peekFirst() != null
						&& operatorStack.peekFirst().getTokenType() != TokenType.OPEN_PAREN_FUNC)
					ouputQueue.add(operatorStack.removeFirst());
				final Token first = operatorStack.removeFirst();
				ouputQueue.add(first);
			} else if (token.getTokenType() == TokenType.CLOSE_PAREN_MATH) {
				while (operatorStack.peekFirst().getTokenType() != TokenType.OPEN_PAREN_MATH)
					ouputQueue.add(operatorStack.removeFirst());

				if (operatorStack.peekFirst().getTokenType() == TokenType.OPEN_PAREN_MATH)
					operatorStack.removeFirst();

			} else if (token.getTokenType() == TokenType.COMMA) {
				while (operatorStack.peekFirst() != null
						&& operatorStack.peekFirst().getTokenType() != TokenType.OPEN_PAREN_FUNC)
					ouputQueue.add(operatorStack.removeFirst());

			} else {
				throw new UnsupportedOperationException(token.toString());
			}
		}

		while (operatorStack.isEmpty() == false) {
			final Token token = operatorStack.removeFirst();
			ouputQueue.add(token);
		}

		// System.err.println("ouputQueue=" + ouputQueue);
	}

	private boolean isVariableName(String name) {
		return name.matches("[a-zA-Z0-9.$_]+");
	}

	private boolean thereIsAFunctionAtTheTopOfTheOperatorStack() {
		final Token top = operatorStack.peekFirst();
		return top != null && top.getTokenType() == TokenType.FUNCTION_NAME;
	}

	private boolean thereIsAnOperatorAtTheTopOfTheOperatorStackWithGreaterPrecedence(Token token) {
		final Token top = operatorStack.peekFirst();
		if (top != null && isOperatorOrAffectation(top) && top.getPrecedence() > token.getPrecedence())
			return true;

		return false;
	}

	private boolean theOperatorAtTheTopOfTheOperatorStackHasEqualPrecedenceAndIsLeftAssociative(Token token) {
		final Token top = operatorStack.peekFirst();
		if (top != null && isOperatorOrAffectation(top) && top.getLeftAssociativity()
				&& top.getPrecedence() == token.getPrecedence())
			return true;

		return false;
	}

	private boolean isOperatorOrAffectation(final Token token) {
		return token.getTokenType() == TokenType.OPERATOR || token.getTokenType() == TokenType.AFFECTATION;
	}

	private boolean theOperatorAtTheTopOfTheOperatorStackIsNotALeftParenthesis(Token token) {
		final Token top = operatorStack.peekFirst();
		if (top != null && top.getTokenType() == TokenType.OPEN_PAREN_MATH)
			return true;

		return true;
	}

	public TokenStack getQueue() {
		return this.ouputQueue;
	}

}
