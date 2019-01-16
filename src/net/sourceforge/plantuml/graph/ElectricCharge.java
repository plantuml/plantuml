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
package net.sourceforge.plantuml.graph;

import java.awt.geom.Point2D;

public class ElectricCharge {

	private boolean moveable;
	private final Point2D position;
	private final double charge;

	public ElectricCharge(double x, double y, double charge) {
		this.position = new Point2D.Double(x, y);
		this.charge = charge;
	}

	public Point2D getPosition() {
		return position;
	}

	public double getCharge() {
		return charge;
	}

	public final boolean isMoveable() {
		return moveable;
	}

	public final void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public void move(double deltax, double deltay) {
		position.setLocation(position.getX() + deltax, position.getY() + deltay);
	}
}
