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
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class PlayerClock implements Player {

	private final TimingRuler ruler;
	private final int period;

	public PlayerClock(ISkinParam skinParam, TimingRuler ruler, int period) {
		this.ruler = ruler;
		this.period = period;
	}

	public double getHeight(StringBounder stringBounder) {
		return 30;
	}

	private SymbolContext getContext() {
		return new SymbolContext(HtmlColorUtils.COL_D7E0F2, HtmlColorUtils.COL_038048).withStroke(new UStroke(1.5));
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

	private final double ymargin = 8;

	public void drawTitle(UGraphic ug) {
	}

	public void drawContent(UGraphic ug) {
		ug = getContext().apply(ug);
		final ULine vline = new ULine(0, getHeight(ug.getStringBounder()) - 2 * ymargin);
		int i = 0;
		double lastx = -Double.MAX_VALUE;
		while (i < 1000) {
			final double x = ruler.getPosInPixel(new BigDecimal(i * period)) / 2;
			if (x > ruler.getWidth()) {
				return;
			}
			i++;
			if (x > lastx) {
				ug.apply(new UTranslate(lastx, ymargin)).draw(vline);
				ug.apply(new UTranslate(lastx, i % 2 == 0 ? ymargin : ymargin + vline.getDY())).draw(
						new ULine(x - lastx, 0));
			}
			lastx = x;
		}
	}

	public void drawLeftHeader(UGraphic ug) {

	}

	public double getWidthHeader(StringBounder stringBounder) {
		return 0;
	}

	public final int getPeriod() {
		return period;
	}

}
