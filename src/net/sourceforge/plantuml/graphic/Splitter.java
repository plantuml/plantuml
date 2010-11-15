/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 5072 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;

public class Splitter {

	static final String endFontPattern = "\\</font\\>|\\</color\\>|\\</size\\>";
	static final String fontPattern = "\\<font(\\s+size\\s*=\\s*\"?\\d+\"?|\\s+color\\s*=\\s*\"?(#[0-9a-fA-F]{6}|\\w+)\"?)+\\s*\\>";
	static final String fontColorPattern2 = "\\<color[\\s:]+(#[0-9a-fA-F]{6}|#?\\w+)\\s*\\>";
	static final String fontSizePattern2 = "\\<size[\\s:]+(\\d+)\\s*\\>";
	static final String imgPattern = "<img\\s+(src\\s*=\\s*['\"]?[^\\s\">]+['\"]?\\s*|vspace\\s*=\\s*['\"]?\\d+['\"]?\\s*|valign\\s*=\\s*['\"]?(top|middle|bottom)['\"]?\\s*)+>";
	static final String imgPattern2 = "<img[\\s:]+([^>]+)/?>";
	static final String htmlTag;

	private static final Pattern tagOrText;

	static {
		final StringBuilder sb = new StringBuilder("(?i)");

		for (FontStyle style : EnumSet.allOf(FontStyle.class)) {
			if (style == FontStyle.PLAIN) {
				continue;
			}
			sb.append(style.getActivationPattern());
			sb.append('|');
			sb.append(style.getDeactivationPattern());
			sb.append('|');
		}
		sb.append(fontPattern);
		sb.append('|');
		sb.append(fontColorPattern2);
		sb.append('|');
		sb.append(fontSizePattern2);
		sb.append('|');
		sb.append(endFontPattern);
		sb.append('|');
		sb.append(imgPattern);
		sb.append('|');
		sb.append(imgPattern2);

		htmlTag = sb.toString();
		tagOrText = Pattern.compile(htmlTag + "|.+?(?=" + htmlTag + ")|.+$",
				Pattern.CASE_INSENSITIVE);
	}

	private final List<String> splitted = new ArrayList<String>();

	public Splitter(String s) {
		final Matcher matcher = tagOrText.matcher(s);
		while (matcher.find()) {
			String part = matcher.group(0);
			part = StringUtils.showComparatorCharacters(part);
			splitted.add(part);
		}
	}

	List<String> getSplittedInternal() {
		return splitted;
	}

	public List<HtmlCommand> getHtmlCommands() {
		final HtmlCommandFactory factory = new HtmlCommandFactory();
		final List<HtmlCommand> result = new ArrayList<HtmlCommand>();
		for (String s : getSplittedInternal()) {
			result.add(factory.getHtmlCommand(s));
		}
		return Collections.unmodifiableList(result);
	}

}
