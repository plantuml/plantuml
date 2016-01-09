/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 18291 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Hideable;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.IHtmlColorSet;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.ugraphic.UFont;

public class Stereotype implements CharSequence, Hideable {
	private final static Pattern circleChar = MyPattern
			.cmpile("\\<\\<[%s]*\\(?(\\S)[%s]*,[%s]*(#[0-9a-fA-F]{6}|\\w+)[%s]*(?:[),](.*?))?\\>\\>");
	private final static Pattern circleSprite = MyPattern
			.cmpile("\\<\\<[%s]*\\(?\\$([-\\p{L}0-9_/]+)[%s]*(?:,[%s]*(#[0-9a-fA-F]{6}|\\w+))?[%s]*(?:[),](.*?))?\\>\\>");

	private final String label;
	private final HtmlColor htmlColor;
	private final char character;
	private final String sprite;
	private final double radius;
	private final UFont circledFont;
	private final boolean automaticPackageStyle;

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
		final Matcher mCircleChar = circleChar.matcher(label);
		final Matcher mCircleSprite = circleSprite.matcher(label);
		if (mCircleSprite.find()) {
			if (StringUtils.isNotEmpty(mCircleSprite.group(3))) {
				this.label = "<<" + mCircleSprite.group(3) + ">>";
			} else {
				this.label = null;
			}
			final String colName = mCircleSprite.group(2);
			final HtmlColor col = htmlColorSet.getColorIfValid(colName);
			this.htmlColor = col == null ? HtmlColorUtils.BLACK : col;
			this.sprite = mCircleSprite.group(1);
			this.character = '\0';
		} else if (mCircleChar.find()) {
			if (StringUtils.isNotEmpty(mCircleChar.group(3))) {
				this.label = "<<" + mCircleChar.group(3) + ">>";
			} else {
				this.label = null;
			}
			final String colName = mCircleChar.group(2);
			this.htmlColor = htmlColorSet.getColorIfValid(colName);
			this.character = mCircleChar.group(1).charAt(0);
			this.sprite = null;
		} else {
			this.label = label;
			this.character = '\0';
			this.htmlColor = null;
			this.sprite = null;
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
			this.sprite = label.substring(3, label.length() - 2);
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

	public String getLabel(boolean withGuillement) {
		assert label == null || label.length() > 0;
		if (isWithOOSymbol()) {
			return null;
		}
		if (withGuillement) {
			return StringUtils.manageGuillemetStrict(label);
		}
		return label;
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

	public List<String> getLabels(boolean useGuillemet) {
		if (getLabel(false) == null) {
			return null;
		}
		final List<String> result = new ArrayList<String>();
		final Pattern p = MyPattern.cmpile("\\<\\<.*?\\>\\>");
		final Matcher m = p.matcher(getLabel(false));
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

	public boolean isHidden() {
		return "<<hidden>>".equalsIgnoreCase(label);
	}

}
