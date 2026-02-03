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
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class GanttDiagramMainBlock extends AbstractTextBlock {

	private final GanttDiagram diagram;
	private final TimeHeader timeHeader;
	private final GanttLayout layout;

	public GanttDiagramMainBlock(GanttDiagram diagram, TimeHeader timeHeader, StringBounder stringBounder) {
		this.diagram = diagram;
		this.timeHeader = timeHeader;		
		this.layout = new GanttLayout(stringBounder, diagram.model, timeHeader);
	}

	@Override
	public void drawU(UGraphic ug) {
		try {
			final UGraphic ugOrig = ug;

			if (diagram.model.labelStrategy.titleInFirstColumn())
				ug = ug.apply(UTranslate.dx(layout.titlesWidth));

			final Style timelineStyle = StyleSignatureBasic
					.of(SName.root, SName.element, SName.ganttDiagram, SName.timeline)
					.getMergedStyle(diagram.getCurrentStyleBuilder());

			final HColor back = timelineStyle.value(PName.BackGroundColor).asColor(diagram.getIHtmlColorSet());
			if (back.isTransparent() == false) {

				final double fullWidth = layout.titlesWidth + layout.barsWidth;

				final URectangle rect1 = URectangle.build(fullWidth, layout.headerHeight);
				ug.apply(back.bg()).draw(rect1);
				if (diagram.model.showFootbox) {
					final URectangle rect2 = URectangle.build(fullWidth, layout.footerHeight);
					ug.apply(back.bg()).apply(UTranslate.dy(diagram.model.totalHeightWithoutFooter)).draw(rect2);
				}
			}

			timeHeader.drawTimeHeader(ug, diagram.model.totalHeightWithoutFooter);

			drawConstraints(ug, timeHeader.getTimeScale());
			drawTasksRect(ug);
			drawTasksTitle(ugOrig, layout.titlesWidth, layout.barsWidth);

			if (diagram.model.hideResourceFoobox == false)
				drawResources(ug);

			if (diagram.model.showFootbox)
				timeHeader.drawTimeFooter(ug.apply(UTranslate.dy(diagram.model.totalHeightWithoutFooter)));

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
		for (Task task : diagram.model.tasks.values()) {
			if (diagram.model.isHidden(task))
				continue;

			final TaskDraw draw = diagram.model.draws.get(task);
			final UTranslate move = UTranslate.dy(draw.getY(ug.getStringBounder()).getCurrentValue());
			draw.drawU(ug.apply(move));
		}
	}

	private void drawConstraints(final UGraphic ug, TimeScale timeScale) {
		for (GanttConstraint constraint : diagram.model.constraints) {
			if (diagram.model.printStart != null && constraint.isHidden(TimePoint.ofStartOfDay(diagram.model.minDay),
					TimePoint.ofEndOfDayMinusOneSecond(diagram.model.maxDay)))
				continue;

			constraint.getUDrawable(timeScale, diagram.model).drawU(ug);
		}

	}

	private void drawTasksTitle(UGraphic ug, double colTitles, double colBars) {
		for (Task task : diagram.model.tasks.values()) {
			if (diagram.model.isHidden(task))
				continue;

			final TaskDraw draw = diagram.model.draws.get(task);
			final UTranslate move = UTranslate.dy(draw.getY(ug.getStringBounder()).getCurrentValue());
			draw.drawTitle(ug.apply(move), diagram.model.labelStrategy, colTitles, colBars);
		}
	}

	private void drawResources(UGraphic ug) {
		for (Resource res : diagram.model.resources.values()) {
			final ResourceDraw draw = res.getResourceDraw();
			final UTranslate move = UTranslate.dy(draw.getY());
			draw.drawU(ug.apply(move));
		}
	}

}
