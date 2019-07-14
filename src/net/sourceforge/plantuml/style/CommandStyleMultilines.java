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
 *
 */
package net.sourceforge.plantuml.style;

import java.util.EnumMap;
import java.util.Map;

import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;

public class CommandStyleMultilines extends CommandMultilines2<UmlDiagram> {

	public CommandStyleMultilines() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^[%s]*\\}[%s]*$";
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandStyleMultilines.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("TYPE", "(style|stereotype)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("NAME", "(\\w+(?:\\+\\w+)*)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("\\{"), //
				RegexLeaf.end() //
				);
	}

	@Override
	public boolean syntaxWithFinalBracket() {
		return true;
	}

	protected CommandExecutionResult executeNow(UmlDiagram diagram, BlocLines lines) {
		if (SkinParam.USE_STYLES()) {
			lines = lines.trimSmart(1);
			final Style modifiedStyle = getDeclaredStyle(lines, diagram.getSkinParam().getCurrentStyleBuilder());
			diagram.getSkinParam().muteStyle(modifiedStyle);
		}

		return CommandExecutionResult.ok();
	}

	public final static Pattern2 p1 = MyPattern.cmpile("^([\\w]+)[%s]+(.*)$");

	public Style getDeclaredStyle(BlocLines lines, AutomaticCounter counter) {
		lines = lines.trimSmart(1);
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst499().getTrimmed().getString());
		final String name = line0.get("NAME", 0);
		final String kind = line0.get("TYPE", 0).toUpperCase();
		// System.err.println("name=" + name);
		if (lines.size() > 1) {
			lines = lines.subExtract(1, 1);
		}
		lines = lines.trim(true);

		final Map<PName, Value> map = new EnumMap<PName, Value>(PName.class);
		for (StringLocated s : lines) {
			assert s.getString().length() > 0;

			final Matcher2 m = p1.matcher(s.getString());
			if (m.find() == false) {
				throw new IllegalStateException();
			}
			final PName key = PName.getFromName(m.group(1));
			final String value = m.group(2);
			// System.err.println("key=" + key + " " + value);
			if (key != null) {
				map.put(key, new ValueImpl(value, counter));
			}
		}

		return new Style(StyleKind.valueOf(kind), name, map);
	}

}
