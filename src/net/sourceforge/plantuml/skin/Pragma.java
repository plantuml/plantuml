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

import java.util.LinkedHashMap;
import java.util.Map;

public class Pragma {

	private final Map<String, String> values = new LinkedHashMap<String, String>();

	public void define(String name, String value) {
		values.put(name, value);
	}

	public boolean isDefine(String name) {
		return values.containsKey(name);
	}

	public void undefine(String name) {
		values.remove(name);
	}

	public String getValue(String name) {
		return values.get(name);
	}

	public boolean horizontalLineBetweenDifferentPackageAllowed() {
		return isDefine("horizontallinebetweendifferentpackageallowed");
	}

	public boolean backToLegacyPackage() {
		return isDefine("backtolegacypackage");
	}

	public boolean useNewPackage() {
		return isDefine("usenewpackage");
	}

	private boolean isTrue(final String s) {
		return "true".equalsIgnoreCase(s) || "on".equalsIgnoreCase(s);
	}

	private boolean isFalse(final String s) {
		return "false".equalsIgnoreCase(s) || "off".equalsIgnoreCase(s);
	}

	public boolean useVerticalIf() {
		return isTrue(getValue("useverticalif"));
	}

	public boolean useTeozLayout() {
		return isTrue(getValue("teoz"));
	}

	public boolean useKermor() {
		return isTrue(getValue("kermor"));
	}

	public boolean useIntermediatePackages() {
		return !isFalse(getValue("useintermediatepackages"));
	}

}
