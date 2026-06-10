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
import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
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
import net.sourceforge.plantuml.stereo.Stereogroup;
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
								new RegexLeaf(1, "INCOMING_COLOR", CommandLinkElement.STYLE_COLORS_MULTIPLES))), //
						new RegexLeaf(1, "INCOMING", "(.*?)"), //
						new RegexLeaf("\\)"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("backward"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(":"), //
				new RegexLeaf(1, "LABEL", "(.*?)"), //
				new RegexLeaf(";"), //
				RegexLeaf.spaceZeroOrMore(), //
				Stereogroup.optionalStereogroup(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexConcat( //
						new RegexLeaf("\\("), //
						new RegexOptional(new RegexOr(//
								new RegexLeaf("->"), //
								new RegexLeaf(1, "OUTCOMING_COLOR", CommandLinkElement.STYLE_COLORS_MULTIPLES))), //
						new RegexLeaf(1, "OUTCOMING", "(.*?)"), //
						new RegexLeaf("\\)"))), //
				RegexLeaf.spaceZeroOrMore(), //
				RegexLeaf.end());
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		final StringBuilder sb = new StringBuilder();

		// 'backward :label;' defines the activity drawn on the return edge of
		// the enclosing repeat loop. The optional '(...)' groups decorate the
		// arrows entering (before the keyword) and leaving (after the
		// stereotypes) this activity.
		sb.append("Defining the backward activity \"").append(arg.get("LABEL", 0))
				.append("\" on the return edge of the repeat loop");

		final Stereogroup stereogroup = Stereogroup.build(arg);
		if (stereogroup.isEmpty() == false) {
			sb.append(", stereotyped ").append(arg.get("STEREOGROUP", 0));
			final BoxStyle style = stereogroup.getBoxStyle();
			if (style != BoxStyle.PLAIN)
				sb.append(" (box style: ").append(style.name().toLowerCase()).append(")");
		}

		appendArrow(sb, arg, "INCOMING", "incoming");
		appendArrow(sb, arg, "OUTCOMING", "outgoing");

		return sb.toString();
	}

	static void appendArrow(StringBuilder sb, RegexResult arg, String key, String word) {
		final String label = arg.get(key, 0);
		final String color = arg.get(key + "_COLOR", 0);
		final boolean hasLabel = label != null && label.isEmpty() == false;
		if (hasLabel == false && color == null)
			return;

		sb.append(", ").append(word).append(" arrow");
		if (hasLabel)
			sb.append(" labelled \"").append(label).append("\"");
		if (color != null)
			sb.append(" with color or style '").append(color).append("'");
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram3 diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {
		final Stereogroup stereogroup = Stereogroup.build(arg);
		final BoxStyle boxStyle = stereogroup.getBoxStyle();
		final Stereotype stereotype = stereogroup.buildStereotype();

		final Display label = Display.getWithNewlines(diagram.getPragma(), arg.get("LABEL", 0));

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
		return in.withDisplay(Display.getWithNewlines(diagram.getPragma(), label));
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
