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
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.activitydiagram3.command;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;

public class CommandPartition3 extends SingleLineCommand2<ActivityDiagram3> {

	public CommandPartition3() {
		super(getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("partition"), //
				new RegexLeaf("[%s]+"), //
				new RegexOptional(//
						new RegexConcat( //
								color().getRegex(),//
								new RegexLeaf("[%s]+"))), //
				new RegexLeaf("TITLECOLOR", "(?:(#\\w+)[%s]+)?"), //
				new RegexLeaf("NAME", "([%g][^%g]+[%g]|\\S+)"), //
				new RegexLeaf("[%s]*\\{?$"));
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram3 diagram, RegexResult arg) {
		final String partitionTitle = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("NAME", 0));
		final HtmlColor titleColor = diagram.getSkinParam().getIHtmlColorSet()
				.getColorIfValid(arg.get("TITLECOLOR", 0));

		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());

		final HtmlColor backColorInSkinparam = diagram.getSkinParam().getHtmlColor(ColorParam.partitionBackground,
				null, false);
		final HtmlColor backColor;
		if (backColorInSkinparam == null) {
			backColor = colors.getColor(ColorType.BACK);
		} else {
			backColor = backColorInSkinparam;

		}

		HtmlColor borderColor = diagram.getSkinParam().getHtmlColor(ColorParam.partitionBorder, null, false);
		if (borderColor == null) {
			borderColor = HtmlColorUtils.BLACK;
		}

		diagram.startGroup(Display.getWithNewlines(partitionTitle), backColor, titleColor, borderColor);

		return CommandExecutionResult.ok();
	}
}
