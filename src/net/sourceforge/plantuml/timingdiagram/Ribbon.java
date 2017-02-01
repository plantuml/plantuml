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
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Ribbon implements TimeDrawing {

	private final List<ChangeState> changes = new ArrayList<ChangeState>();

	private final double delta = 12;
	private final ISkinParam skinParam;
	private final TimingRuler ruler;

	public Ribbon(TimingRuler ruler, ISkinParam skinParam) {
		this.ruler = ruler;
		this.skinParam = skinParam;
	}

	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		final double x = ruler.getPosInPixel(tick);
		final double y = delta * 0.5;
		for (ChangeState change : changes) {
			if (change.getWhen().compareTo(tick) == 0) {
				return new IntricatedPoint(new Point2D.Double(x, y), new Point2D.Double(x, y));
			}
		}
		return new IntricatedPoint(new Point2D.Double(x, y - delta), new Point2D.Double(x, y + delta));
	}

	public void addChange(ChangeState change) {
		this.changes.add(change);
	}

	private double getPosInPixel(ChangeState change) {
		return ruler.getPosInPixel(change.getWhen());
	}

	private FontConfiguration getFontConfiguration() {
		return new FontConfiguration(skinParam, FontParam.ACTIVITY, null);
	}

	private TextBlock getStateTextBlock(ChangeState state) {
		final Display display = Display.getWithNewlines(state.getState());
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(new UTranslate(0, 0.5 * delta));

		// System.err.println("changes=" + changes);
		for (int i = 0; i < changes.size() - 1; i++) {
			final double a = getPosInPixel(changes.get(i));
			final double b = getPosInPixel(changes.get(i + 1));
			assert b > a;
			draw1(ug.apply(new UTranslate(a, 0)), b - a, true);
			draw2(ug.apply(new UTranslate(a, 0)), b - a, true);
		}
		final double a = getPosInPixel(changes.get(changes.size() - 1));
		draw1(ug.apply(new UTranslate(a, 0)), ruler.getWidth() - a, false);
		draw2(ug.apply(new UTranslate(a, 0)), ruler.getWidth() - a, false);

		for (ChangeState change : changes) {
			final TextBlock state = getStateTextBlock(change);
			final Dimension2D dim = state.calculateDimension(ug.getStringBounder());
			final double x = ruler.getPosInPixel(change.getWhen());
			state.drawU(ug.apply(new UTranslate(x + getDelta(), -dim.getHeight() / 2)));
		}

	}

	private void draw1(UGraphic ug, double len, boolean withEnd) {
		ug = ug.apply(new UChangeColor(HtmlColorUtils.COL_038048)).apply(new UStroke(1.5));
		ug.draw(new ULine(delta, delta));
		ug.apply(new UTranslate(delta, delta)).draw(new ULine(len - 2 * delta, 0));
		if (withEnd) {
			ug.apply(new UTranslate(len - delta, delta)).draw(new ULine(delta, -delta));
		}
	}

	private void draw2(UGraphic ug, double len, boolean withEnd) {
		ug = ug.apply(new UChangeColor(HtmlColorUtils.COL_038048)).apply(new UStroke(1.5));
		ug.draw(new ULine(delta, -delta));
		ug.apply(new UTranslate(delta, -delta)).draw(new ULine(len - 2 * delta, 0));
		if (withEnd) {
			ug.apply(new UTranslate(len - delta, -delta)).draw(new ULine(delta, delta));
		}
	}

	public double getHeight() {
		return 3 * delta;
	}

	public double getDelta() {
		return delta;
	}

}
