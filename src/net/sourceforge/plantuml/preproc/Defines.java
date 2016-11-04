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
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.preproc;

import gen.lib.pack.pack__c;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Defines {

	private final Map<String, Define> values = new LinkedHashMap<String, Define>();
	private final Map<String, Define> savedState = new LinkedHashMap<String, Define>();

	public void define(String name, List<String> value) {
		values.put(name, new Define(name, value));
	}

	public boolean isDefine(String name) {
		for (String key : values.keySet()) {
			if (key.equals(name) || key.startsWith(name + "(")) {
				return true;
			}
		}
		return false;
	}

	public void undefine(String name) {
		values.remove(name);
	}

	public List<String> applyDefines(String line) {
		line = manageDate(line);
		for (Map.Entry<String, Define> ent : values.entrySet()) {
			final Define def = ent.getValue();
			line = def.apply(line);
		}
		return Arrays.asList(line.split("\n"));
	}

	private static final String DATE = "(?i)%date(\\[(.+?)\\])?%";
	private final static Pattern datePattern = Pattern.compile(DATE);

	private String manageDate(String line) {
		final Matcher m = datePattern.matcher(line);
		if (m.find()) {
			final String format = m.group(2);
			String replace;
			if (format == null) {
				replace = new Date().toString();
			} else {
				try {
					replace = new SimpleDateFormat(format).format(new Date());
				} catch (Exception e) {
					replace = "(BAD DATE PATTERN:" + format + ")";
				}
			}
			line = line.replaceAll(DATE, replace);
		}
		return line;
	}

	public void saveState() {
		if (savedState.size() > 0) {
			throw new IllegalStateException();
		}
		this.savedState.putAll(values);

	}

	public void restoreState() {
		this.values.clear();
		this.values.putAll(savedState);

	}

}
