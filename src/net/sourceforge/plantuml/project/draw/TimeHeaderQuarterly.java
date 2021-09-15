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
import net.sourceforge.plantuml.project.timescale.TimeScaleCompressed;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class TimeHeaderQuarterly extends TimeHeaderCalendar {

	public double getTimeHeaderHeight() {
		return 16 + 13;
	}

	public double getTimeFooterHeight() {
		return 16 + 13 - 1;
	}

	public TimeHeaderQuarterly(Locale locale, Style timelineStyle, Style closedStyle, double scale, Day calendar, Day min, Day max,
			LoadPlanable defaultPlan, Map<Day, HColor> colorDays, Map<DayOfWeek, HColor> colorDaysOfWeek,
			HColorSet colorSet, ThemeStyle themeStyle) {
		super(locale, timelineStyle, closedStyle, calendar, min, max, defaultPlan, colorDays, colorDaysOfWeek,
				new TimeScaleCompressed(calendar, scale), colorSet, themeStyle);
	}

	@Override
	public void drawTimeHeader(final UGraphic ug, double totalHeightWithoutFooter) {
		drawTextsBackground(ug, totalHeightWithoutFooter);
		drawYears(ug);
		drawQuarters(ug.apply(UTranslate.dy(16)));
		drawHline(ug, 0);
		drawHline(ug, 16);
		drawHline(ug, getFullHeaderHeight());
	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		ug = ug.apply(UTranslate.dy(3));
		drawQuarters(ug);
		drawYears(ug.apply(UTranslate.dy(13)));
		drawHline(ug, 0);
		drawHline(ug, 13);
		drawHline(ug, getTimeFooterHeight());
	}

	private void drawYears(final UGraphic ug) {
		MonthYear last = null;
		double lastChange = -1;
		for (Day wink = min; wink.compareTo(max) < 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			if (last == null || wink.monthYear().year() != last.year()) {
				drawVbar(ug, x1, 0, 15);
				if (last != null) {
					printYear(ug, last, lastChange, x1);
				}
				lastChange = x1;
				last = wink.monthYear();
			}
		}
		final double x1 = getTimeScale().getStartingPosition(max.increment());
		if (x1 > lastChange) {
			printYear(ug, last, lastChange, x1);
		}
		drawVbar(ug, getTimeScale().getEndingPosition(max), 0, 15);
	}

	private void drawQuarters(UGraphic ug) {
		String last = null;
		double lastChange = -1;
		for (Day wink = min; wink.compareTo(max) < 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			if (quarter(wink).equals(last) == false) {
				drawVbar(ug, x1, 0, 12);
				if (last != null) {
					printQuarter(ug, last, lastChange, x1);
				}
				lastChange = x1;
				last = quarter(wink);
			}
		}
		final double x1 = getTimeScale().getStartingPosition(max.increment());
		if (x1 > lastChange) {
			printQuarter(ug, last, lastChange, x1);
		}
		drawVbar(ug, getTimeScale().getEndingPosition(max), 0, 12);
	}

	private String quarter(Day day) {
		return "Q" + ((day.month().ordinal() + 3) / 3);
	}

	private void printYear(UGraphic ug, MonthYear monthYear, double start, double end) {
		final TextBlock small = getTextBlock("" + monthYear.year(), 12, true, openFontColor());
		printCentered(ug, false, start, end, small);
	}

	private void printQuarter(UGraphic ug, String quarter, double start, double end) {
		final TextBlock small = getTextBlock(quarter, 10, false, openFontColor());
		printCentered(ug, false, start, end, small);
	}

	private void printLeft(UGraphic ug, TextBlock text, double start) {
		text.drawU(ug.apply(UTranslate.dx(start)));
	}

	@Override
	public double getFullHeaderHeight() {
		return getTimeHeaderHeight();
	}

}
