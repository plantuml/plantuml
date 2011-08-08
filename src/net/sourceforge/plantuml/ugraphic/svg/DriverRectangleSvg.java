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

import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.svg.SvgGraphics;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UGradient;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DriverRectangleSvg implements UDriver<SvgGraphics> {

	private final ClipContainer clipContainer;

	public DriverRectangleSvg(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, SvgGraphics svg) {
		final URectangle rect = (URectangle) ushape;

		final double rx = rect.getRx();
		final double ry = rect.getRy();
		double width = rect.getWidth();
		double height = rect.getHeight();

		final UGradient gr = param.getGradient();
		if (gr == null) {
			final String color = param.getColor() == null ? "none" : StringUtils.getAsHtml(mapper.getMappedColor(param
					.getColor()));
			final String backcolor = param.getBackcolor() == null ? "none" : StringUtils.getAsHtml(mapper
					.getMappedColor(param.getBackcolor()));
			svg.setFillColor(backcolor);
			svg.setStrokeColor(color);
		} else {
			final String id = svg.createSvgGradient(StringUtils.getAsHtml(mapper.getMappedColor(gr.getColor1())),
					StringUtils.getAsHtml(mapper.getMappedColor(gr.getColor2())));
			svg.setFillColor("url(#" + id + ")");
			svg.setStrokeColor(null);
		}

		svg.setStrokeWidth("" + param.getStroke().getThickness(), param.getStroke().getDasharraySvg());

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			final Rectangle2D.Double r = clip.getClippedRectangle(new Rectangle2D.Double(x, y, width, height));
			x = r.x;
			y = r.y;
			width = r.width;
			height = r.height;
		}

		svg.svgRectangle(x, y, width, height, rx / 2, ry / 2);
	}
}
