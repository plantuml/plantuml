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
 * Revision $Revision: 11707 $
 *
 */
package net.sourceforge.plantuml;

import java.awt.Font;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.cucadiagram.dot.DotSplines;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizLayoutStrategy;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.svek.ConditionStyle;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ColorMapperMonochrome;
import net.sourceforge.plantuml.ugraphic.Sprite;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class SkinParam implements ISkinParam {

	private final Map<String, String> params = new HashMap<String, String>();

	public void setParam(String key, String value) {
		params.put(cleanForKey(key), value.trim());
	}

	private static final String stereoPatternString = "\\<\\<(.*?)\\>\\>";
	private static final Pattern stereoPattern = Pattern.compile(stereoPatternString);

	private final UmlDiagramType type;

	public SkinParam(UmlDiagramType type) {
		this.type = type;
		if (type == null) {
			setParam("shadowing", "false");
		}
	}

	static String cleanForKey(String key) {
		key = key.toLowerCase().trim();
		key = key.replaceAll("_|\\.|\\s", "");
		key = key.replaceAll("partition", "package");
		key = key.replaceAll("activityarrow", "genericarrow");
		key = key.replaceAll("objectarrow", "genericarrow");
		key = key.replaceAll("classarrow", "genericarrow");
		key = key.replaceAll("componentarrow", "genericarrow");
		key = key.replaceAll("statearrow", "genericarrow");
		key = key.replaceAll("usecasearrow", "genericarrow");
		final Matcher m = stereoPattern.matcher(key);
		if (m.find()) {
			final String s = m.group(1);
			key = key.replaceAll(stereoPatternString, "");
			key += "<<" + s + ">>";
		}
		return key;
	}

	public HtmlColor getBackgroundColor() {
		final HtmlColor result = getHtmlColor(ColorParam.background, null, false);
		if (result == null) {
			return HtmlColorUtils.WHITE;
		}
		return result;
	}

	public String getValue(String key) {
		return params.get(cleanForKey(key));
	}

	static String humanName(String key) {
		final StringBuilder sb = new StringBuilder();
		boolean upper = true;
		for (int i = 0; i < key.length(); i++) {
			final char c = key.charAt(i);
			if (c == '_') {
				upper = true;
			} else {
				sb.append(upper ? Character.toUpperCase(c) : Character.toLowerCase(c));
				upper = false;
			}
		}
		return sb.toString();
	}

	public HtmlColor getHtmlColor(ColorParam param, String stereotype, boolean clickable) {
		if (stereotype != null) {
			checkStereotype(stereotype);
			final String value2 = getValue(param.name() + "color" + stereotype);
			if (value2 != null && HtmlColorUtils.getColorIfValid(value2) != null) {
				return HtmlColorUtils.getColorIfValid(value2);
			}
		}
		final String value = getValue(getParamName(param, clickable));
		final boolean acceptTransparent = param == ColorParam.background;
		if (value == null) {
			return null;
		}
		return HtmlColorUtils.getColorIfValid(value, acceptTransparent);
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

	private void checkStereotype(String stereotype) {
		if (stereotype.startsWith("<<") == false || stereotype.endsWith(">>") == false) {
			throw new IllegalArgumentException();
		}
	}

	private int getFontSize(FontParam param, String stereotype) {
		if (stereotype != null) {
			checkStereotype(stereotype);
			final String value2 = getValue(param.name() + "fontsize" + stereotype);
			if (value2 != null && value2.matches("\\d+")) {
				return Integer.parseInt(value2);
			}
		}
		String value = getValue(param.name() + "fontsize");
		if (value == null || value.matches("\\d+") == false) {
			value = getValue("defaultfontsize");
		}
		if (value == null || value.matches("\\d+") == false) {
			return param.getDefaultSize(this);
		}
		return Integer.parseInt(value);
	}

	private String getFontFamily(FontParam param, String stereotype) {
		if (stereotype != null) {
			checkStereotype(stereotype);
			final String value2 = getValue(param.name() + "fontname" + stereotype);
			if (value2 != null) {
				return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(value2);
			}
		}
		// Times, Helvetica, Courier or Symbol
		String value = getValue(param.name() + "fontname");
		if (value != null) {
			return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(value);
		}
		if (param != FontParam.CIRCLED_CHARACTER) {
			value = getValue("defaultfontname");
			if (value != null) {
				return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(value);
			}
		}
		return param.getDefaultFamily();
	}

	public HtmlColor getFontHtmlColor(FontParam param, String stereotype) {
		String value = null;
		if (stereotype != null) {
			checkStereotype(stereotype);
			value = getValue(param.name() + "fontcolor" + stereotype);
		}
		if (value == null || HtmlColorUtils.getColorIfValid(value) == null) {
			value = getValue(param.name() + "fontcolor");
		}
		if (value == null || HtmlColorUtils.getColorIfValid(value) == null) {
			value = getValue("defaultfontcolor");
		}
		if (value == null || HtmlColorUtils.getColorIfValid(value) == null) {
			value = param.getDefaultColor();
		}
		return HtmlColorUtils.getColorIfValid(value);
	}

	private int getFontStyle(FontParam param, String stereotype) {
		String value = null;
		if (stereotype != null) {
			checkStereotype(stereotype);
			value = getValue(param.name() + "fontstyle" + stereotype);
		}
		if (value == null) {
			value = getValue(param.name() + "fontstyle");
		}
		if (value == null) {
			value = getValue("defaultfontstyle");
		}
		if (value == null) {
			return param.getDefaultFontStyle(this);
		}
		int result = Font.PLAIN;
		if (value.toLowerCase().contains("bold")) {
			result = result | Font.BOLD;
		}
		if (value.toLowerCase().contains("italic")) {
			result = result | Font.ITALIC;
		}
		return result;
	}

	public UFont getFont(FontParam fontParam, String stereotype) {
		if (stereotype != null) {
			checkStereotype(stereotype);
		}
		final String fontFamily = getFontFamily(fontParam, stereotype);
		final int fontStyle = getFontStyle(fontParam, stereotype);
		return new UFont(fontFamily, fontStyle, getFontSize(fontParam, stereotype));
	}

	public int getCircledCharacterRadius() {
		final String value = getValue("circledcharacterradius");
		if (value != null && value.matches("\\d+")) {
			return Integer.parseInt(value);
		}
		// return 11;
		// Log.println("SIZE1="+getFontSize(FontParam.CIRCLED_CHARACTER));
		// Log.println("SIZE1="+getFontSize(FontParam.CIRCLED_CHARACTER)/3);
		return getFontSize(FontParam.CIRCLED_CHARACTER, null) / 3 + 6;
	}

	public int classAttributeIconSize() {
		final String value = getValue("classAttributeIconSize");
		if (value != null && value.matches("\\d+")) {
			return Integer.parseInt(value);
		}
		return 10;
	}

	private boolean isMonochrome() {
		return "true".equals(getValue("monochrome"));
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
		return Collections.unmodifiableSet(result);
	}

	private static String capitalize(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
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

	public GraphvizLayoutStrategy getStrategy() {
		final String value = getValue("layout");
		if ("neato".equalsIgnoreCase(value)) {
			return GraphvizLayoutStrategy.NEATO;
		}
		if ("circo".equalsIgnoreCase(value)) {
			return GraphvizLayoutStrategy.CIRCO;
		}
		if ("fdp".equalsIgnoreCase(value)) {
			return GraphvizLayoutStrategy.FDP;
		}
		if ("twopi".equalsIgnoreCase(value)) {
			return GraphvizLayoutStrategy.TWOPI;
		}
		return GraphvizLayoutStrategy.DOT;
	}

	public HorizontalAlignment getHorizontalAlignment(AlignParam param) {
		final String value;
		switch (param) {
		case SEQUENCE_MESSAGE_ALIGN:
			value = getArg(getValue(AlignParam.SEQUENCE_MESSAGE_ALIGN.name()), 0);
			break;
		case SEQUENCE_MESSAGETEXT_ALIGN:
			value = getArg(getValue(AlignParam.SEQUENCE_MESSAGE_ALIGN.name()), 1);
			break;
		default:
			value = getValue(param.name());
		}
		final HorizontalAlignment result = HorizontalAlignment.fromString(value);
		if (result == null) {
			return param.getDefaultValue();
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
		if (isMonochrome()) {
			return new ColorMapperMonochrome();
		}
		return new ColorMapperIdentity();
	}

	public boolean shadowing() {
		if (strictUmlStyle()) {
			return false;
		}
		final String value = getValue("shadowing");
		if ("false".equalsIgnoreCase(value)) {
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

	public void addSprite(String name, Sprite sprite) {
		sprites.put(name, sprite);
	}

	public Sprite getSprite(String name) {
		return sprites.get(name);
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

	public boolean useSwimlanes() {
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

	public double getRoundCorner() {
		final String value = getValue("roundcorner");
		if (value != null && value.matches("\\d+")) {
			return Double.parseDouble(value);
		}
		return 0;
	}

	public UStroke getThickness(LineParam param) {
		final String value = getValue(param.name() + "thickness");
		if (value != null && value.matches("[\\d.]+")) {
			return new UStroke(Double.parseDouble(value));
		}
		return null;
	}

	public double maxMessageSize() {
		final String value = getValue("maxmessagesize");
		if (value != null && value.matches("-?\\d+")) {
			return Double.parseDouble(value);
		}
		return 0;
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

}
