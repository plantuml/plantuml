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
 * Revision $Revision: 5921 $
 *
 */
package net.sourceforge.plantuml.asciiart;

import java.util.List;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;

public class TextSkin implements Skin {

	private final FileFormat fileFormat;

	public TextSkin(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	public Component createComponent(ComponentType type, ISkinParam param, List<? extends CharSequence> stringsToDisplay) {
		if (type == ComponentType.PARTICIPANT_HEAD || type == ComponentType.PARTICIPANT_TAIL) {
			return new ComponentTextParticipant(type, stringsToDisplay, fileFormat);
		}
		if (type == ComponentType.ACTOR_HEAD || type == ComponentType.ACTOR_TAIL) {
			return new ComponentTextActor(type, stringsToDisplay, fileFormat);
		}
		if (type.isArrow()
				&& (type.getArrowConfiguration().isLeftToRightNormal() || type.getArrowConfiguration()
						.isRightToLeftReverse())) {
			return new ComponentTextArrow(type, stringsToDisplay, fileFormat);
		}
		if (type.isArrow() && type.getArrowConfiguration().isSelfArrow()) {
			return new ComponentTextSelfArrow(type, stringsToDisplay, fileFormat);
		}
		if (type == ComponentType.PARTICIPANT_LINE || type == ComponentType.ACTOR_LINE) {
			return new ComponentTextLine(fileFormat);
		}
		if (type == ComponentType.ALIVE_LINE) {
			return new ComponentTextActiveLine(fileFormat);
		}
		if (type == ComponentType.NOTE) {
			return new ComponentTextNote(type, stringsToDisplay, fileFormat);
		}
		if (type == ComponentType.DIVIDER) {
			return new ComponentTextDivider(type, stringsToDisplay, fileFormat);
		}
		if (type == ComponentType.GROUPING_HEADER) {
			return new ComponentTextGroupingHeader(type, stringsToDisplay, fileFormat);
		}
		if (type == ComponentType.GROUPING_BODY) {
			return new ComponentTextGroupingBody(type, stringsToDisplay, fileFormat);
		}
		if (type == ComponentType.GROUPING_TAIL) {
			return new ComponentTextGroupingTail(type, stringsToDisplay, fileFormat);
		}
		if (type == ComponentType.GROUPING_ELSE) {
			return new ComponentTextGroupingElse(type, stringsToDisplay, fileFormat);
		}
		throw new UnsupportedOperationException(type.toString());
	}

	public Object getProtocolVersion() {
		return 1;
	}

}
