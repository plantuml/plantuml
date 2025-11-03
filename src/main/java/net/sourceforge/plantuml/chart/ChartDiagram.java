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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class ChartDiagram extends UmlDiagram {

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
	private final ChartAxis xAxis = new ChartAxis();  // For numeric x-axis (horizontal bar charts)
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
		super(source, UmlDiagramType.CHART, null, preprocessing);
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				drawMe(ug);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return getRenderer().calculateDimension(stringBounder);
			}
		};
	}

	private void drawMe(UGraphic ug) {
		final ChartRenderer renderer = getRenderer();
		renderer.drawU(ug);
	}

	private ChartRenderer getRenderer() {
		// For horizontal orientation, use h-axis data where vertical mode uses y-axis, and vice versa
		// User writes: v-axis [labels], h-axis numeric
		// For horizontal bars: xAxis=numeric (horizontal), yAxisLabels=categories (vertical)
		// We need to pass to renderer properly based on what it expects
		if (orientation == Orientation.HORIZONTAL) {
			// For horizontal: pass h-axis numeric as yAxis, v-axis labels as xAxisLabels
			// This way bars grow along the correct axis
			return new ChartRenderer(getSkinParam(), yAxisLabels, yAxis.getTitle(), null, series, xAxis, null, legendPosition, yGridMode, xGridMode, stackMode, orientation, annotations);
		}

		// For vertical: h-axis=categories (xAxisLabels), v-axis=numeric (yAxis)
		return new ChartRenderer(getSkinParam(), xAxisLabels, xAxisTitle, xAxisTickSpacing, series, yAxis, y2Axis, legendPosition, xGridMode, yGridMode, stackMode, orientation, annotations);
	}

	// Command methods

	public CommandExecutionResult setXAxisLabels(List<String> labels) {
		this.xAxisLabels.clear();
		this.xAxisLabels.addAll(labels);
		return CommandExecutionResult.ok();
	}

	public void setXAxisTitle(String title) {
		this.xAxisTitle = title;
	}

	public void setXAxisTickSpacing(Integer spacing) {
		this.xAxisTickSpacing = spacing;
	}

	public Integer getXAxisTickSpacing() {
		return xAxisTickSpacing;
	}

	public CommandExecutionResult setXAxis(String title, Double min, Double max) {
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
		this.series.add(series);

		// Auto-scale axes if needed
		final ChartAxis axis = series.isUseSecondaryAxis() && y2Axis != null ? y2Axis : yAxis;
		for (double value : series.getValues()) {
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
}
