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
package net.sourceforge.plantuml.chronology;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.lang.Something;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;

public class ComplementHour implements Something<ChronologyDiagram> {

	// [Task1] starts at 2023-11-28 15:41:21, ends at 2023-11-28 19:40:00

	static private final SimpleDateFormat inputFormat;
	static private final SimpleDateFormat inputFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	static {
		inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

	}

	public IRegex toRegex(String suffix) {
		return new RegexConcat( //
				new RegexLeaf("TIME", "(\\d+-\\d+-\\d+ \\d+:\\d+:\\d+)"), //
				new RegexOptional(new RegexLeaf("MS", "\\.(\\d+)")) //
		); //
	}

	public Failable<Day> getMe(ChronologyDiagram system, RegexResult arg, String suffix) {
		final String value = arg.get("TIME", 0);
		System.err.println("value=" + value);
		try {
			final Date date = inputFormat.parse(value);
			return Failable.ok(Day.create(date.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		throw new IllegalStateException();
	}

}
