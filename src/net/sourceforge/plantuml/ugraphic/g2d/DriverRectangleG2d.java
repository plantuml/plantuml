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
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.EnsureVisible;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UPattern;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UShapeSized;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;
import net.sourceforge.plantuml.ugraphic.color.HColorSimple;

public class DriverRectangleG2d extends DriverShadowedG2d implements UDriver<Graphics2D> {

	private final double dpiFactor;
	private final EnsureVisible visible;

	public DriverRectangleG2d(double dpiFactor, EnsureVisible visible) {
		this.dpiFactor = dpiFactor;
		this.visible = visible;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, Graphics2D g2d) {
		g2d.setStroke(new BasicStroke((float) param.getStroke().getThickness()));
		final URectangle rect = (URectangle) ushape;
		final double rx = rect.getRx();
		final double ry = rect.getRy();
		final Shape shape;
		if (rx == 0 && ry == 0) {
			shape = new Rectangle2D.Double(x, y, rect.getWidth(), rect.getHeight());
		} else {
			shape = new RoundRectangle2D.Double(x, y, rect.getWidth(), rect.getHeight(), rx, ry);
		}

		visible.ensureVisible(x, y);
		visible.ensureVisible(x + rect.getWidth(), y + rect.getHeight());

		// Shadow
		if (rect.getDeltaShadow() != 0) {
			drawShadow(g2d, shape, rect.getDeltaShadow(), dpiFactor);
		}

		final HColor back = param.getBackcolor();
		final HColor color = param.getColor();
		if (back instanceof HColorGradient) {
			final GradientPaint paint = getPaintGradient(x, y, mapper, rect.getWidth(), rect.getHeight(), back);
			g2d.setPaint(paint);
			g2d.fill(shape);
			drawBorder(param, color, mapper, rect, shape, g2d, x, y);
		} else {
			if (param.getBackcolor() != null) {
				g2d.setColor(mapper.toColor(param.getBackcolor()));
				DriverLineG2d.manageStroke(param, g2d);
				managePattern(param, g2d);
				g2d.fill(shape);
			}
			if (color != null && color.equals(param.getBackcolor()) == false) {
				drawBorder(param, color, mapper, rect, shape, g2d, x, y);
			}
		}
	}

	public static void drawBorder(UParam param, HColor color, ColorMapper mapper, UShapeSized sized, Shape shape,
			Graphics2D g2d, double x, double y) {
		if (color == null) {
			return;
		}
		if (color instanceof HColorGradient) {
			final GradientPaint paint = getPaintGradient(x, y, mapper, sized.getWidth(), sized.getHeight(), color);
			g2d.setPaint(paint);
		} else {
			g2d.setColor(mapper.toColor(color));
		}
		DriverLineG2d.manageStroke(param, g2d);
		g2d.draw(shape);
	}

	public static GradientPaint getPaintGradient(double x, double y, ColorMapper mapper, double width, double height,
			final HColor back) {
		final HColorGradient gr = (HColorGradient) back;
		final char policy = gr.getPolicy();
		final GradientPaint paint;
		if (policy == '|') {
			paint = new GradientPaint((float) x, (float) (y + height) / 2, mapper.toColor(gr.getColor1()),
					(float) (x + width), (float) (y + height) / 2, mapper.toColor(gr.getColor2()));
		} else if (policy == '\\') {
			paint = new GradientPaint((float) x, (float) (y + height), mapper.toColor(gr.getColor1()),
					(float) (x + width), (float) y, mapper.toColor(gr.getColor2()));
		} else if (policy == '-') {
			paint = new GradientPaint((float) (x + width) / 2, (float) y, mapper.toColor(gr.getColor1()),
					(float) (x + width) / 2, (float) (y + height), mapper.toColor(gr.getColor2()));
		} else {
			// for /
			paint = new GradientPaint((float) x, (float) y, mapper.toColor(gr.getColor1()), (float) (x + width),
					(float) (y + height), mapper.toColor(gr.getColor2()));
		}
		return paint;
	}

	public static void managePattern(UParam param, Graphics2D g2d) {
		final UPattern pattern = param.getPattern();
		if (pattern == UPattern.VERTICAL_STRIPE) {
			final BufferedImage bi = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);
			final Rectangle r = new Rectangle(0, 0, 4, 4);
			final int rgb = ((HColorSimple) param.getBackcolor()).getColor999().getRGB();
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (i == 0 || i == 1) {
						bi.setRGB(i, j, rgb);
					}
				}
			}
			g2d.setPaint(new TexturePaint(bi, r));
		} else if (pattern == UPattern.HORIZONTAL_STRIPE) {
			final BufferedImage bi = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);
			final Rectangle r = new Rectangle(0, 0, 4, 4);
			final int rgb = ((HColorSimple) param.getBackcolor()).getColor999().getRGB();
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (j == 0 || j == 1) {
						bi.setRGB(i, j, rgb);
					}
				}
			}
			g2d.setPaint(new TexturePaint(bi, r));
		} else if (pattern == UPattern.SMALL_CIRCLE) {
			final BufferedImage bi = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);
			final Rectangle r = new Rectangle(0, 0, 4, 4);
			final int rgb = ((HColorSimple) param.getBackcolor()).getColor999().getRGB();
			bi.setRGB(0, 1, rgb);
			bi.setRGB(1, 0, rgb);
			bi.setRGB(1, 1, rgb);
			bi.setRGB(1, 2, rgb);
			bi.setRGB(2, 1, rgb);
			g2d.setPaint(new TexturePaint(bi, r));
		}
	}

}
