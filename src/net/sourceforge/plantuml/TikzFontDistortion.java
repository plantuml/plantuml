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

import java.util.StringTokenizer;

public class TikzFontDistortion {

	private final double magnify;
	private final double distortion;

	private TikzFontDistortion(double magnify, double distortion) {
		this.magnify = magnify;
		this.distortion = distortion;
	}

	@Override
	public String toString() {
		return "" + magnify + ";" + distortion;
	}

	public static TikzFontDistortion fromValue(String value) {
		if (value == null) {
			return getDefault();
		}
		final StringTokenizer st = new StringTokenizer(value, ";");
		if (st.hasMoreElements() == false) {
			return getDefault();
		}
		final String v1 = st.nextToken();
		if (st.hasMoreElements() == false) {
			return getDefault();
		}
		final String v2 = st.nextToken();
		if (v1.matches("[\\d.]+") && v2.matches("[-\\d.]+")) {
			return new TikzFontDistortion(Double.parseDouble(v1), Double.parseDouble(v2));
		}
		return getDefault();
	}

	public static TikzFontDistortion getDefault() {
		return new TikzFontDistortion(1.20, 4.0);
	}

	public final double getMagnify() {
		return magnify;
	}

	public final double getDistortion() {
		return distortion;
	}

}