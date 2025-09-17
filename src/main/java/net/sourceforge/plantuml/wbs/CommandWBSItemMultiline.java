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
package net.sourceforge.plantuml.wbs;

import java.util.List;

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.mindmap.IdeaShape;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.Direction;

public class CommandWBSItemMultiline extends CommandMultilines2<WBSDiagram> {

	private final static Lazy<Pattern2> END = new Lazy<>(() -> Pattern2.cmpile("^(.*);\\s*(\\<\\<(.+)\\>\\>)?$"));

	public CommandWBSItemMultiline() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END);
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandWBSItemMultiline.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "TYPE", "([ \t]*[*+-]+)"), //
				new RegexOr( //
					new RegexConcat( //
						new RegexOptional(new RegexLeaf(1, "BACKCOLOR_1", "\\[(#\\w+)\\]")), //
						new RegexOptional(new RegexLeaf(1, "CODE_1", "\\(([%pLN_]+)\\)"))),
					new RegexConcat( //
						new RegexOptional(new RegexLeaf(1, "CODE_2", "\\(([%pLN_]+)\\)")),
						new RegexOptional(new RegexLeaf(1, "BACKCOLOR_2", "\\[(#\\w+)\\]")))), //
				new RegexOr( //
					new RegexConcat( //
						new RegexLeaf(1, "SHAPE_1", "(_)?"), //
						new RegexLeaf(1, "DIRECTION_1", "([<>])?")), //
					new RegexConcat( //
						new RegexLeaf(1, "DIRECTION_2", "([<>])?")), //
						new RegexLeaf(1, "SHAPE_2", "(_)?")), //
				new RegexLeaf(":"), //
				new RegexLeaf(1, "DATA", "(.*)"), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeNow(WBSDiagram diagram, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());

		final List<String> lineLast = StringUtils.getSplit(getEndPattern(), lines.getLast().getString());
		lines = lines.removeStartingAndEnding(line0.get("DATA", 0), 1);

		final String stereotype = lineLast.get(1);
		if (stereotype != null)
			lines = lines.overrideLastLine(lineLast.get(0));

		final String type = line0.get("TYPE", 0);
		final String stringColor = line0.getLazzy("BACKCOLOR", 0);
		HColor backColor = null;
		if (stringColor != null)
			backColor = diagram.getSkinParam().getIHtmlColorSet().getColor(stringColor);

		final String code = line0.getLazzy("CODE", 0);
		final Direction dir = Direction.getWBSDirection(line0);

		return diagram.addIdea(code, backColor, diagram.getSmartLevel(type), lines.toDisplay(),
				Stereotype.build(stereotype), dir, IdeaShape.fromDesc(line0.getLazzy("SHAPE", 0)));

	}

}
