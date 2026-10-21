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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;

public class ChartDiagram extends TitledDiagram {

	public enum LegendPosition {
		NONE, LEFT, RIGHT, TOP, BOTTOM
	}

	public enum GridMode {
		OFF, MAJOR, BOTH
	}

	public enum StackMode {
		GROUPED, STACKED
	}

	public enum Orientation {
		VERTICAL, HORIZONTAL
	}

	private final List<String> xAxisLabels = new ArrayList<>();
	private String xAxisTitle;
	private Integer xAxisTickSpacing;
	private ChartAxis.LabelPosition xAxisLabelPosition = ChartAxis.LabelPosition.DEFAULT;
	private final ChartAxis xAxis = new ChartAxis(); // For numeric x-axis (horizontal bar charts)
	private final List<String> yAxisLabels = new ArrayList<>();
	private final List<ChartSeries> series = new ArrayList<>();
	private final ChartAxis yAxis = new ChartAxis();
	private ChartAxis y2Axis;
	private LegendPosition legendPosition = LegendPosition.NONE;
	private GridMode xGridMode = GridMode.OFF;
	private GridMode yGridMode = GridMode.OFF;
	private StackMode stackMode = StackMode.GROUPED;
	private Orientation orientation = Orientation.VERTICAL;
	private final List<ChartAnnotation> annotations = new ArrayList<>();

	public DiagramDescription getDescription() {
		return new DiagramDescription("Chart Diagram");
	}

	public ChartDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, DiagramType.CHART, null, preprocessing);
	}

	private void drawMe(UGraphic ug) {
		final ChartRenderer renderer = getRenderer();
		renderer.drawU(ug);
	}

	private ChartRenderer getRenderer() {
		// For horizontal orientation, use h-axis data where vertical mode uses y-axis,
		// and vice versa
		// User writes: v-axis [labels], h-axis numeric
		// For horizontal bars: xAxis=numeric (horizontal), yAxisLabels=categories
		// (vertical)
		// We need to pass to renderer properly based on what it expects
		final ChartAxis hAxis = getHAxis();
		if (orientation == Orientation.HORIZONTAL) {
			// For horizontal: pass h-axis numeric as yAxis, v-axis labels as xAxisLabels
			// This way bars grow along the correct axis
			return new ChartRenderer(getSkinParam(), getCategories(yAxisLabels), yAxis.getTitle(), null,
					xAxisLabelPosition, series, hAxis, hAxis, null, legendPosition, yGridMode, xGridMode, stackMode,
					orientation, annotations);
		}

		// For vertical: h-axis=categories (xAxisLabels), v-axis=numeric (yAxis)
		// Use xAxis title if available (for coordinate-pair mode), otherwise use
		// xAxisTitle
		final String hAxisTitle = (xAxis != null && xAxis.getTitle() != null && !xAxis.getTitle().isEmpty())
				? xAxis.getTitle()
				: xAxisTitle;
		// A numeric h-axis range (h-axis 0 --> 10) is kept as it is
		final List<String> categories = xAxis.isAutoScale() ? getCategories(xAxisLabels) : xAxisLabels;
		return new ChartRenderer(getSkinParam(), categories, hAxisTitle, xAxisTickSpacing, xAxisLabelPosition,
				getPlottedSeries(), hAxis, yAxis, y2Axis, legendPosition, xGridMode, yGridMode, stackMode, orientation,
				annotations);
	}

	// When no labels are given for the category axis (h-axis, or v-axis for a horizontal
	// chart), index-based data is drawn against the categories 1, 2, ..., n, n being the
	// number of values in the longest series, as if "h-axis [1, 2, ..., n]" had been written
	private List<String> getCategories(List<String> labels) {
		if (!labels.isEmpty() || series.isEmpty() || series.get(0).hasExplicitXValues())
			return labels;

		int count = 0;
		for (ChartSeries s : series)
			count = Math.max(count, s.getValues().size());

		final List<String> categories = new ArrayList<>();
		for (int i = 1; i <= count; i++)
			categories.add(Integer.toString(i));

		return categories;
	}

	// When no h-axis range is given, coordinate-pair data is drawn against a numeric h-axis
	// from the smallest to the largest x value, as if "h-axis min -> max" had been written.
	// If all x values are equal, the h-axis goes from x - 1 to x + 1
	private ChartAxis getHAxis() {
		if (xAxis.isAutoScale() == false || hasCoordinatePairs() == false)
			return xAxis;

		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		for (ChartSeries s : series)
			for (double x : s.getXValues()) {
				min = Math.min(min, x);
				max = Math.max(max, x);
			}

		if (min == max) {
			min = min - 1;
			max = max + 1;
		}
		return new ChartAxis(xAxis.getTitle(), min, max);
	}

	// When an h-axis range is given without h-axis labels, index-based values are spread evenly
	// across the range, the first at the start and the last at the end, as if they had been
	// written as coordinate pairs. The i-th value of every series is at the same x, n being the
	// number of values in the longest series. A single value is drawn in the middle of the range
	private List<ChartSeries> getPlottedSeries() {
		if (xAxis.isAutoScale() || xAxisLabels.isEmpty() == false || series.isEmpty() || hasCoordinatePairs())
			return series;

		int count = 0;
		for (ChartSeries s : series)
			count = Math.max(count, s.getValues().size());

		final double min = xAxis.getMin();
		final double max = xAxis.getMax();
		final List<ChartSeries> result = new ArrayList<>();
		for (ChartSeries s : series) {
			final List<Double> xValues = new ArrayList<>();
			for (int i = 0; i < s.getValues().size(); i++)
				xValues.add(count == 1 ? (min + max) / 2 : min + (max - min) * i / (count - 1));
			result.add(s.withXValues(xValues));
		}
		return result;
	}

	private boolean hasCoordinatePairs() {
		return series.isEmpty() == false && series.get(0).hasExplicitXValues();
	}

	// Command methods

	public CommandExecutionResult setXAxisLabels(List<String> labels) {
		if (labels.isEmpty() == false && xAxis.isAutoScale() && hasCoordinatePairs())
			return CommandExecutionResult.error(
					"Coordinate pair notation requires numeric h-axis (e.g., h-axis \"x\" -5 --> 5), not categorical labels");

		this.xAxisLabels.clear();
		this.xAxisLabels.addAll(labels);
		return CommandExecutionResult.ok();
	}

	public void setXAxisTitle(String title) {
		this.xAxisTitle = title;
	}

	public void setXAxisLabelPosition(ChartAxis.LabelPosition position) {
		this.xAxisLabelPosition = position;
	}

	public ChartAxis.LabelPosition getXAxisLabelPosition() {
		return xAxisLabelPosition;
	}

	public void setXAxisTickSpacing(Integer spacing) {
		this.xAxisTickSpacing = spacing;
	}

	public Integer getXAxisTickSpacing() {
		return xAxisTickSpacing;
	}

	public CommandExecutionResult setXAxis(String title, Double min, Double max) {
		// Coordinate pairs given without a range have not been checked against one
		if ((min != null || max != null) && xAxis.isAutoScale() && hasCoordinatePairs())
			return CommandExecutionResult.error("h-axis range must be given before coordinate pair data");

		if (title != null)
			xAxis.setTitle(title);
		if (min != null)
			xAxis.setMin(min);
		if (max != null)
			xAxis.setMax(max);
		return CommandExecutionResult.ok();
	}

	public ChartAxis getXAxis() {
		return xAxis;
	}

	public CommandExecutionResult setYAxis(String title, Double min, Double max) {
		if (title != null)
			yAxis.setTitle(title);
		if (min != null)
			yAxis.setMin(min);
		if (max != null)
			yAxis.setMax(max);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult setYAxisLabels(List<String> labels) {
		this.yAxisLabels.clear();
		this.yAxisLabels.addAll(labels);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult setY2Axis(String title, Double min, Double max) {
		if (y2Axis == null)
			y2Axis = new ChartAxis();
		if (title != null)
			y2Axis.setTitle(title);
		if (min != null)
			y2Axis.setMin(min);
		if (max != null)
			y2Axis.setMax(max);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult addSeries(ChartSeries series) {
		// Validation for coordinate-pair notation
		if (series.hasExplicitXValues()) {
			// Coordinate pairs only allowed for line and scatter charts
			if (series.getType() != ChartSeries.SeriesType.LINE && series.getType() != ChartSeries.SeriesType.SCATTER) {
				return CommandExecutionResult
						.error("Coordinate pair notation (x:y) is only supported for line and scatter charts");
			}

			// Coordinate pairs require numeric h-axis (not categorical labels)
			if (!xAxisLabels.isEmpty()) {
				return CommandExecutionResult.error(
						"Coordinate pair notation requires numeric h-axis (e.g., h-axis \"x\" -5 --> 5), not categorical labels");
			}

			// Without an h-axis range, the h-axis spans the x values (see getHAxis())
			if (xAxis.isAutoScale() == false && xAxis.getMax() == xAxis.getMin()) {
				return CommandExecutionResult
						.error("Coordinate pair notation requires explicit h-axis range (e.g., h-axis \"x\" -5 --> 5)");
			}

			// All series must use the same format
			if (!this.series.isEmpty()) {
				final boolean firstHasX = this.series.get(0).hasExplicitXValues();
				if (firstHasX != series.hasExplicitXValues()) {
					return CommandExecutionResult.error(
							"All series must use the same data format (either all coordinate pairs or all index-based)");
				}
			}

			// Validate x-coordinates fall within axis range
			for (double x : series.getXValues()) {
				if (xAxis.isAutoScale() == false && (x < xAxis.getMin() || x > xAxis.getMax())) {
					return CommandExecutionResult.error("X-coordinate " + x + " is outside h-axis range ["
							+ xAxis.getMin() + ", " + xAxis.getMax() + "]");
				}
			}

			// Auto-scale x-axis to include all x-values
			for (double x : series.getXValues()) {
				xAxis.includeValue(x);
			}
		} else {
			// Index-based mode: ensure consistency
			if (!this.series.isEmpty()) {
				final boolean firstHasX = this.series.get(0).hasExplicitXValues();
				if (firstHasX != series.hasExplicitXValues()) {
					return CommandExecutionResult.error(
							"All series must use the same data format (either all coordinate pairs or all index-based)");
				}
			}
		}

		this.series.add(series);

		// Auto-scale y-axes if needed
		final ChartAxis axis = series.isUseSecondaryAxis() && y2Axis != null ? y2Axis : yAxis;
		for (Double value : series.getValues()) {
			if (value != null)
				axis.includeValue(value);
		}

		return CommandExecutionResult.ok();
	}

	public List<String> getXAxisLabels() {
		return xAxisLabels;
	}

	public List<ChartSeries> getSeries() {
		return series;
	}

	public ChartAxis getYAxis() {
		return yAxis;
	}

	public ChartAxis getY2Axis() {
		return y2Axis;
	}

	public CommandExecutionResult setLegendPosition(LegendPosition position) {
		this.legendPosition = position;
		return CommandExecutionResult.ok();
	}

	public LegendPosition getLegendPosition() {
		return legendPosition;
	}

	public CommandExecutionResult setGridMode(GridMode mode) {
		this.xGridMode = mode;
		this.yGridMode = mode;
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult setXGridMode(GridMode mode) {
		this.xGridMode = mode;
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult setYGridMode(GridMode mode) {
		this.yGridMode = mode;
		return CommandExecutionResult.ok();
	}

	public GridMode getXGridMode() {
		return xGridMode;
	}

	public GridMode getYGridMode() {
		return yGridMode;
	}

	public CommandExecutionResult setStackMode(StackMode mode) {
		this.stackMode = mode;
		return CommandExecutionResult.ok();
	}

	public StackMode getStackMode() {
		return stackMode;
	}

	public CommandExecutionResult setOrientation(Orientation orientation) {
		this.orientation = orientation;
		return CommandExecutionResult.ok();
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public CommandExecutionResult addAnnotation(ChartAnnotation annotation) {
		this.annotations.add(annotation);
		return CommandExecutionResult.ok();
	}

	public List<ChartAnnotation> getAnnotations() {
		return annotations;
	}

	@Override
	public TextBlock getTextBlock(int num, FileFormatOption fileFormatOption) {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				drawMe(ug);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return getRenderer().calculateDimension(stringBounder);
			}
		};
	}
}
