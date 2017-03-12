/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TimingRuler {

	private final List<TimeTick> times = new ArrayList<TimeTick>();
	private int highestCommonFactor = -1;
	private final ISkinParam skinParam;

	private final double tickIntervalInPixels = 50;

	public TimingRuler(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	public void addTime(TimeTick time) {
		times.add(time);
		int tick = time.getTime();
		if (tick > 0) {
			if (highestCommonFactor == -1) {
				highestCommonFactor = time.getTime();
			} else {
				highestCommonFactor = computeHighestCommonFactor(highestCommonFactor, time.getTime());
			}
		}
		// System.err.println("highestCommonFactor=" + highestCommonFactor);
	}

	private FontConfiguration getFontConfiguration() {
		return new FontConfiguration(skinParam, FontParam.ACTIVITY, null);
	}

	private TextBlock getTimeTextBlock(TimeTick time) {
		final Display display = Display.getWithNewlines("" + time.getTime());
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	public void draw(UGraphic ug) {
		ug = ug.apply(new UStroke(2.0));
		final int nb = getNbTick();
		// System.err.println("nb=" + nb);
		final double tickHeight = 5;
		final ULine line = new ULine(0, tickHeight);
		for (int i = 0; i <= nb; i++) {
			ug.apply(new UTranslate(tickIntervalInPixels * i, 0)).draw(line);
		}
		ug.draw(new ULine(nb * tickIntervalInPixels, 0));

		for (TimeTick tick : times) {
			final TextBlock text = getTimeTextBlock(tick);
			final Dimension2D dim = text.calculateDimension(ug.getStringBounder());
			text.drawU(ug.apply(new UTranslate(getPosInPixel(tick) - dim.getWidth() / 2, tickHeight + 1)));
		}

	}

	private int getNbTick() {
		return 1 + getMax().getTime() / highestCommonFactor;
	}

	public double getWidth() {
		return getPosInPixel((getNbTick()) * highestCommonFactor);
	}

	private TimeTick getMax() {
		if (times.size() == 0) {
			throw new IllegalStateException("Empty list!");
		}
		return times.get(times.size() - 1);
	}

	private static int computeHighestCommonFactor(int a, int b) {
		int r = a;
		while (r != 0) {
			r = a % b;
			a = b;
			b = r;
		}
		return (Math.abs(a));
	}

	public final double getPosInPixel(int time) {
		return 1.0 * time / highestCommonFactor * tickIntervalInPixels;
	}

	public double getPosInPixel(TimeTick when) {
		return getPosInPixel(when.getTime());
	}

	public double getMaxPosInPixel() {
		return getPosInPixel(getMax());
	}

}
