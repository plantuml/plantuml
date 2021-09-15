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

import java.util.Objects;

import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.ThemeStyle;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public abstract class TimeHeader {

	protected final double Y_POS_ROW16() {
		return 16;
	}

	protected final double Y_POS_ROW28() {
		return 28;
	}

	private final TimeScale timeScale;
	private final Style closedStyle;
	private final Style timelineStyle;

	private final HColorSet colorSet;
	private final ThemeStyle themeStyle;

	protected final Day min;
	protected final Day max;

	public TimeHeader(Style timelineStyle, Style closedStyle, Day min, Day max, TimeScale timeScale, HColorSet colorSet,
			ThemeStyle themeStyle) {
		this.timeScale = timeScale;
		this.min = min;
		this.max = max;
		this.closedStyle = Objects.requireNonNull(closedStyle);
		this.timelineStyle = Objects.requireNonNull(timelineStyle);
		this.colorSet = colorSet;
		this.themeStyle = themeStyle;
	}

	protected final HColor closedBackgroundColor() {
		return closedStyle.value(PName.BackGroundColor).asColor(themeStyle, colorSet);
	}

	protected final HColor closedFontColor() {
		return closedStyle.value(PName.FontColor).asColor(themeStyle, colorSet);
	}

	protected final HColor openFontColor() {
		return timelineStyle.value(PName.FontColor).asColor(themeStyle, colorSet);
	}

	protected final HColor getBarColor() {
		return timelineStyle.value(PName.LineColor).asColor(themeStyle, colorSet);
	}

	public abstract double getTimeHeaderHeight();

	public abstract double getTimeFooterHeight();

	public abstract void drawTimeHeader(UGraphic ug, double totalHeightWithoutFooter);

	public abstract void drawTimeFooter(UGraphic ug);

	public abstract double getFullHeaderHeight();

	protected final void drawHline(UGraphic ug, double y) {
		final double xmin = getTimeScale().getStartingPosition(min);
		final double xmax = getTimeScale().getEndingPosition(max);
		final ULine hline = ULine.hline(xmax - xmin);
		ug.apply(getBarColor()).apply(UTranslate.dy(y)).draw(hline);
	}

	protected final void drawVbar(UGraphic ug, double x, double y1, double y2) {
		final ULine vbar = ULine.vline(y2 - y1);
		ug.apply(getBarColor()).apply(new UTranslate(x, y1)).draw(vbar);
	}

	final protected FontConfiguration getFontConfiguration(int size, boolean bold, HColor color) {
		UFont font = UFont.serif(size);
		if (bold) {
			font = font.bold();
		}
		return new FontConfiguration(font, color, color, false);
	}

	public final TimeScale getTimeScale() {
		return timeScale;
	}

	protected final TextBlock getTextBlock(String text, int size, boolean bold, HColor color) {
		return Display.getWithNewlines(text).create(getFontConfiguration(size, bold, color), HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
	}

	protected final void printCentered(UGraphic ug, TextBlock text, double start, double end) {
		final double width = text.calculateDimension(ug.getStringBounder()).getWidth();
		final double available = end - start;
		final double diff = Math.max(0, available - width);
		text.drawU(ug.apply(UTranslate.dx(start + diff / 2)));
	}

	protected final void printCentered(UGraphic ug, boolean hideIfTooBig, double start, double end,
			TextBlock... texts) {
		final double available = end - start;
		for (int i = texts.length - 1; i >= 0; i--) {
			final TextBlock text = texts[i];
			final double width = text.calculateDimension(ug.getStringBounder()).getWidth();
			if ((i == 0 && hideIfTooBig == false) || width <= available) {
				final double diff = Math.max(0, available - width);
				text.drawU(ug.apply(UTranslate.dx(start + diff / 2)));
				return;
			}
		}
	}

	protected final void drawRectangle(UGraphic ug, double height, double x1, double x2) {
		if (height == 0) {
			return;
		}
		ug = ug.apply(new HColorNone());
		ug = ug.apply(new UTranslate(x1, getFullHeaderHeight()));
		ug.draw(new URectangle(x2 - x1, height));
	}

}
