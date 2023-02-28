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
package net.sourceforge.plantuml.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

public class Roboto {
    // ::remove folder when __HAXE__

	private final static String[] names = new String[] { "Roboto-BlackItalic.ttf", "Roboto-Black.ttf",
			"Roboto-BoldItalic.ttf", "Roboto-Bold.ttf", "RobotoCondensed-BoldItalic.ttf", "RobotoCondensed-Bold.ttf",
			"RobotoCondensed-Italic.ttf", "RobotoCondensed-LightItalic.ttf", "RobotoCondensed-Light.ttf",
			"RobotoCondensed-MediumItalic.ttf", "RobotoCondensed-Medium.ttf", "RobotoCondensed-Regular.ttf",
			"Roboto-Italic.ttf", "Roboto-LightItalic.ttf", "Roboto-Light.ttf", "Roboto-MediumItalic.ttf",
			"Roboto-Medium.ttf", "Roboto-Regular.ttf", "Roboto-ThinItalic.ttf", "Roboto-Thin.ttf" };

	public static void registerFonts() throws FontFormatException, IOException {
		for (String n : names) {
			final InputStream stream = Roboto.class.getResourceAsStream(n);
			final Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
		}
	}
}
