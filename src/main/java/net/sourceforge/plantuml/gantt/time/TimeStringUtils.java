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
package net.sourceforge.plantuml.gantt.time;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.YearMonth;
import java.util.Locale;

import net.sourceforge.plantuml.utils.I18nTimeData;

public abstract class TimeStringUtils {

	public static String shortDayOfWeek(DayOfWeek dayOfWeek, Locale locale) {
		return I18nTimeData.shortDayOfWeek(dayOfWeek, locale);
	}

	public static String shortMonthYear(YearMonth yearMonth, Locale locale) {
		return I18nTimeData.shortMonth(yearMonth.getMonth(), locale) + " " + yearMonth.getYear();
	}

	public static String longMonth(YearMonth yearMonth, Locale locale) {
		return I18nTimeData.longMonth(yearMonth.getMonth(), locale);
	}

	public static String longMonthYear(YearMonth yearMonth, Locale locale) {
		return I18nTimeData.longMonth(yearMonth.getMonth(), locale) + " " + yearMonth.getYear();
	}

	public static String shortMonth(Month month, Locale locale) {
		return I18nTimeData.shortMonth(month, locale);
	}

}
