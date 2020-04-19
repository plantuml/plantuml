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
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.timingdiagram.graphic.TimeArrow;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class TimeConstraint {

	private final TimeTick tick1;
	private final TimeTick tick2;
	private final Display label;
	private final ISkinParam skinParam;

	public TimeConstraint(TimeTick tick1, TimeTick tick2, String label, ISkinParam skinParam) {
		this.tick1 = tick1;
		this.tick2 = tick2;
		this.label = Display.getWithNewlines(label);
		this.skinParam = skinParam;
	}

	public final TimeTick getTick1() {
		return tick1;
	}

	public final TimeTick getTick2() {
		return tick2;
	}

	public final Display getLabel() {
		return label;
	}

	private TextBlock getTextBlock(Display display) {
		return display.create(getFontConfiguration(), HorizontalAlignment.LEFT, skinParam);
	}

	private FontConfiguration getFontConfiguration() {
		final UFont font = UFont.serif(14);
		return new FontConfiguration(font, HColorUtils.BLACK, HColorUtils.BLUE, false);
	}

	public void drawU(UGraphic ug, TimingRuler ruler) {
		ug = ug.apply(HColorUtils.RED).apply(HColorUtils.RED.bg());
		final double x1 = ruler.getPosInPixel(tick1);
		final double x2 = ruler.getPosInPixel(tick2);
		ug = ug.apply(UTranslate.dx(x1));
		ug.draw(ULine.hline(x2 - x1));

		ug.draw(getPolygon(-Math.PI / 2, new Point2D.Double(0, 0)));
		ug.draw(getPolygon(Math.PI / 2, new Point2D.Double(x2 - x1, 0)));

		final TextBlock text = getTextBlock(label);
		final Dimension2D dimText = text.calculateDimension(ug.getStringBounder());
		final double x = (x2 - x1 - dimText.getWidth()) / 2;
		text.drawU(ug.apply(new UTranslate(x, -getConstraintHeight(ug.getStringBounder()))));
	}

	public double getConstraintHeight(StringBounder stringBounder) {
		final TextBlock text = getTextBlock(label);
		final Dimension2D dimText = text.calculateDimension(stringBounder);
		return dimText.getHeight() + getTopMargin();

	}

	public static double getTopMargin() {
		return 5;
	}

	private UPolygon getPolygon(final double angle, final Point2D end) {
		final double delta = 20.0 * Math.PI / 180.0;
		final Point2D pt1 = TimeArrow.onCircle(end, angle + delta);
		final Point2D pt2 = TimeArrow.onCircle(end, angle - delta);

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(pt1.getX(), pt1.getY());
		polygon.addPoint(pt2.getX(), pt2.getY());
		polygon.addPoint(end.getX(), end.getY());

		return polygon;
	}

	public static double getHeightForConstraints(StringBounder stringBounder, List<TimeConstraint> constraints) {
		if (constraints.size() == 0) {
			return 0;
		}
		double result = 0;
		for (TimeConstraint constraint : constraints) {
			result = Math.max(result, constraint.getConstraintHeight(stringBounder));

		}
		return result;
	}

}
