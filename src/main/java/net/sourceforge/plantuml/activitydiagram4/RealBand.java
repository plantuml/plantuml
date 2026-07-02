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
 * An interval [min, max] on one logical axis, both bounds being solver
 * variables.
 * <p>
 * This is the native representation of a swimlane (a band on the CROSS axis),
 * but it is generic: a branch column of an if, or a fork branch, is also a
 * band. Bands make swimlanes first-class citizens of the layout: elements are
 * constrained <i>inside</i> their band during the solve, so lane widths
 * emerge from the resolution instead of being discovered by the diagram3
 * post-processing (LimitFinder passes, Swimlane.setTranslate, N+1 drawing
 * passes through interceptors).
 * <p>
 * All constraints emitted here are monotone ("X is at least Y plus a
 * constant"), which guarantees convergence of the solver. Bands must not push
 * their content: content pushes bounds, and bands push each other (through
 * {@link #mustFollow(RealBand, double)}), keeping the dependency graph
 * acyclic.
 */
public class RealBand {

	private final String name;
	private final PlanAxis axis;
	private final Real min;
	private final Real max;

	public RealBand(String name, PlanAxis axis, Real min, Real max) {
		this.name = name;
		this.axis = axis;
		this.min = min;
		this.max = max;
	}

	public final String getName() {
		return name;
	}

	public final PlanAxis getAxis() {
		return axis;
	}

	public final Real getMin() {
		return min;
	}

	public final Real getMax() {
		return max;
	}

	/**
	 * Emits the two monotone constraints keeping a fixed-size figure inside
	 * this band, with the given margin:
	 * <ul>
	 * <li>figure.min &gt;= band.min + margin</li>
	 * <li>band.max &gt;= figure.max + margin</li>
	 * </ul>
	 */
	public void mustContain(RealRectangle figure, double margin) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/**
	 * Same as {@link #mustContain(RealRectangle, double)} for a routing
	 * corridor: the corridor (and its label, if any) pushes the band bound.
	 * This is what makes back-arrow corridors visible to lane widths, which
	 * diagram3 structurally cannot do (cross-lane connections contribute to no
	 * LimitFinder).
	 */
	public void mustContain(RealCorridor corridor, double margin) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/**
	 * Emits the ordering constraint between two adjacent bands:
	 * this.min &gt;= previous.max + gap (typically the lane divider width).
	 */
	public void mustFollow(RealBand previous, double gap) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/**
	 * Ensures a minimum size for the band (used by "skinparam SwimlaneWidth"
	 * and, in a second solving phase, by "SwimlaneWidth same").
	 */
	public void ensureMinSize(double minSize) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Solved size. Only valid after {@link RealPlan#compile()}. */
	public double getSolvedSize() {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return name + " on " + axis;
	}

}
