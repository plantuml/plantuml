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
import net.sourceforge.plantuml.project.time.Wink;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public abstract class TimeHeader {
	
	protected static final int Y_POS_ROW16 = 16;
	protected static final int Y_POS_ROW28 = 28;


	private final TimeScale timeScale;
	protected final Wink min;
	protected final Wink max;

	public TimeHeader(Wink min, Wink max, TimeScale timeScale) {
		this.timeScale = timeScale;
		this.min = min;
		this.max = max;
	}

	public abstract void drawTimeHeader(final UGraphic ug, double totalHeight);

	public abstract double getFullHeaderHeight();

	protected final void drawHline(UGraphic ug, double y) {
		final double xmin = getTimeScale().getStartingPosition(min);
		final double xmax = getTimeScale().getEndingPosition(max);
		final ULine hline = ULine.hline(xmax - xmin);
		ug.apply(HColorUtils.LIGHT_GRAY).apply(UTranslate.dy(y)).draw(hline);
	}

	final protected FontConfiguration getFontConfiguration(int size, boolean bold) {
		UFont font = UFont.serif(size);
		if (bold) {
			font = font.bold();
		}
		return new FontConfiguration(font, HColorUtils.BLACK, HColorUtils.BLACK, false);
	}

	public final TimeScale getTimeScale() {
		return timeScale;
	}

	protected final TextBlock getTextBlock(final String text, int size, boolean bold) {
		return Display.getWithNewlines(text).create(getFontConfiguration(size, bold), HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
	}

	protected final void printCentered(UGraphic ug, TextBlock text, double start, double end) {
		final double width = text.calculateDimension(ug.getStringBounder()).getWidth();
		final double available = end - start;
		final double diff = Math.max(0, available - width);
		text.drawU(ug.apply(UTranslate.dx(start + diff / 2)));
	}

	protected final void printCentered(UGraphic ug, double start, double end, TextBlock... texts) {
		final double available = end - start;
		for (int i = texts.length - 1; i >= 0; i--) {
			final TextBlock text = texts[i];
			final double width = text.calculateDimension(ug.getStringBounder()).getWidth();
			if (i == 0 || width <= available) {
				final double diff = Math.max(0, available - width);
				text.drawU(ug.apply(UTranslate.dx(start + diff / 2)));
				return;
			}
		}
	}

}
