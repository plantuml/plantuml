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
package net.sourceforge.plantuml.sequencediagram;

import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;

public class LifeEvent extends AbstractEvent implements Event {

	private final Participant p;
	private final LifeEventType type;
	private final Fashion backcolor;
	private final StyleBuilder styleBuilder;

	public LifeEvent(Participant p, LifeEventType type, Fashion backcolor, StyleBuilder styleBuilder) {
		this.styleBuilder = styleBuilder;
		this.p = p;
		this.type = type;
		this.backcolor = backcolor;
	}

	@Override
	public String toString() {
		return "LifeEvent:" + p + " " + type;
	}

	public Participant getParticipant() {
		return p;
	}

	public LifeEventType getType() {
		return type;
	}

	public Fashion getSpecificColors() {
		return backcolor;
	}

	public boolean dealWith(Participant someone) {
		return this.p == someone;
	}

	public boolean isActivate() {
		return type == LifeEventType.ACTIVATE;
	}

	public boolean isDeactivateOrDestroy() {
		return type == LifeEventType.DEACTIVATE || type == LifeEventType.DESTROY;
	}

	public boolean isDeactivate() {
		return type == LifeEventType.DEACTIVATE;
	}

	public boolean isDestroy(Participant p) {
		return this.p == p && type == LifeEventType.DESTROY;
	}

	private AbstractMessage message;

	public void setMessage(AbstractMessage message) {
		this.message = message;
	}

	public AbstractMessage getMessage() {
		return message;
	}

	public StyleSignature getStyleSignature() {
		return type.getStyleSignature();
	}

	public Style[] getUsedStyle() {
		final Style style = type.getStyleSignature().getMergedStyle(styleBuilder);
		return new Style[] { style };
	}

}
