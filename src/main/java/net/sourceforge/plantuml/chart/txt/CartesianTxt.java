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
package net.sourceforge.plantuml.chart.txt;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.chart.CartesianChart;
import net.sourceforge.plantuml.chart.ChartAxis;
import net.sourceforge.plantuml.chart.ChartDiagram;
import net.sourceforge.plantuml.chart.ChartSeries;
import net.sourceforge.plantuml.chart.ChartTicks;
import net.sourceforge.plantuml.chart.StackLayout;

/**
 * A chart of bars and lines on cartesian axes, drawn as text.
 * <p>
 * Vertical: the value axis on the left, with the ticks of the graphic rendering, and one
 * column of {@link #categoryWidth} characters per category. A bar fills the cells under
 * its value, the last one by half when it is about half filled. A line goes from category
 * to category in steps, turning halfway between them.
 * <p>
 * Horizontal: the categories down the left side, one band of rows each, and the value
 * axis along the bottom. Only bars are drawn so.
 * <p>
 * Each bar series has its own fill and each line series its own line, so that they can be
 * told apart; the legend (see {@link ChartTxt}) shows them.
 */
final class CartesianTxt {

	// Rows of the plot of a vertical chart, above the h-axis
	private static final int PLOT_HEIGHT = 20;
	// Columns of the plot of a horizontal chart, right of the v-axis
	private static final int PLOT_WIDTH = 60;

	private final CartesianChart chart;
	private final TxtCharset charset;
	private final List<ChartSeries> bars = new ArrayList<>();
	private final List<ChartSeries> lines = new ArrayList<>();
	private final boolean stacked;
	private final StackLayout stack;

	CartesianTxt(CartesianChart chart, TxtCharset charset) {
		this.chart = chart;
		this.charset = charset;
		for (ChartSeries s : chart.getSeries())
			if (s.getType() == ChartSeries.SeriesType.BAR)
				bars.add(s);
			else
				lines.add(s);
		this.stacked = chart.getStackMode() == ChartDiagram.StackMode.STACKED && bars.size() > 1;
		this.stack = stacked ? StackLayout.bars(bars) : null;
	}

	/**
	 * Whether a chart can be drawn so: bars and lines against categories, on a single
	 * value axis; only bars when horizontal. The others are written as a table.
	 */
	static boolean isSupported(CartesianChart chart) {
		if (chart.getSeries().isEmpty() || chart.getCategories().isEmpty() || chart.hasCoordinatePairs()
				|| chart.getValueAxis2() != null)
			return false;
		for (ChartSeries s : chart.getSeries()) {
			final ChartSeries.SeriesType type = s.getType();
			if (type == ChartSeries.SeriesType.BAR)
				continue;
			if (type != ChartSeries.SeriesType.LINE || chart.isHorizontal())
				return false;
		}
		return true;
	}

	/**
	 * The character showing a series in the legend.
	 */
	char getSymbol(ChartSeries s) {
		if (s.getType() == ChartSeries.SeriesType.BAR)
			return charset.fill(bars.indexOf(s));
		return charset.lineH(lines.indexOf(s));
	}

	List<String> getLines() {
		if (chart.isHorizontal())
			return drawHorizontal();
		return drawVertical();
	}

	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	// Vertical

	private List<String> drawVertical() {
		final CharGrid grid = new CharGrid();
		final ChartAxis axis = chart.getValueAxis();
		final List<ChartTicks.Tick> ticks = ChartTicks.valueTicks(axis);
		final List<String> categories = chart.getCategories();
		final int spacing = getCategorySpacing();

		int labelWidth = 0;
		for (ChartTicks.Tick tick : ticks)
			labelWidth = Math.max(labelWidth, tick.getLabel().length());
		final int axisX = labelWidth + 1;
		final int x0 = axisX + 1;
		final int slots = stacked || bars.isEmpty() ? 1 : bars.size();
		final int categoryWidth = categoryWidth(categories, spacing, slots);
		final int barWidth = barWidth(categoryWidth, slots);
		final int plotWidth = categories.size() * categoryWidth;

		// The title of the value axis above it
		final String valueTitle = axis.getTitle();
		final int top = isEmpty(valueTitle) ? 0 : 1;
		if (top > 0)
			grid.putString(0, 0, valueTitle);
		final int axisY = top + PLOT_HEIGHT;

		// Dotted grid line at each tick, then the axis
		for (ChartTicks.Tick tick : ticks) {
			final int y = top + PLOT_HEIGHT - (int) Math.round(tick.getRatio() * PLOT_HEIGHT);
			if (y < axisY)
				for (int x = x0; x < x0 + plotWidth; x++)
					grid.put(x, y, charset.dot());
		}
		for (int y = top; y < axisY; y++)
			grid.put(axisX, y, charset.vAxis());
		for (int x = x0; x < x0 + plotWidth; x++)
			grid.put(x, axisY, charset.hAxis());
		grid.put(axisX, axisY, charset.origin());
		for (ChartTicks.Tick tick : ticks) {
			final int y = top + PLOT_HEIGHT - (int) Math.round(tick.getRatio() * PLOT_HEIGHT);
			if (y < axisY)
				grid.put(axisX, y, charset.tickLeft());
			grid.putString(axisX - tick.getLabel().length(), y, tick.getLabel());
		}

		// Bars, side by side or stacked
		for (int b = 0; b < bars.size(); b++) {
			final List<Double> values = bars.get(b).getValues();
			for (int c = 0; c < Math.min(values.size(), categories.size()); c++) {
				if (values.get(c) == null)
					continue;
				final double from = stacked ? stack.getBase(b, c) : 0;
				final double to = stacked ? stack.getTop(b, c) : values.get(c);
				final double lo = toUnits(axis, Math.min(from, to), PLOT_HEIGHT);
				final double hi = toUnits(axis, Math.max(from, to), PLOT_HEIGHT);
				final int left = x0 + c * categoryWidth + (categoryWidth - barWidth * slots) / 2
						+ (stacked ? 0 : b * barWidth);
				for (int x = left; x < left + barWidth; x++)
					for (int k = 0; k < PLOT_HEIGHT; k++) {
						final char ch = cell(lo, hi, k, b, true);
						if (ch != 0)
							grid.put(x, axisY - 1 - k, ch);
					}
			}
		}

		// Lines, from the center of a category to the next one
		for (int l = 0; l < lines.size(); l++) {
			final List<Double> values = lines.get(l).getValues();
			final int count = Math.min(values.size(), categories.size());
			for (int c = 0; c < count; c++) {
				if (values.get(c) == null)
					continue;
				final int x = x0 + c * categoryWidth + categoryWidth / 2;
				final int y = axisY - (int) Math.round(toUnits(axis, values.get(c), PLOT_HEIGHT));
				final boolean before = c > 0 && values.get(c - 1) != null;
				final boolean after = c + 1 < count && values.get(c + 1) != null;
				if (after) {
					final int y2 = axisY - (int) Math.round(toUnits(axis, values.get(c + 1), PLOT_HEIGHT));
					drawStep(grid, x, y, x + categoryWidth, y2, l);
				} else if (before == false) {
					// A point alone
					for (int dx = -1; dx <= 1; dx++)
						grid.put(x + dx, y, charset.lineH(l));
				}
			}
		}

		// Category labels, centered under their tick, then the title of the h-axis
		int bottom = axisY;
		for (int c = 0; c < categories.size(); c += spacing) {
			final int center = x0 + c * categoryWidth + categoryWidth / 2;
			grid.put(center, axisY, charset.tickDown());
			final List<String> wrapped = wrap(categories.get(c), categoryWidth - 1);
			for (int j = 0; j < wrapped.size(); j++) {
				final String s = wrapped.get(j);
				grid.putString(center - (s.length() - 1) / 2, axisY + 1 + j, s);
				bottom = Math.max(bottom, axisY + 1 + j);
			}
		}
		final String categoryTitle = chart.getCategoryTitle();
		if (isEmpty(categoryTitle) == false)
			grid.putString(x0 + Math.max(0, (plotWidth - categoryTitle.length()) / 2), bottom + 1, categoryTitle);

		return grid.getLines();
	}

	// Width of a category: its label and a margin, from 6 to 12 characters (a longer
	// label is wrapped), and enough for the bars side by side
	private static int categoryWidth(List<String> categories, int spacing, int slots) {
		int longest = 0;
		for (int c = 0; c < categories.size(); c += spacing)
			longest = Math.max(longest, categories.get(c).length());
		int result = Math.max(6, Math.min(12, longest + 3));
		while (barWidth(result, slots) * slots + 2 > result)
			result++;
		return result;
	}

	// Width of one bar: two thirds of the category, shared by the bars side by side,
	// which are at least two characters wide
	private static int barWidth(int categoryWidth, int slots) {
		return Math.max(slots > 1 ? 2 : 1, (int) Math.round(categoryWidth * 2.0 / 3 / slots));
	}

	private int getCategorySpacing() {
		final Integer spacing = chart.getCategoryTickSpacing();
		return spacing != null && spacing > 0 ? spacing : 1;
	}

	// A line from (x1, y1) to (x2, y2), x1 < x2: level, or in a step turning halfway
	private void drawStep(CharGrid grid, int x1, int y1, int x2, int y2, int style) {
		if (y1 == y2) {
			for (int x = x1; x <= x2; x++)
				grid.put(x, y1, charset.lineH(style));
			return;
		}
		final int middle = (x1 + x2) / 2;
		for (int x = x1; x < middle; x++)
			grid.put(x, y1, charset.lineH(style));
		for (int x = middle + 1; x <= x2; x++)
			grid.put(x, y2, charset.lineH(style));
		for (int y = Math.min(y1, y2) + 1; y < Math.max(y1, y2); y++)
			grid.put(middle, y, charset.lineV(style));
		if (y2 < y1) {
			// Going up
			grid.put(middle, y1, charset.cornerLeftUp(style));
			grid.put(middle, y2, charset.cornerRightDown(style));
		} else {
			grid.put(middle, y1, charset.cornerLeftDown(style));
			grid.put(middle, y2, charset.cornerRightUp(style));
		}
	}

	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	// Horizontal

	private List<String> drawHorizontal() {
		final CharGrid grid = new CharGrid();
		final ChartAxis axis = chart.getValueAxis();
		final List<ChartTicks.Tick> ticks = ChartTicks.horizontalValueTicks(axis);
		final List<String> categories = chart.getCategories();
		final int slots = stacked ? 1 : bars.size();
		final int band = slots + 3;

		int labelWidth = 0;
		for (String category : categories)
			labelWidth = Math.max(labelWidth, category.length());
		final int axisX = labelWidth + 1;
		final int x0 = axisX + 1;

		// The title of the category axis above it
		final String categoryTitle = chart.getCategoryTitle();
		final int top = isEmpty(categoryTitle) ? 0 : 1;
		if (top > 0)
			grid.putString(0, 0, categoryTitle);
		final int axisY = top + categories.size() * band;

		// Dotted grid line at each tick, then the axis
		for (ChartTicks.Tick tick : ticks) {
			final int x = axisX + (int) Math.round(tick.getRatio() * PLOT_WIDTH);
			if (x > axisX)
				for (int y = top; y < axisY; y++)
					grid.put(x, y, charset.dot());
		}
		for (int y = top; y < axisY; y++)
			grid.put(axisX, y, charset.vAxis());
		for (int x = x0; x <= axisX + PLOT_WIDTH; x++)
			grid.put(x, axisY, charset.hAxis());
		grid.put(axisX, axisY, charset.origin());
		int lastEnd = -1;
		for (ChartTicks.Tick tick : ticks) {
			final int x = axisX + (int) Math.round(tick.getRatio() * PLOT_WIDTH);
			if (x > axisX)
				grid.put(x, axisY, charset.tickDown());
			final String label = tick.getLabel();
			final int start = Math.max(0, x - (label.length() - 1) / 2);
			if (start > lastEnd) {
				grid.putString(start, axisY + 1, label);
				lastEnd = start + label.length();
			}
		}

		// Category labels, in front of their bars
		for (int c = 0; c < categories.size(); c++) {
			final int y = top + c * band + 2 + (slots - 1) / 2;
			grid.putString(axisX - categories.get(c).length(), y, categories.get(c));
		}

		// Bars, one under the other or stacked
		for (int b = 0; b < bars.size(); b++) {
			final List<Double> values = bars.get(b).getValues();
			for (int c = 0; c < Math.min(values.size(), categories.size()); c++) {
				if (values.get(c) == null)
					continue;
				final double from = stacked ? stack.getBase(b, c) : 0;
				final double to = stacked ? stack.getTop(b, c) : values.get(c);
				final double lo = toUnits(axis, Math.min(from, to), PLOT_WIDTH);
				final double hi = toUnits(axis, Math.max(from, to), PLOT_WIDTH);
				final int y = top + c * band + 2 + (stacked ? 0 : b);
				for (int k = 0; k < PLOT_WIDTH; k++) {
					final char ch = cell(lo, hi, k, b, false);
					if (ch != 0)
						grid.put(x0 + k, y, ch);
				}
			}
		}

		// The title of the value axis under its labels
		final String valueTitle = axis.getTitle();
		if (isEmpty(valueTitle) == false)
			grid.putString(x0 + Math.max(0, (PLOT_WIDTH - valueTitle.length()) / 2), axisY + 2, valueTitle);

		return grid.getLines();
	}

	// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	// Common

	// The position of a value along a plot of "size" cells, from the min of the axis.
	// The value is kept inside the axis, as in the graphic rendering
	private static double toUnits(ChartAxis axis, double value, int size) {
		final double range = axis.getMax() - axis.getMin();
		if (range <= 0)
			return 0;
		final double clamped = Math.max(axis.getMin(), Math.min(axis.getMax(), value));
		return (clamped - axis.getMin()) / range * size;
	}

	// What a bar going from lo to hi draws in the k-th cell (0 for nothing): the whole
	// cell when it covers most of it, else a half cell for the plain block
	private char cell(double lo, double hi, int k, int style, boolean vertical) {
		final double covered = Math.min(hi, k + 1) - Math.max(lo, k);
		final boolean halves = charset.hasHalves(style);
		if (covered >= (halves ? 0.75 : 0.5))
			return charset.fill(style);
		if (halves && covered >= 0.25) {
			final boolean fromStart = lo <= k + 1e-9;
			if (vertical)
				return fromStart ? charset.lowerHalf() : charset.upperHalf();
			return fromStart ? charset.leftHalf() : charset.rightHalf();
		}
		return 0;
	}

	// A label cut into lines of at most "width" characters, each cut line ending with "-"
	static List<String> wrap(String label, int width) {
		final List<String> result = new ArrayList<>();
		String rest = label;
		while (rest.length() > width && width > 2) {
			result.add(rest.substring(0, width - 1) + "-");
			rest = rest.substring(width - 1);
		}
		result.add(rest);
		return result;
	}

	private static boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}
}
