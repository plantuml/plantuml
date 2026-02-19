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

import java.util.ArrayList;
import java.util.List;

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

	// Collects extra colored lines (underline/strikethrough) that cannot be
	// expressed as simple CSS text-decoration because they use a custom color.
	static class ExtraLines {
		private final List<HColor> colors = new ArrayList<>();
		private final List<Double> deltaYs = new ArrayList<>();

		void add(HColor color, double deltaY) {
			colors.add(color);
			deltaYs.add(deltaY);
		}

		void drawAll(double x, double y, double width, UFont font, ColorMapper mapper, SvgGraphicsTeaVM svg) {
			for (int i = 0; i < colors.size(); i++) {
				svg.setStrokeColor(colors.get(i).toSvg(mapper));
				svg.setStrokeWidth(font.getSize2D() / 28.0);
				svg.drawLine(x, y + deltaYs.get(i), x + width, y + deltaYs.get(i));
			}
		}
	}

	private final ClipContainer clipContainer;

	public DriverTextTeaVM(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	@Override
	public void draw(UText shape, double x, double y, ColorMapper mapper, UParam param, SvgGraphicsTeaVM svg) {
		final FontConfiguration fontConfiguration = shape.getFontConfiguration();
		if (fontConfiguration.getColor().isTransparent())
			return;

		final UFont font = fontConfiguration.getFont();

		// Set text color
		final String color = fontConfiguration.getColor().toSvg(mapper);
		svg.setFillColor(color);

		// Extract font properties
		final String fontFamily = font.getFamily(null, null);
		final int fontSize = font.getSize();

		String fontWeight = null;
		if (fontConfiguration.containsStyle(FontStyle.BOLD) || font.isBold())
			fontWeight = "bold";

		String fontStyle = null;
		if (fontConfiguration.containsStyle(FontStyle.ITALIC) || font.isItalic())
			fontStyle = "italic";

		// Handle text decoration (underline, strike, wave)
		final ExtraLines extraLines = new ExtraLines();
		final StringBuilder decorations = new StringBuilder();

		if (fontConfiguration.containsStyle(FontStyle.UNDERLINE)
				&& fontConfiguration.getUnderlineStroke().getThickness() > 0) {
			if (fontConfiguration.getExtendedColor() == null)
				decorations.append("underline ");
			else
				extraLines.add(fontConfiguration.getExtendedColor(), font.getSize2D() / 14.0);
		}

		if (fontConfiguration.containsStyle(FontStyle.STRIKE)) {
			if (fontConfiguration.getExtendedColor() == null)
				decorations.append("line-through ");
			else
				extraLines.add(fontConfiguration.getExtendedColor(), -font.getSize2D() / 4.0);
		}

		if (fontConfiguration.containsStyle(FontStyle.WAVE)) {
			// Note: some browsers may not render wave properly
			decorations.append("wavy underline ");
		}

		final String textDecoration = decorations.length() > 0 ? decorations.toString().trim() : null;

		// Handle background color
		String backColor = null;
		if (fontConfiguration.containsStyle(FontStyle.BACKCOLOR)) {
			final HColor back = fontConfiguration.getExtendedColor();
			if (back != null && !(back instanceof net.sourceforge.plantuml.klimt.color.HColorGradient)) {
				backColor = back.toRGB(mapper);
			}
		}

		svg.drawText(shape.getText(), x, y, fontFamily, fontSize, fontWeight, fontStyle, textDecoration, backColor);

		// Draw extra lines for colored underline/strike
		final double width = SvgGraphicsTeaVM.getTextWidth(shape.getText(), fontFamily, fontSize);
		extraLines.drawAll(x, y, width, font, mapper, svg);
	}
}
