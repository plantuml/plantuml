/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Player implements TextBlock, TimeProjected {

	private final String code;
	private final Display full;
	private final TimingStyle type;
	private final ISkinParam skinParam;
	private final TimingRuler ruler;
	private String initialState;

	private final Set<ChangeState> changes = new TreeSet<ChangeState>();
	private final List<TimeConstraint> constraints = new ArrayList<TimeConstraint>();

	public Player(String code, String full, TimingStyle type, ISkinParam skinParam, TimingRuler ruler) {
		this.code = code;
		this.full = Display.getWithNewlines(full);
		this.type = type;
		this.skinParam = skinParam;
		this.ruler = ruler;
	}

	private FontConfiguration getFontConfiguration() {
		return new FontConfiguration(skinParam, FontParam.ACTIVITY, null);
	}

	public void drawU(UGraphic ug) {
		final TextBlock title = getTitle();
		title.drawU(ug);
		final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		drawLine(ug.apply(new UChangeColor(HtmlColorUtils.BLACK)).apply(new UStroke(1.0)), -TimingDiagram.marginX1,
				dimTitle.getHeight() + 1, dimTitle.getWidth() + 1, dimTitle.getHeight() + 1,
				dimTitle.getWidth() + 1 + 10, 0);
	}

	public void drawContent(UGraphic ug) {
		ug = ug.apply(getTranslateForTimeDrawing(ug.getStringBounder()));
		getTimeDrawing().drawU(ug);
	}

	public void drawWidthHeader(UGraphic ug) {
		ug = ug.apply(getTranslateForTimeDrawing(ug.getStringBounder()));
		getTimeDrawing().getWidthHeader(ug.getStringBounder()).drawU(ug);
	}

	public double getGetWidthHeader(StringBounder stringBounder) {
		return getTimeDrawing().getWidthHeader(stringBounder).calculateDimension(stringBounder).getWidth();
	}

	private void drawLine(UGraphic ug, double... coord) {
		for (int i = 0; i < coord.length - 2; i += 2) {
			final double x1 = coord[i];
			final double y1 = coord[i + 1];
			final double x2 = coord[i + 2];
			final double y2 = coord[i + 3];
			ug.apply(new UTranslate(x1, y1)).draw(new ULine(x2 - x1, y2 - y1));
		}

	}

	private UTranslate getTranslateForTimeDrawing(StringBounder stringBounder) {
		final TextBlock title = getTitle();
		return new UTranslate(0, title.calculateDimension(stringBounder).getHeight() * 2);
	}

	private TextBlock getTitle() {
		return full.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	private TimeDrawing cached;
	private Colors initialColors;

	private TimeDrawing getTimeDrawing() {
		if (cached == null) {
			cached = computeTimeDrawing();
		}
		return cached;
	}

	private TimeDrawing computeTimeDrawing() {
		final TimeDrawing result;
		if (type == TimingStyle.CONCISE) {
			result = new Ribbon(ruler, skinParam);
		} else if (type == TimingStyle.ROBUST) {
			result = new Histogram(ruler, skinParam);
		} else {
			throw new IllegalStateException();
		}
		result.setInitialState(initialState, initialColors);
		for (ChangeState change : changes) {
			result.addChange(change);
		}
		for (TimeConstraint constraint : constraints) {
			result.addConstraint(constraint);
		}
		return result;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final TextBlock title = getTitle();
		final double width = ruler.getWidth();
		final double zoneHeight = getZoneHeight();
		return new Dimension2DDouble(width, title.calculateDimension(stringBounder).getHeight() * 2 + zoneHeight);
	}

	private double getZoneHeight() {
		return getTimeDrawing().getHeight();
	}

	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		return null;
	}

	public void setState(TimeTick now, String state, String comment, Colors color) {
		if (now == null) {
			this.initialState = state;
			this.initialColors = color;
		} else {
			if (state == null) {
				throw new IllegalArgumentException();
			}
			this.changes.add(new ChangeState(now, state, comment, color));
		}

	}

	public IntricatedPoint getTimeProjection(StringBounder stringBounder, TimeTick tick) {
		final IntricatedPoint point = getTimeDrawing().getTimeProjection(stringBounder, tick);
		if (point == null) {
			return null;
		}
		final UTranslate translation = getTranslateForTimeDrawing(stringBounder);
		return point.translated(translation);
	}

	public void createConstraint(TimeTick tick1, TimeTick tick2, String message) {
		this.constraints.add(new TimeConstraint(tick1, tick2, message));
	}

}
