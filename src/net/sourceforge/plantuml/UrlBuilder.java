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
package net.sourceforge.plantuml;

import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;

public class UrlBuilder {

	public static enum ModeUrl {
		STRICT, ANYWHERE
	}

	private static final String S_QUOTED = "\\[\\[[%s]*" + //
			"[%g]([^%g]+)[%g]" + // Quoted part
			"(?:[%s]*\\{([^{}]*)\\})?" + // Optional tooltip
			"(?:[%s]([^%s\\{\\}\\[\\]][^\\[\\]]*))?" + // Optional label
			"[%s]*\\]\\]";

	private static final String S_ONLY_TOOLTIP = "\\[\\[[%s]*" + //
			"\\{(.*)\\}" + // Tooltip
			"[%s]*\\]\\]";

	private static final String S_ONLY_TOOLTIP_AND_LABEL = "\\[\\[[%s]*" + //
			"\\{([^{}]*)\\}" + // Tooltip
			"[%s]*" + //
			"([^\\[%s\\{\\}\\[\\]][^\\[\\]]*)" // Label
			+ "[%s]*\\]\\]";

	private static final String S_LINK_TOOLTIP_NOLABEL = "\\[\\[[%s]*" + //
			"([^\\s%g{}\\[\\]]+?)" + // Link
			"[%s]*\\{(.+)\\}" + // Tooltip
			"[%s]*\\]\\]";

	private static final String S_LINK_WITH_OPTIONAL_TOOLTIP_WITH_OPTIONAL_LABEL = "\\[\\[[%s]*" + //
			"([^%s%g\\[\\]]+?)" + // Link
			"(?:[%s]*\\{([^{}]*)\\})?" + // Optional tooltip
			"(?:[%s]([^%s\\{\\}\\[\\]][^\\[\\]]*))?" + // Optional label
			"[%s]*\\]\\]";

	public static String getRegexp() {
		return S_QUOTED + "|" + //
				S_ONLY_TOOLTIP + "|" + //
				S_ONLY_TOOLTIP_AND_LABEL + "|" + //
				S_LINK_TOOLTIP_NOLABEL + "|" + //
				S_LINK_WITH_OPTIONAL_TOOLTIP_WITH_OPTIONAL_LABEL;
	}

	private static final Pattern2 QUOTED = MyPattern.cmpile(S_QUOTED);
	private static final Pattern2 ONLY_TOOLTIP = MyPattern.cmpile(S_ONLY_TOOLTIP);
	private static final Pattern2 ONLY_TOOLTIP_AND_LABEL = MyPattern.cmpile(S_ONLY_TOOLTIP_AND_LABEL);
	private static final Pattern2 LINK_TOOLTIP_NOLABEL = MyPattern.cmpile(S_LINK_TOOLTIP_NOLABEL);
	private static final Pattern2 LINK_WITH_OPTIONAL_TOOLTIP_WITH_OPTIONAL_LABEL = MyPattern
			.cmpile(S_LINK_WITH_OPTIONAL_TOOLTIP_WITH_OPTIONAL_LABEL);

	private final String topurl;
	private ModeUrl mode;

	public UrlBuilder(String topurl, ModeUrl mode) {
		this.topurl = topurl;
		this.mode = mode;
	}

	public Url getUrl(String s) {
		Matcher2 m;
		m = QUOTED.matcher(s);
		if (matchesOrFind(m)) {
			return new Url(withTopUrl(m.group(1)), m.group(2), m.group(3));
		}

		m = ONLY_TOOLTIP.matcher(s);
		if (matchesOrFind(m)) {
			return new Url("", m.group(1), null);
		}

		m = ONLY_TOOLTIP_AND_LABEL.matcher(s);
		if (matchesOrFind(m)) {
			return new Url("", m.group(1), m.group(2));
		}

		m = LINK_TOOLTIP_NOLABEL.matcher(s);
		if (matchesOrFind(m)) {
			return new Url(withTopUrl(m.group(1)), m.group(2), null);
		}

		m = LINK_WITH_OPTIONAL_TOOLTIP_WITH_OPTIONAL_LABEL.matcher(s);
		if (matchesOrFind(m)) {
			return new Url(withTopUrl(m.group(1)), m.group(2), m.group(3));
		}

		return null;

	}

	private boolean matchesOrFind(Matcher2 m) {
		if (mode == ModeUrl.STRICT) {
			return m.matches();
		} else if (mode == ModeUrl.ANYWHERE) {
			return m.find();
		} else {
			throw new IllegalStateException();
		}
	}

	private String withTopUrl(String url) {
		if (url.startsWith("http:") == false && url.startsWith("https:") == false && url.startsWith("file:") == false
				&& topurl != null) {
			return topurl + url;
		}
		return url;
	}

}
