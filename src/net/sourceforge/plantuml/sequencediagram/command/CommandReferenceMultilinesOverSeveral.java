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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.Reference;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandReferenceMultilinesOverSeveral extends CommandMultilines<SequenceDiagram> {

	public CommandReferenceMultilinesOverSeveral() {
		super(getConcat().getPattern());
	}

	private static RegexConcat getConcat() {
		return RegexConcat.build(CommandReferenceMultilinesOverSeveral.class.getName(), //
				RegexLeaf.start(), //
				new RegexLeaf("ref"), //
				new RegexLeaf("REF", "(#\\w+)?"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("over"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("PARTS", "((?:[%pLN_.@]+|[%g][^%g]+[%g])(?:[%s]*,[%s]*(?:[%pLN_.@]+|[%g][^%g]+[%g]))*)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf("URL", "(\\[\\[.*?\\]\\])")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("UNUSED", "(#\\w+)?"), //
				RegexLeaf.end());
	}

	@Override
	public String getPatternEnd() {
		return "^end[%s]?(ref)?$";
	}

	public CommandExecutionResult execute(final SequenceDiagram diagram, BlocLines lines) throws NoSuchColorException {
		final String firstLine = lines.getFirst().getTrimmed().getString();
		final RegexResult arg = getConcat().matcher(firstLine);
		if (arg == null)
			return CommandExecutionResult.error("Cannot parse line " + firstLine);

		final String s1 = arg.get("REF", 0);
		final HColor backColorElement = s1 == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s1);
		// final HtmlColor backColorGeneral =
		// HtmlColorSetSimple.instance().getColorIfValid(line0.get(1));

		final List<String> participants = StringUtils.splitComma(arg.get("PARTS", 0));
		final List<Participant> p = new ArrayList<>();
		for (String s : participants)
			p.add(diagram.getOrCreateParticipant(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s)));

		lines = lines.subExtract(1, 1);
		lines = lines.removeEmptyColumns();
		final Display strings = lines.toDisplay();

		final String url = arg.get("URL", 0);
		final UrlBuilder b = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
		Url u = null;
		if (url != null)
			u = b.getUrl(url);

		final HColor backColorGeneral = null;
		final Reference ref = new Reference(p, u, strings, backColorGeneral, backColorElement,
				diagram.getSkinParam().getCurrentStyleBuilder());
		diagram.addReference(ref);
		return CommandExecutionResult.ok();
	}

}