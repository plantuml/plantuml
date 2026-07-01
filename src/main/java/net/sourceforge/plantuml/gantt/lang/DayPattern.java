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
package net.sourceforge.plantuml.gantt.lang;

import java.time.LocalDate;
import java.time.Month;

import com.plantuml.ubrex.CaptureLookup;
import com.plantuml.ubrex.builder.UBrexOr;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.gantt.time.MonthUtils;

public class DayPattern {

	private final String yearKeyA;
	private final String yearKeyB;
	private final String yearKeyC;
	private final String monthKeyA;
	private final String monthKeyB;
	private final String monthKeyC;
	private final String dayKeyA;
	private final String dayKeyB;
	private final String dayKeyC;

	public DayPattern(String id) {
		this.yearKeyA = "AYEAR" + id;
		this.yearKeyB = "BYEAR" + id;
		this.yearKeyC = "CYEAR" + id;
		this.monthKeyA = "AMONTH" + id;
		this.monthKeyB = "BMONTH" + id;
		this.monthKeyC = "CMONTH" + id;
		this.dayKeyA = "ADAY" + id;
		this.dayKeyB = "BDAY" + id;
		this.dayKeyC = "CDAY" + id;
	}

	public UBrexPart toUbrex() {
		return new UBrexOr(TimeResolution.toUbrexA_DD_MONTH_YYYY(yearKeyA, monthKeyA, dayKeyA),
				TimeResolution.toUbrexB_YYYY_MM_DD(yearKeyB, monthKeyB, dayKeyB),
				TimeResolution.toUbrexC_MONTH_DD_YYYY(yearKeyC, monthKeyC, dayKeyC));
	}

	public LocalDate getDay(CaptureLookup arg) {
		if (arg.findFirstValueByKey(dayKeyA) != null)
			return resultA(arg);

		if (arg.findFirstValueByKey(dayKeyB) != null)
			return resultB(arg);

		if (arg.findFirstValueByKey(dayKeyC) != null)
			return resultC(arg);
		return null;
	}

	private LocalDate resultA(CaptureLookup arg) {
		final int day = Integer.parseInt(arg.findFirstValueByKey(dayKeyA));
		final Month month = MonthUtils.fromString(arg.findFirstValueByKey(monthKeyA));
		final int year = Integer.parseInt(arg.findFirstValueByKey(yearKeyA));
		return LocalDate.of(year, month, day);
	}

	private LocalDate resultB(CaptureLookup arg) {
		final int day = Integer.parseInt(arg.findFirstValueByKey(dayKeyB));
		final int month = Integer.parseInt(arg.findFirstValueByKey(monthKeyB));
		final int year = Integer.parseInt(arg.findFirstValueByKey(yearKeyB));
		return LocalDate.of(year, month, day);
	}

	private LocalDate resultC(CaptureLookup arg) {
		final int day = Integer.parseInt(arg.findFirstValueByKey(dayKeyC));
		final Month month = MonthUtils.fromString(arg.findFirstValueByKey(monthKeyC));
		final int year = Integer.parseInt(arg.findFirstValueByKey(yearKeyC));
		return LocalDate.of(year, month, day);
	}

}
