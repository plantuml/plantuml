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
package net.sourceforge.plantuml.utils;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.Dimension2DDouble;

public class MathUtils {

	public static double max(double a, double b) {
		return Math.max(a, b);
	}

	public static double max(double a, double b, double c) {
		return max(max(a, b), c);
	}

	public static double max(double a, double b, double c, double d) {
		return max(max(a, b), max(c, d));
	}

	public static double max(double a, double b, double c, double d, double e) {
		return max(max(a, b, c), max(d, e));
	}

	public static double min(double a, double b) {
		return Math.min(a, b);
	}

	public static double min(double a, double b, double c) {
		return min(min(a, b), c);
	}

	public static double min(double a, double b, double c, double d) {
		return min(min(a, b), min(c, d));
	}

	public static double min(double a, double b, double c, double d, double e) {
		return min(min(a, b, c), min(d, e));
	}

	public static double limitation(double v, double min, double max) {
		if (min >= max) {
			// assert false : "min="+min+" max="+max+" v="+v;
			return v;
			// throw new IllegalArgumentException("min="+min+" max="+max+" v="+v);
		}
		if (v < min) {
			return min;
		}
		if (v > max) {
			return max;
		}
		return v;
	}

	public static Dimension2D max(Dimension2D dim1, Dimension2D dim2) {
		return new Dimension2DDouble(Math.max(dim1.getWidth(), dim2.getWidth()),
				Math.max(dim1.getHeight(), dim2.getHeight()));
	}

	public static Dimension2D max(Dimension2D dim1, Dimension2D dim2, Dimension2D dim3) {
		return new Dimension2DDouble(MathUtils.max(dim1.getWidth(), dim2.getWidth(), dim3.getWidth()),
				MathUtils.max(dim1.getHeight(), dim2.getHeight(), dim3.getHeight()));
	}

	public static Point2D max(Point2D pt1, Point2D pt2) {
		return new Point2D.Double(Math.max(pt1.getX(), pt2.getX()), Math.max(pt1.getY(), pt2.getY()));
	}

	public static Point2D max(Point2D pt1, Point2D pt2, Point2D pt3) {
		return new Point2D.Double(max(pt1.getX(), pt2.getX(), pt3.getX()), max(pt1.getY(), pt2.getY(), pt3.getY()));
	}

}
