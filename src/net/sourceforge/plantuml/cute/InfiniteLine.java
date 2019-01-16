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
package net.sourceforge.plantuml.cute;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.geom.AbstractLineSegment;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class InfiniteLine {

	private final Point2D a;
	private final Point2D b;

	public InfiniteLine(Point2D a, Point2D b) {
		this.a = a;
		this.b = b;
	}

	public InfiniteLine(AbstractLineSegment segment) {
		this(segment.getP1(), segment.getP2());
	}

	@Override
	public String toString() {
		return "{" + a + ";" + b + "}";
	}

	public double getDeltaX() {
		return b.getX() - a.getX();
	}

	public double getDeltaY() {
		return b.getY() - a.getY();
	}

	public double getDr() {
		return a.distance(b);
	}

	public double getDiscriminant() {
		return a.getX() * b.getY() - b.getX() * a.getY();
	}

	public InfiniteLine translate(UTranslate translate) {
		return new InfiniteLine(translate.getTranslated(a), translate.getTranslated(b));
	}

}
