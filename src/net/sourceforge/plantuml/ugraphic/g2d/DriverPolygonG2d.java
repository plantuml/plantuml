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
 *
 */
package net.sourceforge.plantuml.ugraphic.g2d;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.EnsureVisible;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;

public class DriverPolygonG2d extends DriverShadowedG2d implements UDriver<Graphics2D> {

	private final double dpiFactor;
	private final EnsureVisible visible;

	public DriverPolygonG2d(double dpiFactor, EnsureVisible visible) {
		this.dpiFactor = dpiFactor;
		this.visible = visible;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, Graphics2D g2d) {
		final UPolygon shape = (UPolygon) ushape;

		g2d.setStroke(new BasicStroke((float) param.getStroke().getThickness()));

		final GeneralPath path = new GeneralPath();

		boolean first = true;
		for (Point2D pt : shape.getPoints()) {
			final double xp = pt.getX() + x;
			final double yp = pt.getY() + y;
			visible.ensureVisible(xp, yp);
			if (first) {
				path.moveTo((float) xp, (float) yp);
			} else {
				path.lineTo((float) xp, (float) yp);
			}
			first = false;
		}

		if (first == false) {
			path.closePath();
		}

		if (shape.getDeltaShadow() != 0) {
			drawShadow(g2d, path, shape.getDeltaShadow(), dpiFactor);
		}

		final HColor back = param.getBackcolor();
		if (back instanceof HColorGradient) {
			final HColorGradient gr = (HColorGradient) back;
			final char policy = gr.getPolicy();
			final GradientPaint paint;
//			final Rectangle2D bound = path.getBounds();
			if (policy == '|') {
				paint = new GradientPaint((float) x, (float) (y + shape.getHeight()) / 2,
						mapper.toColor(gr.getColor1()), (float) (x + shape.getWidth()),
						(float) (y + shape.getHeight()) / 2, mapper.toColor(gr.getColor2()));
			} else if (policy == '\\') {
				paint = new GradientPaint((float) x, (float) (y + shape.getHeight()), mapper.toColor(gr.getColor1()),
						(float) (x + shape.getWidth()), (float) y, mapper.toColor(gr.getColor2()));
			} else if (policy == '-') {
				paint = new GradientPaint((float) (x + shape.getWidth()) / 2, (float) y, mapper.toColor(gr.getColor1()),
						(float) (x + shape.getWidth()) / 2, (float) (y + shape.getHeight()),
						mapper.toColor(gr.getColor2()));
			} else {
				// for /
				paint = new GradientPaint((float) x, (float) y, mapper.toColor(gr.getColor1()),
						(float) (x + shape.getWidth()), (float) (y + shape.getHeight()),
						mapper.toColor(gr.getColor2()));
			}
			g2d.setPaint(paint);
			g2d.fill(path);
		} else if (back != null) {
			g2d.setColor(mapper.toColor(back));
			DriverRectangleG2d.managePattern(param, g2d);
			g2d.fill(path);
		}

		if (param.getColor() != null) {
			g2d.setColor(mapper.toColor(param.getColor()));
			DriverLineG2d.manageStroke(param, g2d);
			g2d.draw(path);
		}
	}
}
