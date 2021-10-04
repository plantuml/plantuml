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
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.Dimension2DDouble;

public class UEllipse extends AbstractShadowable implements UShapeSized {

	private final double width;
	private final double height;
	private final double start;
	private final double extend;

	public UEllipse(double width, double height) {
		this(width, height, 0, 0);
	}

	public UEllipse(double width, double height, double start, double extend) {
		this.width = width;
		this.height = height;
		this.start = start;
		this.extend = extend;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public final double getStart() {
		return start;
	}

	public final double getExtend() {
		return extend;
	}

	public Dimension2D getDimension() {
		return new Dimension2DDouble(width, height);
	}

	public UEllipse bigger(double more) {
		final UEllipse result = new UEllipse(width + more, height + more);
		result.setDeltaShadow(getDeltaShadow());
		return result;
	}

	public UEllipse scale(double factor) {
		final UEllipse result = new UEllipse(width * factor, height * factor);
		result.setDeltaShadow(getDeltaShadow());
		return result;
	}

	public double getStartingX(double y) {
		y = y / height * 2;
		final double x = 1 - Math.sqrt(1 - (y - 1) * (y - 1));
		return x * width / 2;
	}

	public double getEndingX(double y) {
		y = y / height * 2;
		final double x = 1 + Math.sqrt(1 - (y - 1) * (y - 1));
		return x * width / 2;
	}

	public Point2D getPointAtAngle(double alpha) {
		final double x = width / 2 + width / 2 * Math.cos(alpha);
		final double y = height / 2 + height / 2 * Math.sin(alpha);
		return new Point2D.Double(x, y);
	}

}
