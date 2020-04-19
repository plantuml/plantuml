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
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.project.core.AbstractTask;
import net.sourceforge.plantuml.project.lang.ComplementColors;
import net.sourceforge.plantuml.project.time.Wink;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class TaskDrawSeparator implements TaskDraw {

	private final TimeScale timeScale;
	private final double y;
	private final Wink min;
	private final Wink max;
	private final String name;

	public TaskDrawSeparator(String name, TimeScale timeScale, double y, Wink min, Wink max) {
		this.name = name;
		this.y = y;
		this.timeScale = timeScale;
		this.min = min;
		this.max = max;
	}

	public void drawTitle(UGraphic ug) {
		getTitle().drawU(ug.apply(UTranslate.dx(MARGIN1)));
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
		return new FontConfiguration(font, HColorUtils.BLACK, HColorUtils.BLACK, false);
	}

	private final static double MARGIN1 = 10;
	private final static double MARGIN2 = 2;

	public void drawU(UGraphic ug) {
		final double widthTitle = getTitle().calculateDimension(ug.getStringBounder()).getWidth();
		final double start = timeScale.getStartingPosition(min) + widthTitle;
		final double end = timeScale.getEndingPosition(max);

		ug = ug.apply(HColorUtils.BLACK);
		ug = ug.apply(UTranslate.dy(getHeight() / 2));

		if (widthTitle == 0) {
			final ULine line = ULine.hline(end - start);
			ug.draw(line);
		} else {
			final ULine line1 = ULine.hline(MARGIN1 - MARGIN2);
			final ULine line2 = ULine.hline(end - start - MARGIN1 - MARGIN2);
			ug.draw(line1);
			ug.apply(UTranslate.dx(widthTitle + MARGIN1 + MARGIN2)).draw(line2);
		}
	}

	public double getHeight() {
		return AbstractTask.HEIGHT;
	}

	public double getY() {
		return y;
	}

	public void setColorsAndCompletion(ComplementColors colors, int completion, Url url) {
	}

}
