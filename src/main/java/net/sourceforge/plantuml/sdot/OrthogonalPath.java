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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;

/**
 * Routes an edge between the attachment sides supplied by the Smetana layout.
 * The search grid uses the attachment coordinates and the edges of nearby
 * obstacles. A* weighs path length, bends and runs alongside existing tracks.
 * Straight terminal runs align the decorations with the painted node borders.
 */
final class OrthogonalPath {
	private static final double NODE_GAP = 4;
	static final double MIN_NODE_GAP = 0.5;
	private static final double BEND_COST = 18;
	static final double TRACK_GAP = 6;
	private static final double ENDPOINT_TOLERANCE = 2;
	private static final double STRAIGHT_TOLERANCE = 1.5;
	private static final double LOCAL_SEARCH_MARGIN = 72;
	private static final double OUTER_SEARCH_MARGIN = 24;
	// A route with this many bends is a detour that another side may avoid.
	private static final int DETOUR_BENDS = 3;
	// Counted in scanned obstacles and tracks. A second handles about 200 million.
	private static final long EDGE_BUDGET = 2_000_000;
	// Dense diagrams of 150 to 200 edges need up to 300 million for all their routes.
	private static final long DIAGRAM_BUDGET = 500_000_000;

	private static final SearchAttempt[] SEARCH_ATTEMPTS = {
			new SearchAttempt(16, NODE_GAP),
			new SearchAttempt(6, NODE_GAP),
			new SearchAttempt(1, NODE_GAP),
			new SearchAttempt(1, MIN_NODE_GAP)
	};

	private OrthogonalPath() {
	}

	/**
	 * Finds an orthogonal route within the budget that all edges of a diagram
	 * share, between the attachments that {@link OrthogonalPorts} chose. These
	 * keep their sides even where they moved close to a corner. A moved
	 * attachment can leave a stem less room than the layout gave it: if no
	 * route is found, the ends of the spline get a try within the same budget.
	 * A route that the chosen sides force into a detour tries the sides next
	 * to them. A null result leaves the original Smetana spline available to
	 * the caller.
	 */
	static DotPath route(DotPath spline, OrthogonalPorts.Attachment attachment, Endpoint tail, Endpoint head,
			List<RectangleArea> nodes, List<RectangleArea> usedTracks, SearchBudget budget) {
		budget.startEdge();
		if (budget.exhausted())
			return null;
		DotPath path = between(spline, attachment, tail, head, nodes, usedTracks, budget);
		if (path == null && budget.exhausted() == false && attachment.isAsLaidOut(spline) == false)
			path = route(spline, spline.getStartPoint(), null, spline.getEndPoint(), null, tail, head, nodes,
					usedTracks, budget);
		if (path == null || bends(path) < DETOUR_BENDS)
			return path;
		// The other side has to save bends without making the route longer or
		// crossing more of the earlier edges.
		OrthogonalPorts.Attachment chosen = null;
		int crossings = crossings(corners(path), usedTracks);
		for (OrthogonalPorts.Attachment other : attachment.nextSides()) {
			if (budget.exhausted())
				break;
			final DotPath shorter = between(spline, other, tail, head, nodes, usedTracks, budget);
			if (shorter == null || bends(shorter) >= bends(path) || length(shorter) > length(path))
				continue;
			final int crossed = crossings(corners(shorter), usedTracks);
			if (crossed <= crossings) {
				path = shorter;
				chosen = other;
				crossings = crossed;
			}
		}
		if (chosen != null)
			chosen.use();
		return path;
	}

	private static DotPath between(DotPath spline, OrthogonalPorts.Attachment attachment, Endpoint tail,
			Endpoint head, List<RectangleArea> nodes, List<RectangleArea> usedTracks, SearchBudget budget) {
		return route(spline, attachment.getStart(), attachment.getStartSide(), attachment.getEnd(),
				attachment.getEndSide(), tail, head, nodes, usedTracks, budget);
	}

	private static int bends(DotPath path) {
		return path.getBeziers().size() - 1;
	}

	private static double length(DotPath path) {
		double length = 0;
		for (XCubicCurve2D segment : path.getBeziers())
			length += segment.getP1().distance(segment.getP2());
		return length;
	}

	private static List<XPoint2D> corners(DotPath path) {
		final List<XPoint2D> corners = new ArrayList<>();
		corners.add(path.getStartPoint());
		for (XCubicCurve2D segment : path.getBeziers())
			corners.add(segment.getP2());
		return corners;
	}

	// A null side is the one the spline leaves or enters through.
	private static DotPath route(DotPath spline, XPoint2D start, Direction startSide, XPoint2D end,
			Direction endSide, Endpoint tail, Endpoint head, List<RectangleArea> nodes,
			List<RectangleArea> usedTracks, SearchBudget budget) {
		final List<RectangleArea> obstacles = new ArrayList<>(nodes);
		addEndpointObstacle(obstacles, tail.area, end);
		addEndpointObstacle(obstacles, head.area, start);

		final MinMax original = spline.getMinMax();
		final RectangleArea originalBounds = new RectangleArea(original.getMinX(), original.getMinY(),
				original.getMaxX(), original.getMaxY());
		// A port can attach on any side: the one with the fewest bends wins, the
		// side of the layout on a tie. A pair of sides that cannot do better than
		// the best route so far is not searched.
		DotPath best = null;
		final List<Port> targets = head.ports(end, endSide, spline.getEndAngle() + Math.PI);
		for (Port from : tail.ports(start, startSide, spline.getStartAngle()))
			for (Port to : targets) {
				if (best != null && fewestBends(from, to) >= bends(best))
					continue;
				final DotPath path = route(originalBounds, from, to, obstacles, usedTracks, budget);
				if (path != null && (best == null || bends(path) < bends(best)))
					best = path;
				if (budget.exhausted() || (best != null && bends(best) == 0))
					return best;
			}
		return best;
	}

	private static DotPath route(RectangleArea originalBounds, Port from, Port to, List<RectangleArea> obstacles,
			List<RectangleArea> usedTracks, SearchBudget budget) {
		budget.spend(obstacles.size() + usedTracks.size());
		final DotPath straight = tryStraight(from, to, obstacles, usedTracks);
		if (straight != null || budget.exhausted())
			return straight;

		// Search near the original edge first, so a small local reroute does not consider
		// every row and column in a large diagram.
		final DotPath local = search(expand(originalBounds, LOCAL_SEARCH_MARGIN), from, to, obstacles, usedTracks,
				budget);
		if (local != null || budget.exhausted())
			return local;
		budget.spend(obstacles.size());
		return search(expand(enclosing(originalBounds, obstacles), OUTER_SEARCH_MARGIN), from, to, obstacles, usedTracks,
				budget);
	}

	private static DotPath tryStraight(Port from, Port to, List<RectangleArea> obstacles,
			List<RectangleArea> usedTracks) {
		final double distance = from.facingDistance(to);
		if (distance <= 0 || distance < from.decoration + to.decoration)
			return null;
		final boolean horizontal = from.outward.dx != 0;
		final double offset = horizontal ? to.point.y - from.point.y : to.point.x - from.point.x;
		if (Math.abs(offset) > STRAIGHT_TOLERANCE)
			return null;
		XPoint2D start = from.point;
		XPoint2D end = to.point;
		if (offset != 0) {
			// Split the adjustment between the two attachment sides. Require
			// room to slide along each side, away from its corners.
			final double shift = offset / 2;
			start = horizontal ? start.move(0, shift) : start.move(shift, 0);
			end = horizontal ? new XPoint2D(end.x, start.y) : new XPoint2D(start.x, end.y);
			if (from.canSlideTo(start) == false || to.canSlideTo(end) == false)
				return null;
		}
		if (clear(start, end, obstacles, from.area, to.area, MIN_NODE_GAP)
				&& overlapCost(RectangleArea.build(start, end), usedTracks) == 0)
			return toPath(corners(Arrays.asList(start, end)));
		return null;
	}

	private static DotPath search(RectangleArea bounds, Port from, Port to, List<RectangleArea> obstacles,
			List<RectangleArea> usedTracks, SearchBudget budget) {
		final double facingDistance = from.facingDistance(to);
		// The first stem that works is usually the best. A stem that reaches
		// past the turn the route needs costs extra bends, for example by
		// making it run back towards its own node: shorter stems get a try then,
		// and the fewest bends win, the longer stem on a tie.
		final int fewest = fewestBends(from, to);
		DotPath best = null;
		int bestBends = Integer.MAX_VALUE;
		boolean bestTurnsBack = false;
		// Shorten the terminal runs before narrowing the clearance to half
		// a pixel, the space needed on either side of a one-pixel stroke.
		for (SearchAttempt attempt : SEARCH_ATTEMPTS) {
			// Half a pixel beside a node is a last resort, not a way to save bends.
			if (best != null && attempt.gap < NODE_GAP)
				break;
			// Checking both stems and building the grid scans every obstacle and track.
			if (budget.spend(3 * obstacles.size() + usedTracks.size()) == false)
				break;
			// Share the available space between opposing ports so neither
			// stem can force the middle of the path to double back.
			final double length = facingDistance > 0 ? Math.min(attempt.stem, facingDistance / 2) : attempt.stem;
			final XPoint2D exit = from.exit(length, attempt.gap);
			final XPoint2D entry = to.exit(length, attempt.gap);
			if (exit.equals(entry))
				continue;
			if (facingDistance > 0 && from.outward.progress(exit, entry) < 0)
				continue;
			if (clear(from.point, exit, obstacles, from.area, null, MIN_NODE_GAP) == false
					|| clear(entry, to.point, obstacles, to.area, null, MIN_NODE_GAP) == false)
				continue;

			final Grid grid = new Grid(bounds.merge(exit).merge(entry), exit, entry, obstacles, attempt.gap, usedTracks);
			final List<XPoint2D> points = grid.find(from.outward, to.outward, budget);
			if (points == null)
				continue;
			points.add(0, from.point);
			points.add(to.point);
			final List<XPoint2D> corners = centerJog(corners(points), from, to, obstacles, usedTracks);
			final DotPath path = toPath(corners);
			if (path == null)
				continue;
			final int bends = corners.size() - 2;
			final boolean turnsBack = turnsBack(corners);
			if (bends < bestBends || (bends == bestBends && bestTurnsBack && turnsBack == false)) {
				best = path;
				bestBends = bends;
				bestTurnsBack = turnsBack;
			}
			if (bestBends <= fewest && bestTurnsBack == false)
				break;
		}
		return best;
	}

	// The fewest bends between two ports with nothing in the way.
	private static int fewestBends(Port from, Port to) {
		final Direction leaving = from.outward;
		final Direction arriving = to.outward.opposite();
		final double ahead = leaving.progress(from.point, to.point);
		final double across = leaving.dx != 0 ? Math.abs(to.point.y - from.point.y)
				: Math.abs(to.point.x - from.point.x);
		if (leaving == arriving)
			return ahead > STRAIGHT_TOLERANCE ? (across <= STRAIGHT_TOLERANCE ? 0 : 2) : 4;
		if (leaving == arriving.opposite())
			return across > STRAIGHT_TOLERANCE ? 2 : 4;
		return ahead > STRAIGHT_TOLERANCE && arriving.progress(from.point, to.point) > STRAIGHT_TOLERANCE ? 1 : 3;
	}

	// Whether the route runs back towards an end right after its stem. A
	// U-shaped route between two ends that face the same way does that only
	// on its way into the other end.
	private static boolean turnsBack(List<XPoint2D> corners) {
		final int last = corners.size() - 1;
		return last >= 4 && (reverses(corners.get(0), corners.get(1), corners.get(2), corners.get(3))
				|| reverses(corners.get(last), corners.get(last - 1), corners.get(last - 2), corners.get(last - 3)));
	}

	// Whether the run c-d heads against the run a-b.
	private static boolean reverses(XPoint2D a, XPoint2D b, XPoint2D c, XPoint2D d) {
		return (b.x - a.x) * (d.x - c.x) + (b.y - a.y) * (d.y - c.y) < 0;
	}

	private static void addEndpointObstacle(List<RectangleArea> obstacles, RectangleArea area, XPoint2D other) {
		// A cluster can be an endpoint and also contain the other endpoint. Its interior
		// has to remain traversable in that case.
		if (area != null && obstacles.contains(area) == false && area.contains(other) == false)
			obstacles.add(area);
	}

	private static RectangleArea enclosing(RectangleArea bounds, List<RectangleArea> obstacles) {
		for (RectangleArea area : obstacles)
			bounds = bounds.merge(area);
		return bounds;
	}

	static List<RectangleArea> sides(RectangleArea area) {
		final double left = area.getMinX();
		final double right = area.getMaxX();
		final double top = area.getMinY();
		final double bottom = area.getMaxY();
		return Arrays.asList(new RectangleArea(left, top, right, top), new RectangleArea(left, bottom, right, bottom),
				new RectangleArea(left, top, left, bottom), new RectangleArea(right, top, right, bottom));
	}

	static RectangleArea expand(RectangleArea area, double margin) {
		return new RectangleArea(area.getMinX() - margin, area.getMinY() - margin, area.getMaxX() + margin,
				area.getMaxY() + margin);
	}

	static boolean overlaps(RectangleArea a, RectangleArea b, double gap) {
		// A horizontal or vertical segment has zero area. Strict comparisons let
		// it run along the boundary of an expanded obstacle.
		return a.getMinX() < b.getMaxX() + gap && a.getMaxX() > b.getMinX() - gap
				&& a.getMinY() < b.getMaxY() + gap && a.getMaxY() > b.getMinY() - gap;
	}

	private static boolean clear(XPoint2D a, XPoint2D b, List<RectangleArea> obstacles, RectangleArea allowedA,
			RectangleArea allowedB, double gap) {
		final RectangleArea span = RectangleArea.build(a, b);
		for (RectangleArea area : obstacles)
			if (area != allowedA && area != allowedB && overlaps(span, area, gap))
				return false;
		return true;
	}

	// Shared stems near a port are unavoidable. Penalize long runs on or beside
	// an earlier edge without treating it as a wall or penalizing ordinary crossings.
	private static double overlapCost(RectangleArea span, List<RectangleArea> tracks) {
		double cost = 0;
		for (RectangleArea track : tracks) {
			if (span.getHeight() == 0 && track.getHeight() == 0
					&& Math.abs(span.getMinY() - track.getMinY()) < TRACK_GAP)
				cost += Math.max(0, Math.min(span.getMaxX(), track.getMaxX()) - Math.max(span.getMinX(), track.getMinX()));
			if (span.getWidth() == 0 && track.getWidth() == 0
					&& Math.abs(span.getMinX() - track.getMinX()) < TRACK_GAP)
				cost += Math.max(0, Math.min(span.getMaxY(), track.getMaxY()) - Math.max(span.getMinY(), track.getMinY()));
		}
		return cost;
	}

	// The ends and the bends of a route. Consecutive straight segments merge,
	// so arrowhead shortening can use the full run up to the nearest bend.
	private static List<XPoint2D> corners(List<XPoint2D> points) {
		final List<XPoint2D> corners = new ArrayList<>();
		XPoint2D start = points.get(0);
		XPoint2D previous = start;
		corners.add(start);
		for (int i = 1; i < points.size(); i++) {
			final XPoint2D next = points.get(i);
			if (previous.equals(next))
				continue;
			if (start.equals(previous) == false && (start.x == previous.x) != (previous.x == next.x)) {
				corners.add(previous);
				start = previous;
			}
			previous = next;
		}
		if (start.equals(previous) == false)
			corners.add(previous);
		return corners;
	}

	/**
	 * A* follows a run as far as it can before it turns, which leaves the jog of
	 * a Z-shaped route right next to one of its ends. Moves the jog halfway
	 * between the ends, or as close to that as the obstacles allow, unless it
	 * would cross or run alongside more of the earlier edges there.
	 */
	private static List<XPoint2D> centerJog(List<XPoint2D> corners, Port from, Port to, List<RectangleArea> obstacles,
			List<RectangleArea> usedTracks) {
		if (corners.size() != 4)
			return corners;
		final XPoint2D a = corners.get(0);
		final XPoint2D b = corners.get(1);
		final XPoint2D c = corners.get(2);
		final XPoint2D d = corners.get(3);
		// The outer runs of a Z are parallel and head the same way.
		final boolean vertical = a.x == b.x;
		if (vertical ? c.x != d.x || (b.y - a.y) * (d.y - c.y) <= 0 : c.y != d.y || (b.x - a.x) * (d.x - c.x) <= 0)
			return corners;
		// The jog stays clear of both stems and their decorations. A route that
		// only fits with less clearance than that keeps its jog where it is.
		final double first = along(from.exit(0, NODE_GAP), vertical);
		final double last = along(to.exit(0, NODE_GAP), vertical);
		if ((last - first) * (along(d, vertical) - along(a, vertical)) < 0)
			return corners;
		final double low = Math.min(first, last);
		final double high = Math.max(first, last);
		final double current = along(b, vertical);
		final double middle = Math.max(low, Math.min(high, (along(a, vertical) + along(d, vertical)) / 2));
		final double cost = routeCost(corners, usedTracks);
		final int crossings = crossings(corners, usedTracks);
		for (double position : new double[] { middle, (middle + current) / 2 }) {
			if (Math.abs(position - current) < 1)
				break;
			final XPoint2D b2 = vertical ? new XPoint2D(b.x, position) : new XPoint2D(position, b.y);
			final XPoint2D c2 = vertical ? new XPoint2D(c.x, position) : new XPoint2D(position, c.y);
			final List<XPoint2D> moved = Arrays.asList(a, b2, c2, d);
			if (clear(a, b2, obstacles, from.area, to.area, NODE_GAP) && clear(b2, c2, obstacles, from.area, to.area, NODE_GAP)
					&& clear(c2, d, obstacles, from.area, to.area, NODE_GAP) && routeCost(moved, usedTracks) <= cost
					&& crossings(moved, usedTracks) <= crossings)
				return moved;
		}
		return corners;
	}

	private static int crossings(List<XPoint2D> corners, List<RectangleArea> usedTracks) {
		int count = 0;
		for (int i = 1; i < corners.size(); i++) {
			final RectangleArea span = RectangleArea.build(corners.get(i - 1), corners.get(i));
			for (RectangleArea track : usedTracks)
				if (crosses(span, track) || crosses(track, span))
					count++;
		}
		return count;
	}

	// Whether a horizontal segment crosses a vertical one away from their ends.
	private static boolean crosses(RectangleArea horizontal, RectangleArea vertical) {
		return horizontal.getHeight() == 0 && vertical.getWidth() == 0 && horizontal.getMinX() < vertical.getMinX()
				&& vertical.getMinX() < horizontal.getMaxX() && vertical.getMinY() < horizontal.getMinY()
				&& horizontal.getMinY() < vertical.getMaxY();
	}

	private static double along(XPoint2D point, boolean vertical) {
		return vertical ? point.y : point.x;
	}

	private static double routeCost(List<XPoint2D> corners, List<RectangleArea> usedTracks) {
		double cost = 0;
		for (int i = 1; i < corners.size(); i++)
			cost += overlapCost(RectangleArea.build(corners.get(i - 1), corners.get(i)), usedTracks);
		return cost;
	}

	private static DotPath toPath(List<XPoint2D> corners) {
		final List<XCubicCurve2D> segments = new ArrayList<>();
		for (int i = 1; i < corners.size(); i++)
			segments.add(line(corners.get(i - 1), corners.get(i)));
		return segments.isEmpty() ? null : DotPath.fromBeziers(segments);
	}

	private static XCubicCurve2D line(XPoint2D a, XPoint2D b) {
		return new XCubicCurve2D(a.x, a.y, a.x, a.y, b.x, b.y, b.x, b.y);
	}

	private static final class SearchAttempt {
		private final double stem;
		private final double gap;

		SearchAttempt(double stem, double gap) {
			this.stem = stem;
			this.gap = gap;
		}
	}

	enum Direction {
		RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0), UP(0, -1);

		final int dx;
		final int dy;

		Direction(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}

		Direction opposite() {
			return DIRECTIONS[(ordinal() + 2) % DIRECTIONS.length];
		}

		double progress(XPoint2D a, XPoint2D b) {
			return dx != 0 ? (b.x - a.x) * dx : (b.y - a.y) * dy;
		}

		static Direction fromAngle(double angle) {
			final double x = Math.cos(angle);
			final double y = Math.sin(angle);
			if (Math.abs(x) > Math.abs(y))
				return x > 0 ? RIGHT : LEFT;
			return y > 0 ? DOWN : UP;
		}
	}

	private static final Direction[] DIRECTIONS = Direction.values();

	/** The side of the area that an attachment at the point leaves through. */
	static Direction side(XPoint2D point, double angle, RectangleArea area) {
		return Port.outward(point, angle, area);
	}

	static final class Endpoint {
		enum Shape {
			// Attaches where the layout put it.
			FIXED,
			// A rectangular node: the attachment may move along the side the layout chose.
			SIDE,
			// A port (portin, portout) or an entry or exit point: the attachment
			// may use any side of its box or circle.
			BOX, CIRCLE
		}

		private final RectangleArea area;
		private final Shape shape;
		private final double decoration;
		private final double decorationHalfWidth;
		private final double straightBorderInset;

		Endpoint(RectangleArea area, Shape shape, double decoration) {
			this(area, shape, decoration, 0, 0);
		}

		private Endpoint(RectangleArea area, Shape shape, double decoration, double decorationHalfWidth,
				double straightBorderInset) {
			this.area = area;
			this.shape = shape;
			this.decoration = decoration;
			this.decorationHalfWidth = decorationHalfWidth;
			this.straightBorderInset = straightBorderInset;
		}

		/** A node whose border runs straight from this far off each corner. */
		static Endpoint side(RectangleArea area, double decoration, double decorationHalfWidth,
				double straightBorderInset) {
			return new Endpoint(area, Shape.SIDE, decoration, decorationHalfWidth, straightBorderInset);
		}

		RectangleArea area() {
			return area;
		}

		boolean slidesAlongSide() {
			return shape == Shape.SIDE && area != null;
		}

		double straightBorderInset() {
			return straightBorderInset;
		}

		// How far the decoration reaches to either side of the line, at least the stroke.
		double decorationHalfWidth() {
			return Math.max(1, decorationHalfWidth);
		}

		private List<Port> ports(XPoint2D point, Direction side, double angle) {
			final Direction preferred = side == null ? Port.outward(point, angle, area) : side;
			if (shape == Shape.FIXED || shape == Shape.SIDE || area == null)
				return Collections.singletonList(new Port(point, area, preferred, decoration));

			// The layout side is usually best. A boundary symbol can also
			// attach on another side if the group title blocks its stem.
			final List<Port> ports = new ArrayList<>();
			ports.add(new Port(attachmentOn(preferred, point), area, preferred, decoration));
			for (Direction direction : DIRECTIONS)
				if (direction != preferred)
					ports.add(new Port(attachmentOn(direction, point), area, direction, decoration));
			return ports;
		}

		private XPoint2D attachmentOn(Direction side, XPoint2D hint) {
			final XPoint2D center = area.getPointCenter();
			final boolean horizontal = side.dx != 0;
			final double halfWidth = area.getWidth() / 2;
			final double halfHeight = area.getHeight() / 2;
			final double across = horizontal ? halfHeight : halfWidth;
			final double offset = horizontal ? hint.y - center.y : hint.x - center.x;
			// Stay away from the corners so the first segment leaves the
			// symbol cleanly, even when the layout hint was on a different side.
			final double fraction = Math.max(-1.0 / 3, Math.min(1.0 / 3, offset / across));
			final double reach = shape == Shape.CIRCLE ? Math.sqrt(1 - fraction * fraction) : 1;
			return horizontal ? center.move(side.dx * halfWidth * reach, fraction * halfHeight)
					: center.move(fraction * halfWidth, side.dy * halfHeight * reach);
		}
	}

	private static final class Port {
		private final XPoint2D point;
		private final RectangleArea area;
		private final Direction outward;
		private final double reach;
		private final double decoration;

		Port(XPoint2D point, RectangleArea area, Direction outward, double decoration) {
			this.point = point;
			this.area = area;
			this.outward = outward;
			this.decoration = decoration;
			this.reach = area == null ? 0 : Math.max(0, distanceToSide(point, area, outward));
		}

		private static Direction outward(XPoint2D point, double angle, RectangleArea area) {
			final Direction tangent = Direction.fromAngle(angle);
			Direction direction = tangent;
			if (area != null) {
				// Near a corner the spline tangent tells which side the edge
				// leaves through. Otherwise the nearest side does.
				double closest = Math.abs(distanceToSide(point, area, tangent)) < ENDPOINT_TOLERANCE ? 0
						: ENDPOINT_TOLERANCE;
				for (Direction side : DIRECTIONS) {
					final double offset = Math.abs(distanceToSide(point, area, side));
					if (offset < closest) {
						closest = offset;
						direction = side;
					}
				}
			}
			return direction;
		}

		double facingDistance(Port other) {
			return outward == other.outward.opposite() ? outward.progress(point, other.point) : 0;
		}

		boolean canSlideTo(XPoint2D shifted) {
			if (area == null || Math.abs(distanceToSide(point, area, outward)) > ENDPOINT_TOLERANCE)
				return false;
			final boolean horizontal = outward.dx != 0;
			final double min = horizontal ? area.getMinY() : area.getMinX();
			final double max = horizontal ? area.getMaxY() : area.getMaxX();
			final double original = horizontal ? point.y : point.x;
			final double proposed = horizontal ? shifted.y : shifted.x;
			return Math.min(original, proposed) > min + ENDPOINT_TOLERANCE
					&& Math.max(original, proposed) < max - ENDPOINT_TOLERANCE;
		}

		XPoint2D exit(double stem, double gap) {
			// Keep the first bend outside both the expanded endpoint box and
			// the decoration outline. Otherwise the bend can cross the arrowhead.
			final double distance = Math.max(Math.max(stem, decoration), reach + gap + 1);
			return point.move(distance * outward.dx, distance * outward.dy);
		}

		private static double distanceToSide(XPoint2D point, RectangleArea area, Direction side) {
			switch (side) {
			case RIGHT:
				return area.getMaxX() - point.x;
			case DOWN:
				return area.getMaxY() - point.y;
			case LEFT:
				return point.x - area.getMinX();
			default:
				return point.y - area.getMinY();
			}
		}
	}

	// The grid builds rows and columns from geometric coordinates and checks
	// neighboring intersections on demand. Direction is part of the A* state
	// because the cost of continuing from an intersection depends on how it was reached.
	private static final class Grid {
		private final double[] xs;
		private final double[] ys;
		private final XPoint2D from;
		private final XPoint2D to;
		private final List<RectangleArea> blocked = new ArrayList<>();
		private final List<RectangleArea> tracks = new ArrayList<>();

		Grid(RectangleArea bounds, XPoint2D from, XPoint2D to, List<RectangleArea> obstacles, double gap,
				List<RectangleArea> usedTracks) {
			this.from = from;
			this.to = to;
			final SortedSet<Double> columns = new TreeSet<>();
			final SortedSet<Double> rows = new TreeSet<>();
			// In open space the midpoint axes let the path turn between the ports.
			columns.addAll(Arrays.asList(bounds.getMinX(), bounds.getMaxX(), from.x, to.x, (from.x + to.x) / 2));
			rows.addAll(Arrays.asList(bounds.getMinY(), bounds.getMaxY(), from.y, to.y, (from.y + to.y) / 2));
			for (RectangleArea area : obstacles) {
				final RectangleArea obstacle = expand(area, gap);
				if (overlaps(bounds, obstacle, 0) == false)
					continue;
				blocked.add(obstacle);
				columns.add(Math.max(bounds.getMinX(), obstacle.getMinX()));
				columns.add(Math.min(bounds.getMaxX(), obstacle.getMaxX()));
				rows.add(Math.max(bounds.getMinY(), obstacle.getMinY()));
				rows.add(Math.min(bounds.getMaxY(), obstacle.getMaxY()));
			}
			for (RectangleArea track : usedTracks) {
				if (overlaps(bounds, track, TRACK_GAP) == false)
					continue;
				tracks.add(track);
				if (track.getWidth() == 0)
					addTracks(columns, track.getMinX(), bounds.getMinX(), bounds.getMaxX());
				else
					addTracks(rows, track.getMinY(), bounds.getMinY(), bounds.getMaxY());
			}
			this.xs = columns.stream().mapToDouble(Double::doubleValue).toArray();
			this.ys = rows.stream().mapToDouble(Double::doubleValue).toArray();
		}

		private static void addTracks(SortedSet<Double> axes, double used, double min, double max) {
			if (used - TRACK_GAP > min && used - TRACK_GAP < max)
				axes.add(used - TRACK_GAP);
			if (used + TRACK_GAP > min && used + TRACK_GAP < max)
				axes.add(used + TRACK_GAP);
		}

		List<XPoint2D> find(Direction outgoing, Direction incoming, SearchBudget budget) {
			final int fromX = Arrays.binarySearch(xs, from.x);
			final int fromY = Arrays.binarySearch(ys, from.y);
			final int toX = Arrays.binarySearch(xs, to.x);
			final int toY = Arrays.binarySearch(ys, to.y);
			final Map<Long, Visit> best = new HashMap<>();
			final PriorityQueue<Visit> queue = new PriorityQueue<>(Comparator.comparingDouble((Visit v) -> v.estimate)
					.thenComparingDouble(v -> -v.cost).thenComparingLong(v -> key(v)));
			final Visit initial = new Visit(fromX, fromY, outgoing, 0, heuristic(from), null);
			best.put(key(initial), initial);
			queue.add(initial);
			final int visitCost = 1 + blocked.size() + tracks.size();
			while (queue.isEmpty() == false) {
				final Visit here = queue.remove();
				if (best.get(key(here)) != here)
					continue;
				// The target port faces outward. Arriving in that direction
				// would require reversing into the final straight section.
				if (here.x == toX && here.y == toY && here.direction != incoming)
					return reconstruct(here);
				if (budget.spend(visitCost) == false)
					return null;

				final XPoint2D a = new XPoint2D(xs[here.x], ys[here.y]);
				for (Direction direction : DIRECTIONS) {
					if (direction == here.direction.opposite())
						continue;
					final int x = here.x + direction.dx;
					final int y = here.y + direction.dy;
					if (x < 0 || x >= xs.length || y < 0 || y >= ys.length)
						continue;
					final XPoint2D b = new XPoint2D(xs[x], ys[y]);
					if (clear(a, b, blocked, null, null, 0) == false)
						continue;
					final double cost = here.cost + a.distance(b) + (direction == here.direction ? 0 : BEND_COST)
							+ overlapCost(RectangleArea.build(a, b), tracks);
					final Visit next = new Visit(x, y, direction, cost, cost + heuristic(b), here);
					final long key = key(next);
					final Visit previous = best.get(key);
					if (previous == null || cost < previous.cost) {
						best.put(key, next);
						queue.add(next);
					}
				}
			}
			return null;
		}

		private long key(Visit visit) {
			return ((long) visit.x * ys.length + visit.y) * DIRECTIONS.length + visit.direction.ordinal();
		}

		private double heuristic(XPoint2D point) {
			// Manhattan distance is a lower bound because all moves are
			// axis-aligned and bends and overlaps only add cost.
			return Math.abs(point.x - to.x) + Math.abs(point.y - to.y);
		}

		private List<XPoint2D> reconstruct(Visit visit) {
			final List<XPoint2D> path = new ArrayList<>();
			for (Visit step = visit; step != null; step = step.previous)
				path.add(new XPoint2D(xs[step.x], ys[step.y]));
			Collections.reverse(path);
			return path;
		}
	}

	/**
	 * Bounds the routing work of a whole diagram, counted in scanned obstacles
	 * and tracks. A single edge only gets a share of it, so that one crowded
	 * edge cannot use up what the edges after it need. An edge that finds its
	 * share spent keeps its spline.
	 */
	static final class SearchBudget {
		private long diagram;
		private long edge;

		SearchBudget() {
			this(DIAGRAM_BUDGET);
		}

		SearchBudget(long diagram) {
			this.diagram = diagram;
		}

		private void startEdge() {
			edge = Math.min(diagram, EDGE_BUDGET);
		}

		private boolean spend(long amount) {
			diagram -= amount;
			edge -= amount;
			return edge >= 0;
		}

		private boolean exhausted() {
			return edge <= 0;
		}

		// Work done for all edges together, before any of them is routed. It may
		// only take a small share of what is left, so that the routes get the rest.
		boolean spendBeforeRouting(long amount) {
			if (amount > diagram / 10)
				return false;
			diagram -= amount;
			return true;
		}

		// Nothing is left for any further edge of the diagram.
		boolean isSpent() {
			return diagram <= 0;
		}
	}

	private static final class Visit {
		private final int x;
		private final int y;
		private final Direction direction;
		private final double cost;
		private final double estimate;
		private final Visit previous;

		Visit(int x, int y, Direction direction, double cost, double estimate, Visit previous) {
			this.x = x;
			this.y = y;
			this.direction = direction;
			this.cost = cost;
			this.estimate = estimate;
			this.previous = previous;
		}
	}
}
