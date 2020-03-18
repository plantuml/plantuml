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
package net.sourceforge.plantuml.project.lang;

import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.DaysAsDates;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.time.Day;

public class ComplementDates implements ComplementPattern {

	public IRegex toRegex(String suffix) {
		return new RegexConcat( //
				new RegexLeaf("YEAR1" + suffix, "([\\d]{4})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("MONTH1" + suffix, "([\\d]{1,2})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("DAY1" + suffix, "([\\d]{1,2})"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("to"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("YEAR2" + suffix, "([\\d]{4})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("MONTH2" + suffix, "([\\d]{1,2})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("DAY2" + suffix, "([\\d]{1,2})") //

		);
	}

	public Subject getSubject(GanttDiagram project, RegexResult arg) {
		final Day date1 = getDate(arg, "1");
		final Day date2 = getDate(arg, "2");
		return new DaysAsDates(date1, date2);
	}

	private Day getDate(RegexResult arg, String suffix) {
		final int day = Integer.parseInt(arg.get("DAY" + suffix, 0));
		final int month = Integer.parseInt(arg.get("MONTH" + suffix, 0));
		final int year = Integer.parseInt(arg.get("YEAR" + suffix, 0));
		return Day.create(year, month, day);
	}

	public Failable<Complement> getComplement(GanttDiagram system, RegexResult arg, String suffix) {

		final int day1 = Integer.parseInt(arg.get("DAY1" + suffix, 0));
		final int month1 = Integer.parseInt(arg.get("MONTH1" + suffix, 0));
		final int year1 = Integer.parseInt(arg.get("YEAR1" + suffix, 0));
		final Day date1 = Day.create(year1, month1, day1);

		final int day2 = Integer.parseInt(arg.get("DAY2" + suffix, 0));
		final int month2 = Integer.parseInt(arg.get("MONTH2" + suffix, 0));
		final int year2 = Integer.parseInt(arg.get("YEAR2" + suffix, 0));
		final Day date2 = Day.create(year2, month2, day2);

		return Failable.<Complement> ok(new DaysAsDates(date1, date2));
	}

}
