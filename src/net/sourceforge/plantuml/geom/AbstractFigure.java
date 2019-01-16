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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.Log;

abstract class AbstractFigure {

	private final Set<LineSegmentInt> segments = new HashSet<LineSegmentInt>();

	@Override
	public String toString() {
		return segments.toString();
	}

	@Override
	final public boolean equals(Object obj) {
		final AbstractFigure other = (AbstractFigure) obj;
		return segments.equals(other.segments);
	}

	@Override
	final public int hashCode() {
		return segments.hashCode();
	}

	protected boolean knowThisPoint(Point2DInt p) {
		for (LineSegmentInt seg : segments) {
			if (seg.getP1().equals(p) || seg.getP2().equals(p)) {
				return true;
			}
		}
		return false;
	}

	LineSegmentInt existingSegment(Point2DInt p1, Point2DInt p2) {
		for (LineSegmentInt seg : segments) {
			if (seg.getP1().equals(p1) && seg.getP2().equals(p2)) {
				return seg;
			}
			if (seg.getP1().equals(p2) && seg.getP2().equals(p1)) {
				return seg;
			}
		}
		return null;
	}

	Collection<LineSegmentInt> getSegmentsWithExtremity(Point2DInt extremity, Collection<LineSegmentInt> exceptions) {
		final Collection<LineSegmentInt> result = new HashSet<LineSegmentInt>();
		for (LineSegmentInt seg : segments) {
			if (exceptions.contains(seg)) {
				continue;
			}
			if (seg.getP1().equals(extremity) || seg.getP2().equals(extremity)) {
				result.add(seg);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	public void addSegment(LineSegmentInt seg) {
		segments.add(seg);
	}

	protected final Set<LineSegmentInt> getSegments() {
		return Collections.unmodifiableSet(segments);
	}

	@Deprecated
	public Polyline addPath(Point2DInt start, Point2DInt end) {
		if (knowThisPoint(start) && knowThisPoint(end)) {
			return getPath(start, end);
		}
		final LineSegmentInt direct = new LineSegmentInt(start, end);
		addSegment(direct);
		return new PolylineImpl(start, end);
	}

	public Polyline addDirectLink(Point2DInt start, Point2DInt end) {
		final LineSegmentInt direct = new LineSegmentInt(start, end);
		addSegment(direct);
		Log.println("AbstractFigure::addDirectLink " + direct);
		return new PolylineImpl(start, end);
	}

	public boolean isSimpleSegmentPossible(Point2DInt start, Point2DInt end) {
		final LineSegmentInt direct = new LineSegmentInt(start, end);
		return hasIntersectionStrict(direct) == false;
	}

	public Polyline getPath(Pointable start, Pointable end) {
		if (knowThisPoint(start.getPosition()) == false) {
			throw new IllegalArgumentException();
		}
		if (knowThisPoint(end.getPosition()) == false) {
			throw new IllegalArgumentException("" + end.getPosition());
		}
		if (isSimpleSegmentPossible(start.getPosition(), end.getPosition())) {
			throw new IllegalArgumentException();
			// return new PolylineImpl(start, end);
		}
		if (arePointsConnectable(start.getPosition(), end.getPosition()) == false) {
			return null;
		}
		return findBestPath(start, end);
	}

	private Polyline findBestPath(Pointable start, Pointable end) {
		Log.println("start=" + start.getPosition());
		Log.println("end=" + end.getPosition());
		final Set<Point2DInt> points = getAllPoints();
		if (points.contains(start.getPosition()) == false || points.contains(end.getPosition()) == false) {
			throw new IllegalArgumentException();
		}
		points.remove(start.getPosition());
		points.remove(end.getPosition());
		final List<Neighborhood> neighborhoods = new ArrayList<Neighborhood>();
		for (Point2DInt p : points) {
			neighborhoods.addAll(getSingularity(p).getNeighborhoods());
		}
		for (int i = 0; i < neighborhoods.size(); i++) {
			Log.println("N" + (i + 1) + " " + neighborhoods.get(i));
		}
		final Dijkstra dijkstra = new Dijkstra(neighborhoods.size() + 2);
		Log.println("size=" + dijkstra.getSize());
		for (int i = 0; i < neighborhoods.size(); i++) {
			if (isConnectable(start.getPosition(), neighborhoods.get(i))) {
				dijkstra.addLink(0, i + 1, distance(start.getPosition(), neighborhoods.get(i).getCenter()));
			}
		}
		for (int i = 0; i < neighborhoods.size(); i++) {
			for (int j = 0; j < neighborhoods.size(); j++) {
				if (i == j) {
					continue;
				}
				if (isConnectable(neighborhoods.get(i), neighborhoods.get(j))) {
					dijkstra.addLink(i + 1, j + 1, distance(neighborhoods.get(i).getCenter(), neighborhoods.get(j)
							.getCenter()));
				}
			}
		}
		for (int i = 0; i < neighborhoods.size(); i++) {
			if (isConnectable(end.getPosition(), neighborhoods.get(i))) {
				dijkstra.addLink(i + 1, neighborhoods.size() + 1, distance(end.getPosition(), neighborhoods.get(i)
						.getCenter()));
			}
		}
		final List<Integer> path = dijkstra.getBestPath();
		if (path.get(path.size() - 1) != neighborhoods.size() + 1) {
			throw new IllegalStateException("No Path");
		}
		assert path.size() > 2;

		Log.println("PATH=" + path);
		final List<Neighborhood> usedNeighborhoods = new ArrayList<Neighborhood>();
		for (int i = 1; i < path.size() - 1; i++) {
			final int idx = path.get(i) - 1;
			usedNeighborhoods.add(neighborhoods.get(idx));
		}
		return findApproximatePath(start, end, usedNeighborhoods);
	}

	private Polyline findApproximatePath(Pointable start, Pointable end, final List<Neighborhood> neighborhoods) {
		System.err
				.println("findApproximatePath " + start.getPosition() + " " + end.getPosition() + " " + neighborhoods);
		final PolylineImpl result = new PolylineImpl(start, end);
		for (Neighborhood n : neighborhoods) {
			Log.println("Neighborhood =" + n);
			final double d = getProximaDistance(n.getCenter()) / 2;
			final double a = n.getMiddle();
			Log.println("d=" + d);
			Log.println("a=" + a * 180 / Math.PI);
			final double deltaX = d * Math.cos(a);
			final double deltaY = d * Math.sin(a);
			assert d > 0;
			Log.println("Result = " + n.getCenter().translate((int) deltaX, (int) deltaY));
			result.addIntermediate(n.getCenter().translate((int) deltaX, (int) deltaY));
		}
		return result;
	}

	private double getProximaDistance(Point2DInt center) {
		double result = Double.MAX_VALUE;
		for (Point2DInt p : getAllPoints()) {
			if (center.equals(p)) {
				continue;
			}
			final double cur = new LineSegmentInt(p, center).getLength();
			result = Math.min(result, cur);
		}
		return result;
	}

	static private double distance(Point2DInt p1, Point2DInt p2) {
		return new LineSegmentInt(p1, p2).getLength();
	}

	public boolean isConnectable(Point2DInt p, Neighborhood n) {
		final LineSegmentInt seg = new LineSegmentInt(n.getCenter(), p);
		if (hasIntersectionStrict(seg)) {
			return false;
		}
		final double angle = Singularity.convertAngle(seg.getAngle());
		return n.isInAngleLarge(angle);
	}

	public boolean isConnectable(Neighborhood n1, Neighborhood n2) {
		final boolean result = isConnectableInternal(n1, n2);
		assert result == isConnectableInternal(n2, n1);
		return result;
	}

	private boolean isConnectableInternal(Neighborhood n1, Neighborhood n2) {
		if (n1.getCenter().equals(n2.getCenter())) {
			return false;
		}
		final LineSegmentInt seg1 = new LineSegmentInt(n1.getCenter(), n2.getCenter());
		if (hasIntersectionStrict(seg1)) {
			return false;
		}
		final double angle1 = Singularity.convertAngle(seg1.getAngle());
		final double angle2 = Singularity.convertAngle(seg1.getOppositeAngle());
		assert angle2 == Singularity.convertAngle(new LineSegmentInt(n2.getCenter(), n1.getCenter()).getAngle());
		if (n1.isInAngleStrict(angle1) && n2.isInAngleStrict(angle2)) {
			return true;
		}
		if (n1.isAngleLimit(angle1) && n2.isAngleLimit(angle2)) {
			if (n1.is360() || n2.is360()) {
				return true;
			}
			final Orientation o1 = n1.getOrientationFrom(angle1);
			final Orientation o2 = n2.getOrientationFrom(angle2);
			return o1 != o2;
		}
		return false;
	}

	private boolean hasIntersectionStrict(LineSegmentInt direct) {
		for (LineSegmentInt seg : getSegments()) {
			if (seg.atLeastOneCommonExtremities(direct)) {
				continue;
			}
			if (seg.doesIntersect(direct)) {
				Log.println("seg=" + seg);
				Log.println("direct=" + direct);
				Log.println("AbstractFigure::hasIntersectionStrict true");
				return true;
			}
		}
		Log.println("AbstractFigure::hasIntersectionStrict false");
		return false;
	}

	public Singularity getSingularity(Point2DInt center) {
		final Singularity singularity = new Singularity(center);
		for (LineSegmentInt seg : getSegments()) {
			if (seg.containsPoint(center)) {
				singularity.addLineSegment(seg);
			}
		}
		return singularity;
	}

	private Set<Point2DInt> getAllPoints() {
		final Set<Point2DInt> result = new HashSet<Point2DInt>();
		for (LineSegmentInt seg : segments) {
			result.add(seg.getP1());
			result.add(seg.getP2());
		}
		return result;
	}

	abstract boolean arePointsConnectable(Point2DInt p1, Point2DInt p2);

}
