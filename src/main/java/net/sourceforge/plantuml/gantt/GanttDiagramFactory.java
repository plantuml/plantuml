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
package net.sourceforge.plantuml.gantt;

import java.util.List;

import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommonCommands;
import net.sourceforge.plantuml.command.PSystemCommandFactory;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.gantt.command.CommandColorTask;
import net.sourceforge.plantuml.gantt.command.CommandFootboxGantt;
import net.sourceforge.plantuml.gantt.command.CommandGanttArrow;
import net.sourceforge.plantuml.gantt.command.CommandGanttArrow2;
import net.sourceforge.plantuml.gantt.command.CommandGroupEnd;
import net.sourceforge.plantuml.gantt.command.CommandGroupStart;
import net.sourceforge.plantuml.gantt.command.CommandHideClosed;
import net.sourceforge.plantuml.gantt.command.CommandHideResourceFootbox;
import net.sourceforge.plantuml.gantt.command.CommandHideResourceName;
import net.sourceforge.plantuml.gantt.command.CommandLabelOnColumn;
import net.sourceforge.plantuml.gantt.command.CommandLanguage;
import net.sourceforge.plantuml.gantt.command.CommandNoteBottom;
import net.sourceforge.plantuml.gantt.command.CommandPrintBetween;
import net.sourceforge.plantuml.gantt.command.CommandPrintScale;
import net.sourceforge.plantuml.gantt.command.CommandSeparator;
import net.sourceforge.plantuml.gantt.command.CommandTaskCompleteDefault;
import net.sourceforge.plantuml.gantt.command.CommandWeekNumberStrategy;
import net.sourceforge.plantuml.gantt.command.NaturalGanttCommand;
import net.sourceforge.plantuml.gantt.lang.SubjectDayAsDate;
import net.sourceforge.plantuml.gantt.lang.SubjectDayOfWeek;
import net.sourceforge.plantuml.gantt.lang.SubjectDaysAsDates;
import net.sourceforge.plantuml.gantt.lang.SubjectGantt;
import net.sourceforge.plantuml.gantt.lang.SubjectResource;
import net.sourceforge.plantuml.gantt.lang.SubjectSeparator;
import net.sourceforge.plantuml.gantt.lang.SubjectTask;
import net.sourceforge.plantuml.gantt.lang.SubjectToday;
import net.sourceforge.plantuml.gantt.lang.SubjectWorkingHours;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;

public class GanttDiagramFactory extends PSystemCommandFactory {

	public GanttDiagramFactory() {
		super(DiagramType.GANTT);
	}

	@Override
	protected void initCommandsList(List<Command> cmds) {
		CommonCommands.addTitleCommands(cmds);
		CommonCommands.addCommonCommands2(cmds);
		CommonCommands.addCommonScaleCommands(cmds);

		addLanguageCommands(cmds);

		cmds.add(new CommandGanttArrow());
		cmds.add(new CommandGanttArrow2());
		cmds.add(new CommandColorTask());
		cmds.add(new CommandSeparator());
		cmds.add(new CommandWeekNumberStrategy());
		cmds.add(new CommandGroupStart());
		cmds.add(new CommandGroupEnd());

		cmds.add(new CommandLanguage());
		cmds.add(new CommandPrintScale());
		cmds.add(new CommandPrintBetween());
		cmds.add(new CommandNoteBottom());
		cmds.add(new CommandFootboxGantt());
		cmds.add(new CommandLabelOnColumn());
		cmds.add(new CommandHideResourceName());
		cmds.add(new CommandHideResourceFootbox());
		cmds.add(new CommandHideClosed());
		cmds.add(new CommandTaskCompleteDefault());
	}

	private void addLanguageCommands(List<Command> cmd) {
		cmd.add(new NaturalGanttCommand(SubjectGantt.ME));
		cmd.add(new NaturalGanttCommand(SubjectToday.ME));
		cmd.add(new NaturalGanttCommand(SubjectTask.ME));
		cmd.add(new NaturalGanttCommand(SubjectResource.ME));
		cmd.add(new NaturalGanttCommand(SubjectDaysAsDates.ME));
		cmd.add(new NaturalGanttCommand(SubjectDayOfWeek.ME));
		cmd.add(new NaturalGanttCommand(SubjectDayAsDate.ME));
		cmd.add(new NaturalGanttCommand(SubjectSeparator.ME));
		cmd.add(new NaturalGanttCommand(SubjectWorkingHours.ME));
	}

	@Override
	public GanttDiagram createEmptyDiagram(PathSystem pathSystem, UmlSource source, Previous previous,
			PreprocessingArtifact preprocessing) {
		return new GanttDiagram(source, preprocessing);
	}

}
