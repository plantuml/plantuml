/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.skin;

import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.Styleable;

public enum ComponentType implements Styleable {

	ARROW,

	ACTOR_HEAD, ACTOR_TAIL,

	BOUNDARY_HEAD, BOUNDARY_TAIL, CONTROL_HEAD, CONTROL_TAIL, ENTITY_HEAD, ENTITY_TAIL, QUEUE_HEAD, QUEUE_TAIL, DATABASE_HEAD, DATABASE_TAIL, COLLECTIONS_HEAD, COLLECTIONS_TAIL,

	//
	ALIVE_BOX_CLOSE_CLOSE, ALIVE_BOX_CLOSE_OPEN, ALIVE_BOX_OPEN_CLOSE, ALIVE_BOX_OPEN_OPEN,

	DELAY_TEXT, DESTROY,

	DELAY_LINE, PARTICIPANT_LINE, CONTINUE_LINE,

	//
	GROUPING_ELSE, GROUPING_HEADER, GROUPING_SPACE,
	//
	NEWPAGE, NOTE, NOTE_HEXAGONAL, NOTE_BOX, DIVIDER, REFERENCE, ENGLOBER,

	//
	PARTICIPANT_HEAD, PARTICIPANT_TAIL

	//
	/* TITLE, SIGNATURE */;

	public boolean isArrow() {
		return this == ARROW;
	}

	public StyleSignature getDefaultStyleDefinition() {
		if (this == PARTICIPANT_HEAD || this == PARTICIPANT_TAIL) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.participant);
		}
		if (this == PARTICIPANT_LINE || this == CONTINUE_LINE) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.lifeLine);
		}
		if (this == ALIVE_BOX_CLOSE_CLOSE || this == ALIVE_BOX_CLOSE_OPEN || this == ALIVE_BOX_OPEN_CLOSE
				|| this == ALIVE_BOX_OPEN_OPEN) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.lifeLine);
		}
		if (this == DESTROY) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.lifeLine);
		}
		if (this == DIVIDER) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.separator);
		}
		if (this == ENGLOBER) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.box);
		}
		if (this == NOTE) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.note);
		}
		if (this == DELAY_TEXT) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.delay);
		}
		if (this == DELAY_LINE) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.delay);
		}
//		if (this == REFERENCE) {
//			return StyleSignature.of(SName.root, SName.element,
//					SName.sequenceDiagram, SName.reference);
//		}
		if (SkinParam.USE_STYLES()) {
			throw new UnsupportedOperationException(toString());

		}
		return StyleSignature.of(SName.root);
	}
}
