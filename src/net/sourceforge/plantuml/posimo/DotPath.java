/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
 *
 * 
 */
package net.sourceforge.plantuml.posimo;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.EnsureVisible;
import net.sourceforge.plantuml.asciiart.BasicCharArea;
import net.sourceforge.plantuml.eps.EpsGraphics;
import net.sourceforge.plantuml.geom.LineSegmentDouble;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.ClusterPosition;
import net.sourceforge.plantuml.svek.MinFinder;
import net.sourceforge.plantuml.svek.PointAndAngle;
import net.sourceforge.plantuml.svek.PointDirected;
import net.sourceforge.plantuml.svek.SvgResult;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.USegmentType;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DotPath implements UShape, Moveable {

	static class TriPoints {
		public TriPoints(Point2D.Double p1, Point2D.Double p2, Point2D.Double p) {
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

	private final List<CubicCurve2D.Double> beziers = new ArrayList<CubicCurve2D.Double>();
	private String comment;

	public DotPath() {
		this(new ArrayList<CubicCurve2D.Double>());
	}

	public DotPath(DotPath other) {
		this(new ArrayList<CubicCurve2D.Double>());
		for (CubicCurve2D.Double c : other.beziers) {
			this.beziers.add(new CubicCurve2D.Double(c.x1, c.y1, c.ctrlx1, c.ctrly1, c.ctrlx2, c.ctrly2, c.x2, c.y2));
		}
	}

	private DotPath(List<CubicCurve2D.Double> beziers) {
		this.beziers.addAll(beziers);
	}

	public DotPath addCurve(Point2D pt1, Point2D pt2, Point2D pt3, Point2D pt4) {
		final List<CubicCurve2D.Double> beziersNew = new ArrayList<CubicCurve2D.Double>(beziers);
		beziersNew.add(new CubicCurve2D.Double(pt1.getX(), pt1.getY(), pt2.getX(), pt2.getY(), pt3.getX(), pt3.getY(),
				pt4.getX(), pt4.getY()));
		return new DotPath(beziersNew);
	}

	public DotPath addCurve(Point2D pt2, Point2D pt3, Point2D pt4) {
		final CubicCurve2D.Double last = beziers.get(beziers.size() - 1);
		final Point2D p1 = last.getP2();
		return addCurve(p1, pt2, pt3, pt4);
	}

	private Point2D mirror(Point2D center, Point2D pt) {
		final double x = 2 * center.getX() - pt.getX();
		final double y = 2 * center.getY() - pt.getY();
		return new Point2D.Double(x, y);
	}

	public DotPath(SvgResult fullSvg) {
		if (isPathConsistent(fullSvg.getSvg()) == false) {
			throw new IllegalArgumentException();
		}
		final int posC = fullSvg.indexOf("C", 0);
		if (posC == -1) {
			throw new IllegalArgumentException();
		}
		final Point2D start = fullSvg.substring(1, posC).getNextPoint();

		final List<TriPoints> triPoints = new ArrayList<TriPoints>();
		for (Iterator<Point2D.Double> it = fullSvg.substring(posC + 1).getPoints(" ").iterator(); it.hasNext();) {
			final Point2D.Double p1 = it.next();
			final Point2D.Double p2 = it.next();
			final Point2D.Double p = it.next();
			triPoints.add(new TriPoints(p1, p2, p));
		}
		double x = start.getX();
		double y = start.getY();
		for (TriPoints p : triPoints) {
			final CubicCurve2D.Double bezier = new CubicCurve2D.Double(x, y, p.x1, p.y1, p.x2, p.y2, p.x, p.y);
			beziers.add(bezier);
			x = p.x;
			y = p.y;
		}
		// this.print = triPoints.toString();
	}

	public static boolean isPathConsistent(String init) {
		if (init.startsWith("M") == false) {
			return false;
		}
		return true;
	}

	// private final String print;

	public Point2D getStartPoint() {
		return beziers.get(0).getP1();
	}

	public Set<Point2D> sample() {
		final Set<Point2D> result = new HashSet<Point2D>();
		for (CubicCurve2D.Double bez : beziers) {
			sample(bez, result);
		}
		return Collections.unmodifiableSet(result);
	}

	private static void sample(CubicCurve2D bez, Set<Point2D> result) {
		final Point2D p1 = bez.getCtrlP1();
		final Point2D p2 = bez.getCtrlP2();
		if (bez.getFlatnessSq() > 0.5 || p1.distance(p2) > 4) {
			final CubicCurve2D.Double left = new CubicCurve2D.Double();
			final CubicCurve2D.Double right = new CubicCurve2D.Double();
			bez.subdivide(left, right);
			sample(left, result);
			sample(right, result);
		} else {
			result.add(p1);
			result.add(p2);
		}
	}

	public PointAndAngle getMiddle() {
		Point2D result = null;
		double angle = 0;
		for (CubicCurve2D.Double bez : beziers) {
			final CubicCurve2D.Double left = new CubicCurve2D.Double();
			final CubicCurve2D.Double right = new CubicCurve2D.Double();
			bez.subdivide(left, right);
			final Point2D p1 = left.getP1();
			final Point2D p2 = left.getP2();
			final Point2D p3 = right.getP1();
			final Point2D p4 = right.getP2();
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

	private double getCost(Point2D pt) {
		final Point2D start = getStartPoint();
		final Point2D end = getEndPoint();
		return pt.distanceSq(start) + pt.distanceSq(end);
	}

	public void forceStartPoint(double x, double y) {
		beziers.get(0).x1 = x;
		beziers.get(0).y1 = y;
		beziers.get(0).ctrlx1 = x;
		beziers.get(0).ctrly1 = y;
	}

	public Point2D getEndPoint() {
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
		for (CubicCurve2D.Double c : beziers) {
			result.manage(c.x1, c.y1);
			result.manage(c.x2, c.y2);
			result.manage(c.ctrlx1, c.ctrly1);
			result.manage(c.ctrlx2, c.ctrly2);
		}
		return result;
	}

	public MinMax getMinMax() {
		MinMax result = MinMax.getEmpty(false);
		for (CubicCurve2D.Double c : beziers) {
			result = result.addPoint(c.x1, c.y1);
			result = result.addPoint(c.x2, c.y2);
			result = result.addPoint(c.ctrlx1, c.ctrly1);
			result = result.addPoint(c.ctrlx2, c.ctrly2);
		}
		return result;
	}

	public double getMinDist(Point2D ref) {
		double result = Double.MAX_VALUE;
		for (CubicCurve2D.Double c : beziers) {
			final double d1 = ref.distance(c.x1, c.y1);
			if (d1 < result) {
				result = d1;
			}
			final double d2 = ref.distance(c.x2, c.y2);
			if (d2 < result) {
				result = d2;
			}
			final double d3 = ref.distance(c.ctrlx1, c.ctrly1);
			if (d3 < result) {
				result = d3;
			}
			final double d4 = ref.distance(c.ctrlx2, c.ctrly2);
			if (d4 < result) {
				result = d4;
			}
		}
		return result;

	}

	public Line2D getEndTangeante() {
		final CubicCurve2D.Double last = beziers.get(beziers.size() - 1);
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
		final CubicCurve2D.Double first = beziers.get(0);
		double dx = first.ctrlx1 - first.x1;
		double dy = first.ctrly1 - first.y1;
		if (dx == 0 && dy == 0) {
			dx = first.x2 - first.x1;
			dy = first.y2 - first.y1;
		}
		return new Line2D.Double(first.x1, first.y1, first.x1 + dx, first.y1 + dy);
	}

	public DotPath addBefore(CubicCurve2D.Double before) {
		final List<CubicCurve2D.Double> copy = new ArrayList<CubicCurve2D.Double>(beziers);
		copy.add(0, before);
		return new DotPath(copy);
	}

	private DotPath addBefore(DotPath other) {
		final List<CubicCurve2D.Double> copy = new ArrayList<CubicCurve2D.Double>(beziers);
		copy.addAll(0, other.beziers);
		return new DotPath(copy);
	}

	public DotPath addAfter(CubicCurve2D.Double after) {
		final List<CubicCurve2D.Double> copy = new ArrayList<CubicCurve2D.Double>(beziers);
		copy.add(after);
		return new DotPath(copy);
	}

	public DotPath addAfter(DotPath other) {
		final List<CubicCurve2D.Double> copy = new ArrayList<CubicCurve2D.Double>(beziers);
		copy.addAll(other.beziers);
		return new DotPath(copy);
	}

	public Map<Point2D, Double> somePoints() {
		final Map<Point2D, Double> result = new HashMap<Point2D, Double>();
		for (CubicCurve2D.Double bez : beziers) {
			final CubicCurve2D.Double left = new CubicCurve2D.Double();
			final CubicCurve2D.Double right = new CubicCurve2D.Double();
			bez.subdivide(left, right);
			result.put(left.getP1(), BezierUtils.getStartingAngle(left));
			result.put(left.getP2(), BezierUtils.getEndingAngle(left));
			result.put(right.getP1(), BezierUtils.getStartingAngle(right));
			result.put(right.getP2(), BezierUtils.getEndingAngle(right));
		}
		return result;
	}

	private PointDirected getIntersection(ClusterPosition position) {
		for (CubicCurve2D.Double bez : beziers) {
			final PointDirected result = position.getIntersection(bez);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	public void draw(Graphics2D g2d, double x, double y) {
		final GeneralPath p = new GeneralPath();
		for (CubicCurve2D.Double bez : beziers) {
			bez = new CubicCurve2D.Double(x + bez.x1, y + bez.y1, x + bez.ctrlx1, y + bez.ctrly1, x + bez.ctrlx2,
					y + bez.ctrly2, x + bez.x2, y + bez.y2);
			p.append(bez, true);
		}
		g2d.draw(p);
	}

	public void manageEnsureVisible(double x, double y, EnsureVisible visible) {
		for (CubicCurve2D.Double bez : beziers) {
			visible.ensureVisible(x + bez.x1, y + bez.y1);
			visible.ensureVisible(x + bez.x2, y + bez.y2);
		}

	}

	public void drawOk(EpsGraphics eps, double x, double y) {
		// boolean first = true;
		for (CubicCurve2D.Double bez : beziers) {
			bez = new CubicCurve2D.Double(x + bez.x1, y + bez.y1, x + bez.ctrlx1, y + bez.ctrly1, x + bez.ctrlx2,
					y + bez.ctrly2, x + bez.x2, y + bez.y2);
			eps.epsLine(bez.x1, bez.y1, bez.x2, bez.y2);
		}
	}

	public void draw(EpsGraphics eps, double x, double y) {
		eps.newpathDot();
		final boolean dashed = false;
		boolean first = true;
		for (CubicCurve2D.Double bez : beziers) {
			bez = new CubicCurve2D.Double(x + bez.x1, y + bez.y1, x + bez.ctrlx1, y + bez.ctrly1, x + bez.ctrlx2,
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
		final UPath result = new UPath(comment);
		boolean start = true;
		for (CubicCurve2D.Double bez : beziers) {
			if (start) {
				result.add(new double[] { bez.x1, bez.y1 }, USegmentType.SEG_MOVETO);
				start = false;
			}
			result.add(new double[] { bez.ctrlx1, bez.ctrly1, bez.ctrlx2, bez.ctrly2, bez.x2, bez.y2 },
					USegmentType.SEG_CUBICTO);

		}
		return result;
	}

	private Point2D getFrontierIntersection(Shape shape, Rectangle2D... notIn) {
		final List<CubicCurve2D.Double> all = new ArrayList<CubicCurve2D.Double>(beziers);
		for (int i = 0; i < 8; i++) {
			for (CubicCurve2D.Double immutable : all) {
				if (contains(immutable, notIn)) {
					continue;
				}
				final CubicCurve2D.Double bez = new CubicCurve2D.Double();
				bez.setCurve(immutable);
				if (BezierUtils.isCutting(bez, shape)) {
					while (BezierUtils.dist(bez) > 1.0) {
						BezierUtils.shorten(bez, shape);
					}
					final Point2D.Double result = new Point2D.Double((bez.x1 + bez.x2) / 2, (bez.y1 + bez.y2) / 2);
					if (contains(result, notIn) == false) {
						return result;
					}
				}
			}
			cutAllCubic(all);
		}
		throw new IllegalArgumentException("shape=" + shape);
	}

	private void cutAllCubic(List<CubicCurve2D.Double> all) {
		final List<CubicCurve2D.Double> tmp = new ArrayList<CubicCurve2D.Double>(all);
		all.clear();
		for (CubicCurve2D.Double bez : tmp) {
			final CubicCurve2D.Double left = new CubicCurve2D.Double();
			final CubicCurve2D.Double right = new CubicCurve2D.Double();
			bez.subdivide(left, right);
			all.add(left);
			all.add(right);
		}
	}

	static private boolean contains(Point2D.Double point, Rectangle2D... rects) {
		for (Rectangle2D r : rects) {
			if (r.contains(point)) {
				return true;
			}
		}
		return false;
	}

	static private boolean contains(CubicCurve2D.Double cubic, Rectangle2D... rects) {
		for (Rectangle2D r : rects) {
			if (r.contains(cubic.getP1()) && r.contains(cubic.getP2())) {
				return true;
			}
		}
		return false;
	}

	private DotPath manageRect(Rectangle2D start, Rectangle2D end) {
		final List<CubicCurve2D.Double> list = new ArrayList<CubicCurve2D.Double>(this.beziers);
		while (true) {
			if (BezierUtils.isCutting(list.get(0), start) == false) {
				throw new IllegalStateException();
			}
			if (BezierUtils.dist(list.get(0)) <= 1.0) {
				break;
			}
			final CubicCurve2D.Double left = new CubicCurve2D.Double();
			final CubicCurve2D.Double right = new CubicCurve2D.Double();
			list.get(0).subdivide(left, right);
			list.set(0, left);
			list.add(1, right);
			if (BezierUtils.isCutting(list.get(1), start)) {
				list.remove(0);
			}
		}
		return new DotPath(list);
	}

	private Point2D getFrontierIntersection(Positionable p) {
		return getFrontierIntersection(PositionableUtils.convert(p));
	}

	public void draw(BasicCharArea area, double pixelXPerChar, double pixelYPerChar) {
		for (CubicCurve2D.Double bez : beziers) {
			if (bez.x1 == bez.x2) {
				area.drawVLine('|', (int) (bez.x1 / pixelXPerChar), (int) (bez.y1 / pixelYPerChar),
						(int) (bez.y2 / pixelYPerChar));
			} else if (bez.y1 == bez.y2) {
				area.drawHLine('-', (int) (bez.y1 / pixelYPerChar), (int) (bez.x1 / pixelXPerChar),
						(int) (bez.x2 / pixelXPerChar));
			} /*
				 * else { throw new UnsupportedOperationException("bez=" + toString(bez)); }
				 */
		}
	}

	static String toString(CubicCurve2D.Double c) {
		return "(" + c.x1 + "," + c.y1 + ") " + "(" + c.ctrlx1 + "," + c.ctrly1 + ") " + "(" + c.ctrlx2 + "," + c.ctrly2
				+ ") " + "(" + c.x2 + "," + c.y2 + ") ";

	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (CubicCurve2D.Double c : beziers) {
			sb.append(toString(c));
			sb.append(" - ");
		}
		return sb.toString();
	}

	public static CubicCurve2D.Double reverse(CubicCurve2D curv) {
		return new CubicCurve2D.Double(curv.getX2(), curv.getY2(), curv.getCtrlX2(), curv.getCtrlY2(), curv.getCtrlX1(),
				curv.getCtrlY1(), curv.getX1(), curv.getY1());
	}

	public DotPath reverse() {
		final List<CubicCurve2D.Double> reverse = new ArrayList<CubicCurve2D.Double>(beziers);
		Collections.reverse(reverse);
		final List<CubicCurve2D.Double> copy = new ArrayList<CubicCurve2D.Double>();
		for (CubicCurve2D.Double cub : reverse) {
			copy.add(reverse(cub));
		}
		return new DotPath(copy);

	}

	public void moveSvek(double deltaX, double deltaY) {
		for (int i = 0; i < beziers.size(); i++) {
			final CubicCurve2D.Double c = beziers.get(i);
			beziers.set(i, new CubicCurve2D.Double(c.x1 + deltaX, c.y1 + deltaY, c.ctrlx1 + deltaX, c.ctrly1 + deltaY,
					c.ctrlx2 + deltaX, c.ctrly2 + deltaY, c.x2 + deltaX, c.y2 + deltaY));
		}

	}

	public final List<CubicCurve2D.Double> getBeziers() {
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
					if (clusterPosition.contains(this.beziers.get(idx).getP1()) == false) {
						throw new IllegalStateException();
					}
					idx++;
				}
				if (clusterPosition.contains(this.beziers.get(idx).getP2())) {
					// System.err.println("strange1");
				} else {
					assert clusterPosition.contains(this.beziers.get(idx).getP1());
					assert clusterPosition.contains(this.beziers.get(idx).getP2()) == false;
					CubicCurve2D current = this.beziers.get(idx);
					for (int k = 0; k < 8; k++) {
						// System.err.println("length=" + length(current));
						final CubicCurve2D.Double part1 = new CubicCurve2D.Double();
						final CubicCurve2D.Double part2 = new CubicCurve2D.Double();
						current.subdivide(part1, part2);
						assert part1.getP2().equals(part2.getP1());
						if (clusterPosition.contains(part1.getP2())) {
							current = part2;
						} else {
							result.beziers.add(0, part2);
							current = part1;
						}
					}
					for (int i = idx + 1; i < this.beziers.size(); i++) {
						result.beziers.add(this.beziers.get(i));
					}
					me = result;
				}
			}
		}
		if (head != null) {
			// System.err.println("beziers2=" + me.toString());
			final DotPath result = new DotPath();
			final ClusterPosition clusterPosition = head.getClusterPosition();
			if (clusterPosition.contains(getEndPoint())) {
				for (CubicCurve2D.Double current : me.beziers) {
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
							final CubicCurve2D.Double part1 = new CubicCurve2D.Double();
							final CubicCurve2D.Double part2 = new CubicCurve2D.Double();
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

	private double length(CubicCurve2D curve) {
		return curve.getP1().distance(curve.getP2());
	}

	public boolean isLine() {
		for (CubicCurve2D.Double curve : beziers) {
			if (curve.getFlatnessSq() > 0.001) {
				return false;
			}
		}
		return true;
	}

	public List<LineSegmentDouble> getLineSegments() {
		final List<LineSegmentDouble> result = new ArrayList<LineSegmentDouble>();
		for (CubicCurve2D.Double curve : beziers) {
			if (curve.getFlatnessSq() <= 0.001) {
				result.add(new LineSegmentDouble(curve));

			}
		}
		return Collections.unmodifiableList(result);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
