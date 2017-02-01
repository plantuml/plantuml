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
import java.util.Arrays;
import java.util.Collections;
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

public class Histogram implements TimeDrawing {

	private final List<ChangeState> changes = new ArrayList<ChangeState>();

	private List<String> allStates = new ArrayList<String>();
	private final double stepHeight = 20;

	private final ISkinParam skinParam;
	private final TimingRuler ruler;

	public Histogram(TimingRuler ruler, ISkinParam skinParam) {
		this.ruler = ruler;
		this.skinParam = skinParam;
	}

	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		final double x = ruler.getPosInPixel(tick);
		final List<String> states = getStatesAt(tick);
		if (states.size() == 1) {
			final int num = getStateNumFor(states.get(0));
			return new IntricatedPoint(new Point2D.Double(x, num * stepHeight), new Point2D.Double(x, num * stepHeight));
		}
		assert states.size() == 2;
		final int num1 = getStateNumFor(states.get(0));
		final int num2 = getStateNumFor(states.get(1));
		assert num1 != num2;
		return new IntricatedPoint(new Point2D.Double(x, num1 * stepHeight), new Point2D.Double(x, num2 * stepHeight));
		// if (isTransition(tick)) {
		// return new IntricatedPoint(new Point2D.Double(x, state * stepHeight), new Point2D.Double(x, state
		// * stepHeight + stepHeight));
		// }
		// return new IntricatedPoint(new Point2D.Double(x, state * stepHeight), new Point2D.Double(x, state *
		// stepHeight));
	}

	private int getStateNumFor(String state) {
		// final String state = getStateAt(tick);
		return allStates.size() - 1 - allStates.indexOf(state);
	}

	private List<String> getStatesAt(TimeTick tick) {
		for (int i = 0; i < changes.size(); i++) {
			if (changes.get(i).getWhen().compareTo(tick) == 0) {
				return Arrays.asList(changes.get(i - 1).getState(), changes.get(i).getState());
			}
			if (changes.get(i).getWhen().compareTo(tick) > 0) {
				return Collections.singletonList(changes.get(i - 1).getState());
			}
		}
		return Collections.singletonList(changes.get(changes.size() - 1).getState());
	}

	public void addChange(ChangeState change) {
		final String state = change.getState();
		if (allStates.contains(state) == false) {
			allStates.add(state);
		}
		changes.add(change);
	}

	private Point2D getPoint(int i) {
		final ChangeState change = changes.get(i);
		final double x = ruler.getPosInPixel(change.getWhen());
		return new Point2D.Double(x, yOfState(change.getState()));
	}

	private double yOfState(String state) {
		return -stepHeight * allStates.indexOf(state);
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(new UChangeColor(HtmlColorUtils.COL_038048)).apply(new UStroke(1.5));
		final UTranslate deltaY = new UTranslate(0, stepHeight * (allStates.size() - 1));
		// System.err.println("changes=" + changes);
		for (int i = 0; i < changes.size() - 1; i++) {
			final Point2D pt = getPoint(i);
			final Point2D pt2 = getPoint(i + 1);
			final double len = pt2.getX() - pt.getX();
			ug.apply(new UTranslate(pt).compose(deltaY)).draw(new ULine(len, 0));
		}
		final Point2D pt = getPoint(changes.size() - 1);
		final double len = ruler.getWidth() - pt.getX();
		ug.apply(new UTranslate(pt).compose(deltaY)).draw(new ULine(len, 0));

		for (int i = 1; i < changes.size(); i++) {
			final Point2D before = getPoint(i - 1);
			final Point2D current = getPoint(i);
			ug.apply(new UTranslate(current).compose(deltaY)).draw(new ULine(0, before.getY() - current.getY()));
		}

		for (int i = 0; i < changes.size(); i++) {
			final Point2D ptLabel = getPoint(i);
			final TextBlock label = getStateTextBlock(changes.get(i).getState());
			final Dimension2D dim = label.calculateDimension(ug.getStringBounder());
			label.drawU(ug.apply(new UTranslate(ptLabel).compose(deltaY).compose(new UTranslate(2, -dim.getHeight()))));
		}

	}

	private FontConfiguration getFontConfiguration() {
		return new FontConfiguration(skinParam, FontParam.ACTIVITY, null);
	}

	private TextBlock getStateTextBlock(String state) {
		final Display display = Display.getWithNewlines(state);
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	public double getHeight() {
		return stepHeight * allStates.size() + 10;
	}

}
