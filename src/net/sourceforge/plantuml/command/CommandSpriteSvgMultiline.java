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

import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.emoji.SvgNanoParser;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandSpriteSvgMultiline extends CommandMultilines2<TitledDiagram> {

	public static final CommandSpriteSvgMultiline ME = new CommandSpriteSvgMultiline();

	private CommandSpriteSvgMultiline() {
		super(getRegexConcat(), MultilinesStrategy.KEEP_STARTING_QUOTE, Trim.BOTH);
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandSpriteSvgMultiline.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("sprite"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("\\$?"), //
				new RegexLeaf("NAME", "([-%pLN_]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("SVGSTART", "(\\<svg\\b.*)"), //
				RegexLeaf.end());
	}

	@Override
	public String getPatternEnd() {
		return "(.*\\</svg\\>)$";
	}

	@Override
	protected CommandExecutionResult executeNow(TitledDiagram system, BlocLines lines) throws NoSuchColorException {

		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		final String svgStart = line0.get("SVGSTART", 0);
		lines = lines.subExtract(1, 0);

		final StringBuilder svg = new StringBuilder(svgStart);
		for (StringLocated sl : lines)
			svg.append(sl.getString());

		final SvgNanoParser nanoParser = new SvgNanoParser(svg.toString(), true);
		system.addSprite(line0.get("NAME", 0), nanoParser);

		return CommandExecutionResult.ok();
	}
}
