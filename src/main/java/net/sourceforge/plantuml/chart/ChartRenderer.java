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

import java.util.List;

import net.sourceforge.plantuml.style.ISkinParam;
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
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class ChartRenderer {

	private final ISkinParam skinParam;
	private final List<String> xAxisLabels;
	private final List<ChartSeries> series;
	private final ChartAxis yAxis;
	private final ChartAxis y2Axis;

	// Layout constants
	private static final double MARGIN = 20;
	private static final double AXIS_LABEL_SPACE = 40;
	private static final double TITLE_SPACE = 30;
	private static final double TICK_SIZE = 5;

	public ChartRenderer(ISkinParam skinParam, List<String> xAxisLabels, List<ChartSeries> series,
			ChartAxis yAxis, ChartAxis y2Axis) {
		this.skinParam = skinParam;
		this.xAxisLabels = xAxisLabels;
		this.series = series;
		this.yAxis = yAxis;
		this.y2Axis = y2Axis;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final double width = MARGIN + AXIS_LABEL_SPACE + getPlotWidth() + (y2Axis != null ? AXIS_LABEL_SPACE : 0)
				+ MARGIN;
		final double height = MARGIN + TITLE_SPACE + getPlotHeight() + AXIS_LABEL_SPACE + MARGIN;
		return new XDimension2D(width, height);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		// Get style and colors
		final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		final HColor lineColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final HColor fontColor = style.value(PName.FontColor).asColor(skinParam.getIHtmlColorSet());

		// Apply stroke and color
		ug = ug.apply(lineColor).apply(UStroke.withThickness(1.0));

		// Calculate positions
		final double leftMargin = MARGIN + AXIS_LABEL_SPACE;
		final double topMargin = MARGIN + TITLE_SPACE;
		final double plotWidth = getPlotWidth();
		final double plotHeight = getPlotHeight();

		// Draw axes
		drawYAxis(ug.apply(UTranslate.dx(leftMargin).compose(UTranslate.dy(topMargin))), plotHeight, yAxis, true,
				lineColor, fontColor, stringBounder);

		if (y2Axis != null) {
			drawYAxis(ug.apply(UTranslate.dx(leftMargin + plotWidth).compose(UTranslate.dy(topMargin))), plotHeight,
					y2Axis, false, lineColor, fontColor, stringBounder);
		}

		drawXAxis(ug.apply(UTranslate.dx(leftMargin).compose(UTranslate.dy(topMargin + plotHeight))), plotWidth,
				lineColor, fontColor, stringBounder);

		// Draw series data
		final UGraphic ugPlot = ug.apply(UTranslate.dx(leftMargin).compose(UTranslate.dy(topMargin)));
		drawSeries(ugPlot, plotWidth, plotHeight, stringBounder);
	}

	private void drawYAxis(UGraphic ug, double height, ChartAxis axis, boolean leftSide, HColor lineColor,
			HColor fontColor, StringBounder stringBounder) {
		// Draw axis line
		ug.draw(ULine.vline(height));

		// Draw ticks and labels
		final int numTicks = 5;
		final UFont font = UFont.sansSerif(10);
		final FontConfiguration fontConfig = FontConfiguration.create(font, fontColor, fontColor, null);

		for (int i = 0; i <= numTicks; i++) {
			final double y = height * (1.0 - (double) i / numTicks);
			final double value = axis.getMin() + (axis.getMax() - axis.getMin()) * i / numTicks;

			// Draw tick
			if (leftSide)
				ug.apply(UTranslate.dy(y)).draw(ULine.hline(-TICK_SIZE));
			else
				ug.apply(UTranslate.dy(y)).draw(ULine.hline(TICK_SIZE));

			// Draw label
			final String label = formatValue(value);
			final TextBlock textBlock = Display.getWithNewlines(skinParam.getPragma(), label)
					.create(fontConfig, HorizontalAlignment.RIGHT, skinParam);
			final double textHeight = textBlock.calculateDimension(stringBounder).getHeight();

			if (leftSide) {
				final double textWidth = textBlock.calculateDimension(stringBounder).getWidth();
				textBlock.drawU(ug.apply(UTranslate.dx(-TICK_SIZE - textWidth - 5).compose(UTranslate.dy(y - textHeight / 2))));
			} else {
				textBlock.drawU(ug.apply(UTranslate.dx(TICK_SIZE + 5).compose(UTranslate.dy(y - textHeight / 2))));
			}
		}

		// Draw axis title
		if (axis.getTitle() != null && !axis.getTitle().isEmpty()) {
			final TextBlock titleBlock = Display.getWithNewlines(skinParam.getPragma(), axis.getTitle())
					.create(fontConfig, HorizontalAlignment.CENTER, skinParam);
			final double titleHeight = titleBlock.calculateDimension(stringBounder).getHeight();
			final double titleY = height / 2 - titleHeight / 2;

			if (leftSide) {
				titleBlock.drawU(ug.apply(UTranslate.dx(-AXIS_LABEL_SPACE + 5).compose(UTranslate.dy(titleY))));
			} else {
				titleBlock.drawU(ug.apply(UTranslate.dx(AXIS_LABEL_SPACE - 5).compose(UTranslate.dy(titleY))));
			}
		}
	}

	private void drawXAxis(UGraphic ug, double width, HColor lineColor, HColor fontColor,
			StringBounder stringBounder) {
		// Draw axis line
		ug.draw(ULine.hline(width));

		// Draw labels
		if (xAxisLabels.isEmpty())
			return;

		final UFont font = UFont.sansSerif(10);
		final FontConfiguration fontConfig = FontConfiguration.create(font, fontColor, fontColor, null);
		final double categoryWidth = width / xAxisLabels.size();

		for (int i = 0; i < xAxisLabels.size(); i++) {
			final double x = (i + 0.5) * categoryWidth;

			// Draw tick
			ug.apply(UTranslate.dx(x)).draw(ULine.vline(TICK_SIZE));

			// Draw label
			final TextBlock textBlock = Display.getWithNewlines(skinParam.getPragma(), xAxisLabels.get(i))
					.create(fontConfig, HorizontalAlignment.CENTER, skinParam);
			final double textWidth = textBlock.calculateDimension(stringBounder).getWidth();
			textBlock.drawU(ug.apply(UTranslate.dx(x - textWidth / 2).compose(UTranslate.dy(TICK_SIZE + 5))));
		}
	}

	private void drawSeries(UGraphic ug, double plotWidth, double plotHeight, StringBounder stringBounder) {
		if (xAxisLabels.isEmpty() || series.isEmpty())
			return;

		for (ChartSeries s : series) {
			final ChartAxis axis = s.isUseSecondaryAxis() && y2Axis != null ? y2Axis : yAxis;
			final HColor color = s.getColor() != null ? s.getColor() : getDefaultColor(series.indexOf(s));

			if (s.getType() == ChartSeries.SeriesType.BAR) {
				final BarRenderer barRenderer = new BarRenderer(skinParam, plotWidth, plotHeight, xAxisLabels.size(),
						axis);
				barRenderer.draw(ug, s, color);
			} else if (s.getType() == ChartSeries.SeriesType.LINE) {
				final LineRenderer lineRenderer = new LineRenderer(skinParam, plotWidth, plotHeight,
						xAxisLabels.size(), axis);
				lineRenderer.draw(ug, s, color);
			}
		}
	}

	private double getPlotWidth() {
		return Math.max(400, xAxisLabels.size() * 60);
	}

	private double getPlotHeight() {
		return 300;
	}

	private String formatValue(double value) {
		if (Math.abs(value) < 0.01 && value != 0)
			return String.format("%.2e", value);
		if (value == (long) value)
			return String.format("%d", (long) value);
		return String.format("%.2f", value);
	}

	private HColor getDefaultColor(int index) {
		final String[] defaultColors = { "#1f77b4", "#ff7f0e", "#2ca02c", "#d62728", "#9467bd", "#8c564b",
				"#e377c2", "#7f7f7f", "#bcbd22", "#17becf" };
		try {
			return skinParam.getIHtmlColorSet().getColor(defaultColors[index % defaultColors.length]);
		} catch (Exception e) {
			return null;
		}
	}

	private StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chartDiagram);
	}
}
