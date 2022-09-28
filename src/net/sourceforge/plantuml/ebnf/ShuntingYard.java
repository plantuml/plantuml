/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
			if (token.getSymbol() == Symbol.LITTERAL || token.getSymbol() == Symbol.TERMINAL_STRING1
					|| token.getSymbol() == Symbol.TERMINAL_STRING2) {
				ouputQueue.add(token);
			} else if (token.getSymbol().isOperator()) {
				while (thereIsAnOperatorAtTheTopOfTheOperatorStackWithGreaterPrecedence(token))
					ouputQueue.add(operatorStack.removeFirst());
				operatorStack.addFirst(token);
			} else if (token.getSymbol() == Symbol.GROUPING_OPEN) {
				operatorStack.addFirst(token);
			} else if (token.getSymbol() == Symbol.GROUPING_CLOSE) {
				while (operatorStack.peekFirst().getSymbol() != Symbol.GROUPING_OPEN)
					ouputQueue.add(operatorStack.removeFirst());
				if (operatorStack.peekFirst().getSymbol() == Symbol.GROUPING_OPEN)
					operatorStack.removeFirst();
			} else if (token.getSymbol() == Symbol.OPTIONAL_OPEN) {
				operatorStack.addFirst(new Token(Symbol.OPTIONAL, null));
			} else if (token.getSymbol() == Symbol.OPTIONAL_CLOSE) {
				final Token first = operatorStack.removeFirst();
				ouputQueue.add(first);
			} else if (token.getSymbol() == Symbol.REPETITION_OPEN) {
				operatorStack.addFirst(new Token(Symbol.REPETITION, null));
			} else if (token.getSymbol() == Symbol.REPETITION_CLOSE) {
				final Token first = operatorStack.removeFirst();
				ouputQueue.add(first);
			} else {
				throw new UnsupportedOperationException(token.toString());
			}

		}
		while (operatorStack.isEmpty() == false) {
			final Token token = operatorStack.removeFirst();
			ouputQueue.add(token);
		}
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
