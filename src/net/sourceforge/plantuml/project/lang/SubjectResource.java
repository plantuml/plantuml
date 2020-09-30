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
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.DaysAsDates;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Resource;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;

public class SubjectResource implements Subject {

	public Failable<Resource> getMe(GanttDiagram project, RegexResult arg) {
		final String s = arg.get("RESOURCE", 0);
		return Failable.ok(project.getResource(s));
	}

	public Collection<? extends SentenceSimple> getSentences() {
		return Arrays.asList(new IsOffDate(), new IsOffDates(), new IsOffDayOfWeek(), new IsOnDate(), new IsOnDates());
	}

	public IRegex toRegex() {
		return new RegexConcat( //
				new RegexLeaf("RESOURCE", "\\{([^{}]+)\\}") //
		);
	}

	public class IsOffDate extends SentenceSimple {

		public IsOffDate() {
			super(SubjectResource.this, Verbs.isOff(), new ComplementDate());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final Resource resource = (Resource) subject;
			final Day when = (Day) complement;
			resource.addCloseDay(when);
			return CommandExecutionResult.ok();
		}

	}

	public class IsOffDates extends SentenceSimple {

		public IsOffDates() {
			super(SubjectResource.this, Verbs.isOff(), new ComplementDates());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final Resource resource = (Resource) subject;
			for (Day when : (DaysAsDates) complement) {
				resource.addCloseDay(when);
			}
			return CommandExecutionResult.ok();
		}

	}

	public class IsOffDayOfWeek extends SentenceSimple {

		public IsOffDayOfWeek() {
			super(SubjectResource.this, Verbs.isOff(), new ComplementDayOfWeek());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final Resource resource = (Resource) subject;
			resource.addCloseDay(((DayOfWeek) complement));
			return CommandExecutionResult.ok();
		}

	}

	public class IsOnDate extends SentenceSimple {

		public IsOnDate() {
			super(SubjectResource.this, Verbs.isOn(), new ComplementDate());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final Resource resource = (Resource) subject;
			final Day when = (Day) complement;
			resource.addForceOnDay(when);
			return CommandExecutionResult.ok();
		}

	}

	public class IsOnDates extends SentenceSimple {

		public IsOnDates() {
			super(SubjectResource.this, Verbs.isOn(), new ComplementDates());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final Resource resource = (Resource) subject;
			for (Day when : (DaysAsDates) complement) {
				resource.addForceOnDay(when);
			}
			return CommandExecutionResult.ok();
		}

	}

}
