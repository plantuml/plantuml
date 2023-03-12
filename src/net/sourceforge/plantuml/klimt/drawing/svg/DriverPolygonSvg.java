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
import net.sourceforge.plantuml.klimt.shape.UPolygon;

public class DriverPolygonSvg implements UDriver<UPolygon, SvgGraphics> {
    // ::remove file when __HAXE__

	private final ClipContainer clipContainer;

	public DriverPolygonSvg(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(UPolygon shape, double x, double y, ColorMapper mapper, UParam param, SvgGraphics svg) {
		final double points[] = shape.getPointArray(x, y);
		assert points.length % 2 == 0;
		final UClip clip = clipContainer.getClip();
		if (clip != null)
			for (int j = 0; j < points.length; j += 2)
				if (clip.isInside(points[j], points[j + 1]) == false)
					return;

		DriverRectangleSvg.applyFillColor(svg, mapper, param);
		DriverRectangleSvg.applyStrokeColor(svg, mapper, param);

		svg.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDasharraySvg());

		svg.svgPolygon(shape.getDeltaShadow(), points);
	}
}
