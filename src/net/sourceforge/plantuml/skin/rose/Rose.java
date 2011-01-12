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
 * Revision $Revision: 5943 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;

public class Rose implements Skin {

	private final Map<ColorParam, HtmlColor> defaultsColor = new EnumMap<ColorParam, HtmlColor>(ColorParam.class);

	public Rose() {
		defaultsColor.put(ColorParam.background, new HtmlColor("white"));

		defaultsColor.put(ColorParam.sequenceArrow, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.usecaseArrow, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.classArrow, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.objectArrow, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.activityArrow, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.componentArrow, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.stateArrow, new HtmlColor("#A80036"));

		defaultsColor.put(ColorParam.sequenceLifeLineBackground, new HtmlColor("white"));
		defaultsColor.put(ColorParam.sequenceLifeLineBorder, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.sequenceGroupBackground, new HtmlColor("#EEEEEE"));
		defaultsColor.put(ColorParam.sequenceDividerBackground, new HtmlColor("#EEEEEE"));
		defaultsColor.put(ColorParam.sequenceEngloberLine, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.sequenceEngloberBackground, new HtmlColor("#DDDDDD"));

		defaultsColor.put(ColorParam.noteBackground, new HtmlColor("#FBFB77"));
		defaultsColor.put(ColorParam.noteBorder, new HtmlColor("#A80036"));

		defaultsColor.put(ColorParam.activityBackground, new HtmlColor("#FEFECE"));
		defaultsColor.put(ColorParam.activityBorder, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.activityStart, new HtmlColor("black"));
		defaultsColor.put(ColorParam.activityEnd, new HtmlColor("black"));
		defaultsColor.put(ColorParam.activityBar, new HtmlColor("black"));

		defaultsColor.put(ColorParam.stateBackground, new HtmlColor("#FEFECE"));
		defaultsColor.put(ColorParam.stateBorder, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.stateStart, new HtmlColor("black"));
		defaultsColor.put(ColorParam.stateEnd, new HtmlColor("black"));

		defaultsColor.put(ColorParam.usecaseBackground, new HtmlColor("#FEFECE"));
		defaultsColor.put(ColorParam.usecaseBorder, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.componentBackground, new HtmlColor("#FEFECE"));
		defaultsColor.put(ColorParam.componentBorder, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.componentInterfaceBackground, new HtmlColor("#FEFECE"));
		defaultsColor.put(ColorParam.componentInterfaceBorder, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.usecaseActorBackground, new HtmlColor("#FEFECE"));
		defaultsColor.put(ColorParam.usecaseActorBorder, new HtmlColor("#A80036"));

		defaultsColor.put(ColorParam.sequenceActorBackground, new HtmlColor("#FEFECE"));
		defaultsColor.put(ColorParam.sequenceActorBorder, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.sequenceParticipantBackground, new HtmlColor("#FEFECE"));
		defaultsColor.put(ColorParam.sequenceParticipantBorder, new HtmlColor("#A80036"));
		defaultsColor.put(ColorParam.classBackground, new HtmlColor("#FEFECE"));
		defaultsColor.put(ColorParam.classBorder, new HtmlColor("#A80036"));

		defaultsColor.put(ColorParam.objectBackground, new HtmlColor("#FEFECE"));
		defaultsColor.put(ColorParam.objectBorder, new HtmlColor("#A80036"));

		defaultsColor.put(ColorParam.stereotypeCBackground, new HtmlColor("#ADD1B2"));
		defaultsColor.put(ColorParam.stereotypeABackground, new HtmlColor("#A9DCDF"));
		defaultsColor.put(ColorParam.stereotypeIBackground, new HtmlColor("#B4A7E5"));
		defaultsColor.put(ColorParam.stereotypeEBackground, new HtmlColor("#EB937F"));

		defaultsColor.put(ColorParam.packageBackground, new HtmlColor("#FEFECE"));
		defaultsColor.put(ColorParam.packageBorder, new HtmlColor("black"));

		defaultsColor.put(ColorParam.iconPrivate, new HtmlColor("#C82930"));
		defaultsColor.put(ColorParam.iconPrivateBackground, new HtmlColor("#F24D5C"));
		defaultsColor.put(ColorParam.iconProtected, new HtmlColor("#B38D22"));
		defaultsColor.put(ColorParam.iconProtectedBackground, new HtmlColor("#FFFF44"));
		defaultsColor.put(ColorParam.iconPackage, new HtmlColor("#1963A0"));
		defaultsColor.put(ColorParam.iconPackageBackground, new HtmlColor("#4177AF"));
		defaultsColor.put(ColorParam.iconPublic, new HtmlColor("#038048"));
		defaultsColor.put(ColorParam.iconPublicBackground, new HtmlColor("#84BE84"));
	}

	public Color getFontColor(ISkinParam skin, FontParam fontParam) {
		return skin.getFontHtmlColor(fontParam, null).getColor();
	}

	public HtmlColor getHtmlColor(ISkinParam param, ColorParam color) {
		return getHtmlColor(param, color, null);
	}

	public HtmlColor getHtmlColor(ISkinParam param, ColorParam color, String stereotype) {
		HtmlColor result = param.getHtmlColor(color, stereotype);
		if (result == null) {
			result = defaultsColor.get(color);
			if (result == null) {
				throw new IllegalArgumentException();
			}
		}
		return result;
	}

	public Component createComponent(ComponentType type, ISkinParam param, List<? extends CharSequence> stringsToDisplay) {
		final Color background = param.getBackgroundColor().getColor();
		final Color groundBackgroundColor = getHtmlColor(param, ColorParam.sequenceGroupBackground).getColor();
		final Color sequenceDividerBackground = getHtmlColor(param, ColorParam.sequenceDividerBackground).getColor();
		final Color lifeLineBackgroundColor = getHtmlColor(param, ColorParam.sequenceLifeLineBackground).getColor();
		final Color sequenceArrow = getHtmlColor(param, ColorParam.sequenceArrow).getColor();
		final Color sequenceActorBackground = getHtmlColor(param, ColorParam.sequenceActorBackground).getColor();
		final Color sequenceParticipantBackground = getHtmlColor(param, ColorParam.sequenceParticipantBackground)
				.getColor();
		// final Color borderColor = getHtmlColor(param,
		// ColorParam.border).getColor();

		final Font fontArrow = param.getFont(FontParam.SEQUENCE_ARROW, null);
		final Font fontGrouping = param.getFont(FontParam.SEQUENCE_GROUPING, null);
		final Font fontParticipant = param.getFont(FontParam.SEQUENCE_PARTICIPANT, null);
		final Font fontActor = param.getFont(FontParam.SEQUENCE_ACTOR, null);

		if (type.isArrow()) {
			if (type.getArrowConfiguration().isSelfArrow()) {
				return new ComponentRoseSelfArrow(sequenceArrow, getFontColor(param, FontParam.SEQUENCE_ARROW),
						fontArrow, stringsToDisplay, type.getArrowConfiguration());
			}
			return new ComponentRoseArrow(sequenceArrow, getFontColor(param, FontParam.SEQUENCE_ARROW), fontArrow,
					stringsToDisplay, type.getArrowConfiguration());

		}
		if (type == ComponentType.PARTICIPANT_HEAD) {
			final Color borderColor = getHtmlColor(param, ColorParam.sequenceParticipantBorder).getColor();
			return new ComponentRoseParticipant(sequenceParticipantBackground, borderColor, getFontColor(param,
					FontParam.SEQUENCE_PARTICIPANT), fontParticipant, stringsToDisplay);
		}
		if (type == ComponentType.PARTICIPANT_TAIL) {
			final Color borderColor = getHtmlColor(param, ColorParam.sequenceParticipantBorder).getColor();
			return new ComponentRoseParticipant(sequenceParticipantBackground, borderColor, getFontColor(param,
					FontParam.SEQUENCE_PARTICIPANT), fontParticipant, stringsToDisplay);
		}
		if (type == ComponentType.PARTICIPANT_LINE) {
			final Color borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder).getColor();
			return new ComponentRoseLine(borderColor);
		}
		if (type == ComponentType.ACTOR_HEAD) {
			final Color borderColor = getHtmlColor(param, ColorParam.sequenceActorBorder).getColor();
			return new ComponentRoseActor(sequenceActorBackground, borderColor, getFontColor(param,
					FontParam.SEQUENCE_ACTOR), fontActor, stringsToDisplay, true);
		}
		if (type == ComponentType.ACTOR_TAIL) {
			final Color borderColor = getHtmlColor(param, ColorParam.sequenceActorBorder).getColor();
			return new ComponentRoseActor(sequenceActorBackground, borderColor, getFontColor(param,
					FontParam.SEQUENCE_ACTOR), fontActor, stringsToDisplay, false);
		}
		if (type == ComponentType.ACTOR_LINE) {
			final Color borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder).getColor();
			return new ComponentRoseLine(borderColor);
		}
		if (type == ComponentType.NOTE) {
			final Color noteBackgroundColor = getHtmlColor(param, ColorParam.noteBackground).getColor();
			final Color borderColor = getHtmlColor(param, ColorParam.noteBorder).getColor();
			final Font fontNote = param.getFont(FontParam.NOTE, null);
			return new ComponentRoseNote(noteBackgroundColor, borderColor, getFontColor(param, FontParam.NOTE),
					fontNote, stringsToDisplay);
		}
		if (type == ComponentType.GROUPING_HEADER) {
			final Font fontGroupingHeader = param.getFont(FontParam.SEQUENCE_GROUPING_HEADER, null);
			return new ComponentRoseGroupingHeader(getFontColor(param, FontParam.SEQUENCE_GROUPING_HEADER), background,
					groundBackgroundColor, fontGroupingHeader, fontGrouping, stringsToDisplay);
		}
		if (type == ComponentType.GROUPING_BODY) {
			return new ComponentRoseGroupingBody(background, getFontColor(param, FontParam.SEQUENCE_GROUPING));
		}
		if (type == ComponentType.GROUPING_TAIL) {
			return new ComponentRoseGroupingTail(getFontColor(param, FontParam.SEQUENCE_GROUPING));
		}
		if (type == ComponentType.GROUPING_ELSE) {
			return new ComponentRoseGroupingElse(getFontColor(param, FontParam.SEQUENCE_GROUPING), fontGrouping,
					stringsToDisplay.get(0));
		}
		if (type == ComponentType.ALIVE_LINE) {
			final Color borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder).getColor();
			return new ComponentRoseActiveLine(borderColor, lifeLineBackgroundColor);
		}
		if (type == ComponentType.DELAY_LINE) {
			// final Color borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder).getColor();
			final Color borderColor = getFontColor(param, FontParam.SEQUENCE_DELAY);
			return new ComponentRoseDelayLine(borderColor);
		}
		if (type == ComponentType.DELAY_TEXT) {
			return new ComponentRoseDelayText(getFontColor(param, FontParam.SEQUENCE_DELAY), param.getFont(
					FontParam.SEQUENCE_DELAY, null), stringsToDisplay);
		}
		if (type == ComponentType.DESTROY) {
			final Color borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder).getColor();
			return new ComponentRoseDestroy(borderColor);
		}
		if (type == ComponentType.NEWPAGE) {
			return new ComponentRoseNewpage(getFontColor(param, FontParam.SEQUENCE_GROUPING));
		}
		if (type == ComponentType.DIVIDER) {
			return new ComponentRoseDivider(getFontColor(param, FontParam.SEQUENCE_DIVIDER), param.getFont(
					FontParam.SEQUENCE_DIVIDER, null), sequenceDividerBackground, stringsToDisplay);
		}
		if (type == ComponentType.TITLE) {
			return new ComponentRoseTitle(getFontColor(param, FontParam.SEQUENCE_TITLE), param.getFont(
					FontParam.SEQUENCE_TITLE, null), stringsToDisplay);
		}
		if (type == ComponentType.SIGNATURE) {
			return new ComponentRoseTitle(Color.BLACK, fontGrouping, Arrays.asList("This skin was created ",
					"in April 2009."));
		}
		if (type == ComponentType.ENGLOBER) {
			final Color borderColor = getHtmlColor(param, ColorParam.sequenceEngloberLine).getColor();
			final Color backColor = getHtmlColor(param, ColorParam.sequenceEngloberBackground).getColor();
			return new ComponentRoseEnglober(borderColor, backColor, stringsToDisplay, getFontColor(param,
					FontParam.SEQUENCE_ENGLOBER), param.getFont(FontParam.SEQUENCE_ENGLOBER, null));
		}
		return null;
	}

	public Object getProtocolVersion() {
		return 1;
	}

}
