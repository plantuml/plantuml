/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 11025 $
 *
 */
package net.sourceforge.plantuml.creole;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.FontConfiguration;

public class CreoleStripeSimpleParser {

	final private String line;
	final private StripeStyle style;

	private final FontConfiguration fontConfiguration;
	private final ISkinParam skinParam;

	public CreoleStripeSimpleParser(String line, FontConfiguration fontConfiguration, ISkinParam skinParam) {
		this.fontConfiguration = fontConfiguration;
		this.skinParam = skinParam;

		final Pattern p4 = Pattern.compile("^--([^-]*)--$");
		final Matcher m4 = p4.matcher(line);
		if (m4.find()) {
			this.line = m4.group(1);
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '-');
			return;
		}

		final Pattern p5 = Pattern.compile("^==([^=]*)==$");
		final Matcher m5 = p5.matcher(line);
		if (m5.find()) {
			this.line = m5.group(1);
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '=');
			return;
		}

		final Pattern p6 = Pattern.compile("^__([^_]*)__$");
		final Matcher m6 = p6.matcher(line);
		if (m6.find()) {
			this.line = m6.group(1);
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '_');
			return;
		}

		final Pattern p7 = Pattern.compile("^\\.\\.([^\\.]*)\\.\\.$");
		final Matcher m7 = p7.matcher(line);
		if (m7.find()) {
			this.line = m7.group(1);
			this.style = new StripeStyle(StripeStyleType.HORIZONTAL_LINE, 0, '.');
			return;
		}

		final Pattern p1 = Pattern.compile("^(\\*+)([^*]+)$");
		final Matcher m1 = p1.matcher(line);
		if (m1.find()) {
			this.line = m1.group(2).trim();
			final int order = m1.group(1).length() - 1;
			this.style = new StripeStyle(StripeStyleType.LIST_WITHOUT_NUMBER, order, '\0');
			return;
		}

		final Pattern p2 = Pattern.compile("^(#+)([^*]+)$");
		final Matcher m2 = p2.matcher(line);
		if (m2.find()) {
			this.line = m2.group(2).trim();
			final int order = m2.group(1).length() - 1;
			this.style = new StripeStyle(StripeStyleType.LIST_WITH_NUMBER, order, '\0');
			return;
		}

		final Pattern p3 = Pattern.compile("^(=+)([^*]+)$");
		final Matcher m3 = p3.matcher(line);
		if (m3.find()) {
			this.line = m3.group(2).trim();
			final int order = m3.group(1).length() - 1;
			this.style = new StripeStyle(StripeStyleType.HEADING, order, '\0');
			return;
		}

		this.line = line;
		this.style = new StripeStyle(StripeStyleType.NORMAL, 0, '\0');

	}

	public Stripe createStripe(CreoleContext context) {
		final StripeSimple result = new StripeSimple(fontConfiguration, style, context, skinParam);
		result.analyzeAndAdd(line);
		return result;
	}

}
