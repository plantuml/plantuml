/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * Revision $Revision: 4762 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.command;

import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.StringUtils;

public class CommandIf2Multilines extends CommandMultilines2<ActivityDiagram3> {

	public CommandIf2Multilines() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^(.*?)\\)[%s]*(?:then[%s]*(?:\\((.+?)\\))?)?;?$";
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("COLOR", "(?:(" + HtmlColorUtils.COLOR_REGEXP + "):)?"), //
				new RegexLeaf("if"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("\\("), //
				new RegexLeaf("TEST", "([^)]*)$"));
	}

	@Override
	public CommandExecutionResult executeNow(ActivityDiagram3 diagram, List<String> lines) {
		StringUtils.trim(lines, false);
		final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.get(0)));
		final List<String> lineLast = StringUtils.getSplit(MyPattern.cmpile(getPatternEnd()),
				lines.get(lines.size() - 1));

		final HtmlColor color = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(line0.get("COLOR", 0));

		final String test = line0.get("TEST", 0);
		Display testDisplay = Display.getWithNewlines(test);
		for (int i = 1; i < lines.size() - 1; i++) {
			testDisplay = testDisplay.add(lines.get(i));
		}
		final String trailTest = lineLast.get(0);
		if (StringUtils.isEmpty(trailTest) == false) {
			testDisplay = testDisplay.add(trailTest);
		}

		diagram.startIf(testDisplay, Display.getWithNewlines(lineLast.get(1)), color);
		return CommandExecutionResult.ok();
	}

}
