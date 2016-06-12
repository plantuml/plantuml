/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.sequencediagram;

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.skin.ArrowConfiguration;

public class MessageExo extends AbstractMessage {

	final private MessageExoType type;
	final private Participant participant;
	final private boolean shortArrow;

	public MessageExo(Participant p, MessageExoType type, Display label, ArrowConfiguration arrowConfiguration,
			String messageNumber, boolean shortArrow) {
		super(label, arrowConfiguration, messageNumber);
		this.participant = p;
		this.type = type;
		this.shortArrow = shortArrow;
	}
	
	public boolean isShortArrow() {
		return shortArrow;
	}


	@Override
	protected NotePosition overideNotePosition(NotePosition notePosition) {
		if (type == MessageExoType.FROM_LEFT || type == MessageExoType.TO_LEFT) {
			return NotePosition.RIGHT;
		}
		if (type == MessageExoType.FROM_RIGHT || type == MessageExoType.TO_RIGHT) {
			return NotePosition.LEFT;
		}
		throw new IllegalStateException();
	}

	public Participant getParticipant() {
		return participant;
	}

	public final MessageExoType getType() {
		return type;
	}

	public boolean dealWith(Participant someone) {
		return participant == someone;
	}

	@Override
	public boolean compatibleForCreate(Participant p) {
		return p == participant;
	}
	
	public boolean isSelfMessage() {
		return false;
	}



}
