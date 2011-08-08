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
 * Revision $Revision: 6602 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.AlignParam;
import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UFont;

public class Rose implements Skin {

	private final Map<ColorParam, HtmlColor> defaultsColor = new EnumMap<ColorParam, HtmlColor>(ColorParam.class);

	public Rose() {
		defaultsColor.put(ColorParam.background, HtmlColor.getColorIfValid("white"));

		defaultsColor.put(ColorParam.sequenceArrow, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.usecaseArrow, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.classArrow, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.objectArrow, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.activityArrow, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.componentArrow, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.stateArrow, HtmlColor.getColorIfValid("#A80036"));

		defaultsColor.put(ColorParam.sequenceLifeLineBackground, HtmlColor.getColorIfValid("white"));
		defaultsColor.put(ColorParam.sequenceLifeLineBorder, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.sequenceGroupBackground, HtmlColor.getColorIfValid("#EEEEEE"));
		defaultsColor.put(ColorParam.sequenceGroupBorder, HtmlColor.BLACK);
		defaultsColor.put(ColorParam.sequenceDividerBackground, HtmlColor.getColorIfValid("#EEEEEE"));
		defaultsColor.put(ColorParam.sequenceReferenceBorder, HtmlColor.BLACK);
		defaultsColor.put(ColorParam.sequenceReferenceBackground, HtmlColor.getColorIfValid("white"));
		defaultsColor.put(ColorParam.sequenceReferenceHeaderBackground, HtmlColor.getColorIfValid("#EEEEEE"));
		defaultsColor.put(ColorParam.sequenceBoxBorder, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.sequenceBoxBackground, HtmlColor.getColorIfValid("#DDDDDD"));

		defaultsColor.put(ColorParam.noteBackground, HtmlColor.getColorIfValid("#FBFB77"));
		defaultsColor.put(ColorParam.noteBorder, HtmlColor.getColorIfValid("#A80036"));

		defaultsColor.put(ColorParam.activityBackground, HtmlColor.getColorIfValid("#FEFECE"));
		defaultsColor.put(ColorParam.activityBorder, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.activityStart, HtmlColor.BLACK);
		defaultsColor.put(ColorParam.activityEnd, HtmlColor.BLACK);
		defaultsColor.put(ColorParam.activityBar, HtmlColor.BLACK);

		defaultsColor.put(ColorParam.stateBackground, HtmlColor.getColorIfValid("#FEFECE"));
		defaultsColor.put(ColorParam.stateBorder, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.stateStart, HtmlColor.BLACK);
		defaultsColor.put(ColorParam.stateEnd, HtmlColor.BLACK);

		defaultsColor.put(ColorParam.usecaseBackground, HtmlColor.getColorIfValid("#FEFECE"));
		defaultsColor.put(ColorParam.usecaseBorder, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.componentBackground, HtmlColor.getColorIfValid("#FEFECE"));
		defaultsColor.put(ColorParam.componentBorder, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.componentInterfaceBackground, HtmlColor.getColorIfValid("#FEFECE"));
		defaultsColor.put(ColorParam.componentInterfaceBorder, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.usecaseActorBackground, HtmlColor.getColorIfValid("#FEFECE"));
		defaultsColor.put(ColorParam.usecaseActorBorder, HtmlColor.getColorIfValid("#A80036"));

		defaultsColor.put(ColorParam.sequenceActorBackground, HtmlColor.getColorIfValid("#FEFECE"));
		defaultsColor.put(ColorParam.sequenceActorBorder, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.sequenceParticipantBackground, HtmlColor.getColorIfValid("#FEFECE"));
		defaultsColor.put(ColorParam.sequenceParticipantBorder, HtmlColor.getColorIfValid("#A80036"));
		defaultsColor.put(ColorParam.classBackground, HtmlColor.getColorIfValid("#FEFECE"));
		defaultsColor.put(ColorParam.classBorder, HtmlColor.getColorIfValid("#A80036"));

		defaultsColor.put(ColorParam.objectBackground, HtmlColor.getColorIfValid("#FEFECE"));
		defaultsColor.put(ColorParam.objectBorder, HtmlColor.getColorIfValid("#A80036"));

		defaultsColor.put(ColorParam.stereotypeCBackground, HtmlColor.getColorIfValid("#ADD1B2"));
		defaultsColor.put(ColorParam.stereotypeABackground, HtmlColor.getColorIfValid("#A9DCDF"));
		defaultsColor.put(ColorParam.stereotypeIBackground, HtmlColor.getColorIfValid("#B4A7E5"));
		defaultsColor.put(ColorParam.stereotypeEBackground, HtmlColor.getColorIfValid("#EB937F"));

		defaultsColor.put(ColorParam.packageBackground, HtmlColor.getColorIfValid("#FEFECE"));
		defaultsColor.put(ColorParam.packageBorder, HtmlColor.BLACK);

		defaultsColor.put(ColorParam.iconPrivate, HtmlColor.getColorIfValid("#C82930"));
		defaultsColor.put(ColorParam.iconPrivateBackground, HtmlColor.getColorIfValid("#F24D5C"));
		defaultsColor.put(ColorParam.iconProtected, HtmlColor.getColorIfValid("#B38D22"));
		defaultsColor.put(ColorParam.iconProtectedBackground, HtmlColor.getColorIfValid("#FFFF44"));
		defaultsColor.put(ColorParam.iconPackage, HtmlColor.getColorIfValid("#1963A0"));
		defaultsColor.put(ColorParam.iconPackageBackground, HtmlColor.getColorIfValid("#4177AF"));
		defaultsColor.put(ColorParam.iconPublic, HtmlColor.getColorIfValid("#038048"));
		defaultsColor.put(ColorParam.iconPublicBackground, HtmlColor.getColorIfValid("#84BE84"));
	}

	public HtmlColor getFontColor(ISkinParam skin, FontParam fontParam) {
		return skin.getFontHtmlColor(fontParam, null);
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
		final HtmlColor background = param.getBackgroundColor();
		final HtmlColor groupBorder = getHtmlColor(param, ColorParam.sequenceGroupBorder);
		final HtmlColor groupBackground = getHtmlColor(param, ColorParam.sequenceGroupBackground);
		final HtmlColor sequenceDividerBackground = getHtmlColor(param, ColorParam.sequenceDividerBackground);
		final HtmlColor sequenceReferenceBackground = getHtmlColor(param, ColorParam.sequenceReferenceBackground);
		final HtmlColor sequenceReferenceHeaderBackground = getHtmlColor(param,
				ColorParam.sequenceReferenceHeaderBackground);
		final HtmlColor sequenceReferenceBorder = getHtmlColor(param, ColorParam.sequenceReferenceBorder);
		final HtmlColor lifeLineBackgroundColor = getHtmlColor(param, ColorParam.sequenceLifeLineBackground);
		final HtmlColor sequenceArrow = getHtmlColor(param, ColorParam.sequenceArrow);
		final HtmlColor sequenceActorBackground = getHtmlColor(param, ColorParam.sequenceActorBackground);
		final HtmlColor sequenceParticipantBackground = getHtmlColor(param, ColorParam.sequenceParticipantBackground);
		// final Color borderColor = getHtmlColor(param,
		// ColorParam.border).getColor();

		final UFont fontArrow = param.getFont(FontParam.SEQUENCE_ARROW, null);
		final UFont fontGrouping = param.getFont(FontParam.SEQUENCE_GROUP, null);
		final UFont fontParticipant = param.getFont(FontParam.SEQUENCE_PARTICIPANT, null);
		final UFont fontActor = param.getFont(FontParam.SEQUENCE_ACTOR, null);

		if (type.isArrow()) {
			if (type.getArrowConfiguration().isSelfArrow()) {
				return new ComponentRoseSelfArrow(sequenceArrow, getFontColor(param, FontParam.SEQUENCE_ARROW),
						fontArrow, stringsToDisplay, type.getArrowConfiguration());
			}
			return new ComponentRoseArrow(sequenceArrow, getFontColor(param, FontParam.SEQUENCE_ARROW), fontArrow,
					stringsToDisplay, type.getArrowConfiguration(),
					param.getHorizontalAlignement(AlignParam.SEQUENCE_MESSAGE_ALIGN));

		}
		if (type == ComponentType.PARTICIPANT_HEAD) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceParticipantBorder);
			return new ComponentRoseParticipant(sequenceParticipantBackground, borderColor, getFontColor(param,
					FontParam.SEQUENCE_PARTICIPANT), fontParticipant, stringsToDisplay);
		}
		if (type == ComponentType.PARTICIPANT_TAIL) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceParticipantBorder);
			return new ComponentRoseParticipant(sequenceParticipantBackground, borderColor, getFontColor(param,
					FontParam.SEQUENCE_PARTICIPANT), fontParticipant, stringsToDisplay);
		}
		if (type == ComponentType.PARTICIPANT_LINE) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder);
			return new ComponentRoseLine(borderColor, false);
		}
		if (type == ComponentType.CONTINUE_LINE) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder);
			return new ComponentRoseLine(borderColor, true);
		}
		if (type == ComponentType.ACTOR_HEAD) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceActorBorder);
			return new ComponentRoseActor(sequenceActorBackground, borderColor, getFontColor(param,
					FontParam.SEQUENCE_ACTOR), fontActor, stringsToDisplay, true);
		}
		if (type == ComponentType.ACTOR_TAIL) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceActorBorder);
			return new ComponentRoseActor(sequenceActorBackground, borderColor, getFontColor(param,
					FontParam.SEQUENCE_ACTOR), fontActor, stringsToDisplay, false);
		}
		if (type == ComponentType.NOTE) {
			final HtmlColor noteBackgroundColor = getHtmlColor(param, ColorParam.noteBackground);
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.noteBorder);
			final UFont fontNote = param.getFont(FontParam.NOTE, null);
			return new ComponentRoseNote(noteBackgroundColor, borderColor, getFontColor(param, FontParam.NOTE),
					fontNote, stringsToDisplay);
		}
		if (type == ComponentType.GROUPING_HEADER) {
			final UFont fontGroupingHeader = param.getFont(FontParam.SEQUENCE_GROUP_HEADER, null);
			return new ComponentRoseGroupingHeader(getFontColor(param, FontParam.SEQUENCE_GROUP_HEADER), background,
					groupBackground, groupBorder, fontGroupingHeader, fontGrouping, stringsToDisplay);
		}
		if (type == ComponentType.GROUPING_BODY) {
			return new ComponentRoseGroupingBody(background, groupBorder);
		}
		if (type == ComponentType.GROUPING_TAIL) {
			return new ComponentRoseGroupingTail(getFontColor(param, FontParam.SEQUENCE_GROUP), groupBorder);
		}
		if (type == ComponentType.GROUPING_ELSE) {
			return new ComponentRoseGroupingElse(getFontColor(param, FontParam.SEQUENCE_GROUP), groupBorder,
					fontGrouping, stringsToDisplay.get(0));
		}
		if (type == ComponentType.ALIVE_BOX_CLOSE_CLOSE) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder);
			return new ComponentRoseActiveLine(borderColor, lifeLineBackgroundColor, true, true);
		}
		if (type == ComponentType.ALIVE_BOX_CLOSE_OPEN) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder);
			return new ComponentRoseActiveLine(borderColor, lifeLineBackgroundColor, true, false);
		}
		if (type == ComponentType.ALIVE_BOX_OPEN_CLOSE) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder);
			return new ComponentRoseActiveLine(borderColor, lifeLineBackgroundColor, false, true);
		}
		if (type == ComponentType.ALIVE_BOX_OPEN_OPEN) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder);
			return new ComponentRoseActiveLine(borderColor, lifeLineBackgroundColor, false, false);
		}
		if (type == ComponentType.DELAY_LINE) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder);
			return new ComponentRoseDelayLine(borderColor);
		}
		if (type == ComponentType.DELAY_TEXT) {
			return new ComponentRoseDelayText(getFontColor(param, FontParam.SEQUENCE_DELAY), param.getFont(
					FontParam.SEQUENCE_DELAY, null), stringsToDisplay);
		}
		if (type == ComponentType.DESTROY) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceLifeLineBorder);
			return new ComponentRoseDestroy(borderColor);
		}
		if (type == ComponentType.NEWPAGE) {
			return new ComponentRoseNewpage(getFontColor(param, FontParam.SEQUENCE_GROUP));
		}
		if (type == ComponentType.DIVIDER) {
			return new ComponentRoseDivider(getFontColor(param, FontParam.SEQUENCE_DIVIDER), param.getFont(
					FontParam.SEQUENCE_DIVIDER, null), sequenceDividerBackground, stringsToDisplay);
		}
		if (type == ComponentType.REFERENCE) {
			final HtmlColor sequenceReferenceBackground2 = getHtmlColor(param, ColorParam.sequenceReferenceBackground);
			final HtmlColor sequenceReferenceHeaderBackground2 = getHtmlColor(param,
					ColorParam.sequenceReferenceHeaderBackground);

			final UFont fontGroupingHeader = param.getFont(FontParam.SEQUENCE_GROUP_HEADER, null);
			return new ComponentRoseReference(getFontColor(param, FontParam.SEQUENCE_REFERENCE), getFontColor(param,
					FontParam.SEQUENCE_GROUP), param.getFont(FontParam.SEQUENCE_REFERENCE, null),
					sequenceReferenceBorder, sequenceReferenceHeaderBackground, sequenceReferenceBackground,
					fontGroupingHeader, stringsToDisplay,
					param.getHorizontalAlignement(AlignParam.SEQUENCE_REFERENCE_ALIGN));
		}
		if (type == ComponentType.TITLE) {
			return new ComponentRoseTitle(getFontColor(param, FontParam.SEQUENCE_TITLE), param.getFont(
					FontParam.SEQUENCE_TITLE, null), stringsToDisplay);
		}
		if (type == ComponentType.SIGNATURE) {
			return new ComponentRoseTitle(HtmlColor.BLACK, fontGrouping, Arrays.asList("This skin was created ",
					"in April 2009."));
		}
		if (type == ComponentType.ENGLOBER) {
			final HtmlColor borderColor = getHtmlColor(param, ColorParam.sequenceBoxBorder);
			final HtmlColor backColor = getHtmlColor(param, ColorParam.sequenceBoxBackground);
			return new ComponentRoseEnglober(borderColor, backColor, stringsToDisplay, getFontColor(param,
					FontParam.SEQUENCE_BOX), param.getFont(FontParam.SEQUENCE_BOX, null));
		}
		return null;
	}

	public Object getProtocolVersion() {
		return 1;
	}

}
