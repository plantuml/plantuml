/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 */
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.Rectangle2D;

public class UClip {

	private final double x;
	private final double y;
	private final double width;
	private final double height;

	public UClip(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public String toString() {
		return "CLIP x=" + x + " y=" + y + " w=" + width + " h=" + height;
	}

	public UClip translate(double dx, double dy) {
		return new UClip(x + dx, y + dy, width, height);
	}

	public final double getX() {
		return x;
	}

	public final double getY() {
		return y;
	}

	public final double getWidth() {
		return width;
	}

	public final double getHeight() {
		return height;
	}

	public boolean isInside(double xp, double yp) {
		if (xp < x) {
			return false;
		}
		if (xp > x + width) {
			return false;
		}
		if (yp < y) {
			return false;
		}
		if (yp > y + height) {
			return false;
		}
		return true;
	}

	public Rectangle2D.Double getClippedRectangle(Rectangle2D.Double r) {
		return (Rectangle2D.Double) r.createIntersection(new Rectangle2D.Double(x, y, width, height));
	}
}
