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
package net.sourceforge.plantuml.salt.element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListWidth {

	private final List<Double> allWidth = new ArrayList<Double>();

	public void add(double width) {
		this.allWidth.add(width);
	}

	public ListWidth mergeMax(ListWidth other) {
		final ListWidth result = new ListWidth();
		for (int i = 0; i < this.allWidth.size() || i < other.allWidth.size(); i++) {
			final double w1 = this.getWidthSafe(i);
			final double w2 = other.getWidthSafe(i);
			result.add(Math.max(w1, w2));
		}
		return result;
	}

	private double getWidthSafe(int i) {
		if (i < allWidth.size()) {
			return allWidth.get(i);
		}
		return 0;
	}

	public double getTotalWidthWithMargin(final double margin) {
		double result = 0;
		for (Double w : allWidth) {
			if (result > 0) {
				result += margin;
			}
			result += w;
		}
		return result;
	}

	public Iterator<Double> iterator() {
		return allWidth.iterator();
	}

}
