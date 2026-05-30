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
import net.sourceforge.plantuml.gantt.data.WorkingHours;
import net.sourceforge.plantuml.gantt.time.TimePoint;
import net.sourceforge.plantuml.gantt.time.TimeStringUtils;
import net.sourceforge.plantuml.gantt.timescale.VariantTimeScale;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.style.SName;

class TimeHeaderVariant extends TimeHeaderCalendar {

	private final WorkingHours workingHours;

	public TimeHeaderVariant(VariantTimeScale timeScale, WeekConfigData weekConfigData, DayCalendarData dayCalendar,
			TimeBoundsData timeBounds, TimeScaleConfigData scaleConfig, TimelineStyleData timelineStyle,
			WorkingHours workingHours) {
		super(weekConfigData, dayCalendar, timeBounds, scaleConfig, timelineStyle, timeScale);
		// super(timeScale, weekConfigData, dayCalendar, timeBounds, scaleConfig, timelineStyle);
		this.workingHours = workingHours;
	}

	@Override
	public double getFullHeaderHeight(StringBounder stringBounder) {
		return getTimeHeaderHeight(stringBounder) + getHeaderNameDayHeight();
	}

	@Override
	public double getTimeHeaderHeight(StringBounder stringBounder) {
		final double h = timelineStyle.getFontSizeDay();
		return h + 6;
	}

	@Override
	public double getTimeFooterHeight(StringBounder stringBounder) {
		final double h = timelineStyle.getFontSizeDay();
		return h + 6;
	}

	private double getHeaderNameDayHeight() {
		return 0;
	}

//	private int delta = 0;
//
//	private TimePoint increment(TimePoint day) {
//		if (delta == 0)
//			initDelta(day);
//
//		for (int i = 0; i < delta; i++)
//			day = day.increment(scaleConfig.getPrintScale());
//
//		return day;
//	}
//
//	private LocalDate increment(LocalDate day) {
//		if (delta == 0)
//			initDelta(day);
//
//		for (int i = 0; i < delta; i++)
//			day = increment(day, scaleConfig.getPrintScale());
//
//		return day;
//	}

	private LocalDate increment(LocalDate day/* , PrintScale printScale */) {
//		if (printScale == PrintScale.WEEKLY)
//			return day.plusDays(7);
		return day.plusDays(1);
	}

//	private void initDelta(LocalDate day) {
//		if (scaleConfig.getPrintScale() == PrintScale.DAILY) {
//			final double x1 = getTimeScale().getPosition(TimePoint.ofStartOfDay(day));
//			do {
//				delta++;
//				day = day.plusDays(1);
//			} while (getTimeScale().getPosition(TimePoint.ofStartOfDay(day)) < x1 + 16);
//		} else {
//			delta = 1;
//		}
//
//	}
//
//	private void initDelta(TimePoint day) {
//		if (scaleConfig.getPrintScale() == PrintScale.DAILY) {
//			final double x1 = getTimeScale().getPosition(day);
//			do {
//				delta++;
//				day = day.increment();
//			} while (getTimeScale().getPosition(day) < x1 + 16);
//		} else {
//			delta = 1;
//		}
//
//	}

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

//	@Override
//	protected boolean isZeroOnDay(TimePoint instant) {
//		return false;
//	}
//
//	@Override
//	public void drawTimeHeader(UGraphic ug, double totalHeightWithoutFooter) {
//		drawTextsDayOfWeek(ug);
//		// drawColorsBackground(ug, totalHeightWithoutFooter);
//		drawSmallVlinesDay(ug, totalHeightWithoutFooter);
//		workingHours.drawOneDay(ug, 8);
//		printVerticalSeparators(ug, totalHeightWithoutFooter);
//		drawSimpleDayCounter(ug);
//
//	}

	@Override
	public void drawTimeFooter(UGraphic ug) {
		ug = ug.apply(UTranslate.dy(3));
		// drawSimpleDayCounter(ug);
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
	
	private void drawTextsDayOfWeek(UGraphic ug) {

		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);

			if (isHidden(wink))
				continue;
			final double x1 = getTimeScale().getPosition(wink);
			final double x2 = getTimeScale().getPosition(wink.increment());
			final FontConfiguration fc = getFc(wink);

			printCentered(ug, getTextBlockSLOW(TimeStringUtils.shortName(wink.toDayOfWeek(), locale()), fc), x1, x2);
		}
	}
	
	private FontConfiguration fc1;
	private FontConfiguration fc2;

	private FontConfiguration getFc(TimePoint wink) {
		if (isZeroOnDay(wink)) {
			if (fc1 == null)
				fc1 = getFontConfigurationSLOW(SName.day, false, closedFontColor());
			return fc1;
		}

		if (fc2 == null)
			fc2 = getFontConfigurationSLOW(SName.day, false, openFontColor());
		return fc2;
	}

	
	private boolean isHidden(TimePoint wink) {
		if (scaleConfig.isHideClosed() && dayCalendar.getOpenClose().isClosed(wink.toDay()))
			return true;
		return false;
	}





	@Override
	public void drawTimeHeaderInternal(UGraphic ug, double totalHeightWithoutFooter) {
		drawTextsDayOfWeek(ug);
//		// drawColorsBackground(ug, totalHeightWithoutFooter);
		drawSmallVlinesDay(ug, totalHeightWithoutFooter);
		workingHours.drawOneDay(ug, 8);
		printVerticalSeparators(ug, totalHeightWithoutFooter);
//		drawSimpleDayCounter(ug);
		
	}

}
