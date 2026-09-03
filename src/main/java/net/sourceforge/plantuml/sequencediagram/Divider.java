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

import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.WithStyle;

public class Divider extends AbstractEvent implements Event, WithStyle, EventWithDeactivate {

	private final Display text;

	final private Style style;

	public StyleSignature getStyleSignature() {
		return ComponentType.DIVIDER.getStyleSignature();
	}

	public Style[] getUsedStyles() {
		return new Style[] { style };
	}

	public Divider(Display text, StyleBuilder styleBuilder) {
		this.text = text;
		this.style = getStyleSignature().getMergedStyleREMOVEME(styleBuilder);
	}

	public final Display getText() {
		return text;
	}

	public boolean dealWith(Participant someone) {
		return false;
	}

	// A divider (== ... ==) closes off whatever message preceded it, exactly like
	// the end of an alt/opt/loop block does for a GroupingLeaf. Without this,
	// SequenceDiagram.lastEventWithDeactivate keeps pointing at the last message
	// that was sent *before* the divider, so a bare activate/deactivate/destroy
	// placed right after the divider (with no message of its own) wrongly tries
	// to reattach to that stale message instead of becoming its own event. When
	// the participant matches and already carries a decoration from before the
	// divider, that reattachment fails outright with a spurious
	// "Activate/Deactivate already done" error (the exact case in
	// https://github.com/plantuml/plantuml/issues/2729). When it doesn't collide
	// -- e.g. a bare "destroy" reusing a message several sections back -- it can
	// silently attach in the wrong place instead of erroring.
	//
	// dealWith() staying false (like GroupingLeaf's) makes such a bare
	// activate/deactivate/destroy become a standalone, unattached LifeEvent
	// instead, positioned by its own place in the event timeline -- i.e. right
	// after this divider, which is where it belongs.
	private double posYendLevel;

	public void setPosYendLevel(double posYendLevel) {
		this.posYendLevel = posYendLevel;
	}

	public double getPosYendLevel() {
		return posYendLevel;
	}

	public boolean addLifeEvent(LifeEvent lifeEvent) {
		return true;
	}

}
