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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.real.RealOrigin;
import net.sourceforge.plantuml.real.RealUtils;

/**
 * The shared 2D constraint plan: the central object of the diagram4 layout.
 * <p>
 * A RealPlan owns two independent constraint lines (one per logical axis,
 * see {@link PlanAxis}) and the registries of everything declared during the
 * planning phase: figures, connections, bands, scopes, corridors. It is the
 * 2D generalization of what teoz does in 1D for sequence diagrams.
 * <p>
 * The lifecycle is:
 * <ol>
 * <li><b>Planning</b>: the instruction tree is traversed once; each planner
 * declares figures, ports and monotone constraints into this plan. Nothing is
 * computed, everything is declared. The fixpoint of the solver does not
 * depend on the emission order (only the number of iterations does), so
 * planners never need to coordinate — this must remain an explicit, tested
 * invariant.</li>
 * <li><b>Solve</b>: {@link #compile()} runs the relaxation on both axes.
 * All constraints being of the form "X &gt;= Y + c" (plus RealMax/RealMin
 * aggregations) over an acyclic dependency graph, convergence is
 * guaranteed.</li>
 * <li><b>Cosmetic pass</b> (optional): the monotone solver packs everything
 * toward the minimum; recentering elements that have slack is a local,
 * post-solve computation with no divergence risk.</li>
 * <li><b>Routing and drawing</b>: connections are routed with absolute
 * coordinates, then figures and routes are drawn flat, in a single pass.</li>
 * </ol>
 */
public class RealPlan {

	private final RealOrigin flowOrigin = RealUtils.createOrigin();
	private final RealOrigin crossOrigin = RealUtils.createOrigin();

	private final FlowDirection direction;

	private final RealPoint origin;

	private final List<PlanFigure> figures = new ArrayList<>();
	private final List<PlanConnection> connections = new ArrayList<>();
	private final List<RealBand> bands = new ArrayList<>();
	private final List<RealScope> scopes = new ArrayList<>();
	private final List<RealCorridor> corridors = new ArrayList<>();

	public RealPlan(FlowDirection direction) {
		this.direction = direction;
		this.origin = new RealPoint("origin", flowOrigin, crossOrigin);
	}

	public final FlowDirection getDirection() {
		return direction;
	}

	public final RealPoint getOrigin() {
		return origin;
	}

	// ::comment
	// Factory methods: every variable is created through the plan, so that it
	// is registered on the right line and carries a debuggable name.
	// ::done

	/** Creates a free point (one new variable per axis). */
	public RealPoint createPoint(String name) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Creates a fixed-size figure position (one new variable per axis). */
	public RealRectangle createRectangle(String name, FlowDimension dimension) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Creates a band (two new variables) on the given axis. */
	public RealBand createBand(String name, PlanAxis axis) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Creates a scope, optionally nested in a parent scope. */
	public RealScope createScope(String name, RealScope parent) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Creates a routing corridor (one new variable) on the given axis. */
	public RealCorridor createCorridor(String name, PlanAxis axis, double thickness) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Registers a drawable figure for the flat drawing pass. */
	public void addFigure(PlanFigure figure) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Registers a connection for the post-solve routing pass. */
	public void addConnection(PlanConnection connection) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/**
	 * Solves both axes. After this call, every Real has its final value and
	 * the plan is frozen: no new constraint may be added.
	 */
	public void compile() {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/**
	 * Optional cosmetic pass, after the solve: recenters elements that have
	 * slack (the monotone solver packs toward the minimum). Local computation
	 * on frozen values, no divergence risk.
	 */
	public void beautify() {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Maps a solved logical point to screen coordinates. */
	public XPoint2D resolve(RealPoint point) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Overall solved dimension (from the absolute min/max of both lines). */
	public XDimension2D getSolvedDimension() {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/**
	 * Flat drawing: iterates figures then routed connections, in one pass, in
	 * one absolute coordinate system. No recursion, no interceptors, no
	 * per-swimlane passes.
	 */
	public void drawAll(UGraphic ug, ConnectionRouter router) {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

	/** Debug dump of the solved plan (names and values of all variables). */
	public String dump() {
		// TODO: to be implemented
		throw new UnsupportedOperationException();
	}

}
