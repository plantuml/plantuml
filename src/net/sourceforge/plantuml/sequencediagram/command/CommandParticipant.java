/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 6109 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantType;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.ugraphic.UFont;

public abstract class CommandParticipant extends SingleLineCommand2<SequenceDiagram> {

	public CommandParticipant(RegexConcat pattern) {
		super(pattern);
	}

	static IRegex getRegexType() {
		return new RegexOr(new RegexLeaf("TYPE", "(participant|actor|create|boundary|control|entity|database)"), //
				new RegexLeaf("CREATE", "create (participant|actor|boundary|control|entity|database)"));
	}

	@Override
	final protected CommandExecutionResult executeArg(SequenceDiagram system, RegexResult arg2) {
		final String code = arg2.get("CODE", 0);
		if (system.participants().containsKey(code)) {
			system.putParticipantInLast(code);
			return CommandExecutionResult.ok();
		}

		Display strings = null;
		if (arg2.get("FULL", 0) != null) {
			strings = Display.getWithNewlines(arg2.get("FULL", 0));
		}

		final String typeString1 = arg2.get("TYPE", 0);
		final String typeCreate1 = arg2.get("CREATE", 0);
		final ParticipantType type;
		final boolean create;
		if (typeCreate1 != null) {
			type = ParticipantType.valueOf(typeCreate1.toUpperCase());
			create = true;
		} else if (typeString1.equalsIgnoreCase("CREATE")) {
			type = ParticipantType.PARTICIPANT;
			create = true;
		} else {
			type = ParticipantType.valueOf(typeString1.toUpperCase());
			create = false;
		}
		final Participant participant = system.createNewParticipant(type, code, strings);

		final String stereotype = arg2.get("STEREO", 0);

		if (stereotype != null) {
			final ISkinParam skinParam = system.getSkinParam();
			final boolean stereotypePositionTop = skinParam.stereotypePositionTop();
			final UFont font = skinParam.getFont(FontParam.CIRCLED_CHARACTER, null);
			participant.setStereotype(new Stereotype(stereotype, skinParam.getCircledCharacterRadius(), font),
					stereotypePositionTop);
		}
		participant.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(arg2.get("COLOR", 0)));

		final String urlString = arg2.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(system.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			participant.setUrl(url);
		}

		if (create) {
			final String error = system.activate(participant, LifeEventType.CREATE, null);
			if (error != null) {
				return CommandExecutionResult.error(error);
			}

		}

		return CommandExecutionResult.ok();
	}

}
