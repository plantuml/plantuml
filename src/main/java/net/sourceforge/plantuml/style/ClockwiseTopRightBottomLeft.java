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
package net.sourceforge.plantuml.style;

import java.util.regex.Pattern;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class ClockwiseTopRightBottomLeft {

	private static final ClockwiseTopRightBottomLeft NONE = new ClockwiseTopRightBottomLeft(0, 0, 0, 0);

	private final double top;
	private final double right;
	private final double bottom;
	private final double left;

	public static ClockwiseTopRightBottomLeft same(double value) {
		return new ClockwiseTopRightBottomLeft(value, value, value, value);
	}

	public static ClockwiseTopRightBottomLeft none() {
		return NONE;
	}

	private static final Pattern NUMBERS_ONLY = Pattern.compile("[0-9 ]+");

	private static boolean isOnlyNumbersAndSpaces(String value) {
		return NUMBERS_ONLY.matcher(value).matches();
	}

	public static ClockwiseTopRightBottomLeft read(String value) {
		if (isOnlyNumbersAndSpaces(value) == false)
			return none();

		final String[] split = value.trim().split(" +");

		try {
			switch (split.length) {
			case 1: {
				final double v = Integer.parseInt(split[0]);
				return new ClockwiseTopRightBottomLeft(v, v, v, v);
			}
			case 2: {
				final double a = Integer.parseInt(split[0]);
				final double b = Integer.parseInt(split[1]);
				return new ClockwiseTopRightBottomLeft(a, b, a, b);
			}
			case 3: {
				final double a = Integer.parseInt(split[0]);
				final double b = Integer.parseInt(split[1]);
				final double c = Integer.parseInt(split[2]);
				return new ClockwiseTopRightBottomLeft(a, b, c, b);
			}
			case 4: {
				final double a = Integer.parseInt(split[0]);
				final double b = Integer.parseInt(split[1]);
				final double c = Integer.parseInt(split[2]);
				final double d = Integer.parseInt(split[3]);
				return new ClockwiseTopRightBottomLeft(a, b, c, d);
			}
			default:
				return none();
			}
		} catch (NumberFormatException e) {
			return none();
		}
	}

	public static ClockwiseTopRightBottomLeft margin1margin2(double margin1, double margin2) {
		return topRightBottomLeft(margin1, margin2, margin1, margin2);
	}

	public static ClockwiseTopRightBottomLeft topRightBottomLeft(double top, double right, double bottom, double left) {
		return new ClockwiseTopRightBottomLeft(top, right, bottom, left);
	}

	private ClockwiseTopRightBottomLeft(double top, double right, double bottom, double left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	public ClockwiseTopRightBottomLeft incTop(double delta) {
		return new ClockwiseTopRightBottomLeft(top + delta, right, bottom, left);
	}

	@Override
	public String toString() {
		return "" + top + ":" + right + ":" + bottom + ":" + left;
	}

	public final double getTop() {
		return top;
	}

	public final double getRight() {
		return right;
	}

	public final double getBottom() {
		return bottom;
	}

	public final double getLeft() {
		return left;
	}

	public static ClockwiseTopRightBottomLeft marginForDocument(StyleBuilder styleBuilder) {
		final Style style = StyleSignatureBasic.of(SName.root, SName.document).getMergedStyle(styleBuilder);
		return style.getMargin();
	}

	public UTranslate getTranslate() {
		return new UTranslate(left, top);
	}

	public XDimension2D apply(XDimension2D dim) {
		return new XDimension2D(left + dim.getWidth() + right, top + dim.getHeight() + bottom);
	}

	public boolean isZero() {
		return left == 0 && right == 0 && top == 0 && bottom == 0;
	}

}