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
package net.sourceforge.plantuml.project3;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TaskDrawSeparator implements TaskDraw {

	private final TimeScale timeScale;
	private final double y;
	private final Instant min;
	private final Instant max;
	private final String name;

	public TaskDrawSeparator(TaskSeparator task, TimeScale timeScale, double y, Instant min, Instant max) {
		this.name = task.getName();
		this.y = y;
		this.timeScale = timeScale;
		this.min = min;
		this.max = max;
	}

	public void drawTitle(UGraphic ug) {
		getTitle().drawU(ug.apply(new UTranslate(MARGIN1, 0)));
	}

	private TextBlock getTitle() {
		if (name == null) {
			return TextBlockUtils.empty(0, 0);
		}
		return Display.getWithNewlines(this.name).create(getFontConfiguration(), HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
	}

	private FontConfiguration getFontConfiguration() {
		final UFont font = UFont.serif(11);
		return new FontConfiguration(font, HtmlColorUtils.BLACK, HtmlColorUtils.BLACK, false);
	}

	private final static double MARGIN1 = 10;
	private final static double MARGIN2 = 2;

	public void drawU(UGraphic ug) {
		final double widthTitle = getTitle().calculateDimension(ug.getStringBounder()).getWidth();
		final double start = timeScale.getStartingPosition(min) + widthTitle;
		final double end = timeScale.getEndingPosition(max);

		ug = ug.apply(new UChangeColor(HtmlColorUtils.BLACK));
		ug = ug.apply(new UTranslate(0, getHeight() / 2));

		if (widthTitle == 0) {
			final ULine line = new ULine(end - start, 0);
			ug.draw(line);
		} else {
			final ULine line1 = new ULine(MARGIN1 - MARGIN2, 0);
			final ULine line2 = new ULine(end - start - MARGIN1 - MARGIN2, 0);
			ug.draw(line1);
			ug.apply(new UTranslate(widthTitle + MARGIN1 + MARGIN2, 0)).draw(line2);
		}
	}

	public double getHeight() {
		return 16;
	}

	public double getY() {
		return y;
	}

	public double getY(Direction direction) {
		if (direction == Direction.UP) {
			return y;
		}
		if (direction == Direction.DOWN) {
			return y + getHeight();
		}
		return y + getHeight() / 2;
	}

	public void setColors(ComplementColors colors) {
	}

}
