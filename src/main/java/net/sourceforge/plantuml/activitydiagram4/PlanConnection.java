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

import net.sourceforge.plantuml.decoration.Rainbow;
import net.sourceforge.plantuml.klimt.shape.TextBlock;

/**
 * A first-class connection between two ports, declared during the planning
 * phase and routed only after the plan is solved.
 * <p>
 * If the connection needs a lateral detour (loop-back arrows, cross-lane
 * jumps), it owns a {@link RealCorridor} that reserves the necessary space
 * during the solve — including the space of its label, whose size is known
 * upfront.
 * <p>
 * Contrast with diagram3, where a single loop-back arrow needs five
 * Connection classes (ConnectionBackSimple1/2, ConnectionBackComplex1,
 * ConnectionBackBackward1/2), chosen at construction time from the swimlane
 * <i>order</i> used as a proxy for a geometry that does not exist yet. Here
 * the routing decisions (exit side, elbow positions) are plain arithmetic on
 * solved coordinates, performed once by the {@link ConnectionRouter}.
 */
public class PlanConnection {

	private final PlanPort from;
	private final PlanPort to;

	/** Optional label, measured upfront. May be null. */
	private final TextBlock label;

	private final Rainbow color;

	/** Optional reserved routing channel. May be null for straight arrows. */
	private final RealCorridor corridor;

	public PlanConnection(PlanPort from, PlanPort to, TextBlock label, Rainbow color, RealCorridor corridor) {
		this.from = from;
		this.to = to;
		this.label = label;
		this.color = color;
		this.corridor = corridor;
	}

	public final PlanPort getFrom() {
		return from;
	}

	public final PlanPort getTo() {
		return to;
	}

	public final TextBlock getLabel() {
		return label;
	}

	public final Rainbow getColor() {
		return color;
	}

	public final RealCorridor getCorridor() {
		return corridor;
	}

	@Override
	public String toString() {
		return from + " -> " + to;
	}

}
