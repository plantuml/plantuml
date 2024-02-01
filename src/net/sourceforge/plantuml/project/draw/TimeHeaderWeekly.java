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

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.project.TimeHeaderParameters;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.MonthYear;
import net.sourceforge.plantuml.project.time.WeekNumberStrategy;
import net.sourceforge.plantuml.project.timescale.TimeScaleCompressed;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;

public class TimeHeaderWeekly extends TimeHeaderCalendar {

	private final WeekNumberStrategy weekNumberStrategy;
	private final boolean withCalendarDate;

	@Override
	public double getTimeHeaderHeight(StringBounder stringBounder) {
		final double h1 = thParam.getStyle(SName.timeline, SName.month).value(PName.FontSize).asDouble();
		final double h2 = thParam.getStyle(SName.timeline, SName.day).value(PName.FontSize).asDouble();
		return h1 + h2 + 5;
	}

	@Override
	public double getTimeFooterHeight(StringBounder stringBounder) {
		final double h = thParam.getStyle(SName.timeline, SName.month).value(PName.FontSize).asDouble() + 4;
		return h;
	}

	@Override
	public double getFullHeaderHeight(StringBounder stringBounder) {
		return getTimeHeaderHeight(stringBounder);
	}

	private double getH1(StringBounder stringBounder) {
		final double h = thParam.getStyle(SName.timeline, SName.month).value(PName.FontSize).asDouble() + 4;
		return h;
	}

	public TimeHeaderWeekly(StringBounder stringBounder, TimeHeaderParameters thParam,
			WeekNumberStrategy weekNumberStrategy, boolean withCalendarDate, Day printStart) {
		super(thParam, new TimeScaleCompressed(thParam.getCellWidth(stringBounder), thParam.getStartingDay(),
				thParam.getScale(), printStart));
		this.weekNumberStrategy = weekNumberStrategy;
		this.withCalendarDate = withCalendarDate;
	}

	@Override
	public void drawTimeHeader(final UGraphic ug, double totalHeightWithoutFooter) {
		drawTextsBackground(ug, totalHeightWithoutFooter);
		drawCalendar(ug, totalHeightWithoutFooter);
		drawHline(ug, 0);
		drawHline(ug, getH1(ug.getStringBounder()));
		drawHline(ug, getFullHeaderHeight(ug.getStringBounder()));
	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		drawHline(ug, 0);
		printMonths(ug);
		drawHline(ug, getTimeFooterHeight(ug.getStringBounder()));
	}

	private void drawCalendar(final UGraphic ug, double totalHeightWithoutFooter) {
		printDaysOfMonth(ug);
		printVerticalSeparators(ug, totalHeightWithoutFooter);
		printMonths(ug);
	}

	private void printMonths(final UGraphic ug) {
		MonthYear last = null;
		double lastChangeMonth = -1;
		for (Day wink = getMin(); wink.compareTo(getMax()) < 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			if (wink.monthYear().equals(last) == false) {
				drawVline(ug.apply(getLineColor()), x1, 0, getH1(ug.getStringBounder()));
				if (last != null)
					printMonth(ug, last, lastChangeMonth, x1);

				lastChangeMonth = x1;
				last = wink.monthYear();
			}
		}
		drawVline(ug.apply(getLineColor()), getTimeScale().getEndingPosition(getMax()), (double) 0,
				getH1(ug.getStringBounder()));
		final double x1 = getTimeScale().getStartingPosition(getMax().increment());
		if (last != null && x1 > lastChangeMonth)
			printMonth(ug, last, lastChangeMonth, x1);

	}

	@Override
	protected void printVerticalSeparators(final UGraphic ug, double totalHeightWithoutFooter) {
		for (Day wink = getMin(); wink.compareTo(getMax()) <= 0; wink = wink.increment())
			if (wink.getDayOfWeek() == weekNumberStrategy.getFirstDayOfWeek())
				drawVline(ug.apply(getLineColor()), getTimeScale().getStartingPosition(wink),
						getH1(ug.getStringBounder()), totalHeightWithoutFooter);

		drawVline(ug.apply(getLineColor()), getTimeScale().getEndingPosition(getMax()), getH1(ug.getStringBounder()),
				totalHeightWithoutFooter);
		super.printVerticalSeparators(ug, totalHeightWithoutFooter);
	}

	private void printDaysOfMonth(final UGraphic ug) {
		for (Day wink = getMin(); wink.compareTo(getMax()) < 0; wink = wink.increment()) {
			if (wink.getDayOfWeek() == weekNumberStrategy.getFirstDayOfWeek()) {
				final String num;
				if (withCalendarDate)
					num = "" + wink.getDayOfMonth();
				else
					num = "" + wink.getWeekOfYear(weekNumberStrategy);
				final TextBlock textBlock = getTextBlock(SName.day, num, false, openFontColor());
				printLeft(ug.apply(UTranslate.dy(getH1(ug.getStringBounder()))), textBlock,
						getTimeScale().getStartingPosition(wink) + 5);
			}
		}
	}

	private void printMonth(UGraphic ug, MonthYear monthYear, double start, double end) {
		final TextBlock small = getTextBlock(SName.month, monthYear.shortName(locale()), true, openFontColor());
		final TextBlock big = getTextBlock(SName.month, monthYear.shortNameYYYY(locale()), true, openFontColor());
		printCentered(ug, false, start, end, small, big);
	}

	private void printLeft(UGraphic ug, TextBlock text, double start) {
		text.drawU(ug.apply(UTranslate.dx(start)));
	}

}
