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
import java.util.Deque;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class EbnfEngine {

	private final Deque<ETile> stack = new ArrayDeque<>();
	private final FontConfiguration fontConfiguration;
	private final Style style;
	private final HColorSet colorSet;

	public EbnfEngine(ISkinParam skinParam) {
		this.style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		this.fontConfiguration = style.getFontConfiguration(skinParam.getIHtmlColorSet());
		this.colorSet = skinParam.getIHtmlColorSet();

	}

	public void push(Token element) {
		stack.addFirst(new ETileBox(element.getData(), element.getSymbol(), fontConfiguration, style, colorSet));
	}

	public void optional() {
		final ETile arg1 = stack.removeFirst();
		stack.addFirst(new ETileOptional(arg1));
	}

	public void repetition() {
		final ETile arg1 = stack.removeFirst();
		stack.addFirst(new ETileRepetition(arg1));
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
			throw new IllegalStateException();
		return new ETileWithCircles(stack.peekFirst());
	}

	private StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

}
