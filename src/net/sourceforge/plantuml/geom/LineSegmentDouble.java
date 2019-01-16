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

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.Locale;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class LineSegmentDouble extends AbstractLineSegment {

	private final Point2D p1;
	private final Point2D p2;

	@Override
	public String toString() {
		return String.format(Locale.US, "( %.2f,%.2f - %.2f,%.2f )", getP1().getX(), getP1().getY(), getP2().getX(),
				getP2().getY());
	}

	public LineSegmentDouble(double x1, double y1, double x2, double y2) {
		this(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));
	}

	public LineSegmentDouble(Point2D p1, Point2D p2) {
		this.p1 = p1;
		this.p2 = p2;
		if (p1.equals(p2)) {
			throw new IllegalArgumentException();
		}
		assert p1 != null && p2 != null;
		assert getLength() > 0;
		assert this.getDistance(this) == 0;
	}

	public LineSegmentDouble(CubicCurve2D.Double curve) {
		this(curve.getP1(), curve.getP2());
	}

	public LineSegmentDouble translate(UTranslate translate) {
		return new LineSegmentDouble(translate.getTranslated(getP1()), translate.getTranslated(getP2()));
	}

	@Override
	public Point2D getP1() {
		return p1;
	}

	@Override
	public Point2D getP2() {
		return p2;
	}

	@Override
	public double getX1() {
		return p1.getX();
	}

	@Override
	public double getX2() {
		return p2.getX();
	}

	@Override
	public double getY1() {
		return p1.getY();
	}

	@Override
	public double getY2() {
		return p2.getY();
	}

	public void draw(UGraphic ug) {
		final double x1 = p1.getX();
		final double y1 = p1.getY();
		final double x2 = p2.getX();
		final double y2 = p2.getY();
		ug = ug.apply(new UTranslate(x1, y1));
		ug.draw(new ULine(x2 - x1, y2 - y1));

	}


}
