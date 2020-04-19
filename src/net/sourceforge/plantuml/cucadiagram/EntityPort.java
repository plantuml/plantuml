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
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.svek.Ports;

public class EntityPort {

	private final String entityUid;
	private final String portId;

	public EntityPort(String entityUid, String portName) {
		this.entityUid = entityUid;
		this.portId = portName == null ? null : Ports.encodePortNameToId(portName);
	}

	public String getFullString() {
		if (portId != null) {
			return entityUid + ":" + portId;
		}
		return entityUid;
	}

	private boolean isShielded() {
		return entityUid.endsWith(":h");
	}

	public String getPrefix() {
		if (isShielded()) {
			return entityUid.substring(0, entityUid.length() - 2);
		}
		return entityUid;
	}

	public boolean startsWith(String centerId) {
		return entityUid.startsWith(centerId);
	}

	public boolean equalsId(EntityPort other) {
		return this.entityUid.equals(other.entityUid);
	}
}
