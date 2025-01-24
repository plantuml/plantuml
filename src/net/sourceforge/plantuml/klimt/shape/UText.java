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
package net.sourceforge.plantuml.klimt.shape;

import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class UText implements UShape {

	private final String text;
	private final FontConfiguration font;
	private final int orientation;

	@Override
	public String toString() {
		return "UText[" + text + "]";
	}

	private UText(String text, FontConfiguration font, int orientation) {
		assert text.indexOf('\t') == -1;
		this.text = text.replace(Jaws.BLOCK_E1_NEWLINE, '\u21b5').replace(Jaws.BLOCK_E1_BREAKLINE, '\u23ce');
		this.font = font;
		this.orientation = orientation;
	}

	public static UText build(String text, FontConfiguration font) {
		return new UText(text, font, 0);
	}

	public UText withOrientation(int orientation) {
		return new UText(text, font, orientation);
	}

	public String getText() {
		return text;
	}

	public FontConfiguration getFontConfiguration() {
		return font;
	}

	// ::comment when __HAXE__
	public double getDescent(StringBounder stringBounder) {
		return stringBounder.getDescent(font.getFont(), text);
	}
	// ::done

	public final int getOrientation() {
		return orientation;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return stringBounder.calculateDimension(font.getFont(), text);
	}

}
