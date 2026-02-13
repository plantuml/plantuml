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
 *
 */
package net.sourceforge.plantuml.teavm;

import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontContext;
import net.sourceforge.plantuml.klimt.shape.UCenteredCharacter;

/**
 * Driver for rendering UCenteredCharacter in TeaVM/SVG.
 * 
 * This is a simplified implementation that uses SVG text element with
 * text-anchor="middle" and dominant-baseline for centering, instead of
 * converting the character to a path (which would require AWT).
 */
public class DriverCenteredCharacterTeaVM implements UDriver<UCenteredCharacter, SvgGraphicsTeaVM> {

	@Override
	public void draw(UCenteredCharacter characterCircled, double x, double y, ColorMapper mapper, UParam param,
			SvgGraphicsTeaVM svg) {
		// ::uncomment when __TEAVM__
//		final char c = characterCircled.getChar();
//		final UFont font = characterCircled.getFont();
//		final HColor textColor = param.getColor();
//		final String text = String.valueOf(c);
//
//		svg.setFillColor(textColor.toSvg(mapper));
//		svg.drawCenteredCharacter(c, x, y, "monospace", font.getSize());
		// ::done
	}
}
