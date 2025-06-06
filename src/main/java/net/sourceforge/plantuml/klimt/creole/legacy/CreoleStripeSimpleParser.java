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
package net.sourceforge.plantuml.klimt.creole.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.klimt.creole.CreoleContext;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Stripe;
import net.sourceforge.plantuml.klimt.creole.StripeStyle;
import net.sourceforge.plantuml.klimt.creole.StripeStyleType;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.style.ISkinSimple;
import net.sourceforge.plantuml.utils.CharHidder;

public class CreoleStripeSimpleParser {

	final private String line;
	final private StripeStyle style;
	private final CreoleMode modeSimpleLine;

	private final FontConfiguration fontConfiguration;
	private final ISkinSimple skinParam;

	static private final Pattern2 SECTION_TITLE_PATTERN = Pattern2.cmpile("^==([^=]*)==$");
	static private final Pattern2 SECTION_SEPARATOR_PATTERN = Pattern2.cmpile("^===*==$");
	static private final Pattern2 DOUBLE_DOT_DELIMITED_LINE = Pattern2.cmpile("^\\.\\.([^\\.]*)\\.\\.$");
	static private final Pattern2 ASTERISK_PREFIXED_LINE_PATTERN = Pattern2.cmpile("^(\\*+)([^*]+(?:[^*]|\\*\\*[^*]+\\*\\*)*)$");
	static private final Pattern2 ASTERISK_HEADER_LINE_PATTERN = Pattern2.cmpile("^(\\*+)([%s].+)$");
	static private final Pattern2 HASH_HEADING_PATTERN = Pattern2.cmpile("^(#+)(.+)$");
	static private final Pattern2 EQUALS_HEADING_PATTERN = Pattern2.cmpile("^(=+)(.+)$");
	static private final Pattern2 SECTION_HEADER_PATTERN = Pattern2.cmpile("^--([^-]*)--$");

	public CreoleStripeSimpleParser(String line, CreoleContext creoleContext, FontConfiguration fontConfiguration,
			ISkinSimple skinParam, CreoleMode mode) {
//		if (line.contains("" + BackSlash.hiddenNewLine()))
//			throw new IllegalArgumentException(line);

		if (Jaws.TRACE)
			System.err.println("CreoleStripeSimpleParser " + line);
		this.fontConfiguration = fontConfiguration;
		this.modeSimpleLine = mode;
		this.skinParam = Objects.requireNonNull(skinParam);

		if (mode == CreoleMode.NO_CREOLE) {
			this.line = line;
			this.style = new StripeStyle(StripeStyleType.NORMAL, 0, '\0');
			return;
		}

		final Matcher2 m4 = SECTION_HEADER_PATTERN.matcher(line);
		if (m4.find()) {
			this.line = m4.group(1);
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '-');
			return;
		}

		final Matcher2 m5 = SECTION_TITLE_PATTERN.matcher(line);
		if (m5.find()) {
			this.line = m5.group(1);
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '=');
			return;
		}
		final Matcher2 m5b = SECTION_SEPARATOR_PATTERN.matcher(line);
		if (m5b.find()) {
			this.line = "";
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '=');
			return;
		}

		final Matcher2 m7 = DOUBLE_DOT_DELIMITED_LINE.matcher(line);
		if (m7.find()) {
			this.line = m7.group(1);
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '.');
			return;
		}

		if (mode == CreoleMode.FULL) {
			final Matcher2 m1 = ASTERISK_PREFIXED_LINE_PATTERN.matcher(line);
			if (m1.find()) {
				this.line = StringUtils.trin(m1.group(2));
				final int order = m1.group(1).length() - 1;
				this.style = new StripeStyle(StripeStyleType.LIST_WITHOUT_NUMBER, order, '\0');
				return;
			}
		}

		if (mode == CreoleMode.FULL) {
			final Matcher2 m1 = ASTERISK_HEADER_LINE_PATTERN.matcher(line);
			if (m1.find()) {
				this.line = StringUtils.trin(m1.group(2));
				final int order = m1.group(1).length() - 1;
				this.style = new StripeStyle(StripeStyleType.LIST_WITHOUT_NUMBER, order, '\0');
				return;
			}
		}

		if (mode == CreoleMode.FULL) {
			final Matcher2 m2 = HASH_HEADING_PATTERN.matcher(CharHidder.hide(line));
			if (m2.find()) {
				this.line = StringUtils.trin(CharHidder.unhide(m2.group(2)));
				final int order = CharHidder.unhide(m2.group(1)).length() - 1;
				this.style = new StripeStyle(StripeStyleType.LIST_WITH_NUMBER, order, '\0');
				return;
			}
		}

		final Matcher2 m3 = EQUALS_HEADING_PATTERN.matcher(line);
		if (m3.find()) {
			this.line = StringUtils.trin(m3.group(2));
			final int order = m3.group(1).length() - 1;
			this.style = new StripeStyle(StripeStyleType.HEADING, order, '\0');
			return;
		}

		this.line = line;
		this.style = new StripeStyle(StripeStyleType.NORMAL, 0, '\0');

	}

	public List<Stripe> createStripes(CreoleContext context) {
		final List<Stripe> result = new ArrayList<>();
		for (String singleLine : line.split("" + Jaws.BLOCK_E1_NEWLINE)) {
			final StripeSimple stripe = new StripeSimple(fontConfiguration, style, context, skinParam, modeSimpleLine);
			stripe.analyzeAndAdd(singleLine);
			result.add(stripe);
		}
		return Collections.unmodifiableList(result);
	}

}
