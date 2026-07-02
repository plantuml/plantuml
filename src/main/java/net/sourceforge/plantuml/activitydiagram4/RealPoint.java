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
import net.sourceforge.plantuml.real.Real;

/**
 * A 2D point whose coordinates are solver variables, one {@link Real} per
 * logical axis.
 * <p>
 * Two points are aligned when they <i>share</i> the same {@link Real}
 * instance on an axis: alignment by identity of variables, not by forces.
 * This is the mechanism (borrowed from teoz lifelines) that replaces
 * diagram3's spine bookkeeping (FtileGeometry.left and the
 * getLeft()/getRight() gymnastics).
 * <p>
 * The name is used for debugging: when a constraint misbehaves, the symptom
 * may appear far away from the cause, so every variable should be traceable
 * to its construction path (e.g. "repeat_1/diamond2/crossCenter").
 */
public class RealPoint {

	private final String name;
	private final Real flow;
	private final Real cross;

	public RealPoint(String name, Real flow, Real cross) {
		this.name = name;
		this.flow = flow;
		this.cross = cross;
	}

	public final Real getFlow() {
		return flow;
	}

	public final Real getCross() {
		return cross;
	}

	public final String getName() {
		return name;
	}

	/**
	 * A derived point, shifted by a fixed offset along the FLOW axis. Shares
	 * the CROSS variable of this point.
	 */
	public RealPoint moveFlow(double delta) {
		// TODO: to be implemented (Real.addFixed)
		throw new UnsupportedOperationException();
	}

	/**
	 * A derived point, shifted by a fixed offset along the CROSS axis. Shares
	 * the FLOW variable of this point.
	 */
	public RealPoint moveCross(double delta) {
		// TODO: to be implemented (Real.addFixed)
		throw new UnsupportedOperationException();
	}

	/**
	 * Solved screen coordinates. Only valid after {@link RealPlan#compile()}.
	 */
	public XPoint2D getSolved(FlowDirection direction) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return name;
	}

}
