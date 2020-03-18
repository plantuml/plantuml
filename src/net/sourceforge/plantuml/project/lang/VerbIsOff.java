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

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.DaysAsDates;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Resource;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;

public class VerbIsOff implements VerbPattern {

	public Collection<ComplementPattern> getComplements() {
		return Arrays
				.<ComplementPattern> asList(new ComplementDate(), new ComplementDates(), new ComplementDayOfWeek());
	}

	public IRegex toRegex() {
		return new RegexConcat(new RegexLeaf("is"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("off"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(//
						new RegexLeaf("on"),//
						new RegexLeaf("for"),//
						new RegexLeaf("the"),//
						new RegexLeaf("at") //
				));
	}

	public Verb getVerb(final GanttDiagram project, RegexResult arg) {
		return new Verb() {
			public CommandExecutionResult execute(Subject subject, Complement complement) {
				final Resource resource = (Resource) subject;
				if (complement instanceof DayOfWeek) {
					resource.addCloseDay(((DayOfWeek) complement));
				} else if (complement instanceof DaysAsDates) {
					for (Day when : (DaysAsDates) complement) {
						resource.addCloseDay(project.convert(when));
					}
				} else {
					final Day when = (Day) complement;
					resource.addCloseDay(project.convert(when));
				}
				return CommandExecutionResult.ok();
			}

		};
	}
}
