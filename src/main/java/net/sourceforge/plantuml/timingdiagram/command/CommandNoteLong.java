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
package net.sourceforge.plantuml.timingdiagram.command;

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.timingdiagram.Player;
import net.sourceforge.plantuml.timingdiagram.TimeTick;
import net.sourceforge.plantuml.timingdiagram.TimingDiagram;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.Position;

public class CommandNoteLong extends CommandMultilines2<TimingDiagram> {

	private final static Lazy<Pattern2> END = new Lazy<>(
			() -> Pattern2.cmpile("^end[%s]?note$"));

	public CommandNoteLong() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END);
	}

	@Override
	protected CommandExecutionResult executeNow(final TimingDiagram diagram, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {

		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		lines = lines.subExtract(1, 1);
		lines = lines.removeEmptyColumns();
		final String code = line0.get("CODE", 0);
		final Player player = diagram.getPlayer(code);
		if (player == null)
			return CommandExecutionResult.error("Unkown \"" + code + "\"");

		final Display note = lines.toDisplay();
		final TimeTick now = diagram.getNow();
		// final Colors colors = color().getColor(arg,
		// diagram.getSkinParam().getIHtmlColorSet());

		final String stereotypeString = line0.get("STEREO", 0);
		Stereotype stereotype = null;
		if (stereotypeString != null)
			stereotype = Stereotype.build(stereotypeString);

		player.addNote(now, note, Position.fromString(line0.get("POSITION", 0)), stereotype);
		return CommandExecutionResult.ok();
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandNoteLong.class.getName(), RegexLeaf.start(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("note"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "POSITION", "(top|bottom)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("of"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "CODE", CommandTimeMessage.PLAYER_CODE), //
				RegexLeaf.spaceZeroOrMore(), //
				StereotypePattern.optional("STEREO"), //
				RegexLeaf.spaceZeroOrMore(), //
				RegexLeaf.end());
	}

}
