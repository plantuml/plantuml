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
import java.util.HashSet;
import java.util.List;

public class ClosedArea extends AbstractFigure {

	private final List<Point2DInt> points = new ArrayList<Point2DInt>();
	private final List<LineSegmentInt> segmentsList = new ArrayList<LineSegmentInt>();

	private int minY = Integer.MAX_VALUE;
	private int minX = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int maxY = Integer.MIN_VALUE;

	public ClosedArea() {
		assert isConsistent();
	}

	@Override
	public String toString() {
		return points.toString();
	}

	public boolean contains(Point2DInt point) {
		return contains(point.getXint(), point.getYint());
	}

	private boolean contains(int x, int y) {
		if (points.size() <= 2) {
			return false;
		}
		if (x > maxX) {
			return false;
		}
		if (x < minX) {
			return false;
		}
		if (y > maxY) {
			return false;
		}
		if (y < minY) {
			return false;
		}
		if (isOnFrontier(new Point2DInt(x, y))) {
			return true;
		}
		int hits = 0;

		int lastx = getLastPoint().getXint();
		int lasty = getLastPoint().getYint();
		int curx;
		int cury;

		// Walk the edges of the polygon
		for (int i = 0; i < points.size(); lastx = curx, lasty = cury, i++) {
			curx = points.get(i).getXint();
			cury = points.get(i).getYint();

			if (cury == lasty) {
				continue;
			}

			final int leftx;
			if (curx < lastx) {
				if (x >= lastx) {
					continue;
				}
				leftx = curx;
			} else {
				if (x >= curx) {
					continue;
				}
				leftx = lastx;
			}

			final double test1;
			final double test2;
			if (cury < lasty) {
				if (y < cury || y >= lasty) {
					continue;
				}
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - curx;
				test2 = y - cury;
			} else {
				if (y < lasty || y >= cury) {
					continue;
				}
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - lastx;
				test2 = y - lasty;
			}

			if (test1 < test2 / (lasty - cury) * (lastx - curx)) {
				hits++;
			}

		}
		return (hits & 1) != 0;

	}

	private boolean isConsistent() {
		assert getSegments().size() == segmentsList.size();
		assert getSegments().equals(new HashSet<LineSegmentInt>(segmentsList));
		if (getSegments().size() > 0) {
			assert getSegments().size() + 1 == points.size() : "points=" + points + " getSegment()=" + getSegments();
		}
		for (int i = 0; i < segmentsList.size(); i++) {
			final LineSegmentInt seg = segmentsList.get(i);
			if (seg.sameExtremities(new LineSegmentInt(points.get(i), points.get(i + 1))) == false) {
				return false;
			}
		}
		return true;
	}

	public boolean isOnFrontier(Point2DInt point) {
		for (LineSegmentInt seg : segmentsList) {
			if (seg.containsPoint(point)) {
				return true;
			}
		}
		return false;
	}

	public boolean isClosed() {
		if (getSegments().size() < 3) {
			return false;
		}
		if (getFirstSegment().atLeastOneCommonExtremities(getLastSegment())) {
			return true;
		}
		return false;
	}

	ClosedArea append(LineSegmentInt other) {
		if (isClosed()) {
			throw new IllegalStateException();
		}
		if (getSegments().contains(other)) {
			throw new IllegalArgumentException();
		}
		final ClosedArea result = new ClosedArea();
		for (LineSegmentInt seg : segmentsList) {
			result.addSegment(seg);
		}
		if (result.getSegments().size() > 0 && result.getLastSegment().atLeastOneCommonExtremities(other) == false) {
			throw new IllegalArgumentException();
		}
		if (points.contains(other.getP1()) && points.contains(other.getP2())
				&& other.getP1().equals(getFirstPoint()) == false && other.getP2().equals(getFirstPoint()) == false) {
			return null;
		}
		result.addSegment(other);
		assert result.isConsistent();

		return result;
	}

	@Override
	public void addSegment(LineSegmentInt seg) {
		super.addSegment(seg);
		minY = Math.min(minY, seg.getMinY());
		maxY = Math.max(maxY, seg.getMaxY());
		minX = Math.min(minX, seg.getMinX());
		maxX = Math.max(maxX, seg.getMaxX());
		segmentsList.add(seg);
		if (points.size() == 0) {
			assert getSegments().size() == 1;
			points.add(seg.getP1());
			points.add(seg.getP2());
		} else if (points.size() == 2) {
			assert segmentsList.size() == 2;
			final LineSegmentInt seg0 = segmentsList.get(0);
			final LineSegmentInt seg1 = segmentsList.get(1);
			points.clear();
			final Point2DInt common = seg0.getCommonExtremities(seg1);
			if (common == null) {
				throw new IllegalArgumentException();
			}
			assert common.equals(seg1.getCommonExtremities(seg0));
			points.add(seg0.getOtherExtremity(common));
			points.add(common);
			points.add(seg1.getOtherExtremity(common));

		} else {
			final Point2DInt lastPoint = getLastPoint();
			points.add(seg.getOtherExtremity(lastPoint));
		}
		assert isConsistent();
	}

	private Point2DInt getLastPoint() {
		return points.get(points.size() - 1);
	}

	private Point2DInt getFirstPoint() {
		return points.get(0);
	}

	public LineSegmentInt getLastSegment() {
		return segmentsList.get(segmentsList.size() - 1);
	}

	private LineSegmentInt getFirstSegment() {
		return segmentsList.get(0);
	}

	public Point2DInt getFreePoint() {
		if (isClosed()) {
			throw new IllegalStateException();
		}
		return getLastPoint();
	}

	public int getMinY() {
		return minY;
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMaxX() {
		return maxX;
	}

	public boolean contains(ClosedArea other) {
		if (isClosed() == false) {
			throw new IllegalStateException();
		}
		for (Point2DInt point : other.points) {
			if (this.contains(point) == false) {
				return false;
			}
		}
		return true;
	}

	@Override
	boolean arePointsConnectable(Point2DInt p1, Point2DInt p2) {
		if (isOnFrontier(p1) || isOnFrontier(p2)) {
			return true;
		}
		final boolean pos1 = contains(p1);
		final boolean pos2 = contains(p2);
		return pos1 == pos2;
	}
}
