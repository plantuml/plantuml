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

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.MessageExoType;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandExoArrowLeft extends CommandExoArrowAny {

	public CommandExoArrowLeft() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandExoArrowLeft.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "PARALLEL", "(&[%s]*)?"), //
				new RegexLeaf(2, "ANCHOR", CommandArrow.ANCHOR), //
				new RegexLeaf(1, ARROW_SUPPCIRCLE2, "([?\\[\\]][ox]?)?"), //
				new RegexOr( //
						new RegexConcat( //
								new RegexLeaf(1, "ARROW_BOTHDRESSING", "(<<?|//?|\\\\\\\\?)?"), //
								new RegexLeaf(1, "ARROW_BODYA1", "(-+)"), //
								new RegexLeaf(1, "ARROW_STYLE1", CommandArrow.getColorOrStylePattern()), //
								new RegexLeaf(1, "ARROW_BODYB1", "(-*)"), //
								new RegexLeaf(1, "ARROW_DRESSING1", "(>>?|//?|\\\\\\\\?)")), //
						new RegexConcat( //
								new RegexLeaf(1, "ARROW_DRESSING2", "(<<?|//?|\\\\\\\\?)"), //
								new RegexLeaf(1, "ARROW_BODYB2", "(-*)"), //
								new RegexLeaf(1, "ARROW_STYLE2", CommandArrow.getColorOrStylePattern()), //
								new RegexLeaf(1, "ARROW_BODYA2", "(-+)"))), //
				new RegexLeaf(1, ARROW_SUPPCIRCLE1, "([ox][%s]+)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "PARTICIPANT", "([%pLN_.@]+|[%g][^%g]+[%g])"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "ACTIVATION", "(?:([+*!-]+)?)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "LIFECOLOR", "(?:(#\\w+)?)"), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(":"), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf(1, "LABEL", "(.*)") //
						)), RegexLeaf.end());
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		final StringBuilder sb = new StringBuilder();

		// Exogenous message: one endpoint is a participant, the other is the
		// diagram border. The side and the direction are deduced from the
		// leading symbol ('[' or ']') and from which dressing matched, exactly
		// as in getMessageExoType().
		final String participant = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("PARTICIPANT", 0));

		final MessageExoType type = getMessageExoType(arg);
		switch (type) {
		case FROM_LEFT:
			sb.append("Incoming message from the left border to '").append(participant).append("'");
			break;
		case TO_LEFT:
			sb.append("Outgoing message from '").append(participant).append("' to the left border");
			break;
		case FROM_RIGHT:
			sb.append("Incoming message from the right border to '").append(participant).append("'");
			break;
		case TO_RIGHT:
			sb.append("Outgoing message from '").append(participant).append("' to the right border");
			break;
		default:
			sb.append("Exogenous message with '").append(participant).append("'");
			break;
		}

		// A '?' as leading symbol means a short arrow (see isShortArrow()).
		final String start = arg.get(ARROW_SUPPCIRCLE2, 0);
		if (start != null && start.contains("?"))
			sb.append(" (short arrow)");

		// Body style: a '--' in the body means a dotted arrow.
		final String body = arg.getLazzy("ARROW_BODYA", 0) + arg.getLazzy("ARROW_BODYB", 0);
		if (body.contains("--"))
			sb.append(", dotted");

		// A doubled dressing (>>, //, \\) means an async head.
		final String dressing = arg.getLazzy("ARROW_DRESSING", 0);
		if (dressing != null && dressing.length() == 2)
			sb.append(", async");

		// A leading '<' before the body means an arrow in both directions.
		if (arg.get("ARROW_BOTHDRESSING", 0) != null)
			sb.append(", in both directions");

		// Circle ('o') and cross ('x') decorations, on the border side
		// (ARROW_SUPPCIRCLE2) or on the participant side (ARROW_SUPPCIRCLE1).
		if (start != null && start.contains("o"))
			sb.append(", with a circle decoration on the border side");
		if (start != null && start.contains("x"))
			sb.append(", with a cross head on the border side");

		final String suppCircle1 = arg.get(ARROW_SUPPCIRCLE1, 0);
		if (suppCircle1 != null && suppCircle1.contains("o"))
			sb.append(", with a circle decoration on the participant side");
		if (suppCircle1 != null && suppCircle1.contains("x"))
			sb.append(", with a cross head on the participant side");

		// Activation specifier (+, -, *, !) drives the participant's life line.
		final String activation = arg.get("ACTIVATION", 0);
		if (activation != null && activation.isEmpty() == false)
			sb.append(", activation '").append(activation).append("'");

		final String lifeColor = arg.get("LIFECOLOR", 0);
		if (lifeColor != null && lifeColor.isEmpty() == false)
			sb.append(", life line color ").append(lifeColor);

		if (arg.get(UrlBuilder.URL_KEY, 0) != null)
			sb.append(", with a URL link");

		if (arg.get("PARALLEL", 0) != null)
			sb.append(", parallel");

		if (arg.get("ANCHOR", 1) != null)
			sb.append(", anchor '").append(arg.get("ANCHOR", 1)).append("'");

		return sb.toString();
	}

	@Override
	MessageExoType getMessageExoType(RegexResult arg2) {
		final String start = arg2.get(ARROW_SUPPCIRCLE2, 0);
		final String dressing1 = arg2.get("ARROW_DRESSING1", 0);
		final String dressing2 = arg2.get("ARROW_DRESSING2", 0);
		if (start != null && start.contains("]")) {
			if (dressing1 != null)
				return MessageExoType.FROM_RIGHT;

			if (dressing2 != null)
				return MessageExoType.TO_RIGHT;

			throw new IllegalArgumentException();
		}
		if (dressing1 != null)
			return MessageExoType.FROM_LEFT;

		if (dressing2 != null)
			return MessageExoType.TO_LEFT;

		throw new IllegalArgumentException();
	}

}
