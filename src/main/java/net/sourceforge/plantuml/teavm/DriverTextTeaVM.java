/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 */
package net.sourceforge.plantuml.teavm;

import net.sourceforge.plantuml.klimt.ClipContainer;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.shape.UText;

public class DriverTextTeaVM implements UDriver<UText, SvgGraphicsTeaVM> {

	private final ClipContainer clipContainer;

	public DriverTextTeaVM(ClipContainer clipContainer) {
		this.clipContainer = clipContainer;
	}

	@Override
	public void draw(UText text, double x, double y, ColorMapper mapper, UParam param, SvgGraphicsTeaVM svg) {
		final FontConfiguration fontConfig = text.getFontConfiguration();
		final UFont font = fontConfig.getFont();
		
		// Set text color
		final String color = fontConfig.getColor().toSvg(mapper);
		svg.setFillColor(color);

		// Extract font properties
		final String fontFamily = getFontFamily(font);
		final int fontSize = font.getSize();
		final String fontWeight = font.isBold() ? "bold" : "normal";
		final String fontStyle = font.isItalic() ? "italic" : "normal";

		svg.drawText(text.getText(), x, y, fontFamily, fontSize, fontWeight, fontStyle);
	}

	private String getFontFamily(UFont font) {
		// Try to get a web-safe font family
		// UFont.getFamily() needs a context, so we use a simplified approach
		String family = "sans-serif";
		
		// This is a simplification - in real implementation we'd need 
		// to extract the font family name properly
		return family;
	}
}
