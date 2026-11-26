/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
 */
package net.sourceforge.plantuml.sdot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;
import net.sourceforge.plantuml.sdot.OrthogonalPath.Direction;
import net.sourceforge.plantuml.sdot.OrthogonalPath.Endpoint;

/**
 * Chooses where the edges attach along the sides of rectangular nodes, for all
 * edges together and before any of them is routed.
 * <p>
 * The layout aims its splines at the node centers. Kept as they are, these
 * attachments turn into short jogs in front of a node and put several
 * arrowheads on one point. Here, each attachment moves along the side the
 * layout chose, to where its route can run straight or turn only once. Edges
 * that share a side keep the order of their other ends, so they leave it
 * without crossing, and a minimum distance between them.
 */
final class OrthogonalPorts {
	private static final double SPACING = 12;
	// Where an attachment would rather not be: near a corner, where the first
	// bend would hug the node. Where it must not be is closer than half its
	// decoration. A straight line or a crowded side may use the room between.
	// Both count from where a rounded or cut corner ends.
	private static final double CORNER_MARGIN = 12;
	// Two ends facing each other converge on a common line over a few rounds.
	private static final int ROUNDS = 3;
	// Ends of a straight line hardly move to make room for the others on their side.
	private static final double STRAIGHT_WEIGHT = 1000;
	// Steps just past the edge of a stretch that blocks a run.
	private static final double EPSILON = 0.01;
	// The distance a run would rather keep from nodes and titles. Where there
	// is no room for that, it may come as close as the router allows.
	private static final double CLEARANCE = 8;

	private final List<RectangleArea> obstacles;
	private final List<RectangleArea> clusters;
	private final List<End> ends = new ArrayList<>();
	private int edges;
	// RectangleArea compares by identity: one entry per node, in the order of the edges.
	private final Map<RectangleArea, Map<Direction, List<End>>> sides = new LinkedHashMap<>();
	// Ends that the router moved to a side next to theirs, so that later moves keep clear of them.
	private final Map<RectangleArea, Map<Direction, List<XPoint2D>>> movedAway = new LinkedHashMap<>();

	/**
	 * The obstacles are the nodes and cluster titles, as for the router. A
	 * cluster is in the way of the edges that neither start nor end inside it.
	 */
	OrthogonalPorts(List<RectangleArea> obstacles, List<RectangleArea> clusters) {
		this.obstacles = obstacles;
		this.clusters = clusters;
	}

	/** Registers an edge. Its attachments are final once {@link #assign} ran. */
	Attachment add(DotPath spline, Endpoint tail, Endpoint head) {
		final int edge = edges++;
		final End start = new End(edge, spline.getStartPoint(), spline.getStartAngle(), tail);
		final End end = new End(edge, spline.getEndPoint(), spline.getEndAngle() + Math.PI, head);
		start.other = end;
		end.other = start;
		// A self loop leaves and enters the same node, often on one side.
		if (tail.area() != head.area()) {
			register(start);
			register(end);
		}
		return new Attachment(this, start, end);
	}

	private void register(End end) {
		if (end.slides == false)
			return;
		ends.add(end);
		sides.computeIfAbsent(end.area, area -> new EnumMap<>(Direction.class))
				.computeIfAbsent(end.side, side -> new ArrayList<>()).add(end);
	}

	/**
	 * Places the attachments. Each round checks the run of every end against
	 * every obstacle, which the diagram pays from its routing budget. When a
	 * round would take more than a small share of what is left, the
	 * attachments stay where the previous round, or the layout, put them.
	 */
	void assign(OrthogonalPath.SearchBudget budget) {
		for (int round = 0; round < ROUNDS; round++) {
			if (budget.spendBeforeRouting(4L * ends.size() * (obstacles.size() + clusters.size())) == false)
				return;
			for (End end : ends)
				aim(end);
			for (Map<Direction, List<End>> bySide : sides.values())
				for (List<End> side : bySide.values())
					spread(side);
		}
	}

	// Where an end would attach if it had its side to itself: in line with the
	// other end, or, if they face each other, on a line through the middle of
	// what both sides share. The run from there towards the other end must be
	// clear; if it is not, the layout position, which the spline led around
	// the nodes, stays the better guess.
	private void aim(End end) {
		final End other = end.other;
		final double target = end.along(other.point);
		// Spreading moves the other ends during the round; ordering uses where they were.
		end.target = target;
		end.outward = end.side.progress(end.point, other.point);
		end.straight = false;
		if (other.side == end.side.opposite() && end.side.progress(end.point, other.point) > 0) {
			final double from = Math.max(end.low(true), other.slides ? other.low(true) : target);
			final double to = Math.min(end.high(true), other.slides ? other.high(true) : target);
			if (from <= to) {
				// The preferred margins keep the line off the corners, if there is room.
				final double preferredFrom = Math.max(end.low(false), other.slides ? other.low(false) : target);
				final double preferredTo = Math.min(end.high(false), other.slides ? other.high(false) : target);
				final double line = preferredFrom <= preferredTo ? (preferredFrom + preferredTo) / 2 : (from + to) / 2;
				// A straight line is worth running close by.
				end.straight = isClear(end, line, OrthogonalPath.MIN_NODE_GAP);
				end.ideal = end.straight ? line : end.clamp(end.along(end.original));
				return;
			}
		}
		final double aligned = end.clamp(target);
		double ideal = nearestClear(end, aligned, CLEARANCE);
		if (Double.isNaN(ideal))
			ideal = nearestClear(end, aligned, OrthogonalPath.MIN_NODE_GAP);
		end.ideal = Double.isNaN(ideal) ? end.clamp(end.along(end.original)) : ideal;
	}

	// The position closest to the wanted one whose run keeps the clearance:
	// the wanted one itself, or one just past what blocks the run there. NaN
	// if neither is clear.
	private double nearestClear(End end, double wanted, double clearance) {
		final List<double[]> blockers = blockers(end, wanted, clearance);
		if (blockers.isEmpty())
			return wanted;
		double best = Double.NaN;
		for (double[] stretch : blockers)
			for (double candidate : new double[] { stretch[0] - EPSILON, stretch[1] + EPSILON })
				if (candidate >= end.low(false) && candidate <= end.high(false) && isClear(end, candidate, clearance)
						&& (Double.isNaN(best) || Math.abs(candidate - wanted) < Math.abs(best - wanted)))
					best = candidate;
		return best;
	}

	// Whether the run that leaves the side at this position reaches the line
	// of the other end, keeping the clearance from nodes and foreign clusters.
	private boolean isClear(End end, double position, double clearance) {
		return blockers(end, position, clearance).isEmpty();
	}

	// What the run that leaves the side at this position runs into, as the
	// stretches of the side it rules out. The run stops short of the line of
	// the other end, where the neighbors of the other end begin.
	private List<double[]> blockers(End end, double position, double clearance) {
		final List<double[]> result = new ArrayList<>();
		final XPoint2D from = end.at(position);
		final double distance = end.side.progress(from, end.other.point) - 2 * OrthogonalPath.MIN_NODE_GAP;
		if (distance <= 0)
			return result;
		final XPoint2D to = from.move(distance * end.side.dx, distance * end.side.dy);
		final RectangleArea run = RectangleArea.build(from, to);
		for (RectangleArea obstacle : obstacles)
			if (obstacle != end.area && obstacle != end.other.area && OrthogonalPath.overlaps(run, obstacle, clearance))
				result.add(end.stretch(obstacle, clearance));
		for (RectangleArea cluster : clusters) {
			if (cluster.contains(end.original) == false && cluster.contains(end.other.original) == false
					&& OrthogonalPath.overlaps(run, cluster, clearance))
				result.add(end.stretch(cluster, clearance));
			// A run beside a cluster border, for example towards a port on it,
			// would disappear into that border.
			for (RectangleArea border : OrthogonalPath.sides(cluster))
				if (runsAlong(run, border))
					result.add(end.stretch(border, OrthogonalPath.TRACK_GAP));
		}
		return result;
	}

	private static boolean runsAlong(RectangleArea run, RectangleArea border) {
		final boolean vertical = run.getWidth() == 0;
		if (vertical != (border.getWidth() == 0))
			return false;
		final double offset = vertical ? run.getMinX() - border.getMinX() : run.getMinY() - border.getMinY();
		final double from = Math.max(vertical ? run.getMinY() : run.getMinX(),
				vertical ? border.getMinY() : border.getMinX());
		final double to = Math.min(vertical ? run.getMaxY() : run.getMaxX(),
				vertical ? border.getMaxY() : border.getMaxX());
		return Math.abs(offset) < OrthogonalPath.TRACK_GAP && from < to;
	}

	// Puts the ends of one side in an order that does not cross and moves
	// them as little as possible from their ideal positions to keep them apart,
	// the ends of straight lines least of all.
	private static void spread(List<End> side) {
		// Past a corner, ends that turn equally deep keep the order of their other
		// ends. Any other tie is broken the same way on both sides of an edge.
		side.sort(Comparator.comparingInt(End::beyondCorner).thenComparingDouble(End::order)
				.thenComparingDouble(end -> end.beyondCorner() == 0 ? 0 : end.target)
				.thenComparingInt(end -> end.edge));
		final int count = side.size();
		// The widest decoration of the side decides how close to the corners all may go.
		double low = Double.NEGATIVE_INFINITY;
		double high = Double.POSITIVE_INFINITY;
		for (End end : side) {
			low = Math.max(low, end.low(true));
			high = Math.min(high, end.high(true));
		}
		final double gap = count == 1 ? 0 : Math.min(SPACING, (high - low) / (count - 1));
		// With the i-th position shifted by i gaps, the spacing constraint becomes
		// a monotonic sequence: pool adjacent violators into blocks at their
		// weighted mean.
		final double[] mean = new double[count];
		final double[] weight = new double[count];
		final int[] size = new int[count];
		int blocks = 0;
		for (int i = 0; i < count; i++) {
			mean[blocks] = side.get(i).ideal - i * gap;
			weight[blocks] = side.get(i).straight ? STRAIGHT_WEIGHT : 1;
			size[blocks] = 1;
			blocks++;
			while (blocks > 1 && mean[blocks - 2] > mean[blocks - 1]) {
				final double merged = weight[blocks - 2] + weight[blocks - 1];
				mean[blocks - 2] = (mean[blocks - 2] * weight[blocks - 2] + mean[blocks - 1] * weight[blocks - 1])
						/ merged;
				weight[blocks - 2] = merged;
				size[blocks - 2] += size[blocks - 1];
				blocks--;
			}
		}
		final double last = high - (count - 1) * gap;
		int i = 0;
		for (int block = 0; block < blocks; block++) {
			final double base = Math.max(low, Math.min(last, mean[block]));
			for (int k = 0; k < size[block]; k++, i++)
				side.get(i).moveTo(base + i * gap);
		}
	}

	// The end on a side next to its own: towards the other end, and clear of
	// the attachments already on that side. Null if that side has no room.
	private End nextTo(End end, Direction side) {
		final End moved = new End(end, side);
		if (moved.slides == false)
			return null;
		final List<Double> taken = new ArrayList<>();
		final Map<Direction, List<End>> bySide = sides.get(end.area);
		if (bySide != null && bySide.containsKey(side))
			for (End other : bySide.get(side))
				taken.add(moved.along(other.point));
		final Map<Direction, List<XPoint2D>> away = movedAway.get(end.area);
		if (away != null && away.containsKey(side))
			for (XPoint2D point : away.get(side))
				taken.add(moved.along(point));
		final double wanted = moved.clamp(moved.along(end.other.point));
		final List<Double> candidates = new ArrayList<>();
		candidates.add(wanted);
		for (double position : taken) {
			candidates.add(position - SPACING);
			candidates.add(position + SPACING);
		}
		double best = Double.NaN;
		for (double candidate : candidates)
			if (candidate >= moved.low(true) && candidate <= moved.high(true) && isFree(candidate, taken)
					&& (Double.isNaN(best) || Math.abs(candidate - wanted) < Math.abs(best - wanted)))
				best = candidate;
		if (Double.isNaN(best))
			return null;
		moved.moveTo(best);
		return moved;
	}

	private static boolean isFree(double position, List<Double> taken) {
		for (double other : taken)
			if (Math.abs(position - other) < SPACING)
				return false;
		return true;
	}

	static final class Attachment {
		private final OrthogonalPorts ports;
		private final End start;
		private final End end;

		private Attachment(OrthogonalPorts ports, End start, End end) {
			this.ports = ports;
			this.start = start;
			this.end = end;
		}

		/**
		 * The same edge with one end moved to a side next to the one it was
		 * placed on, for a route that the chosen sides force into a detour.
		 */
		List<Attachment> nextSides() {
			final List<Attachment> result = new ArrayList<>();
			for (End moving : Arrays.asList(start, end)) {
				if (moving.slides == false)
					continue;
				for (Direction side : Direction.values()) {
					if (side == moving.side || side == moving.side.opposite())
						continue;
					final End moved = ports.nextTo(moving, side);
					if (moved != null)
						result.add(moving == start ? new Attachment(ports, moved, end) : new Attachment(ports, start, moved));
				}
			}
			return result;
		}

		// Keeps the ends that this alternative moved off the sides of later ones.
		void use() {
			for (End moved : Arrays.asList(start, end))
				if (moved.replaces != null)
					ports.movedAway.computeIfAbsent(moved.area, area -> new EnumMap<>(Direction.class))
							.computeIfAbsent(moved.side, side -> new ArrayList<>()).add(moved.point);
		}

		XPoint2D getStart() {
			return start.point;
		}

		XPoint2D getEnd() {
			return end.point;
		}

		Direction getStartSide() {
			return start.side;
		}

		Direction getEndSide() {
			return end.side;
		}

		// Whether both attachments are still where the layout put them.
		boolean isAsLaidOut(DotPath spline) {
			return start.point.equals(spline.getStartPoint()) && end.point.equals(spline.getEndPoint());
		}
	}

	private static final class End {
		// The edge this end belongs to, numbered as registered.
		private final int edge;
		private final RectangleArea area;
		private final Direction side;
		private final double inset;
		private final double decorationHalfWidth;
		private final boolean slides;
		private final XPoint2D original;
		private XPoint2D point;
		private End other;
		private double ideal;
		// Whether the ideal position lines up with a facing other end.
		private boolean straight;
		// Where the other end was at the start of the round: along this side,
		// and away from it.
		private double target;
		private double outward;
		// For an end moved to the next side, the end it stands in for.
		private final End replaces;

		End(int edge, XPoint2D point, double angle, Endpoint endpoint) {
			this.edge = edge;
			this.original = point;
			this.point = point;
			this.area = endpoint.area();
			this.side = area == null ? null : OrthogonalPath.side(point, angle, area);
			this.inset = endpoint.straightBorderInset();
			this.decorationHalfWidth = endpoint.decorationHalfWidth();
			this.slides = endpoint.slidesAlongSide() && sideLength() > 2 * decorationHalfWidth;
			this.replaces = null;
		}

		// The same end on another side of its node.
		End(End end, Direction side) {
			this.edge = end.edge;
			this.original = end.original;
			this.point = end.point;
			this.area = end.area;
			this.side = side;
			this.inset = end.inset;
			this.decorationHalfWidth = end.decorationHalfWidth;
			this.slides = end.slides && sideLength() > 2 * decorationHalfWidth;
			this.other = end.other;
			this.replaces = end;
		}

		private double straightLength() {
			return (alongX() ? area.getWidth() : area.getHeight()) - 2 * inset;
		}

		// Top and bottom sides run along x, left and right sides along y.
		private boolean alongX() {
			return side.dx == 0;
		}

		// The stretch of this side that an area covers, widened by the margin.
		double[] stretch(RectangleArea covered, double margin) {
			return alongX() ? new double[] { covered.getMinX() - margin, covered.getMaxX() + margin }
					: new double[] { covered.getMinY() - margin, covered.getMaxY() + margin };
		}

		double along(XPoint2D p) {
			return alongX() ? p.x : p.y;
		}

		// Keeps the distance the layout left to the side, unless the spline ended
		// clearly off it, for example on the next side near a corner. On a
		// rounded corner, the attachment reaches in to the painted outline.
		XPoint2D at(double position) {
			final double line = side == Direction.UP ? area.getMinY()
					: side == Direction.DOWN ? area.getMaxY() : side == Direction.LEFT ? area.getMinX() : area.getMaxX();
			final double across = alongX() ? original.y : original.x;
			final double onSide = (Math.abs(across - line) > 0.5 ? line : across) - depth(position) * (side.dx + side.dy);
			return alongX() ? new XPoint2D(position, onSide) : new XPoint2D(onSide, position);
		}

		// How far inside the box the outline runs at this position. Like SVG and
		// Java2D, a corner radius larger than half the box narrows to fit it.
		double depth(double position) {
			final double length = sideLength();
			final double across = alongX() ? area.getHeight() : area.getWidth();
			final double radiusAlong = Math.min(inset, length / 2);
			final double radiusAcross = Math.min(inset, across / 2);
			final double start = alongX() ? area.getMinX() : area.getMinY();
			final double fromCorner = Math.max(0, Math.min(position - start, start + length - position));
			if (radiusAlong <= 0 || fromCorner >= radiusAlong)
				return 0;
			final double t = (radiusAlong - fromCorner) / radiusAlong;
			return radiusAcross * (1 - Math.sqrt(1 - t * t));
		}

		private double sideLength() {
			return alongX() ? area.getWidth() : area.getHeight();
		}

		// The decoration stays on the straight part of the side, and would rather
		// keep away from its ends. A side rounded all along has no straight part:
		// there it may sit anywhere, reaching in to the outline, and would rather
		// sit in the middle.
		private double margin(boolean least) {
			final double straight = straightLength();
			if (straight <= 2 * decorationHalfWidth)
				return least ? decorationHalfWidth : sideLength() / 2;
			if (least)
				return inset + decorationHalfWidth;
			return inset + Math.max(decorationHalfWidth, Math.min(CORNER_MARGIN, straight / 4));
		}

		double low(boolean least) {
			return (alongX() ? area.getMinX() : area.getMinY()) + margin(least);
		}

		double high(boolean least) {
			return (alongX() ? area.getMaxX() : area.getMaxY()) - margin(least);
		}

		// Within the preferred margins.
		double clamp(double value) {
			return Math.max(low(false), Math.min(high(false), value));
		}

		// Whether the other end lies past the first (-1) or the last (1) corner
		// of this side, or along it (0). A straight line counts as along it.
		int beyondCorner() {
			if (straight)
				return 0;
			return target < low(false) ? -1 : target > high(false) ? 1 : 0;
		}

		// The order in which the ends of a side leave it without crossing, within
		// the groups of beyondCorner(). Along the side, the ideal positions keep
		// the order of the other ends and know where a straight line runs. Past a
		// corner, the ends turn towards it: the one whose other end is nearer
		// turns first and has to sit closer to that corner.
		double order() {
			final int beyond = beyondCorner();
			if (beyond == 0)
				return ideal;
			return beyond < 0 ? outward : -outward;
		}

		void moveTo(double position) {
			point = at(position);
		}
	}
}
