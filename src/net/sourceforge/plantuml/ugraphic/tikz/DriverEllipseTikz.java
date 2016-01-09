/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 */
package net.sourceforge.plantuml.ugraphic.tikz;

import net.sourceforge.plantuml.tikz.TikzGraphics;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DriverEllipseTikz implements UDriver<TikzGraphics> {

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, TikzGraphics tikz) {
		final UEllipse shape = (UEllipse) ushape;
		final double width = shape.getWidth();
		final double height = shape.getHeight();

		double start = shape.getStart();
		final double extend = shape.getExtend();
		final double cx = x + width / 2;
		final double cy = y + height / 2;
		tikz.setFillColor(mapper.getMappedColor(param.getBackcolor()));
		tikz.setStrokeColor(mapper.getMappedColor(param.getColor()));
		tikz.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDashTikz());
		if (start == 0 && extend == 0) {
			tikz.ellipse(cx, cy, width / 2, height / 2);
		} else {
			throw new UnsupportedOperationException();
			// // http://www.itk.ilstu.edu/faculty/javila/SVG/SVG_drawing1/elliptical_curve.htm
			// start = start + 90;
			// final double x1 = cx + Math.sin(start * Math.PI / 180.) * width / 2;
			// final double y1 = cy + Math.cos(start * Math.PI / 180.) * height / 2;
			// final double x2 = cx + Math.sin((start + extend) * Math.PI / 180.) * width / 2;
			// final double y2 = cy + Math.cos((start + extend) * Math.PI / 180.) * height / 2;
			// // svg.svgEllipse(x1, y1, 1, 1, 0);
			// // svg.svgEllipse(x2, y2, 1, 1, 0);
			// svg.svgArcEllipse(width / 2, height / 2, x1, y1, x2, y2);
		}
	}
}
