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
package net.sourceforge.plantuml.ugraphic.comp;

import java.util.Set;
import java.util.TreeSet;

public class ExpandTransform implements PiecewiseAffineTransform {

	private final Set<Expand> all = new TreeSet<Expand>();

	@Override
	public String toString() {
		return all.toString();
	}

	public void addExpandIncludingLimit(double position, double extend) {
		this.all.add(new Expand(ExpandType.INCLUDING_LIMIT, position, extend));
	}

	public void addExpandExcludingLimit(double position, double extend) {
		this.all.add(new Expand(ExpandType.EXCLUDING_LIMIT, position, extend));
	}

	public double transform(final double init) {
		double result = init;
		for (Expand expand : all) {
			if (ExpandType.INCLUDING_LIMIT == expand.getType() && init >= expand.getPosition()) {
				result += expand.getExtend();
			}
			if (ExpandType.EXCLUDING_LIMIT == expand.getType() && init > expand.getPosition()) {
				result += expand.getExtend();
			}
		}
		return result;
	}

}
