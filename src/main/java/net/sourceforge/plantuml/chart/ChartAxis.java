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

import java.util.LinkedHashMap;
import java.util.Map;

public class ChartAxis {

	private String title;
	private double min;
	private double max;
	private boolean autoScale;
	private Map<Double, String> customTicks;
	private Double tickSpacing;

	public ChartAxis() {
		this.title = "";
		this.min = 0;
		this.max = 100;
		this.autoScale = true;
		this.customTicks = null;
		this.tickSpacing = null;
	}

	public ChartAxis(String title, double min, double max) {
		this.title = title;
		this.min = min;
		this.max = max;
		this.autoScale = false;
		this.customTicks = null;
		this.tickSpacing = null;
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
	 * Update axis range to include the given value
	 */
	public void includeValue(double value) {
		if (autoScale) {
			if (value < min)
				min = value;
			if (value > max)
				max = value;
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
}
