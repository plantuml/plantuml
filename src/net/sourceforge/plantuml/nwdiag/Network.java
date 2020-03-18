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
 */
package net.sourceforge.plantuml.nwdiag;

import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.ugraphic.color.HColor;

public class Network {

	private final String name;
	private final Map<DiagElement, String> localElements = new LinkedHashMap<DiagElement, String>();
	private HColor color;

	private String ownAdress;

	@Override
	public String toString() {
		return name;
	}

	public Network(String name) {
		this.name = name;
	}

	public String getAdress(DiagElement element) {
		return localElements.get(element);
	}

	public void addElement(DiagElement element, Map<String, String> props) {
		String address = props.get("address");
		if (address == null) {
			address = "";
		}
		if (address.length() == 0 && localElements.containsKey(element)) {
			return;
		}
		localElements.put(element, address);
	}

	public boolean constainsLocally(String name) {
		for (DiagElement element : localElements.keySet()) {
			if (element.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public final String getOwnAdress() {
		return ownAdress;
	}

	public final void setOwnAdress(String ownAdress) {
		this.ownAdress = ownAdress;
	}

	public final String getName() {
		return name;
	}

	public final HColor getColor() {
		return color;
	}

	public final void setColor(HColor color) {
		this.color = color;
	}

}
