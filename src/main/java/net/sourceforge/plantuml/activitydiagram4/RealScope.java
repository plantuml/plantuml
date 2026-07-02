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
 * The bounding box of a subtree, as <b>solver variables</b>.
 * <p>
 * This is the direct replacement of diagram3's FtileGeometry, with the
 * computation direction reversed: instead of each tile computing its
 * dimension bottom-up and translating its children top-down, the content
 * <i>pushes</i> the bounds of its enclosing scope (RealMax/RealMin
 * aggregations), and the bounds are read only after the solve.
 * <p>
 * Composition stays local: a planner registers its figures and child scopes
 * into its own scope, and never needs to know its size nor its children's
 * positions. Scopes nest: a child scope pushes its parent's bounds.
 * <p>
 * Scopes are also what makes conservative routing possible: a
 * {@link RealCorridor} clears a whole scope without opening the composition
 * (see {@link RealCorridor#mustClear(RealScope, double)}). For finer needs
 * (e.g. the backward activity that must clear only the body elements living
 * in its own lane), a scope can expose its extent restricted to a band, which
 * is again a RealMax over a subset of the registered figures.
 */
public class RealScope {

	private final String name;
	private final RealScope parent;

	public RealScope(String name, RealScope parent) {
		this.name = name;
		this.parent = parent;
	}

	public final String getName() {
		return name;
	}

	public final RealScope getParent() {
		return parent;
	}

	/**
	 * Registers a figure: its edges push the bounds of this scope (and,
	 * transitively, of all enclosing scopes).
	 */
	public void register(RealRectangle figure) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Registers a child scope: its bounds push the bounds of this scope. */
	public void register(RealScope child) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/**
	 * Registers a corridor: its reserved thickness pushes the bounds of this
	 * scope, so that the space taken by a routed arrow (and its label) is
	 * accounted for during the solve, not discovered at drawing time.
	 */
	public void register(RealCorridor corridor) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Aggregated bound (RealMin over the content). */
	public Real getFlowMin() {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Aggregated bound (RealMax over the content). */
	public Real getFlowMax() {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Aggregated bound (RealMin over the content). */
	public Real getCrossMin() {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Aggregated bound (RealMax over the content). */
	public Real getCrossMax() {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/**
	 * Aggregated CROSS bound restricted to the figures of this scope that live
	 * inside the given band. Needed for fine corridor placement (e.g. a
	 * backward activity clearing only the body elements of its own lane).
	 */
	public Real getCrossMaxInside(RealBand band) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return name;
	}

}
