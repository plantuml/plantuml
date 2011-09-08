/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
package net.sourceforge.plantuml.ugraphic.svg;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.svg.SvgGraphics;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DriverEllipseSvg implements UDriver<SvgGraphics> {

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, SvgGraphics svg) {
		final UEllipse shape = (UEllipse) ushape;
		final double width = shape.getWidth();
		final double height = shape.getHeight();

		final String color = param.getColor() == null ? "none" : StringUtils.getAsHtml(mapper.getMappedColor(param.getColor()));
		final String backcolor = param.getBackcolor() == null ? "none" : StringUtils.getAsHtml(mapper.getMappedColor(param.getBackcolor()));

		// Shadow
		if (shape.getDeltaShadow() != 0) {
			svg.svgEllipseShadow(x + width / 2, y + height / 2, width / 2, height / 2, shape.getDeltaShadow());
		}
		
		svg.setFillColor(backcolor);
		svg.setStrokeColor(color);
		svg.setStrokeWidth(""+param.getStroke().getThickness(), param.getStroke().getDasharraySvg());

		svg.svgEllipse(x + width / 2, y + height / 2, width / 2, height / 2);
	}

}
