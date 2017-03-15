/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml;

import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;

public class UrlBuilder {

	public static enum ModeUrl {
		STRICT, ANYWHERE
	}

	// private static String level0() {
	// return "(?:[^{}]|\\{[^{}]*\\})+";
	// }
	//
	// private static String levelN(int n) {
	// if (n == 0) {
	// return level0();
	// }
	// return "(?:[^{}]|\\{" + levelN(n - 1) + "\\})+";
	// }

	// private static final String URL_PATTERN_OLD =
	// "\\[\\[([%g][^%g]+[%g]|[^{}%s\\]\\[]*)(?:[%s]*\\{((?:[^{}]|\\{[^{}]*\\})+)\\})?(?:[%s]*([^\\]\\[]+))?\\]\\]";
	private static final String URL_PATTERN = "\\[\\[([%g][^%g]+[%g])?([\\w\\W]*?)\\]\\]";

	// private static final String URL_PATTERN_BAD = "\\[\\[([%g][^%g]+[%g]|[^{}%s\\]\\[]*)(?:[%s]*\\{" + "(" +
	// levelN(3)
	// + ")" + "\\})?(?:[%s]*([^\\]\\[]+))?\\]\\]";

	private final String topurl;
	private ModeUrl mode;

	public UrlBuilder(String topurl, ModeUrl mode) {
		this.topurl = topurl;
		this.mode = mode;
	}

	private static String multilineTooltip(String label) {
		final Pattern2 p = MyPattern.cmpile("(?i)^(" + URL_PATTERN + ")?(.*)$");
		final Matcher2 m = p.matcher(label);
		if (m.matches() == false) {
			return label;
		}
		String gr1 = m.group(1);
		if (gr1 == null) {
			return label;
		}
		final String gr2 = m.group(m.groupCount());
		gr1 = gr1.replaceAll("\\\\n", "\n");
		return gr1 + gr2;
	}

	public Url getUrl(String s) {
		final Pattern2 p;
		if (mode == ModeUrl.STRICT) {
			p = MyPattern.cmpile("(?i)^" + URL_PATTERN + "$");
		} else if (mode == ModeUrl.ANYWHERE) {
			p = MyPattern.cmpile("(?i).*" + URL_PATTERN + ".*");
		} else {
			throw new IllegalStateException();
		}
		final Matcher2 m = p.matcher(StringUtils.trinNoTrace(s));
		if (m.matches() == false) {
			return null;
		}
		// String url = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(m.group(1));
		// if (url.startsWith("http:") == false && url.startsWith("https:") == false) {
		// // final String top = getSystem().getSkinParam().getValue("topurl");
		// if (topurl != null) {
		// url = topurl + url;
		// }
		// }

		final String quotedPart = m.group(1);
		final String full = m.group(2);
		final int openBracket = full.indexOf('{');
		final int closeBracket = full.lastIndexOf('}');
		if (quotedPart == null) {
			if (openBracket != -1 && closeBracket != -1) {
				return new Url(withTopUrl(full.substring(0, openBracket)),
						full.substring(openBracket + 1, closeBracket), full.substring(closeBracket + 1).trim());
			}
			final int firstSpace = full.indexOf(' ');
			if (firstSpace == -1) {
				return new Url(full, null, null);
			}
			return new Url(withTopUrl(full.substring(0, firstSpace)), null, full.substring(firstSpace + 1).trim());
		}
		if (openBracket != -1 && closeBracket != -1) {
			return new Url(withTopUrl(quotedPart), full.substring(openBracket + 1, closeBracket), full.substring(
					closeBracket + 1).trim());
		}
		return new Url(withTopUrl(quotedPart), null, null);
	}

	private String withTopUrl(String url) {
		if (url.startsWith("http:") == false && url.startsWith("https:") == false && topurl != null) {
			return topurl + url;
		}
		return url;
	}

	public static String getRegexp() {
		return URL_PATTERN;
	}

	// private static String purgeUrl(final String label) {
	// final Pattern2 p = MyPattern.cmpile("[%s]*" + URL_PATTERN + "[%s]*");
	// final Matcher2 m = p.matcher(label);
	// if (m.find() == false) {
	// return label;
	// }
	// final String url = m.group(0);
	// final int x = label.indexOf(url);
	// return label.substring(0, x) + label.substring(x + url.length());
	// }

}
