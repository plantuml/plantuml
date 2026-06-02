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

import java.util.Locale;

import net.sourceforge.plantuml.utils.I18nTimeData;

public enum TimePointFormat {

	DAY_OF_WEEK_SHORT, DAY_OF_WEEK_LONG, MONTH_YEAR_SHORT, MONTH_YEAR_LONG, MONTH_LONG, MONTH_SHORT, YEAR, QUARTER, DAY_OF_MONTH;

	public String format(TimePoint when, Locale locale) {
		switch (this) {
		case DAY_OF_MONTH:
			return "" + when.getDayOfMonth();

		case QUARTER:
			return "" + when.quarter();

		case YEAR:
			return "" + when.year();

		case DAY_OF_WEEK_LONG:
			return I18nTimeData.dayOfWeekLong(when.toDayOfWeek(), locale);

		case DAY_OF_WEEK_SHORT:
			return I18nTimeData.dayOfWeekShort(when.toDayOfWeek(), locale);

		case MONTH_LONG:
			return I18nTimeData.monthLong(when.month(), locale);

		case MONTH_SHORT:
			return I18nTimeData.monthShort(when.month(), locale);

		case MONTH_YEAR_SHORT:
			return I18nTimeData.monthShort(when.month(), locale) + " " + when.year();

		case MONTH_YEAR_LONG:
			return I18nTimeData.monthLong(when.month(), locale) + " " + when.year();

		}
		throw new IllegalStateException();
	}

}
