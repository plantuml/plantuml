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
import java.util.Deque;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.ISkinSimple;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class EbnfEngine {

	private final Deque<ETile> stack = new ArrayDeque<>();
	private final FontConfiguration fontConfiguration;
	private final Style style;
	private final HColorSet colorSet;
	private final ISkinParam skinParam;
	private final HColor lineColor;

	public EbnfEngine(ISkinParam skinParam) {
		this.skinParam = skinParam;
		this.style = ETile.getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		this.fontConfiguration = style.getFontConfiguration(skinParam.getIHtmlColorSet());
		this.colorSet = skinParam.getIHtmlColorSet();
		this.lineColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());

	}

	public void push(Token element) {
		stack.addFirst(
				new ETileBox(element.getData(), element.getSymbol(), fontConfiguration, style, colorSet, skinParam));
	}

	public void optional() {
		final ETile arg1 = stack.removeFirst();
		stack.addFirst(new ETileOptional(arg1, skinParam));
	}

	public void repetitionZeroOrMore(boolean isCompact) {
		final ETile arg1 = stack.removeFirst();
		if (isCompact)
			stack.addFirst(new ETileZeroOrMore(arg1));
		else
			stack.addFirst(new ETileOptional(new ETileOneOrMore(arg1), skinParam));
	}

	public void repetitionOneOrMore() {
		final ETile arg1 = stack.removeFirst();
		stack.addFirst(new ETileOneOrMore(arg1));
	}

	public void repetitionSymbol() {
		final ETile arg1 = stack.removeFirst();
		final String arg2 = stack.removeFirst().getRepetitionLabel() + "\u00D7";
		stack.addFirst(new ETileOneOrMore(arg1, arg2, fontConfiguration.bigger(-2), skinParam));

	}

	public void alternation() {
		final ETile arg1 = stack.removeFirst();
		final ETile arg2 = stack.removeFirst();
		if (arg1 instanceof ETileAlternation) {
			arg1.push(arg2);
			stack.addFirst(arg1);
		} else if (arg2 instanceof ETileAlternation) {
			arg2.push(arg1);
			stack.addFirst(arg2);
		} else {
			final ETile concat = new ETileAlternation();
			concat.push(arg1);
			concat.push(arg2);
			stack.addFirst(concat);
		}
	}

	public void concatenation() {

		final ETile arg1 = stack.removeFirst();
		final ETile arg2 = stack.removeFirst();
		if (arg1 instanceof ETileConcatenation) {
			arg1.push(arg2);
			stack.addFirst(arg1);
		} else if (arg2 instanceof ETileConcatenation) {
			arg2.push(arg1);
			stack.addFirst(arg2);
		} else {
			final ETile concat = new ETileConcatenation();
			concat.push(arg1);
			concat.push(arg2);
			stack.addFirst(concat);
		}
	}

	public TextBlock getTextBlock() {
		if (stack.size() != 1)
			return syntaxError(fontConfiguration, skinParam);
		return new ETileWithCircles(stack.peekFirst(), lineColor);
	}

	public static TextBlock syntaxError(FontConfiguration fontConfiguration, ISkinSimple spriteContainer) {
		final Display msg = Display.create("Syntax error!");
		return msg.create(fontConfiguration, HorizontalAlignment.LEFT, spriteContainer);
	}

	public void commentBelow(String comment) {
		final ETile arg1 = stack.peekFirst();
		if (arg1 == null)
			throw new IllegalStateException();

		arg1.addCommentBelow(comment);
	}

	public void commentAbove(String comment) {
		final ETile arg1 = stack.peekFirst();
		if (arg1 == null)
			throw new IllegalStateException();

		arg1.addCommentAbove(comment);
	}

}
