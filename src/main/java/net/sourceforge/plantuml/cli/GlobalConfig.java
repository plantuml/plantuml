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
package net.sourceforge.plantuml.cli;

import java.util.EnumMap;
import java.util.Map;

public class GlobalConfig {
	// ::remove file when __HAXE__

	private static final GlobalConfig singleton = new GlobalConfig();
	static public final boolean STRICT_SELFMESSAGE_POSITION = true;
	static public final boolean USE_INTERFACE_EYE1 = false;
	static public final boolean USE_INTERFACE_EYE2 = false;
	static public final boolean FORCE_TEOZ = false;

	public static GlobalConfig getInstance() {
		return singleton;
	}

	private GlobalConfig() {
	}

	private final Map<GlobalConfigKey, Object> keys = new EnumMap<>(GlobalConfigKey.class);

	public void put(GlobalConfigKey key, Object value) {
		this.keys.put(key, value);
	}

	public boolean boolValue(GlobalConfigKey key) {
		return (Boolean) value(key);
	}

	public Object value(GlobalConfigKey key) {
		final Object value = keys.get(key);
		if (value == null)
			return key.getDefaultValue();
		return value;
	}

	// ::done
}
