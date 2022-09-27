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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class EbnfSingleExpression {

	final List<Token> tokens = new ArrayList<>();

	EbnfSingleExpression(CharIterator it) {
		while (true) {
			final char ch = it.peek();
			if (Character.isWhitespace(ch)) {
			} else if (isLetterOrDigit(ch)) {
				final String litteral = readLitteral(it);
				tokens.add(new Token(Symbol.LITTERAL, litteral));
			} else if (ch == ',') {
				tokens.add(new Token(Symbol.CONCATENATION, null));
			} else if (ch == '|') {
				tokens.add(new Token(Symbol.ALTERNATION, null));
			} else if (ch == '=') {
				tokens.add(new Token(Symbol.DEFINITION, null));
			} else if (ch == '(') {
				tokens.add(new Token(Symbol.GROUPING_OPEN, null));
			} else if (ch == ')') {
				tokens.add(new Token(Symbol.GROUPING_CLOSE, null));
			} else if (ch == '[') {
				tokens.add(new Token(Symbol.OPTIONAL_OPEN, null));
			} else if (ch == ']') {
				tokens.add(new Token(Symbol.OPTIONAL_CLOSE, null));
			} else if (ch == '{') {
				tokens.add(new Token(Symbol.REPETITION_OPEN, null));
			} else if (ch == '}') {
				tokens.add(new Token(Symbol.REPETITION_CLOSE, null));
			} else if (ch == ';' || ch == 0) {
				it.next();
				break;
			} else if (ch == '\"') {
				final String litteral = readString(it);
				tokens.add(new Token(Symbol.TERMINAL_STRING1, litteral));
			} else if (ch == '\'') {
				final String litteral = readString(it);
				tokens.add(new Token(Symbol.TERMINAL_STRING2, litteral));
			} else
				throw new UnsupportedOperationException("" + ch);
			it.next();
			continue;
		}
	}

	private StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

	public TextBlock getUDrawable(ISkinParam skinParam) {
		final Iterator<Token> iterator = tokens.iterator();
		final Token name = iterator.next();
		final Token definition = iterator.next();
		final List<Token> full = new ShuntingYard(iterator).getOuputQueue();

		final TextBlock main = getMainDrawing(skinParam, full.iterator());

		final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		final FontConfiguration fc = style.getFontConfiguration(skinParam.getIHtmlColorSet());

		final TitleBox titleBox = new TitleBox(name.getData() + ":", fc);

		return TextBlockUtils.mergeTB(titleBox, TextBlockUtils.withMargin(main, 0, 0, 10, 15),
				HorizontalAlignment.LEFT);
	}

	private TextBlock getMainDrawing(ISkinParam skinParam, Iterator<Token> it) {
		final EbnfEngine engine = new EbnfEngine(skinParam);
		while (it.hasNext()) {
			final Token element = it.next();
			if (element.getSymbol() == Symbol.TERMINAL_STRING1 || element.getSymbol() == Symbol.TERMINAL_STRING2
					|| element.getSymbol() == Symbol.LITTERAL)
				engine.push(element);
			else if (element.getSymbol() == Symbol.ALTERNATION)
				engine.alternation();
			else if (element.getSymbol() == Symbol.CONCATENATION)
				engine.concatenation();
			else if (element.getSymbol() == Symbol.OPTIONAL_CLOSE)
				engine.optional();
			else if (element.getSymbol() == Symbol.REPETITION_CLOSE)
				engine.repetition();
			else
				throw new UnsupportedOperationException(element.toString());
		}

		return engine.getTextBlock();
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
		while (line < data.size() && data.get(line).length() == 0)
			line++;
		if (line >= data.size())
			line = -1;
	}
}
