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

import net.sourceforge.plantuml.klimt.CopyForegroundColorToBackgroundColor;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public abstract class ETile extends AbstractTextBlock {

	protected final boolean TRACE = false;

	public static StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.ebnf);
	}

	public abstract void push(ETile tile);

	@Override
	final public XDimension2D calculateDimension(StringBounder stringBounder) {
		final double width = getWidth(stringBounder);
		final double h1 = getH1(stringBounder);
		final double h2 = getH2(stringBounder);
		return new XDimension2D(width, h1 + h2);
	}

	public abstract double getWidth(StringBounder stringBounder);

	public abstract double getH1(StringBounder stringBounder);

	public abstract double getH2(StringBounder stringBounder);

	protected final void drawHline(UGraphic ug, double y, double x1, double x2) {
		ug.apply(new UTranslate(x1, y)).draw(ULine.hline(x2 - x1));
	}

	protected final void drawHlineDirected(UGraphic ug, double y, double x1, double x2, double coef, double minimunSizeForArrow) {
		ug.apply(new UTranslate(x1, y)).draw(ULine.hline(x2 - x1));
		if (x2 > x1 + minimunSizeForArrow)
			ug.apply(new CopyForegroundColorToBackgroundColor())
					.apply(new UTranslate(x1 * (1 - coef) + x2 * coef - 2, y)).draw(getArrowToRight());

	}

	protected final void drawHlineAntiDirected(UGraphic ug, double y, double x1, double x2, double coef) {
		ug.apply(new UTranslate(x1, y)).draw(ULine.hline(x2 - x1));
		ug.apply(new CopyForegroundColorToBackgroundColor()).apply(new UTranslate(x1 * (1 - coef) + x2 * coef - 2, y))
				.draw(getArrowToLeft());

	}

	protected final void drawVline(UGraphic ug, double x, double y1, double y2) {
		ug.apply(new UTranslate(x, y1)).draw(ULine.vline(y2 - y1));
	}

	protected final void drawVlineDirected(UGraphic ug, double x, double y1, double y2) {
		ug.apply(new UTranslate(x, y1)).draw(ULine.vline(y2 - y1));
		ug.apply(new CopyForegroundColorToBackgroundColor()).apply(new UTranslate(x, y1 * .5 + y2 * .5 - 2))
				.draw(getArrowToBottom());

	}

	protected final void drawVlineAntiDirected(UGraphic ug, double x, double y1, double y2) {
		ug.apply(new UTranslate(x, y1)).draw(ULine.vline(y2 - y1));
		ug.apply(new CopyForegroundColorToBackgroundColor()).apply(new UTranslate(x, y1 * .5 + y2 * .5 + 2))
				.draw(getArrowToTop());

	}

	static UPath getArrowToLeft() {
		final UPath arrow = UPath.none();
		arrow.moveTo(0, 0);
		arrow.lineTo(0, -3);
		arrow.lineTo(-6, 0);
		arrow.lineTo(0, 3);
		arrow.lineTo(0, 0);
		arrow.closePath();
		return arrow;
	}

	static UPath getArrowToRight() {
		final UPath arrow = UPath.none();
		arrow.moveTo(0, 0);
		arrow.lineTo(0, -3);
		arrow.lineTo(6, 0);
		arrow.lineTo(0, 3);
		arrow.lineTo(0, 0);
		arrow.closePath();
		return arrow;
	}

	static UPath getArrowToBottom() {
		final UPath arrow = UPath.none();
		arrow.moveTo(0, 0);
		arrow.lineTo(3, 0);
		arrow.lineTo(0, 6);
		arrow.lineTo(-3, 0);
		arrow.lineTo(0, 0);
		arrow.closePath();
		return arrow;
	}

	static UPath getArrowToTop() {
		final UPath arrow = UPath.none();
		arrow.moveTo(0, 0);
		arrow.lineTo(3, 0);
		arrow.lineTo(0, -6);
		arrow.lineTo(-3, 0);
		arrow.lineTo(0, 0);
		arrow.closePath();
		return arrow;
	}

	protected void addCommentBelow(String comment) {
		System.err.println("Ignoring below comment " + comment + " " + getClass());
	}

	protected void addCommentAbove(String comment) {
		System.err.println("Ignoring above comment " + comment + " " + getClass());
	}

	protected String getRepetitionLabel() {
		return "?";
	}

}
