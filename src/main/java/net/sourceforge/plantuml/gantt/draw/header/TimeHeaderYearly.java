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
import java.time.YearMonth;

import net.sourceforge.plantuml.gantt.data.DayCalendarData;
import net.sourceforge.plantuml.gantt.data.TimeBoundsData;
import net.sourceforge.plantuml.gantt.data.TimeScaleConfigData;
import net.sourceforge.plantuml.gantt.data.TimelineStyleData;
import net.sourceforge.plantuml.gantt.data.WeekConfigData;
import net.sourceforge.plantuml.gantt.time.TimePoint;
import net.sourceforge.plantuml.gantt.timescale.TimeScale;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.SName;

class TimeHeaderYearly extends TimeHeaderCalendar {

	public TimeHeaderYearly(TimeScale timeScale, WeekConfigData weekConfigData, DayCalendarData dayCalendar,
			TimeBoundsData timeBounds, TimeScaleConfigData scaleConfig, TimelineStyleData timelineStyle) {
		super(weekConfigData, dayCalendar, timeBounds, scaleConfig, timelineStyle, timeScale);
	}

	@Override
	public double getTimeHeaderHeight(StringBounder stringBounder) {
		final double h1 = timelineStyle.getFontSizeYear();
		return h1 + 3;
	}

	@Override
	public double getTimeFooterHeight(StringBounder stringBounder) {
		final double h1 = timelineStyle.getFontSizeYear();
		return h1 + 3;
	}

	@Override
	public double getFullHeaderHeight(StringBounder stringBounder) {
		return getTimeHeaderHeight(stringBounder);
	}

	@Override
	public void drawTimeHeaderInternal(final UGraphic ug, double totalHeightWithoutFooter) {
		drawYears(ug);
		printVerticalSeparators(ug, totalHeightWithoutFooter);
		drawHline(ug, 0);
		drawHline(ug, getFullHeaderHeight(ug.getStringBounder()));
	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		// ug = ug.apply(UTranslate.dy(3));
		drawYears(ug);
		drawHline(ug, 0);
		drawHline(ug, getTimeFooterHeight(ug.getStringBounder()));
	}

	private void drawYears(final UGraphic ug) {
		final double h1 = timelineStyle.getFontSizeYear();

		final FontConfiguration fc = getFontConfigurationSLOW(SName.year, true, openFontColor());

		YearMonth last = null;
		double lastChange = -1;
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			final double x1 = getTimeScale().getPosition(wink);
			if (last == null || wink.monthYear().getYear() != last.getYear()) {
				drawVline(ug.apply(getLineColor()), x1, 0, h1 + 2);
				if (last != null)
					printYear(ug, last, lastChange, x1, fc);

				lastChange = x1;
				last = wink.monthYear();
			}
		}
		final double x1 = getTimeScale().getPosition(TimePoint.ofStartOfDay(getMaxDay().plusDays(1)));
		if (x1 > lastChange)
			printYear(ug, last, lastChange, x1, fc);

		final double end = x1;
		drawVline(ug.apply(getLineColor()), end, 0, h1 + 2);
	}

	private void printYear(UGraphic ug, YearMonth monthYear, double start, double end, FontConfiguration fc) {
		final TextBlock small = getTextBlockSLOW("" + monthYear.getYear(), fc);
		printCentered(ug, true, start, end, small);
	}

}
