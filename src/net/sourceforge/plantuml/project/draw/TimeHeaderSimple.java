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
package net.sourceforge.plantuml.project.draw;

import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.core.Wink;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.project.timescale.TimeScaleWink;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TimeHeaderSimple extends TimeHeader {

	public TimeHeaderSimple(Wink min, Wink max) {
		super(min, max, new TimeScaleWink());
	}

	@Override
	public void drawTimeHeader(final UGraphic ug, double totalHeight) {
		final double xmin = getTimeScale().getStartingPosition(min);
		final double xmax = getTimeScale().getEndingPosition(max);
		drawSimpleDayCounter(ug, getTimeScale(), totalHeight);
		ug.apply(new UChangeColor(HtmlColorUtils.LIGHT_GRAY)).draw(new ULine(xmax - xmin, 0));
		ug.apply(new UChangeColor(HtmlColorUtils.LIGHT_GRAY)).apply(new UTranslate(0, getFullHeaderHeight() - 3))
				.draw(new ULine(xmax - xmin, 0));

	}

	private void drawSimpleDayCounter(final UGraphic ug, TimeScale timeScale, double totalHeight) {
		final ULine vbar = new ULine(0, totalHeight);
		for (Wink i = min; i.compareTo(max.increment()) <= 0; i = i.increment()) {
			final TextBlock num = Display.getWithNewlines(i.toShortString()).create(getFontConfiguration(10, false),
					HorizontalAlignment.LEFT, new SpriteContainerEmpty());
			final double x1 = timeScale.getStartingPosition(i);
			final double x2 = timeScale.getEndingPosition(i);
			final double width = num.calculateDimension(ug.getStringBounder()).getWidth();
			final double delta = (x2 - x1) - width;
			if (i.compareTo(max.increment()) < 0) {
				num.drawU(ug.apply(new UTranslate(x1 + delta / 2, 0)));
			}
			ug.apply(new UChangeColor(HtmlColorUtils.LIGHT_GRAY)).apply(new UTranslate(x1, 0)).draw(vbar);
		}
	}

	@Override
	public double getFullHeaderHeight() {
		return getTimeHeaderHeight() + getHeaderNameDayHeight();
	}

	private double getTimeHeaderHeight() {
		return 16;
	}

	private double getHeaderNameDayHeight() {
		return 0;
	}

}
