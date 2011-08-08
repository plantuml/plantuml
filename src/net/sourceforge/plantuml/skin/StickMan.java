/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6577 $
 *
 */
package net.sourceforge.plantuml.skin;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class StickMan implements UDrawable {

	private final float thickness = 2;

	private final double armsY = 8;
	private final double armsLenght = 13;
	private final double bodyLenght = 27;
	private final double legsX = 13;
	private final double legsY = 15;
	private final double headDiam = 16;

	private final HtmlColor backgroundColor;
	private final HtmlColor foregroundColor;

	public StickMan(HtmlColor backgroundColor, HtmlColor foregroundColor) {
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
	}

	public void draw(ColorMapper colorMapper, Graphics2D g2d) {

		g2d.setStroke(new BasicStroke(thickness));

		final double startX = Math.max(armsLenght, legsX) - headDiam / 2.0 + thickness;

		final Shape head = new Ellipse2D.Double(startX, thickness, headDiam, headDiam);
		final Rectangle2D headBound = head.getBounds2D();

		final double centerX = headBound.getCenterX();

		final Shape body = new Line2D.Double(centerX, headBound.getMaxY(), centerX, headBound.getMaxY() + bodyLenght);

		final Shape arms = new Line2D.Double(centerX - armsLenght, headBound.getMaxY() + armsY, centerX + armsLenght,
				headBound.getMaxY() + armsY);

		final double y = body.getBounds2D().getMaxY();

		final Shape legs1 = new Line2D.Double(centerX, y, centerX - legsX, y + legsY);
		final Shape legs2 = new Line2D.Double(centerX, y, centerX + legsX, y + legsY);

		g2d.setColor(colorMapper.getMappedColor(backgroundColor));
		g2d.fill(head);

		g2d.setColor(colorMapper.getMappedColor(foregroundColor));
		g2d.draw(head);
		g2d.draw(body);
		g2d.draw(arms);
		g2d.draw(legs1);
		g2d.draw(legs2);

		g2d.setStroke(new BasicStroke());
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug) {

		ug.getParam().setStroke(new UStroke(thickness));

		final double startX = Math.max(armsLenght, legsX) - headDiam / 2.0 + thickness;

		final UEllipse head = new UEllipse(headDiam, headDiam);
		final double centerX = startX + headDiam / 2;

		final ULine body = new ULine(0, bodyLenght);

		final ULine arms = new ULine(armsLenght * 2, 0);

		final double y = headDiam + thickness + bodyLenght;

		final ULine legs1 = new ULine(-legsX, legsY);
		final ULine legs2 = new ULine(legsX, legsY);

		ug.getParam().setBackcolor(backgroundColor);
		ug.getParam().setColor(foregroundColor);
		ug.draw(startX, thickness, head);

		ug.draw(centerX, headDiam + thickness, body);
		ug.draw(centerX - armsLenght, headDiam + thickness + armsY, arms);
		ug.draw(centerX, y, legs1);
		ug.draw(centerX, y, legs2);

		ug.getParam().setStroke(new UStroke());
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		return Math.max(armsLenght, legsX) * 2 + 2 * thickness;
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return headDiam + bodyLenght + legsY + 2 * thickness;
	}

}
