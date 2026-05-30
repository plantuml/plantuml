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
package net.sourceforge.plantuml.gantt.draw.header;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.sourceforge.plantuml.gantt.data.DayCalendarData;
import net.sourceforge.plantuml.gantt.data.TimeBoundsData;
import net.sourceforge.plantuml.gantt.data.TimeScaleConfigData;
import net.sourceforge.plantuml.gantt.data.TimelineStyleData;
import net.sourceforge.plantuml.gantt.data.WeekConfigData;
import net.sourceforge.plantuml.gantt.time.TimePoint;
import net.sourceforge.plantuml.gantt.timescale.TimeScale;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFace;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.style.SName;

public abstract class TimeHeader {

	private final TimeScale timeScale;

	protected final DayCalendarData dayCalendar;
	protected final TimeBoundsData timeBounds;
	protected final TimeScaleConfigData scaleConfig;
	protected final TimelineStyleData timelineStyle;
	protected final WeekConfigData weekConfigData;

	public TimeHeader(TimeScale timeScale, WeekConfigData weekConfigData, DayCalendarData dayCalendar,
			TimeBoundsData timeBounds, TimeScaleConfigData scaleConfig, TimelineStyleData timelineStyle) {
		this.weekConfigData = weekConfigData;
		this.dayCalendar = dayCalendar;
		this.timeBounds = timeBounds;
		this.scaleConfig = scaleConfig;
		this.timelineStyle = timelineStyle;
		this.timeScale = timeScale;
	}

	public HColor getColor(TimePoint wink) {
		return dayCalendar.getDayColor(wink);
	}

	public HColor getColor(DayOfWeek dayOfWeek) {
		return dayCalendar.getDayOfWeekColor(dayOfWeek);
	}

	protected final boolean isBold(LocalDate wink) {
		return dayCalendar.hasSeparatorBefore(wink);
	}

	protected final LocalDate getMinDay() {
		return timeBounds.getMinDay();
	}

	protected final LocalDate getMaxDay() {
		return timeBounds.getMaxDay();
	}

	protected final HColor closedBackgroundColor() {
		return timelineStyle.getClosedBackgroundColor();
	}

	protected final HColor closedFontColor() {
		return timelineStyle.getClosedFontColor();
	}

	protected final HColor openFontColor() {
		return timelineStyle.getOpenFontColor();
	}

	protected final HColor getLineColor() {
		return timelineStyle.getLineColor();
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
		final double xmin = getTimeScale().getPosition(TimePoint.ofStartOfDay(timeBounds.getMinDay()));
		final double xmax = getTimeScale().getPosition(TimePoint.ofStartOfDay(timeBounds.getMaxDay()).addDays(1));
		final ULine hline = ULine.hline(xmax - xmin);
		ug.apply(getLineColor()).apply(UTranslate.dy(y)).draw(hline);
	}

	protected final void drawVline(UGraphic ug, double x, double y1, double y2) {
		final ULine vbar = ULine.vline(y2 - y1);
		ug.apply(new UTranslate(x, y1)).draw(vbar);
	}

	final protected FontConfiguration getFontConfiguration(UFont font, boolean bold, HColor color) {
		if (bold)
			font = font.withFontFace(UFontFace.bold());

		return FontConfiguration.create(font, color, color, null);
	}

	protected abstract boolean isZeroOnDay(TimePoint instant);

	protected final void drawColorsBackground(UGraphic ug, double totalHeightWithoutFooter) {

		final double height = totalHeightWithoutFooter - getFullHeaderHeight(ug.getStringBounder());
		Pending pending = null;

		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1)) {
			final TimePoint wink = TimePoint.ofStartOfDay(day);
			final double x1 = getTimeScale().getPosition(wink);
			final double x2 = getTimeScale().getPosition(wink) + getTimeScale().getWidth(wink);
			HColor back = getColor(wink);
			// Day of week should be stronger than period of time (back color).
			final HColor backDoW = getColor(wink.toDayOfWeek());
			if (backDoW != null)
				back = backDoW;

			if (back == null && isZeroOnDay(wink))
				back = closedBackgroundColor();

			if (back == null) {
				if (pending != null)
					pending.draw(ug, height);
				pending = null;
			} else {
				if (pending != null && pending.color.equals(back) == false) {
					pending.draw(ug, height);
					pending = null;
				}
				if (pending == null)
					pending = new Pending(back, x1, x2);
				else
					pending.x2 = x2;

			}
		}

		if (pending != null)
			pending.draw(ug, height);

	}

	private static final class TextBlockKey {
		private final String text;
		private final FontConfiguration fontConfiguration;
		private final int hash;

		TextBlockKey(String text, FontConfiguration fontConfiguration) {
			this.text = text;
			this.fontConfiguration = fontConfiguration;
			this.hash = Objects.hash(text, fontConfiguration);
		}

		@Override
		public int hashCode() {
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj instanceof TextBlockKey == false)
				return false;
			final TextBlockKey other = (TextBlockKey) obj;
			return text.equals(other.text) && fontConfiguration.equals(other.fontConfiguration);
		}
	}

	private final Map<TextBlockKey, TextBlock> cache = new HashMap<>();

	protected final TextBlock getTextBlockSLOW(String text, FontConfiguration fontConfiguration) {
		final TextBlockKey key = new TextBlockKey(text, fontConfiguration);
		return cache.computeIfAbsent(key, k -> Display.getWithNewlines(getPragma(), text).create(fontConfiguration,
				HorizontalAlignment.LEFT, new SpriteContainerEmpty()));
	}

	protected final FontConfiguration getFontConfigurationSLOW(SName param, boolean bold, HColor color) {
		final UFont font = timelineStyle.getFont(param);
		return getFontConfiguration(font, bold, color);
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
		ug = timelineStyle.applyVerticalSeparatorStyle(ug);
		for (LocalDate day = getMinDay(); day.compareTo(getMaxDay()) <= 0; day = day.plusDays(1))
			if (isBold(day))
				drawVline(ug, getTimeScale().getPosition(TimePoint.ofStartOfDay(day)),
						getFullHeaderHeight(ug.getStringBounder()), totalHeightWithoutFooter);

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
