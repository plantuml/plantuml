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
package net.sourceforge.plantuml.klimt.drawing.g2d;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;

import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.shape.ULine;

public class DriverLineG2d extends DriverShadowedG2d implements UDriver<ULine, Graphics2D> {

	private final double dpiFactor;

	public DriverLineG2d(double dpiFactor) {
		this.dpiFactor = dpiFactor;
	}

	public void draw(ULine shape, double x, double y, ColorMapper mapper, UParam param, Graphics2D g2d) {
		final Shape line = new Line2D.Double(x, y, x + shape.getDX(), y + shape.getDY());
		manageStroke(param, g2d);
		// Shadow
		if (shape.getDeltaShadow() != 0) {
			drawShadow(g2d, line, shape.getDeltaShadow(), dpiFactor);
		}
		final HColor color = param.getColor();
		DriverRectangleG2d.drawBorder(param, color, mapper, shape, line, g2d, x, y);
//		g2d.setColor(mapper.getMappedColor(color));
//		g2d.draw(line);
	}

	static void manageStroke(UParam param, Graphics2D g2d) {
		manageStroke(param.getStroke(), g2d);
	}

	static void manageStroke(UStroke stroke, Graphics2D g2d) {
		final float thickness = (float) stroke.getThickness();
		if (stroke.getDashVisible() == 0) {
			g2d.setStroke(new BasicStroke(thickness));
		} else {
			final float dash1 = (float) stroke.getDashVisible();
			final float dash2 = (float) stroke.getDashSpace();
			final float[] style = { dash1, dash2 };
			g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, style, 0));
		}
	}
}
