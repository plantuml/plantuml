/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.klimt;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.utils.LineLocation;

public class UGroup {

	private final Map<UGroupType, String> map = new EnumMap<>(UGroupType.class);

	public UGroup(LineLocation location) {
		if (location != null)
			map.put(UGroupType.DATA_SOURCE_LINE, "" + location.getPosition());
	}

	public UGroup() {
	}

	public void put(UGroupType key, String value) {
		value = fix(value);
		map.put(key, value);
	}

	public static UGroup singletonMap(UGroupType key, String value) {
		value = fix(value);
		final UGroup result = new UGroup();
		result.put(key, value);
		return result;
	}

	private static final Pattern NON_WORD = Pattern.compile("[^-\\w ]");

	private static String fix(String value) {
		return NON_WORD.matcher(value).replaceAll(".");
	}

	public Map<UGroupType, String> asMap() {
		return Collections.unmodifiableMap(map);
	}

}
