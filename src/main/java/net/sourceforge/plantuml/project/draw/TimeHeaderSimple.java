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
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;
import net.sourceforge.plantuml.project.TimeHeaderParameters;
import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScaleWink;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;

public class TimeHeaderSimple extends TimeHeader {

	private final PrintScale printScale;

	@Override
	public double getFullHeaderHeight(StringBounder stringBounder) {
		return getTimeHeaderHeight(stringBounder) + getHeaderNameDayHeight();
	}

	@Override
	public double getTimeHeaderHeight(StringBounder stringBounder) {
		final double h = thParam.getStyle(SName.timeline, SName.day).value(PName.FontSize).asDouble();
		return h + 6;
	}

	@Override
	public double getTimeFooterHeight(StringBounder stringBounder) {
		final double h = thParam.getStyle(SName.timeline, SName.day).value(PName.FontSize).asDouble();
		return h + 6;
	}

	private double getHeaderNameDayHeight() {
		return 0;
	}

	public TimeHeaderSimple(StringBounder stringBounder, TimeHeaderParameters thParam, PrintScale printScale) {
		super(thParam, new TimeScaleWink(thParam.getCellWidth(stringBounder), thParam.getScale(), printScale));
		this.printScale = printScale;
	}

	private int delta = 0;

	private Day increment(Day day) {
		if (delta == 0)
			initDelta(day);

		for (int i = 0; i < delta; i++)
			day = day.increment(printScale);

		return day;
	}

	private void initDelta(Day day) {
		if (printScale == PrintScale.DAILY) {
			final double x1 = getTimeScale().getStartingPosition(day);
			do {
				delta++;
				day = day.increment();
			} while (getTimeScale().getStartingPosition(day) < x1 + 16);
		} else {
			delta = 1;
		}

	}

	private void drawSmallVlinesDay(UGraphic ug, double totalHeightWithoutFooter) {
		ug = ug.apply(getLineColor());
		ug = ug.apply(UTranslate.dy(6));
		final ULine vbar = ULine.vline(totalHeightWithoutFooter + 2);
		for (Day i = getMin(); i.compareTo(getMax().increment()) <= 0; i = increment(i)) {
			final double x1 = getTimeScale().getStartingPosition(i);
			ug.apply(UTranslate.dx(x1)).draw(vbar);
		}
	}

	private void drawSimpleDayCounter(UGraphic ug) {
		for (Day i = getMin(); i.compareTo(getMax().increment()) <= 0; i = increment(i)) {
			final int value;
			if (printScale == PrintScale.WEEKLY)
				value = i.getAbsoluteDayNum() / 7 + 1;
			else
				value = i.getAbsoluteDayNum() + 1;
			final UFont font = thParam.getStyle(SName.timeline, SName.day).getUFont();
			final FontConfiguration fontConfiguration = getFontConfiguration(font, false, openFontColor());
			final TextBlock num = Display.getWithNewlines(getPragma(), "" + value).create(fontConfiguration,
					HorizontalAlignment.LEFT, new SpriteContainerEmpty());
			final double x1 = getTimeScale().getStartingPosition(i);
			final double x2;
			if (printScale == PrintScale.WEEKLY)
				x2 = getTimeScale().getEndingPosition(i.addDays(6));
			else
				x2 = getTimeScale().getStartingPosition(increment(i));
			final double width = num.calculateDimension(ug.getStringBounder()).getWidth();
			final double delta = (x2 - x1) - width;
			if (i.compareTo(getMax().increment()) < 0)
				num.drawU(ug.apply(UTranslate.dx(x1 + delta / 2)));

		}
	}

	@Override
	public void drawTimeHeader(UGraphic ug, double totalHeightWithoutFooter) {
//		final double xmin = getTimeScale().getStartingPosition(getMin());
//		final double xmax = getTimeScale().getEndingPosition(getMax());
		drawSmallVlinesDay(ug, totalHeightWithoutFooter);
		printVerticalSeparators(ug, totalHeightWithoutFooter);
		drawSimpleDayCounter(ug);

	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
//		final double xmin = getTimeScale().getStartingPosition(getMin());
//		final double xmax = getTimeScale().getEndingPosition(getMax());
		ug = ug.apply(UTranslate.dy(3));
		drawSimpleDayCounter(ug);
	}

	// Duplicate in TimeHeaderDaily
	class Pending {
		final double x1;
		double x2;
		final HColor color;

		Pending(HColor color, double x1, double x2) {
			this.x1 = x1;
			this.x2 = x2;
			this.color = color;
		}

		public void draw(UGraphic ug, double height) {
			drawRectangle(ug.apply(color.bg()), height, x1, x2);
		}
	}

	protected final void drawTextsBackground(UGraphic ug, double totalHeightWithoutFooter) {

		final double height = totalHeightWithoutFooter - getFullHeaderHeight(ug.getStringBounder());
		Pending pending = null;

		for (Day wink = getMin(); wink.compareTo(getMax()) <= 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			final double x2 = getTimeScale().getEndingPosition(wink);
			HColor back = thParam.getColor(wink);
//			// Day of week should be stronger than period of time (back color).
//			final HColor backDoW = colorDaysOfWeek.get(wink.getDayOfWeek());
//			if (backDoW != null) {
//				back = backDoW;
//			}
//			if (back == null && defaultPlan.getLoadAt(wink) == 0) {
//				back = closedBackgroundColor();
//			}
			if (back == null) {
				if (pending != null)
					pending.draw(ug, height);
				pending = null;
			} else {
				if (pending != null && pending.color.equals(back) == false) {
					pending.draw(ug, height);
					pending = null;
				}
				if (pending == null)
					pending = new Pending(back, x1, x2);
				else
					pending.x2 = x2;

			}
		}
	}

}
