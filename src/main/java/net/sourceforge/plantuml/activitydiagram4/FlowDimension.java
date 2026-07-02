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

import net.sourceforge.plantuml.klimt.geom.XDimension2D;

/**
 * A dimension expressed on the logical axes: extent along FLOW and extent
 * along CROSS.
 * <p>
 * This is the projection adapter between the pixel world (text is measured by
 * the StringBounder as width x height, and cannot be rotated) and the logical
 * world of the plan. All sizes are known <i>before</i> the layout is solved:
 * only positions are solver variables. Sizes are therefore plain immutable
 * doubles.
 */
public class FlowDimension {

	private final double alongFlow;
	private final double acrossFlow;

	public FlowDimension(double alongFlow, double acrossFlow) {
		this.alongFlow = alongFlow;
		this.acrossFlow = acrossFlow;
	}

	/**
	 * Projects a screen dimension onto the logical axes, according to the
	 * diagram orientation.
	 */
	public static FlowDimension of(XDimension2D dim, FlowDirection direction) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	public final double getAlongFlow() {
		return alongFlow;
	}

	public final double getAcrossFlow() {
		return acrossFlow;
	}

	@Override
	public String toString() {
		return "[flow=" + alongFlow + " cross=" + acrossFlow + "]";
	}

}
