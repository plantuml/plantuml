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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Histogram implements TimeDrawing {

	private final List<ChangeState> changes = new ArrayList<ChangeState>();
	private final List<TimeConstraint> constraints = new ArrayList<TimeConstraint>();

	private List<String> allStates;
	private final double stepHeight = 20;

	private final ISkinParam skinParam;
	private final TimingRuler ruler;
	private String initialState;

	public Histogram(TimingRuler ruler, ISkinParam skinParam, Collection<String> someStates) {
		this.ruler = ruler;
		this.skinParam = skinParam;
		this.allStates = new ArrayList<String>(someStates);
		Collections.reverse(allStates);
	}

	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		final double x = ruler.getPosInPixel(tick);
		final List<String> states = getStatesAt(tick);
		if (states.size() == 0) {
			return null;
		}
		if (states.size() == 1) {
			final double y = getStateYFor(states.get(0));
			return new IntricatedPoint(new Point2D.Double(x, y), new Point2D.Double(x, y));
		}
		assert states.size() == 2;
		final double y1 = getStateYFor(states.get(0));
		final double y2 = getStateYFor(states.get(1));
		assert y1 != y2;
		return new IntricatedPoint(new Point2D.Double(x, y1), new Point2D.Double(x, y2));
	}

	private double getStateYFor(String state) {
		return (allStates.size() - 1 - allStates.indexOf(state)) * stepHeight;
	}

	private List<String> getStatesAt(TimeTick tick) {
		if (changes.size() == 0) {
			return Collections.emptyList();
		}
		for (int i = 0; i < changes.size(); i++) {
			final int tickWithCurrentChangeTimeComparisonResult = changes.get(i).getWhen().compareTo(tick);
			if (tickWithCurrentChangeTimeComparisonResult == 0) {
				if (i == 0 && initialState == null) {
					return Arrays.asList(changes.get(i).getState());
				}
				if (i == 0 && initialState != null) {
					return Arrays.asList(initialState, changes.get(i).getState());
				}
				return Arrays.asList(changes.get(i - 1).getState(), changes.get(i).getState());
			}
			if (tickWithCurrentChangeTimeComparisonResult > 0) {
				final int changeIndex;
				if (i == 0) {
					// if this time tick was not yet defined in any place, and is less then the first one,
					// assume it's the leftmost
					changeIndex = 0;
				} else {
					changeIndex = i - 1;
				}
				return Collections.singletonList(changes.get(changeIndex).getState());
			}
		}
		return Collections.singletonList(changes.get(changes.size() - 1).getState());
	}

	public void addChange(ChangeState change) {
		changes.add(change);
		final String[] states = change.getStates();
		for (String state : states) {
			if (allStates.contains(state) == false) {
				allStates.add(state);
			}
		}
	}

	private Point2D[] getPoints(int n) {
		final ChangeState change = changes.get(n);
		final double x = ruler.getPosInPixel(change.getWhen());
		final String[] states = change.getStates();
		if (states.length == 2) {
			return new Point2D[] { new Point2D.Double(x, yOfState(states[0])),
					new Point2D.Double(x, yOfState(states[1])) };
		}
		return new Point2D[] { new Point2D.Double(x, yOfState(states[0])) };
	}

	private double getPointx(int n) {
		final ChangeState change = changes.get(n);
		return ruler.getPosInPixel(change.getWhen());
	}

	private double getPointMinY(int n) {
		final String[] states = changes.get(n).getStates();
		if (states.length == 2) {
			return Math.min(yOfState(states[0]), yOfState(states[1]));
		}
		return yOfState(states[0]);
	}

	private double getPointMaxY(int n) {
		final String[] states = changes.get(n).getStates();
		if (states.length == 2) {
			return Math.max(yOfState(states[0]), yOfState(states[1]));
		}
		return yOfState(states[0]);
	}

	private double yOfState(String state) {
		// if (state.equals("{?}")) {
		// throw new IllegalArgumentException();
		// }
		return -stepHeight * allStates.indexOf(state);
	}

	// private SortedSet<Double> getAllYofStates() {
	// final SortedSet<Double> result = new TreeSet<Double>();
	// for (String state : allStates) {
	// result.add(yOfState(state));
	// }
	// return result;
	// }

	private SymbolContext getContext() {
		return new SymbolContext(HtmlColorUtils.COL_D7E0F2, HtmlColorUtils.COL_038048).withStroke(new UStroke(1.5));
	}

	public void drawU(UGraphic ug) {
		ug = getContext().apply(ug);
		final UTranslate deltaY = new UTranslate(0, getFullDeltaY());
		if (changes.size() == 0) {
			return;
		}
		if (initialState != null) {
			for (Point2D pt : getPoints(0)) {
				drawHLine(ug, getInitialPoint(), getInitialWidth() + pt.getX());
			}
		}
		for (int i = 0; i < changes.size(); i++) {
			final double x2 = i < changes.size() - 1 ? getPointx(i + 1) : ruler.getWidth();
			final double len = x2 - getPointx(i);
			final Point2D[] points = getPoints(i);
			if (points.length == 2) {
				drawHBlock(ug.apply(new UChangeBackColor(changes.get(i).getBackColor())), points[0], points[1], len);
			}
			if (i < changes.size() - 1) {
				for (Point2D pt : points) {
					drawHLine(ug, pt, len);
				}
			}
		}
		for (Point2D pt : getPoints(changes.size() - 1)) {
			final double len = ruler.getWidth() - pt.getX();
			drawHLine(ug, pt, len);
		}

		if (initialState != null) {
			final Point2D before = getInitialPoint();
			final Point2D current = getPoints(0)[0];
			ug.apply(new UTranslate(current).compose(deltaY)).draw(new ULine(0, before.getY() - current.getY()));
		}
		for (int i = 1; i < changes.size(); i++) {
			final double minY = Math.min(getPointMinY(i), getPointMinY(i - 1));
			final double maxY = Math.max(getPointMaxY(i), getPointMaxY(i - 1));
			ug.apply(new UTranslate(getPointx(i), minY).compose(deltaY)).draw(new ULine(0, maxY - minY));
		}

		for (int i = 0; i < changes.size(); i++) {
			final Point2D ptLabel = getPoints(i)[0];
			final String comment = changes.get(i).getComment();
			if (comment == null) {
				continue;
			}
			final TextBlock label = getTextBlock(comment);
			final Dimension2D dim = label.calculateDimension(ug.getStringBounder());
			label.drawU(ug.apply(new UTranslate(ptLabel).compose(deltaY).compose(new UTranslate(2, -dim.getHeight()))));
		}

		for (TimeConstraint constraint : constraints) {
			final String state1 = last(getStatesAt(constraint.getTick1()));
			final String state2 = getStatesAt(constraint.getTick2()).get(0);
			final double y1 = getStateYFor(state1);
			final double y2 = getStateYFor(state2);
			constraint.drawU(ug.apply(new UTranslate(0, y1 - stepHeight / 2)), ruler, skinParam);
		}

	}

	private static String last(List<String> list) {
		return list.get(list.size() - 1);
	}

	private Point2D.Double getInitialPoint() {
		return new Point2D.Double(-getInitialWidth(), yOfState(initialState));
	}

	private void drawHBlock(UGraphic ug, Point2D pt1, Point2D pt2, double len) {
		final double minY = Math.min(pt1.getY(), pt2.getY());
		final double maxY = Math.max(pt1.getY(), pt2.getY());
		final Point2D pt = new Point2D.Double(pt1.getX(), minY);
		final UTranslate deltaY = new UTranslate(0, getFullDeltaY());
		final UTranslate pos = new UTranslate(pt).compose(deltaY);
		ug = ug.apply(pos);
		ug.draw(new URectangle(len, maxY - minY));
		for (double x = 0; x < len; x += 5) {
			ug.apply(new UTranslate(x, 0)).draw(new ULine(0, maxY - minY));
		}

	}

	private void drawHLine(UGraphic ug, final Point2D pt, final double len) {
		final UTranslate deltaY = new UTranslate(0, getFullDeltaY());
		final UTranslate pos = new UTranslate(pt).compose(deltaY);
		ug = ug.apply(pos);
		ug.draw(new ULine(len, 0));
	}

	private double getFullDeltaY() {
		return stepHeight * (allStates.size() - 1);
	}

	private FontConfiguration getFontConfiguration() {
		return new FontConfiguration(skinParam, FontParam.TIMING, null);
	}

	private TextBlock getTextBlock(String value) {
		final Display display = Display.getWithNewlines(value);
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	public double getHeight(StringBounder stringBounder) {
		return stepHeight * allStates.size() + 10;
	}

	public TextBlock getWidthHeader(StringBounder stringBounder) {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				for (String state : allStates) {
					final TextBlock label = getTextBlock(state);
					final Dimension2D dim = label.calculateDimension(ug.getStringBounder());
					label.drawU(ug.apply(new UTranslate(0, getFullDeltaY() + yOfState(state) - dim.getHeight())));
				}
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				double width = 0;
				for (String state : allStates) {
					final TextBlock label = getTextBlock(state);
					final Dimension2D dim = label.calculateDimension(stringBounder);
					width = Math.max(width, dim.getWidth());
				}
				if (initialState != null) {
					width += getInitialWidth();
				}
				return new Dimension2DDouble(width, getFullDeltaY());
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return null;
			}

		};
	}

	public void setInitialState(String initialState, Colors initialColors) {
		this.initialState = initialState;
		if (initialState != null) {
			allStates.add(initialState);
		}
	}

	private double getInitialWidth() {
		return stepHeight * 2;
	}

	public void addConstraint(TimeConstraint constraint) {
		this.constraints.add(constraint);
	}

}
