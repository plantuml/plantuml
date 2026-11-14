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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.chart.txt.ChartTxt;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.NaturalSpline;
import net.sourceforge.plantuml.klimt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.security.SecurityUtils;

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

	// The family of a keyword: a chart is either drawn on cartesian axes, or is a pie, or a
	// radar. POLAR is a keyword shared by a pie and a radar (a-axis)
	public enum Kind {
		CARTESIAN, PIE, RADAR, POLAR
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
	// Pie and radar: the categories around the circle (a-axis), and the radial scale (r-axis)
	private final List<String> angularLabels = new ArrayList<>();
	private final ChartAxis radialAxis = new ChartAxis();
	private boolean radarCircleGrid;
	private Kind kind;
	private String kindKeyword;

	public DiagramDescription getDescription() {
		return new DiagramDescription("Chart Diagram");
	}

	public ChartDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, DiagramType.CHART, null, preprocessing);
	}

	private void drawMe(UGraphic ug) {
		if (kind == Kind.PIE)
			getPieRenderer().drawU(ug);
		else if (kind == Kind.RADAR)
			getRadarRenderer().drawU(ug);
		else
			getRenderer().drawU(ug);
	}

	private XDimension2D calculateDimension(StringBounder stringBounder) {
		if (kind == Kind.PIE)
			return getPieRenderer().calculateDimension(stringBounder);
		if (kind == Kind.RADAR)
			return getRadarRenderer().calculateDimension(stringBounder);
		return getRenderer().calculateDimension(stringBounder);
	}

	private PieRenderer getPieRenderer() {
		return new PieRenderer(getSkinParam(), resolvePie());
	}

	private RadarRenderer getRadarRenderer() {
		return new RadarRenderer(getSkinParam(), resolveRadar());
	}

	/**
	 * The pie, its slices resolved: see {@link PieChart}.
	 */
	public PieChart resolvePie() {
		return new PieChart(series.get(0), angularLabels, legendPosition);
	}

	/**
	 * The radar, its categories, values and scale resolved: see {@link RadarChart}.
	 */
	public RadarChart resolveRadar() {
		return new RadarChart(getRadarCategories(), getRadarValues(), series, getRadialAxis(), radarCircleGrid,
				legendPosition);
	}

	public Kind getKind() {
		return kind;
	}

	// The categories around a radar: the a-axis labels, else the keys of the first series
	// with keys, then the keys of the next ones not seen yet, else 1, 2, ..., n
	private List<String> getRadarCategories() {
		if (angularLabels.isEmpty() == false)
			return angularLabels;
		final List<String> result = new ArrayList<>();
		for (ChartSeries s : series)
			if (s.hasKeys())
				for (String key : s.getKeys())
					if (result.contains(key) == false)
						result.add(key);
		int count = 0;
		for (ChartSeries s : series)
			if (s.hasKeys() == false)
				count = Math.max(count, s.getValues().size());
		for (int i = result.size() + 1; i <= count; i++)
			result.add(Integer.toString(i));
		return result;
	}

	// The values of each radar series, one per category (null when missing)
	private List<List<Double>> getRadarValues() {
		final List<String> categories = getRadarCategories();
		final List<List<Double>> result = new ArrayList<>();
		for (ChartSeries s : series) {
			final List<Double> values = new ArrayList<>();
			for (int i = 0; i < categories.size(); i++)
				values.add(null);
			for (int i = 0; i < s.getValues().size(); i++) {
				final int index = s.hasKeys() ? categories.indexOf(s.getKeys().get(i)) : i;
				if (index >= 0 && index < values.size())
					values.set(index, s.getValues().get(i));
			}
			result.add(values);
		}
		return result;
	}

	// Without an r-axis range, the radial scale goes from 0 to a round value above the data
	private ChartAxis getRadialAxis() {
		if (radialAxis.isAutoScale() == false)
			return radialAxis;
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		for (ChartSeries s : series)
			for (Double value : s.getValues())
				if (value != null) {
					min = Math.min(min, value);
					max = Math.max(max, value);
				}
		if (min > max)
			return radialAxis;
		return radialAxis.withNiceRange(min, max);
	}

	private ChartRenderer getRenderer() {
		return new ChartRenderer(getSkinParam(), resolveCartesian());
	}

	/**
	 * The chart as it is drawn on cartesian axes, every implicit value resolved: see
	 * {@link CartesianChart}.
	 */
	public CartesianChart resolveCartesian() {
		final ChartAxis hAxis = getHAxis();
		if (orientation == Orientation.HORIZONTAL) {
			// The categories are given by the v-axis and go down the left side, the
			// values go along the bottom, against the h-axis range. The grid modes are
			// swapped: "v-axis [A, B] grid" gives one line per category
			final ChartAxis valueAxis = hasCoordinatePairs() ? hAxis : getValueAxis(xAxis, false);
			return new CartesianChart(orientation, getCategories(yAxisLabels), yAxis.getTitle(), null,
					xAxisLabelPosition, hAxis, series, valueAxis, null, legendPosition, yGridMode, xGridMode,
					stackMode, annotations);
		}

		// Vertical: the categories (h-axis) along the bottom, the values (v-axis) on the
		// left. The h-axis title is the one given with a range, else with the labels
		final String hAxisTitle = (xAxis != null && xAxis.getTitle() != null && !xAxis.getTitle().isEmpty())
				? xAxis.getTitle()
				: xAxisTitle;
		// A numeric h-axis range (h-axis 0 --> 10) is kept as it is
		final List<String> categories = xAxis.isAutoScale() ? getCategories(xAxisLabels) : xAxisLabels;
		return new CartesianChart(orientation, categories, hAxisTitle, xAxisTickSpacing, xAxisLabelPosition, hAxis,
				getPlottedSeries(), getValueAxis(yAxis, false),
				y2Axis == null ? null : getValueAxis(y2Axis, true), legendPosition, xGridMode, yGridMode, stackMode,
				annotations);
	}

	// When no range is given for a value axis, it is fitted to the values drawn against it
	// (see ChartAxis.withNiceRange). This is done here, once every line is parsed, rather
	// than while the series are added: a "v2-axis" line may come after the series using it,
	// and stacked bars and areas reach the sum of their values, not each value.
	// Series are drawn against y2Axis when they ask for it and it exists, as ChartRenderer does
	private ChartAxis getValueAxis(ChartAxis axis, boolean secondary) {
		if (axis.isAutoScale() == false)
			return axis;

		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		// Stacked bars and areas reach the top of their stack (see StackLayout)
		final List<ChartSeries> bars = new ArrayList<>();
		final List<ChartSeries> areas = new ArrayList<>();
		for (ChartSeries s : series)
			if ((s.isUseSecondaryAxis() && y2Axis != null) == secondary)
				if (s.getType() == ChartSeries.SeriesType.BAR)
					bars.add(s);
				else if (s.getType() == ChartSeries.SeriesType.AREA)
					areas.add(s);
		final StackLayout barStack = StackLayout.bars(bars);
		final StackLayout areaStack = StackLayout.areas(areas);
		for (ChartSeries s : series) {
			if ((s.isUseSecondaryAxis() && y2Axis != null) != secondary)
				continue;
			final List<Double> values = s.getValues();
			for (int i = 0; i < values.size(); i++) {
				final Double value = values.get(i);
				if (value == null)
					continue;
				final double reached;
				if (s.getType() == ChartSeries.SeriesType.AREA)
					reached = areaStack.getTop(areas.indexOf(s), i);
				else if (s.getType() == ChartSeries.SeriesType.BAR && stackMode == StackMode.STACKED)
					reached = barStack.getTop(bars.indexOf(s), i);
				else
					reached = value;
				min = Math.min(min, reached);
				max = Math.max(max, reached);
			}
			if (s.getType() == ChartSeries.SeriesType.LINE && s.isInterpolate())
				for (XCubicCurve2D curve : NaturalSpline.through(valuePoints(s)))
					for (int k = 1; k < CURVE_SAMPLES; k++) {
						final double y = bezierY(curve, (double) k / CURVE_SAMPLES);
						min = Math.min(min, y);
						max = Math.max(max, y);
					}
		}
		if (min > max)
			return axis;

		// Annotations are placed against the primary value axis. An arrow is drawn
		// downwards from its text only when it has about 55 px (text + 40) above the
		// point, that is 19% of the 300 px plot, otherwise it is flipped below the point
		// (see ChartRenderer.drawAnnotations). Asking for the point plus 25% of its
		// distance to the bottom keeps the arrow above it even when the point is the
		// maximum, as in annotation "Peak" at (B, 40) over a bar of 40
		if (secondary == false)
			for (ChartAnnotation annotation : annotations) {
				final double y = annotation.getYPosition();
				min = Math.min(min, y);
				max = Math.max(max, y);
				if (annotation.isShowArrow() && orientation == Orientation.VERTICAL)
					max = Math.max(max, y + 0.25 * (y - Math.min(0, min)));
			}

		return axis.withNiceRange(min, max);
	}

	// An interpolated line is a natural spline, which overshoots its points: 38 between 12
	// and 10 peaks near 41. Its extent is sampled so that the curve stays inside the axis.
	// The spline is computed on values rather than pixels: it does not change under an
	// affine change of x or y, so it is the same curve as the one LineRenderer draws
	private static final int CURVE_SAMPLES = 32;

	// The points of a series in value space (x = explicit x, or index), missing values left out
	private static List<XPoint2D> valuePoints(ChartSeries s) {
		final List<XPoint2D> result = new ArrayList<>();
		final List<Double> values = s.getValues();
		for (int i = 0; i < values.size(); i++)
			if (values.get(i) != null)
				result.add(new XPoint2D(s.hasExplicitXValues() ? s.getXValues().get(i) : i, values.get(i)));
		return result;
	}

	private static double bezierY(XCubicCurve2D c, double t) {
		final double u = 1 - t;
		return u * u * u * c.getY1() + 3 * u * u * t * c.getCtrlY1() + 3 * u * t * t * c.getCtrlY2()
				+ t * t * t * c.getY2();
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

	/**
	 * Checks that a keyword can be used with the ones already written: a pie, a radar and the
	 * cartesian keywords (bar, h-axis, orientation...) cannot be mixed. Returns an error, or
	 * null when the keyword is accepted.
	 */
	public CommandExecutionResult checkKind(String keyword, Kind wanted) {
		if (kind == null || kind == wanted || wanted == Kind.POLAR && (kind == Kind.PIE || kind == Kind.RADAR)) {
			if (kind == null)
				kindKeyword = keyword;
			if (kind == null || kind == Kind.POLAR)
				kind = wanted;
			return null;
		}
		if (kind == Kind.POLAR && (wanted == Kind.PIE || wanted == Kind.RADAR)) {
			kind = wanted;
			kindKeyword = keyword;
			return null;
		}
		return CommandExecutionResult.error("\"" + keyword + "\" cannot be used with \"" + kindKeyword + "\"");
	}

	public CommandExecutionResult setAngularLabels(List<String> labels) {
		final CommandExecutionResult error = checkKind("a-axis", Kind.POLAR);
		if (error != null)
			return error;
		for (ChartSeries s : series) {
			final CommandExecutionResult unknown = checkKeys(s, labels);
			if (unknown != null)
				return unknown;
		}
		this.angularLabels.clear();
		this.angularLabels.addAll(labels);
		return CommandExecutionResult.ok();
	}

	// The keys of a radar series must be categories of the a-axis, when it is given
	private static CommandExecutionResult checkKeys(ChartSeries s, List<String> labels) {
		if (s.getType() != ChartSeries.SeriesType.RADAR || s.hasKeys() == false || labels.isEmpty())
			return null;
		for (String key : s.getKeys())
			if (labels.contains(key) == false)
				return CommandExecutionResult.error("\"" + key + "\" is not in a-axis");
		return null;
	}

	public CommandExecutionResult setRadialAxis(Double min, Double max, Map<Double, String> customTicks,
			Double tickSpacing, Boolean circleGrid) {
		final CommandExecutionResult error = checkKind("r-axis", Kind.RADAR);
		if (error != null)
			return error;
		if (min != null && max != null && min >= max)
			return CommandExecutionResult.error("r-axis range must go from a lower to a higher value");
		if (min != null)
			radialAxis.setMin(min);
		if (max != null)
			radialAxis.setMax(max);
		if (customTicks != null)
			radialAxis.setCustomTicks(customTicks);
		if (tickSpacing != null)
			radialAxis.setTickSpacing(tickSpacing);
		if (circleGrid != null)
			radarCircleGrid = circleGrid;
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult addPolarSeries(ChartSeries s) {
		if (s.getType() == ChartSeries.SeriesType.PIE) {
			if (series.isEmpty() == false)
				return CommandExecutionResult.error("Only one pie is allowed in a chart");
			boolean positive = false;
			for (Double value : s.getValues())
				if (value != null) {
					if (value < 0)
						return CommandExecutionResult.error("A pie cannot have a negative value: " + value);
					if (value > 0)
						positive = true;
				}
			if (positive == false)
				return CommandExecutionResult.error("A pie needs at least one value greater than 0");
		} else {
			final CommandExecutionResult unknown = checkKeys(s, angularLabels);
			if (unknown != null)
				return unknown;
		}
		series.add(s);
		return CommandExecutionResult.ok();
	}

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
		final boolean polar = series.getType() == ChartSeries.SeriesType.PIE
				|| series.getType() == ChartSeries.SeriesType.RADAR;
		final Kind seriesKind = series.getType() == ChartSeries.SeriesType.PIE ? Kind.PIE
				: series.getType() == ChartSeries.SeriesType.RADAR ? Kind.RADAR : Kind.CARTESIAN;
		final CommandExecutionResult error = checkKind(series.getType().name().toLowerCase(Locale.ROOT), seriesKind);
		if (error != null)
			return error;
		if (polar)
			return addPolarSeries(series);

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

	// ATXT and UTXT: see ChartTxt
	@Override
	protected ImageData exportTxt(OutputStream os, int index, FileFormat fileFormat) throws IOException {
		final PrintStream ps = SecurityUtils.createPrintStream(os, true, StandardCharsets.UTF_8);
		for (String line : ChartTxt.getLines(this, fileFormat == FileFormat.UTXT))
			ps.println(line);
		ps.flush();
		return ImageDataSimple.ok();
	}

	@Override
	public TextBlock getTextBlock(int num, FileFormatOption fileFormatOption) {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				drawMe(ug);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return ChartDiagram.this.calculateDimension(stringBounder);
			}
		};
	}
}
