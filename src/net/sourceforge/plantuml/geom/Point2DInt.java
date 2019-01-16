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

import java.awt.geom.Point2D;

public class Point2DInt extends Point2D implements Pointable {

	private final int x;
	private final int y;

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public Point2DInt(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getXint() {
		return x;
	}

	public int getYint() {
		return y;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setLocation(double x, double y) {
		throw new UnsupportedOperationException();
	}

	public Point2DInt getPosition() {
		return this;
	}

	public Point2DInt translate(int deltaX, int deltaY) {
		return new Point2DInt(x + deltaX, y + deltaY);
	}

	public Point2DInt inflateX(int xpos, int inflation) {
		if (inflation % 2 != 0) {
			throw new IllegalArgumentException();
		}
		if (x < xpos) {
			return this;
		}
		if (x == xpos) {
			// throw new IllegalArgumentException();
			return translate(inflation / 2, 0);
		}
		return translate(inflation, 0);
	}

	public Point2DInt inflateX(InflateData inflateData) {
		return inflateX(inflateData.getPos(), inflateData.getInflation());
	}

	public Point2DInt inflateY(InflateData inflateData) {
		return inflateY(inflateData.getPos(), inflateData.getInflation());
	}

	public Point2DInt inflateY(int ypos, int inflation) {
		if (inflation % 2 != 0) {
			throw new IllegalArgumentException();
		}
		if (y < ypos) {
			return this;
		}
		if (y == ypos) {
			// throw new IllegalArgumentException();
			return translate(0, inflation / 2);
		}
		return translate(0, inflation);
	}

}
