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

import java.math.BigDecimal;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.timingdiagram.graphic.IntricatedPoint;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class PlayerClock extends Player {

	private final int period;
	private final int pulse;
	private final double ymargin = 8;

	public PlayerClock(ISkinParam skinParam, TimingRuler ruler, int period, int pulse, boolean compact) {
		super("", skinParam, ruler, compact);
		this.period = period;
		this.pulse = pulse;
		this.suggestedHeight = 30;
	}

	public double getFullHeight(StringBounder striWngBounder) {
		return suggestedHeight;
	}

	public void drawFrameTitle(UGraphic ug) {
	}

	private SymbolContext getContext() {
		return new SymbolContext(HColorUtils.COL_D7E0F2, HColorUtils.COL_038048).withStroke(new UStroke(1.5));
	}

	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		throw new UnsupportedOperationException();
	}

	public void addNote(TimeTick now, Display note, Position position) {
		throw new UnsupportedOperationException();
	}

	public void defineState(String stateCode, String label) {
		throw new UnsupportedOperationException();
	}

	public void setState(TimeTick now, String comment, Colors color, String... states) {
		throw new UnsupportedOperationException();
	}

	public void createConstraint(TimeTick tick1, TimeTick tick2, String message) {
		throw new UnsupportedOperationException();
	}

	private double getPulseCoef() {
		if (pulse == 0) {
			return 0.5;
		}
		return 1.0 * pulse / period;
	}

	public final int getPeriod() {
		return period;
	}

	public TextBlock getPart1(double fullAvailableWidth, double specialVSpace) {
		return TextBlockUtils.empty(0, 0);
	}

	public UDrawable getPart2() {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				ug = getContext().apply(ug);
				final ULine vline = ULine.vline(getFullHeight(ug.getStringBounder()) - 2 * ymargin);
				int i = 0;
				double lastx = -Double.MAX_VALUE;
				while (i < 1000) {
					final double x = ruler
							.getPosInPixel(new TimeTick(new BigDecimal(i * period), TimingFormat.DECIMAL));
					if (x > ruler.getWidth()) {
						return;
					}
					i++;
					if (x > lastx) {
						final double dx = x - lastx;
						final ULine hline1 = ULine.hline(dx * getPulseCoef());
						final ULine hline2 = ULine.hline(dx * (1 - getPulseCoef()));
						ug.apply(new UTranslate(lastx, ymargin)).draw(vline);
						ug.apply(new UTranslate(lastx, ymargin)).draw(hline1);
						final double x2 = lastx + dx * getPulseCoef();
						ug.apply(new UTranslate(x2, ymargin)).draw(vline);
						ug.apply(new UTranslate(x2, ymargin + vline.getDY())).draw(hline2);
					}
					lastx = x;
				}
			}
		};
	}

}
