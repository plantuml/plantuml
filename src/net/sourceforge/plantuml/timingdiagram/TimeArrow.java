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

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TimeArrow implements UDrawable {

	private final Point2D start;
	private final Point2D end;
	private final Display label;
	private final ISkinSimple spriteContainer;

	public static TimeArrow create(IntricatedPoint pt1, IntricatedPoint pt2, Display label, ISkinSimple spriteContainer) {
		final TimeArrow arrow1 = new TimeArrow(pt1.getPointA(), pt2.getPointA(), label, spriteContainer);
		final TimeArrow arrow2 = new TimeArrow(pt1.getPointA(), pt2.getPointB(), label, spriteContainer);
		final TimeArrow arrow3 = new TimeArrow(pt1.getPointB(), pt2.getPointA(), label, spriteContainer);
		final TimeArrow arrow4 = new TimeArrow(pt1.getPointB(), pt2.getPointB(), label, spriteContainer);
		return shorter(arrow1, arrow2, arrow3, arrow4);
	}

	private TimeArrow(Point2D start, Point2D end, Display label, ISkinSimple spriteContainer) {
		this.start = start;
		this.end = end;
		this.label = label;
		this.spriteContainer = spriteContainer;
	}

	private double getAngle() {
		return Math.atan2(end.getX() - start.getX(), end.getY() - start.getY());
	}

	private static TimeArrow shorter(TimeArrow arrow1, TimeArrow arrow2) {
		if (arrow1.len() < arrow2.len()) {
			return arrow1;
		}
		return arrow2;
	}

	private static TimeArrow shorter(TimeArrow arrow1, TimeArrow arrow2, TimeArrow arrow3, TimeArrow arrow4) {
		return shorter(shorter(arrow1, arrow2), shorter(arrow3, arrow4));
	}

	private double len() {
		return start.distance(end);
	}

	public TimeArrow translate(UTranslate translate) {
		return new TimeArrow(translate.getTranslated(start), translate.getTranslated(end), label, spriteContainer);
	}

	private Point2D onCircle(double alpha, double radius) {
		final double x = end.getX() - Math.sin(alpha) * radius;
		final double y = end.getY() - Math.cos(alpha) * radius;
		return new Point2D.Double(x, y);
	}

	private FontConfiguration getFontConfiguration() {
		final UFont font = new UFont("Serif", Font.PLAIN, 14);
		return new FontConfiguration(font, HtmlColorUtils.BLUE, HtmlColorUtils.BLUE, false);
	}

	public void drawU(UGraphic ug) {
		final double angle = getAngle();
		ug = ug.apply(new UChangeColor(HtmlColorUtils.BLUE)).apply(new UStroke(1.5));
		final ULine line = new ULine(end.getX() - start.getX(), end.getY() - start.getY());
		ug.apply(new UTranslate(start)).draw(line);

		final double delta = 20.0 * Math.PI / 180.0;
		final Point2D pt1 = onCircle(angle + delta, 8);
		final Point2D pt2 = onCircle(angle - delta, 8);

		final UPolygon polygon = new UPolygon();
		polygon.addPoint(pt1.getX(), pt1.getY());
		polygon.addPoint(pt2.getX(), pt2.getY());
		polygon.addPoint(end.getX(), end.getY());

		ug = ug.apply(new UChangeBackColor(HtmlColorUtils.BLUE));
		ug.draw(polygon);

		final TextBlock textLabel = label.create(getFontConfiguration(), HorizontalAlignment.LEFT, spriteContainer);
		double xText = (pt1.getX() + pt2.getX()) / 2;
		double yText = (pt1.getY() + pt2.getY()) / 2;
		if (start.getY() < end.getY()) {
			final Dimension2D dimLabel = textLabel.calculateDimension(ug.getStringBounder());
			yText -= dimLabel.getHeight();
		}
		textLabel.drawU(ug.apply(new UTranslate(xText, yText)));

		// final double radius = 4;
		// final UTranslate forCirle = new UTranslate(end.getX() - radius, end.getY() - radius);
		// ug.apply(forCirle).draw(new UEllipse(2 * radius, 2 * radius));
	}

}
