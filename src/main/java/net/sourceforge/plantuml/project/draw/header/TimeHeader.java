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
package net.sourceforge.plantuml.project.draw.header;

import java.time.DayOfWeek;
import java.time.LocalDate;

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
import net.sourceforge.plantuml.project.GanttPreparedModel;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.Value;

public abstract class TimeHeader {
	// ::remove folder when __HAXE__

	private final TimeScale timeScale;

	protected final GanttPreparedModel model;

	public TimeHeader(GanttPreparedModel model, TimeScale timeScale) {
		this.model = model;
		this.timeScale = timeScale;
	}

	public HColor getColor(TimePoint wink) {
		return model.colorDays().get(wink);
	}

	public HColor getColor(DayOfWeek dayOfWeek) {
		return model.colorDaysOfWeek.get(dayOfWeek);
	}

	protected final boolean isBold2(TimePoint wink) {
		return model.verticalSeparatorBefore.contains(wink);
	}

	protected final LocalDate getMinDay() {
		return model.minDay;
	}

	protected final LocalDate getMaxDay() {
		return model.maxDay;
	}

	protected final HColor closedBackgroundColor() {
		return model.getStyleTOTO4(SName.closed).value(PName.BackGroundColor).asColor(model.colorSet);
	}

	protected final HColor closedFontColor() {
		return model.getStyleTOTO4(SName.closed).value(PName.FontColor).asColor(model.colorSet);
	}

	protected final HColor openFontColor() {
		return model.getStyleTOTO4(SName.timeline).value(PName.FontColor).asColor(model.colorSet);
	}

	protected final HColor getLineColor() {
		return model.getStyleTOTO4(SName.timeline).value(PName.LineColor).asColor(model.colorSet);
	}

	public abstract double getTimeHeaderHeight(StringBounder stringBounder);

	public abstract double getTimeFooterHeight(StringBounder stringBounder);

	public abstract double getFullHeaderHeight(StringBounder stringBounder);

	public abstract void drawTimeHeader(UGraphic ug, double totalHeightWithoutFooter);

	public abstract void drawTimeFooter(UGraphic ug);

	public final TimeScale getTimeScale() {
		return timeScale;
	}

	protected final void drawHline(UGraphic ug, double y) {
		final double xmin = getTimeScale().getPosition(TimePoint.ofStartOfDay(model.minDay));
		final double xmax = getTimeScale().getPosition(TimePoint.ofEndOfDayMinusOneSecond(model.maxDay));
		final ULine hline = ULine.hline(xmax - xmin);
		ug.apply(getLineColor()).apply(UTranslate.dy(y)).draw(hline);
	}

	protected final void drawVline(UGraphic ug, double x, double y1, double y2) {
		final ULine vbar = ULine.vline(y2 - y1);
		ug.apply(new UTranslate(x, y1)).draw(vbar);
	}

	final protected FontConfiguration getFontConfiguration(UFont font, boolean bold, HColor color) {
		if (bold)
			font = font.bold();

		return FontConfiguration.create(font, color, color, null);
	}

	protected final TextBlock getTextBlock(SName param, String text, boolean bold, HColor color) {
		final UFont font = model.getStyleTOTO3(SName.timeline, param).getUFont();
		final FontConfiguration fontConfiguration = getFontConfiguration(font, bold, color);
		return Display.getWithNewlines(getPragma(), text).create(fontConfiguration, HorizontalAlignment.LEFT,
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
		if (height == 0)
			return;

		ug = ug.apply(HColors.none());
		ug = ug.apply(new UTranslate(x1, getFullHeaderHeight(ug.getStringBounder())));
		if (x2 > x1)
			ug.draw(URectangle.build(x2 - x1, height));
	}

	protected void printVerticalSeparators(UGraphic ug, double totalHeightWithoutFooter) {
		ug = model.forVerticalSeparator(ug);
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			if (isBold2(wink))
				drawVline(ug, getTimeScale().getPosition(wink), getFullHeaderHeight(ug.getStringBounder()),
						totalHeightWithoutFooter);
		}
	}

	protected Pragma getPragma() {
		return Pragma.createEmpty();
	}

	class Pending {
		final double x1;
		double x2;
		final HColor color;

		Pending(HColor color, double x1, double x2) {
			this.x1 = x1;
			this.x2 = x2;
			this.color = color;
		}

		public void draw(UGraphic ug, double height) {
			drawRectangle(ug.apply(color.bg()), height, x1, x2);
		}
	}

}
