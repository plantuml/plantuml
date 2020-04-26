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

import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import net.sourceforge.plantuml.golem.MinMaxDouble;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.USegment;
import net.sourceforge.plantuml.ugraphic.USegmentType;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;

public class DriverPathOldG2d extends DriverShadowedG2d implements UDriver<Graphics2D> {

	private final double dpiFactor;

	public DriverPathOldG2d(double dpiFactor) {
		this.dpiFactor = dpiFactor;
	}

	public void draw(UShape ushape, final double x, final double y, ColorMapper mapper, UParam param, Graphics2D g2d) {
		final UPath shape = (UPath) ushape;
		DriverLineG2d.manageStroke(param, g2d);

		final GeneralPath p = new GeneralPath();
		boolean hasBezier = false;
		final MinMaxDouble minMax = new MinMaxDouble();
		minMax.manage(x, y);
		for (USegment seg : shape) {
			final USegmentType type = seg.getSegmentType();
			final double coord[] = seg.getCoord();
			// Cast float for Java 1.5
			if (type == USegmentType.SEG_MOVETO) {
				p.moveTo((float) (x + coord[0]), (float) (y + coord[1]));
				minMax.manage(x + coord[0], y + coord[1]);
			} else if (type == USegmentType.SEG_LINETO) {
				p.lineTo((float) (x + coord[0]), (float) (y + coord[1]));
				minMax.manage(x + coord[0], y + coord[1]);
			} else if (type == USegmentType.SEG_CUBICTO) {
				p.curveTo((float) (x + coord[0]), (float) (y + coord[1]), (float) (x + coord[2]),
						(float) (y + coord[3]), (float) (x + coord[4]), (float) (y + coord[5]));
				minMax.manage(x + coord[4], y + coord[5]);
				hasBezier = true;
			} else {
				throw new UnsupportedOperationException();
			}
			// bez = new CubicCurve2D.Double(x + bez.x1, y + bez.y1, x +
			// bez.ctrlx1, y + bez.ctrly1, x + bez.ctrlx2, y
			// + bez.ctrly2, x + bez.x2, y + bez.y2);
			// p.append(bez, true);
		}
		// p.closePath();

		// Shadow
		if (shape.getDeltaShadow() != 0) {
			if (hasBezier) {
				drawShadow(g2d, p, shape.getDeltaShadow(), dpiFactor);
			} else {
				double lastX = 0;
				double lastY = 0;
				for (USegment seg : shape) {
					final USegmentType type = seg.getSegmentType();
					final double coord[] = seg.getCoord();
					// Cast float for Java 1.5
					if (type == USegmentType.SEG_MOVETO) {
						lastX = x + coord[0];
						lastY = y + coord[1];
					} else if (type == USegmentType.SEG_LINETO) {
						final Shape line = new Line2D.Double(lastX, lastY, x + coord[0], y + coord[1]);
						drawShadow(g2d, line, shape.getDeltaShadow(), dpiFactor);
						lastX = x + coord[0];
						lastY = y + coord[1];
					} else {
						throw new UnsupportedOperationException();
					}
				}
			}
		}

		final HColor back = param.getBackcolor();
		if (back instanceof HColorGradient) {
			final HColorGradient gr = (HColorGradient) back;
			final char policy = gr.getPolicy();
			final GradientPaint paint;
			if (policy == '|') {
				paint = new GradientPaint((float) minMax.getMinX(), (float) minMax.getMaxY() / 2,
						mapper.toColor(gr.getColor1()), (float) minMax.getMaxX(), (float) minMax.getMaxY() / 2,
						mapper.toColor(gr.getColor2()));
			} else if (policy == '\\') {
				paint = new GradientPaint((float) minMax.getMinX(), (float) minMax.getMaxY(), mapper.toColor(gr
						.getColor1()), (float) minMax.getMaxX(), (float) minMax.getMinY(), mapper.toColor(gr
						.getColor2()));
			} else if (policy == '-') {
				paint = new GradientPaint((float) minMax.getMaxX() / 2, (float) minMax.getMinY(),
						mapper.toColor(gr.getColor1()), (float) minMax.getMaxX() / 2, (float) minMax.getMaxY(),
						mapper.toColor(gr.getColor2()));
			} else {
				// for /
				paint = new GradientPaint((float) x, (float) y, mapper.toColor(gr.getColor1()),
						(float) minMax.getMaxX(), (float) minMax.getMaxY(), mapper.toColor(gr.getColor2()));
			}
			g2d.setPaint(paint);
			g2d.fill(p);
		} else if (back != null) {
			g2d.setColor(mapper.toColor(back));
			g2d.fill(p);
		}

		if (param.getColor() != null) {
			g2d.setColor(mapper.toColor(param.getColor()));
			g2d.draw(p);
		}
	}

}
