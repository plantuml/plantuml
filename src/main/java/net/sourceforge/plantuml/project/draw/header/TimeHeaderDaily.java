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
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.project.data.DayCalendarData;
import net.sourceforge.plantuml.project.data.TimeBoundsData;
import net.sourceforge.plantuml.project.data.TimeScaleConfigData;
import net.sourceforge.plantuml.project.data.TimelineStyleData;
import net.sourceforge.plantuml.project.data.WeekConfigData;
import net.sourceforge.plantuml.project.time.DayOfWeekUtils;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.time.YearMonthUtils;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.style.SName;

class TimeHeaderDaily extends TimeHeaderCalendar {

	public TimeHeaderDaily(TimeScale timeScale, WeekConfigData weekConfigData, DayCalendarData dayCalendar,
			TimeBoundsData timeBounds, TimeScaleConfigData scaleConfig, TimelineStyleData timelineStyle) {
		super(weekConfigData, dayCalendar, timeBounds, scaleConfig, timelineStyle, timeScale);
	}

	private double getH1(StringBounder stringBounder) {
		final double h = timelineStyle.getFontSizeMonth() + 2;
		return h;
	}

	private double getH2(StringBounder stringBounder) {
		final double h = timelineStyle.getFontSizeDay() + 2;
		return getH1(stringBounder) + h;
	}

	private double getH3(StringBounder stringBounder) {
		final double h = timelineStyle.getFontSizeDay() + 3;
		return getH2(stringBounder) + h;
	}

	@Override
	public double getTimeHeaderHeight(StringBounder stringBounder) {
		return getH3(stringBounder);
	}

	@Override
	public double getTimeFooterHeight(StringBounder stringBounder) {
		final double h1 = timelineStyle.getFontSizeDay();
		final double h2 = timelineStyle.getFontSizeDay();
		final double h3 = timelineStyle.getFontSizeMonth();
		return h1 + h2 + h3 + 8;
	}

	@Override
	public double getFullHeaderHeight(StringBounder stringBounder) {
		return getTimeHeaderHeight(stringBounder) + getHeaderNameDayHeight();
	}

	@Override
	public void drawTimeHeaderInternal(final UGraphic ug, double totalHeightWithoutFooter) {
		drawTextsDayOfWeek(ug.apply(UTranslate.dy(getH1(ug.getStringBounder()))));
		drawTextDayOfMonth(ug.apply(UTranslate.dy(getH2(ug.getStringBounder()))));
		drawMonths(ug);
		printVerticalSeparators(ug, totalHeightWithoutFooter);

		printNamedDays(ug);

		drawHline(ug, getFullHeaderHeight(ug.getStringBounder()));
		drawHline(ug, totalHeightWithoutFooter);
	}

	@Override
	protected void printVerticalSeparators(final UGraphic ug, double totalHeightWithoutFooter) {
		final UGraphic ugVerticalSeparator = timelineStyle.applyVerticalSeparatorStyle(ug);
		final UGraphic ugLineColor = ug.apply(getLineColor());
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			if (isBold(day) || getTimeScale().getWidth(wink.decrement()) == 0)
				drawVline(ugVerticalSeparator, getTimeScale().getPosition(wink),
						getFullHeaderHeight(ug.getStringBounder()), totalHeightWithoutFooter);
			else
				drawVline(ugLineColor, getTimeScale().getPosition(wink), getFullHeaderHeight(ug.getStringBounder()),
						totalHeightWithoutFooter);
		}

		final double end = getTimeScale().getPosition(TimePoint.ofStartOfDay(getMaxDay().plusDays(1)));

		drawVline(ugLineColor, end, getFullHeaderHeight(ug.getStringBounder()), totalHeightWithoutFooter);
	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		final double h = timelineStyle.getFontSizeDay() + 2;
		drawTextsDayOfWeek(ug);
		drawTextDayOfMonth(ug.apply(UTranslate.dy(h + 2)));
		drawMonths(ug.apply(UTranslate.dy(2 * h + 3)));
	}

	private void drawTextsDayOfWeek(UGraphic ug) {
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);

			if (isHidden(wink))
				continue;
			final double x1 = getTimeScale().getPosition(wink);
			final double x2 = getTimeScale().getPosition(wink.increment());
			final HColor textColor = getTextBackColor(wink);
			printCentered(ug,
					getTextBlock(SName.day, DayOfWeekUtils.shortName(wink.toDayOfWeek(), locale()), false, textColor),
					x1, x2);
		}
	}

	private void drawTextDayOfMonth(UGraphic ug) {
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);

			if (isHidden(wink))
				continue;
			final double x1 = getTimeScale().getPosition(wink);
			final double x2 = getTimeScale().getPosition(wink.increment());
			final HColor textColor = getTextBackColor(wink);
			printCentered(ug, getTextBlock(SName.day, "" + wink.getDayOfMonth(), false, textColor), x1, x2);
		}
	}

	private boolean isHidden(TimePoint wink) {
		if (scaleConfig.isHideClosed() && dayCalendar.getOpenClose().isClosed(wink.toDay()))
			return true;
		return false;
	}

	private HColor getTextBackColor(TimePoint wink) {
		if (getLoadAt(wink) <= 0)
			return closedFontColor();

		return openFontColor();
	}

	private void drawMonths(final UGraphic ug) {
		YearMonth last = null;
		double lastChangeMonth = -1;
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			if (isHidden(wink))
				continue;
			final double x1 = getTimeScale().getPosition(wink);
			if (wink.monthYear().equals(last) == false) {
				if (last != null)
					printMonth(ug, last, lastChangeMonth, x1);

				lastChangeMonth = x1;
				last = wink.monthYear();
			}
		}
		final double x1 = getTimeScale().getPosition(TimePoint.ofStartOfDay(getMaxDay().plusDays(1)));
		if (x1 > lastChangeMonth)
			printMonth(ug, last, lastChangeMonth, x1);

	}

	private void printMonth(UGraphic ug, YearMonth monthYear, double start, double end) {
		final TextBlock tiny = getTextBlock(SName.month, YearMonthUtils.shortName(monthYear, locale()), true,
				openFontColor());
		final TextBlock small = getTextBlock(SName.month, YearMonthUtils.longName(monthYear, locale()), true,
				openFontColor());
		final TextBlock big = getTextBlock(SName.month, YearMonthUtils.longNameYYYY(monthYear, locale()), true,
				openFontColor());
		printCentered(ug, false, start, end, tiny, small, big);
	}

}
