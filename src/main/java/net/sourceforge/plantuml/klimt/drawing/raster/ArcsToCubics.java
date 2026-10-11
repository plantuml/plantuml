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

/**
 * A {@link RasterPath.Visitor} for canvases that only know cubic Bézier curves:
 * it turns the arcs into cubics (at most a quarter of a turn each), and keeps
 * track of the current point, which SVG arcs start from.
 */
public abstract class ArcsToCubics implements RasterPath.Visitor {

	private double currentX;
	private double currentY;
	private double startX;
	private double startY;

	/** Segments drawn by the canvas, once the arcs are turned into cubics. */
	protected abstract void doMoveTo(double x, double y);

	protected abstract void doLineTo(double x, double y);

	protected abstract void doCubicTo(double x1, double y1, double x2, double y2, double x3, double y3);

	protected abstract void doClosePath();

	@Override
	public final void moveTo(double x, double y) {
		doMoveTo(x, y);
		currentX = startX = x;
		currentY = startY = y;
	}

	@Override
	public final void lineTo(double x, double y) {
		doLineTo(x, y);
		currentX = x;
		currentY = y;
	}

	@Override
	public final void cubicTo(double x1, double y1, double x2, double y2, double x3, double y3) {
		doCubicTo(x1, y1, x2, y2, x3, y3);
		currentX = x3;
		currentY = y3;
	}

	@Override
	public final void closePath() {
		doClosePath();
		currentX = startX;
		currentY = startY;
	}

	/**
	 * An open arc as {@code Arc2D}: the point at angle a is (cx + rx cos a, cy - ry
	 * sin a), angles in degrees.
	 */
	@Override
	public final void arc(double x, double y, double width, double height, double start, double extent) {
		final double rx = width / 2;
		final double ry = height / 2;
		final double cx = x + rx;
		final double cy = y + ry;
		final double a0 = Math.toRadians(start);
		final double delta = Math.toRadians(extent);
		moveTo(cx + rx * Math.cos(a0), cy - ry * Math.sin(a0));
		final int n = Math.max(1, (int) Math.ceil(Math.abs(delta) / (Math.PI / 2) - 1e-9));
		final double step = delta / n;
		final double k = 4.0 / 3.0 * Math.tan(step / 4);
		for (int i = 0; i < n; i++) {
			final double t0 = a0 + i * step;
			final double t1 = t0 + step;
			final double x0 = cx + rx * Math.cos(t0);
			final double y0 = cy - ry * Math.sin(t0);
			final double x1 = cx + rx * Math.cos(t1);
			final double y1 = cy - ry * Math.sin(t1);
			// Derivatives of the parametric arc at t0 and t1
			final double dx0 = -rx * Math.sin(t0);
			final double dy0 = -ry * Math.cos(t0);
			final double dx1 = -rx * Math.sin(t1);
			final double dy1 = -ry * Math.cos(t1);
			cubicTo(x0 + k * dx0, y0 + k * dy0, x1 - k * dx1, y1 - k * dy1, x1, y1);
		}
	}

	/**
	 * An SVG elliptical arc from the current point (endpoint to center
	 * parameterization of the SVG specification, appendix F.6.5).
	 */
	@Override
	public final void arcTo(double rx, double ry, double rotation, boolean largeArc, boolean sweep, double x,
			double y) {
		final double x1 = currentX;
		final double y1 = currentY;
		if (x1 == x && y1 == y)
			return;
		rx = Math.abs(rx);
		ry = Math.abs(ry);
		if (rx == 0 || ry == 0) {
			lineTo(x, y);
			return;
		}
		final double phi = Math.toRadians(rotation % 360);
		final double cos = Math.cos(phi);
		final double sin = Math.sin(phi);
		final double dx2 = (x1 - x) / 2;
		final double dy2 = (y1 - y) / 2;
		final double x1p = cos * dx2 + sin * dy2;
		final double y1p = -sin * dx2 + cos * dy2;
		// Radii too small: scale them up
		final double lambda = (x1p * x1p) / (rx * rx) + (y1p * y1p) / (ry * ry);
		if (lambda > 1) {
			rx *= Math.sqrt(lambda);
			ry *= Math.sqrt(lambda);
		}
		final double numerator = rx * rx * ry * ry - rx * rx * y1p * y1p - ry * ry * x1p * x1p;
		final double denominator = rx * rx * y1p * y1p + ry * ry * x1p * x1p;
		double coef = denominator == 0 ? 0 : Math.sqrt(Math.max(0, numerator / denominator));
		if (largeArc == sweep)
			coef = -coef;
		final double cxp = coef * rx * y1p / ry;
		final double cyp = -coef * ry * x1p / rx;
		final double cx = cos * cxp - sin * cyp + (x1 + x) / 2;
		final double cy = sin * cxp + cos * cyp + (y1 + y) / 2;
		final double theta1 = angle(1, 0, (x1p - cxp) / rx, (y1p - cyp) / ry);
		double delta = angle((x1p - cxp) / rx, (y1p - cyp) / ry, (-x1p - cxp) / rx, (-y1p - cyp) / ry);
		if (sweep == false && delta > 0)
			delta -= 2 * Math.PI;
		else if (sweep && delta < 0)
			delta += 2 * Math.PI;

		final int n = Math.max(1, (int) Math.ceil(Math.abs(delta) / (Math.PI / 2) - 1e-9));
		final double step = delta / n;
		final double k = 4.0 / 3.0 * Math.tan(step / 4);
		for (int i = 0; i < n; i++) {
			final double t0 = theta1 + i * step;
			final double t1 = t0 + step;
			final double ex0 = rx * Math.cos(t0);
			final double ey0 = ry * Math.sin(t0);
			final double ex1 = rx * Math.cos(t1);
			final double ey1 = ry * Math.sin(t1);
			final double dx0 = -rx * Math.sin(t0);
			final double dy0 = ry * Math.cos(t0);
			final double dx1 = -rx * Math.sin(t1);
			final double dy1 = ry * Math.cos(t1);
			final double px0 = cx + cos * ex0 - sin * ey0;
			final double py0 = cy + sin * ex0 + cos * ey0;
			final double px1 = i == n - 1 ? x : cx + cos * ex1 - sin * ey1;
			final double py1 = i == n - 1 ? y : cy + sin * ex1 + cos * ey1;
			cubicTo(px0 + k * (cos * dx0 - sin * dy0), py0 + k * (sin * dx0 + cos * dy0),
					px1 - k * (cos * dx1 - sin * dy1), py1 - k * (sin * dx1 + cos * dy1), px1, py1);
		}
	}

	// Signed angle from vector u to vector v
	private static double angle(double ux, double uy, double vx, double vy) {
		return Math.atan2(ux * vy - uy * vx, ux * vx + uy * vy);
	}

}
