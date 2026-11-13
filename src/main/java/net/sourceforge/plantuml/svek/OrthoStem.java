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
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.abel.EntityPosition;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;

/**
 * Makes the end of an orthogonal edge leave (or reach) a state at right angles
 * to the side it touches.
 * <p>
 * Orthogonal routing starts an edge in the middle of a side of its node, but is
 * then free to run the first segment along that side, so the edge seems to
 * slide off the state; or to turn a few pixels away from it, too close for the
 * arrowhead to read. This rewrites the end of the edge so that it leaves the
 * side perpendicularly, clear of the rounded corners of a state (on the
 * cardinal point of a round or diamond pseudo-state), and runs at least
 * {@link #STEM} before its first turn. A rewrite is only kept if it does not
 * make the edge cross more nodes, labels, frames or edges than it did.
 */
final class OrthoStem {

	private static final double EPSILON = 0.5;

	/** Shortest end segment wanted: three arrowhead widths. */
	private static final double STEM = 24;

	/** Shortest end segment accepted when {@link #STEM} cannot be had. */
	private static final double SHORT_STEM = 8;

	/** Spacing of the lanes tried when the first turn has to move out. */
	private static final double LANE = 8;

	private static final int LANES = 6;

	/** Keeps a stem clear of the rounded corners of a state. */
	private static final double CORNER = 15;

	/** How close to a corner a stem may go when there is no room elsewhere. */
	private static final double TIGHT_CORNER = 8;

	/** How far off its node an edge may end: an arrowhead takes 5. */
	private static final double TOUCH = 7;

	/** Parallel segments closer than this are drawn on top of each other. */
	private static final double OVERLAP = 3;

	private final List<RectangleArea> obstacles;
	private final List<RectangleArea> frames;
	private final List<List<XPoint2D>> others = new ArrayList<>();

	/**
	 * A rewrite must keep as clear of {@code obstacles} (nodes and labels) and of
	 * the {@code frames} of composite states as the original edge, and cross or
	 * run along no more of the {@code others} edges.
	 */
	OrthoStem(List<RectangleArea> obstacles, List<RectangleArea> frames, List<DotPath> others) {
		this.obstacles = obstacles;
		this.frames = frames;
		for (DotPath other : others)
			this.others.add(corners(other));
	}

	/**
	 * Returns {@code path}, with its start rewritten if it touches a state or
	 * pseudo-state {@code node} badly and a rewrite is possible; otherwise
	 * {@code path} itself. {@code far} is the node at the other end, or null.
	 */
	DotPath alignStart(DotPath path, SvekNode node, SvekNode far) {
		if (isStemNode(node) == false)
			return path;

		final List<XPoint2D> points = BorderPointPath.toOrthogonalPolyline(path);
		if (points == null)
			return path;

		final RectangleArea box = node.getRectangleArea();
		// Graphviz clips an edge to the outline of a round node, inside its box.
		final double inside = isRound(node.getType()) ? Math.min(box.getWidth(), box.getHeight()) / 2 : EPSILON;
		final Frame frame = Frame.of(box, points.get(0), points.get(1), inside);
		if (frame == null)
			return path;

		// Worked out as if the edge left the bottom side of the node.
		final RectangleArea local = frame.toLocal(box);
		final double bottom = local.getMaxY();
		final List<XPoint2D> q = normalize(frame.toLocal(points), bottom);
		if (q == null)
			return path;

		final double[] range = range(node.getType(), local, CORNER);
		final double min = range[0];
		final double max = range[1];

		// A round node has one point per side: edges there share it only when
		// every other point is taken.
		final boolean round = isRound(node.getType());
		final double x0 = q.get(0).getX();
		final boolean perpendicular = same(x0, q.get(1).getX());
		final boolean fine = perpendicular && q.get(1).getY() - bottom >= STEM && x0 >= min - EPSILON
				&& x0 <= max + EPSILON;
		if (fine && (round == false || sideTaken(box, inside, points) == false))
			return path;

		final RectangleArea farBox = far == null ? null : far.getRectangleArea();
		// The far end keeps a stem of its own; into an edge label, a short one will do.
		final double farStem = isStemNode(far) ? STEM : SHORT_STEM;
		final List<Double> slots = slots(x0, range);
		for (double x : slots(x0, range(node.getType(), local, TIGHT_CORNER)))
			if (slots.contains(x) == false)
				slots.add(x);

		final int s = perpendicular ? 1 : 0;
		final List<List<XPoint2D>> candidates = candidates(q, s, bottom, min, max, slots);

		// Last resort: leave from the neighbouring side the edge turns towards,
		// either straight to where it turns next, or a stem away and then back
		// onto its way - as long as can be, but even a short one beats sharing
		// the point of a round node.
		if (s + 1 < q.size()) {
			final XPoint2D runEnd = q.get(s + 1);
			final double dir = Math.signum(runEnd.getX() - x0);
			final double sideX = dir > 0 ? local.getMaxX() : local.getMinX();
			final double centerY = local.getPointCenter().getY();
			final double sideY = round ? centerY : Math.max(centerY, bottom - CORNER);
			final XPoint2D side = new XPoint2D(sideX, sideY);
			if (s + 2 < q.size() && (runEnd.getX() - sideX) * dir > 0)
				candidates.add(join(Arrays.asList(side, new XPoint2D(runEnd.getX(), sideY)),
						q.subList(s + 2, q.size())));
			final List<Double> stems = new ArrayList<>();
			for (int i = 0; i < LANES; i++)
				stems.add(STEM + i * LANE);
			for (double stem = STEM - LANE; stem >= SHORT_STEM - EPSILON; stem -= LANE)
				stems.add(stem);
			for (double stem : stems) {
				final double x = sideX + dir * stem;
				if ((runEnd.getX() - x) * dir > EPSILON)
					candidates.add(join(Arrays.asList(side, new XPoint2D(x, sideY), new XPoint2D(x, q.get(s).getY())),
							q.subList(s + 1, q.size())));
			}
			// Or the far side, going round the node.
			if (round) {
				final double farX = dir > 0 ? local.getMinX() : local.getMaxX();
				for (double stem : stems) {
					final double x = farX - dir * stem;
					candidates.add(join(Arrays.asList(new XPoint2D(farX, sideY), new XPoint2D(x, sideY),
							new XPoint2D(x, Math.max(q.get(s).getY(), bottom + STEM))), q.subList(s + 1, q.size())));
				}
			}
		}

		for (boolean share : round && fine == false ? new boolean[] { false, true } : new boolean[] { false })
			for (double stem : new double[] { STEM, SHORT_STEM })
				for (List<XPoint2D> candidate : candidates) {
					final List<XPoint2D> result = frame.toWorld(BorderPointPath.simplify(candidate));
					if (result.size() >= 2 && result.get(0).distance(result.get(1)) >= stem
							&& (round == false || share || sideTaken(box, inside, result) == false)
							&& acceptable(points, result, box, farBox, farStem, share ? box : null))
						return BorderPointPath.toDotPath(result);
				}

		return path;
	}

	/** Same as {@link #alignStart}, applied to the end of the path. */
	DotPath alignEnd(DotPath path, SvekNode node, SvekNode far) {
		final DotPath reversed = path.reverse();
		final DotPath aligned = alignStart(reversed, node, far);
		if (aligned == reversed)
			return path;

		return aligned.reverse();
	}

	/**
	 * Where on the side at the bottom of {@code local} a stem may leave, keeping
	 * {@code corner} away from the rounded corners of a state: {min, max}.
	 */
	private static double[] range(ShapeType type, RectangleArea local, double corner) {
		double min = local.getMinX();
		double max = local.getMaxX();
		if (type == ShapeType.ROUND_RECTANGLE) {
			min += corner;
			max -= corner;
		}
		if (isRound(type) || min > max) {
			min = local.getPointCenter().getX();
			max = min;
		}
		return new double[] { min, max };
	}

	/**
	 * Where a stem may leave the side within {@code range}: as near {@code x0} as
	 * allowed first, then one lane further at a time, so that a stem already
	 * there can be stepped past.
	 */
	private static List<Double> slots(double x0, double[] range) {
		final double min = range[0];
		final double max = range[1];
		final double x = Math.max(min, Math.min(max, x0));
		final List<Double> result = new ArrayList<>();
		result.add(x);
		for (int i = 1; x - i * LANE >= min - EPSILON || x + i * LANE <= max + EPSILON; i++) {
			if (x - i * LANE >= min - EPSILON)
				result.add(x - i * LANE);
			if (x + i * LANE <= max + EPSILON)
				result.add(x + i * LANE);
		}
		return result;
	}

	/** Whether edges should meet a node of this shape on its cardinal points. */
	private static boolean isRound(ShapeType type) {
		return type == ShapeType.CIRCLE || type == ShapeType.OVAL || type == ShapeType.DIAMOND;
	}

	private static boolean isStemNode(SvekNode node) {
		if (node == null || node.getEntityPosition() != EntityPosition.NORMAL)
			return false;

		final LeafType type = node.getLeafType();
		return type == LeafType.STATE || type == LeafType.CIRCLE_START || type == LeafType.CIRCLE_END
				|| type == LeafType.STATE_CHOICE || type == LeafType.PSEUDO_STATE || type == LeafType.DEEP_HISTORY
				|| type == LeafType.STATE_FORK_JOIN || type == LeafType.JUNCTION;
	}

	/**
	 * {@code points}, local to the side at {@code bottom}, made to start right on
	 * that side; or null if they do not start from it.
	 */
	private static List<XPoint2D> normalize(List<XPoint2D> points, double bottom) {
		final List<XPoint2D> result = new ArrayList<>(points);
		final XPoint2D p0 = result.get(0);
		final XPoint2D p1 = result.get(1);
		final double gap = p0.getY() - bottom;
		if (same(p0.getX(), p1.getX())) {
			// Leaves perpendicularly: must head away from the node.
			if (p1.getY() < bottom + EPSILON)
				return null;

			result.set(0, new XPoint2D(p0.getX(), bottom));
		} else if (result.size() == 2) {
			return null;
		} else if (gap < 1) {
			// Runs along the side itself.
			result.set(0, new XPoint2D(p0.getX(), bottom));
			result.set(1, new XPoint2D(p1.getX(), bottom));
		} else {
			// Runs along the side, a few pixels off it.
			result.add(0, new XPoint2D(p0.getX(), bottom));
		}
		return result;
	}

	/**
	 * Rewrites of {@code q} worth trying, most wanted first, all leaving the side
	 * at {@code bottom} from one of the {@code slots}, or between {@code min} and
	 * {@code max}. {@code q} turns sideways at index {@code s}: 1 after a short
	 * stem, 0 if it runs along the side from the start.
	 */
	private static List<List<XPoint2D>> candidates(List<XPoint2D> q, int s, double bottom, double min, double max,
			List<Double> slots) {
		final List<List<XPoint2D>> result = new ArrayList<>();
		final int last = q.size() - 1;
		if (s + 1 > last)
			return result;

		final double x0 = q.get(0).getX();
		final double run = q.get(s).getY();
		final XPoint2D runEnd = q.get(s + 1);
		final boolean outward = s + 2 <= last && q.get(s + 2).getY() > run + EPSILON;

		// Leave the side straight below the end of the sideways run.
		if (outward && runEnd.getX() >= min - EPSILON && runEnd.getX() <= max + EPSILON)
			result.add(join(Arrays.asList(new XPoint2D(runEnd.getX(), bottom)), q.subList(s + 2, last + 1)));

		// A long enough stem, too close to a corner: let the run absorb the move.
		if (s == 1)
			for (double x : slots)
				if (same(x, x0) == false)
					result.add(join(Arrays.asList(new XPoint2D(x, bottom), new XPoint2D(x, run)),
							q.subList(2, last + 1)));

		// Go straight out to the level of the next sideways run.
		if (outward && s + 3 <= last)
			for (double x : slots)
				result.add(join(Arrays.asList(new XPoint2D(x, bottom), new XPoint2D(x, q.get(s + 2).getY())),
						q.subList(s + 3, last + 1)));

		// Move the sideways run out, onto the first free lane.
		if (s + 2 <= last)
			for (double x : slots)
				for (int i = 0; i < LANES; i++) {
					final double y = bottom + STEM + i * LANE;
					if (y > run + EPSILON)
						result.add(join(Arrays.asList(new XPoint2D(x, bottom), new XPoint2D(x, y),
								new XPoint2D(runEnd.getX(), y)), q.subList(s + 2, last + 1)));
				}

		return result;
	}

	private static List<XPoint2D> join(List<XPoint2D> head, List<XPoint2D> tail) {
		final List<XPoint2D> result = new ArrayList<>(head);
		result.addAll(tail);
		return result;
	}

	/**
	 * Whether {@code after} may replace {@code before}: it reaches the far node
	 * the same way, leaving it a stem of {@code farStem} if it had one, and hits
	 * no more obstacles, frames and edges; it may share its first segment with
	 * the other edges ending at {@code shared}, if any.
	 */
	private boolean acceptable(List<XPoint2D> before, List<XPoint2D> after, RectangleArea own, RectangleArea far,
			double farStem, RectangleArea shared) {
		final XPoint2D b1 = before.get(before.size() - 2);
		final XPoint2D b2 = before.get(before.size() - 1);
		final XPoint2D a1 = after.get(after.size() - 2);
		final XPoint2D a2 = after.get(after.size() - 1);
		if (same(a2.getX(), b2.getX()) == false || same(a2.getY(), b2.getY()) == false)
			return false;
		// Same direction into the far node, and no shorter than wanted.
		final double lengthBefore = b1.distance(b2);
		final double lengthAfter = a1.distance(a2);
		if (same(a1.getX(), a2.getX()) != same(b1.getX(), b2.getX())
				|| (a2.getX() - a1.getX()) * (b2.getX() - b1.getX()) < 0
				|| (a2.getY() - a1.getY()) * (b2.getY() - b1.getY()) < 0
				|| lengthAfter < Math.min(lengthBefore, farStem) - EPSILON)
			return false;

		return hits(after, own, far) <= hits(before, own, far)
				&& overlaps(after, shared) <= overlaps(before, shared)
				&& crossings(after) <= crossings(before);
	}

	/**
	 * How many times {@code path} goes through an obstacle, through a frame, back
	 * through its own node or into the far one before its end.
	 */
	private int hits(List<XPoint2D> path, RectangleArea own, RectangleArea far) {
		int result = 0;
		for (int i = 0; i < path.size() - 1; i++) {
			final XPoint2D a = path.get(i);
			final XPoint2D b = path.get(i + 1);
			for (RectangleArea obstacle : obstacles)
				if (enters(obstacle, a, b))
					result++;
			for (RectangleArea frame : frames)
				if (inside(frame, a) != inside(frame, b) || (inside(frame, a) == false && enters(frame, a, b)))
					result++;
			if (i > 0 && enters(own, a, b))
				result++;
			if (far != null && i < path.size() - 2 && enters(far, a, b))
				result++;
		}
		return result;
	}

	/**
	 * How many segments of {@code path} run along one of the other edges. Other
	 * edges ending at the {@code shared} node, if any, may share the first
	 * segment of {@code path}.
	 */
	private int overlaps(List<XPoint2D> path, RectangleArea shared) {
		int result = 0;
		for (List<XPoint2D> other : others)
			for (int i = 0; i < path.size() - 1; i++)
				for (int j = 0; j < other.size() - 1; j++) {
					if (shared != null && i == 0 && (near(shared, other.get(j)) || near(shared, other.get(j + 1))))
						continue;
					if (overlap(path.get(i), path.get(i + 1), other.get(j), other.get(j + 1)))
						result++;
				}
		return result;
	}

	private int crossings(List<XPoint2D> path) {
		int result = 0;
		for (List<XPoint2D> other : others)
			for (int i = 0; i < path.size() - 1; i++)
				for (int j = 0; j < other.size() - 1; j++)
					if (cross(path.get(i), path.get(i + 1), other.get(j), other.get(j + 1))
							|| cross(other.get(j), other.get(j + 1), path.get(i), path.get(i + 1)))
						result++;
		return result;
	}

	/** Whether the axis-aligned segment from {@code a} to {@code b} goes into {@code area}. */
	private static boolean enters(RectangleArea area, XPoint2D a, XPoint2D b) {
		return Math.max(a.getX(), b.getX()) > area.getMinX() + EPSILON
				&& Math.min(a.getX(), b.getX()) < area.getMaxX() - EPSILON
				&& Math.max(a.getY(), b.getY()) > area.getMinY() + EPSILON
				&& Math.min(a.getY(), b.getY()) < area.getMaxY() - EPSILON;
	}

	/**
	 * Whether another edge already ends on the side of {@code box} that
	 * {@code path} starts from.
	 */
	private boolean sideTaken(RectangleArea box, double inside, List<XPoint2D> path) {
		final Frame frame = Frame.of(box, path.get(0), path.get(1), inside);
		for (List<XPoint2D> other : others) {
			final int n = other.size();
			if (n < 2)
				continue;
			if (near(box, other.get(0)) && Frame.of(box, other.get(0), other.get(1), inside) == frame)
				return true;
			if (near(box, other.get(n - 1)) && Frame.of(box, other.get(n - 1), other.get(n - 2), inside) == frame)
				return true;
		}
		return false;
	}

	/** Whether {@code pt} is where an edge ending at {@code area} would end. */
	private static boolean near(RectangleArea area, XPoint2D pt) {
		return pt.getX() >= area.getMinX() - TOUCH && pt.getX() <= area.getMaxX() + TOUCH
				&& pt.getY() >= area.getMinY() - TOUCH && pt.getY() <= area.getMaxY() + TOUCH;
	}

	private static boolean inside(RectangleArea area, XPoint2D pt) {
		return pt.getX() > area.getMinX() && pt.getX() < area.getMaxX() && pt.getY() > area.getMinY()
				&& pt.getY() < area.getMaxY();
	}

	/** Whether two parallel segments would be drawn on top of each other. */
	private static boolean overlap(XPoint2D a1, XPoint2D a2, XPoint2D b1, XPoint2D b2) {
		if (same(a1.getY(), a2.getY()) && same(b1.getY(), b2.getY()))
			return Math.abs(a1.getY() - b1.getY()) < OVERLAP && Math.min(Math.max(a1.getX(), a2.getX()),
					Math.max(b1.getX(), b2.getX())) - Math.max(Math.min(a1.getX(), a2.getX()),
							Math.min(b1.getX(), b2.getX())) > 1;
		if (same(a1.getX(), a2.getX()) && same(b1.getX(), b2.getX()))
			return Math.abs(a1.getX() - b1.getX()) < OVERLAP && Math.min(Math.max(a1.getY(), a2.getY()),
					Math.max(b1.getY(), b2.getY())) - Math.max(Math.min(a1.getY(), a2.getY()),
							Math.min(b1.getY(), b2.getY())) > 1;
		return false;
	}

	/** Whether horizontal {@code h1-h2} crosses vertical {@code v1-v2} away from their ends. */
	private static boolean cross(XPoint2D h1, XPoint2D h2, XPoint2D v1, XPoint2D v2) {
		if (same(h1.getY(), h2.getY()) == false || same(v1.getX(), v2.getX()) == false)
			return false;
		final double x = v1.getX();
		final double y = h1.getY();
		return x > Math.min(h1.getX(), h2.getX()) + EPSILON && x < Math.max(h1.getX(), h2.getX()) - EPSILON
				&& y > Math.min(v1.getY(), v2.getY()) + EPSILON && y < Math.max(v1.getY(), v2.getY()) - EPSILON;
	}

	/** The ends of the segments of {@code path}, curved or not. */
	private static List<XPoint2D> corners(DotPath path) {
		final List<XPoint2D> result = new ArrayList<>();
		final List<XCubicCurve2D> beziers = path.getBeziers();
		if (beziers.size() == 0)
			return result;

		result.add(beziers.get(0).getP1());
		for (XCubicCurve2D bez : beziers)
			result.add(bez.getP2());
		return BorderPointPath.simplify(result);
	}

	private static boolean same(double a, double b) {
		return BorderPointPath.same(a, b);
	}

	/**
	 * The four ways of turning the drawing so that a given side of a node becomes
	 * its bottom side, with the outside of the node downwards.
	 */
	private enum Frame {
		BOTTOM, TOP, RIGHT, LEFT;

		/**
		 * The side of {@code box} that an edge leaving from {@code p0} towards
		 * {@code p1} starts on, or null if it starts from none; it may start up to
		 * {@code inside} within the box. At a corner, the side the edge leaves at
		 * right angles wins.
		 */
		static Frame of(RectangleArea box, XPoint2D p0, XPoint2D p1, double inside) {
			Frame result = null;
			double best = TOUCH;
			for (Frame frame : values()) {
				final RectangleArea local = frame.toLocal(box);
				final XPoint2D l0 = frame.toLocal(p0);
				final double dy = l0.getY() - local.getMaxY();
				if (dy < -inside)
					continue;

				final double dx = Math.max(0, Math.max(local.getMinX() - l0.getX(), l0.getX() - local.getMaxX()));
				double distance = Math.sqrt(dx * dx + dy * dy);
				if (same(l0.getX(), frame.toLocal(p1).getX()))
					distance -= EPSILON;
				if (distance < best) {
					best = distance;
					result = frame;
				}
			}
			return result;
		}

		XPoint2D toLocal(XPoint2D pt) {
			switch (this) {
			case TOP:
				return new XPoint2D(pt.getX(), -pt.getY());
			case RIGHT:
				return new XPoint2D(pt.getY(), pt.getX());
			case LEFT:
				return new XPoint2D(pt.getY(), -pt.getX());
			default:
				return pt;
			}
		}

		XPoint2D toWorld(XPoint2D pt) {
			switch (this) {
			case TOP:
				return new XPoint2D(pt.getX(), -pt.getY());
			case RIGHT:
				return new XPoint2D(pt.getY(), pt.getX());
			case LEFT:
				return new XPoint2D(-pt.getY(), pt.getX());
			default:
				return pt;
			}
		}

		RectangleArea toLocal(RectangleArea area) {
			final XPoint2D a = toLocal(new XPoint2D(area.getMinX(), area.getMinY()));
			final XPoint2D b = toLocal(new XPoint2D(area.getMaxX(), area.getMaxY()));
			return new RectangleArea(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()),
					Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()));
		}

		List<XPoint2D> toLocal(List<XPoint2D> points) {
			final List<XPoint2D> result = new ArrayList<>();
			for (XPoint2D pt : points)
				result.add(toLocal(pt));
			return result;
		}

		List<XPoint2D> toWorld(List<XPoint2D> points) {
			final List<XPoint2D> result = new ArrayList<>();
			for (XPoint2D pt : points)
				result.add(toWorld(pt));
			return result;
		}
	}

}
