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
 */
package net.sourceforge.plantuml.klimt.drawing.svg;

import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UClip;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.shape.UEllipse;

public class DriverEllipseSvg implements UDriver<UEllipse, SvgGraphics> {
    // ::remove file when __HAXE__

	private final ClipContainer clipContainer;

	public DriverEllipseSvg(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(UEllipse shape, double x, double y, ColorMapper mapper, UParam param, SvgGraphics svg) {
		final double width = shape.getWidth();
		final double height = shape.getHeight();

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			if (clip.isInside(x, y) == false) {
				return;
			}
			if (clip.isInside(x + width, y + height) == false) {
				return;
			}
		}

		DriverRectangleSvg.applyStrokeColor(svg, mapper, param);
		DriverRectangleSvg.applyFillColor(svg, mapper, param);

		svg.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDasharraySvg());

		double start = shape.getStart();
		final double extend = shape.getExtend();
		final double cx = x + width / 2;
		final double cy = y + height / 2;
		if (start == 0 && extend == 0) {
			svg.svgEllipse(cx, cy, width / 2, height / 2, shape.getDeltaShadow());
		} else {
			start = start + 90;
			if (extend > 0) {
				// http://www.itk.ilstu.edu/faculty/javila/SVG/SVG_drawing1/elliptical_curve.htm
				final double x1 = cx + Math.sin(start * Math.PI / 180.) * width / 2;
				final double y1 = cy + Math.cos(start * Math.PI / 180.) * height / 2;
				final double x2 = cx + Math.sin((start + extend) * Math.PI / 180.) * width / 2;
				final double y2 = cy + Math.cos((start + extend) * Math.PI / 180.) * height / 2;
				svg.svgArcEllipse(width / 2, height / 2, x1, y1, x2, y2);
			} else {
				final double x1 = cx + Math.sin((start + extend) * Math.PI / 180.) * width / 2;
				final double y1 = cy + Math.cos((start + extend) * Math.PI / 180.) * height / 2;
				final double x2 = cx + Math.sin(start * Math.PI / 180.) * width / 2;
				final double y2 = cy + Math.cos(start * Math.PI / 180.) * height / 2;
				svg.svgArcEllipse(width / 2, height / 2, x1, y1, x2, y2);

			}
		}
	}

}
