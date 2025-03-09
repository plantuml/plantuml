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
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18n {

	/**
	 * Retrieves a localized value for a given language and key, with the option to
	 * specify a default value.
	 *
	 * @param language     the language code (e.g., "en", "cn", "fr"); if null, the
	 *                     default locale's language is used
	 * @param key          the key for the message to retrieve; must not be null
	 * @param defaultValue the fallback value to return if no translation is found;
	 *                     must not be null
	 * 
	 * @return the localized value corresponding to the key, or the
	 *         {@code defaultValue} if no match is found
	 * @throws IllegalArgumentException if {@code key} or {@code defaultValue} is
	 *                                  null
	 */
	public static String getLocalizedValue(String language, String key, String defaultValue) {
		if (key == null)
			throw new IllegalArgumentException("Key must not be null.");
		if (defaultValue == null)
			throw new IllegalArgumentException("defaultValue must not be null.");

		// Default to the system's default language if none is provided
		if (language == null)
			language = Locale.getDefault().getLanguage();

		final Locale locale = Locale.forLanguageTag(language);

		try {
			final ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			// Return the default value if the translation is not found
			return defaultValue;
		}
	}
}