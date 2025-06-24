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
package net.sourceforge.plantuml.klimt.font;

import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;

import net.sourceforge.plantuml.klimt.SvgAttributes;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.skin.SkinParamUtils;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class FontConfiguration {

	public static FontConfiguration create(UFont font, HColor color, HColor hyperlinkColor,
			UStroke hyperlinkUnderlineStroke, int tabSize) {
		return new FontConfiguration(getStyles(font), font, color, font, color, null, FontPosition.NORMAL,
				SvgAttributes.empty(), hyperlinkColor, hyperlinkUnderlineStroke, tabSize);
	}

	public static FontConfiguration blackBlueTrue(UFont font) {
		return create(font, HColors.BLACK.withDark(HColors.WHITE), HColors.BLUE, UStroke.simple(), 8);
	}

	private static EnumSet<FontStyle> getStyles(UFont font) {
		final boolean bold = font.isBold();
		final boolean italic = font.isItalic();
		if (bold && italic)
			return EnumSet.of(FontStyle.ITALIC, FontStyle.BOLD);

		if (bold)
			return EnumSet.of(FontStyle.BOLD);

		if (italic)
			return EnumSet.of(FontStyle.ITALIC);

		return EnumSet.noneOf(FontStyle.class);
	}

	private FontConfiguration(EnumSet<FontStyle> styles, UFont motherFont, HColor motherColor, UFont currentFont,
			HColor currentColor, HColor extendedColor, FontPosition fontPosition, SvgAttributes svgAttributes,
			HColor hyperlinkColor, UStroke hyperlinkUnderlineStroke, int tabSize) {
		this.styles = styles;
		this.currentFont = currentFont;
		this.motherFont = motherFont;
		this.currentColor = currentColor;
		this.motherColor = motherColor;
		this.extendedColor = extendedColor;
		this.fontPosition = fontPosition;
		this.svgAttributes = svgAttributes;
		this.hyperlinkColor = hyperlinkColor;
		this.hyperlinkUnderlineStroke = hyperlinkUnderlineStroke;
		this.tabSize = tabSize;
	}

	public UFont getFont() {
		UFont result = currentFont;
		for (FontStyle style : styles)
			result = style.mutateFont(result);

		return fontPosition.mute(result);
	}

	private final EnumSet<FontStyle> styles;
	private final UFont currentFont;
	private final UFont motherFont;
	private final HColor motherColor;
	private final HColor currentColor;
	private final HColor extendedColor;
	private final FontPosition fontPosition;
	private final SvgAttributes svgAttributes;

	private final UStroke hyperlinkUnderlineStroke;
	private final HColor hyperlinkColor;

	private final int tabSize;

	// ::comment when __HAXE__
	public String toStringDebug() {
		return getFont().toString() + " " + styles.toString();
		// return getFont().toStringDebug() + " " + styles.toString();
	}

	@Override
	public int hashCode() {
		return currentFont.hashCode()//
				+ styles.hashCode() //
				+ currentColor.hashCode()//
				+ hashCode(extendedColor)//
				+ hyperlinkColor.hashCode()//
				+ hashCode(hyperlinkUnderlineStroke)//
				+ fontPosition.hashCode() //
				+ tabSize;
	}

	private int hashCode(Object obj) {
		if (obj == null)
			return 43;
		return obj.hashCode();
	}

	private boolean same(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null)
			return true;
		if (obj1 != null && obj2 != null)
			return obj1.equals(obj2);
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		final FontConfiguration other = (FontConfiguration) obj;
		return currentFont.equals(other.currentFont) && styles.equals(other.styles)
				&& currentColor.equals(other.currentColor) && same(extendedColor, other.extendedColor)
				&& hyperlinkColor.equals(other.hyperlinkColor)
				&& same(hyperlinkUnderlineStroke, other.hyperlinkUnderlineStroke)
				&& fontPosition.equals(other.fontPosition) && tabSize == other.tabSize;
	}

	public FontConfiguration mute(Colors colors) {
		final HColor color = Objects.requireNonNull(colors).getColor(ColorType.TEXT);
		if (color == null)
			return this;

		return changeColor(color);
	}

	public static FontConfiguration create(ISkinParam skinParam, FontParam fontParam, Stereotype stereo) {
		return create(SkinParamUtils.getFont(skinParam, fontParam, stereo),
				SkinParamUtils.getFontColor(skinParam, fontParam, stereo), skinParam.getHyperlinkColor(),
				skinParam.useUnderlineForHyperlink(), skinParam.getTabSize());
	}

	public static FontConfiguration create(ISkinParam skinParam, Style style) {
		return create(skinParam, style, null);
	}

	public static FontConfiguration create(ISkinParam skinParam, Style style, Colors colors) {
		final HColor hyperlinkColor = style.value(PName.HyperLinkColor).asColor(skinParam.getIHtmlColorSet());
		final UStroke hyperlinkUnderlineStroke = skinParam.useUnderlineForHyperlink();
		HColor color = colors == null ? null : colors.getColor(ColorType.TEXT);
		if (color == null)
			color = style.value(PName.FontColor).asColor(skinParam.getIHtmlColorSet());
		return create(style.getUFont(), color, hyperlinkColor, hyperlinkUnderlineStroke, skinParam.getTabSize());
	}

	@Override
	public String toString() {
		return styles.toString() + " " + currentColor;
	}

	// ::done

	public static FontConfiguration create(UFont font, HColor color, HColor hyperlinkColor,
			UStroke hyperlinkUnderlineStroke) {
		return create(font, color, hyperlinkColor, hyperlinkUnderlineStroke, 8);
	}

	// ---

	public FontConfiguration forceFont(UFont newFont, HColor htmlColorForStereotype) {
		if (newFont == null)
			return add(FontStyle.ITALIC);

		FontConfiguration result = new FontConfiguration(styles, newFont, motherColor, newFont, currentColor,
				extendedColor, fontPosition, svgAttributes, hyperlinkColor, hyperlinkUnderlineStroke, tabSize);
		if (htmlColorForStereotype != null)
			result = result.changeColor(htmlColorForStereotype);

		return result;
	}

	public FontConfiguration changeAttributes(SvgAttributes toBeAdded) {
		return new FontConfiguration(styles, motherFont, motherColor, currentFont, currentColor, extendedColor,
				fontPosition, svgAttributes.add(toBeAdded), hyperlinkColor, hyperlinkUnderlineStroke, tabSize);
	}

	private FontConfiguration withHyperlink() {
		return new FontConfiguration(styles, motherFont, motherColor, currentFont, hyperlinkColor, extendedColor,
				fontPosition, svgAttributes, hyperlinkColor, hyperlinkUnderlineStroke, tabSize);
	}

	public FontConfiguration changeColor(HColor newHtmlColor) {
		return new FontConfiguration(styles, motherFont, motherColor, currentFont, newHtmlColor, extendedColor,
				fontPosition, svgAttributes, hyperlinkColor, hyperlinkUnderlineStroke, tabSize);
	}

	public FontConfiguration changeExtendedColor(HColor newExtendedColor) {
		return new FontConfiguration(styles, motherFont, motherColor, currentFont, currentColor, newExtendedColor,
				fontPosition, svgAttributes, hyperlinkColor, hyperlinkUnderlineStroke, tabSize);
	}

	public FontConfiguration changeSize(float size) {
		return new FontConfiguration(styles, motherFont, motherColor, currentFont.withSize(size), currentColor,
				extendedColor, fontPosition, svgAttributes, hyperlinkColor, hyperlinkUnderlineStroke, tabSize);
	}

	public FontConfiguration bigger(double delta) {
		return changeSize((float) (currentFont.getSize() + delta));
	}

	public FontConfiguration changeFontPosition(FontPosition fontPosition) {
		return new FontConfiguration(styles, motherFont, motherColor, currentFont, currentColor, extendedColor,
				fontPosition, svgAttributes, hyperlinkColor, hyperlinkUnderlineStroke, tabSize);
	}

	public FontConfiguration changeFamily(String family) {
		return new FontConfiguration(styles, motherFont, motherColor,
				UFont.build(family, currentFont.getStyle(), currentFont.getSize()), currentColor, extendedColor,
				fontPosition, svgAttributes, hyperlinkColor, hyperlinkUnderlineStroke, tabSize);
	}

	public FontConfiguration resetFont() {
		return new FontConfiguration(styles, motherFont, motherColor, motherFont, motherColor, null,
				FontPosition.NORMAL, SvgAttributes.empty(), hyperlinkColor, hyperlinkUnderlineStroke, tabSize);
	}

	public FontConfiguration add(FontStyle style) {
		final EnumSet<FontStyle> r = styles.clone();
		if (style == FontStyle.PLAIN)
			r.clear();

		r.add(style);
		return new FontConfiguration(r, motherFont, motherColor, currentFont, currentColor, extendedColor, fontPosition,
				svgAttributes, hyperlinkColor, hyperlinkUnderlineStroke, tabSize);
	}

	public FontConfiguration italic() {
		return add(FontStyle.ITALIC);
	}

	public FontConfiguration bold() {
		return add(FontStyle.BOLD);
	}

	public FontConfiguration unbold() {
		return remove(FontStyle.BOLD);
	}

	public FontConfiguration unitalic() {
		return remove(FontStyle.ITALIC);
	}

	public FontConfiguration underline() {
		return add(FontStyle.UNDERLINE);
	}

	public FontConfiguration wave(HColor color) {
		return add(FontStyle.WAVE).changeExtendedColor(color);
	}

	public FontConfiguration hyperlink() {
		if (hyperlinkUnderlineStroke != null)
			return add(FontStyle.UNDERLINE).withHyperlink();

		return withHyperlink();
	}

	public FontConfiguration remove(FontStyle style) {
		final EnumSet<FontStyle> r = styles.clone();
		r.remove(style);
		return new FontConfiguration(r, motherFont, motherColor, currentFont, currentColor, extendedColor, fontPosition,
				svgAttributes, hyperlinkColor, hyperlinkUnderlineStroke, tabSize);
	}

	public HColor getColor() {
		return currentColor;
	}

	public HColor getExtendedColor() {
		return extendedColor;
	}

	public boolean containsStyle(FontStyle style) {
		return styles.contains(style);
	}

	public int getSpace() {
		return fontPosition.getSpace();
	}

	public Map<String, String> getAttributes() {
		return svgAttributes.attributes();
	}

	public double getSize2D() {
		return currentFont.getSize2D();
	}

	public int getTabSize() {
		return tabSize;
	}

	public UStroke getUnderlineStroke() {
		return hyperlinkUnderlineStroke;
		// return UStroke.simple();
		// return new UStroke(3, 5, 2);
	}

}
