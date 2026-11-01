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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleQueries;

public class TimingRuler {

	private static final double TICK_HEIGHT = 5;
	private static final double UNIT_GAP = 6;

	private final SortedSet<TimeTick> times = new TreeSet<>();

	private final ISkinParam skinParam;

	private long tickIntervalInPixels = 50;
	private long forcedTickUnitary;

	private TimingFormat format = TimingFormat.DECIMAL;

	private String unit;

	private final List<Break> breaks = new ArrayList<>();

	private static final double BREAK_IN_TICKS = 0.5;

	/**
	 * A discontinuity of the time axis: the whole range between {@code from} and
	 * {@code to} is squeezed into {@link #BREAK_IN_TICKS} tick interval.
	 */
	static private class Break {

		private final double from;
		private final double to;

		private Break(double from, double to) {
			this.from = from;
			this.to = to;
		}

		private boolean isStrictlyInside(double time) {
			return time > from && time < to;
		}

		private boolean overlaps(Break other) {
			return from < other.to && other.from < to;
		}

	}

	static public UGraphic applyForVLines(UGraphic ug, Style style, ISkinParam skinParam) {
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

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void scaleInPixels(long tick, long pixel) {
		if (pixel <= 0 || tick <= 0)
			throw new IllegalArgumentException();
		this.tickIntervalInPixels = pixel;
		this.forcedTickUnitary = tick;
	}

	private long getOptimalTickUnit() {
		if (forcedTickUnitary == 0) {
			final long hcfTickUnit = calculateHighestCommonFactor();
			final double maxDiagramWidth = 4000.0;
			if (hcfTickUnit == 1 && calculateDiagramWidth(hcfTickUnit) > maxDiagramWidth) {
				/*
				 * Typically, we determine the optimal tick unit using the highest common factor
				 * (HCF) of all significant timing values. However, when the HCF is too small
				 * (e.g., equal to 1), the resulting diagram becomes excessively wide. In such
				 * cases, we fall back to an approximate tick unit calculation based on the
				 * diagram's pixel width and the total time range, ensuring the diagram remains
				 * clear and readable.
				 */
				final double totalTimeRange = getMax().getTime().doubleValue() - getMin().getTime().doubleValue()
						- getBreaksDuration();
				return Math.round(1 + (tickIntervalInPixels * totalTimeRange / maxDiagramWidth));
			}
			return hcfTickUnit;
		}
		return forcedTickUnitary;
	}

	public double getWidth() {
		if (times.size() == 0)
			return 100;
		return calculateDiagramWidth(getOptimalTickUnit());
	}

	private double calculateDiagramWidth(final long tickUnitary) {
		return (getPosInTicks(getMax().getTime().doubleValue(), tickUnitary) + 1) * tickIntervalInPixels;
	}

	/**
	 * Position of a time on the axis, expressed in tick intervals from the
	 * smallest time. Each break takes {@link #BREAK_IN_TICKS} tick interval,
	 * whatever its duration.
	 */
	private double getPosInTicks(double time, long tickUnitary) {
		final double min = getMin().getTime().doubleValue();
		double shift = 0;
		for (Break b : breaks) {
			if (time <= b.from)
				break;
			if (time < b.to)
				return (b.from - min) / tickUnitary + shift + BREAK_IN_TICKS * (time - b.from) / (b.to - b.from);
			shift += BREAK_IN_TICKS - (b.to - b.from) / tickUnitary;
		}
		return (time - min) / tickUnitary + shift;
	}

	private double getBreaksDuration() {
		double result = 0;
		for (Break b : breaks)
			result += b.to - b.from;
		return result;
	}

	public boolean addBreak(TimeTick from, TimeTick to) {
		final Break added = new Break(from.getTime().doubleValue(), to.getTime().doubleValue());
		for (Break other : breaks)
			if (added.overlaps(other))
				return false;

		breaks.add(added);
		Collections.sort(breaks, new Comparator<Break>() {
			public int compare(Break b1, Break b2) {
				return Double.compare(b1.from, b2.from);
			}
		});
		addTime(from);
		addTime(to);
		return true;
	}

	private Break getBreakContaining(double time) {
		for (Break b : breaks)
			if (b.isStrictlyInside(time))
				return b;
		return null;
	}

	/**
	 * Regularly spaced times starting at {@code first}, jumping over breaks, up to
	 * the end of the axis.
	 */
	private List<Double> getRegularTimes(double first) {
		final long tickUnitary = getOptimalTickUnit();
		final double width = getWidth();
		final List<Double> result = new ArrayList<>();
		long i = 0;
		while (result.size() <= 1000) {
			final double time = first + i * tickUnitary;
			if (getPosInPixelInternal(time) > width)
				break;
			final Break inside = getBreakContaining(time);
			if (inside == null) {
				result.add(time);
				i++;
			} else {
				i = Math.max(i + 1, (long) Math.ceil((inside.to - first) / tickUnitary));
			}
		}
		return result;
	}

	private long highestCommonFactorInternal = -1;

	private long calculateHighestCommonFactor() {
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
		return Math.min(1000, (int) (1 + delta / getOptimalTickUnit()));
	}

	public final double getPosInPixel(TimeTick when) {
		return getPosInPixelInternal(when.getTime().doubleValue());
	}

	private double getPosInPixelInternal(double time) {
		return getPosInTicks(time, getOptimalTickUnit()) * tickIntervalInPixels;
	}

	public void addTime(TimeTick time) {
		this.highestCommonFactorInternal = -1;
		times.add(time);
		if (time.getFormat() != TimingFormat.DECIMAL)
			this.format = time.getFormat();

	}

	private Style getStyleTimegrid() {
		return skinParam.getCurrentStyleBuilder().getMergedStyle(StyleQueries.TIMINGDIAG_TIMEGRID);
	}

	private Style getStyleTimeline() {
		return skinParam.getCurrentStyleBuilder().getMergedStyle(StyleQueries.TIMINGDIAG_TIMELINE);
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

		final HColor color = styleTimeline.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final UStroke stroke = styleTimeline.getStroke();

		ug = ug.apply(stroke).apply(color);

		if (timeAxisStategy == TimeAxisStategy.AUTOMATIC)
			drawTicksAutomatic(ug);
		else
			drawTicksManual(ug);

		for (TimeLabel label : getTimeLabels(ug.getStringBounder(), timeAxisStategy, codes))
			label.text.drawU(ug.apply(new UTranslate(label.x, TICK_HEIGHT + 1)));

	}

	private void drawTicksManual(UGraphic ug) {
		final ULine line = ULine.vline(TICK_HEIGHT);
		final double firstTickPosition = getFirstTickPosition();

		drawAxisLine(ug, firstTickPosition, getAxisEnd(TimeAxisStategy.MANUAL) - firstTickPosition);

		for (TimeTick tick : times) {
			if (getBreakContaining(tick.getTime().doubleValue()) != null)
				continue;
			ug.apply(UTranslate.dx(getPosInPixel(tick))).draw(line);
		}
	}

	private void drawTicksAutomatic(UGraphic ug) {
		final ULine line = ULine.vline(TICK_HEIGHT);
		final double firstTickPosition = getFirstTickPosition();
		if (breaks.isEmpty()) {
			final double axisEnd = getAxisEnd(TimeAxisStategy.AUTOMATIC);
			for (int i = 0; firstTickPosition + i * tickIntervalInPixels <= axisEnd; i++)
				ug.apply(UTranslate.dx(firstTickPosition + i * tickIntervalInPixels)).draw(line);
			ug.apply(UTranslate.dx(firstTickPosition)).draw(ULine.hline(axisEnd - firstTickPosition));
		} else {
			final List<Double> ticks = getRegularTimes(getFirstPositiveOrZeroValue().doubleValue());
			for (double time : ticks)
				ug.apply(UTranslate.dx(getPosInPixelInternal(time))).draw(line);
			for (Break b : breaks) {
				ug.apply(UTranslate.dx(getPosInPixelInternal(b.from))).draw(line);
				ug.apply(UTranslate.dx(getPosInPixelInternal(b.to))).draw(line);
			}
			final double firstPosition = getPosInPixelInternal(ticks.get(0));
			drawAxisLine(ug, firstPosition, getAxisEnd(TimeAxisStategy.AUTOMATIC) - firstPosition);
		}
	}

	private double getFirstTickPosition() {
		return getPosInPixelInternal(getFirstPositiveOrZeroValue().doubleValue());
	}

	private double getAxisEnd(TimeAxisStategy timeAxisStategy) {
		if (timeAxisStategy == TimeAxisStategy.AUTOMATIC && breaks.isEmpty() == false) {
			final List<Double> ticks = getRegularTimes(getFirstPositiveOrZeroValue().doubleValue());
			return getPosInPixelInternal(ticks.get(ticks.size() - 1));
		}
		final double firstTickPosition = getFirstTickPosition();
		int nb = 0;
		while (firstTickPosition + nb * tickIntervalInPixels <= getWidth())
			nb++;

		return firstTickPosition + (nb - 1) * tickIntervalInPixels;
	}

	static class TimeLabel {
		private final TextBlock text;
		private final double x;
		private final double width;

		private TimeLabel(TextBlock text, double x, double width) {
			this.text = text;
			this.x = x;
			this.width = width;
		}

		private double getRight() {
			return x + width;
		}
	}

	private List<TimeLabel> getTimeLabels(StringBounder stringBounder, TimeAxisStategy timeAxisStategy,
			Map<String, TimeTick> codes) {
		final List<Double> moments = new ArrayList<>();
		final List<String> labels = new ArrayList<>();
		if (timeAxisStategy == TimeAxisStategy.AUTOMATIC) {
			for (long round : roundValues()) {
				moments.add((double) round);
				labels.add(format.formatTime(round));
			}
		} else {
			for (TimeTick tick : times) {
				if (getBreakContaining(tick.getTime().doubleValue()) != null)
					continue;
				final String label = getLabel(tick, codes);
				if (label.length() == 0)
					continue;
				moments.add(tick.getTime().doubleValue());
				labels.add(label);
			}
		}

		final List<TimeLabel> result = new ArrayList<>();
		for (int i = 0; i < labels.size(); i++) {
			final TextBlock text = getTimeTextBlock(labels.get(i));
			final double width = text.calculateDimension(stringBounder).getWidth();
			result.add(new TimeLabel(text, getLabelX(moments.get(i), width), width));
		}
		if (unit == null)
			return result;

		// The unit is right-aligned on the last tick of the axis, so that it never makes the
		// diagram wider: the time values it would overlap are removed instead
		final TextBlock unitText = getTimeTextBlock(unit);
		final double unitWidth = unitText.calculateDimension(stringBounder).getWidth();
		final double unitX = getAxisEnd(timeAxisStategy) - unitWidth;
		while (result.size() > 0 && result.get(result.size() - 1).getRight() + UNIT_GAP > unitX)
			result.remove(result.size() - 1);

		result.add(new TimeLabel(unitText, unitX, unitWidth));
		return result;
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
		} else if (breaks.isEmpty()) {
			final int nb = getNbTick();
			for (int i = 0; i <= nb; i++) {
				final long round = tickToTime(i);
				result.add(round);
			}
		} else {
			for (double time : getRegularTimes(getMin().getTime().doubleValue()))
				if (isTooCloseToBreak(time) == false)
					result.add((long) time);
		}
		if (result.first() < 0 && result.last() > 0)
			result.add(0L);

		for (Break b : breaks) {
			result.add((long) b.from);
			result.add((long) b.to);
		}
		for (Iterator<Long> it = result.iterator(); it.hasNext();)
			if (getBreakContaining(it.next()) != null)
				it.remove();

		return result;
	}

	private long tickToTime(int i) {
		return forcedTickUnitary * i + getMin().getTime().longValue();
	}

	public void drawVlines(UGraphic ug, double height) {
		ug = applyForVLines(ug, getStyleTimegrid(), skinParam);
		final ULine line = ULine.vline(height);
		if (breaks.isEmpty()) {
			final int nb = getNbTick();
			for (int i = 0; i <= nb; i++)
				ug.apply(UTranslate.dx(tickIntervalInPixels * i)).draw(line);
		} else {
			for (double time : getRegularTimes(getMin().getTime().doubleValue()))
				ug.apply(UTranslate.dx(getPosInPixelInternal(time))).draw(line);
		}
	}

	// Amplitude of the zigzag edges of a break
	private static final double BREAK_MARK = 3;

	private double getBreakStart(Break b) {
		return getPosInPixelInternal(b.from);
	}

	private double getBreakEnd(Break b) {
		return getPosInPixelInternal(b.to);
	}

	/**
	 * Labels are centred on their tick, except at the edges of a break where they
	 * are pushed away from it, so that both edges stay readable.
	 */
	private double getLabelX(double time, double labelWidth) {
		final double x = getPosInPixelInternal(time);
		for (Break b : breaks)
			if (time == b.to)
				return x;
		for (Break b : breaks)
			if (time == b.from)
				return x - labelWidth;
		return x - labelWidth / 2;
	}

	// A regular label that would collide with the label of a break edge
	private boolean isTooCloseToBreak(double time) {
		final long tickUnitary = getOptimalTickUnit();
		for (Break b : breaks) {
			if (time != b.from && Math.abs(time - b.from) < tickUnitary)
				return true;
			if (time != b.to && Math.abs(time - b.to) < tickUnitary)
				return true;
		}
		return false;
	}

	private void drawAxisLine(UGraphic ug, double x, double length) {
		final double end = x + length;
		double current = x;
		for (Break b : breaks) {
			final double start = getBreakStart(b);
			final double stop = getBreakEnd(b);
			if (stop <= current || start >= end)
				continue;
			if (start > current)
				ug.apply(UTranslate.dx(current)).draw(ULine.hline(start - current));
			current = stop;
		}
		if (current == x)
			ug.apply(UTranslate.dx(x)).draw(ULine.hline(length));
		else if (end > current)
			ug.apply(UTranslate.dx(current)).draw(ULine.hline(end - current));
	}

	/**
	 * Masks the squeezed part of each break over a player area, and draws its two
	 * zigzag edges.
	 */
	public void drawBreaks(UGraphic ug, double height, HColor back) {
		final Style styleTimeline = getStyleTimeline();
		final HColor color = styleTimeline.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		for (Break b : breaks) {
			final List<XPoint2D> left = getZigzag(getBreakStart(b), height);
			final List<XPoint2D> right = getZigzag(getBreakEnd(b), height);

			final UPath mask = UPath.none();
			for (XPoint2D pt : left)
				if (mask.isEmpty())
					mask.moveTo(pt);
				else
					mask.lineTo(pt);
			for (int i = right.size() - 1; i >= 0; i--)
				mask.lineTo(right.get(i));
			mask.closePath();
			ug.apply(back).apply(back.bg()).draw(mask);

			final UGraphic ugEdge = ug.apply(styleTimeline.getStroke()).apply(color);
			ugEdge.draw(toPath(left));
			ugEdge.draw(toPath(right));
		}
	}

	private static List<XPoint2D> getZigzag(double x, double height) {
		final List<XPoint2D> result = new ArrayList<>();
		final int nb = Math.max(1, (int) Math.round(height / (2 * BREAK_MARK)));
		for (int i = 0; i <= nb; i++) {
			final double dx;
			if (i == 0 || i == nb)
				dx = 0;
			else
				dx = i % 2 == 0 ? -BREAK_MARK / 2 : BREAK_MARK / 2;
			result.add(new XPoint2D(x + dx, height * i / nb));
		}
		return result;
	}

	private static UPath toPath(List<XPoint2D> points) {
		final UPath result = UPath.none();
		result.moveTo(points.get(0));
		for (int i = 1; i < points.size(); i++)
			result.lineTo(points.get(i));
		return result;
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


	public void setStopAt(TimeTick timeTick) {
		// WIP
		
	}

}
