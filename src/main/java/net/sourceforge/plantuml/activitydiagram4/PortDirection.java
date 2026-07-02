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

/**
 * The side of a figure on which a {@link PlanPort} is attached, in logical
 * (orientation-neutral) terms.
 * <p>
 * This is the counterpart of gtile's GPoint hooks (NORTH_HOOK, SOUTH_HOOK,
 * WEST_HOOK, EAST_HOOK), reworded on the FLOW/CROSS axes: in a top-to-bottom
 * diagram FLOW_IN maps to north, CROSS_MAX to east; in a left-to-right
 * diagram FLOW_IN maps to west, CROSS_MAX to south.
 */
public enum PortDirection {

	/** Where the flow enters the figure (north in TOP_TO_BOTTOM mode). */
	FLOW_IN,

	/** Where the flow leaves the figure (south in TOP_TO_BOTTOM mode). */
	FLOW_OUT,

	/** Lateral side, minimum of the CROSS axis (west in TOP_TO_BOTTOM mode). */
	CROSS_MIN,

	/** Lateral side, maximum of the CROSS axis (east in TOP_TO_BOTTOM mode). */
	CROSS_MAX;

}
