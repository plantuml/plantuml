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
 * Revision $Revision: 6097 $
 *
 */
package net.sourceforge.plantuml.sequencediagram;

import java.util.List;

import net.sourceforge.plantuml.Url;

public class Reference implements Event {

	private final Participant p;
	private final Participant p2;
	private final Url url;

	private final List<String> strings;

	// public Reference(Participant p, List<String> strings) {
	// this.p = p;
	// this.p2 = null;
	// this.strings = strings;
	// }
	//
	public Reference(Participant p, Participant p2, Url url, List<String> strings) {
		this.p = p;
		this.p2 = p2;
		this.url = url;
		this.strings = strings;
	}

	public Participant getParticipant() {
		return p;
	}

	public Participant getParticipant2() {
		return p2;
	}

	public List<String> getStrings() {
		return strings;
	}

	public boolean dealWith(Participant someone) {
		return p == someone || p2 == someone;
	}

	public final Url getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return p.getCode() + "-" + p2.getCode();
	}
}
