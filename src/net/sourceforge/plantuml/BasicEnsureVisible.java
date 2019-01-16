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
package net.sourceforge.plantuml;

public class BasicEnsureVisible implements EnsureVisible {

	private double minX = Double.MAX_VALUE;
	private double maxX = -Double.MAX_VALUE;
	private double minY = Double.MAX_VALUE;
	private double maxY = -Double.MAX_VALUE;

	public void ensureVisible(double x, double y) {
		if (x > maxX) {
			maxX = x;
		}
		if (x < minX) {
			minX = x;
		}
		if (y > maxY) {
			maxY = y;
		}
		if (y < minY) {
			minY = y;
		}
	}

	public boolean hasData() {
		return minX != Double.MAX_VALUE;
	}

	public String getCoords(double scale) {
		if (minX == Double.MAX_VALUE) {
			return "0,0,0,0";
		}
		final int x1 = (int) (minX * scale);
		final int y1 = (int) (minY * scale);
		final int x2 = (int) (maxX * scale);
		final int y2 = (int) (maxY * scale);
		return "" + x1 + "," + y1 + "," + x2 + "," + y2;
	}

	public double getSurface() {
		if (minX == Double.MAX_VALUE) {
			return 0;
		}
		return (maxX - minX) * (maxY - minY);
	}

}
