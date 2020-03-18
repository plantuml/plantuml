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
package net.sourceforge.plantuml.cute;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class VarArgs {

	private final Map<String, String> args = new HashMap<String, String>();

	public VarArgs(String data) {
		for (String s : data.split("\\s")) {
			if (s.contains("=")) {
				final StringTokenizer st = new StringTokenizer(s, "=");
				final String key = st.nextToken();
				final String value = st.nextToken();
				args.put(key, value);
			}
		}
		// System.err.println("arg=" + args);
	}

	@Override
	public String toString() {
		return args.toString();
	}

	public double getAsDouble(String k, double def) {
		if (args.containsKey(k)) {
			return getAsDouble(k);
		}
		return def;
	}

	public double getAsDouble(String k) {
		final String value = args.get(k);
		if (value == null) {
			throw new IllegalArgumentException("no key " + k);
		}
		return Double.parseDouble(value);
	}

	public MyDouble getAsMyDouble(String k) {
		final String value = args.get(k);
		if (value == null) {
			throw new IllegalArgumentException("no key " + k);
		}
		return new MyDouble(value);
	}

	public HColor getAsColor(String k) {
		final String value = args.get(k);
		if (value == null) {
			return HColorUtils.BLACK;
		}
		final HColor result = HColorSet.instance().getColorIfValid(value);
		if (result == null) {
			return HColorUtils.BLACK;
		}
		return result;
	}

	public Point2D getAsPoint(String k) {
		final String value = args.get(k);
		if (value == null) {
			throw new IllegalArgumentException("no key " + k);
		}
		final StringTokenizer st = new StringTokenizer(value.replaceAll("[()]", ""), ",");
		return new Point2D.Double(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
	}

	public Point2D getAsPoint(String k, Point2D def) {
		if (args.containsKey(k)) {
			return getAsPoint(k);
		}
		return def;
	}

	public CutePath getPointList(String k) {
		final String value = args.get(k);
		if (value == null) {
			throw new IllegalArgumentException("no key " + k);
		}
		return new CutePath(value);
	}

	public UTranslate getPosition() {
		return new UTranslate(getAsPoint("position", new Point2D.Double()));
	}

}
