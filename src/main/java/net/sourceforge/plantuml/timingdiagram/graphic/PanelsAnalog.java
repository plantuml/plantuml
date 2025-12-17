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
package net.sourceforge.plantuml.timingdiagram.graphic;

import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.timingdiagram.TimeConstraint;
import net.sourceforge.plantuml.timingdiagram.TimeSeries;
import net.sourceforge.plantuml.timingdiagram.TimeTick;
import net.sourceforge.plantuml.timingdiagram.TimingRuler;

public class PanelsAnalog extends Panels {

	private final TimeSeries timeSeries;
	private final Double initialState;
	private final Integer ticksEvery;

	public PanelsAnalog(TimingRuler ruler, ISkinParam skinParam, int suggestedHeight, Style style, TimeSeries timeSeries,
			List<TimeConstraint> constraints, Double initialState, Integer ticksEvery) {
		super(ruler, skinParam, suggestedHeight, style, null, constraints);

		this.timeSeries = timeSeries;
		this.initialState = initialState;
		this.ticksEvery = ticksEvery;
	}

	@Override
	public double getFullHeight(StringBounder stringBounder) {
		return getHeightForConstraints(stringBounder) + suggestedHeight;
	}

	@Override
	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		if (tick == null)
			return null;
		final double x = ruler.getPosInPixel(tick);
		final double value = timeSeries.getValueAt(tick);
		return new IntricatedPoint(new XPoint2D(x, getYpos(stringBounder, value)),
				new XPoint2D(x, getYpos(stringBounder, value)));
	}

	private double getYpos(StringBounder stringBounder, double value) {
		final double y = (value - timeSeries.getMin()) * (suggestedHeight - 2 * MARGIN_Y)
				/ (timeSeries.getMax() - timeSeries.getMin());
		return getHeightForConstraints(stringBounder) + suggestedHeight - MARGIN_Y - y;
	}

	@Override
	public void drawLeftPanel(UGraphic ug, double fullAvailableWidth) {
		if (ticksEvery == null) {
			drawScaleLabel(ug, timeSeries.getMin(), fullAvailableWidth);
			drawScaleLabel(ug, timeSeries.getMax(), fullAvailableWidth);
		} else {
			final int first = (int) Math.ceil(timeSeries.getMin());
			final int last = (int) Math.floor(timeSeries.getMax());
			for (int i = first; i <= last; i++)
				if (i % ticksEvery == 0)
					drawScaleLabel(ug, i, fullAvailableWidth);
		}
	}

	@Override
	public final double getLeftPanelWidth(StringBounder stringBounder) {
		return LEFT_PANEL_MIN_WIDTH + getMaxWidthForTicks(stringBounder);
	}

	private double getMaxWidthForTicks(StringBounder stringBounder) {
		if (ticksEvery == null)
			return Math.max(getWidthLabel(stringBounder, timeSeries.getMin()),
					getWidthLabel(stringBounder, timeSeries.getMax()));

		double result = 0;
		final int first = (int) Math.ceil(timeSeries.getMin());
		final int last = (int) Math.floor(timeSeries.getMax());
		for (int i = first; i <= last; i++)
			if (i % ticksEvery == 0)
				result = Math.max(result, getWidthLabel(stringBounder, i));

		return result;
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
		final String formattedValue = timeSeries.getDisplayValue(value);
		final Display display = Display.getWithNewlines(skinParam.getPragma(), formattedValue);
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	private void drawTickHlines(UGraphic ug) {
		ug = TimingRuler.applyForVLines(ug, style, skinParam);
		final int first = (int) Math.ceil(timeSeries.getMin());
		final int last = (int) Math.floor(timeSeries.getMax());
		final ULine hline = ULine.hline(ruler.getWidth());
		for (int i = first; i <= last; i++)
			if (i % ticksEvery == 0)
				ug.apply(UTranslate.dy(getYpos(ug.getStringBounder(), i))).draw(hline);
	}

	@Override
	public void drawRightPanel(UGraphic ug) {
		if (ticksEvery != null)
			drawTickHlines(ug);

		ug = getContext().apply(ug);
		double lastx = 0;
		double lastValue = initialState == null ? 0 : initialState;
		for (Map.Entry<TimeTick, Double> ent : timeSeries.entrySet()) {
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

	public void setBounds(String min, String max) {
		timeSeries.setBounds(min, max);
	}

}
