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

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.project.GanttConstraint;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.core.TaskInstant;

public class SentenceOccurs extends SentenceSimple<GanttDiagram> {

	public SentenceOccurs() {
		super(SubjectTask.ME, Verbs.occurs, new ComplementFromTo());
	}

	@Override
	public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
		final Task task = (Task) subject;
		final TwoNames bothNames = (TwoNames) complement;
		final String name1 = bothNames.getName1();
		final String name2 = bothNames.getName2();
		final Task from = project.getExistingTask(name1);
		if (from == null) {
			return CommandExecutionResult.error("No such " + name1 + " task");
		}
		final Task to = project.getExistingTask(name2);
		if (to == null) {
			return CommandExecutionResult.error("No such " + name2 + " task");
		}
		task.setStart(from.getEnd());
		task.setEnd(to.getEnd());
		project.addContraint(new GanttConstraint(project.getIHtmlColorSet(), project.getCurrentStyleBuilder(),
				new TaskInstant(from, TaskAttribute.START), new TaskInstant(task, TaskAttribute.START)));
		project.addContraint(new GanttConstraint(project.getIHtmlColorSet(), project.getCurrentStyleBuilder(),
				new TaskInstant(to, TaskAttribute.END), new TaskInstant(task, TaskAttribute.END)));
		return CommandExecutionResult.ok();
	}

}
