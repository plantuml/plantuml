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

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClassMultilines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.skin.ColorParam;
import net.sourceforge.plantuml.stereo.Stereotag;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.utils.Position;

public final class CommandFactoryNoteOnEntity implements SingleMultiFactoryCommand<AbstractEntityDiagram> {

	private final IRegex partialPattern;
	private final String key;
	private final ParserPass selectedPass;

	public CommandFactoryNoteOnEntity(String key, IRegex partialPattern, ParserPass selectedPass) {
		this.partialPattern = partialPattern;
		this.key = key;
		this.selectedPass = selectedPass;
	}

	private IRegex getRegexConcatSingleLine(IRegex partialPattern) {
		return RegexConcat.build(CommandFactoryNoteOnEntity.class.getName() + key + "single", RegexLeaf.start(), //
				new RegexLeaf("note"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)"), //
				new RegexOr(//
						new RegexConcat(RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("of"), //
								RegexLeaf.spaceOneOrMore(), partialPattern), //
						new RegexLeaf("")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("TAGS1", Stereotag.pattern() + "?"), //
				StereotypePattern.optional("STEREO"), //
				new RegexLeaf("TAGS2", Stereotag.pattern() + "?"), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(":"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("NOTE", "(.*)"), //
				RegexLeaf.end() //
		);
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	private IRegex getRegexConcatMultiLine(IRegex partialPattern, final boolean withBracket) {
		if (withBracket) {
			return RegexConcat.build(CommandFactoryNoteOnEntity.class.getName() + key + "multi" + withBracket,
					RegexLeaf.start(), //
					new RegexLeaf("note"), //
					RegexLeaf.spaceOneOrMore(), //
					new RegexLeaf("POSITION", "(right|left|top|bottom)"), //
					new RegexOr(//
							new RegexConcat(RegexLeaf.spaceOneOrMore(), //
									new RegexLeaf("of"), //
									RegexLeaf.spaceOneOrMore(), //
									partialPattern), //
							new RegexLeaf("")), //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("TAGS1", Stereotag.pattern() + "?"), //
					StereotypePattern.optional("STEREO"), //
					new RegexLeaf("TAGS2", Stereotag.pattern() + "?"), //
					RegexLeaf.spaceZeroOrMore(), //
					color().getRegex(), //
					RegexLeaf.spaceZeroOrMore(), //
					UrlBuilder.OPTIONAL, //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("\\{"), //
					RegexLeaf.end() //
			);
		}
		return RegexConcat.build(CommandFactoryNoteOnEntity.class.getName() + key + "multi" + withBracket,
				RegexLeaf.start(), //
				new RegexLeaf("note"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)"), //
				new RegexOr(//
						new RegexConcat(RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("of"), //
								RegexLeaf.spaceOneOrMore(), //
								partialPattern), //
						new RegexLeaf("")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("TAGS1", Stereotag.pattern() + "?"), //
				StereotypePattern.optional("STEREO"), //
				new RegexLeaf("TAGS2", Stereotag.pattern() + "?"), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.end() //
		);
	}

	public Command<AbstractEntityDiagram> createSingleLine() {
		return new SingleLineCommand2<AbstractEntityDiagram>(getRegexConcatSingleLine(partialPattern)) {

			@Override
			protected CommandExecutionResult executeArg(final AbstractEntityDiagram diagram, LineLocation location,
					RegexResult arg, ParserPass currentPass) throws NoSuchColorException {
				final Display display = Display.getWithNewlines(diagram.getPragma(), arg.get("NOTE", 0));
				return executeInternal(arg, diagram, null, display);
			}

			@Override
			public boolean isEligibleFor(ParserPass pass) {
				return selectedPass == pass;
			}
		};
	}

	public Command<AbstractEntityDiagram> createMultiLine(final boolean withBracket) {
		return new CommandMultilines2<AbstractEntityDiagram>(getRegexConcatMultiLine(partialPattern, withBracket),
				MultilinesStrategy.KEEP_STARTING_QUOTE, Trim.BOTH) {

			@Override
			public String getPatternEnd() {
				if (withBracket)
					return "^(\\})$";

				return "^[%s]*(end[%s]?note)$";
			}

			@Override
			protected CommandExecutionResult executeNow(final AbstractEntityDiagram system, BlocLines lines,
					ParserPass currentPass) throws NoSuchColorException {
				// StringUtils.trim(lines, false);
				final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
				lines = lines.subExtract(1, 1).expandsJaws5();
				lines = lines.removeEmptyColumns();
				final Display display = lines.toDisplay();

				Url url = null;
				if (line0.get("URL", 0) != null) {
					final UrlBuilder urlBuilder = new UrlBuilder(system.getSkinParam().getValue("topurl"),
							UrlMode.STRICT);
					url = urlBuilder.getUrl(line0.get("URL", 0));
				}

				return executeInternal(line0, system, url, display);
			}

			@Override
			public boolean isEligibleFor(ParserPass pass) {
				return selectedPass == pass;
			}

		};
	}

	private CommandExecutionResult executeInternal(RegexResult line0, AbstractEntityDiagram diagram, Url url,
			Display display) throws NoSuchColorException {
		final String pos = line0.get("POSITION", 0);
		final String idShort = diagram.cleanId(line0.get("CODE", 0));
		final Entity cl1;
		if (idShort == null) {
			cl1 = diagram.getLastEntity();
			if (cl1 == null)
				return CommandExecutionResult.error("Nothing to note to");

		} else {
			final Quark<Entity> quark = diagram.quarkInContext(true, idShort);
			cl1 = quark.getData();
			if (cl1 == null)
				return CommandExecutionResult.error("Not known: " + idShort);
		}

		final Position position = Position.valueOf(StringUtils.goUpperCase(pos))
				.withRankdir(diagram.getSkinParam().getRankdir());
		Colors colors = color().getColor(line0, diagram.getSkinParam().getIHtmlColorSet());

		final String stereotypeString = line0.get("STEREO", 0);
		Stereotype stereotype = null;
		if (stereotypeString != null) {
			stereotype = Stereotype.build(stereotypeString);
			colors = colors.applyStereotypeForNote(stereotype, diagram.getSkinParam(), ColorParam.noteBackground,
					ColorParam.noteBorder);
		}

		if (diagram.getPragma().useKermor() && cl1.isGroup()) {
			cl1.addNote(display, position, colors);
			return CommandExecutionResult.ok();
		}

		final String tmp = diagram.getUniqueSequence("GMN");
		final Quark<Entity> quark = diagram.quarkInContext(true, tmp);
		final Entity note = diagram.reallyCreateLeaf(quark, display, LeafType.NOTE, null);

		if (stereotypeString != null)
			note.setStereotype(stereotype);

		note.setColors(colors);
		if (url != null)
			note.addUrl(url);

		CommandCreateClassMultilines.addTags(note, line0.getLazzy("TAGS", 0));

		final Link link;

		final LinkType type = new LinkType(LinkDecor.NONE, LinkDecor.NONE).goDashed();
		if (position == Position.RIGHT) {
			link = new Link(diagram, diagram.getSkinParam().getCurrentStyleBuilder(), cl1, note, type,
					LinkArg.noDisplay(1));
			link.setHorizontalSolitary(true);
		} else if (position == Position.LEFT) {
			link = new Link(diagram, diagram.getSkinParam().getCurrentStyleBuilder(), note, cl1, type,
					LinkArg.noDisplay(1));
			link.setHorizontalSolitary(true);
		} else if (position == Position.BOTTOM) {
			link = new Link(diagram, diagram.getSkinParam().getCurrentStyleBuilder(), cl1, note, type,
					LinkArg.noDisplay(2));
		} else if (position == Position.TOP) {
			link = new Link(diagram, diagram.getSkinParam().getCurrentStyleBuilder(), note, cl1, type,
					LinkArg.noDisplay(2));
		} else {
			throw new IllegalArgumentException();
		}
		diagram.addLink(link);
		return CommandExecutionResult.ok();
	}

}
