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
package net.sourceforge.plantuml.ugraphic.color;

import java.awt.Color;

import net.sourceforge.plantuml.svek.DotStringFactory;

public abstract class AbstractColorMapper implements ColorMapper {

	final public String toRGB(HColor hcolor) {
		if (hcolor == null) {
			return null;
		}
		final Color color = toColor(hcolor);
		return DotStringFactory.sharp000000(color.getRGB());
	}

	final public String toSvg(HColor hcolor) {
		if (hcolor == null) {
			return "none";
		}
		if (hcolor instanceof HColorBackground) {
			hcolor = ((HColorBackground) hcolor).getBack();
//			Thread.dumpStack();
//			System.exit(0);
//			return toHtml(result);
		}
		if (HColorUtils.isTransparent(hcolor)) {
			return "#00000000";
		}
		final Color color = toColor(hcolor);
		final int alpha = color.getAlpha();
		if (alpha == 255) {
			return toRGB(hcolor);
		}
		String s = "0" + Integer.toHexString(alpha).toUpperCase();
		s = s.substring(s.length() - 2);
		return toRGB(hcolor) + s;
	}

	private static String sharpAlpha(int color) {
		final int v = color & 0xFFFFFF;
		String s = "00000" + Integer.toHexString(v).toUpperCase();
		s = s.substring(s.length() - 6);
		final int alpha = (int) (((long) color) & 0x000000FF) << 24;
		final String s2 = "0" + Integer.toHexString(alpha).toUpperCase();
		return "#" + s + s2.substring(0, 2);
	}

}
