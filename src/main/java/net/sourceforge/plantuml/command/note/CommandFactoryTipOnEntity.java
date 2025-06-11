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

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.NameAndCodeParser;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
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

public final class CommandFactoryTipOnEntity implements SingleMultiFactoryCommand<AbstractEntityDiagram> {

	private final String key = "a";

	private RegexConcat getRegexConcatMultiLine(final boolean withBracket) {
		if (withBracket) {
			return RegexConcat.build(CommandFactoryTipOnEntity.class.getName() + key + withBracket, RegexLeaf.start(), //
					new RegexLeaf("note"), //
					RegexLeaf.spaceOneOrMore(), //
					new RegexLeaf(1, "POSITION", "(right|left)"), //
					RegexLeaf.spaceOneOrMore(), //
					new RegexLeaf("of"), //
					RegexLeaf.spaceOneOrMore(), //
					NameAndCodeParser.codeWithMemberForClass(), //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf(4, "TAGS1", Stereotag.pattern() + "?"), //
					StereotypePattern.optional("STEREO"), //
					new RegexLeaf(4, "TAGS2", Stereotag.pattern() + "?"), //
					RegexLeaf.spaceZeroOrMore(), //
					ColorParser.exp1(), //
					RegexLeaf.spaceZeroOrMore(), //
					UrlBuilder.OPTIONAL, //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("\\{"), //
					RegexLeaf.end() //
			);
		}
		return RegexConcat.build(CommandFactoryTipOnEntity.class.getName() + key + withBracket, RegexLeaf.start(), //
				new RegexLeaf("note"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "POSITION", "(right|left)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("of"), //
				RegexLeaf.spaceOneOrMore(), //
				NameAndCodeParser.codeWithMemberForClass(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(4, "TAGS1", Stereotag.pattern() + "?"), //
				StereotypePattern.optional("STEREO"), //
				new RegexLeaf(4, "TAGS2", Stereotag.pattern() + "?"), //
				RegexLeaf.spaceZeroOrMore(), //
				ColorParser.exp1(), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.end() //
		);
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	public Command<AbstractEntityDiagram> createSingleLine() {
		throw new UnsupportedOperationException();
	}

	private final static Lazy<Pattern2> END = new Lazy<>(
			() -> Pattern2.cmpile("^[%s]*(end[%s]?note)$"));

	private final static Lazy<Pattern2> END_WITH_BRACKET = new Lazy<>(
			() -> Pattern2.cmpile("^(\\})$"));

	public Command<AbstractEntityDiagram> createMultiLine(final boolean withBracket) {
		return new CommandMultilines2<AbstractEntityDiagram>(getRegexConcatMultiLine(withBracket),
				MultilinesStrategy.KEEP_STARTING_QUOTE, Trim.BOTH, withBracket ? END_WITH_BRACKET : END) {

			@Override
			protected CommandExecutionResult executeNow(final AbstractEntityDiagram system, BlocLines lines,
					ParserPass currentPass) throws NoSuchColorException {
				// StringUtils.trim(lines, false);
				final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
				lines = lines.subExtract(1, 1).expandsNewline(false);
				lines = lines.removeEmptyColumns();
				final Display display = lines.toDisplay();

				Url url = null;
				if (line0.get("URL", 0) != null) {
					final UrlBuilder urlBuilder = new UrlBuilder(system.getSkinParam().getValue("topurl"),
							UrlMode.STRICT);
					url = urlBuilder.getUrl(line0.get("URL", 0));
				}

				return executeInternal(lines.getLocation(), line0, system, url, display);
			}
		};
	}

	private CommandExecutionResult executeInternal(LineLocation location, RegexResult line0,
			AbstractEntityDiagram diagram, Url url, Display display) throws NoSuchColorException {

		final String pos = line0.get("POSITION", 0);

		final String idShort = line0.get("CODE", 0);
		final String member = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(line0.get("CODE", 1));

		final Quark<Entity> quark = diagram.quarkInContext(true, idShort);
		final Entity cl1 = quark.getData();
		if (cl1 == null)
			return CommandExecutionResult.error("Nothing to note to");

		final Position position = Position.valueOf(StringUtils.goUpperCase(pos))
				.withRankdir(diagram.getSkinParam().getRankdir());

		final String tmp = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(idShort + "$$$" + position.name());
		final Quark<Entity> identTip = diagram.quarkInContext(true, tmp);
		Entity tips = identTip.getData();

		if (tips == null) {
			tips = diagram.reallyCreateLeaf(location, identTip, Display.getWithNewlines(diagram.getPragma(), ""),
					LeafType.TIPS, null);
			final LinkType type = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getInvisible();
			final Link link;
			if (position == Position.RIGHT)
				link = new Link(location, diagram, diagram.getSkinParam().getCurrentStyleBuilder(), cl1, tips, type,
						LinkArg.noDisplay(1));
			else
				link = new Link(location, diagram, diagram.getSkinParam().getCurrentStyleBuilder(), tips, cl1, type,
						LinkArg.noDisplay(1));

			diagram.addLink(link);
		}
		tips.putTip(member, display);

		Colors colors = color().getColor(line0, diagram.getSkinParam().getIHtmlColorSet());

		final String stereotypeString = line0.get("STEREO", 0);
		Stereotype stereotype = null;
		if (stereotypeString != null) {
			stereotype = Stereotype.build(stereotypeString);
			colors = colors.applyStereotypeForNote(stereotype, diagram.getSkinParam(), ColorParam.noteBackground,
					ColorParam.noteBorder);
		}
		if (stereotypeString != null)
			tips.setStereotype(stereotype);

		tips.setColors(colors);

		return CommandExecutionResult.ok();
	}

}
