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
package net.sourceforge.plantuml.creole;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.utils.CharHidder;

public class CreoleStripeSimpleParser {

	final private String line;
	final private StripeStyle style;
	private final CreoleMode modeSimpleLine;

	private final FontConfiguration fontConfiguration;
	private final ISkinSimple skinParam;

	public CreoleStripeSimpleParser(String line, CreoleContext creoleContext, FontConfiguration fontConfiguration,
			ISkinSimple skinParam, CreoleMode modeSimpleLine) {
		if (line.contains("" + BackSlash.hiddenNewLine())) {
			throw new IllegalArgumentException(line);
		}
		this.fontConfiguration = fontConfiguration;
		this.modeSimpleLine = modeSimpleLine;
		this.skinParam = skinParam;
		if (skinParam == null) {
			throw new IllegalArgumentException();
		}

		final Pattern2 p4 = MyPattern.cmpile("^--([^-]*)--$");
		final Matcher2 m4 = p4.matcher(line);
		if (m4.find()) {
			this.line = m4.group(1);
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '-');
			return;
		}

		final Pattern2 p5 = MyPattern.cmpile("^==([^=]*)==$");
		final Matcher2 m5 = p5.matcher(line);
		if (m5.find()) {
			this.line = m5.group(1);
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '=');
			return;
		}
		final Pattern2 p5b = MyPattern.cmpile("^===*==$");
		final Matcher2 m5b = p5b.matcher(line);
		if (m5b.find()) {
			this.line = "";
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '=');
			return;
		}

		// if (modeSimpleLine == CreoleMode.FULL) {
		// final Pattern p6 = MyPattern.cmpile("^__([^_]*)__$");
		// final Matcher m6 = p6.matcher(line);
		// if (m6.find()) {
		// this.line = m6.group(1);
		// this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '_');
		// return;
		// }
		// }

		final Pattern2 p7 = MyPattern.cmpile("^\\.\\.([^\\.]*)\\.\\.$");
		final Matcher2 m7 = p7.matcher(line);
		if (m7.find()) {
			this.line = m7.group(1);
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '.');
			return;
		}

		if (modeSimpleLine == CreoleMode.FULL) {
			final Pattern2 p1 = MyPattern.cmpile("^(\\*+)([^*]+(?:[^*]|\\*\\*[^*]+\\*\\*)*)$");
			final Matcher2 m1 = p1.matcher(line);
			if (m1.find()) {
				this.line = StringUtils.trin(m1.group(2));
				final int order = m1.group(1).length() - 1;
				this.style = new StripeStyle(StripeStyleType.LIST_WITHOUT_NUMBER, order, '\0');
				return;
			}
		}

		if (modeSimpleLine == CreoleMode.FULL) {
			final Pattern2 p2 = MyPattern.cmpile("^(#+)(.+)$");
			final Matcher2 m2 = p2.matcher(CharHidder.hide(line));
			if (m2.find()) {
				this.line = StringUtils.trin(CharHidder.unhide(m2.group(2)));
				final int order = CharHidder.unhide(m2.group(1)).length() - 1;
				this.style = new StripeStyle(StripeStyleType.LIST_WITH_NUMBER, order, '\0');
				return;
			}
		}

		final Pattern2 p3 = MyPattern.cmpile("^(=+)(.+)$");
		final Matcher2 m3 = p3.matcher(line);
		if (m3.find()) {
			this.line = StringUtils.trin(m3.group(2));
			final int order = m3.group(1).length() - 1;
			this.style = new StripeStyle(StripeStyleType.HEADING, order, '\0');
			return;
		}

		this.line = line;
		this.style = new StripeStyle(StripeStyleType.NORMAL, 0, '\0');

	}

	public Stripe createStripe(CreoleContext context) {
		final StripeSimple result = new StripeSimple(fontConfiguration, style, context, skinParam, modeSimpleLine);
		result.analyzeAndAdd(line);
		return result;
	}

}
