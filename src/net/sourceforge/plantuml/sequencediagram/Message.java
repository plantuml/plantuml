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

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.style.StyleBuilder;

public final class Message extends AbstractMessage {

	final private Participant p1;
	final private Participant p2;

	public Message(StyleBuilder styleBuilder, Participant p1, Participant p2, Display label,
			ArrowConfiguration arrowConfiguration, String messageNumber) {
		super(styleBuilder, label, arrowConfiguration, messageNumber);
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public String toString() {
		return super.toString() + " " + p1 + "->" + p2 + " " + getLabel();
	}

	@Override
	public Participant getParticipant1() {
		return p1;
	}

	@Override
	public Participant getParticipant2() {
		return p2;
	}

	public boolean dealWith(Participant someone) {
		return someone == p1 || someone == p2;
	}

	@Override
	public boolean compatibleForCreate(Participant p) {
		return p1 != p && p2 == p;
	}

	public boolean isSelfMessage() {
		return p1 == p2;
	}

}
