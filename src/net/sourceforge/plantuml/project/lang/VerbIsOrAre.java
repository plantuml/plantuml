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
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.DaysAsDates;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class VerbIsOrAre implements VerbPattern {

	public Collection<ComplementPattern> getComplements() {
		return Arrays
				.<ComplementPattern> asList(new ComplementClose(), new ComplementOpen(), new ComplementInColors2());
	}

	public IRegex toRegex() {
		return new RegexLeaf("(is|are)");
	}

	public Verb getVerb(final GanttDiagram project, final RegexResult arg) {
		return new Verb() {
			public CommandExecutionResult execute(Subject subject, Complement complement) {
				if (complement instanceof ComplementColors) {
					final HColor color = ((ComplementColors) complement).getCenter();
					return manageColor(project, subject, color);
				}
				if (complement == ComplementClose.CLOSE) {
					return manageClose(project, subject);
				}
				if (complement == ComplementOpen.OPEN) {
					return manageOpen(project, subject);
				}
				return CommandExecutionResult.error("assertion fail");
			}
		};
	}

	private CommandExecutionResult manageColor(final GanttDiagram project, Subject subject, HColor color) {
		if (subject instanceof Day) {
			final Day day = (Day) subject;
			project.colorDay(day, color);
		}
		if (subject instanceof DaysAsDates) {
			final DaysAsDates days = (DaysAsDates) subject;
			for (Day d : days) {
				project.colorDay(d, color);
			}
		}
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult manageClose(final GanttDiagram project, Subject subject) {
		if (subject instanceof Day) {
			final Day day = (Day) subject;
			project.closeDayAsDate(day);
		}
		if (subject instanceof DaysAsDates) {
			final DaysAsDates days = (DaysAsDates) subject;
			for (Day d : days) {
				project.closeDayAsDate(d);
			}
		}
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult manageOpen(final GanttDiagram project, Subject subject) {
		if (subject instanceof Day) {
			final Day day = (Day) subject;
			project.openDayAsDate(day);
		}
		if (subject instanceof DaysAsDates) {
			final DaysAsDates days = (DaysAsDates) subject;
			for (Day d : days) {
				project.openDayAsDate(d);
			}
		}
		return CommandExecutionResult.ok();
	}

}
