/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
 */
package net.sourceforge.plantuml.klimt.drawing.raster;

import java.util.Arrays;

/**
 * A shape to fill or stroke on a {@link RasterCanvas}: a sequence of segments,
 * as a {@code java.awt.geom.GeneralPath}, where rectangles, ellipses and arcs
 * stay whole (each canvas draws them the way it draws them best, Java2D with
 * its own shapes).
 */
public final class RasterPath {

	public static final int MOVE_TO = 0;
	public static final int LINE_TO = 1;
	/** Control point 1, control point 2, end point. */
	public static final int CUBIC_TO = 2;
	public static final int CLOSE = 3;
	/**
	 * x, y, width, height, arcWidth, arcHeight: as {@code RoundRectangle2D}, the
	 * arcs are the diameters of the corners (a plain rectangle when both are 0).
	 */
	public static final int RECTANGLE = 4;
	/** x, y, width, height of the bounding box, as {@code Ellipse2D}. */
	public static final int ELLIPSE = 5;
	/**
	 * x, y, width, height of the bounding box, start, extent: an open arc, as
	 * {@code Arc2D} (degrees, counterclockwise on screen, 0 at 3 o'clock).
	 */
	public static final int ARC = 6;
	/**
	 * rx, ry, x axis rotation (degrees), large arc flag, sweep flag, x, y: an
	 * elliptical arc from the current point, as in SVG.
	 */
	public static final int ARC_TO = 7;

	private static final int[] SIZES = { 2, 2, 6, 0, 6, 4, 6, 7 };

	private int[] types = new int[8];
	private double[] coords = new double[32];
	private int count;
	private int coordCount;

	public static RasterPath rectangle(double x, double y, double width, double height, double arcWidth,
			double arcHeight) {
		return new RasterPath().add(RECTANGLE, x, y, width, height, arcWidth, arcHeight);
	}

	public static RasterPath ellipse(double x, double y, double width, double height) {
		return new RasterPath().add(ELLIPSE, x, y, width, height);
	}

	public static RasterPath arc(double x, double y, double width, double height, double start, double extent) {
		return new RasterPath().add(ARC, x, y, width, height, start, extent);
	}

	public static RasterPath line(double x1, double y1, double x2, double y2) {
		return new RasterPath().moveTo(x1, y1).lineTo(x2, y2);
	}

	public RasterPath moveTo(double x, double y) {
		return add(MOVE_TO, x, y);
	}

	public RasterPath lineTo(double x, double y) {
		return add(LINE_TO, x, y);
	}

	public RasterPath cubicTo(double x1, double y1, double x2, double y2, double x3, double y3) {
		return add(CUBIC_TO, x1, y1, x2, y2, x3, y3);
	}

	public RasterPath arcTo(double rx, double ry, double rotation, boolean largeArc, boolean sweep, double x,
			double y) {
		return add(ARC_TO, rx, ry, rotation, largeArc ? 1 : 0, sweep ? 1 : 0, x, y);
	}

	public RasterPath closePath() {
		return add(CLOSE);
	}

	private RasterPath add(int type, double... values) {
		if (values.length != SIZES[type])
			throw new IllegalArgumentException();
		if (count == types.length)
			types = Arrays.copyOf(types, count * 2);
		if (coordCount + values.length > coords.length)
			coords = Arrays.copyOf(coords, Math.max(coords.length * 2, coordCount + values.length));
		types[count++] = type;
		System.arraycopy(values, 0, coords, coordCount, values.length);
		coordCount += values.length;
		return this;
	}

	public boolean isEmpty() {
		return count == 0;
	}

	/**
	 * Calls the visitor for every segment, in order.
	 */
	public void visit(Visitor visitor) {
		int c = 0;
		for (int i = 0; i < count; i++) {
			final double[] v = coords;
			switch (types[i]) {
			case MOVE_TO:
				visitor.moveTo(v[c], v[c + 1]);
				break;
			case LINE_TO:
				visitor.lineTo(v[c], v[c + 1]);
				break;
			case CUBIC_TO:
				visitor.cubicTo(v[c], v[c + 1], v[c + 2], v[c + 3], v[c + 4], v[c + 5]);
				break;
			case CLOSE:
				visitor.closePath();
				break;
			case RECTANGLE:
				visitor.rectangle(v[c], v[c + 1], v[c + 2], v[c + 3], v[c + 4], v[c + 5]);
				break;
			case ELLIPSE:
				visitor.ellipse(v[c], v[c + 1], v[c + 2], v[c + 3]);
				break;
			case ARC:
				visitor.arc(v[c], v[c + 1], v[c + 2], v[c + 3], v[c + 4], v[c + 5]);
				break;
			case ARC_TO:
				visitor.arcTo(v[c], v[c + 1], v[c + 2], v[c + 3] != 0, v[c + 4] != 0, v[c + 5], v[c + 6]);
				break;
			default:
				throw new IllegalStateException();
			}
			c += SIZES[types[i]];
		}
	}

	/**
	 * The segments of a {@link RasterPath}, see its constants.
	 */
	public interface Visitor {

		public void moveTo(double x, double y);

		public void lineTo(double x, double y);

		public void cubicTo(double x1, double y1, double x2, double y2, double x3, double y3);

		public void closePath();

		public void rectangle(double x, double y, double width, double height, double arcWidth, double arcHeight);

		public void ellipse(double x, double y, double width, double height);

		public void arc(double x, double y, double width, double height, double start, double extent);

		public void arcTo(double rx, double ry, double rotation, boolean largeArc, boolean sweep, double x, double y);

	}

}
