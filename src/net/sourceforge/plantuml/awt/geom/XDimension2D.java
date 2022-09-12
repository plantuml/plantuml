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
package net.sourceforge.plantuml.awt.geom;

import java.awt.Dimension;

import net.sourceforge.plantuml.utils.MathUtils;

public class XDimension2D {

	final private double width;
	final private double height;

	public XDimension2D() {
		this(0, 0);
	}

	public XDimension2D(double width, double height) {
		if (Double.isNaN(width) || Double.isNaN(height))
			throw new IllegalArgumentException();

		this.width = width;
		this.height = height;
	}

	@Override
	public String toString() {
		return "[" + width + "," + height + "]";
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public static XDimension2D delta(XDimension2D dim, double delta) {
		return delta(dim, delta, delta);
	}

	public XDimension2D withWidth(double newWidth) {
		return new XDimension2D(newWidth, height);
	}

	public static XDimension2D delta(XDimension2D dim, double deltaWidth, double deltaHeight) {
		if (deltaHeight == 0 && deltaWidth == 0)
			return dim;

		return new XDimension2D(dim.getWidth() + deltaWidth, dim.getHeight() + deltaHeight);
	}

	public static XDimension2D mergeTB(XDimension2D top, XDimension2D bottom) {
		final double width = Math.max(top.getWidth(), bottom.getWidth());
		final double height = top.getHeight() + bottom.getHeight();
		return new XDimension2D(width, height);
	}

	public static XDimension2D mergeTB(XDimension2D a, XDimension2D b, XDimension2D c) {
		final double width = MathUtils.max(a.getWidth(), b.getWidth(), c.getWidth());
		final double height = a.getHeight() + b.getHeight() + c.getHeight();
		return new XDimension2D(width, height);
	}

	public static XDimension2D mergeLR(XDimension2D left, XDimension2D right) {
		final double height = Math.max(left.getHeight(), right.getHeight());
		final double width = left.getWidth() + right.getWidth();
		return new XDimension2D(width, height);
	}

	public static XDimension2D mergeLayoutT12B3(XDimension2D top1, XDimension2D top2, XDimension2D bottom) {
		final double width = MathUtils.max(top1.getWidth(), top2.getWidth(), bottom.getWidth());
		final double height = top1.getHeight() + top2.getHeight() + bottom.getHeight();
		return new XDimension2D(width, height);
	}

	public static XDimension2D max(XDimension2D dim1, XDimension2D dim2) {
		return atLeast(dim1, dim2.getWidth(), dim2.getHeight());
	}

	public static XDimension2D atLeast(XDimension2D dim, double minWidth, double minHeight) {
		double h = dim.getHeight();
		double w = dim.getWidth();
		if (w > minWidth && h > minHeight)
			return dim;

		if (h < minHeight)
			h = minHeight;

		if (w < minWidth)
			w = minWidth;

		return new XDimension2D(w, h);
	}

	public static XDimension2D fromDimension(Dimension dimension) {
		return new XDimension2D(dimension.getWidth(), dimension.getHeight());
	}

}
