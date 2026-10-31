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
 */
package net.sourceforge.plantuml.yaml.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts a Monomorph tree to plain Java values: a map becomes a
 * LinkedHashMap (keys in document order), a list an ArrayList and a scalar a
 * String. Every scalar stays a String, whatever it looks like ("3.5", "true").
 */
public class MonomorphToPlain {

	public static Object convert(Monomorph input) {
		if (input.getType() == MonomorphType.SCALAR)
			return input.getValue();
		if (input.getType() == MonomorphType.LIST) {
			final List<Object> result = new ArrayList<Object>();
			for (int i = 0; i < input.size(); i++)
				result.add(convert(input.getElementAt(i)));
			return result;
		}
		if (input.getType() == MonomorphType.MAP) {
			final Map<String, Object> result = new LinkedHashMap<String, Object>();
			for (String key : input.keys())
				result.put(key, convert(input.getMapValue(key)));
			return result;
		}
		// UNDETERMINATE: nothing was put in it (an empty document)
		return new LinkedHashMap<String, Object>();
	}

}
