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
 * Revision $Revision: 6475 $
 *
 */
package net.sourceforge.plantuml.ugraphic.g2d;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UGradient;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DriverRectangleG2d implements UDriver<Graphics2D> {

	public void draw(UShape ushape, double x, double y, UParam param, Graphics2D g2d) {
		g2d.setStroke(new BasicStroke((float) param.getStroke().getThickness()));
		final URectangle shape = (URectangle) ushape;
		final double rx = shape.getRx();
		final double ry = shape.getRy();
		final Shape rect;
		if (rx == 0 && ry == 0) {
			rect = new Rectangle2D.Double(x, y, shape.getWidth(), shape.getHeight());
		} else {
			rect = new RoundRectangle2D.Double(x, y, shape.getWidth(), shape.getHeight(), rx, ry);
		}
		final UGradient gr = param.getGradient();
		if (gr == null) {
			if (param.getBackcolor() != null) {
				g2d.setColor(param.getBackcolor());
				DriverLineG2d.manageStroke(param, g2d);
				g2d.fill(rect);
			}
			if (param.getColor() != null) {
				g2d.setColor(param.getColor());
				g2d.draw(rect);
			}
		} else {
			final GradientPaint paint = new GradientPaint((float) x, (float) y, gr.getColor1(), (float) (x + shape
					.getWidth()), (float) (y + shape.getHeight()), gr.getColor2());
			g2d.setPaint(paint);
			g2d.fill(rect);
		}
	}

}
