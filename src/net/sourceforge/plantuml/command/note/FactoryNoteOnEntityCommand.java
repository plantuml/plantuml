/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
package net.sourceforge.plantuml.command.note;

import java.util.List;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
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
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.utils.UniqueSequence;

public final class FactoryNoteOnEntityCommand implements SingleMultiFactoryCommand<AbstractEntityDiagram> {

	private final IRegex partialPattern;

	public FactoryNoteOnEntityCommand(IRegex partialPattern) {
		this.partialPattern = partialPattern;
	}

	private RegexConcat getRegexConcatSingleLine(IRegex partialPattern) {
		return new RegexConcat(new RegexLeaf("^note[%s]+"), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)"), //
				new RegexOr(//
						new RegexConcat(new RegexLeaf("[%s]+of[%s]+"), partialPattern), //
						new RegexLeaf("")), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("COLOR", "(" + HtmlColorUtils.COLOR_REGEXP + ")?"), //
				new RegexLeaf("[%s]*:[%s]*"), //
				new RegexLeaf("NOTE", "(.*)"), //
				new RegexLeaf("$") //
		);
	}

	private RegexConcat getRegexConcatMultiLine(IRegex partialPattern) {
		return new RegexConcat(new RegexLeaf("^note[%s]+"), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)"), //
				new RegexOr(//
						new RegexConcat(new RegexLeaf("[%s]+of[%s]+"), partialPattern), //
						new RegexLeaf("")), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("COLOR", "(" + HtmlColorUtils.COLOR_REGEXP + ")?"), //
				new RegexLeaf("[%s]*\\{?"), //
				new RegexLeaf("$") //
		);
	}

	public Command<AbstractEntityDiagram> createSingleLine() {
		return new SingleLineCommand2<AbstractEntityDiagram>(getRegexConcatSingleLine(partialPattern)) {

			@Override
			protected CommandExecutionResult executeArg(final AbstractEntityDiagram system, RegexResult arg) {
				final String s = arg.get("NOTE", 0);
				return executeInternal(arg, system, null, StringUtils.getWithNewlines2(s));
			}
		};
	}

	public Command<AbstractEntityDiagram> createMultiLine() {
		return new CommandMultilines2<AbstractEntityDiagram>(getRegexConcatMultiLine(partialPattern),
				MultilinesStrategy.KEEP_STARTING_QUOTE) {

			@Override
			public String getPatternEnd() {
				return "(?i)^(end[%s]?note|\\})$";
			}

			public CommandExecutionResult executeNow(final AbstractEntityDiagram system, List<String> lines) {
				// StringUtils.trim(lines, false);
				final RegexResult line0 = getStartingPattern().matcher(lines.get(0).trim());

				List<String> strings = StringUtils.removeEmptyColumns(lines.subList(1, lines.size() - 1));
				Url url = null;
				if (strings.size() > 0) {
					final UrlBuilder urlBuilder = new UrlBuilder(system.getSkinParam().getValue("topurl"),
							ModeUrl.STRICT);
					url = urlBuilder.getUrl(strings.get(0));
				}
				if (url != null) {
					strings = strings.subList(1, strings.size());
				}

				return executeInternal(line0, system, url, strings);
			}
		};
	}

	private CommandExecutionResult executeInternal(RegexResult line0, AbstractEntityDiagram diagram, Url url,
			List<? extends CharSequence> s) {

		final String pos = line0.get("POSITION", 0);

		final Code code = Code.of(line0.get("ENTITY", 0));
		final IEntity cl1;
		if (code == null) {
			cl1 = diagram.getLastEntity();
			if (cl1 == null) {
				return CommandExecutionResult.error("Nothing to note to");
			}
		} else {
			cl1 = diagram.getOrCreateLeaf(code, null, null);
		}

		final IEntity note = diagram.createLeaf(UniqueSequence.getCode("GMN"), Display.create(s), LeafType.NOTE, null);
		note.setSpecificBackcolor(diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(line0.get("COLOR", 0)));
		if (url != null) {
			note.addUrl(url);
		}

		final Position position = Position.valueOf(StringUtils.goUpperCase(pos)).withRankdir(diagram.getSkinParam().getRankdir());
		final Link link;

		final LinkType type = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getDashed();
		if (position == Position.RIGHT) {
			link = new Link(cl1, note, type, null, 1);
			link.setHorizontalSolitary(true);
		} else if (position == Position.LEFT) {
			link = new Link(note, cl1, type, null, 1);
			link.setHorizontalSolitary(true);
		} else if (position == Position.BOTTOM) {
			link = new Link(cl1, note, type, null, 2);
		} else if (position == Position.TOP) {
			link = new Link(note, cl1, type, null, 2);
		} else {
			throw new IllegalArgumentException();
		}
		diagram.addLink(link);
		return CommandExecutionResult.ok();
	}

}
