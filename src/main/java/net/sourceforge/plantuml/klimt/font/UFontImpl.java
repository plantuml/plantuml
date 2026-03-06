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

import java.awt.Font;
import java.util.Objects;

import net.sourceforge.plantuml.teavm.TeaVM;

public final class UFontImpl implements UFont {

	private final FontStack fontStack;
	/**
	 * The full font face (italic axis + CSS weight 100-900).  This is the
	 * canonical weight/style store; the legacy {@code int style} field has been
	 * removed.  Use {@link UFontFace#toLegacyStyle()} when a binary
	 * {@code java.awt.Font} style flag is needed (e.g. for EPS/SVG).
	 */
	private final UFontFace face;
	private final int size;

	public static Font getUnderlayingFont(final UFont font, final String text) {
		return ((UFontImpl) font).getUnderlayingFont(text);
	}

	/**
	 * Legacy constructor: converts the binary {@code java.awt.Font} style flag to
	 * a {@link UFontFace} (bold → weight 700, italic → italic axis).  Any
	 * intermediate CSS weight is not representable through this path; prefer
	 * {@link #UFontImpl(FontStack, UFontFace, int)} for new code.
	 */
	UFontImpl(FontStack fontStack, int style, int size) {
		this.fontStack = fontStack;
		this.face = UFontFace.fromLegacyStyle(style);
		this.size = size;
	}

	/**
	 * Face-aware constructor.  Stores the full {@link UFontFace} so that
	 * intermediate CSS weights (100-900) are preserved and applied via
	 * {@link TextAttribute#WEIGHT} during Java2D rendering.
	 */
	UFontImpl(FontStack fontStack, UFontFace face, int size) {
		this.fontStack = fontStack;
		this.face = face == null ? UFontFace.normal() : face;
		this.size = size;
	}

	/**
	 * Returns the underlying {@link java.awt.Font} for the given text, with all
	 * font properties applied via {@link java.awt.font.TextAttribute}s.
	 *
	 * <p>Weight (CSS 100-900) and italic axis are both applied through
	 * {@link UFontFace#deriveFont(Font)} so that fonts with intermediate weight
	 * faces (e.g. Helvetica Neue Medium) render at the requested weight.
	 * Fonts that only provide binary bold/normal fall back gracefully.
	 */
	public Font getUnderlayingFont(String text) {
		return fontStack.getFont(text, face, size);
	}

	public UFont withSize(float size) {
		return new UFontImpl(fontStack, this.face, (int) size);
	}

	public UFontFace getFontFace() {
		return face;
	}

	public UFont withFontFace(UFontFace newFace) {
		if (newFace == null)
			return this;
		return new UFontImpl(fontStack, newFace, this.size);
	}

	/**
	 * @deprecated Use {@link #withFontFace(UFontFace)} to preserve CSS weight.
	 *             This method converts the binary italic/bold flags to a
	 *             {@link UFontFace}, losing any intermediate weight.
	 */
	@Deprecated
	public UFont withStyle(int style) {
		return new UFontImpl(fontStack, UFontFace.fromLegacyStyle(style), this.size);
	}

	@Deprecated
	public UFont bold() {
		return withFontFace(UFontFace.bold());
	}

	@Deprecated
	public UFont italic() {
		return withFontFace(UFontFace.italic());
	}

	/**
	 * @deprecated Use {@link #getFontFace()} and {@link UFontFace#toLegacyStyle()}
	 *             instead.  This method discards any intermediate CSS weight.
	 */
	@Deprecated
	public int getStyle() {
		return face.toLegacyStyle();
	}

	public int getSize() {
		return size;
	}

	public double getSize2D() {
		return size;
	}

	@Deprecated
	public boolean isBold() {
		return face.isBold();
	}

	@Deprecated
	public boolean isItalic() {
		return face.isItalic();
	}

	public String getFamily(String text, UFontContext context) {
		if (TeaVM.isTeaVM()) {
			final String fullDefinition = fontStack.getFullDefinition();
			// Map Java font name to web-safe equivalent
			// http://plantuml.sourceforge.net/qa/?qa=5432/svg-monospace-output-has-wrong-font-family
			if ("monospaced".equalsIgnoreCase(fullDefinition))
				return "monospace";

			return fullDefinition.replace('"', '\'').replaceAll("(?i)sansserif", "sans-serif");
		} else {
			if (context == UFontContext.EPS) {
//			if (fontStack.getFamily() == null)
//				return "Times-Roman";
				return getUnderlayingFont(text).getPSName();
			}
			if (context == UFontContext.SVG) {
				String result = fontStack.getFullDefinition().replace('"', '\'');
				result = result.replaceAll("(?i)sansserif", "sans-serif");

				return result;
			}
		}
		throw new IllegalArgumentException();
	}

	// Kludge for testing because font names on some machines (only macOS?) do not
	// end with <DOT><STYLE>
	// See https://github.com/plantuml/plantuml/issues/720
	private String getPortableFontName() {
		final Font font = getUnderlayingFont(null);
		final String name = font.getFontName();
		if (font.isBold() && font.isItalic())
			return name.endsWith(".bolditalic") ? name : name + ".bolditalic";
		else if (font.isBold())
			return name.endsWith(".bold") ? name : name + ".bold";
		else if (font.isItalic())
			return name.endsWith(".italic") ? name : name + ".italic";
		else
			return name.endsWith(".plain") ? name : name + ".plain";
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(getPortableFontName());
		sb.append("/");
		sb.append(getSize());
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(fontStack, face, size);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UFontImpl))
			return false;
		UFontImpl other = (UFontImpl) obj;
		return Objects.equals(fontStack, other.fontStack) && Objects.equals(face, other.face) && size == other.size;
	}

}
