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

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.annotation.Explain;
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
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.Note;
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

public final class FactorySequenceNoteAcrossCommand implements SingleMultiFactoryCommand<SequenceDiagram> {

	private IRegex getRegexConcatMultiLine() {
		return RegexConcat.build(FactorySequenceNoteAcrossCommand.class.getName() + "multi", RegexLeaf.start(), //
				new RegexLeaf(1, "VMERGE", "(/)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "STYLE", "(note|hnote|rnote)"), //
				StereotypePattern.optional("STEREO1"), //
				new RegexLeaf(1, "ACROSS", "(accross|across)"), //
				StereotypePattern.optional("STEREO2"), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.end() //
		);
	}

	private IRegex getRegexConcatSingleLine() {
		return RegexConcat.build(FactorySequenceNoteAcrossCommand.class.getName() + "single", RegexLeaf.start(), //
				new RegexLeaf(1, "VMERGE", "(/)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "STYLE", "(note|hnote|rnote)"), //
				StereotypePattern.optional("STEREO1"), //
				new RegexLeaf(1, "ACROSS", "(accross|across)"), //
				StereotypePattern.optional("STEREO2"), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(":"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "NOTE", "(.*)"), RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	public Command<SequenceDiagram> createSingleLine() {
		return new SingleLineCommand2<SequenceDiagram>(getRegexConcatSingleLine()) {

			@Override
			@Explain
			protected String explainArg(LineLocation location, RegexResult arg) {
				return explainInternal(arg, arg.get("NOTE", 0));
			}

			@Override
			protected CommandExecutionResult executeArg(final SequenceDiagram diagram, LineLocation location,
					RegexResult arg, ParserPass currentPass) throws NoSuchColorException {
				final Display display = Display.getWithNewlines(diagram.getPragma(), arg.get("NOTE", 0));
				return executeInternal(diagram, arg, diagram.manageVariable(display));
			}

		};
	}

	@Explain
	private String explainInternal(RegexResult arg, String label) {
		final StringBuilder sb = new StringBuilder();

		// The note spans the whole diagram width, across all participants.
		final String style = StringUtils.goLowerCase(arg.get("STYLE", 0));
		if ("hnote".equals(style))
			sb.append("Adding a hexagonal note");
		else if ("rnote".equals(style))
			sb.append("Adding a rectangular note");
		else
			sb.append("Adding a note");

		sb.append(" across all participants");

		if (label != null && label.isEmpty() == false)
			sb.append(" labelled \"").append(label).append("\"");

		// The stereotype may be written before or after 'across'
		// (STEREO1/STEREO2), hence the lazzy lookup.
		final String stereo = arg.getLazzy("STEREO", 0);
		if (stereo != null)
			sb.append(", stereotype ").append(stereo);

		if (arg.get("COLOR", 0) != null)
			sb.append(", background color ").append(arg.get("COLOR", 0));

		if (arg.get(UrlBuilder.URL_KEY, 0) != null)
			sb.append(", with a URL link");

		// A leading '/' vertically merges this note with the previous one.
		if (arg.get("VMERGE", 0) != null)
			sb.append(", merged with the previous note");

		// The misspelling 'accross' is matched by the pattern but rejected by
		// executeInternal with an explicit error.
		if ("accross".equalsIgnoreCase(arg.get("ACROSS", 0)))
			sb.append(" (the misspelling 'accross' is rejected at execution: use 'across')");

		return sb.toString();
	}

	private final static Lazy<Pattern2> END = new Lazy<>(() -> Pattern2.cmpile("^end[%s]?(note|hnote|rnote)$"));

	public Command<SequenceDiagram> createMultiLine(boolean withBracket) {
		return new CommandMultilines2<SequenceDiagram>(getRegexConcatMultiLine(),
				MultilinesStrategy.KEEP_STARTING_QUOTE, Trim.BOTH, END) {

			@Override
			@Explain
			protected String explainNow(BlocLines lines) {
				// Mirror executeNow: the first line carries the declaration, the
				// lines up to the closing 'end note' are the text of the note.
				final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
				if (line0 == null)
					return "Adding a note";

				final StringBuilder sb = new StringBuilder(explainInternal(line0, null));
				final int bodyCount = lines.size() > 2 ? lines.size() - 2 : 0;
				if (bodyCount > 0)
					sb.append(", with ").append(bodyCount).append(bodyCount == 1 ? " line" : " lines")
							.append(" of text");

				return sb.toString();
			}

			@Override
			protected CommandExecutionResult executeNow(final SequenceDiagram diagram, BlocLines lines,
					ParserPass currentPass) throws NoSuchColorException {
				final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
				lines = lines.subExtract(1, 1);
				lines = lines.removeEmptyColumns().expandsNewline(false);
				final Display display = lines.toDisplay();
				return executeInternal(diagram, line0, diagram.manageVariable(display));
			}

		};
	}

	private CommandExecutionResult executeInternal(SequenceDiagram diagram, final RegexResult line0, Display display)
			throws NoSuchColorException {
		final String across = line0.get("ACROSS", 0);
		if (across.equalsIgnoreCase("accross"))
			return CommandExecutionResult.error("Use 'across' instead of 'accross'");

		if (display.size() > 0) {
			final boolean tryMerge = line0.get("VMERGE", 0) != null;
			final Note note = new Note((Participant) null, (Participant) null, display,
					diagram.getSkinParam().getCurrentStyleBuilder());
			Colors colors = color().getColor(line0, diagram.getSkinParam().getIHtmlColorSet());
			final String stereotypeString = line0.getLazzy("STEREO", 0);
			if (stereotypeString != null) {
				final Stereotype stereotype = Stereotype.build(stereotypeString);
				colors = colors.applyStereotypeForNote(stereotype, diagram.getSkinParam(), ColorParam.noteBackground,
						ColorParam.noteBorder);
				note.setStereotype(stereotype);
			}
			note.setColors(colors);
			note.setNoteStyle(NoteStyle.getNoteStyle(line0.get("STYLE", 0)));
			if (line0.get(UrlBuilder.URL_KEY, 0) != null) {
				final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
				final Url urlLink = urlBuilder.getUrl(line0.get(UrlBuilder.URL_KEY, 0));
				note.setUrl(urlLink);
			}
			diagram.addNote(note, tryMerge);
		}
		return CommandExecutionResult.ok();
	}

}
