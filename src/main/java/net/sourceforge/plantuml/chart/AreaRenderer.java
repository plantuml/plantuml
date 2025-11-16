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

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
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
		final double categoryWidth = plotWidth / categoryCount;
		final StringBounder stringBounder = ug.getStringBounder();

		// Create polygon for filled area
		final UPolygon polygon = new UPolygon();

		// Add points along the top of the area (cumulative values)
		for (int i = 0; i < Math.min(values.size(), categoryCount); i++) {
			final double value = values.get(i);
			// If we have baseline values, add current value to baseline to get cumulative top
			double topValue = value;
			if (baselineValues != null && i < baselineValues.size()) {
				topValue = baselineValues.get(i) + value;
			}
			final double x = (i + 0.5) * categoryWidth;
			final double y = plotHeight - (topValue - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;
			polygon.addPoint(x, y);
		}

		// Add points along the bottom (baseline - either axis minimum or previous series top)
		for (int i = Math.min(values.size(), categoryCount) - 1; i >= 0; i--) {
			final double x = (i + 0.5) * categoryWidth;
			double baselineValue = axis.getMin();
			if (baselineValues != null && i < baselineValues.size()) {
				baselineValue = baselineValues.get(i);
			}
			final double y = plotHeight - (baselineValue - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;
			polygon.addPoint(x, y);
		}

		// Draw filled area with transparency
		ug.apply(color).apply(color.bg()).draw(polygon);

		// Draw top line of the area (for definition)
		for (int i = 0; i < Math.min(values.size() - 1, categoryCount - 1); i++) {
			final double value1 = values.get(i);
			final double value2 = values.get(i + 1);

			// Calculate cumulative top values
			double topValue1 = value1;
			double topValue2 = value2;
			if (baselineValues != null) {
				if (i < baselineValues.size()) {
					topValue1 = baselineValues.get(i) + value1;
				}
				if (i + 1 < baselineValues.size()) {
					topValue2 = baselineValues.get(i + 1) + value2;
				}
			}

			final double x1 = (i + 0.5) * categoryWidth;
			final double y1 = plotHeight - (topValue1 - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;

			final double x2 = (i + 1.5) * categoryWidth;
			final double y2 = plotHeight - (topValue2 - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;

			final net.sourceforge.plantuml.klimt.shape.ULine line = new net.sourceforge.plantuml.klimt.shape.ULine(x2 - x1, y2 - y1);
			ug.apply(color).apply(UStroke.withThickness(2.0)).apply(UTranslate.dx(x1).compose(UTranslate.dy(y1))).draw(line);
		}

		// Draw labels if enabled
		if (series.isShowLabels()) {
			for (int i = 0; i < Math.min(values.size(), categoryCount); i++) {
				final double value = values.get(i);
				// Calculate cumulative top value for label position
				double topValue = value;
				if (baselineValues != null && i < baselineValues.size()) {
					topValue = baselineValues.get(i) + value;
				}
				final double x = (i + 0.5) * categoryWidth;
				final double y = plotHeight - (topValue - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;
				drawLabel(ug, value, x, y - 8, stringBounder);
			}
		}
	}

	private void drawLabel(UGraphic ug, double value, double x, double y, StringBounder stringBounder) {
		try {
			final String label = formatValue(value);
			final UFont font = UFont.sansSerif(10).bold();
			final HColor labelColor = skinParam.getIHtmlColorSet().getColor("#000000");
			final FontConfiguration fontConfig = FontConfiguration.create(font, labelColor, labelColor, null);
			final TextBlock textBlock = Display.getWithNewlines(skinParam.getPragma(), label)
					.create(fontConfig, HorizontalAlignment.CENTER, skinParam);
			final XDimension2D textDim = textBlock.calculateDimension(stringBounder);
			final double labelX = x - textDim.getWidth() / 2;
			final double labelY = y - textDim.getHeight() - 2;
			textBlock.drawU(ug.apply(UTranslate.dx(labelX).compose(UTranslate.dy(labelY))));
		} catch (Exception e) {
			// Silently ignore rendering errors
		}
	}

	private String formatValue(double value) {
		if (Math.abs(value) < 0.01 && value != 0)
			return String.format("%.2e", value);
		if (value == (long) value)
			return String.format("%d", (long) value);
		return String.format("%.2f", value);
	}
}
