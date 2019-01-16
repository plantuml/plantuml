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
 */
package net.sourceforge.plantuml.ugraphic.eps;

import java.awt.Shape;
import java.awt.geom.PathIterator;

public class PathIteratorLimited implements PathIterator {

	private final PathIterator path;
	private final int limit;
	private int current = 0;

	public static int count(Shape source) {
		int result = 0;
		final PathIterator path = source.getPathIterator(null);
		while (path.isDone() == false) {
			result++;
			path.next();
		}
		return result;
	}

	public PathIteratorLimited(Shape source, int start, int limit) {
		this.path = source.getPathIterator(null);
		this.limit = limit;
		for (int i = 0; i < start; i++) {
			this.next();
		}
	}

	public int currentSegment(float[] arg0) {
		return path.currentSegment(arg0);
	}

	public int currentSegment(double[] arg0) {
		return path.currentSegment(arg0);
	}

	public int getWindingRule() {
		return path.getWindingRule();
	}

	public boolean isDone() {
		if (current >= limit) {
			return true;
		}
		return path.isDone();
	}

	public void next() {
		path.next();
		current++;
	}

}
