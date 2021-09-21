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
package net.sourceforge.plantuml.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandNope;
import net.sourceforge.plantuml.command.PSystemCommandFactory;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.project.command.CommandColorTask;
import net.sourceforge.plantuml.project.command.CommandFootbox;
import net.sourceforge.plantuml.project.command.CommandGanttArrow;
import net.sourceforge.plantuml.project.command.CommandGanttArrow2;
import net.sourceforge.plantuml.project.command.CommandLabelOnColumn;
import net.sourceforge.plantuml.project.command.CommandLanguage;
import net.sourceforge.plantuml.project.command.CommandNoteBottom;
import net.sourceforge.plantuml.project.command.CommandPrintBetween;
import net.sourceforge.plantuml.project.command.CommandPrintScale;
import net.sourceforge.plantuml.project.command.CommandSeparator;
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
import net.sourceforge.plantuml.project.lang.SubjectTask;
import net.sourceforge.plantuml.project.lang.SubjectToday;
import net.sourceforge.plantuml.style.CommandStyleImport;
import net.sourceforge.plantuml.style.CommandStyleMultilinesCSS;

public class GanttDiagramFactory extends PSystemCommandFactory {

	static private final List<Subject> subjects() {
		return Arrays.<Subject>asList(new SubjectTask(), new SubjectProject(), new SubjectDayOfWeek(),
				new SubjectDayAsDate(), new SubjectDaysAsDates(), new SubjectResource(), new SubjectToday());
	}

	public GanttDiagramFactory(DiagramType type) {
		super(type);
	}

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = new ArrayList<>();
		addTitleCommands(cmds);
		addCommonCommands2(cmds);

		cmds.add(new CommandStyleMultilinesCSS());
		cmds.add(new CommandStyleImport());

		// addCommonCommands(cmds);
		cmds.add(new CommandNope());
		// cmds.add(new CommandComment());
		// cmds.add(new CommandMultilinesComment());
		cmds.addAll(getLanguageCommands());
		cmds.add(new CommandGanttArrow());
		cmds.add(new CommandGanttArrow2());
		cmds.add(new CommandColorTask());
		cmds.add(new CommandSeparator());
		cmds.add(new CommandWeekNumberStrategy());

		cmds.add(new CommandLanguage());
		cmds.add(new CommandPrintScale());
		cmds.add(new CommandPrintBetween());
		cmds.add(new CommandNoteBottom());
		cmds.add(new CommandFootbox());
		cmds.add(new CommandLabelOnColumn());

		// cmds.add(new CommandScaleWidthAndHeight());
		// cmds.add(new CommandScaleWidthOrHeight());
		// cmds.add(new CommandScaleMaxWidth());
		// cmds.add(new CommandScaleMaxHeight());
		// cmds.add(new CommandScaleMaxWidthAndHeight());

		return cmds;
	}

	static private final Collection<Command> cache = new ArrayList<>();

	public static void clearCache() {
		cache.clear();
	}

	private static Collection<Command> getLanguageCommands() {
		// Useless synchronized now
		synchronized (cache) {
			if (cache.size() == 0) {

				for (Subject subject : subjects()) {
					for (SentenceSimple sentenceA : subject.getSentences()) {
						cache.add(NaturalCommand.create(sentenceA));
						for (SentenceSimple sentenceB : subject.getSentences()) {
							if (sentenceA.getVerbPattern().equals(sentenceB.getVerbPattern()) == false) {
								cache.add(NaturalCommand.create(new SentenceAnd(sentenceA, sentenceB)));
							}
						}
					}
				}

				for (Subject subject : subjects()) {
					for (SentenceSimple sentenceA : subject.getSentences()) {
						for (SentenceSimple sentenceB : subject.getSentences()) {
							for (SentenceSimple sentenceC : subject.getSentences()) {
								if (sentenceA.getVerbPattern().equals(sentenceB.getVerbPattern()) == false
										&& sentenceA.getVerbPattern().equals(sentenceC.getVerbPattern()) == false
										&& sentenceC.getVerbPattern().equals(sentenceB.getVerbPattern()) == false) {
									cache.add(
											NaturalCommand.create(new SentenceAndAnd(sentenceA, sentenceB, sentenceC)));
								}
							}
						}
					}
				}
			}
		}
		return cache;
	}

	@Override
	public GanttDiagram createEmptyDiagram(UmlSource source, ISkinSimple skinParam) {
		return new GanttDiagram(source);
	}

}
