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
 * A chart drawn on cartesian axes (bars, lines, areas, scatter), once every line is
 * parsed and every implicit value resolved: the categories (1, 2, ..., n when none is
 * given), the ranges of the value axes fitted to the data, the series as they are
 * plotted. It is built by {@link ChartDiagram#resolveCartesian()} and read by the
 * renderers, graphic or text, which do not need to know how it was written.
 * <p>
 * Whatever the orientation, the "category" axis holds the categories (or the numeric
 * h-axis of coordinate pairs), and the "value" axis the values: for a horizontal chart,
 * the categories go down the left side and the values along the bottom.
 */
public final class CartesianChart {

	private final ChartDiagram.Orientation orientation;
	private final List<String> categories;
	private final String categoryTitle;
	private final Integer categoryTickSpacing;
	private final ChartAxis.LabelPosition categoryLabelPosition;
	private final ChartAxis numericAxis;
	private final List<ChartSeries> series;
	private final ChartAxis valueAxis;
	private final ChartAxis valueAxis2;
	private final ChartDiagram.LegendPosition legendPosition;
	private final ChartDiagram.GridMode categoryGridMode;
	private final ChartDiagram.GridMode valueGridMode;
	private final ChartDiagram.StackMode stackMode;
	private final List<ChartAnnotation> annotations;

	CartesianChart(ChartDiagram.Orientation orientation, List<String> categories, String categoryTitle,
			Integer categoryTickSpacing, ChartAxis.LabelPosition categoryLabelPosition, ChartAxis numericAxis,
			List<ChartSeries> series, ChartAxis valueAxis, ChartAxis valueAxis2,
			ChartDiagram.LegendPosition legendPosition, ChartDiagram.GridMode categoryGridMode,
			ChartDiagram.GridMode valueGridMode, ChartDiagram.StackMode stackMode, List<ChartAnnotation> annotations) {
		this.orientation = orientation;
		this.categories = Collections.unmodifiableList(categories);
		this.categoryTitle = categoryTitle;
		this.categoryTickSpacing = categoryTickSpacing;
		this.categoryLabelPosition = categoryLabelPosition;
		this.numericAxis = numericAxis;
		this.series = Collections.unmodifiableList(series);
		this.valueAxis = valueAxis;
		this.valueAxis2 = valueAxis2;
		this.legendPosition = legendPosition;
		this.categoryGridMode = categoryGridMode;
		this.valueGridMode = valueGridMode;
		this.stackMode = stackMode;
		this.annotations = Collections.unmodifiableList(annotations);
	}

	public ChartDiagram.Orientation getOrientation() {
		return orientation;
	}

	public boolean isHorizontal() {
		return orientation == ChartDiagram.Orientation.HORIZONTAL;
	}

	/**
	 * The labels of the categories, empty when the data are coordinate pairs.
	 */
	public List<String> getCategories() {
		return categories;
	}

	public String getCategoryTitle() {
		return categoryTitle;
	}

	public Integer getCategoryTickSpacing() {
		return categoryTickSpacing;
	}

	public ChartAxis.LabelPosition getCategoryLabelPosition() {
		return categoryLabelPosition;
	}

	/**
	 * The numeric h-axis, used when the data are coordinate pairs (or are spread over an
	 * h-axis range).
	 */
	public ChartAxis getNumericAxis() {
		return numericAxis;
	}

	public List<ChartSeries> getSeries() {
		return series;
	}

	public boolean hasCoordinatePairs() {
		return series.isEmpty() == false && series.get(0).hasExplicitXValues();
	}

	public ChartAxis getValueAxis() {
		return valueAxis;
	}

	/**
	 * The secondary value axis (v2-axis), or null.
	 */
	public ChartAxis getValueAxis2() {
		return valueAxis2;
	}

	/**
	 * The value axis a series is drawn against.
	 */
	public ChartAxis getValueAxis(ChartSeries s) {
		return s.isUseSecondaryAxis() && valueAxis2 != null ? valueAxis2 : valueAxis;
	}

	public ChartDiagram.LegendPosition getLegendPosition() {
		return legendPosition;
	}

	public ChartDiagram.GridMode getCategoryGridMode() {
		return categoryGridMode;
	}

	public ChartDiagram.GridMode getValueGridMode() {
		return valueGridMode;
	}

	public ChartDiagram.StackMode getStackMode() {
		return stackMode;
	}

	public List<ChartAnnotation> getAnnotations() {
		return annotations;
	}
}
