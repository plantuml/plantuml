/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
 * Original Author:  Roshan Banisetti
 *
 */
package net.sourceforge.plantuml.klimt.geom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class NaturalSpline {

	private NaturalSpline() {
	}

	// A natural cubic spline through the points (second derivative 0 at both ends), each interval
	// written exactly as one cubic Bezier curve. Empty when it cannot be built: fewer than two
	// points, or x values that do not strictly increase.
	public static List<XCubicCurve2D> through(List<XPoint2D> pts) {
		final int n = pts.size();
		if (n < 2)
			return Collections.emptyList();
		final double[] h = new double[n - 1];
		for (int i = 0; i < n - 1; i++) {
			h[i] = pts.get(i + 1).x - pts.get(i).x;
			if (h[i] <= 0)
				return Collections.emptyList();
		}
		// Second derivatives m[1..n-2] from the tridiagonal system, solved with the Thomas algorithm
		final double[] m = new double[n];
		final double[] c = new double[n];
		final double[] d = new double[n];
		for (int i = 1; i < n - 1; i++) {
			final double b = 2 * (h[i - 1] + h[i]);
			final double r = 6 * ((pts.get(i + 1).y - pts.get(i).y) / h[i] - (pts.get(i).y - pts.get(i - 1).y) / h[i - 1]);
			final double denominator = b - h[i - 1] * c[i - 1];
			c[i] = h[i] / denominator;
			d[i] = (r - h[i - 1] * d[i - 1]) / denominator;
		}
		for (int i = n - 2; i >= 1; i--)
			m[i] = d[i] - c[i] * m[i + 1];

		final List<XCubicCurve2D> result = new ArrayList<>();
		for (int i = 0; i < n - 1; i++) {
			final double x0 = pts.get(i).x;
			final double y0 = pts.get(i).y;
			final double x1 = pts.get(i + 1).x;
			final double y1 = pts.get(i + 1).y;
			final double slope = (y1 - y0) / h[i];
			final double start = slope - h[i] * (2 * m[i] + m[i + 1]) / 6;
			final double end = slope + h[i] * (m[i] + 2 * m[i + 1]) / 6;
			result.add(new XCubicCurve2D(x0, y0, x0 + h[i] / 3, y0 + start * h[i] / 3, x1 - h[i] / 3,
					y1 - end * h[i] / 3, x1, y1));
		}
		return result;
	}

}
