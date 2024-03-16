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

import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;

public class ComplementDate implements Something<GanttDiagram> {

	private final Type type;

	static enum Type {
		ANY, ONLY_RELATIVE, ONLY_ABSOLUTE;
	}

	private ComplementDate(Type type) {
		this.type = type;
	}

	public static ComplementDate any() {
		return new ComplementDate(Type.ANY);
	}

	public static ComplementDate onlyRelative() {
		return new ComplementDate(Type.ONLY_RELATIVE);
	}

	public static ComplementDate onlyAbsolute() {
		return new ComplementDate(Type.ONLY_ABSOLUTE);
	}

	public IRegex toRegex(String suffix) {
		final DayPattern dayPattern = new DayPattern(suffix);
		switch (type) {
		case ONLY_ABSOLUTE:
			return dayPattern.toRegex();
		case ONLY_RELATIVE:
			return new RegexOr(toRegexD(suffix), toRegexE(suffix));
		}
		return new RegexOr(dayPattern.toRegex(), toRegexD(suffix), toRegexE(suffix));
	}

	private IRegex toRegexD(String suffix) {
		return new RegexConcat( //
				new RegexLeaf("DCOUNT" + suffix, "([\\d]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("days?"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("after"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("start") //
		);
	}

	private IRegex toRegexE(String suffix) {
		return new RegexConcat( //
				new RegexLeaf("[dD]\\+"), //
				new RegexLeaf("ECOUNT" + suffix, "([\\d]+)") //
		);
	}

	public Failable<Day> getMe(GanttDiagram system, RegexResult arg, String suffix) {
		final DayPattern dayPattern = new DayPattern(suffix);
		final Day result = dayPattern.getDay(arg);

		if (result != null)
			return Failable.ok(result);

		if (arg.get("DCOUNT" + suffix, 0) != null)
			return Failable.ok(resultD(system, arg, suffix));

		if (arg.get("ECOUNT" + suffix, 0) != null)
			return Failable.ok(resultE(system, arg, suffix));

		throw new IllegalStateException();
	}

	private Day resultD(GanttDiagram system, RegexResult arg, String suffix) {
		final int day = Integer.parseInt(arg.get("DCOUNT" + suffix, 0));
		return system.getStartingDate().addDays(day);
	}

	private Day resultE(GanttDiagram system, RegexResult arg, String suffix) {
		final int day = Integer.parseInt(arg.get("ECOUNT" + suffix, 0));
		return system.getStartingDate().addDays(day);
	}

}
