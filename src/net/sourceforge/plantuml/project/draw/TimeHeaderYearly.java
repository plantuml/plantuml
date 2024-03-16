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

import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.project.TimeHeaderParameters;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.MonthYear;
import net.sourceforge.plantuml.project.timescale.TimeScaleCompressed;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;

public class TimeHeaderYearly extends TimeHeaderCalendar {

	@Override
	public double getTimeHeaderHeight(StringBounder stringBounder) {
		final double h1 = thParam.getStyle(SName.timeline, SName.year).value(PName.FontSize).asDouble();
		return h1 + 3;
	}

	@Override
	public double getTimeFooterHeight(StringBounder stringBounder) {
		final double h1 = thParam.getStyle(SName.timeline, SName.year).value(PName.FontSize).asDouble();
		return h1 + 3;
	}

	@Override
	public double getFullHeaderHeight(StringBounder stringBounder) {
		return getTimeHeaderHeight(stringBounder);
	}

	public TimeHeaderYearly(StringBounder stringBounder, TimeHeaderParameters thParam, Day printStart) {
		super(thParam, new TimeScaleCompressed(thParam.getCellWidth(stringBounder), thParam.getStartingDay(),
				thParam.getScale(), printStart));
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
		final double h1 = thParam.getStyle(SName.timeline, SName.year).value(PName.FontSize).asDouble();

		MonthYear last = null;
		double lastChange = -1;
		for (Day wink = getMin(); wink.compareTo(getMax()) < 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			if (last == null || wink.monthYear().year() != last.year()) {
				drawVline(ug.apply(getLineColor()), x1, 0, h1 + 2);
				if (last != null)
					printYear(ug, last, lastChange, x1);

				lastChange = x1;
				last = wink.monthYear();
			}
		}
		final double x1 = getTimeScale().getStartingPosition(getMax().increment());
		if (x1 > lastChange)
			printYear(ug, last, lastChange, x1);

		drawVline(ug.apply(getLineColor()), getTimeScale().getEndingPosition(getMax()), 0, h1 + 2);
	}

	private void printYear(UGraphic ug, MonthYear monthYear, double start, double end) {
		final TextBlock small = getTextBlock(SName.year, "" + monthYear.year(), true, openFontColor());
		printCentered(ug, true, start, end, small);
	}

}
