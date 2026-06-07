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
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantType;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.LineLocation;

public abstract class CommandParticipant extends SingleLineCommand2<SequenceDiagram> {

	public CommandParticipant(IRegex pattern) {
		super(pattern);
	}

	static IRegex getOrderRegex() {
		return new RegexOptional( //
				new RegexConcat( //
						new RegexLeaf("order"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf(1, "ORDER", "(-?\\d{1,7})") //
				));
	}

	static IRegex getRegexType() {
		return new RegexOr(//
				new RegexLeaf(1, "TYPE",
						"(participant|actor|create|boundary|control|entity|queue|database|collections)"), //
				new RegexLeaf(1, "CREATE",
						"create[%s](participant|actor|boundary|control|entity|queue|database|collections)"));
	}

	@Override
	protected String explainArg(LineLocation location, RegexResult arg) {
		final StringBuilder sb = new StringBuilder();

		// A participant that already exists is not recreated: executeArg just
		// moves it to the last position. Reflect that early-return case.
		final String code = arg.get("CODE", 0);

		// 'create participant', 'create actor', etc. set CREATE; a bare 'create'
		// keyword shows up in TYPE. Either way the participant is created and
		// activated with a CREATE life event.
		final String typeCreate = arg.get("CREATE", 0);
		final String typeString = arg.get("TYPE", 0);
		final boolean create = typeCreate != null || (typeString != null && typeString.equalsIgnoreCase("create"));

		final String type;
		if (typeCreate != null)
			type = typeCreate;
		else if (typeString != null && typeString.equalsIgnoreCase("create") == false)
			type = typeString;
		else
			type = "participant";

		sb.append(create ? "Creating " : "Declaring ").append(type.toLowerCase());

		if (code != null)
			sb.append(" '").append(code).append("'");

		final String displayString = arg.get("FULL", 0);
		if (displayString != null)
			sb.append(" displayed as \"").append(displayString).append("\"");

		final String orderString = arg.get("ORDER", 0);
		if (orderString != null)
			sb.append(", order ").append(orderString);

		final String stereo = arg.get("STEREO", 0);
		if (stereo != null)
			sb.append(", stereotype ").append(stereo);

		final String color = arg.get("COLOR", 0);
		if (color != null)
			sb.append(", background color ").append(color);

		if (arg.get(UrlBuilder.URL_KEY, 0) != null)
			sb.append(", with a URL link");

		if (create)
			sb.append(" (created on the spot, with a CREATE life event)");

		return sb.toString();
	}

	@Override
	final protected CommandExecutionResult executeArg(SequenceDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {
		final String code = arg.get("CODE", 0);
		if (diagram.participantsContainsKey(code)) {
			diagram.putParticipantInLast(code);
			return CommandExecutionResult.ok();
		}

		Display strings = Display.NULL;
		if (arg.get("FULL", 0) != null)
			strings = Display.getWithNewlines(diagram.getPragma(), arg.get("FULL", 0));
		// strings = Display.create(arg.get("FULL", 0));

		final String typeString1 = arg.get("TYPE", 0);
		final String typeCreate1 = arg.get("CREATE", 0);
		final ParticipantType type;
		final boolean create;
		if (typeCreate1 != null) {
			type = ParticipantType.valueOf(StringUtils.goUpperCase(typeCreate1));
			create = true;
		} else if (typeString1.equalsIgnoreCase("CREATE")) {
			type = ParticipantType.PARTICIPANT;
			create = true;
		} else {
			type = ParticipantType.valueOf(StringUtils.goUpperCase(typeString1));
			create = false;
		}
		final String orderString = arg.get("ORDER", 0);
		final int order = orderString == null ? 0 : Integer.parseInt(orderString);
		final Participant participant = diagram.createNewParticipant(location, type, code, strings, order);

		final String stereotype = arg.get("STEREO", 0);

		if (stereotype != null) {
			final ISkinParam skinParam = diagram.getSkinParam();
			final boolean stereotypePositionTop = skinParam.stereotypePositionTop();
			final UFont font = skinParam.getFont(null, false, FontParam.CIRCLED_CHARACTER);
			participant.setStereotype(Stereotype.build(stereotype, skinParam.getCircledCharacterRadius(), font,
					diagram.getSkinParam().getIHtmlColorSet()), stereotypePositionTop);
		}
		final String s = arg.get("COLOR", 0);
		participant.setSpecificColorTOBEREMOVED(ColorType.BACK,
				s == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s));

		final String urlString = arg.get(UrlBuilder.URL_KEY, 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			participant.setUrl(url);
		}

		if (create) {
			final String error = diagram.activate(participant, LifeEventType.CREATE, null);
			if (error != null)
				return CommandExecutionResult.error(error);

		}

		return CommandExecutionResult.ok();
	}

}
