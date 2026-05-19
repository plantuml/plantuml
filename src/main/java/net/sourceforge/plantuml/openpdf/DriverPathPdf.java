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
package net.sourceforge.plantuml.openpdf;

import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UClip;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorGradient;
import net.sourceforge.plantuml.klimt.color.HColorLinearGradient;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.drawing.g2d.DriverShadowedG2d;

public class DriverPathPdf extends DriverShadowedG2d implements UDriver<UPath, PdfGraphics> {

	private final ClipContainer clipContainer;

	public DriverPathPdf(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(UPath shape, double x, double y, ColorMapper mapper, UParam param, PdfGraphics svg) {
		final UClip clip = clipContainer.getClip();
		if (clip != null && clip.isInside(x, y, shape) == false)
			return;

		final HColor color = param.getColor();
		final HColor back = param.getBackcolor();

		if (color != null && back != null && color.equals(back) && !(color instanceof HColorGradient)
				&& !(color instanceof HColorLinearGradient)) {

			svg.setFillColor(color.toColor(mapper).toAwtColor());
			svg.setStrokeColor(null);
			svg.setStrokeWidth(0, null);
		} else {
			DriverRectanglePdf.applyFillColor(svg, mapper, param);
			DriverRectanglePdf.applyStrokeColor(svg, mapper, param);

			svg.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDasharraySvg());
		}

		svg.pdfPath(x, y, shape);

	}
}
