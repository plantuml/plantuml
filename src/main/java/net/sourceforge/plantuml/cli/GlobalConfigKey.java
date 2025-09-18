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

public enum GlobalConfigKey {

	REPLACE_WHITE_BACKGROUND_BY_TRANSPARENT(Boolean.FALSE), //
	VERBOSE(Boolean.FALSE), //
	WORD(Boolean.FALSE), //
	// SYSTEM_EXIT(Boolean.TRUE), //
	GUI(Boolean.FALSE), //
	OVERWRITE(Boolean.FALSE), //
	ENABLE_STATS(defaultForStats()), //
	SILENTLY_COMPLETELY_IGNORE_ERRORS(Boolean.FALSE), //
	FILE_SEPARATOR("_"), //
	TIMEOUT_MS(30_000L);// 30 seconds

	private final Object defaultValue;

	GlobalConfigKey(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}
	
	
	private static boolean defaultForStats() {
		return isTrue(System.getProperty("PLANTUML_STATS")) || isTrue(System.getenv("PLANTUML_STATS"));
	}

	private static boolean isTrue(final String value) {
		return "on".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value);
	}



}
