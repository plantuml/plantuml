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
 * The single, post-solve, orthogonal router.
 * <p>
 * Runs only after {@link RealPlan#compile()}: every port and every corridor
 * has absolute coordinates, so decisions that diagram3 makes at construction
 * time with heuristics (which side of the diamond the loop-back arrow exits,
 * where the elbow goes) become trivial arithmetic on solved values.
 * <p>
 * There is exactly one routing algorithm for a given connection shape,
 * whether swimlanes are present or not — this replaces the systematic
 * drawU/drawTranslate duality of diagram3 connections, where the same arrow
 * was routed differently in mono-lane and multi-lane modes.
 */
public interface ConnectionRouter {

	/**
	 * Computes the orthogonal path of the given connection, honoring its
	 * corridor if any. Only valid after the plan has been compiled.
	 */
	Route route(PlanConnection connection);

}
