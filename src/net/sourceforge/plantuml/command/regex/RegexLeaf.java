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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringLocated;

public class RegexLeaf implements IRegex {

	private static final RegexLeaf END = new RegexLeaf("$");
	private static final RegexLeaf START = new RegexLeaf("^");
	private final String pattern;
	private final String name;

	private int count = -1;

	public RegexLeaf(String regex) {
		this(null, regex);
	}

	public static RegexLeaf spaceZeroOrMore() {
		return new RegexLeaf("[%s]*");
	}

	public static RegexLeaf spaceOneOrMore() {
		return new RegexLeaf("[%s]+");
	}

	public static RegexLeaf start() {
		return START;
	}

	public static RegexLeaf end() {
		return END;
	}

	public RegexLeaf(String name, String regex) {
		this.pattern = regex;
		this.name = name;
	}

	@Override
	public String toString() {
		return super.toString() + " " + name + " " + pattern;
	}

	public String getName() {
		return name;
	}

	public String getPattern() {
		return pattern;
	}

	public int count() {
		if (count == -1) {
			count = MyPattern.cmpile(pattern, Pattern.CASE_INSENSITIVE).matcher("").groupCount();
		}
		return count;
	}

	public Map<String, RegexPartialMatch> createPartialMatch(Iterator<String> it) {
		final RegexPartialMatch m = new RegexPartialMatch(name);
		for (int i = 0; i < count(); i++) {
			final String group = it.next();
			m.add(group);
		}
		if (name == null) {
			return Collections.emptyMap();
		}
		return Collections.singletonMap(name, m);
	}

	public boolean match(StringLocated full) {
		throw new UnsupportedOperationException();
	}

	public RegexResult matcher(String full) {
		throw new UnsupportedOperationException();
	}

	static private final Set<String> UNKNOWN = new HashSet<String>();

	static private final Pattern p1 = Pattern.compile("^[-0A-Za-z_!:@;/=,\"]+$");
	static private final Pattern p2 = Pattern.compile("^[-0A-Za-z_!:@;/=,\"]+\\?$");
	static private final Pattern p3 = Pattern
			.compile("^\\(?[-0A-Za-z_!:@;/=\" ]+\\??(\\|[-0A-Za-z_!:@;/=,\" ]+\\??)+\\)?$");

	private static long getSignatureP3(String s) {
		long result = -1L;
		for (StringTokenizer st = new StringTokenizer(s, "()|"); st.hasMoreTokens();) {
			final String val = st.nextToken();
			final long sig = FoxSignature.getFoxSignature(val.endsWith("?") ? val.substring(0, val.length() - 2) : val);
			result = result & sig;
		}
		return result;
	}

	public long getFoxSignatureNone() {
		return 0;
	}

	public long getFoxSignature() {
		if (p1.matcher(pattern).matches()) {
			return FoxSignature.getFoxSignature(pattern);
		}
		if (p2.matcher(pattern).matches()) {
			return FoxSignature.getFoxSignature(pattern.substring(0, pattern.length() - 2));
		}
		if (p3.matcher(pattern).matches()) {
			// System.err.println("special " + pattern);
			// System.err.println("result " + FoxSignature.backToString(getSignatureP3(pattern)));
			return getSignatureP3(pattern);
		}
		if (pattern.length() == 2 && pattern.startsWith("\\") && Character.isLetterOrDigit(pattern.charAt(1)) == false) {
			return FoxSignature.getFoxSignature(pattern.substring(1));
		}
		if (pattern.equals("\\<\\>") || pattern.equals("(\\<\\<.*\\>\\>)")) {
			return FoxSignature.getFoxSignature("<>");
		}
		if (pattern.equals("\\<-\\>")) {
			return FoxSignature.getFoxSignature("<->");
		}
		if (pattern.equals("(-+)")) {
			return FoxSignature.getFoxSignature("-");
		}
		if (pattern.equals("\\|+") || pattern.equals("\\|\\|")) {
			return FoxSignature.getFoxSignature("|");
		}
		if (pattern.equals("([*]+)")) {
			return FoxSignature.getFoxSignature("*");
		}
		if (pattern.equals("[%s]+") || pattern.equals("[%s]*")) {
			return 0;
		}
//		synchronized (UNKNOWN) {
//			final boolean changed = UNKNOWN.add(pattern);
//			if (changed)
//				System.err.println("unknow=" + pattern);
//
//		}
		return 0;
	}

}
