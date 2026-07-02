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
 * A routing channel: the position, on one axis, of the long segment of an
 * orthogonal arrow (typically the vertical segment of a loop-back arrow),
 * together with the thickness it reserves (arrow clearance plus label width).
 * <p>
 * Corridors are first-class citizens of the solve. This fixes, by
 * construction, two structural problems of diagram3:
 * <ul>
 * <li>the corridor pushes its enclosing scope and band, so lane widths
 * account for the routed arrows — whereas in diagram3 a cross-lane connection
 * contributes to no LimitFinder and its corridor is invisible to the width
 * computation;</li>
 * <li>the space needed by an arrow label is a plain constraint — replacing
 * the FtileMargedRight / getMaxX post-fixups.</li>
 * </ul>
 * The routing policy is deliberately <b>conservative</b>: a corridor clears a
 * whole scope ({@link #mustClear(RealScope, double)}) rather than finding the
 * shortest path. This keeps every constraint monotone and the solve
 * deterministic; fine collision avoidance stays a post-solve concern
 * (CollisionDetector).
 */
public class RealCorridor {

	private final String name;

	/** The axis on which the corridor position lives. */
	private final PlanAxis axis;

	/** Position of the corridor on {@link #axis}. */
	private final Real position;

	/** Reserved thickness: arrow clearance + label extent, known upfront. */
	private final double thickness;

	public RealCorridor(String name, PlanAxis axis, Real position, double thickness) {
		this.name = name;
		this.axis = axis;
		this.position = position;
		this.thickness = thickness;
	}

	public final String getName() {
		return name;
	}

	public final PlanAxis getAxis() {
		return axis;
	}

	public final Real getPosition() {
		return position;
	}

	public final double getThickness() {
		return thickness;
	}

	/**
	 * Emits the monotone constraint placing this corridor beyond the given
	 * scope: position &gt;= scope.max (on this axis) + margin.
	 */
	public void mustClear(RealScope scope, double margin) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/**
	 * Emits the monotone constraint placing this corridor beyond a single
	 * figure: position &gt;= figure.max (on this axis) + margin.
	 */
	public void mustClear(RealRectangle figure, double margin) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return name + " on " + axis;
	}

}
