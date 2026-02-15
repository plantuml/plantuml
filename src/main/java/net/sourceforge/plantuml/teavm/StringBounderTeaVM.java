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
package net.sourceforge.plantuml.teavm;

import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class StringBounderTeaVM implements StringBounder {

	@Override
	public XDimension2D calculateDimension(UFont font, String text) {
		// ::uncomment when __TEAVM__
//		if (text == null || text.isEmpty())
//			return new XDimension2D(0, 0);
//
//		final String fontFamily = font.getFamily(null, null);
//		final int fontSize = font.getSize();
//		final String fontWeight = font.isBold() ? "bold" : "normal";
//
//		final double[] metrics = SvgGraphicsTeaVM.measureTextCanvas(text, fontFamily, fontSize, fontWeight);
//		final double width = metrics[0];
//
//		// Use SVG getBBox for height as it's more accurate for layout
//		final double[] svgMetrics = SvgGraphicsTeaVM.measureTextSvgBBox(text, fontFamily, fontSize);
//		double height = svgMetrics[1];
//
//		// getBBox returns height=0 for whitespace-only strings (no visible glyphs)
//		// In this case, use font metrics to get the proper line height
//		if (height == 0 && !text.isEmpty()) {
//			final double[] detailedMetrics = SvgGraphicsTeaVM.getDetailedTextMetrics(text, fontFamily, fontSize, fontWeight);
//			height = detailedMetrics[3] + detailedMetrics[4]; // fontBoundingBoxAscent + fontBoundingBoxDescent
//		}
//
//		return new XDimension2D(width, height);
		// ::done
		
		// ::comment when __TEAVM__
		throw new UnsupportedOperationException();
		// ::done

	}

	@Override
	public double getDescent(UFont font, String text) {
		// ::uncomment when __TEAVM__
//		if (text == null || text.isEmpty())
//			return 0;
//
//		final String fontFamily = font.getFamily(null, null);
//		final int fontSize = font.getSize();
//		final String fontWeight = font.isBold() ? "bold" : "normal";
//
//		final double[] metrics = SvgGraphicsTeaVM.getDetailedTextMetrics(text, fontFamily, fontSize, fontWeight);
//		// metrics[2] is actualBoundingBoxDescent
//		return metrics[2];
		// ::done

		// ::comment when __TEAVM__
		throw new UnsupportedOperationException();
		// ::done

	}

	@Override
	public boolean matchesProperty(String propertyName) {
		if ("TEAVM".equalsIgnoreCase(propertyName))
			return true;

		if ("SVG".equalsIgnoreCase(propertyName))
			return true;

		return false;
	}
}
