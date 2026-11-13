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

import java.util.Map;

public class ChartAxis {

	public enum LabelPosition {
		DEFAULT, // Vertical for v-axis, horizontal below for h-axis
		TOP,     // Horizontal at top for v-axis
		RIGHT    // At right for h-axis
	}

	private String title;
	private double min;
	private double max;
	private boolean autoScale;
	private Map<Double, String> customTicks;
	private Double tickSpacing;
	private LabelPosition labelPosition;

	public ChartAxis() {
		this.title = "";
		this.min = 0;
		this.max = 100;
		this.autoScale = true;
		this.customTicks = null;
		this.tickSpacing = null;
		this.labelPosition = LabelPosition.DEFAULT;
	}

	public ChartAxis(String title, double min, double max) {
		this.title = title;
		this.min = min;
		this.max = max;
		this.autoScale = false;
		this.customTicks = null;
		this.tickSpacing = null;
		this.labelPosition = LabelPosition.DEFAULT;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
		this.autoScale = false;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
		this.autoScale = false;
	}

	public boolean isAutoScale() {
		return autoScale;
	}

	public void setAutoScale(boolean autoScale) {
		this.autoScale = autoScale;
	}

	/**
	 * Convert a data value to pixel coordinate
	 */
	public double valueToPixel(double value, double pixelMin, double pixelMax) {
		if (max == min)
			return pixelMin;
		return pixelMin + (value - min) / (max - min) * (pixelMax - pixelMin);
	}

	/**
	 * A copy of this axis (title, ticks, label position) with the given range.
	 */
	public ChartAxis withRange(double newMin, double newMax) {
		final ChartAxis result = new ChartAxis(title, newMin, newMax);
		result.customTicks = customTicks;
		result.tickSpacing = tickSpacing;
		result.labelPosition = labelPosition;
		return result;
	}

	// Multipliers of a power of ten accepted as a tick step. 1.5 and 2.5 are left out on
	// purpose: below 1 they need two decimals (0.25), which tick labels do not show
	private static final double[] NICE_STEPS = { 1, 2, 3, 4, 5, 6, 8 };

	// Number of intervals drawn by the automatic ticks of the value axis
	private static final int TICK_INTERVALS = 5;

	/**
	 * A copy of this axis whose range is the smallest "round" range holding every value
	 * between dataMin and dataMax, and 0. The range is split into five intervals by the
	 * automatic ticks, so it is chosen as min + 5 * step, step being 1, 2, 3, 4, 5, 6 or
	 * 8 times a power of ten, and min a multiple of step: 0 always falls on a tick.
	 *
	 * Examples: values up to 38 give 0 -> 40, up to 138 give 0 -> 150, from -20 to 70
	 * give -20 -> 80. When there is no value at all, the axis is left as it is (0 -> 100).
	 */
	public ChartAxis withNiceRange(double dataMin, double dataMax) {
		final double lo = Math.min(0, dataMin);
		final double hi = Math.max(0, dataMax);
		if (lo == hi)
			return withRange(min, max);

		double magnitude = Math.pow(10, Math.floor(Math.log10((hi - lo) / TICK_INTERVALS)) - 1);
		// Termination: once step >= hi - lo, any multiple min <= lo gives min + 5 * step > hi
		while (true) {
			for (double nice : NICE_STEPS) {
				final double step = nice * magnitude;
				final double start = Math.floor(lo / step) * step;
				if (start + TICK_INTERVALS * step >= hi)
					return withRange(start, start + TICK_INTERVALS * step);
			}
			magnitude *= 10;
		}
	}

	/**
	 * Get custom tick labels map
	 */
	public Map<Double, String> getCustomTicks() {
		return customTicks;
	}

	/**
	 * Set custom tick labels. The map keys are tick values and values are labels.
	 */
	public void setCustomTicks(Map<Double, String> customTicks) {
		this.customTicks = customTicks;
	}

	/**
	 * Check if custom ticks are defined
	 */
	public boolean hasCustomTicks() {
		return customTicks != null && !customTicks.isEmpty();
	}

	/**
	 * Get tick spacing value
	 */
	public Double getTickSpacing() {
		return tickSpacing;
	}

	/**
	 * Set tick spacing. The value represents the interval between ticks.
	 */
	public void setTickSpacing(Double tickSpacing) {
		this.tickSpacing = tickSpacing;
	}

	/**
	 * Check if custom tick spacing is defined
	 */
	public boolean hasTickSpacing() {
		return tickSpacing != null && tickSpacing > 0;
	}

	/**
	 * Get label position
	 */
	public LabelPosition getLabelPosition() {
		return labelPosition;
	}

	/**
	 * Set label position
	 */
	public void setLabelPosition(LabelPosition labelPosition) {
		this.labelPosition = labelPosition;
	}
}
