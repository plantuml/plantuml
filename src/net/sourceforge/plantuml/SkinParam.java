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
package net.sourceforge.plantuml;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.creole.command.CommandCreoleMonospaced;
import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.DotSplines;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.SkinParameter;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.skin.ActorStyle;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.Padder;
import net.sourceforge.plantuml.sprite.Sprite;
import net.sourceforge.plantuml.sprite.SpriteImage;
import net.sourceforge.plantuml.style.FromSkinparamToStyle;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleLoader;
import net.sourceforge.plantuml.svek.ConditionEndStyle;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperLightnessInverse;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperMonochrome;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperReverse;
import net.sourceforge.plantuml.ugraphic.color.ColorOrder;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class SkinParam implements ISkinParam {

	// private String skin = "debug.skin";

	private String skin = "plantuml.skin";

	private SkinParam(UmlDiagramType type) {
		USE_STYLE2.set(false);
		this.type = type;
		if (type == UmlDiagramType.MINDMAP) {
			USE_STYLE2.set(true);
		}
		if (type == UmlDiagramType.WBS) {
			USE_STYLE2.set(true);
		}
		if (type == UmlDiagramType.SEQUENCE) {
			// skin = "debug.skin";
			// USE_STYLE2.set(true);
		}
		// if (type == UmlDiagramType.ACTIVITY) {
		// // skin = "debug.skin";
		// USE_STYLE2.set(true);
		// }
	}

	private StyleBuilder styleBuilder;

	public StyleBuilder getCurrentStyleBuilder() {
		if (styleBuilder == null && SkinParam.USE_STYLES()) {
			try {
				this.styleBuilder = getCurrentStyleBuilderInternal();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return styleBuilder;
	}

	public void muteStyle(Style modifiedStyle) {
		if (SkinParam.USE_STYLES()) {
			styleBuilder = getCurrentStyleBuilder().muteStyle(modifiedStyle);
		}
	}

	public String getDefaultSkin() {
		return skin;
	}

	public void setDefaultSkin(String newSkin) {
		this.skin = newSkin;
	}

	public StyleBuilder getCurrentStyleBuilderInternal() throws IOException {
		final StyleLoader tmp = new StyleLoader(this);
		StyleBuilder result = tmp.loadSkin(this.getDefaultSkin());
		if (result == null) {
			result = tmp.loadSkin("plantuml.skin");
		}

		return result;
	}

	private static ThreadLocal<Boolean> USE_STYLE2 = new ThreadLocal<Boolean>();

	static public boolean USE_STYLES() {
		final Boolean result = USE_STYLE2.get();
		if (result == null) {
			return false;
		}
		return result;
	}

	static public void setBetaStyle(boolean betastyle) {
		USE_STYLE2.set(betastyle);
	}

	public static int zeroMargin(int defaultValue) {
		return defaultValue;
	}

	private static final String stereoPatternString = "\\<\\<(.*?)\\>\\>";
	private static final Pattern2 stereoPattern = MyPattern.cmpile(stereoPatternString);

	private final Map<String, String> params = new HashMap<String, String>();
	private Rankdir rankdir = Rankdir.TOP_TO_BOTTOM;
	private final UmlDiagramType type;
	private boolean useVizJs;

	public void copyAllFrom(ISkinSimple other) {
		this.params.putAll(other.values());
	}

	public Map<String, String> values() {
		return Collections.unmodifiableMap(params);
	}

	public void setParam(String key, String value) {
		for (String key2 : cleanForKey(key)) {
			params.put(key2, StringUtils.trin(value));
			if (key2.startsWith("usebetastyle")) {
				final boolean betastyle = "true".equalsIgnoreCase(value);
				setBetaStyle(betastyle);
			}
			if (USE_STYLES()) {
				final FromSkinparamToStyle convertor = new FromSkinparamToStyle(key2, value, getCurrentStyleBuilder());
				for (Style style : convertor.getStyles()) {
					muteStyle(style);
				}
			}
		}
		if ("style".equalsIgnoreCase(key) && "strictuml".equalsIgnoreCase(value)) {
			if (USE_STYLES()) {
				final InputStream internalIs = StyleLoader.class.getResourceAsStream("/skin/strictuml.skin");
				final StyleBuilder styleBuilder = this.getCurrentStyleBuilder();
				try {
					final BlocLines lines = BlocLines.load(internalIs, null);
					for (Style modifiedStyle : StyleLoader.getDeclaredStyles(lines, styleBuilder)) {
						this.muteStyle(modifiedStyle);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static SkinParam create(UmlDiagramType type) {
		return new SkinParam(type);
	}

	public static SkinParam noShadowing(UmlDiagramType type) {
		final SkinParam result = new SkinParam(type);
		result.setParam("shadowing", "false");
		return result;
	}

	private final Map<String, List<String>> cacheCleanForKey = new HashMap<String, List<String>>();

	List<String> cleanForKey(String key) {
		List<String> result = cacheCleanForKey.get(key);
		if (result == null) {
			result = cleanForKeySlow(key);
			cacheCleanForKey.put(key, result);
		}
		return result;
	}

	List<String> cleanForKeySlow(String key) {
		key = StringUtils.trin(StringUtils.goLowerCase(key));
		key = key.replaceAll("_|\\.|\\s", "");
		// key = replaceSmart(key, "partition", "package");
		key = replaceSmart(key, "sequenceparticipant", "participant");
		key = replaceSmart(key, "sequenceactor", "actor");
		key = key.replaceAll("activityarrow", "arrow");
		key = key.replaceAll("objectarrow", "arrow");
		key = key.replaceAll("classarrow", "arrow");
		key = key.replaceAll("componentarrow", "arrow");
		key = key.replaceAll("statearrow", "arrow");
		key = key.replaceAll("usecasearrow", "arrow");
		key = key.replaceAll("sequencearrow", "arrow");
		key = key.replaceAll("align$", "alignment");
		final Matcher2 mm = stereoPattern.matcher(key);
		final List<String> result = new ArrayList<String>();
		while (mm.find()) {
			final String s = mm.group(1);
			result.add(key.replaceAll(stereoPatternString, "") + "<<" + s + ">>");
		}
		if (result.size() == 0) {
			result.add(key);
		}
		return Collections.unmodifiableList(result);
	}

	private static String replaceSmart(String s, String src, String target) {
		if (s.contains(src) == false) {
			return s;
		}
		return s.replaceAll(src, target);
	}

	public HColor getHyperlinkColor() {
		final HColor result = getHtmlColor(ColorParam.hyperlink, null, false);
		if (result == null) {
			return HColorUtils.BLUE;
		}
		return result;
	}

	public HColor getBackgroundColor(boolean replaceTransparentByWhite) {
		final HColor result = getHtmlColor(ColorParam.background, null, false);
		if (result == null) {
			return HColorUtils.WHITE;
		}
		if (replaceTransparentByWhite && HColorUtils.transparent().equals(result)) {
			return HColorUtils.WHITE;
		}
		return result;
	}

	public String getValue(String key) {
		for (String key2 : cleanForKey(key)) {
			final String result = params.get(key2);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	static String humanName(String key) {
		final StringBuilder sb = new StringBuilder();
		boolean upper = true;
		for (int i = 0; i < key.length(); i++) {
			final char c = key.charAt(i);
			if (c == '_') {
				upper = true;
			} else {
				sb.append(upper ? StringUtils.goUpperCase(c) : StringUtils.goLowerCase(c));
				upper = false;
			}
		}
		return sb.toString();
	}

	public HColor getHtmlColor(ColorParam param, Stereotype stereotype, boolean clickable) {
		if (stereotype != null) {
			checkStereotype(stereotype);
			for (String s : stereotype.getMultipleLabels()) {
				final String value2 = getValue(param.name() + "color" + "<<" + s + ">>");
				if (value2 != null && getIHtmlColorSet().getColorIfValid(value2) != null) {
					return getIHtmlColorSet().getColorIfValid(value2);
				}
			}
		}
		final String value = getValue(getParamName(param, clickable));
		if (value == null) {
			return null;
		}
		if ((param == ColorParam.background || param == ColorParam.arrowHead)
				&& (value.equalsIgnoreCase("transparent") || value.equalsIgnoreCase("none"))) {
			return HColorUtils.transparent();
		}
		if (param == ColorParam.background) {
			return getIHtmlColorSet().getColorIfValid(value);
		}
		assert param != ColorParam.background;
//		final boolean acceptTransparent = param == ColorParam.background
//				|| param == ColorParam.sequenceGroupBodyBackground || param == ColorParam.sequenceBoxBackground;
		return getIHtmlColorSet().getColorIfValid(value, getBackgroundColor(false));
	}

	public char getCircledCharacter(Stereotype stereotype) {
		if (stereotype == null) {
			throw new IllegalArgumentException();
		}
		final String value2 = getValue("spotchar" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));
		if (value2 != null && value2.length() > 0) {
			return value2.charAt(0);
		}
		return 0;
	}

	public Colors getColors(ColorParam param, Stereotype stereotype) {
		if (stereotype != null) {
			checkStereotype(stereotype);
			final String value2 = getValue(param.name() + "color" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));
			if (value2 != null && getIHtmlColorSet().getColorIfValid(value2) != null) {
				return new Colors(value2, getIHtmlColorSet(), param.getColorType());
			}
		}
		final String value = getValue(getParamName(param, false));
		if (value == null) {
			return Colors.empty();
		}
		return new Colors(value, getIHtmlColorSet(), param.getColorType());
	}

	private String getParamName(ColorParam param, boolean clickable) {
		String n = param.name();
		if (clickable && n.endsWith("Background")) {
			n = n.replaceAll("Background", "ClickableBackground");
		} else if (clickable && n.endsWith("Border")) {
			n = n.replaceAll("Border", "ClickableBorder");
		}
		return n + "color";
	}

	private void checkStereotype(Stereotype stereotype) {
		// if (stereotype.startsWith("<<") == false || stereotype.endsWith(">>") ==
		// false) {
		// throw new IllegalArgumentException();
		// }
	}

	private int getFontSize(Stereotype stereotype, FontParam... param) {
		if (stereotype != null) {
			checkStereotype(stereotype);
			final String value2 = getFirstValueNonNullWithSuffix(
					"fontsize" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR), param);
			if (value2 != null && value2.matches("\\d+")) {
				return Integer.parseInt(value2);
			}
		}
		String value = getFirstValueNonNullWithSuffix("fontsize", param);
		if (value == null || value.matches("\\d+") == false) {
			value = getValue("defaultfontsize");
		}
		if (value == null || value.matches("\\d+") == false) {
			return param[0].getDefaultSize(this);
		}
		return Integer.parseInt(value);
	}

	private String getFontFamily(Stereotype stereotype, FontParam... param) {
		if (stereotype != null) {
			checkStereotype(stereotype);
			final String value2 = getFirstValueNonNullWithSuffix(
					"fontname" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR), param);
			if (value2 != null) {
				return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(value2);
			}
		}
		// Times, Helvetica, Courier or Symbol
		String value = getFirstValueNonNullWithSuffix("fontname", param);
		if (value != null) {
			return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(value);
		}
		if (param[0] != FontParam.CIRCLED_CHARACTER) {
			value = getValue("defaultfontname");
			if (value != null) {
				return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(value);
			}
		}
		return param[0].getDefaultFamily();
	}

	public HColor getFontHtmlColor(Stereotype stereotype, FontParam... param) {
		String value = null;
		if (stereotype != null) {
			checkStereotype(stereotype);
			value = getFirstValueNonNullWithSuffix("fontcolor" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR),
					param);
		}
		if (value == null || getIHtmlColorSet().getColorIfValid(value) == null) {
			value = getFirstValueNonNullWithSuffix("fontcolor", param);
		}
		if (value == null || getIHtmlColorSet().getColorIfValid(value) == null) {
			value = getValue("defaultfontcolor");
		}
		if (value == null || getIHtmlColorSet().getColorIfValid(value) == null) {
			value = param[0].getDefaultColor();
		}
		return getIHtmlColorSet().getColorIfValid(value);
	}

	private String getFirstValueNonNullWithSuffix(String suffix, FontParam... param) {
		for (FontParam p : param) {
			final String v = getValue(p.name() + suffix);
			if (v != null) {
				return v;
			}
		}
		return null;
	}

	private int getFontStyle(Stereotype stereotype, boolean inPackageTitle, FontParam... param) {
		String value = null;
		if (stereotype != null) {
			checkStereotype(stereotype);
			value = getFirstValueNonNullWithSuffix("fontstyle" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR),
					param);
		}
		if (value == null) {
			value = getFirstValueNonNullWithSuffix("fontstyle", param);
		}
		if (value == null) {
			value = getValue("defaultfontstyle");
		}
		if (value == null) {
			return param[0].getDefaultFontStyle(this, inPackageTitle);
		}
		int result = Font.PLAIN;
		if (StringUtils.goLowerCase(value).contains("bold")) {
			result = result | Font.BOLD;
		}
		if (StringUtils.goLowerCase(value).contains("italic")) {
			result = result | Font.ITALIC;
		}
		return result;
	}

	public UFont getFont(Stereotype stereotype, boolean inPackageTitle, FontParam... fontParam) {
		if (stereotype != null) {
			checkStereotype(stereotype);
		}
		final String fontFamily = getFontFamily(stereotype, fontParam);
		final int fontStyle = getFontStyle(stereotype, inPackageTitle, fontParam);
		final int fontSize = getFontSize(stereotype, fontParam);
		return new UFont(fontFamily, fontStyle, fontSize);
	}

	public int getCircledCharacterRadius() {
		final String value = getValue("circledcharacterradius");
		if (value != null && value.matches("\\d+")) {
			return Integer.parseInt(value);
		}
		// return 11;
		// Log.println("SIZE1="+getFontSize(FontParam.CIRCLED_CHARACTER));
		// Log.println("SIZE1="+getFontSize(FontParam.CIRCLED_CHARACTER)/3);
		return getFontSize(null, FontParam.CIRCLED_CHARACTER) / 3 + 6;
	}

	public int classAttributeIconSize() {
		final String value = getValue("classAttributeIconSize");
		if (value != null && value.matches("\\d+")) {
			return Integer.parseInt(value);
		}
		return 10;
	}

	public static Collection<String> getPossibleValues() {
		final Set<String> result = new TreeSet<String>();
		result.add("Monochrome");
		// result.add("BackgroundColor");
		result.add("CircledCharacterRadius");
		result.add("ClassAttributeIconSize");
		result.add("DefaultFontName");
		result.add("DefaultFontStyle");
		result.add("DefaultFontSize");
		result.add("DefaultFontColor");
		result.add("MinClassWidth");
		result.add("MinClassWidth");
		result.add("Dpi");
		result.add("DefaultTextAlignment");
		result.add("Shadowing");
		result.add("NoteShadowing");
		result.add("Handwritten");
		result.add("CircledCharacterRadius");
		result.add("ClassAttributeIconSize");
		result.add("Linetype");
		result.add("PackageStyle");
		result.add("ComponentStyle");
		result.add("StereotypePosition");
		result.add("Nodesep");
		result.add("Ranksep");
		result.add("RoundCorner");
		result.add("TitleBorderRoundCorner");
		result.add("MaxMessageSize");
		result.add("Style");
		result.add("SequenceParticipant");
		result.add("ConditionStyle");
		result.add("ConditionEndStyle");
		result.add("SameClassWidth");
		result.add("HyperlinkUnderline");
		result.add("Padding");
		result.add("BoxPadding");
		result.add("ParticipantPadding");
		result.add("Guillemet");
		result.add("SvglinkTarget");
		result.add("DefaultMonospacedFontName");
		result.add("TabSize");
		result.add("MaxAsciiMessageLength");
		result.add("ColorArrowSeparationSpace");
		result.add("ResponseMessageBelowArrow");
		result.add("GenericDisplay");
		result.add("PathHoverColor");
		result.add("SwimlaneWidth");
		result.add("PageBorderColor");
		result.add("PageExternalColor");
		result.add("PageMargin");
		result.add("WrapWidth");
		result.add("SwimlaneWidth");
		result.add("SwimlaneWrapTitleWidth");
		result.add("FixCircleLabelOverlapping");
		result.add("LifelineStrategy");

		for (FontParam p : EnumSet.allOf(FontParam.class)) {
			final String h = humanName(p.name());
			result.add(h + "FontStyle");
			result.add(h + "FontName");
			result.add(h + "FontSize");
			result.add(h + "FontColor");
		}
		for (ColorParam p : EnumSet.allOf(ColorParam.class)) {
			final String h = capitalize(p.name());
			result.add(h + "Color");
		}
		for (LineParam p : EnumSet.allOf(LineParam.class)) {
			final String h = capitalize(p.name());
			result.add(h + "Thickness");
		}
		for (AlignmentParam p : EnumSet.allOf(AlignmentParam.class)) {
			final String h = capitalize(p.name());
			result.add(h);
		}
		return Collections.unmodifiableSet(result);
	}

	private static String capitalize(String name) {
		return StringUtils.goUpperCase(name.substring(0, 1)) + name.substring(1);
	}

	public int getDpi() {
		final String value = getValue("dpi");
		if (value != null && value.matches("\\d+")) {
			return Integer.parseInt(value);
		}
		return 96;
	}

	public DotSplines getDotSplines() {
		final String value = getValue("linetype");
		if ("polyline".equalsIgnoreCase(value)) {
			return DotSplines.POLYLINE;
		}
		if ("ortho".equalsIgnoreCase(value)) {
			return DotSplines.ORTHO;
		}
		return DotSplines.SPLINES;
	}

	public HorizontalAlignment getHorizontalAlignment(AlignmentParam param, ArrowDirection arrowDirection,
			boolean isReverseDefine) {
		final String value;
		switch (param) {
		case sequenceMessageAlignment:
			value = getArg(getValue(AlignmentParam.sequenceMessageAlignment.name()), 0);
			break;
		case sequenceMessageTextAlignment:
			value = getArg(getValue(AlignmentParam.sequenceMessageAlignment.name()), 1);
			break;
		default:
			value = getValue(param.name());
		}
		if ("first".equalsIgnoreCase(value)) {
			if (arrowDirection == ArrowDirection.RIGHT_TO_LEFT_REVERSE) {
				if (isReverseDefine) {
					return HorizontalAlignment.LEFT;
				}
				return HorizontalAlignment.RIGHT;
			} else {
				if (isReverseDefine) {
					return HorizontalAlignment.RIGHT;
				}
				return HorizontalAlignment.LEFT;
			}
		}
		if ("direction".equalsIgnoreCase(value)) {
			if (arrowDirection == ArrowDirection.LEFT_TO_RIGHT_NORMAL) {
				return HorizontalAlignment.LEFT;
			}
			if (arrowDirection == ArrowDirection.RIGHT_TO_LEFT_REVERSE) {
				return HorizontalAlignment.RIGHT;
			}
			if (arrowDirection == ArrowDirection.BOTH_DIRECTION) {
				return HorizontalAlignment.CENTER;
			}
		}
		if ("reversedirection".equalsIgnoreCase(value)) {
			if (arrowDirection == ArrowDirection.LEFT_TO_RIGHT_NORMAL) {
				return HorizontalAlignment.RIGHT;
			}
			if (arrowDirection == ArrowDirection.RIGHT_TO_LEFT_REVERSE) {
				return HorizontalAlignment.LEFT;
			}
			if (arrowDirection == ArrowDirection.BOTH_DIRECTION) {
				return HorizontalAlignment.CENTER;
			}
		}
		final HorizontalAlignment result = HorizontalAlignment.fromString(value);
		if (result == null && param == AlignmentParam.noteTextAlignment) {
			return getDefaultTextAlignment(HorizontalAlignment.LEFT);
		} else if (result == null && param == AlignmentParam.stateMessageAlignment) {
			return getDefaultTextAlignment(HorizontalAlignment.CENTER);
		} else if (result == null) {
			return param.getDefaultValue();
		}
		return result;
	}

	public HorizontalAlignment getDefaultTextAlignment(HorizontalAlignment defaultValue) {
		final String value = getValue("defaulttextalignment");
		final HorizontalAlignment result = HorizontalAlignment.fromString(value);
		if (result == null) {
			return defaultValue;
		}
		return result;
	}

	public HorizontalAlignment getStereotypeAlignment() {
		final String value = getValue("stereotypealignment");
		final HorizontalAlignment result = HorizontalAlignment.fromString(value);
		if (result == null) {
			return HorizontalAlignment.CENTER;
		}
		return result;
	}

	private String getArg(String value, int i) {
		if (value == null) {
			return null;
		}
		final String[] split = value.split(":");
		if (i >= split.length) {
			return split[0];
		}
		return split[i];
	}

	public ColorMapper getColorMapper() {
		final String monochrome = getValue("monochrome");
		if ("true".equals(monochrome)) {
			return new ColorMapperMonochrome(false);
		}
		if ("reverse".equals(monochrome)) {
			return new ColorMapperMonochrome(true);
		}
		final String value = getValue("reversecolor");
		if (value == null) {
			return new ColorMapperIdentity();
		}
		if ("dark".equalsIgnoreCase(value)) {
			return new ColorMapperLightnessInverse();
		}
		final ColorOrder order = ColorOrder.fromString(value);
		if (order == null) {
			return new ColorMapperIdentity();
		}
		return new ColorMapperReverse(order);
	}

	public boolean shadowing(Stereotype stereotype) {
		if (stereotype != null) {
			checkStereotype(stereotype);
			final String value2 = getValue("shadowing" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));
			if (value2 != null) {
				return value2.equalsIgnoreCase("true");
			}
		}
		final String value = getValue("shadowing");
		if ("false".equalsIgnoreCase(value)) {
			return false;
		}
		if ("true".equalsIgnoreCase(value)) {
			return true;
		}
		if (strictUmlStyle()) {
			return false;
		}
		return true;
	}

	public boolean shadowingForNote(Stereotype stereotype) {
		if (stereotype != null) {
			checkStereotype(stereotype);
			final String value2 = getValue("note" + "shadowing" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));
			if (value2 != null) {
				return value2.equalsIgnoreCase("true");
			}
		}
		final String value2 = getValue("note" + "shadowing");
		if (value2 != null) {
			return value2.equalsIgnoreCase("true");
		}
		return shadowing(stereotype);
	}

	public boolean shadowing2(Stereotype stereotype, SkinParameter skinParameter) {
		if (skinParameter == null) {
			throw new IllegalArgumentException();
		}
		final String name = skinParameter.getUpperCaseName();
		if (stereotype != null) {
			checkStereotype(stereotype);
			final String value2 = getValue(name + "shadowing" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));
			if (value2 != null) {
				return value2.equalsIgnoreCase("true");
			}
		}

		final String value = getValue(name + "shadowing");
		if (value == null) {
			return shadowing(stereotype);
		}
		if ("false".equalsIgnoreCase(value)) {
			return false;
		}
		if ("true".equalsIgnoreCase(value)) {
			return true;
		}
		if (strictUmlStyle()) {
			return false;
		}
		return true;
	}

	public PackageStyle getPackageStyle() {
		final String value = getValue("packageStyle");
		final PackageStyle p = PackageStyle.fromString(value);
		if (p == null) {
			return PackageStyle.FOLDER;
		}
		return p;
	}

	private final Map<String, Sprite> sprites = new HashMap<String, Sprite>();

	public Collection<String> getAllSpriteNames() {
		return Collections.unmodifiableCollection(new TreeSet<String>(sprites.keySet()));
	}

	public void addSprite(String name, Sprite sprite) {
		sprites.put(name, sprite);
	}

	public Sprite getSprite(String name) {
		Sprite result = sprites.get(name);
		if (result == null) {
			result = SpriteImage.fromInternal(name);
		}
		return result;
	}

	public boolean useUml2ForComponent() {
		if (strictUmlStyle()) {
			return true;
		}
		final String value = getValue("componentstyle");
		return "uml2".equalsIgnoreCase(value);
	}

	public boolean stereotypePositionTop() {
		final String value = getValue("stereotypePosition");
		if ("bottom".equalsIgnoreCase(value)) {
			return false;
		}
		return true;
	}

	public boolean useSwimlanes(UmlDiagramType type) {
		if (type != UmlDiagramType.ACTIVITY) {
			return false;
		}
		if ("true".equalsIgnoreCase(getValue("swimlane"))) {
			return true;
		}
		if ("true".equalsIgnoreCase(getValue("swimlanes"))) {
			return true;
		}
		return false;
	}

	public double getNodesep() {
		final String value = getValue("nodesep");
		if (value != null && value.matches("\\d+")) {
			return Double.parseDouble(value);
		}
		return 0;
	}

	public double getRanksep() {
		final String value = getValue("ranksep");
		if (value != null && value.matches("\\d+")) {
			return Double.parseDouble(value);
		}
		return 0;
	}

	public double getDiagonalCorner(CornerParam param, Stereotype stereotype) {
		final String key = param.getDiagonalKey();
		Double result = getCornerInternal(key, param, stereotype);
		if (result != null) {
			return result;
		}
		result = getCornerInternal(key, param, null);
		if (result != null) {
			return result;
		}
		if (param == CornerParam.DEFAULT) {
			return 0;
		}
		return getDiagonalCorner(CornerParam.DEFAULT, stereotype);
	}

	public double getRoundCorner(CornerParam param, Stereotype stereotype) {
		final String key = param.getRoundKey();
		Double result = getCornerInternal(key, param, stereotype);
		if (result != null) {
			return result;
		}
		result = getCornerInternal(key, param, null);
		if (result != null) {
			return result;
		}
		if (param == CornerParam.DEFAULT) {
			return 0;
		}
		return getRoundCorner(CornerParam.DEFAULT, stereotype);
	}

	private Double getCornerInternal(String key, CornerParam param, Stereotype stereotype) {
		if (stereotype != null) {
			key += stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR);
		}
		final String value = getValue(key);
		if (value != null && value.matches("\\d+")) {
			return Double.parseDouble(value);
		}
		return null;
	}

	public UStroke getThickness(LineParam param, Stereotype stereotype) {
		LinkStyle style = null;
		if (stereotype != null) {
			checkStereotype(stereotype);

			final String styleValue = getValue(
					param.name() + "style" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));
			if (styleValue != null) {
				style = LinkStyle.fromString2(styleValue);
			}

			final String value2 = getValue(
					param.name() + "thickness" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));
			if (value2 != null && value2.matches("[\\d.]+")) {
				if (style == null) {
					style = LinkStyle.NORMAL();
				}
				return style.goThickness(Double.parseDouble(value2)).getStroke3();
			}
		}
		final String value = getValue(param.name() + "thickness");
		if (value != null && value.matches("[\\d.]+")) {
			if (style == null) {
				style = LinkStyle.NORMAL();
			}
			return style.goThickness(Double.parseDouble(value)).getStroke3();
		}
		if (style == null) {
			final String styleValue = getValue(param.name() + "style");
			if (styleValue != null) {
				style = LinkStyle.fromString2(styleValue);
			}
		}
		if (style != null && style.isNormal() == false) {
			return style.getStroke3();
		}
		return null;
	}

	public LineBreakStrategy maxMessageSize() {
		String value = getValue("wrapmessagewidth");
		if (value == null) {
			value = getValue("maxmessagesize");
		}
		return new LineBreakStrategy(value);
	}

	public LineBreakStrategy wrapWidth() {
		final String value = getValue("wrapwidth");
		return new LineBreakStrategy(value);
	}

	public LineBreakStrategy swimlaneWrapTitleWidth() {
		final String value = getValue("swimlanewraptitlewidth");
		return new LineBreakStrategy(value);
	}

	public boolean strictUmlStyle() {
		final String value = getValue("style");
		if ("strictuml".equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}

	public boolean forceSequenceParticipantUnderlined() {
		final String value = getValue("sequenceParticipant");
		if ("underline".equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}

	public ConditionStyle getConditionStyle() {
		final String value = getValue("conditionStyle");
		final ConditionStyle p = ConditionStyle.fromString(value);
		if (p == null) {
			return ConditionStyle.INSIDE;
		}
		return p;
	}

	public ConditionEndStyle getConditionEndStyle() {
		final String value = getValue("conditionEndStyle");
		final ConditionEndStyle p = ConditionEndStyle.fromString(value);
		if (p == null) {
			return ConditionEndStyle.DIAMOND;
		}
		return p;
	}

	public double minClassWidth() {
		final String value = getValue("minclasswidth");
		if (value != null && value.matches("\\d+")) {
			return Integer.parseInt(value);
		}
		return 0;
	}

	public boolean sameClassWidth() {
		return "true".equals(getValue("sameclasswidth"));
	}

	public final Rankdir getRankdir() {
		return rankdir;
	}

	public final void setRankdir(Rankdir rankdir) {
		this.rankdir = rankdir;
	}

	public boolean useOctagonForActivity(Stereotype stereotype) {
		String value = getValue("activityshape");
		if (stereotype != null) {
			checkStereotype(stereotype);
			final String value2 = getValue("activityshape" + stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));
			if (value2 != null) {
				value = value2;
			}
		}
		if ("roundedbox".equalsIgnoreCase(value)) {
			return false;
		}
		if ("octagon".equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}

	private final HColorSet htmlColorSet = HColorSet.instance();

	public HColorSet getIHtmlColorSet() {
		return htmlColorSet;
	}

	public boolean useUnderlineForHyperlink() {
		final String value = getValue("hyperlinkunderline");
		if ("false".equalsIgnoreCase(value)) {
			return false;
		}
		return true;
	}

	public int groupInheritance() {
		final String value = getValue("groupinheritance");
		int result = Integer.MAX_VALUE;
		if (value != null && value.matches("\\d+")) {
			result = Integer.parseInt(value);
		}
		if (result <= 1) {
			result = Integer.MAX_VALUE;
		}
		return result;
	}

	public Guillemet guillemet() {
		final String value = getValue("guillemet");
		return Guillemet.GUILLEMET.fromDescription(value);
	}

	public boolean handwritten() {
		final String value = getValue("handwritten");
		if ("true".equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}

	public String getSvgLinkTarget() {
		final String value = getValue("svglinktarget");
		if (value == null) {
			return "_top";
		}
		return value;
	}

	public String getPreserveAspectRatio() {
		final String value = getValue("preserveaspectratio");
		if (value == null) {
			return "none";
		}
		return value;
	}

	public String getMonospacedFamily() {
		final String value = getValue("defaultMonospacedFontName");
		if (value == null) {
			return CommandCreoleMonospaced.MONOSPACED;
		}
		return value;
	}

	public int getTabSize() {
		final String value = getValue("tabsize");
		if (value != null && value.matches("\\d+")) {
			return Integer.parseInt(value);
		}
		return 8;
	}

	public int maxAsciiMessageLength() {
		final String value = getValue("maxasciimessagelength");
		if (value != null && value.matches("\\d+")) {
			return Integer.parseInt(value);
		}
		return -1;
	}

	public int colorArrowSeparationSpace() {
		final String value = getValue("colorarrowseparationspace");
		if (value != null && value.matches("\\d+")) {
			return Integer.parseInt(value);
		}
		return 0;
	}

	public SplitParam getSplitParam() {
		final String border = getValue("pageBorderColor");
		final String external = getValue("pageExternalColor");

		final String marginString = getValue("pageMargin");
		int margin = 0;
		if (marginString != null && marginString.matches("\\d+")) {
			margin = Integer.parseInt(marginString);
		}

		return new SplitParam(getIHtmlColorSet().getColorIfValid(border), getIHtmlColorSet().getColorIfValid(external),
				margin);
	}

	public int swimlaneWidth() {
		final String value = getValue("swimlanewidth");
		if ("same".equalsIgnoreCase(value)) {
			return SWIMLANE_WIDTH_SAME;
		}
		if (value != null && value.matches("\\d+")) {
			return Integer.parseInt(value);
		}
		return 0;
	}

	public UmlDiagramType getUmlDiagramType() {
		return type;
	}

	public HColor getHoverPathColor() {
		final String value = getValue("pathhovercolor");
		if (value == null) {
			return null;
		}
		return getIHtmlColorSet().getColorIfValid(value, null);
	}

	public double getPadding() {
		final String name = "padding";
		return getAsDouble(name);
	}

	public double getPadding(PaddingParam param) {
		final String name = param.getSkinName();
		return getAsDouble(name);
	}

	private double getAsDouble(final String name) {
		final String value = getValue(name);
		if (value != null && value.matches("\\d+(\\.\\d+)?")) {
			return Double.parseDouble(value);
		}
		return 0;
	}

	public boolean useRankSame() {
		return false;
	}

	public boolean displayGenericWithOldFashion() {
		final String value = getValue("genericDisplay");
		if ("old".equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}

	public boolean responseMessageBelowArrow() {
		final String value = getValue("responsemessagebelowarrow");
		if ("true".equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}

	public TikzFontDistortion getTikzFontDistortion() {
		final String value = getValue("tikzFont");
		return TikzFontDistortion.fromValue(value);
	}

	public boolean svgDimensionStyle() {
		final String value = getValue("svgdimensionstyle");
		if ("false".equalsIgnoreCase(value)) {
			return false;
		}
		return true;
	}

	public boolean fixCircleLabelOverlapping() {
		final String value = getValue("fixcirclelabeloverlapping");
		if ("true".equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}

	public void setUseVizJs(boolean useVizJs) {
		this.useVizJs = useVizJs;
	}

	public boolean isUseVizJs() {
		return useVizJs;
	}

	public Padder getSequenceDiagramPadder() {
		final double padding = getAsDouble("SequenceMessagePadding");
		final double margin = getAsDouble("SequenceMessageMargin");
		final String borderColor = getValue("SequenceMessageBorderColor");
		final String backgroundColor = getValue("SequenceMessageBackGroundColor");
		if (padding == 0 && margin == 0 && borderColor == null && backgroundColor == null) {
			return Padder.NONE;
		}
		final HColor border = getIHtmlColorSet().getColorIfValid(borderColor);
		final HColor background = getIHtmlColorSet().getColorIfValid(backgroundColor);
		final double roundCorner = getRoundCorner(CornerParam.DEFAULT, null);
		return Padder.NONE.withMargin(margin).withPadding(padding).withBackgroundColor(background)
				.withBorderColor(border).withRoundCorner(roundCorner);
	}

	public ActorStyle getActorStyle() {
		final String value = getValue("actorstyle");
		if ("awesome".equalsIgnoreCase(value)) {
			return ActorStyle.AWESOME;
		}
		return ActorStyle.STICKMAN;
	}

}
