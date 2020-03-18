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
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.command.regex.RegexComposed;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.creole.command.CommandCreoleImg;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.sprite.Sprite;
import net.sourceforge.plantuml.sprite.SpriteUtils;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class Stereotype implements CharSequence {
	private final static RegexComposed circleChar = new RegexConcat( //
			new RegexLeaf("\\<\\<"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexLeaf("\\(?"), //
			new RegexLeaf("CHAR", "(\\S)"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexLeaf(","), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexLeaf("COLOR", "(#[0-9a-fA-F]{6}|\\w+)"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexOptional(new RegexLeaf("LABEL", "[),](.*?)")), //
			new RegexLeaf("\\>\\>") //
	);

	private final static RegexComposed circleSprite = new RegexConcat( //
			new RegexLeaf("\\<\\<"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexLeaf("\\(?\\$"), //
			new RegexLeaf("NAME", "(" + SpriteUtils.SPRITE_NAME + ")"), //
			new RegexLeaf("SCALE", "((?:\\{scale=|\\*)([0-9.]+)\\}?)?"), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexOptional( //
					new RegexConcat( //
							new RegexLeaf(","), //
							RegexLeaf.spaceZeroOrMore(), //
							new RegexLeaf("COLOR", "(#[0-9a-fA-F]{6}|\\w+)") //
					)), //
			RegexLeaf.spaceZeroOrMore(), //
			new RegexOptional(new RegexLeaf("LABEL", "[),](.*?)")), //
			new RegexLeaf("\\>\\>") //
	);

	private final double radius;
	private final UFont circledFont;
	private final boolean automaticPackageStyle;

	private String label;
	private HColor htmlColor;
	private char character;
	private String spriteName;
	private double spriteScale;

	public Stereotype(String label, double radius, UFont circledFont, HColorSet htmlColorSet) {
		this(label, radius, circledFont, true, htmlColorSet);
	}

	public Stereotype(String label, boolean automaticPackageStyle) {
		this.automaticPackageStyle = automaticPackageStyle;
		this.label = label;
		this.htmlColor = null;
		this.character = '\0';
		this.radius = 0;
		this.circledFont = null;
		if (label.startsWith("<<$") && label.endsWith(">>")) {
			final RegexResult mCircleSprite = circleSprite.matcher(label);
			this.spriteName = mCircleSprite.get("NAME", 0);
			this.spriteScale = CommandCreoleImg.getScale(mCircleSprite.get("SCALE", 0), 1);
		} else {
			this.spriteName = null;
		}
	}

	public Stereotype(String label, double radius, UFont circledFont, boolean automaticPackageStyle,
			HColorSet htmlColorSet) {
		if (label == null) {
			throw new IllegalArgumentException();
		}
		if (label.startsWith("<<") == false || label.endsWith(">>") == false) {
			throw new IllegalArgumentException(label);
		}
		this.automaticPackageStyle = automaticPackageStyle;
		this.radius = radius;
		this.circledFont = circledFont;

		final StringBuilder tmpLabel = new StringBuilder();

		final List<String> list = cutLabels(label, Guillemet.DOUBLE_COMPARATOR);
		for (String local : list) {
			final RegexResult mCircleChar = circleChar.matcher(local);
			final RegexResult mCircleSprite = circleSprite.matcher(local);
			if (mCircleSprite != null) {
				if (StringUtils.isNotEmpty(mCircleSprite.get("LABEL", 0))) {
					local = "<<" + mCircleSprite.get("LABEL", 0) + ">>";
				} else {
					local = null;
				}
				final String colName = mCircleSprite.get("COLOR", 0);
				final HColor col = htmlColorSet.getColorIfValid(colName);
				this.htmlColor = col == null ? HColorUtils.BLACK : col;
				this.spriteName = mCircleSprite.get("NAME", 0);
				this.character = '\0';
				this.spriteScale = CommandCreoleImg.getScale(mCircleSprite.get("SCALE", 0), 1);
			} else if (mCircleChar != null) {
				if (StringUtils.isNotEmpty(mCircleChar.get("LABEL", 0))) {
					local = "<<" + mCircleChar.get("LABEL", 0) + ">>";
				} else {
					local = null;
				}
				final String colName = mCircleChar.get("COLOR", 0);
				this.htmlColor = htmlColorSet.getColorIfValid(colName);
				this.character = mCircleChar.get("CHAR", 0).charAt(0);
				this.spriteName = null;
			}
			if (local != null) {
				tmpLabel.append(local);
			}
		}
		if (tmpLabel.length() > 0) {
			this.label = tmpLabel.toString();
		}
	}

	public Stereotype(String label) {
		this(label, true);
	}

	public HColor getHtmlColor() {
		return htmlColor;
	}

	public char getCharacter() {
		return character;
	}

	public final TextBlock getSprite(SpriteContainer container) {
		if (spriteName == null || container == null) {
			return null;
		}
		final Sprite tmp = container.getSprite(spriteName);
		if (tmp == null) {
			return null;
		}
		return tmp.asTextBlock(getHtmlColor(), spriteScale);
	}

	public boolean isWithOOSymbol() {
		return "<<O-O>>".equalsIgnoreCase(label);
	}

	public List<String> getMultipleLabels() {
		final List<String> result = new ArrayList<String>();
		if (label != null) {
			final Pattern p = Pattern.compile("\\<\\<\\s?((?:\\<&\\w+\\>|[^<>])+?)\\s?\\>\\>");
			final Matcher m = p.matcher(label);
			while (m.find()) {
				result.add(m.group(1));
			}
		}
		return Collections.unmodifiableList(result);
	}

	public boolean isSpotted() {
		return character != 0;
	}

	@Override
	public String toString() {
		if (label == null) {
			return "" + character;
		}
		if (character == 0) {
			return label;
		}
		return character + " " + label;
	}

	public char charAt(int arg0) {
		return toString().charAt(arg0);
	}

	public int length() {
		return toString().length();
	}

	public CharSequence subSequence(int arg0, int arg1) {
		return toString().subSequence(arg0, arg1);
	}

	public double getRadius() {
		return radius;
	}

	public final UFont getCircledFont() {
		return circledFont;
	}

	public String getLabel(Guillemet guillemet) {
		assert label == null || label.length() > 0;
		if (isWithOOSymbol()) {
			return null;
		}
		if (spriteName != null && spriteName.startsWith("archimate/")) {
			return guillemet.manageGuillemet("<<" + spriteName.substring("archimate/".length()) + ">>");
		}
		return guillemet.manageGuillemet(label);
	}

	public List<String> getLabels(Guillemet guillemet) {
		final String labelLocal = getLabel(Guillemet.DOUBLE_COMPARATOR);
		if (labelLocal == null) {
			return Collections.emptyList();
		}
		return cutLabels(labelLocal, guillemet);
	}

	public List<Style> getStyles(StyleBuilder builder) {
		final List<Style> result = new ArrayList<Style>();
		for (String s : getStyleNames()) {
			final Style style = builder.createStyle(s);
			assert (style != null);
			result.add(style);
		}
		return Collections.unmodifiableList(result);
	}

	public List<String> getStyleNames() {
		final List<String> labels = getLabels(Guillemet.NONE);
		if (labels == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(labels);
	}

	private static List<String> cutLabels(final String label, Guillemet guillemet) {
		final List<String> result = new ArrayList<String>();
		final Pattern2 p = MyPattern.cmpile("\\<\\<.*?\\>\\>");
		final Matcher2 m = p.matcher(label);
		while (m.find()) {
			result.add(guillemet.manageGuillemetStrict(m.group()));
		}
		return Collections.unmodifiableList(result);
	}

	public PackageStyle getPackageStyle() {
		if (automaticPackageStyle == false) {
			return null;
		}
		for (PackageStyle p : EnumSet.allOf(PackageStyle.class)) {
			if (("<<" + p + ">>").equalsIgnoreCase(label)) {
				return p;
			}
		}
		return null;
	}

	public boolean isBiddableOrUncertain() {
		return label.equalsIgnoreCase("<<B>>") || label.equalsIgnoreCase("<<Biddable>>")
				|| label.equalsIgnoreCase("<<Uncertain>>");
	}

	public boolean isCausal() {
		return label.equalsIgnoreCase("<<C>>") || label.equalsIgnoreCase("<<Causal>>");
	}

	public boolean isLexicalOrGiven() {
		return label.equalsIgnoreCase("<<L>>") || label.equalsIgnoreCase("<<Lexical>>")
				|| label.equalsIgnoreCase("<<X>>") || label.equalsIgnoreCase("<<Given>>");
	}

	public boolean isDesignedOrSolved() {
		return label.equalsIgnoreCase("<<D>>") || label.equalsIgnoreCase("<<Designed>>")
				|| label.equalsIgnoreCase("<<Nested>>") || label.equalsIgnoreCase("<<Solved>>");
	}

	public boolean isMachineOrSpecification() {
		return label.equalsIgnoreCase("M") || label.equalsIgnoreCase("<<Machine>>") || label.equalsIgnoreCase("<<S>>")
				|| label.equalsIgnoreCase("<<Spec>>") || label.equalsIgnoreCase("<<Specification>>");
	}

}
