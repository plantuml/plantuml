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
 * Revision $Revision: 5884 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.Reference;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.StringUtils;

public class CommandReferenceMultilinesOverSeveral extends CommandMultilines<SequenceDiagram> {

	public CommandReferenceMultilinesOverSeveral() {
		super(
				"(?i)^ref(#\\w+)?[%s]+over[%s]+((?:[\\p{L}0-9_.@]+|[%g][^%g]+[%g])(?:[%s]*,[%s]*(?:[\\p{L}0-9_.@]+|[%g][^%g]+[%g]))*)[%s]*(#\\w+)?$");
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^end[%s]?(ref)?$";
	}

	public CommandExecutionResult execute(final SequenceDiagram diagram, List<String> lines) {
		final List<String> line0 = StringUtils.getSplit(getStartingPattern(), StringUtils.trin(lines.get(0)));
		final HtmlColor backColorElement = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(line0.get(0));
		// final HtmlColor backColorGeneral = HtmlColorSet.getInstance().getColorIfValid(line0.get(1));

		final List<String> participants = StringUtils.splitComma(line0.get(1));
		final List<Participant> p = new ArrayList<Participant>();
		for (String s : participants) {
			p.add(diagram.getOrCreateParticipant(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s)));
		}

		Display strings = Display.create(lines.subList(1, lines.size() - 1)).removeEmptyColumns();

		Url u = null;
		if (strings.size() > 0) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			u = urlBuilder.getUrl(strings.get(0).toString());
		}
		if (u != null) {
			strings = strings.subList(1, strings.size());
		}

		final HtmlColor backColorGeneral = null;
		final Reference ref = new Reference(p, u, strings, backColorGeneral, backColorElement);
		diagram.addReference(ref);
		return CommandExecutionResult.ok();
	}

}
