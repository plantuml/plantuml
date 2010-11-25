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
 * Revision $Revision: 5594 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Dimension2D;
import java.awt.geom.PathIterator;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.skin.UDrawable;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.USegmentType;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class CircledCharacter implements UDrawable, TextBlock {

	private final String c;
	private final Font font;
	private final Color innerCircle;
	private final Color circle;
	private final Color fontColor;
	private final double radius;

	public CircledCharacter(char c, double radius, Font font, Color innerCircle, Color circle, Color fontColor) {
		this.c = "" + c;
		this.radius = radius;
		this.font = font;
		this.innerCircle = innerCircle;
		this.circle = circle;
		this.fontColor = fontColor;
	}

	public void draw(Graphics2D g2d, int x, int y) {
		drawU(new UGraphicG2d(g2d, null), x, y);
	}

	public void drawU(UGraphic ug, double x, double y) {

		if (circle != null) {
			ug.getParam().setColor(circle);
		}

		// final Color circleToUse = circle == null ? ug.getParam().getColor() : circle;
		// ug.getParam().setColor(circleToUse);

		ug.getParam().setBackcolor(innerCircle);

		ug.draw(x, y, new UEllipse(radius * 2, radius * 2));

		ug.getParam().setColor(fontColor);

		// if (ug instanceof UGraphicSvg) {
		// final UPath p = getUPath(new FontRenderContext(null, true, true));
		// ug.draw(x + radius, y + radius, p);
		// } else {
		ug.centerChar(x + radius, y + radius, c.charAt(0), font);
		// }

	}

	private Dimension2D getStringDimension(StringBounder stringBounder) {
		return stringBounder.calculateDimension(font, c);
	}

	final public double getPreferredWidth(StringBounder stringBounder) {
		return 2 * radius;
	}

	final public double getPreferredHeight(StringBounder stringBounder) {
		return 2 * radius;
	}

	public void drawU(UGraphic ug) {
		drawU(ug, 0, 0);
	}

	private PathIterator getPathIteratorCharacter(FontRenderContext frc) {
		final TextLayout textLayout = new TextLayout(c, font, frc);
		final Shape s = textLayout.getOutline(null);
		return s.getPathIterator(null);
	}

	public UPath getUPath(FontRenderContext frc) {
		final UPath result = new UPath();

		final PathIterator path = getPathIteratorCharacter(frc);

		final double coord[] = new double[6];
		while (path.isDone() == false) {
			// final int w = path.getWindingRule();
			final int code = path.currentSegment(coord);
			result.add(coord, USegmentType.getByCode(code));
			path.next();
		}

		return result;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(getPreferredWidth(stringBounder), getPreferredHeight(stringBounder));
	}

	public void drawTOBEREMOVED(Graphics2D g2d, double x, double y) {
		throw new UnsupportedOperationException();
	}

}
