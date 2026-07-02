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

import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.creole.Display;

/**
 * Builds {@link PlannedTile}s from the instruction tree: the diagram4
 * counterpart of FtileFactory, so that the whole activitydiagram3 command
 * and Instruction layer can be reused as-is (a future
 * Instruction.createPlanned(...) will mirror Instruction.createFtile(...)).
 * <p>
 * The signatures below are deliberately simplified sketches; they will grow
 * to mirror the FtileFactory ones (link renderings, stereotypes, styles,
 * notes, urls...). Two structural differences are already settled:
 * <ul>
 * <li>every method takes the {@link PlanContext}, because planning nested
 * constructs means deriving contexts (new spine for a branch, new scope for
 * a body, lane switches) — where FtileFactory relied on each Ftile carrying
 * its swimlane;</li>
 * <li>there is no decorateIn/decorateOut: link renderings attach to the
 * connections created at assembly time, not to the tiles.</li>
 * </ul>
 * Whether the delegator-chain pattern of diagram3 (one delegator per
 * construct) is kept or replaced by one planner class per construct is left
 * open; the chain mainly compensated for the factory being built before the
 * traversal, which is no longer required.
 */
public interface PlannerFactory {

	PlannedTile start(PlanContext context, Colors colors);

	PlannedTile stop(PlanContext context, Colors colors);

	PlannedTile end(PlanContext context, Colors colors);

	PlannedTile activity(PlanContext context, Display label, Colors colors);

	/**
	 * Composition: connects tile1.portOut to tile2.portIn with a minimum-gap
	 * constraint on the FLOW axis (arrow clearance + label extent) and
	 * declares the connection. Nothing else — compare with
	 * FtileAssemblySimple and FtileFactoryDelegatorAssembly.
	 */
	PlannedTile assembly(PlanContext context, PlannedTile tile1, PlannedTile tile2);

	/**
	 * Loop with test at the bottom. The loop-back arrow gets a corridor
	 * clearing the body scope; the optional backward tile sits on that
	 * corridor.
	 */
	PlannedTile repeat(PlanContext context, PlannedTile body, PlannedTile backward, Display test, Display yes,
			Display out);

	/** Loop with test at the top. Same corridor mechanism as repeat. */
	PlannedTile createWhile(PlanContext context, PlannedTile body, PlannedTile backward, Display test, Display yes);

	/**
	 * Conditional. Each branch was planned in a derived context (its own
	 * spine, possibly its own band); this method connects the diamonds to the
	 * branch ports.
	 */
	PlannedTile createIf(PlanContext context, List<PlannedTile> branches, List<Display> tests);

	/**
	 * Fork/split. The synchronization bars are the first figures whose extent
	 * on the CROSS axis is itself variable (they must span the branches):
	 * they are bounded by the branch scopes, not by a fixed FlowDimension.
	 */
	PlannedTile createParallel(PlanContext context, List<PlannedTile> branches);

	/** Partition/group frame around a subtree scope. */
	PlannedTile createGroup(PlanContext context, PlannedTile content, Display title);

}
