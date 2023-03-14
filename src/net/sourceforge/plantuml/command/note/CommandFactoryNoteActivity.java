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
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.utils.Position;

public final class CommandFactoryNoteActivity implements SingleMultiFactoryCommand<ActivityDiagram> {

	private IRegex getRegexConcatMultiLine() {
		return RegexConcat.build(CommandFactoryNoteActivity.class.getName() + "multi", RegexLeaf.start(), //
				new RegexLeaf("note"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)"), //
				RegexLeaf.spaceZeroOrMore(), //
				ColorParser.exp1(), //
				RegexLeaf.spaceZeroOrMore(), RegexLeaf.end());
	}

	private IRegex getRegexConcatSingleLine() {
		return RegexConcat.build(CommandFactoryNoteActivity.class.getName() + "single", RegexLeaf.start(), //
				new RegexLeaf("note"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("POSITION", "(right|left|top|bottom)"), //
				RegexLeaf.spaceZeroOrMore(), //
				ColorParser.exp1(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(":"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("NOTE", "(.*)"), RegexLeaf.end());
	}

	public Command<ActivityDiagram> createMultiLine(boolean withBracket) {
		return new CommandMultilines2<ActivityDiagram>(getRegexConcatMultiLine(),
				MultilinesStrategy.KEEP_STARTING_QUOTE, Trim.BOTH) {

			@Override
			public String getPatternEnd() {
				return "^[%s]*end[%s]?note$";
			}

			public final CommandExecutionResult executeNow(final ActivityDiagram diagram, BlocLines lines)
					throws NoSuchColorException {
				// StringUtils.trim(lines, true);
				final RegexResult arg = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
				lines = lines.subExtract(1, 1);
				lines = lines.removeEmptyColumns();

				Display strings = lines.toDisplay();

				Url url = null;
				if (strings.size() > 0) {
					final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"),
							UrlMode.STRICT);
					url = urlBuilder.getUrl(strings.get(0).toString());
				}
				if (url != null)
					strings = strings.subList(1, strings.size());

				final String codeString = diagram.getUniqueSequence("GMN");
				final Quark<Entity> quark = diagram.quarkInContext(true, codeString);
				final Entity note = diagram.reallyCreateLeaf(quark, strings, LeafType.NOTE, null);
				if (url != null)
					note.addUrl(url);

				return executeInternal(diagram, arg, note);
			}
		};
	}

	public Command<ActivityDiagram> createSingleLine() {
		return new SingleLineCommand2<ActivityDiagram>(getRegexConcatSingleLine()) {

			@Override
			protected CommandExecutionResult executeArg(final ActivityDiagram diagram, LineLocation location,
					RegexResult arg) throws NoSuchColorException {
				final String tmp = diagram.getUniqueSequence("GN");
				final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(tmp));

				final Entity note = diagram.createNote(quark, tmp, Display.getWithNewlines(arg.get("NOTE", 0)));
				return executeInternal(diagram, arg, note);
			}
		};
	}

	private CommandExecutionResult executeInternal(ActivityDiagram diagram, RegexResult arg, Entity note)
			throws NoSuchColorException {

		final String s = arg.get("COLOR", 0);
		note.setSpecificColorTOBEREMOVED(ColorType.BACK,
				s == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s));

		Entity activity = diagram.getLastEntityConsulted();
		if (activity == null)
			activity = diagram.getStart();

		final Link link;

		final Position position = Position.valueOf(StringUtils.goUpperCase(arg.get("POSITION", 0)))
				.withRankdir(diagram.getSkinParam().getRankdir());

		final LinkType type = new LinkType(LinkDecor.NONE, LinkDecor.NONE).goDashed();

		if (position == Position.RIGHT)
			link = new Link(diagram.getEntityFactory(), diagram.getSkinParam().getCurrentStyleBuilder(), activity, note,
					type, LinkArg.noDisplay(1));
		else if (position == Position.LEFT)
			link = new Link(diagram.getEntityFactory(), diagram.getSkinParam().getCurrentStyleBuilder(), note, activity,
					type, LinkArg.noDisplay(1));
		else if (position == Position.BOTTOM)
			link = new Link(diagram.getEntityFactory(), diagram.getSkinParam().getCurrentStyleBuilder(), activity, note,
					type, LinkArg.noDisplay(2));
		else if (position == Position.TOP)
			link = new Link(diagram.getEntityFactory(), diagram.getSkinParam().getCurrentStyleBuilder(), note, activity,
					type, LinkArg.noDisplay(2));
		else
			throw new IllegalArgumentException();

		diagram.addLink(link);
		return CommandExecutionResult.ok();
	}

}
