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
package net.sourceforge.plantuml.klimt;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;

public class SvgAttributes {

	private final Map<String, String> attributes = new TreeMap<String, String>();

	private SvgAttributes cloneMe() {
		final SvgAttributes result = new SvgAttributes();
		result.attributes.putAll(this.attributes);
		return result;
	}

	private SvgAttributes() {
	}

	public static SvgAttributes empty() {
		return new SvgAttributes();
	}

	static private final Pattern2 p = Pattern2.cmpile("(\\w+)\\s*=\\s*([%g][^%g]*[%g]|(?:\\w+))");

	public static SvgAttributes build(String args) {
		final SvgAttributes result = new SvgAttributes();
		// ::comment when __HAXE__
		final Matcher2 m = p.matcher(args);
		while (m.find())
			result.attributes.put(m.group(1), StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(m.group(2)));
		// ::done

		return result;
	}

	public Map<String, String> attributes() {
		return Collections.unmodifiableMap(attributes);
	}

	public SvgAttributes add(String key, String value) {
		final SvgAttributes result = cloneMe();
		result.attributes.put(key, value);
		return result;
	}

	public SvgAttributes add(SvgAttributes toBeAdded) {
		final SvgAttributes result = cloneMe();
		result.attributes.putAll(toBeAdded.attributes);
		return result;
	}

}
