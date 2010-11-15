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
 * Revision $Revision: 5506 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.util.EnumSet;
import java.util.regex.Pattern;

class HtmlCommandFactory {

	static final Pattern addStyle;
	static final Pattern removeStyle;

	static {
		final StringBuilder sbAddStyle = new StringBuilder();
		final StringBuilder sbRemoveStyle = new StringBuilder();

		for (FontStyle style : EnumSet.allOf(FontStyle.class)) {
			if (style == FontStyle.PLAIN) {
				continue;
			}
			if (sbAddStyle.length() > 0) {
				sbAddStyle.append('|');
				sbRemoveStyle.append('|');
			}
			sbAddStyle.append(style.getActivationPattern());
			sbRemoveStyle.append(style.getDeactivationPattern());
		}

		addStyle = Pattern.compile(sbAddStyle.toString(), Pattern.CASE_INSENSITIVE);
		removeStyle = Pattern.compile(sbRemoveStyle.toString(), Pattern.CASE_INSENSITIVE);
	}

	private Pattern htmlTag = Pattern.compile(Splitter.htmlTag, Pattern.CASE_INSENSITIVE);

	HtmlCommand getHtmlCommand(String s) {
		if (htmlTag.matcher(s).matches() == false) {
			return new Text(s);
		}
		if (s.matches(Splitter.imgPattern)) {
			return Img.getInstance(s);
		}
		
		if (s.matches(Splitter.imgPattern2)) {
			return Img.getInstance2(s);
		}
		
		if (addStyle.matcher(s).matches()) {
			//return new AddStyle(FontStyle.getStyle(s));
			return new AddStyle(s);
		}
		if (removeStyle.matcher(s).matches()) {
			return new RemoveStyle(FontStyle.getStyle(s));
		}

		if (s.matches(Splitter.fontPattern)) {
			return new ColorAndSizeChange(s);
		}

		if (s.matches(Splitter.fontColorPattern2)) {
			return new ColorChange(s);
		}

		if (s.matches(Splitter.fontSizePattern2)) {
			return new SizeChange(s);
		}

		if (s.matches(Splitter.endFontPattern)) {
			return new ResetFont();
		}

		return null;
	}

}
