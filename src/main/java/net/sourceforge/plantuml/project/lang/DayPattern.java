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
package net.sourceforge.plantuml.project.lang;

import java.time.LocalDate;
import java.time.Month;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.builder.UBrexOr;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.project.time.MonthUtils;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;

public class DayPattern {

	private final String id;
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
		this.id = id;
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

	public IRegex toRegex() {
		return new RegexOr(TimeResolution.toRegexA_DD_MONTH_YYYY(yearKeyA, monthKeyA, dayKeyA),
				TimeResolution.toRegexB_YYYY_MM_DD(yearKeyB, monthKeyB, dayKeyB),
				TimeResolution.toRegexC_MONTH_DD_YYYY(yearKeyC, monthKeyC, dayKeyC));
	}

	public UBrexPart toUbrex() {
		return new UBrexOr(TimeResolution.toUbrexA_DD_MONTH_YYYY(yearKeyA, monthKeyA, dayKeyA),
				TimeResolution.toUbrexB_YYYY_MM_DD(yearKeyB, monthKeyB, dayKeyB),
				TimeResolution.toUbrexC_MONTH_DD_YYYY(yearKeyC, monthKeyC, dayKeyC));
	}

	public LocalDate getDay(RegexResult arg) {
		if (arg.get(dayKeyA, 0) != null)
			return resultA(arg);

		if (arg.get(dayKeyB, 0) != null)
			return resultB(arg);

		if (arg.get(dayKeyC, 0) != null)
			return resultC(arg);
		return null;
	}

	public LocalDate getDay(UMatcher arg) {
		return resultA(arg);
	}

	private LocalDate resultA(UMatcher arg) {
		final int day = Integer.parseInt(arg.getCapture(dayKeyA).get(0));
		final Month month = MonthUtils.fromString(arg.getCapture(monthKeyA).get(0));
		final int year = Integer.parseInt(arg.getCapture(yearKeyA).get(0));
		return LocalDate.of(year, month, day);
	}

	private LocalDate resultA(RegexResult arg) {
		final int day = Integer.parseInt(arg.get(dayKeyA, 0));
		final Month month = MonthUtils.fromString(arg.get(monthKeyA, 0));
		final int year = Integer.parseInt(arg.get(yearKeyA, 0));
		return LocalDate.of(year, month, day);
	}

	private LocalDate resultB(RegexResult arg) {
		final int day = Integer.parseInt(arg.get(dayKeyB, 0));
		final int month = Integer.parseInt(arg.get(monthKeyB, 0));
		final int year = Integer.parseInt(arg.get(yearKeyB, 0));
		return LocalDate.of(year, month, day);
	}

	private LocalDate resultC(RegexResult arg) {
		final int day = Integer.parseInt(arg.get(dayKeyC, 0));
		final Month month = MonthUtils.fromString(arg.get(monthKeyC, 0));
		final int year = Integer.parseInt(arg.get(yearKeyC, 0));
		return LocalDate.of(year, month, day);
	}

}
