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
package net.sourceforge.plantuml.posimo;

import java.util.Locale;

import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;

public class Block implements Clusterable {

	private final int uid;
	private final double width;
	private final double height;
	private double x;
	private double y;
	private final Cluster parent;

	public Block(int uid, double width, double height, Cluster parent) {
		this.uid = uid;
		this.width = width;
		this.height = height;
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "BLOCK " + uid;
	}

	public String toStringPosition() {
		return String.format(Locale.US, "x=%9.2f y=%9.2f w=%9.2f h=%9.2f", x, y, width, height);
	}

	public int getUid() {
		return uid;
	}

	public Cluster getParent() {
		return parent;
	}

	public XPoint2D getPosition() {
		return new XPoint2D(x, y);
	}

	public XDimension2D getSize() {
		return new XDimension2D(width, height);
	}

	public void setCenterX(double center) {
		this.x = center - width / 2;
	}

	public void setCenterY(double center) {
		this.y = center - height / 2;
	}

	public final void setX(double x) {
		this.x = x;
	}

	public final void setY(double y) {
		this.y = y;
	}

	public void moveDelta(double deltaX, double deltaY) {
		throw new UnsupportedOperationException();
	}

}
