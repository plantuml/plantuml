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
package net.sourceforge.plantuml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SvgString {

	private final String svg;
	private final double scale;

	public SvgString(String svg, double scale) {
		this.svg = svg;
		this.scale = scale;
	}
	
	public String getMD5Hex() {
		return SignatureUtils.getMD5Hex(svg);
	}

	public String getSvg(boolean raw) {
		String result = svg;
		if (raw) {
			return result;
		}
		if (result.startsWith("<?xml")) {
			final int idx = result.indexOf("<svg");
			result = result.substring(idx);
		}
		if (result.startsWith("<svg")) {
			final int idx = result.indexOf(">");
			result = "<svg>" + result.substring(idx + 1);
		}
		if (result.startsWith("<svg>") == false) {
			throw new IllegalArgumentException();
		}
		return result;
	}

	public int getData(String name) {
		final Pattern p = Pattern.compile("(?i)" + name + "\\W+(\\d+)");
		final Matcher m = p.matcher(svg);
		if (m.find()) {
			final String s = m.group(1);
			return Integer.parseInt(s);
		}
		throw new IllegalStateException("Cannot find " + name);
	}

	public double getScale() {
		return scale;
	}

}
