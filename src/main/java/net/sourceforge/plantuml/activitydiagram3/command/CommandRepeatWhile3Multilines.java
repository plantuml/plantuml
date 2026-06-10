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
package net.sourceforge.plantuml.activitydiagram3.command;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines3;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereogroup;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandRepeatWhile3Multilines extends CommandMultilines3<ActivityDiagram3> {

	private final static IRegex END = new RegexConcat(//
			new RegexLeaf(1, "TEST1", "(.*)"), new RegexLeaf("\\)"), //
			new RegexLeaf(";?"), //
			RegexLeaf.end());

	public CommandRepeatWhile3Multilines() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END);
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandRepeatWhile3Multilines.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("repeat"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("while"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\("), //
				new RegexLeaf(1, "TEST1", "(.*)"), //
				RegexLeaf.end());
	}

	private static final Pattern2 IS_OR_EQUALS = Pattern2.cmpile("\\)[%s]*(is|equals?)[%s]*\\(");

	@Override
	@Explain
	protected String explainNow(BlocLines lines) {
		// Mirror executeNow: the test of 'repeat while (...' spans several
		// lines, up to the closing ')'. An ') is (' separator inside splits
		// the test from the loop-back arrow label. Unlike the single line
		// form, no 'not (...)' exit label nor arrow decoration is available
		// here.
		lines = lines.trim();
		final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.getFirst().getString()));
		final RegexResult lineLast = getEndingPattern().matcher(lines.getLast().getString());
		if (line0 == null || lineLast == null)
			return "Closing the repeat loop";

		final StringBuilder sb = new StringBuilder();
		sb.append("Closing the repeat loop, looping back while a test spanning ").append(lines.size())
				.append(lines.size() == 1 ? " line" : " lines").append(" holds");

		for (StringLocated s : lines)
			if (IS_OR_EQUALS.matcher(s.getString(), 0).find()) {
				sb.append(", with a loop-back arrow label after the 'is' separator");
				break;
			}

		return sb.toString();
	}

	@Override
	protected CommandExecutionResult executeNow(ActivityDiagram3 diagram, BlocLines lines) throws NoSuchColorException {
		lines = lines.trim();
		final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.getFirst().getString()));
		final RegexResult lineLast = getEndingPattern().matcher(lines.getLast().getString());

		final String test = line0.get("TEST1", 0);
		Display testDisplay = Display.getWithNewlines(diagram.getPragma(), test);
		for (StringLocated s : lines.subExtract(1, 1))
			testDisplay = testDisplay.add(s.getString());

		final String trailTest = lineLast.get("TEST1", 0);
		if (StringUtils.isEmpty(trailTest) == false)
			testDisplay = testDisplay.add(trailTest);

		Display yes = Display.NULL;// Display.getWithNewlines("arg.getLazzy(\"WHEN\", 0)");
		final Display out = Display.NULL; // Display.getWithNewlines("arg.getLazzy(\"OUT\", 0)");
		final Rainbow linkColor = Rainbow.none(); // diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("COLOR",
		// 0));
		final Display linkLabel = Display.NULL; // Display.getWithNewlines("arg.get(\"LABEL\", 0)");
		final List<Display> split = testDisplay.splitMultiline(IS_OR_EQUALS);
		if (split.size() == 2) {
			testDisplay = split.get(0);
			yes = split.get(1);
		}

		return diagram.repeatWhile(testDisplay, yes, out, linkLabel, linkColor, Stereogroup.NONE);
	}

}
