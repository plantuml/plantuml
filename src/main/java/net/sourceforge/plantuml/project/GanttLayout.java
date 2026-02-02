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
package net.sourceforge.plantuml.project;

import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.project.core.Resource;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskGroup;
import net.sourceforge.plantuml.project.core.TaskImpl;
import net.sourceforge.plantuml.project.core.TaskSeparator;
import net.sourceforge.plantuml.project.draw.ResourceDraw;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.draw.TaskDrawDiamond;
import net.sourceforge.plantuml.project.draw.TaskDrawGroup;
import net.sourceforge.plantuml.project.draw.TaskDrawSeparator;
import net.sourceforge.plantuml.project.draw.header.TimeHeader;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.real.Real;

public final class GanttLayout {

	final double titlesWidth;
	final double barsWidth;
	final double totalHeight;
	final double headerHeight;
	final double footerHeight;

	public GanttLayout(StringBounder stringBounder, GanttDiagram diagram, TimeHeader timeHeader) {

		initTaskAndResourceDraws(stringBounder, diagram, timeHeader);

		final double computedTitlesWidth;
		if (diagram.labelStrategy.titleInside()) {
			computedTitlesWidth = 0;
		} else {
			double w = 0;
			for (Task task : diagram.tasks.values()) {
				if (diagram.isHidden(task))
					continue;

				final TaskDraw draw = diagram.model.draws.get(task);
				if (draw == null)
					continue;

				w = Math.max(w, draw.getTitleWidth(stringBounder));
			}
			computedTitlesWidth = w;
		}

		this.titlesWidth = computedTitlesWidth;
		this.barsWidth = getBarsColumnWidth(diagram, timeHeader);
		this.headerHeight = timeHeader.getTimeHeaderHeight(stringBounder);
		this.footerHeight = diagram.showFootbox ? timeHeader.getTimeFooterHeight(stringBounder) : 0;
		this.totalHeight = diagram.model.totalHeightWithoutFooter + this.footerHeight;
	}

	private void initTaskAndResourceDraws(StringBounder stringBounder, GanttDiagram diagram, TimeHeader timeHeader) {
		final TimeScale timeScale = timeHeader.getTimeScale();
		final double fullHeaderHeight = timeHeader.getFullHeaderHeight(stringBounder);
		Real y = diagram.origin.addFixed(fullHeaderHeight);
		for (Task task : diagram.tasks.values()) {
			final TaskDraw draw;
			if (task instanceof TaskSeparator) {
				final TaskSeparator taskSeparator = (TaskSeparator) task;
				draw = new TaskDrawSeparator(taskSeparator.getName(), timeScale, y, diagram.model.minDay,
						diagram.model.maxDay, task.getStyleBuilder(), diagram.getSkinParam());
			} else if (task instanceof TaskGroup) {
				final TaskGroup taskGroup = (TaskGroup) task;
				draw = new TaskDrawGroup(timeScale, y, taskGroup.getCode().getDisplay(),
						diagram.getStartForDrawing(taskGroup), diagram.getEndForDrawing(taskGroup), task, diagram,
						task.getStyleBuilder(), diagram.getSkinParam());
			} else {
				final TaskImpl taskImpl = (TaskImpl) task;
				final String display = diagram.hideResourceName ? taskImpl.getCode().getDisplay()
						: taskImpl.getPrettyDisplay();
				if (taskImpl.isDiamond())
					draw = new TaskDrawDiamond(timeScale, y, display, diagram.getStartForDrawing(taskImpl), taskImpl,
							diagram, task.getStyleBuilder(), diagram.getSkinParam());
				else
					draw = diagram.createTaskDrawRegular(timeScale, y, taskImpl, display);

				draw.setColorsAndCompletion(taskImpl.getColors(), taskImpl.getCompletion(), taskImpl.getUrl(),
						taskImpl.getNote(), taskImpl.getNoteStereotype());
			}
			if (task.getRow() == null)
				y = y.addAtLeast(draw.getFullHeightTask(stringBounder));

			diagram.model.draws.put(task, draw);
		}
		diagram.origin.compileNow();
		diagram.magicPush(stringBounder);
		double yy = diagram.lastY(stringBounder);
		if (yy == 0) {
			yy = fullHeaderHeight;
		} else if (diagram.hideResourceFoobox == false)
			for (Resource res : diagram.resources.values()) {
				final ResourceDraw draw = diagram.buildResourceDraw(diagram, res, timeScale, yy);
				res.setTaskDraw(draw);
				yy += draw.getHeight(stringBounder);
			}

		diagram.model.totalHeightWithoutFooter = yy;
	}

	public XDimension2D calculateDimension() {
		final double width = titlesWidth + barsWidth;
		return new XDimension2D(width, totalHeight);
	}

	private double getBarsColumnWidth(GanttDiagram diagram, TimeHeader timeHeader) {
		final double xmin = timeHeader.getTimeScale().getPosition(TimePoint.ofStartOfDay(diagram.model.minDay));
		final double xmax = timeHeader.getTimeScale()
				.getPosition(TimePoint.ofEndOfDayMinusOneSecond(diagram.model.maxDay));
		return xmax - xmin;
	}
}
