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
import java.util.Locale;

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleQueries;

/**
 * Draws a pie (or a donut). The slices go clockwise from 12 o'clock, in the order of the
 * values. Their names are written around the pie, or in the legend when there is one.
 * "labels" and "percent" write the value and the share of each slice inside it.
 */
public class PieRenderer {

	private static final double RADIUS = 140;
	private static final double MARGIN = 20;
	private static final double NAME_GAP = 10;
	private static final double LEGEND_MARGIN = 20;

	private final ISkinParam skinParam;
	private final ChartSeries series;
	private final ChartDiagram.LegendPosition legendPosition;
	private final Style style;
	private final FontConfiguration fontConfig;
	private final List<Slice> slices = new ArrayList<>();
	private final double total;

	private static class Slice {
		private final String name;
		private final double value;
		private final HColor color;
		private double start;
		private double end;

		Slice(String name, double value, HColor color) {
			this.name = name;
			this.value = value;
			this.color = color;
		}

		double middle() {
			return (start + end) / 2;
		}
	}

	public PieRenderer(ISkinParam skinParam, ChartSeries series, List<String> angularLabels,
			ChartDiagram.LegendPosition legendPosition) {
		this.skinParam = skinParam;
		this.series = series;
		this.legendPosition = legendPosition;
		this.style = ChartRenderer.getSeriesStyle(skinParam, StyleQueries.CHARTDIAG_PIE, series);
		this.fontConfig = style.getFontConfiguration(skinParam.getIHtmlColorSet());

		double sum = 0;
		final List<Double> values = series.getValues();
		for (int i = 0; i < values.size(); i++) {
			final Double value = values.get(i);
			if (value == null || value <= 0)
				continue;
			String name = null;
			if (series.hasKeys())
				name = series.getKeys().get(i);
			else if (i < angularLabels.size())
				name = angularLabels.get(i);
			HColor color = series.getItemColor(i);
			if (color == null)
				color = ChartRenderer.getDefaultColor(skinParam, i);
			slices.add(new Slice(name, value, color));
			sum += value;
		}
		this.total = sum;

		double angle = 0;
		for (Slice slice : slices) {
			slice.start = angle;
			angle += 2 * Math.PI * slice.value / total;
			slice.end = angle;
		}
	}

	// The point at a distance r from the center, at an angle measured clockwise from 12 o'clock
	static XPoint2D polar(double r, double angle) {
		return new XPoint2D(r * Math.sin(angle), -r * Math.cos(angle));
	}

	private boolean hasLegend() {
		return legendPosition != ChartDiagram.LegendPosition.NONE;
	}

	private ChartLegendBlock getLegend() {
		final ChartLegendBlock legend = new ChartLegendBlock(skinParam, legendPosition);
		for (Slice slice : slices)
			if (slice.name != null)
				legend.add(slice.name, slice.color);
		return legend;
	}

	private TextBlock text(String s) {
		return Display.getWithNewlines(skinParam.getPragma(), s).create(fontConfig, HorizontalAlignment.CENTER,
				skinParam);
	}

	// Where the name of a slice is written, outside the pie, relative to the center. The text
	// moves away from the pie in the direction of the middle of the slice: on its right on
	// the right half, above it at the top, and so on
	private UTranslate namePosition(Slice slice, XDimension2D dim) {
		return outside(RADIUS + NAME_GAP, slice.middle(), dim);
	}

	// Where a text of the given size is written next to the point at a distance r and at an
	// angle from the center, on the far side of that point
	static UTranslate outside(double r, double angle, XDimension2D dim) {
		final XPoint2D anchor = polar(r, angle);
		final double left = anchor.getX() - dim.getWidth() / 2 * (1 - Math.sin(angle));
		final double top = anchor.getY() - dim.getHeight() / 2 * (1 + Math.cos(angle));
		return new UTranslate(left, top);
	}

	// The box around the pie and the names written around it, relative to the center:
	// { minX, minY, maxX, maxY }
	private double[] getBounds(StringBounder stringBounder) {
		final double[] bounds = { -RADIUS, -RADIUS, RADIUS, RADIUS };
		if (hasLegend())
			return bounds;
		for (Slice slice : slices) {
			if (slice.name == null)
				continue;
			final XDimension2D dim = text(slice.name).calculateDimension(stringBounder);
			final UTranslate pos = namePosition(slice, dim);
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
		final double pieWidth = bounds[2] - bounds[0] + 2 * MARGIN;
		final double pieHeight = bounds[3] - bounds[1] + 2 * MARGIN;

		// Where the box of the pie starts, the legend being on one of its sides
		double boxX = (dim.getWidth() - pieWidth) / 2;
		double boxY = (dim.getHeight() - pieHeight) / 2;
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
				legendX = pieWidth - MARGIN + LEGEND_MARGIN;
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
				legendY = pieHeight - MARGIN + LEGEND_MARGIN;
				break;
			}
			legend.drawU(ug.apply(new UTranslate(legendX, legendY)));
		}

		final UGraphic ugCenter = ug.apply(new UTranslate(boxX + MARGIN - bounds[0], boxY + MARGIN - bounds[1]));
		drawSlices(ugCenter);
		drawLabels(ugCenter);
		if (hasLegend() == false)
			for (Slice slice : slices)
				if (slice.name != null) {
					final TextBlock text = text(slice.name);
					text.drawU(ugCenter.apply(namePosition(slice, text.calculateDimension(stringBounder))));
				}
	}

	private void drawSlices(UGraphic ug) {
		final HColor lineColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final double thickness = style.value(PName.LineThickness).asDouble();
		final double inner = RADIUS * series.getDonutHole();
		for (Slice slice : slices) {
			final UGraphic ugSlice = ug.apply(slice.color.bg()).apply(lineColor == null ? slice.color : lineColor)
					.apply(UStroke.withThickness(thickness));
			ugSlice.draw(slicePath(slice.start, slice.end, inner));
		}
	}

	// The outline of a slice: along the outer circle, then back along the inner circle of a
	// donut, or to the center of a pie. An arc is split in arcs of at most a half turn, so
	// that a slice of a whole turn (a single value) is drawn too
	private static UPath slicePath(double start, double end, double inner) {
		final UPath path = UPath.none();
		final XPoint2D first = polar(RADIUS, start);
		path.moveTo(first);
		addArc(path, RADIUS, start, end, true);
		final boolean whole = end - start > 2 * Math.PI - 1e-9;
		if (inner > 0) {
			path.lineTo(polar(inner, end));
			addArc(path, inner, end, start, false);
		} else if (whole == false) {
			path.lineTo(0, 0);
		}
		path.lineTo(first);
		return path;
	}

	private static void addArc(UPath path, double r, double from, double to, boolean clockwise) {
		final int count = (int) Math.ceil(Math.abs(to - from) / Math.PI - 1e-9);
		for (int i = 1; i <= count; i++) {
			final XPoint2D pt = polar(r, from + (to - from) * i / count);
			path.arcTo(r, r, 0, 0, clockwise ? 1 : 0, pt.getX(), pt.getY());
		}
	}

	private void drawLabels(UGraphic ug) {
		if (series.isShowLabels() == false && series.isShowPercent() == false)
			return;
		final double hole = series.getDonutHole();
		final double r = hole > 0 ? RADIUS * (1 + hole) / 2 : RADIUS * 0.65;
		for (Slice slice : slices) {
			final TextBlock text = text(getLabel(slice));
			final XDimension2D dim = text.calculateDimension(ug.getStringBounder());
			final XPoint2D center = slices.size() == 1 && hole == 0 ? new XPoint2D(0, 0) : polar(r, slice.middle());
			text.drawU(ug.apply(new UTranslate(center.getX() - dim.getWidth() / 2, center.getY() - dim.getHeight() / 2)));
		}
	}

	private String getLabel(Slice slice) {
		final String percent = formatPercent(100 * slice.value / total);
		if (series.isShowLabels() && series.isShowPercent())
			return ChartRenderer.formatValue(slice.value) + " (" + percent + ")";
		if (series.isShowLabels())
			return ChartRenderer.formatValue(slice.value);
		return percent;
	}

	// 33.3%, 50%, 0.4%
	private static String formatPercent(double percent) {
		final String s = String.format(Locale.ROOT, "%.1f", percent);
		return (s.endsWith(".0") ? s.substring(0, s.length() - 2) : s) + "%";
	}

}
