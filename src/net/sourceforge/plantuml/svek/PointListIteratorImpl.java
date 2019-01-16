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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Log;

class PointListIteratorImpl implements PointListIterator {

	private final SvgResult svg;
	private int pos = 0;

	static PointListIterator create(SvgResult svg, int lineColor) {
		final PointListIteratorImpl result = new PointListIteratorImpl(svg);
		final int idx = svg.getIndexFromColor(lineColor);
		if (idx == -1) {
			result.pos = -1;
		}
		return result;
	}

	public PointListIterator cloneMe() {
		final PointListIteratorImpl result = new PointListIteratorImpl(svg);
		result.pos = this.pos;
		return result;
	}

	private PointListIteratorImpl(SvgResult svg) {
		this.svg = svg;
	}

	public boolean hasNext() {
		return true;
	}

	public List<Point2D.Double> next() {
		if (pos == -1) {
			return Collections.emptyList();
		}
		try {
			final List<Point2D.Double> result = svg.substring(pos).extractList(SvgResult.POINTS_EQUALS);
			pos = svg.indexOf(SvgResult.POINTS_EQUALS, pos) + SvgResult.POINTS_EQUALS.length() + 1;
			return result;
		} catch (StringIndexOutOfBoundsException e) {
			Log.error("Error " + e);
			return Collections.emptyList();
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
