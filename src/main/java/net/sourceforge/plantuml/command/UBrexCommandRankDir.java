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
 */
package net.sourceforge.plantuml.command;

import com.plantuml.ubrex.UnicodeBracketedExpression;
import com.plantuml.ubrex.builder.UBrexConcat;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexNamed;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.klimt.geom.Rankdir;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.skin.SkinParam;
import net.sourceforge.plantuml.utils.LineLocation;

public class UBrexCommandRankDir extends UBrexSingleLineCommand2<TitledDiagram> {

	public UBrexCommandRankDir() {
		super(getRegexConcat());
	}

	static UnicodeBracketedExpression getRegexConcat() {
		return UBrexConcat.build(
				new UBrexNamed("DIRECTION", new UBrexLeaf("【 left∙to∙right ┇ top∙to∙bottom 】")), //
				UBrexLeaf.spaceOneOrMore(), //
				new UBrexLeaf("direction"), //
				UBrexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(TitledDiagram diagram, LineLocation location, RegexResult arg, ParserPass currentPass) {
		final String s = StringUtils.goUpperCase(arg.get("DIRECTION", 0)).replace(' ', '_');
		((SkinParam) diagram.getSkinParam()).setRankdir(Rankdir.valueOf(s));
		// diagram.setRankdir(Rankdir.valueOf(s));
		return CommandExecutionResult.ok();
	}

}
