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

import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.project.GanttPreparedModel;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.style.SName;

public class TimeHeaderYearly extends TimeHeaderCalendar {

	public TimeHeaderYearly(GanttPreparedModel model) {
		super(model, model.yearly());
	}

	@Override
	public double getTimeHeaderHeight(StringBounder stringBounder) {
		final double h1 = getFontSizeYear().asDouble();
		return h1 + 3;
	}

	@Override
	public double getTimeFooterHeight(StringBounder stringBounder) {
		final double h1 = getFontSizeYear().asDouble();
		return h1 + 3;
	}

	@Override
	public double getFullHeaderHeight(StringBounder stringBounder) {
		return getTimeHeaderHeight(stringBounder);
	}

	@Override
	public void drawTimeHeader(final UGraphic ug, double totalHeightWithoutFooter) {
		drawTextsBackground(ug, totalHeightWithoutFooter);
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
		final double h1 = getFontSizeYear().asDouble();

		YearMonth last = null;
		double lastChange = -1;
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
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

	private void printYear(UGraphic ug, YearMonth monthYear, double start, double end) {
		final TextBlock small = getTextBlock(SName.year, "" + monthYear.getYear(), true, openFontColor());
		printCentered(ug, true, start, end, small);
	}

}
