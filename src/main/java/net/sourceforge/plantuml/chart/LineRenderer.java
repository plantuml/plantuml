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
 * Original Author:  David Fyfe
 *
 */
package net.sourceforge.plantuml.chart;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.style.ISkinParam;

public class LineRenderer {

	private final ISkinParam skinParam;
	private final double plotWidth;
	private final double plotHeight;
	private final int categoryCount;
	private final ChartAxis axis;
	private final ChartAxis xAxis;

	public LineRenderer(ISkinParam skinParam, double plotWidth, double plotHeight, int categoryCount,
			ChartAxis axis, ChartAxis xAxis) {
		this.skinParam = skinParam;
		this.plotWidth = plotWidth;
		this.plotHeight = plotHeight;
		this.categoryCount = categoryCount;
		this.axis = axis;
		this.xAxis = xAxis;
	}

	public void draw(UGraphic ug, ChartSeries series, HColor color) {
		if (categoryCount == 0 && !series.hasExplicitXValues())
			return;

		if (series.isInterpolate()) {
			final UPath curve = naturalSpline(points(series));
			if (curve != null) {
				ug.apply(color).apply(UStroke.withThickness(2.0)).apply(HColors.none().bg()).draw(curve);
				return;
			}
		}

		final List<Double> values = series.getValues();

		if (series.hasExplicitXValues()) {
			// Coordinate-pair mode: use explicit x-values
			final List<Double> xValues = series.getXValues();

			// Safety check
			if (xAxis == null) {
				System.err.println("ERROR: xAxis is null in coordinate-pair mode!");
				return;
			}

			// Draw line segments
			for (int i = 0; i < values.size() - 1; i++) {
				final double xVal1 = xValues.get(i);
				final double yVal1 = values.get(i);
				final double xVal2 = xValues.get(i + 1);
				final double yVal2 = values.get(i + 1);

				final double x1 = xAxis.valueToPixel(xVal1, 0, plotWidth);
				final double y1 = plotHeight - (yVal1 - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;
				final double x2 = xAxis.valueToPixel(xVal2, 0, plotWidth);
				final double y2 = plotHeight - (yVal2 - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;

				final ULine line = new ULine(x2 - x1, y2 - y1);
				ug.apply(color).apply(UStroke.withThickness(2.0)).apply(UTranslate.dx(x1).compose(UTranslate.dy(y1))).draw(line);
			}
		} else {
			// Index-based mode: use category positioning
			final double categoryWidth = plotWidth / categoryCount;

			// Draw line segments
			for (int i = 0; i < Math.min(values.size() - 1, categoryCount - 1); i++) {
				// No segment touches a missing value, so the line has a gap there
				if (values.get(i) == null || values.get(i + 1) == null)
					continue;
				final double value1 = values.get(i);
				final double value2 = values.get(i + 1);

				final double x1 = (i + 0.5) * categoryWidth;
				final double y1 = plotHeight - (value1 - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;

				final double x2 = (i + 1.5) * categoryWidth;
				final double y2 = plotHeight - (value2 - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;

				final ULine line = new ULine(x2 - x1, y2 - y1);
				ug.apply(color).apply(UStroke.withThickness(2.0)).apply(UTranslate.dx(x1).compose(UTranslate.dy(y1))).draw(line);
			}
		}
	}

	// The points of the series in pixels, leaving out missing values
	private List<double[]> points(ChartSeries series) {
		final List<double[]> result = new ArrayList<>();
		final List<Double> values = series.getValues();
		final double range = axis.getMax() - axis.getMin();
		if (series.hasExplicitXValues()) {
			if (xAxis == null)
				return result;
			final List<Double> xValues = series.getXValues();
			for (int i = 0; i < values.size(); i++)
				result.add(new double[] { xAxis.valueToPixel(xValues.get(i), 0, plotWidth),
						plotHeight - (values.get(i) - axis.getMin()) / range * plotHeight });
		} else {
			final double categoryWidth = plotWidth / categoryCount;
			for (int i = 0; i < Math.min(values.size(), categoryCount); i++)
				if (values.get(i) != null)
					result.add(new double[] { (i + 0.5) * categoryWidth,
							plotHeight - (values.get(i) - axis.getMin()) / range * plotHeight });
		}
		return result;
	}

	// A natural cubic spline through the points (second derivative 0 at both ends), each interval
	// written exactly as one cubic Bezier curve. Null when it cannot be drawn: fewer than two
	// points, or x values that do not strictly increase.
	private static UPath naturalSpline(List<double[]> pts) {
		final int n = pts.size();
		if (n < 2)
			return null;
		final double[] h = new double[n - 1];
		for (int i = 0; i < n - 1; i++) {
			h[i] = pts.get(i + 1)[0] - pts.get(i)[0];
			if (h[i] <= 0)
				return null;
		}
		// Second derivatives m[1..n-2] from the tridiagonal system, solved with the Thomas algorithm
		final double[] m = new double[n];
		final double[] c = new double[n];
		final double[] d = new double[n];
		for (int i = 1; i < n - 1; i++) {
			final double b = 2 * (h[i - 1] + h[i]);
			final double r = 6 * ((pts.get(i + 1)[1] - pts.get(i)[1]) / h[i]
					- (pts.get(i)[1] - pts.get(i - 1)[1]) / h[i - 1]);
			final double denominator = b - h[i - 1] * c[i - 1];
			c[i] = h[i] / denominator;
			d[i] = (r - h[i - 1] * d[i - 1]) / denominator;
		}
		for (int i = n - 2; i >= 1; i--)
			m[i] = d[i] - c[i] * m[i + 1];

		final UPath path = UPath.none();
		path.moveTo(pts.get(0)[0], pts.get(0)[1]);
		for (int i = 0; i < n - 1; i++) {
			final double x0 = pts.get(i)[0];
			final double y0 = pts.get(i)[1];
			final double x1 = pts.get(i + 1)[0];
			final double y1 = pts.get(i + 1)[1];
			final double slope = (y1 - y0) / h[i];
			final double start = slope - h[i] * (2 * m[i] + m[i + 1]) / 6;
			final double end = slope + h[i] * (m[i] + 2 * m[i + 1]) / 6;
			path.cubicTo(x0 + h[i] / 3, y0 + start * h[i] / 3, x1 - h[i] / 3, y1 - end * h[i] / 3, x1, y1);
		}
		return path;
	}

}
