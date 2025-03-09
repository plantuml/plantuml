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

import java.awt.geom.Line2D;

import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UClip;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorGradient;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.shape.ULine;

public class DriverLineSvg implements UDriver<ULine, SvgGraphics> {
    // ::remove file when __HAXE__

	private final ClipContainer clipContainer;

	public DriverLineSvg(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(ULine shape, double x, double y, ColorMapper mapper, UParam param, SvgGraphics svg) {
		double x2 = x + shape.getDX();
		double y2 = y + shape.getDY();

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			final Line2D.Double line = clip.getClippedLine(new Line2D.Double(x, y, x2, y2));
			if (line == null) {
				return;
			}
			x = line.x1;
			y = line.y1;
			x2 = line.x2;
			y2 = line.y2;
		}

		// // Shadow
		// if (shape.getDeltaShadow() != 0) {
		// svg.svgLineShadow(x, y, x2, y2, shape.getDeltaShadow());
		// }

		final HColor color = param.getColor();
		if (color instanceof HColorGradient) {
			final HColorGradient gr = (HColorGradient) color;
			svg.setStrokeColor(gr.getColor1().toSvg(mapper));
		} else {
			svg.setStrokeColor(color.toSvg(mapper));
		}
		svg.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDasharraySvg());
		svg.svgLine(x, y, x2, y2, shape.getDeltaShadow());
	}
}
