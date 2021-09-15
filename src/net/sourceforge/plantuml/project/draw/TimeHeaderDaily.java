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

import java.util.Locale;
import java.util.Map;

import net.sourceforge.plantuml.ThemeStyle;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.LoadPlanable;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;
import net.sourceforge.plantuml.project.time.MonthYear;
import net.sourceforge.plantuml.project.timescale.TimeScaleDaily;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class TimeHeaderDaily extends TimeHeaderCalendar {

	public double getTimeHeaderHeight() {
		return Y_POS_ROW28() + 13;
	}

	public double getTimeFooterHeight() {
		// return 0;
		return 24 + 14;
	}

	private final Map<Day, String> nameDays;

	public TimeHeaderDaily(Locale locale, Style timelineStyle, Style closedStyle, double scale, Day calendar, Day min,
			Day max, LoadPlanable defaultPlan, Map<Day, HColor> colorDays, Map<DayOfWeek, HColor> colorDaysOfWeek,
			Map<Day, String> nameDays, Day printStart, Day printEnd, HColorSet colorSet, ThemeStyle themeStyle) {
		super(locale, timelineStyle, closedStyle, calendar, min, max, defaultPlan, colorDays, colorDaysOfWeek,
				new TimeScaleDaily(scale, calendar, printStart), colorSet, themeStyle);
		this.nameDays = nameDays;
	}

	@Override
	public void drawTimeHeader(final UGraphic ug, double totalHeightWithoutFooter) {
		drawTextsBackground(ug, totalHeightWithoutFooter);
		drawTextsDayOfWeek(ug.apply(UTranslate.dy(Y_POS_ROW16())));
		drawTextDayOfMonth(ug.apply(UTranslate.dy(Y_POS_ROW28())));
		drawMonths(ug);
		printSmallVbars(ug, totalHeightWithoutFooter);
//		drawVBars(ug, totalHeightWithoutFooter);
//		drawVbar(ug, getTimeScale().getStartingPosition(max.increment()), 0,
//				totalHeightWithoutFooter + getTimeFooterHeight());
		printNamedDays(ug);

		drawHline(ug, getFullHeaderHeight());
		drawHline(ug, totalHeightWithoutFooter);

//		drawHline(ug, 0);
//		drawHline(ug, getFullHeaderHeight());
	}

	private void printSmallVbars(final UGraphic ug, double totalHeightWithoutFooter) {
		for (Day wink = min; wink.compareTo(max) <= 0; wink = wink.increment()) {
			drawVbar(ug, getTimeScale().getStartingPosition(wink), getFullHeaderHeight(), totalHeightWithoutFooter);
		}
		drawVbar(ug, getTimeScale().getEndingPosition(max), getFullHeaderHeight(), totalHeightWithoutFooter);
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

	@Override
	public void drawTimeFooter(UGraphic ug) {
		drawTextDayOfMonth(ug.apply(UTranslate.dy(12)));
		drawTextsDayOfWeek(ug);
		drawMonths(ug.apply(UTranslate.dy(24)));
//		drawHline(ug, 0);
//		drawHline(ug, getTimeFooterHeight());
	}

	private void drawTextsDayOfWeek(UGraphic ug) {
		for (Day wink = min; wink.compareTo(max) <= 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			final double x2 = getTimeScale().getEndingPosition(wink);
			final HColor textColor = getTextBackColor(wink);
			printCentered(ug, getTextBlock(wink.getDayOfWeek().shortName(locale), 10, false, textColor), x1, x2);
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
			return closedFontColor();
		}
		return openFontColor();
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

	private void printMonth(UGraphic ug, MonthYear monthYear, double start, double end) {
		final TextBlock tiny = getTextBlock(monthYear.shortName(locale), 12, true, openFontColor());
		final TextBlock small = getTextBlock(monthYear.longName(locale), 12, true, openFontColor());
		final TextBlock big = getTextBlock(monthYear.longNameYYYY(locale), 12, true, openFontColor());
		printCentered(ug, false, start, end, tiny, small, big);
	}

	private void printNamedDays(final UGraphic ug) {
		if (nameDays.size() > 0) {
			String last = null;
			for (Day wink = min; wink.compareTo(max.increment()) <= 0; wink = wink.increment()) {
				final String name = nameDays.get(wink);
				if (name != null && name.equals(last) == false) {
					final double x1 = getTimeScale().getStartingPosition(wink);
					final double x2 = getTimeScale().getEndingPosition(wink);
					final TextBlock label = getTextBlock(name, 12, false, openFontColor());
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
