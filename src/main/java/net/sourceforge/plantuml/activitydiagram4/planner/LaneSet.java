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
package net.sourceforge.plantuml.activitydiagram4.planner;

import java.util.List;

import net.sourceforge.plantuml.activitydiagram4.RealPlan;
import net.sourceforge.plantuml.klimt.font.StringBounder;

/**
 * The ordered collection of lanes of a diagram, replacing diagram3's
 * Swimlanes post-processing (computeDrawingWidths, the N+1 drawing passes
 * through UGraphicInterceptorOneSwimlane, the Cross interceptor, the
 * trailing special lane).
 * <p>
 * Its responsibilities shrink to the planning phase:
 * <ul>
 * <li>create one {@link Lane} (hence one band) per declared swimlane;</li>
 * <li>emit the ordering constraints between adjacent bands (divider
 * width);</li>
 * <li>emit the minimum-size constraints derived from lane titles (whose
 * dimensions are measured upfront) and from skinparam SwimlaneWidth;</li>
 * <li>register the drawable figures of the lane decoration (dividers,
 * backgrounds, title strip).</li>
 * </ul>
 * "SwimlaneWidth same" needs a second solving phase: solve, measure the
 * widest lane, add minimum-size constraints, re-solve — which is safe since
 * all constraints are monotone (the plan can only grow).
 */
public class LaneSet {

	private final RealPlan plan;

	public LaneSet(RealPlan plan) {
		this.plan = plan;
	}

	/**
	 * Returns the lane with the given name, creating it (and its band) if
	 * needed. Lane order is the creation order.
	 */
	public Lane getOrCreate(String name) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** All lanes, in declaration order. */
	public List<Lane> getLanes() {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/**
	 * Emits the ordering constraints between adjacent bands and the
	 * minimum-size constraints (titles, skinparam). Called once, after all
	 * lanes are known and before the solve.
	 */
	public void emitConstraints(StringBounder stringBounder, double dividerThickness) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/**
	 * Registers the drawable decoration of the lanes (dividers, backgrounds,
	 * titles) into the plan, as ordinary figures of the flat drawing pass.
	 */
	public void registerDecoration() {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

}
