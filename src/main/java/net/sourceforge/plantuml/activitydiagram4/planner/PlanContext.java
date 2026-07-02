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

import net.sourceforge.plantuml.activitydiagram4.RealPlan;
import net.sourceforge.plantuml.activitydiagram4.RealScope;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.style.ISkinParam;

/**
 * Everything a planner needs to declare its part of the layout: the shared
 * plan, the current lane, the enclosing scope, and the <b>spine</b> — the
 * shared CROSS-axis variable of the current alignment context.
 * <p>
 * The spine is the central alignment mechanism of diagram4 (borrowed from
 * teoz lifelines): two consecutive figures are vertically aligned because
 * they center on the <i>same</i> Real instance, not because a force pulls
 * them together. Alignment by identity is cheaper (fewer variables, fewer
 * forces), cannot diverge, and is compatible with the monotone solver, where
 * a bidirectional equality constraint would create a cycle.
 * <p>
 * Contexts are immutable; nested constructs derive child contexts (a branch
 * of an if gets a new spine, the content of a partition gets a new scope,
 * a lane switch changes the current lane).
 */
public class PlanContext {

	private final RealPlan plan;
	private final Lane lane;
	private final RealScope scope;

	/** Shared CROSS-axis alignment variable of the current context. */
	private final Real spine;

	private final ISkinParam skinParam;
	private final StringBounder stringBounder;

	public PlanContext(RealPlan plan, Lane lane, RealScope scope, Real spine, ISkinParam skinParam,
			StringBounder stringBounder) {
		this.plan = plan;
		this.lane = lane;
		this.scope = scope;
		this.spine = spine;
		this.skinParam = skinParam;
		this.stringBounder = stringBounder;
	}

	public final RealPlan getPlan() {
		return plan;
	}

	/** Current lane. May be null when the diagram has no swimlane. */
	public final Lane getLane() {
		return lane;
	}

	public final RealScope getScope() {
		return scope;
	}

	public final Real getSpine() {
		return spine;
	}

	public final ISkinParam getSkinParam() {
		return skinParam;
	}

	public final StringBounder getStringBounder() {
		return stringBounder;
	}

	/** Derived context for content living in another lane. */
	public PlanContext withLane(Lane newLane) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Derived context with a new alignment spine (e.g. an if branch). */
	public PlanContext withNewSpine(String name) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Derived context with a new nested scope (e.g. a loop body). */
	public PlanContext withNewScope(String name) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

}
