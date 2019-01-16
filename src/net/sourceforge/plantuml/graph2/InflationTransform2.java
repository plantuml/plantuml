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
package net.sourceforge.plantuml.graph2;

import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;
import java.util.TreeSet;

class Point2DComparatorDistance implements Comparator<Point2D> {

	private final Point2D center;

	public Point2DComparatorDistance(Point2D center) {
		this.center = center;
	}

	public int compare(Point2D p1, Point2D p2) {
		return Double.compare(p1.distance(center), p2.distance(center));
	}

}

public class InflationTransform2 implements IInflationTransform {

	private final List<InflateData2> inflateX = new ArrayList<InflateData2>();
	private final List<InflateData2> inflateY = new ArrayList<InflateData2>();

	public void addInflationX(double xpos, double inflation) {
		add(inflateX, xpos, inflation);
	}

	@Override
	public String toString() {
		return "inflateX = " + inflateX + " inflateY = " + inflateY;
	}

	public void addInflationY(double ypos, double inflation) {
		add(inflateY, ypos, inflation);
	}

	public double getTotalInflationX() {
		return sumInflation(inflateX);
	}

	public double getTotalInflationY() {
		return sumInflation(inflateY);
	}

	static private double sumInflation(List<InflateData2> list) {
		double result = 0;
		for (InflateData2 data : list) {
			result += data.getInflation();
		}
		return result;
	}

	static private void add(List<InflateData2> list, double ypos, double inflation) {
		for (final ListIterator<InflateData2> it = list.listIterator(); it.hasNext();) {
			final InflateData2 cur = it.next();
			if (cur.getPos() == ypos) {
				it.set(new InflateData2(ypos, Math.max(inflation, cur.getInflation())));
				return;
			}
		}
		list.add(new InflateData2(ypos, inflation));
		Collections.sort(list);
	}

	Collection<Point2D.Double> cutPoints(Line2D.Double original) {

		final SortedSet<Point2D.Double> result = new TreeSet<Point2D.Double>(new Point2DComparatorDistance(original
				.getP1()));

		if (GeomUtils.isHorizontal(original) == false) {
			for (InflateData2 x : inflateX) {
				final Line2D.Double vertical = new Line2D.Double(x.getPos(), GeomUtils.getMinY(original), x.getPos(),
						GeomUtils.getMaxY(original));
				final Point2D.Double inter = GeomUtils.getSegIntersection(original, vertical);
				if (inter != null) {
					result.add(inter);
				}
			}
		}
		if (GeomUtils.isVertical(original) == false) {
			for (InflateData2 y : inflateY) {
				final Line2D.Double horizontal = new Line2D.Double(GeomUtils.getMinX(original), y.getPos(), GeomUtils
						.getMaxX(original), y.getPos());
				final Point2D.Double inter = GeomUtils.getSegIntersection(original, horizontal);
				if (inter != null) {
					result.add(inter);
				}
			}
		}
		return result;
	}

	Collection<Line2D.Double> cutSegments(Line2D.Double original) {
		final List<Line2D.Double> result = new ArrayList<Line2D.Double>();
		Point2D.Double cur = (Point2D.Double) original.getP1();
		final Collection<Point2D.Double> cutPoints = cutPoints(original);
		for (Point2D.Double inter : cutPoints) {
			if (cur.equals(inter)) {
				continue;
			}
			result.add(new Line2D.Double(cur, inter));
			cur = inter;
		}
		if (cur.equals(original.getP2()) == false) {
			result.add(new Line2D.Double(cur, original.getP2()));
		}
		return result;
	}

	Collection<Line2D.Double> cutSegments(Collection<Line2D.Double> segments) {
		final List<Line2D.Double> result = new ArrayList<Line2D.Double>();
		for (Line2D.Double seg : segments) {
			result.addAll(cutSegments(seg));
		}
		return result;
	}

	// private Line2D.Double inflateSegment(Line2D.Double seg) {
	// // if (isOnGrid(seg.getP1()) && isOnGrid(seg.getP2())) {
	// // return new Line2D.Double(inflatePoint2D(seg.getP1()),
	// inflatePoint2D(seg.getP2()));
	// // }
	// // for (InflateData2 x : inflateX) {
	// // seg = x.inflateXAlpha(seg);
	// // }
	// // for (InflateData2 y : inflateY) {
	// // seg = y.inflateYAlpha(seg);
	// // }
	// // return seg;
	// return new Line2D.Double(inflatePoint2D(seg.getP1()),
	// inflatePoint2D(seg.getP2()));
	// }

	// private boolean isOnGrid(Point2D point) {
	// boolean onGrid = false;
	// for (InflateData2 x : inflateX) {
	// if (point.getX() == x.getPos()) {
	// onGrid = true;
	// }
	// }
	// if (onGrid == false) {
	// return false;
	// }
	// for (InflateData2 y : inflateY) {
	// if (point.getY() == y.getPos()) {
	// return true;
	// }
	// }
	// return false;
	//
	// }

	public Point2D inflatePoint2D(Point2D point) {
		return getAffineTransformAt(point).transform(point, null);
	}

	AffineTransform getAffineTransformAt(Point2D point) {
		double deltaX = 0;
		for (InflateData2 x : inflateX) {
			deltaX += x.inflateAt(point.getX());
		}
		double deltaY = 0;
		for (InflateData2 y : inflateY) {
			deltaY += y.inflateAt(point.getY());
		}
		return AffineTransform.getTranslateInstance(deltaX, deltaY);
	}

	List<Line2D.Double> inflateSegmentCollection(Collection<Line2D.Double> segments) {
		final List<Line2D.Double> result = new ArrayList<Line2D.Double>();
		for (Line2D.Double seg : segments) {
			final AffineTransform at = getAffineTransformAt(new Point2D.Double((seg.x1 + seg.x2) / 2,
					(seg.y1 + seg.y2) / 2));
			result.add(new Line2D.Double(at.transform(seg.getP1(), null), at.transform(seg.getP2(), null)));
		}
		return result;
	}

	public List<Line2D.Double> inflate(Collection<Line2D.Double> segments) {
		final Collection<Line2D.Double> cutSegments = cutSegments(segments);
		Line2D.Double last = null;
		final List<Line2D.Double> inflated = inflateSegmentCollection(cutSegments);
		final List<Line2D.Double> result = new ArrayList<Line2D.Double>();
		for (Line2D.Double seg : inflated) {
			if (last != null && last.getP2().equals(seg.getP1()) == false) {
				result.add(new Line2D.Double(last.getP2(), seg.getP1()));
			}
			result.add(seg);
			last = seg;

		}
		return result;
	}

}
