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
package net.sourceforge.plantuml.sequencediagram;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.Styleable;

public enum ParticipantType implements Styleable {
	PARTICIPANT(ColorParam.participantBackground), //
	ACTOR(ColorParam.actorBackground), //
	BOUNDARY(ColorParam.boundaryBackground), //
	CONTROL(ColorParam.controlBackground), //
	ENTITY(ColorParam.entityBackground), //
	QUEUE(ColorParam.queueBackground), //
	DATABASE(ColorParam.databaseBackground), //
	COLLECTIONS(ColorParam.collectionsBackground);

	private final ColorParam background;

	private ParticipantType(ColorParam background) {
		this.background = background;
	}

	public ColorParam getBackgroundColorParam() {
		return background;
	}

	public StyleSignature getDefaultStyleDefinition() {
		if (this == PARTICIPANT) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.participant);
		}
		if (this == ACTOR) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.actor);
		}
		if (this == BOUNDARY) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.boundary);
		}
		if (this == CONTROL) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.control);
		}
		if (this == ENTITY) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.entity);
		}
		if (this == QUEUE) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.queue);
		}
		if (this == DATABASE) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.database);
		}
		if (this == COLLECTIONS) {
			return StyleSignature.of(SName.root, SName.element,
					SName.sequenceDiagram, SName.collections);
		}
		return null;
	}

}
