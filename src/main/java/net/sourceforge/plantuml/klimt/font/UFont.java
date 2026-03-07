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

public interface UFont {

	/**
	 * Returns a new font with the given legacy style flags.
	 *
	 * @deprecated Use {@link #withFontFace(UFontFace)} to change weight and
	 *             italic style independently.
	 */
	@Deprecated
	public UFont withStyle(int style);

	/**
	 * Returns a bold variant of this font.
	 *
	 * @deprecated Use {@link #withFontFace(UFontFace)} and
	 *             {@link UFontFace#bold()} instead.
	 */
	@Deprecated
	public UFont bold();

	/**
	 * Returns an italic variant of this font.
	 *
	 * @deprecated Use {@link #withFontFace(UFontFace)} and
	 *             {@link UFontFace#italic()} instead.
	 */
	@Deprecated
	public UFont italic();

	/**
	 * Returns the legacy style flags for this font.
	 *
	 * @deprecated Use {@link #getFontFace()} and
	 *             {@link UFontFace#toLegacyStyle()} if needed.
	 */
	@Deprecated
	public int getStyle();

	/**
	 * Returns whether this font is bold according to legacy style flags.
	 *
	 * @deprecated Use {@link #getFontFace()} and
	 *             {@link UFontFace#isBold()}.
	 */
	@Deprecated
	public boolean isBold();

	/**
	 * Returns whether this font is italic according to legacy style flags.
	 *
	 * @deprecated Use {@link #getFontFace()} and
	 *             {@link UFontFace#isItalic()}.
	 */
	@Deprecated
	public boolean isItalic();

	/**
	 * Returns the font face (weight + italic axis) for this font.
	 *
	 * @return font face
	 */
	public UFontFace getFontFace();

	/**
	 * Returns a new font with the given face and the same size and family.
	 *
	 * @param face new face
	 * @return new font with the requested face
	 */
	public UFont withFontFace(UFontFace face);

	public int getSize();

	public double getSize2D();

	public UFont withSize(float size);

	public String getFamily(String text, UFontContext context);

}
