/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.klimt.shape.URectangle;

public class DriverRectanglePdf implements UDriver<URectangle, PdfGraphics> {

	private final ClipContainer clipContainer;

	public DriverRectanglePdf(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	public void draw(URectangle rect, double x, double y, ColorMapper mapper, UParam param, PdfGraphics pdf) {
		final double rx = rect.getRx();
		final double ry = rect.getRy();
		double width = rect.getWidth();
		double height = rect.getHeight();

		applyFillColor(pdf, mapper, param);
		applyStrokeColor(pdf, mapper, param);

		pdf.setStrokeWidth(param.getStroke().getThickness(), param.getStroke().getDasharraySvg());

		final UClip clip = clipContainer.getClip();
		if (clip != null) {
			final XRectangle2D r = clip.getClippedRectangle(new XRectangle2D(x, y, width, height));
			x = r.x;
			y = r.y;
			width = r.width;
			height = r.height;
			if (height <= 0) {
				return;
			}
		}
		pdf.pdfRectangle(x, y, width, height, rx / 2, ry / 2);
	}

	public static void applyFillColor(PdfGraphics pdf, ColorMapper mapper, UParam param) {
		final HColor background = param.getBackcolor();
//		if (background instanceof HColorLinearGradient) {
//			final HColorLinearGradient gr = (HColorLinearGradient) background;
//			final String id = pdf.createSvgGradient(gr, mapper);
//			pdf.setFillColor("url(#" + id + ")");
//		} else if (background instanceof HColorGradient) {
//			final HColorGradient gr = (HColorGradient) background;
//			final String id = pdf.createSvgGradient(gr.getColor1().toRGB(mapper), gr.getColor2().toRGB(mapper),
//					gr.getPolicy());
//			pdf.setFillColor("url(#" + id + ")");
//		} else {
		
		// 				g2d.setColor(param.getBackcolor().toColor(mapper).toAwtColor());

			pdf.setFillColor(background.toColor(mapper).toAwtColor(), background.transparentFillBehavior());
//		}
	}

	public static void applyStrokeColor(PdfGraphics pdf, ColorMapper mapper, UParam param) {
		final HColor color = param.getColor();
//		if (color instanceof HColorLinearGradient) {
//			final HColorLinearGradient gr = (HColorLinearGradient) color;
//			final String id = pdf.createSvgGradient(gr, mapper);
//			pdf.setStrokeColor("url(#" + id + ")");
//		} else if (color instanceof HColorGradient) {
//			final HColorGradient gr = (HColorGradient) color;
//			final String id = pdf.createSvgGradient(gr.getColor1().toRGB(mapper), gr.getColor2().toRGB(mapper),
//					gr.getPolicy());
//			pdf.setStrokeColor("url(#" + id + ")");
//		} else {
			pdf.setStrokeColor(color.toColor(mapper).toAwtColor());
//		}

	}
}
