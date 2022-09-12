/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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

import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.awt.geom.XPoint2D;

public class PositionableImpl implements Positionable {

	private final XPoint2D pos;

	private final XDimension2D dim;

	public PositionableImpl(double x, double y, XDimension2D dim) {
		this.pos = new XPoint2D(x, y);
		this.dim = dim;
	}

	public PositionableImpl(XPoint2D pt, XDimension2D dim) {
		this(pt.getX(), pt.getY(), dim);
	}

	public XPoint2D getPosition() {
		return pos;
	}

	public XDimension2D getSize() {
		return dim;
	}

	public void moveSvek(double deltaX, double deltaY) {
		this.pos.setLocation(pos.getX() + deltaX, pos.getY() + deltaY);
	}

}
