/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.command.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Log;

public class Matcher2 {

	private final static boolean INSTRUMENT = false;
	private final Matcher matcher;
	private final String id;

	private Matcher2(Matcher matcher, String id) {
		this.matcher = matcher;
		this.id = id;
	}

	public static Matcher2 build(Pattern pattern, CharSequence input) {
		final long now = System.currentTimeMillis();
		final String id = pattern.pattern();
		try {
			final Matcher matcher2 = pattern.matcher(input);
			return new Matcher2(matcher2, id);
		} finally {
			if (INSTRUMENT) {
				addTime(id, System.currentTimeMillis() - now);
			}
		}
	}

	public boolean matches() {
		final long now = System.currentTimeMillis();
		try {
			return matcher.matches();
		} finally {
			addTime(System.currentTimeMillis() - now);
		}
	}

	private void addTime(long duration) {
		if (INSTRUMENT == false) {
			return;
		}
		addTime(id, duration);
	}

	private static final Map<String, Long> durations = new HashMap<String, Long>();
	private static long printed;

	private static synchronized void addTime(String id, long duration) {
		Long total = durations.get(id);
		if (total == null) {
			total = 0L;
		}
		total += duration;
		durations.put(id, total);
		final String longest = getLongest();
		if (longest == null) {
			return;
		}
		if (durations.get(longest) > printed) {
			Log.info("---------- Regex " + longest + " " + durations.get(longest) + "ms (" + durations.size() + ")");
			printed = durations.get(longest);
		}

	}

	private static String getLongest() {
		long max = 0;
		String result = null;
		for (Map.Entry<String, Long> ent : durations.entrySet()) {
			if (ent.getValue() > max) {
				max = ent.getValue();
				result = ent.getKey();
			}
		}
		return result;
	}

	public String group(int n) {
		final long now = System.currentTimeMillis();
		try {
			return matcher.group(n);
		} finally {
			addTime(System.currentTimeMillis() - now);
		}
	}

	public String group() {
		final long now = System.currentTimeMillis();
		try {
			return matcher.group();
		} finally {
			addTime(System.currentTimeMillis() - now);
		}
	}

	public int groupCount() {
		final long now = System.currentTimeMillis();
		try {
			return matcher.groupCount();
		} finally {
			addTime(System.currentTimeMillis() - now);
		}
	}

	public boolean find() {
		final long now = System.currentTimeMillis();
		try {
			return matcher.find();
		} finally {
			addTime(System.currentTimeMillis() - now);
		}
	}

	public int end() {
		final long now = System.currentTimeMillis();
		try {
			return matcher.end();
		} finally {
			addTime(System.currentTimeMillis() - now);
		}
	}

	public int start() {
		final long now = System.currentTimeMillis();
		try {
			return matcher.start();
		} finally {
			addTime(System.currentTimeMillis() - now);
		}
	}

}
