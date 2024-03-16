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
package net.sourceforge.plantuml.project.draw;

import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.LabelPosition;
import net.sourceforge.plantuml.project.LabelStrategy;
import net.sourceforge.plantuml.project.core.Resource;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScale;

public class ResourceDrawVersion2 implements ResourceDraw {

	private final Resource res;
	private final TimeScale timeScale;
	private final double y;
	private final Day min;
	private final Day max;
	private final GanttDiagram gantt;

	public ResourceDrawVersion2(GanttDiagram gantt, Resource res, TimeScale timeScale, double y, Day min, Day max) {
		this.res = res;
		this.timeScale = timeScale;
		this.y = y;
		this.min = min;
		this.max = max;
		this.gantt = gantt;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		double ypos = 16;
		final double tmpHeight = getHeight(stringBounder) - ypos;
		for (Day wink = gantt.getStartingDate(); wink.compareTo(gantt.getEndingDate()) <= 0; wink = wink.increment()) {
			final double start = timeScale.getStartingPosition(wink);
			final double end = timeScale.getEndingPosition(wink);
			final UShape rect = URectangle.build(end - start, tmpHeight);
			if (res.isClosedAt(wink))
				ug.apply(HColors.LIGHT_GRAY.bg()).apply(new UTranslate(start, ypos)).draw(rect);

		}

		final TextBlock title = Display.getWithNewlines(res.getName()).create(getFontConfiguration(13),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		title.drawU(ug);
		final ULine line = ULine.hline(timeScale.getEndingPosition(max) - timeScale.getStartingPosition(min));
		ug.apply(HColors.BLACK).apply(UTranslate.dy(title.calculateDimension(ug.getStringBounder()).getHeight()))
				.draw(line);

		final LabelStrategy labelStrategy = new LabelStrategy(LabelPosition.LEGACY, HorizontalAlignment.LEFT);

		for (TaskDrawRegular draw : gantt.getAllTasksForResource(res)) {
			draw.drawShape(ug.apply(UTranslate.dy(ypos + getTopMarginBetweenTask())));
			draw.drawTitle(ug.apply(UTranslate.dy(ypos + getTopMarginBetweenTask())), labelStrategy, 10, 100);
			ypos += draw.getShapeHeight(stringBounder) + getMarginBetweenTask();
		}
	}

	@Override
	public double getHeight(StringBounder stringBounder) {
		double ypos = 16;
		for (TaskDrawRegular draw : gantt.getAllTasksForResource(res))
			ypos += draw.getShapeHeight(stringBounder) + getMarginBetweenTask();

		return ypos + 8;
	}

	private double getTopMarginBetweenTask() {
		return 2;
	}

	private double getMarginBetweenTask() {
		return 4;
	}

	private FontConfiguration getFontConfiguration(int size) {
		return getFontConfiguration(size, HColors.BLACK);
	}

	private FontConfiguration getFontConfiguration(int size, HColor color) {
		final UFont font = UFont.serif(size);
		return FontConfiguration.create(font, color, color, null);
	}

	@Override
	public final double getY() {
		return y;
	}

}
