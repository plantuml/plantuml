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
package net.sourceforge.plantuml.preproc;

// http://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form
public class EvalBoolean {

	private final String str;
	private int pos = -1;
	private char ch;
	private final Truth truth;

	public EvalBoolean(String str, Truth truth) {
		this.str = str;
		this.truth = truth;
	}

	private void nextChar() {
		pos++;
		if (pos < str.length()) {
			ch = str.charAt(pos);
		} else {
			ch = '\0';
		}
	}

	private boolean eat(char charToEat) {
		while (ch == ' ') {
			nextChar();
		}
		if (ch == charToEat) {
			nextChar();
			return true;
		}
		return false;
	}

	private boolean parseExpression() {
		boolean x = parseTerm();
		while (true) {
			if (eat('|')) {
				eat('|');
				x = x | parseTerm();
			} else {
				return x;
			}
		}
	}

	private boolean parseTerm() {
		boolean x = parseFactor();
		while (true) {
			if (eat('&')) {
				eat('&');
				x = x & parseFactor();
			} else {
				return x;
			}
		}
	}

	private boolean parseFactor() {
		if (eat('!')) {
			return !(parseFactor());
		}

		final boolean x;
		final int startPos = pos;
		if (eat('(')) {
			x = parseExpression();
			eat(')');
		} else if (isIdentifier()) {
			while (isIdentifier()) {
				nextChar();
			}
			final String func = str.substring(startPos, pos);
			x = truth.isTrue(func);
		} else {
			throw new IllegalArgumentException("Unexpected: " + (char) ch);
		}

		return x;
	}

	private boolean isIdentifier() {
		return ch == '_' || ch == '$' || Character.isLetterOrDigit(ch);
	}

	public boolean eval() {
		nextChar();
		final boolean x = parseExpression();
		if (pos < str.length()) {
			throw new IllegalArgumentException("Unexpected: " + (char) ch);
		}
		return x;
	}
}
