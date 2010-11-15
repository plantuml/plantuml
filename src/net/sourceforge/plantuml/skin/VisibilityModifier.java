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
 * Revision $Revision: 4189 $
 *
 */
package net.sourceforge.plantuml.skin;

import java.awt.Color;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;

public enum VisibilityModifier {
	PRIVATE_FIELD(ColorParam.iconPrivate, null), PROTECTED_FIELD(ColorParam.iconProtected, null), PACKAGE_PRIVATE_FIELD(
			ColorParam.iconPackage, null), PUBLIC_FIELD(ColorParam.iconPublic, null),

	PRIVATE_METHOD(ColorParam.iconPrivate, ColorParam.iconPrivateBackground), PROTECTED_METHOD(
			ColorParam.iconProtected, ColorParam.iconProtectedBackground), PACKAGE_PRIVATE_METHOD(
			ColorParam.iconPackage, ColorParam.iconPackageBackground), PUBLIC_METHOD(ColorParam.iconPublic,
			ColorParam.iconPublicBackground);

	private final ColorParam foreground;
	private final ColorParam background;

	private VisibilityModifier(ColorParam foreground, ColorParam background) {
		this.foreground = foreground;
		this.background = background;
	}

	public UDrawable getUDrawable(final int size, final Color foregroundColor, final Color backgoundColor) {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				drawInternal(ug, size, foregroundColor, backgoundColor);
			}
		};
	}

	private void drawInternal(UGraphic ug, int size, final Color foregroundColor, final Color backgoundColor) {
		ug.getParam().setBackcolor(backgoundColor);
		ug.getParam().setColor(foregroundColor);
		size = ensureEven(size);
		switch (this) {
		case PACKAGE_PRIVATE_FIELD:
			drawTriangle(ug, false, size);
			break;

		case PRIVATE_FIELD:
			drawSquare(ug, false, size);
			break;

		case PROTECTED_FIELD:
			drawDiamond(ug, false, size);
			break;

		case PUBLIC_FIELD:
			drawCircle(ug, false, size);
			break;

		case PACKAGE_PRIVATE_METHOD:
			drawTriangle(ug, true, size);
			break;

		case PRIVATE_METHOD:
			drawSquare(ug, true, size);
			break;

		case PROTECTED_METHOD:
			drawDiamond(ug, true, size);
			break;

		case PUBLIC_METHOD:
			drawCircle(ug, true, size);
			break;

		default:
			throw new IllegalStateException();
		}
	}

	private void drawSquare(UGraphic ug, boolean filled, int size) {
		ug.draw(2, 2, new URectangle(size - 4, size - 4));
	}

	private void drawCircle(UGraphic ug, boolean filled, int size) {
		ug.draw(2, 2, new UEllipse(size - 4, size - 4));
	}

	static private int ensureEven(int n) {
		if (n % 2 == 1) {
			n--;
		}
		return n;
	}

	private void drawDiamond(UGraphic ug, boolean filled, int size) {
		final UPolygon poly = new UPolygon();
		size -= 2;
		poly.addPoint(size / 2.0, 0);
		poly.addPoint(size, size / 2.0);
		poly.addPoint(size / 2.0, size);
		poly.addPoint(0, size / 2.0);
		ug.draw(1, 0, poly);
	}

	private void drawTriangle(UGraphic ug, boolean filled, int size) {
		final UPolygon poly = new UPolygon();
		size -= 2;
		poly.addPoint(size / 2.0, 1);
		poly.addPoint(0, size - 1);
		poly.addPoint(size, size - 1);
		ug.draw(1, 0, poly);
	}

	public static boolean isVisibilityCharacter(char c) {
		if (c == '-') {
			return true;
		}
		if (c == '#') {
			return true;
		}
		if (c == '+') {
			return true;
		}
		if (c == '~') {
			return true;
		}
		return false;
	}

	public static VisibilityModifier getVisibilityModifier(char c, boolean isField) {
		if (isField) {
			return getVisibilityModifierForField(c);
		}
		return getVisibilityModifierForMethod(c);
	}

	private static VisibilityModifier getVisibilityModifierForField(char c) {
		if (c == '-') {
			return VisibilityModifier.PRIVATE_FIELD;
		}
		if (c == '#') {
			return VisibilityModifier.PROTECTED_FIELD;
		}
		if (c == '+') {
			return VisibilityModifier.PUBLIC_FIELD;
		}
		if (c == '~') {
			return VisibilityModifier.PACKAGE_PRIVATE_FIELD;
		}
		return null;
	}

	private static VisibilityModifier getVisibilityModifierForMethod(char c) {
		if (c == '-') {
			return VisibilityModifier.PRIVATE_METHOD;
		}
		if (c == '#') {
			return VisibilityModifier.PROTECTED_METHOD;
		}
		if (c == '+') {
			return VisibilityModifier.PUBLIC_METHOD;
		}
		if (c == '~') {
			return VisibilityModifier.PACKAGE_PRIVATE_METHOD;
		}
		return null;
	}

	public final ColorParam getForeground() {
		return foreground;
	}

	public final ColorParam getBackground() {
		return background;
	}

}
