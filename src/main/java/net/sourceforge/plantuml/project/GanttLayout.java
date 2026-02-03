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

	public GanttLayout(StringBounder stringBounder, GanttPreparedModel model, TimeHeader timeHeader) {

		model.initTaskAndResourceDraws(stringBounder, timeHeader);

		final double computedTitlesWidth;
		if (model.labelStrategy.titleInside()) {
			computedTitlesWidth = 0;
		} else {
			double w = 0;
			for (Task task : model.tasks.values()) {
				if (model.isHidden(task))
					continue;

				final TaskDraw draw = model.draws.get(task);
				if (draw == null)
					continue;

				w = Math.max(w, draw.getTitleWidth(stringBounder));
			}
			computedTitlesWidth = w;
		}

		this.titlesWidth = computedTitlesWidth;
		this.barsWidth = getBarsColumnWidth(model, timeHeader);
		this.headerHeight = timeHeader.getTimeHeaderHeight(stringBounder);
		this.footerHeight = model.showFootbox ? timeHeader.getTimeFooterHeight(stringBounder) : 0;
		this.totalHeight = model.totalHeightWithoutFooter + this.footerHeight;
	}

	public XDimension2D calculateDimension() {
		final double width = titlesWidth + barsWidth;
		return new XDimension2D(width, totalHeight);
	}

	private double getBarsColumnWidth(GanttPreparedModel model, TimeHeader timeHeader) {
		final double xmin = timeHeader.getTimeScale().getPosition(TimePoint.ofStartOfDay(model.minDay));
		final double xmax = timeHeader.getTimeScale().getPosition(TimePoint.ofEndOfDayMinusOneSecond(model.maxDay));
		return xmax - xmin;
	}
}
