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
import net.sourceforge.plantuml.project.data.DisplayConfigData;
import net.sourceforge.plantuml.project.data.GanttModelData;
import net.sourceforge.plantuml.project.data.TaskDrawRegistryData;
import net.sourceforge.plantuml.project.data.TimeBoundsData;
import net.sourceforge.plantuml.project.data.TimelineStyleData;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.draw.header.TimeHeader;

public final class GanttLayout {

	private final double titlesWidth;
	private final double barsWidth;
	private double totalHeight;
	private double headerHeight;
	private double footerHeight;

	public GanttLayout(GanttModelData modelData, DisplayConfigData displayConfig, TimeBoundsData timeBounds,
			TimelineStyleData timelineStyle, TaskDrawRegistryData drawRegistry, StringBounder stringBounder,
			TimeHeader timeHeader) {

		drawRegistry.buildTaskAndResourceDraws(stringBounder, timeHeader, modelData, displayConfig, timeBounds,
				timelineStyle);

		final double computedTitlesWidth;
		if (displayConfig.getLabelStrategy().titleInside()) {
			computedTitlesWidth = 0;
		} else {
			double w = 0;
			for (Task task : modelData.getTasks()) {
				if (timeBounds.isHidden(task))
					continue;

				final TaskDraw draw = drawRegistry.getTaskDraw(task);
				if (draw == null)
					continue;

				w = Math.max(w, draw.getTitleWidth(stringBounder));
			}
			computedTitlesWidth = w;
		}

		this.titlesWidth = computedTitlesWidth;
		this.barsWidth = timeBounds.getBarsColumnWidth(timeHeader);
		this.headerHeight = timeHeader.getTimeHeaderHeight(stringBounder);
		this.footerHeight = displayConfig.isShowFootbox() ? timeHeader.getTimeFooterHeight(stringBounder) : 0;
		this.totalHeight = drawRegistry.getTotalHeightWithoutFooter() + this.footerHeight;
	}

	public XDimension2D calculateDimension() {
		final double width = titlesWidth + barsWidth;
		return new XDimension2D(width, totalHeight);
	}

	public double getTitlesWidth() {
		return titlesWidth;
	}

	public double getBarsWidth() {
		return barsWidth;
	}

	public double getTotalHeight() {
		return totalHeight;
	}

	public double getHeaderHeight() {
		return headerHeight;
	}

	public double getFooterHeight() {
		return footerHeight;
	}
}
