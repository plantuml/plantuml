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
import net.sourceforge.plantuml.project.GanttPreparedModel;
import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.style.SName;

public class TimeHeaderSimple extends TimeHeader {

	public TimeHeaderSimple(GanttPreparedModel model, StringBounder stringBounder) {
		super(model, model.simple());
	}

	@Override
	public double getFullHeaderHeight(StringBounder stringBounder) {
		return getTimeHeaderHeight(stringBounder) + getHeaderNameDayHeight();
	}

	@Override
	public double getTimeHeaderHeight(StringBounder stringBounder) {
		final double h = model.getFontSizeDay().asDouble();
		return h + 6;
	}

	@Override
	public double getTimeFooterHeight(StringBounder stringBounder) {
		final double h = model.getFontSizeDay().asDouble();
		return h + 6;
	}

	private double getHeaderNameDayHeight() {
		return 0;
	}

	private int delta = 0;

	private TimePoint increment(TimePoint day) {
		if (delta == 0)
			initDelta(day);

		for (int i = 0; i < delta; i++)
			day = day.increment(model.printScale);

		return day;
	}

	private LocalDate increment(LocalDate day) {
		if (delta == 0)
			initDelta(day);

		for (int i = 0; i < delta; i++)
			day = increment(day, model.printScale);

		return day;
	}

	private LocalDate increment(LocalDate day, PrintScale printScale) {
		if (printScale == PrintScale.WEEKLY)
			return day.plusDays(7);
		return day.plusDays(1);
	}

	private void initDelta(LocalDate day) {
		if (model.printScale == PrintScale.DAILY) {
			final double x1 = getTimeScale().getPosition(TimePoint.ofStartOfDay(day));
			do {
				delta++;
				day = day.plusDays(1);
			} while (getTimeScale().getPosition(TimePoint.ofStartOfDay(day)) < x1 + 16);
		} else {
			delta = 1;
		}

	}

	private void initDelta(TimePoint day) {
		if (model.printScale == PrintScale.DAILY) {
			final double x1 = getTimeScale().getPosition(day);
			do {
				delta++;
				day = day.increment();
			} while (getTimeScale().getPosition(day) < x1 + 16);
		} else {
			delta = 1;
		}

	}

	private void drawSmallVlinesDay(UGraphic ug, double totalHeightWithoutFooter) {
		ug = ug.apply(getLineColor());
		ug = ug.apply(UTranslate.dy(6));
		final ULine vbar = ULine.vline(totalHeightWithoutFooter + 2);
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay().plusDays(1)) <= 0; day = increment(day)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			final double x1 = getTimeScale().getPosition(wink);
			ug.apply(UTranslate.dx(x1)).draw(vbar);
		}
	}

	private void drawSimpleDayCounter(UGraphic ug) {
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay().plusDays(1)) <= 0; day = increment(day)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			final int value;
			if (model.printScale == PrintScale.WEEKLY)
				value = wink.getAbsoluteDayNum() / 7 + 1;
			else
				value = wink.getAbsoluteDayNum() + 1;
			final UFont font = model.getStyleDay().getUFont();
			final FontConfiguration fontConfiguration = getFontConfiguration(font, false, openFontColor());
			final TextBlock num = Display.getWithNewlines(getPragma(), "" + value).create(fontConfiguration,
					HorizontalAlignment.LEFT, new SpriteContainerEmpty());
			final double x1 = getTimeScale().getPosition(wink);
			final double x2;
			if (model.printScale == PrintScale.WEEKLY)
				x2 = getTimeScale().getPosition(wink.addDays(6)) + getTimeScale().getWidth(wink.addDays(6));
			else
				x2 = getTimeScale().getPosition(increment(wink));
			final double width = num.calculateDimension(ug.getStringBounder()).getWidth();
			final double delta = (x2 - x1) - width;

			if (wink.compareTo(TimePoint.ofStartOfDay(getMaxDay().plusDays(1))) < 0)
				num.drawU(ug.apply(UTranslate.dx(x1 + delta / 2)));

		}
	}

	@Override
	public void drawTimeHeader(UGraphic ug, double totalHeightWithoutFooter) {
		drawSmallVlinesDay(ug, totalHeightWithoutFooter);
		printVerticalSeparators(ug, totalHeightWithoutFooter);
		drawSimpleDayCounter(ug);

	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		ug = ug.apply(UTranslate.dy(3));
		drawSimpleDayCounter(ug);
	}

	protected final void drawTextsBackground(UGraphic ug, double totalHeightWithoutFooter) {

		final double height = totalHeightWithoutFooter - getFullHeaderHeight(ug.getStringBounder());
		Pending pending = null;

		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			final double x1 = getTimeScale().getPosition(wink);
			final double x2 = getTimeScale().getPosition(wink) + getTimeScale().getWidth(wink);
			HColor back = getColor(wink);
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
