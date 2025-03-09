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
package net.sourceforge.plantuml.skin;

import net.atmp.InnerStrategy;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public enum VisibilityModifier {
	PRIVATE_FIELD(StringUtils.PRIVATE_FIELD, ColorParam.iconPrivate, null),
	PROTECTED_FIELD(StringUtils.PROTECTED_FIELD, ColorParam.iconProtected, null),
	PACKAGE_PRIVATE_FIELD(StringUtils.PACKAGE_PRIVATE_FIELD, ColorParam.iconPackage, null),
	PUBLIC_FIELD(StringUtils.PUBLIC_FIELD, ColorParam.iconPublic, null),

	PRIVATE_METHOD(StringUtils.PRIVATE_METHOD, ColorParam.iconPrivate, ColorParam.iconPrivateBackground),
	PROTECTED_METHOD(StringUtils.PROTECTED_METHOD, ColorParam.iconProtected, ColorParam.iconProtectedBackground),
	PACKAGE_PRIVATE_METHOD(StringUtils.PACKAGE_PRIVATE_METHOD, ColorParam.iconPackage,
			ColorParam.iconPackageBackground),
	PUBLIC_METHOD(StringUtils.PUBLIC_METHOD, ColorParam.iconPublic, ColorParam.iconPublicBackground),

	IE_MANDATORY(StringUtils.IE_MANDATORY, ColorParam.iconIEMandatory, ColorParam.iconIEMandatory);

	private final ColorParam foregroundParam;
	private final ColorParam backgroundParam;
	private final char unicode;

	public static String regexForVisibilityCharacterInClassName() {
		return "[-#+~]";
	}

	private VisibilityModifier(char unicode, ColorParam foreground, ColorParam background) {
		this.foregroundParam = foreground;
		this.backgroundParam = background;
		this.unicode = unicode;
	}

	public UDrawable getUDrawable(final int size, final HColor foregroundColor, final HColor backgoundColor) {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				drawInternal(ug, size, foregroundColor, backgoundColor, 0, 0);
			}

		};
	}

	public TextBlock getUBlock(final int size, final HColor foregroundColor, final HColor backgoundColor,
			final boolean withInvisibleRectanble) {
		return new AbstractTextBlock() {

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(size + 1, size + 1);
			}

			@Override
			public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return null;
			}

			public void drawU(UGraphic ug) {
				if (withInvisibleRectanble)
					ug.apply(HColors.none()).draw(URectangle.build(size * 2, size));

				drawInternal(ug, size, foregroundColor, backgoundColor, 0, 0);
			}
		};
	}

	private void drawInternal(UGraphic ug, int size, final HColor foregroundColor, final HColor backgoundColor,
			double x, double y) {
		if (backgoundColor == null)
			ug = ug.apply(HColors.none().bg());
		else
			ug = ug.apply(backgoundColor.bg());

		ug = ug.apply(foregroundColor);
		size = ensureEven(size);
		switch (this) {
		case PACKAGE_PRIVATE_FIELD:
			drawTriangle(ug, false, size, x, y);
			break;

		case PRIVATE_FIELD:
			drawSquare(ug, false, size, x, y);
			break;

		case PROTECTED_FIELD:
			drawDiamond(ug, false, size, x, y);
			break;

		case PUBLIC_FIELD:
			drawCircle(ug, false, size, x, y);
			break;

		case PACKAGE_PRIVATE_METHOD:
			drawTriangle(ug, true, size, x, y);
			break;

		case PRIVATE_METHOD:
			drawSquare(ug, true, size, x, y);
			break;

		case PROTECTED_METHOD:
			drawDiamond(ug, true, size, x, y);
			break;

		case PUBLIC_METHOD:
			drawCircle(ug, true, size, x, y);
			break;

		case IE_MANDATORY:
			drawCircle(ug, true, size, x, y);
			break;

		default:
			throw new IllegalStateException();
		}
	}

	private void drawSquare(UGraphic ug, boolean filled, int size, double x, double y) {
		ug.apply(new UTranslate(x + 2, y + 2)).draw(URectangle.build(size - 4, size - 4));
	}

	private void drawCircle(UGraphic ug, boolean filled, int size, double x, double y) {
		ug.apply(new UTranslate(x + 2, y + 2)).draw(UEllipse.build(size - 4, size - 4));
	}

	static private int ensureEven(int n) {
		if (n % 2 == 1)
			n--;
		return n;
	}

	private void drawDiamond(UGraphic ug, boolean filled, int size, double x, double y) {
		final UPolygon poly = new UPolygon();
		size -= 2;
		poly.addPoint(size / 2.0, 0);
		poly.addPoint(size, size / 2.0);
		poly.addPoint(size / 2.0, size);
		poly.addPoint(0, size / 2.0);
		ug.apply(new UTranslate(x + 1, y)).draw(poly);
	}

	private void drawTriangle(UGraphic ug, boolean filled, int size, double x, double y) {
		final UPolygon poly = new UPolygon();
		size -= 2;
		poly.addPoint(size / 2.0, 1);
		poly.addPoint(0, size - 1);
		poly.addPoint(size, size - 1);
		ug.apply(new UTranslate(x + 1, y)).draw(poly);
	}

	public static boolean isVisibilityCharacter(CharSequence s) {
		if (s.length() <= 2)
			return false;

		final char c = s.charAt(0);
		if (s.charAt(1) == c)
			return false;

		if (c == '-')
			return true;

		if (c == '#')
			return true;

		if (c == '+')
			return true;

		if (c == '~')
			return true;

		if (c == '*')
			return true;

		return false;
	}

	public static VisibilityModifier getByUnicode(char c) {
		for (VisibilityModifier modifier : VisibilityModifier.values())
			if (modifier.unicode == c)
				return modifier;

		return null;
	}

	public static String replaceVisibilityModifierByUnicodeChar(String s, boolean isField) {
		final VisibilityModifier modifier = getVisibilityModifier(s, isField);
		if (modifier == null)
			return s;

		return "" + modifier.unicode + s.substring(1);
	}

	public static VisibilityModifier getVisibilityModifier(CharSequence s, boolean isField) {
		if (s.length() <= 2)
			return null;

		final char c = s.charAt(0);
		if (s.charAt(1) == c)
			return null;

		if (isField)
			return getVisibilityModifierForField(c);

		return getVisibilityModifierForMethod(c);
	}

	private static VisibilityModifier getVisibilityModifierForField(char c) {
		if (c == '-')
			return VisibilityModifier.PRIVATE_FIELD;

		if (c == '#')
			return VisibilityModifier.PROTECTED_FIELD;

		if (c == '+')
			return VisibilityModifier.PUBLIC_FIELD;

		if (c == '~')
			return VisibilityModifier.PACKAGE_PRIVATE_FIELD;

		if (c == '*')
			return VisibilityModifier.IE_MANDATORY;

		return null;
	}

	private static VisibilityModifier getVisibilityModifierForMethod(char c) {
		if (c == '-')
			return VisibilityModifier.PRIVATE_METHOD;

		if (c == '#')
			return VisibilityModifier.PROTECTED_METHOD;

		if (c == '+')
			return VisibilityModifier.PUBLIC_METHOD;

		if (c == '~')
			return VisibilityModifier.PACKAGE_PRIVATE_METHOD;

		if (c == '*')
			return VisibilityModifier.IE_MANDATORY;

		return null;
	}

	public final ColorParam getForeground() {
		return foregroundParam;
	}

	public final ColorParam getBackground() {
		return backgroundParam;
	}

	public String getXmiVisibility() {
		if (this == PUBLIC_FIELD || this == PUBLIC_METHOD)
			return "public";

		if (this == PRIVATE_FIELD || this == PRIVATE_METHOD)
			return "private";

		if (this == PROTECTED_FIELD || this == PROTECTED_METHOD)
			return "protected";

		if (this == PACKAGE_PRIVATE_FIELD || this == VisibilityModifier.PACKAGE_PRIVATE_METHOD)
			return "package";

		throw new IllegalStateException();
	}

	public boolean isField() {
		if (this == PUBLIC_FIELD || this == PRIVATE_FIELD || this == PROTECTED_FIELD || this == PACKAGE_PRIVATE_FIELD)
			return true;

		return false;
	}

	public StyleSignatureBasic getStyleSignature() {
		if (this == IE_MANDATORY)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.visibilityIcon, SName.IEMandatory);

		if (this == PUBLIC_FIELD || this == PUBLIC_METHOD)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.visibilityIcon, SName.public_);

		if (this == PRIVATE_FIELD || this == PRIVATE_METHOD)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.visibilityIcon, SName.private_);

		if (this == PROTECTED_FIELD || this == PROTECTED_METHOD)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.visibilityIcon, SName.protected_);

		if (this == PACKAGE_PRIVATE_FIELD || this == VisibilityModifier.PACKAGE_PRIVATE_METHOD)
			return StyleSignatureBasic.of(SName.root, SName.element, SName.visibilityIcon, SName.package_);

		throw new IllegalStateException();
	}

}
