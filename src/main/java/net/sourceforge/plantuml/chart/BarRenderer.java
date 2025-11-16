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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class BarRenderer {

	private final ISkinParam skinParam;
	private final double plotWidth;
	private final double plotHeight;
	private final int categoryCount;
	private final ChartAxis axis;
	private final boolean horizontal;

	public BarRenderer(ISkinParam skinParam, double plotWidth, double plotHeight, int categoryCount,
			ChartAxis axis) {
		this(skinParam, plotWidth, plotHeight, categoryCount, axis, false);
	}

	public BarRenderer(ISkinParam skinParam, double plotWidth, double plotHeight, int categoryCount,
			ChartAxis axis, boolean horizontal) {
		this.skinParam = skinParam;
		this.plotWidth = plotWidth;
		this.plotHeight = plotHeight;
		this.categoryCount = categoryCount;
		this.axis = axis;
		this.horizontal = horizontal;
	}

	private StyleSignatureBasic getBarStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chartDiagram, SName.bar);
	}

	private Style getBarStyle(ChartSeries series) {
		StyleSignatureBasic signature = getBarStyleSignature();
		if (series != null && series.getStereotype() != null) {
			return signature.withTOBECHANGED(series.getStereotype())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
		}
		return signature.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	public void draw(UGraphic ug, ChartSeries series, HColor color) {
		if (horizontal) {
			drawHorizontal(ug, series, color);
		} else {
			drawVertical(ug, series, color);
		}
	}

	private void drawVertical(UGraphic ug, ChartSeries series, HColor color) {
		// This method is for single-series rendering (backward compatibility)
		if (categoryCount == 0)
			return;

		final List<Double> values = series.getValues();
		final double categoryWidth = plotWidth / categoryCount;
		final StringBounder stringBounder = ug.getStringBounder();

		// Get bar style (with stereotype support)
		final Style barStyle = getBarStyle(series);

		// Extract bar width ratio from style (default 0.6 = 60%)
		double barWidthRatio = 0.6;
		try {
			final Double styleBarWidth = barStyle.value(PName.BarWidth).asDouble();
			if (styleBarWidth != null && styleBarWidth > 0 && styleBarWidth <= 1.0) {
				barWidthRatio = styleBarWidth;
			}
		} catch (Exception e) {
			// Use default
		}
		final double barWidth = categoryWidth * barWidthRatio;
		final double barOffset = (categoryWidth - barWidth) / 2;

		// Extract line color and thickness for bar borders
		HColor lineColor = barStyle.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		if (lineColor == null) {
			lineColor = color; // Use fill color if no line color specified
		}
		final double lineThickness = barStyle.value(PName.LineThickness).asDouble();
		final UStroke stroke = UStroke.withThickness(lineThickness);

		for (int i = 0; i < Math.min(values.size(), categoryCount); i++) {
			final double value = values.get(i);
			final double x = i * categoryWidth + barOffset;

			// For negative values, bar extends from zero down to the value
			// For positive values, bar extends from zero up to the value
			final double zeroY = plotHeight - (0 - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;
			final double valueY = plotHeight - (value - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;

			final double y;
			final double barHeight;
			if (value < 0) {
				// Negative: bar top at zero, extends downward
				y = zeroY;
				barHeight = valueY - zeroY;
			} else {
				// Positive: bar top at value, extends downward to zero
				y = valueY;
				barHeight = zeroY - valueY;
			}

			final URectangle rect = URectangle.build(barWidth, barHeight);
			ug.apply(lineColor).apply(stroke).apply(color.bg()).apply(UTranslate.dx(x).compose(UTranslate.dy(y))).draw(rect);

			// Draw label if enabled
			if (series.isShowLabels()) {
				// Label at the end of the bar (top for positive, bottom for negative)
				final double labelY = value < 0 ? y + barHeight + 5 : y - 5;
				drawLabel(ug, value, x + barWidth / 2, labelY, stringBounder);
			}
		}
	}

	private void drawHorizontal(UGraphic ug, ChartSeries series, HColor color) {
		if (categoryCount == 0)
			return;

		final List<Double> values = series.getValues();
		final double categoryHeight = plotHeight / categoryCount;
		final StringBounder stringBounder = ug.getStringBounder();

		// Get bar style (with stereotype support)
		final Style barStyle = getBarStyle(series);

		// Extract bar width ratio from style (default 0.6 = 60%)
		double barWidthRatio = 0.6;
		try {
			final Double styleBarWidth = barStyle.value(PName.BarWidth).asDouble();
			if (styleBarWidth != null && styleBarWidth > 0 && styleBarWidth <= 1.0) {
				barWidthRatio = styleBarWidth;
			}
		} catch (Exception e) {
			// Use default
		}
		final double barHeight = categoryHeight * barWidthRatio;
		final double barOffset = (categoryHeight - barHeight) / 2;

		// Extract line color and thickness for bar borders
		HColor lineColor = barStyle.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		if (lineColor == null) {
			lineColor = color; // Use fill color if no line color specified
		}
		final double lineThickness = barStyle.value(PName.LineThickness).asDouble();
		final UStroke stroke = UStroke.withThickness(lineThickness);

		for (int i = 0; i < Math.min(values.size(), categoryCount); i++) {
			final double value = values.get(i);
			final double y = i * categoryHeight + barOffset;
			final double barWidth = Math.abs((value - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotWidth);
			final double x = 0; // Start from left

			final URectangle rect = URectangle.build(barWidth, barHeight);
			ug.apply(lineColor).apply(stroke).apply(color.bg()).apply(UTranslate.dx(x).compose(UTranslate.dy(y))).draw(rect);

			// Draw label if enabled
			if (series.isShowLabels()) {
				drawLabelHorizontal(ug, value, barWidth + 5, y + barHeight / 2, stringBounder);
			}
		}
	}

	/**
	 * Draw multiple bar series in grouped mode (side-by-side)
	 */
	public void drawGrouped(UGraphic ug, List<ChartSeries> barSeries, List<HColor> colors) {
		if (categoryCount == 0 || barSeries.isEmpty())
			return;

		final StringBounder stringBounder = ug.getStringBounder();
		final double categoryWidth = plotWidth / categoryCount;
		final int seriesCount = barSeries.size();
		final double groupWidth = categoryWidth * 0.8; // 80% of category for all bars
		final double barWidth = groupWidth / seriesCount;
		final double groupOffset = (categoryWidth - groupWidth) / 2;

		// Get bar style
		final Style barStyle = getBarStyleSignature()
			.getMergedStyle(skinParam.getCurrentStyleBuilder());

		// Extract line thickness for bar borders
		final double lineThickness = barStyle.value(PName.LineThickness).asDouble();
		final UStroke stroke = UStroke.withThickness(lineThickness);

		for (int categoryIndex = 0; categoryIndex < categoryCount; categoryIndex++) {
			for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
				final ChartSeries series = barSeries.get(seriesIndex);
				final HColor color = colors.get(seriesIndex);
				final List<Double> values = series.getValues();

				if (categoryIndex >= values.size())
					continue;

				final double value = values.get(categoryIndex);
				final double x = categoryIndex * categoryWidth + groupOffset + seriesIndex * barWidth;

				// Calculate bar position and height relative to zero
				final double zeroY = plotHeight - (0 - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;
				final double valueY = plotHeight - (value - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;

				final double y;
				final double barHeight;
				if (value < 0) {
					// Negative: bar top at zero, extends downward
					y = zeroY;
					barHeight = valueY - zeroY;
				} else {
					// Positive: bar top at value, extends downward to zero
					y = valueY;
					barHeight = zeroY - valueY;
				}

				// Extract line color for this bar's border
				HColor lineColor = barStyle.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
				if (lineColor == null) {
					lineColor = color; // Use fill color if no line color specified
				}

				final URectangle rect = URectangle.build(barWidth, barHeight);
				ug.apply(lineColor).apply(stroke).apply(color.bg()).apply(UTranslate.dx(x).compose(UTranslate.dy(y))).draw(rect);

				// Draw label if enabled
				if (series.isShowLabels()) {
					// Label at the end of the bar (top for positive, bottom for negative)
					final double labelY = value < 0 ? y + barHeight + 5 : y - 5;
					drawLabel(ug, value, x + barWidth / 2, labelY, stringBounder);
				}
			}
		}
	}

	/**
	 * Draw multiple bar series in stacked mode (one on top of another)
	 */
	public void drawStacked(UGraphic ug, List<ChartSeries> barSeries, List<HColor> colors) {
		if (categoryCount == 0 || barSeries.isEmpty())
			return;

		final StringBounder stringBounder = ug.getStringBounder();
		final double categoryWidth = plotWidth / categoryCount;

		// Get bar style
		final Style barStyle = getBarStyleSignature()
			.getMergedStyle(skinParam.getCurrentStyleBuilder());

		// Extract bar width ratio from style (default 0.6 = 60%)
		double barWidthRatio = 0.6;
		try {
			final Double styleBarWidth = barStyle.value(PName.BarWidth).asDouble();
			if (styleBarWidth != null && styleBarWidth > 0 && styleBarWidth <= 1.0) {
				barWidthRatio = styleBarWidth;
			}
		} catch (Exception e) {
			// Use default
		}
		final double barWidth = categoryWidth * barWidthRatio;
		final double barOffset = (categoryWidth - barWidth) / 2;

		// Track cumulative heights for each category
		final Map<Integer, Double> cumulativeHeights = new HashMap<>();

		// Extract line thickness for bar borders
		final double lineThickness = barStyle.value(PName.LineThickness).asDouble();
		final UStroke stroke = UStroke.withThickness(lineThickness);

		for (int categoryIndex = 0; categoryIndex < categoryCount; categoryIndex++) {
			// Calculate zero position
			final double zeroY = plotHeight - (0 - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;

			// Track cumulative positions for positive and negative stacks separately
			double cumulativePositiveY = zeroY; // Positive bars grow upward from zero
			double cumulativeNegativeY = zeroY; // Negative bars grow downward from zero

			for (int seriesIndex = 0; seriesIndex < barSeries.size(); seriesIndex++) {
				final ChartSeries series = barSeries.get(seriesIndex);
				final HColor color = colors.get(seriesIndex);
				final List<Double> values = series.getValues();

				if (categoryIndex >= values.size())
					continue;

				final double value = values.get(categoryIndex);
				final double x = categoryIndex * categoryWidth + barOffset;
				final double valueY = plotHeight - (value - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight;

				final double y;
				final double barHeight;

				if (value < 0) {
					// Negative values stack downward from zero
					barHeight = valueY - zeroY;
					y = cumulativeNegativeY;
					cumulativeNegativeY += barHeight;
				} else {
					// Positive values stack upward from zero
					barHeight = zeroY - valueY;
					cumulativePositiveY -= barHeight;
					y = cumulativePositiveY;
				}

				// Extract line color for this bar's border
				HColor lineColor = barStyle.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
				if (lineColor == null) {
					lineColor = color; // Use fill color if no line color specified
				}

				final URectangle rect = URectangle.build(barWidth, barHeight);
				ug.apply(lineColor).apply(stroke).apply(color.bg()).apply(UTranslate.dx(x).compose(UTranslate.dy(y))).draw(rect);

				// Draw label if enabled
				if (series.isShowLabels()) {
					// Label at the end of the bar segment
					final double labelY = value < 0 ? y + barHeight + 5 : y - 5;
					drawLabel(ug, value, x + barWidth / 2, labelY, stringBounder);
				}
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

	private void drawLabelHorizontal(UGraphic ug, double value, double x, double y, StringBounder stringBounder) {
		try {
			final String label = formatValue(value);
			final UFont font = UFont.sansSerif(10).bold();
			final HColor labelColor = skinParam.getIHtmlColorSet().getColor("#000000");
			final FontConfiguration fontConfig = FontConfiguration.create(font, labelColor, labelColor, null);
			final TextBlock textBlock = Display.getWithNewlines(skinParam.getPragma(), label)
					.create(fontConfig, HorizontalAlignment.LEFT, skinParam);
			final XDimension2D textDim = textBlock.calculateDimension(stringBounder);
			final double labelY = y - textDim.getHeight() / 2;
			textBlock.drawU(ug.apply(UTranslate.dx(x).compose(UTranslate.dy(labelY))));
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
