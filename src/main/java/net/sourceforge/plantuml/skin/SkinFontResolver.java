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
package net.sourceforge.plantuml.skin;

import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFace;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.text.Guillemet;

final class SkinFontResolver {

	private static final Pattern DIGITS = Pattern.compile("\\d+");
	private final ISkinParam skinParam;

	SkinFontResolver(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	private static boolean isDigits(String value) {
		return value != null && DIGITS.matcher(value).matches();
	}

	int getFontSize(Stereotype stereotype, FontParam... param) {
		if (stereotype != null) {
			final String value2 = getFirstValueNonNullWithSuffix(
					"fontsize" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR), param);
			if (isDigits(value2))
				return Integer.parseInt(value2);

		}
		String value = getFirstValueNonNullWithSuffix("fontsize", param);
		if (isDigits(value) == false)
			value = skinParam.getValue("defaultfontsize");

		if (isDigits(value) == false)
			return param[0].getDefaultSize(skinParam);

		return Integer.parseInt(value);
	}

	private String getFontFamily(Stereotype stereotype, FontParam... param) {
		if (stereotype != null) {
			final String value2 = getFirstValueNonNullWithSuffix(
					"fontname" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR), param);
			if (value2 != null)
				return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(value2);

		}
		// Times, Helvetica, Courier or Symbol
		String value = getFirstValueNonNullWithSuffix("fontname", param);
		if (value != null)
			return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(value);

		if (param[0] != FontParam.CIRCLED_CHARACTER) {
			value = skinParam.getValue("defaultfontname");
			if (value != null)
				return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(value);

		}
		return param[0].getDefaultFamily();
	}

	public HColor getFontHtmlColor(Stereotype stereotype, FontParam... param) {
		String value = null;
		if (stereotype != null) {
			value = getFirstValueNonNullWithSuffix("fontcolor" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR),
					param);
		}

		if (value == null)
			value = getFirstValueNonNullWithSuffix("fontcolor", param);

		if (value == null)
			value = skinParam.getValue("defaultfontcolor");

		if (value == null)
			value = param[0].getDefaultColor();

		if (value == null)
			return null;

		return skinParam.getIHtmlColorSet().getColorOrWhite(value);
	}

	private String getFirstValueNonNullWithSuffix(String suffix, FontParam... param) {
		for (FontParam p : param) {
			final String v = skinParam.getValue(p.name() + suffix);
			if (v != null)
				return v;

		}
		return null;
	}

	private UFontFace getFontFace(Stereotype stereotype, boolean inPackageTitle, FontParam... param) {
		String value = null;
		if (stereotype != null) {
			value = getFirstValueNonNullWithSuffix("fontstyle" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR),
					param);
		}
		if (value == null)
			value = getFirstValueNonNullWithSuffix("fontstyle", param);

		if (value == null)
			value = skinParam.getValue("defaultfontstyle");

		if (value == null)
			return param[0].getDefaultFontFace(skinParam, inPackageTitle);

		final String lower = StringUtils.goLowerCase(value);
		final boolean bold = lower.contains("bold");
		final boolean italic = lower.contains("italic");
		if (bold && italic)
			return UFontFace.boldItalic();
		if (bold)
			return UFontFace.bold();
		if (italic)
			return UFontFace.italic();
		return UFontFace.normal();
	}

	public UFont getFont(Stereotype stereotype, boolean inPackageTitle, FontParam... fontParam) {
		final String fontFamily = getFontFamily(stereotype, fontParam);
		final UFontFace face = getFontFace(stereotype, inPackageTitle, fontParam);
		final int fontSize = getFontSize(stereotype, fontParam);
		return UFontFactory.build(fontFamily, face, fontSize);
	}

}
