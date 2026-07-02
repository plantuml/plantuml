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
package net.sourceforge.plantuml.activitydiagram4;

/**
 * A named connection point exposed by a planned tile: a {@link RealPoint}
 * plus the side it is attached to.
 * <p>
 * Ports are the composition interface of diagram4: assembling two tiles means
 * connecting the FLOW_OUT port of the first to the FLOW_IN port of the second
 * with a minimum-gap constraint — nothing else. No dimension merging, no
 * translation caching (compare with FtileAssemblySimple).
 * <p>
 * Since the port position is made of {@link net.sourceforge.plantuml.real.Real}
 * variables, its solved coordinates are absolute: a cross-lane connection is
 * a connection like any other, which removes the whole
 * drawU/drawTranslate duality and the ConnectionCross machinery of diagram3.
 */
public class PlanPort {

	private final String name;
	private final RealPoint position;
	private final PortDirection direction;

	public PlanPort(String name, RealPoint position, PortDirection direction) {
		this.name = name;
		this.position = position;
		this.direction = direction;
	}

	public final String getName() {
		return name;
	}

	public final RealPoint getPosition() {
		return position;
	}

	public final PortDirection getDirection() {
		return direction;
	}

	@Override
	public String toString() {
		return name + " (" + direction + ")";
	}

}
