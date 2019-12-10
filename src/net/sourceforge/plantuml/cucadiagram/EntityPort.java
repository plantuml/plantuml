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

public class EntityPort {

	private final String uid;
	private final String portName;

	public EntityPort(String uid, String portName) {
		this.uid = uid;
		this.portName = portName;
	}

	public String getFullString() {
		if (portName != null) {
			return uid + ":" + portName;
		}
		return uid;
	}

	private boolean isShielded() {
		return uid.endsWith(":h");
	}

	public String getPrefix() {
		if (isShielded()) {
			return uid.substring(0, uid.length() - 2);
		}
		return uid;
	}

	public boolean startsWith(String centerId) {
		return uid.startsWith(centerId);
	}

	public boolean equalsId(EntityPort other) {
		return this.uid.equals(other.uid);
	}

	// private String getPortName() {
	// return portName;
	// }

}
