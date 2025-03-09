/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
 *
 */
package net.sourceforge.plantuml.ebnf;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class ShuntingYard {

	final private List<Token> ouputQueue = new ArrayList<>();
	final private Deque<Token> operatorStack = new ArrayDeque<>();

	public ShuntingYard(Iterator<Token> it) {
		while (it.hasNext()) {
			final Token token = it.next();
//			System.err.println("token=" + token);
//			System.err.println("ouputQueue=" + ouputQueue);
//			System.err.println("operatorStack=" + operatorStack);
			if (token.getSymbol() == Symbol.LITTERAL || token.getSymbol() == Symbol.TERMINAL_STRING1
					|| token.getSymbol() == Symbol.TERMINAL_STRING2 || token.getSymbol() == Symbol.SPECIAL_SEQUENCE) {
				ouputQueue.add(token);
				while (thereIsAnCommentOnTopOfTheOperatorStack())
					ouputQueue.add(operatorStack_removeFirstAbove());

			} else if (token.getSymbol() == Symbol.COMMENT_TOKEN) {
				operatorStack.addFirst(token);

			} else if (token.getSymbol() == Symbol.NOT) {
				operatorStack.addFirst(new Token(Symbol.CONCATENATION, null));
				operatorStack.addFirst(token);
			} else if (token.getSymbol().isOperator()) {
				while (thereIsAnOperatorAtTheTopOfTheOperatorStackWithGreaterPrecedence(token)
						|| thereIsAnCommentOnTopOfTheOperatorStack())
					ouputQueue.add(operatorStack_removeFirstBelow());
				operatorStack.addFirst(token);

			} else if (token.getSymbol() == Symbol.GROUPING_OPEN) {
				operatorStack.addFirst(token);

			} else if (token.getSymbol() == Symbol.GROUPING_CLOSE) {
				while (operatorStack.peekFirst().getSymbol() != Symbol.GROUPING_OPEN)
					ouputQueue.add(operatorStack_removeFirstBelow());
				if (operatorStack.peekFirst().getSymbol() == Symbol.GROUPING_OPEN)
					operatorStack.removeFirst();

			} else if (token.getSymbol() == Symbol.OPTIONAL_OPEN) {
				operatorStack.addFirst(new Token(Symbol.OPTIONAL, null));

			} else if (token.getSymbol() == Symbol.OPTIONAL_CLOSE) {
				while (thereIsAnOperatorAtTheTopOfTheOperatorStack() || thereIsAnCommentOnTopOfTheOperatorStack())
					ouputQueue.add(operatorStack_removeFirstBelow());
				final Token first = operatorStack.removeFirst();
				if (first.getSymbol() != Symbol.OPTIONAL)
					throw new IllegalStateException();
				ouputQueue.add(first);
				while (thereIsAnCommentOnTopOfTheOperatorStack())
					ouputQueue.add(operatorStack_removeFirstAbove());

			} else if (token.getSymbol() == Symbol.REPETITION_OPEN) {
				operatorStack.addFirst(new Token(Symbol.REPETITION_ZERO_OR_MORE, null));

			} else if (token.getSymbol() == Symbol.REPETITION_CLOSE) {
				while (thereIsAnOperatorAtTheTopOfTheOperatorStack() || thereIsAnCommentOnTopOfTheOperatorStack())
					ouputQueue.add(operatorStack_removeFirstBelow());
				final Token first = operatorStack.removeFirst();
				if (first.getSymbol() != Symbol.REPETITION_ZERO_OR_MORE)
					throw new IllegalStateException();
				ouputQueue.add(first);
				while (thereIsAnCommentOnTopOfTheOperatorStack())
					ouputQueue.add(operatorStack_removeFirstAbove());

			} else if (token.getSymbol() == Symbol.REPETITION_MINUS_CLOSE) {
				while (thereIsAnOperatorAtTheTopOfTheOperatorStack() || thereIsAnCommentOnTopOfTheOperatorStack())
					ouputQueue.add(operatorStack_removeFirstBelow());
				final Token first = operatorStack.removeFirst();
				if (first.getSymbol() != Symbol.REPETITION_ZERO_OR_MORE)
					throw new IllegalStateException();
				ouputQueue.add(new Token(Symbol.REPETITION_ONE_OR_MORE, null));
				while (thereIsAnCommentOnTopOfTheOperatorStack())
					ouputQueue.add(operatorStack_removeFirstAbove());

			} else {
				throw new UnsupportedOperationException(token.toString());
			}

		}
		while (operatorStack.isEmpty() == false) {
			final Token token = operatorStack.removeFirst();
			if (token.getSymbol() == Symbol.OPTIONAL || token.getSymbol() == Symbol.REPETITION_ONE_OR_MORE
					|| token.getSymbol() == Symbol.REPETITION_ZERO_OR_MORE) {
				ouputQueue.clear();
				return;
			}
			if (token.getSymbol() == Symbol.COMMENT_TOKEN)
				ouputQueue.add(new Token(Symbol.COMMENT_BELOW, token.getData()));
			else
				ouputQueue.add(token);
		}

		// System.err.println("ouputQueue=" + ouputQueue);
	}

	private Token operatorStack_removeFirstAbove() {
		final Token result = operatorStack.removeFirst();
		if (result.getSymbol() == Symbol.COMMENT_TOKEN)
			return new Token(Symbol.COMMENT_ABOVE, result.getData());
		return result;
	}

	private Token operatorStack_removeFirstBelow() {
		final Token result = operatorStack.removeFirst();
		if (result.getSymbol() == Symbol.COMMENT_TOKEN)
			return new Token(Symbol.COMMENT_BELOW, result.getData());
		return result;
	}

	private boolean thereIsAnCommentOnTopOfTheOperatorStack() {
		final Token top = operatorStack.peekFirst();
		return top != null && top.getSymbol() == Symbol.COMMENT_TOKEN;
	}

	private boolean thereIsAFunctionAtTheTopOfTheOperatorStack() {
		final Token top = operatorStack.peekFirst();
		return top != null && top.getSymbol().isFunction();
	}

	private boolean thereIsAnOperatorAtTheTopOfTheOperatorStack() {
		final Token top = operatorStack.peekFirst();
		return top != null && top.getSymbol().isOperator();
	}

	private boolean thereIsAnOperatorAtTheTopOfTheOperatorStackWithGreaterPrecedence(Token token) {
		final Token top = operatorStack.peekFirst();
		if (top != null && top.getSymbol().isOperator()
				&& top.getSymbol().getPriority() > token.getSymbol().getPriority())
			return true;
		return false;
	}

	public final List<Token> getOuputQueue() {
		return Collections.unmodifiableList(ouputQueue);
	}

}
