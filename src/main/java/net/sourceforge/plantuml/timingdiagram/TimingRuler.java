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
 */
package net.sourceforge.plantuml.timingdiagram;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class TimingRuler {

	private final SortedSet<TimeTick> times = new TreeSet<>();

	private final ISkinParam skinParam;

	private long tickIntervalInPixels = 50;
	private long forcedTickUnitary;

	private TimingFormat format = TimingFormat.DECIMAL;

	static UGraphic applyForVLines(UGraphic ug, Style style, ISkinParam skinParam) {
		final UStroke stroke = new UStroke(3, 5, 0.5);
		final HColor color = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());

		return ug.apply(stroke).apply(color);
	}

	public void ensureNotEmpty() {
		if (times.size() == 0)
			this.times.add(new TimeTick(BigDecimal.ZERO, TimingFormat.DECIMAL));

		if (getMax().getTime().signum() > 0 && getMin().getTime().signum() < 0)
			this.times.add(new TimeTick(BigDecimal.ZERO, TimingFormat.DECIMAL));

	}

	public TimingRuler(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	public void scaleInPixels(long tick, long pixel) {
		if (pixel <= 0 || tick <= 0)
			throw new IllegalArgumentException();
		this.tickIntervalInPixels = pixel;
		this.forcedTickUnitary = tick;
	}

	private long getTickUnitary() {
		if (forcedTickUnitary == 0) {
			final long highestCommonFactor = highestCommonFactor();
			if (highestCommonFactor > 1)
				return highestCommonFactor;
			/*
			 * Normally, we use the highest common factor (HCF) of significant timing
			 * values. However, if the HCF is 1 (implying no suitable common scale), we fall
			 * back to an approximate calculation based on the diagram width in pixels and
			 * the time range (delta). This ensures readability and prevents extremely long
			 * diagrams.
			 */
			final double delta = getMax().getTime().doubleValue() - getMin().getTime().doubleValue();
			final double totalWidth = 1000.0;
			return Math.round(1 + (tickIntervalInPixels * delta / totalWidth));
		}
		return forcedTickUnitary;
	}

	public double getWidth() {
		if (times.size() == 0)
			return 100;
		final double delta = getMax().getTime().doubleValue() - getMin().getTime().doubleValue();
		return (delta / getTickUnitary() + 1) * tickIntervalInPixels;
	}

	private long highestCommonFactorInternal = -1;

	private long highestCommonFactor() {
		if (highestCommonFactorInternal == -1)
			for (long tick : getAbsolutesTicks())
				if (highestCommonFactorInternal == -1)
					highestCommonFactorInternal = tick;
				else
					highestCommonFactorInternal = computeHighestCommonFactor(highestCommonFactorInternal, tick);

		return highestCommonFactorInternal;
	}

	private Set<Long> getAbsolutesTicks() {
		final Set<Long> result = new TreeSet<>(new Comparator<Long>() {
			public int compare(Long o1, Long o2) {
				return o2.compareTo(o1);
			}
		});
		for (TimeTick time : times) {
			final long value = Math.abs(time.getTime().longValue());
			if (value > 0)
				result.add(value);

		}
		return result;
	}

	private int getNbTick() {
		if (times.size() == 0)
			return 1;

		final long delta = getMax().getTime().longValue() - getMin().getTime().longValue();
		return Math.min(1000, (int) (1 + delta / getTickUnitary()));
	}

	public final double getPosInPixel(TimeTick when) {
		return getPosInPixelInternal(when.getTime().doubleValue());
	}

	private double getPosInPixelInternal(double time) {
		time -= getMin().getTime().doubleValue();
		return time / getTickUnitary() * tickIntervalInPixels;
	}

	public void addTime(TimeTick time) {
		this.highestCommonFactorInternal = -1;
		times.add(time);
		if (time.getFormat() != TimingFormat.DECIMAL)
			this.format = time.getFormat();

	}

	private Style getStyleTimegrid() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram, SName.timegrid)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private Style getStyleTimeline() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram, SName.timeline)
				.getMergedStyle(skinParam.getCurrentStyleBuilder());
	}

	private TextBlock getTimeTextBlock(long time) {
		return getTimeTextBlock(format.formatTime(time));
	}

	private TextBlock getTimeTextBlock(String string) {
		final Display display = Display.getWithNewlines(skinParam.getPragma(), string);
		final FontConfiguration fontConfiguration = FontConfiguration.create(skinParam, getStyleTimeline());
		return display.create(fontConfiguration, HorizontalAlignment.LEFT, skinParam);
	}

	public void drawTimeAxis(UGraphic ug, TimeAxisStategy timeAxisStategy, Map<String, TimeTick> codes) {
		if (timeAxisStategy == TimeAxisStategy.HIDDEN)
			return;

		final Style styleTimeline = getStyleTimeline();
		final Style styleTimegrid = getStyleTimegrid();

		final HColor color = styleTimeline.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final UStroke stroke = styleTimeline.getStroke();

		ug = ug.apply(stroke).apply(color);

		if (timeAxisStategy == TimeAxisStategy.AUTOMATIC)
			drawTimeAxisAutomatic(ug);
		else
			drawTimeAxisManual(ug, codes);

	}

	private void drawTimeAxisManual(UGraphic ug, Map<String, TimeTick> codes) {
		final double tickHeight = 5;
		final ULine line = ULine.vline(tickHeight);
		final double firstTickPosition = getPosInPixelInternal(getFirstPositiveOrZeroValue().doubleValue());
		int nb = 0;
		while (firstTickPosition + nb * tickIntervalInPixels <= getWidth())
			nb++;

		ug.apply(UTranslate.dx(firstTickPosition)).draw(ULine.hline((nb - 1) * tickIntervalInPixels));

		for (TimeTick tick : times) {
			ug.apply(UTranslate.dx(getPosInPixel(tick))).draw(line);
			final String label = getLabel(tick, codes);
			if (label.length() == 0)
				continue;
			final TextBlock text = getTimeTextBlock(label);
			final XDimension2D dim = text.calculateDimension(ug.getStringBounder());
			text.drawU(ug.apply(new UTranslate(getPosInPixel(tick) - dim.getWidth() / 2, tickHeight + 1)));

		}
	}

	private void drawTimeAxisAutomatic(UGraphic ug) {
		final double tickHeight = 5;
		final ULine line = ULine.vline(tickHeight);
		final double firstTickPosition = getPosInPixelInternal(getFirstPositiveOrZeroValue().doubleValue());
		int nb = 0;
		while (firstTickPosition + nb * tickIntervalInPixels <= getWidth()) {
			ug.apply(UTranslate.dx(firstTickPosition + nb * tickIntervalInPixels)).draw(line);
			nb++;
		}
		ug.apply(UTranslate.dx(firstTickPosition)).draw(ULine.hline((nb - 1) * tickIntervalInPixels));

		for (long round : roundValues()) {
			final TextBlock text = getTimeTextBlock(round);
			final XDimension2D dim = text.calculateDimension(ug.getStringBounder());
			text.drawU(ug.apply(new UTranslate(getPosInPixelInternal(round) - dim.getWidth() / 2, tickHeight + 1)));
		}
	}

	private String getLabel(TimeTick tick, Map<String, TimeTick> codes) {
		for (Entry<String, TimeTick> ent : codes.entrySet())
			if (tick.equals(ent.getValue()))
				return ent.getKey();

		return format.formatTime(tick.getTime());
	}

	private BigDecimal getFirstPositiveOrZeroValue() {
		for (TimeTick time : times)
			if (time.getTime().signum() >= 0)
				return time.getTime();

		throw new IllegalStateException();
	}

	private Collection<Long> roundValues() {
		final SortedSet<Long> result = new TreeSet<>();
		if (forcedTickUnitary == 0) {
			for (TimeTick tick : times) {
				final long round = tick.getTime().longValue();
				result.add(round);
			}
		} else {
			final int nb = getNbTick();
			for (int i = 0; i <= nb; i++) {
				final long round = tickToTime(i);
				result.add(round);
			}
		}
		if (result.first() < 0 && result.last() > 0)
			result.add(0L);

		return result;
	}

	private long tickToTime(int i) {
		return forcedTickUnitary * i + getMin().getTime().longValue();
	}

	public void drawVlines(UGraphic ug, double height) {
		ug = applyForVLines(ug, getStyleTimegrid(), skinParam);
		final ULine line = ULine.vline(height);
		final int nb = getNbTick();
		for (int i = 0; i <= nb; i++)
			ug.apply(UTranslate.dx(tickIntervalInPixels * i)).draw(line);

	}

	public double getHeight(StringBounder stringBounder) {
		return getTimeTextBlock(0).calculateDimension(stringBounder).getHeight();
	}

	private TimeTick getMax() {
		return times.last();
	}

	private TimeTick getMin() {
		return times.first();
	}

	private static long computeHighestCommonFactor(long a, long b) {
		long r = a;
		while (r != 0) {
			r = a % b;
			a = b;
			b = r;
		}
		return (Math.abs(a));
	}

}
