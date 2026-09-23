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

import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereogroup;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.warning.Warning;

public class CommandElseIf3 extends SingleLineCommand2<ActivityDiagram3> {

	public CommandElseIf3() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandElseIf3.class.getName(), RegexLeaf.start(), //
				ColorParser.exp4(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexConcat( //
						new RegexLeaf("\\("), //
						new RegexOptional(new RegexOr(//
								new RegexLeaf("->"), //
								new RegexLeaf(1, "INCOMING_COLOR", CommandLinkElement.STYLE_COLORS_MULTIPLES))), //
						new RegexLeaf(1, "INCOMING", "(.*?)"), //
						new RegexLeaf("\\)"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("else"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("if"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\("), //
				new RegexLeaf(1, "TEST", "(.*?)"), //
				new RegexLeaf("\\)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "(is|equals?)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\("), //
				new RegexLeaf(1, "WHEN", "(.+?)"), //
				new RegexLeaf("\\)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf("then"), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexOptional(new RegexConcat( //
										new RegexLeaf("\\("), //
										new RegexOptional(new RegexOr(//
												new RegexLeaf("->"), //
												new RegexLeaf(1, "WHEN_COLOR",
														CommandLinkElement.STYLE_COLORS_MULTIPLES))), //
										new RegexLeaf("\\)"))) //
						)), //
				new RegexLeaf(";?"), //
				RegexLeaf.spaceZeroOrMore(), //
				Stereogroup.optionalStereogroup(), //
				RegexLeaf.end());
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		final StringBuilder sb = new StringBuilder();

		// 'elseif (test) is (value) then' adds a new conditional branch to the
		// enclosing 'if'; the value labels the branch arrow. Unlike the plain
		// 'elseif (test) then (label)' form, the 'then (...)' group may only
		// carry a color.
		sb.append("Adding an 'else if' branch to the enclosing if");

		final String test = arg.get("TEST", 0);
		if (test.length() > 0)
			sb.append(", testing \"").append(test).append("\" is \"").append(arg.get("WHEN", 0)).append("\"");
		else
			sb.append(", when the value is \"").append(arg.get("WHEN", 0)).append("\"");

		final String whenColor = arg.get("WHEN_COLOR", 0);
		if (whenColor != null)
			sb.append(", 'then' arrow color or style '").append(whenColor).append("'");

		CommandBackward3.appendArrow(sb, arg, "INCOMING", "incoming");

		final Stereogroup stereogroup = Stereogroup.build(arg);
		if (stereogroup.isEmpty() == false)
			sb.append(", stereotyped ").append(arg.get("STEREOGROUP", 0));

		// Mirror the deprecation warning emitted by executeArg: the leading
		// color is parsed but no longer applied.
		if (arg.get("COLOR", 0) != null)
			sb.append(" (deprecated and ignored color syntax: write <<").append(arg.get("COLOR", 0))
					.append(">> at the end of the line)");

		return sb.toString();
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram3 diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {
		if (arg.get("COLOR", 0) != null)
			diagram.addWarning(new Warning("This syntax is deprecated, you must add <<" + arg.get("COLOR", 0)
					+ ">> at the end of the line"));

		final Stereogroup stereogroup = Stereogroup.build(arg);
		final Colors colors = stereogroup.getInnerColors(diagram.getSkinParam().getIHtmlColorSet());
		final HColor color = colors.getColor(ColorType.BACK);

		String test = arg.get("TEST", 0);
		if (test.length() == 0) {
			test = null;
		}

		final LinkRendering incoming = CommandBackward3.getBackRendering(diagram, arg, "INCOMING");
		final LinkRendering when = CommandBackward3.getBackRendering(diagram, arg, "WHEN");

		return diagram.elseIf(incoming, Display.getWithNewlines(diagram.getPragma(), test), when, color);
	}

}
