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
 * Revision $Revision: 5272 $
 *
 */
package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.skin.rose.ComponentRoseDestroy;
import net.sourceforge.plantuml.skin.rose.ComponentRoseGroupingElse;
import net.sourceforge.plantuml.skin.rose.ComponentRoseTitle;

public class BlueModern implements Skin {

	private final Font bigFont = new Font("SansSerif", Font.BOLD, 20);
	private final Font participantFont = new Font("SansSerif", Font.PLAIN, 17);
	private final Font normalFont = new Font("SansSerif", Font.PLAIN, 13);
	private final Font smallFont = new Font("SansSerif", Font.BOLD, 11);

	private final Color blue1 = new Color(Integer.parseInt("527BC6", 16));
	private final Color blue2 = new Color(Integer.parseInt("D1DBEF", 16));
	private final Color blue3 = new Color(Integer.parseInt("D7E0F2", 16));

	private final Color red = new Color(Integer.parseInt("A80036", 16));

	private final Color lineColor = new Color(Integer.parseInt("989898", 16));
	private final Color borderGroupColor = new Color(Integer.parseInt("BBBBBB", 16));

	public Component createComponent(ComponentType type, ISkinParam param, List<? extends CharSequence> stringsToDisplay) {

		if (type == ComponentType.PARTICIPANT_HEAD) {
			return new ComponentBlueModernParticipant(blue1, blue2, Color.WHITE, participantFont, stringsToDisplay);
		}
		if (type == ComponentType.PARTICIPANT_TAIL) {
			return new ComponentBlueModernParticipant(blue1, blue2, Color.WHITE, participantFont, stringsToDisplay);
		}
		if (type == ComponentType.PARTICIPANT_LINE) {
			return new ComponentBlueModernLine(lineColor);
		}
		if (type == ComponentType.SELF_ARROW) {
			return new ComponentBlueModernSelfArrow(Color.BLACK, Color.BLACK, normalFont, stringsToDisplay, false, true);
		}
		if (type == ComponentType.DOTTED_SELF_ARROW) {
			return new ComponentBlueModernSelfArrow(Color.BLACK, Color.BLACK, normalFont, stringsToDisplay, true, true);
		}
		if (type == ComponentType.ARROW) {
			return new ComponentBlueModernArrow(Color.BLACK, Color.BLACK, normalFont, stringsToDisplay, 1, false, true);
		}
		if (type == ComponentType.ASYNC_ARROW) {
			return new ComponentBlueModernArrow(Color.BLACK, Color.BLACK, normalFont, stringsToDisplay, 1, false, false);
		}
		if (type == ComponentType.DOTTED_ARROW) {
			return new ComponentBlueModernArrow(Color.BLACK, Color.BLACK, normalFont, stringsToDisplay, 1, true, true);
		}
		if (type == ComponentType.ASYNC_DOTTED_ARROW) {
			return new ComponentBlueModernArrow(Color.BLACK, Color.BLACK, normalFont, stringsToDisplay, 1, true, false);
		}
		if (type == ComponentType.RETURN_ARROW) {
			return new ComponentBlueModernArrow(Color.BLACK, Color.BLACK, normalFont, stringsToDisplay, -1, false, true);
		}
		if (type == ComponentType.ASYNC_RETURN_ARROW) {
			return new ComponentBlueModernArrow(Color.BLACK, Color.BLACK, normalFont, stringsToDisplay, -1, false,
					false);
		}
		if (type == ComponentType.RETURN_DOTTED_ARROW) {
			return new ComponentBlueModernArrow(Color.BLACK, Color.BLACK, normalFont, stringsToDisplay, -1, true, true);
		}
		if (type == ComponentType.ASYNC_RETURN_DOTTED_ARROW) {
			return new ComponentBlueModernArrow(Color.BLACK, Color.BLACK, normalFont, stringsToDisplay, -1, true, false);
		}
		if (type == ComponentType.ACTOR_HEAD) {
			return new ComponentBlueModernActor(blue2, blue1, blue1, participantFont, stringsToDisplay, true);
		}
		if (type == ComponentType.ACTOR_TAIL) {
			return new ComponentBlueModernActor(blue2, blue1, blue1, participantFont, stringsToDisplay, false);
		}
		if (type == ComponentType.ACTOR_LINE) {
			return new ComponentBlueModernLine(lineColor);
		}
		if (type == ComponentType.NOTE) {
			return new ComponentBlueModernNote(Color.WHITE, Color.BLACK, Color.BLACK, normalFont, stringsToDisplay);
		}
		if (type == ComponentType.ALIVE_LINE) {
			return new ComponentBlueModernActiveLine(blue1);
		}
		if (type == ComponentType.DESTROY) {
			return new ComponentRoseDestroy(red);
		}
		if (type == ComponentType.GROUPING_HEADER) {
			return new ComponentBlueModernGroupingHeader(blue1, blue3, borderGroupColor, Color.WHITE, Color.BLACK,
					normalFont, smallFont, stringsToDisplay);
		}
		if (type == ComponentType.GROUPING_BODY) {
			return new ComponentBlueModernGroupingBody(blue3, borderGroupColor);
		}
		if (type == ComponentType.GROUPING_TAIL) {
			return new ComponentBlueModernGroupingTail(borderGroupColor);
		}
		if (type == ComponentType.GROUPING_ELSE) {
			return new ComponentRoseGroupingElse(Color.BLACK, smallFont, stringsToDisplay.get(0));
		}
		if (type == ComponentType.TITLE) {
			return new ComponentRoseTitle(Color.BLACK, bigFont, stringsToDisplay);
		}
		if (type == ComponentType.NEWPAGE) {
			return new ComponentBlueModernNewpage(blue1);
		}
		if (type == ComponentType.DIVIDER) {
			return new ComponentBlueModernDivider(Color.BLACK, normalFont, blue2, blue1, Color.BLACK, stringsToDisplay);
		}
		if (type == ComponentType.SIGNATURE) {
			return new ComponentRoseTitle(Color.BLACK, smallFont, Arrays.asList("This skin was created ",
					"in April 2009."));
		}
		if (type == ComponentType.ENGLOBER) {
			return new ComponentBlueModernEnglober(blue1, blue3, stringsToDisplay, Color.BLACK, param
					.getFont(FontParam.SEQUENCE_ENGLOBER));
		}

		return null;

	}

	public Object getProtocolVersion() {
		return 1;
	}

}
