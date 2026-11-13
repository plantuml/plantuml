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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSimple;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleQueries;

/**
 * Draws a radar chart: one spoke per category (a-axis), clockwise from 12 o'clock, rings at
 * the ticks of the radial scale (r-axis), and one filled polygon per series.
 */
public class RadarRenderer {

	private static final double RADIUS = 140;
	private static final double MARGIN = 20;
	private static final double NAME_GAP = 8;
	private static final double LEGEND_MARGIN = 20;
	private static final double MARKER_SIZE = 6;
	// Opacity of the inside of a series, so that the ones below stay visible
	private static final int FILL_ALPHA = 64;

	private final ISkinParam skinParam;
	private final List<String> categories;
	private final List<List<Double>> values;
	private final List<ChartSeries> series;
	private final ChartAxis axis;
	private final boolean circleGrid;
	private final ChartDiagram.LegendPosition legendPosition;
	private final FontConfiguration axisFont;

	public RadarRenderer(ISkinParam skinParam, List<String> categories, List<List<Double>> values,
			List<ChartSeries> series, ChartAxis axis, boolean circleGrid, ChartDiagram.LegendPosition legendPosition) {
		this.skinParam = skinParam;
		this.categories = categories;
		this.values = values;
		this.series = series;
		this.axis = axis;
		this.circleGrid = circleGrid;
		this.legendPosition = legendPosition;
		this.axisFont = skinParam.getCurrentStyleBuilder().getMergedStyle(StyleQueries.CHARTDIAG_AXIS)
				.getFontConfiguration(skinParam.getIHtmlColorSet());
	}

	private double angle(int i) {
		return 2 * Math.PI * i / categories.size();
	}

	// Distance from the center of a value, kept inside the scale
	private double radius(double value) {
		final double range = axis.getMax() - axis.getMin();
		if (range <= 0)
			return 0;
		return RADIUS * Math.max(0, Math.min(1, (value - axis.getMin()) / range));
	}

	private TextBlock text(String s, FontConfiguration fontConfig) {
		return Display.getWithNewlines(skinParam.getPragma(), s).create(fontConfig, HorizontalAlignment.CENTER,
				skinParam);
	}

	private boolean hasLegend() {
		return legendPosition != ChartDiagram.LegendPosition.NONE && series.isEmpty() == false;
	}

	private ChartLegendBlock getLegend() {
		final ChartLegendBlock legend = new ChartLegendBlock(skinParam, legendPosition);
		for (int i = 0; i < series.size(); i++)
			legend.add(series.get(i).getName(), getLineColor(i));
		return legend;
	}

	private Style getStyle(ChartSeries s) {
		return ChartRenderer.getSeriesStyle(skinParam, StyleQueries.CHARTDIAG_RADAR, s);
	}

	// A color given by the style of a series ("radar { LineColor red }" or a stereotype), or
	// null when it is only the one inherited from the diagram, so that the series are told
	// apart by the default colors
	private HColor getStyledColor(ChartSeries s, PName name) {
		final HColor color = getStyle(s).value(name).asColor(skinParam.getIHtmlColorSet());
		final HColor inherited = skinParam.getCurrentStyleBuilder().getMergedStyle(StyleQueries.CHARTDIAG)
				.value(name).asColor(skinParam.getIHtmlColorSet());
		if (color == null || color.equals(inherited))
			return null;
		return color;
	}

	// The color of a series: explicit color > style LineColor > default color
	private HColor getLineColor(int index) {
		final ChartSeries s = series.get(index);
		if (s.getColor() != null)
			return s.getColor();
		final HColor color = getStyledColor(s, PName.LineColor);
		if (color != null)
			return color;
		return ChartRenderer.getDefaultColor(skinParam, index);
	}

	// The inside of a series: style BackGroundColor, else its color, made translucent
	private HColor getFillColor(int index, HColor lineColor) {
		final HColor color = getStyledColor(series.get(index), PName.BackGroundColor);
		if (color != null)
			return color;
		if (lineColor instanceof HColorSimple) {
			final XColor c = ((HColorSimple) lineColor).getAwtColor();
			return HColorSimple.create(new XColor(c.getRed(), c.getGreen(), c.getBlue(), FILL_ALPHA));
		}
		return HColors.transparent();
	}

	// The box around the rings and the names of the categories, relative to the center:
	// { minX, minY, maxX, maxY }
	private double[] getBounds(StringBounder stringBounder) {
		final double[] bounds = { -RADIUS, -RADIUS, RADIUS, RADIUS };
		for (int i = 0; i < categories.size(); i++) {
			final XDimension2D dim = text(categories.get(i), axisFont).calculateDimension(stringBounder);
			final UTranslate pos = PieRenderer.outside(RADIUS + NAME_GAP, angle(i), dim);
			bounds[0] = Math.min(bounds[0], pos.getDx());
			bounds[1] = Math.min(bounds[1], pos.getDy());
			bounds[2] = Math.max(bounds[2], pos.getDx() + dim.getWidth());
			bounds[3] = Math.max(bounds[3], pos.getDy() + dim.getHeight());
		}
		return bounds;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final double[] bounds = getBounds(stringBounder);
		double width = bounds[2] - bounds[0] + 2 * MARGIN;
		double height = bounds[3] - bounds[1] + 2 * MARGIN;
		if (hasLegend()) {
			final XDimension2D legend = getLegend().calculateDimension(stringBounder);
			if (legendPosition == ChartDiagram.LegendPosition.LEFT
					|| legendPosition == ChartDiagram.LegendPosition.RIGHT) {
				width += legend.getWidth() + LEGEND_MARGIN;
				height = Math.max(height, legend.getHeight() + 2 * MARGIN);
			} else {
				height += legend.getHeight() + LEGEND_MARGIN;
				width = Math.max(width, legend.getWidth() + 2 * MARGIN);
			}
		}
		return new XDimension2D(width, height);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dim = calculateDimension(stringBounder);
		final double[] bounds = getBounds(stringBounder);
		final double chartWidth = bounds[2] - bounds[0] + 2 * MARGIN;
		final double chartHeight = bounds[3] - bounds[1] + 2 * MARGIN;

		double boxX = (dim.getWidth() - chartWidth) / 2;
		double boxY = (dim.getHeight() - chartHeight) / 2;
		if (hasLegend()) {
			final ChartLegendBlock legend = getLegend();
			final XDimension2D legendDim = legend.calculateDimension(stringBounder);
			final double legendX;
			final double legendY;
			switch (legendPosition) {
			case LEFT:
				boxX = legendDim.getWidth() + LEGEND_MARGIN;
				legendX = MARGIN;
				legendY = (dim.getHeight() - legendDim.getHeight()) / 2;
				break;
			case RIGHT:
				boxX = 0;
				legendX = chartWidth - MARGIN + LEGEND_MARGIN;
				legendY = (dim.getHeight() - legendDim.getHeight()) / 2;
				break;
			case TOP:
				boxY = legendDim.getHeight() + LEGEND_MARGIN;
				legendX = (dim.getWidth() - legendDim.getWidth()) / 2;
				legendY = MARGIN;
				break;
			default:
				boxY = 0;
				legendX = (dim.getWidth() - legendDim.getWidth()) / 2;
				legendY = chartHeight - MARGIN + LEGEND_MARGIN;
				break;
			}
			legend.drawU(ug.apply(new UTranslate(legendX, legendY)));
		}

		final UGraphic ugCenter = ug.apply(new UTranslate(boxX + MARGIN - bounds[0], boxY + MARGIN - bounds[1]));
		drawGrid(ugCenter);
		for (int i = 0; i < series.size(); i++)
			drawSeries(ugCenter, i);
		drawTickLabels(ugCenter);
	}

	private void drawGrid(UGraphic ug) {
		final Style gridStyle = skinParam.getCurrentStyleBuilder().getMergedStyle(StyleQueries.CHARTDIAG_GRID);
		HColor gridColor = gridStyle.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		if (gridColor == null)
			gridColor = skinParam.getIHtmlColorSet().getColorOrWhite("#D0D0D0");
		final UGraphic ugGrid = ug.apply(gridColor).apply(HColors.transparent().bg())
				.apply(UStroke.withThickness(gridStyle.value(PName.LineThickness).asDouble()));

		// Rings, at the ticks of the scale
		for (Map.Entry<Double, String> tick : ChartRenderer.getTicks(axis)) {
			final double r = RADIUS * tick.getKey();
			if (r <= 0)
				continue;
			if (circleGrid || categories.size() < 3)
				ugGrid.apply(new UTranslate(-r, -r)).draw(UEllipse.build(2 * r, 2 * r));
			else
				ugGrid.draw(polygon(r));
		}

		// Spokes, and the name of each category at their end
		final Style axisStyle = skinParam.getCurrentStyleBuilder().getMergedStyle(StyleQueries.CHARTDIAG_AXIS);
		HColor axisColor = axisStyle.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		if (axisColor == null)
			axisColor = gridColor;
		final UGraphic ugAxis = ug.apply(axisColor)
				.apply(UStroke.withThickness(axisStyle.value(PName.LineThickness).asDouble()));
		for (int i = 0; i < categories.size(); i++) {
			final XPoint2D end = PieRenderer.polar(RADIUS, angle(i));
			ugAxis.draw(new ULine(end.getX(), end.getY()));
			final TextBlock name = text(categories.get(i), axisFont);
			name.drawU(ug.apply(PieRenderer.outside(RADIUS + NAME_GAP, angle(i),
					name.calculateDimension(ug.getStringBounder()))));
		}
	}

	// A ring of the grid, with a corner on each spoke
	private UPolygon polygon(double r) {
		final UPolygon result = new UPolygon();
		for (int i = 0; i < categories.size(); i++)
			result.addPoint(PieRenderer.polar(r, angle(i)));
		return result;
	}

	// The value of each ring, written just inside it on the right of the first spoke, so that
	// the outer one stays clear of the name of the first category. The center is not labelled
	private void drawTickLabels(UGraphic ug) {
		for (Map.Entry<Double, String> tick : ChartRenderer.getTicks(axis)) {
			if (tick.getKey() <= 0)
				continue;
			final TextBlock text = text(tick.getValue(), axisFont);
			text.drawU(ug.apply(new UTranslate(3, -RADIUS * tick.getKey())));
		}
	}

	private void drawSeries(UGraphic ug, int index) {
		final ChartSeries s = series.get(index);
		final List<Double> seriesValues = values.get(index);
		if (categories.isEmpty())
			return;
		final HColor lineColor = getLineColor(index);
		final HColor fillColor = getFillColor(index, lineColor);
		final double thickness = getStyle(s).value(PName.LineThickness).asDouble();

		// A missing value is drawn at the center
		final List<XPoint2D> points = new ArrayList<>();
		for (int i = 0; i < categories.size(); i++) {
			final Double value = seriesValues.get(i);
			points.add(PieRenderer.polar(value == null ? 0 : radius(value), angle(i)));
		}

		ug.apply(lineColor).apply(fillColor.bg()).apply(UStroke.withThickness(thickness)).draw(new UPolygon(points));

		final UGraphic ugMarker = ug.apply(lineColor).apply(lineColor.bg());
		for (int i = 0; i < points.size(); i++) {
			if (seriesValues.get(i) == null)
				continue;
			final XPoint2D pt = points.get(i);
			ugMarker.apply(new UTranslate(pt.getX() - MARKER_SIZE / 2, pt.getY() - MARKER_SIZE / 2))
					.draw(UEllipse.build(MARKER_SIZE, MARKER_SIZE));
			if (s.isShowLabels()) {
				final TextBlock text = text(ChartRenderer.formatValue(seriesValues.get(i)), axisFont);
				final XDimension2D dim = text.calculateDimension(ug.getStringBounder());
				text.drawU(ug.apply(PieRenderer.outside(radius(seriesValues.get(i)) + MARKER_SIZE, angle(i), dim)));
			}
		}
	}

}
