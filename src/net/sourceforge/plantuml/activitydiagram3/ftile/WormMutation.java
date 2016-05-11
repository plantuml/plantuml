/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 8475 $
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
		final WormMutation result = new WormMutation();
		if (signature.equals("D") || signature.equals("U")) {
			final UTranslate translate = new UTranslate(delta, 0);
			result.translations.add(translate);
			result.translations.add(translate);
			return result;
		}
		if (signature.equals("L") || signature.equals("R")) {
			final UTranslate translate = new UTranslate(0, delta);
			result.translations.add(translate);
			result.translations.add(translate);
			return result;
		}
		if (signature.equals("RD")) {
			result.translations.add(new UTranslate(0, -delta));
			result.translations.add(new UTranslate(delta, -delta));
			result.translations.add(new UTranslate(delta, 0));
			return result;
		}
		if (signature.equals("LD")) {
			result.translations.add(new UTranslate(0, -delta));
			result.translations.add(new UTranslate(-delta, -delta));
			result.translations.add(new UTranslate(-delta, 0));
			return result;
		}
		if (signature.equals("DL")) {
			result.translations.add(new UTranslate(delta, 0));
			result.translations.add(new UTranslate(delta, delta));
			result.translations.add(new UTranslate(0, delta));
			return result;
		}
		if (signature.equals("DR")) {
			result.translations.add(new UTranslate(-delta, 0));
			result.translations.add(new UTranslate(-delta, delta));
			result.translations.add(new UTranslate(0, delta));
			return result;
		}
		if (signature.equals("UL")) {
			result.translations.add(new UTranslate(delta, 0));
			result.translations.add(new UTranslate(delta, -delta));
			result.translations.add(new UTranslate(0, -delta));
			return result;
		}
		if (signature.equals("UR")) {
			result.translations.add(new UTranslate(-delta, 0));
			result.translations.add(new UTranslate(-delta, -delta));
			result.translations.add(new UTranslate(0, -delta));
			return result;
		}
		throw new UnsupportedOperationException(signature);

	}

	public Worm mute(Worm original) {
		final Worm result = new Worm();
		for (int i = 0; i < original.size(); i++) {
			result.addPoint(translations.get(i).getTranslated(original.get(i)));
		}
		return result;
	}

}
