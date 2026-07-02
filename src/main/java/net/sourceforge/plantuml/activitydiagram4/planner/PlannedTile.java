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

import java.util.Map;

import net.sourceforge.plantuml.activitydiagram4.PlanPort;
import net.sourceforge.plantuml.activitydiagram4.RealScope;

/**
 * The value returned by planning a subtree: its composition interface.
 * <p>
 * This is the diagram4 counterpart of Ftile, deliberately reduced to almost
 * nothing: some ports and a scope. It is intentionally <b>not</b> a geometry
 * authority — no calculateDimension, no getTranslateFor, no drawU, no
 * swimlane set to propagate upward. All the truth lives in the shared
 * {@link net.sourceforge.plantuml.activitydiagram4.RealPlan}; the tile only
 * tells its assembler where to plug the arrows.
 * <p>
 * The tile does not survive the planning phase in any meaningful way: once
 * the plan is compiled, drawing is flat and the tile tree is irrelevant.
 * Keeping it this light is a design guard against silently rebuilding a
 * geometry-owning tree.
 */
public class PlannedTile {

	/** Where the flow enters this subtree. */
	private final PlanPort portIn;

	/**
	 * Where the flow leaves this subtree. Null when the subtree kills the
	 * flow (stop, end, detach) — the diagram4 counterpart of
	 * FtileGeometry.hasPointOut() == false.
	 */
	private final PlanPort portOut;

	/** Extra named ports (loop-back hooks, condition sides...). */
	private final Map<String, PlanPort> namedPorts;

	/** The scope this subtree registered its content into. */
	private final RealScope scope;

	public PlannedTile(PlanPort portIn, PlanPort portOut, Map<String, PlanPort> namedPorts, RealScope scope) {
		this.portIn = portIn;
		this.portOut = portOut;
		this.namedPorts = namedPorts;
		this.scope = scope;
	}

	public final PlanPort getPortIn() {
		return portIn;
	}

	public final PlanPort getPortOut() {
		return portOut;
	}

	public final boolean hasPortOut() {
		return portOut != null;
	}

	public final RealScope getScope() {
		return scope;
	}

	/** Extra port by name; null if absent. */
	public PlanPort getPort(String name) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

}
