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

import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.Month;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
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
		return new RegexOr(toRegexA_DD_MONTH_YYYY(), toRegexB_YYYY_MM_DD(), toRegexC_MONTH_DD_YYYY());
	}

	public Day getDay(RegexResult arg) {
		if (arg.get(dayKeyA, 0) != null)
			return resultA(arg);

		if (arg.get(dayKeyB, 0) != null)
			return resultB(arg);

		if (arg.get(dayKeyC, 0) != null)
			return resultC(arg);
		return null;
	}

	private IRegex toRegexA_DD_MONTH_YYYY() {
		return new RegexConcat( //
				new RegexLeaf(dayKeyA, "([\\d]{1,2})"), //
				new RegexLeaf("[\\w, ]*?"), //
				new RegexLeaf(monthKeyA, "(" + Month.getRegexString() + ")"), //
				new RegexLeaf("[\\w, ]*?"), //
				new RegexLeaf(yearKeyA, "([\\d]{1,4})"));
	}

	private Day resultA(RegexResult arg) {
		final int day = Integer.parseInt(arg.get(dayKeyA, 0));
		final String month = arg.get(monthKeyA, 0);
		final int year = Integer.parseInt(arg.get(yearKeyA, 0));
		return Day.create(year, month, day);
	}

	private IRegex toRegexB_YYYY_MM_DD() {
		return new RegexConcat( //
				new RegexLeaf(yearKeyB, "([\\d]{1,4})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf(monthKeyB, "([\\d]{1,2})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf(dayKeyB, "([\\d]{1,2})"));
	}

	private Day resultB(RegexResult arg) {
		final int day = Integer.parseInt(arg.get(dayKeyB, 0));
		final int month = Integer.parseInt(arg.get(monthKeyB, 0));
		final int year = Integer.parseInt(arg.get(yearKeyB, 0));
		return Day.create(year, month, day);
	}

	private IRegex toRegexC_MONTH_DD_YYYY() {
		return new RegexConcat( //
				new RegexLeaf(monthKeyC, "(" + Month.getRegexString() + ")"), //
				new RegexLeaf("[\\w, ]*?"), //
				new RegexLeaf(dayKeyC, "([\\d]{1,2})"), //
				new RegexLeaf("[\\w, ]*?"), //
				new RegexLeaf(yearKeyC, "([\\d]{1,4})"));
	}

	private Day resultC(RegexResult arg) {
		final int day = Integer.parseInt(arg.get(dayKeyC, 0));
		final String month = arg.get(monthKeyC, 0);
		final int year = Integer.parseInt(arg.get(yearKeyC, 0));
		return Day.create(year, month, day);
	}

}
