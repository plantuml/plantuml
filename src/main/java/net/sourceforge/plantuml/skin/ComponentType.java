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
package net.sourceforge.plantuml.skin;

import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.style.Styleable;

public enum ComponentType implements Styleable {

	ARROW,

	ACTOR_HEAD, ACTOR_TAIL,

	BOUNDARY_HEAD, BOUNDARY_TAIL, CONTROL_HEAD, CONTROL_TAIL, ENTITY_HEAD, ENTITY_TAIL, QUEUE_HEAD, QUEUE_TAIL,
	DATABASE_HEAD, DATABASE_TAIL, COLLECTIONS_HEAD, COLLECTIONS_TAIL,

	//
	ACTIVATION_BOX_CLOSE_CLOSE, ACTIVATION_BOX_CLOSE_OPEN, ACTIVATION_BOX_OPEN_CLOSE, ACTIVATION_BOX_OPEN_OPEN,

	DELAY_TEXT, DESTROY,

	DELAY_LINE, PARTICIPANT_LINE, /*CONTINUE_LINE,*/

	//
	GROUPING_ELSE_LEGACY, GROUPING_ELSE_TEOZ, GROUPING_HEADER_LEGACY, GROUPING_HEADER_TEOZ, GROUPING_SPACE,
	//
	NEWPAGE, NOTE, NOTE_HEXAGONAL, NOTE_BOX, DIVIDER, REFERENCE, ENGLOBER,

	//
	PARTICIPANT_HEAD, PARTICIPANT_TAIL

	//
	/* TITLE, SIGNATURE */;

	public boolean isArrow() {
		return this == ARROW;
	}

	public StyleSignature getStyleSignature() {
		if (this == PARTICIPANT_HEAD || this == PARTICIPANT_TAIL)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.sequenceDiagram, SName.participant);

		if (this == PARTICIPANT_LINE /*|| this == CONTINUE_LINE*/)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.sequenceDiagram, SName.lifeLine);

		if (this == ACTIVATION_BOX_CLOSE_CLOSE || this == ACTIVATION_BOX_CLOSE_OPEN || this == ACTIVATION_BOX_OPEN_CLOSE
				|| this == ACTIVATION_BOX_OPEN_OPEN)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.sequenceDiagram, SName.activationBox);

		if (this == DESTROY)
			return LifeEventType.DESTROY.getStyleSignature();

		if (this == DIVIDER)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.sequenceDiagram, SName.separator);

		if (this == ENGLOBER)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.sequenceDiagram, SName.box);

		if (this == NEWPAGE)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.sequenceDiagram, SName.newpage);

		if (this == NOTE)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.sequenceDiagram, SName.note);

		if (this == DELAY_TEXT)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.sequenceDiagram, SName.lifeLine, SName.delay);

		if (this == DELAY_LINE)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.sequenceDiagram, SName.lifeLine, SName.delay);

//		if (this == REFERENCE) {
//			return StyleSignature.of(SName.root, SName.element,
//					SName.sequenceDiagram, SName.reference);
//		}
		throw new UnsupportedOperationException(toString());
	}
}
