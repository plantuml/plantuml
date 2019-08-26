/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.sprite;

import java.awt.image.BufferedImage;
import java.util.List;

import net.sourceforge.plantuml.BackSlash;

public class SpriteUtils {

	public static final String SPRITE_NAME = "[-\\p{L}0-9_/]+";

	private SpriteUtils() {
	}

	public static String encodeColor(BufferedImage img, String name) {
		final StringBuilder sb = new StringBuilder();
		sb.append("sprite $" + name + " [" + img.getWidth() + "x" + img.getHeight() + "/color] {\n");
		final List<String> result = SpriteColorBuilder4096.encodeColor(img);
		for (String s : result) {
			sb.append(s);
			sb.append(BackSlash.NEWLINE);
		}
		sb.append("}\n");
		return sb.toString();
	}

	public static String encode(BufferedImage img, String name, SpriteGrayLevel level) {
		final StringBuilder sb = new StringBuilder();
		sb.append("sprite $" + name + " [" + img.getWidth() + "x" + img.getHeight() + "/" + level.getNbColor()
				+ "] {\n");
		final List<String> result = level.encode(img);
		for (String s : result) {
			sb.append(s);
			sb.append(BackSlash.NEWLINE);
		}
		sb.append("}\n");
		return sb.toString();
	}

	public static String encodeCompressed(BufferedImage img, String name, SpriteGrayLevel level) {
		final StringBuilder sb = new StringBuilder();
		sb.append("sprite $" + name + " [" + img.getWidth() + "x" + img.getHeight() + "/" + level.getNbColor() + "z] ");
		final List<String> list = level.encodeZ(img);
		if (list.size() == 1) {
			sb.append(list.get(0));
			sb.append(BackSlash.NEWLINE);
		} else {
			sb.append("{\n");
			for (String s : list) {
				sb.append(s);
				sb.append(BackSlash.NEWLINE);
			}
			sb.append("}\n");
		}
		return sb.toString();
	}

}
