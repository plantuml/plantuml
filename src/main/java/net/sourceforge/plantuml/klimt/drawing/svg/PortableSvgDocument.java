/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.klimt.drawing.svg;

import net.sourceforge.plantuml.klimt.UGroupType;

public abstract class PortableSvgDocument {

	public abstract IElement createElement(String name);

	public void applyGroupAttribute(IElement element, UGroupType key, String value) {
		switch (key) {
		case TITLE:
			final IElement title = this.createElement(UGroupType.TITLE.getSvgKeyAttributeName());
			title.setTextContent(value);
			element.appendChild(title);
			break;
		case ID:
			// ignored
			break;
		case DATA_UID:
			// DATA_UID *will* be renamed to ID, but right now, we do some hack
			element.setAttribute("id", value);
			break;
		// I also suggest that we rename "data-participant-1" to "data-entity-1" and
		// "data-participant-2" to "data-entity-2"
		case DATA_PARTICIPANT_1:
		case DATA_ENTITY_1_UID:
			element.setAttribute("data-entity-1", value);
			break;
		case DATA_PARTICIPANT_2:
		case DATA_ENTITY_2_UID:
			element.setAttribute("data-entity-2", value);
			break;
		case CLASS:
		case DATA_SOURCE_LINE:
		case DATA_QUALIFIED_NAME:
		case DATA_ENTITY_UID:
		case DATA_VISIBILITY_MODIFIER:
		case DATA_LINK_TYPE:
			element.setAttribute(key.getSvgKeyAttributeName(), value);
			break;
		// default: other keys are intentionally ignored
		}
	}

}
