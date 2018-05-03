/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.IHtmlColorSet;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.sprite.SpriteUtils;

public class Stereotype implements CharSequence {
	private final static Pattern2 circleChar = MyPattern
			.cmpile("\\<\\<[%s]*\\(?(\\S)[%s]*,[%s]*(#[0-9a-fA-F]{6}|\\w+)[%s]*(?:[),](.*?))?\\>\\>");
	private final static Pattern2 circleSprite = MyPattern.cmpile("\\<\\<[%s]*\\(?\\$(" + SpriteUtils.SPRITE_NAME
			+ ")[%s]*(?:,[%s]*(#[0-9a-fA-F]{6}|\\w+))?[%s]*(?:[),](.*?))?\\>\\>");

	private final double radius;
	private final UFont circledFont;
	private final boolean automaticPackageStyle;

	private String label;
	private HtmlColor htmlColor;
	private char character;
	private String sprite;

	public Stereotype(String label, double radius, UFont circledFont, IHtmlColorSet htmlColorSet) {
		this(label, radius, circledFont, true, htmlColorSet);
	}

	public Stereotype(String label, double radius, UFont circledFont, boolean automaticPackageStyle,
			IHtmlColorSet htmlColorSet) {
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

		final List<String> list = cutLabels(label, false);
		for (String local : list) {
			final Matcher2 mCircleChar = circleChar.matcher(local);
			final Matcher2 mCircleSprite = circleSprite.matcher(local);
			if (mCircleSprite.find()) {
				if (StringUtils.isNotEmpty(mCircleSprite.group(3))) {
					local = "<<" + mCircleSprite.group(3) + ">>";
				} else {
					local = null;
				}
				final String colName = mCircleSprite.group(2);
				final HtmlColor col = htmlColorSet.getColorIfValid(colName);
				this.htmlColor = col == null ? HtmlColorUtils.BLACK : col;
				this.sprite = mCircleSprite.group(1);
				this.character = '\0';
			} else if (mCircleChar.find()) {
				if (StringUtils.isNotEmpty(mCircleChar.group(3))) {
					local = "<<" + mCircleChar.group(3) + ">>";
				} else {
					local = null;
				}
				final String colName = mCircleChar.group(2);
				this.htmlColor = htmlColorSet.getColorIfValid(colName);
				this.character = mCircleChar.group(1).charAt(0);
				this.sprite = null;
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

	public Stereotype(String label, boolean automaticPackageStyle) {
		this.automaticPackageStyle = automaticPackageStyle;
		this.label = label;
		this.htmlColor = null;
		this.character = '\0';
		this.radius = 0;
		this.circledFont = null;
		if (label.startsWith("<<$") && label.endsWith(">>")) {
			this.sprite = label.substring(3, label.length() - 2).trim();
		} else {
			this.sprite = null;
		}
	}

	public HtmlColor getHtmlColor() {
		return htmlColor;
	}

	public char getCharacter() {
		return character;
	}

	public final String getSprite() {
		return sprite;
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

	public String getLabel(boolean withGuillement) {
		assert label == null || label.length() > 0;
		if (isWithOOSymbol()) {
			return null;
		}
		if (withGuillement) {
			return StringUtils.manageGuillemet(label);
		}
		return label;
	}

	public List<String> getLabels(boolean useGuillemet) {
		final String labelLocal = getLabel(false);
		if (labelLocal == null) {
			return null;
		}
		return cutLabels(labelLocal, useGuillemet);
	}

	private static List<String> cutLabels(final String label, boolean useGuillemet) {
		final List<String> result = new ArrayList<String>();
		final Pattern2 p = MyPattern.cmpile("\\<\\<.*?\\>\\>");
		final Matcher2 m = p.matcher(label);
		while (m.find()) {
			if (useGuillemet) {
				result.add(StringUtils.manageGuillemetStrict(m.group()));
			} else {
				result.add(m.group());
			}
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

}
