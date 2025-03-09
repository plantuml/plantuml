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
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.skin;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.Set;

import net.sourceforge.plantuml.warning.Warning;
import net.sourceforge.plantuml.warning.WarningHandler;

public class Pragma implements WarningHandler {

	private final EnumMap<PragmaKey, String> values = new EnumMap<>(PragmaKey.class);

	private final Set<Warning> warnings = new LinkedHashSet<>();

	private Pragma() {
	}

	public static Pragma createEmpty() {
		return new Pragma();
	}

	public void define(String keyName, String value) {
		final PragmaKey key = PragmaKey.lazyFrom(keyName);
		if (key != null)
			values.put(key, value);
	}

	public boolean isDefine(PragmaKey key) {
		return values.containsKey(key);
	}

	public void undefine(PragmaKey key) {
		values.remove(key);
	}

	public String getValue(PragmaKey key) {
		return values.get(key);
	}

	public boolean isTrue(PragmaKey key) {
		final String value = getValue(key);
		return "true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value);
	}

	public boolean isFalse(PragmaKey key) {
		final String value = getValue(key);
		return "false".equalsIgnoreCase(value) || "off".equalsIgnoreCase(value);
	}

	public static boolean legacyReplaceBackslashNByNewline() {
		return true;
	}

	@Override
	public void addWarning(Warning warning) {
		this.warnings.add(warning);
	}

	@Override
	public Collection<Warning> getWarnings() {
		return this.warnings;
	}
}
