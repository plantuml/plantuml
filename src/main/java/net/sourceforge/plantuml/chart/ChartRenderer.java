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
import net.sourceforge.plantuml.klimt.color.HColors;
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
	private final String xAxisTitle;
	private final List<ChartSeries> series;
	private final ChartAxis yAxis;
	private final ChartAxis y2Axis;
	private final ChartDiagram.LegendPosition legendPosition;

	// Layout constants
	private static final double MARGIN = 20;
	private static final double AXIS_LABEL_SPACE = 40;
	private static final double TITLE_SPACE = 30;
	private static final double TICK_SIZE = 5;
	private static final double LEGEND_MARGIN = 10;
	private static final double LEGEND_SYMBOL_SIZE = 12;
	private static final double LEGEND_TEXT_SPACING = 5;
	private static final double LEGEND_ITEM_SPACING = 15;

	public ChartRenderer(ISkinParam skinParam, List<String> xAxisLabels, String xAxisTitle, List<ChartSeries> series,
			ChartAxis yAxis, ChartAxis y2Axis, ChartDiagram.LegendPosition legendPosition) {
		this.skinParam = skinParam;
		this.xAxisLabels = xAxisLabels;
		this.xAxisTitle = xAxisTitle;
		this.series = series;
		this.yAxis = yAxis;
		this.y2Axis = y2Axis;
		this.legendPosition = legendPosition;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final XDimension2D legendDim = calculateLegendDimension(stringBounder);
		double width = MARGIN + AXIS_LABEL_SPACE + getPlotWidth() + (y2Axis != null ? AXIS_LABEL_SPACE : 0) + MARGIN;
		double height = MARGIN + TITLE_SPACE + getPlotHeight() + AXIS_LABEL_SPACE + MARGIN;

		// Add space for X-axis title if present
		if (xAxisTitle != null && !xAxisTitle.isEmpty()) {
			height += 20; // Extra space for X-axis title
		}

		// Add space for legend
		if (legendPosition == ChartDiagram.LegendPosition.LEFT || legendPosition == ChartDiagram.LegendPosition.RIGHT) {
			width += legendDim.getWidth() + LEGEND_MARGIN;
		} else if (legendPosition == ChartDiagram.LegendPosition.TOP || legendPosition == ChartDiagram.LegendPosition.BOTTOM) {
			height += legendDim.getHeight() + LEGEND_MARGIN;
		}

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

		// Calculate legend dimensions and adjust layout
		final XDimension2D legendDim = calculateLegendDimension(stringBounder);
		double leftMargin = MARGIN + AXIS_LABEL_SPACE;
		double topMargin = MARGIN + TITLE_SPACE;
		final double plotWidth = getPlotWidth();
		final double plotHeight = getPlotHeight();

		// Adjust margins for legend position
		if (legendPosition == ChartDiagram.LegendPosition.LEFT) {
			leftMargin += legendDim.getWidth() + LEGEND_MARGIN;
		} else if (legendPosition == ChartDiagram.LegendPosition.TOP) {
			topMargin += legendDim.getHeight() + LEGEND_MARGIN;
		}

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

		// Draw legend
		drawLegend(ug, leftMargin, topMargin, plotWidth, plotHeight, lineColor, fontColor, stringBounder);
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

		// Draw axis title vertically
		if (axis.getTitle() != null && !axis.getTitle().isEmpty()) {
			drawVerticalText(ug, axis.getTitle(), height, leftSide, fontColor, stringBounder);
		}
	}

	private void drawVerticalText(UGraphic ug, String text, double height, boolean leftSide, HColor fontColor,
			StringBounder stringBounder) {
		// Parse Creole formatting and extract plain text with font style
		String plainText = text;
		UFont font = UFont.sansSerif(10);

		// Handle Creole formatting markers
		if (text.startsWith("**") && text.endsWith("**") && text.length() > 4) {
			// Bold
			plainText = text.substring(2, text.length() - 2);
			font = font.bold();
		} else if (text.startsWith("//") && text.endsWith("//") && text.length() > 4) {
			// Italic
			plainText = text.substring(2, text.length() - 2);
			font = font.italic();
		} else if (text.startsWith("\"\"") && text.endsWith("\"\"") && text.length() > 4) {
			// Monospaced
			plainText = text.substring(2, text.length() - 2);
			font = UFont.monospaced(10);
		} else if (text.startsWith("__") && text.endsWith("__") && text.length() > 4) {
			// Underlined
			plainText = text.substring(2, text.length() - 2);
			// Underline will be handled by FontConfiguration
		} else if (text.startsWith("--") && text.endsWith("--") && text.length() > 4) {
			// Strike-through
			plainText = text.substring(2, text.length() - 2);
			// Strike will be handled by FontConfiguration
		} else if (text.startsWith("~~") && text.endsWith("~~") && text.length() > 4) {
			// Wave underline
			plainText = text.substring(2, text.length() - 2);
			// Wave will be handled by FontConfiguration
		}

		final FontConfiguration fontConfig = FontConfiguration.create(font, fontColor, fontColor, null);

		// Left axis (Y): 270 degrees (reads from bottom to top) - was reversed
		// Right axis (Y2): 90 degrees (reads from top to bottom) - was reversed
		final int orientation = leftSide ? 270 : 90;
		final net.sourceforge.plantuml.klimt.shape.UText utext = net.sourceforge.plantuml.klimt.shape.UText.build(plainText, fontConfig).withOrientation(orientation);

		// Calculate dimensions of the text
		final double textWidth = stringBounder.calculateDimension(font, plainText).getWidth();
		final double textHeight = stringBounder.calculateDimension(font, plainText).getHeight();

		// Position the rotated text centered vertically along the axis
		// When rotated, we need to position based on where the text baseline starts
		// For 270° rotation (left), text starts at bottom and goes up
		// For 90° rotation (right), text starts at top and goes down
		final double xPos = leftSide ? -AXIS_LABEL_SPACE + textHeight / 2 : AXIS_LABEL_SPACE - textHeight / 2;
		// Center the text vertically - add half the text width since that becomes the vertical span
		final double yPos = leftSide ? (height / 2 + textWidth / 2) : (height / 2 - textWidth / 2);

		ug.apply(UTranslate.dx(xPos).compose(UTranslate.dy(yPos))).draw(utext);
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

		// Draw X-axis title if present
		if (xAxisTitle != null && !xAxisTitle.isEmpty()) {
			final TextBlock titleBlock = Display.getWithNewlines(skinParam.getPragma(), xAxisTitle)
					.create(fontConfig, HorizontalAlignment.CENTER, skinParam);
			final double titleWidth = titleBlock.calculateDimension(stringBounder).getWidth();
			final double titleY = TICK_SIZE + 25; // Position below the labels
			titleBlock.drawU(ug.apply(UTranslate.dx(width / 2 - titleWidth / 2).compose(UTranslate.dy(titleY))));
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

	private XDimension2D calculateLegendDimension(StringBounder stringBounder) {
		if (legendPosition == ChartDiagram.LegendPosition.NONE || series.isEmpty())
			return new XDimension2D(0, 0);

		final UFont font = UFont.sansSerif(10);
		HColor fontColor = HColors.BLACK;
		try {
			fontColor = skinParam.getIHtmlColorSet().getColor("#000000");
		} catch (Exception e) {
			// Use default black
		}
		final FontConfiguration fontConfig = FontConfiguration.create(font, fontColor, fontColor, null);

		double maxWidth = 0;
		double totalHeight = 0;

		for (ChartSeries s : series) {
			final TextBlock textBlock = Display.getWithNewlines(skinParam.getPragma(), s.getName())
					.create(fontConfig, HorizontalAlignment.LEFT, skinParam);
			final XDimension2D dim = textBlock.calculateDimension(stringBounder);

			if (legendPosition == ChartDiagram.LegendPosition.LEFT || legendPosition == ChartDiagram.LegendPosition.RIGHT) {
				maxWidth = Math.max(maxWidth, dim.getWidth());
				totalHeight += dim.getHeight() + LEGEND_ITEM_SPACING;
			} else {
				maxWidth += dim.getWidth() + LEGEND_SYMBOL_SIZE + LEGEND_TEXT_SPACING + LEGEND_ITEM_SPACING;
				totalHeight = Math.max(totalHeight, dim.getHeight());
			}
		}

		if (legendPosition == ChartDiagram.LegendPosition.LEFT || legendPosition == ChartDiagram.LegendPosition.RIGHT) {
			return new XDimension2D(maxWidth + LEGEND_SYMBOL_SIZE + LEGEND_TEXT_SPACING + LEGEND_MARGIN * 2,
					totalHeight);
		} else {
			return new XDimension2D(maxWidth, totalHeight + LEGEND_MARGIN * 2);
		}
	}

	private void drawLegend(UGraphic ug, double leftMargin, double topMargin, double plotWidth, double plotHeight,
			HColor lineColor, HColor fontColor, StringBounder stringBounder) {
		if (legendPosition == ChartDiagram.LegendPosition.NONE || series.isEmpty())
			return;

		final UFont font = UFont.sansSerif(10);
		final FontConfiguration fontConfig = FontConfiguration.create(font, fontColor, fontColor, null);

		double x = 0;
		double y = 0;

		// Calculate starting position based on legend position
		switch (legendPosition) {
		case LEFT:
			x = MARGIN;
			y = topMargin;
			break;
		case RIGHT:
			x = leftMargin + plotWidth + (y2Axis != null ? AXIS_LABEL_SPACE : 0) + LEGEND_MARGIN;
			y = topMargin;
			break;
		case TOP:
			x = leftMargin;
			y = MARGIN;
			break;
		case BOTTOM:
			x = leftMargin;
			y = topMargin + plotHeight + AXIS_LABEL_SPACE + LEGEND_MARGIN;
			break;
		default:
			return;
		}

		double currentX = x;
		double currentY = y;

		for (int i = 0; i < series.size(); i++) {
			final ChartSeries s = series.get(i);
			final HColor color = s.getColor() != null ? s.getColor() : getDefaultColor(i);

			// Draw legend symbol
			if (s.getType() == ChartSeries.SeriesType.BAR) {
				// Draw small rectangle for bar
				final net.sourceforge.plantuml.klimt.shape.URectangle rect = net.sourceforge.plantuml.klimt.shape.URectangle
						.build(LEGEND_SYMBOL_SIZE, LEGEND_SYMBOL_SIZE);
				ug.apply(color).apply(color.bg()).apply(UTranslate.dx(currentX).compose(UTranslate.dy(currentY)))
						.draw(rect);
			} else if (s.getType() == ChartSeries.SeriesType.LINE) {
				// Draw small line for line chart
				final ULine line = ULine.hline(LEGEND_SYMBOL_SIZE);
				ug.apply(color).apply(UStroke.withThickness(2.0))
						.apply(UTranslate.dx(currentX).compose(UTranslate.dy(currentY + LEGEND_SYMBOL_SIZE / 2)))
						.draw(line);
			}

			// Draw series name
			final TextBlock textBlock = Display.getWithNewlines(skinParam.getPragma(), s.getName())
					.create(fontConfig, HorizontalAlignment.LEFT, skinParam);
			final XDimension2D textDim = textBlock.calculateDimension(stringBounder);
			textBlock.drawU(ug.apply(UTranslate.dx(currentX + LEGEND_SYMBOL_SIZE + LEGEND_TEXT_SPACING)
					.compose(UTranslate.dy(currentY))));

			// Move to next item position
			if (legendPosition == ChartDiagram.LegendPosition.LEFT || legendPosition == ChartDiagram.LegendPosition.RIGHT) {
				currentY += textDim.getHeight() + LEGEND_ITEM_SPACING;
			} else {
				currentX += LEGEND_SYMBOL_SIZE + LEGEND_TEXT_SPACING + textDim.getWidth() + LEGEND_ITEM_SPACING;
			}
		}
	}
}
