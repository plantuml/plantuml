/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 4836 $
 *
 */
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.List;

public class ParticipantEngloberContexted {

	final private ParticipantEnglober participantEnglober;
	final private List<Participant> participants = new ArrayList<Participant>();

	public ParticipantEngloberContexted(ParticipantEnglober participantEnglober, Participant first) {
		this.participantEnglober = participantEnglober;
		this.participants.add(first);
	}

	public final ParticipantEnglober getParticipantEnglober() {
		return participantEnglober;
	}
	
	public boolean contains(Participant p) {
		return participants.contains(p);
	}

	public void add(Participant p) {
		if (participants.contains(p)) {
			throw new IllegalArgumentException();
		}
		participants.add(p);
	}

	public final Participant getFirst2() {
		return participants.get(0);
	}

	public final Participant getLast2() {
		return participants.get(participants.size() - 1);
	}
	
	@Override
	public String toString() {
		return super.toString()+" "+participants;
	}

}
