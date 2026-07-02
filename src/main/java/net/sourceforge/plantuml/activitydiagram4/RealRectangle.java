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

import net.sourceforge.plantuml.real.Real;

/**
 * A <b>fixed-size</b> rectangle whose position is a solver variable.
 * <p>
 * This is the workhorse for leaf figures (activity boxes, diamonds,
 * circles...): their size is known upfront (text is measured before the
 * layout), so only one {@link Real} per axis is needed. This halves the number
 * of variables compared to a two-points representation.
 * <p>
 * Containers whose extent depends on their content (groups, partitions, the
 * body of a loop...) must NOT use this class: they are represented by a
 * {@link RealScope}, whose bounds are aggregate variables pushed outward by
 * the content.
 * <p>
 * All derived coordinates (max edge, center) are themselves {@link Real}s
 * (fixed offsets or middles of the position variables), so they can be used
 * in further constraints or shared for alignment.
 */
public class RealRectangle {

	private final String name;
	private final FlowDimension dimension;

	/** Position of the minimum corner on the FLOW axis. */
	private final Real flowMin;

	/** Position of the minimum corner on the CROSS axis. */
	private final Real crossMin;

	public RealRectangle(String name, FlowDimension dimension, Real flowMin, Real crossMin) {
		this.name = name;
		this.dimension = dimension;
		this.flowMin = flowMin;
		this.crossMin = crossMin;
	}

	public final String getName() {
		return name;
	}

	public final FlowDimension getDimension() {
		return dimension;
	}

	public final Real getFlowMin() {
		return flowMin;
	}

	public final Real getCrossMin() {
		return crossMin;
	}

	/** Derived: flowMin + extent along flow (fixed offset). */
	public Real getFlowMax() {
		// TODO: to be implemented (Real.addFixed)
		throw new UnsupportedOperationException();
	}

	/** Derived: crossMin + extent across flow (fixed offset). */
	public Real getCrossMax() {
		// TODO: to be implemented (Real.addFixed)
		throw new UnsupportedOperationException();
	}

	/**
	 * Derived: center on the CROSS axis. This is typically the variable that a
	 * figure <i>shares</i> with the spine of its context, so that consecutive
	 * figures are aligned by construction.
	 */
	public Real getCrossCenter() {
		// TODO: to be implemented (Real.addFixed of half the extent)
		throw new UnsupportedOperationException();
	}

	/**
	 * Anchor point on one side of the rectangle, in logical terms. Used to
	 * build {@link PlanPort}s.
	 */
	public RealPoint getAnchor(PortDirection direction) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return name + " " + dimension;
	}

}
