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
package net.sourceforge.plantuml.command.note;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.CucaNote;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.Trim;
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
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.utils.Position;

public final class CommandFactoryNoteOnLink implements SingleMultiFactoryCommand<CucaDiagram> {

	private final ParserPass selectedpass;

	public CommandFactoryNoteOnLink(ParserPass selectedpass) {
		this.selectedpass = selectedpass;
	}

	private IRegex getRegexConcatSingleLine() {
		return RegexConcat.build(CommandFactoryNoteOnLink.class.getName() + "single", RegexLeaf.start(), //
				new RegexLeaf("note"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "POSITION", "(right|left|top|bottom)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "(on|of)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("link"), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(":"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "NOTE", "(.*)"), RegexLeaf.end());
	}

	private IRegex getRegexConcatMultiLine() {
		return RegexConcat.build(CommandFactoryNoteOnLink.class.getName() + "multi", RegexLeaf.start(), //
				new RegexLeaf("note"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "POSITION", "(right|left|top|bottom)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "(on|of)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("link"), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	private final static Lazy<Pattern2> END = new Lazy<>(
			() -> Pattern2.cmpile("^end[%s]?note$"));

	public Command<CucaDiagram> createMultiLine(boolean withBracket) {
		return new CommandMultilines2<CucaDiagram>(getRegexConcatMultiLine(), MultilinesStrategy.KEEP_STARTING_QUOTE,
				Trim.BOTH, END) {

			@Override
			protected CommandExecutionResult executeNow(final CucaDiagram system, BlocLines lines,
					ParserPass currentPass) throws NoSuchColorException {
				final String line0 = lines.getFirst().getTrimmed().getString();
				lines = lines.subExtract(1, 1).expandsNewline(false);
				lines = lines.removeEmptyColumns();
				if (lines.size() > 0) {
					final RegexResult arg = getStartingPattern().matcher(line0);
					final Display display = lines.toDisplay();
					return executeInternal(system, arg, display);
				}
				return CommandExecutionResult.error("No note defined");
			}

			@Override
			public boolean isEligibleFor(ParserPass pass) {
				return pass == selectedpass;
			}

		};
	}

	public Command<CucaDiagram> createSingleLine() {
		return new SingleLineCommand2<CucaDiagram>(getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(final CucaDiagram diagram, LineLocation location,
					RegexResult arg, ParserPass currentPass) throws NoSuchColorException {
				final Display display = Display.getWithNewlines(diagram.getPragma(), arg.get("NOTE", 0));
				return executeInternal(diagram, arg, display);
			}

			@Override
			public boolean isEligibleFor(ParserPass pass) {
				return pass == selectedpass;
			}
		};
	}

	private CommandExecutionResult executeInternal(CucaDiagram diagram, final RegexResult arg, Display display)
			throws NoSuchColorException {
		final Link link = diagram.getLastLink();
		if (link == null)
			return CommandExecutionResult.error("No link defined");

		Position position = Position.BOTTOM;
		if (arg.get("POSITION", 0) != null)
			position = Position.valueOf(StringUtils.goUpperCase(arg.get("POSITION", 0)));

		Url url = null;
		if (arg.get("URL", 0) != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			url = urlBuilder.getUrl(arg.get("URL", 0));
		}
		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		link.addNote(CucaNote.build(display, position, colors));
		return CommandExecutionResult.ok();
	}

}
