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

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.svg.SvgGraphics;
import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UShape;

public class DriverPolygonSvg implements UDriver<SvgGraphics> {

	private final ClipContainer clipContainer;

	public DriverPolygonSvg(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(UShape ushape, double x, double y, UParam param, SvgGraphics svg) {
		final UPolygon shape = (UPolygon) ushape;

		final double points[] = new double[shape.getPoints().size() * 2];
		int i = 0;

		for (Point2D pt : shape.getPoints()) {
			points[i++] = pt.getX() + x;
			points[i++] = pt.getY() + y;
		}

		final UClip clip = clipContainer.getClip();

		if (clip != null) {
			for (int j = 0; j < points.length; j += 2) {
				if (clip.isInside(points[j], points[j + 1]) == false) {
					return;
				}
			}
		}

		final String color = param.getColor() == null ? "none" : HtmlColor.getAsHtml(param.getColor());
		final String backcolor = param.getBackcolor() == null ? "none" : HtmlColor.getAsHtml(param.getBackcolor());

		svg.setFillColor(backcolor);
		svg.setStrokeColor(color);
		svg.setStrokeWidth("" + param.getStroke().getThickness(), param.getStroke().getDasharraySvg());

		svg.svgPolygon(points);
	}
}
