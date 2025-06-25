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
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.style.NoStyleAvailableException;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.warning.Warning;

public class CommandSkinParam extends SingleLineCommand2<TitledDiagram> {

	public static final CommandSkinParam ME = new CommandSkinParam();

	private CommandSkinParam() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandSkinParam.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "TYPE", "(skinparam|skinparamlocked)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "NAME", "([\\w.]*(?:\\<\\<[^<>]*\\>\\>)?[\\w.]*)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "VALUE", "([^{}]*)"), RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(TitledDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		try {
			final String name = arg.get("NAME", 0);
			if ("handwritten".equalsIgnoreCase(name))
				diagram.addWarning(new Warning("Please use '!option handwritten true' to enable handwritten "));

			diagram.setParam(name, arg.get("VALUE", 0));
			diagram.setSkinParamUsed(true);

			return CommandExecutionResult.ok();
		} catch (NoStyleAvailableException e) {
			// Logme.error(e);
			return CommandExecutionResult.error("General failure: no style available.");
		}

	}

}
