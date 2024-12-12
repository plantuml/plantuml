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
package net.sourceforge.plantuml.command.note.sequence;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.command.note.SingleMultiFactoryCommand;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteStyle;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;

public final class FactorySequenceNoteCommand implements SingleMultiFactoryCommand<SequenceDiagram> {
	// ::remove folder when __HAXE__

	private IRegex getRegexConcatMultiLine() {
		return RegexConcat.build(FactorySequenceNoteCommand.class.getName() + "multi", RegexLeaf.start(), //
				new RegexLeaf("PARALLEL", "(&[%s]*)?"), //
				new RegexLeaf("VMERGE", "(/)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STYLE", "(note|hnote|rnote)"), //
				StereotypePattern.optional("STEREO1"), //
				new RegexLeaf("POSITION", "(right|left|over)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("PARTICIPANT", "(?:of[%s]+)?([%pLN_.@]+|[%g][^%g]+[%g])"), //
				StereotypePattern.optional("STEREO2"), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.end());
	}

	private IRegex getRegexConcatSingleLine() {
		return RegexConcat.build(FactorySequenceNoteCommand.class.getName() + "single", RegexLeaf.start(), //
				new RegexLeaf("PARALLEL", "(&[%s]*)?"), //
				new RegexLeaf("VMERGE", "(/)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STYLE", "(note|hnote|rnote)"), //
				StereotypePattern.optional("STEREO1"), //
				new RegexLeaf("POSITION", "(right|left|over)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("PARTICIPANT", "(?:of[%s])?([%pLN_.@]+|[%g][^%g]+[%g])"), //
				StereotypePattern.optional("STEREO2"), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(":"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("NOTE", "(.*)"), RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	public Command<SequenceDiagram> createMultiLine(boolean withBracket) {
		return new CommandMultilines2<SequenceDiagram>(getRegexConcatMultiLine(),
				MultilinesStrategy.KEEP_STARTING_QUOTE, Trim.BOTH) {

			@Override
			public String getPatternEnd() {
				return "^end[%s]?(note|hnote|rnote)$";
			}

			@Override
			protected CommandExecutionResult executeNow(final SequenceDiagram diagram, BlocLines lines,
					ParserPass currentPass) throws NoSuchColorException {
				final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
				lines = lines.subExtract(1, 1);
				lines = lines.removeEmptyColumns().expandsJaws5();
				final Display display = lines.toDisplay();
				return executeInternal(diagram, line0, diagram.manageVariable(display));
			}
		};
	}

	public Command<SequenceDiagram> createSingleLine() {
		return new SingleLineCommand2<SequenceDiagram>(getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(final SequenceDiagram diagram, LineLocation location,
					RegexResult arg, ParserPass currentPass) throws NoSuchColorException {
				final Display display = Display.getWithNewlines(diagram.getPragma(), arg.get("NOTE", 0));
				return executeInternal(diagram, arg, diagram.manageVariable(display));
			}

		};
	}

	private CommandExecutionResult executeInternal(SequenceDiagram diagram, RegexResult arg, Display display)
			throws NoSuchColorException {
		final Participant p = diagram.getOrCreateParticipant(
				StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("PARTICIPANT", 0)));

		final NotePosition position = NotePosition.valueOf(StringUtils.goUpperCase(arg.get("POSITION", 0)));

		if (display.size() > 0) {
			final boolean tryMerge = arg.get("VMERGE", 0) != null;
			final boolean parallel = arg.get("PARALLEL", 0) != null;

			final Note note = new Note(p, position, display, diagram.getSkinParam().getCurrentStyleBuilder());
			Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
			final String stereotypeString = arg.getLazzy("STEREO", 0);
			if (stereotypeString != null) {
				final Stereotype stereotype = Stereotype.build(stereotypeString);
				colors = colors.applyStereotypeForNote(stereotype, diagram.getSkinParam(), ColorParam.noteBackground,
						ColorParam.noteBorder);
				note.setStereotype(stereotype);
			}
			note.setColors(colors);
			note.setNoteStyle(NoteStyle.getNoteStyle(arg.get("STYLE", 0)));
			if (arg.get("URL", 0) != null) {
				final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
				final Url urlLink = urlBuilder.getUrl(arg.get("URL", 0));
				note.setUrl(urlLink);
			}
			if (parallel) {
				note.goParallel();
			}
			diagram.addNote(note, tryMerge);
		}
		return CommandExecutionResult.ok();
	}

}
