/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.klimt.geom;

public class MinMaxMutable {

	private double maxX;
	private double maxY;
	private double minX;
	private double minY;

	public static MinMaxMutable getEmpty(boolean initToZero) {
		if (initToZero)
			return new MinMaxMutable(0, 0, 0, 0);

		return new MinMaxMutable(Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
	}

	public boolean isInfinity() {
		return minX == Double.MAX_VALUE;
	}

	@Override
	public String toString() {
		return "X=" + minX + " to " + maxX + " and Y=" + minY + " to " + maxY;
	}

	private MinMaxMutable(double minX, double minY, double maxX, double maxY) {
		if (Double.isNaN(minX))
			throw new IllegalArgumentException();

		if (Double.isNaN(maxX))
			throw new IllegalArgumentException();

		if (Double.isNaN(minY))
			throw new IllegalArgumentException();

		if (Double.isNaN(maxY))
			throw new IllegalArgumentException();

		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public void addPoint(double x, double y) {
		if (Double.isNaN(x))
			throw new IllegalArgumentException();

		if (Double.isNaN(y))
			throw new IllegalArgumentException();

		this.maxX = Math.max(x, maxX);
		this.maxY = Math.max(y, maxY);
		this.minX = Math.min(x, minX);
		this.minY = Math.min(y, minY);
	}

	public void addPoint(XPoint2D pt) {
		addPoint(pt.getX(), pt.getY());
	}

	public static MinMaxMutable fromMax(double maxX, double maxY) {
		if (Double.isNaN(maxX))
			throw new IllegalArgumentException();

		if (Double.isNaN(maxY))
			throw new IllegalArgumentException();

		final MinMaxMutable result = MinMaxMutable.getEmpty(true);
		result.addPoint(maxX, maxY);
		return result;
	}

	public final double getMaxX() {
		return maxX;
	}

	public final double getMaxY() {
		return maxY;
	}

	public final double getMinX() {
		return minX;
	}

	public final double getMinY() {
		return minY;
	}

	public XDimension2D getDimension() {
		return new XDimension2D(maxX - minX, maxY - minY);
	}

	public void reset() {
		this.maxX = 0;
		this.maxY = 0;
		this.minX = 0;
		this.minY = 0;
	}

}
