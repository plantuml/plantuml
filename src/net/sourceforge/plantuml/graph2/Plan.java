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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.graph2.Dijkstra.Vertex;

public class Plan {

	private final Map<Point2D.Double, Singularity2> points = new LinkedHashMap<Point2D.Double, Singularity2>();
	private final Collection<Line2D.Double> lines = new ArrayList<Line2D.Double>();

	public void addPoint2D(Point2D.Double point) {
		if (points.containsKey(point)) {
			throw new IllegalArgumentException();
		}
		points.put(point, new Singularity2(point));
	}

	public void debugPrint() {
		Log.println("PLAN PRINT");
		for (Singularity2 s : points.values()) {
			Log.println("s="+s);
		}
		for (Line2D.Double l : lines) {
			Log.println(GeomUtils.toString(l));
		}
	}

	public void createLink(Point2D p1, Point2D p2) {
		final Singularity2 s1 = points.get(p1);
		final Singularity2 s2 = points.get(p2);
		if (s1 == null || s2 == null) {
			throw new IllegalArgumentException();
		}
		final Line2D.Double line = new Line2D.Double(p1, p2);

		s1.addLineSegment(line);
		s2.addLineSegment(line);
		lines.add(line);
	}

	Singularity2 getSingularity(Point2D pt) {
		final Singularity2 result = points.get(pt);
		if (result == null) {
			throw new IllegalArgumentException();
		}
		return result;
	}

	List<Neighborhood2> getShortestPathToInternal(Point2D start, Point2D end) {
		final Dijkstra dijkstra = new Dijkstra();
		if (points.containsKey(start) == false || points.containsKey(end) == false) {
			throw new IllegalArgumentException();
		}
		final Vertex vStart = dijkstra.addVertex(start);
		final Vertex vEnd = dijkstra.addVertex(end);
		final Map<Neighborhood2, Vertex> vertexes = new LinkedHashMap<Neighborhood2, Vertex>();
		for (Singularity2 s : points.values()) {
			for (Neighborhood2 n : s.getNeighborhoods()) {
				final Vertex v = dijkstra.addVertex(n);
				vertexes.put(n, v);
				if (n.getCenter().equals(start)) {
					vStart.addAdjacencies(v, 0.01);
				}
				if (n.getCenter().equals(end)) {
					v.addAdjacencies(vEnd, 0.01);
				}
			}
		}

		for (Vertex v1 : vertexes.values()) {
			for (Vertex v2 : vertexes.values()) {
				final Neighborhood2 n1 = (Neighborhood2) v1.getData();
				final Neighborhood2 n2 = (Neighborhood2) v2.getData();
				if (n1.getCenter().equals(n2.getCenter())) {
					continue;
				}
				final Line2D.Double line = new Line2D.Double(n1.getCenter(), n2.getCenter());
				if (isStrictCrossing(line)) {
					continue;
				}
				if (n1.isConnectable(n2) == false) {
					continue;
				}
				final double dist = n1.getCenter().distance(n2.getCenter());
				v1.addAdjacencies(v2, dist);
				v2.addAdjacencies(v1, dist);
				// Log.println("=(" + n1 + ") (" + n2 + ") " + dist);
			}
		}

		final List<Vertex> list = dijkstra.getShortestPathTo(vStart, vEnd);
		if (list.size() < 2) {
			throw new IllegalStateException("list=" + list);
		}
		final List<Neighborhood2> result = new ArrayList<Neighborhood2>();
		for (Vertex v : list.subList(1, list.size() - 1)) {
			result.add((Neighborhood2) v.getData());
		}
		return result;
	}

	public List<Point2D.Double> getIntermediatePoints(Point2D start, Point2D end) {
		// Log.println("start=" + start + " end=" + end);
		final List<Point2D.Double> result = new ArrayList<Point2D.Double>();
		final List<Neighborhood2> list = getShortestPathToInternal(start, end);
		// Log.println("Neighborhood2 = " + list);
		for (int i = 1; i < list.size() - 1; i++) {
			final Neighborhood2 n = list.get(i);
			final Point2D.Double before = list.get(i - 1).getCenter();
			final Point2D.Double after = list.get(i + 1).getCenter();
			// Log.println("before="+before);
			// Log.println("after="+after);
			// Log.println("n.getCenter()="+n.getCenter());
			// Log.println("getMindist(n.getCenter())="+getMindist(n.getCenter()));
			final Point2D.Double pointInNeighborhood = n.getPointInNeighborhood(getMindist(n.getCenter()) / 2, before,
					after);
			// Log.println("pointInNeighborhood="+pointInNeighborhood);
			result.add(pointInNeighborhood);
		}
		return result;

	}

	private boolean isStrictCrossing(Line2D.Double line) {
		for (Line2D.Double l : lines) {
			if (intersectsLineStrict(l, line)) {
				return true;
			}
		}
		return false;
	}

	public static boolean intersectsLineStrict(Line2D.Double l1, Line2D.Double l2) {
		assert intersectsLineStrictInternal(l1, l2) == intersectsLineStrictInternal(l2, l1);
		assert intersectsLineStrictInternal(l1, l2) == intersectsLineStrictInternal(inverse(l1), l2);
		assert intersectsLineStrictInternal(l1, l2) == intersectsLineStrictInternal(l1, inverse(l2));
		assert intersectsLineStrictInternal(l1, l2) == intersectsLineStrictInternal(inverse(l1), inverse(l2));
		return intersectsLineStrictInternal(l1, l2);
	}

	private static Line2D.Double inverse(Line2D.Double line) {
		return new Line2D.Double(line.getP2(), line.getP1());
	}

	private static boolean intersectsLineStrictInternal(Line2D.Double l1, Line2D.Double l2) {
		if (l1.intersectsLine(l2) == false) {
			return false;
		}
		assert l1.intersectsLine(l2);

		Point2D.Double l1a = (Point2D.Double) l1.getP1();
		Point2D.Double l1b = (Point2D.Double) l1.getP2();
		Point2D.Double l2a = (Point2D.Double) l2.getP1();
		Point2D.Double l2b = (Point2D.Double) l2.getP2();

		if (l1a.equals(l2a) == false && l1a.equals(l2b) == false && l1b.equals(l2a) == false
				&& l1b.equals(l2b) == false) {
			return true;
		}

		if (l1a.equals(l2b)) {
			final Point2D.Double tmp = l2a;
			l2a = l2b;
			l2b = tmp;
		} else if (l2a.equals(l1b)) {
			final Point2D.Double tmp = l1a;
			l1a = l1b;
			l1b = tmp;
		} else if (l1b.equals(l2b)) {
			Point2D.Double tmp = l2a;
			l2a = l2b;
			l2b = tmp;
			tmp = l1a;
			l1a = l1b;
			l1b = tmp;
		}

		assert l1a.equals(l2a);

		return false;

	}

	final double getMindist(Point2D.Double pt) {
		double result = Double.MAX_VALUE;
		for (Point2D p : points.keySet()) {
			if (pt.equals(p)) {
				continue;
			}
			final double v = p.distance(pt);
			if (v < 1E-4) {
				throw new IllegalStateException();
			}
			result = Math.min(result, v);
		}
		for (Line2D line : lines) {
			if (line.getP1().equals(pt) || line.getP2().equals(pt)) {
				continue;
			}
			final double v = line.ptSegDist(pt);
			if (result < 1E-4) {
				throw new IllegalStateException("pt=" + pt + " line=" + GeomUtils.toString(line));
			}
			result = Math.min(result, v);
		}
		if (result == 0) {
			throw new IllegalStateException();
		}
		// Log.println("getMindist=" + result);
		return result;
	}
}
