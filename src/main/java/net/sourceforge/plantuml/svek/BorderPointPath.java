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
import net.sourceforge.plantuml.svek.image.EntityImageStateBorder;

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
 * <p>
 * An edge running inside the frame leaves the point inwards; one running
 * outside leaves it outwards, past the point's label, turning back if the rest
 * of the edge lies the other way.
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
	 * is kept clear of {@code obstacles}, and a turn outside the frame clear of
	 * the runs of {@code others}.
	 */
	static DotPath alignStart(DotPath path, SvekNode node, SvekNode far, StringBounder stringBounder,
			List<RectangleArea> obstacles, List<DotPath> others) {
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

		final boolean inside = frame.contains(points.get(points.size() - 1));

		final boolean vertical;
		final double inward;
		double clearance = JOG;
		if (onTop || onBottom) {
			vertical = true;
			inward = onTop ? 1 : -1;
			if (inside && onTop)
				// The frame's title bar hangs below its top edge: a jog must clear it.
				clearance += cluster.getTitleAndAttributeHeight();
			else if (inside == false)
				// The label is drawn outside, one radius clear of the circle: an
				// edge leaving outwards runs through it, and must turn past it.
				clearance += EntityPosition.RADIUS + labelHeight(node, stringBounder);
		} else if (onLeft || onRight) {
			vertical = false;
			inward = onLeft ? 1 : -1;
		} else {
			return path;
		}
		final double dir = inside ? inward : -inward;

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
		List<XPoint2D> result = alignVertical(points, c, dir, clearance, target, obstacles);
		// Turning round is only worth it when the far end lies well back past the
		// point: one beside it, about level, is best reached from the side.
		final XPoint2D farEnd = points.get(points.size() - 1);
		final boolean farBack = (c.getY() - farEnd.getY()) * dir > clearance;
		if (result == null && inside == false && farBack)
			result = turnBack(points, c, dir, clearance, vertical ? frame : transpose(frame),
					withRuns(obstacles, others, vertical));
		if (result == null && inside == false && farBack)
			result = goRound(points, c, dir, clearance, vertical ? frame : transpose(frame),
					withRuns(obstacles, others, vertical));
		if (result == null)
			return path;

		// An edge leaving a point on the top or bottom of the frame outwards runs
		// through the label: move the label beside it, away from where it turns,
		// or away from the middle of the frame if it goes straight on.
		// Of the two sides, the one crossing fewer other edges wins.
		if (inside == false && vertical && node.getImage() instanceof EntityImageStateBorder) {
			final EntityImageStateBorder image = (EntityImageStateBorder) node.getImage();
			final double away = result.size() > 2 ? c.getX() - result.get(2).getX()
					: c.getX() - frame.getPointCenter().getX();
			final int preferred = away < 0 ? -1 : 1;
			final double width = image.getMaxWidthFromLabelForEntryExit(stringBounder);
			final double height = labelHeight(node, stringBounder);
			final int crossedPreferred = crossings(labelBox(c, dir, preferred, width, height), others);
			final int crossedOther = crossings(labelBox(c, dir, -preferred, width, height), others);
			image.setLabelSide(crossedOther < crossedPreferred ? -preferred : preferred);
		}

		return toDotPath(vertical ? result : transpose(result));
	}

	/** Same as {@link #alignStart}, applied to the end of the path. */
	static DotPath alignEnd(DotPath path, SvekNode node, SvekNode far, StringBounder stringBounder,
			List<RectangleArea> obstacles, List<DotPath> others) {
		final DotPath reversed = path.reverse();
		final DotPath aligned = alignStart(reversed, node, far, stringBounder, obstacles, others);
		if (aligned == reversed)
			return path;

		return aligned.reverse();
	}

	/**
	 * {@code obstacles} (already transposed when not {@code vertical}), plus a
	 * band one radius either side of each horizontal run of {@code others} (each
	 * vertical one when not {@code vertical}), so a new run does not lie on top of
	 * one already there.
	 */
	private static List<RectangleArea> withRuns(List<RectangleArea> obstacles, List<DotPath> others,
			boolean vertical) {
		final List<RectangleArea> result = new ArrayList<>(obstacles);
		for (DotPath other : others) {
			List<XPoint2D> points = toOrthogonalPolyline(other);
			if (points == null)
				continue;
			if (vertical == false)
				points = transpose(points);
			for (int i = 0; i + 1 < points.size(); i++) {
				final XPoint2D a = points.get(i);
				final XPoint2D b = points.get(i + 1);
				if (same(a.getY(), b.getY()))
					result.add(new RectangleArea(Math.min(a.getX(), b.getX()), a.getY() - EntityPosition.RADIUS,
							Math.max(a.getX(), b.getX()), a.getY() + EntityPosition.RADIUS));
			}
		}
		return result;
	}

	/**
	 * Where {@link EntityImageStateBorder} draws the label of the point centred
	 * on {@code c} when it moves it to {@code side} (-1 left, +1 right), outside
	 * the frame in the {@code dir} direction.
	 */
	private static RectangleArea labelBox(XPoint2D c, double dir, int side, double width, double height) {
		// Drawn from three radii above the centre upwards, or one below downwards.
		final double near = c.getY() + (dir < 0 ? -3 : 1) * EntityPosition.RADIUS;
		final double far = near + dir * height;
		final double x1 = side < 0 ? c.getX() - EntityImageStateBorder.LABEL_GAP - width
				: c.getX() + EntityImageStateBorder.LABEL_GAP;
		return new RectangleArea(x1, Math.min(near, far), x1 + width, Math.max(near, far));
	}

	/** How many segments of {@code others} run through {@code box}. */
	private static int crossings(RectangleArea box, List<DotPath> others) {
		int result = 0;
		for (DotPath other : others) {
			final List<XPoint2D> points = toOrthogonalPolyline(other);
			if (points == null)
				continue;
			for (int i = 0; i + 1 < points.size(); i++) {
				final XPoint2D a = points.get(i);
				final XPoint2D b = points.get(i + 1);
				if (Math.max(a.getX(), b.getX()) >= box.getMinX() && Math.min(a.getX(), b.getX()) <= box.getMaxX()
						&& Math.max(a.getY(), b.getY()) >= box.getMinY()
						&& Math.min(a.getY(), b.getY()) <= box.getMaxY())
					result++;
			}
		}
		return result;
	}

	private static double labelHeight(SvekNode node, StringBounder stringBounder) {
		if (node.getImage() instanceof EntityImageStateBorder)
			return ((EntityImageStateBorder) node.getImage()).getLabelHeightForEntryExit(stringBounder);
		return 0;
	}

	/**
	 * Rewrites the start of {@code points} so it leaves the circle centred on
	 * {@code c} in the {@code dir} direction, runs across {@code clearance} past
	 * the circle, and joins the first vertical segment of the path that lies
	 * outside {@code frame}, beside it. Whatever ran before, along the frame or
	 * inside it, is dropped. Returns null when there is no such segment.
	 */
	private static List<XPoint2D> turnBack(List<XPoint2D> points, XPoint2D c, double dir, double clearance,
			RectangleArea frame, List<RectangleArea> obstacles) {
		int k = -1;
		for (int i = 0; i + 1 < points.size(); i++) {
			final XPoint2D a = points.get(i);
			final XPoint2D b = points.get(i + 1);
			if (same(a.getX(), b.getX())
					&& (a.getX() < frame.getMinX() - EPSILON || a.getX() > frame.getMaxX() + EPSILON)) {
				k = i;
				break;
			}
		}
		if (k == -1)
			return null;

		final double x = points.get(k).getX();
		final XPoint2D start = new XPoint2D(c.getX(), c.getY() + dir * EntityPosition.RADIUS);
		double y = start.getY() + dir * clearance;
		// Stepping past one obstacle can land on another: repeat until clear.
		// y only ever moves away from the point, so this terminates.
		boolean moved = true;
		while (moved) {
			moved = false;
			for (RectangleArea obstacle : obstacles)
				if (crosses(obstacle, y, c.getX(), x)) {
					y = (dir > 0 ? obstacle.getMaxY() : obstacle.getMinY()) + dir * EntityPosition.RADIUS;
					moved = true;
				}
		}

		final List<XPoint2D> result = new ArrayList<>();
		result.add(start);
		result.add(new XPoint2D(c.getX(), y));
		result.add(new XPoint2D(x, y));
		result.addAll(points.subList(k + 1, points.size()));
		return simplify(result);
	}

	/**
	 * Rewrites the start of {@code points}, which runs inside {@code frame}
	 * before leaving it, so it leaves the circle centred on {@code c} in the
	 * {@code dir} direction, runs across {@code clearance} past the circle to
	 * just outside the nearer side of the frame, along that side, and joins the
	 * path where it leaves the frame for good. Returns null when the path does
	 * not run through the frame: one only running along it is left alone.
	 */
	private static List<XPoint2D> goRound(List<XPoint2D> points, XPoint2D c, double dir, double clearance,
			RectangleArea frame, List<RectangleArea> obstacles) {
		int m = points.size() - 1;
		if (isOutside(points.get(m), frame) == false)
			return null;
		while (m > 0 && isOutside(points.get(m - 1), frame))
			m--;
		if (m == 0)
			return null;

		boolean through = false;
		for (int i = 0; i < m; i++)
			through = through || runsThrough(points.get(i), points.get(i + 1), frame);
		if (through == false)
			return null;

		final XPoint2D q = points.get(m);
		final XPoint2D start = new XPoint2D(c.getX(), c.getY() + dir * EntityPosition.RADIUS);
		final boolean left = q.getX() < frame.getPointCenter().getX();
		double x = left ? frame.getMinX() - JOG : frame.getMaxX() + JOG;
		double y = start.getY() + dir * clearance;
		// Stepping past one obstacle can land on another: repeat until clear. x
		// and y only ever move away from the frame, so this terminates.
		boolean moved = true;
		while (moved) {
			moved = false;
			for (RectangleArea obstacle : obstacles) {
				if (crosses(obstacle, y, c.getX(), x)) {
					y = (dir > 0 ? obstacle.getMaxY() : obstacle.getMinY()) + dir * EntityPosition.RADIUS;
					moved = true;
				}
				if (x >= obstacle.getMinX() && x <= obstacle.getMaxX()
						&& Math.max(y, q.getY()) >= obstacle.getMinY() && Math.min(y, q.getY()) <= obstacle.getMaxY()) {
					x = left ? obstacle.getMinX() - EntityPosition.RADIUS : obstacle.getMaxX() + EntityPosition.RADIUS;
					moved = true;
				}
			}
		}

		final List<XPoint2D> result = new ArrayList<>();
		result.add(start);
		result.add(new XPoint2D(c.getX(), y));
		result.add(new XPoint2D(x, y));
		result.add(new XPoint2D(x, q.getY()));
		result.addAll(points.subList(m, points.size()));
		return simplify(result);
	}

	/** Whether the segment from {@code a} to {@code b} enters the inside of {@code frame}. */
	private static boolean runsThrough(XPoint2D a, XPoint2D b, RectangleArea frame) {
		return Math.max(a.getX(), b.getX()) > frame.getMinX() + EPSILON
				&& Math.min(a.getX(), b.getX()) < frame.getMaxX() - EPSILON
				&& Math.max(a.getY(), b.getY()) > frame.getMinY() + EPSILON
				&& Math.min(a.getY(), b.getY()) < frame.getMaxY() - EPSILON;
	}

	private static boolean isOutside(XPoint2D pt, RectangleArea frame) {
		return pt.getX() < frame.getMinX() - EPSILON || pt.getX() > frame.getMaxX() + EPSILON
				|| pt.getY() < frame.getMinY() - EPSILON || pt.getY() > frame.getMaxY() + EPSILON;
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
	static List<XPoint2D> toOrthogonalPolyline(DotPath path) {
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

	static boolean same(double a, double b) {
		return Math.abs(a - b) < EPSILON;
	}

	/** Drops repeated points and merges consecutive collinear segments. */
	static List<XPoint2D> simplify(List<XPoint2D> points) {
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

	static DotPath toDotPath(List<XPoint2D> points) {
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
