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
 *
 */
package net.sourceforge.plantuml.ugraphic.g2d;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

import net.sourceforge.plantuml.EnsureVisible;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorGradient;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DriverEllipseG2d extends DriverShadowedG2d implements UDriver<Graphics2D> {

	private final double dpiFactor;
	private final EnsureVisible visible;

	public DriverEllipseG2d(double dpiFactor, EnsureVisible visible) {
		this.dpiFactor = dpiFactor;
		this.visible = visible;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, Graphics2D g2d) {
		final UEllipse shape = (UEllipse) ushape;
		g2d.setStroke(new BasicStroke((float) param.getStroke().getThickness()));
		visible.ensureVisible(x, y);
		visible.ensureVisible(x + shape.getWidth(), y + shape.getHeight());
		if (shape.getStart() == 0 && shape.getExtend() == 0) {
			final Shape ellipse = new Ellipse2D.Double(x, y, shape.getWidth(), shape.getHeight());

			// Shadow
			if (shape.getDeltaShadow() != 0) {
				drawShadow(g2d, ellipse, shape.getDeltaShadow(), dpiFactor);
			}

			final HtmlColor back = param.getBackcolor();
			if (back instanceof HtmlColorGradient) {
				final GradientPaint paint = getPaintGradient(x, y, mapper, shape, back);
				g2d.setPaint(paint);
				g2d.fill(ellipse);

				if (param.getColor() != null) {
					g2d.setColor(mapper.getMappedColor(param.getColor()));
					DriverLineG2d.manageStroke(param, g2d);
					g2d.draw(ellipse);
				}
			} else {
				if (back != null) {
					g2d.setColor(mapper.getMappedColor(param.getBackcolor()));
					DriverRectangleG2d.managePattern(param, g2d);
					g2d.fill(ellipse);
				}
				if (param.getColor() != null && param.getColor().equals(param.getBackcolor()) == false) {
					g2d.setColor(mapper.getMappedColor(param.getColor()));
					DriverLineG2d.manageStroke(param, g2d);
					g2d.draw(ellipse);
				}
			}
		} else {
			final Shape arc = new Arc2D.Double(x, y, shape.getWidth(), shape.getHeight(), round(shape.getStart()),
					round(shape.getExtend()), Arc2D.OPEN);
			if (param.getColor() != null) {
				g2d.setColor(mapper.getMappedColor(param.getColor()));
				g2d.draw(arc);
			}
		}
	}
	
	private GradientPaint getPaintGradient(double x, double y, ColorMapper mapper, final UEllipse shape,
			final HtmlColor back) {
		final HtmlColorGradient gr = (HtmlColorGradient) back;
		final char policy = gr.getPolicy();
		final GradientPaint paint;
		if (policy == '|') {
			paint = new GradientPaint((float) x, (float) (y + shape.getHeight()) / 2, mapper.getMappedColor(gr
					.getColor1()), (float) (x + shape.getWidth()), (float) (y + shape.getHeight()) / 2,
					mapper.getMappedColor(gr.getColor2()));
		} else if (policy == '\\') {
			paint = new GradientPaint((float) x, (float) (y + shape.getHeight()), mapper.getMappedColor(gr
					.getColor1()), (float) (x + shape.getWidth()), (float) y, mapper.getMappedColor(gr.getColor2()));
		} else if (policy == '-') {
			paint = new GradientPaint((float) (x + shape.getWidth()) / 2, (float) y, mapper.getMappedColor(gr
					.getColor1()), (float) (x + shape.getWidth()) / 2, (float) (y + shape.getHeight()),
					mapper.getMappedColor(gr.getColor2()));
		} else {
			// for /
			paint = new GradientPaint((float) x, (float) y, mapper.getMappedColor(gr.getColor1()),
					(float) (x + shape.getWidth()), (float) (y + shape.getHeight()), mapper.getMappedColor(gr
							.getColor2()));
		}
		return paint;
	}


	private static final double ROU = 5.0;

	static double round(double value) {
		return value;
		// final int v = (int) Math.round(value / ROU);
		// return v * ROU;
	}

}
