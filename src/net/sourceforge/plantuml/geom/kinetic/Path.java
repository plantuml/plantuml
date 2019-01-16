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
package net.sourceforge.plantuml.geom.kinetic;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Path {

	private final Frame frame1;
	private final Frame frame2;
	private final List<Point2DCharge> points1 = new ArrayList<Point2DCharge>();
	// private final Map<Point2DCharge, Integer> points2 = new
	// HashMap<Point2DCharge, Integer>(1000, (float).01);
	private final Map<Point2DCharge, Integer> points2 = new HashMap<Point2DCharge, Integer>();

	public Path(Frame f1, Frame f2) {
		if (f1 == null || f2 == null) {
			throw new IllegalArgumentException();
		}
		this.frame1 = f1;
		this.frame2 = f2;
		updateCharges();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(frame1.getMainCorner());
		for (Point2DCharge p : points1) {
			sb.append(' ');
			sb.append(p);
		}
		sb.append(frame2.getMainCorner());
		return sb.toString();
	}

	private void updateCharges() {
		for (Point2DCharge pt : points1) {
			// pt.setCharge(1.0 / points1.size());
			pt.setCharge(1.0);
		}
	}

	private static final double MINDIST = 30;

	public void renderContinue() {
		final List<Point2D> newPoints = new ArrayList<Point2D>();
		Point2D cur = frame1.getMainCorner();
		for (Point2DCharge pc : points1) {
			final SegmentCutter segmentCutter = new SegmentCutter(cur, pc, MINDIST);
			newPoints.addAll(segmentCutter.intermediates());
			cur = pc;
		}
		final SegmentCutter segmentCutter = new SegmentCutter(cur, frame2.getMainCorner(), MINDIST);
		final List<Point2D> in = segmentCutter.intermediates();
		newPoints.addAll(in.subList(0, in.size() - 1));

		points1.clear();
		points2.clear();
		for (Point2D pt : newPoints) {
			addIntermediate(new Point2DCharge(pt.getX(), pt.getY()));
		}
	}

	public List<Line2D> segments() {
		final List<Line2D> result = new ArrayList<Line2D>();
		Point2D cur = frame1.getMainCorner();
		for (Point2D pt : points1) {
			result.add(new Line2D.Double(cur, pt));
			cur = pt;
		}
		result.add(new Line2D.Double(cur, frame2.getMainCorner()));
		return Collections.unmodifiableList(result);
	}

	public void addIntermediate(Point2DCharge point) {
		assert points1.size() == points2.size();
		assert points1.contains(point) == false;
		assert points2.containsKey(point) == false;
		assert containsPoint2DCharge(point) == false;
		points1.add(point);
		points2.put(point, points2.size());
		assert points1.size() == points2.size();
		assert points1.contains(point);
		assert points2.containsKey(point);
		assert containsPoint2DCharge(point);
		updateCharges();
	}

	public VectorForce getElasticForce(Point2DCharge point) {
		final int idx = points1.indexOf(point);
		if (idx == -1) {
			throw new UnsupportedOperationException();
		}
		final Point2D before = getPosition(idx - 1);
		final Point2D after = getPosition(idx + 1);
		final VectorForce f1 = new VectorForce(point, before);
		final VectorForce f2 = new VectorForce(point, after);
		return f1.plus(f2).multiply(0.2);
		// return new VectorForce(0, 0);
	}

	private Point2D getPosition(int idx) {
		if (idx == -1) {
			return frame1.getMainCorner();
		}
		if (idx == points1.size()) {
			return frame2.getMainCorner();
		}
		return points1.get(idx);
	}

	public boolean containsPoint2DCharge(Point2DCharge p) {
		assert points1.contains(p) == points2.containsKey(p) : "p=" + p + "v1=" + points1.contains(p) + "v2="
				+ points2.containsKey(p) + " points1=" + points1 + " points2=" + points2;
		return points2.containsKey(p);
	}

	public final Collection<Point2DCharge> getPoints() {
		assert points1.size() == points2.size();
		return Collections.unmodifiableCollection(points1);
	}

}
