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
 *
 */
package net.sourceforge.plantuml.teavm;

import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontStyle;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.shape.UText;

public class DriverTextTeaVM implements UDriver<UText, SvgGraphicsTeaVM> {

	private final ClipContainer clipContainer;

	public DriverTextTeaVM(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	@Override
	public void draw(UText shape, double x, double y, ColorMapper mapper, UParam param, SvgGraphicsTeaVM svg) {
		// ::uncomment when __TEAVM__
//		final FontConfiguration fontConfiguration = shape.getFontConfiguration();
//		if (fontConfiguration.getColor().isTransparent())
//			return;
//
//		final UFont font = fontConfiguration.getFont();
//		
//		// Set text color
//		final String color = fontConfiguration.getColor().toSvg(mapper);
//		svg.setFillColor(color);
//
//		// Extract font properties
//		final String fontFamily = font.getFamily(null, null);
//		final int fontSize = font.getSize();
//
//		String fontWeight = null;
//		if (fontConfiguration.containsStyle(FontStyle.BOLD) || font.isBold())
//			fontWeight = "bold";
//
//		String fontStyle = null;
//		if (fontConfiguration.containsStyle(FontStyle.ITALIC) || font.isItalic())
//			fontStyle = "italic";
//
//		// Handle text decoration (underline, strike, wave)
//		String textDecoration = null;
//		HColor extraLine = null;
//		double deltaLineY = 0;
//
//		if (fontConfiguration.containsStyle(FontStyle.UNDERLINE)
//				&& fontConfiguration.getUnderlineStroke().getThickness() > 0) {
//			if (fontConfiguration.getExtendedColor() == null)
//				textDecoration = "underline";
//			else {
//				extraLine = fontConfiguration.getExtendedColor();
//				deltaLineY = font.getSize2D() / 14.0;
//			}
//		} else if (fontConfiguration.containsStyle(FontStyle.STRIKE)) {
//			if (fontConfiguration.getExtendedColor() == null)
//				textDecoration = "line-through";
//			else {
//				extraLine = fontConfiguration.getExtendedColor();
//				deltaLineY = -font.getSize2D() / 4.0;
//			}
//		} else if (fontConfiguration.containsStyle(FontStyle.WAVE)) {
//			// Note: some browsers may not render wave properly
//			textDecoration = "wavy underline";
//		}
//
//		svg.drawText(shape.getText(), x, y, fontFamily, fontSize, fontWeight, fontStyle, textDecoration);
//
//		// Draw extra line for colored underline/strike
//		if (extraLine != null) {
//			final double width = SvgGraphicsTeaVM.getTextWidth(shape.getText(), fontFamily, fontSize);
//			svg.setStrokeColor(extraLine.toSvg(mapper));
//			svg.setStrokeWidth(font.getSize2D() / 28.0);
//			svg.drawLine(x, y + deltaLineY, x + width, y + deltaLineY);
//		}
		// ::done
	}
}
