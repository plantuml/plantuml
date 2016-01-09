/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 7558 $
 *
 */
package net.sourceforge.plantuml.command.note.sequence;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.note.SingleMultiFactoryCommand;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteStyle;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public final class FactorySequenceNoteCommand implements SingleMultiFactoryCommand<SequenceDiagram> {

	private RegexConcat getRegexConcatMultiLine() {
		return new RegexConcat(//
				new RegexLeaf("^"), //
				new RegexLeaf("VMERGE", "(/)?[%s]*"), //
				new RegexLeaf("STYLE", "(note|hnote|rnote)"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("STEREO", "(\\<{2}.*\\>{2})?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("POSITION", "(right|left|over)[%s]+"), //
				new RegexLeaf("PARTICIPANT", "(?:of[%s]+)?([\\p{L}0-9_.@]+|[%g][^%g]+[%g])[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("$"));
	}

	private RegexConcat getRegexConcatSingleLine() {
		return new RegexConcat(//
				new RegexLeaf("^"), //
				new RegexLeaf("VMERGE", "(/)?[%s]*"), //
				new RegexLeaf("STYLE", "(note|hnote|rnote)"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("STEREO", "(\\<{2}.*\\>{2})?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("POSITION", "(right|left|over)[%s]+"), //
				new RegexLeaf("PARTICIPANT", "(?:of[%s])?([\\p{L}0-9_.@]+|[%g][^%g]+[%g])[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("[%s]*:[%s]*"), //
				new RegexLeaf("NOTE", "(.*)"), //
				new RegexLeaf("$"));
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	public Command<SequenceDiagram> createMultiLine(boolean withBracket) {
		return new CommandMultilines2<SequenceDiagram>(getRegexConcatMultiLine(),
				MultilinesStrategy.KEEP_STARTING_QUOTE) {

			@Override
			public String getPatternEnd() {
				return "(?i)^end[%s]?(note|hnote|rnote)$";
			}

			public CommandExecutionResult executeNow(final SequenceDiagram system, BlocLines lines) {
				final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.getFirst499()));
				lines = lines.subExtract(1, 1);
				lines = lines.removeEmptyColumns();
				return executeInternal(system, line0, lines);
			}
		};
	}

	public Command<SequenceDiagram> createSingleLine() {
		return new SingleLineCommand2<SequenceDiagram>(getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(final SequenceDiagram system, RegexResult arg) {
				return executeInternal(system, arg, BlocLines.getWithNewlines(arg.get("NOTE", 0)));
			}

		};
	}

	private CommandExecutionResult executeInternal(SequenceDiagram diagram, RegexResult arg, BlocLines strings) {
		final Participant p = diagram.getOrCreateParticipant(StringUtils
				.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("PARTICIPANT", 0)));

		final NotePosition position = NotePosition.valueOf(StringUtils.goUpperCase(arg.get("POSITION", 0)));

		if (strings.size() > 0) {
			final boolean tryMerge = arg.get("VMERGE", 0) != null;
			final Note note = new Note(p, position, strings.toDisplay());
			Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
			final String stereotypeString = arg.get("STEREO", 0);
			if (stereotypeString != null) {
				final Stereotype stereotype = new Stereotype(stereotypeString);
				note.setStereotype(stereotype);
				colors = colors.applyStereotypeForNote(stereotype, diagram.getSkinParam(), FontParam.NOTE,
						ColorParam.noteBackground, ColorParam.noteBorder);
			}
			note.setColors(colors);
			note.setStyle(NoteStyle.getNoteStyle(arg.get("STYLE", 0)));
			diagram.addNote(note, tryMerge);
		}
		return CommandExecutionResult.ok();
	}

}
