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
package net.sourceforge.plantuml.braille2;

import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.URectangle;

public class StringBounderBraille implements StringBounder {

	public static final double CHAR_WIDTH = 16;
	public static final double CHAR_HEIGHT = 24;
	public static final int DOT_SIZE = 3;
	public static final UShape DOT_BRAILLE = URectangle.build(DOT_SIZE, DOT_SIZE);

	@Override
	public XDimension2D calculateDimension(UFont font, String text) {
		return new XDimension2D(text.length() * CHAR_WIDTH, CHAR_HEIGHT);
	}

	@Override
	public double getDescent(UFont font, String text) {
		System.err.println("BrailleStringBounder::getDescent");
		return 0;
	}

	@Override
	public boolean matchesProperty(String propertyName) {
		return false;
	}

	public static void drawString(UGraphic ug, String text) {
		for (int i = 0; i < text.length(); i++) {
			final UTranslate dx = UTranslate.dx(i * CHAR_WIDTH);
			drawChar(ug.apply(dx), text.charAt(i));
		}

	}

	private static void drawChar(UGraphic ug, char ch) {
		// https://en.wikipedia.org/wiki/Braille_Patterns
		System.err.println("Drawing: " + ch);
		BrailleChar2 brailleChar2 = BrailleChar2.fromChar(ch);
		brailleChar2.drawUg(ug);
		// drawBraille(ug, '\u283f');

	}

	private static void drawBraille(UGraphic ug, char ch) {
		ug = ug.apply(HColors.BLACK).apply(HColors.BLACK.bg());
		ug = ug.apply(UTranslate.dy(-CHAR_HEIGHT + CHAR_HEIGHT / 4.0 - DOT_SIZE / 2));

		for (int i = 0; i < 3; i++) {
			ug.apply(new UTranslate(CHAR_WIDTH / 3.0 - DOT_SIZE / 2, 0)).draw(DOT_BRAILLE);
			ug.apply(new UTranslate(2.0 * CHAR_WIDTH / 3.0 - DOT_SIZE / 2, 0)).draw(DOT_BRAILLE);
			ug = ug.apply(UTranslate.dy(CHAR_HEIGHT / 4.0));
		}

	}

}
