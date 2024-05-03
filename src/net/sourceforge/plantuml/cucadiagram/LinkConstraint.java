/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.activitydiagram3.ftile.RectangleCoordinates;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.style.ISkinParam;

public class LinkConstraint {

	private final Link link1;
	private final Link link2;
	private final Display display;

	private RectangleCoordinates rectangleCoordinates = new RectangleCoordinates(0.0, 0.0, 0.0, 0.0);

	public LinkConstraint(Link link1, Link link2, Display display) {
		this.link1 = link1;
		this.link2 = link2;
		this.display = display;
	}

	public void setPosition(Link link, XPoint2D pt) {
		if (link == link1) {
			rectangleCoordinates.setX1(pt.getX());
			rectangleCoordinates.setY1(pt.getY());
		} else if (link == link2) {
			rectangleCoordinates.setX2(pt.getX());
			rectangleCoordinates.setY2(pt.getY());
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void drawMe(UGraphic ug, ISkinParam skinParam) {
		if (rectangleCoordinates.getX1() == 0 && rectangleCoordinates.getY1() == 0) {
			return;
		}
		if (rectangleCoordinates.getX2() == 0 && rectangleCoordinates.getY2() == 0) {
			return;
		}
		ug = ug.apply(HColors.BLACK);
//		ug.apply(new UTranslate(x1, y1)).draw(URectangle.build(10, 10));
//		ug.apply(new UTranslate(x2, y2)).draw(URectangle.build(10, 10));

		final ULine line = new ULine(rectangleCoordinates.getX2() - rectangleCoordinates.getX1(), rectangleCoordinates.getY2() - rectangleCoordinates.getY1());
		ug = ug.apply(new UStroke(3, 3, 1));
		ug.apply(new UTranslate(rectangleCoordinates.getX1(), rectangleCoordinates.getY1())).draw(line);

		final TextBlock label = display.create(FontConfiguration.create(skinParam, FontParam.ARROW, null),
						HorizontalAlignment.CENTER, skinParam);
		final XDimension2D dimLabel = label.calculateDimension(ug.getStringBounder());
		final double x = (rectangleCoordinates.getX1() + rectangleCoordinates.getX2()) / 2 - dimLabel.getWidth() / 2;
		final double y = (rectangleCoordinates.getY1() + rectangleCoordinates.getY2()) / 2 - dimLabel.getHeight() / 2;
		label.drawU(ug.apply(new UTranslate(x, y)));

	}

}
