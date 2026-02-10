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
import java.util.Locale;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.project.data.DayCalendarData;
import net.sourceforge.plantuml.project.data.TimeBoundsData;
import net.sourceforge.plantuml.project.data.TimeScaleConfigData;
import net.sourceforge.plantuml.project.data.TimelineStyleData;
import net.sourceforge.plantuml.project.data.WeekConfigData;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstantUtils;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.style.SName;

abstract class TimeHeaderCalendar extends TimeHeader {

	public TimeHeaderCalendar(WeekConfigData weekConfigData, DayCalendarData dayCalendar, TimeBoundsData timeBounds,
			TimeScaleConfigData scaleConfig, TimelineStyleData timelineStyle, TimeScale timeScale) {
		super(timeScale, weekConfigData, dayCalendar, timeBounds, scaleConfig, timelineStyle);
	}

	protected final Locale locale() {
		return weekConfigData.getLocale();
	}

	protected final int getLoadAt(TimePoint instant) {
		if (PiecewiseConstantUtils.isZeroOnDay(dayCalendar.getOpenClose().asPiecewiseConstant(), instant.toDay()))
			return 0;

		return 100;
	}

	@Override
	final public void drawTimeHeader(final UGraphic ug, double totalHeightWithoutFooter) {
		drawColorsBackground(ug, totalHeightWithoutFooter);
		drawTimeHeaderInternal(ug, totalHeightWithoutFooter);
	}

	public abstract void drawTimeHeaderInternal(final UGraphic ug, double totalHeightWithoutFooter);

	private final void drawColorsBackground(UGraphic ug, double totalHeightWithoutFooter) {

		final double height = totalHeightWithoutFooter - getFullHeaderHeight(ug.getStringBounder());
		Pending pending = null;

		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			final double x1 = getTimeScale().getPosition(wink);
			final double x2 = getTimeScale().getPosition(wink) + getTimeScale().getWidth(wink);
			HColor back = getColor(wink);
			// Day of week should be stronger than period of time (back color).
			final HColor backDoW = getColor(wink.toDayOfWeek());
			if (backDoW != null)
				back = backDoW;

			if (back == null && getLoadAt(wink) == 0)
				back = closedBackgroundColor();

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

		if (pending != null)
			pending.draw(ug, height);

	}
	
	
	protected final void printNamedDays(final UGraphic ug) {
		if (dayCalendar.getNameDays().size() > 0) {
			String last = null;
			for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
				final TimePoint wink = TimePoint.ofStartOfDay(day);
				final String name = dayCalendar.getDayName(wink);
				if (name != null && name.equals(last) == false) {
					final double x1 = getTimeScale().getPosition(wink);
					final TextBlock label = getTextBlock(SName.month, name, false, openFontColor());

					final double position = getTimeHeaderHeight(ug.getStringBounder());
					label.drawU(ug.apply(new UTranslate(x1, position)));
				}
				last = name;
			}
		}
	}



}
