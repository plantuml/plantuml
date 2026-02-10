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
 * Contribution:  Miguel Esteves
 *
 *
 */
package net.sourceforge.plantuml.klimt.shape;

import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.geom.BezierUtils;
import net.sourceforge.plantuml.klimt.geom.EnsureVisible;
import net.sourceforge.plantuml.klimt.geom.MinFinder;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.Moveable;
import net.sourceforge.plantuml.klimt.geom.PointAndAngle;
import net.sourceforge.plantuml.klimt.geom.RectangleArea;
import net.sourceforge.plantuml.klimt.geom.USegmentType;
import net.sourceforge.plantuml.klimt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.klimt.geom.XLine2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;

public class DotPath implements UShape, Moveable {

	public static class TriPoints {
		public TriPoints(XPoint2D p1, XPoint2D p2, XPoint2D p) {
			x1 = p1.getX();
			y1 = p1.getY();
			x2 = p2.getX();
			y2 = p2.getY();
			x = p.getX();
			y = p.getY();
		}

		public final double x1;
		public final double y1;
		public final double x2;
		public final double y2;
		public final double x;
		public final double y;

		// @Override
		// public String toString() {
		// return "[" + x1 + "," + y1 + " " + x2 + "," + y2 + " " + x + "," + y
		// + "]";
		// }
	}

	private final List<XCubicCurve2D> beziers = new ArrayList<>();
	private String comment;
	private String codeLine;

	public DotPath copy() {
		final DotPath result = new DotPath();
		for (XCubicCurve2D c : this.beziers)
			result.beziers.add(new XCubicCurve2D(c.x1, c.y1, c.ctrlx1, c.ctrly1, c.ctrlx2, c.ctrly2, c.x2, c.y2));

		return result;
	}

	public static DotPath fromBeziers(List<XCubicCurve2D> beziers) {
		final DotPath result = new DotPath();
		result.beziers.addAll(Objects.requireNonNull(beziers));
		return result;
	}

	public DotPath() {
	}

	public DotPath addCurve(XPoint2D pt1, XPoint2D pt2, XPoint2D pt3, XPoint2D pt4) {
		final List<XCubicCurve2D> beziersNew = new ArrayList<>(beziers);
		beziersNew.add(new XCubicCurve2D(pt1.getX(), pt1.getY(), pt2.getX(), pt2.getY(), pt3.getX(), pt3.getY(),
				pt4.getX(), pt4.getY()));
		return fromBeziers(beziersNew);
	}

	public DotPath addCurve(XPoint2D pt2, XPoint2D pt3, XPoint2D pt4) {
		final XCubicCurve2D last = beziers.get(beziers.size() - 1);
		final XPoint2D p1 = last.getP2();
		return addCurve(p1, pt2, pt3, pt4);
	}

	// private final String print;

	public XPoint2D getStartPoint() {
		return beziers.get(0).getP1();
	}

	public Set<XPoint2D> sample() {
		final Set<XPoint2D> result = new HashSet<>();
		for (XCubicCurve2D bez : beziers)
			sample(bez, result);

		return Collections.unmodifiableSet(result);
	}

	private static void sample(XCubicCurve2D bez, Set<XPoint2D> result) {
		final XPoint2D p1 = bez.getCtrlP1();
		final XPoint2D p2 = bez.getCtrlP2();
		if (bez.getFlatnessSq() > 0.5 || p1.distance(p2) > 4) {
			final XCubicCurve2D left = XCubicCurve2D.none();
			final XCubicCurve2D right = XCubicCurve2D.none();
			bez.subdivide(left, right);
			sample(left, result);
			sample(right, result);
		} else {
			result.add(p1);
			result.add(p2);
		}
	}

	public PointAndAngle getMiddle() {
		XPoint2D result = null;
		double angle = 0;
		for (XCubicCurve2D bez : beziers) {
			final XCubicCurve2D left = XCubicCurve2D.none();
			final XCubicCurve2D right = XCubicCurve2D.none();
			bez.subdivide(left, right);
			final XPoint2D p1 = left.getP1();
			final XPoint2D p2 = left.getP2();
			final XPoint2D p3 = right.getP1();
			final XPoint2D p4 = right.getP2();
			if (result == null || getCost(p1) < getCost(result)) {
				result = p1;
				angle = BezierUtils.getStartingAngle(left);
			}
			if (getCost(p2) < getCost(result)) {
				result = p2;
				angle = BezierUtils.getEndingAngle(left);
			}
			if (getCost(p3) < getCost(result)) {
				result = p3;
				angle = BezierUtils.getStartingAngle(right);
			}
			if (getCost(p4) < getCost(result)) {
				result = p4;
				angle = BezierUtils.getEndingAngle(right);
			}
		}
		return new PointAndAngle(result, angle);
	}

	private double getCost(XPoint2D pt) {
		final XPoint2D start = getStartPoint();
		final XPoint2D end = getEndPoint();
		return pt.distanceSq(start) + pt.distanceSq(end);
	}

//	public void forceStartPoint(double x, double y) {
//		beziers.get(0).x1 = x;
//		beziers.get(0).y1 = y;
//		beziers.get(0).ctrlx1 = x;
//		beziers.get(0).ctrly1 = y;
//	}

	public void moveStartPoint(UTranslate move) {
		moveStartPoint(move.getDx(), move.getDy());
	}

	public void moveEndPoint(UTranslate move) {
		moveEndPoint(move.getDx(), move.getDy());
	}

	public void moveStartPoint(double dx, double dy) {
		if (beziers.size() > 1 && Math.sqrt(dx * dx + dy * dy) >= beziers.get(0).getLength()) {
			dx -= beziers.get(1).x1 - beziers.get(0).x1;
			dy -= beziers.get(1).y1 - beziers.get(0).y1;
			beziers.remove(0);
		}
		beziers.get(0).x1 += dx;
		beziers.get(0).y1 += dy;
		beziers.get(0).ctrlx1 += dx;
		beziers.get(0).ctrly1 += dy;
	}

	public XPoint2D getEndPoint() {
		return beziers.get(beziers.size() - 1).getP2();
	}

//	public void forceEndPoint(double x, double y) {
//		beziers.get(beziers.size() - 1).x2 = x;
//		beziers.get(beziers.size() - 1).y2 = y;
//		beziers.get(beziers.size() - 1).ctrlx2 = x;
//		beziers.get(beziers.size() - 1).ctrly2 = y;
//	}

	public void moveEndPoint(double dx, double dy) {
		beziers.get(beziers.size() - 1).x2 += dx;
		beziers.get(beziers.size() - 1).y2 += dy;
		beziers.get(beziers.size() - 1).ctrlx2 += dx;
		beziers.get(beziers.size() - 1).ctrly2 += dy;
	}

	public MinFinder getMinFinder() {
		final MinFinder result = new MinFinder();
		for (XCubicCurve2D c : beziers) {
			result.manage(c.x1, c.y1);
			result.manage(c.x2, c.y2);
			result.manage(c.ctrlx1, c.ctrly1);
			result.manage(c.ctrlx2, c.ctrly2);
		}
		return result;
	}

	public MinMax getMinMax() {
		MinMax result = MinMax.getEmpty(false);
		for (XCubicCurve2D c : beziers) {
			result = result.addPoint(c.x1, c.y1);
			result = result.addPoint(c.x2, c.y2);
			result = result.addPoint(c.ctrlx1, c.ctrly1);
			result = result.addPoint(c.ctrlx2, c.ctrly2);
		}
		return result;
	}

	public double getMinDist(XPoint2D ref) {
		double result = Double.MAX_VALUE;
		for (XCubicCurve2D c : beziers) {
			final double d1 = ref.distance(c.x1, c.y1);
			if (d1 < result)
				result = d1;

			final double d2 = ref.distance(c.x2, c.y2);
			if (d2 < result)
				result = d2;

			final double d3 = ref.distance(c.ctrlx1, c.ctrly1);
			if (d3 < result)
				result = d3;

			final double d4 = ref.distance(c.ctrlx2, c.ctrly2);
			if (d4 < result)
				result = d4;

		}
		return result;

	}

	private XLine2D getEndTangeante() {
		final XCubicCurve2D last = beziers.get(beziers.size() - 1);
		double dx = last.x2 - last.ctrlx2;
		double dy = last.y2 - last.ctrly2;
		if (dx == 0 && dy == 0) {
			dx = last.x2 - last.x1;
			dy = last.y2 - last.y1;
		}
		return new XLine2D(last.x2, last.y2, last.x2 + dx, last.y2 + dy);
	}

	public double getEndAngle() {
		final XLine2D tan = getEndTangeante();
		final double theta1 = Math.atan2(tan.getY2() - tan.getY1(), tan.getX2() - tan.getX1());
		return theta1;
	}

	public double getStartAngle() {
		final XLine2D tan = getStartTangeante();
		final double theta1 = Math.atan2(tan.getY2() - tan.getY1(), tan.getX2() - tan.getX1());
		return theta1;
	}

	private XLine2D getStartTangeante() {
		final XCubicCurve2D first = beziers.get(0);
		double dx = first.ctrlx1 - first.x1;
		double dy = first.ctrly1 - first.y1;
		if (dx == 0 && dy == 0) {
			dx = first.x2 - first.x1;
			dy = first.y2 - first.y1;
		}
		return new XLine2D(first.x1, first.y1, first.x1 + dx, first.y1 + dy);
	}

	public DotPath addBefore(XCubicCurve2D before) {
		final List<XCubicCurve2D> copy = new ArrayList<>(beziers);
		copy.add(0, before);
		return fromBeziers(copy);
	}

	private DotPath addBefore(DotPath other) {
		final List<XCubicCurve2D> copy = new ArrayList<>(beziers);
		copy.addAll(0, other.beziers);
		return fromBeziers(copy);
	}

	public DotPath addAfter(XCubicCurve2D after) {
		final List<XCubicCurve2D> copy = new ArrayList<>(beziers);
		copy.add(after);
		return fromBeziers(copy);
	}

	public DotPath addAfter(DotPath other) {
		final List<XCubicCurve2D> copy = new ArrayList<>(beziers);
		copy.addAll(other.beziers);
		return fromBeziers(copy);
	}

	public void draw(Graphics2D g2d, double x, double y) {
		final GeneralPath p = new GeneralPath();
		for (XCubicCurve2D bez : beziers) {
			final CubicCurve2D.Double bez2 = new CubicCurve2D.Double(x + bez.x1, y + bez.y1, x + bez.ctrlx1,
					y + bez.ctrly1, x + bez.ctrlx2, y + bez.ctrly2, x + bez.x2, y + bez.y2);
			p.append(bez2, true);
		}
		g2d.draw(p);
	}

	public void manageEnsureVisible(double x, double y, EnsureVisible visible) {
		for (XCubicCurve2D bez : beziers) {
			visible.ensureVisible(x + bez.x1, y + bez.y1);
			visible.ensureVisible(x + bez.x2, y + bez.y2);
		}

	}

	public UPath toUPath() {
		final UPath result = new UPath(comment, codeLine);
		boolean start = true;
		for (XCubicCurve2D bez : beziers) {
			if (start) {
				result.add(new double[] { bez.x1, bez.y1 }, USegmentType.SEG_MOVETO);
				start = false;
			}
			result.add(new double[] { bez.ctrlx1, bez.ctrly1, bez.ctrlx2, bez.ctrly2, bez.x2, bez.y2 },
					USegmentType.SEG_CUBICTO);

		}
		return result;
	}

	static public String toString(XCubicCurve2D c) {
		return "(" + c.x1 + "," + c.y1 + ") " + "(" + c.ctrlx1 + "," + c.ctrly1 + ") " + "(" + c.ctrlx2 + "," + c.ctrly2
				+ ") " + "(" + c.x2 + "," + c.y2 + ") ";

	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (XCubicCurve2D c : beziers) {
			sb.append(toString(c));
			sb.append(" - ");
		}
		return sb.toString();
	}

	public static XCubicCurve2D reverse(XCubicCurve2D curv) {
		return new XCubicCurve2D(curv.getX2(), curv.getY2(), curv.getCtrlX2(), curv.getCtrlY2(), curv.getCtrlX1(),
				curv.getCtrlY1(), curv.getX1(), curv.getY1());
	}

	public DotPath reverse() {
		final List<XCubicCurve2D> reverse = new ArrayList<>(beziers);
		Collections.reverse(reverse);
		final List<XCubicCurve2D> copy = new ArrayList<>();
		for (XCubicCurve2D cub : reverse)
			copy.add(reverse(cub));

		return fromBeziers(copy);

	}

	public void moveDelta(double deltaX, double deltaY) {
		for (int i = 0; i < beziers.size(); i++) {
			final XCubicCurve2D c = beziers.get(i);
			beziers.set(i, new XCubicCurve2D(c.x1 + deltaX, c.y1 + deltaY, c.ctrlx1 + deltaX, c.ctrly1 + deltaY,
					c.ctrlx2 + deltaX, c.ctrly2 + deltaY, c.x2 + deltaX, c.y2 + deltaY));
		}

	}

	public final List<XCubicCurve2D> getBeziers() {
		return Collections.unmodifiableList(beziers);
	}

	public DotPath simulateCompound(RectangleArea head, RectangleArea tail) {
		if (head == null && tail == null)
			return this;

		// System.err.println("head=" + head + " tail=" + tail);
		DotPath me = this;
		if (tail != null) {
			// System.err.println("beziers1=" + this.toString());
			if (tail.contains(getStartPoint())) {
				final DotPath result = new DotPath();
				int idx = 0;
				while (idx + 1 < this.beziers.size() && tail.contains(this.beziers.get(idx).getP2())) {
					if (tail.contains(this.beziers.get(idx).getP1()) == false)
						throw new IllegalStateException();

					idx++;
				}
				if (tail.contains(this.beziers.get(idx).getP2())) {
					// System.err.println("strange1");
				} else {
					assert tail.contains(this.beziers.get(idx).getP1());
					assert tail.contains(this.beziers.get(idx).getP2()) == false;
					XCubicCurve2D current = this.beziers.get(idx);
					for (int k = 0; k < 8; k++) {
						final XCubicCurve2D part1 = XCubicCurve2D.none();
						final XCubicCurve2D part2 = XCubicCurve2D.none();
						current.subdivide(part1, part2);
						assert part1.getP2().equals(part2.getP1());
						if (tail.contains(part1.getP2())) {
							current = part2;
						} else {
							result.beziers.add(0, part2);
							current = part1;
						}
					}
					for (int i = idx + 1; i < this.beziers.size(); i++)
						result.beziers.add(this.beziers.get(i));

					me = result;
				}
			}
		}
		if (head != null) {
			final DotPath result = new DotPath();
			if (head.contains(getEndPoint())) {
				for (XCubicCurve2D current : me.beziers) {
					if (head.contains(current.getP2()) == false) {
						result.beziers.add(current);
					} else {
						if (head.contains(current.getP1())) {
							return me;
						}
						assert head.contains(current.getP1()) == false;
						assert head.contains(current.getP2());
						for (int k = 0; k < 8; k++) {
							final XCubicCurve2D part1 = XCubicCurve2D.none();
							final XCubicCurve2D part2 = XCubicCurve2D.none();
							current.subdivide(part1, part2);
							assert part1.getP2().equals(part2.getP1());
							if (head.contains(part1.getP2())) {
								current = part1;
							} else {
								result.beziers.add(part1);
								current = part2;
							}
						}
						return result;
					}
				}
			}

		}
		return me;
	}

	public boolean isLine() {
		for (XCubicCurve2D curve : beziers)
			if (curve.getFlatnessSq() > 0.001)
				return false;

		return true;
	}

	public void setCommentAndCodeLine(String comment, String codeLine) {
		this.comment = comment;
		this.codeLine = codeLine;
	}

	/**
	 * Round corners in orthogonal paths by replacing sharp 90-degree corners
	 * with smooth curves.
	 *
	 * @param radius Corner radius in pixels
	 */
	public void muteToRoundOrthogonalPaths(double radius) {
		if (radius <= 0 || beziers.size() < 2)
			return;

		if (!isOrthogonalPathInternal())
			return;

		// Build list of all points in the path
		final List<XPoint2D> points = new ArrayList<>();
		if (beziers.size() > 0) {
			points.add(beziers.get(0).getP1());
			for (XCubicCurve2D bez : beziers)
				points.add(bez.getP2());
		}

		if (points.size() < 3)
			return;

		// Detect corners
		final List<Integer> corners = detectCorners(points);
		if (corners.isEmpty())
			return;

		// Build new path with rounded corners
		final List<XPoint2D> newPoints = buildRoundedPath(points, corners, radius);

		// Build beziers from new points
		final List<XCubicCurve2D> newBeziers = buildBeziersFromPoints(newPoints, points, corners);

		// Replace beziers
		beziers.clear();
		beziers.addAll(newBeziers);
	}

	/**
	 * Detect corners in the path where segments meet at 90 degrees
	 */
	private List<Integer> detectCorners(List<XPoint2D> points) {
		final List<Integer> corners = new ArrayList<>();
		for (int i = 1; i < points.size() - 1; i++) {
			final XPoint2D prev = points.get(i - 1);
			final XPoint2D curr = points.get(i);
			final XPoint2D next = points.get(i + 1);

			// Calculate direction vectors
			double dx1 = curr.getX() - prev.getX();
			double dy1 = curr.getY() - prev.getY();
			double dx2 = next.getX() - curr.getX();
			double dy2 = next.getY() - curr.getY();

			final double len1 = Math.sqrt(dx1 * dx1 + dy1 * dy1);
			final double len2 = Math.sqrt(dx2 * dx2 + dy2 * dy2);

			if (len1 < 0.01 || len2 < 0.01)
				continue;

			// Normalize
			dx1 /= len1;
			dy1 /= len1;
			dx2 /= len2;
			dy2 /= len2;

			// Check if perpendicular (dot product ≈ 0 for 90 degrees)
			final double dotProduct = dx1 * dx2 + dy1 * dy2;
			if (Math.abs(dotProduct) < 0.1)
				corners.add(i);
		}
		return corners;
	}

	/**
	 * Build new path with truncated corners ready for rounding
	 */
	private List<XPoint2D> buildRoundedPath(List<XPoint2D> points, List<Integer> corners, double radius) {
		final List<XPoint2D> newPoints = new ArrayList<>();
		newPoints.add(points.get(0)); // Start point

		for (int i = 1; i < points.size() - 1; i++) {
			if (corners.contains(i)) {
				// This is a corner - add truncated points around it
				final XPoint2D prev = points.get(i - 1);
				final XPoint2D curr = points.get(i);
				final XPoint2D next = points.get(i + 1);

				// Direction vectors
				double dx1 = curr.getX() - prev.getX();
				double dy1 = curr.getY() - prev.getY();
				double dx2 = next.getX() - curr.getX();
				double dy2 = next.getY() - curr.getY();

				final double len1 = Math.sqrt(dx1 * dx1 + dy1 * dy1);
				final double len2 = Math.sqrt(dx2 * dx2 + dy2 * dy2);

				dx1 /= len1;
				dy1 /= len1;
				dx2 /= len2;
				dy2 /= len2;

				final double truncDist1 = Math.min(radius, len1 / 2);
				final double truncDist2 = Math.min(radius, len2 / 2);

				// Add point before corner
				final double beforeX = curr.getX() - dx1 * truncDist1;
				final double beforeY = curr.getY() - dy1 * truncDist1;
				newPoints.add(new XPoint2D(beforeX, beforeY));

				// Add the corner point itself (will be used as control point)
				newPoints.add(curr);

				// Add point after corner
				final double afterX = curr.getX() + dx2 * truncDist2;
				final double afterY = curr.getY() + dy2 * truncDist2;
				newPoints.add(new XPoint2D(afterX, afterY));
			} else {
				// Regular point
				newPoints.add(points.get(i));
			}
		}

		newPoints.add(points.get(points.size() - 1)); // End point
		return newPoints;
	}

	/**
	 * Build bezier curves from the rounded path points
	 */
	private List<XCubicCurve2D> buildBeziersFromPoints(List<XPoint2D> newPoints, List<XPoint2D> originalPoints,
			List<Integer> corners) {
		final List<XCubicCurve2D> newBeziers = new ArrayList<>();
		for (int i = 0; i < newPoints.size() - 1; i++) {
			final XPoint2D p1 = newPoints.get(i);
			final XPoint2D p2 = newPoints.get(i + 1);

			// Check if next point is a control point (corner)
			if (i + 2 < newPoints.size()) {
				final XPoint2D p3 = newPoints.get(i + 2);

				// Check if p2 was originally a corner point
				boolean isCornerControl = false;
				for (int cornerIdx : corners) {
					if (originalPoints.get(cornerIdx).equals(p2)) {
						isCornerControl = true;
						break;
					}
				}

				if (isCornerControl) {
					// Create rounded corner with p2 as control point
					// For circular arc approximation
					final double dx1 = p2.getX() - p1.getX();
					final double dy1 = p2.getY() - p1.getY();
					final double dx2 = p3.getX() - p2.getX();
					final double dy2 = p3.getY() - p2.getY();
					final double len1 = Math.sqrt(dx1 * dx1 + dy1 * dy1);
					final double len2 = Math.sqrt(dx2 * dx2 + dy2 * dy2);

					final double controlDist = Math.min(len1, len2) * 0.55228;

					final double ctrl1X = p1.getX() + (dx1 / len1) * controlDist;
					final double ctrl1Y = p1.getY() + (dy1 / len1) * controlDist;
					final double ctrl2X = p3.getX() - (dx2 / len2) * controlDist;
					final double ctrl2Y = p3.getY() - (dy2 / len2) * controlDist;

					newBeziers.add(new XCubicCurve2D(p1.getX(), p1.getY(), ctrl1X, ctrl1Y,
							ctrl2X, ctrl2Y, p3.getX(), p3.getY()));

					i++; // Skip the control point and the next point
					continue;
				}
			}

			// Regular straight segment
			newBeziers.add(new XCubicCurve2D(p1.getX(), p1.getY(), p1.getX(), p1.getY(),
					p2.getX(), p2.getY(), p2.getX(), p2.getY()));
		}
		return newBeziers;
	}

	/**
	 * Check if path consists of orthogonal (horizontal/vertical) segments
	 */
	private boolean isOrthogonalPathInternal() {
		for (XCubicCurve2D curve : beziers) {
			final double dx = Math.abs(curve.x2 - curve.x1);
			final double dy = Math.abs(curve.y2 - curve.y1);

			// Check if segment is either horizontal or vertical (not diagonal)
			if (dx > 0.1 && dy > 0.1)
				return false;
		}
		return true;
	}

}
