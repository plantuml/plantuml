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

	private final List<String> xAxisLabels = new ArrayList<>();
	private final List<ChartSeries> series = new ArrayList<>();
	private final ChartAxis yAxis = new ChartAxis();
	private ChartAxis y2Axis;
	private LegendPosition legendPosition = LegendPosition.NONE;

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
		return new ChartRenderer(getSkinParam(), xAxisLabels, series, yAxis, y2Axis, legendPosition);
	}

	// Command methods

	public CommandExecutionResult setXAxisLabels(List<String> labels) {
		this.xAxisLabels.clear();
		this.xAxisLabels.addAll(labels);
		return CommandExecutionResult.ok();
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
}
