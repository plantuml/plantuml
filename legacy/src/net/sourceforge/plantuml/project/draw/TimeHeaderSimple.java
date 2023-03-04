/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
package net.sourceforge.plantuml.project.draw;

import java.util.Set;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;
import net.sourceforge.plantuml.project.TimeHeaderParameters;
import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.project.timescale.TimeScaleWink;

public class TimeHeaderSimple extends TimeHeader {

	private final TimeHeaderParameters colorDays;
	private final PrintScale printScale;
	private final Set<Day> verticalSeparators;

	@Override
	public double getFullHeaderHeight() {
		return getTimeHeaderHeight() + getHeaderNameDayHeight();
	}

	public double getTimeHeaderHeight() {
		return 16;
	}

	public double getTimeFooterHeight() {
		return 16;
	}

	private double getHeaderNameDayHeight() {
		return 0;
	}

	public TimeHeaderSimple(TimeHeaderParameters thParam, PrintScale printScale) {
		super(thParam.getTimelineStyle(), thParam.getClosedStyle(), thParam.getMin(), thParam.getMax(),
				new TimeScaleWink(thParam.getScale(), printScale), thParam.getColorSet());
		this.colorDays = thParam;
		this.printScale = printScale;
		this.verticalSeparators = thParam.getVerticalSeparatorBefore();
	}

	private boolean isBold(Day wink) {
		return verticalSeparators.contains(wink);
	}

	private void drawSeparatorsDay(UGraphic ug, TimeScale timeScale, double totalHeightWithoutFooter) {
		final ULine vbar = ULine.vline(totalHeightWithoutFooter - getFullHeaderHeight() + 2);
		ug = goBold(ug).apply(UTranslate.dy(getFullHeaderHeight() - 1));
		for (Day i = min; i.compareTo(max.increment()) <= 0; i = i.increment(printScale))
			if (isBold(i)) {
				final double x1 = timeScale.getStartingPosition(i);
				ug.apply(UTranslate.dx(x1)).draw(vbar);
			}
	}

	private void drawSmallVlinesDay(UGraphic ug, TimeScale timeScale, double totalHeightWithoutFooter) {
		final ULine vbar = ULine.vline(totalHeightWithoutFooter);
		for (Day i = min; i.compareTo(max.increment()) <= 0; i = i.increment(printScale)) {
			final double x1 = timeScale.getStartingPosition(i);
			ug.apply(getBarColor()).apply(UTranslate.dx(x1)).draw(vbar);
		}
	}

	private void drawSimpleDayCounter(UGraphic ug, TimeScale timeScale) {
		for (Day i = min; i.compareTo(max.increment()) <= 0; i = i.increment(printScale)) {
			final int value;
			if (printScale == PrintScale.WEEKLY)
				value = i.getAbsoluteDayNum() / 7 + 1;
			else
				value = i.getAbsoluteDayNum() + 1;
			final TextBlock num = Display.getWithNewlines("" + value).create(
					getFontConfiguration(10, false, openFontColor()), HorizontalAlignment.LEFT,
					new SpriteContainerEmpty());
			final double x1 = timeScale.getStartingPosition(i);
			final double x2;
			if (printScale == PrintScale.WEEKLY)
				x2 = timeScale.getEndingPosition(i.addDays(6));
			else
				x2 = timeScale.getEndingPosition(i);
			final double width = num.calculateDimension(ug.getStringBounder()).getWidth();
			final double delta = (x2 - x1) - width;
			if (i.compareTo(max.increment()) < 0)
				num.drawU(ug.apply(UTranslate.dx(x1 + delta / 2)));

		}
	}

	@Override
	public void drawTimeHeader(final UGraphic ug, double totalHeightWithoutFooter) {
		drawTextsBackground(ug.apply(UTranslate.dy(-3)), totalHeightWithoutFooter + 6);
		final double xmin = getTimeScale().getStartingPosition(min);
		final double xmax = getTimeScale().getEndingPosition(max);
		drawSmallVlinesDay(ug, getTimeScale(), totalHeightWithoutFooter + 2);
		drawSeparatorsDay(ug, getTimeScale(), totalHeightWithoutFooter);
		drawSimpleDayCounter(ug, getTimeScale());
		ug.apply(getBarColor()).draw(ULine.hline(xmax - xmin));
		ug.apply(getBarColor()).apply(UTranslate.dy(getFullHeaderHeight() - 3)).draw(ULine.hline(xmax - xmin));

	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		final double xmin = getTimeScale().getStartingPosition(min);
		final double xmax = getTimeScale().getEndingPosition(max);
		ug = ug.apply(UTranslate.dy(3));
		drawSmallVlinesDay(ug, getTimeScale(), getTimeFooterHeight() - 3);
		drawSimpleDayCounter(ug, getTimeScale());
		ug.apply(getBarColor()).draw(ULine.hline(xmax - xmin));
	}

	// Duplicate in TimeHeaderDaily
	class Pending {
		final double x1;
		double x2;
		final HColor color;

		Pending(HColor color, double x1, double x2) {
			this.x1 = x1;
			this.x2 = x2;
			this.color = color;
		}

		public void draw(UGraphic ug, double height) {
			drawRectangle(ug.apply(color.bg()), height, x1, x2);
		}
	}

	protected final void drawTextsBackground(UGraphic ug, double totalHeightWithoutFooter) {

		final double height = totalHeightWithoutFooter - getFullHeaderHeight();
		Pending pending = null;

		for (Day wink = min; wink.compareTo(max) <= 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			final double x2 = getTimeScale().getEndingPosition(wink);
			HColor back = colorDays.getColor(wink);
//			// Day of week should be stronger than period of time (back color).
//			final HColor backDoW = colorDaysOfWeek.get(wink.getDayOfWeek());
//			if (backDoW != null) {
//				back = backDoW;
//			}
//			if (back == null && defaultPlan.getLoadAt(wink) == 0) {
//				back = closedBackgroundColor();
//			}
			if (back == null) {
				if (pending != null)
					pending.draw(ug, height);
				pending = null;
			} else {
				if (pending != null && pending.color.equals(back) == false) {
					pending.draw(ug, height);
					pending = null;
				}
				if (pending == null)
					pending = new Pending(back, x1, x2);
				else
					pending.x2 = x2;

			}
		}
	}

}
