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

import java.util.List;
import java.util.Locale;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFace;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.style.ISkinParam;

public class AreaRenderer {

	private final ISkinParam skinParam;
	private final double plotWidth;
	private final double plotHeight;
	private final int categoryCount;
	private final ChartAxis axis;

	public AreaRenderer(ISkinParam skinParam, double plotWidth, double plotHeight, int categoryCount,
			ChartAxis axis) {
		this.skinParam = skinParam;
		this.plotWidth = plotWidth;
		this.plotHeight = plotHeight;
		this.categoryCount = categoryCount;
		this.axis = axis;
	}

	public void draw(UGraphic ug, ChartSeries series, HColor color) {
		draw(ug, series, color, null);
	}

	public void draw(UGraphic ug, ChartSeries series, HColor color, List<Double> baselineValues) {
		if (categoryCount == 0)
			return;

		final List<Double> values = series.getValues();
		final int count = Math.min(values.size(), categoryCount);

		// A missing value (null) breaks the area: each run of present values is drawn as an
		// area of its own, as a line is broken by a missing value
		int i = 0;
		while (i < count) {
			if (values.get(i) == null) {
				i++;
				continue;
			}
			int end = i;
			while (end + 1 < count && values.get(end + 1) != null)
				end++;
			drawRun(ug, values, i, end, color, baselineValues);
			i = end + 1;
		}

		// Draw labels if enabled
		if (series.isShowLabels())
			for (int k = 0; k < count; k++)
				if (values.get(k) != null) {
					final double value = values.get(k);
					// Calculate cumulative top value for label position
					drawLabel(ug, value, x(k), y(top(baselineValues, k, value)) - 8);
				}
	}

	// The area of the values from first to last, all present
	private void drawRun(UGraphic ug, List<Double> values, int first, int last, HColor color,
			List<Double> baselineValues) {
		// Create polygon for filled area
		final UPolygon polygon = new UPolygon();

		// Add points along the top of the area (cumulative values)
		for (int i = first; i <= last; i++)
			polygon.addPoint(x(i), y(top(baselineValues, i, values.get(i))));

		// Add points along the bottom (baseline - either axis minimum or previous series top)
		for (int i = last; i >= first; i--)
			polygon.addPoint(x(i), y(hasBaseline(baselineValues, i) ? baselineValues.get(i) : axis.getMin()));

		// Draw filled area with transparency
		ug.apply(color).apply(color.bg()).draw(polygon);

		// Draw top line of the area (for definition)
		for (int i = first; i < last; i++) {
			final double x1 = x(i);
			final double y1 = y(top(baselineValues, i, values.get(i)));
			final double x2 = x(i + 1);
			final double y2 = y(top(baselineValues, i + 1, values.get(i + 1)));
			final ULine line = new ULine(x2 - x1, y2 - y1);
			ug.apply(color).apply(UStroke.withThickness(2.0)).apply(UTranslate.dx(x1).compose(UTranslate.dy(y1)))
					.draw(line);
		}
	}

	private double x(int i) {
		final double categoryWidth = plotWidth / categoryCount;
		return (i + 0.5) * categoryWidth;
	}

	private double y(double value) {
		return plotHeight - (value - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;
	}

	// The base of the i-th value is the top of the previous areas, when they reach it
	// (see StackLayout.getAreaBaseline)
	private static boolean hasBaseline(List<Double> baselineValues, int i) {
		return baselineValues != null && i < baselineValues.size() && baselineValues.get(i) != null;
	}

	private static double top(List<Double> baselineValues, int i, double value) {
		if (hasBaseline(baselineValues, i))
			return baselineValues.get(i) + value;
		return value;
	}

	private void drawLabel(UGraphic ug, double value, double x, double y) {
		try {
			final String label = formatValue(value);
			final UFont font = UFontFactory.sansSerif(10).withFontFace(UFontFace.bold());
			final HColor labelColor = skinParam.getIHtmlColorSet().getColor("#000000");
			final FontConfiguration fontConfig = FontConfiguration.create(font, labelColor, labelColor, null);
			final TextBlock textBlock = Display.getWithNewlines(skinParam.getPragma(), label)
					.create(fontConfig, HorizontalAlignment.CENTER, skinParam);
			final XDimension2D textDim = textBlock.calculateDimension(ug.getStringBounder());
			final double labelX = x - textDim.getWidth() / 2;
			final double labelY = y - textDim.getHeight() - 2;
			textBlock.drawU(ug.apply(UTranslate.dx(labelX).compose(UTranslate.dy(labelY))));
		} catch (Exception e) {
			// Silently ignore rendering errors
		}
	}

	private String formatValue(double value) {
		if (Math.abs(value) < 0.01 && value != 0)
			return String.format(Locale.ROOT, "%.2e", value);
		if (value == (long) value)
			return String.format(Locale.ROOT, "%d", (long) value);
		return String.format(Locale.ROOT, "%.2f", value);
	}
}
