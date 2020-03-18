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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ugraphic.UTranslate;

public class WormMutation {

	private final List<UTranslate> translations = new ArrayList<UTranslate>();

	private WormMutation() {

	}

	public static WormMutation create(Worm worm, double delta) {
		final String signature = worm.getDirectionsCode();
		final String definition = getDefinition(signature);
		if (definition == null) {
			return createFromLongSignature(signature, delta);
		}
		return new WormMutation(definition, delta);
	}

	private static WormMutation createFromLongSignature(final String signature, final double delta) {
		final WormMutation result = new WormMutation();
		for (int i = 0; i < signature.length() - 1; i++) {
			WormMutation tmp = new WormMutation(getDefinition(signature.substring(i, i + 2)), delta);
			if (i == 0) {
				result.translations.add(tmp.translations.get(0));
			} else {
				UTranslate last = result.getLast();
				if (last.isAlmostSame(tmp.translations.get(0)) == false) {
					tmp = tmp.reverse();
				}
			}
			result.translations.add(tmp.translations.get(1));
			if (i == signature.length() - 2) {
				result.translations.add(tmp.translations.get(2));
			}
		}
		return result;
	}

	private WormMutation reverse() {
		final WormMutation result = new WormMutation();
		for (UTranslate tr : translations) {
			result.translations.add(tr.reverse());
		}
		return result;
	}

	public UTranslate getLast() {
		return translations.get(translations.size() - 1);
	}

	public UTranslate getFirst() {
		return translations.get(0);
	}

	public int size() {
		return translations.size();
	}

	private static String getDefinition(final String signature) {
		if (signature.equals("D") || signature.equals("U")) {
			return "33";
		} else if (signature.equals("L") || signature.equals("R")) {
			return "55";
		} else if (signature.equals("RD")) {
			return "123";
		} else if (signature.equals("RU")) {
			return "543";
		} else if (signature.equals("LD")) {
			return "187";
		} else if (signature.equals("DL")) {
			return "345";
		} else if (signature.equals("DR")) {
			return "765";
		} else if (signature.equals("UL")) {
			return "321";
		} else if (signature.equals("UR")) {
			return "781";
			// } else if (signature.equals("DLD")) {
			// return "3443";
		}
		return null;
	}

	private WormMutation(String definition, double delta) {
		if (definition == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < definition.length(); i++) {
			this.translations.add(translation(Integer.parseInt(definition.substring(i, i + 1)), delta));
		}

	}

	private static UTranslate translation(int type, double delta) {
		switch (type) {
		case 1:
			return UTranslate.dy(-delta);
		case 2:
			return new UTranslate(delta, -delta);
		case 3:
			return UTranslate.dx(delta);
		case 4:
			return new UTranslate(delta, delta);
		case 5:
			return UTranslate.dy(delta);
		case 6:
			return new UTranslate(-delta, delta);
		case 7:
			return UTranslate.dx(-delta);
		case 8:
			return new UTranslate(-delta, -delta);
		}
		throw new IllegalArgumentException();
	}

	static private class MinMax {

		private double min = Double.MAX_VALUE;
		private double max = Double.MIN_VALUE;

		private void append(double v) {
			if (v > max) {
				max = v;
			}
			if (v < min) {
				min = v;
			}
		}

		private double getExtreme() {
			if (Math.abs(max) > Math.abs(min)) {
				return max;
			}
			return min;
		}

	}

	public UTranslate getTextTranslate(int size) {
		final MinMax result = new MinMax();
		for (UTranslate tr : translations) {
			result.append(tr.getDx());
		}
		return UTranslate.dx(result.getExtreme() * (size - 1));
	}

	public boolean isDxNegative() {
		return translations.get(0).getDx() < 0;
	}

	public Worm mute(Worm original) {
		final Worm result = new Worm();
		for (int i = 0; i < original.size(); i++) {
			result.addPoint(translations.get(i).getTranslated(original.get(i)));
		}
		return result;
	}

}
