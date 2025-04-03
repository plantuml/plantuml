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
package net.sourceforge.plantuml.url;

import java.util.List;

import com.plantuml.ubrex.TextNavigator;
import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.UnicodeBracketedExpression;

// UrlUbrexTest
public class UbrexUrlBuilder {
	// ::remove file when __HAXE__

//	public static final IRegex MANDATORY = new RegexLeaf("URL", "(" + UbrexUrlBuilder.getRegexp() + ")");
//	public static final IRegex OPTIONAL = new RegexOptional(MANDATORY);

	private static final String START_PART = "[[ 〇*〴s";
	private static final String END_PART = "〇*〴s  ]]";

	private static final String S_QUOTED = START_PART + //
			"〃  〶$URL=〇+「〤〃」 〃" + // Quoted part
			"〇?〘 〇*〴s { 〶$TOOLTIP=〇*「〤{}」 } 〙" + // Optional tooltip
			"〇?〘 〴s 〶$LABEL=〘「〤〴s{}[]」  〇*「〤[]」〙  〙" + // Optional label
			END_PART;

	private static final String S_ONLY_TOOLTIP = START_PART + //
			"{  〶$TOOLTIP=〄>〘 }〇*〴s]]〙"; // Tooltip WARNING!! Not exactly the same

	private static final String S_ONLY_TOOLTIP_AND_LABEL = START_PART + //
			"{  〶$TOOLTIP=〇+「〤{}」 }" + // Tooltip
			"〇*〴s" + //
			"〶$LABEL=〘「〤〴s[]{}」 〇*「〤[]」〙" // Label
			+ END_PART;

	private static final String S_LINK_TOOLTIP_NOLABEL = START_PART + //
			"〶$URL=〇+「〤〴s〴g{}[]」" + // Link
			"〇*〴s" + //
			"{  〶$TOOLTIP=〄>〘 }〇*〴s]]〙"; // Tooltip

	private static final String S_LINK_WITH_OPTIONAL_TOOLTIP_WITH_OPTIONAL_LABEL = START_PART + //
			"〶$URL=〇+「〤〴s〴g{}[]」" + // Link
			"〇?〘  〇*〴s  {  〶$TOOLTIP=〇*「〤{}」  } 〙" + // Optional tooltip
			"〇?〘  〴s 〶$LABEL=〘「〤〴s{}[]」 〇*「〤[]」〙  〙" + // Optional label
			END_PART;

	private static final String S_LINK_NOTOOLTIP_WITH_OPTIONAL_LABEL = START_PART + //
			"〶$URL=〇+「〤〴s〴g[]」" + // Link
			"〇?〘  〴s 〶$LABEL=〘「〤〴s{}[]」 〇*「〤[]」〙  〙" + // Optional label
			END_PART;

//	public static String getRegexp() {
//		return S_QUOTED + "|" + //
//				S_ONLY_TOOLTIP + "|" + //
//				S_ONLY_TOOLTIP_AND_LABEL + "|" + //
//				S_LINK_TOOLTIP_NOLABEL + "|" + //
//				S_LINK_WITH_OPTIONAL_TOOLTIP_WITH_OPTIONAL_LABEL;
//	}
//
	private static final UnicodeBracketedExpression QUOTED = UnicodeBracketedExpression.build(S_QUOTED);
	private static final UnicodeBracketedExpression ONLY_TOOLTIP = UnicodeBracketedExpression.build(S_ONLY_TOOLTIP);
	private static final UnicodeBracketedExpression ONLY_TOOLTIP_AND_LABEL = UnicodeBracketedExpression
			.build(S_ONLY_TOOLTIP_AND_LABEL);
	private static final UnicodeBracketedExpression LINK_TOOLTIP_NOLABEL = UnicodeBracketedExpression
			.build(S_LINK_TOOLTIP_NOLABEL);
	private static final UnicodeBracketedExpression LINK_WITH_OPTIONAL_TOOLTIP_WITH_OPTIONAL_LABEL = UnicodeBracketedExpression
			.build(S_LINK_WITH_OPTIONAL_TOOLTIP_WITH_OPTIONAL_LABEL);
	private static final UnicodeBracketedExpression LINK_NOTOOLTIP_WITH_OPTIONAL_LABEL = UnicodeBracketedExpression
			.build(S_LINK_NOTOOLTIP_WITH_OPTIONAL_LABEL);

	private final String topurl;
	private UrlMode mode;

	public UbrexUrlBuilder(String topurl, UrlMode mode) {
		this.topurl = topurl;
		this.mode = mode;
	}

	public Url getUrl(String s) {
		UMatcher m;
		m = getMatcher(QUOTED, s);
		if (matchesOrFind(m))
			return buildUrl(m);

		m = getMatcher(ONLY_TOOLTIP, s);
		if (matchesOrFind(m))
			return buildUrl(m);

		m = getMatcher(ONLY_TOOLTIP_AND_LABEL, s);
		if (matchesOrFind(m))
			return buildUrl(m);

		m = getMatcher(LINK_TOOLTIP_NOLABEL, s);
		if (matchesOrFind(m))
			return buildUrl(m);

		m = getMatcher(LINK_WITH_OPTIONAL_TOOLTIP_WITH_OPTIONAL_LABEL, s);
		if (matchesOrFind(m))
			return buildUrl(m);

		m = getMatcher(LINK_NOTOOLTIP_WITH_OPTIONAL_LABEL, s);
		if (matchesOrFind(m))
			return buildUrl(m);

		return null;
	}

	private UMatcher getMatcher(UnicodeBracketedExpression ubrex, String s) {
		if (mode == UrlMode.STRICT)
			return ubrex.match(s);
		final TextNavigator tn = TextNavigator.build(s);
		for (int i = 0; i < tn.length() - 2; i++) {
			final UMatcher matcher = ubrex.match(tn, i);
			if (matcher.startMatch())
				return matcher;
		}
		return new UMatchNone();
	}

	private Url buildUrl(UMatcher m) {
		final String url = getValue(m, "URL");
		final String tooltip = getValue(m, "TOOLTIP");
		final String label = getValue(m, "LABEL");
		return new Url(withTopUrl(url), tooltip, label);
	}

	private String getValue(UMatcher m, String key) {
		final List<String> list = m.getCapture(key);
		if (list.size() == 0)
			return null;
		return list.get(0);
	}

	private boolean matchesOrFind(UMatcher m) {
		if (mode == UrlMode.STRICT)
			return m.exactMatch();
		else if (mode == UrlMode.ANYWHERE)
			return m.startMatch();
		else
			throw new IllegalStateException();

	}

	private String withTopUrl(String url) {
		if (url == null)
			return "";
		if (url.startsWith("http:") == false && url.startsWith("https:") == false && url.startsWith("file:") == false
				&& topurl != null)
			return topurl + url;

		return url;
	}

	static class UMatchNone implements UMatcher {

		@Override
		public boolean startMatch() {
			return false;
		}

		@Override
		public boolean exactMatch() {
			return false;
		}

		@Override
		public String getAcceptedMatch() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<String> getCapture(String path) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<String> getKeysToBeRefactored() {
			throw new UnsupportedOperationException();
		}

	}

}
