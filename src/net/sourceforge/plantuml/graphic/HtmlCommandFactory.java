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
package net.sourceforge.plantuml.graphic;

import java.util.EnumSet;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;

class HtmlCommandFactory {

	static final Pattern2 addStyle;
	static final Pattern2 removeStyle;

	static {
		final StringBuilder sbAddStyle = new StringBuilder();
		final StringBuilder sbRemoveStyle = new StringBuilder();

		for (FontStyle style : EnumSet.allOf(FontStyle.class)) {
			if (sbAddStyle.length() > 0) {
				sbAddStyle.append('|');
				sbRemoveStyle.append('|');
			}
			sbAddStyle.append(style.getActivationPattern());
			sbRemoveStyle.append(style.getDeactivationPattern());
		}

		addStyle = MyPattern.cmpile(sbAddStyle.toString(), Pattern.CASE_INSENSITIVE);
		removeStyle = MyPattern.cmpile(sbRemoveStyle.toString(), Pattern.CASE_INSENSITIVE);
	}

	private Pattern2 htmlTag = MyPattern.cmpile(Splitter.htmlTag, Pattern.CASE_INSENSITIVE);

	HtmlCommand getHtmlCommand(String s) {
		if (htmlTag.matcher(s).matches() == false) {
			return new Text(s);
		}
		if (MyPattern.mtches(s, Splitter.imgPattern)) {
			return Img.getInstance(s, true);
		}

		if (MyPattern.mtches(s, Splitter.imgPatternNoSrcColon)) {
			return Img.getInstance(s, false);
		}

		if (addStyle.matcher(s).matches()) {
			return new AddStyle(s);
		}
		if (removeStyle.matcher(s).matches()) {
			return new RemoveStyle(FontStyle.getStyle(s));
		}

		if (MyPattern.mtches(s, Splitter.fontPattern)) {
			return new ColorAndSizeChange(s);
		}

		if (MyPattern.mtches(s, Splitter.fontColorPattern2)) {
			return new ColorChange(s);
		}

		if (MyPattern.mtches(s, Splitter.fontSizePattern2)) {
			return new SizeChange(s);
		}

		if (MyPattern.mtches(s, Splitter.fontSup)) {
			return new ExposantChange(FontPosition.EXPOSANT);
		}

		if (MyPattern.mtches(s, Splitter.fontSub)) {
			return new ExposantChange(FontPosition.INDICE);
		}

		if (MyPattern.mtches(s, Splitter.endFontPattern)) {
			return new ResetFont();
		}

		if (MyPattern.mtches(s, Splitter.endSupSub)) {
			return new ExposantChange(FontPosition.NORMAL);
		}

		if (MyPattern.mtches(s, Splitter.fontFamilyPattern)) {
			return new FontFamilyChange(s);
		}

		if (MyPattern.mtches(s, Splitter.spritePatternForMatch)) {
			return new SpriteCommand(s);
		}

		if (MyPattern.mtches(s, Splitter.linkPattern)) {
			final UrlBuilder urlBuilder = new UrlBuilder(null, ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(s);
			url.setMember(true);
			return new TextLink(url);
		}

		if (MyPattern.mtches(s, Splitter.svgAttributePattern)) {
			return new SvgAttributesChange(s);
		}

		return null;
	}

}
