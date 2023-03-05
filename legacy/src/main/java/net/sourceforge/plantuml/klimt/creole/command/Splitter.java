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
package net.sourceforge.plantuml.klimt.creole.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.emoji.Emoji;
import net.sourceforge.plantuml.klimt.font.FontStyle;
import net.sourceforge.plantuml.klimt.sprite.SpriteUtils;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.MyPattern;
import net.sourceforge.plantuml.regex.Pattern2;

public class Splitter {

	static final String endFontPattern = "\\</font\\>|\\</color\\>|\\</size\\>|\\</text\\>";
	static final String endSupSub = "\\</sup\\>|\\</sub\\>";
	public static final String fontPattern = "\\<font(\\s+size[%s]*=[%s]*[%g]?\\d+[%g]?|[%s]+color[%s]*=\\s*[%g]?(#[0-9a-fA-F]{6}|\\w+)[%g]?)+[%s]*\\>";
	public static final String fontColorPattern2 = "\\<color[\\s:]+(#[0-9a-fA-F]{6}|#?\\w+)[%s]*\\>";
	public static final String fontSizePattern2 = "\\<size[\\s:]+(\\d+)[%s]*\\>";
	static final String fontSup = "\\<sup\\>";
	static final String fontSub = "\\<sub\\>";
	public static final String qrcodePattern = "\\<qrcode[\\s:]+([^>{}]+)" + "(\\{scale=(?:[0-9.]+)\\})?" + "\\>";
	static final String imgPattern = "\\<img\\s+(src[%s]*=[%s]*[%q%g]?[^\\s%g>]+[%q%g]?[%s]*|vspace\\s*=\\s*[%q%g]?\\d+[%q%g]?\\s*|valign[%s]*=[%s]*[%q%g]?(top|middle|bottom)[%q%g]?[%s]*)+\\>";
	public static final String imgPatternNoSrcColon = "\\<img[\\s:]+([^>{}]+)" + "(\\{scale=(?:[0-9.]+)\\})?" + "\\>";
	public static final String fontFamilyPattern = "\\<font[\\s:]+([^>]+)/?\\>";
	public static final String svgAttributePattern = "\\<text[\\s:]+([^>]+)/?\\>";

	private static final String scale2 = "(" + "(?:\\{scale=|\\*)[0-9.]+\\}?" + ")?";
	private static final String scale = "(" + //
			"[\\{,]?" + //
			"(?:(?:scale=|\\*)[0-9.]+)?" + //
			"(?:,color[= :](?:#[0-9a-fA-F]{6}|\\w+))?" + //
			"\\}?" + //
			")?";

	public static final String emojiPattern = Emoji.pattern();
	public static final String openiconPattern = "\\<&([-\\w]+)" + scale + "\\>";
	public static final String spritePattern2 = "\\<\\$(" + SpriteUtils.SPRITE_NAME + ")" + scale + "\\>";

	public static final String spritePatternForMatch = spritePattern2;
	// "\\<\\$" + SpriteUtils.SPRITE_NAME + "(?:\\{scale=(?:[0-9.]+)\\})?" + "\\>";

	static final String htmlTag;

	static final String linkPattern = "\\[\\[([^\\[\\]]+)\\]\\]";
	public static final String mathPattern = "\\<math\\>(.+?)\\</math\\>";
	public static final String latexPattern = "\\<latex\\>(.+?)\\</latex\\>";

	private static final Pattern2 tagOrText;

	static {
		final StringBuilder sb = new StringBuilder();

		for (FontStyle style : EnumSet.allOf(FontStyle.class)) {
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
		sb.append(fontSup);
		sb.append('|');
		sb.append(fontSub);
		sb.append('|');
		sb.append(endFontPattern);
		sb.append('|');
		sb.append(endSupSub);
		sb.append('|');
		sb.append(qrcodePattern);
		sb.append('|');
		sb.append(imgPattern);
		sb.append('|');
		sb.append(imgPatternNoSrcColon);
		sb.append('|');
		sb.append(fontFamilyPattern);
		sb.append('|');
		// sb.append(spritePattern);
		// sb.append('|');
		sb.append(linkPattern);
		sb.append('|');
		sb.append(svgAttributePattern);

		htmlTag = sb.toString();
		tagOrText = MyPattern.cmpile(htmlTag + "|.+?(?=" + htmlTag + ")|.+$");
	}

	private final List<String> splitted = new ArrayList<>();

	public Splitter(String s) {
		final Matcher2 matcher = tagOrText.matcher(s);
		while (matcher.find()) {
			String part = matcher.group(0);
			part = StringUtils.showComparatorCharacters(part);
			splitted.add(part);
		}
	}

	List<String> getSplittedInternal() {
		return splitted;
	}

	public static String purgeAllTag(String s) {
		return s.replaceAll(htmlTag, "");
	}

	public List<HtmlCommand> getHtmlCommands(boolean newLineAlone) {
		final HtmlCommandFactory factory = new HtmlCommandFactory();
		final List<HtmlCommand> result = new ArrayList<>();
		for (String s : getSplittedInternal()) {
			final HtmlCommand cmd = factory.getHtmlCommand(s);
			if (newLineAlone && cmd instanceof PlainText) {
				result.addAll(splitText((PlainText) cmd));
			} else {
				result.add(cmd);
			}
		}
		return Collections.unmodifiableList(result);
	}

	private Collection<PlainText> splitText(PlainText cmd) {
		String s = cmd.getText();
		final Collection<PlainText> result = new ArrayList<>();
		while (true) {
			final int x = s.indexOf(PlainText.TEXT_BS_BS_N.getText());
			if (x == -1) {
				result.add(new PlainText(s));
				return result;
			}
			if (x > 0) {
				result.add(new PlainText(s.substring(0, x)));
			}
			result.add(PlainText.TEXT_BS_BS_N);
			s = s.substring(x + 2);
		}
	}
}
