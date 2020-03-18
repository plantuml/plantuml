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
package net.sourceforge.plantuml.utils;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cypher {

	final private static Pattern p = Pattern.compile("[\\p{L}\\p{N}]+");

	private final SecureRandom rnd = new SecureRandom();
	private final Map<String, String> convert = new HashMap<String, String>();
	private final Set<String> except = new HashSet<String>();

	public synchronized String cypher(String s) {

		final Matcher m = p.matcher(s);
		final StringBuffer sb = new StringBuffer();
		while (m.find()) {
			final String word = m.group(0);
			m.appendReplacement(sb, changeWord(word));
		}
		m.appendTail(sb);

		return sb.toString();
	}

	private String changeWord(final String word) {
		final String lower = word.toLowerCase();
		if (except.contains(lower) || lower.matches("^([a-f0-9]{3}|[a-f0-9]{6})$")) {
			return word;
		}
		String res = convert.get(word);
		if (res != null) {
			return res;
		}
		int len = word.length();
		if (len < 4) {
			len = 4;
		}
		while (true) {
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < len; i++) {
				final char letter = (char) ('a' + rnd.nextInt(26));
				sb.append(letter);
			}
			res = sb.toString();
			if (convert.containsValue(res) == false) {
				convert.put(word, res);
				return res;
			}
		}
	}

	public void addException(String word) {
		except.add(word.toLowerCase());
	}

}
