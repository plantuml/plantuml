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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.EnsureVisible;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

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

		final HColor back = param.getBackcolor();
		final List<Line2D.Double> shadows = shape.getDeltaShadow() != 0 && HColorUtils.isTransparent(back)
				? new ArrayList<Line2D.Double>()
				: null;

		Point2D.Double last = null;
		for (Point2D pt : shape.getPoints()) {
			final double xp = pt.getX() + x;
			final double yp = pt.getY() + y;
			visible.ensureVisible(xp, yp);
			if (last == null) {
				path.moveTo((float) xp, (float) yp);
			} else {
				if (shadows != null) {
					shadows.add(new Line2D.Double(last.x, last.y, xp, yp));
				}
				path.lineTo((float) xp, (float) yp);
			}
			last = new Point2D.Double(xp, yp);
		}

		if (last != null) {
			path.closePath();
		}

		if (shadows != null) {
			for (Line2D.Double line : keepSome(shadows)) {
				drawOnlyLineShadow(g2d, line, shape.getDeltaShadow(), dpiFactor);
			}
		} else if (shape.getDeltaShadow() != 0) {
			drawShadow(g2d, path, shape.getDeltaShadow(), dpiFactor);
		}

		if (back instanceof HColorGradient) {
			final HColorGradient gr = (HColorGradient) back;
			final char policy = gr.getPolicy();
			final GradientPaint paint;
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

	private List<Line2D.Double> keepSome(List<Line2D.Double> shadows) {
		final List<Line2D.Double> result = new ArrayList<Line2D.Double>();
		MinMax minMax = MinMax.getEmpty(true);
		for (Line2D.Double line : shadows) {
			minMax = minMax.addPoint(line.x1, line.y1);
			minMax = minMax.addPoint(line.y2, line.y2);
		}
		for (Line2D.Double line : shadows) {
			if (keepMe(line, minMax.getMaxX(), minMax.getMaxY()))
				result.add(line);
		}
		return result;
	}

	private boolean keepMe(Line2D.Double line, double maxX, double maxY) {
		if (line.x1 >= maxX && line.x2 >= maxX) {
			return true;
		}
		if (line.y1 >= maxY && line.y2 >= maxY) {
			return true;
		}
		final double margin = 10;
		if (line.x1 >= maxX - margin && line.x2 >= maxX - margin && line.y1 >= maxY - margin
				&& line.y2 >= maxY - margin) {
			return true;
		}
		return false;
	}
}
