/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6396 $
 *
 */
package net.sourceforge.plantuml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.command.regex.MyPattern;

public class UrlBuilder {

	public static enum ModeUrl {
		STRICT, AT_START, ANYWHERE, AT_END
	}

	private static String level0() {
		return "(?:[^{}]|\\{[^{}]*\\})+";
	}

	private static String levelN(int n) {
		if (n == 0) {
			return level0();
		}
		return "(?:[^{}]|\\{" + levelN(n - 1) + "\\})+";
	}

	private static final String URL_PATTERN_OLD = "\\[\\[([%g][^%g]+[%g]|[^{}%s\\]\\[]*)(?:[%s]*\\{((?:[^{}]|\\{[^{}]*\\})+)\\})?(?:[%s]*([^\\]\\[]+))?\\]\\]";

	private static final String URL_PATTERN = "\\[\\[([%g][^%g]+[%g]|[^{}%s\\]\\[]*)(?:[%s]*\\{" + "(" + levelN(3)
			+ ")" + "\\})?(?:[%s]*([^\\]\\[]+))?\\]\\]";

	private final String topurl;
	private ModeUrl mode;

	public UrlBuilder(String topurl, ModeUrl mode) {
		this.topurl = topurl;
		this.mode = mode;
	}

	public static String multilineTooltip(String label) {
		final Pattern p = MyPattern.cmpile("(?i)^(" + URL_PATTERN + ")?(.*)$");
		final Matcher m = p.matcher(label);
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
		final Pattern p;
		if (mode == ModeUrl.STRICT) {
			p = MyPattern.cmpile("(?i)^" + URL_PATTERN + "$");
		} else if (mode == ModeUrl.AT_START) {
			p = MyPattern.cmpile("(?i)^" + URL_PATTERN + ".*");
		} else if (mode == ModeUrl.AT_END) {
			p = MyPattern.cmpile("(?i).*" + URL_PATTERN + "$");
		} else if (mode == ModeUrl.ANYWHERE) {
			p = MyPattern.cmpile("(?i).*" + URL_PATTERN + ".*");
		} else {
			throw new IllegalStateException();
		}
		final Matcher m = p.matcher(StringUtils.trinNoTrace(s));
		if (m.matches() == false) {
			return null;
		}
		String url = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(m.group(1));
		if (url.startsWith("http:") == false && url.startsWith("https:") == false) {
			// final String top = getSystem().getSkinParam().getValue("topurl");
			if (topurl != null) {
				url = topurl + url;
			}
		}
		return new Url(url, m.group(2), m.group(3));
	}

	public static String getRegexp() {
		return URL_PATTERN;
	}

	public static String purgeUrl(final String label) {
		final Pattern p = MyPattern.cmpile("[%s]*" + URL_PATTERN + "[%s]*");
		final Matcher m = p.matcher(label);
		if (m.find() == false) {
			throw new IllegalStateException();
		}
		final String url = m.group(0);
		final int x = label.indexOf(url);
		return label.substring(0, x) + label.substring(x + url.length());
	}

}
