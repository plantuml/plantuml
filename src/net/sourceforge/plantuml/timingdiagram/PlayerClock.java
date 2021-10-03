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
import java.math.BigDecimal;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.timingdiagram.graphic.IntricatedPoint;
import net.sourceforge.plantuml.timingdiagram.graphic.PlayerFrame;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class PlayerClock extends Player {

	private final int period;
	private final int pulse;
	private final int offset;
	private final double ymargin = 8;
	private final boolean displayTitle;

	public PlayerClock(String title, ISkinParam skinParam, TimingRuler ruler, int period, int pulse, int offset,
			boolean compact) {
		super(title, skinParam, ruler, compact);
		this.displayTitle = title.length() > 0;
		this.period = period;
		this.pulse = pulse;
		this.offset = offset;
		this.suggestedHeight = 30;
	}

	public double getFullHeight(StringBounder stringBounder) {
		return suggestedHeight + getTitleHeight(stringBounder);
	}

	private double getLineHeight(StringBounder stringBounder) {
		return suggestedHeight - 2 * ymargin;
	}

	private double getTitleHeight(StringBounder stringBounder) {
		if (displayTitle)
			return getTitle().calculateDimension(stringBounder).getHeight();
		return 0;
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

	private double getOffsetCoef() {
		return 1.0 * offset / period;
	}

	public final int getPeriod() {
		return period;
	}

	public TextBlock getPart1(double fullAvailableWidth, double specialVSpace) {
		if (displayTitle)
			return new AbstractTextBlock() {

				public void drawU(UGraphic ug) {
					new PlayerFrame(getTitle()).drawFrameTitle(ug);
				}

				public Dimension2D calculateDimension(StringBounder stringBounder) {
					return getTitle().calculateDimension(stringBounder);
				}
			};
		return TextBlockUtils.empty(0, 0);
	}

	public UDrawable getPart2() {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				ug = getContext().apply(ug);
				ug = ug.apply(UTranslate.dy(getTitleHeight(ug.getStringBounder())));
				final ULine vline = ULine.vline(getLineHeight(ug.getStringBounder()));
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

						final ULine hline1 = ULine.hline(dx * getOffsetCoef());
						final ULine hline2 = ULine.hline(dx * getPulseCoef());
						final ULine hline3 = ULine.hline(dx * (1 - getPulseCoef() - getOffsetCoef()));

						final double x2 = lastx + dx * getOffsetCoef();
						final double x3 = lastx + dx * (getOffsetCoef() + getPulseCoef());
						
						if (offset > 0)
							ug.apply(new UTranslate(lastx, ymargin + vline.getDY())).draw(hline1);
						ug.apply(new UTranslate(x2, ymargin)).draw(hline2);
						ug.apply(new UTranslate(x3, ymargin + vline.getDY())).draw(hline3);

						ug.apply(new UTranslate(x2, ymargin)).draw(vline);
						ug.apply(new UTranslate(x3, ymargin)).draw(vline);

					}
					lastx = x;
				}
			}
		};
	}

}
