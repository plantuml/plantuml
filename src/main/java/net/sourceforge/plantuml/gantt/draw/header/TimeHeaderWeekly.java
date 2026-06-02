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
package net.sourceforge.plantuml.gantt.draw.header;

import java.time.LocalDate;

import net.sourceforge.plantuml.gantt.data.DayCalendarData;
import net.sourceforge.plantuml.gantt.data.TimeBoundsData;
import net.sourceforge.plantuml.gantt.data.TimeScaleConfigData;
import net.sourceforge.plantuml.gantt.data.TimelineStyleData;
import net.sourceforge.plantuml.gantt.data.WeekConfigData;
import net.sourceforge.plantuml.gantt.draw.WeeklyHeaderStrategy;
import net.sourceforge.plantuml.gantt.time.TimePoint;
import net.sourceforge.plantuml.gantt.time.TimePointFormat;
import net.sourceforge.plantuml.gantt.timescale.TimeScale;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.SName;

class TimeHeaderWeekly extends TimeHeaderCalendar {

	public TimeHeaderWeekly(TimeScale timeScale, WeekConfigData weekConfigData, DayCalendarData dayCalendar,
			TimeBoundsData timeBounds, TimeScaleConfigData scaleConfig, TimelineStyleData timelineStyle) {
		super(weekConfigData, dayCalendar, timeBounds, scaleConfig, timelineStyle, timeScale);
	}

	private double getH1() {
		final double h = timelineStyle.getFontSizeMonth() + 4;
		return h;
	}

	private double getH2() {
		final double h = timelineStyle.getFontSizeDay() + 1;
		return getH1() + h;
	}

	@Override
	public double getTimeHeaderHeight(StringBounder stringBounder) {
		return getH2();
	}

	@Override
	public double getTimeFooterHeight(StringBounder stringBounder) {
		final double h = timelineStyle.getFontSizeMonth() + 4;
		return h;
	}

	private double getHeaderNameDayHeight() {
		if (dayCalendar.getNameDays().size() > 0) {
			final double h = timelineStyle.getFontSizeDay() + 6;
			return h;
		}

		return 0;
	}

	@Override
	public double getFullHeaderHeight(StringBounder stringBounder) {
		return getTimeHeaderHeight(stringBounder) + getHeaderNameDayHeight();
	}

	@Override
	public void drawTimeHeaderInternal(final UGraphic ug, double totalHeightWithoutFooter) {
		printDaysOfMonth(ug);
		printVerticalSeparators(ug, totalHeightWithoutFooter);
		printMonths(ug);

		printNamedDays(ug);

		drawHline(ug, 0);
		drawHline(ug, getH1());
		drawHline(ug, getFullHeaderHeight(ug.getStringBounder()));
	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		drawHline(ug, 0);
		printMonths(ug);
		drawHline(ug, getTimeFooterHeight(ug.getStringBounder()));
	}

	private void printMonths(final UGraphic ug) {
		final FontConfiguration fc = getFontConfigurationSLOW(SName.month, true, openFontColor());

		TimePoint last = null;
		double lastChangeMonth = -1;

		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			final double x1 = getTimeScale().getPosition(wink);
			if (last == null || wink.monthYear().equals(last.monthYear()) == false) {
				drawVline(ug.apply(getLineColor()), x1, 0, getH1());
				if (last != null)
					printMonth(ug, last, lastChangeMonth, x1, fc);

				lastChangeMonth = x1;
				last = wink;
			}
		}
		final double end = getTimeScale().getPosition(TimePoint.ofStartOfDay(getMaxDay().plusDays(1)));
		drawVline(ug.apply(getLineColor()), end, 0, getH1());
		final double x1 = end;
		if (last != null && x1 > lastChangeMonth)
			printMonth(ug, last, lastChangeMonth, x1, fc);

	}

	@Override
	protected void printVerticalSeparators(final UGraphic ug, double totalHeightWithoutFooter) {
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			if (wink.toDayOfWeek() == weekConfigData.getWeekNumberStrategy().getFirstDayOfWeek())
				drawVline(ug.apply(getLineColor()), getTimeScale().getPosition(wink), getH1(),
						totalHeightWithoutFooter);
		}

		final double end = getTimeScale().getPosition(TimePoint.ofStartOfDay(getMaxDay().plusDays(1)));
		drawVline(ug.apply(getLineColor()), end, getH1(), totalHeightWithoutFooter);
		super.printVerticalSeparators(ug, totalHeightWithoutFooter);
	}

	private void printDaysOfMonth(final UGraphic ug) {
		final FontConfiguration fc = getFontConfigurationSLOW(SName.day, false, openFontColor());

		int counter = weekConfigData.getWeekStartingNumber();
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay().plusDays(-1)) < 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			if (wink.toDayOfWeek() == weekConfigData.getWeekNumberStrategy().getFirstDayOfWeek()) {
				final String num;
				if (weekConfigData.getWeeklyHeaderStrategy() == WeeklyHeaderStrategy.FROM_N)
					num = "" + (counter++);
				else if (weekConfigData.getWeeklyHeaderStrategy() == WeeklyHeaderStrategy.DAY_OF_MONTH)
					num = "" + wink.getDayOfMonth();
				else
					num = "" + wink.getWeekOfYear(weekConfigData.getWeekNumberStrategy());
				final TextBlock textBlock = getTextBlockSLOW(num, fc);
				printLeft(ug.apply(UTranslate.dy(getH1())), textBlock,
						getTimeScale().getPosition(wink) + 5);
			}
		}
	}

	private void printMonth(UGraphic ug, TimePoint monthYear, double start, double end, FontConfiguration fc) {
		printCentered(ug, false, start, end, monthYear, fc, TimePointFormat.MONTH_SHORT,
				TimePointFormat.MONTH_YEAR_SHORT);
	}

	private void printLeft(UGraphic ug, TextBlock text, double start) {
		text.drawU(ug.apply(UTranslate.dx(start)));
	}

}
