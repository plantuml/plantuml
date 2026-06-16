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
 *
 */
package net.sourceforge.plantuml.gantt;

import java.util.Locale;

import net.sourceforge.plantuml.gantt.core.Task;
import net.sourceforge.plantuml.gantt.core.TaskImpl;
import net.sourceforge.plantuml.gantt.data.GanttModelData;
import net.sourceforge.plantuml.gantt.data.TaskDrawRegistryData;
import net.sourceforge.plantuml.gantt.data.TimeBoundsData;
import net.sourceforge.plantuml.gantt.data.TimelineStyleData;
import net.sourceforge.plantuml.gantt.draw.TaskDraw;
import net.sourceforge.plantuml.gantt.time.TimePoint;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

/**
 * Draws, on the left side of a Gantt diagram, a textual table giving for each
 * task its code, its start date, its end date and its duration (in days). Each
 * row is vertically aligned with the corresponding task bar.
 */
public final class GanttTaskTable {

	private static final double CELL_PADDING = 5;

	private final GanttModelData modelData;
	private final TimeBoundsData timeBounds;
	private final TaskDrawRegistryData drawRegistry;
	private final TimelineStyleData timelineStyle;
	private final Locale locale;
	private final double headerHeight;

	// One boundary per column edge: columnEdges[0] is the left border, the last
	// entry is the right border. There are 4 columns, so 5 edges.
	private final double[] columnEdges;

	public GanttTaskTable(GanttModelData modelData, TimeBoundsData timeBounds, TaskDrawRegistryData drawRegistry,
			TimelineStyleData timelineStyle, double headerHeight, Locale locale, StringBounder stringBounder) {
		this.modelData = modelData;
		this.timeBounds = timeBounds;
		this.drawRegistry = drawRegistry;
		this.timelineStyle = timelineStyle;
		this.headerHeight = headerHeight;
		this.locale = locale;

		double wCode = widthOf(stringBounder, "Code");
		double wStart = widthOf(stringBounder, "Start");
		double wEnd = widthOf(stringBounder, "End");
		double wDuration = widthOf(stringBounder, "Duration");

		for (Task task : modelData.getTasks()) {
			if (isDrawable(task) == false)
				continue;

			wCode = Math.max(wCode, widthOf(stringBounder, codeOf(task)));
			wStart = Math.max(wStart, widthOf(stringBounder, startOf(task)));
			wEnd = Math.max(wEnd, widthOf(stringBounder, endOf(task)));
			wDuration = Math.max(wDuration, widthOf(stringBounder, durationOf(task)));
		}

		final double colCode = wCode + 2 * CELL_PADDING;
		final double colStart = wStart + 2 * CELL_PADDING;
		final double colEnd = wEnd + 2 * CELL_PADDING;
		final double colDuration = wDuration + 2 * CELL_PADDING;

		this.columnEdges = new double[5];
		this.columnEdges[0] = 0;
		this.columnEdges[1] = this.columnEdges[0] + colCode;
		this.columnEdges[2] = this.columnEdges[1] + colStart;
		this.columnEdges[3] = this.columnEdges[2] + colEnd;
		this.columnEdges[4] = this.columnEdges[3] + colDuration;
	}

	public double getWidth() {
		return columnEdges[columnEdges.length - 1];
	}

	public void drawU(UGraphic ug, double totalHeightWithoutFooter) {
		final StringBounder stringBounder = ug.getStringBounder();
		final HColor line = timelineStyle.getLineColor();

		drawGrid(ug.apply(line), totalHeightWithoutFooter);

		drawRow(ug, headerHeight / 2, "Code", "Start", "End", "Duration");

		for (Task task : modelData.getTasks()) {
			if (isDrawable(task) == false)
				continue;

			final TaskDraw draw = drawRegistry.getTaskDraw(task);
			final double yCenter = headerHeight + draw.getY(stringBounder).getCurrentValue()
					+ draw.getFullHeightTask(stringBounder) / 2;
			drawRow(ug, yCenter, codeOf(task), startOf(task), endOf(task), durationOf(task));
		}
	}

	private void drawGrid(UGraphic ug, double totalHeightWithoutFooter) {
		final double width = getWidth();

		ug.draw(ULine.hline(width));
		ug.apply(UTranslate.dy(headerHeight)).draw(ULine.hline(width));
		ug.apply(UTranslate.dy(totalHeightWithoutFooter)).draw(ULine.hline(width));

		for (double x : columnEdges)
			ug.apply(UTranslate.dx(x)).draw(ULine.vline(totalHeightWithoutFooter));
	}

	private void drawRow(UGraphic ug, double yCenter, String code, String start, String end, String duration) {
		drawCell(ug, columnEdges[0], yCenter, code);
		drawCell(ug, columnEdges[1], yCenter, start);
		drawCell(ug, columnEdges[2], yCenter, end);
		drawCell(ug, columnEdges[3], yCenter, duration);
	}

	private void drawCell(UGraphic ug, double xLeft, double yCenter, String text) {
		final StringBounder stringBounder = ug.getStringBounder();
		final TextBlock block = createTextBlock(text);
		final XDimension2D dim = block.calculateDimension(stringBounder);
		block.drawU(ug.apply(new UTranslate(xLeft + CELL_PADDING, yCenter - dim.getHeight() / 2)));
	}

	private boolean isDrawable(Task task) {
		if (task instanceof TaskImpl == false)
			return false;

		if (timeBounds.isHidden(task))
			return false;

		if (drawRegistry.getTaskDraw(task) == null)
			return false;

		return true;
	}

	private String codeOf(Task task) {
		return task.getCode().getDisplay();
	}

	private String startOf(Task task) {
		return task.getStart().toStringShort(locale);
	}

	private String endOf(Task task) {
		return task.getEndMinusOneDayTOBEREMOVED().toStringShort(locale);
	}

	private String durationOf(Task task) {
		final TimePoint start = task.getStart();
		final TimePoint end = task.getEnd();
		final int days = end.getAbsoluteDayNum() - start.getAbsoluteDayNum();
		if (days <= 1)
			return "1 day";

		return days + " days";
	}

	private double widthOf(StringBounder stringBounder, String text) {
		return createTextBlock(text).calculateDimension(stringBounder).getWidth();
	}

	private TextBlock createTextBlock(String text) {
		return Display.getWithNewlines(timelineStyle.getPragma(), text).create(getFontConfiguration(),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
	}

	private Style getStyle() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.ganttDiagram, SName.timeline)
				.getMergedStyle(timelineStyle.getSkinParam().getCurrentStyleBuilder());
	}

	private FontConfiguration getFontConfiguration() {
		return getStyle().getFontConfiguration(timelineStyle.getColorSet());
	}

}
