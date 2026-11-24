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
	private static final double MIN_NODE_GAP = 0.5;
	private static final double BEND_COST = 18;
	private static final double TRACK_GAP = 6;
	private static final double ENDPOINT_TOLERANCE = 2;
	private static final double STRAIGHT_TOLERANCE = 1.5;
	private static final double LOCAL_SEARCH_MARGIN = 72;
	private static final double OUTER_SEARCH_MARGIN = 24;
	private static final int SEARCH_BUDGET = 2_000_000;

	private static final SearchAttempt[] SEARCH_ATTEMPTS = {
			new SearchAttempt(16, NODE_GAP),
			new SearchAttempt(6, NODE_GAP),
			new SearchAttempt(1, NODE_GAP),
			new SearchAttempt(1, MIN_NODE_GAP)
	};

	private OrthogonalPath() {
	}

	/**
	 * Finds an orthogonal route within a shared budget for all search attempts.
	 * A null result leaves the original Smetana spline available to the caller.
	 */
	static DotPath route(DotPath spline, Endpoint tail, Endpoint head, List<RectangleArea> nodes,
			List<RectangleArea> usedTracks) {
		final XPoint2D start = spline.getStartPoint();
		final XPoint2D end = spline.getEndPoint();
		final List<RectangleArea> obstacles = new ArrayList<>(nodes);
		addEndpointObstacle(obstacles, tail.area, end);
		addEndpointObstacle(obstacles, head.area, start);

		final MinMax original = spline.getMinMax();
		final RectangleArea originalBounds = new RectangleArea(original.getMinX(), original.getMinY(),
				original.getMaxX(), original.getMaxY());
		final SearchBudget budget = new SearchBudget();
		final List<Port> targets = head.ports(end, spline.getEndAngle() + Math.PI);
		for (Port from : tail.ports(start, spline.getStartAngle()))
			for (Port to : targets) {
				final DotPath path = route(originalBounds, from, to, obstacles, usedTracks, budget);
				if (path != null || budget.exhausted())
					return path;
			}
		return null;
	}

	private static DotPath route(RectangleArea originalBounds, Port from, Port to, List<RectangleArea> obstacles,
			List<RectangleArea> usedTracks, SearchBudget budget) {
		final DotPath straight = tryStraight(from, to, obstacles, usedTracks);
		if (straight != null)
			return straight;

		// Search near the original edge first, so a small local reroute does not consider
		// every row and column in a large diagram.
		final DotPath local = search(expand(originalBounds, LOCAL_SEARCH_MARGIN), from, to, obstacles, usedTracks,
				budget);
		if (local != null || budget.exhausted())
			return local;
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
			return toPath(Arrays.asList(start, end));
		return null;
	}

	private static DotPath search(RectangleArea bounds, Port from, Port to, List<RectangleArea> obstacles,
			List<RectangleArea> usedTracks, SearchBudget budget) {
		final double facingDistance = from.facingDistance(to);
		// Shorten the terminal runs before narrowing the clearance to half
		// a pixel, the space needed on either side of a one-pixel stroke.
		for (SearchAttempt attempt : SEARCH_ATTEMPTS) {
			if (budget.exhausted())
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
			if (points != null) {
				points.add(0, from.point);
				points.add(to.point);
				final DotPath path = toPath(points);
				if (path != null)
					return path;
			}
		}
		return null;
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

	private static boolean overlaps(RectangleArea a, RectangleArea b, double gap) {
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

	private static DotPath toPath(List<XPoint2D> points) {
		// Merge consecutive straight segments so arrowhead shortening can use
		// the full run up to the nearest bend.
		final List<XCubicCurve2D> segments = new ArrayList<>();
		XPoint2D start = points.get(0);
		XPoint2D previous = start;
		for (int i = 1; i < points.size(); i++) {
			final XPoint2D next = points.get(i);
			if (previous.equals(next))
				continue;
			if (start.equals(previous) == false && (start.x == previous.x) != (previous.x == next.x)) {
				segments.add(line(start, previous));
				start = previous;
			}
			previous = next;
		}
		if (start.equals(previous) == false)
			segments.add(line(start, previous));
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

	private enum Direction {
		RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0), UP(0, -1);

		private final int dx;
		private final int dy;

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

	static final class Endpoint {
		enum Shape {
			FIXED, BOX, CIRCLE
		}

		private final RectangleArea area;
		private final Shape shape;
		private final double decoration;

		Endpoint(RectangleArea area, Shape shape, double decoration) {
			this.area = area;
			this.shape = shape;
			this.decoration = decoration;
		}

		private List<Port> ports(XPoint2D point, double angle) {
			final Direction preferred = Port.outward(point, angle, area);
			if (shape == Shape.FIXED || area == null)
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

	// Each expansion can scan every obstacle and used track. Bound their
	// combined cost across retries so crowded diagrams can fall back promptly.
	private static final class SearchBudget {
		private int remaining = SEARCH_BUDGET;

		boolean spend(int amount) {
			remaining -= amount;
			return remaining >= 0;
		}

		boolean exhausted() {
			return remaining <= 0;
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
