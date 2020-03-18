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
 */
package net.sourceforge.plantuml.braille;

import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.ugraphic.ClipContainer;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UDriver;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;

public class DriverRectangleBraille implements UDriver<BrailleGrid> {

	private final ClipContainer clipContainer;

	public DriverRectangleBraille(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(UShape ushape, double x, double y, ColorMapper mapper, UParam param, BrailleGrid grid) {
		final URectangle rect = (URectangle) ushape;

		// final double rx = rect.getRx();
		// final double ry = rect.getRy();
		double width = rect.getWidth();
		double height = rect.getHeight();

		// final String color = StringUtils.getAsSvg(mapper, param.getColor());
		// final HtmlColor back = param.getBackcolor();
		// if (back instanceof HtmlColorGradient) {
		// final HtmlColorGradient gr = (HtmlColorGradient) back;
		// final String id = svg.createSvgGradient(StringUtils.getAsHtml(mapper.getMappedColor(gr.getColor1())),
		// StringUtils.getAsHtml(mapper.getMappedColor(gr.getColor2())), gr.getPolicy());
		// svg.setFillColor("url(#" + id + ")");
		// svg.setStrokeColor(color);
		// } else {
		// final String backcolor = StringUtils.getAsSvg(mapper, back);
		// svg.setFillColor(backcolor);
		// svg.setStrokeColor(color);
		// }
		//
		// svg.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDasharraySvg());

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			final Rectangle2D.Double r = clip.getClippedRectangle(new Rectangle2D.Double(x, y, width, height));
			x = r.x;
			y = r.y;
			width = r.width;
			height = r.height;
			if (height <= 0) {
				return;
			}
		}
		grid.rectangle(x, y, width, height);
	}
}
