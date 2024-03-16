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

import java.util.Map;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.project.TimeHeaderParameters;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.MonthYear;
import net.sourceforge.plantuml.project.timescale.TimeScaleDaily;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;

public class TimeHeaderDaily extends TimeHeaderCalendar {

	private double getH1(StringBounder stringBounder) {
		final double h = thParam.getStyle(SName.timeline, SName.month).value(PName.FontSize).asDouble() + 2;
		return h;
	}

	private double getH2(StringBounder stringBounder) {
		final double h = thParam.getStyle(SName.timeline, SName.day).value(PName.FontSize).asDouble() + 2;
		return getH1(stringBounder) + h;
	}

	private double getH3(StringBounder stringBounder) {
		final double h = thParam.getStyle(SName.timeline, SName.day).value(PName.FontSize).asDouble() + 3;
		return getH2(stringBounder) + h;
	}

	@Override
	public double getTimeHeaderHeight(StringBounder stringBounder) {
		return getH3(stringBounder);
	}

	@Override
	public double getTimeFooterHeight(StringBounder stringBounder) {
		final double h1 = thParam.getStyle(SName.timeline, SName.day).value(PName.FontSize).asDouble();
		final double h2 = thParam.getStyle(SName.timeline, SName.day).value(PName.FontSize).asDouble();
		final double h3 = thParam.getStyle(SName.timeline, SName.month).value(PName.FontSize).asDouble();
		return h1 + h2 + h3 + 8;
	}

	private double getHeaderNameDayHeight() {
		if (nameDays.size() > 0) {
			final double h = thParam.getStyle(SName.timeline, SName.day).value(PName.FontSize).asDouble() + 6;
			return h;
		}

		return 0;
	}

	@Override
	public double getFullHeaderHeight(StringBounder stringBounder) {
		return getTimeHeaderHeight(stringBounder) + getHeaderNameDayHeight();
	}

	private final Map<Day, String> nameDays;

	public TimeHeaderDaily(StringBounder stringBounder, TimeHeaderParameters thParam, Map<Day, String> nameDays,
			Day printStart) {
		super(thParam, new TimeScaleDaily(thParam.getCellWidth(stringBounder), thParam.getStartingDay(),
				thParam.getScale(), printStart));
		this.nameDays = nameDays;
	}

	@Override
	public void drawTimeHeader(final UGraphic ug, double totalHeightWithoutFooter) {
		drawTextsBackground(ug, totalHeightWithoutFooter);
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
		final UGraphic ugVerticalSeparator = thParam.forVerticalSeparator(ug);
		final UGraphic ugLineColor = ug.apply(getLineColor());
		for (Day wink = getMin(); wink.compareTo(getMax()) <= 0; wink = wink.increment())
			if (isBold2(wink))
				drawVline(ugVerticalSeparator, getTimeScale().getStartingPosition(wink),
						getFullHeaderHeight(ug.getStringBounder()), totalHeightWithoutFooter);
			else
				drawVline(ugLineColor, getTimeScale().getStartingPosition(wink),
						getFullHeaderHeight(ug.getStringBounder()), totalHeightWithoutFooter);

		drawVline(ugLineColor, getTimeScale().getEndingPosition(getMax()), getFullHeaderHeight(ug.getStringBounder()),
				totalHeightWithoutFooter);
	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		final double h = thParam.getStyle(SName.timeline, SName.day).value(PName.FontSize).asDouble() + 2;
		drawTextsDayOfWeek(ug);
		drawTextDayOfMonth(ug.apply(UTranslate.dy(h + 2)));
		drawMonths(ug.apply(UTranslate.dy(2 * h + 3)));
	}

	private void drawTextsDayOfWeek(UGraphic ug) {
		for (Day wink = getMin(); wink.compareTo(getMax()) <= 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			final double x2 = getTimeScale().getEndingPosition(wink);
			final HColor textColor = getTextBackColor(wink);
			printCentered(ug, getTextBlock(SName.day, wink.getDayOfWeek().shortName(locale()), false, textColor), x1,
					x2);
		}
	}

	private void drawTextDayOfMonth(UGraphic ug) {
		for (Day wink = getMin(); wink.compareTo(getMax()) <= 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			final double x2 = getTimeScale().getEndingPosition(wink);
			final HColor textColor = getTextBackColor(wink);
			printCentered(ug, getTextBlock(SName.day, "" + wink.getDayOfMonth(), false, textColor), x1, x2);
		}
	}

	private HColor getTextBackColor(Day wink) {
		if (getLoadAt(wink) <= 0)
			return closedFontColor();

		return openFontColor();
	}

	private void drawMonths(final UGraphic ug) {
		MonthYear last = null;
		double lastChangeMonth = -1;
		for (Day wink = getMin(); wink.compareTo(getMax()) <= 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			if (wink.monthYear().equals(last) == false) {
				if (last != null)
					printMonth(ug, last, lastChangeMonth, x1);

				lastChangeMonth = x1;
				last = wink.monthYear();
			}
		}
		final double x1 = getTimeScale().getStartingPosition(getMax().increment());
		if (x1 > lastChangeMonth)
			printMonth(ug, last, lastChangeMonth, x1);

	}

	private void printMonth(UGraphic ug, MonthYear monthYear, double start, double end) {
		final TextBlock tiny = getTextBlock(SName.month, monthYear.shortName(locale()), true, openFontColor());
		final TextBlock small = getTextBlock(SName.month, monthYear.longName(locale()), true, openFontColor());
		final TextBlock big = getTextBlock(SName.month, monthYear.longNameYYYY(locale()), true, openFontColor());
		printCentered(ug, false, start, end, tiny, small, big);
	}

	private void printNamedDays(final UGraphic ug) {
		if (nameDays.size() > 0) {
			String last = null;
			for (Day wink = getMin(); wink.compareTo(getMax().increment()) <= 0; wink = wink.increment()) {
				final String name = nameDays.get(wink);
				if (name != null && name.equals(last) == false) {
					final double x1 = getTimeScale().getStartingPosition(wink);
					final double x2 = getTimeScale().getEndingPosition(wink);
					final TextBlock label = getTextBlock(SName.month, name, false, openFontColor());
					final double h = label.calculateDimension(ug.getStringBounder()).getHeight();
					double y1 = getTimeHeaderHeight(ug.getStringBounder());
					double y2 = getFullHeaderHeight(ug.getStringBounder());

					final double position = getH3(ug.getStringBounder());
					label.drawU(ug.apply(new UTranslate(x1, position)));
				}
				last = name;
			}
		}
	}

}
