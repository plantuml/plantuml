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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UClip;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorGradient;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontStyle;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontContext;
import net.sourceforge.plantuml.klimt.font.UFontFace;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UText;

public class DriverTextSvg implements UDriver<UText, SvgGraphics> {

	// Collects extra colored lines (underline/strikethrough) that cannot be
	// expressed as simple CSS text-decoration because they use a custom color.
	static class ExtraLines {
		private final List<HColor> colors = new ArrayList<>();
		private final List<Double> deltaYs = new ArrayList<>();

		void add(HColor color, double deltaY) {
			colors.add(color);
			deltaYs.add(deltaY);
		}

		void drawAll(double x, double y, double width, UFont font, ColorMapper mapper, SvgGraphics svg) {
			for (int i = 0; i < colors.size(); i++) {
				svg.setStrokeColor(colors.get(i).toSvg(mapper));
				svg.setStrokeWidth(font.getSize2D() / 28.0, null);
				svg.svgLine(x, y + deltaYs.get(i), x + width, y + deltaYs.get(i), 0);
			}
		}
	}

	private final StringBounder stringBounder;
	private final ClipContainer clipContainer;

	public DriverTextSvg(StringBounder stringBounder, ClipContainer clipContainer) {
		if (stringBounder == null)
			System.err.println("stringBounder=" + stringBounder);
		this.stringBounder = stringBounder;
		this.clipContainer = clipContainer;
	}

	public void draw(UText shape, double x, double y, ColorMapper mapper, UParam param, SvgGraphics svg) {
		final UClip clip = clipContainer.getClip();
		if (clip != null && clip.isInside(x, y) == false)
			return;

		final FontConfiguration fontConfiguration = shape.getFontConfiguration();
		if (fontConfiguration.getColor().isTransparent())
			return;

		final UFont font = fontConfiguration.getFont();
		final UFontFace face = fontConfiguration.getFontFace();

		// Emit full numeric CSS weight (e.g. "300", "500", "700") so that weights
		// parsed by SvgSaxParser and set via FontWeight style property are preserved
		// in SVG output rather than being collapsed to binary bold/normal.
		String fontWeight = null;
		if (fontConfiguration.containsStyle(FontStyle.BOLD))
			// Explicit BOLD decoration: honour face weight if already >= 700, else force 700
			fontWeight = (face.getCssWeight() >= 700) ? face.toCssWeightString() : "700";
		else if (face.getCssWeight() != 400)
			// Non-default weight from FontWeight style property or parsed SVG input
			fontWeight = face.toCssWeightString();

		String fontStyle = null;
		if (fontConfiguration.containsStyle(FontStyle.ITALIC) || face.isItalic())
			fontStyle = "italic";

		String text = shape.getText();
		if (text.matches("^\\s*$"))
			text = text.replace(' ', (char) 160);

		if (text.startsWith(" ")) {
			final double space = stringBounder.calculateDimension(font, " ").getWidth();
			while (text.startsWith(" ")) {
				x += space;
				text = text.substring(1);
			}
		}
		text = StringUtils.trin(text);
		final XDimension2D dim = stringBounder.calculateDimension(font, text);
		final double width = dim.getWidth();
		final double height = dim.getHeight();

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
			// Beware that some current SVG implementations do not render the wave properly
			// (e.g. Chrome just draws a straight line)
			// Works ok on Firefox 85.
			decorations.append("wavy underline ");
		}

		final String textDecoration = decorations.length() > 0 ? decorations.toString().trim() : null;

		String backColor = null;
		if (fontConfiguration.containsStyle(FontStyle.BACKCOLOR)) {
			final HColor back = fontConfiguration.getExtendedColor();
			if (back instanceof HColorGradient) {
				final HColorGradient gr = (HColorGradient) back;
				final String id = svg.createSvgGradient(gr.getColor1().toRGB(mapper), gr.getColor2().toRGB(mapper),
						gr.getPolicy());
				svg.setFillColor("url(#" + id + ")");
				svg.setStrokeColor(null);
				final double deltaPatch = 2;
				svg.svgRectangle(x, y - height + deltaPatch, width, height, 0, 0, 0/* , null, null */);

			} else
				backColor = back.toRGB(mapper);

		}

		final HColor textColor = fontConfiguration.getColor();
		svg.setFillColor(textColor.toSvg(mapper));
		svg.text(text, x, y, font.getFamily(text, UFontContext.SVG), font.getSize(), fontWeight, fontStyle,
				textDecoration, width, fontConfiguration.getAttributes(), backColor, shape.getOrientation());

		extraLines.drawAll(x, y, width, font, mapper, svg);

	}
}
