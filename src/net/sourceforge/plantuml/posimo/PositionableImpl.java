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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

public class PositionableImpl implements Positionable {

	private final Point2D pos;

	private final Dimension2D dim;

	public PositionableImpl(double x, double y, Dimension2D dim) {
		this.pos = new Point2D.Double(x, y);
		this.dim = dim;
	}

	public PositionableImpl(Point2D pt, Dimension2D dim) {
		this(pt.getX(), pt.getY(), dim);
	}

	public Point2D getPosition() {
		return pos;
	}

	public Dimension2D getSize() {
		return dim;
	}

	public void moveSvek(double deltaX, double deltaY) {
		this.pos.setLocation(pos.getX() + deltaX, pos.getY() + deltaY);
	}

}
