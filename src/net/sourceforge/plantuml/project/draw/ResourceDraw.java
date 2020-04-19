/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Resource;
import net.sourceforge.plantuml.project.time.Wink;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class ResourceDraw implements UDrawable {

	private final Resource res;
	private final TimeScale timeScale;
	private final double y;
	private final Wink min;
	private final Wink max;
	private final GanttDiagram gantt;

	public ResourceDraw(GanttDiagram gantt, Resource res, TimeScale timeScale, double y, Wink min, Wink max) {
		this.res = res;
		this.timeScale = timeScale;
		this.y = y;
		this.min = min;
		this.max = max;
		this.gantt = gantt;
	}

	public void drawU(UGraphic ug) {
		final TextBlock title = Display.getWithNewlines(res.getName()).create(getFontConfiguration(13),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		title.drawU(ug);
		final ULine line = ULine.hline(timeScale.getEndingPosition(max) - timeScale.getStartingPosition(min));
		ug.apply(HColorUtils.BLACK)
				.apply(UTranslate.dy(title.calculateDimension(ug.getStringBounder()).getHeight())).draw(line);
		for (Wink i = min; i.compareTo(max) <= 0; i = i.increment()) {
			final int load = gantt.getLoadForResource(res, i);
			if (load > 0) {
				final FontConfiguration fontConfiguration = getFontConfiguration(9, load > 100 ? HColorUtils.RED
						: HColorUtils.BLACK);
				final TextBlock value = Display.getWithNewlines("" + load).create(fontConfiguration,
						HorizontalAlignment.LEFT, new SpriteContainerEmpty());
				final double start = (timeScale.getStartingPosition(i) + timeScale.getEndingPosition(i)) / 2
						- value.calculateDimension(ug.getStringBounder()).getWidth() / 2;
				value.drawU(ug.apply(new UTranslate(start, 16)));
			}

		}

	}

	private FontConfiguration getFontConfiguration(int size) {
		return getFontConfiguration(size, HColorUtils.BLACK);
	}

	private FontConfiguration getFontConfiguration(int size, HColor color) {
		final UFont font = UFont.serif(size);
		return new FontConfiguration(font, color, color, false);
	}

	// public void setColors(ComplementColors colors);
	//
	// public double getY();
	//
	// public double getY(Direction direction);
	//
	// public void drawTitle(UGraphic ug);

	public double getHeight() {
		return 16 * 2;
	}

	public double getY() {
		return y;
	}

}
