/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 3835 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.Reference;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandReferenceOverSeveral extends SingleLineCommand2<SequenceDiagram> {

	public CommandReferenceOverSeveral(SequenceDiagram sequenceDiagram) {
		super(sequenceDiagram, getConcat());
	}

	private static RegexConcat getConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("REF", "ref(#\\w+)?\\s+over\\s+"), //
				new RegexLeaf("PARTS", "(([\\p{L}0-9_.]+|\"[^\"]+\")(\\s*,\\s*([\\p{L}0-9_.]+|\"[^\"]+\"))*)"), //
				new RegexLeaf("\\s*:\\s*"), //
				new RegexLeaf("URL", "(?:\\[\\[([^|]*)(?:\\|([^|]*))?\\]\\])?"), //
				new RegexLeaf("TEXT", "(.*)"), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg) {
		final HtmlColor backColorElement = HtmlColor.getColorIfValid(arg.get("REF").get(0));
		// final HtmlColor backColorGeneral = HtmlColor.getColorIfValid(arg.get("REF").get(1));
		final HtmlColor backColorGeneral = null;
		
		final List<String> participants = StringUtils.splitComma(arg.get("PARTS").get(0));
		final String url = arg.get("URL").get(0);
		final String title = arg.get("URL").get(1);
		final String text = arg.get("TEXT").get(0).trim();

		final List<Participant> p = new ArrayList<Participant>();
		for (String s : participants) {
			p.add(getSystem().getOrCreateParticipant(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s)));
		}

		final List<String> strings = StringUtils.getWithNewlines(text);

		Url u = null;
		if (url != null) {
			u = new Url(url, title);
		}

		final Reference ref = new Reference(p, u, strings, backColorGeneral, backColorElement);
		getSystem().addReference(ref);
		return CommandExecutionResult.ok();
	}

}
