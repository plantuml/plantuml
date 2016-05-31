/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 19885 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.GroupingType;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandGrouping extends SingleLineCommand2<SequenceDiagram> {

	public CommandGrouping() {
		super(getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(//
				new RegexLeaf("^"), //
				new RegexLeaf("TYPE", "(opt|alt|loop|par|par2|break|critical|else|end|also|group)"), //
				new RegexLeaf("COLORS", "((?<!else)(?<!also)(?<!end)#\\w+)?(?:[%s]+(#\\w+))?"), //
				new RegexLeaf("COMMENT", "(?:[%s]+(.*?))?"), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram diagram, RegexResult arg) {
		String type = StringUtils.goLowerCase(arg.get("TYPE", 0));
		final HtmlColor backColorElement = diagram.getSkinParam().getIHtmlColorSet()
				.getColorIfValid(arg.get("COLORS", 0));
		final HtmlColor backColorGeneral = diagram.getSkinParam().getIHtmlColorSet()
				.getColorIfValid(arg.get("COLORS", 1));
		String comment = arg.get("COMMENT", 0);
		final GroupingType groupingType = GroupingType.getType(type);
		if ("group".equals(type)) {
			if (StringUtils.isEmpty(comment)) {
				comment = "group";
			} else {
				final Pattern p = Pattern.compile("^(.*?)\\[(.*)\\]$");
				final Matcher m = p.matcher(comment);
				if (m.find()) {
					type = m.group(1);
					comment = m.group(2);
				}
			}
		}
		final boolean result = diagram.grouping(type, comment, groupingType, backColorGeneral, backColorElement);
		if (result == false) {
			return CommandExecutionResult.error("Cannot create group");
		}
		return CommandExecutionResult.ok();
	}
}
