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
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.draw.header.TimeHeader;
import net.sourceforge.plantuml.project.time.TimePoint;

public final class GanttLayout {

	final double titlesWidth;
	final double barsWidth;
	final double totalHeight;
	final double headerHeight;
	final double footerHeight;

	public GanttLayout(StringBounder sb, GanttDiagram diagram, TimeHeader timeHeader) {

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

				w = Math.max(w, draw.getTitleWidth(sb));
			}
			computedTitlesWidth = w;
		}

		this.titlesWidth = computedTitlesWidth;
		this.barsWidth = getBarsColumnWidth(diagram, timeHeader);
		this.headerHeight = timeHeader.getTimeHeaderHeight(sb);
		this.footerHeight = diagram.showFootbox ? timeHeader.getTimeFooterHeight(sb) : 0;
		this.totalHeight = diagram.model.totalHeightWithoutFooter + this.footerHeight;
	}

	public XDimension2D calculateDimension() {
		final double width = titlesWidth + barsWidth;
		return new XDimension2D(width, totalHeight);
	}

	private double getBarsColumnWidth(GanttDiagram diagram, TimeHeader timeHeader) {
		final double xmin = timeHeader.getTimeScale().getPosition(TimePoint.ofStartOfDay(diagram.model.minDay));
		final double xmax = timeHeader.getTimeScale().getPosition(TimePoint.ofEndOfDayMinusOneSecond(diagram.model.maxDay));
		return xmax - xmin;
	}
}
