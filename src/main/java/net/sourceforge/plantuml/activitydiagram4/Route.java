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

import java.util.List;

import net.sourceforge.plantuml.klimt.geom.XPoint2D;

/**
 * The result of routing a {@link PlanConnection}: an orthogonal polyline in
 * solved screen coordinates.
 * <p>
 * A Route is pure geometry; the actual rendering (arrow heads, rainbow
 * colors, label placement, merging of touching arrows) is delegated to the
 * drawing layer, which may reuse the diagram3 Snake/Worm machinery.
 */
public class Route {

	private final List<XPoint2D> points;

	public Route(List<XPoint2D> points) {
		this.points = points;
	}

	public final List<XPoint2D> getPoints() {
		return points;
	}

	@Override
	public String toString() {
		return points.toString();
	}

}
