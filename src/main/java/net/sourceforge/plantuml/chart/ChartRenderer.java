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
import java.util.Map;

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
import net.sourceforge.plantuml.style.MergeStrategy;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class ChartRenderer {

	private final ISkinParam skinParam;
	private final List<String> xAxisLabels;
	private final String xAxisTitle;
	private final Integer xAxisTickSpacing;
	private final ChartAxis.LabelPosition xAxisLabelPosition;
	private final List<ChartSeries> series;
	private final ChartAxis xAxis;
	private final ChartAxis yAxis;
	private final ChartAxis y2Axis;
	private final ChartDiagram.LegendPosition legendPosition;
	private final ChartDiagram.GridMode xGridMode;
	private final ChartDiagram.GridMode yGridMode;
	private final ChartDiagram.StackMode stackMode;
	private final ChartDiagram.Orientation orientation;
	private final List<ChartAnnotation> annotations;

	// Layout constants
	private static final double MARGIN = 20;
	private static final double AXIS_LABEL_SPACE = 40;
	private static final double TITLE_SPACE = 30;
	private static final double TICK_SIZE = 5;
	private static final double LEGEND_MARGIN = 10;
	private static final double LEGEND_SYMBOL_SIZE = 12;
	private static final double LEGEND_TEXT_SPACING = 5;
	private static final double LEGEND_ITEM_SPACING = 15;

	public ChartRenderer(ISkinParam skinParam, List<String> xAxisLabels, String xAxisTitle, Integer xAxisTickSpacing,
			ChartAxis.LabelPosition xAxisLabelPosition, List<ChartSeries> series, ChartAxis xAxis, ChartAxis yAxis, ChartAxis y2Axis,
			ChartDiagram.LegendPosition legendPosition, ChartDiagram.GridMode xGridMode, ChartDiagram.GridMode yGridMode,
			ChartDiagram.StackMode stackMode, ChartDiagram.Orientation orientation, List<ChartAnnotation> annotations) {
		this.skinParam = skinParam;
		this.orientation = orientation;

		// For horizontal orientation: swap x and y axis interpretation
		// User writes: x-axis "Revenue" 0 --> 100, y-axis [A, B, C]
		// We need: yAxis = numeric (for horizontal bars), xAxisLabels = categories
		// So we interpret user's "x-axis" range as our Y-axis, and "y-axis" labels as our X-axis
		if (orientation == ChartDiagram.Orientation.HORIZONTAL) {
			// Create Y-axis from what user specified as X-axis (numeric range)
			// Note: User's x-axis data comes in as xAxisLabels (categories), but for horizontal
			// they would specify it as numeric range which goes to... wait, that won't work.
			// The user can't specify a numeric range for X-axis, only labels!

			// Actually, keep it simple: just swap the data we received
			this.xAxisLabels = xAxisLabels;
			this.xAxisTitle = xAxisTitle;
			this.xAxisTickSpacing = xAxisTickSpacing;
			this.xAxisLabelPosition = xAxisLabelPosition;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.y2Axis = y2Axis;
			this.xGridMode = xGridMode;
			this.yGridMode = yGridMode;
		} else {
			this.xAxisLabels = xAxisLabels;
			this.xAxisTitle = xAxisTitle;
			this.xAxisTickSpacing = xAxisTickSpacing;
			this.xAxisLabelPosition = xAxisLabelPosition;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.y2Axis = y2Axis;
			this.xGridMode = xGridMode;
			this.yGridMode = yGridMode;
		}

		this.series = series;
		this.legendPosition = legendPosition;
		this.stackMode = stackMode;
		this.annotations = annotations;
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

		// Calculate X-axis position (align with zero if y-axis includes zero)
		double xAxisY = topMargin + plotHeight;
		if (orientation != ChartDiagram.Orientation.HORIZONTAL && yAxis != null) {
			// Check if Y-axis range includes zero
			if (yAxis.getMin() <= 0 && yAxis.getMax() >= 0) {
				// Calculate Y position of zero
				final double zeroRatio = (0 - yAxis.getMin()) / (yAxis.getMax() - yAxis.getMin());
				xAxisY = topMargin + plotHeight * (1.0 - zeroRatio);
			}
		}

		// Calculate Y-axis position (align with zero if x-axis includes zero)
		double yAxisX = leftMargin;
		if (orientation != ChartDiagram.Orientation.HORIZONTAL && xAxis != null) {
			// Check if X-axis range includes zero
			if (xAxis.getMin() <= 0 && xAxis.getMax() >= 0) {
				// Calculate X position of zero
				final double zeroX = xAxis.valueToPixel(0, 0, plotWidth);
				yAxisX = leftMargin + zeroX;
			}
		}

		// Draw axes based on orientation
		if (orientation == ChartDiagram.Orientation.HORIZONTAL) {
			// For horizontal bars: categories on left (vertical), numeric on bottom (horizontal)
			// xAxisLabels = categories (draw vertically on left)
			// yAxis = numeric (draw horizontally at bottom)
			drawXAxis(ug.apply(UTranslate.dx(leftMargin).compose(UTranslate.dy(topMargin))), plotHeight,
					plotHeight, lineColor, fontColor, stringBounder);
			drawYAxisHorizontally(ug.apply(UTranslate.dx(leftMargin).compose(UTranslate.dy(topMargin + plotHeight))), plotWidth,
					plotHeight, yAxis, lineColor, fontColor, stringBounder);
		} else {
			// For vertical bars: categories on bottom (horizontal), numeric on left (vertical)
			// xAxisLabels = categories (draw horizontally at bottom)
			// yAxis = numeric (draw vertically on left)
			drawYAxis(ug.apply(UTranslate.dx(yAxisX).compose(UTranslate.dy(topMargin))), plotHeight, plotWidth, yAxis, true,
					lineColor, fontColor, stringBounder);

			if (y2Axis != null) {
				drawYAxis(ug.apply(UTranslate.dx(leftMargin + plotWidth).compose(UTranslate.dy(topMargin))), plotHeight, plotWidth,
						y2Axis, false, lineColor, fontColor, stringBounder);
			}

			drawXAxis(ug.apply(UTranslate.dx(leftMargin).compose(UTranslate.dy(xAxisY))), plotWidth,
					plotHeight, lineColor, fontColor, stringBounder);
		}

		// Draw grid lines (before series data so series draws on top)
		final UGraphic ugPlot = ug.apply(UTranslate.dx(leftMargin).compose(UTranslate.dy(topMargin)));
		drawGridLines(ugPlot, plotWidth, plotHeight, lineColor, fontColor, stringBounder);

		// Draw series data
		drawSeries(ugPlot, plotWidth, plotHeight, stringBounder);

		// Draw annotations
		drawAnnotations(ugPlot, plotWidth, plotHeight, lineColor, fontColor, stringBounder);

		// Draw legend
		drawLegend(ug, leftMargin, topMargin, plotWidth, plotHeight, lineColor, fontColor, stringBounder);
	}

	private void drawYAxis(UGraphic ug, double height, double width, ChartAxis axis, boolean leftSide, HColor lineColor,
			HColor fontColor, StringBounder stringBounder) {
		// Get axis-specific style
		final Style axisStyle = getAxisStyleSignature(false)
			.getMergedStyle(skinParam.getCurrentStyleBuilder());

		// Extract styled properties
		final HColor styledLineColor = axisStyle.value(PName.LineColor)
			.asColor(skinParam.getIHtmlColorSet());
		final double lineThickness = axisStyle.value(PName.LineThickness)
			.asDouble();
		final FontConfiguration fontConfig = axisStyle.getFontConfiguration(skinParam.getIHtmlColorSet());

		// Use styled properties for axis line
		final HColor actualLineColor = styledLineColor != null ? styledLineColor : lineColor;
		ug = ug.apply(actualLineColor).apply(UStroke.withThickness(lineThickness));

		// Draw axis line
		ug.draw(ULine.vline(height));

		// Get grid style
		final Style gridStyle = getGridStyleSignature()
			.getMergedStyle(skinParam.getCurrentStyleBuilder());

		// Extract grid properties
		HColor gridColor = gridStyle.value(PName.LineColor)
			.asColor(skinParam.getIHtmlColorSet());
		if (gridColor == null) {
			// Fallback to default
			try {
				gridColor = skinParam.getIHtmlColorSet().getColor("#D0D0D0");
			} catch (Exception e) {
				gridColor = lineColor;
			}
		}
		final double gridThickness = gridStyle.value(PName.LineThickness).asDouble();
		final UStroke gridStroke = UStroke.withThickness(gridThickness);

		// Use custom ticks if defined, otherwise use automatic ticks
		if (axis.hasCustomTicks()) {
			// Draw custom ticks
			for (Map.Entry<Double, String> entry : axis.getCustomTicks().entrySet()) {
				final double value = entry.getKey();
				final String label = entry.getValue();

				// Calculate y position based on value
				final double y = height * (1.0 - (value - axis.getMin()) / (axis.getMax() - axis.getMin()));

				// Skip if outside axis range
				if (y < 0 || y > height)
					continue;

				// Draw grid lines if enabled (horizontal lines for Y axis)
				// Skip if coordinate-pair mode (grid drawn separately in drawGridLines)
				if (leftSide && yGridMode != ChartDiagram.GridMode.OFF && xAxis == null) {
					final ULine gridLine = ULine.hline(width);
					ug.apply(gridColor).apply(gridStroke).apply(UTranslate.dy(y)).draw(gridLine);
				}

				// Draw tick
				if (leftSide)
					ug.apply(UTranslate.dy(y)).draw(ULine.hline(-TICK_SIZE));
				else
					ug.apply(UTranslate.dy(y)).draw(ULine.hline(TICK_SIZE));

				// Draw custom label
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
		} else if (axis.hasTickSpacing()) {
			// Draw ticks with custom spacing
			final double spacing = axis.getTickSpacing();
			final double range = axis.getMax() - axis.getMin();

			// Calculate starting value (round up to nearest spacing interval)
			double startValue = Math.ceil(axis.getMin() / spacing) * spacing;

			for (double value = startValue; value <= axis.getMax(); value += spacing) {
				// Avoid floating point precision issues
				if (value > axis.getMax() + spacing * 0.01)
					break;

				final double y = height * (1.0 - (value - axis.getMin()) / range);

				// Draw grid lines if enabled (horizontal lines for Y axis)
				// Skip if coordinate-pair mode (grid drawn separately in drawGridLines)
				if (leftSide && yGridMode != ChartDiagram.GridMode.OFF && xAxis == null) {
					final ULine gridLine = ULine.hline(width);
					ug.apply(gridColor).apply(gridStroke).apply(UTranslate.dy(y)).draw(gridLine);
				}

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
		} else {
			// Draw automatic ticks
			final int numTicks = 5;
			for (int i = 0; i <= numTicks; i++) {
				final double y = height * (1.0 - (double) i / numTicks);
				final double value = axis.getMin() + (axis.getMax() - axis.getMin()) * i / numTicks;

				// Draw grid lines if enabled (horizontal lines for Y axis)
				// Skip if coordinate-pair mode (grid drawn separately in drawGridLines)
				if (leftSide && yGridMode != ChartDiagram.GridMode.OFF && xAxis == null) {
					final ULine gridLine = ULine.hline(width);
					ug.apply(gridColor).apply(gridStroke).apply(UTranslate.dy(y)).draw(gridLine);
				}

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
		}

		// Draw axis title
		if (axis.getTitle() != null && !axis.getTitle().isEmpty()) {
			if (axis.getLabelPosition() == ChartAxis.LabelPosition.TOP) {
				// Draw horizontally at top
				drawHorizontalAxisTitle(ug, axis.getTitle(), height, leftSide, fontColor, stringBounder, true, fontConfig);
			} else {
				// Draw vertically (default)
				drawVerticalText(ug, axis.getTitle(), height, leftSide, fontColor, stringBounder, fontConfig);
			}
		}
	}

	private void drawVerticalText(UGraphic ug, String text, double height, boolean leftSide, HColor fontColor,
			StringBounder stringBounder, FontConfiguration axisFontConfig) {
		// Use the provided font configuration (same as tick labels)
		// Left axis (Y): 90 degrees (reads from bottom to top)
		// Right axis (Y2): 270 degrees (reads from top to bottom)
		final int orientation = leftSide ? 90 : 270;
		final net.sourceforge.plantuml.klimt.shape.UText utext = net.sourceforge.plantuml.klimt.shape.UText.build(text, axisFontConfig).withOrientation(orientation);

		// Calculate dimensions of the text using the axis font
		final UFont font = axisFontConfig.getFont();
		final double textWidth = stringBounder.calculateDimension(font, text).getWidth();
		final double textHeight = stringBounder.calculateDimension(font, text).getHeight();

		// Position the rotated text centered vertically along the axis
		// When rotated 90°, the baseline is the rotation point
		// The text width becomes the vertical span after rotation
		// To center: baseline should be at (height/2 + textWidth/2) for 90° or (height/2 - textWidth/2) for 270°
		// Add extra spacing (10 pixels) to move labels further from the axis
		final double extraSpacing = 10;
		final double xPos = leftSide ? -AXIS_LABEL_SPACE + textHeight / 2 - extraSpacing : AXIS_LABEL_SPACE - textHeight / 2 + extraSpacing;
		// For 90° (left): baseline at top of text, so position at center + half width to center the text
		// For 270° (right): baseline at bottom of text, so position at center - half width to center the text
		final double yPos = leftSide ? (height / 2 + textWidth / 2) : (height / 2 - textWidth / 2);

		ug.apply(UTranslate.dx(xPos).compose(UTranslate.dy(yPos))).draw(utext);
	}

	private void drawHorizontalAxisTitle(UGraphic ug, String text, double height, boolean leftSide, HColor fontColor,
			StringBounder stringBounder, boolean isVerticalAxis, FontConfiguration axisFontConfig) {
		// Use the provided font configuration (same as tick labels)
		final TextBlock textBlock = Display.getWithNewlines(skinParam.getPragma(), text)
				.create(axisFontConfig, HorizontalAlignment.CENTER, skinParam);
		final double textWidth = textBlock.calculateDimension(stringBounder).getWidth();
		final double textHeight = textBlock.calculateDimension(stringBounder).getHeight();

		if (isVerticalAxis) {
			// For vertical axis with label at top
			final double xPos = leftSide ? -AXIS_LABEL_SPACE / 2 - textWidth / 2 : AXIS_LABEL_SPACE / 2 - textWidth / 2;
			final double yPos = -textHeight - 15; // Position higher above the axis
			textBlock.drawU(ug.apply(UTranslate.dx(xPos).compose(UTranslate.dy(yPos))));
		} else {
			// For horizontal axis with label at right
			final double xPos = height + 10; // Position to the right
			final double yPos = -textHeight / 2;
			textBlock.drawU(ug.apply(UTranslate.dx(xPos).compose(UTranslate.dy(yPos))));
		}
	}

	private void drawXAxis(UGraphic ug, double width, double height, HColor lineColor, HColor fontColor,
			StringBounder stringBounder) {
		// For horizontal orientation, draw category labels vertically on the left
		if (orientation == ChartDiagram.Orientation.HORIZONTAL) {
			drawCategoriesVerticallyOnLeft(ug, height, lineColor, fontColor, stringBounder);
			return;
		}

		// Get axis-specific style
		final Style axisStyle = getAxisStyleSignature(true)
			.getMergedStyle(skinParam.getCurrentStyleBuilder());

		// Extract styled properties
		final HColor styledLineColor = axisStyle.value(PName.LineColor)
			.asColor(skinParam.getIHtmlColorSet());
		final double lineThickness = axisStyle.value(PName.LineThickness)
			.asDouble();
		final FontConfiguration fontConfig = axisStyle.getFontConfiguration(skinParam.getIHtmlColorSet());

		// Use styled properties for axis line
		final HColor actualLineColor = styledLineColor != null ? styledLineColor : lineColor;
		ug = ug.apply(actualLineColor).apply(UStroke.withThickness(lineThickness));

		// Draw axis line
		ug.draw(ULine.hline(width));

		// Draw labels (only if we have categorical x-axis labels)
		if (!xAxisLabels.isEmpty()) {
			final double categoryWidth = width / xAxisLabels.size();

		// Get grid style
		final Style gridStyle = getGridStyleSignature()
			.getMergedStyle(skinParam.getCurrentStyleBuilder());

		// Extract grid properties
		HColor gridColor = gridStyle.value(PName.LineColor)
			.asColor(skinParam.getIHtmlColorSet());
		if (gridColor == null) {
			// Fallback to default
			try {
				gridColor = skinParam.getIHtmlColorSet().getColor("#D0D0D0");
			} catch (Exception e) {
				gridColor = lineColor;
			}
		}
		final double gridThickness = gridStyle.value(PName.LineThickness).asDouble();
		final UStroke gridStroke = UStroke.withThickness(gridThickness);

		// Determine which labels to show based on spacing
		final int spacing = (xAxisTickSpacing != null && xAxisTickSpacing > 0) ? xAxisTickSpacing : 1;

		// Draw vertical grid lines at category centers spanning the full plot height
		// Grid lines should span from top of plot to bottom of plot
		// The height parameter represents the full plot height
		if (xGridMode != ChartDiagram.GridMode.OFF) {
			// Calculate where the x-axis is positioned within the plot
			// When axis is at bottom (all positive values), draw upward by full height
			// When axis crosses zero, draw both up (to top) and down (to bottom)
			final double distanceToTop;
			final double distanceToBottom;

			if (yAxis != null && yAxis.getMin() <= 0 && yAxis.getMax() >= 0) {
				// Axis crosses zero - calculate distances to top and bottom
				final double zeroRatio = (0 - yAxis.getMin()) / (yAxis.getMax() - yAxis.getMin());
				distanceToTop = height * (1.0 - zeroRatio);
				distanceToBottom = height * zeroRatio;
			} else {
				// Axis is at bottom (all positive) or top (all negative)
				distanceToTop = height;
				distanceToBottom = 0;
			}

			// Draw grid lines at category centers (where data points are positioned)
			for (int i = 0; i < xAxisLabels.size(); i++) {
				final double gridX = (i + 0.5) * categoryWidth;
				// Draw line upward to top of plot
				if (distanceToTop > 0) {
					final ULine gridLineUp = ULine.vline(-distanceToTop);
					ug.apply(gridColor).apply(gridStroke).apply(UTranslate.dx(gridX)).draw(gridLineUp);
				}
				// Draw line downward to bottom of plot
				if (distanceToBottom > 0) {
					final ULine gridLineDown = ULine.vline(distanceToBottom);
					ug.apply(gridColor).apply(gridStroke).apply(UTranslate.dx(gridX)).draw(gridLineDown);
				}
			}
		}

		// Draw ticks and labels at category centers
		for (int i = 0; i < xAxisLabels.size(); i++) {
			final double labelX = (i + 0.5) * categoryWidth; // Center position for labels

			// Only draw tick and label every N positions based on spacing
			if (i % spacing == 0) {
				// Draw tick at label position
				ug.apply(UTranslate.dx(labelX)).draw(ULine.vline(TICK_SIZE));

				// Draw label
				final TextBlock textBlock = Display.getWithNewlines(skinParam.getPragma(), xAxisLabels.get(i))
						.create(fontConfig, HorizontalAlignment.CENTER, skinParam);
				final double textWidth = textBlock.calculateDimension(stringBounder).getWidth();
				textBlock.drawU(ug.apply(UTranslate.dx(labelX - textWidth / 2).compose(UTranslate.dy(TICK_SIZE + 5))));
			}
		}

		// Draw X-axis title if present
		if (xAxisTitle != null && !xAxisTitle.isEmpty()) {
			if (xAxisLabelPosition == ChartAxis.LabelPosition.RIGHT) {
				// Draw at the right end of the axis
				final TextBlock titleBlock = Display.getWithNewlines(skinParam.getPragma(), xAxisTitle)
						.create(fontConfig, HorizontalAlignment.LEFT, skinParam);
				final double textHeight = titleBlock.calculateDimension(stringBounder).getHeight();
				titleBlock.drawU(ug.apply(UTranslate.dx(width + 10).compose(UTranslate.dy(-textHeight / 2))));
			} else {
				// Draw centered below the axis (default)
				final TextBlock titleBlock = Display.getWithNewlines(skinParam.getPragma(), xAxisTitle)
						.create(fontConfig, HorizontalAlignment.CENTER, skinParam);
				final double titleWidth = titleBlock.calculateDimension(stringBounder).getWidth();
				final double titleY = TICK_SIZE + 25; // Position below the labels
				titleBlock.drawU(ug.apply(UTranslate.dx(width / 2 - titleWidth / 2).compose(UTranslate.dy(titleY))));
			}
		}
		} else if (xAxis != null) {
			// Draw numeric x-axis ticks for coordinate-pair mode
			final double range = xAxis.getMax() - xAxis.getMin();
			final double tickInterval;

			if (xAxisTickSpacing != null && xAxisTickSpacing > 0) {
				// Spacing directly specifies the tick interval
				tickInterval = xAxisTickSpacing;
			} else {
				// Default: approximately 10 ticks
				tickInterval = range / 10.0;
			}

			// Find the starting tick value (round down to nearest multiple of tickInterval)
			final double startValue = Math.floor(xAxis.getMin() / tickInterval) * tickInterval;

			// Draw ticks and labels from start to end
			for (double value = startValue; value <= xAxis.getMax() + tickInterval * 0.01; value += tickInterval) {
				// Skip if outside axis range
				if (value < xAxis.getMin() - tickInterval * 0.01 || value > xAxis.getMax() + tickInterval * 0.01)
					continue;

				final double x = xAxis.valueToPixel(value, 0, width);

				// Draw tick mark
				ug.apply(UTranslate.dx(x)).draw(ULine.vline(TICK_SIZE));

				// Draw label
				final String label = formatAxisValue(value);
				final TextBlock textBlock = Display.getWithNewlines(skinParam.getPragma(), label)
						.create(fontConfig, HorizontalAlignment.CENTER, skinParam);
				final double textWidth = textBlock.calculateDimension(stringBounder).getWidth();
				textBlock.drawU(ug.apply(UTranslate.dx(x - textWidth / 2).compose(UTranslate.dy(TICK_SIZE + 5))));
			}

			// Draw X-axis title if present
			if (xAxisTitle != null && !xAxisTitle.isEmpty()) {
				if (xAxisLabelPosition == ChartAxis.LabelPosition.RIGHT) {
					// Draw at the right end of the axis
					final TextBlock titleBlock = Display.getWithNewlines(skinParam.getPragma(), xAxisTitle)
							.create(fontConfig, HorizontalAlignment.LEFT, skinParam);
					final double textHeight = titleBlock.calculateDimension(stringBounder).getHeight();
					titleBlock.drawU(ug.apply(UTranslate.dx(width + 10).compose(UTranslate.dy(-textHeight / 2))));
				} else {
					// Draw centered below the axis (default)
					final TextBlock titleBlock = Display.getWithNewlines(skinParam.getPragma(), xAxisTitle)
							.create(fontConfig, HorizontalAlignment.CENTER, skinParam);
					final double titleWidth = titleBlock.calculateDimension(stringBounder).getWidth();
					final double titleY = TICK_SIZE + 25; // Position below the labels
					titleBlock.drawU(ug.apply(UTranslate.dx(width / 2 - titleWidth / 2).compose(UTranslate.dy(titleY))));
				}
			}
		} // Close the if (!xAxisLabels.isEmpty()) / else if
	}

	private void drawCategoriesVerticallyOnLeft(UGraphic ug, double height, HColor lineColor, HColor fontColor,
			StringBounder stringBounder) {
		// Draw vertical axis line on the left
		ug.draw(ULine.vline(height));

		if (xAxisLabels.isEmpty())
			return;

		final UFont font = UFont.sansSerif(10);
		final FontConfiguration fontConfig = FontConfiguration.create(font, fontColor, fontColor, null);
		final double categoryHeight = height / xAxisLabels.size();

		for (int i = 0; i < xAxisLabels.size(); i++) {
			final double y = (i + 0.5) * categoryHeight;

			// Draw tick mark pointing left
			ug.apply(UTranslate.dx(-TICK_SIZE).compose(UTranslate.dy(y))).draw(ULine.hline(TICK_SIZE));

			// Draw label to the left of the tick
			final TextBlock textBlock = Display.getWithNewlines(skinParam.getPragma(), xAxisLabels.get(i))
					.create(fontConfig, HorizontalAlignment.RIGHT, skinParam);
			final double textHeight = textBlock.calculateDimension(stringBounder).getHeight();
			final double textWidth = textBlock.calculateDimension(stringBounder).getWidth();
			textBlock.drawU(ug.apply(UTranslate.dx(-TICK_SIZE - textWidth - 5).compose(UTranslate.dy(y - textHeight / 2))));
		}
	}

	private void drawYAxisHorizontally(UGraphic ug, double width, double height, ChartAxis axis, HColor lineColor,
			HColor fontColor, StringBounder stringBounder) {
		// Draw horizontal axis line
		ug.draw(ULine.hline(width));

		// Draw ticks and labels
		final UFont font = UFont.sansSerif(10);
		final FontConfiguration fontConfig = FontConfiguration.create(font, fontColor, fontColor, null);

		// Calculate grid line color (lighter than axis color)
		HColor gridColor = lineColor;
		try {
			gridColor = skinParam.getIHtmlColorSet().getColor("#D0D0D0");
		} catch (Exception e) {
			// Use default line color
		}

		// Use automatic ticks (similar to vertical Y-axis logic)
		final double range = axis.getMax() - axis.getMin();
		final int NUM_TICKS = 5;

		for (int i = 0; i <= NUM_TICKS; i++) {
			final double value = axis.getMin() + (i * range / NUM_TICKS);
			final double x = width * i / NUM_TICKS;

			// Draw tick mark pointing down
			ug.apply(UTranslate.dx(x)).draw(ULine.vline(TICK_SIZE));

			// Draw label below the tick
			final String label = String.format("%.0f", value);
			final TextBlock textBlock = Display.getWithNewlines(skinParam.getPragma(), label)
					.create(fontConfig, HorizontalAlignment.CENTER, skinParam);
			final double textWidth = textBlock.calculateDimension(stringBounder).getWidth();
			textBlock.drawU(ug.apply(UTranslate.dx(x - textWidth / 2).compose(UTranslate.dy(TICK_SIZE + 5))));
		}

		// Draw axis title if present
		if (axis.getTitle() != null && !axis.getTitle().isEmpty()) {
			final TextBlock titleBlock = Display.getWithNewlines(skinParam.getPragma(), axis.getTitle())
					.create(fontConfig, HorizontalAlignment.CENTER, skinParam);
			final double titleWidth = titleBlock.calculateDimension(stringBounder).getWidth();
			final double titleY = TICK_SIZE + 25;
			titleBlock.drawU(ug.apply(UTranslate.dx(width / 2 - titleWidth / 2).compose(UTranslate.dy(titleY))));
		}
	}

	private void drawGridLines(UGraphic ug, double plotWidth, double plotHeight, HColor lineColor, HColor fontColor,
			StringBounder stringBounder) {
		// Draw grid lines for coordinate-pair mode (numeric axes)
		// UGraphic ug is at plot origin (leftMargin, topMargin)

		if (xAxis == null || yAxis == null)
			return; // Only draw grids for coordinate-pair mode

		// Get grid style
		final Style gridStyle = getGridStyleSignature()
			.getMergedStyle(skinParam.getCurrentStyleBuilder());

		// Extract grid properties
		HColor gridColor = gridStyle.value(PName.LineColor)
			.asColor(skinParam.getIHtmlColorSet());
		if (gridColor == null) {
			try {
				gridColor = skinParam.getIHtmlColorSet().getColor("#D0D0D0");
			} catch (Exception e) {
				gridColor = lineColor;
			}
		}
		final double gridThickness = gridStyle.value(PName.LineThickness).asDouble();
		final UStroke gridStroke = UStroke.withThickness(gridThickness);

		// Draw vertical grid lines (h-axis)
		if (xGridMode != ChartDiagram.GridMode.OFF && xAxis != null) {
			final double range = xAxis.getMax() - xAxis.getMin();
			final double tickInterval;

			if (xAxisTickSpacing != null && xAxisTickSpacing > 0) {
				tickInterval = xAxisTickSpacing;
			} else {
				tickInterval = range / 10.0;
			}

			final double startValue = Math.floor(xAxis.getMin() / tickInterval) * tickInterval;

			for (double value = startValue; value <= xAxis.getMax() + tickInterval * 0.01; value += tickInterval) {
				if (value < xAxis.getMin() - tickInterval * 0.01 || value > xAxis.getMax() + tickInterval * 0.01)
					continue;

				final double x = xAxis.valueToPixel(value, 0, plotWidth);
				final ULine gridLine = ULine.vline(plotHeight);
				ug.apply(gridColor).apply(gridStroke).apply(UTranslate.dx(x)).draw(gridLine);
			}
		}

		// Draw horizontal grid lines (v-axis)
		if (yGridMode != ChartDiagram.GridMode.OFF && yAxis != null) {
			if (yAxis.hasTickSpacing()) {
				final double spacing = yAxis.getTickSpacing();
				final double startValue = Math.ceil(yAxis.getMin() / spacing) * spacing;

				for (double value = startValue; value <= yAxis.getMax(); value += spacing) {
					if (value > yAxis.getMax() + spacing * 0.01)
						break;

					final double y = plotHeight * (1.0 - (value - yAxis.getMin()) / (yAxis.getMax() - yAxis.getMin()));
					final ULine gridLine = ULine.hline(plotWidth);
					ug.apply(gridColor).apply(gridStroke).apply(UTranslate.dy(y)).draw(gridLine);
				}
			}
		}
	}

	private void drawSeries(UGraphic ug, double plotWidth, double plotHeight, StringBounder stringBounder) {
		// Check if we have series to render
		boolean hasCoordinatePairs = !series.isEmpty() && series.get(0).hasExplicitXValues();
		if ((xAxisLabels.isEmpty() && !hasCoordinatePairs) || series.isEmpty())
			return;

		// Separate bar series from other series for grouped/stacked rendering
		final java.util.List<ChartSeries> barSeries = new java.util.ArrayList<>();
		final java.util.List<HColor> barColors = new java.util.ArrayList<>();

		for (ChartSeries s : series) {
			if (s.getType() == ChartSeries.SeriesType.BAR) {
				barSeries.add(s);

				// Get bar style with stereotype support
				final Style barStyle = getBarStyle(s);

				// Extract bar color - priority: explicit color > stereotype style > element style > default color
				HColor color = s.getColor();
				if (color == null) {
					// Try to get color from style (with stereotype if present)
					color = barStyle.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
					if (color == null) {
						// Use default color
						color = getDefaultColor(series.indexOf(s));
					}
				}
				barColors.add(color);
			}
		}

		// Render bar series (grouped or stacked based on stackMode)
		if (!barSeries.isEmpty()) {
			// For now, assume all bar series use the same axis (primary Y axis)
			// Future enhancement could support mixed axes in grouped/stacked mode
			final ChartAxis axis = barSeries.get(0).isUseSecondaryAxis() && y2Axis != null ? y2Axis : yAxis;
			final boolean isHorizontal = (orientation == ChartDiagram.Orientation.HORIZONTAL);
			final BarRenderer barRenderer = new BarRenderer(skinParam, plotWidth, plotHeight, xAxisLabels.size(), axis, isHorizontal);

			if (barSeries.size() == 1) {
				// Single bar series - use simple rendering
				barRenderer.draw(ug, barSeries.get(0), barColors.get(0));
			} else {
				// Multiple bar series - use grouped or stacked rendering
				if (stackMode == ChartDiagram.StackMode.STACKED) {
					barRenderer.drawStacked(ug, barSeries, barColors);
				} else {
					barRenderer.drawGrouped(ug, barSeries, barColors);
				}
			}
		}

		// Separate area series for stacked rendering
		final java.util.List<ChartSeries> areaSeries = new java.util.ArrayList<>();
		final java.util.List<HColor> areaColors = new java.util.ArrayList<>();

		// Render non-bar, non-area series (line, scatter) first
		for (ChartSeries s : series) {
			if (s.getType() == ChartSeries.SeriesType.AREA) {
				areaSeries.add(s);

				// Get area style with stereotype support
				final Style areaStyle = getAreaStyle(s);

				// Extract series color - priority: explicit color > style color > default color
				HColor color = s.getColor();
				if (color == null) {
					// Try to get color from style
					color = areaStyle.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
					if (color == null) {
						color = areaStyle.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
					}
					if (color == null) {
						// Use default color
						color = getDefaultColor(series.indexOf(s));
					}
				}
				areaColors.add(color);
			} else if (s.getType() != ChartSeries.SeriesType.BAR) {
				final ChartAxis axis = s.isUseSecondaryAxis() && y2Axis != null ? y2Axis : yAxis;

				// Get series-specific style based on type (with stereotype support)
				Style seriesStyle;
				switch (s.getType()) {
					case LINE:
						seriesStyle = getLineStyle(s);
						break;
					case SCATTER:
						seriesStyle = getScatterStyle(s);
						break;
					default:
						seriesStyle = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
				}

				// Extract series color - priority: explicit color > style color > default color
				HColor color = s.getColor();
				if (color == null) {
					// Try to get color from style
					color = seriesStyle.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
					if (color == null) {
						// Use default color
						color = getDefaultColor(series.indexOf(s));
					}
				}

				if (s.getType() == ChartSeries.SeriesType.LINE) {
					final LineRenderer lineRenderer = new LineRenderer(skinParam, plotWidth, plotHeight,
							xAxisLabels.size(), axis, xAxis);
					lineRenderer.draw(ug, s, color);
				} else if (s.getType() == ChartSeries.SeriesType.SCATTER) {
					final ScatterRenderer scatterRenderer = new ScatterRenderer(skinParam, plotWidth, plotHeight,
							xAxisLabels.size(), axis, xAxis);
					scatterRenderer.draw(ug, s, color);
				}
			}
		}

		// Render area series with stacking
		if (!areaSeries.isEmpty()) {
			// For now, assume all area series use the same axis (primary Y axis)
			final ChartAxis axis = areaSeries.get(0).isUseSecondaryAxis() && y2Axis != null ? y2Axis : yAxis;
			final AreaRenderer areaRenderer = new AreaRenderer(skinParam, plotWidth, plotHeight, xAxisLabels.size(), axis);

			// Track cumulative values for stacking
			java.util.List<Double> cumulativeValues = null;

			for (int i = 0; i < areaSeries.size(); i++) {
				final ChartSeries areaSer = areaSeries.get(i);
				final HColor color = areaColors.get(i);

				// Draw this area with the baseline from previous areas
				areaRenderer.draw(ug, areaSer, color, cumulativeValues);

				// Update cumulative values for next series
				if (cumulativeValues == null) {
					cumulativeValues = new java.util.ArrayList<>(areaSer.getValues());
				} else {
					// Add current series values to cumulative
					for (int j = 0; j < Math.min(areaSer.getValues().size(), cumulativeValues.size()); j++) {
						cumulativeValues.set(j, cumulativeValues.get(j) + areaSer.getValues().get(j));
					}
					// If current series has more values than cumulative, add them
					for (int j = cumulativeValues.size(); j < areaSer.getValues().size(); j++) {
						cumulativeValues.add(areaSer.getValues().get(j));
					}
				}
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

	private StyleSignatureBasic getBarStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chartDiagram, SName.bar);
	}

	private Style getBarStyle(ChartSeries series) {
		StyleSignatureBasic signature = getBarStyleSignature();
		if (series.getStereotype() != null) {
			// Use withTOBECHANGED for element-level stereotype styling
			Style style = signature.withTOBECHANGED(series.getStereotype())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());

			// Use forStereotypeItself for CSS class selector styling (e.g., .primary)
			// This matches the pattern used by sequence diagrams
			Style stereoStyle = signature.forStereotypeItself(series.getStereotype())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());

			// Merge with stereo style overwriting existing values
			if (style != null)
				stereoStyle = style.mergeWith(stereoStyle, MergeStrategy.OVERWRITE_EXISTING_VALUE);

			return stereoStyle;
		}
		return signature.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private StyleSignatureBasic getLineStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chartDiagram, SName.line);
	}

	private Style getLineStyle(ChartSeries series) {
		StyleSignatureBasic signature = getLineStyleSignature();
		if (series.getStereotype() != null) {
			// Use withTOBECHANGED for element-level stereotype styling
			Style style = signature.withTOBECHANGED(series.getStereotype())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());

			// Use forStereotypeItself for CSS class selector styling (e.g., line.target)
			Style stereoStyle = signature.forStereotypeItself(series.getStereotype())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());

			// Merge with stereo style overwriting existing values
			if (style != null)
				stereoStyle = style.mergeWith(stereoStyle, MergeStrategy.OVERWRITE_EXISTING_VALUE);

			return stereoStyle;
		}
		return signature.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private StyleSignatureBasic getAreaStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chartDiagram, SName.area);
	}

	private Style getAreaStyle(ChartSeries series) {
		StyleSignatureBasic signature = getAreaStyleSignature();
		if (series.getStereotype() != null) {
			// Use withTOBECHANGED for element-level stereotype styling
			Style style = signature.withTOBECHANGED(series.getStereotype())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());

			// Use forStereotypeItself for CSS class selector styling (e.g., area.highlight)
			Style stereoStyle = signature.forStereotypeItself(series.getStereotype())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());

			// Merge with stereo style overwriting existing values
			if (style != null)
				stereoStyle = style.mergeWith(stereoStyle, MergeStrategy.OVERWRITE_EXISTING_VALUE);

			return stereoStyle;
		}
		return signature.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private String formatAxisValue(double value) {
		// Format axis tick labels
		if (Math.abs(value) < 0.01 && value != 0)
			return String.format("%.2e", value);
		if (value == (long) value)
			return String.format("%d", (long) value);
		return String.format("%.1f", value);
	}

	private StyleSignatureBasic getScatterStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chartDiagram, SName.scatter);
	}

	private Style getScatterStyle(ChartSeries series) {
		StyleSignatureBasic signature = getScatterStyleSignature();
		if (series.getStereotype() != null) {
			// Use withTOBECHANGED for element-level stereotype styling
			Style style = signature.withTOBECHANGED(series.getStereotype())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());

			// Use forStereotypeItself for CSS class selector styling (e.g., scatter.highlight)
			Style stereoStyle = signature.forStereotypeItself(series.getStereotype())
				.getMergedStyle(skinParam.getCurrentStyleBuilder());

			// Merge with stereo style overwriting existing values
			if (style != null)
				stereoStyle = style.mergeWith(stereoStyle, MergeStrategy.OVERWRITE_EXISTING_VALUE);

			return stereoStyle;
		}
		return signature.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private StyleSignatureBasic getAxisStyleSignature(boolean horizontal) {
		SName axisType = horizontal ? SName.hAxis : SName.vAxis;
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chartDiagram, SName.axis, axisType);
	}

	private StyleSignatureBasic getGridStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chartDiagram, SName.grid);
	}

	private StyleSignatureBasic getLegendStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chartDiagram, SName.legend);
	}

	private StyleSignatureBasic getAnnotationStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.chartDiagram, SName.annotation);
	}

	private XDimension2D calculateLegendDimension(StringBounder stringBounder) {
		if (legendPosition == ChartDiagram.LegendPosition.NONE || series.isEmpty())
			return new XDimension2D(0, 0);

		// Get legend style
		final Style legendStyle = getLegendStyleSignature()
			.getMergedStyle(skinParam.getCurrentStyleBuilder());

		// Extract legend font configuration
		final FontConfiguration fontConfig = legendStyle.getFontConfiguration(skinParam.getIHtmlColorSet());

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

		// Get legend style
		final Style legendStyle = getLegendStyleSignature()
			.getMergedStyle(skinParam.getCurrentStyleBuilder());

		// Extract legend properties
		final FontConfiguration fontConfig = legendStyle.getFontConfiguration(skinParam.getIHtmlColorSet());

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
			HColor color = s.getColor() != null ? s.getColor() : getDefaultColor(i);

			// Draw legend symbol
			if (s.getType() == ChartSeries.SeriesType.BAR) {
				// Get bar color from style if not explicitly set
				if (s.getColor() == null) {
					final Style barStyle = getBarStyle(s);
					HColor styleColor = barStyle.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
					if (styleColor != null) {
						color = styleColor;
					}
				}
				// Draw small rectangle for bar
				final net.sourceforge.plantuml.klimt.shape.URectangle rect = net.sourceforge.plantuml.klimt.shape.URectangle
						.build(LEGEND_SYMBOL_SIZE, LEGEND_SYMBOL_SIZE);
				ug.apply(color).apply(color.bg()).apply(UTranslate.dx(currentX).compose(UTranslate.dy(currentY)))
						.draw(rect);
			} else if (s.getType() == ChartSeries.SeriesType.LINE) {
				// Get line color from style if not explicitly set
				if (s.getColor() == null) {
					final Style lineStyle = getLineStyle(s);
					HColor styleColor = lineStyle.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
					if (styleColor != null) {
						color = styleColor;
					}
				}
				// Draw small line for line chart
				final ULine line = ULine.hline(LEGEND_SYMBOL_SIZE);
				ug.apply(color).apply(UStroke.withThickness(2.0))
						.apply(UTranslate.dx(currentX).compose(UTranslate.dy(currentY + LEGEND_SYMBOL_SIZE / 2)))
						.draw(line);
			} else if (s.getType() == ChartSeries.SeriesType.AREA) {
				// Get area color from style if not explicitly set
				if (s.getColor() == null) {
					final Style areaStyle = getAreaStyle(s);
					HColor styleColor = areaStyle.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
					if (styleColor != null) {
						color = styleColor;
					}
				}
				// Draw small filled rectangle for area chart
				final net.sourceforge.plantuml.klimt.shape.URectangle rect = net.sourceforge.plantuml.klimt.shape.URectangle
						.build(LEGEND_SYMBOL_SIZE, LEGEND_SYMBOL_SIZE);
				ug.apply(color).apply(color.bg()).apply(UTranslate.dx(currentX).compose(UTranslate.dy(currentY)))
						.draw(rect);
			} else if (s.getType() == ChartSeries.SeriesType.SCATTER) {
				// Get scatter style
				final Style scatterStyle = getScatterStyle(s);

				// Get marker color from style - priority: MarkerColor > LineColor > explicit color
				HColor markerColor = color;
				HColor styleMarkerColor = scatterStyle.value(PName.MarkerColor).asColor(skinParam.getIHtmlColorSet());
				if (styleMarkerColor != null) {
					markerColor = styleMarkerColor;
				} else if (s.getColor() == null) {
					HColor styleLineColor = scatterStyle.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
					if (styleLineColor != null) {
						markerColor = styleLineColor;
					}
				}

				// Get marker shape from style (same logic as ScatterRenderer)
				ChartSeries.MarkerShape markerShape = s.getMarkerShape();
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
				// Draw marker shape for scatter plot
				drawLegendScatterMarker(ug, markerColor, currentX + LEGEND_SYMBOL_SIZE / 2, currentY + LEGEND_SYMBOL_SIZE / 2, LEGEND_SYMBOL_SIZE * 0.7, markerShape);
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

	private void drawLegendScatterMarker(UGraphic ug, HColor color, double x, double y, double size, ChartSeries.MarkerShape shape) {
		switch (shape) {
		case CIRCLE:
			final net.sourceforge.plantuml.klimt.shape.UEllipse circle = net.sourceforge.plantuml.klimt.shape.UEllipse.build(size, size);
			ug.apply(color).apply(color.bg()).apply(UTranslate.dx(x - size / 2).compose(UTranslate.dy(y - size / 2))).draw(circle);
			break;
		case SQUARE:
			final net.sourceforge.plantuml.klimt.shape.URectangle square = net.sourceforge.plantuml.klimt.shape.URectangle.build(size, size);
			ug.apply(color).apply(color.bg()).apply(UTranslate.dx(x - size / 2).compose(UTranslate.dy(y - size / 2))).draw(square);
			break;
		case TRIANGLE:
			final net.sourceforge.plantuml.klimt.shape.UPolygon triangle = new net.sourceforge.plantuml.klimt.shape.UPolygon();
			triangle.addPoint(0, -size / 2);
			triangle.addPoint(-size / 2, size / 2);
			triangle.addPoint(size / 2, size / 2);
			ug.apply(color).apply(color.bg()).apply(UTranslate.dx(x).compose(UTranslate.dy(y))).draw(triangle);
			break;
		}
	}

	private void drawAnnotations(UGraphic ug, double plotWidth, double plotHeight, HColor lineColor, HColor fontColor,
			StringBounder stringBounder) {
		if (annotations == null || annotations.isEmpty())
			return;

		// Get annotation style
		final Style annotationStyle = getAnnotationStyleSignature()
			.getMergedStyle(skinParam.getCurrentStyleBuilder());

		// Extract annotation properties
		final FontConfiguration fontConfig = annotationStyle.getFontConfiguration(skinParam.getIHtmlColorSet());

		// Extract arrow line color
		HColor arrowColor = annotationStyle.value(PName.LineColor)
			.asColor(skinParam.getIHtmlColorSet());
		if (arrowColor == null) {
			// Fallback to black
			arrowColor = HColors.BLACK;
			try {
				arrowColor = skinParam.getIHtmlColorSet().getColor("#000000");
			} catch (Exception e) {
				arrowColor = lineColor;
			}
		}

		// Extract arrow line thickness
		final double arrowThickness = annotationStyle.value(PName.LineThickness).asDouble();

		for (ChartAnnotation annotation : annotations) {
			// Calculate the pixel position of the annotation
			double x = 0;
			double y = 0;

			// Handle X position (categorical or numeric)
			if (annotation.getXPosition() instanceof Double) {
				// Numeric X position - convert to pixel coordinate
				double xValue = (Double) annotation.getXPosition();
				if (orientation == ChartDiagram.Orientation.HORIZONTAL) {
					// For horizontal, numeric axis is Y
					y = plotHeight * (1.0 - (xValue - yAxis.getMin()) / (yAxis.getMax() - yAxis.getMin()));
				} else {
					// For vertical, numeric X would map across the plot width
					// This is unusual but support it for flexibility
					x = plotWidth * ((xValue - yAxis.getMin()) / (yAxis.getMax() - yAxis.getMin()));
				}
			} else if (annotation.getXPosition() instanceof String) {
				// Categorical X position - find index in labels
				String xLabel = (String) annotation.getXPosition();
				int index = xAxisLabels.indexOf(xLabel);
				if (index < 0)
					continue; // Label not found, skip this annotation

				if (orientation == ChartDiagram.Orientation.HORIZONTAL) {
					// For horizontal, categories are on Y axis
					y = plotHeight * (index + 0.5) / xAxisLabels.size();
				} else {
					// For vertical, categories are on X axis
					x = plotWidth * (index + 0.5) / xAxisLabels.size();
				}
			}

			// Handle Y position (always numeric)
			if (orientation == ChartDiagram.Orientation.HORIZONTAL) {
				// For horizontal, numeric axis is X (yAxis holds the numeric range)
				x = plotWidth * ((annotation.getYPosition() - yAxis.getMin()) / (yAxis.getMax() - yAxis.getMin()));
			} else {
				// For vertical, numeric axis is Y
				y = plotHeight * (1.0 - (annotation.getYPosition() - yAxis.getMin()) / (yAxis.getMax() - yAxis.getMin()));
			}

			// Create text block for annotation
			final TextBlock textBlock = Display.getWithNewlines(skinParam.getPragma(), annotation.getText())
					.create(fontConfig, HorizontalAlignment.LEFT, skinParam);
			final XDimension2D textDim = textBlock.calculateDimension(stringBounder);

			// Draw arrow if requested
			if (annotation.isShowArrow()) {
				// Check if there's enough space above the point for text + arrow
				final double minSpaceAbove = textDim.getHeight() + 40;
				final boolean placeAbove = y > minSpaceAbove;

				if (placeAbove) {
					// Position text above the data point with arrow pointing down
					final double textX = x - textDim.getWidth() / 2;
					final double textY = y - textDim.getHeight() - 40;

					// Draw text first
					textBlock.drawU(ug.apply(UTranslate.dx(textX).compose(UTranslate.dy(textY))));

					// Draw arrow from bottom of text to data point
					final double arrowStartY = textY + textDim.getHeight() + 5;
					final double arrowEndY = y - 5;
					final double arrowLength = arrowEndY - arrowStartY;

					if (arrowLength > 0) {
						final ULine arrowLine = new ULine(0, arrowLength);
						ug.apply(arrowColor).apply(UStroke.withThickness(arrowThickness))
								.apply(UTranslate.dx(x).compose(UTranslate.dy(arrowStartY)))
								.draw(arrowLine);

						// Draw arrowhead pointing down
						final net.sourceforge.plantuml.klimt.shape.UPolygon arrowhead = new net.sourceforge.plantuml.klimt.shape.UPolygon();
						arrowhead.addPoint(0, 0);
						arrowhead.addPoint(-5, -8);
						arrowhead.addPoint(5, -8);
						ug.apply(arrowColor).apply(arrowColor.bg())
								.apply(UTranslate.dx(x).compose(UTranslate.dy(arrowEndY)))
								.draw(arrowhead);
					}
				} else {
					// Position text below the data point with arrow pointing up
					final double textX = x - textDim.getWidth() / 2;
					final double textY = y + 40;

					// Draw text below
					textBlock.drawU(ug.apply(UTranslate.dx(textX).compose(UTranslate.dy(textY))));

					// Draw arrow from top of text to data point
					final double arrowStartY = textY - 5;
					final double arrowEndY = y + 5;
					final double arrowLength = arrowStartY - arrowEndY;

					if (arrowLength > 0) {
						final ULine arrowLine = new ULine(0, -arrowLength);
						ug.apply(arrowColor).apply(UStroke.withThickness(arrowThickness))
								.apply(UTranslate.dx(x).compose(UTranslate.dy(arrowStartY)))
								.draw(arrowLine);

						// Draw arrowhead pointing up
						final net.sourceforge.plantuml.klimt.shape.UPolygon arrowhead = new net.sourceforge.plantuml.klimt.shape.UPolygon();
						arrowhead.addPoint(0, 0);
						arrowhead.addPoint(-5, 8);
						arrowhead.addPoint(5, 8);
						ug.apply(arrowColor).apply(arrowColor.bg())
								.apply(UTranslate.dx(x).compose(UTranslate.dy(arrowEndY)))
								.draw(arrowhead);
					}
				}
			} else {
				// Draw text near the point without arrow
				textBlock.drawU(ug.apply(UTranslate.dx(x - textDim.getWidth() / 2)
						.compose(UTranslate.dy(y - textDim.getHeight() - 5))));
			}
		}
	}

}
