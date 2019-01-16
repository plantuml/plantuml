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

import net.sourceforge.plantuml.Dimension2DDouble;

public class Label implements Positionable {

	private double width;
	private double height;
	private double x;
	private double y;

	public Label(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public final void setCenterX(double center) {
		this.x = center - width / 2;
	}

	public final void setCenterY(double center) {
		this.y = center - height / 2;
	}

	public Point2D getPosition() {
		return new Point2D.Double(x, y);
	}

	public Dimension2D getSize() {
		return new Dimension2DDouble(width, height);
	}

	public final void setWidth(double width) {
		this.width = width;
	}

	public final void setHeight(double height) {
		this.height = height;
	}

	public final void setX(double x) {
		this.x = x;
	}

	public final void setY(double y) {
		this.y = y;
	}
	
	public void moveSvek(double deltaX, double deltaY) {
		throw new UnsupportedOperationException();
	}


}
