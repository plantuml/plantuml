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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UEllipse;

public class ContainingEllipse {

	private final SmallestEnclosingCircle sec = new SmallestEnclosingCircle();
	private final YTransformer ytransformer;

	@Override
	public String toString() {
		return "ContainingEllipse " + getWidth() + " " + getHeight();
	}

	public ContainingEllipse(double coefY) {
		ytransformer = new YTransformer(coefY);
	}

	public void append(Point2D pt) {
		pt = ytransformer.getReversePoint2D(pt);
		sec.append(pt);
	}

	public void append(double x, double y) {
		append(new Point2D.Double(x, y));
	}

	public double getWidth() {
		return 2 * sec.getCircle().getRadius();
	}

	public double getHeight() {
		return 2 * sec.getCircle().getRadius() * ytransformer.getAlpha();
	}

	public Point2D getCenter() {
		return ytransformer.getPoint2D(sec.getCircle().getCenter());
	}

	public UEllipse asUEllipse() {
		final UEllipse ellipse = new UEllipse(getWidth(), getHeight());
		ellipse.setDeltaShadow(deltaShadow);
		return ellipse;
	}

	private double deltaShadow;

	public void setDeltaShadow(double deltaShadow) {
		this.deltaShadow = deltaShadow;
	}

}
