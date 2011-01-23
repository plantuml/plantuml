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
 * Revision $Revision: 5982 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.awt.Color;
import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class Stereotype implements CharSequence {

	private final static Pattern circle = Pattern
			.compile("\\<\\<\\s*\\(?(\\S)\\s*,\\s*(#[0-9a-fA-F]{6}|\\w+)\\s*(?:[),](.*?))?\\>\\>");

	private final String label;
	private final HtmlColor htmlColor;
	private final char character;
	private final double radius;
	private final Font circledFont;

	public Stereotype(String label, double radius, Font circledFont) {
		if (label == null) {
			throw new IllegalArgumentException();
		}
		if (label.startsWith("<<") == false || label.endsWith(">>") == false) {
			throw new IllegalArgumentException(label);
		}
		this.radius = radius;
		this.circledFont = circledFont;
		final Matcher m = circle.matcher(label);
		if (m.find()) {
			if (StringUtils.isNotEmpty(m.group(3))) {
				this.label = "<<" + m.group(3) + ">>";
			} else {
				this.label = null;
			}
			final String colName = m.group(2);
			this.htmlColor = HtmlColor.getColorIfValid(colName);
			this.character = m.group(1).charAt(0);
		} else {
			this.label = label;
			this.character = '\0';
			this.htmlColor = null;
		}
	}

	public Stereotype(String stereotype) {
		this.label = stereotype;
		this.htmlColor = null;
		this.character = '\0';
		this.radius = 0;
		this.circledFont = null;
	}

	public Color getColor() {
		if (htmlColor == null) {
			return null;
		}
		return htmlColor.getColor();
	}

	public char getCharacter() {
		return character;
	}

	public String getLabel() {
		assert label == null || label.length() > 0;
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

	public final Font getCircledFont() {
		return circledFont;
	}

}
