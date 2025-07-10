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
package net.sourceforge.plantuml.project;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandNope;
import net.sourceforge.plantuml.command.CommonCommands;
import net.sourceforge.plantuml.command.PSystemCommandFactory;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.project.command.CommandColorTask;
import net.sourceforge.plantuml.project.command.CommandFootboxGantt;
import net.sourceforge.plantuml.project.command.CommandGanttArrow;
import net.sourceforge.plantuml.project.command.CommandGanttArrow2;
import net.sourceforge.plantuml.project.command.CommandGroupEnd;
import net.sourceforge.plantuml.project.command.CommandGroupStart;
import net.sourceforge.plantuml.project.command.CommandHideClosed;
import net.sourceforge.plantuml.project.command.CommandHideResourceFootbox;
import net.sourceforge.plantuml.project.command.CommandHideResourceName;
import net.sourceforge.plantuml.project.command.CommandLabelOnColumn;
import net.sourceforge.plantuml.project.command.CommandLanguage;
import net.sourceforge.plantuml.project.command.CommandNoteBottom;
import net.sourceforge.plantuml.project.command.CommandPrintBetween;
import net.sourceforge.plantuml.project.command.CommandPrintScale;
import net.sourceforge.plantuml.project.command.CommandSeparator;
import net.sourceforge.plantuml.project.command.CommandTaskCompleteDefault;
import net.sourceforge.plantuml.project.command.CommandWeekNumberStrategy;
import net.sourceforge.plantuml.project.command.NaturalCommand;
import net.sourceforge.plantuml.project.lang.SentenceAnd;
import net.sourceforge.plantuml.project.lang.SentenceAndAnd;
import net.sourceforge.plantuml.project.lang.SentenceSimple;
import net.sourceforge.plantuml.project.lang.Subject;
import net.sourceforge.plantuml.project.lang.SubjectDayAsDate;
import net.sourceforge.plantuml.project.lang.SubjectDayOfWeek;
import net.sourceforge.plantuml.project.lang.SubjectDaysAsDates;
import net.sourceforge.plantuml.project.lang.SubjectProject;
import net.sourceforge.plantuml.project.lang.SubjectResource;
import net.sourceforge.plantuml.project.lang.SubjectSeparator;
import net.sourceforge.plantuml.project.lang.SubjectTask;
import net.sourceforge.plantuml.project.lang.SubjectToday;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.CommandStyleImport;
import net.sourceforge.plantuml.style.CommandStyleMultilinesCSS;

public class GanttDiagramFactory extends PSystemCommandFactory {

	static private final List<Subject<GanttDiagram>> subjects() {
		return Arrays.asList(SubjectTask.ME, SubjectProject.ME, SubjectDayOfWeek.ME, SubjectDayAsDate.ME,
				SubjectDaysAsDates.ME, SubjectResource.ME, SubjectToday.ME, SubjectSeparator.ME);
	}

	public GanttDiagramFactory() {
		super(DiagramType.GANTT);
	}

	@Override
	protected void initCommandsList(List<Command> cmds) {
		CommonCommands.addTitleCommands(cmds);
		CommonCommands.addCommonCommands2(cmds);
		CommonCommands.addCommonScaleCommands(cmds);

		cmds.add(CommandStyleMultilinesCSS.ME);
		cmds.add(CommandStyleImport.ME);

		cmds.add(CommandNope.ME);

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
		for (Subject<GanttDiagram> subject : subjects())
			for (SentenceSimple<GanttDiagram> sentenceA : subject.getSentences()) {
				cmd.add(NaturalCommand.create(sentenceA));
				for (SentenceSimple<GanttDiagram> sentenceB : subject.getSentences()) {
					final String signatureA = sentenceA.getSignature();
					final String signatureB = sentenceB.getSignature();
					if (signatureA.equals(signatureB) == false)
						cmd.add(NaturalCommand.create(new SentenceAnd<GanttDiagram>(sentenceA, sentenceB)));

				}
			}

		for (Subject<GanttDiagram> subject : subjects())
			for (SentenceSimple<GanttDiagram> sentenceA : subject.getSentences())
				for (SentenceSimple<GanttDiagram> sentenceB : subject.getSentences())
					for (SentenceSimple<GanttDiagram> sentenceC : subject.getSentences()) {
						final String signatureA = sentenceA.getSignature();
						final String signatureB = sentenceB.getSignature();
						final String signatureC = sentenceC.getSignature();
						if (signatureA.equals(signatureB) == false && signatureA.equals(signatureC) == false
								&& signatureC.equals(signatureB) == false)
							cmd.add(NaturalCommand.create(new SentenceAndAnd<GanttDiagram>(sentenceA, sentenceB, sentenceC)));
					}
	}

	@Override
	public GanttDiagram createEmptyDiagram(UmlSource source, Previous previous, PreprocessingArtifact preprocessing) {
		return new GanttDiagram(source, preprocessing);
	}
	
	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.GANTT;
	}


}
