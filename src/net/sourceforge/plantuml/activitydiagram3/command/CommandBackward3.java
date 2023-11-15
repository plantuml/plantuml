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
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandBackward3 extends SingleLineCommand2<ActivityDiagram3> {

	public CommandBackward3() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandBackward3.class.getName(), RegexLeaf.start(), //
				new RegexOptional(new RegexConcat( //
						new RegexLeaf("\\("), //
						new RegexOptional(new RegexOr(//
								new RegexLeaf("->"), //
								new RegexLeaf("INCOMING_COLOR", CommandLinkElement.STYLE_COLORS_MULTIPLES))), //
						new RegexLeaf("INCOMING", "(.*?)"), //
						new RegexLeaf("\\)"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("backward"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(":"), //
				new RegexLeaf("LABEL", "(.*?)"), //
				new RegexLeaf("STYLE", CommandActivity3.endingGroup()), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexConcat( //
						new RegexLeaf("\\("), //
						new RegexOptional(new RegexOr(//
								new RegexLeaf("->"), //
								new RegexLeaf("OUTCOMING_COLOR", CommandLinkElement.STYLE_COLORS_MULTIPLES))), //
						new RegexLeaf("OUTCOMING", "(.*?)"), //
						new RegexLeaf("\\)"))), //
				RegexLeaf.spaceZeroOrMore(), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram3 diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final BoxStyle boxStyle;
		final String styleString = arg.get("STYLE", 0);

		if (styleString == null)
			boxStyle = BoxStyle.PLAIN;
		else
			boxStyle = BoxStyle.fromString(styleString);

		final String stereo = arg.get("STYLE", 1);

		Stereotype stereotype = null;
		if (stereo != null)
			stereotype = Stereotype.build(stereo);

		final Display label = Display.getWithNewlines(arg.get("LABEL", 0));

		final LinkRendering in = getBackRendering(diagram, arg, "INCOMING");
		final LinkRendering out = getBackRendering(diagram, arg, "OUTCOMING");

		return diagram.backward(label, boxStyle, in, out, stereotype);
	}

	static public LinkRendering getBackRendering(ActivityDiagram3 diagram, RegexResult arg, String name)
			throws NoSuchColorException {
		final LinkRendering in;
		final Rainbow incomingColor = getRainbow(name + "_COLOR", diagram, arg);
		if (incomingColor == null)
			in = LinkRendering.none();
		else
			in = LinkRendering.create(incomingColor);
		final String label = arg.get(name, 0);
		return in.withDisplay(Display.getWithNewlines(label));
	}

	static private Rainbow getRainbow(String key, ActivityDiagram3 diagram, RegexResult arg)
			throws NoSuchColorException {
		final String colorString = arg.get(key, 0);
		if (colorString == null) {
			return null;
		}
		return Rainbow.build(diagram.getSkinParam(), colorString, diagram.getSkinParam().colorArrowSeparationSpace());
	}

}
