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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.timingdiagram.graphic.IntricatedPoint;
import net.sourceforge.plantuml.utils.Position;

public class PlayerAnalog extends Player {

	private final SortedMap<TimeTick, Double> values = new TreeMap<TimeTick, Double>();

	private final List<TimeConstraint> constraints = new ArrayList<>();

	private final double ymargin = 8;
	private Double initialState;
	private Double start;
	private Double end;
	private Integer ticksEvery;

	public PlayerAnalog(String code, ISkinParam skinParam, TimingRuler ruler, boolean compact, Stereotype stereotype) {
		super(code, skinParam, ruler, compact, stereotype);
		this.suggestedHeight = 100;
	}

	private double getMin() {
		if (start != null)
			return start;

		double min = 0;
		for (Double val : values.values())
			min = Math.min(min, val);

		return min;
	}

	private double getMax() {
		if (end != null)
			return end;

		double max = 0;
		for (Double val : values.values())
			max = Math.max(max, val);

		if (max == 0)
			return 10;

		return max;
	}

	@Override
	public double getFullHeight(StringBounder stringBounder) {
		return getHeightForConstraints(stringBounder) + suggestedHeight;
	}

	@Override
	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		final double x = ruler.getPosInPixel(tick);
		final double value = getValueAt(stringBounder, tick);
		return new IntricatedPoint(new XPoint2D(x, getYpos(stringBounder, value)),
				new XPoint2D(x, getYpos(stringBounder, value)));
	}

	private double getValueAt(StringBounder stringBounder, TimeTick tick) {
		final Double result = values.get(tick);
		if (result != null)
			return result;

		Entry<TimeTick, Double> last = null;
		for (Entry<TimeTick, Double> ent : values.entrySet()) {
			if (ent.getKey().compareTo(tick) > 0) {
				final double v2 = ent.getValue();
				if (last == null)
					return v2;

				final double t2 = ent.getKey().getTime().doubleValue();
				final double v1 = last.getValue();
				final double t1 = last.getKey().getTime().doubleValue();
				final double p = (tick.getTime().doubleValue() - t1) / (t2 - t1);
				return v1 + (v2 - v1) * p;
			}
			last = ent;
		}
		return last.getValue();
	}

	@Override
	public void addNote(TimeTick now, Display note, Position position) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void defineState(String stateCode, String label) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setState(TimeTick now, String comment, Colors color, String... valueString) {
		final double value = getState(valueString[0]);
		if (now == null)
			this.initialState = value;
		else
			this.values.put(now, value);

		if (this.initialState == null)
			this.initialState = value;

	}

	private double getState(String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			Logme.error(e);
			return 0;
		}
	}

	@Override
	public void createConstraint(TimeTick tick1, TimeTick tick2, String message, ArrowConfiguration config) {
		this.constraints.add(new TimeConstraint(1, tick1, tick2, message, skinParam, config));
	}

	private double getYpos(StringBounder stringBounder, double value) {
		final double y = (value - getMin()) * (suggestedHeight - 2 * ymargin) / (getMax() - getMin());
		return getHeightForConstraints(stringBounder) + suggestedHeight - ymargin - y;
	}

	public TextBlock getPart1(final double fullAvailableWidth, final double specialVSpace) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				drawPart1(ug, fullAvailableWidth, specialVSpace);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				final XDimension2D dim = getTitle().calculateDimension(stringBounder);
				return dim.delta(5 + getMaxWidthForTicks(stringBounder), 0);
			}
		};
	}

	private double getMaxWidthForTicks(StringBounder stringBounder) {
		if (ticksEvery == null)
			return Math.max(getWidthLabel(stringBounder, getMin()), getWidthLabel(stringBounder, getMax()));

		double result = 0;
		final int first = (int) Math.ceil(getMin());
		final int last = (int) Math.floor(getMax());
		for (int i = first; i <= last; i++)
			if (i % ticksEvery == 0)
				result = Math.max(result, getWidthLabel(stringBounder, i));

		return result;
	}

	private void drawPart1(UGraphic ug, double fullAvailableWidth, double specialVSpace) {
		final StringBounder stringBounder = ug.getStringBounder();
		final TextBlock title = getTitle();
		final XDimension2D dim = title.calculateDimension(stringBounder);
		final double y = (getFullHeight(stringBounder) - dim.getHeight()) / 2;
		title.drawU(ug.apply(UTranslate.dy(y)));

		if (ticksEvery == null) {
			drawScaleLabel(ug.apply(UTranslate.dy(specialVSpace)), getMin(), fullAvailableWidth);
			drawScaleLabel(ug.apply(UTranslate.dy(specialVSpace)), getMax(), fullAvailableWidth);
		} else {
			final int first = (int) Math.ceil(getMin());
			final int last = (int) Math.floor(getMax());
			for (int i = first; i <= last; i++)
				if (i % ticksEvery == 0)
					drawScaleLabel(ug.apply(UTranslate.dy(specialVSpace)), i, fullAvailableWidth);

		}

	}

	private double getWidthLabel(StringBounder stringBounder, double value) {
		final TextBlock label = getTextBlock(value);
		final XDimension2D dim = label.calculateDimension(stringBounder);
		return dim.getWidth();
	}

	private void drawScaleLabel(UGraphic ug, double value, double fullAvailableWidth) {
		final TextBlock label = getTextBlock(value);
		final XDimension2D dim = label.calculateDimension(ug.getStringBounder());
		ug = ug.apply(UTranslate.dx(fullAvailableWidth - dim.getWidth() - 2));
		label.drawU(ug.apply(UTranslate.dy(getYpos(ug.getStringBounder(), value) - dim.getHeight() / 2)));
	}

	private TextBlock getTextBlock(double value) {
		final Display display = Display.getWithNewlines("" + value);
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	private void drawTickHlines(UGraphic ug) {
		ug = TimingRuler.applyForVLines(ug, getStyle(), skinParam);
		final int first = (int) Math.ceil(getMin());
		final int last = (int) Math.floor(getMax());
		final ULine hline = ULine.hline(ruler.getWidth());
		for (int i = first; i <= last; i++)
			if (i % ticksEvery == 0)
				ug.apply(UTranslate.dy(getYpos(ug.getStringBounder(), i))).draw(hline);

	}

	@Override
	public UDrawable getPart2() {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				if (ticksEvery != null)
					drawTickHlines(ug);

				ug = getContext().apply(ug);
				double lastx = 0;
				double lastValue = initialState == null ? 0 : initialState;
				for (Map.Entry<TimeTick, Double> ent : values.entrySet()) {
					final double y1 = getYpos(ug.getStringBounder(), lastValue);
					final double y2 = getYpos(ug.getStringBounder(), ent.getValue());
					final double x = ruler.getPosInPixel(ent.getKey());
					ug.apply(new UTranslate(lastx, y1)).draw(new ULine(x - lastx, y2 - y1));
					lastx = x;
					lastValue = ent.getValue();
				}
				ug.apply(new UTranslate(lastx, getYpos(ug.getStringBounder(), lastValue)))
						.draw(ULine.hline(ruler.getWidth() - lastx));

				drawConstraints(ug.apply(UTranslate.dy(getHeightForConstraints(ug.getStringBounder()))));

			}
		};
	}

	public void setStartEnd(double start, double end) {
		this.start = start;
		this.end = end;
	}

	public void setTicks(int ticksEvery) {
		this.ticksEvery = ticksEvery;
	}

	@Override
	protected StyleSignature getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.timingDiagram, SName.analog)
				.withTOBECHANGED(stereotype);
	}

	private void drawConstraints(final UGraphic ug) {
		for (TimeConstraint constraint : constraints) {
			constraint.drawU(ug, ruler);
		}
	}

	private double getHeightForConstraints(StringBounder stringBounder) {
		return TimeConstraint.getHeightForConstraints(stringBounder, constraints);
	}

}
