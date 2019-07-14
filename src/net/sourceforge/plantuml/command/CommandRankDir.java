/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 */
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Rankdir;

public class CommandRankDir extends SingleLineCommand2<CucaDiagram> {

	public CommandRankDir() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandRankDir.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("DIRECTION", "(left[%s]to[%s]right|top[%s]to[%s]bottom)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("direction"), //
				RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(CucaDiagram diagram, LineLocation location, RegexResult arg) {
		final String s = StringUtils.goUpperCase(arg.get("DIRECTION", 0)).replace(' ', '_');
		((SkinParam) diagram.getSkinParam()).setRankdir(Rankdir.valueOf(s));
		// diagram.setRankdir(Rankdir.valueOf(s));
		return CommandExecutionResult.ok();
	}

}
