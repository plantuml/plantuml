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

import net.sourceforge.plantuml.crash.CrashImage;
import net.sourceforge.plantuml.crash.ReportLog;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.project.core.Resource;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.data.DisplayConfigData;
import net.sourceforge.plantuml.project.data.GanttModelData;
import net.sourceforge.plantuml.project.data.TaskDrawRegistryData;
import net.sourceforge.plantuml.project.data.TimeBoundsData;
import net.sourceforge.plantuml.project.data.TimelineStyleData;
import net.sourceforge.plantuml.project.draw.ResourceDraw;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.draw.header.TimeHeader;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class GanttDiagramMainBlock extends AbstractTextBlock {

	private final GanttDiagram diagram;
	private final TimeHeader timeHeader;
	private final GanttLayout layout;
	private final GanttModelData modelData;
	private final TimeBoundsData timeBounds;

	private final DisplayConfigData displayConfig;
	private final TimelineStyleData timelineStyle;
	private final TaskDrawRegistryData drawRegistry;

	public GanttDiagramMainBlock(TimeBoundsData timeBounds, GanttModelData modelData, TaskDrawRegistryData drawRegistry,
			DisplayConfigData displayConfig, TimelineStyleData timelineStyle, GanttDiagram diagram,
			TimeHeader timeHeader, StringBounder stringBounder) {
		this.diagram = diagram;
		this.timeBounds = timeBounds;
		this.modelData = modelData;
		this.drawRegistry = drawRegistry;
		this.displayConfig = displayConfig;
		this.timelineStyle = timelineStyle;
		this.timeHeader = timeHeader;
		this.layout = new GanttLayout(modelData, displayConfig, timeBounds, timelineStyle, drawRegistry, stringBounder,
				timeHeader);
	}

	@Override
	public void drawU(UGraphic ug) {
		try {
			final UGraphic ugOrig = ug;

			if (displayConfig.getLabelStrategy().titleInFirstColumn())
				ug = ug.apply(UTranslate.dx(layout.getTitlesWidth()));

			final Style style = StyleSignatureBasic.of(SName.root, SName.element, SName.ganttDiagram, SName.timeline)
					.getMergedStyle(timelineStyle.getSkinParam().getCurrentStyleBuilder());

			final HColor back = style.value(PName.BackGroundColor).asColor(timelineStyle.getColorSet());
			if (back.isTransparent() == false) {

				final double fullWidth = layout.getTitlesWidth() + layout.getBarsWidth();

				final URectangle rect1 = URectangle.build(fullWidth, layout.getHeaderHeight());
				ug.apply(back.bg()).draw(rect1);
				if (displayConfig.isShowFootbox()) {
					final URectangle rect2 = URectangle.build(fullWidth, layout.getFooterHeight());
					ug.apply(back.bg()).apply(UTranslate.dy(drawRegistry.getTotalHeightWithoutFooter())).draw(rect2);
				}
			}

			timeHeader.drawTimeHeader(ug, drawRegistry.getTotalHeightWithoutFooter());

			drawConstraints(ug, timeHeader.getTimeScale());
			drawTasksRect(ug);
			drawTasksTitle(ugOrig, layout.getTitlesWidth(), layout.getBarsWidth());

			if (displayConfig.isHideResourceFootbox() == false)
				drawResources(ug);

			if (displayConfig.isShowFootbox())
				timeHeader.drawTimeFooter(ug.apply(UTranslate.dy(drawRegistry.getTotalHeightWithoutFooter())));

		} catch (Throwable e) {
			Logme.error(e);

			final ReportLog report = new ReportLog();
			report.anErrorHasOccured(e, diagram.getFlashData());

			report.addProperties();
			report.addEmptyLine();
			report.youShouldSendThisDiagram();

			final CrashImage image = new CrashImage(e, diagram.getFlashData(), report);
			image.drawU(ug);

		}

	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return layout.calculateDimension();
	}

	private void drawTasksRect(UGraphic ug) {
		for (Task task : modelData.getTasks()) {
			if (timeBounds.isHidden(task))
				continue;

			final TaskDraw draw = drawRegistry.getTaskDraw(task);
			final UTranslate move = UTranslate.dy(draw.getY(ug.getStringBounder()).getCurrentValue());
			draw.drawU(ug.apply(move));
		}
	}

	private void drawConstraints(final UGraphic ug, TimeScale timeScale) {
		for (GanttConstraint constraint : modelData.getConstraints()) {
			if (timeBounds.getPrintStart() != null
					&& constraint.isHidden(TimePoint.ofStartOfDay(timeBounds.getMinDay()),
							TimePoint.ofEndOfDayMinusOneSecond(timeBounds.getMaxDay())))
				continue;

			constraint.getUDrawable(timeScale, drawRegistry).drawU(ug);
		}

	}

	private void drawTasksTitle(UGraphic ug, double colTitles, double colBars) {
		for (Task task : modelData.getTasks()) {
			if (timeBounds.isHidden(task))
				continue;

			final TaskDraw draw = drawRegistry.getTaskDraw(task);
			final UTranslate move = UTranslate.dy(draw.getY(ug.getStringBounder()).getCurrentValue());
			draw.drawTitle(ug.apply(move), displayConfig.getLabelStrategy(), colTitles, colBars);
		}
	}

	private void drawResources(UGraphic ug) {
		for (Resource res : modelData.getResources()) {
			final ResourceDraw draw = res.getResourceDraw();
			final UTranslate move = UTranslate.dy(draw.getY());
			draw.drawU(ug.apply(move));
		}
	}

}
