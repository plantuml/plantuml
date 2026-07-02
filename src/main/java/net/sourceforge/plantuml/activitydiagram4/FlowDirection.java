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

import net.sourceforge.plantuml.klimt.geom.XPoint2D;

/**
 * Orientation of the whole diagram: decides how the logical (FLOW, CROSS)
 * coordinates of a solved {@link RealPlan} are mapped to screen (x, y)
 * coordinates.
 * <p>
 * All planners emit constraints in (FLOW, CROSS) terms and never look at this
 * enum for positioning; only the final drawing pass and some intrinsically
 * oriented skins (hexagon branch sides, yes/no label placement...) do.
 */
public enum FlowDirection {

	/** Classic activity diagram: flow goes downward, swimlanes are columns. */
	TOP_TO_BOTTOM,

	/** Transposed diagram: flow goes rightward, swimlanes are rows. */
	LEFT_TO_RIGHT;

	/**
	 * Maps a solved logical coordinate pair to screen coordinates.
	 *
	 * @param flow  solved position on the FLOW axis
	 * @param cross solved position on the CROSS axis
	 * @return the corresponding screen point
	 */
	public XPoint2D toScreen(double flow, double cross) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/**
	 * Extent of a (width x height) box along the FLOW axis: height in
	 * TOP_TO_BOTTOM mode, width in LEFT_TO_RIGHT mode.
	 */
	public double alongFlow(double width, double height) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/**
	 * Extent of a (width x height) box along the CROSS axis: width in
	 * TOP_TO_BOTTOM mode, height in LEFT_TO_RIGHT mode.
	 */
	public double acrossFlow(double width, double height) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

}
