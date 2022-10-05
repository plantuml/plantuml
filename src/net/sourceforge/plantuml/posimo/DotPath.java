/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.posimo;

import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.EnsureVisible;
import net.sourceforge.plantuml.asciiart.BasicCharArea;
import net.sourceforge.plantuml.awt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.awt.geom.XPoint2D;
import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.ClusterPosition;
import net.sourceforge.plantuml.svek.MinFinder;
import net.sourceforge.plantuml.svek.PointAndAngle;
import net.sourceforge.plantuml.svek.SvgResult;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.USegmentType;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DotPath implements UShape, Moveable {

	static class TriPoints {
		public TriPoints(XPoint2D p1, XPoint2D p2, XPoint2D p) {
			x1 = p1.getX();
			y1 = p1.getY();
			x2 = p2.getX();
			y2 = p2.getY();
			x = p.getX();
			y = p.getY();
		}

		private final double x1;
		private final double y1;
		private final double x2;
		private final double y2;
		private final double x;
		private final double y;

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

	private static DotPath fromBeziers(List<XCubicCurve2D> beziers) {
		final DotPath result = new DotPath();
		result.beziers.addAll(Objects.requireNonNull(beziers));
		return result;
	}

	public DotPath() {
	}

	public DotPath(SvgResult fullSvg) {
		if (isPathConsistent(fullSvg.getSvg()) == false)
			throw new IllegalArgumentException();

		final int posC = fullSvg.indexOf("C", 0);
		if (posC == -1)
			throw new IllegalArgumentException();

		final XPoint2D start = fullSvg.substring(1, posC).getNextPoint();

		final List<TriPoints> triPoints = new ArrayList<>();
		for (Iterator<XPoint2D> it = fullSvg.substring(posC + 1).getPoints(" ").iterator(); it.hasNext();) {
			final XPoint2D p1 = it.next();
			final XPoint2D p2 = it.next();
			final XPoint2D p = it.next();
			triPoints.add(new TriPoints(p1, p2, p));
		}
		double x = start.getX();
		double y = start.getY();
		for (TriPoints p : triPoints) {
			final XCubicCurve2D bezier = new XCubicCurve2D(x, y, p.x1, p.y1, p.x2, p.y2, p.x, p.y);
			beziers.add(bezier);
			x = p.x;
			y = p.y;
		}
		// this.print = triPoints.toString();
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

	public static boolean isPathConsistent(String init) {
		if (init.startsWith("M") == false)
			return false;

		return true;
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
			final XCubicCurve2D left = new XCubicCurve2D();
			final XCubicCurve2D right = new XCubicCurve2D();
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
			final XCubicCurve2D left = new XCubicCurve2D();
			final XCubicCurve2D right = new XCubicCurve2D();
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

	public void forceStartPoint(double x, double y) {
		beziers.get(0).x1 = x;
		beziers.get(0).y1 = y;
		beziers.get(0).ctrlx1 = x;
		beziers.get(0).ctrly1 = y;
	}

	public void moveStartPoint(double dx, double dy) {
		beziers.get(0).x1 += dx;
		beziers.get(0).y1 += dy;
		beziers.get(0).ctrlx1 += dx;
		beziers.get(0).ctrly1 += dy;
	}

	public XPoint2D getEndPoint() {
		return beziers.get(beziers.size() - 1).getP2();
	}

	public void forceEndPoint(double x, double y) {
		beziers.get(beziers.size() - 1).x2 = x;
		beziers.get(beziers.size() - 1).y2 = y;
		beziers.get(beziers.size() - 1).ctrlx2 = x;
		beziers.get(beziers.size() - 1).ctrly2 = y;
	}

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

	public Line2D getEndTangeante() {
		final XCubicCurve2D last = beziers.get(beziers.size() - 1);
		double dx = last.x2 - last.ctrlx2;
		double dy = last.y2 - last.ctrly2;
		if (dx == 0 && dy == 0) {
			dx = last.x2 - last.x1;
			dy = last.y2 - last.y1;
		}
		return new Line2D.Double(last.x2, last.y2, last.x2 + dx, last.y2 + dy);
	}

	public double getEndAngle() {
		final Line2D tan = getEndTangeante();
		final double theta1 = Math.atan2(tan.getY2() - tan.getY1(), tan.getX2() - tan.getX1());
		return theta1;
	}

	public double getStartAngle() {
		final Line2D tan = getStartTangeante();
		final double theta1 = Math.atan2(tan.getY2() - tan.getY1(), tan.getX2() - tan.getX1());
		return theta1;
	}

	public Line2D getStartTangeante() {
		final XCubicCurve2D first = beziers.get(0);
		double dx = first.ctrlx1 - first.x1;
		double dy = first.ctrly1 - first.y1;
		if (dx == 0 && dy == 0) {
			dx = first.x2 - first.x1;
			dy = first.y2 - first.y1;
		}
		return new Line2D.Double(first.x1, first.y1, first.x1 + dx, first.y1 + dy);
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

	public void drawOk(EpsGraphics eps, double x, double y) {
		// boolean first = true;
		for (XCubicCurve2D bez : beziers) {
			bez = new XCubicCurve2D(x + bez.x1, y + bez.y1, x + bez.ctrlx1, y + bez.ctrly1, x + bez.ctrlx2,
					y + bez.ctrly2, x + bez.x2, y + bez.y2);
			eps.epsLine(bez.x1, bez.y1, bez.x2, bez.y2);
		}
	}

	public void draw(EpsGraphics eps, double x, double y) {
		eps.newpathDot();
		final boolean dashed = false;
		boolean first = true;
		for (XCubicCurve2D bez : beziers) {
			bez = new XCubicCurve2D(x + bez.x1, y + bez.y1, x + bez.ctrlx1, y + bez.ctrly1, x + bez.ctrlx2,
					y + bez.ctrly2, x + bez.x2, y + bez.y2);
			if (first) {
				eps.movetoNoMacro(bez.x1, bez.y1);
				first = dashed;
			}
			eps.curvetoNoMacro(bez.ctrlx1, bez.ctrly1, bez.ctrlx2, bez.ctrly2, bez.x2, bez.y2);
		}
		eps.closepathDot();
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

	public void draw(BasicCharArea area, double pixelXPerChar, double pixelYPerChar) {
		for (XCubicCurve2D bez : beziers)
			if (bez.x1 == bez.x2)
				area.drawVLine('|', (int) (bez.x1 / pixelXPerChar), (int) (bez.y1 / pixelYPerChar),
						(int) (bez.y2 / pixelYPerChar));
			else if (bez.y1 == bez.y2)
				area.drawHLine('-', (int) (bez.y1 / pixelYPerChar), (int) (bez.x1 / pixelXPerChar),
						(int) (bez.x2 / pixelXPerChar));

	}

	static String toString(XCubicCurve2D c) {
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

	public void moveSvek(double deltaX, double deltaY) {
		for (int i = 0; i < beziers.size(); i++) {
			final XCubicCurve2D c = beziers.get(i);
			beziers.set(i, new XCubicCurve2D(c.x1 + deltaX, c.y1 + deltaY, c.ctrlx1 + deltaX, c.ctrly1 + deltaY,
					c.ctrlx2 + deltaX, c.ctrly2 + deltaY, c.x2 + deltaX, c.y2 + deltaY));
		}

	}

	public final List<XCubicCurve2D> getBeziers() {
		return Collections.unmodifiableList(beziers);
	}

	public DotPath simulateCompound(Cluster head, Cluster tail) {
		// if (OptionFlags.USE_COMPOUND) {
		// throw new IllegalStateException();
		// }
		if (head == null && tail == null) {
			return this;
		}
		// System.err.println("head=" + head + " tail=" + tail);
		DotPath me = this;
		if (tail != null) {
			// System.err.println("beziers1=" + this.toString());
			final ClusterPosition clusterPosition = tail.getClusterPosition();
			if (clusterPosition.contains(getStartPoint())) {
				final DotPath result = new DotPath();
				int idx = 0;
				while (idx + 1 < this.beziers.size() && clusterPosition.contains(this.beziers.get(idx).getP2())) {
					if (clusterPosition.contains(this.beziers.get(idx).getP1()) == false)
						throw new IllegalStateException();

					idx++;
				}
				if (clusterPosition.contains(this.beziers.get(idx).getP2())) {
					// System.err.println("strange1");
				} else {
					assert clusterPosition.contains(this.beziers.get(idx).getP1());
					assert clusterPosition.contains(this.beziers.get(idx).getP2()) == false;
					XCubicCurve2D current = this.beziers.get(idx);
					for (int k = 0; k < 8; k++) {
						// System.err.println("length=" + length(current));
						final XCubicCurve2D part1 = new XCubicCurve2D();
						final XCubicCurve2D part2 = new XCubicCurve2D();
						current.subdivide(part1, part2);
						assert part1.getP2().equals(part2.getP1());
						if (clusterPosition.contains(part1.getP2())) {
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
			// System.err.println("beziers2=" + me.toString());
			final DotPath result = new DotPath();
			final ClusterPosition clusterPosition = head.getClusterPosition();
			if (clusterPosition.contains(getEndPoint())) {
				for (XCubicCurve2D current : me.beziers) {
					if (clusterPosition.contains(current.getP2()) == false) {
						result.beziers.add(current);
					} else {
						if (clusterPosition.contains(current.getP1())) {
							// System.err.println("strange2");
							return me;
						}
						assert clusterPosition.contains(current.getP1()) == false;
						assert clusterPosition.contains(current.getP2());
						for (int k = 0; k < 8; k++) {
							// System.err.println("length=" + length(current));
							final XCubicCurve2D part1 = new XCubicCurve2D();
							final XCubicCurve2D part2 = new XCubicCurve2D();
							current.subdivide(part1, part2);
							assert part1.getP2().equals(part2.getP1());
							if (clusterPosition.contains(part1.getP2())) {
								current = part1;
							} else {
								result.beziers.add(part1);
								current = part2;
								// System.err.println("k=" + k + " result=" + result.toString());
							}
						}
						// System.err.println("Final Result=" + result.toString());
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

}
