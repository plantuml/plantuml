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

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantType;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandParticipantMultilines extends CommandMultilines2<SequenceDiagram> {

	private final static Lazy<Pattern2> END = new Lazy<>(() -> Pattern2.cmpile("^([^\\[\\]]*)\\]$"));

	public CommandParticipantMultilines() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END);
	}

	private static RegexConcat getRegexConcat() {
		return RegexConcat.build(CommandParticipantMultilines.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "TYPE", "(participant)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "CODE", "([%pLN_.@]+)"), //
				StereotypePattern.optional("STEREO"), //
				CommandParticipant.getOrderRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				ColorParser.exp1(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\["), //
				RegexLeaf.end());
	}

	@Override
	@Explain
	protected String explainNow(BlocLines lines) {
		// Mirror executeNow: the first line carries the participant
		// declaration, the lines up to the closing ']' are its multiline
		// display. If the participant already exists, executeNow just moves it
		// to the last position.
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		if (line0 == null)
			return "Declaring a participant";

		final StringBuilder sb = new StringBuilder();
		sb.append("Declaring participant '").append(line0.get("CODE", 0)).append("'");

		final String order = line0.get("ORDER", 0);
		if (order != null)
			sb.append(", order ").append(order);

		final String stereo = line0.get("STEREO", 0);
		if (stereo != null)
			sb.append(", stereotype ").append(stereo);

		// The color is parsed by the starting pattern but never applied by
		// executeNow.
		final String color = line0.get("COLOR", 0);
		if (color != null)
			sb.append(", background color ").append(color).append(" (currently ignored)");

		if (line0.get(UrlBuilder.URL_KEY, 0) != null)
			sb.append(", with a URL link");

		// Body: the display lines between the opening '[' and the closing ']'.
		final int bodyCount = lines.size() > 2 ? lines.size() - 2 : 0;
		if (bodyCount > 0)
			sb.append(", displayed as ").append(bodyCount).append(bodyCount == 1 ? " line" : " lines")
					.append(" of text");

		return sb.toString();
	}

	@Override
	protected CommandExecutionResult executeNow(SequenceDiagram diagram, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {

		final RegexResult arg = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());

		final String code = arg.get("CODE", 0);
		if (diagram.participantsContainsKey(code)) {
			diagram.putParticipantInLast(code);
			return CommandExecutionResult.ok();
		}

		lines = lines.subExtract(1, 1);
		lines = lines.removeEmptyColumns();
		final Display strings = lines.toDisplay();

		final ParticipantType type = ParticipantType.PARTICIPANT;
		final boolean create = false;
		final String orderString = arg.get("ORDER", 0);
		final int order = orderString == null ? 0 : Integer.parseInt(orderString);

		final Participant participant = diagram.createNewParticipant(lines.getLocation(), type, code, strings, order);

		final String stereotype = arg.get("STEREO", 0);

		if (stereotype != null) {
			final ISkinParam skinParam = diagram.getSkinParam();
			final boolean stereotypePositionTop = skinParam.stereotypePositionTop();
			final UFont font = skinParam.getFont(null, false, FontParam.CIRCLED_CHARACTER);
			participant.setStereotype(Stereotype.build(stereotype, skinParam.getCircledCharacterRadius(), font,
					diagram.getSkinParam().getIHtmlColorSet()), stereotypePositionTop);
		}

		final String urlString = arg.get(UrlBuilder.URL_KEY, 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			participant.setUrl(url);
		}

		return CommandExecutionResult.ok();
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

}
