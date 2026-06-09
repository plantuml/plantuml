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
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.GroupingType;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandGrouping extends SingleLineCommand2<SequenceDiagram> {

	public CommandGrouping() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandGrouping.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "PARALLEL", "(&[%s]*)?"), //
				new RegexLeaf(1, "TYPE", "(opt|alt|loop|par|par2|break|critical|else|end|also|group|partition)"), //
				new RegexLeaf(2, "COLORS", "((?<!else)(?<!also)(?<!end)#\\w+)?(?:[%s]+(#\\w+))?"), //
				new RegexOptional(//
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "COMMENT", "(.*?)") //
						)), RegexLeaf.end());
	}

	static private final Pattern TRAILING_BRACKET_CONTENT_PATTERN = Pattern
			.compile("^(.*\\[\\[.*\\]\\].*?|.*?)\\[(.*)\\]$");

	@Override
	protected String explainArg(LineLocation location, RegexResult arg) {
		final StringBuilder sb = new StringBuilder();

		// Mirror the massaging done by executeArg: the 'group Title [text]'
		// syntax turns the title into a custom header and the bracket content
		// into the label (see TRAILING_BRACKET_CONTENT_PATTERN).
		final String keyword = StringUtils.goLowerCase(arg.get("TYPE", 0));
		String header = keyword;
		String comment = arg.get("COMMENT", 0);
		if ("group".equals(keyword) && StringUtils.isEmpty(comment) == false) {
			final Matcher m = TRAILING_BRACKET_CONTENT_PATTERN.matcher(comment);
			if (m.find()) {
				header = m.group(1);
				comment = m.group(2);
			}
		}

		// 'end' closes the current group, 'else' and 'also' add a branch to it,
		// every other keyword opens a new group.
		if ("end".equals(keyword))
			sb.append("Closing the current group");
		else if ("else".equals(keyword) || "also".equals(keyword))
			sb.append("Adding an '").append(keyword).append("' branch to the current group");
		else
			sb.append("Starting a '").append(keyword).append("' group");

		if (header.equals(keyword) == false)
			sb.append(" with header \"").append(header).append("\"");

		if (comment != null && comment.isEmpty() == false)
			sb.append(", labelled \"").append(comment).append("\"");

		// First color is the header background, second one (space separated)
		// is the body background of the whole group.
		final String colorElement = arg.get("COLORS", 0);
		if (colorElement != null)
			sb.append(", header color ").append(colorElement);

		final String colorGeneral = arg.get("COLORS", 1);
		if (colorGeneral != null)
			sb.append(", background color ").append(colorGeneral);

		if (arg.get("PARALLEL", 0) != null)
			sb.append(", parallel");

		return sb.toString();
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {
		final HColorSet colorSet = diagram.getSkinParam().getIHtmlColorSet();

		final HColor backColorElement = getColor(arg.get("COLORS", 0), colorSet);
		final HColor backColorGeneral = getColor(arg.get("COLORS", 1), colorSet);

		String type = StringUtils.goLowerCase(arg.get("TYPE", 0));
		String comment = arg.get("COMMENT", 0);
		final GroupingType groupingType = GroupingType.getType(type);
		if ("group".equals(type))
			if (StringUtils.isEmpty(comment)) {
				comment = "group";
			} else {
				final Matcher m = TRAILING_BRACKET_CONTENT_PATTERN.matcher(comment);
				if (m.find()) {
					type = m.group(1);
					comment = m.group(2);
				}
			}

		final boolean parallel = arg.get("PARALLEL", 0) != null;
		final boolean result = diagram.grouping(type, comment, groupingType, backColorGeneral, backColorElement,
				parallel);

		if (result == false)
			return CommandExecutionResult.error("Cannot create group");

		return CommandExecutionResult.ok();
	}

	private HColor getColor(String color, HColorSet colorSet) throws NoSuchColorException {
		if (color == null)
			return null;
		return colorSet.getColor(color);
	}

}
