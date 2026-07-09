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
package net.sourceforge.plantuml.asciiverse;

public class ADimension2D {

	final private int width;
	final private int height;

	public ADimension2D(int width, int height) {
		if (width < 0)
			throw new IllegalArgumentException();
		if (height < 0)
			throw new IllegalArgumentException();

		this.width = width;
		this.height = height;
	}

	@Override
	public String toString() {
		return "[" + width + "," + height + "]";
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public ADimension2D delta(int delta) {
		return this.delta(delta, delta);
	}

	public ADimension2D withWidth(int newWidth) {
		return new ADimension2D(newWidth, height);
	}

	public ADimension2D delta(int deltaWidth, int deltaHeight) {
		if (deltaHeight == 0 && deltaWidth == 0)
			return this;

		return new ADimension2D(getWidth() + deltaWidth, getHeight() + deltaHeight);
	}

	public ADimension2D mergeTB(ADimension2D bottom) {
		final int width = Math.max(this.getWidth(), bottom.getWidth());
		final int height = this.getHeight() + bottom.getHeight();
		return new ADimension2D(width, height);
	}

	public ADimension2D mergeLR(ADimension2D right) {
		final int height = Math.max(this.getHeight(), right.getHeight());
		final int width = this.getWidth() + right.getWidth();
		return new ADimension2D(width, height);
	}

	public ADimension2D atLeast(int minWidth, int minHeight) {
		int h = getHeight();
		int w = getWidth();
		if (w > minWidth && h > minHeight)
			return this;

		if (h < minHeight)
			h = minHeight;

		if (w < minWidth)
			w = minWidth;

		return new ADimension2D(w, h);
	}

	public static ADimension2D max(ADimension2D dim1, ADimension2D dim2) {
		return dim1.atLeast(dim2.getWidth(), dim2.getHeight());
	}

}
