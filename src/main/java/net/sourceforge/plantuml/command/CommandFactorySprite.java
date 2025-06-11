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
package net.sourceforge.plantuml.command;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.WithSprite;
import net.sourceforge.plantuml.command.note.SingleMultiFactoryCommand;
import net.sourceforge.plantuml.klimt.sprite.Sprite;
import net.sourceforge.plantuml.klimt.sprite.SpriteColorBuilder4096;
import net.sourceforge.plantuml.klimt.sprite.SpriteGrayLevel;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;

public final class CommandFactorySprite implements SingleMultiFactoryCommand<WithSprite> {

	private final static Lazy<Pattern2> END = new Lazy<>(
			() -> Pattern2.cmpile("^end[%s]?sprite|\\}$"));

	public static final CommandFactorySprite ME = new CommandFactorySprite();

	private CommandFactorySprite() {

	}

	private final RegexConcat multiline = RegexConcat.build(CommandFactorySprite.class.getName() + "multi",
			RegexLeaf.start(), //
			new RegexLeaf("sprite"), //
			RegexLeaf.spaceOneOrMore(), //
			new RegexLeaf("\\$?"), //
			new RegexLeaf(1, "NAME", "([-.%pLN_]+)"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexOptional(new RegexLeaf(5, "DIM", "\\[(\\d+)x(\\d+)/(?:(\\d+)(z)?|(color))\\]")), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexLeaf("\\{"), RegexLeaf.end());

	private final RegexConcat singleLine = RegexConcat.build(CommandFactorySprite.class.getName() + "single",
			RegexLeaf.start(), //
			new RegexLeaf("sprite"), //
			RegexLeaf.spaceOneOrMore(), //
			new RegexLeaf("\\$?"), //
			new RegexLeaf(1, "NAME", "([-.%pLN_]+)"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexOptional(new RegexLeaf(5, "DIM", "\\[(\\d+)x(\\d+)/(?:(\\d+)(z)|(color))\\]")), //
			RegexLeaf.spaceOneOrMore(), //
			new RegexLeaf(1, "DATA", "([-_A-Za-z0-9]+)"), RegexLeaf.end());

	private final Command<WithSprite> commandMultiline = new CommandMultilines2<WithSprite>(multiline,
			MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END) {

		@Override
		protected CommandExecutionResult executeNow(final WithSprite system, BlocLines lines, ParserPass currentPass) {
			lines = lines.trim().removeEmptyLines();
			final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());

			lines = lines.subExtract(1, 1);
			lines = lines.removeEmptyColumns();
			if (lines.size() == 0)
				return CommandExecutionResult.error("No sprite defined.");

			return executeInternal(system, line0, lines.getLinesAsStringForSprite());
		}

	};

	public Command<WithSprite> createSingleLine() {
		return new SingleLineCommand2<WithSprite>(singleLine) {

			@Override
			protected CommandExecutionResult executeArg(final WithSprite system, LineLocation location, RegexResult arg,
					ParserPass currentPass) {
				return executeInternal(system, arg, Arrays.asList((String) arg.get("DATA", 0)));
			}

		};
	}

	public Command<WithSprite> createMultiLine(boolean withBracketUnused) {
		return commandMultiline;
	}

	private CommandExecutionResult executeInternal(WithSprite system, RegexResult line0, final List<String> strings) {
		final Sprite sprite;
		if (line0.get("DIM", 0) == null) {
			sprite = SpriteGrayLevel.GRAY_16.buildSprite(-1, -1, strings);
		} else {
			final int width = Integer.parseInt(line0.get("DIM", 0));
			final int height = Integer.parseInt(line0.get("DIM", 1));
			if (line0.get("DIM", 4) == null) {
				final int nbLevel = Integer.parseInt(line0.get("DIM", 2));
				if (nbLevel != 4 && nbLevel != 8 && nbLevel != 16)
					return CommandExecutionResult.error("Only 4, 8 or 16 graylevel are allowed.");

				final SpriteGrayLevel level = SpriteGrayLevel.get(nbLevel);
				if (line0.get("DIM", 3) == null) {
					sprite = level.buildSprite(width, height, strings);
				} else {
					sprite = level.buildSpriteZ(width, height, concat(strings));
					if (sprite == null)
						return CommandExecutionResult.error("Cannot decode sprite.");

				}
			} else {
				sprite = SpriteColorBuilder4096.buildSprite(strings);
			}
		}
		system.addSprite(line0.get("NAME", 0), sprite);
		return CommandExecutionResult.ok();
	}

	private String concat(final List<String> strings) {
		final StringBuilder sb = new StringBuilder();
		for (String s : strings)
			sb.append(StringUtils.trin(s));

		return sb.toString();
	}

}
