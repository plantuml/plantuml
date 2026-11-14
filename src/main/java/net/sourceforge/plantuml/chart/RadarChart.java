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

import java.util.Collections;
import java.util.List;

/**
 * A radar once resolved: its categories around the circle, the value of each series for
 * each category (null when missing), and its radial scale fitted to the data. It is
 * built by {@link ChartDiagram#resolveRadar()} and read by the renderers.
 */
public final class RadarChart {

	private final List<String> categories;
	private final List<List<Double>> values;
	private final List<ChartSeries> series;
	private final ChartAxis axis;
	private final boolean circleGrid;
	private final ChartDiagram.LegendPosition legendPosition;

	RadarChart(List<String> categories, List<List<Double>> values, List<ChartSeries> series, ChartAxis axis,
			boolean circleGrid, ChartDiagram.LegendPosition legendPosition) {
		this.categories = Collections.unmodifiableList(categories);
		this.values = Collections.unmodifiableList(values);
		this.series = Collections.unmodifiableList(series);
		this.axis = axis;
		this.circleGrid = circleGrid;
		this.legendPosition = legendPosition;
	}

	public List<String> getCategories() {
		return categories;
	}

	/**
	 * For each series, its value for each category, or null when it has none.
	 */
	public List<List<Double>> getValues() {
		return values;
	}

	public List<ChartSeries> getSeries() {
		return series;
	}

	public ChartAxis getAxis() {
		return axis;
	}

	public boolean isCircleGrid() {
		return circleGrid;
	}

	public ChartDiagram.LegendPosition getLegendPosition() {
		return legendPosition;
	}
}
