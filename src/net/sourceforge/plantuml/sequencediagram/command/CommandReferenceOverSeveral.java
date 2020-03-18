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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.Reference;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class CommandReferenceOverSeveral extends SingleLineCommand2<SequenceDiagram> {

	public CommandReferenceOverSeveral() {
		super(getConcat());
	}

	private static RegexConcat getConcat() {
		return RegexConcat.build(CommandReferenceOverSeveral.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("ref"), //
				new RegexLeaf("REF", "(#\\w+)?"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("over"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("PARTS",
						"(([\\p{L}0-9_.@]+|[%g][^%g]+[%g])([%s]*,[%s]*([\\p{L}0-9_.@]+|[%g][^%g]+[%g]))*)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(":"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf("URL", "\\[\\[([^|]*)(?:\\|([^|]*))?\\]\\]")), //
				new RegexLeaf("TEXT", "(.*)"), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram diagram, LineLocation location, RegexResult arg) {
		final HColor backColorElement = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("REF", 0));
		// final HtmlColor backColorGeneral = HtmlColorSetSimple.instance().getColorIfValid(arg.get("REF").get(1));

		final List<String> participants = StringUtils.splitComma(arg.get("PARTS", 0));
		final String url = arg.get("URL", 0);
		final String title = arg.get("URL", 1);
		final String text = StringUtils.trin(arg.get("TEXT", 0));

		final List<Participant> p = new ArrayList<Participant>();
		for (String s : participants) {
			p.add(diagram.getOrCreateParticipant(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s)));
		}

		final Display strings = Display.getWithNewlines(text);

		Url u = null;
		if (url != null) {
			u = new Url(url, title);
		}

		final HColor backColorGeneral = null;
		final Reference ref = new Reference(p, u, strings, backColorGeneral, backColorElement, diagram.getSkinParam()
				.getCurrentStyleBuilder());
		diagram.addReference(ref);
		return CommandExecutionResult.ok();
	}

}
