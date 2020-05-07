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
package net.sourceforge.plantuml.style;

public class ClockwiseTopRightBottomLeft {

	private final double top;
	private final double right;
	private final double bottom;
	private final double left;

	public static ClockwiseTopRightBottomLeft same(double value) {
		return new ClockwiseTopRightBottomLeft(value, value, value, value);
	}

	public static ClockwiseTopRightBottomLeft none() {
		return new ClockwiseTopRightBottomLeft(0, 0, 0, 0);
	}

	public static ClockwiseTopRightBottomLeft read(String value) {
		if (value.matches("[0-9 ]+")) {
			final String split[] = value.split(" +");
			if (split.length == 1) {
				final double first = Integer.parseInt(split[0]);
				return new ClockwiseTopRightBottomLeft(first, first, first, first);
			}
			if (split.length == 2) {
				final double first = Integer.parseInt(split[0]);
				final double second = Integer.parseInt(split[1]);
				return new ClockwiseTopRightBottomLeft(first, second, first, second);
			}
			if (split.length == 3) {
				final double first = Integer.parseInt(split[0]);
				final double second = Integer.parseInt(split[1]);
				final double third = Integer.parseInt(split[2]);
				return new ClockwiseTopRightBottomLeft(first, second, third, second);
			}
			if (split.length == 4) {
				final double first = Integer.parseInt(split[0]);
				final double second = Integer.parseInt(split[1]);
				final double third = Integer.parseInt(split[2]);
				final double forth = Integer.parseInt(split[3]);
				return new ClockwiseTopRightBottomLeft(first, second, third, forth);
			}
		}
		return none();
	}

	public static ClockwiseTopRightBottomLeft margin1margin2(double margin1, double margin2) {
		return new ClockwiseTopRightBottomLeft(margin1, margin2, margin2, margin1);
	}

	private ClockwiseTopRightBottomLeft(double top, double right, double bottom, double left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
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
		final Style style = StyleSignature.of(SName.root, SName.document).getMergedStyle(styleBuilder);
		return style.getMargin();
	}

}