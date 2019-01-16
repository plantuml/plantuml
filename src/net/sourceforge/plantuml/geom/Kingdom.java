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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Kingdom extends AbstractFigure {

	private Set<ClosedArea> buildClosedArea(ClosedArea area) {
		if (area.isClosed()) {
			throw new IllegalArgumentException();
		}
		final Set<ClosedArea> result = new HashSet<ClosedArea>();
		for (LineSegmentInt seg : getSegmentsWithExtremity(area.getFreePoint(), area.getSegments())) {
			final ClosedArea newArea = area.append(seg);
			if (newArea != null) {
				result.add(newArea);
			}
		}
		return Collections.unmodifiableSet(result);
	}

	private void grow(Set<ClosedArea> areas) {
		for (ClosedArea area : new HashSet<ClosedArea>(areas)) {
			if (area.isClosed() == false) {
				areas.addAll(buildClosedArea(area));
			}
		}
	}

	public Set<ClosedArea> getAllClosedArea() {
		final Set<ClosedArea> result = new HashSet<ClosedArea>();
		for (LineSegmentInt seg : getSegments()) {
			result.add(new ClosedArea().append(seg));
		}
		int lastSize;
		do {
			lastSize = result.size();
			grow(result);
		} while (result.size() != lastSize);
		for (final Iterator<ClosedArea> it = result.iterator(); it.hasNext();) {
			final ClosedArea area = it.next();
			if (area.isClosed() == false) {
				it.remove();
			}
		}
		return Collections.unmodifiableSet(result);
	}

	// public Set<ClosedArea> getAllSmallClosedArea() {
	// final Set<ClosedArea> all = getAllClosedArea();
	// final Set<ClosedArea> result = new HashSet<ClosedArea>(all);
	//
	// for (final Iterator<ClosedArea> it = result.iterator(); it.hasNext();) {
	// final ClosedArea area = it.next();
	// if (containsAnotherArea(area, all)) {
	// it.remove();
	// }
	// }
	//
	// return Collections.unmodifiableSet(result);
	// }

	// static private boolean containsAnotherArea(ClosedArea area,
	// Set<ClosedArea> all) {
	// for (ClosedArea another : all) {
	// if (another == area) {
	// continue;
	// }
	// if (area.contains(another)) {
	// return true;
	// }
	// }
	// return false;
	// }

	@Override
	public boolean arePointsConnectable(Point2DInt p1, Point2DInt p2) {
		for (ClosedArea area : getAllClosedArea()) {
			if (area.arePointsConnectable(p1, p2) == false) {
				return false;
			}
		}
		return true;
	}

}
