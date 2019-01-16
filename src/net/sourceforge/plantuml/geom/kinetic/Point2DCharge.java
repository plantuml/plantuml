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

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.Log;

public class Point2DCharge extends Point2D.Double {

	private double charge = 1.0;

	private MoveObserver moveObserver = null;

	public Point2DCharge(double x, double y) {
		super(x, y);
	}

	public Point2DCharge(Point2D pt, double ch) {
		super(pt.getX(), pt.getY());
		this.charge = ch;
	}

	public void apply(VectorForce value) {
		Log.println("Applying " + value);
		x += value.getX();
		y += value.getY();
		if (moveObserver != null) {
			moveObserver.pointMoved(this);
		}
	}

	@Override
	final public void setLocation(double x, double y) {
		throw new UnsupportedOperationException();
	}

	@Override
	final public void setLocation(Point2D p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return System.identityHashCode(this) + " " + String.format("[%8.2f %8.2f]", x, y);
	}

	public final double getCharge() {
		return charge;
	}

	public final void setCharge(double charge) {
		this.charge = charge;
	}

	private final int hash = System.identityHashCode(this);

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}

	public final void setMoveObserver(MoveObserver moveObserver) {
		this.moveObserver = moveObserver;
	}

}
