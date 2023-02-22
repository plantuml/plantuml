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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FloatingNote;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.utils.CharInspector;

public class EbnfExpression implements TextBlockable {

	private final List<Token> tokens = new ArrayList<>();
	private final boolean isCompact;
	private final String commentAbove;
	private final String commentBelow;

	public static EbnfExpression create(CharInspector it, boolean isCompact, String commentAbove, String commentBelow) {
		return new EbnfExpression(it, isCompact, commentAbove, commentBelow);
	}

	private EbnfExpression(CharInspector it, boolean isCompact, String commentAbove, String commentBelow) {
		this.isCompact = isCompact;
		this.commentAbove = commentAbove;
		this.commentBelow = commentBelow;
		while (true) {
			final char ch = it.peek(0);
			if (Character.isWhitespace(ch)) {
			} else if (isLetterOrDigit(ch)) {
				final String litteral = readLitteral(it);
				tokens.add(new Token(Symbol.LITTERAL, litteral));
				continue;
			} else if (ch == '*') {
				tokens.add(new Token(Symbol.REPETITION_SYMBOL, null));
			} else if (ch == '(' && it.peek(1) == '*') {
				final String comment = readComment(it);
				if (comment.trim().length() > 0)
					tokens.add(new Token(Symbol.COMMENT_TOKEN, comment));
				continue;
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
			} else if (ch == '}' && it.peek(1) == '-') {
				tokens.add(new Token(Symbol.REPETITION_MINUS_CLOSE, null));
				it.jump();
			} else if (ch == '}') {
				tokens.add(new Token(Symbol.REPETITION_CLOSE, null));
			} else if (ch == ';' || ch == 0) {
				// it.next();
				break;
			} else if (ch == '\"') {
				final String litteral = readString(it);
				tokens.add(new Token(Symbol.TERMINAL_STRING1, protect(litteral)));
			} else if (ch == '\'') {
				final String litteral = readString(it);
				tokens.add(new Token(Symbol.TERMINAL_STRING2, protect(litteral)));
			} else if (ch == '?') {
				final String litteral = readString(it);
				tokens.add(new Token(Symbol.SPECIAL_SEQUENCE, protect(litteral)));
			} else {
				tokens.clear();
				return;
			}
			it.jump();
			continue;
		}
	}

	private static String protect(final String litteral) {
		return litteral.length() == 0 ? " " : litteral;
	}

	public TextBlock getUDrawable(ISkinParam skinParam) {
		final Style style = ETile.getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		final FontConfiguration fc = style.getFontConfiguration(skinParam.getIHtmlColorSet());

		if (tokens.size() == 0)
			return EbnfEngine.syntaxError(fc, skinParam);

		try {
			final Iterator<Token> iterator = tokens.iterator();
			final Token name = iterator.next();
			final Token definition = iterator.next();
			if (definition.getSymbol() != Symbol.DEFINITION)
				return EbnfEngine.syntaxError(fc, skinParam);

			final TextBlock main;
			if (iterator.hasNext()) {
				final List<Token> full = new ShuntingYard(iterator).getOuputQueue();
				if (full.size() == 0)
					return EbnfEngine.syntaxError(fc, skinParam);

				main = getMainDrawing(skinParam, full.iterator());
			} else {
				final HColor lineColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
				main = new ETileWithCircles(new ETileEmpty(), lineColor);
			}

			TextBlock titleBox = new TitleBox(name.getData(), fc);
			if (commentAbove != null)
				titleBox = TextBlockUtils.mergeTB(getNoteAbove(skinParam), titleBox, HorizontalAlignment.CENTER);
			if (commentBelow != null)
				titleBox = TextBlockUtils.mergeTB(titleBox, getNoteBelow(skinParam), HorizontalAlignment.CENTER);

			return TextBlockUtils.mergeTB(titleBox, TextBlockUtils.withMargin(main, 0, 0, 10, 15),
					HorizontalAlignment.LEFT);
		} catch (Exception e) {
			e.printStackTrace();
			return EbnfEngine.syntaxError(fc, skinParam);
		}
	}

	private TextBlock getNoteAbove(ISkinParam skinParam) {
		if (commentAbove == null)
			return null;
		final FloatingNote note = FloatingNote.create(Display.getWithNewlines(commentAbove), skinParam, SName.ebnf);
		return note;
	}

	private TextBlock getNoteBelow(ISkinParam skinParam) {
		if (commentBelow == null)
			return null;
		final FloatingNote note = FloatingNote.create(Display.getWithNewlines(commentBelow), skinParam, SName.ebnf);
		return note;
	}

	private TextBlock getMainDrawing(ISkinParam skinParam, Iterator<Token> it) {
		final EbnfEngine engine = new EbnfEngine(skinParam);
		while (it.hasNext()) {
			final Token element = it.next();
			if (element.getSymbol() == Symbol.TERMINAL_STRING1 || element.getSymbol() == Symbol.TERMINAL_STRING2
					|| element.getSymbol() == Symbol.LITTERAL || element.getSymbol() == Symbol.SPECIAL_SEQUENCE)
				engine.push(element);
			else if (element.getSymbol() == Symbol.COMMENT_ABOVE)
				engine.commentAbove(element.getData());
			else if (element.getSymbol() == Symbol.COMMENT_BELOW)
				engine.commentBelow(element.getData());
			else if (element.getSymbol() == Symbol.ALTERNATION)
				engine.alternation();
			else if (element.getSymbol() == Symbol.CONCATENATION)
				engine.concatenation();
			else if (element.getSymbol() == Symbol.OPTIONAL)
				engine.optional();
			else if (element.getSymbol() == Symbol.REPETITION_ZERO_OR_MORE)
				engine.repetitionZeroOrMore(isCompact);
			else if (element.getSymbol() == Symbol.REPETITION_ONE_OR_MORE)
				engine.repetitionOneOrMore();
			else if (element.getSymbol() == Symbol.REPETITION_SYMBOL)
				engine.repetitionSymbol();
			else
				throw new UnsupportedOperationException(element.toString());
		}

		return engine.getTextBlock();
	}

	private String readString(CharInspector it) {
		final char separator = it.peek(0);
		it.jump();
		final StringBuilder sb = new StringBuilder();
		while (true) {
			final char ch = it.peek(0);
			if (ch == separator)
				return sb.toString();
			sb.append(ch);
			it.jump();
		}
	}

	private String readLitteral(CharInspector it) {
		final StringBuilder sb = new StringBuilder();
		while (true) {
			final char ch = it.peek(0);
			if (isLetterOrDigit(ch) == false)
				return sb.toString();
			sb.append(ch);
			it.jump();
		}
	}

	private String readComment(CharInspector it) {
		final StringBuilder sb = new StringBuilder();
		it.jump();
		it.jump();
		while (true) {
			final char ch = it.peek(0);
			if (ch == '\0')
				return sb.toString();
			if (ch == '*' && it.peek(1) == ')') {
				it.jump();
				it.jump();
				return sb.toString();
			}
			sb.append(ch);
			it.jump();
		}
	}

	private boolean isLetterOrDigit(char ch) {
		return ch == '-' || ch == '_' || Character.isLetterOrDigit(ch);
	}

	public boolean isEmpty() {
		return tokens.size() == 0;
	}

}
