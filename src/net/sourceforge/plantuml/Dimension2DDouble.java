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
package net.sourceforge.plantuml;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.utils.MathUtils;

public class Dimension2DDouble extends Dimension2D {

	final private double width;
	final private double height;

	public Dimension2DDouble(double width, double height) {
		if (Double.isNaN(width) || Double.isNaN(height)) {
			throw new IllegalArgumentException();
		}
		this.width = width;
		this.height = height;
	}

	@Override
	public String toString() {
		return "[" + width + "," + height + "]";
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public void setSize(double width, double height) {
		throw new UnsupportedOperationException();
	}

	public static Dimension2D delta(Dimension2D dim, double delta) {
		return delta(dim, delta, delta);
	}

	public Dimension2DDouble withWidth(double newWidth) {
		return new Dimension2DDouble(newWidth, height);
	}

	public static Dimension2D delta(Dimension2D dim, double deltaWidth, double deltaHeight) {
		if (deltaHeight == 0 && deltaWidth == 0) {
			return dim;
		}
		return new Dimension2DDouble(dim.getWidth() + deltaWidth, dim.getHeight() + deltaHeight);
	}

	public static Dimension2D mergeTB(Dimension2D top, Dimension2D bottom) {
		final double width = Math.max(top.getWidth(), bottom.getWidth());
		final double height = top.getHeight() + bottom.getHeight();
		return new Dimension2DDouble(width, height);
	}

	public static Dimension2D mergeTB(Dimension2D a, Dimension2D b, Dimension2D c) {
		final double width = MathUtils.max(a.getWidth(), b.getWidth(), c.getWidth());
		final double height = a.getHeight() + b.getHeight() + c.getHeight();
		return new Dimension2DDouble(width, height);
	}

	public static Dimension2D mergeLR(Dimension2D left, Dimension2D right) {
		final double height = Math.max(left.getHeight(), right.getHeight());
		final double width = left.getWidth() + right.getWidth();
		return new Dimension2DDouble(width, height);
	}

	public static Dimension2D mergeLayoutT12B3(Dimension2D top1, Dimension2D top2, Dimension2D bottom) {
		final double width = MathUtils.max(top1.getWidth(), top2.getWidth(), bottom.getWidth());
		final double height = top1.getHeight() + top2.getHeight() + bottom.getHeight();
		return new Dimension2DDouble(width, height);
	}

	public static Dimension2D max(Dimension2D dim1, Dimension2D dim2) {
		return atLeast(dim1, dim2.getWidth(), dim2.getHeight());
	}

	public static Dimension2D atLeast(Dimension2D dim, double minWidth, double minHeight) {
		double h = dim.getHeight();
		double w = dim.getWidth();
		if (w > minWidth && h > minHeight) {
			return dim;
		}
		if (h < minHeight) {
			h = minHeight;
		}
		if (w < minWidth) {
			w = minWidth;
		}
		return new Dimension2DDouble(w, h);
	}

}
