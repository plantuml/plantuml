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
import java.util.List;

public class PolylineImpl extends AbstractPolyline implements Polyline {

	final private List<Point2DInt> intermediates = new ArrayList<Point2DInt>();

	public PolylineImpl(Pointable start, Pointable end) {
		super(start, end);
	}

	public int nbSegments() {
		return intermediates.size() + 1;
	}

	public List<LineSegmentInt> segments() {
		final List<LineSegmentInt> result = new ArrayList<LineSegmentInt>();
		Point2DInt cur = getStart().getPosition();
		for (Point2DInt intermediate : intermediates) {
			result.add(new LineSegmentInt(cur, intermediate));
			cur = intermediate;
		}
		result.add(new LineSegmentInt(cur, getEnd().getPosition()));
		return Collections.unmodifiableList(result);
	}

	public void addIntermediate(Point2DInt intermediate) {
		assert intermediates.contains(intermediate) == false;
		intermediates.add(intermediate);
	}

	public void inflate(InflationTransform transform) {
		// final List<LineSegment> segments = segments();
		// if (segments.size() == 1) {
		// return;
		// }
		// intermediates.clear();
		// if (segments.size() == 2) {
		// final Point2DInt p = segments.get(0).getP2();
		// intermediates.add(transform.inflatePoint2DInt(p));
		// } else {
		// final List<LineSegment> segmentsT = transform.inflate(segments);
		// for (int i = 0; i < segmentsT.size() - 2; i++) {
		// intermediates.add(segmentsT.get(i).getP2());
		// }
		//
		// }

		final List<LineSegmentInt> segments = transform.inflate(this.segments());
		// Log.println("segments="+segments);
		intermediates.clear();
		for (int i = 1; i < segments.size() - 1; i++) {
			addIntermediate(segments.get(i).getP1());
		}
	}

	public final Collection<Point2DInt> getIntermediates() {
		return Collections.unmodifiableCollection(intermediates);
	}

}
