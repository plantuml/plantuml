/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 9786 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UImageSvg implements UShape {

	private final String svg;

	public UImageSvg(String svg) {
		if (svg.startsWith("<?xml")) {
			final int idx = svg.indexOf("<svg");
			svg = svg.substring(idx);
		}
		this.svg = svg;
	}

	public final String getSvg() {
		if (svg.startsWith("<svg")) {
			final int idx = svg.indexOf(">");
			return "<svg>" + svg.substring(idx + 1);
		}
		return svg;
	}

	private int getData(String name) {
		final Pattern p = Pattern.compile("(?i)" + name + "\\W+(\\d+)");
		final Matcher m = p.matcher(svg);
		if (m.find()) {
			final String s = m.group(1);
			return Integer.parseInt(s);
		}
		throw new IllegalStateException("Cannot find " + name);
	}

	public int getHeight() {
		return getData("height");
	}

	public int getWidth() {
		return getData("width");
	}

}
