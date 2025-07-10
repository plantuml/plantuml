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

import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommonCommands;
import net.sourceforge.plantuml.command.PSystemCommandFactory;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.project.command.NaturalCommand;
import net.sourceforge.plantuml.project.lang.SentenceAnd;
import net.sourceforge.plantuml.project.lang.SentenceAndAnd;
import net.sourceforge.plantuml.project.lang.SentenceSimple;
import net.sourceforge.plantuml.project.lang.Subject;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class ChronologyDiagramFactory extends PSystemCommandFactory {

	static private final List<Subject<ChronologyDiagram>> subjects() {
		return Arrays.asList(SubjectTask.ME);
	}

	public ChronologyDiagramFactory() {
		super(DiagramType.CHRONOLOGY);
	}

	@Override
	protected void initCommandsList(List<Command> cmds) {
		CommonCommands.addTitleCommands(cmds);
		CommonCommands.addCommonCommands2(cmds);
		CommonCommands.addCommonScaleCommands(cmds);

//		cmds.add(CommandStyleMultilinesCSS.ME);
//		cmds.add(CommandStyleImport.ME);
//
//		cmds.add(CommandNope.ME);

		addLanguageCommands(cmds);

//		cmds.add(new CommandGanttArrow());
//		cmds.add(new CommandGanttArrow2());
//		cmds.add(new CommandColorTask());
//		cmds.add(new CommandSeparator());
//		cmds.add(new CommandWeekNumberStrategy());
//		cmds.add(new CommandGroupStart());
//		cmds.add(new CommandGroupEnd());
//
//		cmds.add(new CommandLanguage());
//		cmds.add(new CommandPrintScale());
//		cmds.add(new CommandPrintBetween());
//		cmds.add(new CommandNoteBottom());
//		cmds.add(new CommandFootbox());
//		cmds.add(new CommandLabelOnColumn());
//		cmds.add(new CommandHideResourceName());
//		cmds.add(new CommandHideResourceFootbox());
//		cmds.add(new CommandTaskCompleteDefault());
	}

	private void addLanguageCommands(List<Command> cmd) {
		for (Subject<ChronologyDiagram> subject : subjects())
			for (SentenceSimple<ChronologyDiagram> sentenceA : subject.getSentences()) {
				cmd.add(NaturalCommand.create(sentenceA));
				for (SentenceSimple<ChronologyDiagram> sentenceB : subject.getSentences()) {
					final String signatureA = sentenceA.getSignature();
					final String signatureB = sentenceB.getSignature();
					if (signatureA.equals(signatureB) == false)
						cmd.add(NaturalCommand.create(new SentenceAnd<ChronologyDiagram>(sentenceA, sentenceB)));

				}
			}

		for (Subject<ChronologyDiagram> subject : subjects())
			for (SentenceSimple<ChronologyDiagram> sentenceA : subject.getSentences())
				for (SentenceSimple<ChronologyDiagram> sentenceB : subject.getSentences())
					for (SentenceSimple<ChronologyDiagram> sentenceC : subject.getSentences()) {
						final String signatureA = sentenceA.getSignature();
						final String signatureB = sentenceB.getSignature();
						final String signatureC = sentenceC.getSignature();
						if (signatureA.equals(signatureB) == false && signatureA.equals(signatureC) == false
								&& signatureC.equals(signatureB) == false)
							cmd.add(NaturalCommand
									.create(new SentenceAndAnd<ChronologyDiagram>(sentenceA, sentenceB, sentenceC)));
					}
	}

	@Override
	public ChronologyDiagram createEmptyDiagram(UmlSource source, Previous previous, PreprocessingArtifact preprocessing) {
		return new ChronologyDiagram(source, preprocessing);
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.CHRONOLOGY;
	}

}
