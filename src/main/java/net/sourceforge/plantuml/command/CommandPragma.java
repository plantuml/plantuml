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

import java.util.StringTokenizer;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandPragma extends SingleLineCommand2<TitledDiagram> {

	public static final CommandPragma ME = new CommandPragma();

	private CommandPragma() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPragma.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("!pragma"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "NAME", "([A-Za-z_][A-Za-z_0-9]*)"), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "VALUE", "(.*)") //
						)), RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(TitledDiagram system, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		final String name = StringUtils.goLowerCase(arg.get("NAME", 0));
		final String value = arg.get("VALUE", 0);
		if (name.equalsIgnoreCase("svgsize")) {
			if (value.contains(" ")) {
				final StringTokenizer st = new StringTokenizer(value);
				system.getSkinParam().setSvgSize(st.nextToken(), st.nextToken());
			}
		} else {
			system.getPragma().define(name, value);
			// ::comment when __CORE__
			if (name.equalsIgnoreCase("graphviz_dot") && value.equalsIgnoreCase("jdot"))
				return CommandExecutionResult.error(
						"This directive has been renamed to '!pragma layout smetana'. Please update your diagram.");

			if (name.equalsIgnoreCase("graphviz_dot"))
				return CommandExecutionResult.error("This directive has been renamed to '!pragma layout " + value
						+ "'. Please update your diagram.");

			if (name.equalsIgnoreCase("layout") && value.equalsIgnoreCase("smetana"))
				system.setUseSmetana(true);

			if (name.equalsIgnoreCase("layout") && value.equalsIgnoreCase("elk"))
				system.setUseElk(true);

			if (name.equalsIgnoreCase("layout") && value.equalsIgnoreCase(GraphvizRuntimeEnvironment.VIZJS))
				system.getSkinParam().setUseVizJs(true);
			// ::done

		}
		return CommandExecutionResult.ok();
	}

}
