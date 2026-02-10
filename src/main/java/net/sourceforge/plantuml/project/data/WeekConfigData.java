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
package net.sourceforge.plantuml.project.data;

import java.time.DayOfWeek;
import java.util.Locale;

import net.sourceforge.plantuml.project.draw.WeeklyHeaderStrategy;
import net.sourceforge.plantuml.project.time.WeekNumberStrategy;

/**
 * Value object containing week-related configuration and locale.
 */
public class WeekConfigData {

	private Locale locale = Locale.ENGLISH;

	// Let's follow ISO-8601 rules
	private WeekNumberStrategy weekNumberStrategy = new WeekNumberStrategy(DayOfWeek.MONDAY, 4);
	private WeeklyHeaderStrategy weeklyHeaderStrategy;
	private int weekStartingNumber;

	public WeekNumberStrategy getWeekNumberStrategy() {
		return weekNumberStrategy;
	}

	public WeeklyHeaderStrategy getWeeklyHeaderStrategy() {
		return weeklyHeaderStrategy;
	}

	public int getWeekStartingNumber() {
		return weekStartingNumber;
	}

	public Locale getLocale() {
		return locale;
	}

	// Setters

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setWeekNumberStrategy(WeekNumberStrategy weekNumberStrategy) {
		this.weekNumberStrategy = weekNumberStrategy;
	}

	public void setWeeklyHeaderStrategy(WeeklyHeaderStrategy weeklyHeaderStrategy) {
		this.weeklyHeaderStrategy = weeklyHeaderStrategy;
	}

	public void setWeekStartingNumber(int weekStartingNumber) {
		this.weekStartingNumber = weekStartingNumber;
	}

}
