/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.utils.UniqueSequence;

public final class FactoryNoteOnEntityCommand implements SingleMultiFactoryCommand<AbstractEntityDiagram> {

	private final IRegex partialPattern;

	// private final boolean withBracket;

	public FactoryNoteOnEntityCommand(IRegex partialPattern/* , boolean withBracket */) {
		this.partialPattern = partialPattern;
		// this.withBracket = withBracket;
	}

	private RegexConcat getRegexConcatSingleLine(IRegex partialPattern) {
		return new RegexConcat(new RegexLeaf("^[%s]*note[%s]+"), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)"), //
				new RegexOr(//
						new RegexConcat(new RegexLeaf("[%s]+of[%s]+"), partialPattern), //
						new RegexLeaf("")), //
				new RegexLeaf("[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("[%s]*:[%s]*"), //
				new RegexLeaf("NOTE", "(.*)"), //
				new RegexLeaf("$") //
		);
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	private RegexConcat getRegexConcatMultiLine(IRegex partialPattern, final boolean withBracket) {
		return new RegexConcat(new RegexLeaf("^[%s]*note[%s]+"), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)"), //
				new RegexOr(//
						new RegexConcat(new RegexLeaf("[%s]+of[%s]+"), partialPattern), //
						new RegexLeaf("")), //
				new RegexLeaf("[%s]*"), //
				color().getRegex(), //
				new RegexLeaf(withBracket ? "[%s]*\\{" : "[%s]*"), //
				new RegexLeaf("$") //
		);
	}

	public Command<AbstractEntityDiagram> createSingleLine() {
		return new SingleLineCommand2<AbstractEntityDiagram>(getRegexConcatSingleLine(partialPattern)) {

			@Override
			protected CommandExecutionResult executeArg(final AbstractEntityDiagram system, RegexResult arg) {
				final String s = arg.get("NOTE", 0);
				return executeInternal(arg, system, null, BlocLines.getWithNewlines(s));
			}
		};
	}

	public Command<AbstractEntityDiagram> createMultiLine(final boolean withBracket) {
		return new CommandMultilines2<AbstractEntityDiagram>(getRegexConcatMultiLine(partialPattern, withBracket),
				MultilinesStrategy.KEEP_STARTING_QUOTE) {

			@Override
			public String getPatternEnd() {
				if (withBracket) {
					return "(?i)^(\\})$";
				}
				return "(?i)^[%s]*(end[%s]?note)$";
			}

			public CommandExecutionResult executeNow(final AbstractEntityDiagram system, BlocLines lines) {
				// StringUtils.trim(lines, false);
				final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.getFirst499()));
				lines = lines.subExtract(1, 1);
				lines = lines.removeEmptyColumns();

				Url url = null;
				if (lines.size() > 0) {
					final UrlBuilder urlBuilder = new UrlBuilder(system.getSkinParam().getValue("topurl"),
							ModeUrl.STRICT);
					url = urlBuilder.getUrl(lines.getFirst499().toString());
				}
				if (url != null) {
					lines = lines.subExtract(1, 0);
				}

				return executeInternal(line0, system, url, lines);
			}
		};
	}

	private CommandExecutionResult executeInternal(RegexResult line0, AbstractEntityDiagram diagram, Url url,
			BlocLines strings) {

		final String pos = line0.get("POSITION", 0);

		final Code code = Code.of(line0.get("ENTITY", 0));
		final IEntity cl1;
		if (code == null) {
			cl1 = diagram.getLastEntity();
			if (cl1 == null) {
				return CommandExecutionResult.error("Nothing to note to");
			}
		} else if (diagram.isGroup(code)) {
			cl1 = diagram.getGroup(code);
		} else {
			cl1 = diagram.getOrCreateLeaf(code, null, null);
		}

		final IEntity note = diagram
				.createLeaf(UniqueSequence.getCode("GMN"), strings.toDisplay(), LeafType.NOTE, null);

		final Colors colors = color().getColor(line0, diagram.getSkinParam().getIHtmlColorSet());
		note.setColors(colors);
		if (url != null) {
			note.addUrl(url);
		}

		final Position position = Position.valueOf(StringUtils.goUpperCase(pos)).withRankdir(
				diagram.getSkinParam().getRankdir());
		final Link link;

		final LinkType type = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getDashed();
		if (position == Position.RIGHT) {
			link = new Link(cl1, note, type, Display.NULL, 1);
			link.setHorizontalSolitary(true);
		} else if (position == Position.LEFT) {
			link = new Link(note, cl1, type, Display.NULL, 1);
			link.setHorizontalSolitary(true);
		} else if (position == Position.BOTTOM) {
			link = new Link(cl1, note, type, Display.NULL, 2);
		} else if (position == Position.TOP) {
			link = new Link(note, cl1, type, Display.NULL, 2);
		} else {
			throw new IllegalArgumentException();
		}
		diagram.addLink(link);
		return CommandExecutionResult.ok();
	}

}
