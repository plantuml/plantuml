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
package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.skin.rose.ComponentRoseDestroy;
import net.sourceforge.plantuml.skin.rose.ComponentRoseGroupingElse;
import net.sourceforge.plantuml.skin.rose.ComponentRoseReference;
import net.sourceforge.plantuml.skin.rose.ComponentRoseTitle;
import net.sourceforge.plantuml.ugraphic.UFont;

public class BlueModern implements Skin {

	private final UFont bigFont = new UFont("SansSerif", Font.BOLD, 20);
	private final UFont participantFont = new UFont("SansSerif", Font.PLAIN, 17);
	private final UFont normalFont = new UFont("SansSerif", Font.PLAIN, 13);
	private final UFont smallFont = new UFont("SansSerif", Font.BOLD, 11);

	private final HtmlColor blue1 = HtmlColor.getColorIfValid("#527BC6");
	private final HtmlColor blue2 = HtmlColor.getColorIfValid("#D1DBEF");
	private final HtmlColor blue3 = HtmlColor.getColorIfValid("#D7E0F2");

	private final HtmlColor red = HtmlColor.getColorIfValid("#A80036");

	private final HtmlColor lineColor = HtmlColor.getColorIfValid("#989898");
	private final HtmlColor borderGroupColor = HtmlColor.getColorIfValid("#BBBBBB");

	public Component createComponent(ComponentType type, ISkinParam param, List<? extends CharSequence> stringsToDisplay) {

		if (type.isArrow()) {
			if (type.getArrowConfiguration().isSelfArrow()) {
				return new ComponentBlueModernSelfArrow(HtmlColor.BLACK, HtmlColor.BLACK, normalFont, stringsToDisplay,
						type.getArrowConfiguration());
			}
			return new ComponentBlueModernArrow(HtmlColor.BLACK, HtmlColor.BLACK, normalFont, stringsToDisplay,
					type.getArrowConfiguration());
		}
		if (type == ComponentType.PARTICIPANT_HEAD) {
			return new ComponentBlueModernParticipant(blue1, blue2, HtmlColor.WHITE, participantFont, stringsToDisplay);
		}
		if (type == ComponentType.PARTICIPANT_TAIL) {
			return new ComponentBlueModernParticipant(blue1, blue2, HtmlColor.WHITE, participantFont, stringsToDisplay);
		}
		if (type == ComponentType.PARTICIPANT_LINE) {
			return new ComponentBlueModernLine(lineColor);
		}
		if (type == ComponentType.CONTINUE_LINE) {
			return new ComponentBlueModernLine(lineColor);
		}
		if (type == ComponentType.ACTOR_HEAD) {
			return new ComponentBlueModernActor(blue2, blue1, blue1, participantFont, stringsToDisplay, true);
		}
		if (type == ComponentType.ACTOR_TAIL) {
			return new ComponentBlueModernActor(blue2, blue1, blue1, participantFont, stringsToDisplay, false);
		}
		if (type == ComponentType.NOTE) {
			return new ComponentBlueModernNote(HtmlColor.WHITE, HtmlColor.BLACK, HtmlColor.BLACK, normalFont,
					stringsToDisplay);
		}
		if (type == ComponentType.ALIVE_BOX_CLOSE_CLOSE) {
			return new ComponentBlueModernActiveLine(blue1, true, true);
		}
		if (type == ComponentType.ALIVE_BOX_CLOSE_OPEN) {
			return new ComponentBlueModernActiveLine(blue1, true, false);
		}
		if (type == ComponentType.ALIVE_BOX_OPEN_CLOSE) {
			return new ComponentBlueModernActiveLine(blue1, false, true);
		}
		if (type == ComponentType.ALIVE_BOX_OPEN_OPEN) {
			return new ComponentBlueModernActiveLine(blue1, false, false);
		}
		if (type == ComponentType.DELAY_LINE) {
			return new ComponentBlueModernDelayLine(lineColor);
		}
		if (type == ComponentType.DELAY_TEXT) {
			return new ComponentBlueModernDelayText(HtmlColor.BLACK, param.getFont(FontParam.SEQUENCE_DELAY, null),
					stringsToDisplay);
		}
		if (type == ComponentType.DESTROY) {
			return new ComponentRoseDestroy(red);
		}
		if (type == ComponentType.GROUPING_HEADER) {
			return new ComponentBlueModernGroupingHeader(blue1, blue3, borderGroupColor, HtmlColor.WHITE,
					HtmlColor.BLACK, normalFont, smallFont, stringsToDisplay);
		}
		if (type == ComponentType.GROUPING_BODY) {
			return new ComponentBlueModernGroupingBody(blue3, borderGroupColor);
		}
		if (type == ComponentType.GROUPING_TAIL) {
			return new ComponentBlueModernGroupingTail(borderGroupColor);
		}
		if (type == ComponentType.GROUPING_ELSE) {
			return new ComponentRoseGroupingElse(HtmlColor.BLACK, HtmlColor.BLACK, smallFont, stringsToDisplay.get(0));
		}
		if (type == ComponentType.TITLE) {
			return new ComponentRoseTitle(HtmlColor.BLACK, bigFont, stringsToDisplay);
		}
		if (type == ComponentType.REFERENCE) {
			return new ComponentRoseReference(HtmlColor.BLACK, HtmlColor.WHITE, normalFont, borderGroupColor,
					blue1, blue3, normalFont, stringsToDisplay, HorizontalAlignement.CENTER);
		}
		if (type == ComponentType.NEWPAGE) {
			return new ComponentBlueModernNewpage(blue1);
		}
		if (type == ComponentType.DIVIDER) {
			return new ComponentBlueModernDivider(HtmlColor.BLACK, normalFont, blue2, blue1, HtmlColor.BLACK,
					stringsToDisplay);
		}
		if (type == ComponentType.SIGNATURE) {
			return new ComponentRoseTitle(HtmlColor.BLACK, smallFont, Arrays.asList("This skin was created ",
					"in April 2009."));
		}
		if (type == ComponentType.ENGLOBER) {
			return new ComponentBlueModernEnglober(blue1, blue3, stringsToDisplay, HtmlColor.BLACK, param.getFont(
					FontParam.SEQUENCE_BOX, null));
		}

		return null;

	}

	public Object getProtocolVersion() {
		return 1;
	}

}
