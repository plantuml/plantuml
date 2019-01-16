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
package net.sourceforge.plantuml.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;
import java.util.TreeSet;

class Point2DIntComparatorDistance implements Comparator<Point2DInt> {

	private final Point2DInt center;

	public Point2DIntComparatorDistance(Point2DInt center) {
		this.center = center;
	}

	public int compare(Point2DInt p1, Point2DInt p2) {
		return (int) Math.signum(p1.distance(center) - p2.distance(center));
	}

}

public class InflationTransform {

	private final List<InflateData> inflateX = new ArrayList<InflateData>();
	private final List<InflateData> inflateY = new ArrayList<InflateData>();

	public void addInflationX(int xpos, int inflation) {
		add(inflateX, xpos, inflation);
	}

	@Override
	public String toString() {
		return "inflateX = " + inflateX + " inflateY = " + inflateY;
	}

	public void addInflationY(int ypos, int inflation) {
		add(inflateY, ypos, inflation);
	}

	static private void add(List<InflateData> list, int ypos, int inflation) {
		for (final ListIterator<InflateData> it = list.listIterator(); it.hasNext();) {
			final InflateData cur = it.next();
			if (cur.getPos() == ypos) {
				it.set(new InflateData(ypos, Math.max(inflation, cur.getInflation())));
				return;
			}
		}
		list.add(new InflateData(ypos, inflation));
		Collections.sort(list);
	}

	Collection<Point2DInt> cutPoints(LineSegmentInt original) {

		// Log.println("original=" + original);
		// Log.println("inflateX=" + inflateX);
		// Log.println("inflateY=" + inflateY);

		final SortedSet<Point2DInt> result = new TreeSet<Point2DInt>(new Point2DIntComparatorDistance(original.getP1()));

		if (original.isHorizontal() == false) {
			for (InflateData x : inflateX) {
				final LineSegmentInt vertical = new LineSegmentInt(x.getPos(), original.getMinY(), x.getPos(), original
						.getMaxY());
				final Point2DInt inter = original.getSegIntersection(vertical);
				if (inter != null) {
					result.add(inter);
				}
			}
		}
		if (original.isVertical() == false) {
			for (InflateData y : inflateY) {
				final LineSegmentInt horizontal = new LineSegmentInt(original.getMinX(), y.getPos(),
						original.getMaxX(), y.getPos());
				final Point2DInt inter = original.getSegIntersection(horizontal);
				if (inter != null) {
					result.add(inter);
				}
			}
		}
		return result;
	}

	Collection<LineSegmentInt> cutSegments(LineSegmentInt original) {
		final List<LineSegmentInt> result = new ArrayList<LineSegmentInt>();
		Point2DInt cur = original.getP1();
		final Collection<Point2DInt> cutPoints = cutPoints(original);
		for (Point2DInt inter : cutPoints) {
			if (cur.equals(inter)) {
				continue;
			}
			result.add(new LineSegmentInt(cur, inter));
			cur = inter;
		}
		if (cur.equals(original.getP2()) == false) {
			result.add(new LineSegmentInt(cur, original.getP2()));
		}
		return result;
	}

	Collection<LineSegmentInt> cutSegments(Collection<LineSegmentInt> segments) {
		final List<LineSegmentInt> result = new ArrayList<LineSegmentInt>();
		for (LineSegmentInt seg : segments) {
			result.addAll(cutSegments(seg));
		}
		return result;
	}

	private LineSegmentInt inflateSegment(LineSegmentInt seg) {
		if (isOnGrid(seg.getP1()) || isOnGrid(seg.getP2())) {
			return new LineSegmentInt(inflatePoint2DInt(seg.getP1()), inflatePoint2DInt(seg.getP2()));
		}
		for (InflateData x : inflateX) {
			seg = seg.inflateXAlpha(x);
		}
		for (InflateData y : inflateY) {
			seg = seg.inflateYAlpha(y);
		}
		return seg;
	}

	private boolean isOnGrid(Point2DInt point) {
		boolean onGrid = false;
		for (InflateData x : inflateX) {
			if (point.getX() == x.getPos()) {
				onGrid = true;
			}
		}
		if (onGrid == false) {
			return false;
		}
		for (InflateData y : inflateY) {
			if (point.getY() == y.getPos()) {
				return true;
			}
		}
		return false;

	}

	public Point2DInt inflatePoint2DInt(Point2DInt point) {
		for (InflateData x : inflateX) {
			point = point.inflateX(x);
		}
		for (InflateData y : inflateY) {
			point = point.inflateY(y);
		}
		return point;
	}

	List<LineSegmentInt> inflateSegmentCollection(Collection<LineSegmentInt> segments) {
		final List<LineSegmentInt> result = new ArrayList<LineSegmentInt>();
		for (LineSegmentInt seg : segments) {
			result.add(inflateSegment(seg));
		}
		return result;
	}

	public List<LineSegmentInt> inflate(Collection<LineSegmentInt> segments) {
		final List<LineSegmentInt> result = new ArrayList<LineSegmentInt>();
		LineSegmentInt last = null;
		final Collection<LineSegmentInt> cutSegments = cutSegments(segments);
		for (LineSegmentInt seg : inflateSegmentCollection(cutSegments)) {
			if (last != null && last.getP2().equals(seg.getP1()) == false) {
				result.add(new LineSegmentInt(last.getP2(), seg.getP1()));
			}
			result.add(seg);
			last = seg;

		}
		return result;
	}
}
