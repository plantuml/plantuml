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
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.WithSprite;
import net.sourceforge.plantuml.annotation.Explain;
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

public final class CommandFactorySprite implements SingleMultiFactoryCommand<TitledDiagram> {

	private final static Lazy<Pattern2> END = new Lazy<>(() -> Pattern2.cmpile("^end[%s]?sprite|\\}$"));

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

	private final Command<TitledDiagram> commandMultiline = new CommandMultilines2<TitledDiagram>(multiline,
			MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END) {

		@Override
		@Explain
		protected String explainNow(BlocLines lines) {
			// Mirror executeNow: same preprocessing, then the lines between the
			// declaration and the closing '}' (or 'end sprite') are the data.
			lines = lines.trim().removeEmptyLines();
			final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
			if (line0 == null)
				return "Defining a sprite";

			final StringBuilder sb = new StringBuilder(explainSprite(line0));
			final int bodyCount = lines.size() > 2 ? lines.size() - 2 : 0;
			if (bodyCount > 0)
				sb.append(", with ").append(bodyCount).append(bodyCount == 1 ? " line" : " lines").append(" of data");
			else
				sb.append(" (rejected at execution: no sprite data)");

			return sb.toString();
		}

		@Override
		protected CommandExecutionResult executeNow(final TitledDiagram system, BlocLines lines,
				ParserPass currentPass) {
			lines = lines.trim().removeEmptyLines();
			final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());

			lines = lines.subExtract(1, 1);
			lines = lines.removeEmptyColumns();
			if (lines.size() == 0)
				return CommandExecutionResult.error("No sprite defined.");

			return executeInternal(system, line0, lines.getLinesAsStringForSprite());
		}

	};

	public Command<TitledDiagram> createSingleLine() {
		return new SingleLineCommand2<TitledDiagram>(singleLine) {

			@Override
			@Explain
			protected String explainArg(LineLocation location, RegexResult arg) {
				return explainSprite(arg) + " from an inline data string";
			}

			@Override
			protected CommandExecutionResult executeArg(final TitledDiagram system, LineLocation location,
					RegexResult arg, ParserPass currentPass) {
				return executeInternal(system, arg, Arrays.asList((String) arg.get("DATA", 0)));
			}

		};
	}

	/**
	 * Builds the explanation shared by the single line and the multiline flavors,
	 * mirroring the fields read by
	 * {@link #executeInternal(WithSprite, RegexResult, List)}.
	 */
	@Explain
	private String explainSprite(RegexResult line0) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Defining the sprite '").append(line0.get("NAME", 0)).append("'");

		if (line0.get("DIM", 0) == null) {
			// No [WxH/...] specification: 16 gray levels, dimensions deduced
			// from the data.
			sb.append(", 16 gray levels, dimensions deduced from the data");
		} else {
			sb.append(", ").append(line0.get("DIM", 0)).append(" x ").append(line0.get("DIM", 1)).append(" pixels");
			if (line0.get("DIM", 4) != null) {
				// The [WxH/color] form uses the 4096 colors encoding.
				sb.append(", 4096 colors");
			} else {
				final String nbLevel = line0.get("DIM", 2);
				sb.append(", ").append(nbLevel).append(" gray levels");
				if (line0.get("DIM", 3) != null)
					sb.append(", compressed (z) encoding");
				if (nbLevel.equals("4") == false && nbLevel.equals("8") == false && nbLevel.equals("16") == false)
					sb.append(" (rejected at execution: only 4, 8 or 16 gray levels are allowed)");
			}
		}

		return sb.toString();
	}

	public Command<TitledDiagram> createMultiLine(boolean withBracketUnused) {
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
