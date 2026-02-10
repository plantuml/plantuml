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
package net.sourceforge.plantuml.project.draw.header;

import java.time.LocalDate;
import java.time.YearMonth;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.project.data.DayCalendarData;
import net.sourceforge.plantuml.project.data.TimeBoundsData;
import net.sourceforge.plantuml.project.data.TimeScaleConfigData;
import net.sourceforge.plantuml.project.data.TimelineStyleData;
import net.sourceforge.plantuml.project.data.WeekConfigData;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.time.YearMonthUtils;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.style.SName;

class TimeHeaderMonthly extends TimeHeaderCalendar {

	public TimeHeaderMonthly(TimeScale timeScale, WeekConfigData weekConfigData, DayCalendarData dayCalendar,
			TimeBoundsData timeBounds, TimeScaleConfigData scaleConfig, TimelineStyleData timelineStyle) {
		super(weekConfigData, dayCalendar, timeBounds, scaleConfig, timelineStyle, timeScale);
	}

	private double getH1(StringBounder stringBounder) {
		final double h = timelineStyle.getFontSizeYear() + 2;
		return h;
	}

	private double getH2(StringBounder stringBounder) {
		final double h = timelineStyle.getFontSizeMonth() + 2;
		return getH1(stringBounder) + h;
	}

	@Override
	public double getTimeHeaderHeight(StringBounder stringBounder) {
		return getH2(stringBounder);
	}

	@Override
	public double getTimeFooterHeight(StringBounder stringBounder) {
		final double h1 = timelineStyle.getFontSizeYear();
		final double h2 = timelineStyle.getFontSizeMonth();
		return h1 + h2 + 5;
	}

	@Override
	public double getFullHeaderHeight(StringBounder stringBounder) {
		return getTimeHeaderHeight(stringBounder) + getHeaderNameDayHeight();
	}

	@Override
	public void drawTimeHeaderInternal(final UGraphic ug, double totalHeightWithoutFooter) {
		drawYears(ug);
		final double h1 = getH1(ug.getStringBounder());
		final double h2 = getH2(ug.getStringBounder());
		drawMonths(ug.apply(UTranslate.dy(h1)));
		printVerticalSeparators(ug, totalHeightWithoutFooter);

		printNamedDays(ug);

		drawHline(ug, 0);
		drawHline(ug, h1);
		drawHline(ug, h2);
	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		final double h1 = timelineStyle.getFontSizeYear();
		final double h2 = timelineStyle.getFontSizeMonth();
		// ug = ug.apply(UTranslate.dy(3));
		drawMonths(ug);
		drawYears(ug.apply(UTranslate.dy(h2 + 2)));
		drawHline(ug, 0);
		drawHline(ug, h2 + 2);
		drawHline(ug, h1 + 2 + h2 + 2);
//		drawHline(ug, getTimeFooterHeight(ug.getStringBounder()));
	}

	private void drawYears(final UGraphic ug) {
		final double h1 = timelineStyle.getFontSizeYear();
		YearMonth last = null;
		double lastChange = -1;
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) < 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			final double x1 = getTimeScale().getPosition(wink);
			if (last == null || wink.monthYear().getYear() != last.getYear()) {
				drawVline(ug.apply(getLineColor()), x1, 0, h1 + 2);
				if (last != null)
					printYear(ug, last, lastChange, x1);

				lastChange = x1;
				last = wink.monthYear();
			}
		}
		final double x1 = getTimeScale().getPosition(TimePoint.ofStartOfDay(getMaxDay().plusDays(1)));
		if (x1 > lastChange)
			printYear(ug, last, lastChange, x1);

		final double end = x1;
		drawVline(ug.apply(getLineColor()), end, 0, h1 + 2);
	}

	private void drawMonths(UGraphic ug) {
		final double h2 = timelineStyle.getFontSizeMonth();
		YearMonth last = null;
		double lastChange = -1;
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) < 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			final double x1 = getTimeScale().getPosition(wink);
			if (wink.monthYear().equals(last) == false) {
				drawVline(ug.apply(getLineColor()), x1, 0, h2 + 2);
				if (last != null)
					printMonth(ug, last, lastChange, x1);

				lastChange = x1;
				last = wink.monthYear();
			}
		}
		final double x1 = getTimeScale().getPosition(TimePoint.ofStartOfDay(getMaxDay().plusDays(1)));
		if (x1 > lastChange)
			printMonth(ug, last, lastChange, x1);

		final double end = x1;
		drawVline(ug.apply(getLineColor()), end, 0, h2 + 2);
	}

	private void printYear(UGraphic ug, YearMonth monthYear, double start, double end) {
		final TextBlock small = getTextBlock(SName.month, "" + monthYear.getYear(), true, openFontColor());
		printCentered(ug, false, start, end, small);
	}

	private void printMonth(UGraphic ug, YearMonth monthYear, double start, double end) {
		final TextBlock small = getTextBlock(SName.day, YearMonthUtils.shortName(monthYear, locale()), false,
				openFontColor());
		final TextBlock big = getTextBlock(SName.day, YearMonthUtils.longName(monthYear, locale()), false,
				openFontColor());
		printCentered(ug, false, start, end, small, big);
	}

	private double getHeaderNameDayHeight() {
		if (dayCalendar.getNameDays().size() > 0) {
			final double h = timelineStyle.getFontSizeDay() + 6;
			return h;
		}

		return 0;
	}

}
