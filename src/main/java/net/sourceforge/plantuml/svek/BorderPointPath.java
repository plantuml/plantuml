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
import java.util.List;

import net.sourceforge.plantuml.abel.EntityPosition;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;

/**
 * Straightens the end of an orthogonal edge that touches an entry or exit
 * point.
 * <p>
 * An entry/exit point is drawn as a small circle centred on the frame of its
 * state, but the Graphviz node behind it is wider than the circle (it also
 * reserves room for the label), and orthogonal routing ignores the port that
 * would pin the edge to the circle. The edge therefore reaches the node
 * anywhere on that wider box: off-centre, or running along the frame. This
 * rewrites the segment next to the point so that it leaves (or reaches) the
 * circle's centre perpendicular to the frame edge the point sits on.
 */
final class BorderPointPath {

	private static final double EPSILON = 0.5;

	/** Distance kept between the frame and a jog added next to the point. */
	private static final double JOG = 2 * EntityPosition.RADIUS;

	private BorderPointPath() {
	}

	/**
	 * Returns {@code path}, with its start rewritten if {@code node} is an
	 * entry/exit point sitting on its cluster's frame, and the path is
	 * orthogonal. Otherwise returns {@code path} unchanged. {@code far} is the
	 * node at the other end of the path, or null; a jog added next to the point
	 * is kept clear of {@code obstacles}.
	 */
	static DotPath alignStart(DotPath path, SvekNode node, SvekNode far, StringBounder stringBounder,
			List<RectangleArea> obstacles) {
		if (node == null || isBorderPoint(node.getEntityPosition()) == false)
			return path;

		final Cluster cluster = node.getCluster();
		if (cluster == null || cluster.getRectangleArea() == null)
			return path;

		List<XPoint2D> points = toOrthogonalPolyline(path);
		if (points == null)
			return path;

		cluster.manageEntryExitPoint(stringBounder);
		final RectangleArea frame = cluster.getRectangleArea();
		final XPoint2D center = node.getRectangleArea().getPointCenter();

		final boolean onTop = Math.abs(center.getY() - frame.getMinY()) < EPSILON;
		final boolean onBottom = Math.abs(center.getY() - frame.getMaxY()) < EPSILON;
		final boolean onLeft = Math.abs(center.getX() - frame.getMinX()) < EPSILON;
		final boolean onRight = Math.abs(center.getX() - frame.getMaxX()) < EPSILON;

		// Only an edge running inside the frame is rewritten. One reaching the
		// point from outside would have to cross the point's own label, which
		// is drawn outside the frame, to leave it perpendicularly.
		if (frame.contains(points.get(points.size() - 1)) == false)
			return path;

		final boolean vertical;
		final double inward;
		double clearance = JOG;
		if (onTop || onBottom) {
			vertical = true;
			inward = onTop ? 1 : -1;
			// The frame's title bar hangs below its top edge: a jog must clear it.
			if (onTop)
				clearance += cluster.getTitleAndAttributeHeight();
		} else if (onLeft || onRight) {
			vertical = false;
			inward = onLeft ? 1 : -1;
		} else {
			return path;
		}

		// The rewrite is written for a vertical stem; a horizontal stem is the
		// same problem with x and y swapped.
		RectangleArea target = far == null ? null : far.getRectangleArea();
		if (vertical == false) {
			points = transpose(points);
			obstacles = transposeAreas(obstacles);
			if (target != null)
				target = transpose(target);
		}

		final XPoint2D c = vertical ? center : swap(center);
		final List<XPoint2D> result = alignVertical(points, c, inward, clearance, target, obstacles);
		if (result == null)
			return path;

		return toDotPath(vertical ? result : transpose(result));
	}

	/** Same as {@link #alignStart}, applied to the end of the path. */
	static DotPath alignEnd(DotPath path, SvekNode node, SvekNode far, StringBounder stringBounder,
			List<RectangleArea> obstacles) {
		final DotPath reversed = path.reverse();
		final DotPath aligned = alignStart(reversed, node, far, stringBounder, obstacles);
		if (aligned == reversed)
			return path;

		return aligned.reverse();
	}

	private static boolean isBorderPoint(EntityPosition position) {
		return position == EntityPosition.ENTRY_POINT || position == EntityPosition.EXIT_POINT;
	}

	/**
	 * Rewrites the start of {@code points} so it leaves the circle centred on
	 * {@code c} with a vertical stem heading in the {@code dir} direction (+1
	 * down, -1 up), or returns null when the path has a shape this does not
	 * handle.
	 */
	private static List<XPoint2D> alignVertical(List<XPoint2D> points, XPoint2D c, double dir, double clearance,
			RectangleArea target, List<RectangleArea> obstacles) {
		final XPoint2D start = new XPoint2D(c.getX(), c.getY() + dir * EntityPosition.RADIUS);

		final XPoint2D p0 = points.get(0);
		final XPoint2D p1 = points.get(1);
		final boolean firstIsVertical = Math.abs(p1.getX() - p0.getX()) < EPSILON;

		final List<XPoint2D> result = new ArrayList<>();
		result.add(start);
		if (firstIsVertical && points.size() > 2) {
			// Vertical stem, then a horizontal run: move the stem onto the centre
			// and let the horizontal run absorb the difference.
			if (beyond(p1.getY(), start.getY(), dir) == false)
				return null;

			result.add(new XPoint2D(c.getX(), p1.getY()));
			result.addAll(points.subList(2, points.size()));
		} else if (firstIsVertical) {
			// A single vertical segment: straight if the node at the other end
			// allows it, otherwise jog just inside the frame.
			if (beyond(p1.getY(), start.getY(), dir) == false)
				return null;

			if (canGoStraight(c.getX(), start.getY(), p1.getY(), target, obstacles)) {
				result.add(new XPoint2D(c.getX(), p1.getY()));
				return result;
			}
			if (Math.abs(p1.getX() - c.getX()) >= EPSILON) {
				final double y = jogLevel(start.getY(), p1.getY(), c.getX(), p1.getX(), dir, clearance, obstacles);
				result.add(new XPoint2D(c.getX(), y));
				result.add(new XPoint2D(p1.getX(), y));
			}
			result.add(p1);
		} else if (points.size() > 2) {
			// A horizontal run along the frame, then a vertical one: leave the
			// circle vertically first, and make the horizontal run just inside.
			final XPoint2D p2 = points.get(2);
			if (beyond(p2.getY(), start.getY(), dir) == false)
				return null;

			final double y = jogLevel(start.getY(), p2.getY(), c.getX(), p1.getX(), dir, clearance, obstacles);
			result.add(new XPoint2D(c.getX(), y));
			result.add(new XPoint2D(p1.getX(), y));
			result.addAll(points.subList(2, points.size()));
		} else {
			return null;
		}
		return simplify(result);
	}

	/**
	 * Where to put a horizontal jog from {@code x1} to {@code x2}, somewhere
	 * between {@code from} and {@code to}: close to the frame, where Graphviz
	 * leaves a margin free of nodes, and past any of {@code obstacles} it would
	 * otherwise cross. Half-way if the gap is too short for that.
	 */
	private static double jogLevel(double from, double to, double x1, double x2, double dir, double clearance,
			List<RectangleArea> obstacles) {
		double y = from + dir * clearance;
		// Stepping past one obstacle can land on another: repeat until clear.
		// y only ever moves away from the frame, so this terminates.
		boolean moved = true;
		while (moved) {
			moved = false;
			for (RectangleArea obstacle : obstacles)
				if (crosses(obstacle, y, x1, x2)) {
					y = (dir > 0 ? obstacle.getMaxY() : obstacle.getMinY()) + dir * EntityPosition.RADIUS;
					moved = true;
				}
		}

		if (beyond(to, y + dir * clearance, dir))
			return y;
		return (from + to) / 2;
	}

	/**
	 * Whether a vertical segment at {@code x}, from {@code y1} to {@code y2},
	 * would reach {@code target} clear of its rounded corners without crossing
	 * any of {@code obstacles}.
	 */
	private static boolean canGoStraight(double x, double y1, double y2, RectangleArea target,
			List<RectangleArea> obstacles) {
		if (target == null || x < target.getMinX() + JOG || x > target.getMaxX() - JOG)
			return false;

		for (RectangleArea obstacle : obstacles)
			if (x >= obstacle.getMinX() && x <= obstacle.getMaxX() && Math.max(y1, y2) >= obstacle.getMinY()
					&& Math.min(y1, y2) <= obstacle.getMaxY())
				return false;
		return true;
	}

	private static boolean crosses(RectangleArea area, double y, double x1, double x2) {
		return y >= area.getMinY() && y <= area.getMaxY() && Math.max(x1, x2) >= area.getMinX()
				&& Math.min(x1, x2) <= area.getMaxX();
	}

	private static boolean beyond(double value, double ref, double dir) {
		return (value - ref) * dir > EPSILON;
	}

	/**
	 * The corners of {@code path}, or null if it is not made only of horizontal
	 * and vertical straight segments.
	 */
	private static List<XPoint2D> toOrthogonalPolyline(DotPath path) {
		final List<XCubicCurve2D> beziers = path.getBeziers();
		if (beziers.size() == 0)
			return null;

		final List<XPoint2D> points = new ArrayList<>();
		points.add(beziers.get(0).getP1());
		for (XCubicCurve2D bez : beziers) {
			final boolean horizontal = same(bez.y1, bez.y2) && same(bez.y1, bez.ctrly1) && same(bez.y1, bez.ctrly2);
			final boolean vertical = same(bez.x1, bez.x2) && same(bez.x1, bez.ctrlx1) && same(bez.x1, bez.ctrlx2);
			if (horizontal == false && vertical == false)
				return null;
			points.add(bez.getP2());
		}
		final List<XPoint2D> result = simplify(points);
		if (result.size() < 2)
			return null;
		return result;
	}

	private static boolean same(double a, double b) {
		return Math.abs(a - b) < EPSILON;
	}

	/** Drops repeated points and merges consecutive collinear segments. */
	private static List<XPoint2D> simplify(List<XPoint2D> points) {
		final List<XPoint2D> result = new ArrayList<>();
		for (XPoint2D pt : points) {
			if (result.size() > 0) {
				final XPoint2D last = result.get(result.size() - 1);
				if (same(last.getX(), pt.getX()) && same(last.getY(), pt.getY()))
					continue;
			}
			if (result.size() > 1) {
				final XPoint2D a = result.get(result.size() - 2);
				final XPoint2D b = result.get(result.size() - 1);
				final boolean collinear = (same(a.getX(), b.getX()) && same(b.getX(), pt.getX()))
						|| (same(a.getY(), b.getY()) && same(b.getY(), pt.getY()));
				if (collinear)
					result.remove(result.size() - 1);
			}
			result.add(pt);
		}
		return result;
	}

	private static DotPath toDotPath(List<XPoint2D> points) {
		final List<XCubicCurve2D> beziers = new ArrayList<>();
		for (int i = 0; i < points.size() - 1; i++) {
			final XPoint2D a = points.get(i);
			final XPoint2D b = points.get(i + 1);
			beziers.add(new XCubicCurve2D(a.getX(), a.getY(), a.getX(), a.getY(), b.getX(), b.getY(), b.getX(),
					b.getY()));
		}
		return DotPath.fromBeziers(beziers);
	}

	private static XPoint2D swap(XPoint2D pt) {
		return new XPoint2D(pt.getY(), pt.getX());
	}

	private static RectangleArea transpose(RectangleArea area) {
		return new RectangleArea(area.getMinY(), area.getMinX(), area.getMaxY(), area.getMaxX());
	}

	private static List<RectangleArea> transposeAreas(List<RectangleArea> areas) {
		final List<RectangleArea> result = new ArrayList<>();
		for (RectangleArea area : areas)
			result.add(transpose(area));
		return result;
	}

	private static List<XPoint2D> transpose(List<XPoint2D> points) {
		final List<XPoint2D> result = new ArrayList<>();
		for (XPoint2D pt : points)
			result.add(swap(pt));
		return result;
	}

}
