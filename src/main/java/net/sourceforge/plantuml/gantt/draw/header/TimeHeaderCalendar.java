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
import java.util.Locale;

import net.sourceforge.plantuml.gantt.data.DayCalendarData;
import net.sourceforge.plantuml.gantt.data.TimeBoundsData;
import net.sourceforge.plantuml.gantt.data.TimeScaleConfigData;
import net.sourceforge.plantuml.gantt.data.TimelineStyleData;
import net.sourceforge.plantuml.gantt.data.WeekConfigData;
import net.sourceforge.plantuml.gantt.ngm.math.PiecewiseConstantUtils;
import net.sourceforge.plantuml.gantt.time.TimePoint;
import net.sourceforge.plantuml.gantt.timescale.TimeScale;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.SName;

abstract class TimeHeaderCalendar extends TimeHeader {

	public TimeHeaderCalendar(WeekConfigData weekConfigData, DayCalendarData dayCalendar, TimeBoundsData timeBounds,
			TimeScaleConfigData scaleConfig, TimelineStyleData timelineStyle, TimeScale timeScale) {
		super(timeScale, weekConfigData, dayCalendar, timeBounds, scaleConfig, timelineStyle);
	}

	protected final Locale locale() {
		return weekConfigData.getLocale();
	}

	@Override
	protected final boolean isZeroOnDay(TimePoint instant) {
		return PiecewiseConstantUtils.isZeroOnDay(dayCalendar.getOpenClose().asPiecewiseConstant(), instant.toDay());
	}

	@Override
	final public void drawTimeHeader(final UGraphic ug, double totalHeightWithoutFooter) {
		drawColorsBackground(ug, totalHeightWithoutFooter);
		drawTimeHeaderInternal(ug, totalHeightWithoutFooter);
	}

	public abstract void drawTimeHeaderInternal(final UGraphic ug, double totalHeightWithoutFooter);

	protected final void printNamedDays(final UGraphic ug) {
		if (dayCalendar.getNameDays().size() > 0) {
			String last = null;
			final FontConfiguration fc = getFontConfigurationSLOW(SName.month, false, openFontColor());
			for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
				final TimePoint wink = TimePoint.ofStartOfDay(day);
				final String name = dayCalendar.getDayName(wink);
				if (name != null && name.equals(last) == false) {
					final double x1 = getTimeScale().getPosition(wink);
					final TextBlock label = getTextBlockSLOW(name, fc);

					final double position = getTimeHeaderHeight(ug.getStringBounder());
					label.drawU(ug.apply(new UTranslate(x1, position)));
				}
				last = name;
			}
		}
	}

}
