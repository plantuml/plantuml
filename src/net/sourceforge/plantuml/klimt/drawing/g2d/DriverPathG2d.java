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

import java.awt.GradientPaint;
import java.awt.Graphics2D;

import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorGradient;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.USegment;
import net.sourceforge.plantuml.klimt.geom.USegmentType;

public class DriverPathG2d extends DriverShadowedG2d implements UDriver<UPath, Graphics2D> {

	private final double dpiFactor;

	public DriverPathG2d(double dpiFactor) {
		this.dpiFactor = dpiFactor;
	}

	public void draw(UPath shape, final double x, final double y, ColorMapper mapper, UParam param, Graphics2D g2d) {
		DriverLineG2d.manageStroke(param, g2d);

		final HColor back = param.getBackcolor();

		final ExtendedGeneralPath p = new ExtendedGeneralPath();
		MinMax minMax = MinMax.getEmpty(false);
		minMax = minMax.addPoint(x, y);
		for (USegment seg : shape) {
			final USegmentType type = seg.getSegmentType();
			final double coord[] = seg.getCoord();
			if (type == USegmentType.SEG_MOVETO) {
				p.moveTo(x + coord[0], y + coord[1]);
				minMax = minMax.addPoint(x + coord[0], y + coord[1]);
			} else if (type == USegmentType.SEG_LINETO) {
				p.lineTo(x + coord[0], y + coord[1]);
				minMax = minMax.addPoint(x + coord[0], y + coord[1]);
			} else if (type == USegmentType.SEG_CUBICTO) {
				p.curveTo(x + coord[0], y + coord[1], x + coord[2], y + coord[3], x + coord[4], y + coord[5]);
				minMax = minMax.addPoint(x + coord[4], y + coord[5]);
			} else if (type == USegmentType.SEG_ARCTO) {
				p.arcTo(coord[0], coord[1], coord[2], coord[3] != 0, coord[4] != 0, x + coord[5], y + coord[6]);
			} else {
				throw new UnsupportedOperationException();
			}
		}

		if (shape.isOpenIconic()) {
			p.closePath();
			g2d.setColor(param.getColor().toColor(mapper));
			g2d.fill(p);
			return;
		}

		// Shadow
		if (shape.getDeltaShadow() != 0)
			if (back == null || back.isTransparent())
				drawOnlyLineShadowSpecial(g2d, p, shape.getDeltaShadow(), dpiFactor);
			else
				drawShadow(g2d, p, shape.getDeltaShadow(), dpiFactor);

		if (back instanceof HColorGradient) {
			final HColorGradient gr = (HColorGradient) back;
			final char policy = gr.getPolicy();
			final GradientPaint paint;
			if (policy == '|') {
				paint = new GradientPaint((float) minMax.getMinX(), (float) minMax.getMaxY() / 2,
						gr.getColor1().toColor(mapper), (float) minMax.getMaxX(), (float) minMax.getMaxY() / 2,
						gr.getColor2().toColor(mapper));
			} else if (policy == '\\') {
				paint = new GradientPaint((float) minMax.getMinX(), (float) minMax.getMaxY(),
						gr.getColor1().toColor(mapper), (float) minMax.getMaxX(), (float) minMax.getMinY(),
						gr.getColor2().toColor(mapper));
			} else if (policy == '-') {
				paint = new GradientPaint((float) minMax.getMaxX() / 2, (float) minMax.getMinY(),
						gr.getColor1().toColor(mapper), (float) minMax.getMaxX() / 2, (float) minMax.getMaxY(),
						gr.getColor2().toColor(mapper));
			} else {
				// for /
				paint = new GradientPaint((float) x, (float) y, gr.getColor1().toColor(mapper),
						(float) minMax.getMaxX(), (float) minMax.getMaxY(), gr.getColor2().toColor(mapper));
			}
			g2d.setPaint(paint);
			g2d.fill(p);
		} else if (back.isTransparent() == false) {
			g2d.setColor(back.toColor(mapper));
			g2d.fill(p);
		}

		if (param.getColor().isTransparent() == false) {
			g2d.setColor(param.getColor().toColor(mapper));
			g2d.draw(p);
		}
	}

}
