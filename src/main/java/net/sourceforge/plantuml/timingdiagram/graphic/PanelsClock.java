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

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.timingdiagram.TimeConstraint;
import net.sourceforge.plantuml.timingdiagram.TimeTick;
import net.sourceforge.plantuml.timingdiagram.TimingRuler;

public class PanelsClock extends PanelsNoLeft {

	private final int period;
	private final int pulse;
	private final int offset;

	public PanelsClock(TimingRuler ruler, ISkinParam skinParam, int suggestedHeight, Style style, int period, int pulse,
			int offset, List<TimeConstraint> constraints) {
		super(ruler, skinParam, suggestedHeight, style, null, constraints);

		this.period = period;
		this.pulse = pulse;
		this.offset = offset;
	}

	@Override
	public double getFullHeight(StringBounder stringBounder) {
		return suggestedHeight;
	}

	private double getLineHeight(StringBounder stringBounder) {
		return suggestedHeight - 2 * MARGIN_Y;
	}

	@Override
	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void drawRightPanel(UGraphic ug) {
		ug = getContext().apply(ug);

		final ULine vline = ULine.vline(getLineHeight(ug.getStringBounder()));
		ug = ug.apply(UTranslate.dy(MARGIN_Y));
		double value = 0;
		if (offset != 0) {
			drawHorizontalBetweenTimes(ug.apply(UTranslate.dy(vline.getDY())), value, offset);
			value += offset;
		}
		if (xOfTime(value) > ruler.getWidth())
			return;
		drawVline(ug, xOfTime(value), 0, vline);

		final double vpulse = pulse == 0 ? period / 2.0 : pulse;
		final double remain = period - vpulse;
		for (int i = 0; i < 1000; i++) {
			drawHorizontalBetweenTimes(ug, value, value + vpulse);
			value += vpulse;
			if (xOfTime(value) > ruler.getWidth())
				return;
			drawVline(ug, xOfTime(value), 0, vline);
			drawHorizontalBetweenTimes(ug.apply(UTranslate.dy(vline.getDY())), value, value + remain);
			value += remain;
			if (xOfTime(value) > ruler.getWidth())
				return;
			drawVline(ug, xOfTime(value), 0, vline);
		}
	}

}
