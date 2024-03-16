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
package net.sourceforge.plantuml.regexdiagram;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class ShuntingYard {

	final private List<ReToken> ouputQueue = new ArrayList<>();
	final private Deque<ReToken> operatorStack = new ArrayDeque<>();

	public ShuntingYard(Iterator<ReToken> it) {
		while (it.hasNext()) {
			final ReToken token = it.next();
//			System.err.println("token=" + token);
//			System.err.println("ouputQueue=" + ouputQueue);
//			System.err.println("operatorStack=" + operatorStack);
			if (token.getType() == ReTokenType.SIMPLE_CHAR) {
				ouputQueue.add(token);
			} else if (token.getType() == ReTokenType.ESCAPED_CHAR) {
				ouputQueue.add(token);
			} else if (token.getType() == ReTokenType.GROUP) {
				ouputQueue.add(token);
			} else if (token.getType().isNamedGroupOrLookAheadOrLookBehind()) {
				operatorStack.addFirst(token);
			} else if (token.getType() == ReTokenType.CLASS) {
				ouputQueue.add(token);
			} else if (token.getType() == ReTokenType.ANCHOR) {
				ouputQueue.add(token);
			} else if (token.getType() == ReTokenType.QUANTIFIER) {
				ouputQueue.add(token);
			} else if (token.getType() == ReTokenType.CONCATENATION_IMPLICIT) {
				// push it onto the operator stack.
				operatorStack.addFirst(token);
			} else if (token.getType() == ReTokenType.ALTERNATIVE) {
				while (thereIsAConcatenationAtTheTopOfTheOperatorStack())
					ouputQueue.add(operatorStack.removeFirst());
				// push it onto the operator stack.
				operatorStack.addFirst(token);
			} else if (token.getType() == ReTokenType.PARENTHESIS_OPEN) {
				operatorStack.addFirst(token);
			} else if (token.getType() == ReTokenType.PARENTHESIS_CLOSE) {
				while (operatorStack.peekFirst() != null
						&& operatorStack.peekFirst().getType() != ReTokenType.PARENTHESIS_OPEN)
					ouputQueue.add(operatorStack.removeFirst());
				final ReToken first = operatorStack.removeFirst();
				if (operatorStack.peekFirst() != null && operatorStack.peekFirst().getType().isNamedGroupOrLookAheadOrLookBehind())
					ouputQueue.add(operatorStack.removeFirst());

			} else {
				throw new UnsupportedOperationException(token.toString());
			}

		}

		while (operatorStack.isEmpty() == false) {
			final ReToken token = operatorStack.removeFirst();
			ouputQueue.add(token);
		}

		// System.err.println("ouputQueue=" + ouputQueue);
	}

	private boolean thereIsAConcatenationAtTheTopOfTheOperatorStack() {
		final ReToken top = operatorStack.peekFirst();
		return top != null && top.getType() == ReTokenType.CONCATENATION_IMPLICIT;
	}

	public final List<ReToken> getOuputQueue() {
		return Collections.unmodifiableList(ouputQueue);
	}

}
