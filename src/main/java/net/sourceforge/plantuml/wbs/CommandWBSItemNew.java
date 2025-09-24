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

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.mindmap.IdeaShape;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.Constant;
import net.sourceforge.plantuml.utils.Direction;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandWBSItemNew extends SingleLineCommand2<WBSDiagram> {

	public CommandWBSItemNew(int mode) {
		super(false, getRegexConcat(mode));
	}

	static IRegex getRegexConcat(int mode) {
		if (mode == 0)
			return RegexConcat.build(CommandWBSItemNew.class.getName() + mode, RegexLeaf.start(), //
					new RegexLeaf(1, Constant.WBS_TYPE, "([ \t]*[*+-]+)"), //
					new RegexOr( //
						new RegexConcat( //
							new RegexLeaf(1, "SHAPE_1", "(_)?"), //
							new RegexLeaf(1, Constant.WBS_DIRECTION + "_1", "([<>])?")), //
						new RegexConcat( //
							new RegexLeaf(1, Constant.WBS_DIRECTION + "_2", "([<>])?")), //
							new RegexLeaf(1, "SHAPE_2", "(_)?")), //
					new RegexOr( //
						new RegexConcat( //
							new RegexOptional(new RegexLeaf(1, "BACKCOLOR_1", "\\[(#\\w+)\\]")), //
							new RegexOptional(new RegexLeaf(1, "CODE_1", "\\(([%pLN_]+)\\)"))),
						new RegexConcat( //
							new RegexOptional(new RegexLeaf(1, "CODE_2", "\\(([%pLN_]+)\\)")),
							new RegexOptional(new RegexLeaf(1, "BACKCOLOR_2", "\\[(#\\w+)\\]")))), //
					RegexLeaf.spaceOneOrMore(), //
					new RegexLeaf(1, "LABEL", "([^%s].*)"), //
					RegexLeaf.end());

		return RegexConcat.build(CommandWBSItemNew.class.getName() + mode, RegexLeaf.start(), //
				new RegexLeaf(1, Constant.WBS_TYPE, "([ \t]*[*+-]+)"), //
				new RegexOr( //
					new RegexConcat( //
						new RegexLeaf(1, "SHAPE_1", "(_)?"), //
						new RegexLeaf(1, Constant.WBS_DIRECTION + "_1", "([<>])?")), //
					new RegexConcat( //
						new RegexLeaf(1, Constant.WBS_DIRECTION + "_2", "([<>])?")), //
						new RegexLeaf(1, "SHAPE_2", "(_)?")), //
				new RegexOptional(new RegexLeaf(1, "BACKCOLOR", "\\[(#\\w+)\\]")), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "LABEL", "[%g](.*)[%g]"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("as"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "CODE", "([%pLN_]+)"), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(WBSDiagram diagram, LineLocation location, RegexResult arg, ParserPass currentPass)
			throws NoSuchColorException {
		final String type = arg.get(Constant.WBS_TYPE, 0);
		final String label = arg.get("LABEL", 0);
		final String code = arg.getLazzy("CODE", 0);
		final String stringColor = arg.getLazzy("BACKCOLOR", 0);
		HColor backColor = null;
		if (stringColor != null)
			backColor = diagram.getSkinParam().getIHtmlColorSet().getColor(stringColor);

		final Direction dir = Direction.getWBSDirection(arg);

		return diagram.addIdea(code, backColor, diagram.getSmartLevel(type), label, dir,
				IdeaShape.fromDesc(arg.getLazzy("SHAPE", 0)));
	}

}
