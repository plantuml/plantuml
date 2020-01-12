/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.MessageExoType;

public class CommandExoArrowLeft extends CommandExoArrowAny {

	public CommandExoArrowLeft() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandExoArrowLeft.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("PARALLEL", "(&[%s]*)?"), //
				new RegexLeaf("SHORT", "([?\\[\\]][ox]?)?"), //
				new RegexOr( //
						new RegexConcat( //
								new RegexLeaf("ARROW_BOTHDRESSING", "(<<?|//?|\\\\\\\\?)?"), //
								new RegexLeaf("ARROW_BODYA1", "(-+)"), //
								new RegexLeaf("ARROW_STYLE1", CommandArrow.getColorOrStylePattern()), //
								new RegexLeaf("ARROW_BODYB1", "(-*)"), //
								new RegexLeaf("ARROW_DRESSING1", "(>>?|//?|\\\\\\\\?)")), //
						new RegexConcat( //
								new RegexLeaf("ARROW_DRESSING2", "(<<?|//?|\\\\\\\\?)"), //
								new RegexLeaf("ARROW_BODYB2", "(-*)"), //
								new RegexLeaf("ARROW_STYLE2", CommandArrow.getColorOrStylePattern()), //
								new RegexLeaf("ARROW_BODYA2", "(-+)"))), //
				new RegexLeaf("ARROW_SUPPCIRCLE", "([ox][%s]+)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("PARTICIPANT", "([\\p{L}0-9_.@]+|[%g][^%g]+[%g])"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("ACTIVATION", "(?:([+*!-]+)?)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("LIFECOLOR", "(?:(#\\w+)?)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(":"), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("LABEL", "(.*)") //
						)), RegexLeaf.end());
	}

	@Override
	MessageExoType getMessageExoType(RegexResult arg2) {
		final String start = arg2.get("SHORT", 0);
		final String dressing1 = arg2.get("ARROW_DRESSING1", 0);
		final String dressing2 = arg2.get("ARROW_DRESSING2", 0);
		if (start != null && start.contains("]")) {
			if (dressing1 != null) {
				return MessageExoType.FROM_RIGHT;
			}
			if (dressing2 != null) {
				return MessageExoType.TO_RIGHT;
			}
			throw new IllegalArgumentException();
		}
		if (dressing1 != null) {
			return MessageExoType.FROM_LEFT;
		}
		if (dressing2 != null) {
			return MessageExoType.TO_LEFT;
		}
		throw new IllegalArgumentException();
	}

}
