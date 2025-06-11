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
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandActivate extends SingleLineCommand2<SequenceDiagram> {

	public CommandActivate() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandActivate.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "TYPE", "(activate|deactivate|destroy|create)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "WHO", "([%pLN_.@]+|[%g][^%g]+[%g])"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "BACK", "(#\\w+)?"), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "LINE", "(#\\w+)") //
						)), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {
		final LifeEventType type = LifeEventType.valueOf(StringUtils.goUpperCase(arg.get("TYPE", 0)));
		final Participant p = diagram.getOrCreateParticipant(location,
				StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("WHO", 0)));
		final String back = arg.get("BACK", 0);
		final HColor backColor = back == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(back);
		final String line = arg.get("LINE", 0);
		final HColor lineColor = line == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(line);
		final String error = diagram.activate(p, type, backColor, lineColor);
		if (error == null) {
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error(error);
	}

}
