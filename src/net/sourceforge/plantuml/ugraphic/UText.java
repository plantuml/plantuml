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
package net.sourceforge.plantuml.ugraphic;

import java.awt.font.LineMetrics;

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.TextBlockUtils;

public class UText implements UShape {

	private final String text;
	private final FontConfiguration font;

	@Override
	public String toString() {
		return "UText[" + text + "]";
	}

	public UText(String text, FontConfiguration font) {
		assert text.indexOf('\t') == -1;
		this.text = text;
		this.font = font;
	}

	public String getText() {
		return text;
	}

	public FontConfiguration getFontConfiguration() {
		return font;
	}

	public double getDescent() {
		final LineMetrics fm = TextBlockUtils.getLineMetrics(font.getFont(), text);
		final double descent = fm.getDescent();
		return descent;
	}

}
