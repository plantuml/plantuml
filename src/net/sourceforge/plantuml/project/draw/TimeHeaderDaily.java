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

import java.util.Map;

import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.LoadPlanable;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;
import net.sourceforge.plantuml.project.time.MonthYear;
import net.sourceforge.plantuml.project.timescale.TimeScaleDaily;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class TimeHeaderDaily extends TimeHeaderCalendar {

	public double getTimeHeaderHeight() {
		return Y_POS_ROW28() + 13;
	}

	public double getTimeFooterHeight() {
		// return 0;
		return 24 + 14;
	}

	private final Map<Day, String> nameDays;

	public TimeHeaderDaily(Day calendar, Day min, Day max, LoadPlanable defaultPlan, Map<Day, HColor> colorDays,
			Map<DayOfWeek, HColor> colorDaysOfWeek, Map<Day, String> nameDays, Day printStart, Day printEnd) {
		super(calendar, min, max, defaultPlan, colorDays, colorDaysOfWeek, new TimeScaleDaily(calendar, printStart));
		this.nameDays = nameDays;
	}

	@Override
	public void drawTimeHeader(final UGraphic ug, double totalHeightWithoutFooter) {
		drawTextsBackground(ug, totalHeightWithoutFooter);
		drawTextsDayOfWeek(ug.apply(UTranslate.dy(Y_POS_ROW16())));
		drawTextDayOfMonth(ug.apply(UTranslate.dy(Y_POS_ROW28())));
		drawMonths(ug);
		drawVBars(ug, totalHeightWithoutFooter);
		drawVbar(ug, getTimeScale().getStartingPosition(max.increment()), 0,
				totalHeightWithoutFooter + getTimeFooterHeight());
		printNamedDays(ug);
		drawHline(ug, 0);
		drawHline(ug, getFullHeaderHeight());
	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		drawTextDayOfMonth(ug.apply(UTranslate.dy(12)));
		drawTextsDayOfWeek(ug);
		drawMonths(ug.apply(UTranslate.dy(24)));
		drawHline(ug, 0);
		drawHline(ug, getTimeFooterHeight());
	}

	private void drawTextsDayOfWeek(UGraphic ug) {
		for (Day wink = min; wink.compareTo(max) <= 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			final double x2 = getTimeScale().getEndingPosition(wink);
			final HColor textColor = getTextBackColor(wink);
			printCentered(ug, getTextBlock(wink.getDayOfWeek().shortName(), 10, false, textColor), x1, x2);
		}
	}

	private void drawTextDayOfMonth(UGraphic ug) {
		for (Day wink = min; wink.compareTo(max) <= 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			final double x2 = getTimeScale().getEndingPosition(wink);
			final HColor textColor = getTextBackColor(wink);
			printCentered(ug, getTextBlock("" + wink.getDayOfMonth(), 10, false, textColor), x1, x2);
		}
	}

	private HColor getTextBackColor(Day wink) {
		if (defaultPlan.getLoadAt(wink) <= 0) {
			return lightGray;
		}
		return HColorUtils.BLACK;
	}

	private void drawMonths(final UGraphic ug) {
		MonthYear last = null;
		double lastChangeMonth = -1;
		for (Day wink = min; wink.compareTo(max) <= 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			if (wink.monthYear().equals(last) == false) {
				if (last != null) {
					printMonth(ug, last, lastChangeMonth, x1);
				}
				lastChangeMonth = x1;
				last = wink.monthYear();
			}
		}
		final double x1 = getTimeScale().getStartingPosition(max.increment());
		if (x1 > lastChangeMonth) {
			printMonth(ug, last, lastChangeMonth, x1);
		}
	}

	private void drawVBars(UGraphic ug, double totalHeightWithoutFooter) {
		MonthYear last = null;
		for (Day wink = min; wink.compareTo(max) <= 0; wink = wink.increment()) {
			double startingY = getFullHeaderHeight();
			double len = totalHeightWithoutFooter;
			if (wink.monthYear().equals(last) == false) {
				startingY = 0;
				last = wink.monthYear();
				len += 24 + 13;
			}
			drawVbar(ug, getTimeScale().getStartingPosition(wink), startingY, len);
		}
	}

	private void printMonth(UGraphic ug, MonthYear monthYear, double start, double end) {
		final TextBlock tiny = getTextBlock(monthYear.shortName(), 12, true, HColorUtils.BLACK);
		final TextBlock small = getTextBlock(monthYear.longName(), 12, true, HColorUtils.BLACK);
		final TextBlock big = getTextBlock(monthYear.longNameYYYY(), 12, true, HColorUtils.BLACK);
		printCentered(ug, false, start, end, tiny, small, big);
	}

	private void drawVbar(UGraphic ug, double x, double y1, double y2) {
		final ULine vbar = ULine.vline(y2 - y1);
		ug.apply(HColorUtils.LIGHT_GRAY).apply(new UTranslate(x, y1)).draw(vbar);
	}

	private void printNamedDays(final UGraphic ug) {
		if (nameDays.size() > 0) {
			String last = null;
			for (Day wink = min; wink.compareTo(max.increment()) <= 0; wink = wink.increment()) {
				final String name = nameDays.get(wink);
				if (name != null && name.equals(last) == false) {
					final double x1 = getTimeScale().getStartingPosition(wink);
					final double x2 = getTimeScale().getEndingPosition(wink);
					final TextBlock label = getTextBlock(name, 12, false, HColorUtils.BLACK);
					final double h = label.calculateDimension(ug.getStringBounder()).getHeight();
					double y1 = getTimeHeaderHeight();
					double y2 = getFullHeaderHeight();
					label.drawU(ug.apply(new UTranslate(x1, Y_POS_ROW28() + 11)));
				}
				last = name;
			}
		}
	}

	@Override
	public double getFullHeaderHeight() {
		return getTimeHeaderHeight() + getHeaderNameDayHeight();
	}

	private double getHeaderNameDayHeight() {
		if (nameDays.size() > 0) {
			return 16;
		}
		return 0;
	}

}
