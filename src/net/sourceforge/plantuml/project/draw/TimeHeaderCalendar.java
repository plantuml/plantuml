/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import java.util.Locale;
import java.util.Map;

import net.sourceforge.plantuml.ThemeStyle;
import net.sourceforge.plantuml.project.LoadPlanable;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public abstract class TimeHeaderCalendar extends TimeHeader {

	protected final LoadPlanable defaultPlan;
	protected final Map<Day, HColor> colorDays;
	protected final Map<DayOfWeek, HColor> colorDaysOfWeek;
	protected final Locale locale;

	public TimeHeaderCalendar(Locale locale, Style timelineStyle, Style closedStyle, Day calendar, Day min, Day max,
			LoadPlanable defaultPlan, Map<Day, HColor> colorDays, Map<DayOfWeek, HColor> colorDaysOfWeek,
			TimeScale timeScale, HColorSet colorSet, ThemeStyle themeStyle) {
		super(timelineStyle, closedStyle, min, max, timeScale, colorSet, themeStyle);
		this.locale = locale;
		this.defaultPlan = defaultPlan;
		this.colorDays = colorDays;
		this.colorDaysOfWeek = colorDaysOfWeek;
	}

	// Duplicate in TimeHeaderSimple
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

		final double height = totalHeightWithoutFooter - getFullHeaderHeight();
		Pending pending = null;

		for (Day wink = min; wink.compareTo(max) <= 0; wink = wink.increment()) {
			final double x1 = getTimeScale().getStartingPosition(wink);
			final double x2 = getTimeScale().getEndingPosition(wink);
			HColor back = colorDays.get(wink);
			// Day of week should be stronger than period of time (back color).
			final HColor backDoW = colorDaysOfWeek.get(wink.getDayOfWeek());
			if (backDoW != null) {
				back = backDoW;
			}
			if (back == null && defaultPlan.getLoadAt(wink) == 0) {
				back = closedBackgroundColor();
			}
			if (back == null) {
				if (pending != null)
					pending.draw(ug, height);
				pending = null;
			} else {
				if (pending != null && pending.color.equals(back) == false) {
					pending.draw(ug, height);
					pending = null;
				}
				if (pending == null) {
					pending = new Pending(back, x1, x2);
				} else {
					pending.x2 = x2;
				}
			}
		}
	}

}
