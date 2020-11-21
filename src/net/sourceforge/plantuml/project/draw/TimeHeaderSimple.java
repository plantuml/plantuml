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
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.project.timescale.TimeScaleWink;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class TimeHeaderSimple extends TimeHeader {

	@Override
	public double getFullHeaderHeight() {
		return getTimeHeaderHeight() + getHeaderNameDayHeight();
	}

	protected double getTimeHeaderHeight() {
		return 16;
	}

	public double getTimeFooterHeight() {
		return 16;
	}

	private double getHeaderNameDayHeight() {
		return 0;
	}

	public TimeHeaderSimple(Day min, Day max) {
		super(min, max, new TimeScaleWink());
	}

	private void drawSmallVlinesDay(UGraphic ug, TimeScale timeScale, double totalHeightWithoutFooter) {
		final ULine vbar = ULine.vline(totalHeightWithoutFooter);
		for (Day i = min; i.compareTo(max.increment()) <= 0; i = i.increment()) {
			final double x1 = timeScale.getStartingPosition(i);
			ug.apply(HColorUtils.LIGHT_GRAY).apply(UTranslate.dx(x1)).draw(vbar);
		}
	}

	private void drawSimpleDayCounter(UGraphic ug, TimeScale timeScale) {
		for (Day i = min; i.compareTo(max.increment()) <= 0; i = i.increment()) {
			final String number = "" + (i.getAbsoluteDayNum() + 1);
			final TextBlock num = Display.getWithNewlines(number).create(
					getFontConfiguration(10, false, HColorUtils.BLACK), HorizontalAlignment.LEFT,
					new SpriteContainerEmpty());
			final double x1 = timeScale.getStartingPosition(i);
			final double x2 = timeScale.getEndingPosition(i);
			final double width = num.calculateDimension(ug.getStringBounder()).getWidth();
			final double delta = (x2 - x1) - width;
			if (i.compareTo(max.increment()) < 0) {
				num.drawU(ug.apply(UTranslate.dx(x1 + delta / 2)));
			}
		}
	}

	@Override
	public void drawTimeHeader(final UGraphic ug, double totalHeightWithoutFooter) {
		final double xmin = getTimeScale().getStartingPosition(min);
		final double xmax = getTimeScale().getEndingPosition(max);
		drawSmallVlinesDay(ug, getTimeScale(), totalHeightWithoutFooter);
		drawSimpleDayCounter(ug, getTimeScale());
		ug.apply(HColorUtils.LIGHT_GRAY).draw(ULine.hline(xmax - xmin));
		ug.apply(HColorUtils.LIGHT_GRAY).apply(UTranslate.dy(getFullHeaderHeight() - 3)).draw(ULine.hline(xmax - xmin));

	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		final double xmin = getTimeScale().getStartingPosition(min);
		final double xmax = getTimeScale().getEndingPosition(max);
		drawSmallVlinesDay(ug, getTimeScale(), getTimeFooterHeight());
		ug = ug.apply(UTranslate.dy(3));
		drawSimpleDayCounter(ug, getTimeScale());
		ug.apply(HColorUtils.LIGHT_GRAY).draw(ULine.hline(xmax - xmin));
	}

}
