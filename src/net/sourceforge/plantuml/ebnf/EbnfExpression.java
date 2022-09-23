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
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.UDrawable;

interface CharIterator {
	char peek();

	void next();
}

class CharIteratorImpl implements CharIterator {

	final private List<String> data;
	private int line = 0;
	private int pos = 0;

	public CharIteratorImpl(List<String> data) {
		this.data = data;
	}

	public char peek() {
		if (line == -1)
			return 0;
		return data.get(line).charAt(pos);
	}

	public void next() {
		if (line == -1)
			throw new IllegalStateException();
		pos++;
		if (pos >= data.get(line).length()) {
			line++;
			pos = 0;
		}
		if (line >= data.size())
			line = -1;
	}
}

public class EbnfExpression {

	final List<Token> tokens = new ArrayList<>();

	public EbnfExpression(String... data) {
		this(Arrays.asList(data));
	}

	public EbnfExpression(List<String> data) {
		final CharIterator it = new CharIteratorImpl(data);
		analyze(it);
	}

	public UDrawable getUDrawable(ISkinParam skinParam) {
		final Iterator<Token> iterator = tokens.iterator();
		final Token name = iterator.next();
		final Token definition = iterator.next();
		final ShuntingYard shuntingYard = new ShuntingYard(iterator);

		return build(shuntingYard.getOuputQueue(), skinParam);

	}

	private Alternation build(Iterator<Token> it, ISkinParam skinParam) {
		final Deque<Token> stack = new ArrayDeque<>();
		final Alternation ebnf = new Alternation(skinParam);
		while (it.hasNext()) {
			final Token element = it.next();
			if (element.getSymbol() == Symbol.TERMINAL_STRING1 || element.getSymbol() == Symbol.LITTERAL) {
				stack.addFirst(element);
			} else if (element.getSymbol() == Symbol.ALTERNATION) {
				ebnf.alternation(stack.removeFirst());
			} else {
				throw new UnsupportedOperationException(element.toString());
			}
		}
		ebnf.alternation(stack.removeFirst());

		return ebnf;
	}

	private void analyze(CharIterator it) {
		while (true) {
			final char ch = it.peek();
			if (Character.isWhitespace(ch)) {
			} else if (isLetterOrDigit(ch)) {
				final String litteral = readLitteral(it);
				tokens.add(new Token(Symbol.LITTERAL, litteral));
			} else if (ch == '|') {
				tokens.add(new Token(Symbol.ALTERNATION, null));
			} else if (ch == '=') {
				tokens.add(new Token(Symbol.DEFINITION, null));
			} else if (ch == ';' || ch == 0) {
				break;
			} else if (ch == '\"') {
				final String litteral = readString(it);
				tokens.add(new Token(Symbol.TERMINAL_STRING1, litteral));
			} else
				throw new UnsupportedOperationException("" + ch);
			it.next();
			continue;
		}

	}

	private String readString(CharIterator it) {
		final char separator = it.peek();
		it.next();
		final StringBuilder sb = new StringBuilder();
		while (true) {
			final char ch = it.peek();
			if (ch == separator)
				return sb.toString();
			sb.append(ch);
			it.next();
		}
	}

	private String readLitteral(CharIterator it) {
		final StringBuilder sb = new StringBuilder();
		while (true) {
			final char ch = it.peek();
			if (isLetterOrDigit(ch) == false)
				return sb.toString();
			sb.append(ch);
			it.next();
		}
	}

	private boolean isLetterOrDigit(char ch) {
		return ch == '-' || ch == '_' || Character.isLetterOrDigit(ch);
	}

}
