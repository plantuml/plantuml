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
 * Original Author:  Shunli Han
 *
 *
 */
package net.sourceforge.plantuml.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

	/**
	 * get the localized language word by key
	 *
	 * @param locale -  the locale
	 * @param key    - The key must not be null
	 * @return the localized language word
	 */
	public static String get(Locale locale, String key) {
		if (key == null) {
			throw new RuntimeException("localized language key must not be null");
		}
		ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
		return bundle.getString(key);
	}
}