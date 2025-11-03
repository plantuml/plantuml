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
 * Original Author:  Arnaud Roques
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
import net.sourceforge.plantuml.style.ISkinParam;

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
		final double barWidth = categoryWidth * 0.6; // 60% of category width
		final double barOffset = (categoryWidth - barWidth) / 2;
		final StringBounder stringBounder = ug.getStringBounder();

		for (int i = 0; i < Math.min(values.size(), categoryCount); i++) {
			final double value = values.get(i);
			final double x = i * categoryWidth + barOffset;
			final double barHeight = Math.abs((value - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight);
			final double y = plotHeight - barHeight;

			final URectangle rect = URectangle.build(barWidth, barHeight);
			ug.apply(color).apply(color.bg()).apply(UTranslate.dx(x).compose(UTranslate.dy(y))).draw(rect);

			// Draw label if enabled
			if (series.isShowLabels()) {
				drawLabel(ug, value, x + barWidth / 2, y - 5, stringBounder);
			}
		}
	}

	private void drawHorizontal(UGraphic ug, ChartSeries series, HColor color) {
		if (categoryCount == 0)
			return;

		final List<Double> values = series.getValues();
		final double categoryHeight = plotHeight / categoryCount;
		final double barHeight = categoryHeight * 0.6; // 60% of category height
		final double barOffset = (categoryHeight - barHeight) / 2;
		final StringBounder stringBounder = ug.getStringBounder();

		for (int i = 0; i < Math.min(values.size(), categoryCount); i++) {
			final double value = values.get(i);
			final double y = i * categoryHeight + barOffset;
			final double barWidth = Math.abs((value - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotWidth);
			final double x = 0; // Start from left

			final URectangle rect = URectangle.build(barWidth, barHeight);
			ug.apply(color).apply(color.bg()).apply(UTranslate.dx(x).compose(UTranslate.dy(y))).draw(rect);

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

		for (int categoryIndex = 0; categoryIndex < categoryCount; categoryIndex++) {
			for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
				final ChartSeries series = barSeries.get(seriesIndex);
				final HColor color = colors.get(seriesIndex);
				final List<Double> values = series.getValues();

				if (categoryIndex >= values.size())
					continue;

				final double value = values.get(categoryIndex);
				final double x = categoryIndex * categoryWidth + groupOffset + seriesIndex * barWidth;
				final double barHeight = Math.abs((value - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight);
				final double y = plotHeight - barHeight;

				final URectangle rect = URectangle.build(barWidth, barHeight);
				ug.apply(color).apply(color.bg()).apply(UTranslate.dx(x).compose(UTranslate.dy(y))).draw(rect);

				// Draw label if enabled
				if (series.isShowLabels()) {
					drawLabel(ug, value, x + barWidth / 2, y - 5, stringBounder);
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
		final double barWidth = categoryWidth * 0.6; // 60% of category width
		final double barOffset = (categoryWidth - barWidth) / 2;

		// Track cumulative heights for each category
		final Map<Integer, Double> cumulativeHeights = new HashMap<>();

		for (int categoryIndex = 0; categoryIndex < categoryCount; categoryIndex++) {
			double cumulativeY = plotHeight; // Start from the bottom

			for (int seriesIndex = 0; seriesIndex < barSeries.size(); seriesIndex++) {
				final ChartSeries series = barSeries.get(seriesIndex);
				final HColor color = colors.get(seriesIndex);
				final List<Double> values = series.getValues();

				if (categoryIndex >= values.size())
					continue;

				final double value = values.get(categoryIndex);
				final double x = categoryIndex * categoryWidth + barOffset;
				final double barHeight = Math.abs((value - axis.getMin()) / (axis.getMax() - axis.getMin()) * plotHeight);

				// Stack this bar on top of previous bars
				cumulativeY -= barHeight;

				final URectangle rect = URectangle.build(barWidth, barHeight);
				ug.apply(color).apply(color.bg()).apply(UTranslate.dx(x).compose(UTranslate.dy(cumulativeY))).draw(rect);

				// Draw label if enabled
				if (series.isShowLabels()) {
					drawLabel(ug, value, x + barWidth / 2, cumulativeY - 5, stringBounder);
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
