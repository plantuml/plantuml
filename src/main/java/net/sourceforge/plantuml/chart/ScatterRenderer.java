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

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class ScatterRenderer {

	private final ISkinParam skinParam;
	private final double plotWidth;
	private final double plotHeight;
	private final int categoryCount;
	private final ChartAxis axis;
	private final ChartAxis xAxis;

	public ScatterRenderer(ISkinParam skinParam, double plotWidth, double plotHeight, int categoryCount,
			ChartAxis axis, ChartAxis xAxis) {
		this.skinParam = skinParam;
		this.plotWidth = plotWidth;
		this.plotHeight = plotHeight;
		this.categoryCount = categoryCount;
		this.axis = axis;
		this.xAxis = xAxis;
	}

	private StyleSignatureBasic getScatterStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chartDiagram, SName.scatter);
	}

	private Style getScatterStyle(ChartSeries series) {
		StyleSignatureBasic signature = getScatterStyleSignature();
		if (series != null && series.getStereotype() != null) {
			return signature.withTOBECHANGED(series.getStereotype())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		}
		return signature.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	public void draw(UGraphic ug, ChartSeries series, HColor color) {
		if (categoryCount == 0 && !series.hasExplicitXValues())
			return;

		final List<Double> values = series.getValues();
		final StringBounder stringBounder = ug.getStringBounder();

		// Get scatter style (with stereotype support)
		final Style scatterStyle = getScatterStyle(series);

		// Extract marker color from style - priority: MarkerColor > LineColor > explicit color
		HColor markerColor = color;
		HColor styleMarkerColor = scatterStyle.value(PName.MarkerColor).asColor(skinParam.getIHtmlColorSet());
		if (styleMarkerColor != null) {
			markerColor = styleMarkerColor;
		} else {
			HColor styleLineColor = scatterStyle.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
			if (styleLineColor != null && color == null) {
				markerColor = styleLineColor;
			}
		}

		// Extract marker size from style (default 8)
		double markerSize = 8.0;
		try {
			final Double styleMarkerSize = scatterStyle.value(PName.MarkerSize).asDouble();
			if (styleMarkerSize != null && styleMarkerSize > 0) {
				markerSize = styleMarkerSize;
			}
		} catch (Exception e) {
			// Use default
		}

		// Extract marker shape from style (default to series marker or CIRCLE)
		ChartSeries.MarkerShape markerShape = series.getMarkerShape();
		try {
			final String styleMarkerShape = scatterStyle.value(PName.MarkerShape).asString();
			if (styleMarkerShape != null && !styleMarkerShape.isEmpty()) {
				switch (styleMarkerShape.toLowerCase()) {
					case "circle":
						markerShape = ChartSeries.MarkerShape.CIRCLE;
						break;
					case "square":
						markerShape = ChartSeries.MarkerShape.SQUARE;
						break;
					case "triangle":
						markerShape = ChartSeries.MarkerShape.TRIANGLE;
						break;
				}
			}
		} catch (Exception e) {
			// Use default
		}

		if (series.hasExplicitXValues()) {
			// Coordinate-pair mode: use explicit x-values
			final List<Double> xValues = series.getXValues();

			// Draw markers at data points (no connecting lines)
			for (int i = 0; i < values.size(); i++) {
				final double xVal = xValues.get(i);
				final double yVal = values.get(i);

				final double x = xAxis.valueToPixel(xVal, 0, plotWidth);
				final double y = plotHeight - (yVal - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;

				drawMarker(ug, markerColor, x, y, markerSize, markerShape);

				// Draw label if enabled
				if (series.isShowLabels()) {
					drawLabel(ug, yVal, x, y - markerSize / 2 - 8, stringBounder);
				}
			}
		} else {
			// Index-based mode: use category positioning
			final double categoryWidth = plotWidth / categoryCount;

			// Draw markers at data points (no connecting lines)
			for (int i = 0; i < Math.min(values.size(), categoryCount); i++) {
				final double value = values.get(i);
				final double x = (i + 0.5) * categoryWidth;
				final double y = plotHeight - (value - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;

				drawMarker(ug, markerColor, x, y, markerSize, markerShape);

				// Draw label if enabled
				if (series.isShowLabels()) {
					drawLabel(ug, value, x, y - markerSize / 2 - 8, stringBounder);
				}
			}
		}
	}

	private void drawMarker(UGraphic ug, HColor color, double x, double y, double size, ChartSeries.MarkerShape shape) {
		switch (shape) {
		case CIRCLE:
			drawCircleMarker(ug, color, x, y, size);
			break;
		case SQUARE:
			drawSquareMarker(ug, color, x, y, size);
			break;
		case TRIANGLE:
			drawTriangleMarker(ug, color, x, y, size);
			break;
		default:
			drawCircleMarker(ug, color, x, y, size);
		}
	}

	private void drawCircleMarker(UGraphic ug, HColor color, double x, double y, double size) {
		final UEllipse marker = UEllipse.build(size, size);
		ug.apply(color).apply(color.bg()).apply(UTranslate.dx(x - size / 2).compose(UTranslate.dy(y - size / 2))).draw(marker);
	}

	private void drawSquareMarker(UGraphic ug, HColor color, double x, double y, double size) {
		final URectangle marker = URectangle.build(size, size);
		ug.apply(color).apply(color.bg()).apply(UTranslate.dx(x - size / 2).compose(UTranslate.dy(y - size / 2))).draw(marker);
	}

	private void drawTriangleMarker(UGraphic ug, HColor color, double x, double y, double size) {
		final UPolygon triangle = new UPolygon();
		// Create upward-pointing triangle
		triangle.addPoint(0, -size / 2);          // Top point
		triangle.addPoint(-size / 2, size / 2);   // Bottom left
		triangle.addPoint(size / 2, size / 2);    // Bottom right
		ug.apply(color).apply(color.bg()).apply(UTranslate.dx(x).compose(UTranslate.dy(y))).draw(triangle);
	}

	private void drawLabel(UGraphic ug, double value, double x, double y, StringBounder stringBounder) {
		try {
			final String label = formatValue(value);
			final UFont font = UFontFactory.sansSerif(10).bold();
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
