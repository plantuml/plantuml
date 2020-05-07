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
 */
package net.sourceforge.plantuml.timingdiagram;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.timingdiagram.graphic.IntricatedPoint;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class PlayerAnalog extends Player {

	private final SortedMap<TimeTick, Double> values = new TreeMap<TimeTick, Double>();
	private final double ymargin = 8;
	private Double initialState;
	private Double start;
	private Double end;
	private Integer ticksEvery;

	public PlayerAnalog(String code, ISkinParam skinParam, TimingRuler ruler, boolean compact) {
		super(code, skinParam, ruler, compact);
		this.suggestedHeight = 100;
	}

	private double getMin() {
		if (start != null) {
			return start;
		}
		return 0;
	}

	private double getMax() {
		if (end != null) {
			return end;
		}
		double max = 0;
		for (Double val : values.values()) {
			max = Math.max(max, val);
		}
		if (max == 0) {
			return 10;
		}
		return max;
	}

	public double getFullHeight(StringBounder stringBounder) {
		return suggestedHeight;
	}

	public void drawFrameTitle(UGraphic ug) {
	}

	private SymbolContext getContext() {
		return new SymbolContext(HColorUtils.COL_D7E0F2, HColorUtils.COL_038048).withStroke(new UStroke(1.5));
	}

	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		final double x = ruler.getPosInPixel(tick);
		final double value = getValueAt(tick);
		return new IntricatedPoint(new Point2D.Double(x, getYpos(value)), new Point2D.Double(x, getYpos(value)));
	}

	private double getValueAt(TimeTick tick) {
		final Double result = values.get(tick);
		if (result != null) {
			return result;
		}
		Entry<TimeTick, Double> last = null;
		for (Entry<TimeTick, Double> ent : values.entrySet()) {
			if (ent.getKey().compareTo(tick) > 0) {
				final double v2 = ent.getValue();
				if (last == null) {
					return v2;
				}
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

	public void addNote(TimeTick now, Display note, Position position) {
		throw new UnsupportedOperationException();
	}

	public void defineState(String stateCode, String label) {
		throw new UnsupportedOperationException();
	}

	public void setState(TimeTick now, String comment, Colors color, String... valueString) {
		final double value = getState(valueString[0]);
		if (now == null) {
			this.initialState = value;
		} else {
			this.values.put(now, value);
		}
		if (this.initialState == null) {
			this.initialState = value;
		}
	}

	private double getState(String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void createConstraint(TimeTick tick1, TimeTick tick2, String message) {
		throw new UnsupportedOperationException();
	}

	private double getYpos(double value) {
		final double fullHeight = getFullHeight(null);
		final double y = (value - getMin()) * (fullHeight - 2 * ymargin) / (getMax() - getMin());
		return fullHeight - ymargin - y;
	}

	public TextBlock getPart1(final double fullAvailableWidth, final double specialVSpace) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				drawPart1(ug, fullAvailableWidth, specialVSpace);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dim = getTitle().calculateDimension(stringBounder);
				return Dimension2DDouble.delta(dim, 5 + getMaxWidthForTicks(stringBounder), 0);
			}
		};
	}

	private double getMaxWidthForTicks(StringBounder stringBounder) {
		if (ticksEvery == null) {
			return Math.max(getWidthLabel(stringBounder, getMin()), getWidthLabel(stringBounder, getMax()));
		}
		double result = 0;
		final int first = (int) Math.ceil(getMin());
		final int last = (int) Math.floor(getMax());
		for (int i = first; i <= last; i++) {
			if (i % ticksEvery == 0) {
				result = Math.max(result, getWidthLabel(stringBounder, i));
			}
		}
		return result;
	}

	private void drawPart1(UGraphic ug, double fullAvailableWidth, double specialVSpace) {
		final StringBounder stringBounder = ug.getStringBounder();
		final TextBlock title = getTitle();
		final Dimension2D dim = title.calculateDimension(stringBounder);
		final double y = (getFullHeight(stringBounder) - dim.getHeight()) / 2;
		title.drawU(ug.apply(UTranslate.dy(y)));

		if (ticksEvery == null) {
			drawScaleLabel(ug.apply(UTranslate.dy(specialVSpace)), getMin(), fullAvailableWidth);
			drawScaleLabel(ug.apply(UTranslate.dy(specialVSpace)), getMax(), fullAvailableWidth);
		} else {
			final int first = (int) Math.ceil(getMin());
			final int last = (int) Math.floor(getMax());
			for (int i = first; i <= last; i++) {
				if (i % ticksEvery == 0) {
					drawScaleLabel(ug.apply(UTranslate.dy(specialVSpace)), i, fullAvailableWidth);
				}
			}
		}

	}

	private double getWidthLabel(StringBounder stringBounder, double value) {
		final TextBlock label = getTextBlock(value);
		final Dimension2D dim = label.calculateDimension(stringBounder);
		return dim.getWidth();
	}

	private void drawScaleLabel(UGraphic ug, double value, double fullAvailableWidth) {
		final TextBlock label = getTextBlock(value);
		final Dimension2D dim = label.calculateDimension(ug.getStringBounder());
		ug = ug.apply(UTranslate.dx(fullAvailableWidth - dim.getWidth() - 2));
		label.drawU(ug.apply(UTranslate.dy(getYpos(value) - dim.getHeight() / 2)));
	}

	private TextBlock getTextBlock(double value) {
		final Display display = Display.getWithNewlines("" + value);
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	private void drawTickHlines(UGraphic ug) {
		ug = TimingRuler.applyForVLines(ug);
		final int first = (int) Math.ceil(getMin());
		final int last = (int) Math.floor(getMax());
		final ULine hline = ULine.hline(ruler.getWidth());
		for (int i = first; i <= last; i++) {
			if (i % ticksEvery == 0) {
				ug.apply(UTranslate.dy(getYpos(i))).draw(hline);
			}
		}

	}

	public UDrawable getPart2() {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				if (ticksEvery != null) {
					drawTickHlines(ug);
				}
				ug = getContext().apply(ug);
				double lastx = 0;
				double lastValue = initialState == null ? 0 : initialState;
				for (Map.Entry<TimeTick, Double> ent : values.entrySet()) {
					final double y1 = getYpos(lastValue);
					final double y2 = getYpos(ent.getValue());
					final double x = ruler.getPosInPixel(ent.getKey());
					ug.apply(new UTranslate(lastx, y1)).draw(new ULine(x - lastx, y2 - y1));
					lastx = x;
					lastValue = ent.getValue();
				}
				ug.apply(new UTranslate(lastx, getYpos(lastValue))).draw(ULine.hline(ruler.getWidth() - lastx));
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

}
