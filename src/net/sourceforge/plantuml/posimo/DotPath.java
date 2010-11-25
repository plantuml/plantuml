/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 4236 $
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
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.asciiart.BasicCharArea;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DotPath implements UShape {

	static class TriPoints {
		public TriPoints(String p1, String p2, String p, double deltaY) {
			final StringTokenizer st1 = new StringTokenizer(p1, ",");
			x1 = Double.parseDouble(st1.nextToken());
			y1 = Double.parseDouble(st1.nextToken()) + deltaY;
			final StringTokenizer st2 = new StringTokenizer(p2, ",");
			x2 = Double.parseDouble(st2.nextToken());
			y2 = Double.parseDouble(st2.nextToken()) + deltaY;
			final StringTokenizer st = new StringTokenizer(p, ",");
			x = Double.parseDouble(st.nextToken());
			y = Double.parseDouble(st.nextToken()) + deltaY;
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

	// private final String print;

	public Point2D getStartPoint() {
		return beziers.get(0).getP1();
	}

	public MinMax getMinMax() {
		final MinMax result = new MinMax();
		for (CubicCurve2D.Double c : beziers) {
			result.manage(c.x1, c.y1);
			result.manage(c.x2, c.y2);
			result.manage(c.ctrlx1, c.ctrly1);
			result.manage(c.ctrlx2, c.ctrly2);
		}
		return result;
	}

	public DotPath() {
		this(new ArrayList<CubicCurve2D.Double>());
	}

	public Point2D getEndPoint() {
		return beziers.get(beziers.size() - 1).getP2();
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

	public DotPath addBefore(DotPath other) {
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


	private DotPath(List<CubicCurve2D.Double> beziers) {
		this.beziers.addAll(beziers);
		// this.print = super.toString();
	}

	// @Override
	// public String toString() {
	// return print;
	// }

	public DotPath(String init, double deltaY) {
		if (init.startsWith("M") == false) {
			throw new IllegalArgumentException();
		}
		final int posC = init.indexOf("C");
		if (posC == -1) {
			throw new IllegalArgumentException();
		}
		final StringTokenizer st = new StringTokenizer(init.substring(1, posC), ",");
		final double startX = Double.parseDouble(st.nextToken());
		final double startY = Double.parseDouble(st.nextToken()) + deltaY;

		final StringTokenizer st2 = new StringTokenizer(init.substring(posC + 1), " ");
		final List<TriPoints> triPoints = new ArrayList<TriPoints>();
		while (st2.hasMoreTokens()) {
			final String p1 = st2.nextToken();
			final String p2 = st2.nextToken();
			final String p = st2.nextToken();
			triPoints.add(new TriPoints(p1, p2, p, deltaY));
		}
		double x = startX;
		double y = startY;
		for (TriPoints p : triPoints) {
			final CubicCurve2D.Double bezier = new CubicCurve2D.Double(x, y, p.x1, p.y1, p.x2, p.y2, p.x, p.y);
			beziers.add(bezier);
			x = p.x;
			y = p.y;
		}
		// this.print = triPoints.toString();
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

	// public void drawOld(Graphics2D g2d, double x, double y) {
	// for (CubicCurve2D.Double bez : beziers) {
	// bez = new CubicCurve2D.Double(x + bez.x1, y + bez.y1, x + bez.ctrlx1, y +
	// bez.ctrly1, x + bez.ctrlx2, y
	// + bez.ctrly2, x + bez.x2, y + bez.y2);
	// g2d.draw(bez);
	// }
	// }
	//
	public void draw(Graphics2D g2d, double x, double y) {
		final GeneralPath p = new GeneralPath();
		for (CubicCurve2D.Double bez : beziers) {
			bez = new CubicCurve2D.Double(x + bez.x1, y + bez.y1, x + bez.ctrlx1, y + bez.ctrly1, x + bez.ctrlx2, y
					+ bez.ctrly2, x + bez.x2, y + bez.y2);
			p.append(bez, true);
		}
		g2d.draw(p);
	}

	public Point2D getFrontierIntersection(Shape shape, Rectangle2D... notIn) {
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

	public DotPath manageRect(Rectangle2D start, Rectangle2D end) {
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

	public Point2D getFrontierIntersection(Positionable p) {
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
			} else {
				throw new UnsupportedOperationException("bez=" + toString(bez));
			}
		}
	}

	static String toString(CubicCurve2D.Double c) {
		return "(" + c.x1 + "," + c.y1 + ") " + "(" + c.ctrlx1 + "," + c.ctrly1 + ") " + "(" + c.ctrlx2 + ","
				+ c.ctrly2 + ") " + "(" + c.x2 + "," + c.y2 + ") ";

	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (CubicCurve2D.Double c : beziers) {
			sb.append(toString(c));
		}
		return sb.toString();
	}

	public static CubicCurve2D.Double reverse(CubicCurve2D curv) {
		return new CubicCurve2D.Double(curv.getX2(), curv.getY2(), curv.getCtrlX2(), curv.getCtrlY2(),
				curv.getCtrlX1(), curv.getCtrlY1(), curv.getX1(), curv.getY1());
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

}
