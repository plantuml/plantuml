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
package net.sourceforge.plantuml.project3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandNope;
import net.sourceforge.plantuml.command.CommandScale;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.core.DiagramType;

public class GanttDiagramFactory extends UmlDiagramFactory {

	static private final List<SubjectPattern> subjects() {
		return Arrays.<SubjectPattern> asList(new SubjectTask(), new SubjectProject(), new SubjectDayOfWeek(),
				new SubjectDayAsDate(), new SubjectDaysAsDates(), new SubjectResource(), new SubjectToday());
	}

	static private final Collection<Command> ALL = getLanguageCommands();

	public GanttDiagramFactory(DiagramType type) {
		super(type);
	}

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = new ArrayList<Command>();
		// addCommonCommands(cmds);
		cmds.add(new CommandNope());
		// cmds.add(new CommandComment());
		// cmds.add(new CommandMultilinesComment());
		cmds.addAll(ALL);
		cmds.add(new CommandGanttArrow());
		cmds.add(new CommandGanttArrow2());
		cmds.add(new CommandSeparator());

		cmds.add(new CommandScale());
		cmds.add(new CommandPage());
		// cmds.add(new CommandScaleWidthAndHeight());
		// cmds.add(new CommandScaleWidthOrHeight());
		// cmds.add(new CommandScaleMaxWidth());
		// cmds.add(new CommandScaleMaxHeight());
		// cmds.add(new CommandScaleMaxWidthAndHeight());

		return cmds;
	}

	private static Collection<Command> getLanguageCommands() {
		final Collection<Command> result = new ArrayList<Command>();
		for (SubjectPattern subject : subjects()) {
			for (VerbPattern verb : subject.getVerbs()) {
				for (ComplementPattern complement : verb.getComplements()) {
					result.add(NaturalCommand.create(subject, verb, complement));
				}
			}
		}
		for (SubjectPattern subject : subjects()) {
			final Collection<VerbPattern> verbs = subject.getVerbs();
			for (VerbPattern verb1 : verbs) {
				for (VerbPattern verb2 : verbs) {
					if (verb1 == verb2) {
						continue;
					}
					for (ComplementPattern complement1 : verb1.getComplements()) {
						for (ComplementPattern complement2 : verb2.getComplements()) {
							result.add(NaturalCommandAnd.create(subject, verb1, complement1, verb2, complement2));
						}
					}
				}
			}
		}
		for (SubjectPattern subject : subjects()) {
			final Collection<VerbPattern> verbs = subject.getVerbs();
			for (VerbPattern verb1 : verbs) {
				for (VerbPattern verb2 : verbs) {
					for (VerbPattern verb3 : verbs) {
						if (verb1 == verb2 || verb1 == verb3 || verb2 == verb3) {
							continue;
						}
						for (ComplementPattern complement1 : verb1.getComplements()) {
							for (ComplementPattern complement2 : verb2.getComplements()) {
								for (ComplementPattern complement3 : verb3.getComplements()) {
									result.add(NaturalCommandAndAnd.create(subject, verb1, complement1, verb2,
											complement2, verb3, complement3));
								}
							}
						}
					}
				}
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	@Override
	public GanttDiagram createEmptyDiagram() {
		return new GanttDiagram();
	}

}
