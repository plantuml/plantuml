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
package net.sourceforge.plantuml.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.jaws.Jaws;

public class Pattern2 {

	private static final ConcurrentHashMap<String, AtomicInteger> COUNT = new ConcurrentHashMap<>();

	private static final Pattern2 EMPTY = new Pattern2("");

	private final String patternString;
	private final Lazy<Pattern> pattern;

	private Pattern2(String s) {
		this.patternString = s;
		this.pattern = new Lazy<>(() -> compileInternal(patternString));

	}

	public Matcher2 matcher(CharSequence input) {
		return Matcher2.build(pattern.get(), input);
	}

	public String pattern() {
		return patternString;
	}

	public static Pattern2 cmpile(final String p) {
		if (p == null || p.length() == 0)
			return EMPTY;

		// Assert that the number of compilations for the pattern 'p' remains below the
		// threshold (5);
		// this helps detect potential excessive or unintended pattern compilations.
		// Note: using 'assert' does not impact performance in production, as assertions
		// are typically disabled by default.
		// assert COUNT.computeIfAbsent(p, k -> new AtomicInteger()).incrementAndGet() < 5;

		return new Pattern2(p);

	}

	public static Pattern compileInternal(String patternString) {
		return Pattern.compile(transform(patternString), Pattern.CASE_INSENSITIVE);
	}

	private static String transformOld(String p) {
		// Replace ReadLineReader.java
		p = p.replace("%pLN", "\\p{L}\\p{N}"); // Unicode Letter, digit
		p = p.replace("%s", "\\s\u00A0"); // space
		p = p.replace("%q", "'\u2018\u2019"); // quote
		p = p.replace("%g", "\"\u201c\u201d" + Jaws.BLOCK_E1_INVISIBLE_QUOTE); // double quote
		return p;
	}

	private static final Map<String, String> QUOTED_REPLACEMENTS = new HashMap<>();
	static {
		QUOTED_REPLACEMENTS.put("%pLN", Matcher.quoteReplacement("\\p{L}\\p{N}")); // Unicode Letter or digit
		QUOTED_REPLACEMENTS.put("%s", Matcher.quoteReplacement("\\s\u00A0")); // normal or non-breaking space
		QUOTED_REPLACEMENTS.put("%q", Matcher.quoteReplacement("'\u2018\u2019")); // single quotes
		QUOTED_REPLACEMENTS.put("%g", Matcher.quoteReplacement("\"\u201c\u201d" + Jaws.BLOCK_E1_INVISIBLE_QUOTE));
	}

	// private static final Pattern TRANSFORM_PATTERN = Pattern.compile("%pLN|%s|%q|%g");
	private static final Pattern TRANSFORM_PATTERN = Pattern.compile("%(pLN|s|q|g)");

	public static String transform(String input) {
		final Matcher m = TRANSFORM_PATTERN.matcher(input);
		final StringBuffer sb = new StringBuffer(input.length());
		while (m.find()) {
			final String replacement = QUOTED_REPLACEMENTS.get(m.group());
			m.appendReplacement(sb, replacement);
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
