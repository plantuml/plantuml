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
import java.util.Collection;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandPragma extends SingleLineCommand2<TitledDiagram> {

	private static final Collection<String> FIRST_TOKENS = Arrays.asList("!");

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
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		final String name = StringUtils.goLowerCase(arg.get("NAME", 0));
		final String value = arg.get("VALUE", 0);

		// Mirror the special cases of executeArg.
		if (name.equals("svgsize")) {
			if (value != null && svgSizeWords(value).countTokens() >= 2)
				return "Setting the SVG size to '" + value + "'";

			return "Setting the SVG size (ignored: two space-separated values are expected)";
		}

		if (name.equals("graphviz_dot"))
			return "Selecting the layout engine (rejected at execution: "
					+ "this directive has been renamed to '!pragma layout ...')";

		if (name.equals("layout") && value != null)
			return "Selecting the '" + value + "' layout engine";

		final StringBuilder sb = new StringBuilder();
		sb.append("Defining the pragma '").append(name).append("'");
		if (value != null)
			sb.append(" with value '").append(value).append("'");

		return sb.toString();
	}

	// The two values of svgsize, separated by any blank %s accepts: a tab or a
	// non-breaking space was not seen as a separator, and the pragma was ignored
	private static StringTokenizer svgSizeWords(String value) {
		return new StringTokenizer(value, " \t\r\n\f\u00A0");
	}

	@Override
	protected CommandExecutionResult executeArg(TitledDiagram system, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		final String name = StringUtils.goLowerCase(arg.get("NAME", 0));
		final String value = arg.get("VALUE", 0);
		// The value is optional ("!pragma layout" alone): it may be null below
		if (name.equalsIgnoreCase("svgsize")) {
			final StringTokenizer st = value == null ? null : svgSizeWords(value);
			if (st != null && st.countTokens() >= 2)
				system.getSkinParam().setSvgSize(st.nextToken(), st.nextToken());
		} else {
			system.getPragma().define(name, value);

			// Honored in every build: the browser (TeaVM) build uses it to lay out
			// class/component/deployment/state/usecase diagrams without viz-global.js.
			if (name.equalsIgnoreCase("layout") && "smetana".equalsIgnoreCase(value))
				system.setUseSmetana(true);

			if (!TeaVM.isTeaVM()) {
				if (name.equalsIgnoreCase("graphviz_dot") && "jdot".equalsIgnoreCase(value))
					return CommandExecutionResult.error(
							"This directive has been renamed to '!pragma layout smetana'. Please update your diagram.");

				if (name.equalsIgnoreCase("graphviz_dot"))
					return CommandExecutionResult.error("This directive has been renamed to '!pragma layout " + value
							+ "'. Please update your diagram.");

				if (name.equalsIgnoreCase("layout") && "elk".equalsIgnoreCase(value))
					system.setUseElk(true);

				if (name.equalsIgnoreCase("layout") && GraphvizRuntimeEnvironment.VIZJS.equalsIgnoreCase(value))
					system.getSkinParam().setUseVizJs(true);
			}

		}
		return CommandExecutionResult.ok();
	}

	@Override
	public Collection<String> mandatoryFirstTokensFast() {
		return FIRST_TOKENS;
	}

}
