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

import net.sourceforge.plantuml.klimt.drawing.UGraphic;

/**
 * A leaf drawable registered in the plan: an activity box, a diamond, a
 * circle, a lane divider, a group frame...
 * <p>
 * Once the plan is solved, drawing is <b>flat</b>: the plan iterates its
 * figures and routed connections directly, in a single pass, in one absolute
 * coordinate system. The instruction tree is only used at planning time; it
 * has no authority over drawing. This removes diagram3's recursive drawU,
 * the per-swimlane interceptor passes, and the Genealogy translation
 * reconstruction.
 */
public interface PlanFigure {

	/**
	 * The rectangle (fixed size, variable position) this figure occupies in
	 * the plan.
	 */
	RealRectangle getRectangle();

	/**
	 * Draws the figure at its solved position. Only valid after
	 * {@link RealPlan#compile()}; coordinates are read from the solved
	 * variables, so no UTranslate is applied by the caller.
	 */
	void drawSolved(UGraphic ug, FlowDirection direction);

}
