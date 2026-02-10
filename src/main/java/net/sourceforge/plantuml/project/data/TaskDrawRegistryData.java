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
package net.sourceforge.plantuml.project.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.project.core.Resource;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskGroup;
import net.sourceforge.plantuml.project.core.TaskImpl;
import net.sourceforge.plantuml.project.core.TaskSeparator;
import net.sourceforge.plantuml.project.draw.FingerPrint;
import net.sourceforge.plantuml.project.draw.ResourceDraw;
import net.sourceforge.plantuml.project.draw.ResourceDrawNumbers;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.draw.TaskDrawDiamond;
import net.sourceforge.plantuml.project.draw.TaskDrawGroup;
import net.sourceforge.plantuml.project.draw.TaskDrawRegular;
import net.sourceforge.plantuml.project.draw.TaskDrawSeparator;
import net.sourceforge.plantuml.project.draw.header.TimeHeader;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealOrigin;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.style.ISkinParam;

/**
 * Value object containing the mapping from Task to TaskDraw.
 */
public class TaskDrawRegistryData {

	private final RealOrigin origin = RealUtils.createOrigin();
	private final Map<Task, TaskDraw> draws = new LinkedHashMap<>();
	private double totalHeightWithoutFooter;

	public double getTotalHeightWithoutFooter() {
		return totalHeightWithoutFooter;
	}

	public RealOrigin getOrigin() {
		return origin;
	}

	public TaskDraw getTaskDraw(Task task) {
		return draws.get(task);
	}

	// Mutators

	public void putTaskDraw(Task task, TaskDraw draw) {
		draws.put(task, draw);
	}

	// Internal access

	private double computeBottomY(StringBounder stringBounder) {
		double result = 0;
		for (TaskDraw td : draws.values())
			result = Math.max(result, td.getY(stringBounder).getCurrentValue() + td.getHeightMax(stringBounder));

		return result;
	}

	private void resolveNoteOverlaps(StringBounder stringBounder) {
		final List<TaskDraw> notes = new ArrayList<>();
		for (TaskDraw td : draws.values()) {
			final FingerPrint taskPrint = td.getFingerPrint(stringBounder);
			final FingerPrint fingerPrintNote = td.getFingerPrintNote(stringBounder);

			if (td.getTrueRow() == null)
				for (TaskDraw note : notes) {
					final FingerPrint otherNote = note.getFingerPrintNote(stringBounder);
					final double deltaY = otherNote.overlap(taskPrint);
					if (deltaY > 0) {
						final Real bottom = note.getY(stringBounder).addAtLeast(note.getHeightMax(stringBounder));
						td.getY(stringBounder).ensureBiggerThan(bottom);
						getOrigin().compileNow();
					}

				}

			if (fingerPrintNote != null)
				notes.add(td);

		}
	}

	private TaskDraw createRegularTaskDraw(GanttModelData modelData, TimeBoundsData timeBounds,
			TimelineStyleData timelineStyle, TimeScale timeScale, Real y, final Task task, final String display) {
		final boolean oddEnd;
		final boolean oddStart;
		final TimePoint startForDrawing = timeBounds.getStartForDrawing(task);
		final TimePoint endForDrawing = timeBounds.getEndForDrawing(task);
		if (timeBounds.getPrintStart() != null) {
			oddStart = TimePoint.ofStartOfDay(timeBounds.getMinDay()).compareTo(startForDrawing) == 0;
			oddEnd = TimePoint.ofStartOfDay(timeBounds.getMaxDay().plusDays(1)).compareTo(endForDrawing) == 0;
		} else {
			oddStart = false;
			oddEnd = false;
		}
		return new TaskDrawRegular(timelineStyle.getColorSet(), timeScale, y, display, startForDrawing, endForDrawing,
				oddStart, oddEnd, timelineStyle.getSkinParam(), task, this, modelData.getConstraintsForTask(task),
				task.getStyleBuilder());
	}

	public void buildTaskAndResourceDraws(StringBounder stringBounder, TimeHeader timeHeader, GanttModelData modelData,
			DisplayConfigData displayConfig, TimeBoundsData timeBounds, TimelineStyleData timelineStyle) {

		final TimeScale timeScale = timeHeader.getTimeScale();
		final double headerTotalHeight = timeHeader.getFullHeaderHeight(stringBounder);
		final ISkinParam skinParam = timelineStyle.getSkinParam();

		Real y = getOrigin().addFixed(headerTotalHeight);
		for (Task task : modelData.getTasks()) {
			final TaskDraw draw;
			if (task instanceof TaskSeparator) {
				final TaskSeparator taskSeparator = (TaskSeparator) task;
				draw = new TaskDrawSeparator(taskSeparator.getName(), timeScale, y, timeBounds.getMinDay(),
						timeBounds.getMaxDay(), task.getStyleBuilder(), skinParam);
			} else if (task instanceof TaskGroup) {
				final TaskGroup taskGroup = (TaskGroup) task;
				draw = new TaskDrawGroup(timelineStyle.getColorSet(), timeScale, y, taskGroup.getCode().getDisplay(),
						timeBounds.getStartForDrawing(taskGroup), timeBounds.getEndForDrawing(taskGroup), task, this,
						task.getStyleBuilder(), skinParam);
			} else {
				final TaskImpl taskImpl = (TaskImpl) task;
				final String taskLabel = displayConfig.isHideResourceName() ? taskImpl.getCode().getDisplay()
						: taskImpl.getPrettyDisplay();
				if (taskImpl.isDiamond())
					draw = new TaskDrawDiamond(timelineStyle.getColorSet(), timeScale, y, taskLabel,
							timeBounds.getStartForDrawing(taskImpl), taskImpl, this, task.getStyleBuilder(), skinParam);
				else
					draw = createRegularTaskDraw(modelData, timeBounds, timelineStyle, timeScale, y, taskImpl,
							taskLabel);

				draw.setColorsAndCompletion(taskImpl.getColors(), taskImpl.getCompletion(), taskImpl.getUrl(),
						taskImpl.getNote(), taskImpl.getNoteStereotype());
			}
			if (task.getRow() == null)
				y = y.addAtLeast(draw.getFullHeightTask(stringBounder));

			putTaskDraw(task, draw);
		}
		getOrigin().compileNow();
		resolveNoteOverlaps(stringBounder);
		buildResourceDraws(stringBounder, timeScale, modelData, displayConfig, timeBounds, timelineStyle,
				headerTotalHeight);
	}

	private void buildResourceDraws(StringBounder stringBounder, final TimeScale timeScale, GanttModelData modelData,
			DisplayConfigData displayConfig, TimeBoundsData timeBounds, TimelineStyleData timelineStyle,
			final double fullHeaderHeight) {
		double yy = computeBottomY(stringBounder);
		if (yy == 0) {
			yy = fullHeaderHeight;
		} else if (displayConfig.isHideResourceFootbox() == false)
			for (Resource res : modelData.getResources()) {
				final ResourceDraw draw = new ResourceDrawNumbers(modelData, timelineStyle, res, timeScale, yy,
						TimePoint.ofStartOfDay(timeBounds.getMinDay()),
						TimePoint.ofEndOfDayMinusOneSecond(timeBounds.getMaxDay()));
				res.setTaskDraw(draw);
				yy += draw.getHeight(stringBounder);
			}

		totalHeightWithoutFooter = yy;
	}

}
